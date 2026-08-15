package erp.employees.command;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

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
		if (req.getMethod().equalsIgnoreCase("GET")) {
			return processList(req);
		}
		if (req.getMethod().equalsIgnoreCase("POST")) {
			return processDelete(req, res);
		}
		res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		return null;
	}

	private String processList(HttpServletRequest req) {
		CertificateRegisterCondition condition = createCondition(req);
		if ("search".equals(req.getParameter("mode")) && !hasSearchCondition(condition)) {
			// 조건 검색은 증명서 종류, 발급일 또는 검색어 중 하나 이상을 입력해야 한다.
			req.setAttribute("popupMessage", "검색 조건을 하나 이상 설정해주세요.");
		}
		CertificateRegisterResult result = registerService.getRegister(condition);
		req.setAttribute("certificates", result.getCertificates());
		req.setAttribute("totalCount", result.getTotalCount());
		req.setAttribute("pageInfo", result.getPageInfo());
		req.setAttribute("condition", condition);
		if ("deleted".equals(req.getParameter("result"))) {
			req.setAttribute("popupMessage", "선택한 발급내역을 삭제했습니다.");
		}
		if ("allDeleted".equals(req.getParameter("result"))) {
			req.setAttribute("popupMessage", "전체 발급내역을 삭제했습니다.");
		}
		if ("notSelected".equals(req.getParameter("result"))) {
			req.setAttribute("popupMessage", "삭제할 발급내역을 선택해주세요.");
		}
		return VIEW;
	}

	private String processDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String action = req.getParameter("deleteAction");
		String[] certificateIds = req.getParameterValues("certificateIds");
		if ("requestSelected".equals(action) && (certificateIds == null || certificateIds.length == 0)) {
			res.sendRedirect(req.getContextPath() + "/employees/certificate-register.do?result=notSelected");
			return null;
		}
		if ("requestSelected".equals(action) || "requestAll".equals(action)) {
			// 첫 번째 POST에서는 삭제하지 않고 확인창에 삭제 범위만 전달한다.
			req.setAttribute("deleteConfirmation", true);
			req.setAttribute("deleteMode", "requestAll".equals(action) ? "ALL" : "SELECTED");
			req.setAttribute("deleteCertificateIds", certificateIds);
			req.setAttribute("deleteCertificateCount", certificateIds == null ? 0 : certificateIds.length);
			return processList(req);
		}
		if ("confirmSelected".equals(action) || "confirmAll".equals(action)) {
			String deleteMode = "confirmAll".equals(action) ? "ALL" : "SELECTED";
			registerService.delete(deleteMode, certificateIds);
			String result = "ALL".equals(deleteMode) ? "allDeleted" : "deleted";
			res.sendRedirect(req.getContextPath() + "/employees/certificate-register.do?result=" + result);
			return null;
		}
		res.sendRedirect(req.getContextPath() + "/employees/certificate-register.do");
		return null;
	}

	private boolean hasSearchCondition(CertificateRegisterCondition condition) {
		return !condition.getCertificateType().isEmpty() || !condition.getIssueDateFrom().isEmpty()
				|| !condition.getIssueDateTo().isEmpty() || !condition.getKeyword().isEmpty();
	}

	private CertificateRegisterCondition createCondition(HttpServletRequest req) {
		CertificateRegisterCondition condition = new CertificateRegisterCondition();
		condition.setCertificateType(trim(req.getParameter("certificateType")));
		String issueDateFrom = validDate(req.getParameter("issueDateFrom"));
		String issueDateTo = validDate(req.getParameter("issueDateTo"));
		if (!issueDateFrom.isEmpty() && !issueDateTo.isEmpty()
				&& LocalDate.parse(issueDateFrom).isAfter(LocalDate.parse(issueDateTo))) {
			String temporaryDate = issueDateFrom;
			issueDateFrom = issueDateTo;
			issueDateTo = temporaryDate;
		}
		condition.setIssueDateFrom(issueDateFrom);
		condition.setIssueDateTo(issueDateTo);
		condition.setKeyword(trim(req.getParameter("keyword")));
		condition.setPage(parsePositiveInt(req.getParameter("page"), 1));
		condition.setPageSize(10);
		return condition;
	}

	private String trim(String value) { return value == null ? "" : value.trim(); }
	private String validDate(String value) {
		String date = trim(value);
		if (date.isEmpty()) {
			return "";
		}
		try {
			return LocalDate.parse(date).toString();
		} catch (DateTimeParseException e) {
			return "";
		}
	}
	private int parsePositiveInt(String value, int defaultValue) {
		try { int parsed = Integer.parseInt(value); return parsed > 0 ? parsed : defaultValue; }
		catch (Exception e) {
			return defaultValue;
		}
	}
}
