package erp.employees.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import erp.employees.dao.EmployeeCareerDao;
import erp.employees.dao.EmployeeDependentDao;
import erp.employees.dao.EmployeeEducationDao;
import erp.employees.dao.EmployeeInsuranceHistoryDao;
import erp.employees.model.EmployeeCareer;
import erp.employees.model.EmployeeDependent;
import erp.employees.model.EmployeeEducation;
import erp.employees.model.EmployeeInsuranceHistory;
import jdbc.connection.ConnectionProvider;
import jdbc.JdbcUtil;

// 사원 하위에 1:N으로 달리는 부양가족, 학력, 경력, 4대보험 이력을 통합 관리하는 서비스
public class EmployeeHistoryService {

	// 싱글톤 패턴 적용
	private static EmployeeHistoryService employeeHistoryService = new EmployeeHistoryService();

	public static EmployeeHistoryService getInstance() {
		return employeeHistoryService;
	}

	private EmployeeHistoryService() {
	}

	// 1:N 하위 테이블을 담당하는 DAO들
	private EmployeeDependentDao dependentDao = EmployeeDependentDao.getInstance();
	private EmployeeEducationDao educationDao = EmployeeEducationDao.getInstance();
	private EmployeeCareerDao careerDao = EmployeeCareerDao.getInstance();
	private EmployeeInsuranceHistoryDao insuranceDao = EmployeeInsuranceHistoryDao.getInstance();

	// [조회] 사원번호(EMPLOYEE_ID)를 기준으로 부양가족 목록을 조회
	public List<EmployeeDependent> getDependents(int empId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			return dependentDao.selectByEmpId(conn, empId);
		} catch (SQLException e) {
			throw new RuntimeException("사원 부양가족 목록 조회 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// [조회] 사원번호(EMPLOYEE_ID)를 기준으로 학력 목록을 조회
	public List<EmployeeEducation> getEducations(int empId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			return educationDao.selectByEmpId(conn, empId);
		} catch (SQLException e) {
			throw new RuntimeException("사원 학력 목록 조회 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// [조회] 사원번호(EMPLOYEE_ID)를 기준으로 경력 목록을 조회
	public List<EmployeeCareer> getCareers(int empId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			return careerDao.selectByEmpId(conn, empId);
		} catch (SQLException e) {
			throw new RuntimeException("사원 경력 목록 조회 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// [통합 저장] 폼에서 넘어온 1:N 리스트 데이터 일괄 갱신 (action="save" 시 호출)
	public void saveAllHistories(int empId, List<EmployeeDependent> deps, List<EmployeeEducation> edus,
			List<EmployeeCareer> cars, List<EmployeeInsuranceHistory> insurances) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false); // 하나의 거대한 트랜잭션으로 묶기

			// 1. 기존 이력 일괄 삭제 (사원번호 기준)
			dependentDao.deleteByEmpId(conn, empId);
			educationDao.deleteByEmpId(conn, empId);
			careerDao.deleteByEmpId(conn, empId);
			insuranceDao.deleteByEmpId(conn, empId);

			// 2. 화면에서 넘어온 새로운 리스트 일괄 추가 (반복문 사용)
			if (deps != null) {
				for (EmployeeDependent dep : deps) {
					dependentDao.insert(conn, dep);
				}
			}
			if (edus != null) {
				for (EmployeeEducation edu : edus) {
					educationDao.insert(conn, edu);
				}
			}
			if (cars != null) {
				for (EmployeeCareer car : cars) {
					careerDao.insert(conn, car);
				}
			}
			if (insurances != null) {
				for (EmployeeInsuranceHistory ins : insurances) {
					insuranceDao.insert(conn, ins);
				}
			}

			conn.commit(); // 모두 성공 시 커밋
		} catch (SQLException e) {
			JdbcUtil.rollback(conn); // 단 하나라도 실패하면 롤백 (이력 꼬임 방지)
			throw new RuntimeException("사원 이력 정보 일괄 갱신 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// [선택 삭제] 화면에서 체크박스 선택 후 "선택삭제" 버튼 클릭 시 개별 삭제 수행
	public void deleteSelectedItems(String type, List<Integer> deleteIds) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);

			for (Integer id : deleteIds) {
				switch (type) {
				case "dependent":
					dependentDao.delete(conn, id);
					break;
				case "education":
					educationDao.delete(conn, id);
					break;
				case "career":
					careerDao.delete(conn, id);
					break;
				}
			}
			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException("선택 항목 삭제 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// [조회] 사원번호(EMPLOYEE_ID)를 기준으로 4대보험 취득/상실 이력 목록을 조회
	public List<EmployeeInsuranceHistory> getInsuranceHistories(int empId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			return insuranceDao.selectByEmpId(conn, empId);
		} catch (SQLException e) {
			throw new RuntimeException("사원 4대보험 이력 조회 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}
}