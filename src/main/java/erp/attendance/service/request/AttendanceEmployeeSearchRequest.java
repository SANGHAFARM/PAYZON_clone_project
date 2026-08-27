package erp.attendance.service.request;


/*
 * 근태기록에서 관리 탭에서 사용하는 조회 클래스
 * 勤怠記録の管理で使う照会クラス
 * */
public class AttendanceEmployeeSearchRequest {
	private int employeeId;    // 사원ID(社員ID)
	private int year;          // 연도(年度)
	private Integer month;     // 월(月) - null이면 전체 조회
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
