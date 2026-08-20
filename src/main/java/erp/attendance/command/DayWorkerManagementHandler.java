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

import erp.attendance.dao.DailyWorkRecordDao;
import erp.attendance.dao.ProjectDao;
import erp.attendance.dto.DailyWorkRecordDto;
import erp.attendance.model.Project;
import erp.attendance.service.DailyWorkDeleteService;
import erp.attendance.service.DailyWorkInsertService;
import erp.attendance.service.DailyWorkUpdateService;
import erp.attendance.service.ListDailyWorkRecordService;
import erp.attendance.service.request.DailyWorkInsertRequest;
import erp.attendance.service.request.DailyWorkRecordRequest;
import erp.attendance.service.request.DailyWorkUpdateRequest;
import erp.employees.dao.EmployeeDao;
import erp.employees.dto.DayWorkerDto;
import jdbc.connection.ConnectionProvider;
import mvc.command.CommandHandler;

public class DayWorkerManagementHandler implements CommandHandler {
	private static final String FORM_VIEW = "/WEB-INF/view/attendance/day-worker-management-test.jsp";
	private static final String SUCCESS_VIEW = "";

	ListDailyWorkRecordService listService = new ListDailyWorkRecordService();
	DailyWorkInsertService insertService = new DailyWorkInsertService();
	DailyWorkUpdateService updateService = new DailyWorkUpdateService();

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

