package erp.payroll.dto;

import java.util.ArrayList;
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

	public static class PayrollItemLedgerOption {
		private String itemCode;
		private String itemName;
		public PayrollItemLedgerOption(String itemCode, String itemName) { this.itemCode = itemCode; this.itemName = itemName; }
		public String getItemCode() { return itemCode; }
		public String getItemName() { return itemName; }
	}

	// 사원 한 명의 월별 항목 금액
	public static class PayrollItemLedgerRow {
		private int employeeId;
		private String employmentTypeName;
		private String employeeName;
		private String departmentName;
		private String positionName;
		private List<Long> monthlyAmounts = new ArrayList<>();
		public int getEmployeeId() { return employeeId; }
		public void setEmployeeId(int value) { employeeId = value; }
		public String getEmploymentTypeName() { return employmentTypeName; }
		public void setEmploymentTypeName(String value) { employmentTypeName = value; }
		public String getEmployeeName() { return employeeName; }
		public void setEmployeeName(String value) { employeeName = value; }
		public String getDepartmentName() { return departmentName; }
		public void setDepartmentName(String value) { departmentName = value; }
		public String getPositionName() { return positionName; }
		public void setPositionName(String value) { positionName = value; }
		public List<Long> getMonthlyAmounts() { return monthlyAmounts; }
		public void setMonthlyAmounts(List<Long> value) { monthlyAmounts = value; }
		public long getTotalAmount() { long total = 0; for (Long amount : monthlyAmounts) total += amount == null ? 0 : amount; return total; }
	}

	public static class PayrollItemLedgerTotals {
		private List<Long> monthlyAmounts = new ArrayList<>();
		public List<Long> getMonthlyAmounts() { return monthlyAmounts; }
		public void setMonthlyAmounts(List<Long> value) { monthlyAmounts = value; }
		public long getTotalAmount() { long total = 0; for (Long amount : monthlyAmounts) total += amount == null ? 0 : amount; return total; }
	}
}
