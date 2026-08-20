package erp.attendance.command;

import java.sql.Connection;
import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.attendance.service.ListMonthlyAttendanceService;
import erp.attendance.service.MonthlyAttendanceRequest;
import erp.settings.dao.DepartmentDao;
import erp.settings.dao.JobPositionDao;
import jdbc.connection.ConnectionProvider;
import mvc.command.CommandHandler;

public class AttendanceInquiryHandler implements CommandHandler {

	final String FORM_VIEW = "/WEB-INF/view/attendance/attendance-inquiry-test.jsp";

	ListMonthlyAttendanceService listService = new ListMonthlyAttendanceService();
	DepartmentDao departmentDao = DepartmentDao.getInstance();
	JobPositionDao jobPositionDao = JobPositionDao.getInstance();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {

		if (req.getMethod().equalsIgnoreCase("GET")) {
			return processForm(req, res);
		} else {
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
	}

	public String processForm(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String viewMode = req.getParameter("viewMode");
		if (viewMode == null) {
			viewMode = "MONTH";
		}
		req.setAttribute("viewMode", viewMode);
		if(viewMode=="MONTH") {

		String yearParam = req.getParameter("year");
		String monthParam = req.getParameter("month");
		int year = (yearParam != null && !yearParam.isEmpty()) ? Integer.parseInt(yearParam)
				: LocalDate.now().getYear();
		int month = (monthParam != null && !monthParam.isEmpty()) ? Integer.parseInt(monthParam)
				: LocalDate.now().getMonthValue();
		req.setAttribute("year", year);
		req.setAttribute("month", month);

		String status = req.getParameter("status");
		String empType = req.getParameter("empType");
		req.setAttribute("status", status);
		req.setAttribute("empType", empType);

		String departmentIdStr = req.getParameter("departmentId");
		String jobPositionIdStr = req.getParameter("jobPositionId");
		Integer departmentId = (departmentIdStr != null && !departmentIdStr.isEmpty())
				? Integer.parseInt(departmentIdStr)
				: null;
		Integer jobPositionId = (jobPositionIdStr != null && !jobPositionIdStr.isEmpty())
				? Integer.parseInt(jobPositionIdStr)
				: null;
		req.setAttribute("departmentId", departmentId);
		req.setAttribute("jobPositionId", jobPositionId);

		MonthlyAttendanceRequest request = new MonthlyAttendanceRequest(year, month, status, empType, departmentId,
				jobPositionId);
		req.setAttribute("monthlyEmployees", listService.getMonthlyAttendance(request));

		try (Connection conn = ConnectionProvider.getConnection()) {
			req.setAttribute("departments", departmentDao.selectAll(conn));
			req.setAttribute("jobPositions", jobPositionDao.selectAll(conn));
		}
		String[] empTypes = { "정규직", "계약직", "임시직", "파견직", "위촉직", "일용직" };
		req.setAttribute("empTypes", empTypes);
		} else {
			
		}
		
		return FORM_VIEW;

	}
}
