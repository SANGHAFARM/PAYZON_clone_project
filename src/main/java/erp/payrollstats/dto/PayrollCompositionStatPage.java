package erp.payrollstats.dto;

import java.util.List;

public class PayrollCompositionStatPage {

	private List<StatEmployee> employeeOptions;
	private List<ChartItem> paymentItems;
	private List<ChartItem> deductionItems;
	private List<ChartItem> summaryItems;
	private String totalPaymentText;
	private String totalDeductionText;
	private String netPaymentText;

	public List<StatEmployee> getEmployeeOptions() { return employeeOptions; }
	public void setEmployeeOptions(List<StatEmployee> value) { employeeOptions = value; }
	public List<ChartItem> getPaymentItems() { return paymentItems; }
	public void setPaymentItems(List<ChartItem> value) { paymentItems = value; }
	public List<ChartItem> getDeductionItems() { return deductionItems; }
	public void setDeductionItems(List<ChartItem> value) { deductionItems = value; }
	public List<ChartItem> getSummaryItems() { return summaryItems; }
	public void setSummaryItems(List<ChartItem> value) { summaryItems = value; }
	public String getTotalPaymentText() { return totalPaymentText; }
	public void setTotalPaymentText(String value) { totalPaymentText = value; }
	public String getTotalDeductionText() { return totalDeductionText; }
	public void setTotalDeductionText(String value) { totalDeductionText = value; }
	public String getNetPaymentText() { return netPaymentText; }
	public void setNetPaymentText(String value) { netPaymentText = value; }

	// 모달창 및 선택된 사원 정보
	public static class StatEmployee {
		private int employeeId;
		private String employeeNo;
		private String type;
		private String name;
		private String department;
		private String position;
		private String status;

		public int getEmployeeId() { return employeeId; }
		public void setEmployeeId(int value) { employeeId = value; }
		public String getEmployeeNo() { return employeeNo; }
		public void setEmployeeNo(String value) { employeeNo = value; }
		public String getType() { return type; }
		public void setType(String value) { type = value; }
		public String getName() { return name; }
		public void setName(String value) { name = value; }
		public String getDepartment() { return department; }
		public void setDepartment(String value) { department = value; }
		public String getPosition() { return position; }
		public void setPosition(String value) { position = value; }
		public String getStatus() { return status; }
		public void setStatus(String value) { status = value; }
	}

	// DB 원본 데이터
	public static class StatItem {
		private String itemName;
		private long amount;
		private String type;

		public String getItemName() { return itemName; }
		public void setItemName(String value) { itemName = value; }
		public long getAmount() { return amount; }
		public void setAmount(long value) { amount = value; }
		public String getType() { return type; }
		public void setType(String value) { type = value; }
	}

	// SVG 차트 렌더링 데이터
	public static class ChartItem {
		private String name;
		private String amountText;
		private String ratioText;
		private String color;
		private String ratioValue;
		private String dashOffset;
		private String labelLeft;
		private String labelTop;

		public String getName() { return name; }
		public void setName(String value) { name = value; }
		public String getAmountText() { return amountText; }
		public void setAmountText(String value) { amountText = value; }
		public String getRatioText() { return ratioText; }
		public void setRatioText(String value) { ratioText = value; }
		public String getColor() { return color; }
		public void setColor(String value) { color = value; }
		public String getRatioValue() { return ratioValue; }
		public void setRatioValue(String value) { ratioValue = value; }
		public String getDashOffset() { return dashOffset; }
		public void setDashOffset(String value) { dashOffset = value; }
		public String getLabelLeft() { return labelLeft; }
		public void setLabelLeft(String value) { labelLeft = value; }
		public String getLabelTop() { return labelTop; }
		public void setLabelTop(String value) { labelTop = value; }
	}
}