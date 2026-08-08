package erp.attend.model;

import java.util.Date;

// EMP_ATTEND_RECORD: 사원 근태 기록
public class EmpAttendRecord {
	private int attendRecId;
	private int empId;
	private int attendItemId;
	private Integer leaveItemId;
	private Date inputDate;
	private Date startDate;
	private Date endDate;
	private double attendValue;
	private long payAmount;
	private String note;

	// 기본 생성자
	public EmpAttendRecord() {
	}

	public int getAttendRecId() {
		return attendRecId;
	}

	public void setAttendRecId(int attendRecId) {
		this.attendRecId = attendRecId;
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