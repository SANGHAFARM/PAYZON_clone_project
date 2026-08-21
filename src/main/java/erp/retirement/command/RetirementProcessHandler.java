package erp.retirement.command;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.employees.service.EmployeeSearchCondition;
import erp.retirement.service.RetirementProcessService;
import erp.retirement.service.RetirementProcessService.RetirementEmployeePage;
import mvc.command.CommandHandler;

// 사원 퇴직처리 화면의 조회·처리·취소 요청을 처리한다.
public class RetirementProcessHandler implements CommandHandler {
	private static final String VIEW = "/WEB-INF/view/retirement/retirement-process.jsp";
	private final RetirementProcessService retirementService = new RetirementProcessService();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		if (req.getMethod().equalsIgnoreCase("GET")) {
			return processList(req);
		}
		if (req.getMethod().equalsIgnoreCase("POST")) {
			return processSubmit(req, res);
		}
		res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		return null;
	}

	private String processList(HttpServletRequest req) {
		String keyword = trim(req.getParameter("keyword"));
		if (req.getParameterMap().containsKey("keyword") && !keyword.isEmpty() && keyword.length() < 2) {
			req.setAttribute("retirementPopupMessage", "검색어를 2자 이상 입력해주세요");
		}
		EmployeeSearchCondition condition = createCondition(req);
		RetirementEmployeePage employeePage = retirementService.getEmployeePage(condition);
		req.setAttribute("employees", employeePage.getEmployees());
		req.setAttribute("pageInfo", employeePage.getPageInfo());
		req.setAttribute("retirementTypes", retirementService.getRetirementTypes());
		req.setAttribute("condition", condition);
		req.setAttribute("currentDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		if ("retired".equals(req.getParameter("result"))) {
			req.setAttribute("message", "퇴직처리를 완료했습니다.");
		}
		if ("cancelled".equals(req.getParameter("result"))) {
			req.setAttribute("message", "퇴직처리를 취소했습니다.");
		}
		return VIEW;
	}

	private String processSubmit(HttpServletRequest req, HttpServletResponse res) throws IOException {
		Integer employeeId = parseInteger(req.getParameter("employeeId"));
		if (employeeId == null) {
			req.setAttribute("message", "처리할 사원을 선택하세요.");
			return processList(req);
		}
		try {
			if ("CANCEL".equals(req.getParameter("processType"))) {
				retirementService.cancel(employeeId);
				redirect(req, res, "cancelled");
			} else {
				retirementService.retire(employeeId, trim(req.getParameter("retirementType")),
						parseDate(req.getParameter("retirementDate")), trim(req.getParameter("retirementReason")),
						trim(req.getParameter("afterContact")));
				redirect(req, res, "retired");
			}
			return null;
		} catch (IllegalArgumentException e) {
			req.setAttribute("message", e.getMessage());
			return processList(req);
		}
	}

	private EmployeeSearchCondition createCondition(HttpServletRequest req) {
		EmployeeSearchCondition condition = new EmployeeSearchCondition();
		String target = req.getParameter("searchTarget");
		String keyword = trim(req.getParameter("keyword")).length() >= 2
				? trim(req.getParameter("keyword")) : "";
		condition.setSearchTarget("employeeNo".equals(target) ? "EMPLOYEE_NO"
				: "department".equals(target) ? "DEPARTMENT" : "name".equals(target) ? "NAME" : "ALL");
		condition.setKeyword(keyword);
		condition.setEmploymentType("");
		String status = req.getParameter("status");
		condition.setStatus("ACTIVE".equals(status) ? "WORK" : "RETIRED".equals(status) ? "RETIRED" : "");
		condition.setPage(Math.max(1, intValue(req.getParameter("page"), 1)));
		condition.setPageSize(30);
		return condition;
	}

	private int intValue(String value, int defaultValue) {
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	private void redirect(HttpServletRequest req, HttpServletResponse res, String result) throws IOException {
		res.sendRedirect(req.getContextPath() + "/retirement/process.do?result=" + result);
	}
	private Date parseDate(String value) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false);
			return dateFormat.parse(value);
		}
		catch (Exception e) {
			return null;
		}
	}
	private Integer parseInteger(String value) {
		try {
			return Integer.valueOf(value);
		} catch (Exception e) {
			return null;
		}
	}
	private String trim(String value) { return value == null ? "" : value.trim(); }
}
