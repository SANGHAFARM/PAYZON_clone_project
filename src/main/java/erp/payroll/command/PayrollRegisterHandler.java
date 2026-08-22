package erp.payroll.command;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.payroll.dto.PayrollRegisterPage.PayrollRegisterDetailPage;
import erp.payroll.dto.PayrollRegisterPage.PayrollRegisterListPage;
import erp.payroll.service.PayrollRegisterService;
import mvc.command.CommandHandler;

// 급여대장의 목록·상세 조회와 삭제 요청을 처리한다.
// 급여등록 화면의 HTTP 요청을 구분하고 적절한 서비스 호출과 화면 이동을 담당한다.
// 給与登録画面のHTTPリクエストを判別し、適切なサービス呼び出しと画面遷移を担当する。
public class PayrollRegisterHandler implements CommandHandler {

	private static final String LIST_VIEW = "/WEB-INF/view/payroll/payroll-register.jsp";
	private static final String DETAIL_VIEW = "/WEB-INF/view/payroll/payroll-register-detail.jsp";
	private PayrollRegisterService registerService = new PayrollRegisterService();

	@Override
	// 요청 방식과 작업 구분을 확인하여 급여등록 조회·저장 작업을 적절한 처리로 연결한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエスト方式と処理区分を確認し、給与登録の照会・保存処理へ適切に振り分ける。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String uri = req.getRequestURI();
		if (uri.endsWith("/register.do") && req.getMethod().equalsIgnoreCase("GET")) {
			return processList(req, res);
		} else if (uri.endsWith("/register/detail.do") && req.getMethod().equalsIgnoreCase("GET")) {
			return processDetail(req, res);
		} else if (uri.endsWith("/register/delete.do") && req.getMethod().equalsIgnoreCase("POST")) {
			return processDelete(req, res);
		}
		res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		return null;
	}

	// 급여등록 화면에 필요한 데이터를 조회하여 request에 저장하고 JSP 경로를 반환한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// 給与登録画面に必要なデータを照会してrequestへ保存し、JSPパスを返す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	private String processList(HttpServletRequest req, HttpServletResponse res) {
		String year = value(req.getParameter("year"), String.valueOf(LocalDate.now().getYear()));
		int pageNumber = intValue(req.getParameter("page"), 0);
		PayrollRegisterListPage page = registerService.getList(year, pageNumber);
		req.setAttribute("paymentYears", makePaymentYears());
		req.setAttribute("selectedYear", Integer.parseInt(year));
		req.setAttribute("paymentRegisters", page.getRegisters());
		req.setAttribute("registerTotals", page.getTotals());
		req.setAttribute("page", page.getPageInfo());
		return LIST_VIEW;
	}

	// 요청에서 상세정보 작업에 필요한 값을 읽고 검증한 뒤 해당 서비스 처리를 호출한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエストから詳細情報処理に必要な値を取得・検証し、該当するサービス処理を呼び出す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	private String processDetail(HttpServletRequest req, HttpServletResponse res) {
		int runId = requiredInt(req.getParameter("registerId"));
		PayrollRegisterDetailPage page = registerService.getDetail(runId, req.getParameter("employmentType"),
				integerValue(req.getParameter("departmentId")), req.getParameter("incomeType"));
		req.setAttribute("register", page.getRegister());
		req.setAttribute("paymentItems", page.getPaymentItems());
		req.setAttribute("deductionItems", page.getDeductionItems());
		req.setAttribute("registerEmployees", page.getEmployees());
		req.setAttribute("registerTotals", page.getTotals());
		req.setAttribute("departments", page.getDepartments());
		req.setAttribute("employmentTypes", registerService.getEmploymentTypes());
		req.setAttribute("selectedEmploymentType", req.getParameter("employmentType"));
		req.setAttribute("selectedDepartmentId", integerValue(req.getParameter("departmentId")));
		req.setAttribute("selectedIncomeType", req.getParameter("incomeType"));
		return DETAIL_VIEW;
	}

	// 요청에서 선택된 급여등록 대상을 확인하고 삭제 결과를 화면에 전달한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエストで選択された給与登録対象を確認し、削除結果を画面へ渡す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	private String processDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String year = value(req.getParameter("year"), String.valueOf(LocalDate.now().getYear()));
		Integer registerId = integerValue(req.getParameter("registerId"));
		if (registerId == null) {
			res.sendRedirect(req.getContextPath() + "/payroll/register.do?year=" + year);
			return null;
		}
		if ("requestDelete".equals(req.getParameter("action"))) {
			// 첫 번째 POST에서는 삭제하지 않고 확인 팝업만 표시한다.
			// HTTPメソッドと処理区分を確認し、照会またはデータ変更に対応する処理へ分岐する。
			req.setAttribute("deleteConfirmation", true);
			req.setAttribute("deleteRegisterId", registerId);
			req.setAttribute("deleteRegisterName", req.getParameter("registerName"));
			return processList(req, res);
		}
		if (!"confirmDelete".equals(req.getParameter("action"))) {
			res.sendRedirect(req.getContextPath() + "/payroll/register.do?year=" + year);
			return null;
		}

		registerService.delete(registerId);
		res.sendRedirect(req.getContextPath() + "/payroll/register.do?year=" + year);
		return null;
	}

	// 조회값과 입력값을 조합하여 지급연도 목록 처리 데이터를 구성한다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 照会値と入力値を組み合わせて支給年度一覧の処理データを構成する。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private List<Integer> makePaymentYears() {
		List<Integer> years = new ArrayList<>();
		int currentYear = LocalDate.now().getYear();
		for (int year = currentYear + 1; year >= currentYear - 5; year--) {
			years.add(year);
		}
		return years;
	}

	// 요청 문자열을 정리하고 필수값정수 처리에 필요한 안전한 값으로 변환한다.
	// 누락되거나 잘못된 파라미터가 500 오류로 이어지지 않도록 기본값과 형식 검사를 적용한다.
	// リクエスト文字列を整え、必須値整数処理に必要な安全な値へ変換する。
	// 不足または不正なパラメーターが500エラーにつながらないよう、初期値と形式検証を適用する。
	private int requiredInt(String value) {
		Integer number = integerValue(value);
		if (number == null) {
			throw new IllegalArgumentException("급여대장을 선택하세요.");
		}
		return number;
	}

	// 요청 문자열을 정리하고 정수값 처리에 필요한 안전한 값으로 변환한다.
	// 누락되거나 잘못된 파라미터가 500 오류로 이어지지 않도록 기본값과 형식 검사를 적용한다.
	// リクエスト文字列を整え、整数値処理に必要な安全な値へ変換する。
	// 不足または不正なパラメーターが500エラーにつながらないよう、初期値と形式検証を適用する。
	private Integer integerValue(String value) {
		try {
			return Integer.valueOf(value);
		} catch (Exception e) {
			return null;
		}
	}

	// 요청 문자열을 정리하고 정수값 처리에 필요한 안전한 값으로 변환한다.
	// 누락되거나 잘못된 파라미터가 500 오류로 이어지지 않도록 기본값과 형식 검사를 적용한다.
	// リクエスト文字列を整え、整数値処理に必要な安全な値へ変換する。
	// 不足または不正なパラメーターが500エラーにつながらないよう、初期値と形式検証を適用する。
	private int intValue(String value, int defaultValue) {
		Integer number = integerValue(value);
		return number == null ? defaultValue : number;
	}

	// 요청 문자열을 정리하고 값 처리에 필요한 안전한 값으로 변환한다.
	// 누락되거나 잘못된 파라미터가 500 오류로 이어지지 않도록 기본값과 형식 검사를 적용한다.
	// リクエスト文字列を整え、値処理に必要な安全な値へ変換する。
	// 不足または不正なパラメーターが500エラーにつながらないよう、初期値と形式検証を適用する。
	private String value(String value, String defaultValue) {
		return value == null || value.trim().isEmpty() ? defaultValue : value;
	}
}
