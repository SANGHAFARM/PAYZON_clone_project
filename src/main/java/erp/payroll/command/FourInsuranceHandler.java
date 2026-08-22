package erp.payroll.command;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.payroll.dto.FourInsurancePage;
import erp.payroll.service.FourInsuranceService;
import mvc.command.CommandHandler;

// 선택한 급여 차수의 4대보험 공제내역을 조회한다.
// 4대보험보험 화면의 HTTP 요청을 구분하고 적절한 서비스 호출과 화면 이동을 담당한다.
// 四大保険保険画面のHTTPリクエストを判別し、適切なサービス呼び出しと画面遷移を担当する。
public class FourInsuranceHandler implements CommandHandler {

	private static final String VIEW = "/WEB-INF/view/payroll/four-insurance-deduction.jsp";
	private FourInsuranceService insuranceService = new FourInsuranceService();

	@Override
	// 요청 방식과 작업 구분을 확인하여 4대보험보험 조회·저장 작업을 적절한 처리로 연결한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエスト方式と処理区分を確認し、四大保険保険の照会・保存処理へ適切に振り分ける。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	public String process(HttpServletRequest req, HttpServletResponse res) {
		if (!req.getMethod().equalsIgnoreCase("GET")) {
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}

		int currentYear = LocalDate.now().getYear();
		int year = limit(intValue(req.getParameter("year"), currentYear), currentYear - 20, currentYear + 1);
		int month = limit(intValue(req.getParameter("month"), LocalDate.now().getMonthValue()), 1, 12);
		int sequence = limit(intValue(req.getParameter("round"), 1), 1, 10);
		FourInsurancePage page = insuranceService.getPage(String.valueOf(year), twoDigits(month),
				twoDigits(sequence));

		req.setAttribute("paymentYears", makePaymentYears(currentYear));
		req.setAttribute("selectedYear", year);
		req.setAttribute("selectedMonth", month);
		req.setAttribute("selectedRound", sequence);
		req.setAttribute("calculationStart", page.getCalculationStart());
		req.setAttribute("calculationEnd", page.getCalculationEnd());
		req.setAttribute("paymentDate", page.getPaymentDate());
		req.setAttribute("insuranceDeductions", page.getDeductions());
		req.setAttribute("insuranceTotals", page.getTotals());
		return VIEW;
	}

	// 조회값과 입력값을 조합하여 지급연도 목록 처리 데이터를 구성한다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 照会値と入力値を組み合わせて支給年度一覧の処理データを構成する。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private List<Integer> makePaymentYears(int currentYear) {
		List<Integer> years = new ArrayList<>();
		for (int year = currentYear + 1; year >= currentYear - 10; year--) {
			years.add(year);
		}
		return years;
	}

	// 요청 문자열을 정리하고 정수값 처리에 필요한 안전한 값으로 변환한다.
	// 누락되거나 잘못된 파라미터가 500 오류로 이어지지 않도록 기본값과 형식 검사를 적용한다.
	// リクエスト文字列を整え、整数値処理に必要な安全な値へ変換する。
	// 不足または不正なパラメーターが500エラーにつながらないよう、初期値と形式検証を適用する。
	private int intValue(String value, int defaultValue) {
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	// 요청한 조회 건수를 허용 범위로 제한하여 과도한 데이터 조회를 방지한다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 要求された照会件数を許容範囲へ制限し、過剰なデータ照会を防止する。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private int limit(int value, int minimum, int maximum) {
		return Math.min(Math.max(value, minimum), maximum);
	}

	// 요청 문자열을 정리하고 두자리 처리에 필요한 안전한 값으로 변환한다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// リクエスト文字列を整え、二桁処理に必要な安全な値へ変換する。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private String twoDigits(int value) {
		return String.format("%02d", value);
	}
}
