package erp.payroll.dto;

// 항목별 대장에서 선택하는 지급 또는 공제 항목
public class PayrollItemLedgerOption {

	private String itemCode;
	private String itemName;

	public PayrollItemLedgerOption(String itemCode, String itemName) {
		this.itemCode = itemCode;
		this.itemName = itemName;
	}

	public String getItemCode() {
		return itemCode;
	}

	public String getItemName() {
		return itemName;
	}
}
