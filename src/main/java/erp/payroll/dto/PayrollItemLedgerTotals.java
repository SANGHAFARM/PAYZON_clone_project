package erp.payroll.dto;

import java.util.ArrayList;
import java.util.List;

// 항목별 대장의 월별 및 전체 합계
public class PayrollItemLedgerTotals {

	private List<Long> monthlyAmounts = new ArrayList<>();

	public List<Long> getMonthlyAmounts() {
		return monthlyAmounts;
	}

	public void setMonthlyAmounts(List<Long> monthlyAmounts) {
		this.monthlyAmounts = monthlyAmounts;
	}

	public long getTotalAmount() {
		long total = 0;
		for (Long amount : monthlyAmounts) {
			total += amount == null ? 0 : amount;
		}
		return total;
	}
}
