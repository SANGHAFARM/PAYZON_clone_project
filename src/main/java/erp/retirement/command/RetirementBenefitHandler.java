package erp.retirement.command;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.retirement.dto.RetirementBenefitForm;
import erp.retirement.model.RetirementIncomeEntry;
import erp.retirement.model.RetirementTaxDeferral;
import erp.retirement.service.RetirementBenefitService;
import erp.retirement.service.RetirementBenefitService.BenefitPageData;
import mvc.command.CommandHandler;

// 퇴직급여 조회, 계산, 저장과 삭제 요청을 처리한다.
public class RetirementBenefitHandler implements CommandHandler {

	private static final String VIEW = "/WEB-INF/view/retirement/retirement-benefit.jsp";
	private final RetirementBenefitService service = new RetirementBenefitService();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String uri = req.getRequestURI();
		boolean get = "GET".equalsIgnoreCase(req.getMethod());
		boolean post = "POST".equalsIgnoreCase(req.getMethod());

		if (get && (uri.endsWith("/benefit.do") || uri.endsWith("/employee-search.do"))) {
			return showPage(req, null);
		}
		if (post && uri.endsWith("/new.do")) {
			try {
				List<Integer> employeeIds = integerValues(req.getParameterValues("employeeIds"));
				if (employeeIds.isEmpty()) {
					throw new IllegalArgumentException("추가할 사원을 선택해주세요");
				}
				Integer activeEmployeeId = parseInt(req.getParameter("activeEmployeeId"));
				if (activeEmployeeId == null || !employeeIds.contains(activeEmployeeId)) {
					activeEmployeeId = employeeIds.get(0);
				}
				req.setAttribute("draftEmployeeIds", employeeIds);
				return showPage(req, service.prepareNew(activeEmployeeId));
			} catch (IllegalArgumentException e) {
				req.setAttribute("message", e.getMessage());
				return showPage(req, null);
			}
		}
		if (post && uri.endsWith("/load-pay.do")) {
			return processLoadPay(req);
		}
		if (post && uri.endsWith("/calculate.do")) {
			return processCalculate(req);
		}
		if (post && uri.endsWith("/save.do")) {
			return processSave(req, res);
		}
		if (post && uri.endsWith("/delete-all.do")) {
			service.delete(null, true, parseInt(req.getParameter("paymentYear")));
			redirectList(req, res);
			return null;
		}
		if (post && uri.endsWith("/delete.do")) {
			Integer calculationId = parseInt(req.getParameter("calculationId"));
			if (calculationId == null) {
				res.sendRedirect(req.getContextPath() + "/retirement/benefit.do");
				return null;
			}
			service.delete(calculationId, false, null);
			redirectList(req, res);
			return null;
		}

