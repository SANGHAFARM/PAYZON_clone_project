package erp.payroll.dto;

// 월별 급여 및 법정 공제 내역
public class EmployeePayrollHistoryItem {

	private String paymentMonth;
	private String paymentRound;
	private long standardMonthlyIncome;
	private long totalPayment;
	private long totalDeduction;
	private long nationalPension;
	private long healthInsurance;
	private long longTermCareInsurance;
	private long employmentInsurance;
	private long incomeTax;
	private long localIncomeTax;

	public String getPaymentMonth() { return paymentMonth; }
	public void setPaymentMonth(String paymentMonth) { this.paymentMonth = paymentMonth; }
	public String getPaymentRound() { return paymentRound; }
	public void setPaymentRound(String paymentRound) { this.paymentRound = paymentRound; }
	public long getStandardMonthlyIncome() { return standardMonthlyIncome; }
	public void setStandardMonthlyIncome(long value) { this.standardMonthlyIncome = value; }
	public long getTotalPayment() { return totalPayment; }
	public void setTotalPayment(long value) { this.totalPayment = value; }
	public long getTotalDeduction() { return totalDeduction; }
	public void setTotalDeduction(long value) { this.totalDeduction = value; }
	public long getNetPayment() { return totalPayment - totalDeduction; }
	public long getNationalPension() { return nationalPension; }
	public void setNationalPension(long value) { this.nationalPension = value; }
	public long getHealthInsurance() { return healthInsurance; }
	public void setHealthInsurance(long value) { this.healthInsurance = value; }
	public long getLongTermCareInsurance() { return longTermCareInsurance; }
	public void setLongTermCareInsurance(long value) { this.longTermCareInsurance = value; }
	public long getEmploymentInsurance() { return employmentInsurance; }
	public void setEmploymentInsurance(long value) { this.employmentInsurance = value; }
	public long getIncomeTax() { return incomeTax; }
	public void setIncomeTax(long value) { this.incomeTax = value; }
	public long getLocalIncomeTax() { return localIncomeTax; }
	public void setLocalIncomeTax(long value) { this.localIncomeTax = value; }
}
