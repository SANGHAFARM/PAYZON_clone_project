package erp.employees.service;

import java.sql.Connection;
import java.sql.SQLException;

import erp.employees.dao.EmployeeDao;
import erp.employees.model.Employee;
import jdbc.connection.ConnectionProvider;
import jdbc.JdbcUtil;

// 사원 기본정보, 급여/4대보험 설정, 병역사항 등 1:1 핵심 데이터를 관리하는 서비스
public class EmployeeRegisterService {

	// 싱글톤 패턴 적용
	private static EmployeeRegisterService employeeRegisterService = new EmployeeRegisterService();

	public static EmployeeRegisterService getInstance() {
		return employeeRegisterService;
	}

	private EmployeeRegisterService() {
	}

	private EmployeeDao employeeDao = EmployeeDao.getInstance();

	// [조회] 사원 기본 프로필 정보 가져오기 (화면 렌더링용)
	public Employee getEmployeeBasicProfile(int empId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			return employeeDao.selectById(conn, empId);
		} catch (SQLException e) {
			throw new RuntimeException("사원 기본정보 조회 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// [저장] 사원 기본정보 통합 저장
	public void saveEmployeeBasicInfo(Employee employee) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false); // 트랜잭션 시작

			// 기존 사원 존재 여부 확인
			Employee existingEmp = employeeDao.selectById(conn, employee.getEmployeeId());

			if (existingEmp == null) {
				// 신규 등록
				employeeDao.insert(conn, employee);
			} else {
				// 기존 정보 덮어쓰기 (사진 경로 등 화면에서 넘어오지 않는 기존 유지 데이터는 보호 처리 필요)
				employee.setPhotoPath(existingEmp.getPhotoPath()); // 기존 사진 경로 유지
				employeeDao.update(conn, employee);
			}

			conn.commit(); // 정상 완료 시 커밋
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException("사원 기본정보 저장 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}
}