package erp.attendance.model;

import java.util.Date;

public class EmployeeAttendance {
	private Long employeeAttendanceId;
	private Long employeeId;
	private Long attendanceItemId;
	private Long leaveItemId;
	private Date inputDate;
	private Date startDate;
	private Date endDate;
	private Double attendValue;
	private Long payAmount;
	private String note;
	public EmployeeAttendance() {
	}
	public EmployeeAttendance(Long employeeAttendanceId, Long employeeId, Long attendanceItemId, Long leaveItemId,
			Date inputDate, Date startDate, Date endDate, Double attendValue, Long payAmount, String note) {
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
	public Long getEmployeeAttendanceId() {
		return employeeAttendanceId;
	}
	public void setEmployeeAttendanceId(Long employeeAttendanceId) {
		this.employeeAttendanceId = employeeAttendanceId;
	}
	public Long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}
	public Long getAttendanceItemId() {
		return attendanceItemId;
	}
	public void setAttendanceItemId(Long attendanceItemId) {
		this.attendanceItemId = attendanceItemId;
	}
	public Long getLeaveItemId() {
		return leaveItemId;
	}
	public void setLeaveItemId(Long leaveItemId) {
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
	public Double getAttendValue() {
		return attendValue;
	}
	public void setAttendValue(Double attendValue) {
		this.attendValue = attendValue;
	}
	public Long getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(Long payAmount) {
		this.payAmount = payAmount;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	
}
