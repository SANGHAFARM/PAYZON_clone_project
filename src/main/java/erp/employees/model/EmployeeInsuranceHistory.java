package erp.employees.model;

import java.util.Date;

/**
 * [인사/사원관리] 사원별 4대보험 취득/상실 이력 Model DB 테이블: EMPLOYEE_INSURANCE_HISTORY
 */
public class EmployeeInsuranceHistory {

	// [DB 관리 항목]
	private int employeeInsuranceHistoryId; // 4대보험 자격정보 식별 번호 (PK)
	private int employeeId; // 사원 식별 번호 (FK)

	// [4대보험 취득·상실 이력]
	private String insuranceType; // 구분 (국민연금 등)
	private String symbolNo; // 기호번호
	private Date acquireDate; // 취득일
	private Date lossDate; // 상실일

	public EmployeeInsuranceHistory() {
	}

	// Getter & Setter
	public int getEmployeeInsuranceHistoryId() {
		return employeeInsuranceHistoryId;
	}

	public void setEmployeeInsuranceHistoryId(int employeeInsuranceHistoryId) {
		this.employeeInsuranceHistoryId = employeeInsuranceHistoryId;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getInsuranceType() {
		return insuranceType;
	}

	public void setInsuranceType(String insuranceType) {
		this.insuranceType = insuranceType;
	}

	public String getSymbolNo() {
		return symbolNo;
	}

	public void setSymbolNo(String symbolNo) {
		this.symbolNo = symbolNo;
	}

	public Date getAcquireDate() {
		return acquireDate;
	}

	public void setAcquireDate(Date acquireDate) {
		this.acquireDate = acquireDate;
	}

	public Date getLossDate() {
		return lossDate;
	}

	public void setLossDate(Date lossDate) {
		this.lossDate = lossDate;
	}
}