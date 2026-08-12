package erp.payroll.model;

/**
 * [급여관리] 급여회차별 대상 사원 급여 결과 Model DB 테이블: PAYROLL_EMPLOYEE
 */
public class PayrollEmployee {

	// [DB 관리 항목]
	private Long payrollEmployeeId; // 사원별 급여결과 식별 번호 (PK)
	private Long payrollRunId; // 급여회차 마스터 식별 번호 (FK)
	private Long employeeId; // 대상 사원 식별 번호 (FK)

	public PayrollEmployee() {
	}

	// Getter & Setter
	public Long getPayrollEmployeeId() {
		return payrollEmployeeId;
	}

	public void setPayrollEmployeeId(Long payrollEmployeeId) {
		this.payrollEmployeeId = payrollEmployeeId;
	}

	public Long getPayrollRunId() {
		return payrollRunId;
	}

	public void setPayrollRunId(Long payrollRunId) {
		this.payrollRunId = payrollRunId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}
}