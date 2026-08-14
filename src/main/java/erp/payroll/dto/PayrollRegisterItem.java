package erp.payroll.dto;

import java.util.Date;

// 급여대장 목록의 급여 회차별 집계 정보
public class PayrollRegisterItem {

	private int registerId;
	private String paymentYear;
	private String paymentYearMonth;
	private String paymentRoundName;
	private String incomeType;
	private Date calculationStart;
	private Date calculationEnd;
	private Date paymentDate;
	private int employeeCount;
	private long totalPayment;
	private long totalDeduction;

	public int getRegisterId() {
		return registerId;
	}

	public void setRegisterId(int registerId) {
		this.registerId = registerId;
	}

	public String getPaymentYear() {
		return paymentYear;
	}

	public void setPaymentYear(String paymentYear) {
		this.paymentYear = paymentYear;
	}

	public String getPaymentYearMonth() {
		return paymentYearMonth;
	}

	public void setPaymentYearMonth(String paymentYearMonth) {
		this.paymentYearMonth = paymentYearMonth;
	}

	public String getPaymentRoundName() {
		return paymentRoundName;
	}

	public void setPaymentRoundName(String paymentRoundName) {
		this.paymentRoundName = paymentRoundName;
	}

	public String getIncomeType() {
		return incomeType;
	}

	public void setIncomeType(String incomeType) {
		this.incomeType = incomeType;
	}

	public Date getCalculationStart() {
		return calculationStart;
	}

	public void setCalculationStart(Date calculationStart) {
		this.calculationStart = calculationStart;
	}

	public Date getCalculationEnd() {
		return calculationEnd;
	}

	public void setCalculationEnd(Date calculationEnd) {
		this.calculationEnd = calculationEnd;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public int getEmployeeCount() {
		return employeeCount;
	}

	public void setEmployeeCount(int employeeCount) {
		this.employeeCount = employeeCount;
	}

	public long getTotalPayment() {
		return totalPayment;
	}

	public void setTotalPayment(long totalPayment) {
		this.totalPayment = totalPayment;
	}

	public long getTotalDeduction() {
		return totalDeduction;
	}

	public void setTotalDeduction(long totalDeduction) {
		this.totalDeduction = totalDeduction;
	}

	public long getNetPayment() {
		return totalPayment - totalDeduction;
	}
}
