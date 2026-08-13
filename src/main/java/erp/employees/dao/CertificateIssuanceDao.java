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
import erp.employees.dto.CertificateRegisterItem;
import erp.employees.service.CertificateRegisterCondition;
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

	// CERTIFICATE_ISSUANCE를 중심으로 사원, 부서, 직위를 JOIN한 발급대장 행 수 조회
	public int countByCondition(Connection conn, CertificateRegisterCondition condition) throws SQLException {
		StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM CERTIFICATE_ISSUANCE C ");
		sql.append("JOIN EMPLOYEE E ON C.EMPLOYEE_ID = E.EMPLOYEE_ID ");
		sql.append("LEFT JOIN DEPARTMENT D ON E.DEPARTMENT_ID = D.DEPARTMENT_ID ");
		sql.append("LEFT JOIN JOB_POSITION J ON E.JOB_POSITION_ID = J.JOB_POSITION_ID WHERE 1=1 ");
		List<Object> params = new ArrayList<>();
		appendRegisterConditions(sql, params, condition);
		try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
			setRegisterParameters(pstmt, params);
			try (ResultSet rs = pstmt.executeQuery()) { return rs.next() ? rs.getInt(1) : 0; }
		}
	}

	// 발급대장 화면에 필요한 증명서, 사원, 부서, 직위 정보를 페이지 단위로 조회
	public List<CertificateRegisterItem> selectRegisterByCondition(Connection conn, CertificateRegisterCondition condition)
			throws SQLException {
		StringBuilder inner = new StringBuilder();
		inner.append("SELECT C.CERTIFICATE_ISSUANCE_ID, C.CERT_DOC_NO, C.CERT_TYPE, C.PURPOSE, ");
		inner.append("E.EMP_TYPE, E.EMP_NAME_KR, D.DEPARTMENT_NAME, J.JOB_POSITION_NAME, ");
		inner.append("TO_CHAR(C.ISSUE_DATE, 'YYYY-MM-DD') ISSUE_DATE FROM CERTIFICATE_ISSUANCE C ");
		inner.append("JOIN EMPLOYEE E ON C.EMPLOYEE_ID = E.EMPLOYEE_ID ");
		inner.append("LEFT JOIN DEPARTMENT D ON E.DEPARTMENT_ID = D.DEPARTMENT_ID ");
		inner.append("LEFT JOIN JOB_POSITION J ON E.JOB_POSITION_ID = J.JOB_POSITION_ID WHERE 1=1 ");
		List<Object> params = new ArrayList<>();
		appendRegisterConditions(inner, params, condition);
		inner.append("ORDER BY C.ISSUE_DATE DESC, C.CERTIFICATE_ISSUANCE_ID DESC");
		String sql = "SELECT * FROM (SELECT REGISTER_DATA.*, ROWNUM RN FROM (" + inner
				+ ") REGISTER_DATA WHERE ROWNUM <= ?) WHERE RN >= ?";
		params.add(condition.getPage() * condition.getPageSize());
		params.add((condition.getPage() - 1) * condition.getPageSize() + 1);

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			setRegisterParameters(pstmt, params);
			try (ResultSet rs = pstmt.executeQuery()) {
				List<CertificateRegisterItem> result = new ArrayList<>();
				while (rs.next()) result.add(makeRegisterItem(rs));
				return result;
			}
		}
	}

	public int deleteAll(Connection conn) throws SQLException {
		try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM CERTIFICATE_ISSUANCE")) {
			return pstmt.executeUpdate();
		}
	}

	private void appendRegisterConditions(StringBuilder sql, List<Object> params, CertificateRegisterCondition condition) {
		String databaseType = toDatabaseCertificateType(condition.getCertificateType());
		if (databaseType != null) { sql.append("AND C.CERT_TYPE = ? "); params.add(databaseType); }
		if (!condition.getIssueDateFrom().isEmpty()) { sql.append("AND C.ISSUE_DATE >= TO_DATE(?, 'YYYY-MM-DD') "); params.add(condition.getIssueDateFrom()); }
		if (!condition.getIssueDateTo().isEmpty()) { sql.append("AND C.ISSUE_DATE < TO_DATE(?, 'YYYY-MM-DD') + 1 "); params.add(condition.getIssueDateTo()); }
		if (!condition.getKeyword().isEmpty()) {
			String keyword = "%" + condition.getKeyword() + "%";
			sql.append("AND (C.CERT_DOC_NO LIKE ? OR C.PURPOSE LIKE ? OR E.EMP_NAME_KR LIKE ? OR D.DEPARTMENT_NAME LIKE ?) ");
			params.add(keyword); params.add(keyword); params.add(keyword); params.add(keyword);
		}
	}

	private String toDatabaseCertificateType(String type) {
		if ("WORKING".equals(type)) return "재직경력서";
		if ("CAREER".equals(type)) return "경력증명서";
		if ("RETIREMENT".equals(type)) return "퇴직증명서";
		return null;
	}

	private void setRegisterParameters(PreparedStatement pstmt, List<Object> params) throws SQLException {
		for (int i = 0; i < params.size(); i++) pstmt.setObject(i + 1, params.get(i));
	}

	private CertificateRegisterItem makeRegisterItem(ResultSet rs) throws SQLException {
		CertificateRegisterItem item = new CertificateRegisterItem();
		item.setCertificateId(rs.getInt("CERTIFICATE_ISSUANCE_ID"));
		item.setCertificateNo(rs.getString("CERT_DOC_NO"));
		String typeName = rs.getString("CERT_TYPE");
		item.setCertificateTypeName("재직경력서".equals(typeName) ? "재직증명서" : typeName);
		item.setCertificateType("재직경력서".equals(typeName) ? "WORKING" : "경력증명서".equals(typeName) ? "CAREER" : "RETIREMENT");
		item.setCertificateUse(rs.getString("PURPOSE"));
		item.setEmploymentType(rs.getString("EMP_TYPE"));
		item.setEmployeeName(rs.getString("EMP_NAME_KR"));
		item.setDepartmentName(rs.getString("DEPARTMENT_NAME"));
		item.setPositionName(rs.getString("JOB_POSITION_NAME"));
		item.setIssueDate(rs.getString("ISSUE_DATE"));
		return item;
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
