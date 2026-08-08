package erp.payroll.model;

import java.util.Date;

// PAYROLL_MASTER: 월별 급여계산 회차
public class PayrollMaster {
	private int payrollMstId;
	private String payYear;
	private String payMonth;
	private String paySeq;
	private String incomeType;
	private Date calcStartDate;
	private Date calcEndDate;
	private Date payDate;

	// 기본 생성자
	public PayrollMaster() {
	}

	public int getPayrollMstId() {
		return payrollMstId;
	}

	public void setPayrollMstId(int payrollMstId) {
		this.payrollMstId = payrollMstId;
	}

	public String getPayYear() {
		return payYear;
	}

	public void setPayYear(String payYear) {
		this.payYear = payYear;
	}

	public String getPayMonth() {
		return payMonth;
	}

	public void setPayMonth(String payMonth) {
		this.payMonth = payMonth;
	}

	public String getPaySeq() {
		return paySeq;
	}

	public void setPaySeq(String paySeq) {
		this.paySeq = paySeq;
	}

	public String getIncomeType() {
		return incomeType;
	}

	public void setIncomeType(String incomeType) {
		this.incomeType = incomeType;
	}

	public Date getCalcStartDate() {
		return calcStartDate;
	}

	public void setCalcStartDate(Date calcStartDate) {
		this.calcStartDate = calcStartDate;
	}

	public Date getCalcEndDate() {
		return calcEndDate;
	}

	public void setCalcEndDate(Date calcEndDate) {
		this.calcEndDate = calcEndDate;
	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
}