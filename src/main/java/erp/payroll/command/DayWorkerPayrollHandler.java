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

// 일용직 급여입력 화면의 조회와 저장 요청을 처리한다.
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
		Object flashMessage = req.getSession().getAttribute("dayWorkerPayrollMessage");
		if (flashMessage != null) {
			req.setAttribute("payrollPopupMessage", flashMessage);
			req.getSession().removeAttribute("dayWorkerPayrollMessage");
		}
		return VIEW;
	}

	private String processAddEmployees(HttpServletRequest req, HttpServletResponse res) throws IOException {
		PayrollRun run = makeSafeRun(req);
		int[] employeeIds = intValues(req.getParameterValues("employeeIds"));
		if (employeeIds.length == 0) {
			return redirectWithMessage(req, res, run, "추가할 사원을 선택해주세요");
		}
		try {
			managementService.addEmployees(run, employeeIds);
		} catch (RuntimeException e) {
			return redirectWithMessage(req, res, run, makeFailureMessage(e));
		}
		redirect(req, res, run, null);
		return null;
	}

	private String processDeleteEmployees(HttpServletRequest req, HttpServletResponse res) throws IOException {
		PayrollRun run = makeSafeRun(req);
		boolean deleteAll = "ALL".equals(req.getParameter("deleteType"));
		int[] employeeIds = intValues(req.getParameterValues("employeeIds"));
		if (!deleteAll && employeeIds.length == 0) {
			redirect(req, res, run, null);
			return null;
		}
		try {
			managementService.deleteEmployees(run, employeeIds, deleteAll);
		} catch (RuntimeException e) {
			return redirectWithMessage(req, res, run, makeFailureMessage(e));
		}
		redirect(req, res, run, null);
		return null;
	}

	private String processSave(HttpServletRequest req, HttpServletResponse res) throws IOException {
		try {
			return savePayroll(req, res);
		} catch (RuntimeException e) {
			// POST에서 JSP로 바로 포워드하지 않고 GET으로 돌아가 안내 팝업을 표시한다.
			return redirectWithMessage(req, res, makeSafeRun(req), makeFailureMessage(e));
		}
	}

	private String savePayroll(HttpServletRequest req, HttpServletResponse res) throws IOException {
		PayrollRun run = makeRun(req);
		int employeeId = intValue(req.getParameter("employeeId"), 0);
		String calculationType = req.getParameter("calculationType");
		if (employeeId == 0) {
			return redirectWithMessage(req, res, run, makeSelectionMessage(calculationType));
		}
		if (!dayWorkerService.hasWorkPayments(run, employeeId)) {
			return redirectWithMessage(req, res, run, "근무내역이 있는 사원을 선택해주세요", employeeId);
		}
		long[] amounts = readAmounts(req);
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

	private String makeSelectionMessage(String calculationType) {
		if ("INSURANCE".equals(calculationType)) {
			return "4대보험을 계산할 사원을 선택해주세요";
		}
		if ("PERIOD_TAX".equals(calculationType)) {
			return "기간단위 소득세를 계산할 사원을 선택해주세요";
		}
		return "급여내역을 저장할 사원을 선택해주세요";
	}

	private String makeFailureMessage(RuntimeException e) {
		String message = e.getMessage();
		if (message != null && message.contains("unique constraint")) {
			return "화면을 다시 조회한 후 저장해주세요";
		}
		return "입력값과 급여 대상 내역을 확인해주세요";
	}

	private String redirectWithMessage(HttpServletRequest req, HttpServletResponse res, PayrollRun run,
			String message) throws IOException {
		return redirectWithMessage(req, res, run, message, null);
	}

	private String redirectWithMessage(HttpServletRequest req, HttpServletResponse res, PayrollRun run,
			String message, Integer employeeId) throws IOException {
		req.getSession().setAttribute("dayWorkerPayrollMessage", message);
		redirect(req, res, run, employeeId);
		return null;
	}

	private PayrollRun makeSafeRun(HttpServletRequest req) {
		try {
			return makeRun(req);
		} catch (RuntimeException e) {
			return managementService.makeRun(String.valueOf(LocalDate.now().getYear()),
					String.valueOf(LocalDate.now().getMonthValue()), "1", "daily", null, null, null);
		}
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
			return Math.max(0, Long.parseLong(value == null ? "0" : value.replace(",", "").trim()));
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	private String value(String value, String defaultValue) {
		return value == null || value.trim().isEmpty() ? defaultValue : value;
	}
}
