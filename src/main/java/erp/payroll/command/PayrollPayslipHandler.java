package erp.payroll.command;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.payroll.dto.PayrollPayslipPage;
import erp.payroll.service.PayrollPayslipService;
import mvc.command.CommandHandler;

// 급여명세서 회차와 사원 선택 요청을 처리하는 Handler
public class PayrollPayslipHandler implements CommandHandler {

	private static final String VIEW = "/WEB-INF/view/payroll/payroll-payslip.jsp";
	private PayrollPayslipService payslipService = new PayrollPayslipService();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) {
		if (!req.getMethod().equalsIgnoreCase("GET")) {
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
		String year = value(req.getParameter("paymentYear"), String.valueOf(LocalDate.now().getYear()));
		String month = value(req.getParameter("paymentMonth"), String.valueOf(LocalDate.now().getMonthValue()));
		String sequence = value(req.getParameter("paymentRound"), "1");
		String keyword = trim(req.getParameter("keyword"));
		if ("search".equals(req.getParameter("mode")) && keyword.length() < 2) {
			// 검색 버튼은 두 글자 이상의 검색어가 있을 때만 조건 조회한다.
			req.setAttribute("payslipPopupMessage", "검색어를 2자 이상 입력해주세요");
			keyword = "";
		}
		PayrollPayslipPage page = payslipService.getPage(year, twoDigits(month), twoDigits(sequence),
				integerValue(req.getParameter("employeeId")), keyword);
		req.setAttribute("paymentYears", makePaymentYears());
		req.setAttribute("selectedYear", Integer.parseInt(year));
		req.setAttribute("selectedMonth", Integer.parseInt(month));
		req.setAttribute("selectedRound", Integer.parseInt(sequence));
		req.setAttribute("calculationStart", page.getCalculationStart());
		req.setAttribute("calculationEnd", page.getCalculationEnd());
		req.setAttribute("paymentDate", page.getPaymentDate());
		req.setAttribute("payslipEmployees", page.getEmployees());
		req.setAttribute("selectedEmployee", page.getSelectedEmployee());
		req.setAttribute("paymentItems", page.getPaymentItems());
		req.setAttribute("deductionItems", page.getDeductionItems());
		req.setAttribute("company", page.getCompany());
		return VIEW;
	}

	private List<Integer> makePaymentYears() {
		List<Integer> years = new ArrayList<>();
		int currentYear = LocalDate.now().getYear();
		for (int year = currentYear + 1; year >= currentYear - 5; year--) {
			years.add(year);
		}
		return years;
	}

	private Integer integerValue(String value) {
		try {
			return Integer.valueOf(value);
		} catch (Exception e) {
			return null;
		}
	}

	private String twoDigits(String value) {
		return String.format("%02d", Integer.parseInt(value));
	}

	private String value(String value, String defaultValue) {
		return value == null || value.trim().isEmpty() ? defaultValue : value;
	}

	private String trim(String value) {
		return value == null ? "" : value.trim();
	}
}
