package erp.settings.model;

/**
 * [기본환경설정] 급여 공제항목 설정 Model DB 테이블: DEDUCT_ITEM
 */
public class DeductItem {

	private int deductItemId; // 공제항목 식별 번호 (PK)
	private String deductName;
	private String calcMethod;
	private String useYn;

	private Integer roundUnit; // 절사단위
	private String note;

	public DeductItem() {
	}

	// Getter & Setter
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

	public Integer getRoundUnit() {
		return roundUnit;
	}

	public void setRoundUnit(Integer roundUnit) {
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