package erp.attendance.command;

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

import erp.attendance.dto.AttendanceEmployeeRecordDto;
import erp.attendance.service.EmployeeAttendanceDeleteService;
import erp.attendance.service.EmployeeAttendanceUpdateService;
import erp.attendance.service.EmployeeAttendanceInsertService;
import erp.attendance.service.AttendanceEmployeeListService;
import erp.attendance.service.EmployeeAttendanceListService;
import erp.attendance.service.request.AttendanceEmployeeSearchRequest;
import erp.attendance.service.request.AttendanceRecordInsertRequest;
import erp.attendance.service.request.AttendanceRecordUpdateRequest;
import erp.employees.dto.AttendanceEmployeeDto;
import erp.settings.model.AttendanceItem;
import erp.settings.service.AttendanceSettingService;
import mvc.command.CommandHandler;

// 근태입력·관리 화면의 HTTP 요청을 구분하고 적절한 서비스 호출과 화면 이동을 담당한다.
// 勤怠入力・管理画面のHTTPリクエストを判別し、適切なサービス呼び出しと画面遷移を担当する。
public class AttendanceManagementHandler implements CommandHandler {

	private static final String FORM_VIEW = "/WEB-INF/view/attendance/attendance-management.jsp";
	private static final String SUCCESS_VIEW = ""; // 임시적으로 적은 경로

	/*
	 * InsertEmpAttendRecordService insertService = new
	 * InsertEmpAttendRecordService();
	 */
	
	private EmployeeAttendanceInsertService insertService = new EmployeeAttendanceInsertService();
	private EmployeeAttendanceUpdateService updateService = new EmployeeAttendanceUpdateService();
	private EmployeeAttendanceDeleteService deleteService = new EmployeeAttendanceDeleteService();
	
	// 요청 방식과 작업 구분을 확인하여 근태입력·관리 조회·저장 작업을 적절한 처리로 연결한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエスト方式と処理区分を確認し、勤怠入力・管理の照会・保存処理へ適切に振り分ける。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
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

	// 근태입력·관리 화면에 필요한 데이터를 조회하여 request에 저장하고 JSP 경로를 반환한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// 勤怠入力・管理画面に必要なデータを照会してrequestへ保存し、JSPパスを返す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	private String processForm(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String employeeIdStr = req.getParameter("employeeId");
		if (employeeIdStr != null) {
			int employeeId = Integer.parseInt(employeeIdStr);
			String editId = req.getParameter("editId");
			//수정 폼
			// 識別された既存データへ変更値を反映し、未変更項目は従来の値を維持する。
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
			// 社員の勤務・休暇記録と適用期間を確認し、勤怠照会または残日数計算へ反映する。
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
				AttendanceEmployeeSearchRequest request = new AttendanceEmployeeSearchRequest(employeeId, year, month);
				EmployeeAttendanceListService listEmployeeAttendanceService = new EmployeeAttendanceListService();
				List<AttendanceEmployeeRecordDto> attendanceRecords = listEmployeeAttendanceService
						.getAttendanceEmployeeRecord(request);
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
		// 対象一覧を順番に処理し、各行の入力値または照会結果を同じ基準で構成する。
		AttendanceSettingService attendanceSettingService = AttendanceSettingService.getInstance();
		List<AttendanceItem> attendanceItems = attendanceSettingService.getAttendItems();
		req.setAttribute("attendanceItems", attendanceItems);

		// 사원목록 조회
		// 社員の識別情報と在職・雇用・所属条件を確認し、対象社員データへ反映する。
		AttendanceEmployeeListService listAttendanceEmployeeService = new AttendanceEmployeeListService();
		List<AttendanceEmployeeDto> employees = listAttendanceEmployeeService.getAttendanceEmployee(keyword,
				status);
		req.setAttribute("employees", employees);

		// 사원 근태 조회
		// 社員の識別情報と在職・雇用・所属条件を確認し、対象社員データへ反映する。

		// 사원 휴가 조회
		// 社員の識別情報と在職・雇用・所属条件を確認し、対象社員データへ反映する。

		return FORM_VIEW;

	}

	// 근태입력·관리 입력 요청을 검증한 뒤 서비스에 저장을 위임하고 처리 결과를 전달한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// 勤怠入力・管理の入力リクエストを検証し、サービスへ保存を委譲して処理結果を渡す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
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
			AttendanceRecordUpdateRequest request = createAttendanceRecordUpdateRequest(req, errors);
			request.validate(errors);
			if (!errors.isEmpty()) {
				return processForm(req, res);
			}
			updateService.update(request);
		} else {
			AttendanceRecordInsertRequest request = createAttendanceRecordInsertRequest(req, errors);
			request.validate(errors);
			if (!errors.isEmpty()) {
				return processForm(req, res);
			}
			insertService.insert(request);
		}
		return processForm(req, res);

	}

	// 근태입력·관리 처리에 사용할 사원근태Update요청정보 데이터나 객체를 생성한다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 勤怠入力・管理処理で使用する社員勤怠Updateリクエスト情報データまたはオブジェクトを生成する。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private AttendanceRecordUpdateRequest createAttendanceRecordUpdateRequest(HttpServletRequest req, Map<String, Boolean> errors) {
		AttendanceRecordUpdateRequest request = new AttendanceRecordUpdateRequest();
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
			errors.put("date", Boolean.TRUE);
		}
		
		request.setInputDate(inputDate);
		request.setStartDate(startDate);
		request.setEndDate(endDate);
		String attendanceItemIdStr = req.getParameter("attendanceItemId");
		request.setAttendanceItemId((attendanceItemIdStr!=null&&!attendanceItemIdStr.trim().isEmpty())?Integer.parseInt(attendanceItemIdStr):null);
		
		String attendValue = req.getParameter("attendValue");
		request.setAttendValue(
				(attendValue != null && !attendValue.trim().isEmpty()) ? Double.parseDouble(attendValue) : 0.0);
		String payAmount = req.getParameter("payAmount");
		request.setPayAmount((payAmount != null && !payAmount.trim().isEmpty()) ? Long.parseLong(payAmount) : 0);
		request.setNote(req.getParameter("note"));
		return request;
	}
	
	// 근태입력·관리 처리에 사용할 Insert사원근태요청정보 데이터나 객체를 생성한다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 勤怠入力・管理処理で使用するInsert社員勤怠リクエスト情報データまたはオブジェクトを生成する。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private AttendanceRecordInsertRequest createAttendanceRecordInsertRequest(HttpServletRequest req,
			Map<String, Boolean> errors) {
		AttendanceRecordInsertRequest request = new AttendanceRecordInsertRequest();
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
			errors.put("date", Boolean.TRUE);
		}

		request.setEmployeeIds(employeeIds);
		request.setInputDate(inputDate);
		request.setStartDate(startDate);
		request.setEndDate(endDate);
		
		String attendanceItemIdStr = req.getParameter("attendanceItemId");
		request.setAttendanceItemId((attendanceItemIdStr!=null&&!attendanceItemIdStr.trim().isEmpty())?Integer.parseInt(attendanceItemIdStr):null);
		
		String attendValue = req.getParameter("attendValue");
		request.setAttendValue(
				(attendValue != null && !attendValue.trim().isEmpty()) ? Double.parseDouble(attendValue) : 0.0);
		String payAmount = req.getParameter("payAmount");
		request.setPayAmount((payAmount != null && !payAmount.trim().isEmpty()) ? Long.parseLong(payAmount) : 0);
		request.setNote(req.getParameter("note"));
		return request;
	}

}
