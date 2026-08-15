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
import erp.payroll.dto.PayrollManagementPage.PayrollEmployeePage;
import erp.payroll.dto.PayrollManagementPage.PayrollManagementEmployee;
import erp.payroll.dto.PayrollManagementItem;
import erp.payroll.dto.PayrollManagementPage;
import erp.payroll.dto.PayrollManagementPage.PayrollPositionOption;
import erp.payroll.dto.PayrollManagementPage.PayrollTotals;
import erp.payroll.model.PayrollRun;
import erp.settings.dao.DepartmentDao;
import erp.settings.dao.AttendanceItemDao;
import erp.settings.dao.JobPositionDao;
import erp.settings.dao.TaxFreeItemDao;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

// 급여 회차와 사원별 지급·공제 내역을 관리하는 서비스
public class PayrollManagementService {

	private static final int EMPLOYEE_PAGE_SIZE = 10;

	private PayrollManagementDao managementDao = new PayrollManagementDao();
	private DepartmentDao departmentDao = DepartmentDao.getInstance();
	private JobPositionDao positionDao = JobPositionDao.getInstance();
	private TaxFreeItemDao taxFreeItemDao = TaxFreeItemDao.getInstance();
	private AttendanceItemDao attendanceItemDao = AttendanceItemDao.getInstance();

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
			// 기본환경설정에 등록된 비과세/감면 코드를 지급항목 팝업에 제공한다.
			page.setTaxFreeItems(taxFreeItemDao.selectAll(conn));
			page.setAttendanceItems(attendanceItemDao.selectAll(conn));

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
			long taxFreeLimit, String calculationMethod, int roundUnit, String payMethod, Integer attendanceItemId,
			Long bulkAmount) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			String calculation = defaultPayCalculation(calculationMethod, payMethod);
			Long roundedBulkAmount = bulkAmount == null ? null : roundDown(bulkAmount, roundUnit);
			managementDao.managePayItem(conn, action, itemId, itemName, taxType, taxFreeCode, taxFreeLimit,
					calculation, roundUnit, payMethod, attendanceItemId, roundedBulkAmount);
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
			managementDao.manageDeductItem(conn, action, itemId, itemName,
					defaultDeductionCalculation(itemName, calculationMethod), roundUnit, note);
			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 산식을 입력하지 않으면 지급방식에 맞는 기본 계산 설명을 저장한다.
	private String defaultPayCalculation(String calculationMethod, String payMethod) {
		if (calculationMethod != null && !calculationMethod.trim().isEmpty()) return calculationMethod.trim();
		if ("일괄지급".equals(payMethod)) return "일괄지급액";
		if ("근태연계".equals(payMethod)) return "근태수량 × 지급단가";
		return "직접입력";
	}

	// 대표적인 법정 공제는 실무에서 사용하는 기본 산식을 안내값으로 저장한다.
	private String defaultDeductionCalculation(String itemName, String calculationMethod) {
		if (calculationMethod != null && !calculationMethod.trim().isEmpty()) return calculationMethod.trim();
		String name = itemName == null ? "" : itemName;
		if (name.contains("국민연금")) return "기준소득월액 × 4.5%";
		if (name.contains("건강보험")) return "보수월액 × 3.545%";
		if (name.contains("장기요양")) return "건강보험료 × 장기요양보험요율";
		if (name.contains("고용보험")) return "보수월액 × 0.9%";
		if (name.contains("지방소득세")) return "소득세 × 10%";
		if (name.contains("소득세")) return "근로소득 간이세액표";
		return "직접입력";
	}

	// 절사단위 미만 금액은 버림 처리하여 실제 지급 단위와 맞춘다.
	private long roundDown(long amount, int roundUnit) {
		return roundUnit > 1 ? amount / roundUnit * roundUnit : amount;
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
			copyItemNames(deductItems, managementDao.selectDeductItems(conn, payrollEmployeeId));
			applyDefaultDeductions(payItems, deductItems);
			managementDao.replaceEntries(conn, payrollEmployeeId, payItems, deductItems);
			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	private void copyItemNames(List<PayrollManagementItem> requested, List<PayrollManagementItem> configured) {
		for (PayrollManagementItem item : requested) {
			for (PayrollManagementItem source : configured) {
				if (item.getItemCode() == source.getItemCode()) {
					item.setItemName(source.getItemName());
					break;
				}
			}
		}
	}

	// 사용자가 0원으로 둔 법정 공제만 간이 산식으로 계산한다. 직접 입력한 금액은 그대로 보존한다.
	private void applyDefaultDeductions(List<PayrollManagementItem> payItems,
			List<PayrollManagementItem> deductItems) {
		long grossPay = 0;
		for (PayrollManagementItem item : payItems) grossPay += item.getAmount();
		long healthInsurance = Math.round(grossPay * 0.03545);
		long incomeTax = Math.round(Math.max(0, grossPay - 1500000) * 0.06);
		for (PayrollManagementItem item : deductItems) {
			String name = item.getItemName() == null ? "" : item.getItemName();
			if (name.contains("건강보험") && item.getAmount() != 0) healthInsurance = item.getAmount();
			if (name.equals("소득세") && item.getAmount() != 0) incomeTax = item.getAmount();
		}
		for (PayrollManagementItem item : deductItems) {
			if (item.getAmount() != 0) continue;
			String name = item.getItemName() == null ? "" : item.getItemName();
			long amount = 0;
			if (name.contains("국민연금")) amount = Math.round(grossPay * 0.045);
			else if (name.contains("건강보험")) amount = Math.round(grossPay * 0.03545);
			else if (name.contains("장기요양")) amount = Math.round(healthInsurance * 0.1295);
			else if (name.contains("고용보험")) amount = Math.round(grossPay * 0.009);
			else if (name.equals("소득세")) amount = incomeTax;
			else if (name.contains("지방소득세")) amount = Math.round(incomeTax * 0.1);
			item.setAmount(roundDown(amount, 10));
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
