package erp.attendance.dto;

import java.util.Map;

//일용직 근무 월별 조회 결과를 담을 클래스
public class DailyWorkListDto {
	// 구분, 사원번호, 성명, 부서, 근무기록, 합계, 소득세합계, 지방소득세 합계,실지급합계 
	private String empType;
	private String empNo;
	private String empNameKr;
	private String departmentName;
	private Map<Integer, DailyWorkRecordDto> workDayMap;
	private int totalDays;
	private long totalIncomeTax;
	private long totalLocalIncomeTax;
	private long totalActualPay;
	public DailyWorkListDto() {
	}
	public DailyWorkListDto(String empType, String empNo, String empNameKr, String departmentName,
			Map<Integer, DailyWorkRecordDto> workDayMap, int totalDays, long totalIncomeTax, long totalLocalIncomeTax,
			long totalActualPay) {
		super();
		this.empType = empType;
		this.empNo = empNo;
		this.empNameKr = empNameKr;
		this.departmentName = departmentName;
		this.workDayMap = workDayMap;
		this.totalDays = totalDays;
		this.totalIncomeTax = totalIncomeTax;
		this.totalLocalIncomeTax = totalLocalIncomeTax;
		this.totalActualPay = totalActualPay;
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
	public Map<Integer, DailyWorkRecordDto> getWorkDayMap() {
		return workDayMap;
	}
	public void setWorkDayMap(Map<Integer, DailyWorkRecordDto> workDayMap) {
		this.workDayMap = workDayMap;
	}
	public int getTotalDays() {
		return totalDays;
	}
	public void setTotalDays(int totalDays) {
		this.totalDays = totalDays;
	}
	public long getTotalIncomeTax() {
		return totalIncomeTax;
	}
	public void setTotalIncomeTax(long totalIncomeTax) {
		this.totalIncomeTax = totalIncomeTax;
	}
	public long getTotalLocalIncomeTax() {
		return totalLocalIncomeTax;
	}
	public void setTotalLocalIncomeTax(long totalLocalIncomeTax) {
		this.totalLocalIncomeTax = totalLocalIncomeTax;
	}
	public long getTotalActualPay() {
		return totalActualPay;
	}
	public void setTotalActualPay(long totalActualPay) {
		this.totalActualPay = totalActualPay;
	}
	
	
}
