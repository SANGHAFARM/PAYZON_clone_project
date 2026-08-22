package erp.payroll.command;

import java.time.YearMonth;
import java.time.format.DateTimeParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.payroll.dto.PayrollItemLedgerPage;
import erp.payroll.service.PayrollItemLedgerService;
import mvc.command.CommandHandler;

// 항목별 대장 조회 요청을 처리한다.
// 급여항목Ledger 화면의 HTTP 요청을 구분하고 적절한 서비스 호출과 화면 이동을 담당한다.
// 給与項目Ledger画面のHTTPリクエストを判別し、適切なサービス呼び出しと画面遷移を担当する。
public class PayrollItemLedgerHandler implements CommandHandler {

	private static final String VIEW = "/WEB-INF/view/payroll/payroll-item-ledger.jsp";
	private PayrollItemLedgerService ledgerService = new PayrollItemLedgerService();

	@Override
	// 요청 방식과 작업 구분을 확인하여 급여항목Ledger 조회·저장 작업을 적절한 처리로 연결한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエスト方式と処理区分を確認し、給与項目Ledgerの照会・保存処理へ適切に振り分ける。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String uri = req.getRequestURI();
		if (uri.endsWith("/item-ledger.do") && req.getMethod().equalsIgnoreCase("GET")) {
			return processList(req);
		}
		res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		return null;
	}

	// 급여항목Ledger 화면에 필요한 데이터를 조회하여 request에 저장하고 JSP 경로를 반환한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// 給与項目Ledger画面に必要なデータを照会してrequestへ保存し、JSPパスを返す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	private String processList(HttpServletRequest req) {
		YearMonth currentMonth = YearMonth.now();
		String startMonth = validMonth(req.getParameter("startMonth"), currentMonth.withMonth(1).toString());
		String endMonth = validMonth(req.getParameter("endMonth"), currentMonth.toString());
		String itemCode = value(req.getParameter("itemCode"));
		boolean requestedSearch = "search".equals(req.getParameter("mode"));
		if (YearMonth.parse(startMonth).isAfter(YearMonth.parse(endMonth))) {
			startMonth = endMonth;
		}
		if (YearMonth.parse(startMonth).plusMonths(11).isBefore(YearMonth.parse(endMonth))) {
			startMonth = YearMonth.parse(endMonth).minusMonths(11).toString();
		}

		// 조회 버튼을 눌렀지만 항목을 고르지 않은 경우 화면에서 안내한다.
		// 画面表示に必要なデータとメッセージをrequestまたはsessionへ保存し、JSPへ引き渡す。
		if (requestedSearch && itemCode.isEmpty()) {
			req.setAttribute("ledgerPopupMessage", "조회할 항목을 선택해주세요");
		}

		PayrollItemLedgerPage page = ledgerService.getPage(startMonth, endMonth, itemCode);
		req.setAttribute("startMonth", startMonth);
		req.setAttribute("endMonth", endMonth);
		req.setAttribute("selectedItemCode", itemCode);
		req.setAttribute("paymentItems", page.getItems());
		req.setAttribute("ledgerMonths", page.getMonths());
		req.setAttribute("ledgerRows", page.getRows());
		req.setAttribute("ledgerTotals", page.getTotals());
		return VIEW;
	}

	// 입력된 월 또는 날짜가 허용 범위와 형식에 맞는지 검증한다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 入力された月または日付が許容範囲と形式に合っているか検証する。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private String validMonth(String value, String defaultValue) {
		try {
			return YearMonth.parse(value).toString();
		} catch (DateTimeParseException | NullPointerException e) {
			return defaultValue;
		}
	}

	// 요청 문자열을 정리하고 값 처리에 필요한 안전한 값으로 변환한다.
	// 누락되거나 잘못된 파라미터가 500 오류로 이어지지 않도록 기본값과 형식 검사를 적용한다.
	// リクエスト文字列を整え、値処理に必要な安全な値へ変換する。
	// 不足または不正なパラメーターが500エラーにつながらないよう、初期値と形式検証を適用する。
	private String value(String value) {
		return value == null ? "" : value.trim();
	}
}
