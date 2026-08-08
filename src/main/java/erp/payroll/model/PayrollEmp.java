package erp.payroll.model;

// PAYROLL_EMP: 급여회차별 사원 급여 결과
public class PayrollEmp {
	private int payrollEmpId;
	private int payrollMstId;
	private int empId;

	// 기본 생성자
	public PayrollEmp() {
	}

	public int getPayrollEmpId() {
		return payrollEmpId;
	}

	public void setPayrollEmpId(int payrollEmpId) {
		this.payrollEmpId = payrollEmpId;
	}

	public int getPayrollMstId() {
		return payrollMstId;
	}

	public void setPayrollMstId(int payrollMstId) {
		this.payrollMstId = payrollMstId;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}
}