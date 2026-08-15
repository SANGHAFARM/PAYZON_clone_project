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

public class PayItemHandler implements CommandHandler {

	// 급여항목 및 근태항목 마스터 서비스 객체 할당 처리
	private PayItemSettingService payService = PayItemSettingService.getInstance();
	private AttendanceSettingService attendService = AttendanceSettingService.getInstance();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		if (req.getMethod().equalsIgnoreCase("GET")) {
			return processForm(req, res);
		} else if (req.getMethod().equalsIgnoreCase("POST")) {
			return processAction(req, res);
		}
		return null;
	}

	// [GET] 급여항목 설정 페이지 전체 화면 렌더링 및 마스터 데이터 조회 처리
	private String processForm(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// 1. 화면의 목록(Table) 및 모달창을 구성할 마스터 데이터 일괄 조회
		List<PayItemRow> paymentItems = payService.getPayItemRows();
		List<DeductItem> deductionItems = payService.getDeductItems();
		List<TaxFreeItem> taxFreeItems = payService.getTaxFreeItems();

		// 근태연결 셀렉트 박스를 구성하기 위한 근태항목 목록 조회
		List<AttendanceItem> attendItems = attendService.getAttendItems();

		req.setAttribute("paymentItems", paymentItems);
		req.setAttribute("deductionItems", deductionItems);
		req.setAttribute("taxFreeItems", taxFreeItems);
		req.setAttribute("attendItems", attendItems);

		// 2. 선택된 지급항목 파라미터 확인 및 단건 조회 (에디터 패널용)
		String payItemIdStr = req.getParameter("payItemId");
		if (payItemIdStr != null && !payItemIdStr.isEmpty()) {
			int payItemId = Integer.parseInt(payItemIdStr);
			PayItem selectedPaymentItem = payService.getPayItem(payItemId);

			// 비과세 모달창에서 비과세 코드를 선택하여 돌아왔을 경우 임시 매핑 처리
			String taxFreeCode = req.getParameter("taxFreeCode");
			if (taxFreeCode != null && !taxFreeCode.isEmpty()) {
				selectedPaymentItem.setTaxType("비과세");
				selectedPaymentItem.setTaxFreeCode(taxFreeCode);
			}

			req.setAttribute("selectedPaymentItem", selectedPaymentItem);
		}

		// 3. 선택된 공제항목 파라미터 확인 및 단건 조회 (에디터 패널용)
		String deductItemIdStr = req.getParameter("deductItemId");
		if (deductItemIdStr != null && !deductItemIdStr.isEmpty()) {
			int deductItemId = Integer.parseInt(deductItemIdStr);
			DeductItem selectedDeductionItem = payService.getDeductItem(deductItemId);
			req.setAttribute("selectedDeductionItem", selectedDeductionItem);
		}

		// JSP 뷰 포워딩 처리
		return "/WEB-INF/view/settings/pay-item-settings.jsp";
	}

	// [POST] 지급항목 추가/수정/삭제 액션 폼 데이터 파싱 및 서비스 호출 처리
	private String processAction(HttpServletRequest req, HttpServletResponse res) throws Exception {
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");

		try {
			PayItem item = new PayItem();

			// 기본키 파싱
			String idStr = req.getParameter("payItemId");
			if (idStr != null && !idStr.isEmpty()) {
				item.setPayItemId(Integer.parseInt(idStr));
			}

			// 스키마 명명 규칙이 적용된 파라미터 파싱 처리
			item.setPayName(req.getParameter("payName"));
			item.setTaxType(req.getParameter("taxType"));
			item.setTaxFreeCode(req.getParameter("taxFreeCode"));

			String limitStr = req.getParameter("taxFreeLimit");
			if (limitStr != null && !limitStr.isEmpty()) {
				item.setTaxFreeLimit(Long.parseLong(limitStr));
			}

			item.setCalcMethod(req.getParameter("calcMethod"));

			String roundStr = req.getParameter("roundUnit");
			if (roundStr != null && !roundStr.isEmpty()) {
				item.setRoundUnit(Integer.parseInt(roundStr));
			}

			item.setUseYn(req.getParameter("useYn"));

			// 근태연결 또는 일괄지급 선택값 파싱 처리
			String linkAttendStr = req.getParameter("linkAttendId");
			if ("BATCH".equals(linkAttendStr)) {
				// 일괄지급 선택 시
				String bulkAmountStr = req.getParameter("bulkPayAmount");
				if (bulkAmountStr != null && !bulkAmountStr.isEmpty()) {
					item.setBulkPayAmount(Long.parseLong(bulkAmountStr));
				}
			} else if (linkAttendStr != null && !linkAttendStr.isEmpty()) {
				// 근태항목 연결 선택 시
				item.setLinkAttendId(Integer.parseInt(linkAttendStr));
			}

			// 내용 지우기 액션 외에는 서비스 로직 호출을 통한 트랜잭션 수행
			if (!"clear".equals(action)) {
				payService.processPayItemAction(item, action);
				req.getSession().setAttribute("message", "지급항목 설정이 완료되었습니다.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			req.getSession().setAttribute("message", "오류 발생: " + e.getMessage());
		}

		// 처리 후 데이터 중복 전송을 막기 위한 PRG(Post-Redirect-Get) 패턴 적용
		res.sendRedirect(req.getContextPath() + "/settings/pay-item.do#payment-settings");
		return null;
	}
}