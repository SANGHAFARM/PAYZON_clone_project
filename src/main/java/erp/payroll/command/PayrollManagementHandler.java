package erp.payroll.command;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.payroll.dto.PayrollManagementItem;
import erp.payroll.dto.PayrollManagementPage;
import erp.payroll.model.PayrollRun;
import erp.payroll.service.PayrollManagementService;
import mvc.command.CommandHandler;

// 급여입력 화면의 조회, 사원 관리, 급여 저장 요청을 처리하는 Handler
public class PayrollManagementHandler implements CommandHandler {

	private static final String VIEW = "/WEB-INF/view/payroll/payroll-management.jsp";
	private PayrollManagementService managementService = new PayrollManagementService();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String uri = req.getRequestURI();
		if (uri.endsWith("/management.do") && req.getMethod().equalsIgnoreCase("GET")) {
			return processForm(req, res);
		} else if (uri.endsWith("/employees/add.do") && req.getMethod().equalsIgnoreCase("POST")) {
			return processAddEmployees(req, res);
		} else if (uri.endsWith("/employees/delete.do") && req.getMethod().equalsIgnoreCase("POST")) {
			return processDeleteEmployees(req, res);
		} else if (uri.endsWith("/management/save.do") && req.getMethod().equalsIgnoreCase("POST")) {
			return processSave(req, res);
		} else if (uri.endsWith("/load-previous.do") && req.getMethod().equalsIgnoreCase("POST")) {
			return processLoadPrevious(req, res);
		} else if (uri.endsWith("/give-item/save.do") && req.getMethod().equalsIgnoreCase("POST")) {
			return processGiveItem(req, res);
		} else if (uri.endsWith("/deduction-item/save.do") && req.getMethod().equalsIgnoreCase("POST")) {
			return processDeductionItem(req, res);
		}
		res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		return null;
	}

	private String processForm(HttpServletRequest req, HttpServletResponse res) {
		String year = value(req.getParameter("paymentYear"), String.valueOf(LocalDate.now().getYear()));
		String month = value(req.getParameter("paymentMonth"), String.valueOf(LocalDate.now().getMonthValue()));
		String sequence = value(req.getParameter("paymentRound"), "1");
		String incomeMode = value(req.getParameter("incomeType"), "general");
		String incomeType = "business".equals(incomeMode) ? "1" : "0";
		int employeePage = intValue(req.getParameter("employeePage"), 1);

		PayrollManagementPage page = managementService.getPage(year, twoDigits(month), twoDigits(sequence),
				incomeType, integerValue(req.getParameter("employeeId")), req.getParameter("employeeKeyword"),
				employeePage);
		setPageAttributes(req, page, year, month, sequence, incomeMode, employeePage);
		return VIEW;
	}

	private String processAddEmployees(HttpServletRequest req, HttpServletResponse res) throws IOException {
		if ("search".equals(req.getParameter("action"))) {
			return processForm(req, res);
		}
		PayrollRun run = makeRun(req);
		managementService.addEmployees(run, intValues(req.getParameterValues("employeeIds")));
		redirect(req, res, run, req.getParameter("incomeType"));
		return null;
	}

	private String processDeleteEmployees(HttpServletRequest req, HttpServletResponse res) throws IOException {
		PayrollRun run = makeRun(req);
		boolean deleteAll = "ALL".equals(req.getParameter("deleteType"));
		managementService.deleteEmployees(run, intValues(req.getParameterValues("employeeIds")), deleteAll);
		redirect(req, res, run, req.getParameter("incomeType"));
		return null;
	}

	private String processSave(HttpServletRequest req, HttpServletResponse res) throws IOException {
		try {
			return savePayroll(req, res);
		} catch (RuntimeException e) {
			// 저장 오류도 서버 오류 페이지로 넘기지 않고 급여 화면에서 안내한다.
			req.setAttribute("payrollPopupMessage", makeFailureMessage(e));
			return processForm(req, res);
		}
	}

	private String savePayroll(HttpServletRequest req, HttpServletResponse res) throws IOException {
		PayrollRun run = makeRun(req);
		int employeeId = intValue(req.getParameter("employeeId"), 0);
		if (employeeId == 0) {
			req.setAttribute("payrollPopupMessage", "급여내역을 저장할 사원을 선택해주세요");
			return processForm(req, res);
		}
		List<PayrollManagementItem> payItems = readItems(req, "give_");
		List<PayrollManagementItem> deductItems = readItems(req, "deduction_");
		if ("business".equals(req.getParameter("incomeType"))) {
			PayrollManagementPage page = managementService.getPage(run.getPayYear(), run.getPayMonth(),
					run.getPaySeq(), run.getIncomeType(), employeeId, null, 1);
			payItems = page.getPaymentGiveItems();
			deductItems = page.getPaymentDeductionItems();
			setAmount(payItems, 0, req.getParameter("businessIncome"));
			setAmount(payItems, 1, req.getParameter("otherIncome"));
			setAmount(deductItems, 0, req.getParameter("businessTax"));
			setAmount(deductItems, 1, req.getParameter("businessLocalTax"));
		}
		managementService.save(run, employeeId, payItems, deductItems);
		redirect(req, res, run, req.getParameter("incomeType"), employeeId);
		return null;
	}

	private String makeFailureMessage(RuntimeException e) {
		String message = e.getMessage();
		if (message != null && message.contains("unique constraint")) {
			return "이미 저장된 급여항목과 충돌했습니다. 화면을 다시 조회한 후 시도해주세요.";
		}
		return "요청을 처리하지 못했습니다. 사원 선택과 입력내역을 확인해주세요.";
	}

	private String processLoadPrevious(HttpServletRequest req, HttpServletResponse res) throws IOException {
		PayrollRun run = makeRun(req);
		managementService.loadPrevious(run, intValue(req.getParameter("previousPaymentPeriod"), 0));
		redirect(req, res, run, req.getParameter("incomeType"));
		return null;
	}

	private String processGiveItem(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String action = req.getParameter("action");
		if ("requestDelete".equals(action) && integerValue(req.getParameter("giveItemId")) == null) {
			req.setAttribute("payrollPopupMessage", "삭제할 지급항목을 선택해주세요.");
			return processForm(req, res);
		}
		if ("requestDelete".equals(action) || "requestDeleteAll".equals(action)) {
			req.setAttribute("itemDeleteConfirmation", "GIVE");
			req.setAttribute("itemDeleteAll", "requestDeleteAll".equals(action));
			req.setAttribute("deleteItemId", req.getParameter("giveItemId"));
			return processForm(req, res);
		}
		if ("confirmDelete".equals(action)) action = "delete";
		if ("confirmDeleteAll".equals(action)) action = "deleteAll";
		String taxType = "FREE".equals(req.getParameter("taxType")) ? "비과세" : "전체과세";
		String taxFreeCode = "비과세".equals(taxType) ? req.getParameter("taxFreeName") : null;
		if ("비과세".equals(taxType) && value(taxFreeCode, "").isEmpty()) {
			req.setAttribute("payrollPopupMessage", "비과세 항목을 선택해주세요.");
			return processForm(req, res);
		}
		long taxFreeLimit = "비과세".equals(taxType) ? longValue(req.getParameter("taxFreeLimit")) : 0;
		String attendanceLink = req.getParameter("attendanceLink");
		Integer attendanceItemId = "BATCH".equals(attendanceLink) ? null : integerValue(attendanceLink);
		String payMethod = "BATCH".equals(attendanceLink) ? "일괄지급"
				: attendanceItemId == null ? null : "근태연계";
		managementService.managePayItem(action, integerValue(req.getParameter("giveItemId")),
				req.getParameter("itemName"), taxType, taxFreeCode, taxFreeLimit,
				req.getParameter("calculationMethod"), intValue(req.getParameter("roundingUnit"), 0), payMethod,
				attendanceItemId,
				"BATCH".equals(attendanceLink) ? longValue(req.getParameter("batchAmount")) : null);
		PayrollRun run = makeRun(req);
		redirect(req, res, run, req.getParameter("incomeType"));
		return null;
	}

	private String processDeductionItem(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String action = req.getParameter("action");
		if ("requestDelete".equals(action) && integerValue(req.getParameter("deductionItemId")) == null) {
			req.setAttribute("payrollPopupMessage", "삭제할 공제항목을 선택해주세요.");
			return processForm(req, res);
		}
		if ("requestDelete".equals(action) || "requestDeleteAll".equals(action)) {
			req.setAttribute("itemDeleteConfirmation", "DEDUCTION");
			req.setAttribute("itemDeleteAll", "requestDeleteAll".equals(action));
			req.setAttribute("deleteItemId", req.getParameter("deductionItemId"));
			return processForm(req, res);
		}
		if ("confirmDelete".equals(action)) action = "delete";
		if ("confirmDeleteAll".equals(action)) action = "deleteAll";
		managementService.manageDeductItem(action,
				integerValue(req.getParameter("deductionItemId")), req.getParameter("itemName"),
				req.getParameter("calculationMethod"), intValue(req.getParameter("roundingUnit"), 0),
				req.getParameter("note"));
		PayrollRun run = makeRun(req);
		redirect(req, res, run, req.getParameter("incomeType"));
		return null;
	}

	private List<PayrollManagementItem> readItems(HttpServletRequest req, String prefix) {
		List<PayrollManagementItem> result = new ArrayList<>();
		for (Map.Entry<String, String[]> parameter : req.getParameterMap().entrySet()) {
			if (!parameter.getKey().startsWith(prefix)) {
				continue;
			}
			Integer itemCode = integerValue(parameter.getKey().substring(prefix.length()));
			if (itemCode == null) {
				continue;
			}
			PayrollManagementItem item = new PayrollManagementItem();
			item.setItemCode(itemCode);
			item.setAmount(longValue(parameter.getValue()[0]));
			result.add(item);
		}
		return result;
	}

	private void setAmount(List<PayrollManagementItem> items, int index, String amount) {
		if (index < items.size()) {
			items.get(index).setAmount(longValue(amount));
		}
	}

	private void setPageAttributes(HttpServletRequest req, PayrollManagementPage page, String year, String month,
			String sequence, String incomeMode, int employeePage) {
		req.setAttribute("paymentYears", makePaymentYears());
		req.setAttribute("selectedYear", Integer.parseInt(year));
		req.setAttribute("selectedMonth", Integer.parseInt(month));
		req.setAttribute("selectedRound", Integer.parseInt(sequence));
		req.setAttribute("incomeType", incomeMode);
		req.setAttribute("paymentEmployees", page.getPaymentEmployees());
		req.setAttribute("selectedEmployee", page.getSelectedEmployee());
		req.setAttribute("paymentGiveItems", page.getPaymentGiveItems());
		req.setAttribute("paymentDeductionItems", page.getPaymentDeductionItems());
		req.setAttribute("allGiveItems", page.getPaymentGiveItems());
		req.setAttribute("allDeductionItems", page.getPaymentDeductionItems());
		req.setAttribute("paymentTotals", page.getPaymentTotals());
		if ("business".equals(incomeMode)) {
			Map<String, Object> businessPayment = new HashMap<>();
			putBusinessItem(businessPayment, page.getPaymentGiveItems(), 0, "businessIncome",
					"businessCalculationMethod");
			putBusinessItem(businessPayment, page.getPaymentGiveItems(), 1, "otherIncome",
					"otherIncomeCalculationMethod");
			putBusinessItem(businessPayment, page.getPaymentDeductionItems(), 0, "incomeTax",
					"incomeTaxCalculationMethod");
			putBusinessItem(businessPayment, page.getPaymentDeductionItems(), 1, "localIncomeTax",
					"localIncomeTaxCalculationMethod");
			req.setAttribute("businessPayment", businessPayment);
		}
		req.setAttribute("availableEmployeePage", page.getAvailableEmployeePage());
		req.setAttribute("availableEmployees", page.getAvailableEmployeePage().getContent());
		req.setAttribute("departments", page.getDepartments());
		req.setAttribute("positions", page.getPositions());
		req.setAttribute("previousPaymentPeriods", page.getPreviousPaymentPeriods());
		req.setAttribute("taxFreeItems", page.getTaxFreeItems());
		req.setAttribute("attendanceItems", page.getAttendanceItems());
		req.setAttribute("employeePage", employeePage);
		if (page.getRun() != null) {
			req.setAttribute("calculationStart", page.getRun().getCalcStartDate());
			req.setAttribute("calculationEnd", page.getRun().getCalcEndDate());
			req.setAttribute("paymentDate", page.getRun().getPayDate());
		}
	}

	private PayrollRun makeRun(HttpServletRequest req) {
		return managementService.makeRun(req.getParameter("paymentYear"), req.getParameter("paymentMonth"),
				req.getParameter("paymentRound"), value(req.getParameter("incomeType"), "general"),
				req.getParameter("calculationStart"), req.getParameter("calculationEnd"),
				req.getParameter("paymentDate"));
	}

	private void putBusinessItem(Map<String, Object> values, List<PayrollManagementItem> items, int index,
			String amountName, String calculationName) {
		if (index < items.size()) {
			values.put(amountName, items.get(index).getAmount());
			values.put(calculationName, items.get(index).getCalculationMethod());
		}
	}

	private List<Integer> makePaymentYears() {
		List<Integer> years = new ArrayList<>();
		int currentYear = LocalDate.now().getYear();
		for (int year = currentYear + 1; year >= currentYear - 5; year--) {
			years.add(year);
		}
		return years;
	}

	private void redirect(HttpServletRequest req, HttpServletResponse res, PayrollRun run, String incomeMode)
			throws IOException {
		redirect(req, res, run, incomeMode, null);
	}

	private void redirect(HttpServletRequest req, HttpServletResponse res, PayrollRun run, String incomeMode,
			Integer employeeId) throws IOException {
		String location = req.getContextPath() + "/payroll/management.do?paymentYear=" + run.getPayYear()
				+ "&paymentMonth=" + Integer.parseInt(run.getPayMonth()) + "&paymentRound="
				+ Integer.parseInt(run.getPaySeq()) + "&incomeType=" + value(incomeMode, "general");
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

	private String twoDigits(String value) {
		return String.format("%02d", Integer.parseInt(value));
	}

	private String value(String value, String defaultValue) {
		return value == null || value.trim().isEmpty() ? defaultValue : value;
	}
}
