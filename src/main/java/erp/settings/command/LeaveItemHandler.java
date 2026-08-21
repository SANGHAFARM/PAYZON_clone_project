package erp.settings.command;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.attendance.dto.EmployeeLeaveRow;
import erp.attendance.service.EmployeeLeaveBalanceService;
import erp.settings.model.AttendanceGroup;
import erp.settings.model.AttendanceItem;
import erp.settings.model.LeaveItem;
import erp.settings.service.AttendanceSettingService;
import mvc.command.CommandHandler;

public class LeaveItemHandler implements CommandHandler {

	// 서비스 객체 할당 처리
	private AttendanceSettingService settingService = AttendanceSettingService.getInstance();
	private EmployeeLeaveBalanceService leaveBalanceService = EmployeeLeaveBalanceService.getInstance();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		if (req.getMethod().equalsIgnoreCase("POST")) {
			return processAction(req, res);
		}
		return null;
	}

	// [POST] 휴가항목 추가/수정/삭제 액션 분기 처리
	private String processAction(HttpServletRequest req, HttpServletResponse res) throws Exception {
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		String idStr = req.getParameter("leaveItemId");

		if ("requestDelete".equals(action)) {
			if (idStr != null && !idStr.isEmpty()) {
				res.sendRedirect(req.getContextPath() + "/settings/attendance.do?deleteType=leave&deleteId="
						+ idStr + "#leave-settings");
			} else {
				res.sendRedirect(req.getContextPath() + "/settings/attendance.do#leave-settings");
			}
			return null;
		}

		try {
			// 파라미터 파싱 및 DTO 세팅
			LeaveItem item = new LeaveItem();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			if (idStr != null && !idStr.isEmpty()) {
				item.setLeaveItemId(Integer.parseInt(idStr));
			}

			item.setItemName(req.getParameter("itemName"));
			item.setUseYn(req.getParameter("useYn"));

			String startDate = req.getParameter("applyStartDate");
			if (startDate != null && !startDate.isEmpty()) {
				item.setApplyStartDate(sdf.parse(startDate));
			}

			String endDate = req.getParameter("applyEndDate");
			if (endDate != null && !endDate.isEmpty()) {
				item.setApplyEndDate(sdf.parse(endDate));
			}

			// 내용 지우기 액션 외에는 서비스 로직 호출
			if (!"clear".equals(action)) {
				String serviceAction = "confirmDelete".equals(action) ? "delete" : action;
				settingService.processLeaveItemAction(item, serviceAction);
				String message = "insert".equals(serviceAction) ? "휴가항목이 추가되었습니다."
						: "update".equals(serviceAction) ? "휴가항목이 수정되었습니다."
						: "휴가항목이 삭제되었습니다.";
				req.getSession().setAttribute("message", message);
			}

		} catch (Exception e) {
			e.printStackTrace();

			Throwable cause = e.getCause();

			// 중복 에러(SQLIntegrityConstraintViolationException)인지 확인
			if (cause instanceof java.sql.SQLIntegrityConstraintViolationException) {
				req.getSession().setAttribute("message", "이미 등록된 휴가항목입니다. 다른 이름이나 날짜를 선택해 주세요.");
			} else {
				req.getSession().setAttribute("message", "오류 발생: " + e.getMessage());
			}
		}

		// PRG 패턴 적용 리다이렉트 처리
		res.sendRedirect(req.getContextPath() + "/settings/attendance.do#leave-settings");
		return null;
	}
}
