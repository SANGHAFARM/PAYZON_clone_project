package erp.settings.model;

/**
 * [기본환경설정] 급여 지급항목 설정 Model DB 테이블: PAY_ITEM
 */
public class PayItem {

	private int payItemId; // 지급항목 식별 번호 (PK)
	private String payName;
	private String taxType;
	private String calcMethod;
	private String payMethod;
	private String useYn;

	private String taxFreeCode; // 비과세 코드 외래키
	private Long taxFreeLimit; // 비과세 한도
	private Integer roundUnit; // 절사단위
	private Integer linkAttendId; // 근태항목 외래키
	private Long bulkPayAmount; // 일괄지급액

	public PayItem() {
	}

	// Getter & Setter
	public int getPayItemId() {
		return payItemId;
	}

	public void setPayItemId(int payItemId) {
		this.payItemId = payItemId;
	}

	public String getPayName() {
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

	public String getTaxType() {
		return taxType;
	}

	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}

	public String getTaxFreeCode() {
		return taxFreeCode;
	}

	public void setTaxFreeCode(String taxFreeCode) {
		this.taxFreeCode = taxFreeCode;
	}

	public Long getTaxFreeLimit() {
		return taxFreeLimit;
	}

	public void setTaxFreeLimit(Long taxFreeLimit) {
		this.taxFreeLimit = taxFreeLimit;
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

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public Integer getLinkAttendId() {
		return linkAttendId;
	}

	public void setLinkAttendId(Integer linkAttendId) {
		this.linkAttendId = linkAttendId;
	}

	public Long getBulkPayAmount() {
		return bulkPayAmount;
	}

	public void setBulkPayAmount(Long bulkPayAmount) {
		this.bulkPayAmount = bulkPayAmount;
	}

	public String getUseYn() {
		return useYn;
	}

	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
}