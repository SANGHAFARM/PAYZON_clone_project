package erp.settings.command;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.attendance.model.EmployeeLeaveBalance;
import erp.attendance.service.EmployeeLeaveBalanceService;
import mvc.command.CommandHandler;

// 사원휴가 화면의 HTTP 요청을 구분하고 적절한 서비스 호출과 화면 이동을 담당한다.
// 社員休暇画面のHTTPリクエストを判別し、適切なサービス呼び出しと画面遷移を担当する。
public class EmployeeLeaveHandler implements CommandHandler {

	// 휴가 현황 서비스 객체 할당
	// 担当業務を処理するServiceまたはDAOを共有フィールドへ割り当て、各処理から再利用する。
	private EmployeeLeaveBalanceService leaveBalanceService = EmployeeLeaveBalanceService.getInstance();

	@Override
	// 요청 방식과 작업 구분을 확인하여 사원휴가 조회·저장 작업을 적절한 처리로 연결한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエスト方式と処理区分を確認し、社員休暇の照会・保存処理へ適切に振り分ける。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		if (req.getMethod().equalsIgnoreCase("POST")) {
			return processAction(req, res);
		}
		return null;
	}

	// [POST] 사원별 휴가일수 다중 레코드 저장 및 삭제 처리
	// 요청에서 작업구분 작업에 필요한 값을 읽고 검증한 뒤 해당 서비스 처리를 호출한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエストから処理区分処理に必要な値を取得・検証し、該当するサービス処理を呼び出す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	private String processAction(HttpServletRequest req, HttpServletResponse res) throws Exception {
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		String leaveItemIdStr = req.getParameter("leaveItemId");

		try {
			if ("save".equals(action)) {
				// 1. 화면에서 체크박스에 체크한 사원들의 ID만 추출
				// 画面表示に必要なデータとメッセージをrequestまたはsessionへ保存し、JSPへ引き渡す。
				String[] checkedEmployeeIds = req.getParameterValues("checkedEmpIds");

				if (checkedEmployeeIds != null) {
					List<EmployeeLeaveBalance> balances = new ArrayList<>();

					// 2. 체크된 사원들의 ID만 추출
					// 社員の識別情報と在職・雇用・所属条件を確認し、対象社員データへ反映する。
					for (String empIdStr : checkedEmployeeIds) {
						EmployeeLeaveBalance balance = new EmployeeLeaveBalance();
						balance.setLeaveItemId(Integer.parseInt(leaveItemIdStr));
						balance.setEmployeeId(Integer.parseInt(empIdStr));

						// 3. 해당 사원 번호가 꼬리표로 붙은 전용 '휴가일수'와 '휴가내역ID' 추출
						// 社員の識別情報と在職・雇用・所属条件を確認し、対象社員データへ反映する。
						String leaveDaysStr = req.getParameter("leaveDays_" + empIdStr);
						String empLeaveIdStr = req.getParameter("empLeaveId_" + empIdStr);

						if (leaveDaysStr != null && !leaveDaysStr.trim().isEmpty()) {
							balance.setTotalDays(Double.parseDouble(leaveDaysStr));
						}

						// 기존 내역이 있으면 Update를 위해 세팅
						// 識別された既存データへ変更値を反映し、未変更項目は従来の値を維持する。
						if (empLeaveIdStr != null && !empLeaveIdStr.trim().isEmpty()) {
							balance.setEmployeeLeaveBalanceId(Integer.parseInt(empLeaveIdStr));
						}

						balances.add(balance);
					}

					// 4. 리스트에 담긴 '체크된 사원'들의 데이터만 일괄 저장
					// 社員の識別情報と在職・雇用・所属条件を確認し、対象社員データへ反映する。
					leaveBalanceService.saveLeaveBalances(balances);
					// 저장 후 같은 휴가항목의 관리 팝업을 다시 조회해 변경값을 바로 보여준다.
					// 識別された既存データへ変更値を反映し、未変更項目は従来の値を維持する。
				}
			} else if ("requestDelete".equals(action)) {
				// 1. 화면에서 체크박스에 체크한 사원들의 ID(employeeId) 배열을 추출
				// 画面表示に必要なデータとメッセージをrequestまたはsessionへ保存し、JSPへ引き渡す。
				String[] checkedEmployeeIds = req.getParameterValues("checkedEmpIds");

				if (checkedEmployeeIds != null) {
					List<Integer> deleteIds = new ArrayList<>();

					// 2. 체크된 사원 번호들을 하나씩 돌면서 확인
					// 入力条件と必須値を検証し、不正なデータが後続処理へ渡らないようにする。
					for (String empIdStr : checkedEmployeeIds) {
						// 3. 해당 사원의 꼬리표가 붙은 '휴가내역 ID(empLeaveId)'를 숨겨진 태그에서 추출
						// 社員の識別情報と在職・雇用・所属条件を確認し、対象社員データへ反映する。
						String empLeaveIdStr = req.getParameter("empLeaveId_" + empIdStr);

						// 4. 기존에 저장된 휴가 내역이 있는(DB에 존재하는) 경우에만 삭제 목록에 추가
						// 入力された内容を新しい業務データとして構成し、関連データとともに登録する。
						if (empLeaveIdStr != null && !empLeaveIdStr.trim().isEmpty()) {
							deleteIds.add(Integer.parseInt(empLeaveIdStr));
						}
					}

					if (!deleteIds.isEmpty()) {
						req.getSession().setAttribute("employeeLeaveDeleteIds", deleteIds);
						req.getSession().setAttribute("employeeLeaveDeleteCount", deleteIds.size());
					}
				}
			} else if ("confirmDelete".equals(action)) {
				@SuppressWarnings("unchecked")
				List<Integer> deleteIds = (List<Integer>) req.getSession().getAttribute("employeeLeaveDeleteIds");
				if (deleteIds != null && !deleteIds.isEmpty()) {
					leaveBalanceService.deleteLeaveBalances(deleteIds);
					req.getSession().setAttribute("message", "선택한 사원의 휴가일수가 삭제되었습니다.");
					req.getSession().setAttribute("messageReturnTarget", "employeeLeave");
				}
				req.getSession().removeAttribute("employeeLeaveDeleteIds");
				req.getSession().removeAttribute("employeeLeaveDeleteCount");
			} else if ("search".equals(action) || "showAll".equals(action)) {
				// 1. 파라미터를 추출 (전체보기일 경우 빈 칸으로 초기화)
				// リクエストから入力値を取得し、空白除去と形式変換を行って業務処理へ渡す。
				String keyword = "search".equals(action) ? req.getParameter("keyword") : "";
				String status = "search".equals(action) ? req.getParameter("status") : "";

				// Null 방어
				// 未入力値とNULLを区別して正規化し、制約違反や不要な値の保存を防止する。
				if (keyword == null)
					keyword = "";
				if (status == null)
					status = "";

				// 2. URL에 UTF-8로 인코딩
				// メッセージまたは検索条件をUTF-8でURLエンコードし、安全にリダイレクト先へ渡す。
				String encodedKeyword = java.net.URLEncoder.encode(keyword, "UTF-8");
				String encodedStatus = java.net.URLEncoder.encode(status, "UTF-8");

				// 3. 인코딩된 안전한 문자로 리다이렉트
				// 処理結果に応じて表示対象のJSPまたは次のリクエスト経路へ遷移する。
				res.sendRedirect(req.getContextPath() + "/settings/attendance.do?leaveItemId=" + leaveItemIdStr
						+ "&keyword=" + encodedKeyword + "&status=" + encodedStatus + "#employee-leave-modal");
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			req.getSession().setAttribute("message", "오류 발생: " + e.getMessage());
			req.getSession().setAttribute("messageReturnTarget", "employeeLeave");
		}

		String keyword = req.getParameter("keyword");
		String status = req.getParameter("status");
		String query = "&keyword=" + java.net.URLEncoder.encode(keyword == null ? "" : keyword, "UTF-8")
				+ "&status=" + java.net.URLEncoder.encode(status == null ? "" : status, "UTF-8");
		res.sendRedirect(req.getContextPath() + "/settings/attendance.do?leaveItemId=" + leaveItemIdStr
				+ query + "#employee-leave-modal");
		return null;
	}
}
