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

public class PayrollItemCompositionStatHandler implements CommandHandler {

	private PayrollItemCompositionStatService statService = new PayrollItemCompositionStatService();
	private PayrollItemCompositionStatDao statDao = new PayrollItemCompositionStatDao();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		DecimalFormat df = new DecimalFormat("#,###");
		
		String baseYearStr = req.getParameter("baseYear");
		String baseMonthStr = req.getParameter("baseMonth");
		String employeeIdStr = req.getParameter("employeeId");
		
		Calendar cal = Calendar.getInstance();
		int baseYear = (baseYearStr == null || baseYearStr.trim().isEmpty()) ? cal.get(Calendar.YEAR) : Integer.parseInt(baseYearStr);
		int baseMonth = (baseMonthStr == null || baseMonthStr.trim().isEmpty()) ? cal.get(Calendar.MONTH) + 1 : Integer.parseInt(baseMonthStr);

		// 월(Month)은 고정이므로 그대로 유지합니다. (연도 하드코딩 삭제 완료)
		req.setAttribute("availableMonths", Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"));
		req.setAttribute("selectedYear", baseYear);
		req.setAttribute("selectedMonth", String.format("%02d", baseMonth));

		try (Connection conn = ConnectionProvider.getConnection()) {
			
			// ⭐ [수정된 부분] DB에서 연도 목록을 직접 캐옵니다 ⭐
			PayrollStatDao commonDao = new PayrollStatDao();
			List<Integer> availableYears = commonDao.selectAvailableYears(conn);
			
			// 만약 DB에 데이터가 없으면 기본값으로 2026년을 세팅합니다.
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