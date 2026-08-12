package erp.attendance.model;

public class EmployeeLeaveBalance {
	int employeeLeaveBalanceId;
	int employeeId;
	int leaveItemId;
	double totalDays;
	public EmployeeLeaveBalance() {
	}
	public EmployeeLeaveBalance(int employeeLeaveBalanceId, int employeeId, int leaveItemId, double totalDays) {
		super();
		this.employeeLeaveBalanceId = employeeLeaveBalanceId;
		this.employeeId = employeeId;
		this.leaveItemId = leaveItemId;
		this.totalDays = totalDays;
	}
	public int getEmployeeLeaveBalanceId() {
		return employeeLeaveBalanceId;
	}
	public void setEmployeeLeaveBalanceId(int employeeLeaveBalanceId) {
		this.employeeLeaveBalanceId = employeeLeaveBalanceId;
	}
	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
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
