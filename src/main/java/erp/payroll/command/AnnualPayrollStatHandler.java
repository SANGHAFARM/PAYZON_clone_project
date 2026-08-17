package erp.payroll.command;

import java.util.Calendar;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.payroll.dto.PayrollStatItem;
import erp.payroll.service.PayrollStatService;
import mvc.command.CommandHandler;

public class AnnualPayrollStatHandler implements CommandHandler {
    private PayrollStatService statService = new PayrollStatService();

    @Override
    public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String baseYearParam = req.getParameter("baseYear");
        int baseYear = Calendar.getInstance().get(Calendar.YEAR); // 기본값 현재 연도
        
        if (baseYearParam != null && !baseYearParam.trim().isEmpty()) {
            baseYear = Integer.parseInt(baseYearParam);
        }

        List<Integer> availableYears = statService.getAvailableYears();
        if (availableYears.isEmpty()) { availableYears.add(baseYear); }

        List<PayrollStatItem> annualStats = statService.getAnnualStatistics(baseYear);

        req.setAttribute("availableYears", availableYears);
        req.setAttribute("selectedYear", baseYear);
        req.setAttribute("statisticsStartYear", baseYear - 9);
        req.setAttribute("statisticsEndYear", baseYear);
        req.setAttribute("annualStats", annualStats);

        // ※ 프로젝트의 실제 뷰 경로에 맞게 적어주세요.
        return "/WEB-INF/view/payroll-stats/annual-payroll-statistics.jsp"; 
    }
}