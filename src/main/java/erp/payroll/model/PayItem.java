package erp.payroll.model;

// PAY_ITEM: 지급항목
public class PayItem {
	private int payItemId;
	private String payName;
	private String taxType;
	private String taxFreeCode;
	private long taxFreeLimit;
	private String calcMethod;
	private int roundUnit;
	private String payMethod;
	private Integer linkAttendId;
	private Long bulkPayAmount;
	private String useYn;

	// 기본 생성자
	public PayItem() {
	}

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

	public long getTaxFreeLimit() {
		return taxFreeLimit;
	}

	public void setTaxFreeLimit(long taxFreeLimit) {
		this.taxFreeLimit = taxFreeLimit;
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