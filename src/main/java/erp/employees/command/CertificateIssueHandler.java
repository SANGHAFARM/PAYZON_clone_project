package erp.employees.command;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.employees.model.CertificateIssuance;
import erp.employees.service.CertificateIssueService;
import erp.employees.service.CertificateIssueService.CertificateIssueData;
import mvc.command.CommandHandler;

// 제증명서 발급 화면의 GET 조회와 POST 발급내역 저장을 처리하는 Handler
public class CertificateIssueHandler implements CommandHandler {
	private static final String VIEW = "/WEB-INF/view/employees/certificate-issue.jsp";
	private final CertificateIssueService certificateService = new CertificateIssueService();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		if (req.getMethod().equalsIgnoreCase("GET")) return processForm(req);
		if (req.getMethod().equalsIgnoreCase("POST")) return processSubmit(req, res);
		res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		return null;
	}

	private String processForm(HttpServletRequest req) {
		CertificateIssueData data = certificateService.getIssueData(parseInteger(req.getParameter("employeeId")), req.getParameter("keyword"));
		req.setAttribute("employees", data.getEmployees());
		req.setAttribute("selectedEmployee", data.getSelectedEmployee());
		req.setAttribute("careers", data.getCareers());
		req.setAttribute("departments", data.getDepartments());
		req.setAttribute("company", data.getCompany());
		req.setAttribute("issueDate", data.getIssueDate());
		req.setAttribute("issueYear", new SimpleDateFormat("yyyy").format(data.getIssueDate()));
		req.setAttribute("issueMonth", new SimpleDateFormat("MM").format(data.getIssueDate()));
		req.setAttribute("issueDay", new SimpleDateFormat("dd").format(data.getIssueDate()));
		if ("issued".equals(req.getParameter("result"))) req.setAttribute("message", "증명서 발급내역을 저장했습니다.");
		return VIEW;
	}

	private String processSubmit(HttpServletRequest req, HttpServletResponse res) throws IOException {
		Integer employeeId = parseInteger(req.getParameter("employeeId"));
		String purpose = "DIRECT".equals(req.getParameter("certificateUse"))
				? trim(req.getParameter("certificateUseDirect")) : trim(req.getParameter("certificateUse"));
		if (employeeId == null || purpose.isEmpty()) {
			req.setAttribute("message", "사원과 발급용도를 선택하세요.");
			return processForm(req);
		}

		CertificateIssuance certificate = new CertificateIssuance();
		certificate.setEmployeeId(employeeId);
		certificate.setCertType(toDatabaseCertificateType(req.getParameter("certificateType")));
		certificate.setPurpose(purpose);
		certificate.setCertMemo(trim(req.getParameter("certificateMemo")));
		certificate.setIssueDate(parseIssueDate(req));
		certificate.setIssueDeptId(parseInteger(req.getParameter("issueDepartmentId")));
		certificate.setShowCeoYn(yn(req.getParameter("showCeo")));
		certificate.setHideJuminYn(yn(req.getParameter("hideResidentNoWorking"), req.getParameter("hideResidentNoCareer"), req.getParameter("hideResidentNoRetirement")));
		certificate.setShowLogoYn("Y");
		certificate.setShowStampYn("Y");
		certificateService.issue(certificate);

		res.sendRedirect(req.getContextPath() + "/personnel/certificate.do?employeeId=" + employeeId + "&result=issued");
		return null;
	}

	private String toDatabaseCertificateType(String type) {
		if ("CAREER".equals(type)) return "경력증명서";
		if ("RETIREMENT".equals(type)) return "퇴직증명서";
		return "재직경력서";
	}

	private Date parseIssueDate(HttpServletRequest req) {
		try {
			String value = req.getParameter("issueYear") + "-" + req.getParameter("issueMonth") + "-" + req.getParameter("issueDay");
			return new SimpleDateFormat("yyyy-MM-dd").parse(value);
		} catch (Exception e) { return new Date(); }
	}

	private String yn(String... values) {
		for (String value : values) if ("Y".equals(value)) return "Y";
		return "N";
	}
	private Integer parseInteger(String value) {
		try { return value == null || value.trim().isEmpty() ? null : Integer.valueOf(value); }
		catch (NumberFormatException e) { return null; }
	}
	private String trim(String value) { return value == null ? "" : value.trim(); }
}
