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
// 홈 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// ホームの業務ルールとデータ変更トランザクションを処理する。
public class HomeService {

	private final EmployeeDao employeeDao = EmployeeDao.getInstance();
	private final CompanyDao companyDao = CompanyDao.getInstance();
	private final PayrollRegisterDao payrollRegisterDao = new PayrollRegisterDao();

	// 요청 조건에 맞는 홈 대시보드를 구성하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// リクエスト条件に合うホームのダッシュボードを構成して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
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
