package erp.retirement.model;

import java.util.Date;

/**
 * [퇴직관리] 사원별 퇴직급여 산정, 퇴직소득세, 지급정보 마스터 Model DB 테이블: RETIREMENT_CALCULATION
 */
public class RetirementCalculation {

	// [DB 관리 항목]
	private Long retirementCalculationId; // 퇴직급여 계산내역 식별 번호 (PK)
	private Long employeeId; // 퇴직 대상 사원 식별 번호 (FK)

	// [퇴직급여 계산구분]
	private String calcType; // 구분 (퇴직정산 / 중간정산)

	// [입사일·퇴직일·근속기간]
	private Date calcStartDate; // 입사일 (정산 시작일)
	private Date retireDate; // 퇴직일
	private Integer serviceYears; // 근속년수
	private Integer serviceDays; // 근속일수
	private Integer excludeDays; // 제외일수

	// [추가 지급·공제 입력액]
	private Long compensationAmt; // 퇴직위로금
	private Long dismissalAmt; // 해고예고수당
	private Long taxFreeRetireAmt; // 비과세 퇴직급여
	private Long prepaidTaxAmt; // 기납부(또는 기과세이연) 세액
	private Long taxCreditAmt; // 세액공제

	// [평균임금·퇴직급여 계산결과]
	private Long threeMonthTotal; // 3개월 급여 총계
	private Long avgMonthWage; // 평균임금 (월)
	private Long avgDayWage; // 1일 평균임금
	private Long ordinaryDayWage; // 1일 통상임금

	// [퇴직소득세 계산과정]
	private Long retireIncome; // 퇴직소득
	private Long calculatedTaxAmt; // 산출세액

	// [세금·공제 및 실지급액]
	private Long incomeTax; // 퇴직소득세
	private Long localIncomeTax; // 지방소득세
	private Long deferredIncomeTax; // 이연 퇴직소득세
	private Long deferredLocalTax; // 이연 지방소득세
	private Long specialRuralTax; // 농어촌특별세
	private Long otherDeductAmt; // 기타공제
	private Long taxableRetireAmt; // 과세대상 퇴직급여 (자동 계산)
	private Long withholdingTaxAmt; // 차감원천징수세액 (자동 계산)
	private Long actualPayAmt; // 실수령액 (자동 계산)

	// [지급정보]
	private String payMethod; // 지급방법 (현금, 계좌이체 등)
	private Date payDate; // 지급일

	public RetirementCalculation() {
	}

	// Getter & Setter (요약형)
	public Long getRetirementCalculationId() {
		return retirementCalculationId;
	}

	public void setRetirementCalculationId(Long retirementCalculationId) {
		this.retirementCalculationId = retirementCalculationId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
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

	public Integer getServiceYears() {
		return serviceYears;
	}

	public void setServiceYears(Integer serviceYears) {
		this.serviceYears = serviceYears;
	}

	public Integer getServiceDays() {
		return serviceDays;
	}

	public void setServiceDays(Integer serviceDays) {
		this.serviceDays = serviceDays;
	}

	public Integer getExcludeDays() {
		return excludeDays;
	}

	public void setExcludeDays(Integer excludeDays) {
		this.excludeDays = excludeDays;
	}

	public Long getCompensationAmt() {
		return compensationAmt;
	}

	public void setCompensationAmt(Long compensationAmt) {
		this.compensationAmt = compensationAmt;
	}

	public Long getDismissalAmt() {
		return dismissalAmt;
	}

	public void setDismissalAmt(Long dismissalAmt) {
		this.dismissalAmt = dismissalAmt;
	}

	public Long getTaxFreeRetireAmt() {
		return taxFreeRetireAmt;
	}

	public void setTaxFreeRetireAmt(Long taxFreeRetireAmt) {
		this.taxFreeRetireAmt = taxFreeRetireAmt;
	}

	public Long getPrepaidTaxAmt() {
		return prepaidTaxAmt;
	}

	public void setPrepaidTaxAmt(Long prepaidTaxAmt) {
		this.prepaidTaxAmt = prepaidTaxAmt;
	}

	public Long getTaxCreditAmt() {
		return taxCreditAmt;
	}

	public void setTaxCreditAmt(Long taxCreditAmt) {
		this.taxCreditAmt = taxCreditAmt;
	}

	public Long getThreeMonthTotal() {
		return threeMonthTotal;
	}

	public void setThreeMonthTotal(Long threeMonthTotal) {
		this.threeMonthTotal = threeMonthTotal;
	}

	public Long getAvgMonthWage() {
		return avgMonthWage;
	}

	public void setAvgMonthWage(Long avgMonthWage) {
		this.avgMonthWage = avgMonthWage;
	}

	public Long getAvgDayWage() {
		return avgDayWage;
	}

	public void setAvgDayWage(Long avgDayWage) {
		this.avgDayWage = avgDayWage;
	}

	public Long getOrdinaryDayWage() {
		return ordinaryDayWage;
	}

	public void setOrdinaryDayWage(Long ordinaryDayWage) {
		this.ordinaryDayWage = ordinaryDayWage;
	}

	public Long getRetireIncome() {
		return retireIncome;
	}

	public void setRetireIncome(Long retireIncome) {
		this.retireIncome = retireIncome;
	}

	public Long getCalculatedTaxAmt() {
		return calculatedTaxAmt;
	}

	public void setCalculatedTaxAmt(Long calculatedTaxAmt) {
		this.calculatedTaxAmt = calculatedTaxAmt;
	}

	public Long getIncomeTax() {
		return incomeTax;
	}

	public void setIncomeTax(Long incomeTax) {
		this.incomeTax = incomeTax;
	}

	public Long getLocalIncomeTax() {
		return localIncomeTax;
	}

	public void setLocalIncomeTax(Long localIncomeTax) {
		this.localIncomeTax = localIncomeTax;
	}

	public Long getDeferredIncomeTax() {
		return deferredIncomeTax;
	}

	public void setDeferredIncomeTax(Long deferredIncomeTax) {
		this.deferredIncomeTax = deferredIncomeTax;
	}

	public Long getDeferredLocalTax() {
		return deferredLocalTax;
	}

	public void setDeferredLocalTax(Long deferredLocalTax) {
		this.deferredLocalTax = deferredLocalTax;
	}

	public Long getSpecialRuralTax() {
		return specialRuralTax;
	}

	public void setSpecialRuralTax(Long specialRuralTax) {
		this.specialRuralTax = specialRuralTax;
	}

	public Long getOtherDeductAmt() {
		return otherDeductAmt;
	}

	public void setOtherDeductAmt(Long otherDeductAmt) {
		this.otherDeductAmt = otherDeductAmt;
	}

	public Long getTaxableRetireAmt() {
		return taxableRetireAmt;
	}

	public void setTaxableRetireAmt(Long taxableRetireAmt) {
		this.taxableRetireAmt = taxableRetireAmt;
	}

	public Long getWithholdingTaxAmt() {
		return withholdingTaxAmt;
	}

	public void setWithholdingTaxAmt(Long withholdingTaxAmt) {
		this.withholdingTaxAmt = withholdingTaxAmt;
	}

	public Long getActualPayAmt() {
		return actualPayAmt;
	}

	public void setActualPayAmt(Long actualPayAmt) {
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