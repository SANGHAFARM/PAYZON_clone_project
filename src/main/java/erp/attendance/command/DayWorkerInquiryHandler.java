package erp.attendance.command;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.attendance.dto.DailyWorkMonthlyDto;
import erp.attendance.model.Project;
import erp.attendance.service.DailyWorkDetailListService;
import erp.attendance.service.DailyWorkMonthlyService;
import erp.attendance.service.ProjectListService;
import erp.attendance.service.page.DailyWorkDetailPage;
import erp.attendance.service.request.DailyWorkDetailSearchRequest;
import erp.attendance.service.request.DailyWorkMonthlySearchRequest;
import erp.settings.service.DepartmentPositionService;
import mvc.command.CommandHandler;

// 일용직근로자조회 화면의 HTTP 요청을 구분하고 적절한 서비스 호출과 화면 이동을 담당한다.
// 日雇い労働者照会画面のHTTPリクエストを判別し、適切なサービス呼び出しと画面遷移を担当する。
public class DayWorkerInquiryHandler implements CommandHandler {
	final private String FORM_VIEW = "/WEB-INF/view/attendance/day-worker-inquiry.jsp";
	// 현장, 프로젝트 목록 조회 서비스
	// 現場・プロジェクト目録照会サービス
	private ProjectListService projectListService = new ProjectListService();
	// 부서, 직위 관련 서비스
	// 部署、役職関連サービス
	private DepartmentPositionService departmentPositionService = DepartmentPositionService.getInstance();
	// 일용직 월별 근무 기록 조회 서비스
	// 日雇い月別勤務記録照会サービス
	private DailyWorkMonthlyService dailyWorkMonthlyService = new DailyWorkMonthlyService();
	// 일용직 상세 근무 기록 조회 서비스
	// 日雇い詳細勤務記録照会サービス
	private DailyWorkDetailListService listService = new DailyWorkDetailListService();

