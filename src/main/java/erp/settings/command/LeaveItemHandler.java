package erp.settings.command;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.attendance.dto.EmployeeLeaveRow;
import erp.attendance.service.EmployeeLeaveBalanceService;
import erp.settings.model.AttendanceGroup;
import erp.settings.model.AttendanceItem;
import erp.settings.model.LeaveItem;
import erp.settings.service.AttendanceSettingService;
import mvc.command.CommandHandler;

// 휴가항목 화면의 HTTP 요청을 구분하고 적절한 서비스 호출과 화면 이동을 담당한다.
// 休暇項目画面のHTTPリクエストを判別し、適切なサービス呼び出しと画面遷移を担当する。
public class LeaveItemHandler implements CommandHandler {

	// 서비스 객체 할당 처리
	// 担当業務を処理するServiceまたはDAOを共有フィールドへ割り当て、各処理から再利用する。
	private AttendanceSettingService settingService = AttendanceSettingService.getInstance();
	private EmployeeLeaveBalanceService leaveBalanceService = EmployeeLeaveBalanceService.getInstance();

	@Override
	// 요청 방식과 작업 구분을 확인하여 휴가항목 조회·저장 작업을 적절한 처리로 연결한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエスト方式と処理区分を確認し、休暇項目の照会・保存処理へ適切に振り分ける。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		if (req.getMethod().equalsIgnoreCase("POST")) {
			return processAction(req, res);
		}
		return null;
	}

	// [POST] 휴가항목 추가/수정/삭제 액션 분기 처리
	// 요청에서 작업구분 작업에 필요한 값을 읽고 검증한 뒤 해당 서비스 처리를 호출한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエストから処理区分処理に必要な値を取得・検証し、該当するサービス処理を呼び出す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	private String processAction(HttpServletRequest req, HttpServletResponse res) throws Exception {
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		String idStr = req.getParameter("leaveItemId");

		if ("requestDelete".equals(action)) {
			if (idStr != null && !idStr.isEmpty()) {
				res.sendRedirect(req.getContextPath() + "/settings/attendance.do?deleteType=leave&deleteId="
						+ idStr + "#leave-settings");
			} else {
				res.sendRedirect(req.getContextPath() + "/settings/attendance.do#leave-settings");
			}
			return null;
		}

		try {
			// 파라미터 파싱 및 DTO 세팅
			// リクエストから入力値を取得し、空白除去と形式変換を行って業務処理へ渡す。
			LeaveItem item = new LeaveItem();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			if (idStr != null && !idStr.isEmpty()) {
				item.setLeaveItemId(Integer.parseInt(idStr));
			}

			item.setItemName(req.getParameter("itemName"));
			item.setUseYn(req.getParameter("useYn"));

			String startDate = req.getParameter("applyStartDate");
			if (startDate != null && !startDate.isEmpty()) {
				item.setApplyStartDate(sdf.parse(startDate));
			}

			String endDate = req.getParameter("applyEndDate");
			if (endDate != null && !endDate.isEmpty()) {
				item.setApplyEndDate(sdf.parse(endDate));
			}

			if (item.getApplyStartDate() != null && item.getApplyEndDate() != null
					&& item.getApplyEndDate().before(item.getApplyStartDate())) {
				throw new IllegalArgumentException("휴가 적용 종료일은 시작일보다 빠를 수 없습니다.");
			}

			// 내용 지우기 액션 외에는 서비스 로직 호출
			// 処理区分と現在状態を確認し、条件に合う業務処理だけを実行する。
			if (!"clear".equals(action)) {
				String serviceAction = "confirmDelete".equals(action) ? "delete" : action;
				settingService.processLeaveItemAction(item, serviceAction);
				String message = "insert".equals(serviceAction) ? "휴가항목이 추가되었습니다."
						: "update".equals(serviceAction) ? "휴가항목이 수정되었습니다."
						: "휴가항목이 삭제되었습니다.";
				req.getSession().setAttribute("message", message);
			}

		} catch (Exception e) {
			e.printStackTrace();

			Throwable cause = e.getCause();

			// 중복 에러(SQLIntegrityConstraintViolationException)인지 확인
			// 業務条件に合うSQLを準備し、入力値をバインドしてデータベースで実行する。
			if (e instanceof IllegalArgumentException) {
				req.getSession().setAttribute("message", e.getMessage());
			} else if ("confirmDelete".equals(action)) {
				req.getSession().setAttribute("message", "休暇項目を削除できませんでした。");
			} else if (cause instanceof java.sql.SQLIntegrityConstraintViolationException) {
				req.getSession().setAttribute("message", "이미 등록된 휴가항목입니다. 다른 이름이나 날짜를 선택해 주세요.");
			} else {
				req.getSession().setAttribute("message", "오류 발생: " + e.getMessage());
			}
		}

		// PRG 패턴 적용 리다이렉트 처리
		// 処理結果に応じて表示対象のJSPまたは次のリクエスト経路へ遷移する。
		res.sendRedirect(req.getContextPath() + "/settings/attendance.do#leave-settings");
		return null;
	}
}
