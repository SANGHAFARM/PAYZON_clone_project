package erp.payroll.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import erp.payroll.dao.PayrollRegisterDao;
import erp.payroll.dto.PayrollRegisterColumn;
import erp.payroll.dto.PayrollRegisterPage.PayrollEmploymentTypeOption;
import erp.payroll.dto.PayrollRegisterPage.PayrollRegisterDetailPage;
import erp.payroll.dto.PayrollRegisterPage.PayrollRegisterEmployee;
import erp.payroll.dto.PayrollRegisterPage.PayrollRegisterItem;
import erp.payroll.dto.PayrollRegisterPage.PayrollRegisterListPage;
import erp.payroll.dto.PayrollRegisterPage.PayrollRegisterPageInfo;
import erp.payroll.dto.PayrollRegisterPage.PayrollRegisterTotals;
import erp.settings.dao.DepartmentDao;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

// 급여대장 목록과 사원별 상세 집계를 제공하는 서비스
public class PayrollRegisterService {

	private static final int PAGE_SIZE = 10;

	private PayrollRegisterDao registerDao = new PayrollRegisterDao();
	private DepartmentDao departmentDao = DepartmentDao.getInstance();

	public PayrollRegisterListPage getList(String year, int pageNumber) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			int count = registerDao.countRuns(conn, year);
			int totalPages = count == 0 ? 0 : (count + PAGE_SIZE - 1) / PAGE_SIZE;
			int currentPage = totalPages == 0 ? 0 : Math.min(Math.max(pageNumber, 0), totalPages - 1);
			List<PayrollRegisterItem> registers = registerDao.selectRuns(conn, year, currentPage * PAGE_SIZE,
					PAGE_SIZE);
			return new PayrollRegisterListPage(registers, calculateListTotals(registers),
					new PayrollRegisterPageInfo(currentPage, totalPages));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	public PayrollRegisterDetailPage getDetail(int runId, String employmentType, Integer departmentId,
			String incomeType) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			PayrollRegisterItem register = registerDao.selectRunById(conn, runId);
			if (register == null) {
				throw new IllegalArgumentException("조회할 급여대장이 없습니다.");
			}
			List<PayrollRegisterColumn> paymentItems;
			if ("2".equals(register.getIncomeType())) {
				paymentItems = new ArrayList<>();
				paymentItems.add(new PayrollRegisterColumn(-1, "일용직 급여"));
			} else {
				paymentItems = registerDao.selectPayColumns(conn, runId);
			}
			List<PayrollRegisterColumn> deductionItems = registerDao.selectDeductColumns(conn, runId);
			List<PayrollRegisterEmployee> employees = new ArrayList<>();
			if (matchesIncomeType(register.getIncomeType(), incomeType)) {
				employees = registerDao.selectEmployees(conn, runId, emptyToNull(employmentType), departmentId);
			}
			registerDao.fillEntryAmounts(conn, runId, employees);
			if ("2".equals(register.getIncomeType())) {
				registerDao.fillDailyPayments(conn, runId, employees);
			}
			fillEmptyAmounts(employees, paymentItems, deductionItems);

			PayrollRegisterDetailPage page = new PayrollRegisterDetailPage();
			page.setRegister(register);
			page.setPaymentItems(paymentItems);
			page.setDeductionItems(deductionItems);
			page.setEmployees(employees);
			page.setTotals(calculateDetailTotals(employees));
			page.setDepartments(departmentDao.selectAll(conn));
			return page;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	public void delete(int runId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			registerDao.deleteRun(conn, runId);
			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	public List<PayrollEmploymentTypeOption> getEmploymentTypes() {
		List<PayrollEmploymentTypeOption> types = new ArrayList<>();
		types.add(new PayrollEmploymentTypeOption("정규직", "정규직"));
		types.add(new PayrollEmploymentTypeOption("계약직", "계약직"));
		types.add(new PayrollEmploymentTypeOption("임시직", "임시직"));
		types.add(new PayrollEmploymentTypeOption("파견직", "파견직"));
		types.add(new PayrollEmploymentTypeOption("위촉직", "위촉직"));
		types.add(new PayrollEmploymentTypeOption("일용직", "일용직"));
		return types;
	}

	private PayrollRegisterTotals calculateListTotals(List<PayrollRegisterItem> registers) {
		PayrollRegisterTotals totals = new PayrollRegisterTotals();
		long payment = 0;
		long deduction = 0;
		for (PayrollRegisterItem register : registers) {
			payment += register.getTotalPayment();
			deduction += register.getTotalDeduction();
		}
		totals.setTotalPayment(payment);
		totals.setTotalDeduction(deduction);
		return totals;
	}

	private PayrollRegisterTotals calculateDetailTotals(List<PayrollRegisterEmployee> employees) {
		PayrollRegisterTotals totals = new PayrollRegisterTotals();
		long payment = 0;
		long deduction = 0;
		for (PayrollRegisterEmployee employee : employees) {
			addAmounts(totals.getPaymentAmounts(), employee.getPaymentAmounts());
			addAmounts(totals.getDeductionAmounts(), employee.getDeductionAmounts());
			payment += employee.getTotalPayment();
			deduction += employee.getTotalDeduction();
		}
		totals.setTotalPayment(payment);
		totals.setTotalDeduction(deduction);
		return totals;
	}

	private void addAmounts(Map<Integer, Long> totals, Map<Integer, Long> amounts) {
		for (Map.Entry<Integer, Long> entry : amounts.entrySet()) {
			long current = totals.containsKey(entry.getKey()) ? totals.get(entry.getKey()) : 0;
			totals.put(entry.getKey(), current + entry.getValue());
		}
	}

	private void fillEmptyAmounts(List<PayrollRegisterEmployee> employees,
			List<PayrollRegisterColumn> paymentItems, List<PayrollRegisterColumn> deductionItems) {
		for (PayrollRegisterEmployee employee : employees) {
			for (PayrollRegisterColumn item : paymentItems) {
				employee.getPaymentAmounts().putIfAbsent(item.getItemId(), 0L);
			}
			for (PayrollRegisterColumn item : deductionItems) {
				employee.getDeductionAmounts().putIfAbsent(item.getItemId(), 0L);
			}
		}
	}

	private boolean matchesIncomeType(String runIncomeType, String selectedIncomeType) {
		String selected = emptyToNull(selectedIncomeType);
		if (selected == null) {
			return true;
		} else if ("0".equals(runIncomeType)) {
			return "WORK".equals(selected);
		} else if ("1".equals(runIncomeType)) {
			return "BUSINESS".equals(selected);
		}
		return "DAILY".equals(selected);
	}

	private String emptyToNull(String value) {
		return value == null || value.trim().isEmpty() ? null : value.trim();
	}
}
