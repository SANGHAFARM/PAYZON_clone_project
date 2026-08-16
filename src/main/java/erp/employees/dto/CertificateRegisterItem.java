package erp.employees.dto;

// 제증명서 발급대장의 조회 결과 한 행을 전달한다.
public class CertificateRegisterItem {
	private int certificateId;
	private String certificateNo;
	private String certificateType;
	private String certificateTypeName;
	private String certificateUse;
	private String employmentType;
	private String employeeName;
	private String departmentName;
	private String positionName;
	private String issueDate;

	public int getCertificateId() { return certificateId; }
	public void setCertificateId(int certificateId) { this.certificateId = certificateId; }
	public String getCertificateNo() { return certificateNo; }
	public void setCertificateNo(String certificateNo) { this.certificateNo = certificateNo; }
	public String getCertificateType() { return certificateType; }
	public void setCertificateType(String certificateType) { this.certificateType = certificateType; }
	public String getCertificateTypeName() { return certificateTypeName; }
	public void setCertificateTypeName(String certificateTypeName) { this.certificateTypeName = certificateTypeName; }
	public String getCertificateUse() { return certificateUse; }
	public void setCertificateUse(String certificateUse) { this.certificateUse = certificateUse; }
	public String getEmploymentType() { return employmentType; }
	public void setEmploymentType(String employmentType) { this.employmentType = employmentType; }
	public String getEmployeeName() { return employeeName; }
	public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
	public String getDepartmentName() { return departmentName; }
	public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
	public String getPositionName() { return positionName; }
	public void setPositionName(String positionName) { this.positionName = positionName; }
	public String getIssueDate() { return issueDate; }
	public void setIssueDate(String issueDate) { this.issueDate = issueDate; }
}
