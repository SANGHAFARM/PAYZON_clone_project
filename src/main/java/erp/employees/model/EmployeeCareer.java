package erp.employees.model;

import java.util.Date;

/**
 * [인사/사원관리] 사원 외부 경력 이력 Model DB 테이블: EMPLOYEE_CAREER
 */
public class EmployeeCareer {

	// [DB 관리 항목]
	private int employeeCareerId; // 경력 이력 식별 번호 (PK)
	private int employeeId; // 사원 식별 번호 (FK)

	// [경력 입력 항목]
	private String companyName; // 회사명
	private Date joinDate; // 입사일자
	private Date quitDate; // 퇴사일자
	private String finalPosition; // 최종직위
	private String duty; // 담당직무
	private String quitReason; // 퇴직사유

	private Integer years; // 화면 출력용 근무기간(연) 변수
	private Integer months; // 화면 출력용 근무기간(월) 변수

	public EmployeeCareer() {
	}

	// Getter & Setter
	public int getEmployeeCareerId() {
		return employeeCareerId;
	}

	public void setEmployeeCareerId(int employeeCareerId) {
		this.employeeCareerId = employeeCareerId;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Date getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}

	public Date getQuitDate() {
		return quitDate;
	}

	public void setQuitDate(Date quitDate) {
		this.quitDate = quitDate;
	}

	public String getFinalPosition() {
		return finalPosition;
	}

	public void setFinalPosition(String finalPosition) {
		this.finalPosition = finalPosition;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public String getQuitReason() {
		return quitReason;
	}

	public void setQuitReason(String quitReason) {
		this.quitReason = quitReason;
	}

	public Integer getYears() {
		return years;
	}

	public void setYears(Integer years) {
		this.years = years;
	}

	public Integer getMonths() {
		return months;
	}

	public void setMonths(Integer months) {
		this.months = months;
	}
}