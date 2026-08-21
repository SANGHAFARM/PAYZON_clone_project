package erp.attendance.service.request;
/*
 * 근태관리>휴가조회에서 검색조건을 담을 클래스
*/
public class LeaveInquiryRequest {
	private int leaveItemId;
	private String keyword;
	private String status;
	private String empType;
	private Integer departmentId;
	private Integer jobPositionId;
	public LeaveInquiryRequest(int leaveItemId, String keyword, String status, String empType, Integer departmentId,
			Integer jobPositionId) {
		super();
		this.leaveItemId = leaveItemId;
		this.keyword = keyword;
		this.status = status;
		this.empType = empType;
		this.departmentId = departmentId;
		this.jobPositionId = jobPositionId;
	}
	public LeaveInquiryRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getLeaveItemId() {
		return leaveItemId;
	}
	public void setLeaveItemId(int leaveItemId) {
		this.leaveItemId = leaveItemId;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getEmpType() {
		return empType;
	}
	public void setEmpType(String empType) {
		this.empType = empType;
	}
	public Integer getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}
	public Integer getJobPositionId() {
		return jobPositionId;
	}
	public void setJobPositionId(Integer jobPositionId) {
		this.jobPositionId = jobPositionId;
	}
	



}
