package erp.settings.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.settings.model.DeductItem;
import erp.settings.service.PayItemSettingService;
import mvc.command.CommandHandler;

public class DeductItemHandler implements CommandHandler {

	// 급여항목 마스터 서비스 객체 할당
	private PayItemSettingService payService = PayItemSettingService.getInstance();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		if (req.getMethod().equalsIgnoreCase("POST")) {
			return processAction(req, res);
		}
		return null; // GET 요청 무시 처리
	}

	// [POST] 공제항목 추가/수정/삭제 액션 폼 데이터 파싱 및 서비스 호출 처리
	private String processAction(HttpServletRequest req, HttpServletResponse res) throws Exception {
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		if ("requestDelete".equals(action)) {
			req.getSession().setAttribute("deleteItemType", "DEDUCT");
			req.getSession().setAttribute("deleteItemId", req.getParameter("deductItemId"));
			res.sendRedirect(req.getContextPath() + "/settings/pay-item.do#deduction-settings");
			return null;
		}

		try {
			DeductItem item = new DeductItem();

			// 기본키 파싱
			String idStr = req.getParameter("deductItemId");
			if (idStr != null && !idStr.isEmpty()) {
				item.setDeductItemId(Integer.parseInt(idStr));
			}

			// 스키마 명명 규칙이 적용된 파라미터 파싱 처리
			item.setDeductName(req.getParameter("deductName"));
			item.setCalcMethod(req.getParameter("calcMethod"));

			String roundStr = req.getParameter("roundUnit");
			if (roundStr != null && !roundStr.isEmpty()) {
				item.setRoundUnit(Integer.parseInt(roundStr));
			}

			item.setNote(req.getParameter("note"));
			item.setUseYn(req.getParameter("useYn"));

			// 내용 지우기 액션 외에는 서비스 로직 호출을 통한 트랜잭션 수행
			if (!"clear".equals(action)) {
				payService.processDeductItemAction(item, action);
				req.getSession().setAttribute("message", "공제항목 설정이 완료되었습니다.");
				req.getSession().setAttribute("messageAnchor", "#deduction-settings");
			}

		} catch (Exception e) {
			e.printStackTrace();
			req.getSession().setAttribute("message", "오류 발생: " + e.getMessage());
			req.getSession().setAttribute("messageAnchor", "#deduction-settings");
		}

		// 처리 후 공제항목 설정 패널 앵커(#deduction-settings)로 리다이렉트 처리
		res.sendRedirect(req.getContextPath() + "/settings/pay-item.do#deduction-settings");
		return null;
	}
}
