package erp.attend.model;

import java.util.Date;

// DAY_WORKER_RECORD: 일용직 근무기록
public class DayWorkerRecord {
	private int dayWorkRecId;
	private int empId;
	private Date workDate;
	private Integer projectId;
	private long dailyPay;
	private double payRate;
	private long incomeTax;
	private long localIncomeTax;
	private long actualPay;

	// 기본 생성자
	public DayWorkerRecord() {
	}

	public int getDayWorkRecId() {
		return dayWorkRecId;
	}

	public void setDayWorkRecId(int dayWorkRecId) {
		this.dayWorkRecId = dayWorkRecId;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
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