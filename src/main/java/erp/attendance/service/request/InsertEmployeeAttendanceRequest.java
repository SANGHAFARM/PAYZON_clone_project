package erp.attendance.service.request;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class InsertEmployeeAttendanceRequest {
	private List<Integer> employeeIds;
	private Date inputDate;
	private int attendanceItemId;
	private Date startDate;
	private Date endDate;
	private double attendValue;
	private long payAmount;
	private String note;
	public InsertEmployeeAttendanceRequest() {
	}
	public InsertEmployeeAttendanceRequest(List<Integer> employeeIds, Date inputDate, int attendanceItemId, Date startDate,
			Date endDate, double attendValue, long payAmount, String note) {
		super();
		this.employeeIds = employeeIds;
		this.inputDate = inputDate;
		this.attendanceItemId = attendanceItemId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.attendValue = attendValue;
		this.payAmount = payAmount;
		this.note = note;
	}
	public List<Integer> getEmployeeIds() {
		return employeeIds;
	}
	public void setEmployeeIds(List<Integer> employeeIds) {
		this.employeeIds = employeeIds;
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
		if (employeeIds==null||employeeIds.isEmpty()||employeeIds.size()==0) {
			errors.put("empIds", Boolean.TRUE);
		}
		if (attendanceItemId<=0) {
			errors.put("attendanceItemId", Boolean.TRUE);
		}
		if (endDate.before(startDate)) {
			errors.put("dateError", Boolean.TRUE);
		}
		if (attendValue<=0) {
			errors.put("attendValue", Boolean.TRUE);
		}
	}
	
	
	
	
}
