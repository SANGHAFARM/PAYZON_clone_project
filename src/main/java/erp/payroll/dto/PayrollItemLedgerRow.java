package erp.payroll.dto;

import java.util.ArrayList;
import java.util.List;

// 사원 한 명의 월별 항목 금액
public class PayrollItemLedgerRow {

	private int employeeId;
	private String employmentTypeName;
	private String employeeName;
	private String departmentName;
	private String positionName;
	private List<Long> monthlyAmounts = new ArrayList<>();

	public int getEmployeeId() { return employeeId; }
	public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
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

	public long getTotalAmount() {
		long total = 0;
		for (Long amount : monthlyAmounts) {
			total += amount == null ? 0 : amount;
		}
		return total;
	}
}
