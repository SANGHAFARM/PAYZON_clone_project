package erp.retire.model;

import java.util.Date;

// RETIRE_CALC_MST: 퇴직급여 계산
public class RetireCalcMst {
	private int retireCalcMstId;
	private int empId;
	private String calcType;
	private Date calcStartDate;
	private Date retireDate;
	private int serviceYears;
	private int serviceDays;
	private int excludeDays;
	private long compensationAmt;
	private long dismissalAmt;
	private long taxFreeRetireAmt;
	private long prepaidTaxAmt;
	private long taxCreditAmt;
	private long threeMonthTotal;
	private long avgMonthWage;
	private long avgDayWage;
	private long ordinaryDayWage;
	private long retireIncome;
	private long calculatedTaxAmt;
	private long incomeTax;
	private long localIncomeTax;
	private long deferredIncomeTax;
	private long deferredLocalTax;
	private long specialRuralTax;
	private long otherDeductAmt;
	private long taxableRetireAmt;
	private long withholdingTaxAmt;
	private long actualPayAmt;
	private String payMethod;
	private Date payDate;

	// 기본 생성자
	public RetireCalcMst() {
	}

	public int getRetireCalcMstId() {
		return retireCalcMstId;
	}

	public void setRetireCalcMstId(int retireCalcMstId) {
		this.retireCalcMstId = retireCalcMstId;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
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