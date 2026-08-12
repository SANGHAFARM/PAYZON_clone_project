package erp.attendance.model;

import java.util.Date;

/*
 *근태 조회탭의 상세 조회 결과를 담을 모델
*/
public class AttendDetailItem {
    private int attendRecId;
    private Date inputDate;
    private String empType;
    private String empNameKr;
    private String deptName;
    private String posName;
    private String attendItemName;
    private Date startDate;
    private Date endDate;
    private double attendValue;
    private long payAmount;
    private String note;
	public AttendDetailItem() {
	}
	public AttendDetailItem(int attendRecId, Date inputDate, String empType, String empNameKr, String deptName,
			String posName, String attendItemName, Date startDate, Date endDate, double attendValue, long payAmount,
			String note) {
		super();
		this.attendRecId = attendRecId;
		this.inputDate = inputDate;
		this.empType = empType;
		this.empNameKr = empNameKr;
		this.deptName = deptName;
		this.posName = posName;
		this.attendItemName = attendItemName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.attendValue = attendValue;
		this.payAmount = payAmount;
		this.note = note;
	}
	public int getAttendRecId() {
		return attendRecId;
	}
	public void setAttendRecId(int attendRecId) {
		this.attendRecId = attendRecId;
	}
	public Date getInputDate() {
		return inputDate;
	}
	public void setInputDate(Date inputDate) {
		this.inputDate = inputDate;
	}
	public String getEmpType() {
		return empType;
	}
	public void setEmpType(String empType) {
		this.empType = empType;
	}
	public String getEmpNameKr() {
		return empNameKr;
	}
	public void setEmpNameKr(String empNameKr) {
		this.empNameKr = empNameKr;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getPosName() {
		return posName;
	}
	public void setPosName(String posName) {
		this.posName = posName;
	}
	public String getAttendItemName() {
		return attendItemName;
	}
	public void setAttendItemName(String attendItemName) {
		this.attendItemName = attendItemName;
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
