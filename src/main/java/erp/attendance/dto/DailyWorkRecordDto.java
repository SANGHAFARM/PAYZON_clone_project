package erp.attendance.dto;

import java.util.Date;

//일용직 근무기록 관리 & 월별조회에서 빨간 점 클릭 시 사용
public class DailyWorkRecordDto {
    private int dailyWorkRecordId; //일용직 근무기록ID
    private String empNameKr; //근무자 이름
    private Date workDate; //근무일자
    private Integer projectId; //프로젝트ID
    private String projectName;//프로젝트 이름
    private long dailyPay; //일당
    private double payRate; //지급율
    private long grossPay;   // 지급액(일당*지급율)
    private long incomeTax; //소득세
    private long localIncomeTax; //지방소득세
    private long actualPay; //실지급액
    
	public DailyWorkRecordDto() {
	}

	public DailyWorkRecordDto(int dailyWorkRecordId, String empNameKr, Date workDate, Integer projectId,
			String projectName, long dailyPay, double payRate, long grossPay, long incomeTax, long localIncomeTax,
			long actualPay) {
		super();
		this.dailyWorkRecordId = dailyWorkRecordId;
		this.empNameKr = empNameKr;
		this.workDate = workDate;
		this.projectId = projectId;
		this.projectName = projectName;
		this.dailyPay = dailyPay;
		this.payRate = payRate;
		this.grossPay = grossPay;
		this.incomeTax = incomeTax;
		this.localIncomeTax = localIncomeTax;
		this.actualPay = actualPay;
	}

	public int getDailyWorkRecordId() {
		return dailyWorkRecordId;
	}
	public void setDailyWorkRecordId(int dailyWorkRecordId) {
		this.dailyWorkRecordId = dailyWorkRecordId;
	}

	public String getEmpNameKr() {
		return empNameKr;
	}
	public void setEmpNameKr(String empNameKr) {
		this.empNameKr = empNameKr;
	}
	public Date getWorkDate() {
		return workDate;
	}
	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}
	public Integer getProjectId() {
		return projectId;
	}
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
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
	public long getGrossPay() {
		return grossPay;
	}
	public void setGrossPay(long grossPay) {
		this.grossPay = grossPay;
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
