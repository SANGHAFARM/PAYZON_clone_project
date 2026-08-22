package erp.payrollstats.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import erp.payrollstats.dto.PayrollCompositionStatPage.ChartItem;
import erp.payrollstats.dto.PayrollCompositionStatPage.StatItem;

// 급여항목구성비통계 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 給与項目構成比統計の業務ルールとデータ変更トランザクションを処理する。
public class PayrollItemCompositionStatService {

	public final String[] PAYMENT_COLORS = {"#075f9f", "#72b9e6", "#f1b65c", "#a58bd0", "#e88686", "#84a7bd", "#c3cf75", "#de94bd", "#89bdd7"};
	public final String[] DEDUCTION_COLORS = {"#ef4e00", "#f36f00", "#ffab24", "#ff8a00", "#ff9700", "#ffc15a", "#ffd89b"};
	private final DecimalFormat df = new DecimalFormat("#,###");

	// 조회 결과를 화면에서 사용할 차트데이터 구조로 집계하여 생성한다.
	// 여러 DAO와 입력값을 조합해 화면 또는 다음 업무 단계에서 바로 사용할 수 있는 결과 객체를 만든다.
	// 照会結果を画面で使用するチャートデータ構造へ集計して生成する。
	// 複数のDAO結果と入力値を組み合わせ、画面または次の業務段階で直ちに使用できる結果オブジェクトを作成する。
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

	// 조회 결과를 화면에서 사용할 요약정보차트데이터 구조로 집계하여 생성한다.
	// 여러 DAO와 입력값을 조합해 화면 또는 다음 업무 단계에서 바로 사용할 수 있는 결과 객체를 만든다.
	// 照会結果を画面で使用する集計情報チャートデータ構造へ集計して生成する。
	// 複数のDAO結果と入力値を組み合わせ、画面または次の業務段階で直ちに使用できる結果オブジェクトを作成する。
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
