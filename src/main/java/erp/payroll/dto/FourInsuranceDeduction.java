package erp.payroll.dto;

// 사원 한 명의 4대보험 근로자 및 사업주 부담액
public class FourInsuranceDeduction {

	private String employmentTypeName;
	private String employeeName;
	private String departmentName;
	private String positionName;
	private long pensionEmployer;
	private long pensionEmployee;
	private long healthEmployer;
	private long healthEmployee;
	private long careEmployer;
	private long careEmployee;
	private long employmentEmployer;
	private long employmentEmployee;

	public String getEmploymentTypeName() { return employmentTypeName; }
	public void setEmploymentTypeName(String value) { employmentTypeName = value; }
	public String getEmployeeName() { return employeeName; }
	public void setEmployeeName(String value) { employeeName = value; }
	public String getDepartmentName() { return departmentName; }
	public void setDepartmentName(String value) { departmentName = value; }
	public String getPositionName() { return positionName; }
	public void setPositionName(String value) { positionName = value; }
	public long getPensionEmployer() { return pensionEmployer; }
	public void setPensionEmployer(long value) { pensionEmployer = value; }
	public long getPensionEmployee() { return pensionEmployee; }
	public void setPensionEmployee(long value) { pensionEmployee = value; }
	public long getPensionTotal() { return pensionEmployer + pensionEmployee; }
	public long getHealthEmployer() { return healthEmployer; }
	public void setHealthEmployer(long value) { healthEmployer = value; }
	public long getHealthEmployee() { return healthEmployee; }
	public void setHealthEmployee(long value) { healthEmployee = value; }
	public long getHealthTotal() { return healthEmployer + healthEmployee; }
	public long getCareEmployer() { return careEmployer; }
	public void setCareEmployer(long value) { careEmployer = value; }
	public long getCareEmployee() { return careEmployee; }
	public void setCareEmployee(long value) { careEmployee = value; }
	public long getCareTotal() { return careEmployer + careEmployee; }
	public long getEmploymentEmployer() { return employmentEmployer; }
	public void setEmploymentEmployer(long value) { employmentEmployer = value; }
	public long getEmploymentEmployee() { return employmentEmployee; }
	public void setEmploymentEmployee(long value) { employmentEmployee = value; }
	public long getEmploymentTotal() { return employmentEmployer + employmentEmployee; }
	public long getTotalEmployer() { return pensionEmployer + healthEmployer + careEmployer + employmentEmployer; }
	public long getTotalEmployee() { return pensionEmployee + healthEmployee + careEmployee + employmentEmployee; }
	public long getGrandTotal() { return getTotalEmployer() + getTotalEmployee(); }
}
