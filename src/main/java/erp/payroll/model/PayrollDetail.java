package erp.payroll.model;

// PAYROLL_DETAIL: 사원별 지급·공제 상세
public class PayrollDetail {
	private int payrollDtlId;
	private int payrollEmpId;
	private Integer payItemId;
	private Integer deductItemId;
	private long amount;

	// 기본 생성자
	public PayrollDetail() {
	}

	public int getPayrollDtlId() {
		return payrollDtlId;
	}

	public void setPayrollDtlId(int payrollDtlId) {
		this.payrollDtlId = payrollDtlId;
	}

	public int getPayrollEmpId() {
		return payrollEmpId;
	}

	public void setPayrollEmpId(int payrollEmpId) {
		this.payrollEmpId = payrollEmpId;
	}

	public Integer getPayItemId() {
		return payItemId;
	}

	public void setPayItemId(Integer payItemId) {
		this.payItemId = payItemId;
	}

	public Integer getDeductItemId() {
		return deductItemId;
	}

	public void setDeductItemId(Integer deductItemId) {
		this.deductItemId = deductItemId;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}
}