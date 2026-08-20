package erp.attendance.dto;

import java.util.Map;

//근태조회 월별 조회에서 사용할 dto
public class MonthlyAttendanceDto {
	private String empType;
	private String empNo;
	private String empNameKr;
	private String departmentName;
	private String jobPositionName;
	private Map<Integer, String> dailyAttendance; 
	private Map<String, Double> totalAttendValue;
	private double totalLeaveDeduction;
	public MonthlyAttendanceDto() {
	}
	public MonthlyAttendanceDto(String empType, String empNo, String empNameKr, String departmentName,
			String jobPositionName, Map<Integer, String> dailyAttendance, Map<String, Double> totalAttendValue,
			double totalLeaveDeduction) {
		super();
		this.empType = empType;
		this.empNo = empNo;
		this.empNameKr = empNameKr;
		this.departmentName = departmentName;
		this.jobPositionName = jobPositionName;
		this.dailyAttendance = dailyAttendance;
		this.totalAttendValue = totalAttendValue;
		this.totalLeaveDeduction = totalLeaveDeduction;
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
	public Map<Integer, String> getDailyAttendance() {
		return dailyAttendance;
	}
	public void setDailyAttendance(Map<Integer, String> dailyAttendance) {
		this.dailyAttendance = dailyAttendance;
	}
	public Map<String, Double> getTotalAttendValue() {
		return totalAttendValue;
	}
	public void setTotalAttendValue(Map<String, Double> totalAttendValue) {
		this.totalAttendValue = totalAttendValue;
	}
	public double getTotalLeaveDeduction() {
		return totalLeaveDeduction;
	}
	public void setTotalLeaveDeduction(double totalLeaveDeduction) {
		this.totalLeaveDeduction = totalLeaveDeduction;
	}


	
	
}
