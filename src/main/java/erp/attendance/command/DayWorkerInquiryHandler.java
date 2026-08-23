package erp.attendance.command;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.attendance.dao.ProjectDao;
import erp.attendance.dto.DailyWorkDetailDto;
import erp.attendance.dto.DailyWorkListDto;
import erp.attendance.model.Project;
import erp.attendance.service.DailyWorkDetailListService;
import erp.attendance.service.DailyWorkDetailService;
import erp.attendance.service.DailyWorkListService;
import erp.attendance.service.page.DailyWorkDetailPage;
import erp.attendance.service.request.DailyWorkDetailRequest;
import erp.attendance.service.request.DailyWorkListRequest;
import erp.settings.service.DepartmentPositionService;
import erp.settings.service.JobPositionListService;
import jdbc.connection.ConnectionProvider;
import mvc.command.CommandHandler;

// 일용직근로자조회 화면의 HTTP 요청을 구분하고 적절한 서비스 호출과 화면 이동을 담당한다.
// 日雇い労働者照会画面のHTTPリクエストを判別し、適切なサービス呼び出しと画面遷移を担当する。
public class DayWorkerInquiryHandler implements CommandHandler{
	final private String FORM_VIEW = "/WEB-INF/view/attendance/day-worker-inquiry.jsp";
	
	
	@Override
	// 요청 방식과 작업 구분을 확인하여 일용직근로자조회 조회·저장 작업을 적절한 처리로 연결한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエスト方式と処理区分を確認し、日雇い労働者照会の照会・保存処理へ適切に振り分ける。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		if (req.getMethod().equalsIgnoreCase("GET")) {
			return processForm(req, res);
		}  else {
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
	}
	
	// 일용직근로자조회 화면에 필요한 데이터를 조회하여 request에 저장하고 JSP 경로를 반환한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// 日雇い労働者照会画面に必要なデータを照会してrequestへ保存し、JSPパスを返す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	public String processForm(HttpServletRequest req, HttpServletResponse res) throws Exception{
		String view = req.getParameter("view");
		if (view==null) {
			view="month";
		}

		DepartmentPositionService departmentPositionService = DepartmentPositionService.getInstance();
		req.setAttribute("departments", departmentPositionService.getDepartmentOptions());
		if ("month".equals(view)) {
			String yearParam = req.getParameter("year");
			String monthParam = req.getParameter("month");
			int year = (yearParam != null && !yearParam.isEmpty()) ? Integer.parseInt(yearParam)
					: LocalDate.now().getYear();
			int month = (monthParam != null && !monthParam.isEmpty()) ? Integer.parseInt(monthParam)
					: LocalDate.now().getMonthValue();
			req.setAttribute("year", year);
			req.setAttribute("month", month);
			String deptIdParam = req.getParameter("departmentId");
			String posIdParam = req.getParameter("jobPositionId");
			Integer departmentId = (deptIdParam!=null && !deptIdParam.isEmpty())?Integer.parseInt(deptIdParam):null;
			Integer jobPositionId = (posIdParam!=null && !posIdParam.isEmpty())?Integer.parseInt(posIdParam):null;
			req.setAttribute("departmentId", departmentId);
			req.setAttribute("jobPositionId", jobPositionId);
			DailyWorkListService dailyWorkListService = new DailyWorkListService();
			List<DailyWorkListDto> list = dailyWorkListService.list(new DailyWorkListRequest(year, month, departmentId, jobPositionId));
			req.setAttribute("dailyWorkList", list);
			JobPositionListService jobPositionListService = new JobPositionListService();
			req.setAttribute("jobPositions", jobPositionListService.list());
		} 
		//상세 조회
		// 照会条件または詳細入力値を受け取り、送信後も現在の画面状態を維持する領域である。
		else if ("detail".equals(view)) {
			req.setAttribute("view", view);
			


			//현장/프로젝트 목록
			// 対象一覧を順番に処理し、各行の入力値または照会結果を同じ基準で構成する。
			ProjectDao projectDao = ProjectDao.getInstance();
			try (Connection conn = ConnectionProvider.getConnection()){
			List<Project> projects = projectDao.selectAll(conn);
			req.setAttribute("projects", projects);
			}
			
			//파라미터 기반으로 리퀘스트 생성
			// リクエストから入力値を取得し、空白除去と形式変換を行って業務処理へ渡す。
			String startStr = req.getParameter("startDate");
			String endStr = req.getParameter("endDate");
			String empNameKr = req.getParameter("empNameKr");
			String departmentIdStr = req.getParameter("departmentId");
			String projectIdStr = req.getParameter("projectId");
			DailyWorkDetailRequest request = new DailyWorkDetailRequest();
			request.setEmpNameKr(empNameKr);
			Integer departmentId = (departmentIdStr!=null && !departmentIdStr.isEmpty())?Integer.parseInt(departmentIdStr):null;
		    Integer projectId = (projectIdStr!=null && !projectIdStr.isEmpty())?Integer.parseInt(projectIdStr):null;
		    request.setDepartmentId(departmentId);
		    request.setProjectId(projectId);
			if (startStr!=null && !startStr.trim().isEmpty()&&endStr!=null && !endStr.trim().isEmpty()) {
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");	
					Date startDate = sdf.parse(startStr);
					Date endDate = sdf.parse(endStr);
					/*
					 * if (endDate.before(startDate)) { startDate=null; endDate=null;
					 * req.setAttribute("dateError", "dateError"); }
					 */
					request.setStartDate(startDate);
					request.setEndDate(endDate);
					req.setAttribute("startDate", startDate!=null?startStr:null);
					req.setAttribute("endDate", endDate!=null?endStr:null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			
			//일용직 근무 기록 상세 조회
			// 照会条件または詳細入力値を受け取り、送信後も現在の画面状態を維持する領域である。
			/*
			 * DailyWorkDetailService dailyWorkDetailService = new DailyWorkDetailService();
			 * List<DailyWorkDetailDto> dailyWorkDetail =
			 * dailyWorkDetailService.search(request); req.setAttribute("dailyWorkDetail",
			 * dailyWorkDetail);
			 */
			String pageNoVal = req.getParameter("pageNo");
			int pageNo = 1;
			if (pageNoVal!=null&&!pageNoVal.trim().isEmpty()) {
				pageNo = Integer.parseInt(pageNoVal);
			}
			DailyWorkDetailListService listService = new DailyWorkDetailListService();
			DailyWorkDetailPage dailyWorkDetail = listService.getDetailPage(pageNo, request);
			req.setAttribute("dailyWorkDetail", dailyWorkDetail);

		}
		
		return FORM_VIEW;
	}
	
}
