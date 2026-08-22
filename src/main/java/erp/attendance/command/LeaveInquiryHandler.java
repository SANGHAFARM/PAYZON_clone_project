package erp.attendance.command;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.attendance.dto.AttendanceRecordDto;
import erp.attendance.dto.LeaveInquiryDto;
import erp.attendance.service.LeaveInquiryService;
import erp.attendance.service.LeaveRecordInquiryService;
import erp.attendance.service.request.LeaveInquiryRequest;
import erp.settings.model.LeaveItem;
import erp.settings.service.AttendanceSettingService;
import erp.settings.service.DepartmentPositionService;
import mvc.command.CommandHandler;

public class LeaveInquiryHandler implements CommandHandler {
	final String FORM_VIEW = "/WEB-INF/view/attendance/leave-inquiry.jsp";
	AttendanceSettingService attendanceSettingService = AttendanceSettingService.getInstance();
	DepartmentPositionService departmentPositionService = DepartmentPositionService.getInstance();
	LeaveInquiryService leaveInquiryService = new LeaveInquiryService();
	LeaveRecordInquiryService leaveRecordInquiryService = new LeaveRecordInquiryService();
	
	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		if (req.getMethod().equalsIgnoreCase("GET")) {
			return processForm(req, res);
		} else {
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
	}

	private String processForm(HttpServletRequest req, HttpServletResponse res) throws Exception {
		List<LeaveItem> leaveItems = attendanceSettingService.getUsableLeaveItems();
		req.setAttribute("leaveItems", leaveItems);
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
		if (status == null) {
		    status = "재직";
		}
		String leaveItemIdStr = req.getParameter("leaveItemId");
		int leaveItemId = 0;
		if (leaveItemIdStr != null && !leaveItemIdStr.trim().isEmpty()) {
			leaveItemId = Integer.parseInt(leaveItemIdStr);
		} else {
			leaveItemId = leaveItems.get(0).getLeaveItemId();
		}
		String departmentIdStr = req.getParameter("departmentId");
		String jobPositionIdstr = req.getParameter("jobPositionId");
		Integer departmentId = (departmentIdStr != null && !departmentIdStr.trim().isEmpty())
				? Integer.parseInt(departmentIdStr)
				: null;
		Integer jobPositionId = (jobPositionIdstr != null && !jobPositionIdstr.trim().isEmpty())
				? Integer.parseInt(jobPositionIdstr)
				: null;
		LeaveInquiryRequest request = new LeaveInquiryRequest(leaveItemId, keyword, status, empType, departmentId,
				jobPositionId);
		List<LeaveInquiryDto> leaveEmployees = leaveInquiryService.getLeaveEmployees(request);
		req.setAttribute("leaveEmployees", leaveEmployees);

		req.setAttribute("empType", empType);
		req.setAttribute("keyword", keyword);
		req.setAttribute("status", status);
		req.setAttribute("leaveItemId", leaveItemId);
		req.setAttribute("departmentId", departmentId);
		req.setAttribute("jobPositionId", jobPositionId);

		// 사원별 휴가현황 모달용 - employeeId 파라미터가 있을 때만 처리
		String employeeIdStr = req.getParameter("employeeId");
		if (employeeIdStr != null && !employeeIdStr.trim().isEmpty()) {
			int employeeId = Integer.parseInt(employeeIdStr);
			req.setAttribute("employeeId", employeeId);

			// leaveEmployees 목록에서 해당 사원 찾기
			LeaveInquiryDto selectedEmployee = null;
			for (LeaveInquiryDto dto : leaveEmployees) {
				if (dto.getEmployeeId() == employeeId) {
					selectedEmployee = dto;
					break;
				}
			}
			req.setAttribute("selectedEmployee", selectedEmployee);

			// 상세 사용내역 조회
			List<AttendanceRecordDto> leaveRecords = leaveRecordInquiryService.getLeaveRecords(employeeId, leaveItemId);
			req.setAttribute("leaveRecords", leaveRecords);
		}

		return FORM_VIEW;
	}
}
