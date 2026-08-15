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
				// [저장 로직] 화면에 표시된 모든 행의 휴가일수 배열 추출
				String[] allEmpLeaveIds = req.getParameterValues("allEmpLeaveIds");
				String[] allEmployeeIds = req.getParameterValues("allEmployeeIds");
				String[] leaveDays = req.getParameterValues("leaveDays");

				if (leaveDays != null && allEmployeeIds != null) {
					List<EmployeeLeaveBalance> balances = new ArrayList<>();

					for (int i = 0; i < leaveDays.length; i++) {
						EmployeeLeaveBalance balance = new EmployeeLeaveBalance();
						balance.setLeaveItemId(Integer.parseInt(leaveItemIdStr));
						balance.setEmployeeId(Integer.parseInt(allEmployeeIds[i]));
						balance.setTotalDays(Double.parseDouble(leaveDays[i]));

						// 기존 부여 내역이 있으면 기본키 세팅 (Update 유도), 없으면 0 유지 (Insert 유도)
						if (allEmpLeaveIds != null && allEmpLeaveIds[i] != null && !allEmpLeaveIds[i].isEmpty()) {
							balance.setEmployeeLeaveBalanceId(Integer.parseInt(allEmpLeaveIds[i]));
						}

						balances.add(balance);
					}
					// 서비스로 일괄 트랜잭션 요청
					leaveBalanceService.saveLeaveBalances(balances);
					req.getSession().setAttribute("message", "사원별 휴가일수가 저장되었습니다.");
				}

			} else if ("delete".equals(action)) {
				// [삭제 로직] 체크박스로 선택된 식별 번호 배열 파싱
				String[] deleteIdsStr = req.getParameterValues("employeeLeaveIds");
				if (deleteIdsStr != null) {
					List<Integer> deleteIds = new ArrayList<>();
					for (String id : deleteIdsStr) {
						deleteIds.add(Integer.parseInt(id));
					}
					// 서비스로 일괄 삭제 트랜잭션 요청
					leaveBalanceService.deleteLeaveBalances(deleteIds);
					req.getSession().setAttribute("message", "선택된 사원의 휴가 내역이 초기화되었습니다.");
				}

			} else if ("search".equals(action) || "showAll".equals(action)) {
				// 검색 또는 전체보기의 경우 별도 트랜잭션 없이 GET 파라미터를 달고 리다이렉트 처리
				String keyword = "search".equals(action) ? req.getParameter("keyword") : "";
				String status = "search".equals(action) ? req.getParameter("status") : "";

				res.sendRedirect(req.getContextPath() + "/settings/attendance.do?leaveItemId=" + leaveItemIdStr
						+ "&keyword=" + keyword + "&status=" + status + "#employee-leave-modal");
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			req.getSession().setAttribute("message", "오류 발생: " + e.getMessage());
		}

		res.sendRedirect(req.getContextPath() + "/settings/attendance.do?leaveItemId=" + leaveItemIdStr
				+ "#employee-leave-modal");
		return null;
	}
}