package erp.employees.model;

import java.util.Date;

/**
 * [인사/사원관리] 사원 신원보증보험 가입 내역 Model DB 테이블: EMPLOYEE_SURETY_INSURANCE
 */
public class EmployeeSuretyInsurance {

	// [DB 관리 항목]
	private Long employeeSuretyInsuranceId; // 보증보험 식별 번호 (PK)
	private Long employeeId; // 사원 식별 번호 (FK)

	// [보증보험 입력 항목]
	private String providerName; // 가입기관명
	private String insuranceNo; // 보험번호
	private Long insuranceAmt; // 보험금액 (Wrapper)
	private Date signupDate; // 가입일자
	private Date expireDate; // 만료일자
	private String note; // 비고

	public EmployeeSuretyInsurance() {
	}

	// Getter & Setter
	public Long getEmployeeSuretyInsuranceId() {
		return employeeSuretyInsuranceId;
	}

	public void setEmployeeSuretyInsuranceId(Long employeeSuretyInsuranceId) {
		this.employeeSuretyInsuranceId = employeeSuretyInsuranceId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
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

	public Long getInsuranceAmt() {
		return insuranceAmt;
	}

	public void setInsuranceAmt(Long insuranceAmt) {
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