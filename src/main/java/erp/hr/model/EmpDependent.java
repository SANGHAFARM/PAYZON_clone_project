package erp.hr.model;

// EMP_DEPENDENT: 부양가족 정보
public class EmpDependent {

	private int empId;
	private String relation;
	private String depName;
	private String nationalType;
	private String juminNo;
	private String disabledYn;
	private String basicDeductYn;
	private String healthInsYn;
	private String cohabitYn;
	private String incomeTaxYn;
	private String childUnder20Yn;
	private int depId;

	// 기본 생성자
	public EmpDependent() {
	}

	public int getDepId() {
		return depId;
	}

	public void setDepId(int depId) {
		this.depId = depId;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
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