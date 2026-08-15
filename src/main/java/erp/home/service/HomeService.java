package erp.home.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

import erp.employees.dao.EmployeeDao;
import erp.home.dto.HomeDashboard;
import erp.payroll.dao.PayrollRegisterDao;
import erp.settings.dao.CompanyDao;
import jdbc.connection.ConnectionProvider;

// 홈 화면에 표시할 조회 데이터를 구성한다.
public class HomeService {

	private final EmployeeDao employeeDao = EmployeeDao.getInstance();
	private final CompanyDao companyDao = CompanyDao.getInstance();
	private final PayrollRegisterDao payrollRegisterDao = new PayrollRegisterDao();

	public HomeDashboard getDashboard() {
		try (Connection conn = ConnectionProvider.getConnection()) {
			HomeDashboard dashboard = new HomeDashboard();
			dashboard.setCompany(companyDao.selectById(conn, 1));
			dashboard.setEmployeeSummary(employeeDao.selectSummary(conn));
			dashboard.setRecentPayrolls(payrollRegisterDao.selectRuns(conn,
					String.valueOf(LocalDate.now().getYear()), 0, 5));
			return dashboard;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
