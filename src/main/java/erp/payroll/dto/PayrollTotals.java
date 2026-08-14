package erp.payroll.dto;

// 지급액과 공제액의 합계를 전달하는 DTO
public class PayrollTotals {

	private long grossPayment;
	private long totalDeduction;

	public long getGrossPayment() {
		return grossPayment;
	}

	public void setGrossPayment(long grossPayment) {
		this.grossPayment = grossPayment;
	}

	public long getTotalDeduction() {
		return totalDeduction;
	}

	public void setTotalDeduction(long totalDeduction) {
		this.totalDeduction = totalDeduction;
	}

	public long getNetPayment() {
		return grossPayment - totalDeduction;
	}
}
