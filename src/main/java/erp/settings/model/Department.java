package erp.settings.model;

/**
 * [기본환경설정] 부서 설정 Model DB 테이블: DEPARTMENT
 */
public class Department {

	private int departmentId; // 부서 식별 번호 (PK)
	private String departmentName;

	public Department() {
	}

	// Getter & Setter
	public int getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
}