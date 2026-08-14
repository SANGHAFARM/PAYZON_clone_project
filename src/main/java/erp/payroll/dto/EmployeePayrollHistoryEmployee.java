package erp.payroll.dto;

// 사원별 급여내역에서 사용하는 사원 정보
public class EmployeePayrollHistoryEmployee {

	private int employeeId;
	private String employmentTypeName;
	private String employeeNumber;
	private String employeeName;
	private String departmentName;
	private String positionName;
	private String status;

	public int getEmployeeId() { return employeeId; }
	public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
	public String getEmploymentTypeName() { return employmentTypeName; }
	public void setEmploymentTypeName(String employmentTypeName) { this.employmentTypeName = employmentTypeName; }
	public String getEmployeeNumber() { return employeeNumber; }
	public void setEmployeeNumber(String employeeNumber) { this.employeeNumber = employeeNumber; }
	public String getEmployeeName() { return employeeName; }
	public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
	public String getDepartmentName() { return departmentName; }
	public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
	public String getPositionName() { return positionName; }
	public void setPositionName(String positionName) { this.positionName = positionName; }
	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }
}
