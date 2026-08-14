package erp.payroll.command;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.payroll.dto.PayrollRegisterDetailPage;
import erp.payroll.dto.PayrollRegisterListPage;
import erp.payroll.service.PayrollRegisterService;
import mvc.command.CommandHandler;

// 급여대장 목록, 상세 조회와 삭제 요청을 처리하는 Handler
public class PayrollRegisterHandler implements CommandHandler {

	private static final String LIST_VIEW = "/WEB-INF/view/payroll/payroll-register.jsp";
	private static final String DETAIL_VIEW = "/WEB-INF/view/payroll/payroll-register-detail.jsp";
	private PayrollRegisterService registerService = new PayrollRegisterService();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String uri = req.getRequestURI();
		if (uri.endsWith("/register.do") && req.getMethod().equalsIgnoreCase("GET")) {
			return processList(req, res);
		} else if (uri.endsWith("/register/detail.do") && req.getMethod().equalsIgnoreCase("GET")) {
			return processDetail(req, res);
		} else if (uri.endsWith("/register/delete.do") && req.getMethod().equalsIgnoreCase("POST")) {
			return processDelete(req, res);
		}
		res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		return null;
	}

	private String processList(HttpServletRequest req, HttpServletResponse res) {
		String year = value(req.getParameter("year"), String.valueOf(LocalDate.now().getYear()));
		int pageNumber = intValue(req.getParameter("page"), 0);
		PayrollRegisterListPage page = registerService.getList(year, pageNumber);
		req.setAttribute("paymentYears", makePaymentYears());
		req.setAttribute("selectedYear", Integer.parseInt(year));
		req.setAttribute("paymentRegisters", page.getRegisters());
		req.setAttribute("registerTotals", page.getTotals());
		req.setAttribute("page", page.getPageInfo());
		return LIST_VIEW;
	}

	private String processDetail(HttpServletRequest req, HttpServletResponse res) {
		int runId = requiredInt(req.getParameter("registerId"));
		PayrollRegisterDetailPage page = registerService.getDetail(runId, req.getParameter("employmentType"),
				integerValue(req.getParameter("departmentId")), req.getParameter("incomeType"));
		req.setAttribute("register", page.getRegister());
		req.setAttribute("paymentItems", page.getPaymentItems());
		req.setAttribute("deductionItems", page.getDeductionItems());
		req.setAttribute("registerEmployees", page.getEmployees());
		req.setAttribute("registerTotals", page.getTotals());
		req.setAttribute("departments", page.getDepartments());
		req.setAttribute("employmentTypes", registerService.getEmploymentTypes());
		req.setAttribute("selectedEmploymentType", req.getParameter("employmentType"));
		req.setAttribute("selectedDepartmentId", integerValue(req.getParameter("departmentId")));
		req.setAttribute("selectedIncomeType", req.getParameter("incomeType"));
		return DETAIL_VIEW;
	}

	private String processDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
		registerService.delete(requiredInt(req.getParameter("registerId")));
		String year = value(req.getParameter("year"), String.valueOf(LocalDate.now().getYear()));
		res.sendRedirect(req.getContextPath() + "/payroll/register.do?year=" + year);
		return null;
	}

	private List<Integer> makePaymentYears() {
		List<Integer> years = new ArrayList<>();
		int currentYear = LocalDate.now().getYear();
		for (int year = currentYear + 1; year >= currentYear - 5; year--) {
			years.add(year);
		}
		return years;
	}

	private int requiredInt(String value) {
		Integer number = integerValue(value);
		if (number == null) {
			throw new IllegalArgumentException("급여대장을 선택하세요.");
		}
		return number;
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

	private String value(String value, String defaultValue) {
		return value == null || value.trim().isEmpty() ? defaultValue : value;
	}
}
