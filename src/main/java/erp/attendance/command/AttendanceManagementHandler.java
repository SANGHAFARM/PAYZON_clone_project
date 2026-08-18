package erp.attendance.command;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.attendance.service.ListAttendanceEmployeeService;
import erp.attendance.service.ListAttendanceItemService;
import erp.employees.dto.AttendanceEmployeeDto;
import erp.employees.dto.EmployeeListItem;
import erp.settings.dao.AttendanceItemDao;
import erp.settings.model.AttendanceItem;
import erp.settings.service.AttendanceSettingService;
// import erp.attend.dao.AttendItemDao;
// import erp.attend.dao.EmpAttendRecordDao;
// /*import erp.attend.model.AttendItem;*/
// /*import erp.attend.model.EmpAttendRecord;*/
// import erp.attend.service.AttendRecordRequest;
// import erp.attend.service.InsertEmpAttendRecordService;
// import erp.hr.dao.EmployeeListItemDao;
// import erp.hr.model.EmployeeListItem;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;
import mvc.command.CommandHandler;

public class AttendanceManagementHandler implements CommandHandler {

	private static final String FORM_VIEW = "/WEB-INF/view/attendance/attendance-management-test.jsp";
	private static final String SUCCESS_VIEW = ""; // 임시적으로 적은 경로

	/*
	 * InsertEmpAttendRecordService insertService = new
	 * InsertEmpAttendRecordService();
	 */
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
		
		//근태목록 조회
		AttendanceSettingService attendanceSettingService = AttendanceSettingService.getInstance();
		List<AttendanceItem> attendanceItems = attendanceSettingService.getAttendItems();
		req.setAttribute("attendanceItems", attendanceItems);
		
		//사원목록 조회
		ListAttendanceEmployeeService listAttendanceEmployeeService = new ListAttendanceEmployeeService();
		List<AttendanceEmployeeDto> employees = listAttendanceEmployeeService.getAttendanceEmployeeDtos(keyword, status);
		req.setAttribute("employees", employees);
		
		return FORM_VIEW;

	}

	private String processSubmit(HttpServletRequest req, HttpServletResponse res) throws Exception {
		return null;
	}



	private Date parseDate(String dateStr) {
		if (dateStr == null || dateStr.isEmpty()) {
			return null;
		}
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}

}