		// 사원별 근무기록 기능인지 확인
		String employeeId = req.getParameter("employeeId");
		if (employeeId != null) {

			String editId = req.getParameter("editId");
			if (editId != null) {
				req.setAttribute("editId", editId);
				req.setAttribute("workDate", req.getParameter("workDate"));
				req.setAttribute("projectId", req.getParameter("projectId"));
				req.setAttribute("dailyPay", req.getParameter("dailyPay"));
				req.setAttribute("payRate", req.getParameter("payRate"));
				req.setAttribute("incomeTax", req.getParameter("incomeTax"));
				req.setAttribute("localIncomeTax", req.getParameter("localIncomeTax"));
				req.setAttribute("actualPay", req.getParameter("actualPay"));
			} else {
				req.setAttribute("employeeId", employeeId);
				DailyWorkRecordDao dailyWorkRecordDao = DailyWorkRecordDao.getInstance();
				DailyWorkRecordRequest request = new DailyWorkRecordRequest();
				String yearParam = req.getParameter("year");
				String monthParam = req.getParameter("month");
				int year = (yearParam != null && !yearParam.isEmpty()) ? Integer.parseInt(yearParam)
						: LocalDate.now().getYear();
				int month = (monthParam != null && !monthParam.isEmpty()) ? Integer.parseInt(monthParam)
						: LocalDate.now().getMonthValue();
				request.setEmployeeId(Integer.parseInt(employeeId));
				request.setYear(year);
				request.setMonth(month);
				req.setAttribute("year", year);
				req.setAttribute("month", month);
				try (Connection conn = ConnectionProvider.getConnection()) {
					List<DailyWorkRecordDto> workRecords = dailyWorkRecordDao.selectByRequest(conn, request);
					req.setAttribute("workRecords", workRecords);
				}
			}
		}
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
		try (Connection conn = ConnectionProvider.getConnection()){
			List<DayWorkerDto> dayWorkers = employeeDao.selectDayWorkerListByKeywordAndStatus(conn, keyword, status);
			req.setAttribute("dayWorkers", dayWorkers);
			List<Project> projects = projectDao.selectAll(conn);
			req.setAttribute("projects", projects);

		}
		return FORM_VIEW;

	}

	private String processSubmit(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String deleteId = req.getParameter("deleteId");
		if (deleteId != null) {
			DailyWorkDeleteService dailyWorkDeleteService = new DailyWorkDeleteService();
			int no = Integer.parseInt(deleteId);
			dailyWorkDeleteService.delete(no);
			// 삭제 후 파라미터를유지한 채 원래 페이지로 리다이렉트 (모달 앵커 포함)
			String empId = req.getParameter("employeeId");
			String year = req.getParameter("year");
			String month = req.getParameter("month");

			res.sendRedirect(req.getContextPath() + "/attendance/day-worker-management.do?employeeId=" + empId
					+ "&year=" + year + "&month=" + month + "#work-history-" + empId);
			return null; // 리다이렉트를 직접 처리했으므로 뷰 경로는 null 리턴
		}
		String editId = req.getParameter("editId");
		Map<String, Boolean> errors = new HashMap<>();
		req.setAttribute("errors", errors);
		if (editId != null) {
			DailyWorkUpdateRequest request = createDailyWorkUpdateRequest(req, errors);
			request.validate(errors);
			if (!errors.isEmpty()) {
				return processForm(req, res); // <-수정해야 할지도
			}
			updateService.update(request);
		} else {
			DailyWorkInsertRequest request = createDailyWorkInsertRequest(req, errors);
			request.validate(errors);
			if (!errors.isEmpty()) {
				return processForm(req, res); // <-수정해야 할지도
			}
			insertService.insert(request);
		}
		return processForm(req, res);
	}

	private DailyWorkUpdateRequest createDailyWorkUpdateRequest(HttpServletRequest req, Map<String, Boolean> errors) {
		DailyWorkUpdateRequest request = new DailyWorkUpdateRequest();
		request.setDailyWorkRecordId(Integer.parseInt(req.getParameter("editId")));
		String date = req.getParameter("workDate");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date workDate = null;
		try {
			workDate = formatter.parse(date);
		} catch (ParseException e) {
			errors.put("ParseException", Boolean.TRUE);
		}

		request.setWorkDate(workDate);
		request.setProjectId(Integer.parseInt(req.getParameter("projectId")));
		
		String dailyPayStr = req.getParameter("dailyPay");
		long dailyPay = (dailyPayStr!=null&&!dailyPayStr.trim().isEmpty())?Long.parseLong(dailyPayStr):0L;
		request.setDailyPay(dailyPay);
		
		String payRateStr = req.getParameter("payRate");
		double payRate = (payRateStr!=null&&!payRateStr.trim().isEmpty())?Double.parseDouble(payRateStr):0.0;
		request.setPayRate(payRate);

		String incomeTaxStr = req.getParameter("incomeTax").replaceAll(",", "");
		String localIncomTaxStr = req.getParameter("localIncomeTax").replaceAll(",", "");
		String actualPayStr = req.getParameter("actualPay").replaceAll(",", "");
		Long incomTax = (incomeTaxStr!=null&&!incomeTaxStr.trim().isEmpty())?Long.parseLong(incomeTaxStr):0L;
		Long localIncomeTax = (localIncomTaxStr!=null&&!localIncomTaxStr.trim().isEmpty())?Long.parseLong(localIncomTaxStr):0L;
		Long actualPay = (actualPayStr!=null&&!actualPayStr.trim().isEmpty())?Long.parseLong(actualPayStr):0L;
		request.setIncomeTax(incomTax);
		request.setLocalIncomeTax(localIncomeTax);
		request.setActualPay(actualPay);

		return request;
	}

	private DailyWorkInsertRequest createDailyWorkInsertRequest(HttpServletRequest req, Map<String, Boolean> errors) {
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
		String date = req.getParameter("workDate");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date workDate = null;
		try {
			workDate = formatter.parse(date);
		} catch (ParseException e) {
			errors.put("ParseException", Boolean.TRUE);
		}
		request.setEmployeeIds(employeeIds);
		request.setWorkDate(workDate);
		request.setProjectId(Integer.parseInt(req.getParameter("projectId")));
		
		String dailyPayStr = req.getParameter("dailyPay");
		long dailyPay = (dailyPayStr!=null&&!dailyPayStr.trim().isEmpty())?Long.parseLong(dailyPayStr):0L;
		request.setDailyPay(dailyPay);
		
		String payRateStr = req.getParameter("payRate");
		double payRate = (payRateStr!=null&&!payRateStr.trim().isEmpty())?Double.parseDouble(payRateStr):0.0;
		request.setPayRate(payRate);

		String incomeTaxStr = req.getParameter("incomeTax").replaceAll(",", "");
		String localIncomTaxStr = req.getParameter("localIncomeTax").replaceAll(",", "");
		String actualPayStr = req.getParameter("actualPay").replaceAll(",", "");
		Long incomTax = (incomeTaxStr!=null&&!incomeTaxStr.trim().isEmpty())?Long.parseLong(incomeTaxStr):0L;
		Long localIncomeTax = (localIncomTaxStr!=null&&!localIncomTaxStr.trim().isEmpty())?Long.parseLong(localIncomTaxStr):0L;
		Long actualPay = (actualPayStr!=null&&!actualPayStr.trim().isEmpty())?Long.parseLong(actualPayStr):0L;
		request.setIncomeTax(incomTax);
		request.setLocalIncomeTax(localIncomeTax);
		request.setActualPay(actualPay);

		return request;
	}

}
