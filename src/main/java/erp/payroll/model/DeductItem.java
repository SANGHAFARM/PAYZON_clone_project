package erp.payroll.model;

// DEDUCT_ITEM: 공제항목
public class DeductItem {
	private int deductItemId;
	private String deductName;
	private String calcMethod;
	private int roundUnit;
	private String note;
	private String useYn;

	// 기본 생성자
	public DeductItem() {
	}

	public int getDeductItemId() {
		return deductItemId;
	}

	public void setDeductItemId(int deductItemId) {
		this.deductItemId = deductItemId;
	}

	public String getDeductName() {
		return deductName;
	}

	public void setDeductName(String deductName) {
		this.deductName = deductName;
	}

	public String getCalcMethod() {
		return calcMethod;
	}

	public void setCalcMethod(String calcMethod) {
		this.calcMethod = calcMethod;
	}

	public int getRoundUnit() {
		return roundUnit;
	}

	public void setRoundUnit(int roundUnit) {
		this.roundUnit = roundUnit;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getUseYn() {
		return useYn;
	}

	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
}