package erp.settings.dto;

/**
 * [기본환경설정] 급여항목 설정 - 지급항목 목록 출력용 DTO PAY_ITEM, TAX_FREE_ITEM, ATTENDANCE_ITEM
 * 테이블 조인 결과 매핑
 */
public class PayItemRow {

	// [PAY_ITEM 테이블 기본 컬럼]
	private int payItemId;
	private String payName;
	private String taxType;
	private String taxFreeCode;
	private Long taxFreeLimit;
	private String calcMethod;
	private Integer roundUnit;
	private String payMethod;
	private Integer linkAttendId;
	private Long bulkPayAmount;
	private String useYn;

	// [조인(Join)을 통해 가져오는 추가 컬럼]
	private String taxFreeName; // TAX_FREE_ITEM 테이블의 비과세명
	private String attendName; // ATTENDANCE_ITEM 테이블의 근태항목명

	public PayItemRow() {
	}

	// ==========================================
	// Getter 및 Setter 구현부
	// ==========================================

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

	public String getTaxFreeName() {
		return taxFreeName;
	}

	public void setTaxFreeName(String taxFreeName) {
		this.taxFreeName = taxFreeName;
	}

	public String getAttendName() {
		return attendName;
	}

	public void setAttendName(String attendName) {
		this.attendName = attendName;
	}
}