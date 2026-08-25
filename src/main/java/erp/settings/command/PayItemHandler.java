package erp.settings.command;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.settings.dto.PayItemRow;
import erp.settings.model.AttendanceItem;
import erp.settings.model.DeductItem;
import erp.settings.model.PayItem;
import erp.settings.model.TaxFreeItem;
import erp.settings.service.AttendanceSettingService;
import erp.settings.service.PayItemSettingService;
import mvc.command.CommandHandler;

// 지급항목 화면의 HTTP 요청을 구분하고 적절한 서비스 호출과 화면 이동을 담당한다.
// 支給項目画面のHTTPリクエストを判別し、適切なサービス呼び出しと画面遷移を担当する。
public class PayItemHandler implements CommandHandler {

	// 급여항목 및 근태항목 마스터 서비스 객체 할당 처리
	// 支給額・控除額・期間値を業務基準に従って計算し、合計と最終金額へ反映する。
	private PayItemSettingService payService = PayItemSettingService.getInstance();
	private AttendanceSettingService attendService = AttendanceSettingService.getInstance();

	@Override
	// 요청 방식과 작업 구분을 확인하여 지급항목 조회·저장 작업을 적절한 처리로 연결한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエスト方式と処理区分を確認し、支給項目の照会・保存処理へ適切に振り分ける。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		if (req.getMethod().equalsIgnoreCase("GET")) {
			return processForm(req, res);
		} else if (req.getMethod().equalsIgnoreCase("POST")) {
			return processAction(req, res);
		}
		return null;
	}

	// [GET] 급여항목 설정 페이지 전체 화면 렌더링 및 마스터 데이터 조회 처리
	// 지급항목 화면에 필요한 데이터를 조회하여 request에 저장하고 JSP 경로를 반환한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// 支給項目画面に必要なデータを照会してrequestへ保存し、JSPパスを返す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	private String processForm(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// 1. 화면의 목록(Table) 및 모달창을 구성할 마스터 데이터 일괄 조회
		// 画面表示に必要なデータとメッセージをrequestまたはsessionへ保存し、JSPへ引き渡す。
		List<PayItemRow> paymentItems = payService.getPayItemRows();
		List<DeductItem> deductionItems = payService.getDeductItems();
		List<TaxFreeItem> taxFreeItems = payService.getTaxFreeItems();

		// 근태연결 셀렉트 박스를 구성하기 위한 근태항목 목록 조회
		// 対象一覧を順番に処理し、各行の入力値または照会結果を同じ基準で構成する。
		List<AttendanceItem> attendItems = attendService.getAttendItems();

		req.setAttribute("paymentItems", paymentItems);
		req.setAttribute("deductionItems", deductionItems);
		req.setAttribute("taxFreeItems", taxFreeItems);
		req.setAttribute("attendItems", attendItems);

		// 2. 선택된 지급항목 파라미터 확인 및 단건 조회 (에디터 패널용)
		// リクエストから入力値を取得し、空白除去と形式変換を行って業務処理へ渡す。
		String payItemIdStr = req.getParameter("payItemId");
		PayItem selectedPaymentItem = null;
		if (payItemIdStr != null && !payItemIdStr.isEmpty()) {
			int payItemId = Integer.parseInt(payItemIdStr);
			selectedPaymentItem = payService.getPayItem(payItemId);
		}

		// 비과세 목록에서 선택한 경우 입력 중인 폼 값과 법정 한도액을 함께 복원한다.
		// 保険・税区分と適用基準を確認し、対象金額に合う控除または非課税処理を適用する。
		String selectedTaxFreeCode = req.getParameter("selectedTaxFreeCode");
		if (selectedTaxFreeCode != null && !selectedTaxFreeCode.isEmpty()) {
			selectedPaymentItem = parsePayItem(req);
			if ("DIRECT".equals(selectedTaxFreeCode)) {
				selectedPaymentItem.setTaxType("비과세");
				selectedPaymentItem.setTaxFreeCode("DIRECT");
				selectedPaymentItem.setTaxFreeName("直接入力");
			} else {
				TaxFreeItem taxFreeItem = payService.getTaxFreeItem(selectedTaxFreeCode);
				if (taxFreeItem != null) {
					selectedPaymentItem.setTaxType("비과세");
					selectedPaymentItem.setTaxFreeCode(taxFreeItem.getTaxFreeCode());
					selectedPaymentItem.setTaxFreeName(taxFreeItem.getTaxFreeName());
					selectedPaymentItem.setTaxFreeLimit(taxFreeItem.getDefaultLimit());
				}
			}
		}
		req.setAttribute("selectedPaymentItem", selectedPaymentItem);

		// 3. 선택된 공제항목 파라미터 확인 및 단건 조회 (에디터 패널용)
		// リクエストから入力値を取得し、空白除去と形式変換を行って業務処理へ渡す。
		String deductItemIdStr = req.getParameter("deductItemId");
		if (deductItemIdStr != null && !deductItemIdStr.isEmpty()) {
			int deductItemId = Integer.parseInt(deductItemIdStr);
			DeductItem selectedDeductionItem = payService.getDeductItem(deductItemId);
			req.setAttribute("selectedDeductionItem", selectedDeductionItem);
		}

		// JSP 뷰 포워딩 처리
		// 画面表示に必要なデータとメッセージをrequestまたはsessionへ保存し、JSPへ引き渡す。
		return "/WEB-INF/view/settings/pay-item-settings.jsp";
	}

	// [POST] 지급항목 추가/수정/삭제 액션 폼 데이터 파싱 및 서비스 호출 처리
	// 요청에서 작업구분 작업에 필요한 값을 읽고 검증한 뒤 해당 서비스 처리를 호출한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエストから処理区分処理に必要な値を取得・検証し、該当するサービス処理を呼び出す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	private String processAction(HttpServletRequest req, HttpServletResponse res) throws Exception {
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		if ("requestDelete".equals(action)) {
			String payItemIdValue = req.getParameter("payItemId");
			if (payItemIdValue == null || payItemIdValue.trim().isEmpty()) {
				req.getSession().setAttribute("message", "削除する支給項目を選択してください。");
				req.getSession().setAttribute("messageAnchor", "#payment-settings");
				res.sendRedirect(req.getContextPath() + "/settings/pay-item.do#payment-settings");
				return null;
			}
			int payItemId = Integer.parseInt(payItemIdValue);
			if (payService.isRequiredPayItem(payItemId)) {
				req.getSession().setAttribute("message", "必須支給項目は修正または削除できません。");
				req.getSession().setAttribute("messageAnchor", "#payment-settings");
				res.sendRedirect(req.getContextPath() + "/settings/pay-item.do#payment-settings");
				return null;
			}
			req.getSession().setAttribute("deleteItemType", "PAY");
			req.getSession().setAttribute("deleteItemId", req.getParameter("payItemId"));
			res.sendRedirect(req.getContextPath() + "/settings/pay-item.do#payment-settings");
			return null;
		}

		try {
			PayItem item = parsePayItem(req);

			// 내용 지우기 액션 외에는 서비스 로직 호출을 통한 트랜잭션 수행
			// 複数のデータ変更を一つのトランザクションとして処理し、成功時はコミット、失敗時はロールバックする。
			if (!"clear".equals(action)) {
				payService.processPayItemAction(item, action);
				req.getSession().setAttribute("message", "지급항목 설정이 완료되었습니다.");
				req.getSession().setAttribute("messageAnchor", "#payment-settings");
			}

		} catch (Exception e) {
			e.printStackTrace();
			String message = e instanceof IllegalArgumentException
					? e.getMessage() : "エラー: " + e.getMessage();
			req.getSession().setAttribute("message", message);
			req.getSession().setAttribute("messageAnchor", "#payment-settings");
		}

		// 처리 후 데이터 중복 전송을 막기 위한 PRG(Post-Redirect-Get) 패턴 적용
		// 重複値とデータベース制約違反を確認し、保存可能なデータだけを処理する。
		res.sendRedirect(req.getContextPath() + "/settings/pay-item.do#payment-settings");
		return null;
	}

	// 지급항목 입력값을 화면 재표시와 저장 처리에서 공통으로 사용한다.
	// 입력 데이터를 지급항목 처리에 필요한 형식으로 변환한다.
	// 누락되거나 잘못된 파라미터가 500 오류로 이어지지 않도록 기본값과 형식 검사를 적용한다.
	// 入力データを支給項目処理に必要な形式へ変換する。
	// 不足または不正なパラメーターが500エラーにつながらないよう、初期値と形式検証を適用する。
	private PayItem parsePayItem(HttpServletRequest req) {
		PayItem item = new PayItem();
		String idStr = req.getParameter("payItemId");
		if (idStr != null && !idStr.isEmpty()) {
			item.setPayItemId(Integer.parseInt(idStr));
		}

		item.setPayName(req.getParameter("payName"));
		item.setTaxType(req.getParameter("taxType"));
		item.setTaxFreeCode(req.getParameter("taxFreeCode"));
		item.setTaxFreeName(req.getParameter("taxFreeName"));
		item.setDirectTaxFreeName(req.getParameter("directTaxFreeName"));
		item.setCalcMethod(req.getParameter("calcMethod"));
		item.setUseYn(req.getParameter("useYn"));

		String limitStr = req.getParameter("taxFreeLimit");
		if (limitStr != null && !limitStr.isEmpty()) {
			item.setTaxFreeLimit(Long.parseLong(limitStr));
		}
		String directLimitStr = req.getParameter("directTaxFreeLimit");
		if (directLimitStr != null && !directLimitStr.isEmpty()) {
			item.setDirectTaxFreeLimit(Long.parseLong(directLimitStr));
		}
		String roundStr = req.getParameter("roundUnit");
		if (roundStr != null && !roundStr.isEmpty()) {
			item.setRoundUnit(Integer.parseInt(roundStr));
		}

		String linkAttendStr = req.getParameter("linkAttendId");
		if ("BATCH".equals(linkAttendStr)) {
			item.setPayMethod("일괄지급");
			String bulkAmountStr = req.getParameter("bulkPayAmount");
			if (bulkAmountStr != null && !bulkAmountStr.isEmpty()) {
				item.setBulkPayAmount(Long.parseLong(bulkAmountStr));
			}
		} else if (linkAttendStr != null && !linkAttendStr.isEmpty()) {
			item.setLinkAttendId(Integer.parseInt(linkAttendStr));
		}
		return item;
	}
}
