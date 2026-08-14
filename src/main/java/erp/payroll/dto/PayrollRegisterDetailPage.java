package erp.payroll.dto;

import java.util.List;

import erp.settings.model.Department;

// 급여대장 상세 화면의 사원별 금액과 항목 정보를 전달하는 DTO
public class PayrollRegisterDetailPage {

	private PayrollRegisterItem register;
	private List<PayrollRegisterColumn> paymentItems;
	private List<PayrollRegisterColumn> deductionItems;
	private List<PayrollRegisterEmployee> employees;
	private PayrollRegisterTotals totals;
	private List<Department> departments;

	public PayrollRegisterItem getRegister() {
		return register;
	}

	public void setRegister(PayrollRegisterItem register) {
		this.register = register;
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

	public List<PayrollRegisterEmployee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<PayrollRegisterEmployee> employees) {
		this.employees = employees;
	}

	public PayrollRegisterTotals getTotals() {
		return totals;
	}

	public void setTotals(PayrollRegisterTotals totals) {
		this.totals = totals;
	}

	public List<Department> getDepartments() {
		return departments;
	}

	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}
}
