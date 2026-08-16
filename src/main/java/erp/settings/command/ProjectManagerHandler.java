package erp.settings.command;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import erp.settings.model.Project;
import erp.settings.service.ProjectDeleteService;
import erp.settings.service.ProjectInsertService;
import erp.settings.service.ProjectModifyService;

import mvc.command.CommandHandler;

public class ProjectManagerHandler implements CommandHandler {
	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		if (req.getMethod().equalsIgnoreCase("POST")) {
			return processSubmit(req, res);
		} else {
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}

	}

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
