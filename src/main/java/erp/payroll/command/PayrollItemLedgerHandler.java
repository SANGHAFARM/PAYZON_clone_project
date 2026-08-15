package erp.payroll.command;

import java.time.YearMonth;
import java.time.format.DateTimeParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.payroll.dto.PayrollItemLedgerPage;
import erp.payroll.service.PayrollItemLedgerService;
import mvc.command.CommandHandler;

// 항목별 대장 조회 요청을 처리하는 Handler
public class PayrollItemLedgerHandler implements CommandHandler {

	private static final String VIEW = "/WEB-INF/view/payroll/payroll-item-ledger.jsp";
	private PayrollItemLedgerService ledgerService = new PayrollItemLedgerService();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String uri = req.getRequestURI();
		if (uri.endsWith("/item-ledger.do") && req.getMethod().equalsIgnoreCase("GET")) {
			return processList(req);
		}
		res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		return null;
	}

	private String processList(HttpServletRequest req) {
		YearMonth currentMonth = YearMonth.now();
		String startMonth = validMonth(req.getParameter("startMonth"), currentMonth.withMonth(1).toString());
		String endMonth = validMonth(req.getParameter("endMonth"), currentMonth.toString());
		String itemCode = value(req.getParameter("itemCode"));
		boolean requestedSearch = "search".equals(req.getParameter("mode"));
		if (YearMonth.parse(startMonth).isAfter(YearMonth.parse(endMonth))) {
			startMonth = endMonth;
		}
		if (YearMonth.parse(startMonth).plusMonths(11).isBefore(YearMonth.parse(endMonth))) {
			startMonth = YearMonth.parse(endMonth).minusMonths(11).toString();
		}

		// 조회 버튼을 눌렀지만 항목을 고르지 않은 경우 화면에서 안내한다.
		if (requestedSearch && itemCode.isEmpty()) {
			req.setAttribute("ledgerPopupMessage", "조회할 항목을 선택해주세요");
		}

		PayrollItemLedgerPage page = ledgerService.getPage(startMonth, endMonth, itemCode);
		req.setAttribute("startMonth", startMonth);
		req.setAttribute("endMonth", endMonth);
		req.setAttribute("selectedItemCode", itemCode);
		req.setAttribute("paymentItems", page.getItems());
		req.setAttribute("ledgerMonths", page.getMonths());
		req.setAttribute("ledgerRows", page.getRows());
		req.setAttribute("ledgerTotals", page.getTotals());
		return VIEW;
	}

	private String validMonth(String value, String defaultValue) {
		try {
			return YearMonth.parse(value).toString();
		} catch (DateTimeParseException | NullPointerException e) {
			return defaultValue;
		}
	}

	private String value(String value) {
		return value == null ? "" : value.trim();
	}
}
