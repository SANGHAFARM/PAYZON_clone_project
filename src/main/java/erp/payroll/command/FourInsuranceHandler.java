package erp.payroll.command;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.payroll.dto.FourInsurancePage;
import erp.payroll.service.FourInsuranceService;
import mvc.command.CommandHandler;

// 선택한 급여 차수의 4대보험 공제내역을 조회한다.
public class FourInsuranceHandler implements CommandHandler {

	private static final String VIEW = "/WEB-INF/view/payroll/four-insurance-deduction.jsp";
	private FourInsuranceService insuranceService = new FourInsuranceService();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) {
		if (!req.getMethod().equalsIgnoreCase("GET")) {
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}

		int currentYear = LocalDate.now().getYear();
		int year = limit(intValue(req.getParameter("year"), currentYear), currentYear - 20, currentYear + 1);
		int month = limit(intValue(req.getParameter("month"), LocalDate.now().getMonthValue()), 1, 12);
		int sequence = limit(intValue(req.getParameter("round"), 1), 1, 10);
		FourInsurancePage page = insuranceService.getPage(String.valueOf(year), twoDigits(month),
				twoDigits(sequence));

		req.setAttribute("paymentYears", makePaymentYears(currentYear));
		req.setAttribute("selectedYear", year);
		req.setAttribute("selectedMonth", month);
		req.setAttribute("selectedRound", sequence);
		req.setAttribute("calculationStart", page.getCalculationStart());
		req.setAttribute("calculationEnd", page.getCalculationEnd());
		req.setAttribute("paymentDate", page.getPaymentDate());
		req.setAttribute("insuranceDeductions", page.getDeductions());
		req.setAttribute("insuranceTotals", page.getTotals());
		return VIEW;
	}

	private List<Integer> makePaymentYears(int currentYear) {
		List<Integer> years = new ArrayList<>();
		for (int year = currentYear + 1; year >= currentYear - 10; year--) {
			years.add(year);
		}
		return years;
	}

	private int intValue(String value, int defaultValue) {
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	private int limit(int value, int minimum, int maximum) {
		return Math.min(Math.max(value, minimum), maximum);
	}

	private String twoDigits(int value) {
		return String.format("%02d", value);
	}
}
