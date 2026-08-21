package erp.employees.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import erp.employees.dao.EmployeeAppointmentDao;
import erp.employees.dao.EmployeeLanguageDao;
import erp.employees.dao.EmployeeLicenseDao;
import erp.employees.dao.EmployeeRewardDisciplineDao;
import erp.employees.dao.EmployeeTrainingDao;
import erp.employees.model.EmployeeAppointment;
import erp.employees.model.EmployeeLanguage;
import erp.employees.model.EmployeeLicense;
import erp.employees.model.EmployeeRewardDiscipline;
import erp.employees.model.EmployeeTraining;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

public class EmployeeSkillRecordService {

	// 싱글톤 인스턴스 생성
	private static EmployeeSkillRecordService instance = new EmployeeSkillRecordService();

	// 싱글톤 접근 메서드
	public static EmployeeSkillRecordService getInstance() {
		return instance;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	private EmployeeSkillRecordService() {
	}

	// 각 1:N 하위 테이블을 담당하는 DAO 객체들 (동일한 싱글톤 패턴 적용 가정)
	private EmployeeLicenseDao licenseDao = EmployeeLicenseDao.getInstance();
	private EmployeeLanguageDao languageDao = EmployeeLanguageDao.getInstance();
	private EmployeeTrainingDao trainingDao = EmployeeTrainingDao.getInstance();
	private EmployeeRewardDisciplineDao rewardDao = EmployeeRewardDisciplineDao.getInstance();
	private EmployeeAppointmentDao appointmentDao = EmployeeAppointmentDao.getInstance();

	// [조회] 사원번호(empId)로 각 역량 및 인사기록 리스트를 조회
	public List<EmployeeLicense> getLicenses(int empId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			return licenseDao.selectByEmpId(conn, empId);
		} catch (SQLException e) {
			throw new RuntimeException("사원 자격/면허 조회 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	public List<EmployeeLanguage> getLanguages(int empId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			return languageDao.selectByEmpId(conn, empId);
		} catch (SQLException e) {
			throw new RuntimeException("사원 어학능력 조회 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	public List<EmployeeTraining> getTrainings(int empId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			return trainingDao.selectByEmpId(conn, empId);
		} catch (SQLException e) {
			throw new RuntimeException("사원 교육/훈련 조회 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	public List<EmployeeRewardDiscipline> getRewardPunishes(int empId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			return rewardDao.selectByEmpId(conn, empId);
		} catch (SQLException e) {
			throw new RuntimeException("사원 상벌 조회 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	public List<EmployeeAppointment> getAppointments(int empId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			return appointmentDao.selectByEmpId(conn, empId);
		} catch (SQLException e) {
			throw new RuntimeException("사원 발령 조회 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// [통합 저장] 폼에서 넘어온 1:N 리스트 데이터를 하나의 트랜잭션으로 일괄 갱신
	public void saveAllSkillRecords(int empId, List<EmployeeLicense> licenses, List<EmployeeLanguage> languages,
			List<EmployeeTraining> trainings, List<EmployeeRewardDiscipline> rewards,
			List<EmployeeAppointment> appointments) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false); // 트랜잭션 시작

			// 1. 기존 데이터 일괄 삭제
			licenseDao.deleteByEmpId(conn, empId);
			languageDao.deleteByEmpId(conn, empId);
			trainingDao.deleteByEmpId(conn, empId);
			rewardDao.deleteByEmpId(conn, empId);
			appointmentDao.deleteByEmpId(conn, empId);

			// 2. 화면에서 넘어온 새로운 리스트 일괄 추가
			if (licenses != null) {
				for (EmployeeLicense item : licenses)
					licenseDao.insert(conn, item);
			}
			if (languages != null) {
				for (EmployeeLanguage item : languages)
					languageDao.insert(conn, item);
			}
			if (trainings != null) {
				for (EmployeeTraining item : trainings)
					trainingDao.insert(conn, item);
			}
			if (rewards != null) {
				for (EmployeeRewardDiscipline item : rewards)
					rewardDao.insert(conn, item);
			}
			if (appointments != null) {
				for (EmployeeAppointment item : appointments)
					appointmentDao.insert(conn, item);
			}

			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException("사원 역량 및 인사기록 일괄 갱신 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// [선택 삭제] 화면에서 체크박스 선택 후 삭제 버튼 클릭 시 개별 삭제를 수행
	public void deleteSelectedItems(String type, List<Integer> deleteIds) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			for (Integer id : deleteIds) {
				switch (type) {
				case "license":
					licenseDao.delete(conn, id);
					break;
				case "language":
					languageDao.delete(conn, id);
					break;
				case "training":
					trainingDao.delete(conn, id);
					break;
				case "reward":
					rewardDao.delete(conn, id);
					break;
				case "appointment":
					appointmentDao.delete(conn, id);
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
}
