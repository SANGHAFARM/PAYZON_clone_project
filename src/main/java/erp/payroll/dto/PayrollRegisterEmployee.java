package erp.payroll.dto;

import java.util.LinkedHashMap;
import java.util.Map;

// 급여대장 상세에 표시할 사원별 지급·공제 정보
public class PayrollRegisterEmployee {

	private int employeeId;
	private String employmentTypeName;
	private String employeeName;
	private String departmentName;
	private String positionName;
	private Map<Integer, Long> paymentAmounts = new LinkedHashMap<>();
	private Map<Integer, Long> deductionAmounts = new LinkedHashMap<>();

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmploymentTypeName() {
		return employmentTypeName;
	}

	public void setEmploymentTypeName(String employmentTypeName) {
		this.employmentTypeName = employmentTypeName;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	public Map<Integer, Long> getPaymentAmounts() {
		return paymentAmounts;
	}

	public Map<Integer, Long> getDeductionAmounts() {
		return deductionAmounts;
	}

	public long getTotalPayment() {
		return sum(paymentAmounts);
	}

	public long getTotalDeduction() {
		return sum(deductionAmounts);
	}

	public long getNetPayment() {
		return getTotalPayment() - getTotalDeduction();
	}

	private long sum(Map<Integer, Long> amounts) {
		long total = 0;
		for (Long amount : amounts.values()) {
			total += amount == null ? 0 : amount;
		}
		return total;
	}
}