	@Override
	// 요청 방식과 작업 구분을 확인하여 일용직근로자조회 조회·저장 작업을 적절한 처리로 연결한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트
	// 경로를 결정한다.
	// リクエスト方式と処理区分を確認し、日雇い労働者照会の照会・保存処理へ適切に振り分ける。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		if (req.getMethod().equalsIgnoreCase("GET")) {
			return processForm(req, res);
		} else {
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
	}

	// 일용직근로자조회 화면에 필요한 데이터를 조회하여 request에 저장하고 JSP 경로를 반환한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트
	// 경로를 결정한다.
	// 日雇い労働者照会画面に必要なデータを照会してrequestへ保存し、JSPパスを返す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	public String processForm(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// view 파라미터 확인, 파라미터가 null일 시 기본값은 month로 설정
		// viewパラメータ確認、パラメータがnullの場合、デフォルト値をmonthに設定
		String view = req.getParameter("view");
		if (view == null) {
			view = "month";
		}
		// request 영역에 부서 정보 저장
		// リクエストスコープに部署情報保存
		req.setAttribute("departments", departmentPositionService.getDepartmentOptions());

		// 월별 조회
		// 月別照会
		if ("month".equals(view)) {
			// 1. 조회란 설정
			// 1. 照会欄設定
			String yearParam = req.getParameter("year"); // 연도(年度)
			String monthParam = req.getParameter("month"); // 월(月)
			int year = (yearParam != null && !yearParam.isEmpty()) ? Integer.parseInt(yearParam)
					: LocalDate.now().getYear();
			int month = (monthParam != null && !monthParam.isEmpty()) ? Integer.parseInt(monthParam)
					: LocalDate.now().getMonthValue();
			// null일 시 오늘 날짜를 저장
			// nullの場合、今日の日付を保存
			String deptIdParam = req.getParameter("departmentId"); // 부서(部署)
			String posIdParam = req.getParameter("jobPositionId"); // 직위(役職)
			Integer departmentId = (deptIdParam != null && !deptIdParam.isEmpty()) ? Integer.parseInt(deptIdParam)
					: null;
			Integer jobPositionId = (posIdParam != null && !posIdParam.isEmpty()) ? Integer.parseInt(posIdParam) : null;
			req.setAttribute("year", year);
			req.setAttribute("month", month);
			req.setAttribute("departmentId", departmentId);
			req.setAttribute("jobPositionId", jobPositionId);
			// 직위 목록을 request영역에 저장
			// 役職目録をリクエストスコープに保存
			req.setAttribute("jobPositions", departmentPositionService.getJobPositionOptions());

			// 2. 조회 요청 리퀘스트 객체를 기반으로 사원별 월별 근무 기록 조회
			// 2. 照会要請リクエストオブジェクトを基に社員別月別勤務記録照会
			List<DailyWorkMonthlyDto> list = dailyWorkMonthlyService
					.getDailyWorkMonthly(new DailyWorkMonthlySearchRequest(year, month, departmentId, jobPositionId));
			req.setAttribute("dailyWorkList", list);

			// 해당 월의 마지막날 확인 및 request 영역에 저장(28일, 30일, 31일)
			// 該当月の最終日の確認、リクエストスコープに保存(２８日、３０日、３１日)
			int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
			req.setAttribute("daysInMonth", daysInMonth);

		}
		// 상세 조회
		// 照会条件または詳細入力値を受け取り、送信後も現在の画面状態を維持する領域である。
		else if ("detail".equals(view)) {
			// view정보를 request영역에 저장
			// view情報をリクエストスコープに保存
			req.setAttribute("view", view);

			// 1. 검색폼 설정
			// 1. 検索フォーム設定
			// 현장,프로젝트 목록을 request영역에 저장
			// 現場、プロジェクト目録をリクエストスコープに保存
			List<Project> projects = projectListService.getProjects();
			req.setAttribute("projects", projects);

			// 파라미터 확인
			//　パラメータ確認
			String startStr = req.getParameter("startDate"); // 시작일(開始日)
			String endStr = req.getParameter("endDate"); // 종료일(終了日)
			String empNameKr = req.getParameter("empNameKr"); // 사원명(社員名)
			String departmentIdStr = req.getParameter("departmentId"); // 부서ID(部署ID)
			String projectIdStr = req.getParameter("projectId"); // 현장프로젝트ID(現場・プロジェクトID)
			Date startDate = null;
			Date endDate = null;
			if (startStr != null && !startStr.trim().isEmpty() && endStr != null && !endStr.trim().isEmpty()) {
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					startDate = sdf.parse(startStr);
					endDate = sdf.parse(endStr);
					req.setAttribute("startDate", startDate);
					req.setAttribute("endDate", endDate);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				// 파라미터가 없을 때는 오늘 날짜를 기본값으로 설정
				// パラメータがない場合は今日の日付をデフォルト値に設定
				String todayStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
				req.setAttribute("startDate", todayStr);
				req.setAttribute("endDate", todayStr);
			}
			Integer departmentId = (departmentIdStr != null && !departmentIdStr.isEmpty())
					? Integer.parseInt(departmentIdStr)
					: null; //부서ID(部署ID)
			Integer projectId = (projectIdStr != null && !projectIdStr.isEmpty()) ? Integer.parseInt(projectIdStr)
					: null; //현장,프로젝트ID(現場・プロジェクトID)

			// 2. 상세 조회 요청 리퀘스트 객체 생성
			// 2. 詳細照会要請リクエストオブジェクト生成
			DailyWorkDetailSearchRequest request = new DailyWorkDetailSearchRequest();
			request.setStartDate(startDate);
			request.setEndDate(endDate);
			request.setEmpNameKr(empNameKr);
			request.setDepartmentId(departmentId);
			request.setProjectId(projectId);

			// 3. 페이징 처리 및 request영역에 상세 근무 기록 저장
			// 3. ページング処理とリクエストスコープに詳細勤務記録保存
			String pageNoVal = req.getParameter("pageNo");
			// pageNo파라미터가 null이면 1로 설정
			// pageNoパラメータがnullの場合、１に設定
			int pageNo = 1;
			if (pageNoVal != null && !pageNoVal.trim().isEmpty()) {
				pageNo = Integer.parseInt(pageNoVal);
			}
			// pageNo와 조회 요청 객체를 기반으로 상세 근무 기록 페이지 객체를 생성하고 저장
			// pageNoと照会要請オブジェクトを基に詳細勤務記録ページオブジェクトを生成し保存
			DailyWorkDetailPage dailyWorkDetail = listService.getDetailPage(pageNo, request);
			req.setAttribute("dailyWorkDetail", dailyWorkDetail);

		}

		return FORM_VIEW;
	}

}
