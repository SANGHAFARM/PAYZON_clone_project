package erp.attendance.model;

import java.util.Date;

public class DailyWorkRecord {
	private int dailyWorkRecordId;
	private int employeeId;
	private Date workDate;
	private Integer projectId;
	private long dailyPay;
	private double payRate;
	private long incomeTax;
	private long localIncomeTax;
	private long actualPay;
	public DailyWorkRecord() {
		super();
		// TODO Auto-generated constructor stub
	}
	public DailyWorkRecord(int dailyWorkRecordId, int employeeId, Date workDate, Integer projectId, long dailyPay,
			double payRate, long incomeTax, long localIncomeTax, long actualPay) {
		super();
		this.dailyWorkRecordId = dailyWorkRecordId;
		this.employeeId = employeeId;
		this.workDate = workDate;
		this.projectId = projectId;
		this.dailyPay = dailyPay;
		this.payRate = payRate;
		this.incomeTax = incomeTax;
		this.localIncomeTax = localIncomeTax;
		this.actualPay = actualPay;
	}
	public int getDailyWorkRecordId() {
		return dailyWorkRecordId;
	}
	public void setDailyWorkRecordId(int dailyWorkRecordId) {
		this.dailyWorkRecordId = dailyWorkRecordId;
	}
	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	public Date getWorkDate() {
		return workDate;
	}
	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}
	public Integer getProjectId() {
		return projectId;
	}
	public void setProjectId(Integer projectId) {
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
