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
		if ("search".equals(req.getParameter("mode")) && condition.getKeyword().length() < 2) {
			// 검색 버튼 요청은 공백을 제외한 검색어가 두 글자 이상일 때만 조회한다.
			req.setAttribute("searchValidationMessage", "검색어를 2자 이상 입력해주세요.");
			condition.setKeyword("");
		}
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
			res.sendRedirect(req.getContextPath() + "/employees/employees.do");
			return null;
		}

		List<Integer> employeeIds;
		try {
			employeeIds = toEmployeeIds(selectedIds);
		} catch (IllegalArgumentException e) {
			req.getSession().setAttribute("employeeListMessage", e.getMessage());
			res.sendRedirect(req.getContextPath() + "/employees/employees.do");
			return null;
		}
		if ("requestDelete".equals(req.getParameter("action"))) {
			// 첫 요청에서는 삭제하지 않고 서버가 확인 화면을 표시한다.
			req.setAttribute("deleteConfirmation", true);
			req.setAttribute("deleteEmployeeIds", employeeIds);
			req.setAttribute("deleteEmployeeCount", employeeIds.size());
			return processList(req);
		}

		if ("confirmDelete".equals(req.getParameter("action"))) {
			try {
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

	private List<Integer> toEmployeeIds(String[] selectedIds) {
		List<Integer> employeeIds = new ArrayList<>();
		for (String selectedId : selectedIds) {
			try {
				employeeIds.add(Integer.valueOf(selectedId));
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("잘못된 사원번호가 포함되어 있습니다.", e);
			}
		}
		return employeeIds;
	}

	private String processColumnSetting(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String[] columns = req.getParameterValues("columns");
		req.getSession().setAttribute("employeeVisibleColumns",
				columns == null ? new ArrayList<String>() : Arrays.asList(columns));
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
