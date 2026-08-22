package erp.payrollstats.command;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.payrollstats.dto.PayrollStatItem;
import erp.payrollstats.service.PayrollStatService;
import mvc.command.CommandHandler;

// 월간급여통계 화면의 HTTP 요청을 구분하고 적절한 서비스 호출과 화면 이동을 담당한다.
// 月間給与統計画面のHTTPリクエストを判別し、適切なサービス呼び出しと画面遷移を担当する。
public class MonthlyPayrollStatHandler implements CommandHandler {
    private PayrollStatService statService = new PayrollStatService();

    @Override
    // 요청 방식과 작업 구분을 확인하여 월간급여통계 조회·저장 작업을 적절한 처리로 연결한다.
    // 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
    // リクエスト方式と処理区分を確認し、月間給与統計の照会・保存処理へ適切に振り分ける。
    // リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
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
        // 支給額・控除額・期間値を業務基準に従って計算し、合計と最終金額へ反映する。
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
        // 選択された画像識別値と保存パスを確認し、登録済み画像を各画面で再利用できるようにする。
        return "/WEB-INF/view/payroll-stats/monthly-payroll-statistics.jsp";
    }
}
