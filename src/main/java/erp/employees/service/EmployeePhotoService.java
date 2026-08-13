package erp.employees.service;

import java.sql.Connection;
import java.sql.SQLException;

import erp.employees.dao.EmployeeDao;
import erp.employees.model.Employee;
import jdbc.connection.ConnectionProvider;
import jdbc.JdbcUtil;

// 사원 프로필 사진(이미지 파일) DB 경로 업데이트 및 삭제 처리를 전담하는 서비스
public class EmployeePhotoService {

	// 싱글톤 패턴 적용
	private static EmployeePhotoService employeePhotoService = new EmployeePhotoService();

	public static EmployeePhotoService getInstance() {
		return employeePhotoService;
	}

	private EmployeePhotoService() {
	}

	private EmployeeDao employeeDao = EmployeeDao.getInstance();

	// 사진 등록
	public void uploadPhoto(int empId, String savedFilePath) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);

			Employee employee = employeeDao.selectById(conn, empId);
			if (employee != null) {
				employee.setPhotoPath(savedFilePath);
				employeeDao.update(conn, employee); // 경로 업데이트
			}

			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException("사원 사진 경로 업데이트 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// [사진 삭제] 사원의 DB 사진 경로를 NULL로 초기화
	public void deletePhoto(int empId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);

			Employee employee = employeeDao.selectById(conn, empId);
			if (employee != null) {
				employee.setPhotoPath(null); // 경로 비우기
				employeeDao.update(conn, employee);
			}

			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException("사원 사진 삭제 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}
}