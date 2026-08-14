package erp.employees.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.employees.service.EmployeeListService;
import erp.employees.service.EmployeeListService.EmployeeListResult;
import erp.employees.service.EmployeeSearchCondition;
import mvc.command.CommandHandler;

// 사원현황/관리 화면의 조회와 목록 설정 요청을 처리하는 Handler
public class EmployeeListHandler implements CommandHandler {

	private static final String VIEW = "/WEB-INF/view/employees/employees-list.jsp";
	private EmployeeListService employeeListService = new EmployeeListService();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String uri = req.getRequestURI();
		if (uri.endsWith("/employee-action.do") && req.getMethod().equalsIgnoreCase("POST")) {
			return processEmployeeAction(req, res);
		} else if (uri.endsWith("/employee-columns.do") && req.getMethod().equalsIgnoreCase("POST")) {
			return processColumnSetting(req, res);
		} else if (!req.getMethod().equalsIgnoreCase("GET")) {
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
		return processList(req);
	}

	private String processList(HttpServletRequest req) {
		EmployeeSearchCondition condition = createCondition(req);
		EmployeeListResult result = employeeListService.getEmployeeList(condition);
		req.setAttribute("employees", result.getEmployees());
		req.setAttribute("employeeSummary", result.getSummary());
		req.setAttribute("pageInfo", result.getPageInfo());
		req.setAttribute("employmentTypes", employeeListService.getEmploymentTypes());
		req.setAttribute("condition", condition);
		req.setAttribute("visibleColumns", getVisibleColumns(req));

		Object message = req.getSession().getAttribute("employeeListMessage");
		if (message != null) {
			req.setAttribute("message", message);
			req.getSession().removeAttribute("employeeListMessage");
		}
		return VIEW;
	}

	private String processEmployeeAction(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String[] selectedIds = req.getParameterValues("employeeIds");
		if (selectedIds == null || selectedIds.length == 0) {
			req.getSession().setAttribute("employeeListMessage", "삭제할 사원을 선택해주세요.");
		} else {
			try {
				List<Integer> employeeIds = new ArrayList<>();
				for (String selectedId : selectedIds) {
					employeeIds.add(Integer.valueOf(selectedId));
				}
				int deletedCount = employeeListService.deleteEmployees(employeeIds);
				req.getSession().setAttribute("employeeListMessage",
						deletedCount + "명의 사원정보를 삭제했습니다.");
			} catch (RuntimeException e) {
				req.getSession().setAttribute("employeeListMessage", e.getMessage());
			}
		}
		res.sendRedirect(req.getContextPath() + "/employees/employees.do");
		return null;
	}

	private String processColumnSetting(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String[] columns = req.getParameterValues("columns");
		req.getSession().setAttribute("employeeVisibleColumns",
				columns == null ? new ArrayList<String>() : Arrays.asList(columns));
		req.getSession().setAttribute("employeeListMessage", "표시항목 설정을 저장했습니다.");
		res.sendRedirect(req.getContextPath() + "/employees/employees.do");
		return null;
	}

	@SuppressWarnings("unchecked")
	private List<String> getVisibleColumns(HttpServletRequest req) {
		List<String> columns = (List<String>) req.getSession().getAttribute("employeeVisibleColumns");
		if (columns == null) {
			columns = Arrays.asList("employmentType", "joinDate", "employeeNo", "name", "department",
					"position", "residentNo", "mobile", "email", "retirementDate", "status");
		}
		return columns;
	}

	private EmployeeSearchCondition createCondition(HttpServletRequest req) {
		EmployeeSearchCondition condition = new EmployeeSearchCondition();
		condition.setSearchTarget(valueOrDefault(req.getParameter("searchTarget"), "ALL"));
		condition.setKeyword(trim(req.getParameter("keyword")));
		condition.setEmploymentType(trim(req.getParameter("employmentType")));
		condition.setStatus(req.getParameter("status") == null ? "WORK" : trim(req.getParameter("status")));
		condition.setPage(parsePositiveInt(req.getParameter("page"), 1));
		int pageSize = parsePositiveInt(req.getParameter("pageSize"), 30);
		condition.setPageSize(pageSize == 10 || pageSize == 30 || pageSize == 50 || pageSize == 100 ? pageSize : 30);
		return condition;
	}

	private String valueOrDefault(String value, String defaultValue) {
		String trimmed = trim(value);
		return trimmed.isEmpty() ? defaultValue : trimmed;
	}

	private String trim(String value) {
		return value == null ? "" : value.trim();
	}

	private int parsePositiveInt(String value, int defaultValue) {
		try {
			int parsed = Integer.parseInt(value);
			return parsed > 0 ? parsed : defaultValue;
		} catch (Exception e) {
			return defaultValue;
		}
	}
}
