package erp.payroll.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import erp.payroll.dao.PayrollManagementDao;
import erp.payroll.dto.PayrollEmployeePage;
import erp.payroll.dto.PayrollManagementEmployee;
import erp.payroll.dto.PayrollManagementItem;
import erp.payroll.dto.PayrollManagementPage;
import erp.payroll.dto.PayrollPositionOption;
import erp.payroll.dto.PayrollTotals;
import erp.payroll.model.PayrollRun;
import erp.settings.dao.DepartmentDao;
import erp.settings.dao.JobPositionDao;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

// 급여 회차와 사원별 지급·공제 내역을 관리하는 서비스
public class PayrollManagementService {

	private static final int EMPLOYEE_PAGE_SIZE = 10;

	private PayrollManagementDao managementDao = new PayrollManagementDao();
	private DepartmentDao departmentDao = DepartmentDao.getInstance();
	private JobPositionDao positionDao = JobPositionDao.getInstance();

	public PayrollManagementPage getPage(String year, String month, String sequence, String incomeType,
			Integer employeeId, String keyword, int employeePage) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			PayrollRun run = managementDao.selectRun(conn, year, month, sequence, incomeType);
			List<PayrollManagementEmployee> employees = new ArrayList<>();
			Integer payrollEmployeeId = null;
			PayrollManagementEmployee selectedEmployee = null;

			if (run != null) {
				employees = managementDao.selectPayrollEmployees(conn, run.getPayrollRunId());
				selectedEmployee = selectEmployee(employees, employeeId);
				if (selectedEmployee != null) {
					payrollEmployeeId = managementDao.selectPayrollEmployeeId(conn, run.getPayrollRunId(),
							selectedEmployee.getEmployeeId());
				}
			}

			List<PayrollManagementItem> payItems = managementDao.selectPayItems(conn, payrollEmployeeId);
			List<PayrollManagementItem> deductItems = managementDao.selectDeductItems(conn, payrollEmployeeId);
			PayrollManagementPage page = new PayrollManagementPage();
			page.setRun(run);
			page.setPaymentEmployees(employees);
			page.setSelectedEmployee(selectedEmployee);
			page.setPaymentGiveItems(payItems);
			page.setPaymentDeductionItems(deductItems);
			page.setPaymentTotals(calculateTotals(payItems, deductItems));
			page.setDepartments(departmentDao.selectAll(conn));
			List<PayrollPositionOption> positions = new ArrayList<>();
			positionDao.selectAll(conn).forEach(position -> positions.add(new PayrollPositionOption(
					position.getJobPositionId(), position.getJobPositionName())));
			page.setPositions(positions);
			page.setPreviousPaymentPeriods(managementDao.selectPreviousRuns(conn, incomeType));

