package erp.retirement.command;

import java.time.LocalDate;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.retirement.service.RetirementPayslipService;
import erp.retirement.service.RetirementPayslipService.PayslipData;
import mvc.command.CommandHandler;

// 지급년도와 사원을 기준으로 퇴직급여명세서를 조회한다.
public class RetirementPayslipHandler implements CommandHandler {

	private static final String VIEW = "/WEB-INF/view/retirement/retirement-payslip.jsp";
	private final RetirementPayslipService service = new RetirementPayslipService();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) {
		if (!"GET".equalsIgnoreCase(req.getMethod())) {
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}

		int year = intValue(req.getParameter("paymentYear"), LocalDate.now().getYear());
		Integer calculationId = parseInt(req.getParameter("calculationId"));
		PayslipData data = service.getData(year, req.getParameter("keyword"), calculationId);

		req.setAttribute("paymentYears", service.getPaymentYears());
		req.setAttribute("selectedYear", year);
		req.setAttribute("retirementPayslips", data.getItems());
		req.setAttribute("selectedPayslip", data.getSelected());
		req.setAttribute("company", data.getCompany());

		Calendar today = Calendar.getInstance();
		req.setAttribute("issueYear", today.get(Calendar.YEAR));
		req.setAttribute("issueMonth", today.get(Calendar.MONTH) + 1);
		req.setAttribute("issueDay", today.get(Calendar.DAY_OF_MONTH));
		return VIEW;
	}

	private Integer parseInt(String value) {
		try {
			return Integer.valueOf(value);
		} catch (Exception e) {
			return null;
		}
	}

	private int intValue(String value, int defaultValue) {
		Integer number = parseInt(value);
		return number == null ? defaultValue : number;
	}
}
