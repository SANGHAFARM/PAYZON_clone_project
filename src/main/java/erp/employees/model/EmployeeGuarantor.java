package erp.employees.model;

import java.util.Date;

/**
 * [인사/사원관리] 사원 신원보증인 내역 Model DB 테이블: EMPLOYEE_GUARANTOR
 */
public class EmployeeGuarantor {

	// [DB 관리 항목]
	private int employeeGuarantorId; // 보증인 식별 번호 (PK)
	private int employeeId; // 사원 식별 번호 (FK)

	// [보증인 입력 항목]
	private String guarantorName; // 보증인 성명
	private String relation; // 관계
	private String juminNo; // 주민등록번호
	private Long guaranteeAmt; // 보증금액
	private Date guaranteeDate; // 보증일자(시작일)
	private Date expireDate; // 만료일자
	private String telNo; // 전화번호

	public EmployeeGuarantor() {
	}

	// Getter & Setter
	public int getEmployeeGuarantorId() {
		return employeeGuarantorId;
	}

	public void setEmployeeGuarantorId(int employeeGuarantorId) {
		this.employeeGuarantorId = employeeGuarantorId;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getGuarantorName() {
		return guarantorName;
	}

	public void setGuarantorName(String guarantorName) {
		this.guarantorName = guarantorName;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getJuminNo() {
		return juminNo;
	}

	public void setJuminNo(String juminNo) {
		this.juminNo = juminNo;
	}

	public Long getGuaranteeAmt() {
		return guaranteeAmt;
	}

	public void setGuaranteeAmt(Long guaranteeAmt) {
		this.guaranteeAmt = guaranteeAmt;
	}

	public Date getGuaranteeDate() {
		return guaranteeDate;
	}

	public void setGuaranteeDate(Date guaranteeDate) {
		this.guaranteeDate = guaranteeDate;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public String getTelNo() {
		return telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}
}