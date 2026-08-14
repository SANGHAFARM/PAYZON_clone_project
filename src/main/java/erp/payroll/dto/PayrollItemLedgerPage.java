package erp.payroll.dto;

import java.util.List;

// 항목별 대장 화면 조회 결과
public class PayrollItemLedgerPage {

	private List<PayrollItemLedgerOption> items;
	private List<String> months;
	private List<PayrollItemLedgerRow> rows;
	private PayrollItemLedgerTotals totals;

	public List<PayrollItemLedgerOption> getItems() { return items; }
	public void setItems(List<PayrollItemLedgerOption> value) { items = value; }
	public List<String> getMonths() { return months; }
	public void setMonths(List<String> value) { months = value; }
	public List<PayrollItemLedgerRow> getRows() { return rows; }
	public void setRows(List<PayrollItemLedgerRow> value) { rows = value; }
	public PayrollItemLedgerTotals getTotals() { return totals; }
	public void setTotals(PayrollItemLedgerTotals value) { totals = value; }
}
