package erp.employees.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import erp.employees.dao.CertificateIssuanceDao;
import erp.employees.dto.CertificateRegisterItem;
import erp.employees.dto.EmployeePageInfo;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

// 제증명서 발급대장 조회와 삭제 트랜잭션을 처리한다.
public class CertificateRegisterService {
	private final CertificateIssuanceDao certificateDao = CertificateIssuanceDao.getInstance();

	public CertificateRegisterResult getRegister(CertificateRegisterCondition condition) {
		try (Connection conn = ConnectionProvider.getConnection()) {
			int totalCount = certificateDao.countByCondition(conn, condition);
			EmployeePageInfo pageInfo = new EmployeePageInfo(totalCount, condition.getPage(), condition.getPageSize());
			condition.setPage(pageInfo.getCurrentPage());
			List<CertificateRegisterItem> certificates = certificateDao.selectRegisterByCondition(conn, condition);
			return new CertificateRegisterResult(certificates, totalCount, pageInfo);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public int delete(String deleteMode, String[] certificateIds) {
		if (!"ALL".equals(deleteMode) && !"SELECTED".equals(deleteMode)) {
			throw new IllegalArgumentException("올바르지 않은 삭제 요청입니다.");
		}
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			int deletedCount = 0;
			if ("ALL".equals(deleteMode)) {
				deletedCount = certificateDao.deleteAll(conn);
			} else if (certificateIds != null) {
				for (String id : certificateIds) {
					deletedCount += certificateDao.delete(conn, Integer.parseInt(id));
				}
			}
			conn.commit();
			return deletedCount;
		} catch (SQLException | NumberFormatException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	public static class CertificateRegisterResult {
		private final List<CertificateRegisterItem> certificates;
		private final int totalCount;
		private final EmployeePageInfo pageInfo;
		public CertificateRegisterResult(List<CertificateRegisterItem> certificates, int totalCount, EmployeePageInfo pageInfo) {
			this.certificates = certificates;
			this.totalCount = totalCount;
			this.pageInfo = pageInfo;
		}
		public List<CertificateRegisterItem> getCertificates() { return certificates; }
		public int getTotalCount() { return totalCount; }
		public EmployeePageInfo getPageInfo() { return pageInfo; }
	}
}
