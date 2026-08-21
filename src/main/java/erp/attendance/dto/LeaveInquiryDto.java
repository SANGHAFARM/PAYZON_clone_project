package erp.attendance.dto;

public class LeaveInquiryDto {
	private int employeeId;
	private String empType;
	private String empNo;
	private String empNameKr;
	private String departmentName;
	private String jobPositionName;
	private String itemName;
	private long totalDays;
	private double usedDays;
	private double remainingDays;

	public LeaveInquiryDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LeaveInquiryDto(int employeeId, String empType, String empNo, String empNameKr, String departmentName,
			String jobPositionName, String itemName, long totalDays, double usedDays, double remainingDays) {
		super();
		this.employeeId = employeeId;
		this.empType = empType;
		this.empNo = empNo;
		this.empNameKr = empNameKr;
		this.departmentName = departmentName;
		this.jobPositionName = jobPositionName;
		this.itemName = itemName;
		this.totalDays = totalDays;
		this.usedDays = usedDays;
		this.remainingDays = remainingDays;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmpType() {
		return empType;
	}

	public void setEmpType(String empType) {
		this.empType = empType;
	}

	public String getEmpNo() {
		return empNo;
	}

	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}

	public String getEmpNameKr() {
		return empNameKr;
	}

	public void setEmpNameKr(String empNameKr) {
		this.empNameKr = empNameKr;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getJobPositionName() {
		return jobPositionName;
	}

	public void setJobPositionName(String jobPositionName) {
		this.jobPositionName = jobPositionName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public long getTotalDays() {
		return totalDays;
	}

	public void setTotalDays(long totalDays) {
		this.totalDays = totalDays;
	}

	public double getUsedDays() {
		return usedDays;
	}

	public void setUsedDays(double usedDays) {
		this.usedDays = usedDays;
	}

	public double getRemainingDays() {
		return remainingDays;
	}

	public void setRemainingDays(double remainingDays) {
		this.remainingDays = remainingDays;
	}

}
