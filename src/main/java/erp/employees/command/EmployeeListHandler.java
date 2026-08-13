package erp.employees.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.employees.service.EmployeeListService;
import erp.employees.service.EmployeeListService.EmployeeListResult;
import erp.employees.service.EmployeeSearchCondition;
import mvc.command.CommandHandler;

// 사원현황/관리 화면의 검색 조건을 받고 조회 결과를 JSP로 전달하는 Handler
public class EmployeeListHandler implements CommandHandler {
	private static final String VIEW = "/WEB-INF/view/employees/employees-list.jsp";
	private final EmployeeListService employeeListService = new EmployeeListService();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) {
		// 요청 파라미터를 검색조건으로 변환한 뒤 목록, 집계, 페이징 정보를 조회한다.
		EmployeeSearchCondition condition = createCondition(req);
		EmployeeListResult result = employeeListService.getEmployeeList(condition);

		req.setAttribute("employees", result.getEmployees());
		req.setAttribute("employeeSummary", result.getSummary());
		req.setAttribute("pageInfo", result.getPageInfo());
		req.setAttribute("employmentTypes", employeeListService.getEmploymentTypes());
		req.setAttribute("condition", condition);
		return VIEW;
	}

	private EmployeeSearchCondition createCondition(HttpServletRequest req) {
		// 최초 접속 시 참고 홈페이지와 동일하게 재직 사원 30명을 기본 조회한다.
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

	private String trim(String value) { return value == null ? "" : value.trim(); }
	private int parsePositiveInt(String value, int defaultValue) {
		// 잘못된 숫자 파라미터가 들어와도 화면이 중단되지 않도록 기본값을 사용한다.
		try { int parsed = Integer.parseInt(value); return parsed > 0 ? parsed : defaultValue; }
		catch (Exception e) { return defaultValue; }
	}
}
