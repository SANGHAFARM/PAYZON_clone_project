package erp.payrollstats.command;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.payrollstats.service.PersonalAnnualStatService;
import mvc.command.CommandHandler;

public class PersonalAnnualStatHandler implements CommandHandler {

    private PersonalAnnualStatService statService = new PersonalAnnualStatService();

    @Override
    public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
        
        req.setCharacterEncoding("UTF-8");

        String baseYear = req.getParameter("baseYear");
        String employeeNo = req.getParameter("employeeNo");

        // 검색어 파라미터 체크 (한글 깨짐 방지 포함)
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

        // Service 호출
        Map<String, Object> pageData = statService.getAnnualStatPage(baseYear, employeeNo, employeeKeyword);

        // JSP로 넘길 속성 설정
        req.setAttribute("availableYears", pageData.get("availableYears"));
        req.setAttribute("selectedYear", pageData.get("selectedYear"));
        req.setAttribute("selectedEmployeeNo", pageData.get("selectedEmployeeNo"));
        req.setAttribute("selectedEmployeeName", pageData.get("selectedEmployeeName"));
        
        if (pageData.containsKey("employeeOptions")) {
            req.setAttribute("employeeOptions", pageData.get("employeeOptions"));
        }

        // 💡 통계 데이터 JSP로 전달
        if (pageData.containsKey("salaryStats")) {
            req.setAttribute("salaryStats", pageData.get("salaryStats"));
        }

        return "/WEB-INF/view/payroll-stats/personal-annual-salary-statistics.jsp";
    }
}