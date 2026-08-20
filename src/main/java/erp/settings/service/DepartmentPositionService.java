package erp.settings.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import erp.settings.dao.DepartmentDao;
import erp.settings.dao.JobPositionDao;
import erp.settings.model.Department;
import erp.settings.model.JobPosition;
import jdbc.connection.ConnectionProvider;
import jdbc.JdbcUtil;

public class DepartmentPositionService {

	// 싱글톤 인스턴스 생성
	private static DepartmentPositionService departmentPositionService = new DepartmentPositionService();

	// 싱글톤 접근 메서드
	public static DepartmentPositionService getInstance() {
		return departmentPositionService;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	private DepartmentPositionService() {
	}

	private DepartmentDao departmentDao = DepartmentDao.getInstance();
	private JobPositionDao jobPositionDao = JobPositionDao.getInstance();

	/**
	 * [목록 조회] 부서 설정 팝업 및 Select Box 바인딩용
	 */
	public List<Department> getDepartmentOptions() {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			return departmentDao.selectAll(conn);
		} catch (SQLException e) {
			throw new RuntimeException("부서 목록 조회 중 에러", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	/**
	 * [목록 조회] 직위 설정 팝업 및 Select Box 바인딩용
	 */
	public List<JobPosition> getJobPositionOptions() {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			return jobPositionDao.selectAll(conn);
		} catch (SQLException e) {
			throw new RuntimeException("직위 목록 조회 중 에러", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	/**
	 * [추가] 새로운 부서 등록
	 */
	public void addDepartment(Department dept) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			validateDepartmentName(conn, dept.getDepartmentName(), 0);
			departmentDao.insert(conn, dept);
			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException("부서를 등록하지 못했습니다.", e);
		} catch (RuntimeException e) {
			JdbcUtil.rollback(conn);
			throw e;
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 사원등록에서 전달된 부서 ID가 실제 설정 테이블에 존재하는지 확인합니다.
	public int requireDepartmentId(String value) {
		Connection conn = null;
		try {
			int departmentId = Integer.parseInt(value);
			conn = ConnectionProvider.getConnection();
			if (departmentDao.selectById(conn, departmentId) == null) {
				throw new IllegalArgumentException("선택한 부서가 존재하지 않습니다.");
			}
			return departmentId;
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("부서를 다시 선택해주세요.");
		} catch (SQLException e) {
			throw new RuntimeException("부서 확인 중 에러", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 사원등록에서 전달된 직위 ID가 실제 설정 테이블에 존재하는지 확인합니다.
	public int requireJobPositionId(String value) {
		Connection conn = null;
		try {
			int positionId = Integer.parseInt(value);
			conn = ConnectionProvider.getConnection();
			if (jobPositionDao.selectById(conn, positionId) == null) {
				throw new IllegalArgumentException("선택한 직위가 존재하지 않습니다.");
			}
			return positionId;
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("직위를 다시 선택해주세요.");
		} catch (SQLException e) {
			throw new RuntimeException("직위 확인 중 에러", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 등록된 부서명을 수정한다.
	public void updateDepartment(Department dept) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			validateDepartmentName(conn, dept.getDepartmentName(), dept.getDepartmentId());
			if (departmentDao.update(conn, dept) == 0) {
				throw new IllegalArgumentException("수정할 부서가 없습니다.");
			}
			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException("부서를 수정하지 못했습니다.", e);
		} catch (RuntimeException e) {
			JdbcUtil.rollback(conn);
			throw e;
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 다른 데이터에서 사용하지 않는 부서를 삭제한다.
	public void deleteDepartment(int departmentId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			if (departmentDao.delete(conn, departmentId) == 0) {
				throw new IllegalArgumentException("삭제할 부서가 없습니다.");
			}
			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException("사용 중인 부서는 삭제할 수 없습니다.", e);
		} catch (RuntimeException e) {
			JdbcUtil.rollback(conn);
			throw e;
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 새로운 직위를 등록한다.
	public void addJobPosition(JobPosition position) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			validatePositionName(conn, position.getJobPositionName(), 0);
			jobPositionDao.insert(conn, position);
			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException("직위를 등록하지 못했습니다.", e);
		} catch (RuntimeException e) {
			JdbcUtil.rollback(conn);
			throw e;
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 등록된 직위명을 수정한다.
	public void updateJobPosition(JobPosition position) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			validatePositionName(conn, position.getJobPositionName(), position.getJobPositionId());
			if (jobPositionDao.update(conn, position) == 0) {
				throw new IllegalArgumentException("수정할 직위가 없습니다.");
			}
			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException("직위를 수정하지 못했습니다.", e);
		} catch (RuntimeException e) {
			JdbcUtil.rollback(conn);
			throw e;
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 다른 데이터에서 사용하지 않는 직위를 삭제한다.
	public void deleteJobPosition(int positionId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			if (jobPositionDao.delete(conn, positionId) == 0) {
				throw new IllegalArgumentException("삭제할 직위가 없습니다.");
			}
			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException("사용 중인 직위는 삭제할 수 없습니다.", e);
		} catch (RuntimeException e) {
			JdbcUtil.rollback(conn);
			throw e;
		} finally {
			JdbcUtil.close(conn);
		}
	}

	private void validateDepartmentName(Connection conn, String name, int currentId) throws SQLException {
		String normalizedName = normalizeName(name, "부서명을 입력해주세요.");
		for (Department department : departmentDao.selectAll(conn)) {
			if (department.getDepartmentId() != currentId
					&& normalizedName.equalsIgnoreCase(department.getDepartmentName())) {
				throw new IllegalArgumentException("이미 등록된 부서명입니다.");
			}
		}
	}

	private void validatePositionName(Connection conn, String name, int currentId) throws SQLException {
		String normalizedName = normalizeName(name, "직위명을 입력해주세요.");
		for (JobPosition position : jobPositionDao.selectAll(conn)) {
			if (position.getJobPositionId() != currentId
					&& normalizedName.equalsIgnoreCase(position.getJobPositionName())) {
				throw new IllegalArgumentException("이미 등록된 직위명입니다.");
			}
		}
	}

	private String normalizeName(String name, String emptyMessage) {
		String normalizedName = name == null ? "" : name.trim();
		if (normalizedName.isEmpty()) {
			throw new IllegalArgumentException(emptyMessage);
		}
		if (normalizedName.length() > 100) {
			throw new IllegalArgumentException("이름은 100자 이하로 입력해주세요.");
		}
		return normalizedName;
	}
}
