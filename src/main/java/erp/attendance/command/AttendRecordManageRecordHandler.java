package erp.attendance.command;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.attendance.dao.AttendItemDao;
import erp.attendance.dao.EmpAttendRecordDao;
import erp.attendance.model.AttendItem;
import erp.attendance.model.EmpAttendRecord;
import erp.attendance.service.AttendRecordRequest;
import erp.attendance.service.InsertEmpAttendRecordService;
import erp.employees.dao.EmployeeListItemDao;
import erp.employees.model.EmployeeListItem;

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

public class AttendRecordManageRecordHandler implements CommandHandler {

	private static final String FORM_VIEW = "/WEB-INF/view/attend/attendance-management-test.jsp";
	private static final String SUCCESS_VIEW = ""; // 임시적으로 적은 경로

	InsertEmpAttendRecordService insertService = new InsertEmpAttendRecordService();

	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		/*
		 * if (req.getMethod().equalsIgnoreCase("GET")) { return processForm(req, res);
		 * } else if (req.getMethod().equalsIgnoreCase("POST")) { return
		 * processSubmit(req, res); } else {
		 * res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED); return null; }
		 */
		return null;
	}
	/*
	 * private String processForm(HttpServletRequest req, HttpServletResponse res)
	 * throws Exception { String status = req.getParameter("status"); if (status ==
	 * null) { status = "재직"; } req.setAttribute("status", status); String keyword =
	 * req.getParameter("keyword"); if (keyword == null) { keyword = ""; }
	 * 
	 * EmployeeListItemDao employeeListItemDao = EmployeeListItemDao.getInstance();
	 * AttendItemDao attendItemDao = AttendItemDao.getInstance(); EmpAttendRecordDao
	 * empAttendRecordDao = EmpAttendRecordDao.getInstance();
	 * 
	 * Connection conn = null; try { conn = ConnectionProvider.getConnection();
	 * 
	 * List<EmployeeListItem> employees =
	 * employeeListItemDao.selectByCondition(conn, status, null, null, null,
	 * keyword); req.setAttribute("employees", employees);
	 * 
	 * List<AttendItem> attendItems = attendItemDao.selectAll(conn);
	 * req.setAttribute("attendanceItems", attendItems);
	 * 
	 * req.setAttribute("today", new SimpleDateFormat("yyyy-MM-dd").format(new
	 * Date()));
	 * 
	 * String empIdVal = req.getParameter("empId"); if (empIdVal != null &&
	 * !empIdVal.isEmpty()) { int empId = Integer.parseInt(empIdVal);
	 * 
	 * EmployeeListItem selectedEmployee = employeeListItemDao.selectById(conn,
	 * empId); req.setAttribute("selectedEmployee", selectedEmployee);
	 * 
	 * String yearVal = req.getParameter("recordYear"); int recordYear = (yearVal !=
	 * null && !yearVal.isEmpty()) ? Integer.parseInt(yearVal) : 2026;
	 * req.setAttribute("recordYear", recordYear);
	 * 
	 * String monthVal = req.getParameter("recordMonth"); Integer recordMonth =
	 * (monthVal != null && !monthVal.isEmpty()) ? Integer.parseInt(monthVal) :
	 * null; req.setAttribute("recordMonth", recordMonth);
	 * 
	 * List<EmpAttendRecord> records =
	 * empAttendRecordDao.selectByEmpIdAndYearAndMonth(conn, empId, recordYear,
	 * recordMonth); req.setAttribute("attendanceRecords", records); }
	 * 
	 * } finally { JdbcUtil.close(conn); }
	 * 
	 * return FORM_VIEW; }
	 * 
	 * private String processSubmit(HttpServletRequest req, HttpServletResponse res)
	 * throws Exception { AttendRecordRequest attendReq = toRequest(req);
	 * req.setAttribute("attendReq", attendReq);
	 * 
	 * Map<String, Boolean> errors = new HashMap<>(); req.setAttribute("errors",
	 * errors); attendReq.validate(errors);
	 * 
	 * if (!errors.isEmpty()) { return processForm(req, res); }
	 * 
	 * try { Integer successCount = insertService.insert(attendReq);
	 * req.setAttribute("successCount", successCount); return SUCCESS_VIEW; } catch
	 * (RuntimeException e) {
	 * res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); return null; } }
	 * 
	 * private AttendRecordRequest toRequest(HttpServletRequest req) { String[]
	 * empIdParams = req.getParameterValues("empIds"); List<Integer> empIds = new
	 * ArrayList<>(); if (empIdParams != null) { for (String s : empIdParams) {
	 * empIds.add(Integer.parseInt(s)); } }
	 * 
	 * String attendItemIdVal = req.getParameter("attendItemId"); int attendItemId =
	 * (attendItemIdVal != null && !attendItemIdVal.isEmpty()) ?
	 * Integer.parseInt(attendItemIdVal) : 0;
	 * 
	 * Date inputDate = parseDate(req.getParameter("inputDate")); Date startDate =
	 * parseDate(req.getParameter("startDate")); Date endDate =
	 * parseDate(req.getParameter("endDate"));
	 * 
	 * String attendValueVal = req.getParameter("attendValue"); double attendValue =
	 * (attendValueVal != null && !attendValueVal.isEmpty()) ?
	 * Double.parseDouble(attendValueVal) : 0;
	 * 
	 * String payAmountVal = req.getParameter("payAmount"); long payAmount =
	 * (payAmountVal != null && !payAmountVal.isEmpty()) ?
	 * Long.parseLong(payAmountVal) : 0;
	 * 
	 * String note = req.getParameter("note");
	 * 
	 * AttendRecordRequest attendReq = new AttendRecordRequest();
	 * attendReq.setEmpIds(empIds); attendReq.setAttendItemId(attendItemId);
	 * attendReq.setInputDate(inputDate); attendReq.setStartDate(startDate);
	 * attendReq.setEndDate(endDate); attendReq.setAttendValue(attendValue);
	 * attendReq.setPayAmount(payAmount); attendReq.setNote(note); return attendReq;
	 * }
	 * 
	 * private Date parseDate(String dateStr) { if (dateStr == null ||
	 * dateStr.isEmpty()) { return null; } try { return new
	 * SimpleDateFormat("yyyy-MM-dd").parse(dateStr); } catch (ParseException e) {
	 * return null; } }
	 */

}