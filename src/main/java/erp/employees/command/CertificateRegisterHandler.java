package erp.employees.command;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.employees.service.CertificateRegisterCondition;
import erp.employees.service.CertificateRegisterService;
import erp.employees.service.CertificateRegisterService.CertificateRegisterResult;
import mvc.command.CommandHandler;

// 제증명서 발급대장의 검색, 페이징, 선택삭제 및 전체삭제 요청 Handler
public class CertificateRegisterHandler implements CommandHandler {
	private static final String VIEW = "/WEB-INF/view/employees/certificate-register.jsp";
	private final CertificateRegisterService registerService = new CertificateRegisterService();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		if (req.getMethod().equalsIgnoreCase("GET")) return processList(req);
		if (req.getMethod().equalsIgnoreCase("POST")) return processDelete(req, res);
		res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		return null;
	}

	private String processList(HttpServletRequest req) {
		CertificateRegisterCondition condition = createCondition(req);
		CertificateRegisterResult result = registerService.getRegister(condition);
		req.setAttribute("certificates", result.getCertificates());
		req.setAttribute("totalCount", result.getTotalCount());
		req.setAttribute("pageInfo", result.getPageInfo());
		req.setAttribute("condition", condition);
		if ("deleted".equals(req.getParameter("result"))) req.setAttribute("message", "선택한 발급내역을 삭제했습니다.");
		if ("allDeleted".equals(req.getParameter("result"))) req.setAttribute("message", "전체 발급내역을 삭제했습니다.");
		return VIEW;
	}

	private String processDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String mode = req.getParameter("deleteMode");
		registerService.delete(mode, req.getParameterValues("certificateIds"));
		String result = "ALL".equals(mode) ? "allDeleted" : "deleted";
		res.sendRedirect(req.getContextPath() + "/personnel/certificate-register.do?result=" + result);
		return null;
	}

	private CertificateRegisterCondition createCondition(HttpServletRequest req) {
		CertificateRegisterCondition condition = new CertificateRegisterCondition();
		condition.setCertificateType(trim(req.getParameter("certificateType")));
		condition.setIssueDateFrom(trim(req.getParameter("issueDateFrom")));
		condition.setIssueDateTo(trim(req.getParameter("issueDateTo")));
		condition.setKeyword(trim(req.getParameter("keyword")));
		condition.setPage(parsePositiveInt(req.getParameter("page"), 1));
		condition.setPageSize(10);
		return condition;
	}

	private String trim(String value) { return value == null ? "" : value.trim(); }
	private int parsePositiveInt(String value, int defaultValue) {
		try { int parsed = Integer.parseInt(value); return parsed > 0 ? parsed : defaultValue; }
		catch (Exception e) { return defaultValue; }
	}
}
