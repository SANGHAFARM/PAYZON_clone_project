package erp.employees.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import erp.employees.dao.EmployeeGuarantorDao;
import erp.employees.dao.EmployeeRecommenderDao;
import erp.employees.dao.EmployeeSuretyInsuranceDao;
import erp.employees.model.EmployeeGuarantor;
import erp.employees.model.EmployeeRecommender;
import erp.employees.model.EmployeeSuretyInsurance;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

public class EmployeeGuaranteeService {

	// 싱글톤 인스턴스 생성
	private static EmployeeGuaranteeService instance = new EmployeeGuaranteeService();

	// 싱글톤 접근 메서드
	public static EmployeeGuaranteeService getInstance() {
		return instance;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	private EmployeeGuaranteeService() {
	}

	private EmployeeRecommenderDao recommenderDao = EmployeeRecommenderDao.getInstance();
	private EmployeeSuretyInsuranceDao suretyInsuranceDao = EmployeeSuretyInsuranceDao.getInstance();
	private EmployeeGuarantorDao guarantorDao = EmployeeGuarantorDao.getInstance();

	public EmployeeRecommender getRecommender(int empId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			List<EmployeeRecommender> rows = recommenderDao.selectByEmpId(conn, empId);
			return rows.isEmpty() ? null : rows.get(0);
		} catch (SQLException e) {
			throw new RuntimeException("사원 추천인 조회 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	public EmployeeSuretyInsurance getSuretyInsurance(int empId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			List<EmployeeSuretyInsurance> rows = suretyInsuranceDao.selectByEmpId(conn, empId);
			return rows.isEmpty() ? null : rows.get(0);
		} catch (SQLException e) {
			throw new RuntimeException("사원 보증보험 조회 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	public EmployeeGuarantor getGuarantor(int empId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			List<EmployeeGuarantor> rows = guarantorDao.selectByEmpId(conn, empId);
			return rows.isEmpty() ? null : rows.get(0);
		} catch (SQLException e) {
			throw new RuntimeException("사원 신원보증인 조회 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// [통합 저장] 추천 및 신원보증 내역을 일괄 갱신합니다. v5 스키마 기준 단일 행 데이터들을 처리
	public void saveGuarantees(int empId, EmployeeRecommender recommender, EmployeeSuretyInsurance surety,
			EmployeeGuarantor guarantor) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);

			// 기존 데이터 삭제 후 삽입 (1:1 테이블이더라도 확장성을 위해 Delete & Insert 적용)
			recommenderDao.deleteByEmpId(conn, empId);
			suretyInsuranceDao.deleteByEmpId(conn, empId);
			guarantorDao.deleteByEmpId(conn, empId);

			if (recommender != null && recommender.getRecommenderName() != null
					&& !recommender.getRecommenderName().isEmpty()) {
				recommenderDao.insert(conn, recommender);
			}
			if (surety != null && surety.getProviderName() != null && !surety.getProviderName().isEmpty()) {
				suretyInsuranceDao.insert(conn, surety);
			}
			if (guarantor != null && guarantor.getGuarantorName() != null && !guarantor.getGuarantorName().isEmpty()) {
				guarantorDao.insert(conn, guarantor);
			}

			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException("추천 및 보증정보 갱신 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}
}
