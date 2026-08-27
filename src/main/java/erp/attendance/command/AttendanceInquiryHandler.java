package erp.attendance.command;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.attendance.service.AttendanceDetailListService;
import erp.attendance.service.AttendanceMonthlyListService;
import erp.attendance.service.page.AttendanceDetailPage;
import erp.attendance.service.request.AttendanceDetailSearchRequest;
import erp.attendance.service.request.AttendanceMonthlySearchRequest;
import erp.settings.service.AttendanceSettingService;
import erp.settings.service.DepartmentPositionService;
import mvc.command.CommandHandler;

// 근태조회 화면의 HTTP 요청을 구분하고 적절한 서비스 호출과 화면 이동을 담당한다.
// 勤怠照会画面のHTTPリクエストを判別し、適切なサービス呼び出しと画面遷移を担当する。
public class AttendanceInquiryHandler implements CommandHandler {

	final String FORM_VIEW = "/WEB-INF/view/attendance/attendance-inquiry.jsp";

	private AttendanceMonthlyListService listService = new AttendanceMonthlyListService(); //월별 사원 근태 기록 조회 서비스(月別社員勤怠記録照会サービス)
	private DepartmentPositionService departmentPositionService = DepartmentPositionService.getInstance(); //부서, 직위 서비스(部署、役職サービス)
	private AttendanceDetailListService detailService = new AttendanceDetailListService(); //상세 사원 근태 기록 조회 서비스(詳細社員勤怠記録照会サービス)

	@Override
	// 요청 방식과 작업 구분을 확인하여 근태조회 조회·저장 작업을 적절한 처리로 연결한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트
	// 경로를 결정한다.
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
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트
	// 경로를 결정한다.
	// 勤怠照会画面に必要なデータを照会してrequestへ保存し、JSPパスを返す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	public String processForm(HttpServletRequest req, HttpServletResponse res) throws Exception {

		// 부서 정보를 request영역에 저장
		// 部署情報をリクエストスコープに保存
		req.setAttribute("departments", departmentPositionService.getDepartmentOptions());

		// view 파라미터를 확인해서 null일 시, 기본값을 "MONTH"로 설정
		// view パラメータを確認してnullの場合、デフォルト値を「MONTH」に設定
		String view = req.getParameter("view");
		if (view == null) {
			view = "MONTH";
		}

		// view 정보를 request영역에 저장
		// view情報をリクエストスコープに保存
		req.setAttribute("viewMode", view);

