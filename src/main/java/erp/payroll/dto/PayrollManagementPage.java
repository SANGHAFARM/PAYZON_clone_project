package erp.payroll.dto;

import java.util.List;

import erp.payroll.model.PayrollRun;
import erp.settings.model.Department;

// 급여입력 화면에 필요한 조회 결과를 한 번에 전달하는 DTO
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
}
