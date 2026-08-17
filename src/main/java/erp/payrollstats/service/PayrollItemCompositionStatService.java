package erp.payrollstats.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import erp.payrollstats.dto.PayrollCompositionStatPage.ChartItem;
import erp.payrollstats.dto.PayrollCompositionStatPage.StatItem;

public class PayrollItemCompositionStatService {

	public final String[] PAYMENT_COLORS = {"#075f9f", "#72b9e6", "#f1b65c", "#a58bd0", "#e88686", "#84a7bd", "#c3cf75", "#de94bd", "#89bdd7"};
	public final String[] DEDUCTION_COLORS = {"#ef4e00", "#f36f00", "#ffab24", "#ff8a00", "#ff9700", "#ffc15a", "#ffd89b"};
	private final DecimalFormat df = new DecimalFormat("#,###");

	public List<ChartItem> generateChartData(List<StatItem> items, long totalAmount, String[] colors) {
		List<ChartItem> resultData = new ArrayList<>();
		if (totalAmount == 0) return resultData;

		double accumulatedRatio = 0.0;
		int colorIndex = 0;

		for (StatItem item : items) {
			ChartItem chartItem = new ChartItem();
			double ratio = (double) item.getAmount() / totalAmount * 100.0;
			
			chartItem.setName(item.getItemName());
			chartItem.setAmountText(df.format(item.getAmount()));
			chartItem.setRatioText(String.format("%.1f%%", ratio));
			chartItem.setColor(colors[colorIndex % colors.length]);
			
			chartItem.setRatioValue(String.format("%.1f", ratio));
			chartItem.setDashOffset(String.format("%.1f", accumulatedRatio));
			
			if (ratio > 0) {
				double midRatio = accumulatedRatio + (ratio / 2.0);
				double angleInRadians = (midRatio / 100.0 * 360 - 90) * (Math.PI / 180);
				int labelLeft = (int) (50 + 32 * Math.cos(angleInRadians));
				int labelTop = (int) (50 + 32 * Math.sin(angleInRadians));
				chartItem.setLabelLeft(String.valueOf(labelLeft));
				chartItem.setLabelTop(String.valueOf(labelTop));
			} else {
				chartItem.setLabelLeft("50");
				chartItem.setLabelTop("50");
			}
			
			accumulatedRatio += ratio;
			colorIndex++;
			resultData.add(chartItem);
		}
		return resultData;
	}

	public List<ChartItem> generateSummaryChartData(long totalPayment, long totalDeduction) {
		List<StatItem> summaryItems = new ArrayList<>();
		
		StatItem payItem = new StatItem();
		payItem.setItemName("지급항목");
		payItem.setAmount(totalPayment);
		summaryItems.add(payItem);
		
		StatItem dedItem = new StatItem();
		dedItem.setItemName("공제항목");
		dedItem.setAmount(totalDeduction);
		summaryItems.add(dedItem);
		
		String[] summaryColors = {"#149bd7", "#ff8a00"};
		return generateChartData(summaryItems, totalPayment + totalDeduction, summaryColors);
	}
}