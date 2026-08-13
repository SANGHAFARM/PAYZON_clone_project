package erp.attendance.service;

//일용직 근무기록/관리 탭에서 사용
public class DailyWorkRecordRequest {
	int employeeId;
	int year;
	Integer month;
	public DailyWorkRecordRequest() {
	}
	public DailyWorkRecordRequest(int employeeId, int year, Integer month) {
		super();
		this.employeeId = employeeId;
		this.year = year;
		this.month = month;
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
