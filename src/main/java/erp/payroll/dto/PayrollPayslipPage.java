package erp.payroll.dto;

import java.util.Date;
import java.util.List;

// 급여명세서 화면에 필요한 회차와 사원 정보를 전달하는 DTO
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
}
