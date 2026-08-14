package erp.payroll.dto;

import java.util.List;

import erp.payroll.model.PayrollRun;
import erp.settings.model.Department;

// 일용직 급여입력 화면에 필요한 조회 결과
public class DayWorkerPaymentPage {

	private PayrollRun run;
	private List<DayWorkerPaymentEmployee> paymentEmployees;
	private DayWorkerPaymentEmployee selectedEmployee;
	private DayWorkerEmployeePage availableEmployeePage;
	private List<Department> departments;

	public PayrollRun getRun() {
		return run;
	}

	public void setRun(PayrollRun run) {
		this.run = run;
	}

	public List<DayWorkerPaymentEmployee> getPaymentEmployees() {
		return paymentEmployees;
	}

	public void setPaymentEmployees(List<DayWorkerPaymentEmployee> paymentEmployees) {
		this.paymentEmployees = paymentEmployees;
	}

	public DayWorkerPaymentEmployee getSelectedEmployee() {
		return selectedEmployee;
	}

	public void setSelectedEmployee(DayWorkerPaymentEmployee selectedEmployee) {
		this.selectedEmployee = selectedEmployee;
	}

	public DayWorkerEmployeePage getAvailableEmployeePage() {
		return availableEmployeePage;
	}

	public void setAvailableEmployeePage(DayWorkerEmployeePage availableEmployeePage) {
		this.availableEmployeePage = availableEmployeePage;
	}

	public List<Department> getDepartments() {
		return departments;
	}

	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}
}
