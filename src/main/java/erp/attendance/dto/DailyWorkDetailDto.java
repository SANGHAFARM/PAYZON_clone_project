package erp.attendance.dto;

import java.util.Date;
//일용직 근무 상세조회에서 데이터를 담을 클래스
public class DailyWorkDetailDto {
	private Date workDate;//근무일자
	private String empNo;//사원번호
	private String empNameKr;//성명(EMPLOYEE)
	private String departmentName;//부서(DEPARTMNET)
	private String projectName;//현장/프로젝트(PROJECT)
	private long dailyPay;//일당
	private double payRate;//지급율
	private long incomeTax;//소득세
	private long localIncomeTax;//지방소득세
	private long actualPay;//실지급액
	public DailyWorkDetailDto() {
	}
	public DailyWorkDetailDto(Date workDate, String empNo, String empNameKr, String departmentName, String projectName,
			long dailyPay, double payRate, long incomeTax, long localIncomeTax, long actualPay) {
		super();
		this.workDate = workDate;
		this.empNo = empNo;
		this.empNameKr = empNameKr;
		this.departmentName = departmentName;
		this.projectName = projectName;
		this.dailyPay = dailyPay;
		this.payRate = payRate;
		this.incomeTax = incomeTax;
		this.localIncomeTax = localIncomeTax;
		this.actualPay = actualPay;
	}
	public Date getWorkDate() {
		return workDate;
	}
	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
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
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public long getDailyPay() {
		return dailyPay;
	}
	public void setDailyPay(long dailyPay) {
		this.dailyPay = dailyPay;
	}
	public double getPayRate() {
		return payRate;
	}
	public void setPayRate(double payRate) {
		this.payRate = payRate;
	}
	public long getIncomeTax() {
		return incomeTax;
	}
	public void setIncomeTax(long incomeTax) {
		this.incomeTax = incomeTax;
	}
	public long getLocalIncomeTax() {
		return localIncomeTax;
	}
	public void setLocalIncomeTax(long localIncomeTax) {
		this.localIncomeTax = localIncomeTax;
	}
	public long getActualPay() {
		return actualPay;
	}
	public void setActualPay(long actualPay) {
		this.actualPay = actualPay;
	}
	
	
}
