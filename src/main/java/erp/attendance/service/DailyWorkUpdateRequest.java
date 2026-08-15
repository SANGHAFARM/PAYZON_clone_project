package erp.attendance.service;

import java.util.Date;
import java.util.Map;

//일용직 근무기록 수정 리퀘스트
public class DailyWorkUpdateRequest {
	private int dailyWorkRecordId;
	private Date workDate;
	private Integer projectId;
	private long dailyPay;
	private double payRate;
	private long incomeTax;
	private long localIncomeTax;
	private long actualPay;
	
	

	public DailyWorkUpdateRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DailyWorkUpdateRequest(int dailyWorkRecordId, Date workDate, Integer projectId, long dailyPay,
			double payRate, long incomeTax, long localIncomeTax, long actualPay) {
		super();
		this.dailyWorkRecordId = dailyWorkRecordId;
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

	public void validate(Map<String, Boolean> errors) {
		if (dailyWorkRecordId==0) {
			errors.put("dailyWorkRecordId", Boolean.TRUE);
		}
		if (projectId == null || projectId == 0) {
			errors.put("projectId", Boolean.TRUE);
		}
		if (workDate == null) {
			errors.put("workDate", Boolean.TRUE);
		}

	}

	public int getDailyWorkRecordId() {
		return dailyWorkRecordId;
	}

	public void setDailyWorkRecordId(int dailyWorkRecordId) {
		this.dailyWorkRecordId = dailyWorkRecordId;
	}
	

}
