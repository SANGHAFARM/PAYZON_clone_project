package erp.employees.model;

/**
 * [인사/사원관리] 사원 부양가족 및 소득세 공제 설정 Model DB 테이블: EMPLOYEE_DEPENDENT
 */
public class EmployeeDependent {

	// [DB 관리 항목]
	private Long employeeDependentId; // 부양가족 식별 번호 (PK)
	private Long employeeId; // 사원 식별 번호 (FK)

	// [부양가족 기본정보]
	private String relation; // 관계
	private String depName; // 성명
	private String nationalType; // 구분 (내국인/외국인)
	private String juminNo; // 주민등록번호

	// [공제 적용 설정]
	private String disabledYn; // 장애여부
	private String basicDeductYn; // 인적공제 적용여부
	private String healthInsYn; // 건강보험 적용여부
	private String cohabitYn; // 동거여부
	private String incomeTaxYn; // 갑근세 적용여부
	private String childUnder20Yn; // 20세 이하 자녀 여부

	public EmployeeDependent() {
	}

	// Getter & Setter
	public Long getEmployeeDependentId() {
		return employeeDependentId;
	}

	public void setEmployeeDependentId(Long employeeDependentId) {
		this.employeeDependentId = employeeDependentId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}

	public String getNationalType() {
		return nationalType;
	}

	public void setNationalType(String nationalType) {
		this.nationalType = nationalType;
	}

	public String getJuminNo() {
		return juminNo;
	}

	public void setJuminNo(String juminNo) {
		this.juminNo = juminNo;
	}

	public String getDisabledYn() {
		return disabledYn;
	}

	public void setDisabledYn(String disabledYn) {
		this.disabledYn = disabledYn;
	}

	public String getBasicDeductYn() {
		return basicDeductYn;
	}

	public void setBasicDeductYn(String basicDeductYn) {
		this.basicDeductYn = basicDeductYn;
	}

	public String getHealthInsYn() {
		return healthInsYn;
	}

	public void setHealthInsYn(String healthInsYn) {
		this.healthInsYn = healthInsYn;
	}

	public String getCohabitYn() {
		return cohabitYn;
	}

	public void setCohabitYn(String cohabitYn) {
		this.cohabitYn = cohabitYn;
	}

	public String getIncomeTaxYn() {
		return incomeTaxYn;
	}

	public void setIncomeTaxYn(String incomeTaxYn) {
		this.incomeTaxYn = incomeTaxYn;
	}

	public String getChildUnder20Yn() {
		return childUnder20Yn;
	}

	public void setChildUnder20Yn(String childUnder20Yn) {
		this.childUnder20Yn = childUnder20Yn;
	}
}