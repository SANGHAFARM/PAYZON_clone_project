package erp.employees.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import erp.employees.dao.CertificateIssuanceDao;
import erp.employees.dao.EmployeeDao;
import erp.employees.dto.EmployeeListItem;
import erp.employees.dto.EmployeePageInfo;
import erp.employees.dto.EmployeeSummary;
import jdbc.connection.ConnectionProvider;
import jdbc.JdbcUtil;
import erp.payroll.dao.PayrollEmployeeDao;
import erp.retirement.dao.RetirementCalculationDao;

// 사원 목록 조회와 상단 현황 집계를 함께 처리하는 Service
public class EmployeeListService {
	private final EmployeeDao employeeDao = EmployeeDao.getInstance();
	private final CertificateIssuanceDao certificateIssuanceDao = CertificateIssuanceDao.getInstance();
	private final PayrollEmployeeDao payrollEmployeeDao = PayrollEmployeeDao.getInstance();
	private final RetirementCalculationDao retirementCalculationDao = RetirementCalculationDao.getInstance();

	public EmployeeListResult getEmployeeList(EmployeeSearchCondition condition) {
		try (Connection conn = ConnectionProvider.getConnection()) {
			// 조건에 맞는 전체 행 수를 먼저 구해 현재 페이지 범위를 계산한다.
			int totalCount = employeeDao.countByCondition(conn, condition);
			EmployeePageInfo pageInfo = new EmployeePageInfo(totalCount, condition.getPage(), condition.getPageSize());
			condition.setPage(pageInfo.getCurrentPage());
			// 계산된 범위의 목록과 화면 상단에 표시할 전체 사원 현황을 조회한다.
			List<EmployeeListItem> employees = employeeDao.selectListByCondition(conn, condition);
			EmployeeSummary summary = employeeDao.selectSummary(conn);
			return new EmployeeListResult(employees, summary, pageInfo);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<String> getEmploymentTypes() {
		return Arrays.asList("정규직", "계약직", "임시직", "파견직", "위촉직", "일용직");
	}

	// 연결된 업무 자료와 선택한 사원을 하나의 트랜잭션으로 영구 삭제한다.
	public int deleteEmployees(List<Integer> employeeIds) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			int deletedCount = 0;
			for (Integer employeeId : employeeIds) {
				// EMPLOYEE를 참조하는 NO ACTION 외래키 자료부터 정리한다.
				certificateIssuanceDao.deleteByEmployeeId(conn, employeeId);
				payrollEmployeeDao.deleteByEmployeeId(conn, employeeId);
				retirementCalculationDao.deleteByEmployeeId(conn, employeeId);
				deletedCount += employeeDao.delete(conn, employeeId);
			}
			conn.commit();
			return deletedCount;
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException("사원정보를 삭제하지 못했습니다. 잠시 후 다시 시도해주세요.", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	public static class EmployeeListResult {
		// Handler에 여러 조회 결과를 한 번에 반환하기 위한 결과 객체
		private final List<EmployeeListItem> employees;
		private final EmployeeSummary summary;
		private final EmployeePageInfo pageInfo;

		public EmployeeListResult(List<EmployeeListItem> employees, EmployeeSummary summary, EmployeePageInfo pageInfo) {
			this.employees = employees;
			this.summary = summary;
			this.pageInfo = pageInfo;
		}
		public List<EmployeeListItem> getEmployees() { return employees; }
		public EmployeeSummary getSummary() { return summary; }
		public EmployeePageInfo getPageInfo() { return pageInfo; }
	}
}
