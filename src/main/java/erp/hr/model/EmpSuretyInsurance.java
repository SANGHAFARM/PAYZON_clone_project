package erp.hr.model;

import java.util.Date;

// EMP_SURETY_INSURANCE: 보증보험
public class EmpSuretyInsurance {
	private int suretyInsId;
	private int empId;
	private String providerName;
	private String insuranceNo;
	private long insuranceAmt;
	private Date signupDate;
	private Date expireDate;
	private String note;

	// 기본 생성자
	public EmpSuretyInsurance() {
	}

	public int getSuretyInsId() {
		return suretyInsId;
	}

	public void setSuretyInsId(int suretyInsId) {
		this.suretyInsId = suretyInsId;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public String getInsuranceNo() {
		return insuranceNo;
	}

	public void setInsuranceNo(String insuranceNo) {
		this.insuranceNo = insuranceNo;
	}

	public long getInsuranceAmt() {
		return insuranceAmt;
	}

	public void setInsuranceAmt(long insuranceAmt) {
		this.insuranceAmt = insuranceAmt;
	}

	public Date getSignupDate() {
		return signupDate;
	}

	public void setSignupDate(Date signupDate) {
		this.signupDate = signupDate;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}