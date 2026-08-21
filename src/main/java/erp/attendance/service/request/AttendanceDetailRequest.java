package erp.attendance.service.request;

import java.util.Date;
import java.util.Map;

/*
 * 근태 조회탭의 상세 조회 검색에 사용할 모델
*/
public class AttendanceDetailRequest {
	private Date inputDate;
	private Date startDate;
	private Date endDate;
	private Integer departmentId;
	private Integer attendanceGroupId;
    private Integer attendanceItemId;
    private Integer leaveItemId;
	private String empNameKr;
    private String note;
	public AttendanceDetailRequest() {
	}
	public AttendanceDetailRequest(Date inputDate, Date startDate, Date endDate, Integer departmentId,
			Integer attendanceGroupId, Integer attendanceItemId, Integer leaveItemId, String empNameKr, String note) {
		super();
		this.inputDate = inputDate;
		this.startDate = startDate;
		this.endDate = endDate;
		this.departmentId = departmentId;
		this.attendanceGroupId = attendanceGroupId;
		this.attendanceItemId = attendanceItemId;
		this.leaveItemId = leaveItemId;
		this.empNameKr = empNameKr;
		this.note = note;
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
	public Integer getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}
	
	public Integer getAttendanceGroupId() {
		return attendanceGroupId;
	}
	public void setAttendanceGroupId(Integer attendanceGroupId) {
		this.attendanceGroupId = attendanceGroupId;
	}
	public Integer getAttendanceItemId() {
		return attendanceItemId;
	}
	public void setAttendanceItemId(Integer attendanceItemId) {
		this.attendanceItemId = attendanceItemId;
	}
	public Integer getLeaveItemId() {
		return leaveItemId;
	}
	public void setLeaveItemId(Integer leaveItemId) {
		this.leaveItemId = leaveItemId;
	}
	public String getEmpNameKr() {
		return empNameKr;
	}
	public void setEmpNameKr(String empNameKr) {
		this.empNameKr = empNameKr;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public void validate(Map<String, Boolean> errors) {
		if (startDate!=null&&endDate!=null) {
			if (endDate.before(startDate)) {
				errors.put("DateError", Boolean.TRUE);
			}
		}
	}
    
    
	
    
}
