package erp.payroll.command;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.payroll.dto.PayrollPayslipPage;
import erp.payroll.service.PayrollPayslipService;
import mvc.command.CommandHandler;

// 급여명세서의 급여 회차와 사원 선택 요청을 처리한다.
// 급여명세서 화면의 HTTP 요청을 구분하고 적절한 서비스 호출과 화면 이동을 담당한다.
// 給与明細書画面のHTTPリクエストを判別し、適切なサービス呼び出しと画面遷移を担当する。
public class PayrollPayslipHandler implements CommandHandler {

	private static final String VIEW = "/WEB-INF/view/payroll/payroll-payslip.jsp";
	private PayrollPayslipService payslipService = new PayrollPayslipService();

	@Override
	// 요청 방식과 작업 구분을 확인하여 급여명세서 조회·저장 작업을 적절한 처리로 연결한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエスト方式と処理区分を確認し、給与明細書の照会・保存処理へ適切に振り分ける。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	public String process(HttpServletRequest req, HttpServletResponse res) {
		if (!req.getMethod().equalsIgnoreCase("GET")) {
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
		String year = value(req.getParameter("paymentYear"), String.valueOf(LocalDate.now().getYear()));
		String month = value(req.getParameter("paymentMonth"), String.valueOf(LocalDate.now().getMonthValue()));
		String sequence = value(req.getParameter("paymentRound"), "1");
		String keyword = trim(req.getParameter("keyword"));
		if ("search".equals(req.getParameter("mode")) && keyword.length() < 2) {
			// 검색 버튼은 두 글자 이상의 검색어가 있을 때만 조건 조회한다.
			// 検索語と選択条件を組み合わせ、利用者が指定した対象だけを照会する。
			req.setAttribute("payslipPopupMessage", "검색어를 2자 이상 입력해주세요");
			keyword = "";
		}
		PayrollPayslipPage page = payslipService.getPage(year, twoDigits(month), twoDigits(sequence),
				integerValue(req.getParameter("employeeId")), keyword);
		req.setAttribute("paymentYears", makePaymentYears());
		req.setAttribute("selectedYear", Integer.parseInt(year));
		req.setAttribute("selectedMonth", Integer.parseInt(month));
		req.setAttribute("selectedRound", Integer.parseInt(sequence));
		req.setAttribute("calculationStart", page.getCalculationStart());
		req.setAttribute("calculationEnd", page.getCalculationEnd());
		req.setAttribute("paymentDate", page.getPaymentDate());
		req.setAttribute("payslipEmployees", page.getEmployees());
		req.setAttribute("selectedEmployee", page.getSelectedEmployee());
		req.setAttribute("paymentItems", page.getPaymentItems());
		req.setAttribute("deductionItems", page.getDeductionItems());
		req.setAttribute("company", page.getCompany());
		return VIEW;
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

	// 요청 문자열을 정리하고 두자리 처리에 필요한 안전한 값으로 변환한다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// リクエスト文字列を整え、二桁処理に必要な安全な値へ変換する。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private String twoDigits(String value) {
		return String.format("%02d", Integer.parseInt(value));
	}

	// 요청 문자열을 정리하고 값 처리에 필요한 안전한 값으로 변환한다.
	// 누락되거나 잘못된 파라미터가 500 오류로 이어지지 않도록 기본값과 형식 검사를 적용한다.
	// リクエスト文字列を整え、値処理に必要な安全な値へ変換する。
	// 不足または不正なパラメーターが500エラーにつながらないよう、初期値と形式検証を適用する。
	private String value(String value, String defaultValue) {
		return value == null || value.trim().isEmpty() ? defaultValue : value;
	}

	// 요청 문자열을 정리하고 공백제거 처리에 필요한 안전한 값으로 변환한다.
	// 누락되거나 잘못된 파라미터가 500 오류로 이어지지 않도록 기본값과 형식 검사를 적용한다.
	// リクエスト文字列を整え、空白除去処理に必要な安全な値へ変換する。
	// 不足または不正なパラメーターが500エラーにつながらないよう、初期値と形式検証を適用する。
	private String trim(String value) {
		return value == null ? "" : value.trim();
	}
}
