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

// 제증명서 발급대장의 조회와 삭제 요청을 처리한다.
// 제증명서등록 화면의 HTTP 요청을 구분하고 적절한 서비스 호출과 화면 이동을 담당한다.
// 証明書登録画面のHTTPリクエストを判別し、適切なサービス呼び出しと画面遷移を担当する。
public class CertificateRegisterHandler implements CommandHandler {
	private static final String VIEW = "/WEB-INF/view/employees/certificate-register.jsp";
	private final CertificateRegisterService registerService = new CertificateRegisterService();

	@Override
	// 요청 방식과 작업 구분을 확인하여 제증명서등록 조회·저장 작업을 적절한 처리로 연결한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエスト方式と処理区分を確認し、証明書登録の照会・保存処理へ適切に振り分ける。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
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

	// 제증명서등록 화면에 필요한 데이터를 조회하여 request에 저장하고 JSP 경로를 반환한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// 証明書登録画面に必要なデータを照会してrequestへ保存し、JSPパスを返す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	private String processList(HttpServletRequest req) {
		CertificateRegisterCondition condition = createCondition(req);
		if ("search".equals(req.getParameter("mode")) && !hasSearchCondition(condition)) {
			// 조건 검색은 증명서 종류, 발급일 또는 검색어 중 하나 이상을 입력해야 한다.
			// 検索語と選択条件を組み合わせ、利用者が指定した対象だけを照会する。
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

	// 요청에서 선택된 제증명서등록 대상을 확인하고 삭제 결과를 화면에 전달한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエストで選択された証明書登録対象を確認し、削除結果を画面へ渡す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	private String processDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String action = req.getParameter("deleteAction");
		String[] certificateIds = req.getParameterValues("certificateIds");
		if ("requestSelected".equals(action) && (certificateIds == null || certificateIds.length == 0)) {
			res.sendRedirect(req.getContextPath() + "/employees/certificate-register.do?result=notSelected");
			return null;
		}
		if ("requestSelected".equals(action) || "requestAll".equals(action)) {
			// 첫 번째 POST에서는 삭제하지 않고 확인창에 삭제 범위만 전달한다.
			// HTTPメソッドと処理区分を確認し、照会またはデータ変更に対応する処理へ分岐する。
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

	// 검색검색조건 조건의 충족 여부를 확인하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 検索検索条件条件を満たしているか確認して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	private boolean hasSearchCondition(CertificateRegisterCondition condition) {
		return !condition.getCertificateType().isEmpty() || !condition.getIssueDateFrom().isEmpty()
				|| !condition.getIssueDateTo().isEmpty() || !condition.getKeyword().isEmpty();
	}

	// 제증명서등록 처리에 사용할 검색조건 데이터나 객체를 생성한다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 証明書登録処理で使用する検索条件データまたはオブジェクトを生成する。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
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
	// 입력된 월 또는 날짜가 허용 범위와 형식에 맞는지 검증한다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 入力された月または日付が許容範囲と形式に合っているか検証する。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
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
	// 입력 데이터를 Positive정수 처리에 필요한 형식으로 변환한다.
	// 누락되거나 잘못된 파라미터가 500 오류로 이어지지 않도록 기본값과 형식 검사를 적용한다.
	// 入力データをPositive整数処理に必要な形式へ変換する。
	// 不足または不正なパラメーターが500エラーにつながらないよう、初期値と形式検証を適用する。
	private int parsePositiveInt(String value, int defaultValue) {
		try { int parsed = Integer.parseInt(value); return parsed > 0 ? parsed : defaultValue; }
		catch (Exception e) {
			return defaultValue;
		}
	}
}
