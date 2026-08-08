package erp.hr.model;

import java.util.Date;

// EMP_APPOINTMENT: 발령
public class EmpAppointment {
	private int appId;
	private int empId;
	private String appType;
	private Date appDate;
	private String deptName;
	private String posName;
	private String jobTitleDuty;
	private String note;

	// 기본 생성자
	public EmpAppointment() {
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public Date getAppDate() {
		return appDate;
	}

	public void setAppDate(Date appDate) {
		this.appDate = appDate;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getPosName() {
		return posName;
	}

	public void setPosName(String posName) {
		this.posName = posName;
	}

	public String getJobTitleDuty() {
		return jobTitleDuty;
	}

	public void setJobTitleDuty(String jobTitleDuty) {
		this.jobTitleDuty = jobTitleDuty;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}