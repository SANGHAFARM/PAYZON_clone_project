package erp.hr.model;

import java.util.Date;

// EMP_GUARANTOR: 보증인
public class EmpGuarantor {
	private int guarantorId;
	private int empId;
	private String guarantorName;
	private String relation;
	private String juminNo;
	private long guaranteeAmt;
	private Date guaranteeDate;
	private Date expireDate;
	private String telNo;

	// 기본 생성자
	public EmpGuarantor() {
	}

	public int getGuarantorId() {
		return guarantorId;
	}

	public void setGuarantorId(int guarantorId) {
		this.guarantorId = guarantorId;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
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

	public long getGuaranteeAmt() {
		return guaranteeAmt;
	}

	public void setGuaranteeAmt(long guaranteeAmt) {
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