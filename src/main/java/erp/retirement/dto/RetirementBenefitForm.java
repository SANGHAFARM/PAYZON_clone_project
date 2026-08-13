package erp.retirement.dto;

import java.util.ArrayList;
import java.util.List;
import erp.retirement.model.RetirementIncomeEntry;
import erp.retirement.model.RetirementTaxDeferral;

// 퇴직급여 계산 폼과 저장할 상세 행을 함께 전달하는 DTO
public class RetirementBenefitForm {
	private int calculationId, employeeId, serviceYears, serviceDays, excludedDays, taxYear;
	private String settlementType, startDate, endDate, paymentMethod, paymentDate;
	private long compensation, dismissalAllowance, taxFreeRetirement, prepaidTax, taxCredit,
			threeMonthTotal, salaryDaysTotal, salaryTotal, dailyAverage, dailyOrdinary, retirementIncome,
			calculatedTax, incomeTax, localIncomeTax, deferredIncomeTax, deferredLocalTax, ruralTax,
			otherDeduction, taxablePayment, withholdingTax, netPayment;
	private List<RetirementIncomeEntry> incomeEntries = new ArrayList<>();
	private List<RetirementTaxDeferral> taxDeferrals = new ArrayList<>();
	public int getCalculationId(){return calculationId;} public void setCalculationId(int v){calculationId=v;}
	public int getEmployeeId(){return employeeId;} public void setEmployeeId(int v){employeeId=v;}
	public int getServiceYears(){return serviceYears;} public void setServiceYears(int v){serviceYears=v;}
	public int getServiceDays(){return serviceDays;} public void setServiceDays(int v){serviceDays=v;}
	public int getExcludedDays(){return excludedDays;} public void setExcludedDays(int v){excludedDays=v;}
	public int getTaxYear(){return taxYear;} public void setTaxYear(int v){taxYear=v;}
	public String getSettlementType(){return settlementType;} public void setSettlementType(String v){settlementType=v;}
	public String getStartDate(){return startDate;} public void setStartDate(String v){startDate=v;}
	public String getEndDate(){return endDate;} public void setEndDate(String v){endDate=v;}
	public String getPaymentMethod(){return paymentMethod;} public void setPaymentMethod(String v){paymentMethod=v;}
	public String getPaymentDate(){return paymentDate;} public void setPaymentDate(String v){paymentDate=v;}
	public long getCompensation(){return compensation;} public void setCompensation(long v){compensation=v;}
	public long getDismissalAllowance(){return dismissalAllowance;} public void setDismissalAllowance(long v){dismissalAllowance=v;}
	public long getTaxFreeRetirement(){return taxFreeRetirement;} public void setTaxFreeRetirement(long v){taxFreeRetirement=v;}
	public long getPrepaidTax(){return prepaidTax;} public void setPrepaidTax(long v){prepaidTax=v;}
	public long getTaxCredit(){return taxCredit;} public void setTaxCredit(long v){taxCredit=v;}
	public long getThreeMonthTotal(){return threeMonthTotal;} public void setThreeMonthTotal(long v){threeMonthTotal=v;}
	public long getSalaryDaysTotal(){return salaryDaysTotal;} public void setSalaryDaysTotal(long v){salaryDaysTotal=v;}
	public long getSalaryTotal(){return salaryTotal;} public void setSalaryTotal(long v){salaryTotal=v;}
	public long getDailyAverage(){return dailyAverage;} public void setDailyAverage(long v){dailyAverage=v;}
	public long getDailyOrdinary(){return dailyOrdinary;} public void setDailyOrdinary(long v){dailyOrdinary=v;}
	public long getRetirementIncome(){return retirementIncome;} public void setRetirementIncome(long v){retirementIncome=v;}
	public long getCalculatedTax(){return calculatedTax;} public void setCalculatedTax(long v){calculatedTax=v;}
	public long getIncomeTax(){return incomeTax;} public void setIncomeTax(long v){incomeTax=v;}
	public long getLocalIncomeTax(){return localIncomeTax;} public void setLocalIncomeTax(long v){localIncomeTax=v;}
	public long getDeferredIncomeTax(){return deferredIncomeTax;} public void setDeferredIncomeTax(long v){deferredIncomeTax=v;}
	public long getDeferredLocalTax(){return deferredLocalTax;} public void setDeferredLocalTax(long v){deferredLocalTax=v;}
	public long getRuralTax(){return ruralTax;} public void setRuralTax(long v){ruralTax=v;}
	public long getOtherDeduction(){return otherDeduction;} public void setOtherDeduction(long v){otherDeduction=v;}
	public long getTaxablePayment(){return taxablePayment;} public void setTaxablePayment(long v){taxablePayment=v;}
	public long getWithholdingTax(){return withholdingTax;} public void setWithholdingTax(long v){withholdingTax=v;}
	public long getNetPayment(){return netPayment;} public void setNetPayment(long v){netPayment=v;}
	public List<RetirementIncomeEntry> getIncomeEntries(){return incomeEntries;}
	public List<RetirementTaxDeferral> getTaxDeferrals(){return taxDeferrals;}
	public List<RetirementIncomeEntry> getSalaryEntries(){List<RetirementIncomeEntry> result=new ArrayList<>();for(RetirementIncomeEntry e:incomeEntries)if(e.isSalaryData())result.add(e);return result;}
	public List<RetirementIncomeEntry> getOtherIncomeEntries(){List<RetirementIncomeEntry> result=new ArrayList<>();for(RetirementIncomeEntry e:incomeEntries)if(e.isEtcIncomeData())result.add(e);return result;}
}
