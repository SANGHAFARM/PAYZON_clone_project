package erp.payroll.dto;

import java.util.Date;

// 일용직 사원의 근무일별 지급 내역
public class DayWorkerPaymentWork {

	private Date workDate;
	private int paymentRate;
	private long paymentAmount;
	private long incomeTax;
	private long localIncomeTax;

	public Date getWorkDate() {
		return workDate;
	}

	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}

	public int getPaymentRate() {
		return paymentRate;
	}

	public void setPaymentRate(int paymentRate) {
		this.paymentRate = paymentRate;
	}

	public long getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(long paymentAmount) {
		this.paymentAmount = paymentAmount;
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
}
