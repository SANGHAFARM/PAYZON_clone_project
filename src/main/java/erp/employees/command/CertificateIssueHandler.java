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

// 제증명서 발급 화면의 조회와 발급 요청을 처리한다.
public class CertificateIssueHandler implements CommandHandler {
	private static final String VIEW = "/WEB-INF/view/employees/certificate-issue.jsp";
	private final CertificateIssueService certificateService = new CertificateIssueService();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		if (req.getMethod().equalsIgnoreCase("GET")) {
			return processForm(req);
		}
		if (req.getMethod().equalsIgnoreCase("POST")) {
			return processSubmit(req, res);
		}
		res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		return null;
	}

	private String processForm(HttpServletRequest req) {
		String keyword = trim(req.getParameter("keyword"));
		if ("search".equals(req.getParameter("mode")) && keyword.length() < 2) {
			// 검색 버튼 요청은 공백을 제외한 검색어가 두 글자 이상일 때만 조회한다.
			req.setAttribute("popupMessage", "검색어를 2자 이상 입력해주세요.");
			keyword = "";
		}
		CertificateIssueData data = certificateService.getIssueData(parseInteger(req.getParameter("employeeId")), keyword);
		String requestedType = trim(req.getParameter("certificateType"));
		String selectedType = normalizeCertificateType(requestedType);
		if (data.getSelectedEmployee() != null) {
			String defaultType = "RETIRED".equals(data.getSelectedEmployee().getStatus()) ? "RETIREMENT" : "WORKING";
			if (requestedType.isEmpty()) {
				selectedType = defaultType;
			} else {
				// 증명서 탭을 선택하는 순간 재직상태와 발급 가능 여부를 확인한다.
				String validationMessage = certificateService.validateIssue(
						data.getSelectedEmployee().getEmployeeId(), selectedType);
				if (validationMessage != null) {
					req.setAttribute("popupMessage", validationMessage);
					selectedType = defaultType;
				}
			}
		}
		req.setAttribute("employees", data.getEmployees());
		req.setAttribute("selectedEmployee", data.getSelectedEmployee());
		req.setAttribute("selectedCertificateType", selectedType);
		req.setAttribute("careers", data.getCareers());
		req.setAttribute("departments", data.getDepartments());
		req.setAttribute("company", data.getCompany());
		req.setAttribute("issueDate", data.getIssueDate());
		req.setAttribute("issueYear", new SimpleDateFormat("yyyy").format(data.getIssueDate()));
		req.setAttribute("issueMonth", new SimpleDateFormat("MM").format(data.getIssueDate()));
		req.setAttribute("issueDay", new SimpleDateFormat("dd").format(data.getIssueDate()));
		if ("issued".equals(req.getParameter("result"))) {
			req.setAttribute("popupMessage", "증명서 발급내역을 저장했습니다.");
		}
		return VIEW;
	}

	private String processSubmit(HttpServletRequest req, HttpServletResponse res) throws IOException {
		Integer employeeId = parseInteger(req.getParameter("employeeId"));
		String certificateType = normalizeCertificateType(req.getParameter("certificateType"));
		String purpose = "DIRECT".equals(req.getParameter("certificateUse"))
				? trim(req.getParameter("certificateUseDirect")) : trim(req.getParameter("certificateUse"));
		Integer issueDepartmentId = parseInteger(req.getParameter("issueDepartmentId"));
		if (employeeId == null || purpose.isEmpty() || issueDepartmentId == null) {
			req.setAttribute("popupMessage", "사원, 발급용도와 발급부서를 모두 선택해주세요.");
			return processForm(req);
		}
		String validationMessage = certificateService.validateIssue(employeeId, certificateType);
		if (validationMessage != null) {
			req.setAttribute("popupMessage", validationMessage);
			return processForm(req);
		}

		CertificateIssuance certificate = new CertificateIssuance();
		certificate.setEmployeeId(employeeId);
		certificate.setCertType(toDatabaseCertificateType(certificateType));
		certificate.setPurpose(purpose);
		certificate.setCertMemo(trim(req.getParameter("certificateMemo")));
		certificate.setIssueDate(parseIssueDate(req));
		certificate.setIssueDeptId(issueDepartmentId);
		// 화면 옵션을 제거했으므로 대표자는 항상 표시하고 주민번호는 항상 숨긴다.
		certificate.setShowCeoYn("Y");
		certificate.setHideJuminYn("Y");
		certificate.setShowLogoYn("Y");
		certificate.setShowStampYn("Y");
		certificateService.issue(certificate);

		String keyword = java.net.URLEncoder.encode(trim(req.getParameter("keyword")), "UTF-8");
		res.sendRedirect(req.getContextPath() + "/employees/certificate.do?employeeId=" + employeeId
				+ "&result=issued&keyword=" + keyword);
		return null;
	}

	private String toDatabaseCertificateType(String type) {
		if ("CAREER".equals(type)) {
			return "경력증명서";
		}
		if ("RETIREMENT".equals(type)) {
			return "퇴직증명서";
		}
		return "재직경력서";
	}

	private String normalizeCertificateType(String type) {
		String value = trim(type);
		if ("CAREER".equals(value) || "RETIREMENT".equals(value)) {
			return value;
		}
		return "WORKING";
	}

	private Date parseIssueDate(HttpServletRequest req) {
		try {
			String value = req.getParameter("issueYear") + "-" + req.getParameter("issueMonth") + "-" + req.getParameter("issueDay");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false);
			return dateFormat.parse(value);
		} catch (Exception e) {
			return new Date();
		}
	}

	private Integer parseInteger(String value) {
		try { return value == null || value.trim().isEmpty() ? null : Integer.valueOf(value); }
		catch (NumberFormatException e) {
			return null;
		}
	}
	private String trim(String value) { return value == null ? "" : value.trim(); }
}
