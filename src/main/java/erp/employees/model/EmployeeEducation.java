package erp.employees.model;

// EMPLOYEE_EDUCATION: 학력
public class EmployeeEducation {

	private int employeeEducationId;
	private int employeeId;
	private String eduType;
	private String admissionYm;
	private String gradYm;
	private String schoolName;
	private String majorName;
	private String completeType;
	
	// 기본 생성자
	public EmployeeEducation() {
	}

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
