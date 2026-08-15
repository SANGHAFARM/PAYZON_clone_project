package erp.payroll.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import erp.payroll.dao.PayrollPayslipDao;
import erp.payroll.dto.PayrollPayslipPage.PayrollPayslipCompany;
import erp.payroll.dto.PayrollPayslipPage.PayrollPayslipEmployee;
import erp.payroll.dto.PayrollPayslipPage;
import erp.payroll.dto.PayrollRegisterColumn;
import erp.payroll.model.PayrollRun;
import erp.settings.dao.CompanyDao;
import erp.settings.model.Company;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

// 급여 회차와 사원별 급여명세서를 구성하는 서비스
public class PayrollPayslipService {

	private PayrollPayslipDao payslipDao = new PayrollPayslipDao();
	private CompanyDao companyDao = CompanyDao.getInstance();

	public PayrollPayslipPage getPage(String year, String month, String sequence, Integer employeeId,
			String keyword) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			List<PayrollRun> runs = payslipDao.selectRuns(conn, year, month, sequence);
			List<PayrollPayslipEmployee> employees = payslipDao.selectEmployees(conn, year, month, sequence, keyword);
			payslipDao.fillAmounts(conn, year, month, sequence, employees);
			payslipDao.fillDailyPayments(conn, year, month, sequence, employees);
			PayrollPayslipEmployee selectedEmployee = selectEmployee(employees, employeeId);
			List<PayrollRegisterColumn> paymentItems = new ArrayList<>();
			List<PayrollRegisterColumn> deductionItems = new ArrayList<>();
			if (selectedEmployee != null) {
				paymentItems = payslipDao.selectPaymentColumns(conn, year, month, sequence,
						selectedEmployee.getEmployeeId());
				if (selectedEmployee.getPaymentAmounts().containsKey(-1)) {
					paymentItems.add(0, new PayrollRegisterColumn(-1, "일용직 급여"));
				}
				deductionItems = payslipDao.selectDeductionColumns(conn, year, month, sequence,
						selectedEmployee.getEmployeeId());
				fillEmptyAmounts(selectedEmployee, paymentItems, deductionItems);
			}

			PayrollPayslipPage page = new PayrollPayslipPage();
			if (!runs.isEmpty()) {
				PayrollRun run = runs.get(0);
				page.setCalculationStart(run.getCalcStartDate());
				page.setCalculationEnd(run.getCalcEndDate());
				page.setPaymentDate(run.getPayDate());
			}
			page.setEmployees(employees);
			page.setSelectedEmployee(selectedEmployee);
			page.setPaymentItems(paymentItems);
			page.setDeductionItems(deductionItems);
			page.setCompany(makeCompany(companyDao.selectById(conn, 1)));
			return page;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	private PayrollPayslipEmployee selectEmployee(List<PayrollPayslipEmployee> employees, Integer employeeId) {
		// 최초 진입 시 첫 사원의 급여명세서를 자동 표시하지 않는다.
		if (employees.isEmpty() || employeeId == null) {
			return null;
		}
		for (PayrollPayslipEmployee employee : employees) {
			if (employee.getEmployeeId() == employeeId) {
				return employee;
			}
		}
		return null;
	}

	private void fillEmptyAmounts(PayrollPayslipEmployee employee, List<PayrollRegisterColumn> paymentItems,
			List<PayrollRegisterColumn> deductionItems) {
		for (PayrollRegisterColumn item : paymentItems) {
			employee.getPaymentAmounts().putIfAbsent(item.getItemId(), 0L);
			employee.getPaymentCalculations().putIfAbsent(item.getItemId(), "-");
		}
		for (PayrollRegisterColumn item : deductionItems) {
			employee.getDeductionAmounts().putIfAbsent(item.getItemId(), 0L);
			employee.getDeductionCalculations().putIfAbsent(item.getItemId(), "-");
		}
	}

	private PayrollPayslipCompany makeCompany(Company company) {
		PayrollPayslipCompany result = new PayrollPayslipCompany();
		if (company != null) {
			result.setCompanyName(company.getCmpnName());
			result.setRepresentativeName(company.getCeoName());
			result.setLogoUrl(company.getLogoImgPath());
			result.setStampUrl(company.getStampImgPath());
		}
		return result;
	}
}
