package erp.attendance.dto;

import java.util.Date;

//근태기록 입력 폼에 사용할 dto
public class AttendanceRecordDto {
	private int employeeAttendanceId;
	private Date inputDate;
	private int attendanceItemId;
	private String attendName;
	private Date startDate;
	private Date endDate;
	private double attendValue;
	private long payAmount;
	private String note;
	private String itemName;


	
	public AttendanceRecordDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AttendanceRecordDto(int employeeAttendanceId, Date inputDate, int attendanceItemId, String attendName,
			Date startDate, Date endDate, double attendValue, long payAmount, String note, String itemName) {
		super();
		this.employeeAttendanceId = employeeAttendanceId;
		this.inputDate = inputDate;
		this.attendanceItemId = attendanceItemId;
		this.attendName = attendName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.attendValue = attendValue;
		this.payAmount = payAmount;
		this.note = note;
		this.itemName = itemName;
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
	
	public int getAttendanceItemId() {
		return attendanceItemId;
	}
	public void setAttendanceItemId(int attendanceItemId) {
		this.attendanceItemId = attendanceItemId;
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

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	
	
}
