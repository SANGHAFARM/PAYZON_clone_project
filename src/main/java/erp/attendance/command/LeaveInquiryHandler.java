package erp.attendance.command;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.attendance.service.request.LeaveInquiryRequest;
import erp.settings.service.AttendanceSettingService;
import erp.settings.service.DepartmentPositionService;
import mvc.command.CommandHandler;

public class LeaveInquiryHandler implements CommandHandler{
	final String FORM_VIEW = "/WEB-INF/view/attendance/leave-inquiry-test.jsp";
	AttendanceSettingService attendanceSettingService = AttendanceSettingService.getInstance();
	DepartmentPositionService departmentPositionService = DepartmentPositionService.getInstance();
	
	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		if (req.getMethod().equalsIgnoreCase("GET")) {
			return processForm(req, res);
		}  else {
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
	}
	
	private String processForm(HttpServletRequest req, HttpServletResponse res) {
		req.setAttribute("leaveItems", attendanceSettingService.getUsableLeaveItems());
		List<String> empTypes = new ArrayList<>();
		empTypes.add("정규직");
		empTypes.add("계약직");
		empTypes.add("임시직");
		empTypes.add("파견직");
		empTypes.add("위촉직");
		empTypes.add("일용직");
	    req.setAttribute("empTypes", empTypes);
		req.setAttribute("departments", departmentPositionService.getDepartmentOptions());
		req.setAttribute("jobPositions", departmentPositionService.getJobPositionOptions());

		String empType = req.getParameter("empType");
		String keyword = req.getParameter("keyword");
		String status = req.getParameter("status");
		String leaveItemIdStr = req.getParameter("leaveItemId");
		int leaveItemId = Integer.parseInt(leaveItemIdStr);
		String departmentIdStr = req.getParameter("departmentId");
		String jobPositionIdstr = req.getParameter("jobPositionId");
		Integer departmentId = (departmentIdStr!=null&&!departmentIdStr.trim().isEmpty())?Integer.parseInt(departmentIdStr):null;
		Integer jobPositionId = (jobPositionIdstr!=null&&!jobPositionIdstr.trim().isEmpty())?Integer.parseInt(jobPositionIdstr):null;
		LeaveInquiryRequest request = new LeaveInquiryRequest(leaveItemId, keyword,status,empType,departmentId,jobPositionId);
		
		
		
		return FORM_VIEW;

	}
}
