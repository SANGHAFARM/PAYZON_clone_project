package erp.retirement.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Date;
import java.util.List;
import java.util.Set;

import erp.employees.dao.EmployeeDao;
import erp.employees.dto.EmployeeListItem;
import erp.employees.dto.EmployeePageInfo;
import erp.employees.model.Employee;
import erp.employees.service.EmployeeSearchCondition;
import erp.retirement.dto.RetirementTypeItem;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

// 퇴직대상 사원 조회와 퇴직처리·취소 트랜잭션을 처리한다.
public class RetirementProcessService {
	private static final Set<String> RETIREMENT_TYPES = new HashSet<>(Arrays.asList(
			"자진퇴사", "권고사직", "계약만료", "정년퇴직", "해고", "기타"));

	private final EmployeeDao employeeDao = EmployeeDao.getInstance();

	public RetirementEmployeePage getEmployeePage(EmployeeSearchCondition condition) {
		try (Connection conn = ConnectionProvider.getConnection()) {
			int totalCount = employeeDao.countByCondition(conn, condition);
			EmployeePageInfo pageInfo = new EmployeePageInfo(totalCount, condition.getPage(), condition.getPageSize());
			condition.setPage(pageInfo.getCurrentPage());
			List<EmployeeListItem> employees = employeeDao.selectListByCondition(conn, condition);
			return new RetirementEmployeePage(employees, pageInfo);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void retire(int employeeId, String retirementType, Date retirementDate, String reason, String afterContact) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			Employee employee = employeeDao.selectById(conn, employeeId);
			if (employee == null) {
				throw new IllegalArgumentException("존재하지 않는 사원입니다.");
			}
			if ("퇴직".equals(employee.getStatus())) {
				throw new IllegalArgumentException("이미 퇴직 처리된 사원입니다.");
			}
			if (!RETIREMENT_TYPES.contains(retirementType)) {
				throw new IllegalArgumentException("올바른 퇴직구분을 선택하세요.");
			}
			if (retirementDate == null) {
				throw new IllegalArgumentException("퇴직일자를 입력하세요.");
			}
			if (employee.getJoinDate() != null && retirementDate.before(employee.getJoinDate())) {
				throw new IllegalArgumentException("퇴직일자는 입사일보다 빠를 수 없습니다.");
			}
			employeeDao.updateRetirement(conn, employeeId, retirementType, retirementDate, reason, afterContact);
			conn.commit();
		} catch (SQLException | RuntimeException e) {
			JdbcUtil.rollback(conn);
			throw e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	public void cancel(int employeeId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			if (employeeDao.cancelRetirement(conn, employeeId) == 0) {
				throw new IllegalArgumentException("퇴직처리된 사원이 아닙니다.");
			}
			conn.commit();
		} catch (SQLException | RuntimeException e) {
			JdbcUtil.rollback(conn);
			throw e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	public List<RetirementTypeItem> getRetirementTypes() {
		return Arrays.asList(new RetirementTypeItem("자진퇴사", "자진퇴사"),
				new RetirementTypeItem("권고사직", "권고사직"), new RetirementTypeItem("계약만료", "계약만료"),
				new RetirementTypeItem("정년퇴직", "정년퇴직"), new RetirementTypeItem("해고", "해고"),
				new RetirementTypeItem("기타", "기타"));
	}

	public static class RetirementEmployeePage {
		private final List<EmployeeListItem> employees;
		private final EmployeePageInfo pageInfo;

		public RetirementEmployeePage(List<EmployeeListItem> employees, EmployeePageInfo pageInfo) {
			this.employees = employees;
			this.pageInfo = pageInfo;
		}

		public List<EmployeeListItem> getEmployees() { return employees; }
		public EmployeePageInfo getPageInfo() { return pageInfo; }
	}
}
