package erp.hr.model;

import java.util.Date;

// EMP_LICENSE: 자격/면허
public class EmpLicense {
	private int licId;
	private int empId;
	private String licName;
	private Date acqDate;
	private String issuer;
	private String licenseNo;
	private String note;

	// 기본 생성자
	public EmpLicense() {
	}

	public int getLicId() {
		return licId;
	}

	public void setLicId(int licId) {
		this.licId = licId;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
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