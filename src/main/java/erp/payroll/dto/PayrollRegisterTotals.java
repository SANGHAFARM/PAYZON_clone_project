package erp.payroll.dto;

import java.util.LinkedHashMap;
import java.util.Map;

// 급여대장 목록과 상세의 합계 정보
public class PayrollRegisterTotals {

	private long totalPayment;
	private long totalDeduction;
	private Map<Integer, Long> paymentAmounts = new LinkedHashMap<>();
	private Map<Integer, Long> deductionAmounts = new LinkedHashMap<>();

	public long getTotalPayment() {
		return totalPayment;
	}

	public void setTotalPayment(long totalPayment) {
		this.totalPayment = totalPayment;
	}

	public long getTotalDeduction() {
		return totalDeduction;
	}

	public void setTotalDeduction(long totalDeduction) {
		this.totalDeduction = totalDeduction;
	}

	public long getNetPayment() {
		return totalPayment - totalDeduction;
	}

	public Map<Integer, Long> getPaymentAmounts() {
		return paymentAmounts;
	}

	public Map<Integer, Long> getDeductionAmounts() {
		return deductionAmounts;
	}
}
