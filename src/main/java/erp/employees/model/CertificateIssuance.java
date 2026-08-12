package erp.employees.model;

import java.util.Date;

/**
 * [인사/사원관리] 제증명서 발급 내역 및 출력 설정 Model DB 테이블: CERTIFICATE_ISSUANCE
 */
public class CertificateIssuance {

	// [DB 관리 항목]
	private int certificateIssuanceId; // 증명서 발급내역 식별 번호 (PK)
	private int employeeId; // 발급 대상 사원 식별 번호 (FK)

	// [증명서 발급정보]
	private String certDocNo; // 문서번호
	private String certType; // 증명서 종류 (재직경력서 등)
	private String purpose; // 발급용도
	private String certMemo; // 증명문구
	private Date issueDate; // 발급일
	private Integer issueDeptId; // 발급부서 식별 번호 (FK)

	// [증명서 출력설정]
	private String showCeoYn; // 대표자 표기 여부
	private String hideJuminYn; // 주민등록번호 숨김 여부
	private String showLogoYn; // 회사 로고 표시 여부
	private String showStampYn; // 회사 도장 표시 여부

	public CertificateIssuance() {
	}

	// Getter & Setter
	public int getCertificateIssuanceId() {
		return certificateIssuanceId;
	}

	public void setCertificateIssuanceId(int certificateIssuanceId) {
		this.certificateIssuanceId = certificateIssuanceId;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
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