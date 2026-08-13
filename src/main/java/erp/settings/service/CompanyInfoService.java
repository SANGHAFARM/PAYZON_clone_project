package erp.settings.service;

import java.sql.Connection;
import java.sql.SQLException;

import erp.settings.dao.CompanyDao;
import erp.settings.model.Company;
import jdbc.connection.ConnectionProvider; // 커넥션 풀 제공자
import jdbc.JdbcUtil; // 자원 반환/롤백 유틸리티

public class CompanyInfoService {

	// 싱글톤 인스턴스 생성
	private static CompanyInfoService companyInfoService = new CompanyInfoService();

	// 싱글톤 접근 메서드
	public static CompanyInfoService getInstance() {
		return companyInfoService;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	private CompanyInfoService() {
	}

	private CompanyDao companyDao = CompanyDao.getInstance();

	/**
	 * [화면 로딩] 회사 기본환경설정 데이터 조회 페이지 진입 시 등록된 회사 정보를 불러와 input/select 박스에 바인딩하기 위해 사용
	 */
	public Company getCompanyDetails(int companyId) {
		Connection conn = null;
		try {
			// ConnectionProvider를 통해 커넥션 풀에서 커넥션 획득
			conn = ConnectionProvider.getConnection();
			return companyDao.selectById(conn, companyId);
		} catch (SQLException e) {
			throw new RuntimeException("회사 정보 조회 중 오류가 발생했습니다.", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	/**
	 * [저장하기] 회사정보 폼 전체 저장 (btnAllSave 클릭 시) 회사, 담당자, 급여지급 탭의 모든 내용을 하나의 트랜잭션으로 덮어쓰기
	 */
	public void saveAllCompanyInfo(Company company) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false); // 트랜잭션 시작

			// 기존 데이터 존재 여부 확인 후 INSERT 또는 UPDATE 분기 처리
			Company existingCompany = companyDao.selectById(conn, company.getCompanyId());

			if (existingCompany == null) {
				companyDao.insert(conn, company);
			} else {
				companyDao.update(conn, company);
			}

			conn.commit(); // 정상 처리 시 커밋
		} catch (SQLException e) {
			JdbcUtil.rollback(conn); // 에러 발생 시 롤백
			throw new RuntimeException("회사 정보 저장 중 오류가 발생했습니다.", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}
}