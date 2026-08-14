package erp.payroll.service;

import java.sql.Connection;
import java.sql.SQLException;

import erp.payroll.dao.FourInsuranceDao;
import erp.payroll.dto.FourInsuranceDeduction;
import erp.payroll.dto.FourInsurancePage;
import erp.payroll.dto.FourInsuranceTotals;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

// 4대보험 공제내역과 전체 합계를 제공하는 서비스
public class FourInsuranceService {

	private FourInsuranceDao insuranceDao = new FourInsuranceDao();

	public FourInsurancePage getPage(String year, String month, String sequence) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			FourInsurancePage page = insuranceDao.selectPage(conn, year, month, sequence);
			page.setTotals(calculateTotals(page));
			return page;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	private FourInsuranceTotals calculateTotals(FourInsurancePage page) {
		FourInsuranceTotals totals = new FourInsuranceTotals();
		for (FourInsuranceDeduction deduction : page.getDeductions()) {
			totals.setPensionEmployer(totals.getPensionEmployer() + deduction.getPensionEmployer());
			totals.setPensionEmployee(totals.getPensionEmployee() + deduction.getPensionEmployee());
			totals.setHealthEmployer(totals.getHealthEmployer() + deduction.getHealthEmployer());
			totals.setHealthEmployee(totals.getHealthEmployee() + deduction.getHealthEmployee());
			totals.setCareEmployer(totals.getCareEmployer() + deduction.getCareEmployer());
			totals.setCareEmployee(totals.getCareEmployee() + deduction.getCareEmployee());
			totals.setEmploymentEmployer(totals.getEmploymentEmployer() + deduction.getEmploymentEmployer());
			totals.setEmploymentEmployee(totals.getEmploymentEmployee() + deduction.getEmploymentEmployee());
		}
		return totals;
	}
}
