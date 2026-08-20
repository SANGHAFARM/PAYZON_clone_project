package erp.settings.command;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.attendance.model.EmployeeLeaveBalance;
import erp.attendance.service.EmployeeLeaveBalanceService;
import mvc.command.CommandHandler;

public class EmployeeLeaveHandler implements CommandHandler {

	// 휴가 현황 서비스 객체 할당
	private EmployeeLeaveBalanceService leaveBalanceService = EmployeeLeaveBalanceService.getInstance();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		if (req.getMethod().equalsIgnoreCase("POST")) {
			return processAction(req, res);
		}
		return null;
	}

	// [POST] 사원별 휴가일수 다중 레코드 저장 및 삭제 처리
	private String processAction(HttpServletRequest req, HttpServletResponse res) throws Exception {
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		String leaveItemIdStr = req.getParameter("leaveItemId");

		try {
			if ("save".equals(action)) {
				// 1. 화면에서 체크박스에 체크한 사원들의 ID만 추출
				String[] checkedEmployeeIds = req.getParameterValues("checkedEmpIds");

				if (checkedEmployeeIds != null) {
					List<EmployeeLeaveBalance> balances = new ArrayList<>();

					// 2. 체크된 사원들의 ID만 추출
					for (String empIdStr : checkedEmployeeIds) {
						EmployeeLeaveBalance balance = new EmployeeLeaveBalance();
						balance.setLeaveItemId(Integer.parseInt(leaveItemIdStr));
						balance.setEmployeeId(Integer.parseInt(empIdStr));

						// 3. 해당 사원 번호가 꼬리표로 붙은 전용 '휴가일수'와 '휴가내역ID' 추출
						String leaveDaysStr = req.getParameter("leaveDays_" + empIdStr);
						String empLeaveIdStr = req.getParameter("empLeaveId_" + empIdStr);

						if (leaveDaysStr != null && !leaveDaysStr.trim().isEmpty()) {
							balance.setTotalDays(Double.parseDouble(leaveDaysStr));
						}

						// 기존 내역이 있으면 Update를 위해 세팅
						if (empLeaveIdStr != null && !empLeaveIdStr.trim().isEmpty()) {
							balance.setEmployeeLeaveBalanceId(Integer.parseInt(empLeaveIdStr));
						}

						balances.add(balance);
					}

					// 4. 리스트에 담긴 '체크된 사원'들의 데이터만 일괄 저장
					leaveBalanceService.saveLeaveBalances(balances);
					req.getSession().setAttribute("message", "선택한 사원의 휴가일수가 저장되었습니다.");
					req.getSession().setAttribute("messageReturnTarget", "employeeLeave");
				}
			} else if ("requestDelete".equals(action)) {
				// 1. 화면에서 체크박스에 체크한 사원들의 ID(employeeId) 배열을 추출
				String[] checkedEmployeeIds = req.getParameterValues("checkedEmpIds");

				if (checkedEmployeeIds != null) {
					List<Integer> deleteIds = new ArrayList<>();

					// 2. 체크된 사원 번호들을 하나씩 돌면서 확인
					for (String empIdStr : checkedEmployeeIds) {
						// 3. 해당 사원의 꼬리표가 붙은 '휴가내역 ID(empLeaveId)'를 숨겨진 태그에서 추출
						String empLeaveIdStr = req.getParameter("empLeaveId_" + empIdStr);

						// 4. 기존에 저장된 휴가 내역이 있는(DB에 존재하는) 경우에만 삭제 목록에 추가
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
				String keyword = "search".equals(action) ? req.getParameter("keyword") : "";
				String status = "search".equals(action) ? req.getParameter("status") : "";

				// Null 방어
				if (keyword == null)
					keyword = "";
				if (status == null)
					status = "";

				// 2. URL에 UTF-8로 인코딩
				String encodedKeyword = java.net.URLEncoder.encode(keyword, "UTF-8");
				String encodedStatus = java.net.URLEncoder.encode(status, "UTF-8");

				// 3. 인코딩된 안전한 문자로 리다이렉트
				res.sendRedirect(req.getContextPath() + "/settings/attendance.do?leaveItemId=" + leaveItemIdStr
						+ "&keyword=" + encodedKeyword + "&status=" + encodedStatus + "#employee-leave-modal");
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			req.getSession().setAttribute("message", "오류 발생: " + e.getMessage());
			req.getSession().setAttribute("messageReturnTarget", "employeeLeave");
		}

		res.sendRedirect(req.getContextPath() + "/settings/attendance.do?leaveItemId=" + leaveItemIdStr
				+ "#employee-leave-modal");
		return null;
	}
}
