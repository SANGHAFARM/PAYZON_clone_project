package erp.payroll.dto;

import java.util.List;
import erp.settings.model.Department;

// 사원별 급여내역 화면 조회 결과
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
}
