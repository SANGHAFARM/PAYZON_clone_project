package erp.attendance.command;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.attendance.dao.ProjectDao;
import erp.attendance.dto.DailyWorkDetailDto;
import erp.attendance.dto.DailyWorkListDto;
import erp.attendance.model.Project;
import erp.attendance.service.DailyWorkDetailRequest;
import erp.attendance.service.DailyWorkDetailService;
import erp.attendance.service.DailyWorkListRequest;
import erp.attendance.service.DailyWorkListService;
import erp.settings.service.DepartmentPositionService;
import erp.settings.service.JobPositionListService;
import jdbc.connection.ConnectionProvider;
import mvc.command.CommandHandler;

public class DayWorkerInquiryHandler implements CommandHandler{
	final private String FORM_VIEW = "/WEB-INF/view/attendance/day-worker-inquiry-test.jsp";
	
	
	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		if (req.getMethod().equalsIgnoreCase("GET")) {
			return processForm(req, res);
		}  else {
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
	}
	
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
			req.setAttribute("dailyWorkList", dailyWorkListService.list(new DailyWorkListRequest(year, month, departmentId, jobPositionId)));
			JobPositionListService jobPositionListService = new JobPositionListService();
			req.setAttribute("jobPositions", jobPositionListService.list());
		} else if ("detail".equals(view)) {
			req.setAttribute("view", view);
			String startStr = req.getParameter("startDate");
			String endStr = req.getParameter("endDate");
			String empNameKr = req.getParameter("empNameKr");
			String departmentIdStr = req.getParameter("departmentId");
			String projectIdStr = req.getParameter("projectId");
			DailyWorkDetailRequest request = new DailyWorkDetailRequest();
			if (startStr!=null && !startStr.trim().isEmpty()&&endStr!=null && !endStr.trim().isEmpty()) {
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");	
					request.setStartDate(sdf.parse(startStr));
					request.setEndDate(sdf.parse(endStr));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			request.setEmpNameKr(empNameKr);
			Integer departmentId = (departmentIdStr!=null && !departmentIdStr.isEmpty())?Integer.parseInt(departmentIdStr):null;
		    Integer projectId = (projectIdStr!=null && !projectIdStr.isEmpty())?Integer.parseInt(projectIdStr):null;
		    request.setDepartmentId(departmentId);
		    request.setProjectId(projectId);
			req.setAttribute("departmentId", departmentId);
			req.setAttribute("projectId", projectId);
		    DailyWorkDetailService dailyWorkDetailService = new DailyWorkDetailService();
			List<DailyWorkDetailDto> dailyWorkDetail = dailyWorkDetailService.search(request);
			req.setAttribute("dailyWorkDetail", dailyWorkDetail);
			ProjectDao projectDao = ProjectDao.getInstance();
			List<Project> projects = projectDao.selectAll(ConnectionProvider.getConnection());
			req.setAttribute("projects", projects);
			

		}
		
		return FORM_VIEW;
	}
	
}
