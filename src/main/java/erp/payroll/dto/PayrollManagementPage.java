package erp.payroll.dto;

import java.util.List;

import erp.payroll.model.PayrollRun;
import erp.settings.model.Department;
import erp.settings.model.AttendanceItem;
import erp.settings.model.TaxFreeItem;

// 급여입력 화면에 필요한 조회 결과를 한 번에 전달한다.
public class PayrollManagementPage {

	private PayrollRun run;
	private List<PayrollManagementEmployee> paymentEmployees;
	private PayrollManagementEmployee selectedEmployee;
	private List<PayrollManagementItem> paymentGiveItems;
	private List<PayrollManagementItem> paymentDeductionItems;
	private PayrollTotals paymentTotals;
	private PayrollEmployeePage availableEmployeePage;
	private List<Department> departments;
	private List<PayrollPositionOption> positions;
	private List<PayrollPeriodOption> previousPaymentPeriods;
	private List<TaxFreeItem> taxFreeItems;
	private List<AttendanceItem> attendanceItems;

	public PayrollRun getRun() {
		return run;
	}

	public void setRun(PayrollRun run) {
		this.run = run;
	}

	public List<PayrollManagementEmployee> getPaymentEmployees() {
		return paymentEmployees;
	}

	public void setPaymentEmployees(List<PayrollManagementEmployee> paymentEmployees) {
		this.paymentEmployees = paymentEmployees;
	}

	public PayrollManagementEmployee getSelectedEmployee() {
		return selectedEmployee;
	}

	public void setSelectedEmployee(PayrollManagementEmployee selectedEmployee) {
		this.selectedEmployee = selectedEmployee;
	}

	public List<PayrollManagementItem> getPaymentGiveItems() {
		return paymentGiveItems;
	}

	public void setPaymentGiveItems(List<PayrollManagementItem> paymentGiveItems) {
		this.paymentGiveItems = paymentGiveItems;
	}

	public List<PayrollManagementItem> getPaymentDeductionItems() {
		return paymentDeductionItems;
	}

	public void setPaymentDeductionItems(List<PayrollManagementItem> paymentDeductionItems) {
		this.paymentDeductionItems = paymentDeductionItems;
	}

	public PayrollTotals getPaymentTotals() {
		return paymentTotals;
	}

	public void setPaymentTotals(PayrollTotals paymentTotals) {
		this.paymentTotals = paymentTotals;
	}

	public PayrollEmployeePage getAvailableEmployeePage() {
		return availableEmployeePage;
	}

	public void setAvailableEmployeePage(PayrollEmployeePage availableEmployeePage) {
		this.availableEmployeePage = availableEmployeePage;
	}

	public List<Department> getDepartments() {
		return departments;
	}

	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}

	public List<PayrollPositionOption> getPositions() {
		return positions;
	}

	public void setPositions(List<PayrollPositionOption> positions) {
		this.positions = positions;
	}

	public List<PayrollPeriodOption> getPreviousPaymentPeriods() {
		return previousPaymentPeriods;
	}

	public void setPreviousPaymentPeriods(List<PayrollPeriodOption> previousPaymentPeriods) {
		this.previousPaymentPeriods = previousPaymentPeriods;
	}

	public List<TaxFreeItem> getTaxFreeItems() {
		return taxFreeItems;
	}

	public void setTaxFreeItems(List<TaxFreeItem> taxFreeItems) {
		this.taxFreeItems = taxFreeItems;
	}

	public List<AttendanceItem> getAttendanceItems() {
		return attendanceItems;
	}

	public void setAttendanceItems(List<AttendanceItem> attendanceItems) {
		this.attendanceItems = attendanceItems;
	}

	// 급여입력 화면에 표시할 사원별 급여 정보
	public static class PayrollManagementEmployee {
		private int employeeId;
		private String employmentType;
		private String employeeNo;
		private String name;
		private String departmentName;
		private String positionName;
		private String statusName;
		private long grossPayment;
		private long totalDeduction;
		public int getEmployeeId() { return employeeId; }
		public void setEmployeeId(int value) { employeeId = value; }
		public String getEmploymentType() { return employmentType; }
		public void setEmploymentType(String value) { employmentType = value; }
		public String getEmployeeNo() { return employeeNo; }
		public void setEmployeeNo(String value) { employeeNo = value; }
		public String getName() { return name; }
		public void setName(String value) { name = value; }
		public String getDepartmentName() { return departmentName; }
		public void setDepartmentName(String value) { departmentName = value; }
		public String getPositionName() { return positionName; }
		public void setPositionName(String value) { positionName = value; }
		public String getStatusName() { return statusName; }
		public void setStatusName(String value) { statusName = value; }
		public long getGrossPayment() { return grossPayment; }
		public void setGrossPayment(long value) { grossPayment = value; }
		public long getTotalDeduction() { return totalDeduction; }
		public void setTotalDeduction(long value) { totalDeduction = value; }
		public long getNetPayment() { return grossPayment - totalDeduction; }
	}

	public static class PayrollEmployeePage {
		private List<PayrollManagementEmployee> content;
		private int totalPages;
		public PayrollEmployeePage(List<PayrollManagementEmployee> content, int totalPages) { this.content = content; this.totalPages = totalPages; }
		public List<PayrollManagementEmployee> getContent() { return content; }
		public int getTotalPages() { return totalPages; }
	}

	public static class PayrollTotals {
		private long grossPayment;
		private long totalDeduction;
		public long getGrossPayment() { return grossPayment; }
		public void setGrossPayment(long value) { grossPayment = value; }
		public long getTotalDeduction() { return totalDeduction; }
		public void setTotalDeduction(long value) { totalDeduction = value; }
		public long getNetPayment() { return grossPayment - totalDeduction; }
	}

	public static class PayrollPositionOption {
		private int positionId;
		private String positionName;
		public PayrollPositionOption(int id, String name) { positionId = id; positionName = name; }
		public int getPositionId() { return positionId; }
		public String getPositionName() { return positionName; }
	}

	public static class PayrollPeriodOption {
		private int periodId;
		private String periodName;
		public PayrollPeriodOption(int id, String name) { periodId = id; periodName = name; }
		public int getPeriodId() { return periodId; }
		public String getPeriodName() { return periodName; }
	}
}
