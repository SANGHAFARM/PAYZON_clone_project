package erp.retirement.dto;

import java.util.ArrayList;
import java.util.List;

import erp.retirement.model.RetirementIncomeEntry;
import erp.retirement.model.RetirementTaxDeferral;

// 퇴직급여 계산 화면의 입력값과 계산 결과를 전달한다.
public class RetirementBenefitForm {

	private int calculationId;
	private int employeeId;
	private int serviceYears;
	private int serviceDays;
	private int excludedDays;
	private int taxYear;
	private String settlementType;
	private String startDate;
	private String endDate;
	private String paymentMethod;
	private String paymentDate;
	private long compensation;
	private long dismissalAllowance;
	private long taxFreeRetirement;
	private long prepaidTax;
	private long taxCredit;
	private long threeMonthTotal;
	private long salaryDaysTotal;
	private long salaryTotal;
	private long dailyAverage;
	private long dailyOrdinary;
	private long retirementIncome;
	private long calculatedTax;
	private long incomeTax;
	private long localIncomeTax;
	private long deferredIncomeTax;
	private long deferredLocalTax;
	private long ruralTax;
	private long otherDeduction;
	private long taxablePayment;
	private long withholdingTax;
	private long netPayment;
	private List<RetirementIncomeEntry> incomeEntries = new ArrayList<>();
	private List<RetirementTaxDeferral> taxDeferrals = new ArrayList<>();

	public int getCalculationId() {
		return calculationId;
	}

	public void setCalculationId(int calculationId) {
		this.calculationId = calculationId;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
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

	public int getExcludedDays() {
		return excludedDays;
	}

	public void setExcludedDays(int excludedDays) {
		this.excludedDays = excludedDays;
	}

	public int getTaxYear() {
		return taxYear;
	}

	public void setTaxYear(int taxYear) {
		this.taxYear = taxYear;
	}

	public String getSettlementType() {
		return settlementType;
	}

	public void setSettlementType(String settlementType) {
		this.settlementType = settlementType;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	public long getCompensation() {
		return compensation;
	}

	public void setCompensation(long compensation) {
		this.compensation = compensation;
	}

	public long getDismissalAllowance() {
		return dismissalAllowance;
	}

	public void setDismissalAllowance(long dismissalAllowance) {
		this.dismissalAllowance = dismissalAllowance;
	}

	public long getTaxFreeRetirement() {
		return taxFreeRetirement;
	}

	public void setTaxFreeRetirement(long taxFreeRetirement) {
		this.taxFreeRetirement = taxFreeRetirement;
	}

	public long getPrepaidTax() {
		return prepaidTax;
	}

	public void setPrepaidTax(long prepaidTax) {
		this.prepaidTax = prepaidTax;
	}

	public long getTaxCredit() {
		return taxCredit;
	}

	public void setTaxCredit(long taxCredit) {
		this.taxCredit = taxCredit;
	}

	public long getThreeMonthTotal() {
		return threeMonthTotal;
	}

	public void setThreeMonthTotal(long threeMonthTotal) {
		this.threeMonthTotal = threeMonthTotal;
	}

	public long getSalaryDaysTotal() {
		return salaryDaysTotal;
	}

	public void setSalaryDaysTotal(long salaryDaysTotal) {
		this.salaryDaysTotal = salaryDaysTotal;
	}

	public long getSalaryTotal() {
		return salaryTotal;
	}

	public void setSalaryTotal(long salaryTotal) {
		this.salaryTotal = salaryTotal;
	}

	public long getDailyAverage() {
		return dailyAverage;
	}

	public void setDailyAverage(long dailyAverage) {
		this.dailyAverage = dailyAverage;
	}

	public long getDailyOrdinary() {
		return dailyOrdinary;
	}

	public void setDailyOrdinary(long dailyOrdinary) {
		this.dailyOrdinary = dailyOrdinary;
	}

	public long getRetirementIncome() {
		return retirementIncome;
	}

	public void setRetirementIncome(long retirementIncome) {
		this.retirementIncome = retirementIncome;
	}

	public long getCalculatedTax() {
		return calculatedTax;
	}

	public void setCalculatedTax(long calculatedTax) {
		this.calculatedTax = calculatedTax;
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

	public long getRuralTax() {
		return ruralTax;
	}

	public void setRuralTax(long ruralTax) {
		this.ruralTax = ruralTax;
	}

	public long getOtherDeduction() {
		return otherDeduction;
	}

	public void setOtherDeduction(long otherDeduction) {
		this.otherDeduction = otherDeduction;
	}

	public long getTaxablePayment() {
		return taxablePayment;
	}

	public void setTaxablePayment(long taxablePayment) {
		this.taxablePayment = taxablePayment;
	}

	public long getWithholdingTax() {
		return withholdingTax;
	}

	public void setWithholdingTax(long withholdingTax) {
		this.withholdingTax = withholdingTax;
	}

	public long getNetPayment() {
		return netPayment;
	}

	public void setNetPayment(long netPayment) {
		this.netPayment = netPayment;
	}

	public List<RetirementIncomeEntry> getIncomeEntries() {
		return incomeEntries;
	}

	public List<RetirementTaxDeferral> getTaxDeferrals() {
		return taxDeferrals;
	}

	public List<RetirementIncomeEntry> getSalaryEntries() {
		List<RetirementIncomeEntry> result = new ArrayList<>();
		for (RetirementIncomeEntry entry : incomeEntries) {
			if (entry.isSalaryData()) {
				result.add(entry);
			}
		}
		return result;
	}

	public List<RetirementIncomeEntry> getOtherIncomeEntries() {
		List<RetirementIncomeEntry> result = new ArrayList<>();
		for (RetirementIncomeEntry entry : incomeEntries) {
			if (entry.isEtcIncomeData()) {
				result.add(entry);
			}
		}
		return result;
	}
}
