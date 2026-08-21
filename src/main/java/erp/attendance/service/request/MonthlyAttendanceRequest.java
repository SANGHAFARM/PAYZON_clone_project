package erp.attendance.service.request;

public class MonthlyAttendanceRequest {
	int year;
	int month;
	String status;
	String empType;
	Integer departmentId;
	Integer jobPositionId;
	public MonthlyAttendanceRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MonthlyAttendanceRequest(int year, int month, String status, String empType, Integer departmentId,
			Integer jobPositionId) {
		super();
		this.year = year;
		this.month = month;
		this.status = status;
		this.empType = empType;
		this.departmentId = departmentId;
		this.jobPositionId = jobPositionId;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getEmpType() {
		return empType;
	}
	public void setEmpType(String empType) {
		this.empType = empType;
	}
	public Integer getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}
	public Integer getJobPositionId() {
		return jobPositionId;
	}
	public void setJobPositionId(Integer jobPositionId) {
		this.jobPositionId = jobPositionId;
	}
	
}
