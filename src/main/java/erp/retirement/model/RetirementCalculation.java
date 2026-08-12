package erp.retirement.model;

import java.util.Date;

/**
 * [퇴직관리] 사원별 퇴직급여 산정, 퇴직소득세, 지급정보 마스터 Model DB 테이블: RETIREMENT_CALCULATION
 */
public class RetirementCalculation {

	// [DB 관리 항목]
	private int retirementCalculationId; // 퇴직급여 계산내역 식별 번호 (PK)
	private int employeeId; // 퇴직 대상 사원 식별 번호 (FK)

	// [퇴직급여 계산구분]
	private String calcType; // 구분 (퇴직정산 / 중간정산)

	// [입사일·퇴직일·근속기간]
	private Date calcStartDate; // 입사일 (정산 시작일)
	private Date retireDate; // 퇴직일
	private int serviceYears; // 근속년수
	private int serviceDays; // 근속일수
	private int excludeDays; // 제외일수

	// [추가 지급·공제 입력액]
	private long compensationAmt; // 퇴직위로금
	private long dismissalAmt; // 해고예고수당
	private long taxFreeRetireAmt; // 비과세 퇴직급여
	private long prepaidTaxAmt; // 기납부(또는 기과세이연) 세액
	private long taxCreditAmt; // 세액공제

	// [평균임금·퇴직급여 계산결과]
	private long threeMonthTotal; // 3개월 급여 총계
	private long avgMonthWage; // 평균임금 (월)
	private long avgDayWage; // 1일 평균임금
	private long ordinaryDayWage; // 1일 통상임금

	// [퇴직소득세 계산과정]
	private long retireIncome; // 퇴직소득
	private long calculatedTaxAmt; // 산출세액

	// [세금·공제 및 실지급액]
	private long incomeTax; // 퇴직소득세
	private long localIncomeTax; // 지방소득세
	private long deferredIncomeTax; // 이연 퇴직소득세
	private long deferredLocalTax; // 이연 지방소득세
	private long specialRuralTax; // 농어촌특별세
	private long otherDeductAmt; // 기타공제
	private long taxableRetireAmt; // 과세대상 퇴직급여 (자동 계산)
	private long withholdingTaxAmt; // 차감원천징수세액 (자동 계산)
	private long actualPayAmt; // 실수령액 (자동 계산)

	// [지급정보]
	private String payMethod; // 지급방법 (현금, 계좌이체 등)
	private Date payDate; // 지급일

	public RetirementCalculation() {
	}

	// Getter & Setter
	public int getRetirementCalculationId() {
		return retirementCalculationId;
	}

	public void setRetirementCalculationId(int retirementCalculationId) {
		this.retirementCalculationId = retirementCalculationId;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getCalcType() {
		return calcType;
	}

	public void setCalcType(String calcType) {
		this.calcType = calcType;
	}

	public Date getCalcStartDate() {
		return calcStartDate;
	}

	public void setCalcStartDate(Date calcStartDate) {
		this.calcStartDate = calcStartDate;
	}

	public Date getRetireDate() {
		return retireDate;
	}

	public void setRetireDate(Date retireDate) {
		this.retireDate = retireDate;
	}

	public int getServiceYears() {
		return serviceYears;
	}

	public void setServiceYears(int serviceYears) {
		this.serviceYears = serviceYears;
	}

	public int getServiceDays() {
		return serviceDays;
	}

	public void setServiceDays(int serviceDays) {
		this.serviceDays = serviceDays;
	}

	public int getExcludeDays() {
		return excludeDays;
	}

	public void setExcludeDays(int excludeDays) {
		this.excludeDays = excludeDays;
	}

	public long getCompensationAmt() {
		return compensationAmt;
	}

	public void setCompensationAmt(long compensationAmt) {
		this.compensationAmt = compensationAmt;
	}

	public long getDismissalAmt() {
		return dismissalAmt;
	}

	public void setDismissalAmt(long dismissalAmt) {
		this.dismissalAmt = dismissalAmt;
	}

	public long getTaxFreeRetireAmt() {
		return taxFreeRetireAmt;
	}

	public void setTaxFreeRetireAmt(long taxFreeRetireAmt) {
		this.taxFreeRetireAmt = taxFreeRetireAmt;
	}

	public long getPrepaidTaxAmt() {
		return prepaidTaxAmt;
	}

	public void setPrepaidTaxAmt(long prepaidTaxAmt) {
		this.prepaidTaxAmt = prepaidTaxAmt;
	}

	public long getTaxCreditAmt() {
		return taxCreditAmt;
	}

	public void setTaxCreditAmt(long taxCreditAmt) {
		this.taxCreditAmt = taxCreditAmt;
	}

	public long getThreeMonthTotal() {
		return threeMonthTotal;
	}

	public void setThreeMonthTotal(long threeMonthTotal) {
		this.threeMonthTotal = threeMonthTotal;
	}

	public long getAvgMonthWage() {
		return avgMonthWage;
	}

	public void setAvgMonthWage(long avgMonthWage) {
		this.avgMonthWage = avgMonthWage;
	}

	public long getAvgDayWage() {
		return avgDayWage;
	}

	public void setAvgDayWage(long avgDayWage) {
		this.avgDayWage = avgDayWage;
	}

	public long getOrdinaryDayWage() {
		return ordinaryDayWage;
	}

	public void setOrdinaryDayWage(long ordinaryDayWage) {
		this.ordinaryDayWage = ordinaryDayWage;
	}

	public long getRetireIncome() {
		return retireIncome;
	}

	public void setRetireIncome(long retireIncome) {
		this.retireIncome = retireIncome;
	}

	public long getCalculatedTaxAmt() {
		return calculatedTaxAmt;
	}

	public void setCalculatedTaxAmt(long calculatedTaxAmt) {
		this.calculatedTaxAmt = calculatedTaxAmt;
	}

	public long getIncomeTax() {
		return incomeTax;
	}

	public void setIncomeTax(long incomeTax) {
		this.incomeTax = incomeTax;
	}

	public long getLocalIncomeTax() {
		return localIncomeTax;
	}

	public void setLocalIncomeTax(long localIncomeTax) {
		this.localIncomeTax = localIncomeTax;
	}

	public long getDeferredIncomeTax() {
		return deferredIncomeTax;
	}

	public void setDeferredIncomeTax(long deferredIncomeTax) {
		this.deferredIncomeTax = deferredIncomeTax;
	}

	public long getDeferredLocalTax() {
		return deferredLocalTax;
	}

	public void setDeferredLocalTax(long deferredLocalTax) {
		this.deferredLocalTax = deferredLocalTax;
	}

	public long getSpecialRuralTax() {
		return specialRuralTax;
	}

	public void setSpecialRuralTax(long specialRuralTax) {
		this.specialRuralTax = specialRuralTax;
	}

	public long getOtherDeductAmt() {
		return otherDeductAmt;
	}

	public void setOtherDeductAmt(long otherDeductAmt) {
		this.otherDeductAmt = otherDeductAmt;
	}

	public long getTaxableRetireAmt() {
		return taxableRetireAmt;
	}

	public void setTaxableRetireAmt(long taxableRetireAmt) {
		this.taxableRetireAmt = taxableRetireAmt;
	}

	public long getWithholdingTaxAmt() {
		return withholdingTaxAmt;
	}

	public void setWithholdingTaxAmt(long withholdingTaxAmt) {
		this.withholdingTaxAmt = withholdingTaxAmt;
	}

	public long getActualPayAmt() {
		return actualPayAmt;
	}

	public void setActualPayAmt(long actualPayAmt) {
		this.actualPayAmt = actualPayAmt;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
}