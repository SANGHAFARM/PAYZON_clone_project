package erp.employees.dto;

// 경력증명서에 표시할 근무기간 한 행을 전달한다.
public class CertificateCareerItem {
	private String joinDate;
	private String retirementDate;
	private String departmentName;
	private String positionName;
	private String duty;

	public String getJoinDate() { return joinDate; }
	public void setJoinDate(String joinDate) { this.joinDate = joinDate; }
	public String getRetirementDate() { return retirementDate; }
	public void setRetirementDate(String retirementDate) { this.retirementDate = retirementDate; }
	public String getDepartmentName() { return departmentName; }
	public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
	public String getPositionName() { return positionName; }
	public void setPositionName(String positionName) { this.positionName = positionName; }
	public String getDuty() { return duty; }
	public void setDuty(String duty) { this.duty = duty; }
}
