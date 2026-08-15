package erp.home.dto;

import java.util.ArrayList;
import java.util.List;

import erp.employees.dto.EmployeeSummary;
import erp.payroll.dto.PayrollRegisterPage.PayrollRegisterItem;
import erp.settings.model.Company;

// 홈 화면에 필요한 회사, 사원 및 최근 급여 현황을 전달한다.
public class HomeDashboard {

	private Company company;
	private EmployeeSummary employeeSummary;
	private List<PayrollRegisterItem> recentPayrolls = new ArrayList<>();

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public EmployeeSummary getEmployeeSummary() {
		return employeeSummary;
	}

	public void setEmployeeSummary(EmployeeSummary employeeSummary) {
		this.employeeSummary = employeeSummary;
	}

	public List<PayrollRegisterItem> getRecentPayrolls() {
		return recentPayrolls;
	}

	public void setRecentPayrolls(List<PayrollRegisterItem> recentPayrolls) {
		this.recentPayrolls = recentPayrolls;
	}

	public PayrollRegisterItem getLatestPayroll() {
		return recentPayrolls.isEmpty() ? null : recentPayrolls.get(0);
	}
}
