package erp.payroll.dto;

import java.util.Date;
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

	// 급여 대상에 추가할 일용직 사원 목록과 페이지 정보
	public static class DayWorkerEmployeePage {
		private List<DayWorkerPaymentEmployee> content;
		private int totalPages;

		public DayWorkerEmployeePage(List<DayWorkerPaymentEmployee> content, int totalPages) {
			this.content = content;
			this.totalPages = totalPages;
		}

		public List<DayWorkerPaymentEmployee> getContent() { return content; }
		public int getTotalPages() { return totalPages; }
	}

	// 일용직 사원의 근무일별 지급 내역
	public static class DayWorkerPaymentWork {
		private Date workDate;
		private int paymentRate;
		private long paymentAmount;
		private long incomeTax;
		private long localIncomeTax;

		public Date getWorkDate() { return workDate; }
		public void setWorkDate(Date workDate) { this.workDate = workDate; }
		public int getPaymentRate() { return paymentRate; }
		public void setPaymentRate(int paymentRate) { this.paymentRate = paymentRate; }
		public long getPaymentAmount() { return paymentAmount; }
		public void setPaymentAmount(long paymentAmount) { this.paymentAmount = paymentAmount; }
		public long getIncomeTax() { return incomeTax; }
		public void setIncomeTax(long incomeTax) { this.incomeTax = incomeTax; }
		public long getLocalIncomeTax() { return localIncomeTax; }
		public void setLocalIncomeTax(long localIncomeTax) { this.localIncomeTax = localIncomeTax; }
	}
}