			if (run == null) {
				page.setAvailableEmployeePage(new PayrollEmployeePage(new ArrayList<>(), 1));
			} else {
				int count = managementDao.countAvailableEmployees(conn, run.getPayrollRunId(), keyword);
				int totalPages = Math.max(1, (count + EMPLOYEE_PAGE_SIZE - 1) / EMPLOYEE_PAGE_SIZE);
				int currentPage = Math.min(Math.max(employeePage, 1), totalPages);
				page.setAvailableEmployeePage(new PayrollEmployeePage(
						managementDao.selectAvailableEmployees(conn, run.getPayrollRunId(), keyword, currentPage,
								EMPLOYEE_PAGE_SIZE),
						totalPages));
			}
			return page;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	public void loadPrevious(PayrollRun requestRun, int previousRunId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			PayrollRun run = getOrCreateRun(conn, requestRun);
			managementDao.copyPreviousRun(conn, previousRunId, run.getPayrollRunId());
			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	public void managePayItem(String action, Integer itemId, String itemName, String taxType, String taxFreeCode,
			long taxFreeLimit, String calculationMethod, int roundUnit, String payMethod, Long bulkAmount) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			managementDao.managePayItem(conn, action, itemId, itemName, taxType, taxFreeCode, taxFreeLimit,
					calculationMethod, roundUnit, payMethod, bulkAmount);
			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	public void manageDeductItem(String action, Integer itemId, String itemName, String calculationMethod,
			int roundUnit, String note) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			managementDao.manageDeductItem(conn, action, itemId, itemName, calculationMethod, roundUnit, note);
			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	public void addEmployees(PayrollRun requestRun, int[] employeeIds) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			PayrollRun run = getOrCreateRun(conn, requestRun);
			for (int employeeId : employeeIds) {
				if (managementDao.selectPayrollEmployeeId(conn, run.getPayrollRunId(), employeeId) == null) {
					managementDao.insertPayrollEmployee(conn, run.getPayrollRunId(), employeeId);
				}
			}
			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	public void deleteEmployees(PayrollRun requestRun, int[] employeeIds, boolean deleteAll) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			PayrollRun run = managementDao.selectRun(conn, requestRun.getPayYear(), requestRun.getPayMonth(),
					requestRun.getPaySeq(), requestRun.getIncomeType());
			if (run != null) {
				managementDao.deletePayrollEmployees(conn, run.getPayrollRunId(), employeeIds, deleteAll);
			}
			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	public void save(PayrollRun requestRun, int employeeId, List<PayrollManagementItem> payItems,
			List<PayrollManagementItem> deductItems) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			PayrollRun run = getOrCreateRun(conn, requestRun);
			managementDao.updateRunDates(conn, run.getPayrollRunId(), requestRun);
			Integer payrollEmployeeId = managementDao.selectPayrollEmployeeId(conn, run.getPayrollRunId(), employeeId);
			if (payrollEmployeeId == null) {
				managementDao.insertPayrollEmployee(conn, run.getPayrollRunId(), employeeId);
				payrollEmployeeId = managementDao.selectPayrollEmployeeId(conn, run.getPayrollRunId(), employeeId);
			}
			managementDao.replaceEntries(conn, payrollEmployeeId, payItems, deductItems);
			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	private PayrollRun getOrCreateRun(Connection conn, PayrollRun requestRun) throws SQLException {
		PayrollRun run = managementDao.selectRun(conn, requestRun.getPayYear(), requestRun.getPayMonth(),
				requestRun.getPaySeq(), requestRun.getIncomeType());
		if (run == null) {
			requestRun.setPayrollRunId(managementDao.insertRun(conn, requestRun));
			return requestRun;
		}
		return run;
	}

	private PayrollManagementEmployee selectEmployee(List<PayrollManagementEmployee> employees, Integer employeeId) {
		if (employees.isEmpty()) {
			return null;
		}
		if (employeeId != null) {
			for (PayrollManagementEmployee employee : employees) {
				if (employee.getEmployeeId() == employeeId) {
					return employee;
				}
			}
		}
		return employees.get(0);
	}

	private PayrollTotals calculateTotals(List<PayrollManagementItem> payItems,
			List<PayrollManagementItem> deductItems) {
		PayrollTotals totals = new PayrollTotals();
		long payTotal = 0;
		long deductTotal = 0;
		for (PayrollManagementItem item : payItems) {
			payTotal += item.getAmount();
		}
		for (PayrollManagementItem item : deductItems) {
			deductTotal += item.getAmount();
		}
		totals.setGrossPayment(payTotal);
		totals.setTotalDeduction(deductTotal);
		return totals;
	}

	public PayrollRun makeRun(String year, String month, String sequence, String incomeType, String startDate,
			String endDate, String payDate) {
		int numericYear = Integer.parseInt(year);
		int numericMonth = Integer.parseInt(month);
		YearMonth yearMonth = YearMonth.of(numericYear, numericMonth);
		PayrollRun run = new PayrollRun();
		run.setPayYear(String.format("%04d", numericYear));
		run.setPayMonth(String.format("%02d", numericMonth));
		run.setPaySeq(String.format("%02d", Integer.parseInt(sequence)));
		if ("daily".equals(incomeType)) {
			run.setIncomeType("2");
		} else if ("business".equals(incomeType)) {
			run.setIncomeType("1");
		} else {
			run.setIncomeType("0");
		}
		run.setCalcStartDate(toDate(startDate, yearMonth.atDay(1)));
		run.setCalcEndDate(toDate(endDate, yearMonth.atEndOfMonth()));
		run.setPayDate(toDate(payDate, yearMonth.atEndOfMonth()));
		return run;
	}

	private Date toDate(String value, LocalDate defaultDate) {
		LocalDate date = value == null || value.trim().isEmpty() ? defaultDate : LocalDate.parse(value);
		return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
}