		// 월별 조회
		// 月別照会
		if (view.equals("MONTH")) {
			// 1. 직위, 구분 정보를 DB에서 조회하고, request 영역에 저장
			// 1. 役職、区分情報をDBから照会し、リクエストスコープに保存
			req.setAttribute("jobPositions", departmentPositionService.getJobPositionOptions());
			String[] empTypes = { "정규직", "계약직", "임시직", "파견직", "위촉직", "일용직" };
			req.setAttribute("empTypes", empTypes);

			// 2. 연도, 월, 재직 상태, 구분, 부서, 직위 파라미터를 확인 후, request 영역에 저장
			// 2. 年度、月、在職ステータス、区分、部署、役職パラメータを確認し、リクエストスコープに保存
			String yearParam = req.getParameter("year"); // 연도(年度)
			String monthParam = req.getParameter("month");// 월(月)
			int year = (yearParam != null && !yearParam.isEmpty()) ? Integer.parseInt(yearParam)
					: LocalDate.now().getYear();
			int month = (monthParam != null && !monthParam.isEmpty()) ? Integer.parseInt(monthParam)
					: LocalDate.now().getMonthValue(); // null일 시 현재 연도와 월로 설정(nullの場合、現在年度と月を設定)

			String status = req.getParameter("status"); // 재직 상태(在職ステータス)
			String empType = req.getParameter("empType"); // 구분(区分)
			String departmentIdStr = req.getParameter("departmentId"); // 부서(部署)
			Integer departmentId = (departmentIdStr != null && !departmentIdStr.isEmpty())
					? Integer.parseInt(departmentIdStr)
					: null;
			String jobPositionIdStr = req.getParameter("jobPositionId");// 직위(役職)
			Integer jobPositionId = (jobPositionIdStr != null && !jobPositionIdStr.isEmpty())
					? Integer.parseInt(jobPositionIdStr)
					: null;
			req.setAttribute("year", year);
			req.setAttribute("month", month);
			req.setAttribute("status", status);
			req.setAttribute("empType", empType);
			req.setAttribute("departmentId", departmentId);
			req.setAttribute("jobPositionId", jobPositionId);

			// 3. 월별 조회 리퀘스트 객체 생성 후 값을 세팅
			// 3. 月別照会リクエストオブジェクトを作り、値をセット
			AttendanceMonthlySearchRequest request = new AttendanceMonthlySearchRequest(year, month, status, empType,
					departmentId, jobPositionId);

			// 4. 조회 리퀘스트 객체를 통해 월별 근태 사원 목록을 조회 후 request 영역에 저장
			// 4. 照会リクエストオブジェクトで月別勤怠社員目録を照会し、リクエストスコープに保存
			req.setAttribute("monthlyEmployees", listService.getAttendanceMonthly(request));

			// 5. 월별 마지막날을 request영역에 저장(28일, 30일, 31일)
			// 5. 月の最終日をリクエストスコープに保存（２８日、３０日、３１日）
			int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
			req.setAttribute("daysInMonth", daysInMonth);
		}
		// 상세 조회
		// 詳細照会
		else {
			// 1. 휴가 항목, 근태 항목, 근태 그룹을 DB에서 조회 후 request 영역에 저장
			// 1. 休暇項目、勤怠項目、勤怠グループをDBから照会し、リクエストスコープに保存
			AttendanceSettingService attendanceSettingService = AttendanceSettingService.getInstance();
			req.setAttribute("leaveItems", attendanceSettingService.getLeaveItems());
			req.setAttribute("attendanceItems", attendanceSettingService.getAttendItems());
			req.setAttribute("attendanceGroups", attendanceSettingService.getAttendGroups());

			// 2. 월별조회에서 클릭으로 넘어온 연도와 월 파라미터가 있는지 확인
			// 2. 月別照会からクリックで渡された年度と月のパラメータがあるかを確認
			String yearParam = req.getParameter("year");
			String monthParam = req.getParameter("month");
			int year = (yearParam != null && !yearParam.isEmpty()) ? Integer.parseInt(yearParam) : 0;
			int month = (monthParam != null && !monthParam.isEmpty()) ? Integer.parseInt(monthParam) : 0;

			// 3. 상세 조회 리퀘스트 객체 생성 후 값을 세팅
			// 3. 詳細照会リクエストオブジェクトを作り、値をセット
			AttendanceDetailSearchRequest request = createAttendanceDetailSearchRequest(req, year, month);

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");// 날짜를 문자열로 바꾸는
																			// formatter(日付を文字列に変えるformatter)
			String todayStr = formatter.format(new Date());// 오늘 날짜를 문자열로 바꿈(今日の日付を文字列に変える)

			// 4. request 영역에 입력일, 시작일, 종료일, 부서, 근태그룹, 근태항목, 휴가항목, 사원명, 적요를 저장
			// 4. リクエストスコープに入力日、開始日、終了日,部署、勤怠グループ、勤怠項目、休暇項目、社員名、摘要を保存
			// 입력일
			req.setAttribute("inputDateStr",
					request.getInputDate() != null ? formatter.format(request.getInputDate()) : todayStr); // 입력일
			req.setAttribute("startDateStr",
					request.getStartDate() != null ? formatter.format(request.getStartDate()) : todayStr); // 시작일
			req.setAttribute("endDateStr",
					request.getEndDate() != null ? formatter.format(request.getEndDate()) : todayStr); // 종료일
			// 파라미터가 존재하지 않을 경우 오늘 날짜를 저장(パラメータがなければ今日の日付を保存)

			req.setAttribute("departmentId", request.getDepartmentId()); // 부서(部署)
			req.setAttribute("attendanceGroupId", request.getAttendanceGroupId()); // 근태그룹(勤怠グループ)
			req.setAttribute("attendanceItemId", request.getAttendanceItemId()); // 근태항목(勤怠項目)
			req.setAttribute("leaveItemId", request.getLeaveItemId()); // 휴가항목(休暇項目)
			req.setAttribute("empNameKr", request.getEmpNameKr()); // 사원명(社員名)
			req.setAttribute("note", request.getNote()); // 적요(摘要)

			// 5. 상세 근태 기록 페이징 처리
			// 5. 詳細勤怠記録ページング処理
			String pageNoVal = req.getParameter("pageNo");// 페이지 번호 파라미터를 확인(ページ番号パラメータを確認)
			int pageNo = 1;
			if (pageNoVal != null && !pageNoVal.trim().isEmpty()) {
				pageNo = Integer.parseInt(pageNoVal); // 파라미터가 없을 시, 1로 설정(パラメータがない場合、１に設定)
			}

			// 페이지 번호와 조회 리퀘스트 객체를 기반으로 사원 근태 상세 조회 페이지를 만들고 request 영역에 저장
			// ページ番号と照会リクエストオブジェクトを基に、社員勤怠照会ページオブジェクトを作り、リクエストスコープに保存
			AttendanceDetailPage attendanceDetail = detailService.getDetailPage(pageNo, request);
			req.setAttribute("attendanceDetail", attendanceDetail);
		}

