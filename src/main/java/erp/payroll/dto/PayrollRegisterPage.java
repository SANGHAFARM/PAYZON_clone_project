package erp.payroll.dto;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import erp.settings.model.Department;

// 급여대장 목록과 상세 화면의 조회 결과를 묶어서 전달한다.
public class PayrollRegisterPage {

	public static class PayrollRegisterListPage {
		private List<PayrollRegisterItem> registers;
		private PayrollRegisterTotals totals;
		private PayrollRegisterPageInfo pageInfo;
		public PayrollRegisterListPage(List<PayrollRegisterItem> registers, PayrollRegisterTotals totals, PayrollRegisterPageInfo pageInfo) {
			this.registers = registers; this.totals = totals; this.pageInfo = pageInfo;
		}
		public List<PayrollRegisterItem> getRegisters() { return registers; }
		public PayrollRegisterTotals getTotals() { return totals; }
		public PayrollRegisterPageInfo getPageInfo() { return pageInfo; }
	}

	public static class PayrollRegisterDetailPage {
		private PayrollRegisterItem register;
		private List<PayrollRegisterColumn> paymentItems;
		private List<PayrollRegisterColumn> deductionItems;
		private List<PayrollRegisterEmployee> employees;
		private PayrollRegisterTotals totals;
		private List<Department> departments;
		public PayrollRegisterItem getRegister() { return register; }
		public void setRegister(PayrollRegisterItem value) { register = value; }
		public List<PayrollRegisterColumn> getPaymentItems() { return paymentItems; }
		public void setPaymentItems(List<PayrollRegisterColumn> value) { paymentItems = value; }
		public List<PayrollRegisterColumn> getDeductionItems() { return deductionItems; }
		public void setDeductionItems(List<PayrollRegisterColumn> value) { deductionItems = value; }
		public List<PayrollRegisterEmployee> getEmployees() { return employees; }
		public void setEmployees(List<PayrollRegisterEmployee> value) { employees = value; }
		public PayrollRegisterTotals getTotals() { return totals; }
		public void setTotals(PayrollRegisterTotals value) { totals = value; }
		public List<Department> getDepartments() { return departments; }
		public void setDepartments(List<Department> value) { departments = value; }
	}

	public static class PayrollRegisterItem {
		private int registerId;
		private String paymentYear;
		private String paymentYearMonth;
		private String paymentRoundName;
		private String incomeType;
		private Date calculationStart;
		private Date calculationEnd;
		private Date paymentDate;
		private int employeeCount;
		private long totalPayment;
		private long totalDeduction;
		public int getRegisterId() { return registerId; }
		public void setRegisterId(int value) { registerId = value; }
		public String getPaymentYear() { return paymentYear; }
		public void setPaymentYear(String value) { paymentYear = value; }
		public String getPaymentYearMonth() { return paymentYearMonth; }
		public void setPaymentYearMonth(String value) { paymentYearMonth = value; }
		public String getPaymentRoundName() { return paymentRoundName; }
		public void setPaymentRoundName(String value) { paymentRoundName = value; }
		public String getIncomeType() { return incomeType; }
		public void setIncomeType(String value) { incomeType = value; }
		public Date getCalculationStart() { return calculationStart; }
		public void setCalculationStart(Date value) { calculationStart = value; }
		public Date getCalculationEnd() { return calculationEnd; }
		public void setCalculationEnd(Date value) { calculationEnd = value; }
		public Date getPaymentDate() { return paymentDate; }
		public void setPaymentDate(Date value) { paymentDate = value; }
		public int getEmployeeCount() { return employeeCount; }
		public void setEmployeeCount(int value) { employeeCount = value; }
		public long getTotalPayment() { return totalPayment; }
		public void setTotalPayment(long value) { totalPayment = value; }
		public long getTotalDeduction() { return totalDeduction; }
		public void setTotalDeduction(long value) { totalDeduction = value; }
		public long getNetPayment() { return totalPayment - totalDeduction; }
	}

	public static class PayrollRegisterEmployee {
		private int employeeId;
		private String employmentTypeName;
		private String employeeName;
		private String departmentName;
		private String positionName;
		private Map<Integer, Long> paymentAmounts = new LinkedHashMap<>();
		private Map<Integer, Long> deductionAmounts = new LinkedHashMap<>();
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
		public Map<Integer, Long> getPaymentAmounts() { return paymentAmounts; }
		public Map<Integer, Long> getDeductionAmounts() { return deductionAmounts; }
		public long getTotalPayment() { return sum(paymentAmounts); }
		public long getTotalDeduction() { return sum(deductionAmounts); }
		public long getNetPayment() { return getTotalPayment() - getTotalDeduction(); }
		private long sum(Map<Integer, Long> values) { long total = 0; for (Long value : values.values()) total += value == null ? 0 : value; return total; }
	}

	public static class PayrollRegisterTotals {
		private long totalPayment;
		private long totalDeduction;
		private Map<Integer, Long> paymentAmounts = new LinkedHashMap<>();
		private Map<Integer, Long> deductionAmounts = new LinkedHashMap<>();
		public long getTotalPayment() { return totalPayment; }
		public void setTotalPayment(long value) { totalPayment = value; }
		public long getTotalDeduction() { return totalDeduction; }
		public void setTotalDeduction(long value) { totalDeduction = value; }
		public long getNetPayment() { return totalPayment - totalDeduction; }
		public Map<Integer, Long> getPaymentAmounts() { return paymentAmounts; }
		public Map<Integer, Long> getDeductionAmounts() { return deductionAmounts; }
	}

	public static class PayrollRegisterPageInfo {
		private int number;
		private int totalPages;
		public PayrollRegisterPageInfo(int number, int totalPages) { this.number = number; this.totalPages = totalPages; }
		public int getNumber() { return number; }
		public int getTotalPages() { return totalPages; }
	}

	public static class PayrollEmploymentTypeOption {
		private String code;
		private String name;
		public PayrollEmploymentTypeOption(String code, String name) { this.code = code; this.name = name; }
		public String getCode() { return code; }
		public String getName() { return name; }
	}
}
