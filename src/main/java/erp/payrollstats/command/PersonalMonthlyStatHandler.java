package erp.payrollstats.command;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.payrollstats.service.PersonalMonthlyStatService;
import mvc.command.CommandHandler;

// 개인별월간통계 화면의 HTTP 요청을 구분하고 적절한 서비스 호출과 화면 이동을 담당한다.
// 個人別月間統計画面のHTTPリクエストを判別し、適切なサービス呼び出しと画面遷移を担当する。
public class PersonalMonthlyStatHandler implements CommandHandler {

    private PersonalMonthlyStatService statService = new PersonalMonthlyStatService();

    @Override
    // 요청 방식과 작업 구분을 확인하여 개인별월간통계 조회·저장 작업을 적절한 처리로 연결한다.
    // 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
    // リクエスト方式と処理区分を確認し、個人別月間統計の照会・保存処理へ適切に振り分ける。
    // リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
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
        // 識別された既存データへ変更値を反映し、未変更項目は従来の値を維持する。
        if (pageData.containsKey("monthlySalaryStats")) {
            req.setAttribute("monthlySalaryStats", pageData.get("monthlySalaryStats"));
            req.setAttribute("totalSalaryYearText", pageData.get("totalSalaryYearText"));
            req.setAttribute("totalDeductionYearText", pageData.get("totalDeductionYearText"));
            req.setAttribute("totalNetYearText", pageData.get("totalNetYearText"));
        }

        return "/WEB-INF/view/payroll-stats/personal-monthly-salary-statistics.jsp";
    }
}
