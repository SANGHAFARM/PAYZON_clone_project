package erp.settings.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.settings.model.AttendanceItem;
import erp.settings.service.AttendanceSettingService;
import mvc.command.CommandHandler;

public class AttendanceItemHandler implements CommandHandler {

	// 설정 서비스 객체 할당
	private AttendanceSettingService settingService = AttendanceSettingService.getInstance();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		if (req.getMethod().equalsIgnoreCase("POST")) {
			return processAction(req, res);
		}
		return null; // GET 요청 무시 처리
	}

	// [POST] 근태항목 추가/수정/삭제 액션 처리
	private String processAction(HttpServletRequest req, HttpServletResponse res) throws Exception {
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		String idStr = req.getParameter("attendItemId");

		if ("requestDelete".equals(action)) {
			if (idStr != null && !idStr.isEmpty()) {
				res.sendRedirect(req.getContextPath() + "/settings/attendance.do?deleteType=attendance&deleteId="
						+ idStr + "#attendance-item-settings");
			} else {
				res.sendRedirect(req.getContextPath() + "/settings/attendance.do#attendance-item-settings");
			}
			return null;
		}

		try {
			AttendanceItem item = new AttendanceItem();

			if (idStr != null && !idStr.isEmpty()) {
				item.setAttendanceItemId(Integer.parseInt(idStr));
			}

			item.setAttendName(req.getParameter("attendName"));
			item.setUnitType(req.getParameter("unitType"));
			item.setWorkHourType(req.getParameter("workHourType"));
			item.setUseYn(req.getParameter("useYn"));

			String groupIdStr = req.getParameter("attendanceGroupId");
			if (groupIdStr != null && !groupIdStr.isEmpty()) {
				item.setAttendanceGroupId(Integer.parseInt(groupIdStr));
			}

			// 휴가공제 식별 번호
			String deductLeaveIdStr = req.getParameter("deductLeaveId");
			if (deductLeaveIdStr != null && !deductLeaveIdStr.isEmpty()) {
				item.setDeductLeaveId(Integer.parseInt(deductLeaveIdStr));
			}

			// 내용 지우기 액션 외에는 서비스 로직 호출
			if (!"clear".equals(action)) {
				String serviceAction = "confirmDelete".equals(action) ? "delete" : action;
				settingService.processAttendItemAction(item, serviceAction);
				String message = "insert".equals(serviceAction) ? "근태항목이 추가되었습니다."
						: "update".equals(serviceAction) ? "근태항목이 수정되었습니다."
						: "근태항목이 삭제되었습니다.";
				req.getSession().setAttribute("message", message);
			}

		} catch (Exception e) {
			e.printStackTrace();

			Throwable cause = e.getCause();

			// 중복 에러(SQLIntegrityConstraintViolationException)인지 확인
			if (cause instanceof java.sql.SQLIntegrityConstraintViolationException) {
				req.getSession().setAttribute("message", "이미 추가된 근태항목입니다.");
			} else {
				req.getSession().setAttribute("message", "오류 발생: " + e.getMessage());
			}
		}

		res.sendRedirect(req.getContextPath() + "/settings/attendance.do#attendance-item-settings");
		return null;
	}
}
