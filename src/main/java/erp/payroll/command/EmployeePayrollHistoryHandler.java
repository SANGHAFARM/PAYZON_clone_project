package erp.payroll.command;

import java.time.YearMonth;
import java.time.format.DateTimeParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.payroll.dto.EmployeePayrollHistoryPage;
import erp.payroll.service.EmployeePayrollHistoryService;
import mvc.command.CommandHandler;

// 사원별 급여내역 조회 요청을 처리한다.
public class EmployeePayrollHistoryHandler implements CommandHandler {

	private static final String VIEW = "/WEB-INF/view/payroll/employees-payroll-history.jsp";
	private EmployeePayrollHistoryService historyService = new EmployeePayrollHistoryService();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) {
		if (!req.getMethod().equalsIgnoreCase("GET")) {
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}

		YearMonth currentMonth = YearMonth.now();
		String startMonth = validMonth(req.getParameter("startMonth"), currentMonth.withMonth(1).toString());
		String endMonth = validMonth(req.getParameter("endMonth"), currentMonth.toString());
		if (YearMonth.parse(startMonth).isAfter(YearMonth.parse(endMonth))) {
			startMonth = endMonth;
		}
		if (YearMonth.parse(startMonth).plusMonths(11).isBefore(YearMonth.parse(endMonth))) {
			startMonth = YearMonth.parse(endMonth).minusMonths(11).toString();
			req.setAttribute("periodMessage", "급여내역은 최대 12개월까지 조회할 수 있습니다.");
		}

		Integer employeeId = integerValue(req.getParameter("employeeId"));
		boolean loadHistories = "history".equals(req.getParameter("mode"));
		if (loadHistories && employeeId == null) {
			req.setAttribute("historyPopupMessage", "급여내역을 조회할 사원을 선택해주세요");
			loadHistories = false;
		}

		EmployeePayrollHistoryPage page = historyService.getPage(employeeId,
				startMonth, endMonth, intValue(req.getParameter("page"), 1), req.getParameter("employeeKeyword"),
				integerValue(req.getParameter("departmentId")), req.getParameter("status"), loadHistories);
		req.setAttribute("startMonth", startMonth);
		req.setAttribute("endMonth", endMonth);
		req.setAttribute("selectedEmployee", page.getSelectedEmployee());
		req.setAttribute("employees", page.getEmployees());
		req.setAttribute("departments", page.getDepartments());
		req.setAttribute("paymentHistories", page.getHistories());
		req.setAttribute("paymentHistoryTotal", page.getTotal());
		req.setAttribute("pageInfo", page.getPageInfo());
		return VIEW;
	}

	private String validMonth(String value, String defaultValue) {
		try {
			return YearMonth.parse(value).toString();
		} catch (DateTimeParseException | NullPointerException e) {
			return defaultValue;
		}
	}

	private Integer integerValue(String value) {
		try {
			return Integer.valueOf(value);
		} catch (Exception e) {
			return null;
		}
	}

	private int intValue(String value, int defaultValue) {
		Integer number = integerValue(value);
		return number == null ? defaultValue : number;
	}
}
