package erp.employees.model;

import java.util.Date;

/**
 * [인사/사원관리] 사원 자격/면허 이력 Model DB 테이블: EMPLOYEE_LICENSE
 */
public class EmployeeLicense {

	// [DB 관리 항목]
	private int employeeLicenseId; // 자격/면허 식별 번호 (PK)
	private int employeeId; // 사원 식별 번호 (FK)

	// [자격·면허 입력 항목]
	private String licName; // 자격/면허명
	private Date acqDate; // 취득일
	private String issuer; // 발행기관
	private String licenseNo; // 증번호
	private String note; // 비고

	public EmployeeLicense() {
	}

	// Getter & Setter
	public int getEmployeeLicenseId() {
		return employeeLicenseId;
	}

	public void setEmployeeLicenseId(int employeeLicenseId) {
		this.employeeLicenseId = employeeLicenseId;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getLicName() {
		return licName;
	}

	public void setLicName(String licName) {
		this.licName = licName;
	}

	public Date getAcqDate() {
		return acqDate;
	}

	public void setAcqDate(Date acqDate) {
		this.acqDate = acqDate;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}