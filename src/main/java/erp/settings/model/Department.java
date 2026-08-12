package erp.settings.model;

/**
 * [기본환경설정] 부서 설정 Model DB 테이블: DEPARTMENT
 */
public class Department {

	private Long departmentId;
	private String departmentName;

	public Department() {
	}

	// Getter & Setter
	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
}