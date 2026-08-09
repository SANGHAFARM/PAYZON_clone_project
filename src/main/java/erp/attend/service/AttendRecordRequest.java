package erp.attend.service;

import java.util.Date;
import java.util.Map;

public class AttendRecordRequest {
	private int empId;
	private int attendItemId;
	private Date inputDate;
	private Date startDate;
	private Date endDate;
	private double attendValue;
	private long payAmount;
	private String note;
	public AttendRecordRequest() {
	}
	public AttendRecordRequest(int empId, int attendItemId,  Date inputDate, Date startDate,
			Date endDate, double attendValue, long payAmount, String note) {
		super();
		this.empId = empId;
		this.attendItemId = attendItemId;
		this.inputDate = inputDate;
		this.startDate = startDate;
		this.endDate = endDate;
		this.attendValue = attendValue;
		this.payAmount = payAmount;
		this.note = note;
	}
	public int getEmpId() {
		return empId;
	}
	public void setEmpId(int empId) {
		this.empId = empId;
	}
	public int getAttendItemId() {
		return attendItemId;
	}
	public void setAttendItemId(int attendItemId) {
		this.attendItemId = attendItemId;
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
	public void validate(Map<String, Boolean> errors) {
		if (empId<=0) {
			errors.put("empId", Boolean.TRUE);
		}
		if (attendItemId<=0) {
			errors.put("attendItemId", Boolean.TRUE);
		}
		if (inputDate==null) {
			errors.put("inputDate", Boolean.TRUE);
		}
		if (startDate==null) {
			errors.put("startDate", Boolean.TRUE);
		}
		if (endDate==null) {
			errors.put("endDate", Boolean.TRUE);
		}
		if (startDate !=null && endDate != null && endDate.before(startDate)) {
			errors.put("endDate", Boolean.TRUE);
		}
		if (attendValue<=0) {
			errors.put("attendValue", Boolean.TRUE);
		}
	}
	
	
	
}
