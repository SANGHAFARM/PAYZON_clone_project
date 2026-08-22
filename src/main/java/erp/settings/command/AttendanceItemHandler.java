package erp.settings.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.settings.model.AttendanceItem;
import erp.settings.service.AttendanceSettingService;
import mvc.command.CommandHandler;

// 근태항목 화면의 HTTP 요청을 구분하고 적절한 서비스 호출과 화면 이동을 담당한다.
// 勤怠項目画面のHTTPリクエストを判別し、適切なサービス呼び出しと画面遷移を担当する。
public class AttendanceItemHandler implements CommandHandler {

	// 설정 서비스 객체 할당
	// 担当業務を処理するServiceまたはDAOを共有フィールドへ割り当て、各処理から再利用する。
	private AttendanceSettingService settingService = AttendanceSettingService.getInstance();

	@Override
	// 요청 방식과 작업 구분을 확인하여 근태항목 조회·저장 작업을 적절한 처리로 연결한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエスト方式と処理区分を確認し、勤怠項目の照会・保存処理へ適切に振り分ける。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		if (req.getMethod().equalsIgnoreCase("POST")) {
			return processAction(req, res);
		}
		return null; // GET 요청 무시 처리
	}

	// [POST] 근태항목 추가/수정/삭제 액션 처리
	// 요청에서 작업구분 작업에 필요한 값을 읽고 검증한 뒤 해당 서비스 처리를 호출한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエストから処理区分処理に必要な値を取得・検証し、該当するサービス処理を呼び出す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	private String processAction(HttpServletRequest req, HttpServletResponse res) throws Exception {
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		String idStr = req.getParameter("attendItemId");

		if ("requestDelete".equals(action)) {
			if (idStr != null && !idStr.isEmpty()) {
				res.sendRedirect(req.getContextPath() + "/settings/attendance.do?deleteType=attendance&deleteId="
						+ idStr + "#attendance-item-settings");
			} else {
				res.sendRedirect(req.getContextPath() + "/settings/attendance.do#attendance-item-settings");
			}
			return null;
		}

		try {
			AttendanceItem item = new AttendanceItem();

			if (idStr != null && !idStr.isEmpty()) {
				item.setAttendanceItemId(Integer.parseInt(idStr));
			}

			item.setAttendName(req.getParameter("attendName"));
			item.setUnitType(req.getParameter("unitType"));
			item.setWorkHourType(req.getParameter("workHourType"));
			item.setUseYn(req.getParameter("useYn"));

			String groupIdStr = req.getParameter("attendanceGroupId");
			if (groupIdStr != null && !groupIdStr.isEmpty()) {
				item.setAttendanceGroupId(Integer.parseInt(groupIdStr));
			}

			// 휴가공제 식별 번호
			// データを一意に識別するキーを発行または適用し、対象レコードを正確に処理する。
			String deductLeaveIdStr = req.getParameter("deductLeaveId");
			if (deductLeaveIdStr != null && !deductLeaveIdStr.isEmpty()) {
				item.setDeductLeaveId(Integer.parseInt(deductLeaveIdStr));
			}

			// 내용 지우기 액션 외에는 서비스 로직 호출
			// 処理区分と現在状態を確認し、条件に合う業務処理だけを実行する。
			if (!"clear".equals(action)) {
				String serviceAction = "confirmDelete".equals(action) ? "delete" : action;
				settingService.processAttendItemAction(item, serviceAction);
				String message = "insert".equals(serviceAction) ? "근태항목이 추가되었습니다."
						: "update".equals(serviceAction) ? "근태항목이 수정되었습니다."
						: "근태항목이 삭제되었습니다.";
				req.getSession().setAttribute("message", message);
			}

		} catch (Exception e) {
			e.printStackTrace();

			Throwable cause = e.getCause();

			// 중복 에러(SQLIntegrityConstraintViolationException)인지 확인
			// 業務条件に合うSQLを準備し、入力値をバインドしてデータベースで実行する。
			if (cause instanceof java.sql.SQLIntegrityConstraintViolationException) {
				req.getSession().setAttribute("message", "이미 추가된 근태항목입니다.");
			} else {
				req.getSession().setAttribute("message", "오류 발생: " + e.getMessage());
			}
		}

		res.sendRedirect(req.getContextPath() + "/settings/attendance.do#attendance-item-settings");
		return null;
	}
}
