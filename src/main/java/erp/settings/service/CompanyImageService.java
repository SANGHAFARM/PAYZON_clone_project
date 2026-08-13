package erp.settings.service;

import java.sql.Connection;
import java.sql.SQLException;

import erp.settings.dao.CompanyDao;
import erp.settings.model.Company;
import jdbc.connection.ConnectionProvider;
import jdbc.JdbcUtil;

public class CompanyImageService {

	// 싱글톤 인스턴스 생성
	private static CompanyImageService companyImageService = new CompanyImageService();

	// 싱글톤 접근 메서드
	public static CompanyImageService getInstance() {
		return companyImageService;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	private CompanyImageService() {
	}

	private CompanyDao companyDao = CompanyDao.getInstance();

	/**
	 * [이미지 등록] 로고 또는 도장 이미지 경로 DB 업데이트 컨트롤러에서 물리적 파일 저장(가로 150px 썸네일 변환 등)을 완료한 후,
	 * 최종 웹 접근 경로(savedFilePath)를 DB에 기록
	 */
	public void uploadImage(int companyId, String imageType, String savedFilePath) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);

			Company company = companyDao.selectById(conn, companyId);
			if (company != null) {
				// 타입에 따라 해당하는 필드 업데이트
				if ("logo".equals(imageType)) {
					company.setLogoImgPath(savedFilePath);
				} else if ("stamp".equals(imageType)) {
					company.setStampImgPath(savedFilePath);
				}
				companyDao.update(conn, company);
			}

			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException("이미지 경로 업데이트 중 오류가 발생했습니다.", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	/**
	 * [이미지 삭제] 로고 또는 도장 이미지 DB 경로 초기화 (btnLogoDel, btnStampDel)
	 */
	public void deleteImage(int companyId, String imageType) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);

			Company company = companyDao.selectById(conn, companyId);
			if (company != null) {
				// 삭제 시 경로를 비워줌
				if ("logo".equals(imageType)) {
					company.setLogoImgPath(null);
				} else if ("stamp".equals(imageType)) {
					company.setStampImgPath(null);
				}
				companyDao.update(conn, company);
			}

			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException("이미지 삭제 중 오류가 발생했습니다.", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}
}