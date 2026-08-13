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
			departmentDao.insert(conn, dept);
			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException("부서 등록 중 에러", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}
}