package erp.employees.model;

/**
 * [인사/사원관리] 사원 학력 이력 Model DB 테이블: EMPLOYEE_EDUCATION
 */
public class EmployeeEducation {

	// [DB 관리 항목]
	private int employeeEducationId; // 학력 이력 식별 번호 (PK)
	private int employeeId; // 사원 식별 번호 (FK)

	// [학력 입력 항목]
	private String eduType; // 구분 (초등학교, 대학교 등)
	private String admissionYm; // 입학년월
	private String gradYm; // 졸업년월
	private String schoolName; // 학교명
	private String majorName; // 전공
	private String completeType; // 이수 (졸업, 재학 등)

	public EmployeeEducation() {
	}

	// Getter & Setter
	public int getEmployeeEducationId() {
		return employeeEducationId;
	}

	public void setEmployeeEducationId(int employeeEducationId) {
		this.employeeEducationId = employeeEducationId;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getEduType() {
		return eduType;
	}

	public void setEduType(String eduType) {
		this.eduType = eduType;
	}

	public String getAdmissionYm() {
		return admissionYm;
	}

	public void setAdmissionYm(String admissionYm) {
		this.admissionYm = admissionYm;
	}

	public String getGradYm() {
		return gradYm;
	}

	public void setGradYm(String gradYm) {
		this.gradYm = gradYm;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getMajorName() {
		return majorName;
	}

	public void setMajorName(String majorName) {
		this.majorName = majorName;
	}

	public String getCompleteType() {
		return completeType;
	}

	public void setCompleteType(String completeType) {
		this.completeType = completeType;
	}
}