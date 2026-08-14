package erp.payroll.dto;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

// 급여명세서의 사원 기본정보와 지급·공제 내역
public class PayrollPayslipEmployee {

	private int employeeId;
	private String employmentTypeName;
	private String employeeName;
	private String birthDate;
	private String departmentName;
	private String positionName;
	private Date hireDate;
	private Map<Integer, Long> paymentAmounts = new LinkedHashMap<>();
	private Map<Integer, Long> deductionAmounts = new LinkedHashMap<>();
	private Map<Integer, String> paymentCalculations = new LinkedHashMap<>();
	private Map<Integer, String> deductionCalculations = new LinkedHashMap<>();

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

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
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

	public Date getHireDate() {
		return hireDate;
	}

	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}

	public Map<Integer, Long> getPaymentAmounts() {
		return paymentAmounts;
	}

	public Map<Integer, Long> getDeductionAmounts() {
		return deductionAmounts;
	}

	public Map<Integer, String> getPaymentCalculations() {
		return paymentCalculations;
	}

	public Map<Integer, String> getDeductionCalculations() {
		return deductionCalculations;
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
