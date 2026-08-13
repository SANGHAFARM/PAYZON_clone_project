package erp.settings.dto;

/**
 * [기본환경설정] 급여 지급항목 상세 정보 및 연관 명칭 포함 응답 DTO 화면 그리드(목록) 및 상세 폼 조회 시 사용
 */
public class PayItemResponseDto {

	// [PAY_ITEM 기본 정보]
	private int payItemId; // 지급항목 식별 번호 (PK)
	private String payName; // 지급항목명
	private String taxType; // 과세구분 (과세, 비과세 등)
	private String taxFreeCode; // 비과세 코드 (외래키)
	private Long taxFreeLimit; // 비과세 한도 금액
	private String calcMethod; // 계산방법
	private Integer roundUnit; // 절사단위
	private String payMethod; // 지급방법
	private Integer linkAttendId; // 연동 근태항목 ID (외래키)
	private Long bulkPayAmount; // 일괄지급액
	private String useYn; // 사용여부 (Y/N)

	// [조인(Join)을 통해 가져오는 연관 명칭 데이터]
	private String taxFreeName; // 비과세 코드에 해당하는 실제 비과세 명칭 (예: 식대, 자가운전보조금)
	private String attendName; // 연동된 근태항목 ID에 해당하는 실제 근태 명칭 (예: 연장근로)

	public PayItemResponseDto() {
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