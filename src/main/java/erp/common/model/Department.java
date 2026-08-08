package erp.common.model;

// DEPARTMENT: 부서 설정
public class Department {
	
	// 기본 생성자
	public Department() {
	}
	
	private int deptId;
	private String deptName;
	
	public int getDeptId() {
		return deptId;
	}

	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
}
