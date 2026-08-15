package erp.attendance.dto;

import java.util.Date;

public class EmployeeLeaveRow {

	// [휴가 현황 데이터]
	// 사원별 휴가 현황 식별 번호 (값이 없으면 0 처리)
	private int empLeaveId;

	// 사원 식별 번호
	private int employeeId;

	// 최종 부여된 휴가일수
	private double leaveDays;

	// [사원 기본정보 조인 데이터]
	// 고용 형태 (정규직, 계약직 등)
	private String empType;

	// 사원번호
	private String empNo;

	// 사원 성명 (한글)
	private String empName;

	// 소속 부서명
	private String deptName;

	// 직위명
	private String posName;

	// 입사일 (근속년수 계산 및 화면 출력용)
	private Date joinDate;

	// ==========================================
	// Getter 및 Setter 구현부
	// ==========================================

	public int getEmpLeaveId() {
		return empLeaveId;
	}

	public void setEmpLeaveId(int empLeaveId) {
		this.empLeaveId = empLeaveId;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public double getLeaveDays() {
		return leaveDays;
	}

	public void setLeaveDays(double leaveDays) {
		this.leaveDays = leaveDays;
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

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getPosName() {
		return posName;
	}

	public void setPosName(String posName) {
		this.posName = posName;
	}

	public Date getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}
}