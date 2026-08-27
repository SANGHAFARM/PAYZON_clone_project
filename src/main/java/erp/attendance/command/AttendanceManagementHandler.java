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
import erp.attendance.dto.LeaveInquiryDto;
import erp.attendance.service.AttendanceEmployeeListService;
import erp.attendance.service.EmployeeAttendanceDeleteService;
import erp.attendance.service.EmployeeAttendanceInsertService;
import erp.attendance.service.EmployeeAttendanceListService;
import erp.attendance.service.EmployeeAttendanceUpdateService;
import erp.attendance.service.LeaveBalanceListService;
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

	// 근태 기록 입력 서비스(勤怠記録入力サービス)
	private EmployeeAttendanceInsertService insertService = new EmployeeAttendanceInsertService();
	// 근태 기록 수정 서비스(勤怠記録修正サービス)
	private EmployeeAttendanceUpdateService updateService = new EmployeeAttendanceUpdateService();
	// 근태 기록 삭제 서비스(勤怠記録削除)
	private EmployeeAttendanceDeleteService deleteService = new EmployeeAttendanceDeleteService();
	// 개별근태기록조회 서비스(個人勤怠記録照会サービス)
	private EmployeeAttendanceListService listEmployeeAttendanceService = new EmployeeAttendanceListService();
	// 사원 목록 조회 서비스(社員目録照会サービス)
	private AttendanceEmployeeListService listAttendanceEmployeeService = new AttendanceEmployeeListService();
	// 근태 관련 서비스(勤怠関連サービス)
	private AttendanceSettingService attendanceSettingService = AttendanceSettingService.getInstance();
	// 휴가 현황 서비스(休暇状況サービス)
	private LeaveBalanceListService leaveBalanceListService = new LeaveBalanceListService();

	// 요청 방식과 작업 구분을 확인하여 근태입력·관리 조회·저장 작업을 적절한 처리로 연결한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트
	// 경로를 결정한다.
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
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트
	// 경로를 결정한다.
	// 勤怠入力・管理画面に必要なデータを照会してrequestへ保存し、JSPパスを返す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	private String processForm(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// 사원ID 파라미터가 있는 지 확인
		// 社員IDパラメータがあるか確認
		String employeeIdStr = req.getParameter("employeeId");
		// 사원ID가 존재하면 수정모드 또는 개별 근태 기록 확인 모드
		// 社員IDがあれば、修正モードまたは個人勤怠記録確認モード
		if (employeeIdStr != null) {
			int employeeId = Integer.parseInt(employeeIdStr);
			String editId = req.getParameter("editId");
			// 수정 모드
			// 修正モード
			if (editId != null) {
				// 수정 폼에 사용될 정보를 request 영역에 저장
				// 修正フォームに使われる情報をリクエストスコープに保存
				req.setAttribute("editId", editId); // 근태기록 ID(勤怠記録ID)
				req.setAttribute("employeeId", employeeId); // 사원 ID(社員ID)
				req.setAttribute("inputDate", req.getParameter("inputDate")); // 입력일(入力日)
				req.setAttribute("attendanceItemId", req.getParameter("attendanceItemId")); // 휴가항목ID(休暇項目ID)
				req.setAttribute("startDate", req.getParameter("startDate")); // 시작일(開始日)
				req.setAttribute("endDate", req.getParameter("endDate")); // 종료일(終了日)
				req.setAttribute("attendValue", req.getParameter("attendValue")); // 근태일수(勤怠日数)
				req.setAttribute("payAmount", req.getParameter("payAmount")); // 금액(金額)
				req.setAttribute("note", req.getParameter("note")); // 적요(摘要)
			}
			// 개별 근태 기록
			// 個人の勤怠記録
			else {
				// 사원 ID를 request 영역에 저장
				// 社員IDをリクエストスコープに保存
				req.setAttribute("employeeId", employeeId);

				// 연도와 월 파라미터를 확인 후 request 영역에 저장
				// 年度と月パラメータを確認し、リクエストスコープに保存
				String yearParam = req.getParameter("year");
				String monthParam = req.getParameter("month");
				int year = (yearParam != null && !yearParam.isEmpty()) ? Integer.parseInt(yearParam)
						: LocalDate.now().getYear();
				// 연도 파라미터가 없을 시, 올해 연도로 설정
				// 年度パラメータがなければ、今年の年度に設定
				Integer month = (monthParam != null && !monthParam.isEmpty()) ? Integer.parseInt(monthParam) : null;
				// 달 파라미터가 없을 시, null로 설정
				// 月パラメータがなければ、nullに設定
				req.setAttribute("year", year);
				req.setAttribute("month", month);

				// 사원 ID와 연월을 기반으로 조회 리퀘스트 객체 생성 후 개별 근태 기록 조회
				// 社員IDと年月を基に照会リクエストオブジェクトを作り、個人勤怠記録を照会
				AttendanceEmployeeSearchRequest request = new AttendanceEmployeeSearchRequest(employeeId, year, month);
				List<AttendanceEmployeeRecordDto> attendanceRecords = listEmployeeAttendanceService
						.getAttendanceEmployeeRecord(request);

				// request영역에 개별 근태 기록 저장
				// リクエストスコープに個人勤怠記録を保存
				req.setAttribute("attendanceRecords", attendanceRecords);
			}
		}
		// 기본 모드
		// 基本モード

		// 1. 조회, 검색란 설정
		// 1. 照会、検索欄設定
		// 재직 및 검색어 정보를 request 영역에 저장
		// 在職と検索キーワードをリクエストスコープに保存
		String status = req.getParameter("status");
		if (status == null) {
			status = "재직";
			// 재직 파라미터가 null일 시, 기본값을 재직으로 설정
			// 在職パラメータがnullの場合、デフォルト値を在職に設定
		}
		String keyword = req.getParameter("keyword");
		if (keyword == null) {
			keyword = "";
			// 검색어 파라미터가 null일 시, 기본값을 공백으로 설정
			// 検索キーワードパラメータがnullの場合、デフォルト値をを空白に設定
		}
		req.setAttribute("status", status);
		req.setAttribute("keyword", keyword);

		// 2. 사원 목록 조회 후 request 영역에 저장
		// 2. 社員目録を照会してリクエストスコープに保存
		List<AttendanceEmployeeDto> employees = listAttendanceEmployeeService.getAttendanceEmployee(keyword, status);
		req.setAttribute("employees", employees);

		// 3. 근태목록 조회 후 request 영역에 저장
		// 3. 勤怠記録を照会してリクエストスコープに保存
		List<AttendanceItem> attendanceItems = attendanceSettingService.getAttendItems();
		req.setAttribute("attendanceItems", attendanceItems);

		// 4. 사원 휴가 조회
		// 4. 社員休暇照会
		String[] employeeIdsStr = req.getParameterValues("employeeIds"); // 체크박스로 선택된 사원ID 배열(チェックボックスで選択された社会ID配列)
		String attendanceItemIdStr = req.getParameter("attendanceItemId"); // 선택된 근태항목ID(選択された勤怠記録ID)

		// 조건을 충족하였을 시, 사원의 휴가를 조회
		// 条件が満たされた場合、社員の休暇を照会
		if (employeeIdsStr != null && employeeIdsStr.length > 0 && attendanceItemIdStr != null
				&& !attendanceItemIdStr.trim().isEmpty()) {

			// 선택된 휴가 항목이 휴가 항목인지 확인
			// 選択された休暇項目が休暇項目か確認
			int selectedAttendanceItemId = Integer.parseInt(attendanceItemIdStr);
			Integer leaveItemId = null;
			// 모든 근태항목을 돌며 선택된 ID와 일치하는 항목이 있는지 확인
			// すべての勤怠項目を確認しながら、選択されたIDと一致する項目があるか確認
			for (AttendanceItem item : attendanceItems) {
				if (item.getAttendanceItemId() == selectedAttendanceItemId) {
					leaveItemId = item.getDeductLeaveId();
					break;
				}
			}

			if (leaveItemId != null) {
				// 선택된 사원 ID를 담은 리스트 생성
				// 選択された社員IDを入れたリストを作る
				List<Integer> employeeIds = new ArrayList<>();
				for (String idStr : employeeIdsStr) {
					employeeIds.add(Integer.parseInt(idStr));
				}

				// 사원ID 리스트와 휴가항목ID를 기반으로 휴가현황을 조회 후, request 영역에 저장
				// 社員IDリストと休暇項目IDを基に休暇状況を照会し、リクエストスコープに保存
				List<LeaveInquiryDto> leaveBalances = leaveBalanceListService.getLeaveEmployees(employeeIds,
						leaveItemId);
				req.setAttribute("leaveBalances", leaveBalances);
			}
		}

		// 오늘 날짜를 request 영역에 저장
		// 今日の日付をリクエストスコープに保存
		req.setAttribute("today", LocalDate.now());

		return FORM_VIEW;

	}

	// 근태입력·관리 입력 요청을 검증한 뒤 서비스에 저장을 위임하고 처리 결과를 전달한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트
	// 경로를 결정한다.
	// 勤怠入力・管理の入力リクエストを検証し、サービスへ保存を委譲して処理結果を渡す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	private String processSubmit(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// 삭제 요청인지 확인
		// 削除要請か確認
		String deleteId = req.getParameter("deleteId");
		// 삭제 요청이면 삭제 서비스 실행 후 리다이렉트
		// 削除要請なら削除サービスを行いリダイレクト
		if (deleteId != null) {
			deleteService.delete(Integer.parseInt(deleteId));
			// 리다이렉트를 위한 사원ID, 연월 파라미터
			// リダイレクトのための社員ID、年月パラメータ
			String empId = req.getParameter("employeeId");
			String year = req.getParameter("year"); // 연도(年度)
			String month = req.getParameter("month");// 월(月)
			res.sendRedirect(req.getContextPath() + "/attendance/attendance-management.do?employeeId=" + empId
					+ "&year=" + year + "&month=" + month + "#attendance-record-modal-" + empId);
			return null;
		}

		String editId = req.getParameter("editId");
		Map<String, Boolean> errors = new HashMap<>();
		req.setAttribute("errors", errors);

		// 수정 작업
		// 修正作業
		if (editId != null) {
			// 수정 요청 리퀘스트 객체 생성
			// 修正要請リクエストオブジェクト生成
			AttendanceRecordUpdateRequest request = createAttendanceRecordUpdateRequest(req, errors);
			request.validate(errors);
			if (!errors.isEmpty()) {

				return processForm(req, res);
			}
			// 수정 작업 수행
			// 修正作業修行
			updateService.update(request);
			// 팝업용
			// ポップアップ用
			req.setAttribute("successMessage", "修正しました");
		}
		// 입력 작업
		// 入力作業
		else {
			// 입력 요청 리퀘스트 객체 생성
			// 入力要請リクエストオブジェクト生成
			AttendanceRecordInsertRequest request = createAttendanceRecordInsertRequest(req, errors);
			request.validate(errors);
			if (!errors.isEmpty()) {
				// 요청에 에러가 있을 시 기본화면으로 돌려보냄
				// 要請にエラーがある場合、基本画面に返す
				return processForm(req, res);
			}
			// 입력 작업 수행
			// 入力作業修行
			insertService.insert(request);
			// 팝업용
			// ポップアップ用
			req.setAttribute("successMessage", "保存しました");
		}
		return processForm(req, res);

	}

	// 근태입력·관리 처리에 사용할 사원근태Update요청정보 데이터나 객체를 생성한다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 勤怠入力・管理処理で使用する社員勤怠Updateリクエスト情報データまたはオブジェクトを生成する。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private AttendanceRecordUpdateRequest createAttendanceRecordUpdateRequest(HttpServletRequest req,
			Map<String, Boolean> errors) {

		// 1. 수정 요청 리퀘스트 객체 생성
		// 1. 修正要請リクエストオブジェクト生成
		AttendanceRecordUpdateRequest request = new AttendanceRecordUpdateRequest();

		// 2. 필수값 세팅
		// 2. 必須値セット
		// 파라미터 확인
		// パラメータ確認
		int employeeAttendanceId = Integer.parseInt(req.getParameter("editId")); // 근태기록ID(勤怠記録ID)
		String startDateStr = req.getParameter("startDate");
		String endDateStr = req.getParameter("endDate");
		String inputDateStr = req.getParameter("inputDate");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = null; // 시작일(開始日)
		Date endDate = null; // 종료일(終了日)
		Date inputDate = null; // 입력일(入力日)
		try {
			startDate = formatter.parse(startDateStr);
			endDate = formatter.parse(endDateStr);
			inputDate = formatter.parse(inputDateStr);
		} catch (ParseException e) {
			// 에러가 발생하면, errors에 에러를 넣음
			// エラーが生じたら、errorsにエラーを入れる
			errors.put("date", Boolean.TRUE);
		}

		String attendanceItemIdStr = req.getParameter("attendanceItemId"); // 근태항목(勤怠項目)
		String attendValue = req.getParameter("attendValue"); // 근태일수(勤怠日数)
		String payAmount = req.getParameter("payAmount"); // 금액(金額)
		String note = req.getParameter("note"); // 적요(摘要)

		// 값 세팅
		// 値セット
		request.setEmployeeAttendanceId(employeeAttendanceId);
		request.setInputDate(inputDate);
		request.setStartDate(startDate);
		request.setEndDate(endDate);
		request.setAttendanceItemId((attendanceItemIdStr != null && !attendanceItemIdStr.trim().isEmpty())
				? Integer.parseInt(attendanceItemIdStr)
				: null);
		request.setAttendValue(
				(attendValue != null && !attendValue.trim().isEmpty()) ? Double.parseDouble(attendValue) : 0.0);
		request.setPayAmount((payAmount != null && !payAmount.trim().isEmpty()) ? Long.parseLong(payAmount) : 0);
		request.setNote(note);

		// 3. 객체 반환
		// 3. オブジェクトを返す
		return request;
	}

	// 근태입력·관리 처리에 사용할 Insert사원근태요청정보 데이터나 객체를 생성한다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 勤怠入力・管理処理で使用するInsert社員勤怠リクエスト情報データまたはオブジェクトを生成する。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private AttendanceRecordInsertRequest createAttendanceRecordInsertRequest(HttpServletRequest req,
			Map<String, Boolean> errors) {

		// 1. 입력 요청 리퀘스트 객체 생성
		// 1. 入力要請リクエストオブジェクト生成
		AttendanceRecordInsertRequest request = new AttendanceRecordInsertRequest();

		// 2. 필수값 세팅
		// 2. 必須値セット
		// 파라미터 확인
		// パラメータ確認
		String[] employeeIdsStr = req.getParameterValues("employeeIds");
		List<Integer> employeeIds = new ArrayList<>(); // 사원 ID 리스트(社員IDリスト)
		if (employeeIdsStr != null) {
			for (String idStr : employeeIdsStr) {
				employeeIds.add(Integer.parseInt(idStr));
			}
		}
		String startDateStr = req.getParameter("startDate");
		String endDateStr = req.getParameter("endDate");
		String inputDateStr = req.getParameter("inputDate");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = null; // 시작일(開始日)
		Date endDate = null; // 종료일(終了日)
		Date inputDate = null; // 입력일(入力日)
		try {
			startDate = formatter.parse(startDateStr);
			endDate = formatter.parse(endDateStr);
			inputDate = formatter.parse(inputDateStr);
		} catch (ParseException e) {
			// 에러가 발생하면, errors에 에러를 넣음
			// エラーが生じたら、errorsにエラーを入れる
			errors.put("date", Boolean.TRUE);
		}
		String attendanceItemIdStr = req.getParameter("attendanceItemId"); // 근태항목ID(勤怠項目ID)
		String attendValue = req.getParameter("attendValue"); // 근태일수(勤怠日数)
		String payAmount = req.getParameter("payAmount"); // 금액(金額)
		String note = req.getParameter("note"); // 적요(摘要)

		// 값 세팅
		// 値セット
		request.setEmployeeIds(employeeIds);
		request.setInputDate(inputDate);
		request.setStartDate(startDate);
		request.setEndDate(endDate);
		request.setAttendanceItemId((attendanceItemIdStr != null && !attendanceItemIdStr.trim().isEmpty())
				? Integer.parseInt(attendanceItemIdStr)
				: null);
		request.setAttendValue(
				(attendValue != null && !attendValue.trim().isEmpty()) ? Double.parseDouble(attendValue) : 0.0);
		request.setPayAmount((payAmount != null && !payAmount.trim().isEmpty()) ? Long.parseLong(payAmount) : 0);
		request.setNote(note);

		// 객체 반환
		// オブジェクト返す
		return request;
	}

}
