package erp.retirement.command;

import java.time.LocalDate;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.retirement.service.RetirementPayslipService;
import erp.retirement.service.RetirementPayslipService.PayslipData;
import mvc.command.CommandHandler;

// 지급년도와 사원을 기준으로 퇴직급여명세서를 조회한다.
// 퇴직급여명세서 화면의 HTTP 요청을 구분하고 적절한 서비스 호출과 화면 이동을 담당한다.
// 退職給与明細書画面のHTTPリクエストを判別し、適切なサービス呼び出しと画面遷移を担当する。
public class RetirementPayslipHandler implements CommandHandler {

	private static final String VIEW = "/WEB-INF/view/retirement/retirement-payslip.jsp";
	private final RetirementPayslipService service = new RetirementPayslipService();

	@Override
	// 요청 방식과 작업 구분을 확인하여 퇴직급여명세서 조회·저장 작업을 적절한 처리로 연결한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエスト方式と処理区分を確認し、退職給与明細書の照会・保存処理へ適切に振り分ける。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	public String process(HttpServletRequest req, HttpServletResponse res) {
		if (!"GET".equalsIgnoreCase(req.getMethod())) {
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}

		int year = intValue(req.getParameter("paymentYear"), LocalDate.now().getYear());
		Integer calculationId = parseInt(req.getParameter("calculationId"));
		String keyword = trim(req.getParameter("keyword"));
		boolean searchRequested = req.getParameterMap().containsKey("keyword");
		if (searchRequested && keyword.length() < 2) {
			// 검색어가 짧으면 검색을 적용하지 않고 안내 팝업을 표시한다.
			// 検索語と選択条件を組み合わせ、利用者が指定した対象だけを照会する。
			req.setAttribute("retirementPayslipPopupMessage", "검색어를 2자 이상 입력해주세요.");
			keyword = "";
		}
		PayslipData data = service.getData(year, keyword, calculationId);

		req.setAttribute("paymentYears", service.getPaymentYears());
		req.setAttribute("selectedYear", year);
		req.setAttribute("retirementPayslips", data.getItems());
		req.setAttribute("selectedPayslip", data.getSelected());
		req.setAttribute("company", data.getCompany());

		Calendar today = Calendar.getInstance();
		req.setAttribute("issueYear", today.get(Calendar.YEAR));
		req.setAttribute("issueMonth", today.get(Calendar.MONTH) + 1);
		req.setAttribute("issueDay", today.get(Calendar.DAY_OF_MONTH));
		return VIEW;
	}

	// 입력 데이터를 정수 처리에 필요한 형식으로 변환한다.
	// 누락되거나 잘못된 파라미터가 500 오류로 이어지지 않도록 기본값과 형식 검사를 적용한다.
	// 入力データを整数処理に必要な形式へ変換する。
	// 不足または不正なパラメーターが500エラーにつながらないよう、初期値と形式検証を適用する。
	private Integer parseInt(String value) {
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
		Integer number = parseInt(value);
		return number == null ? defaultValue : number;
	}

	// 요청 문자열을 정리하고 공백제거 처리에 필요한 안전한 값으로 변환한다.
	// 누락되거나 잘못된 파라미터가 500 오류로 이어지지 않도록 기본값과 형식 검사를 적용한다.
	// リクエスト文字列を整え、空白除去処理に必要な安全な値へ変換する。
	// 不足または不正なパラメーターが500エラーにつながらないよう、初期値と形式検証を適用する。
	private String trim(String value) {
		return value == null ? "" : value.trim();
	}
}
