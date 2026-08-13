package erp.attendance.service;

import java.util.Date;

public class DailyWorkInsertRequest {
	private Date workDate;
	private int projectId;
	private long dailyPay;
	private double payRate;
	private long incomeTax;
	private long localIncomeTax;
	private long actualPay;
	public DailyWorkInsertRequest() {
	}
	public DailyWorkInsertRequest(Date workDate, int projectId, long dailyPay, double payRate, long incomeTax,
			long localIncomeTax, long actualPay) {
		super();
		this.workDate = workDate;
		this.projectId = projectId;
		this.dailyPay = dailyPay;
		this.payRate = payRate;
		this.incomeTax = incomeTax;
		this.localIncomeTax = localIncomeTax;
		this.actualPay = actualPay;
	}
	public Date getWorkDate() {
		return workDate;
	}
	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	public long getDailyPay() {
		return dailyPay;
	}
	public void setDailyPay(long dailyPay) {
		this.dailyPay = dailyPay;
	}
	public double getPayRate() {
		return payRate;
	}
	public void setPayRate(double payRate) {
		this.payRate = payRate;
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
	public long getActualPay() {
		return actualPay;
	}
	public void setActualPay(long actualPay) {
		this.actualPay = actualPay;
	}
	
}
