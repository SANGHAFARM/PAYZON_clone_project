package erp.employees.model;

import java.util.Date;

/**
 * [인사/사원관리] 사원 내부 인사발령 이력 Model DB 테이블: EMPLOYEE_APPOINTMENT
 */
public class EmployeeAppointment {

	// [DB 관리 항목]
	private Long employeeAppointmentId; // 인사발령 이력 식별 번호 (PK)
	private Long employeeId; // 사원 식별 번호 (FK)

	// [인사발령 입력 항목]
	private String appType; // 발령구분 (승진, 부서이동 등)
	private Date appDate; // 발령일자
	private String departmentName; // 부서명
	private String jobPositionName; // 직위명
	private String jobTitleDuty; // 직책/담당직무
	private String note; // 비고

	public EmployeeAppointment() {
	}

	// Getter & Setter
	public Long getEmployeeAppointmentId() {
		return employeeAppointmentId;
	}

	public void setEmployeeAppointmentId(Long employeeAppointmentId) {
		this.employeeAppointmentId = employeeAppointmentId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
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

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getJobPositionName() {
		return jobPositionName;
	}

	public void setJobPositionName(String jobPositionName) {
		this.jobPositionName = jobPositionName;
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