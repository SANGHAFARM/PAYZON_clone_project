package erp.payrollstats.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.*;

import erp.payrollstats.dao.PayrollItemCompositionStatDao;
import erp.payrollstats.dao.PayrollStatDao; // ⭐ 보조 요리사(Dao) 임포트 추가!
import erp.payrollstats.service.PayrollItemCompositionStatService;
import erp.payrollstats.dto.PayrollCompositionStatPage;
import erp.payrollstats.dto.PayrollCompositionStatPage.*;
import jdbc.connection.ConnectionProvider;
import mvc.command.CommandHandler;

// 급여항목구성비통계 화면의 HTTP 요청을 구분하고 적절한 서비스 호출과 화면 이동을 담당한다.
// 給与項目構成比統計画面のHTTPリクエストを判別し、適切なサービス呼び出しと画面遷移を担当する。
public class PayrollItemCompositionStatHandler implements CommandHandler {

	private PayrollItemCompositionStatService statService = new PayrollItemCompositionStatService();
	private PayrollItemCompositionStatDao statDao = new PayrollItemCompositionStatDao();

	@Override
	// 요청 방식과 작업 구분을 확인하여 급여항목구성비통계 조회·저장 작업을 적절한 처리로 연결한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエスト方式と処理区分を確認し、給与項目構成比統計の照会・保存処理へ適切に振り分ける。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		DecimalFormat df = new DecimalFormat("#,###");
		
		String baseYearStr = req.getParameter("baseYear");
		String baseMonthStr = req.getParameter("baseMonth");
		String employeeIdStr = req.getParameter("employeeId");
		
		Calendar cal = Calendar.getInstance();
		int baseYear = (baseYearStr == null || baseYearStr.trim().isEmpty()) ? cal.get(Calendar.YEAR) : Integer.parseInt(baseYearStr);
		int baseMonth = (baseMonthStr == null || baseMonthStr.trim().isEmpty()) ? cal.get(Calendar.MONTH) + 1 : Integer.parseInt(baseMonthStr);

		// 월(Month)은 고정이므로 그대로 유지합니다. (연도 하드코딩 삭제 완료)
		// 基準日と期間の境界を確認し、照会・計算に使用できる日付形式へ変換する。
		req.setAttribute("availableMonths", Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"));
		req.setAttribute("selectedYear", baseYear);
		req.setAttribute("selectedMonth", String.format("%02d", baseMonth));

		try (Connection conn = ConnectionProvider.getConnection()) {
			
			// ⭐ [수정된 부분] DB에서 연도 목록을 직접 캐옵니다 ⭐
			// 基準日と期間の境界を確認し、照会・計算に使用できる日付形式へ変換する。
			PayrollStatDao commonDao = new PayrollStatDao();
			List<Integer> availableYears = commonDao.selectAvailableYears(conn);
			
			// 만약 DB에 데이터가 없으면 기본값으로 2026년을 세팅합니다.
			// アプリケーション起動時または初回表示時に必要な初期値と共通オブジェクトを準備する。
			if (availableYears == null || availableYears.isEmpty()) {
				availableYears = new ArrayList<>();
				availableYears.add(2026);
			}
			req.setAttribute("availableYears", availableYears);
			// -------------------------------------------------------------

			String empKeyword = req.getParameter("employeeKeyword");
			List<StatEmployee> empList = statDao.selectEmployeeSearchList(conn, empKeyword);
			req.setAttribute("employeeOptions", empList);

			if (employeeIdStr != null && !employeeIdStr.trim().isEmpty()) {
				int employeeId = Integer.parseInt(employeeIdStr);
				req.setAttribute("selectedEmployeeId", employeeId);
				
				for (StatEmployee emp : empList) {
					if (employeeId == emp.getEmployeeId()) {
						req.setAttribute("selectedEmployeeName", emp.getName());
						req.setAttribute("selectedEmployeeNo", emp.getEmployeeNo());
						break;
					}
				}
				
				PayrollCompositionStatPage pageData = new PayrollCompositionStatPage();
				List<StatItem> allItems = statDao.selectCompositionItems(conn, baseYear, baseMonth, employeeId);
				
				List<StatItem> pItems = new ArrayList<>();
				List<StatItem> dItems = new ArrayList<>();
				long totalPayment = 0, totalDeduction = 0;

				for (StatItem item : allItems) {
					if ("P".equals(item.getType())) {
						pItems.add(item);
						totalPayment += item.getAmount();
					} else {
						dItems.add(item);
						totalDeduction += item.getAmount();
					}
				}

				pageData.setPaymentItems(statService.generateChartData(pItems, totalPayment, statService.PAYMENT_COLORS));
				pageData.setDeductionItems(statService.generateChartData(dItems, totalDeduction, statService.DEDUCTION_COLORS));
				pageData.setSummaryItems(statService.generateSummaryChartData(totalPayment, totalDeduction));

				pageData.setTotalPaymentText(df.format(totalPayment));
				pageData.setTotalDeductionText(df.format(totalDeduction));
				pageData.setNetPaymentText(df.format(totalPayment - totalDeduction));

				req.setAttribute("paymentItems", pageData.getPaymentItems());
				req.setAttribute("deductionItems", pageData.getDeductionItems());
				req.setAttribute("summaryItems", pageData.getSummaryItems());
				req.setAttribute("totalPaymentText", pageData.getTotalPaymentText());
				req.setAttribute("totalDeductionText", pageData.getTotalDeductionText());
				req.setAttribute("netPaymentText", pageData.getNetPaymentText());
			}
		}
		return "/WEB-INF/view/payroll-stats/payroll-item-composition-statistics.jsp";
	}
}
