package erp.payroll.dto;

// 급여대장 상세의 지급·공제 항목 열 정보
public class PayrollRegisterColumn {

	private int itemId;
	private String itemName;

	public PayrollRegisterColumn(int itemId, String itemName) {
		this.itemId = itemId;
		this.itemName = itemName;
	}

	public int getItemId() {
		return itemId;
	}

	public String getItemName() {
		return itemName;
	}
}
