package erp.attend.model;

//21. EMP_LEAVE: 사원별 휴가현황
public class EmpLeave {
	private int empLeaveId;
	private int empId;
	private int leaveItemId;
	private double totalDays;

	// 기본 생성자
	public EmpLeave() {
	}

	public int getEmpLeaveId() {
		return empLeaveId;
	}

	public void setEmpLeaveId(int empLeaveId) {
		this.empLeaveId = empLeaveId;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public int getLeaveItemId() {
		return leaveItemId;
	}

	public void setLeaveItemId(int leaveItemId) {
		this.leaveItemId = leaveItemId;
	}

	public double getTotalDays() {
		return totalDays;
	}

	public void setTotalDays(double totalDays) {
		this.totalDays = totalDays;
	}
}