package erp.attendance.service;

import java.util.Date;

public class DailyWorkDetailRequest {
	private Date startDate;
	private Date endDate;
	private String empNameKr;
	private Integer departmentId;
	private Integer projectId;
	public DailyWorkDetailRequest() {
	}
	public DailyWorkDetailRequest(Date startDate, Date endDate, String empNameKr, Integer departmentId,
			Integer projectId) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
		this.empNameKr = empNameKr;
		this.departmentId = departmentId;
		this.projectId = projectId;
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
	public String getEmpNameKr() {
		return empNameKr;
	}
	public void setEmpNameKr(String empNameKr) {
		this.empNameKr = empNameKr;
	}
	public Integer getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}
	public Integer getProjectId() {
		return projectId;
	}
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}
	
}
