package erp.payroll.command;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.payroll.dto.PayrollStatItem;
import erp.payroll.service.PayrollStatService;
import mvc.command.CommandHandler;

public class MonthlyPayrollStatHandler implements CommandHandler {
    private PayrollStatService statService = new PayrollStatService();

    @Override
    public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String baseYearParam = req.getParameter("baseYear");
        int baseYear = Calendar.getInstance().get(Calendar.YEAR);
        
        if (baseYearParam != null && !baseYearParam.trim().isEmpty()) {
            baseYear = Integer.parseInt(baseYearParam);
        }

        List<Integer> availableYears = statService.getAvailableYears();
        if (availableYears.isEmpty()) { availableYears.add(baseYear); }

        List<PayrollStatItem> monthlyStats = statService.getMonthlyStatistics(baseYear);

        // 연간 합계 및 평균 구하기
        long totalPayYear = 0;
        double totalHeadcount = 0;
        for (PayrollStatItem item : monthlyStats) {
            totalPayYear += item.getRawTotalPay();
            totalHeadcount += item.getRawHeadcount();
        }

        DecimalFormat commaFormat = new DecimalFormat("#,###");
        DecimalFormat floatFormat = new DecimalFormat("#,##0.0");

        req.setAttribute("availableYears", availableYears);
        req.setAttribute("selectedYear", baseYear);
        req.setAttribute("monthlyStats", monthlyStats);
        req.setAttribute("totalPayrollYearText", commaFormat.format(totalPayYear / 1000));
        req.setAttribute("averageHeadcountYearText", floatFormat.format(totalHeadcount / 12.0));

        // ※ 프로젝트의 실제 뷰 경로에 맞게 적어주세요.
        return "/WEB-INF/view/payroll-stats/monthly-payroll-statistics.jsp";
    }
}