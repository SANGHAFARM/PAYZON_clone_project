package erp.employees.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.employees.model.CertificateIssuance;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 제증명서 발급 내역 데이터베이스 접근(DAO) 클래스
public class CertificateIssuanceDao {

	// 싱글톤 인스턴스 생성
	private static CertificateIssuanceDao certificateIssuanceDao = new CertificateIssuanceDao();

	// 싱글톤 접근 메서드
	public static CertificateIssuanceDao getInstance() {
		return certificateIssuanceDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	private CertificateIssuanceDao() {
	}

	// 제증명서 발급 내역 등록
	// 시퀀스를 사용하여 기본키 발급 및 증명서 발급 정보 저장
	public void insert(Connection conn, CertificateIssuance cert) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO CERTIFICATE_ISSUANCE ("
					+ "CERTIFICATE_ISSUANCE_ID, EMPLOYEE_ID, CERT_DOC_NO, CERT_TYPE, PURPOSE, CERT_MEMO, "
					+ "ISSUE_DATE, ISSUE_DEPT_ID, SHOW_CEO_YN, HIDE_JUMIN_YN, SHOW_LOGO_YN, SHOW_STAMP_YN) "
					+ "VALUES (CERTIFICATE_ISSUANCE_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, cert.getEmployeeId());
			pstmt.setString(2, cert.getCertDocNo());
			pstmt.setString(3, cert.getCertType());
			pstmt.setString(4, cert.getPurpose());
			pstmt.setString(5, cert.getCertMemo());
			pstmt.setTimestamp(6, new Timestamp(cert.getIssueDate().getTime()));

			// 발급부서 ID가 null일 경우를 대비한 데이터베이스 null 방어 로직 적용
			pstmt.setObject(7, cert.getIssueDeptId(), Types.NUMERIC);

			pstmt.setString(8, cert.getShowCeoYn());
			pstmt.setString(9, cert.getHideJuminYn());
			pstmt.setString(10, cert.getShowLogoYn());
			pstmt.setString(11, cert.getShowStampYn());

			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 제증명서 발급 내역 단건 조회
	// 기본키(CERTIFICATE_ISSUANCE_ID)를 기준으로 1건의 데이터 조회
	public CertificateIssuance selectById(Connection conn, int certIssueId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT CERTIFICATE_ISSUANCE_ID, EMPLOYEE_ID, CERT_DOC_NO, CERT_TYPE, PURPOSE, CERT_MEMO, "
					+ "ISSUE_DATE, ISSUE_DEPT_ID, SHOW_CEO_YN, HIDE_JUMIN_YN, SHOW_LOGO_YN, SHOW_STAMP_YN "
					+ "FROM CERTIFICATE_ISSUANCE WHERE CERTIFICATE_ISSUANCE_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, certIssueId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				return makeCertIssueFromResultSet(rs);
			}
			return null; // 조회된 데이터가 없을 경우 null 반환
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 특정 사원의 제증명서 발급 내역 목록 조회
	// 사원번호(EMPLOYEE_ID)를 기준으로 연관된 증명서 발급 내역 전체 반환 (최근 발급순 정렬)
	public List<CertificateIssuance> selectByEmpId(Connection conn, int empId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT CERTIFICATE_ISSUANCE_ID, EMPLOYEE_ID, CERT_DOC_NO, CERT_TYPE, PURPOSE, CERT_MEMO, "
					+ "ISSUE_DATE, ISSUE_DEPT_ID, SHOW_CEO_YN, HIDE_JUMIN_YN, SHOW_LOGO_YN, SHOW_STAMP_YN "
					+ "FROM CERTIFICATE_ISSUANCE WHERE EMPLOYEE_ID = ? ORDER BY CERTIFICATE_ISSUANCE_ID DESC";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, empId);
			rs = pstmt.executeQuery();

			List<CertificateIssuance> result = new ArrayList<>();
			while (rs.next()) {
				result.add(makeCertIssueFromResultSet(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 제증명서 발급 내역 수정
	// 기본키를 기준으로 증명서 세부 내용 및 출력 설정 데이터 수정
	public int update(Connection conn, CertificateIssuance cert) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "UPDATE CERTIFICATE_ISSUANCE SET "
					+ "EMPLOYEE_ID = ?, CERT_DOC_NO = ?, CERT_TYPE = ?, PURPOSE = ?, CERT_MEMO = ?, "
					+ "ISSUE_DATE = ?, ISSUE_DEPT_ID = ?, SHOW_CEO_YN = ?, HIDE_JUMIN_YN = ?, "
					+ "SHOW_LOGO_YN = ?, SHOW_STAMP_YN = ? " + "WHERE CERTIFICATE_ISSUANCE_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, cert.getEmployeeId());
			pstmt.setString(2, cert.getCertDocNo());
			pstmt.setString(3, cert.getCertType());
			pstmt.setString(4, cert.getPurpose());
			pstmt.setString(5, cert.getCertMemo());
			pstmt.setTimestamp(6, new Timestamp(cert.getIssueDate().getTime()));

			pstmt.setObject(7, cert.getIssueDeptId(), Types.NUMERIC);

			pstmt.setString(8, cert.getShowCeoYn());
			pstmt.setString(9, cert.getHideJuminYn());
			pstmt.setString(10, cert.getShowLogoYn());
			pstmt.setString(11, cert.getShowStampYn());
			pstmt.setInt(12, cert.getCertificateIssuanceId());

			return pstmt.executeUpdate(); // 수정된 행의 개수 반환
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 제증명서 발급 내역 삭제
	// 기본키를 기준으로 해당 데이터 삭제
	public int delete(Connection conn, int certIssueId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM CERTIFICATE_ISSUANCE WHERE CERTIFICATE_ISSUANCE_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, certIssueId);

			return pstmt.executeUpdate(); // 삭제된 행의 개수 반환
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// ResultSet 데이터를 CertificateIssuance 객체로 변환
	// 코드 중복 방지를 위한 공통 매핑 처리
	private CertificateIssuance makeCertIssueFromResultSet(ResultSet rs) throws SQLException {
		CertificateIssuance cert = new CertificateIssuance();

		cert.setCertificateIssuanceId(rs.getInt("CERTIFICATE_ISSUANCE_ID"));
		cert.setEmployeeId(rs.getInt("EMPLOYEE_ID"));
		cert.setCertDocNo(rs.getString("CERT_DOC_NO"));
		cert.setCertType(rs.getString("CERT_TYPE"));
		cert.setPurpose(rs.getString("PURPOSE"));
		cert.setCertMemo(rs.getString("CERT_MEMO"));

		Timestamp issueTs = rs.getTimestamp("ISSUE_DATE");
		if (issueTs != null) {
			cert.setIssueDate(new java.util.Date(issueTs.getTime()));
		}

		int issueDeptId = rs.getInt("ISSUE_DEPT_ID");
		cert.setIssueDeptId(rs.wasNull() ? null : issueDeptId);

		cert.setShowCeoYn(rs.getString("SHOW_CEO_YN"));
		cert.setHideJuminYn(rs.getString("HIDE_JUMIN_YN"));
		cert.setShowLogoYn(rs.getString("SHOW_LOGO_YN"));
		cert.setShowStampYn(rs.getString("SHOW_STAMP_YN"));

		return cert;
	}
}