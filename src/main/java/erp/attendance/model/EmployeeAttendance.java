package erp.attendance.model;

import java.util.Date;

public class EmployeeAttendance {
	private int employeeAttendanceId;
	private int employeeId;
	private int attendanceItemId;
	private Integer leaveItemId;
	private Date inputDate;
	private Date startDate;
	private Date endDate;
	private double attendValue;
	private long payAmount;
	private String note;
	public EmployeeAttendance() {
	}
	public EmployeeAttendance(int employeeAttendanceId, int employeeId, int attendanceItemId, Integer leaveItemId,
			Date inputDate, Date startDate, Date endDate, double attendValue, long payAmount, String note) {
		super();
		this.employeeAttendanceId = employeeAttendanceId;
		this.employeeId = employeeId;
		this.attendanceItemId = attendanceItemId;
		this.leaveItemId = leaveItemId;
		this.inputDate = inputDate;
		this.startDate = startDate;
		this.endDate = endDate;
		this.attendValue = attendValue;
		this.payAmount = payAmount;
		this.note = note;
	}
	public int getEmployeeAttendanceId() {
		return employeeAttendanceId;
	}
	public void setEmployeeAttendanceId(int employeeAttendanceId) {
		this.employeeAttendanceId = employeeAttendanceId;
	}
	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	public int getAttendanceItemId() {
		return attendanceItemId;
	}
	public void setAttendanceItemId(int attendanceItemId) {
		this.attendanceItemId = attendanceItemId;
	}
	public Integer getLeaveItemId() {
		return leaveItemId;
	}
	public void setLeaveItemId(Integer leaveItemId) {
		this.leaveItemId = leaveItemId;
	}
	public Date getInputDate() {
		return inputDate;
	}
	public void setInputDate(Date inputDate) {
		this.inputDate = inputDate;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public double getAttendValue() {
		return attendValue;
	}
	public void setAttendValue(double attendValue) {
		this.attendValue = attendValue;
	}
	public long getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(long payAmount) {
		this.payAmount = payAmount;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	
}
