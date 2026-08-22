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
// 제증명서발급 화면의 HTTP 요청을 구분하고 적절한 서비스 호출과 화면 이동을 담당한다.
// 証明書発行画面のHTTPリクエストを判別し、適切なサービス呼び出しと画面遷移を担当する。
public class CertificateIssueHandler implements CommandHandler {
	private static final String VIEW = "/WEB-INF/view/employees/certificate-issue.jsp";
	private final CertificateIssueService certificateService = new CertificateIssueService();

	@Override
	// 요청 방식과 작업 구분을 확인하여 제증명서발급 조회·저장 작업을 적절한 처리로 연결한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエスト方式と処理区分を確認し、証明書発行の照会・保存処理へ適切に振り分ける。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
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

	// 제증명서발급 화면에 필요한 데이터를 조회하여 request에 저장하고 JSP 경로를 반환한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// 証明書発行画面に必要なデータを照会してrequestへ保存し、JSPパスを返す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	private String processForm(HttpServletRequest req) {
		String keyword = trim(req.getParameter("keyword"));
		if ("search".equals(req.getParameter("mode")) && keyword.length() < 2) {
			// 검색 버튼 요청은 공백을 제외한 검색어가 두 글자 이상일 때만 조회한다.
			// 未入力値とNULLを区別して正規化し、制約違反や不要な値の保存を防止する。
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
				// 入力条件と必須値を検証し、不正なデータが後続処理へ渡らないようにする。
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

	// 제증명서발급 입력 요청을 검증한 뒤 서비스에 저장을 위임하고 처리 결과를 전달한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// 証明書発行の入力リクエストを検証し、サービスへ保存を委譲して処理結果を渡す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
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
		// 画面表示に必要なデータとメッセージをrequestまたはsessionへ保存し、JSPへ引き渡す。
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

	// 입력 데이터를 Database제증명서구분 처리에 필요한 형식으로 변환한다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 入力データをDatabase証明書区分処理に必要な形式へ変換する。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private String toDatabaseCertificateType(String type) {
		if ("CAREER".equals(type)) {
			return "경력증명서";
		}
		if ("RETIREMENT".equals(type)) {
			return "퇴직증명서";
		}
		return "재직경력서";
	}

	// 요청 문자열을 정리하고 정규화제증명서구분 처리에 필요한 안전한 값으로 변환한다.
	// 누락되거나 잘못된 파라미터가 500 오류로 이어지지 않도록 기본값과 형식 검사를 적용한다.
	// リクエスト文字列を整え、正規化証明書区分処理に必要な安全な値へ変換する。
	// 不足または不正なパラメーターが500エラーにつながらないよう、初期値と形式検証を適用する。
	private String normalizeCertificateType(String type) {
		String value = trim(type);
		if ("CAREER".equals(value) || "RETIREMENT".equals(value)) {
			return value;
		}
		return "WORKING";
	}

	// 입력 데이터를 발급일자 처리에 필요한 형식으로 변환한다.
	// 누락되거나 잘못된 파라미터가 500 오류로 이어지지 않도록 기본값과 형식 검사를 적용한다.
	// 入力データを発行日付処理に必要な形式へ変換する。
	// 不足または不正なパラメーターが500エラーにつながらないよう、初期値と形式検証を適用する。
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

	// 입력 데이터를 정수 처리에 필요한 형식으로 변환한다.
	// 누락되거나 잘못된 파라미터가 500 오류로 이어지지 않도록 기본값과 형식 검사를 적용한다.
	// 入力データを整数処理に必要な形式へ変換する。
	// 不足または不正なパラメーターが500エラーにつながらないよう、初期値と形式検証を適用する。
	private Integer parseInteger(String value) {
		try { return value == null || value.trim().isEmpty() ? null : Integer.valueOf(value); }
		catch (NumberFormatException e) {
			return null;
		}
	}
	private String trim(String value) { return value == null ? "" : value.trim(); }
}
