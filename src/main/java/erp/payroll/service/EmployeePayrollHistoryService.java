package erp.payroll.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import erp.payroll.dao.EmployeePayrollHistoryDao;
import erp.payroll.dto.EmployeePayrollHistoryPage.EmployeePayrollHistoryEmployee;
import erp.payroll.dto.EmployeePayrollHistoryPage.EmployeePayrollHistoryItem;
import erp.payroll.dto.EmployeePayrollHistoryPage;
import erp.payroll.dto.EmployeePayrollHistoryPage.EmployeePayrollHistoryPageInfo;
import erp.payroll.dto.EmployeePayrollHistoryPage.EmployeePayrollHistoryTotal;
import erp.settings.dao.DepartmentDao;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

// 사원별 급여내역 화면에 필요한 조회 결과를 구성하는 서비스
public class EmployeePayrollHistoryService {

	private static final int PAGE_SIZE = 10;

	private EmployeePayrollHistoryDao historyDao = new EmployeePayrollHistoryDao();
	private DepartmentDao departmentDao = DepartmentDao.getInstance();

	public EmployeePayrollHistoryPage getPage(Integer employeeId, String startMonth, String endMonth,
			int pageNumber, String keyword, Integer departmentId, String status, boolean loadHistories) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			List<EmployeePayrollHistoryEmployee> employees = historyDao.selectEmployees(conn, keyword,
					departmentId, status);
			EmployeePayrollHistoryEmployee selectedEmployee = findSelectedEmployee(conn, employeeId, employees);
			List<EmployeePayrollHistoryItem> allHistories = new ArrayList<>();
			if (loadHistories && selectedEmployee != null) {
				allHistories = combineHistories(historyDao.selectHistories(conn,
						selectedEmployee.getEmployeeId(), startMonth, endMonth));
			}

			int totalPages = allHistories.isEmpty() ? 0 : (allHistories.size() + PAGE_SIZE - 1) / PAGE_SIZE;
			int currentPage = totalPages == 0 ? 1 : Math.min(Math.max(pageNumber, 1), totalPages);
			int fromIndex = totalPages == 0 ? 0 : (currentPage - 1) * PAGE_SIZE;
			int toIndex = Math.min(fromIndex + PAGE_SIZE, allHistories.size());

			EmployeePayrollHistoryPage page = new EmployeePayrollHistoryPage();
			page.setSelectedEmployee(selectedEmployee);
			page.setEmployees(employees);
			page.setDepartments(departmentDao.selectAll(conn));
			page.setHistories(new ArrayList<>(allHistories.subList(fromIndex, toIndex)));
			page.setTotal(calculateTotal(allHistories));
			page.setPageInfo(new EmployeePayrollHistoryPageInfo(currentPage, totalPages));
			return page;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	private EmployeePayrollHistoryEmployee findSelectedEmployee(Connection conn, Integer employeeId,
			List<EmployeePayrollHistoryEmployee> employees) throws SQLException {
		if (employeeId != null) {
			return historyDao.selectEmployee(conn, employeeId);
		}
		return null;
	}

	// 같은 지급월과 차수의 소득 유형별 결과를 한 줄로 합친다.
	private List<EmployeePayrollHistoryItem> combineHistories(List<EmployeePayrollHistoryItem> source) {
		Map<String, EmployeePayrollHistoryItem> combined = new LinkedHashMap<>();
		for (EmployeePayrollHistoryItem item : source) {
			String key = item.getPaymentMonth() + "-" + item.getPaymentRound();
			EmployeePayrollHistoryItem target = combined.get(key);
			if (target == null) {
				target = new EmployeePayrollHistoryItem();
				target.setPaymentMonth(item.getPaymentMonth());
				target.setPaymentRound(item.getPaymentRound());
				combined.put(key, target);
			}
			add(target, item);
		}
		return new ArrayList<>(combined.values());
	}

	private EmployeePayrollHistoryTotal calculateTotal(List<EmployeePayrollHistoryItem> histories) {
		EmployeePayrollHistoryTotal total = new EmployeePayrollHistoryTotal();
		for (EmployeePayrollHistoryItem history : histories) {
			total.setStandardMonthlyIncome(total.getStandardMonthlyIncome() + history.getStandardMonthlyIncome());
			total.setTotalPayment(total.getTotalPayment() + history.getTotalPayment());
			total.setTotalDeduction(total.getTotalDeduction() + history.getTotalDeduction());
			total.setNationalPension(total.getNationalPension() + history.getNationalPension());
			total.setHealthInsurance(total.getHealthInsurance() + history.getHealthInsurance());
			total.setLongTermCareInsurance(total.getLongTermCareInsurance() + history.getLongTermCareInsurance());
			total.setEmploymentInsurance(total.getEmploymentInsurance() + history.getEmploymentInsurance());
			total.setIncomeTax(total.getIncomeTax() + history.getIncomeTax());
			total.setLocalIncomeTax(total.getLocalIncomeTax() + history.getLocalIncomeTax());
		}
		return total;
	}

	private void add(EmployeePayrollHistoryItem target, EmployeePayrollHistoryItem value) {
		target.setStandardMonthlyIncome(Math.max(target.getStandardMonthlyIncome(), value.getStandardMonthlyIncome()));
		target.setTotalPayment(target.getTotalPayment() + value.getTotalPayment());
		target.setTotalDeduction(target.getTotalDeduction() + value.getTotalDeduction());
		target.setNationalPension(target.getNationalPension() + value.getNationalPension());
		target.setHealthInsurance(target.getHealthInsurance() + value.getHealthInsurance());
		target.setLongTermCareInsurance(target.getLongTermCareInsurance() + value.getLongTermCareInsurance());
		target.setEmploymentInsurance(target.getEmploymentInsurance() + value.getEmploymentInsurance());
		target.setIncomeTax(target.getIncomeTax() + value.getIncomeTax());
		target.setLocalIncomeTax(target.getLocalIncomeTax() + value.getLocalIncomeTax());
	}
}
