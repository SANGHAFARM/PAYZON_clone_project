package erp.payroll.dto;

import java.util.List;

// 급여대장 목록과 합계, 페이지 정보를 전달하는 DTO
public class PayrollRegisterListPage {

	private List<PayrollRegisterItem> registers;
	private PayrollRegisterTotals totals;
	private PayrollRegisterPageInfo pageInfo;

	public PayrollRegisterListPage(List<PayrollRegisterItem> registers, PayrollRegisterTotals totals,
			PayrollRegisterPageInfo pageInfo) {
		this.registers = registers;
		this.totals = totals;
		this.pageInfo = pageInfo;
	}

	public List<PayrollRegisterItem> getRegisters() {
		return registers;
	}

	public PayrollRegisterTotals getTotals() {
		return totals;
	}

	public PayrollRegisterPageInfo getPageInfo() {
		return pageInfo;
	}
}
