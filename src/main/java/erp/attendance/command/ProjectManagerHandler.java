package erp.attendance.command;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.attendance.model.Project;
import erp.attendance.service.ProjectDeleteService;
import erp.attendance.service.ProjectInsertService;
import erp.attendance.service.ProjectModifyService;
import mvc.command.CommandHandler;

// 현장·프로젝트관리 화면의 HTTP 요청을 구분하고 적절한 서비스 호출과 화면 이동을 담당한다.
// 現場・プロジェクト管理画面のHTTPリクエストを判別し、適切なサービス呼び出しと画面遷移を担当する。
public class ProjectManagerHandler implements CommandHandler {
	@Override
	// 요청 방식과 작업 구분을 확인하여 현장·프로젝트관리 조회·저장 작업을 적절한 처리로 연결한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエスト方式と処理区分を確認し、現場・プロジェクト管理の照会・保存処理へ適切に振り分ける。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		if (req.getMethod().equalsIgnoreCase("POST")) {
			return processSubmit(req, res);
		} else {
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}

	}

	// 현장·프로젝트관리 입력 요청을 검증한 뒤 서비스에 저장을 위임하고 처리 결과를 전달한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// 現場・プロジェクト管理の入力リクエストを検証し、サービスへ保存を委譲して処理結果を渡す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	private String processSubmit(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String projectAction = req.getParameter("projectAction");
		if ("add".equals(projectAction)) {
			String projectName = req.getParameter("projectName");
			ProjectInsertService projectInsertService = new ProjectInsertService();
			projectInsertService.insert(projectName);
		} else if ("edit".equals(projectAction)) {
			int projectId = Integer.parseInt(req.getParameter("projectId"));
			String projectName = req.getParameter("projectName");
			ProjectModifyService projectModifyService = new ProjectModifyService();
			projectModifyService.modify(new Project(projectId, projectName));

		} else if ("delete".equals(projectAction)) {
			int projectId = Integer.parseInt(req.getParameter("projectId"));
			ProjectDeleteService projectDeleteService = new ProjectDeleteService();
			projectDeleteService.delete(projectId);
		}
		res.sendRedirect(req.getContextPath() + "/attendance/day-worker-management.do#project-manager");
		return null; // 컨트롤러에서 직접 응답을 처리한 경우
	}

}
