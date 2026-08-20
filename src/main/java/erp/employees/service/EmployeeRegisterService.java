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
	public int saveEmployeeBasicInfo(Employee employee) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false); // 트랜잭션 시작

			// 기존 사원 존재 여부 확인
			Employee existingEmp = employee.getEmployeeId() > 0
					? employeeDao.selectById(conn, employee.getEmployeeId()) : null;

			if (existingEmp == null) {
				// PK를 먼저 확보해 사원번호와 모든 하위 이력이 동일한 사원을 가리키게 합니다.
				int employeeId = employee.getEmployeeId() > 0
						? employee.getEmployeeId() : employeeDao.nextEmployeeId(conn);
				employee.setEmployeeId(employeeId);
				employee.setEmpNo(createEmployeeNumber(employeeId));
				employeeDao.insert(conn, employee);
			} else {
				// 외부 사원번호와 사진은 별도 관리 항목이므로 일반 정보 수정 시 기존 값을 유지합니다.
				employee.setEmpNo(existingEmp.getEmpNo());
				employee.setPhotoPath(existingEmp.getPhotoPath());
				employeeDao.update(conn, employee);
			}

			conn.commit();
			return employee.getEmployeeId();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException("사원 기본정보 저장 중 오류 발생", e);
		} catch (RuntimeException e) {
			JdbcUtil.rollback(conn);
			throw e;
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 신규 화면에서 예상 사원번호를 보여주기 위해 실제 시퀀스 값을 미리 예약합니다.
	public int reserveEmployeeId() {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			return employeeDao.nextEmployeeId(conn);
		} catch (SQLException e) {
			throw new RuntimeException("사원번호 예약 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	public String getEmployeeNumberPreview(int employeeId) {
		return createEmployeeNumber(employeeId);
	}

	// 1로 시작하는 7자리 사원번호이며 뒤 6자리는 내부 식별번호와 함께 증가합니다.
	private String createEmployeeNumber(int employeeId) {
		if (employeeId < 0 || employeeId > 999999) {
			throw new IllegalStateException("사원번호 생성 범위를 초과했습니다.");
		}
		return String.format("1%06d", employeeId);
	}
}
