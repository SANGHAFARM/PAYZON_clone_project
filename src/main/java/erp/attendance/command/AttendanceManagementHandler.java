package erp.attendance.command;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import erp.attendance.dto.AttendanceRecordDto;

import erp.attendance.service.EmployeeAttendanceDeleteService;
import erp.attendance.service.EmployeeAttendanceUpdateRequest;
import erp.attendance.service.EmployeeAttendanceUpdateService;
import erp.attendance.service.InsertEmployeeAttendanceRequest;
import erp.attendance.service.InsertEmployeeAttendanceService;
import erp.attendance.service.ListAttendanceEmployeeService;
import erp.attendance.service.ListEmployeeAttendanceService;
import erp.employees.dto.AttendanceEmployeeDto;

import erp.settings.model.AttendanceItem;
import erp.settings.service.AttendanceSettingService;
import mvc.command.CommandHandler;

public class AttendanceManagementHandler implements CommandHandler {

	private static final String FORM_VIEW = "/WEB-INF/view/attendance/attendance-management-test.jsp";
	private static final String SUCCESS_VIEW = ""; // 임시적으로 적은 경로

	/*
	 * InsertEmpAttendRecordService insertService = new
	 * InsertEmpAttendRecordService();
	 */
	
	InsertEmployeeAttendanceService insertService = new InsertEmployeeAttendanceService();
	EmployeeAttendanceUpdateService updateService = new EmployeeAttendanceUpdateService();
	EmployeeAttendanceDeleteService deleteService = new EmployeeAttendanceDeleteService();
	
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {

		if (req.getMethod().equalsIgnoreCase("GET")) {
			return processForm(req, res);
		} else if (req.getMethod().equalsIgnoreCase("POST")) {
			return processSubmit(req, res);
		} else {
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
	}

	private String processForm(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String employeeIdStr = req.getParameter("employeeId");
		if (employeeIdStr != null) {
			int employeeId = Integer.parseInt(employeeIdStr);
			String editId = req.getParameter("editId");
			//수정 폼
			if (editId != null) {
				req.setAttribute("editId", editId);
				req.setAttribute("employeeId", employeeId);
				req.setAttribute("inputDate", req.getParameter("inputDate"));
				req.setAttribute("attendanceItemId", req.getParameter("attendanceItemId"));
				req.setAttribute("startDate", req.getParameter("startDate"));
				req.setAttribute("endDate", req.getParameter("endDate"));
				req.setAttribute("attendValue", req.getParameter("attendValue"));
				req.setAttribute("payAmount", req.getParameter("payAmount"));
				req.setAttribute("note", req.getParameter("note"));

				
			} 
			//개별 근태 기록
			else {
				req.setAttribute("employeeId", employeeId);
				String yearParam = req.getParameter("year");
				String monthParam = req.getParameter("month");
				int year = (yearParam != null && !yearParam.isEmpty()) ? Integer.parseInt(yearParam)
						: LocalDate.now().getYear();
				int month = (monthParam != null && !monthParam.isEmpty()) ? Integer.parseInt(monthParam)
						: LocalDate.now().getMonthValue();
				req.setAttribute("year", year);
				req.setAttribute("month", month);
				ListEmployeeAttendanceService listEmployeeAttendanceService = new ListEmployeeAttendanceService();
				List<AttendanceRecordDto> attendanceRecords = listEmployeeAttendanceService
						.getEmployeeAttendance(employeeId, year, month);
				req.setAttribute("attendanceRecords", attendanceRecords);
			}
		}
		String status = req.getParameter("status");
		if (status == null) {
			status = "재직";
		}
		req.setAttribute("status", status);
		String keyword = req.getParameter("keyword");
		if (keyword == null) {
			keyword = "";
		}
		req.setAttribute("keyword", keyword);

		req.setAttribute("today", LocalDate.now());

		// 근태목록 조회
		AttendanceSettingService attendanceSettingService = AttendanceSettingService.getInstance();
		List<AttendanceItem> attendanceItems = attendanceSettingService.getAttendItems();
		req.setAttribute("attendanceItems", attendanceItems);

		// 사원목록 조회
		ListAttendanceEmployeeService listAttendanceEmployeeService = new ListAttendanceEmployeeService();
		List<AttendanceEmployeeDto> employees = listAttendanceEmployeeService.getAttendanceEmployeeDtos(keyword,
				status);
		req.setAttribute("employees", employees);

		// 사원 근태 조회

		// 사원 휴가 조회

		return FORM_VIEW;

	}

	private String processSubmit(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String deleteId = req.getParameter("deleteId");
		if (deleteId != null) {
			deleteService.delete(Integer.parseInt(deleteId));
			String empId = req.getParameter("employeeId");
			String year = req.getParameter("year");
			String month = req.getParameter("month");
			res.sendRedirect(req.getContextPath() + "/attendance/attendance-management.do?employeeId=" + empId
					+ "&year=" + year + "&month=" + month + "#attendance-record-modal-" + empId);
			return null;
		}
		String editId = req.getParameter("editId");
		Map<String, Boolean> errors = new HashMap<>();
		req.setAttribute("errors", errors);
		if (editId != null) {
			EmployeeAttendanceUpdateRequest request = createEmployeeAttendanceUpdateRequest(req, errors);
			request.validate(errors);
			if (!errors.isEmpty()) {
				return processForm(req, res);
			}
			updateService.update(request);
		} else {
			InsertEmployeeAttendanceRequest request = createInsertEmployeeAttendanceRequest(req, errors);
			request.validate(errors);
			if (!errors.isEmpty()) {
				return processForm(req, res);
			}
			insertService.insert(request);
		}
		return processForm(req, res);

	}

	private EmployeeAttendanceUpdateRequest createEmployeeAttendanceUpdateRequest(HttpServletRequest req, Map<String, Boolean> errors) {
		EmployeeAttendanceUpdateRequest request = new EmployeeAttendanceUpdateRequest();
		int employeeAttendanceId = Integer.parseInt(req.getParameter("editId"));
		request.setEmployeeAttendanceId(employeeAttendanceId);
		

		String startDateStr = req.getParameter("startDate");
		String endDateStr = req.getParameter("endDate");
		String inputDateStr = req.getParameter("inputDate");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = null;
		Date endDate = null;
		Date inputDate = null;
		try {
			startDate = formatter.parse(startDateStr);
			endDate = formatter.parse(endDateStr);
			inputDate = formatter.parse(inputDateStr);
		} catch (ParseException e) {
			errors.put("ParseException", Boolean.TRUE);
		}
		
		request.setInputDate(inputDate);
		request.setAttendanceItemId(Integer.parseInt(req.getParameter("attendanceItemId")));
		request.setStartDate(startDate);
		request.setEndDate(endDate);
		String attendValue = req.getParameter("attendValue");
		request.setAttendValue(
				(attendValue != null && !attendValue.trim().isEmpty()) ? Double.parseDouble(attendValue) : 0.0);
		String payAmount = req.getParameter("payAmount");
		request.setPayAmount((payAmount != null && !payAmount.trim().isEmpty()) ? Long.parseLong(payAmount) : 0);
		request.setNote(req.getParameter("note"));
		return request;
	}
	
	private InsertEmployeeAttendanceRequest createInsertEmployeeAttendanceRequest(HttpServletRequest req,
			Map<String, Boolean> errors) {
		InsertEmployeeAttendanceRequest request = new InsertEmployeeAttendanceRequest();
		String[] employeeIdsStr = req.getParameterValues("employeeIds");
		List<Integer> employeeIds = new ArrayList<>();
		if (employeeIdsStr != null) {
			for (String idStr : employeeIdsStr) {
				employeeIds.add(Integer.parseInt(idStr));
			}
		}
		String startDateStr = req.getParameter("startDate");
		String endDateStr = req.getParameter("endDate");
		String inputDateStr = req.getParameter("inputDate");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = null;
		Date endDate = null;
		Date inputDate = null;
		try {
			startDate = formatter.parse(startDateStr);
			endDate = formatter.parse(endDateStr);
			inputDate = formatter.parse(inputDateStr);
		} catch (ParseException e) {
			errors.put("ParseException", Boolean.TRUE);
		}

		request.setEmployeeIds(employeeIds);
		request.setInputDate(inputDate);
		request.setAttendanceItemId(Integer.parseInt(req.getParameter("attendanceItemId")));
		request.setStartDate(startDate);
		request.setEndDate(endDate);
		String attendValue = req.getParameter("attendValue");
		request.setAttendValue(
				(attendValue != null && !attendValue.trim().isEmpty()) ? Double.parseDouble(attendValue) : 0.0);
		String payAmount = req.getParameter("payAmount");
		request.setPayAmount((payAmount != null && !payAmount.trim().isEmpty()) ? Long.parseLong(payAmount) : 0);
		request.setNote(req.getParameter("note"));
		return request;
	}

}