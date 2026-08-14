package erp.attendance.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

//일용직 근무기록 입력 리퀘스트
public class DailyWorkInsertRequest {
	private List<Integer> employeeIds;
	private Date workDate;
	private Integer projectId;
	private long dailyPay;
	private double payRate;
	private long incomeTax;
	private long localIncomeTax;
	private long actualPay;

	public DailyWorkInsertRequest() {
	}

	public DailyWorkInsertRequest(List<Integer> employeeIds, Date workDate, Integer projectId, long dailyPay,
			double payRate, long incomeTax, long localIncomeTax, long actualPay) {
		super();
		this.employeeIds = employeeIds;
		this.workDate = workDate;
		this.projectId = projectId;
		this.dailyPay = dailyPay;
		this.payRate = payRate;
		this.incomeTax = incomeTax;
		this.localIncomeTax = localIncomeTax;
		this.actualPay = actualPay;
	}

	public List<Integer> getEmployeeIds() {
		return employeeIds;
	}

	public void setEmployeeIds(List<Integer> employeeIds) {
		this.employeeIds = employeeIds;
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
		if (employeeIds == null || employeeIds.size() == 0) {
			errors.put("employeeIds", Boolean.TRUE);
		}
		if (projectId == null || projectId == 0) {
			errors.put("projectId", Boolean.TRUE);
		}
		if (workDate == null) {
			errors.put("workDate", Boolean.TRUE);
		}

	}

}
