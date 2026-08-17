package erp.settings.command;

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

public class AttendanceSettingsHandler implements CommandHandler {

	private AttendanceSettingService settingService = AttendanceSettingService.getInstance();
	private EmployeeLeaveBalanceService leaveBalanceService = EmployeeLeaveBalanceService.getInstance();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// 이 핸들러는 화면을 보여주는 용도(GET)로만 작동

		// 1. 마스터 데이터 리스트 일괄 조회
		List<LeaveItem> leaveItems = settingService.getLeaveItems();
		List<AttendanceItem> attendItems = settingService.getAttendItems();
		List<AttendanceGroup> attendGroups = settingService.getAttendGroups();

		req.setAttribute("leaveItems", leaveItems);
		req.setAttribute("attendItems", attendItems);
		req.setAttribute("attendGroups", attendGroups);

		// 2. 선택된 휴가항목 단건 조회 (수정 폼용)
		String leaveItemIdStr = req.getParameter("leaveItemId");
		if (leaveItemIdStr != null && !leaveItemIdStr.isEmpty()) {
			int leaveItemId = Integer.parseInt(leaveItemIdStr);
			LeaveItem selectedLeaveItem = settingService.getLeaveItem(leaveItemId);
			req.setAttribute("selectedLeaveItem", selectedLeaveItem);

			// 모달용 사원별 휴가일수 목록 동시 조회
			String keyword = req.getParameter("keyword");
			String status = req.getParameter("status");
			List<EmployeeLeaveRow> employeeLeaveRows = leaveBalanceService.getEmployeeLeaveRows(leaveItemId, keyword,
					status);
			req.setAttribute("employeeLeaveRows", employeeLeaveRows);
		}

		// 3. 선택된 근태항목 단건 조회 (수정 폼용)
		String attendItemIdStr = req.getParameter("attendItemId");
		if (attendItemIdStr != null && !attendItemIdStr.isEmpty()) {
			int attendItemId = Integer.parseInt(attendItemIdStr);
			AttendanceItem selectedAttendItem = settingService.getAttendItem(attendItemId);
			req.setAttribute("selectedAttendItem", selectedAttendItem);
		}

		// 4. JSP 뷰 포워딩
		return "/WEB-INF/view/settings/attendance-settings.jsp";
	}
}