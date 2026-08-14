package erp.payroll.dto;

// 급여입력 화면에 표시할 사원별 급여 정보
public class PayrollManagementEmployee {

	private int employeeId;
	private String employmentType;
	private String employeeNo;
	private String name;
	private String departmentName;
	private String positionName;
	private String statusName;
	private long grossPayment;
	private long totalDeduction;

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmploymentType() {
		return employmentType;
	}

	public void setEmploymentType(String employmentType) {
		this.employmentType = employmentType;
	}

	public String getEmployeeNo() {
		return employeeNo;
	}

	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public long getGrossPayment() {
		return grossPayment;
	}

	public void setGrossPayment(long grossPayment) {
		this.grossPayment = grossPayment;
	}

	public long getTotalDeduction() {
		return totalDeduction;
	}

	public void setTotalDeduction(long totalDeduction) {
		this.totalDeduction = totalDeduction;
	}

	public long getNetPayment() {
		return grossPayment - totalDeduction;
	}
}
