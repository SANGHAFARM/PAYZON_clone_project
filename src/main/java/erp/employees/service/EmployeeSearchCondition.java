package erp.employees.service;

// 사원 목록과 선택창에서 공통으로 사용하는 검색조건을 전달한다.
public class EmployeeSearchCondition {
	private String searchTarget;
	private String keyword;
	private String employmentType;
	private String status;
	private int page;
	private int pageSize;
	private Integer departmentId;
	private Integer positionId;

	public String getSearchTarget() { return searchTarget; }
	public void setSearchTarget(String searchTarget) { this.searchTarget = searchTarget; }
	public String getKeyword() { return keyword; }
	public void setKeyword(String keyword) { this.keyword = keyword; }
	public String getEmploymentType() { return employmentType; }
	public void setEmploymentType(String employmentType) { this.employmentType = employmentType; }
	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }
	public int getPage() { return page; }
	public void setPage(int page) { this.page = page; }
	public int getPageSize() { return pageSize; }
	public void setPageSize(int pageSize) { this.pageSize = pageSize; }
	public Integer getDepartmentId() { return departmentId; }
	public void setDepartmentId(Integer departmentId) { this.departmentId = departmentId; }
	public Integer getPositionId() { return positionId; }
	public void setPositionId(Integer positionId) { this.positionId = positionId; }
}
