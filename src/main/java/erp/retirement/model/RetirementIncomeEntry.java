package erp.retirement.model;

import java.util.Date;

/**
 * [퇴직관리] 퇴직급여 계산에 반영할 급여내역 및 기타 과세소득 행 Model DB 테이블: RETIREMENT_INCOME_ENTRY
 */
public class RetirementIncomeEntry {

	// [DB 관리 항목]
	private int retirementIncomeEntryId; // 퇴직급여 산정자료 식별 번호 (PK)
	private int retirementCalculationId; // 퇴직급여 계산내역 식별 번호 (FK)

	// [산정자료 구분]
	private String dataType; // 입력 영역 구분 (SALARY: 급여내역, ETC_INCOME: 기타 과세소득)

	// [급여내역 입력 항목]
	private Date periodStartDate; // 급여내역 산정기간 시작일 (null 허용)
	private Date periodEndDate; // 급여내역 산정기간 종료일 (null 허용)
	private Double calcDays; // 급여내역 산정일수 (소수점이 포함되므로 Double Wrapper 사용)

	// [기타 과세소득 입력 항목]
	private String payYm; // 기타 과세소득 지급년월 (null 허용)
	private String itemName; // 기타 과세소득 지급항목명 (null 허용)

	// [금액]
	private long amount; // 급여총액 또는 기타소득 금액
	private long threeMonthAmount; // 3개월분 환산 금액 (급여내역이면 0)

	public RetirementIncomeEntry() {
	}

	// [비즈니스 로직] 데이터 타입 확인
	public boolean isSalaryData() {
		return "SALARY".equals(this.dataType);
	}

	public boolean isEtcIncomeData() {
		return "ETC_INCOME".equals(this.dataType);
	}

	// Getter & Setter
	public int getRetirementIncomeEntryId() {
		return retirementIncomeEntryId;
	}

	public void setRetirementIncomeEntryId(int retirementIncomeEntryId) {
		this.retirementIncomeEntryId = retirementIncomeEntryId;
	}

	public int getRetirementCalculationId() {
		return retirementCalculationId;
	}

	public void setRetirementCalculationId(int retirementCalculationId) {
		this.retirementCalculationId = retirementCalculationId;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Date getPeriodStartDate() {
		return periodStartDate;
	}

	public void setPeriodStartDate(Date periodStartDate) {
		this.periodStartDate = periodStartDate;
	}

	public Date getPeriodEndDate() {
		return periodEndDate;
	}

	public void setPeriodEndDate(Date periodEndDate) {
		this.periodEndDate = periodEndDate;
	}

	public Double getCalcDays() {
		return calcDays;
	}

	public void setCalcDays(Double calcDays) {
		this.calcDays = calcDays;
	}

	public String getPayYm() {
		return payYm;
	}

	// HTML month 입력값 형식(YYYY-MM)으로 변환하여 JSP에 제공한다.
	public String getPayYmInput() {
		return payYm != null && payYm.length() == 6 ? payYm.substring(0, 4) + "-" + payYm.substring(4) : payYm;
	}

	public void setPayYm(String payYm) {
		this.payYm = payYm;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public long getThreeMonthAmount() {
		return threeMonthAmount;
	}

	public void setThreeMonthAmount(long threeMonthAmount) {
		this.threeMonthAmount = threeMonthAmount;
	}
}
