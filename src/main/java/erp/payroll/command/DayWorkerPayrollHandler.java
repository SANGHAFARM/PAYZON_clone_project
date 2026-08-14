package erp.payroll.command;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.payroll.dto.DayWorkerPaymentPage;
import erp.payroll.model.PayrollRun;
import erp.payroll.service.DayWorkerPayrollService;
import erp.payroll.service.PayrollManagementService;
import mvc.command.CommandHandler;

// 일용직 급여입력 화면의 조회와 저장 요청을 처리하는 Handler
public class DayWorkerPayrollHandler implements CommandHandler {

	private static final String VIEW = "/WEB-INF/view/payroll/day-worker-payroll-management.jsp";
	private DayWorkerPayrollService dayWorkerService = new DayWorkerPayrollService();
	private PayrollManagementService managementService = new PayrollManagementService();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String uri = req.getRequestURI();
		if (uri.endsWith("/day-worker-management.do") && req.getMethod().equalsIgnoreCase("GET")) {
			return processForm(req, res);
		} else if (uri.endsWith("/employees/add.do") && req.getMethod().equalsIgnoreCase("POST")) {
			return processAddEmployees(req, res);
		} else if (uri.endsWith("/employees/delete.do") && req.getMethod().equalsIgnoreCase("POST")) {
			return processDeleteEmployees(req, res);
		} else if (uri.endsWith("/day-worker/save.do") && req.getMethod().equalsIgnoreCase("POST")) {
			return processSave(req, res);
		}
		res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		return null;
	}

	private String processForm(HttpServletRequest req, HttpServletResponse res) {
		PayrollRun run = makeRun(req);
		int employeePage = intValue(req.getParameter("employeePage"), 1);
		DayWorkerPaymentPage page = dayWorkerService.getPage(run, integerValue(req.getParameter("employeeId")),
				req.getParameter("employeeKeyword"), integerValue(req.getParameter("departmentId")), employeePage);
		setPageAttributes(req, page, run, employeePage);
		return VIEW;
	}

	private String processAddEmployees(HttpServletRequest req, HttpServletResponse res) throws IOException {
		PayrollRun run = makeRun(req);
		managementService.addEmployees(run, intValues(req.getParameterValues("employeeIds")));
		redirect(req, res, run, null);
		return null;
	}

	private String processDeleteEmployees(HttpServletRequest req, HttpServletResponse res) throws IOException {
		PayrollRun run = makeRun(req);
		boolean deleteAll = "ALL".equals(req.getParameter("deleteType"));
		managementService.deleteEmployees(run, intValues(req.getParameterValues("employeeIds")), deleteAll);
		redirect(req, res, run, null);
		return null;
	}

	private String processSave(HttpServletRequest req, HttpServletResponse res) throws IOException {
		PayrollRun run = makeRun(req);
		int employeeId = intValue(req.getParameter("employeeId"), 0);
		if (employeeId == 0) {
			throw new IllegalArgumentException("급여를 저장할 일용직 사원을 선택하세요.");
		}
		long[] amounts = readAmounts(req);
		String calculationType = req.getParameter("calculationType");
		if (calculationType != null) {
			long[] calculated = dayWorkerService.calculate(run, employeeId, amounts[6]);
			if ("INSURANCE".equals(calculationType)) {
				for (int i = 0; i < 4; i++) {
					amounts[i] = calculated[i];
				}
			} else if ("PERIOD_TAX".equals(calculationType)) {
				amounts[4] = calculated[4];
				amounts[5] = calculated[5];
			}
		}
		dayWorkerService.save(run, employeeId, amounts);
		redirect(req, res, run, employeeId);
		return null;
	}

	private void setPageAttributes(HttpServletRequest req, DayWorkerPaymentPage page, PayrollRun requestRun,
			int employeePage) {
		PayrollRun displayRun = page.getRun() == null ? requestRun : page.getRun();
		req.setAttribute("paymentYears", makePaymentYears());
		req.setAttribute("selectedYear", Integer.parseInt(displayRun.getPayYear()));
		req.setAttribute("selectedMonth", Integer.parseInt(displayRun.getPayMonth()));
		req.setAttribute("selectedRound", Integer.parseInt(displayRun.getPaySeq()));
		req.setAttribute("calculationStart", displayRun.getCalcStartDate());
		req.setAttribute("calculationEnd", displayRun.getCalcEndDate());
		req.setAttribute("paymentDate", displayRun.getPayDate());
		req.setAttribute("paymentEmployees", page.getPaymentEmployees());
		req.setAttribute("selectedEmployee", page.getSelectedEmployee());
		req.setAttribute("selectedEmployeeId",
				page.getSelectedEmployee() == null ? null : page.getSelectedEmployee().getEmployeeId());
		req.setAttribute("availableEmployeePage", page.getAvailableEmployeePage());
		req.setAttribute("departments", page.getDepartments());
		req.setAttribute("employeePage", employeePage);
	}

	private PayrollRun makeRun(HttpServletRequest req) {
		String year = value(req.getParameter("paymentYear"), String.valueOf(LocalDate.now().getYear()));
		String month = value(req.getParameter("paymentMonth"), String.valueOf(LocalDate.now().getMonthValue()));
		String sequence = value(req.getParameter("paymentRound"), "1");
		return managementService.makeRun(year, month, sequence, "daily", req.getParameter("calculationStart"),
				req.getParameter("calculationEnd"), req.getParameter("paymentDate"));
	}

	private long[] readAmounts(HttpServletRequest req) {
		return new long[] { longValue(req.getParameter("nationalPension")),
				longValue(req.getParameter("healthInsurance")),
				longValue(req.getParameter("longTermCareInsurance")),
				longValue(req.getParameter("employmentInsurance")), longValue(req.getParameter("incomeTax")),
				longValue(req.getParameter("localIncomeTax")), longValue(req.getParameter("mutualAidFee")) };
	}

	private List<Integer> makePaymentYears() {
		List<Integer> years = new ArrayList<>();
		int currentYear = LocalDate.now().getYear();
		for (int year = currentYear + 1; year >= currentYear - 5; year--) {
			years.add(year);
		}
		return years;
	}

	private void redirect(HttpServletRequest req, HttpServletResponse res, PayrollRun run, Integer employeeId)
			throws IOException {
		String location = req.getContextPath() + "/payroll/day-worker-management.do?paymentYear="
				+ run.getPayYear() + "&paymentMonth=" + Integer.parseInt(run.getPayMonth()) + "&paymentRound="
				+ Integer.parseInt(run.getPaySeq());
		if (employeeId != null) {
			location += "&employeeId=" + employeeId;
		}
		res.sendRedirect(location);
	}

	private int[] intValues(String[] values) {
		if (values == null) {
			return new int[0];
		}
		int[] result = new int[values.length];
		for (int i = 0; i < values.length; i++) {
			result[i] = intValue(values[i], 0);
		}
		return result;
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

	private long longValue(String value) {
		try {
			return Long.parseLong(value == null ? "0" : value.replace(",", "").trim());
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	private String value(String value, String defaultValue) {
		return value == null || value.trim().isEmpty() ? defaultValue : value;
	}
}
