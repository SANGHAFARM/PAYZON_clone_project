package erp.payroll.command;

import java.io.IOException;
import java.net.URLEncoder;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import erp.payroll.dto.PayrollApprovalSetting;
import erp.payroll.dto.PayrollItemLedgerPage;
import erp.payroll.service.PayrollItemLedgerService;
import mvc.command.CommandHandler;

// 항목별 대장 조회와 결재란 설정 요청을 처리하는 Handler
public class PayrollItemLedgerHandler implements CommandHandler {

	private static final String VIEW = "/WEB-INF/view/payroll/payroll-item-ledger.jsp";
	private static final String APPROVAL_SETTING = "payrollItemLedgerApprovalSetting";
	private PayrollItemLedgerService ledgerService = new PayrollItemLedgerService();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String uri = req.getRequestURI();
		if (uri.endsWith("/item-ledger.do") && req.getMethod().equalsIgnoreCase("GET")) {
			return processList(req);
		} else if (uri.endsWith("/item-ledger/approval-setting.do")
				&& req.getMethod().equalsIgnoreCase("POST")) {
			return processApprovalSetting(req, res);
		}
		res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		return null;
	}

	private String processList(HttpServletRequest req) {
		YearMonth currentMonth = YearMonth.now();
		String startMonth = validMonth(req.getParameter("startMonth"), currentMonth.withMonth(1).toString());
		String endMonth = validMonth(req.getParameter("endMonth"), currentMonth.toString());
		if (YearMonth.parse(startMonth).isAfter(YearMonth.parse(endMonth))) {
			startMonth = endMonth;
		}
		if (YearMonth.parse(startMonth).plusMonths(11).isBefore(YearMonth.parse(endMonth))) {
			startMonth = YearMonth.parse(endMonth).minusMonths(11).toString();
		}

		PayrollItemLedgerPage page = ledgerService.getPage(startMonth, endMonth, req.getParameter("itemCode"));
		req.setAttribute("startMonth", startMonth);
		req.setAttribute("endMonth", endMonth);
		req.setAttribute("selectedItemCode", req.getParameter("itemCode"));
		req.setAttribute("paymentItems", page.getItems());
		req.setAttribute("ledgerMonths", page.getMonths());
		req.setAttribute("ledgerRows", page.getRows());
		req.setAttribute("ledgerTotals", page.getTotals());
		req.setAttribute("approvalSetting", getApprovalSetting(req.getSession()));
		return VIEW;
	}

	private String processApprovalSetting(HttpServletRequest req, HttpServletResponse res) throws IOException {
		PayrollApprovalSetting setting = new PayrollApprovalSetting();
		setting.setApprovalCount(limit(intValue(req.getParameter("approvalCount"), 3), 1, 5));
		for (int number = 1; number <= 5; number++) {
			String use = "Y".equals(req.getParameter("approvalUse" + number)) ? "Y" : "N";
			setting.getApprovalUses().put(number, use);
			setting.getApproverNames().put(number, value(req.getParameter("approverName" + number)));
		}
		req.getSession().setAttribute(APPROVAL_SETTING, setting);

		String startMonth = validMonth(req.getParameter("startMonth"), YearMonth.now().withMonth(1).toString());
		String endMonth = validMonth(req.getParameter("endMonth"), YearMonth.now().toString());
		String itemCode = value(req.getParameter("itemCode"));
		res.sendRedirect(req.getContextPath() + "/payroll/item-ledger.do?startMonth=" + startMonth
				+ "&endMonth=" + endMonth + "&itemCode=" + URLEncoder.encode(itemCode, "UTF-8"));
		return null;
	}

	private PayrollApprovalSetting getApprovalSetting(HttpSession session) {
		PayrollApprovalSetting setting = (PayrollApprovalSetting) session.getAttribute(APPROVAL_SETTING);
		return setting == null ? new PayrollApprovalSetting() : setting;
	}

	private String validMonth(String value, String defaultValue) {
		try {
			return YearMonth.parse(value).toString();
		} catch (DateTimeParseException | NullPointerException e) {
			return defaultValue;
		}
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

	private String value(String value) {
		return value == null ? "" : value.trim();
	}
}
