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

		try {
			// 파라미터 파싱 및 DTO 세팅
			AttendanceItem item = new AttendanceItem();

			String idStr = req.getParameter("attendItemId");
			if (idStr != null && !idStr.isEmpty()) {
				item.setAttendanceItemId(Integer.parseInt(idStr));
			}

			item.setAttendName(req.getParameter("attendanceName"));
			item.setUnitType(req.getParameter("unitType"));
			item.setWorkHourType(req.getParameter("workHourType"));
			item.setUseYn(req.getParameter("useYn"));

			// 근태그룹 외래키 파싱 처리
			String groupIdStr = req.getParameter("attendanceGroupId");
			if (groupIdStr != null && !groupIdStr.isEmpty()) {
				item.setAttendanceGroupId(Integer.parseInt(groupIdStr));
			}

			// 스키마 컬럼명에 맞춘 휴가공제 식별 번호 파싱 처리
			String deductLeaveIdStr = req.getParameter("leaveItemId");
			if (deductLeaveIdStr != null && !deductLeaveIdStr.isEmpty()) {
				item.setDeductLeaveId(Integer.parseInt(deductLeaveIdStr));
			}

			// 내용 지우기 액션 외에는 서비스 로직 호출
			if (!"clear".equals(action)) {
				settingService.processAttendItemAction(item, action);
				req.getSession().setAttribute("message", "근태항목 설정이 완료되었습니다.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			req.getSession().setAttribute("message", "오류 발생: " + e.getMessage());
		}

		// PRG 패턴 적용 리다이렉트 처리
		res.sendRedirect(req.getContextPath() + "/settings/attendance.do");
		return null;
	}
}