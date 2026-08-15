package erp.payroll.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import erp.payroll.dao.DayWorkerPayrollDao;
import erp.payroll.dao.PayrollManagementDao;
import erp.payroll.dto.DayWorkerPaymentPage.DayWorkerEmployeePage;
import erp.payroll.dto.DayWorkerPaymentEmployee;
import erp.payroll.dto.DayWorkerPaymentPage;
import erp.payroll.dto.PayrollManagementItem;
import erp.payroll.model.PayrollRun;
import erp.settings.dao.DepartmentDao;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

// 일용직 근무기록을 급여 회차와 연결하는 서비스
public class DayWorkerPayrollService {

	private static final int EMPLOYEE_PAGE_SIZE = 10;

	private DayWorkerPayrollDao dayWorkerDao = new DayWorkerPayrollDao();
	private PayrollManagementDao managementDao = new PayrollManagementDao();
	private DepartmentDao departmentDao = DepartmentDao.getInstance();

	public DayWorkerPaymentPage getPage(PayrollRun requestRun, Integer employeeId, String keyword,
			Integer departmentId, int employeePage) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			PayrollRun run = managementDao.selectRun(conn, requestRun.getPayYear(), requestRun.getPayMonth(),
					requestRun.getPaySeq(), "2");
			int runId = run == null ? 0 : run.getPayrollRunId();
			Date startDate = run == null ? requestRun.getCalcStartDate() : run.getCalcStartDate();
			Date endDate = run == null ? requestRun.getCalcEndDate() : run.getCalcEndDate();
			List<DayWorkerPaymentEmployee> employees = run == null ? new ArrayList<>()
					: dayWorkerDao.selectPaymentEmployees(conn, runId, startDate, endDate);
			DayWorkerPaymentEmployee selectedEmployee = selectEmployee(employees, employeeId);
			if (selectedEmployee != null) {
				setEmployeeDetail(conn, runId, selectedEmployee, startDate, endDate);
			}

			int count = dayWorkerDao.countAvailableEmployees(conn, runId, keyword, departmentId);
			int totalPages = Math.max(1, (count + EMPLOYEE_PAGE_SIZE - 1) / EMPLOYEE_PAGE_SIZE);
			int currentPage = Math.min(Math.max(employeePage, 1), totalPages);
			DayWorkerPaymentPage page = new DayWorkerPaymentPage();
			page.setRun(run);
			page.setPaymentEmployees(employees);
			page.setSelectedEmployee(selectedEmployee);
			page.setAvailableEmployeePage(new DayWorkerEmployeePage(dayWorkerDao.selectAvailableEmployees(conn,
					runId, keyword, departmentId, currentPage, EMPLOYEE_PAGE_SIZE), totalPages));
			page.setDepartments(departmentDao.selectAll(conn));
			return page;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	public void save(PayrollRun requestRun, int employeeId, long[] amounts) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			PayrollRun run = getOrCreateRun(conn, requestRun);
			Integer payrollEmployeeId = managementDao.selectPayrollEmployeeId(conn, run.getPayrollRunId(), employeeId);
			if (payrollEmployeeId == null) {
				managementDao.insertPayrollEmployee(conn, run.getPayrollRunId(), employeeId);
				payrollEmployeeId = managementDao.selectPayrollEmployeeId(conn, run.getPayrollRunId(), employeeId);
			}
			List<PayrollManagementItem> deductionItems = dayWorkerDao.selectDeductionEntries(conn,
					payrollEmployeeId);
			setDeductionAmounts(deductionItems, amounts);
			managementDao.replaceEntries(conn, payrollEmployeeId, new ArrayList<>(), deductionItems);
			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	public long[] calculate(PayrollRun requestRun, int employeeId, long mutualAidFee) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			long[] calculated = dayWorkerDao.selectAutomaticDeductions(conn, employeeId,
					requestRun.getCalcStartDate(), requestRun.getCalcEndDate());
			return new long[] { calculated[0], calculated[1], calculated[2], calculated[3], calculated[4],
					calculated[5], mutualAidFee };
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	public boolean hasWorkPayments(PayrollRun requestRun, int employeeId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			return !dayWorkerDao.selectWorkPayments(conn, employeeId, requestRun.getCalcStartDate(),
					requestRun.getCalcEndDate()).isEmpty();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	private PayrollRun getOrCreateRun(Connection conn, PayrollRun requestRun) throws SQLException {
		PayrollRun run = managementDao.selectRun(conn, requestRun.getPayYear(), requestRun.getPayMonth(),
				requestRun.getPaySeq(), "2");
		if (run == null) {
			requestRun.setPayrollRunId(managementDao.insertRun(conn, requestRun));
			return requestRun;
		}
		managementDao.updateRunDates(conn, run.getPayrollRunId(), requestRun);
		return run;
	}

	private void setEmployeeDetail(Connection conn, int runId, DayWorkerPaymentEmployee employee, Date startDate,
			Date endDate) throws SQLException {
		employee.setWorkPayments(dayWorkerDao.selectWorkPayments(conn, employee.getEmployeeId(), startDate, endDate));
		Integer payrollEmployeeId = managementDao.selectPayrollEmployeeId(conn, runId, employee.getEmployeeId());
		if (payrollEmployeeId != null) {
			setDeductionFields(employee, dayWorkerDao.selectDeductionEntries(conn, payrollEmployeeId));
		}
	}

	private void setDeductionFields(DayWorkerPaymentEmployee employee, List<PayrollManagementItem> items) {
		for (PayrollManagementItem item : items) {
			String name = item.getItemName();
			if ("국민연금".equals(name)) {
				employee.setNationalPension(item.getAmount());
			} else if ("건강보험".equals(name)) {
				employee.setHealthInsurance(item.getAmount());
			} else if ("장기요양보험".equals(name)) {
				employee.setLongTermCareInsurance(item.getAmount());
			} else if ("고용보험".equals(name)) {
				employee.setEmploymentInsurance(item.getAmount());
			} else if ("소득세".equals(name)) {
				employee.setIncomeTax(item.getAmount());
			} else if ("지방소득세".equals(name)) {
				employee.setLocalIncomeTax(item.getAmount());
			} else if ("상조회비".equals(name)) {
				employee.setMutualAidFee(item.getAmount());
			}
		}
	}

	private void setDeductionAmounts(List<PayrollManagementItem> items, long[] amounts) {
		for (PayrollManagementItem item : items) {
			String name = item.getItemName();
			if ("국민연금".equals(name)) {
				item.setAmount(amounts[0]);
			} else if ("건강보험".equals(name)) {
				item.setAmount(amounts[1]);
			} else if ("장기요양보험".equals(name)) {
				item.setAmount(amounts[2]);
			} else if ("고용보험".equals(name)) {
				item.setAmount(amounts[3]);
			} else if ("소득세".equals(name)) {
				item.setAmount(amounts[4]);
			} else if ("지방소득세".equals(name)) {
				item.setAmount(amounts[5]);
			} else if ("상조회비".equals(name)) {
				item.setAmount(amounts[6]);
			}
		}
	}

	private DayWorkerPaymentEmployee selectEmployee(List<DayWorkerPaymentEmployee> employees, Integer employeeId) {
		// 최초 진입 시 첫 일용직 사원을 자동 선택하지 않는다.
		if (employees.isEmpty() || employeeId == null) {
			return null;
		}
		for (DayWorkerPaymentEmployee employee : employees) {
			if (employee.getEmployeeId() == employeeId) {
				return employee;
			}
		}
		return null;
	}
}
