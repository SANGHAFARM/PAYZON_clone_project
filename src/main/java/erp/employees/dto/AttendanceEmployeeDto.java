package erp.employees.dto;

//근태기록/관리 탭에서 사원 목록을 조회할 때 사용하는 클래스
public class AttendanceEmployeeDto {
	private int employeeId;
	private String empType;
	private String empNo;
	private String empNameKr;
	private String departmentName;
	private String jobPositionName;
	public AttendanceEmployeeDto() {
	}
	public AttendanceEmployeeDto(int employeeId, String empType, String empNo, String empNameKr, String departmentName,
			String jobPositionName) {
		super();
		this.employeeId = employeeId;
		this.empType = empType;
		this.empNo = empNo;
		this.empNameKr = empNameKr;
		this.departmentName = departmentName;
		this.jobPositionName = jobPositionName;
	}
	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	public String getEmpType() {
		return empType;
	}
	public void setEmpType(String empType) {
		this.empType = empType;
	}
	public String getEmpNo() {
		return empNo;
	}
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}
	public String getEmpNameKr() {
		return empNameKr;
	}
	public void setEmpNameKr(String empNameKr) {
		this.empNameKr = empNameKr;
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
	
}
