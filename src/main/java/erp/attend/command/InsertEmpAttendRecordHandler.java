package erp.attend.command;

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

import erp.attend.dao.AttendItemDao;
import erp.attend.model.AttendItem;
import erp.attend.service.AttendRecordRequest;
import erp.attend.service.InsertEmpAttendRecordService;
import erp.hr.dao.EmployeeListItemDao;
import erp.hr.model.EmployeeListItem;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;
import mvc.command.CommandHandler;

public class InsertEmpAttendRecordHandler implements CommandHandler {
	private static final String FORM_VIEW = "/WEB-INF/view/attend/attendance-management-test.jsp"; // 임시적으로 적은 경로
	private static final String SUCCESS_VIEW = ""; // 임시적으로 적은 경로

	InsertEmpAttendRecordService insertService = new InsertEmpAttendRecordService();

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
		if (status == null || status.isEmpty()) {
			status = "재직";
		}
		String keyword = req.getParameter("keyword");
		if (keyword==null) {
			keyword="";
		}
		EmployeeListItemDao employeeListItemDao = EmployeeListItemDao.getInstance();
		AttendItemDao attendItemDao = AttendItemDao.getInstance();
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			List<EmployeeListItem> employees = employeeListItemDao.selectByCondition(conn, status, null, null, null, keyword);
			req.setAttribute("employees", employees);
			
			List<AttendItem> attendItems = attendItemDao.selectAll(conn);
			req.setAttribute("attendanceItems", attendItems);
			
			req.setAttribute("today", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

		} finally {
			JdbcUtil.close(conn);
		}

		return FORM_VIEW;
	}

	private String processSubmit(HttpServletRequest req, HttpServletResponse res) throws Exception {

		// 1. 파라미터 읽어서 aAtendRecordRequest 생성
		AttendRecordRequest attendReq = toRequest(req);
		req.setAttribute("attendReq", attendReq);

		// 2. 유효성 검사
		Map<String, Boolean> errors = new HashMap<>();
		req.setAttribute("errors", errors);
		attendReq.validate(errors);

		if (!errors.isEmpty()) {
			return processForm(req, res); // 오류 있으면 다시 폼으로
		}

		// 3. Service 호출
		try {
			Integer successCount = insertService.insert(attendReq);
			req.setAttribute("successCount", successCount);
			return SUCCESS_VIEW;
		} catch (RuntimeException e) {
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}

	}

	// request 파라미터를 읽어서 attendRecordRequest로 변환
	private AttendRecordRequest toRequest(HttpServletRequest req) {
		// 체크박스로 여러명 선택 -> 배열로 넘어옴
		String[] empIdParams = req.getParameterValues("empIds");
		List<Integer> empIds = new ArrayList<>();
		//배열이 null이 아니면 반복문을 통해 empIds에 추가
		if (empIdParams != null) {
			for(String s : empIdParams) {
				empIds.add(Integer.parseInt(s));
			}
		}
		
		//근태항목
		String attendItemIdVal = req.getParameter("attendItemId");
		int attendItemId = (attendItemIdVal != null && !attendItemIdVal.isEmpty())? Integer.parseInt(attendItemIdVal):0;
		//입력일자
		Date inputDate = parseDate(req.getParameter("inputDate"));
		//기간 시작일
		Date startDate = parseDate(req.getParameter("startDate"));
		//기간 종료일
		Date endDate = parseDate(req.getParameter("endDate"));
		
		//근태일수/근태시간
		String attendValueVal = req.getParameter("attendValue");
		double attendValue = (attendValueVal!=null && !attendValueVal.isEmpty())?Double.parseDouble(attendValueVal):0;
		
		//금액
		String payAmountVal = req.getParameter("payAmount");
		long payAmount = (payAmountVal!=null && !payAmountVal.isEmpty())?Long.parseLong(payAmountVal):0;
		
		//적요
		String note = req.getParameter("note");
		
		//AttendRecordRequest객체를 생성하고 값 세팅
		AttendRecordRequest attendReq = new AttendRecordRequest();
		attendReq.setEmpIds(empIds);
		attendReq.setAttendItemId(attendItemId);
		attendReq.setInputDate(inputDate);
		attendReq.setStartDate(startDate);
		attendReq.setEndDate(endDate);
		attendReq.setAttendValue(attendValue);
		attendReq.setPayAmount(payAmount);
		attendReq.setNote(note);
		return attendReq;
	}

	// 파라미터로 받은 날짜를 java.util.Date로 변환하는 메서드
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
