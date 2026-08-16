package erp.payroll.dto;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// 급여명세서 화면에 필요한 회차와 사원 정보를 전달한다.
public class PayrollPayslipPage {

	private Date calculationStart;
	private Date calculationEnd;
	private Date paymentDate;
	private List<PayrollPayslipEmployee> employees;
	private PayrollPayslipEmployee selectedEmployee;
	private List<PayrollRegisterColumn> paymentItems;
	private List<PayrollRegisterColumn> deductionItems;
	private PayrollPayslipCompany company;

	public Date getCalculationStart() {
		return calculationStart;
	}

	public void setCalculationStart(Date calculationStart) {
		this.calculationStart = calculationStart;
	}

	public Date getCalculationEnd() {
		return calculationEnd;
	}

	public void setCalculationEnd(Date calculationEnd) {
		this.calculationEnd = calculationEnd;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public List<PayrollPayslipEmployee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<PayrollPayslipEmployee> employees) {
		this.employees = employees;
	}

	public PayrollPayslipEmployee getSelectedEmployee() {
		return selectedEmployee;
	}

	public void setSelectedEmployee(PayrollPayslipEmployee selectedEmployee) {
		this.selectedEmployee = selectedEmployee;
	}

	public List<PayrollRegisterColumn> getPaymentItems() {
		return paymentItems;
	}

	public void setPaymentItems(List<PayrollRegisterColumn> paymentItems) {
		this.paymentItems = paymentItems;
	}

	public List<PayrollRegisterColumn> getDeductionItems() {
		return deductionItems;
	}

	public void setDeductionItems(List<PayrollRegisterColumn> deductionItems) {
		this.deductionItems = deductionItems;
	}

	public PayrollPayslipCompany getCompany() {
		return company;
	}

	public void setCompany(PayrollPayslipCompany company) {
		this.company = company;
	}

	public static class PayrollPayslipCompany {
		private String companyName;
		private String representativeName;
		private String logoUrl;
		private String stampUrl;
		public String getCompanyName() { return companyName; }
		public void setCompanyName(String value) { companyName = value; }
		public String getRepresentativeName() { return representativeName; }
		public void setRepresentativeName(String value) { representativeName = value; }
		public String getLogoUrl() { return logoUrl; }
		public void setLogoUrl(String value) { logoUrl = value; }
		public String getStampUrl() { return stampUrl; }
		public void setStampUrl(String value) { stampUrl = value; }
	}

	// 급여명세서의 사원 기본정보와 지급·공제 내역
	public static class PayrollPayslipEmployee {
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
		public int getEmployeeId() { return employeeId; }
		public void setEmployeeId(int value) { employeeId = value; }
		public String getEmploymentTypeName() { return employmentTypeName; }
		public void setEmploymentTypeName(String value) { employmentTypeName = value; }
		public String getEmployeeName() { return employeeName; }
		public void setEmployeeName(String value) { employeeName = value; }
		public String getBirthDate() { return birthDate; }
		public void setBirthDate(String value) { birthDate = value; }
		public String getDepartmentName() { return departmentName; }
		public void setDepartmentName(String value) { departmentName = value; }
		public String getPositionName() { return positionName; }
		public void setPositionName(String value) { positionName = value; }
		public Date getHireDate() { return hireDate; }
		public void setHireDate(Date value) { hireDate = value; }
		public Map<Integer, Long> getPaymentAmounts() { return paymentAmounts; }
		public Map<Integer, Long> getDeductionAmounts() { return deductionAmounts; }
		public Map<Integer, String> getPaymentCalculations() { return paymentCalculations; }
		public Map<Integer, String> getDeductionCalculations() { return deductionCalculations; }
		public long getTotalPayment() { return sum(paymentAmounts); }
		public long getTotalDeduction() { return sum(deductionAmounts); }
		public long getNetPayment() { return getTotalPayment() - getTotalDeduction(); }
		private long sum(Map<Integer, Long> values) { long total = 0; for (Long value : values.values()) total += value == null ? 0 : value; return total; }
	}
}
