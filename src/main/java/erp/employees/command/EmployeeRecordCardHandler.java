package erp.employees.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.employees.service.EmployeeRecordCardService;
import erp.employees.service.EmployeeRecordCardService.EmployeeRecordCardData;
import erp.employees.service.EmployeeSearchCondition;
import mvc.command.CommandHandler;

// 인사기록카드의 사원 검색과 상세 조회 요청을 처리한다.
public class EmployeeRecordCardHandler implements CommandHandler {
	private static final String VIEW = "/WEB-INF/view/employees/employees-record-card.jsp";
	private final EmployeeRecordCardService recordCardService = new EmployeeRecordCardService();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) {
		if (!req.getMethod().equalsIgnoreCase("GET")) {
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}

		// 사원 선택창에서 사용할 부서, 직위, 검색어 조건을 구성한다.
		EmployeeSearchCondition condition = new EmployeeSearchCondition();
		condition.setSearchTarget("ALL");
		String keyword = trim(req.getParameter("keyword"));
		Integer departmentId = parseInteger(req.getParameter("departmentId"));
		Integer positionId = parseInteger(req.getParameter("positionId"));
		condition.setKeyword(keyword);
		condition.setEmploymentType("");
		condition.setStatus("");
		condition.setPage(1);
		condition.setPageSize(10000);
		condition.setDepartmentId(departmentId);
		condition.setPositionId(positionId);

		String mode = trim(req.getParameter("mode"));
		boolean hasSearchCondition = departmentId != null || positionId != null || !keyword.isEmpty();
		if ("search".equals(mode) && !hasSearchCondition) {
			// 검색 조건이 없으면 결과창 대신 조건 입력 안내창을 표시한다.
			req.setAttribute("searchConditionMessage", "검색 조건을 하나 이상 설정해주세요.");
		} else if ("search".equals(mode) || "all".equals(mode)) {
			req.setAttribute("showEmployeeModal", true);
		}

		// 기록카드 한 화면에 필요한 모든 이력을 조회한다.
		EmployeeRecordCardData data = recordCardService.getRecordCard(parseInteger(req.getParameter("employeeId")), condition);
		// JSP의 각 표에서 바로 사용할 수 있도록 항목별 request 속성으로 저장한다.
		req.setAttribute("selectedEmployee", data.getEmployee());
		req.setAttribute("employees", data.getEmployees());
		req.setAttribute("departments", data.getDepartments());
		req.setAttribute("positions", data.getPositions());
		req.setAttribute("company", data.getCompany());
		req.setAttribute("families", data.getFamilies());
		req.setAttribute("insurances", data.getInsurances());
		req.setAttribute("educations", data.getEducations());
		req.setAttribute("careers", data.getCareers());
		req.setAttribute("licenses", data.getLicenses());
		req.setAttribute("languages", data.getLanguages());
		req.setAttribute("trainings", data.getTrainings());
		req.setAttribute("awards", data.getAwards());
		req.setAttribute("appointments", data.getAppointments());
		req.setAttribute("retirementCalculations", data.getRetirementCalculations());
		return VIEW;
	}

	private Integer parseInteger(String value) {
		// 선택값이 없거나 숫자가 아니면 조건을 적용하지 않는다.
		try { return value == null || value.trim().isEmpty() ? null : Integer.valueOf(value); }
		catch (NumberFormatException e) {
			return null;
		}
	}
	private String trim(String value) { return value == null ? "" : value.trim(); }
}
