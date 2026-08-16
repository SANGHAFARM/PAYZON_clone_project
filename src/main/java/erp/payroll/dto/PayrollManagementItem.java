package erp.payroll.dto;

// 급여입력 화면에 표시할 지급·공제 항목을 전달한다.
public class PayrollManagementItem {

	private int itemCode;
	private String itemName;
	private boolean taxFree;
	private String calculationMethod;
	private long amount;

	public int getItemCode() {
		return itemCode;
	}

	public void setItemCode(int itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public boolean isTaxFree() {
		return taxFree;
	}

	public void setTaxFree(boolean taxFree) {
		this.taxFree = taxFree;
	}

	public String getCalculationMethod() {
		return calculationMethod;
	}

	public void setCalculationMethod(String calculationMethod) {
		this.calculationMethod = calculationMethod;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}
}
