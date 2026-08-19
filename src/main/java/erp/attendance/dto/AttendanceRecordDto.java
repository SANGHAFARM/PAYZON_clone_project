package erp.attendance.dto;

import java.util.Date;

public class AttendanceRecordDto {
	int employeeAttendanceId;
	Date inputDate;
	String attendName;
	Date startDate;
	Date endDate;
	double attendValue;
	long payAmount;
	String note;


	public AttendanceRecordDto(int employeeAttendanceId, Date inputDate, String attendName, Date startDate,
			Date endDate, double attendValue, long payAmount, String note) {
		super();
		this.employeeAttendanceId = employeeAttendanceId;
		this.inputDate = inputDate;
		this.attendName = attendName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.attendValue = attendValue;
		this.payAmount = payAmount;
		this.note = note;
	}
	public AttendanceRecordDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public int getEmployeeAttendanceId() {
		return employeeAttendanceId;
	}
	public void setEmployeeAttendanceId(int employeeAttendanceId) {
		this.employeeAttendanceId = employeeAttendanceId;
	}
	public Date getInputDate() {
		return inputDate;
	}
	public void setInputDate(Date inputDate) {
		this.inputDate = inputDate;
	}
	public String getAttendName() {
		return attendName;
	}
	public void setAttendName(String attendName) {
		this.attendName = attendName;
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
	public void setEndDate(Date enDDate) {
		this.endDate = enDDate;
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
