package erp.payroll.model;

/**
 * [급여관리] 급여회차별 대상 사원 급여 결과 Model DB 테이블: PAYROLL_EMPLOYEE
 */
public class PayrollEmployee {

	// [DB 관리 항목]
	private int payrollEmployeeId; // 사원별 급여결과 식별 번호 (PK)
	private int payrollRunId; // 급여회차 마스터 식별 번호 (FK)
	private int employeeId; // 대상 사원 식별 번호 (FK)

	public PayrollEmployee() {
	}

	// Getter & Setter
	public int getPayrollEmployeeId() {
		return payrollEmployeeId;
	}

	public void setPayrollEmployeeId(int payrollEmployeeId) {
		this.payrollEmployeeId = payrollEmployeeId;
	}

	public int getPayrollRunId() {
		return payrollRunId;
	}

	public void setPayrollRunId(int payrollRunId) {
		this.payrollRunId = payrollRunId;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
}