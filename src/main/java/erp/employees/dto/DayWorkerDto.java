package erp.employees.dto;

public class DayWorkerDto {
	public int employeeId;
	public String empType;
	public String empNo;
	public String empNameKr;
	public String departmentName;

	public DayWorkerDto() {
	}

	public DayWorkerDto(int employeeId, String empType, String empNo, String empNameKr, String departmentName) {
		super();
		this.employeeId = employeeId;
		this.empType = empType;
		this.empNo = empNo;
		this.empNameKr = empNameKr;
		this.departmentName = departmentName;
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

}
