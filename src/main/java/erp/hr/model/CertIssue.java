package erp.hr.model;

import java.util.Date;

// CERT_ISSUE: 제증명서 발급
public class CertIssue {
	private int certIssueId;
	private int empId;
	private String certDocNo;
	private String certType;
	private String purpose;
	private String certMemo;
	private Date issueDate;
	private Integer issueDeptId; // null 허용을 위해 Integer 객체형 사용
	private String showCeoYn;
	private String hideJuminYn;
	private String showLogoYn;
	private String showStampYn;

	// 기본 생성자
	public CertIssue() {
	}

	public int getCertIssueId() {
		return certIssueId;
	}

	public void setCertIssueId(int certIssueId) {
		this.certIssueId = certIssueId;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public String getCertDocNo() {
		return certDocNo;
	}

	public void setCertDocNo(String certDocNo) {
		this.certDocNo = certDocNo;
	}

	public String getCertType() {
		return certType;
	}

	public void setCertType(String certType) {
		this.certType = certType;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getCertMemo() {
		return certMemo;
	}

	public void setCertMemo(String certMemo) {
		this.certMemo = certMemo;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public Integer getIssueDeptId() {
		return issueDeptId;
	}

	public void setIssueDeptId(Integer issueDeptId) {
		this.issueDeptId = issueDeptId;
	}

	public String getShowCeoYn() {
		return showCeoYn;
	}

	public void setShowCeoYn(String showCeoYn) {
		this.showCeoYn = showCeoYn;
	}

	public String getHideJuminYn() {
		return hideJuminYn;
	}

	public void setHideJuminYn(String hideJuminYn) {
		this.hideJuminYn = hideJuminYn;
	}

	public String getShowLogoYn() {
		return showLogoYn;
	}

	public void setShowLogoYn(String showLogoYn) {
		this.showLogoYn = showLogoYn;
	}

	public String getShowStampYn() {
		return showStampYn;
	}

	public void setShowStampYn(String showStampYn) {
		this.showStampYn = showStampYn;
	}
}