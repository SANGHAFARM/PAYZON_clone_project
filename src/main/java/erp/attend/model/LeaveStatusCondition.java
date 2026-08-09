package erp.attend.model;

/*
 * 근태관리>휴가조회에서 검색조건을 담을 클래스
*/
public class LeaveStatusCondition {
	private int leaveItemId;
	private String status;
	private String empType;
	private Integer deptId;
	private Integer posId;
	private String keyword;

	public LeaveStatusCondition(int leaveItemId, String status, String empType, Integer deptId, Integer posId,
			String keyword) {
		super();
		this.leaveItemId = leaveItemId;
		this.status = status;
		this.empType = empType;
		this.deptId = deptId;
		this.posId = posId;
		this.keyword = keyword;
	}

	public LeaveStatusCondition() {
	}

	public int getLeaveItemId() {
		return leaveItemId;
	}

	public void setLeaveItemId(int leaveItemId) {
		this.leaveItemId = leaveItemId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEmpType() {
		return empType;
	}

	public void setEmpType(String empType) {
		this.empType = empType;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public Integer getPosId() {
		return posId;
	}

	public void setPosId(Integer posId) {
		this.posId = posId;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
}
