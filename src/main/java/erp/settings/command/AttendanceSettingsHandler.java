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
		if ("true".equals(req.getParameter("cancelEmployeeLeaveDelete"))) {
			req.getSession().removeAttribute("employeeLeaveDeleteIds");
			req.getSession().removeAttribute("employeeLeaveDeleteCount");
		}

		// 1. 마스터 데이터 리스트 일괄 조회
		List<LeaveItem> leaveItems = settingService.getLeaveItems();
		List<AttendanceItem> attendItems = settingService.getAttendItems();
		List<AttendanceGroup> attendGroups = settingService.getAttendGroups();

		req.setAttribute("leaveItems", leaveItems);
		req.setAttribute("attendItems", attendItems);
		req.setAttribute("attendGroups", attendGroups);

		// 삭제 요청은 실제 삭제 전에 확인 팝업에 필요한 정보만 구성한다.
		String deleteType = req.getParameter("deleteType");
		String deleteIdStr = req.getParameter("deleteId");
		if (deleteType != null && deleteIdStr != null && !deleteIdStr.isEmpty()) {
			int deleteId = Integer.parseInt(deleteIdStr);
			req.setAttribute("deleteSettingType", deleteType);
			req.setAttribute("deleteSettingId", deleteId);

			if ("leave".equals(deleteType)) {
				LeaveItem item = settingService.getLeaveItem(deleteId);
				req.setAttribute("deleteSettingName", item == null ? "휴가항목" : item.getItemName());
				req.setAttribute("deleteActionUrl", "/settings/leave-item.do");
				req.setAttribute("deleteReturnHash", "#leave-settings");
			} else if ("attendance".equals(deleteType)) {
				AttendanceItem item = settingService.getAttendItem(deleteId);
				req.setAttribute("deleteSettingName", item == null ? "근태항목" : item.getAttendName());
				req.setAttribute("deleteActionUrl", "/settings/attend-item.do");
				req.setAttribute("deleteReturnHash", "#attendance-item-settings");
			} else if ("group".equals(deleteType)) {
				String groupName = "근태그룹";
				for (AttendanceGroup group : attendGroups) {
					if (group.getAttendanceGroupId() == deleteId) {
						groupName = group.getGroupName();
						break;
					}
				}
				req.setAttribute("deleteSettingName", groupName);
				req.setAttribute("deleteActionUrl", "/settings/attend-group.do");
				req.setAttribute("deleteReturnHash", "#attend-group-modal");
			}
		}

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