		return FORM_VIEW;

	}

	// 근태조회 처리에 사용할 근태상세정보요청정보 데이터나 객체를 생성한다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 勤怠照会処理で使用する勤怠詳細情報リクエスト情報データまたはオブジェクトを生成する。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private AttendanceDetailSearchRequest createAttendanceDetailSearchRequest(HttpServletRequest req, int year,
			int month) {
		// 1. 파라미터를 확인
		// 1. パラメータを確認
		String inputDateStr = req.getParameter("inputDate"); // 입력일(入力日)
		String startDateStr = req.getParameter("startDate"); // 시작일(開始日)
		String endDateStr = req.getParameter("endDate"); // 종료일(終了日)
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date inputDate = null;
		Date startDate = null;
		Date endDate = null;
		try {
			// 월별 조회에서 클릭으로 넘어온 연도와 월 파라미터가 존재할 시, 해당연월 시작일과 종료일을 만듦
			// 月別照会からクリックで渡された年度と月パラメータがある場合、年月の開始日、終了日を作る
			if (year != 0 && month != 0) {
				LocalDate firstDay = LocalDate.of(year, month, 1);
				LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());
				startDate = Date.from(firstDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
				endDate = Date.from(lastDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
			}
			// 월별 조회에서 넘어오지 않은 경우, 파라미터를 기반으로 입력일, 시작일, 종료일을 만듦
			// 月別照会から渡されたパラメータがない場合、パラメータを基に入力日、開始日、終了日を作る
			else {
				inputDate = (inputDateStr != null && !inputDateStr.trim().isEmpty()) ? formatter.parse(inputDateStr)
						: null;
				startDate = (startDateStr != null && !startDateStr.trim().isEmpty()) ? formatter.parse(startDateStr)
						: null;
				endDate = (endDateStr != null && !endDateStr.trim().isEmpty()) ? formatter.parse(endDateStr) : null;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String departmentIdStr = req.getParameter("departmentId");// 부서(部署)
		String attendanceGroupIdStr = req.getParameter("attendanceGroupId");// 근태그룹(勤怠グループ)
		String attendanceItemIdStr = req.getParameter("attendanceItemId");// 근태항목(勤怠項目)
		String leaveItemIdStr = req.getParameter("leaveItemId");// 휴가항목(休暇項目)
		String empNameKr = req.getParameter("empNameKr");// 사원명(社員名)
		String note = req.getParameter("note");// 적요(摘要)
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

		// 2. 값을 기반으로 조회 리퀘스트 생성 후 반환
		// 2. 値を基に照会リクエストオブジェクトを作って返す
		AttendanceDetailSearchRequest request = new AttendanceDetailSearchRequest(inputDate, startDate, endDate,
				departmentId, attendanceGroupId, attendanceItemId, leaveItemId, empNameKr, note);
		return request;
	}
}
