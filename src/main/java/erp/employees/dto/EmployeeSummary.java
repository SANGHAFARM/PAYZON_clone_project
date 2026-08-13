package erp.employees.dto;

// 사원현황 화면 상단의 상태별·고용형태별 인원수를 담는 DTO
public class EmployeeSummary {
	private int workingCount;
	private int regularCount;
	private int contractCount;
	private int temporaryCount;
	private int dispatchedCount;
	private int commissionedCount;
	private int dailyCount;
	private int retiredCount;
	private int totalCount;

	public int getWorkingCount() { return workingCount; }
	public void setWorkingCount(int workingCount) { this.workingCount = workingCount; }
	public int getRegularCount() { return regularCount; }
	public void setRegularCount(int regularCount) { this.regularCount = regularCount; }
	public int getContractCount() { return contractCount; }
	public void setContractCount(int contractCount) { this.contractCount = contractCount; }
	public int getTemporaryCount() { return temporaryCount; }
	public void setTemporaryCount(int temporaryCount) { this.temporaryCount = temporaryCount; }
	public int getDispatchedCount() { return dispatchedCount; }
	public void setDispatchedCount(int dispatchedCount) { this.dispatchedCount = dispatchedCount; }
	public int getCommissionedCount() { return commissionedCount; }
	public void setCommissionedCount(int commissionedCount) { this.commissionedCount = commissionedCount; }
	public int getDailyCount() { return dailyCount; }
	public void setDailyCount(int dailyCount) { this.dailyCount = dailyCount; }
	public int getRetiredCount() { return retiredCount; }
	public void setRetiredCount(int retiredCount) { this.retiredCount = retiredCount; }
	public int getTotalCount() { return totalCount; }
	public void setTotalCount(int totalCount) { this.totalCount = totalCount; }
}
