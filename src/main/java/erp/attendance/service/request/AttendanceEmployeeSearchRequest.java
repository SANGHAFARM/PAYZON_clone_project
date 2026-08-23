package erp.attendance.service.request;

public class AttendanceEmployeeSearchRequest {
	private int employeeId;
	private int year;
	private Integer month;
	public AttendanceEmployeeSearchRequest(int employeeId, int year, Integer month) {
		super();
		this.employeeId = employeeId;
		this.year = year;
		this.month = month;
	}
	public AttendanceEmployeeSearchRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	
	
}
