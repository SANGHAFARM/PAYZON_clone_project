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

import erp.attendance.service.DailyWorkInsertRequest;
import erp.attendance.service.DailyWorkInsertService;
import erp.attendance.service.ListDailyWorkRecordService;
import erp.employees.dao.EmployeeDao;
import erp.employees.dto.DayWorkerDto;
import erp.settings.dao.ProjectDao;
import erp.settings.model.Project;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;
import mvc.command.CommandHandler;

public class DayWorkerManagementHandler implements CommandHandler {
	private static final String FORM_VIEW = "/WEB-INF/view/attendance/day-worker-management-test.jsp";
	private static final String SUCCESS_VIEW = "";

	ListDailyWorkRecordService listService = new ListDailyWorkRecordService();
	DailyWorkInsertService insertService = new DailyWorkInsertService();
	
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
		req.setAttribute("keyword", keyword);
		EmployeeDao employeeDao = EmployeeDao.getInstance();
		ProjectDao projectDao = ProjectDao.getInstance();
		LocalDate today = LocalDate.now();
		req.setAttribute("today", today);
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			List<DayWorkerDto> dayWorkers = employeeDao.selectDayWorkerListByKeywordAndStatus(conn, keyword, status);
			req.setAttribute("dayWorkers", dayWorkers);
			List<Project> projects = projectDao.selectAll(conn);
			req.setAttribute("projects", projects);

		} finally {
			JdbcUtil.close(conn);
		}
		return FORM_VIEW;

	}

	private String processSubmit(HttpServletRequest req, HttpServletResponse res) {
		Map<String, Boolean> errors = new HashMap<>();
		req.setAttribute("errors", errors);
		DailyWorkInsertRequest request = createDailyWorkInsertRequest(req, errors);
		request.validate(errors);
		if (!errors.isEmpty()) {
			return FORM_VIEW; //<-수정해야 할지도
		}
		
		insertService.insert(request);
		
		return SUCCESS_VIEW;
	}

	private DailyWorkInsertRequest createDailyWorkInsertRequest(HttpServletRequest req, Map<String, Boolean> errors){
		DailyWorkInsertRequest request = new DailyWorkInsertRequest();
		// 사원id 목록을 문자열 배열로 받기
		String[] employeeIdsStr = req.getParameterValues("employeeIds");

		// 문자열 배열을 List<Integer>로 변환
		List<Integer> employeeIds = new ArrayList<>();
		if (employeeIdsStr != null) {
			for (String idStr : employeeIdsStr) {
				employeeIds.add(Integer.parseInt(idStr));
			}
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date workDate = null;
		try {
			formatter.parse(req.getParameter("workDate"));
		} catch (ParseException e) {
			errors.put("ParseException", Boolean.TRUE);
		}


		request.setEmployeeIds(employeeIds);
		request.setWorkDate(workDate);
		request.setProjectId(Integer.parseInt(req.getParameter("projectId")));
		request.setDailyPay(Long.parseLong(req.getParameter("dailyPay")));
		request.setPayRate(Double.parseDouble(req.getParameter("payRate")));
		request.setIncomeTax(Long.parseLong(req.getParameter("incomeTax")));
		request.setLocalIncomeTax(Long.parseLong(req.getParameter("localIncomeTax")));
		request.setActualPay(Long.parseLong(req.getParameter("actualPay")));
		
		return request;
	}

}
