package erp.settings.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.settings.model.DeductItem;
import erp.settings.service.PayItemSettingService;
import mvc.command.CommandHandler;

// 공제항목 화면의 HTTP 요청을 구분하고 적절한 서비스 호출과 화면 이동을 담당한다.
// 控除項目画面のHTTPリクエストを判別し、適切なサービス呼び出しと画面遷移を担当する。
public class DeductItemHandler implements CommandHandler {

	// 급여항목 마스터 서비스 객체 할당
	// 支給額・控除額・期間値を業務基準に従って計算し、合計と最終金額へ反映する。
	private PayItemSettingService payService = PayItemSettingService.getInstance();

	@Override
	// 요청 방식과 작업 구분을 확인하여 공제항목 조회·저장 작업을 적절한 처리로 연결한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエスト方式と処理区分を確認し、控除項目の照会・保存処理へ適切に振り分ける。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		if (req.getMethod().equalsIgnoreCase("POST")) {
			return processAction(req, res);
		}
		return null; // GET 요청 무시 처리
	}

	// [POST] 공제항목 추가/수정/삭제 액션 폼 데이터 파싱 및 서비스 호출 처리
	// 요청에서 작업구분 작업에 필요한 값을 읽고 검증한 뒤 해당 서비스 처리를 호출한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエストから処理区分処理に必要な値を取得・検証し、該当するサービス処理を呼び出す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	private String processAction(HttpServletRequest req, HttpServletResponse res) throws Exception {
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		if ("requestDelete".equals(action)) {
			String deductItemIdValue = req.getParameter("deductItemId");
			if (deductItemIdValue == null || deductItemIdValue.trim().isEmpty()) {
				req.getSession().setAttribute("message", "削除する控除項目を選択してください。");
				req.getSession().setAttribute("messageAnchor", "#deduction-settings");
				res.sendRedirect(req.getContextPath() + "/settings/pay-item.do#deduction-settings");
				return null;
			}
			int deductItemId = Integer.parseInt(deductItemIdValue);
			if (payService.isRequiredDeductItem(deductItemId)) {
				req.getSession().setAttribute("message", "必須控除項目は修正または削除できません。");
				req.getSession().setAttribute("messageAnchor", "#deduction-settings");
				res.sendRedirect(req.getContextPath() + "/settings/pay-item.do#deduction-settings");
				return null;
			}
			req.getSession().setAttribute("deleteItemType", "DEDUCT");
			req.getSession().setAttribute("deleteItemId", req.getParameter("deductItemId"));
			res.sendRedirect(req.getContextPath() + "/settings/pay-item.do#deduction-settings");
			return null;
		}

		try {
			DeductItem item = new DeductItem();

			// 기본키 파싱
			// データを一意に識別するキーを発行または適用し、対象レコードを正確に処理する。
			String idStr = req.getParameter("deductItemId");
			if (idStr != null && !idStr.isEmpty()) {
				item.setDeductItemId(Integer.parseInt(idStr));
			}

			// 스키마 명명 규칙이 적용된 파라미터 파싱 처리
			// リクエストから入力値を取得し、空白除去と形式変換を行って業務処理へ渡す。
			item.setDeductName(req.getParameter("deductName"));
			item.setCalcMethod(req.getParameter("calcMethod"));

			String roundStr = req.getParameter("roundUnit");
			if (roundStr != null && !roundStr.isEmpty()) {
				item.setRoundUnit(Integer.parseInt(roundStr));
			}

			item.setNote(req.getParameter("note"));
			item.setUseYn(req.getParameter("useYn"));

			// 내용 지우기 액션 외에는 서비스 로직 호출을 통한 트랜잭션 수행
			// 複数のデータ変更を一つのトランザクションとして処理し、成功時はコミット、失敗時はロールバックする。
			if (!"clear".equals(action)) {
				payService.processDeductItemAction(item, action);
				if ("insert".equals(action)) {
					req.getSession().setAttribute("message", "공제항목이 추가되었습니다.");
				} else if ("update".equals(action)) {
					req.getSession().setAttribute("message", "공제항목이 수정되었습니다.");
				} else if ("delete".equals(action)) {
					req.getSession().setAttribute("message", "공제항목이 삭제되었습니다.");
				}
				req.getSession().setAttribute("messageAnchor", "#deduction-settings");
			}

		} catch (Exception e) {
			e.printStackTrace();
			String message = e instanceof IllegalArgumentException
					? e.getMessage() : "エラー: " + e.getMessage();
			req.getSession().setAttribute("message", message);
			req.getSession().setAttribute("messageAnchor", "#deduction-settings");
		}

		// 처리 후 공제항목 설정 패널 앵커(#deduction-settings)로 리다이렉트 처리
		// 処理結果に応じて表示対象のJSPまたは次のリクエスト経路へ遷移する。
		res.sendRedirect(req.getContextPath() + "/settings/pay-item.do#deduction-settings");
		return null;
	}
}
