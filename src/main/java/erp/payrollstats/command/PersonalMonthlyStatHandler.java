package erp.payrollstats.command;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.payrollstats.service.PersonalMonthlyStatService;
import mvc.command.CommandHandler;

public class PersonalMonthlyStatHandler implements CommandHandler {

    private PersonalMonthlyStatService statService = new PersonalMonthlyStatService();

    @Override
    public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
        
        req.setCharacterEncoding("UTF-8");

        String baseYear = req.getParameter("baseYear");
        String employeeNo = req.getParameter("employeeNo");

        String employeeKeyword = req.getParameter("employeeKeyword");
        if (employeeKeyword == null || employeeKeyword.trim().isEmpty()) {
            employeeKeyword = req.getParameter("keyword");
        }
        if (employeeKeyword == null || employeeKeyword.trim().isEmpty()) {
            employeeKeyword = req.getParameter("searchKeyword");
        }
        if (employeeKeyword == null || employeeKeyword.trim().isEmpty()) {
            employeeKeyword = req.getParameter("empName");
        }

        if (employeeKeyword != null && !employeeKeyword.trim().isEmpty()) {
            try {
                String decoded = new String(employeeKeyword.getBytes("ISO-8859-1"), "UTF-8");
                if (decoded.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*") || decoded.startsWith("EMP")) {
                    employeeKeyword = decoded;
                }
            } catch (Exception e) {}
        }

        Map<String, Object> pageData = statService.getMonthlyStatPage(baseYear, employeeNo, employeeKeyword);

        req.setAttribute("availableYears", pageData.get("availableYears"));
        req.setAttribute("selectedYear", pageData.get("selectedYear"));
        req.setAttribute("selectedEmployeeNo", pageData.get("selectedEmployeeNo"));
        req.setAttribute("selectedEmployeeName", pageData.get("selectedEmployeeName"));
        
        if (pageData.containsKey("employeeOptions")) {
            req.setAttribute("employeeOptions", pageData.get("employeeOptions"));
        }

        // 💡 수정된 부분: Service에서 넘겨주는 이름(monthlySalaryStats)과 정확히 일치시켰습니다.
        if (pageData.containsKey("monthlySalaryStats")) {
            req.setAttribute("monthlySalaryStats", pageData.get("monthlySalaryStats"));
            req.setAttribute("totalSalaryYearText", pageData.get("totalSalaryYearText"));
            req.setAttribute("totalDeductionYearText", pageData.get("totalDeductionYearText"));
            req.setAttribute("totalNetYearText", pageData.get("totalNetYearText"));
        }

        return "/WEB-INF/view/payroll-stats/personal-monthly-salary-statistics.jsp";
    }
}