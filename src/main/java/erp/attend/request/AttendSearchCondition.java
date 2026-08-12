package erp.attend.request;

import java.util.Date;

/*
 * 근태 조회탭의 상세 조회 검색에 사용할 모델
*/
public class AttendSearchCondition {
	private Date inputDate;
	private Date periodStart;
	private Date periodEnd;
	private Integer deptId;
	private String empName;
    private String empType;
    private Integer attendItemId;
    private Integer leaveItemId;
    private String note;
	public AttendSearchCondition() {
	}
	public AttendSearchCondition(Date inputDate, Date periodStart, Date periodEnd, Integer deptId, String empName,
			String empType, Integer attendItemId, Integer leaveItemId, String note) {
		super();
		this.inputDate = inputDate;
		this.periodStart = periodStart;
		this.periodEnd = periodEnd;
		this.deptId = deptId;
		this.empName = empName;
		this.empType = empType;
		this.attendItemId = attendItemId;
		this.leaveItemId = leaveItemId;
		this.note = note;
	}
	public Date getInputDate() {
		return inputDate;
	}
	public void setInputDate(Date inputDate) {
		this.inputDate = inputDate;
	}
	public Date getPeriodStart() {
		return periodStart;
	}
	public void setPeriodStart(Date periodStart) {
		this.periodStart = periodStart;
	}
	public Date getPeriodEnd() {
		return periodEnd;
	}
	public void setPeriodEnd(Date periodEnd) {
		this.periodEnd = periodEnd;
	}
	public Integer getDeptId() {
		return deptId;
	}
	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getEmpType() {
		return empType;
	}
	public void setEmpType(String empType) {
		this.empType = empType;
	}
	public Integer getAttendItemId() {
		return attendItemId;
	}
	public void setAttendItemId(Integer attendItemId) {
		this.attendItemId = attendItemId;
	}
	public Integer getLeaveItemId() {
		return leaveItemId;
	}
	public void setLeaveItemId(Integer leaveItemId) {
		this.leaveItemId = leaveItemId;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
    
}