		res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		return null;
	}

	private String processLoadPay(HttpServletRequest req) {
		RetirementBenefitForm form = null;
		try {
			form = readForm(req, true);
			validateCalculationDates(form);
			service.loadRecentSalaryEntries(form);
			service.calculate(form);
			req.setAttribute("message", "최근 3개월 급여내역을 불러왔습니다.");
		} catch (IllegalArgumentException e) {
			req.setAttribute("message", e.getMessage());
		}
		return showPage(req, form);
	}

	private String processCalculate(HttpServletRequest req) {
		RetirementBenefitForm form = null;
		try {
			form = readForm(req, true);
			validateCalculationDates(form);
			service.calculate(form);
		} catch (IllegalArgumentException e) {
			req.setAttribute("message", e.getMessage());
		}
		return showPage(req, form);
	}

	private String processSave(HttpServletRequest req, HttpServletResponse res) throws IOException {
		RetirementBenefitForm form = null;
		try {
			form = readForm(req, true);
			validateCalculationDates(form);
			int calculationId = service.save(form);
			redirect(req, res, "saved", calculationId);
			return null;
		} catch (IllegalArgumentException e) {
			req.setAttribute("message", e.getMessage());
			return showPage(req, form);
		}
	}

	private String showPage(HttpServletRequest req, RetirementBenefitForm override) {
		boolean employeeSearchRequest = req.getRequestURI().endsWith("/employee-search.do");
		String employeeKeyword = req.getParameter("employeeKeyword");
		// 사원 검색어는 공백을 제외하고 두 글자 이상 입력해야 한다.
		if (employeeSearchRequest && "keyword".equals(req.getParameter("searchMode"))
				&& (employeeKeyword == null || employeeKeyword.trim().length() < 2)) {
			req.setAttribute("message", "검색어를 2자 이상 입력해주세요");
			employeeKeyword = "";
		}
		Integer requestedYear = parseInt(req.getParameter("paymentYear"));
		int year = requestedYear == null ? LocalDate.now().getYear() : requestedYear;
		Integer calculationId = parseInt(req.getParameter("calculationId"));
		BenefitPageData data = service.getPage(year, calculationId,
				employeeKeyword, parseInt(req.getParameter("departmentId")));

		req.setAttribute("paymentYears", service.getPaymentYears());
		req.setAttribute("selectedYear", year);
		boolean newRequest = req.getRequestURI().endsWith("/new.do");
		boolean loadList = !newRequest && ("list".equals(req.getParameter("mode"))
				|| calculationId != null || req.getParameter("result") != null
				|| override != null);
		req.setAttribute("retirementBenefits", loadList ? data.getBenefits() : java.util.Collections.emptyList());
		req.setAttribute("selectableEmployees", data.getEmployees());
		req.setAttribute("departments", data.getDepartments());
		req.setAttribute("retirementBenefit", override == null ? data.getForm() : override);
		@SuppressWarnings("unchecked")
		List<Integer> draftEmployeeIds = (List<Integer>) req.getAttribute("draftEmployeeIds");
		if (draftEmployeeIds != null) {
			List<erp.employees.dto.EmployeeListItem> draftEmployees = new ArrayList<>();
			for (erp.employees.dto.EmployeeListItem employee : data.getEmployees()) {
				if (draftEmployeeIds.contains(employee.getEmployeeId())) {
					draftEmployees.add(employee);
				}
			}
			req.setAttribute("draftBenefitEmployees", draftEmployees);
		}
		if (req.getAttribute("message") == null && "saved".equals(req.getParameter("result"))) {
			req.setAttribute("message", "퇴직급여 내역을 저장했습니다.");
		}
		if (req.getAttribute("message") == null && "deleted".equals(req.getParameter("result"))) {
			req.setAttribute("message", "퇴직급여 내역을 삭제했습니다.");
		}
		return VIEW;
	}

	private RetirementBenefitForm readForm(HttpServletRequest req, boolean readRows) {
		RetirementBenefitForm form = new RetirementBenefitForm();
		form.setCalculationId(intValue(req.getParameter("calculationId")));
		form.setEmployeeId(requiredInt(req, "employeeId"));
		form.setSettlementType(req.getParameter("settlementType"));
		form.setStartDate(req.getParameter("startDate"));
		form.setEndDate(req.getParameter("endDate"));
		form.setExcludedDays(intValue(req.getParameter("excludedDays")));
		form.setCompensation(longValue(req.getParameter("compensation")));
		form.setDismissalAllowance(longValue(req.getParameter("dismissalAllowance")));
		form.setTaxFreeRetirement(longValue(req.getParameter("taxFreeRetirement")));
		form.setPrepaidTax(longValue(req.getParameter("prepaidTax")));
		form.setTaxCredit(longValue(req.getParameter("taxCredit")));
		form.setDailyOrdinary(longValue(req.getParameter("dailyOrdinary")));
		form.setRetirementIncome(longValue(req.getParameter("retirementIncome")));
		form.setIncomeTax(longValue(req.getParameter("incomeTax")));
		form.setLocalIncomeTax(longValue(req.getParameter("localIncomeTax")));
		form.setRuralTax(longValue(req.getParameter("ruralTax")));
		form.setOtherDeduction(longValue(req.getParameter("otherDeduction")));
		form.setPaymentMethod(req.getParameter("paymentMethod"));
		form.setPaymentDate(req.getParameter("paymentDate"));

		if (readRows) {
			readSalaryRows(req, form);
			readOtherRows(req, form);
			readDeferrals(req, form);
		}
		return form;
	}

	private void readSalaryRows(HttpServletRequest req, RetirementBenefitForm form) {
		String[] starts = req.getParameterValues("salaryStartDate");
		String[] ends = req.getParameterValues("salaryEndDate");
		String[] amounts = req.getParameterValues("salaryTotal");
		if (starts == null) {
			return;
		}
		for (int i = 0; i < starts.length; i++) {
			String end = at(ends, i);
			if (blank(starts[i]) || blank(end)) {
				continue;
			}
			RetirementIncomeEntry entry = new RetirementIncomeEntry();
			entry.setDataType("SALARY");
			entry.setPeriodStartDate(date(starts[i]));
			entry.setPeriodEndDate(date(end));
			entry.setCalcDays((double) (ChronoUnit.DAYS.between(LocalDate.parse(starts[i]), LocalDate.parse(end)) + 1));
			entry.setAmount(longAt(amounts, i));
			entry.setThreeMonthAmount(0);
			form.getIncomeEntries().add(entry);
		}
	}

	private void readOtherRows(HttpServletRequest req, RetirementBenefitForm form) {
		String[] months = req.getParameterValues("otherIncomeMonth");
		String[] names = req.getParameterValues("otherIncomeItem");
		String[] amounts = req.getParameterValues("otherIncomeAmount");
		String[] threeMonthAmounts = req.getParameterValues("threeMonthAmount");
		if (months == null) {
			return;
		}
		for (int i = 0; i < months.length; i++) {
			if (blank(months[i]) || blank(at(names, i))) {
				continue;
			}
			RetirementIncomeEntry entry = new RetirementIncomeEntry();
			entry.setDataType("ETC_INCOME");
			entry.setCalcDays(0d);
			entry.setPayYm(months[i].replace("-", ""));
			entry.setItemName(at(names, i));
			entry.setAmount(longAt(amounts, i));
			entry.setThreeMonthAmount(longAt(threeMonthAmounts, i));
			form.getIncomeEntries().add(entry);
		}
	}

	private void readDeferrals(HttpServletRequest req, RetirementBenefitForm form) {
		String[] names = req.getParameterValues("pensionProvider");
		String[] businessNumbers = req.getParameterValues("pensionBusinessNo");
		String[] accounts = req.getParameterValues("pensionAccount");
		String[] dates = req.getParameterValues("pensionDate");
		String[] amounts = req.getParameterValues("pensionAmount");
		if (names == null) {
			return;
		}
		for (int i = 0; i < names.length; i++) {
			if (blank(names[i]) || blank(at(accounts, i))) {
				continue;
			}
			RetirementTaxDeferral deferral = new RetirementTaxDeferral();
			deferral.setBizName(names[i]);
			deferral.setBizRegNo(at(businessNumbers, i));
			deferral.setAccountNo(at(accounts, i));
			deferral.setDepositDate(blank(at(dates, i)) ? null : date(at(dates, i)));
			deferral.setDepositAmt(longAt(amounts, i));
			form.getTaxDeferrals().add(deferral);
		}
	}

	private void redirect(HttpServletRequest req, HttpServletResponse res,
			String result, Integer calculationId) throws IOException {
		String query = "?result=" + result;
		if (calculationId != null) {
			query += "&calculationId=" + calculationId;
		}
		res.sendRedirect(req.getContextPath() + "/retirement/benefit.do" + query);
	}

	private void redirectList(HttpServletRequest req, HttpServletResponse res) throws IOException {
		Integer paymentYear = parseInt(req.getParameter("paymentYear"));
		String query = paymentYear == null ? "" : "?mode=list&paymentYear=" + paymentYear;
		res.sendRedirect(req.getContextPath() + "/retirement/benefit.do" + query);
	}

	private int requiredInt(HttpServletRequest req, String name) {
		Integer value = parseInt(req.getParameter(name));
		if (value == null) {
			throw new IllegalArgumentException("사원을 선택해주세요");
		}
		return value;
	}

	private List<Integer> integerValues(String[] values) {
		List<Integer> result = new ArrayList<>();
		if (values == null) {
			return result;
		}
		for (String value : values) {
			Integer number = parseInt(value);
			if (number != null && !result.contains(number)) {
				result.add(number);
			}
		}
		return result;
	}

	private void validateCalculationDates(RetirementBenefitForm form) {
		try {
			LocalDate.parse(form.getStartDate());
			LocalDate.parse(form.getEndDate());
		} catch (Exception e) {
			throw new IllegalArgumentException("정산 시작일과 종료일을 입력하세요.");
		}
	}

	private Integer parseInt(String value) {
		try {
			return Integer.valueOf(value);
		} catch (Exception e) {
			return null;
		}
	}

	private int intValue(String value) {
		Integer number = parseInt(value);
		return number == null ? 0 : number;
	}

	private long longValue(String value) {
		try {
			return Long.parseLong(value == null ? "0" : value.replace(",", "").trim());
		} catch (Exception e) {
			return 0;
		}
	}

	private Date date(String value) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			format.setLenient(false);
			return format.parse(value);
		} catch (ParseException e) {
			throw new IllegalArgumentException("날짜 형식을 확인하세요.");
		}
	}

	private boolean blank(String value) {
		return value == null || value.trim().isEmpty();
	}

	private String at(String[] values, int index) {
		return values != null && index < values.length ? values[index] : "";
	}

	private long longAt(String[] values, int index) {
		return longValue(at(values, index));
	}
}
