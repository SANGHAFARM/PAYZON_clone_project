package erp.attendance.command;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.attendance.dto.AttendanceDetailDto;
import erp.attendance.service.ListAttendanceDetailService;
import erp.attendance.service.ListMonthlyAttendanceService;
import erp.attendance.service.request.AttendanceDetailRequest;
import erp.attendance.service.request.MonthlyAttendanceRequest;
import erp.settings.dao.DepartmentDao;
import erp.settings.dao.JobPositionDao;
import erp.settings.service.AttendanceSettingService;
import erp.settings.service.DepartmentPositionService;
import mvc.command.CommandHandler;

// 근태조회 화면의 HTTP 요청을 구분하고 적절한 서비스 호출과 화면 이동을 담당한다.
// 勤怠照会画面のHTTPリクエストを判別し、適切なサービス呼び出しと画面遷移を担当する。
public class AttendanceInquiryHandler implements CommandHandler {

	final String FORM_VIEW = "/WEB-INF/view/attendance/attendance-inquiry.jsp";

	ListMonthlyAttendanceService listService = new ListMonthlyAttendanceService();
	DepartmentDao departmentDao = DepartmentDao.getInstance();
	JobPositionDao jobPositionDao = JobPositionDao.getInstance();
	ListAttendanceDetailService detailService = new ListAttendanceDetailService();

	@Override
	// 요청 방식과 작업 구분을 확인하여 근태조회 조회·저장 작업을 적절한 처리로 연결한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエスト方式と処理区分を確認し、勤怠照会の照会・保存処理へ適切に振り分ける。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {

		if (req.getMethod().equalsIgnoreCase("GET")) {
			return processForm(req, res);
		} else {
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
	}

	// 근태조회 화면에 필요한 데이터를 조회하여 request에 저장하고 JSP 경로를 반환한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// 勤怠照会画面に必要なデータを照会してrequestへ保存し、JSPパスを返す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	public String processForm(HttpServletRequest req, HttpServletResponse res) throws Exception {
		DepartmentPositionService departmentPositionService = DepartmentPositionService.getInstance();
		req.setAttribute("departments", departmentPositionService.getDepartmentOptions());
		req.setAttribute("jobPositions", departmentPositionService.getJobPositionOptions());

		String view = req.getParameter("view");
		if (view == null) {
			view = "MONTH";
		}
		req.setAttribute("viewMode", view);
		if (view.equals("MONTH")) {

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

			String[] empTypes = { "정규직", "계약직", "임시직", "파견직", "위촉직", "일용직" };
			req.setAttribute("empTypes", empTypes);
		} else {
			String yearParam = req.getParameter("year");
			String monthParam = req.getParameter("month");
			int year = (yearParam != null && !yearParam.isEmpty()) ? Integer.parseInt(yearParam) : 0;
			int month = (monthParam != null && !monthParam.isEmpty()) ? Integer.parseInt(monthParam) : 0;

			AttendanceSettingService attendanceSettingService = AttendanceSettingService.getInstance();
			req.setAttribute("leaveItems", attendanceSettingService.getLeaveItems());
			req.setAttribute("attendanceItems", attendanceSettingService.getAttendItems());
			req.setAttribute("attendanceGroups", attendanceSettingService.getAttendGroups());

			AttendanceDetailRequest request = createAttendanceDetailRequest(req, year, month);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			req.setAttribute("inputDateStr", request.getInputDate() != null ? formatter.format(request.getInputDate()) : null);
			req.setAttribute("startDateStr", request.getStartDate() != null ? formatter.format(request.getStartDate()) : null);
			req.setAttribute("endDateStr", request.getEndDate() != null ? formatter.format(request.getEndDate()) : null);
			req.setAttribute("departmentId", request.getDepartmentId());
			req.setAttribute("attendanceGroupId", request.getAttendanceGroupId());
			req.setAttribute("attendanceItemId", request.getAttendanceItemId());
			req.setAttribute("leaveItemId", request.getLeaveItemId());
			req.setAttribute("empNameKr", request.getEmpNameKr());
			req.setAttribute("note", request.getNote());
			List<AttendanceDetailDto> attendanceRecords = detailService.getList(request);
			req.setAttribute("attendanceRecords", attendanceRecords);
		}

		return FORM_VIEW;

	}

	// 근태조회 처리에 사용할 근태상세정보요청정보 데이터나 객체를 생성한다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 勤怠照会処理で使用する勤怠詳細情報リクエスト情報データまたはオブジェクトを生成する。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private AttendanceDetailRequest createAttendanceDetailRequest(HttpServletRequest req, int year, int month) {
		String inputDateStr = req.getParameter("inputDate");
		String startDateStr = req.getParameter("startDate");
		String endDateStr = req.getParameter("endDate");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date inputDate = null;
		Date startDate = null;
		Date endDate = null;
		try {
			if (year != 0 && month != 0) {
				LocalDate firstDay = LocalDate.of(year, month, 1);
				LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());
				startDate = Date.from(firstDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
				endDate = Date.from(lastDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
			} else {
				inputDate = (inputDateStr != null && !inputDateStr.trim().isEmpty()) ? formatter.parse(inputDateStr)
						: null;
				startDate = (startDateStr != null && !startDateStr.trim().isEmpty()) ? formatter.parse(startDateStr)
						: null;
				endDate = (endDateStr != null && !endDateStr.trim().isEmpty()) ? formatter.parse(endDateStr) : null;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		String departmentIdStr = req.getParameter("departmentId");
		String attendanceGroupIdStr = req.getParameter("attendanceGroupId");
		String attendanceItemIdStr = req.getParameter("attendanceItemId");
		String leaveItemIdStr = req.getParameter("leaveItemId");
		Integer departmentId = (departmentIdStr != null && !departmentIdStr.trim().isEmpty())
				? Integer.parseInt(departmentIdStr)
				: null;
		Integer attendanceGroupId = (attendanceGroupIdStr != null && !attendanceGroupIdStr.trim().isEmpty())
				? Integer.parseInt(attendanceGroupIdStr)
				: null;
		Integer attendanceItemId = (attendanceItemIdStr != null && !attendanceItemIdStr.trim().isEmpty())
				? Integer.parseInt(attendanceItemIdStr)
				: null;
		Integer leaveItemId = (leaveItemIdStr != null && !leaveItemIdStr.trim().isEmpty())
				? Integer.parseInt(leaveItemIdStr)
				: null;
		String empNameKr = req.getParameter("empNameKr");
		String note = req.getParameter("note");
		AttendanceDetailRequest request = new AttendanceDetailRequest(inputDate, startDate, endDate, departmentId,
				attendanceGroupId, attendanceItemId, leaveItemId, empNameKr, note);
		return request;
	}
}
