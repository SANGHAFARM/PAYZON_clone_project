package erp.payroll.dto;

import java.util.List;
import erp.settings.model.Department;

// 사원별 급여내역 화면의 조회 결과를 전달한다.
public class EmployeePayrollHistoryPage {

	private EmployeePayrollHistoryEmployee selectedEmployee;
	private List<EmployeePayrollHistoryEmployee> employees;
	private List<Department> departments;
	private List<EmployeePayrollHistoryItem> histories;
	private EmployeePayrollHistoryTotal total;
	private EmployeePayrollHistoryPageInfo pageInfo;

	public EmployeePayrollHistoryEmployee getSelectedEmployee() { return selectedEmployee; }
	public void setSelectedEmployee(EmployeePayrollHistoryEmployee value) { selectedEmployee = value; }
	public List<EmployeePayrollHistoryEmployee> getEmployees() { return employees; }
	public void setEmployees(List<EmployeePayrollHistoryEmployee> value) { employees = value; }
	public List<Department> getDepartments() { return departments; }
	public void setDepartments(List<Department> value) { departments = value; }
	public List<EmployeePayrollHistoryItem> getHistories() { return histories; }
	public void setHistories(List<EmployeePayrollHistoryItem> value) { histories = value; }
	public EmployeePayrollHistoryTotal getTotal() { return total; }
	public void setTotal(EmployeePayrollHistoryTotal value) { total = value; }
	public EmployeePayrollHistoryPageInfo getPageInfo() { return pageInfo; }
	public void setPageInfo(EmployeePayrollHistoryPageInfo value) { pageInfo = value; }

	// 사원별 급여내역에서 사용하는 사원 정보
	public static class EmployeePayrollHistoryEmployee {
		private int employeeId;
		private String employmentTypeName;
		private String employeeNumber;
		private String employeeName;
		private String departmentName;
		private String positionName;
		private String status;

		public int getEmployeeId() { return employeeId; }
		public void setEmployeeId(int value) { employeeId = value; }
		public String getEmploymentTypeName() { return employmentTypeName; }
		public void setEmploymentTypeName(String value) { employmentTypeName = value; }
		public String getEmployeeNumber() { return employeeNumber; }
		public void setEmployeeNumber(String value) { employeeNumber = value; }
		public String getEmployeeName() { return employeeName; }
		public void setEmployeeName(String value) { employeeName = value; }
		public String getDepartmentName() { return departmentName; }
		public void setDepartmentName(String value) { departmentName = value; }
		public String getPositionName() { return positionName; }
		public void setPositionName(String value) { positionName = value; }
		public String getStatus() { return status; }
		public void setStatus(String value) { status = value; }
	}

	// 월별 급여 및 법정 공제 내역
	public static class EmployeePayrollHistoryItem {
		private String paymentMonth;
		private String paymentRound;
		private long standardMonthlyIncome;
		private long totalPayment;
		private long totalDeduction;
		private long nationalPension;
		private long healthInsurance;
		private long longTermCareInsurance;
		private long employmentInsurance;
		private long incomeTax;
		private long localIncomeTax;

		public String getPaymentMonth() { return paymentMonth; }
		public void setPaymentMonth(String value) { paymentMonth = value; }
		public String getPaymentRound() { return paymentRound; }
		public void setPaymentRound(String value) { paymentRound = value; }
		public long getStandardMonthlyIncome() { return standardMonthlyIncome; }
		public void setStandardMonthlyIncome(long value) { standardMonthlyIncome = value; }
		public long getTotalPayment() { return totalPayment; }
		public void setTotalPayment(long value) { totalPayment = value; }
		public long getTotalDeduction() { return totalDeduction; }
		public void setTotalDeduction(long value) { totalDeduction = value; }
		public long getNetPayment() { return totalPayment - totalDeduction; }
		public long getNationalPension() { return nationalPension; }
		public void setNationalPension(long value) { nationalPension = value; }
		public long getHealthInsurance() { return healthInsurance; }
		public void setHealthInsurance(long value) { healthInsurance = value; }
		public long getLongTermCareInsurance() { return longTermCareInsurance; }
		public void setLongTermCareInsurance(long value) { longTermCareInsurance = value; }
		public long getEmploymentInsurance() { return employmentInsurance; }
		public void setEmploymentInsurance(long value) { employmentInsurance = value; }
		public long getIncomeTax() { return incomeTax; }
		public void setIncomeTax(long value) { incomeTax = value; }
		public long getLocalIncomeTax() { return localIncomeTax; }
		public void setLocalIncomeTax(long value) { localIncomeTax = value; }
	}

	public static class EmployeePayrollHistoryTotal extends EmployeePayrollHistoryItem { }

	// 사원별 급여내역 페이징 정보
	public static class EmployeePayrollHistoryPageInfo {
		private int currentPage;
		private int totalPages;
		public EmployeePayrollHistoryPageInfo(int currentPage, int totalPages) { this.currentPage = currentPage; this.totalPages = totalPages; }
		public int getCurrentPage() { return currentPage; }
		public int getStartPage() { return totalPages == 0 ? 1 : ((currentPage - 1) / 5) * 5 + 1; }
		public int getEndPage() { return Math.min(getStartPage() + 4, totalPages); }
		public boolean isHasPrevious() { return getStartPage() > 1; }
		public int getPreviousPage() { return Math.max(1, getStartPage() - 1); }
		public boolean isHasNext() { return getEndPage() < totalPages; }
		public int getNextPage() { return Math.min(totalPages, getEndPage() + 1); }
	}
}
