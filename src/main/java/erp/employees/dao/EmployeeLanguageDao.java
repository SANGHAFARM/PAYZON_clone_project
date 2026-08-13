package erp.employees.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.employees.model.EmployeeLanguage;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 어학 이력 데이터베이스 접근(DAO) 클래스
public class EmployeeLanguageDao {

	// 싱글톤 인스턴스 생성
	private static EmployeeLanguageDao employeeLanguageDao = new EmployeeLanguageDao();

	// 싱글톤 접근 메서드
	public static EmployeeLanguageDao getInstance() {
		return employeeLanguageDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	private EmployeeLanguageDao() {
	}

	// 어학 내역 등록
	public void insert(Connection conn, EmployeeLanguage language) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO EMPLOYEE_LANGUAGE "
					+ "(EMPLOYEE_LANGUAGE_ID, EMPLOYEE_ID, LANG_NAME, TEST_NAME, SCORE, ACQ_DATE, READING_LEVEL, WRITING_LEVEL, SPEAKING_LEVEL) "
					+ "VALUES (EMPLOYEE_LANGUAGE_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, language.getEmployeeId());
			pstmt.setString(2, language.getLangName());
			pstmt.setString(3, language.getTestName());
			pstmt.setString(4, language.getScore());

			if (language.getAcqDate() == null) {
				pstmt.setNull(5, Types.DATE);
			} else {
				pstmt.setTimestamp(5, new Timestamp(language.getAcqDate().getTime()));
			}

			pstmt.setString(6, language.getReadingLevel());
			pstmt.setString(7, language.getWritingLevel());
			pstmt.setString(8, language.getSpeakingLevel());

			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 어학 내역 단건 조회
	public EmployeeLanguage selectById(Connection conn, int langId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT EMPLOYEE_LANGUAGE_ID, EMPLOYEE_ID, LANG_NAME, TEST_NAME, SCORE, ACQ_DATE, READING_LEVEL, WRITING_LEVEL, SPEAKING_LEVEL "
					+ "FROM EMPLOYEE_LANGUAGE WHERE EMPLOYEE_LANGUAGE_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, langId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				return makeLanguageFromResultSet(rs);
			}
			return null;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 특정 사원의 어학 내역 목록 조회
	public List<EmployeeLanguage> selectByEmpId(Connection conn, int empId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT EMPLOYEE_LANGUAGE_ID, EMPLOYEE_ID, LANG_NAME, TEST_NAME, SCORE, ACQ_DATE, READING_LEVEL, WRITING_LEVEL, SPEAKING_LEVEL "
					+ "FROM EMPLOYEE_LANGUAGE WHERE EMPLOYEE_ID = ? ORDER BY ACQ_DATE DESC NULLS LAST, EMPLOYEE_LANGUAGE_ID DESC";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, empId);
			rs = pstmt.executeQuery();

			List<EmployeeLanguage> result = new ArrayList<>();
			while (rs.next()) {
				result.add(makeLanguageFromResultSet(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 어학 내역 수정
	public int update(Connection conn, EmployeeLanguage language) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "UPDATE EMPLOYEE_LANGUAGE SET "
					+ "EMPLOYEE_ID = ?, LANG_NAME = ?, TEST_NAME = ?, SCORE = ?, ACQ_DATE = ?, READING_LEVEL = ?, WRITING_LEVEL = ?, SPEAKING_LEVEL = ? "
					+ "WHERE EMPLOYEE_LANGUAGE_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, language.getEmployeeId());
			pstmt.setString(2, language.getLangName());
			pstmt.setString(3, language.getTestName());
			pstmt.setString(4, language.getScore());

			if (language.getAcqDate() == null) {
				pstmt.setNull(5, Types.DATE);
			} else {
				pstmt.setTimestamp(5, new Timestamp(language.getAcqDate().getTime()));
			}

			pstmt.setString(6, language.getReadingLevel());
			pstmt.setString(7, language.getWritingLevel());
			pstmt.setString(8, language.getSpeakingLevel());
			pstmt.setInt(9, language.getEmployeeLanguageId());

			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 어학 내역 삭제
	public int delete(Connection conn, int langId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM EMPLOYEE_LANGUAGE WHERE EMPLOYEE_LANGUAGE_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, langId);
			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 사원번호(EMPLOYEE_ID)를 기준으로 해당 사원의 어학 내역 전체 삭제
	public void deleteByEmpId(Connection conn, int empId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM EMPLOYEE_LANGUAGE WHERE EMPLOYEE_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, empId);
			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// ResultSet 데이터를 EmployeeLanguage 객체로 변환
	private EmployeeLanguage makeLanguageFromResultSet(ResultSet rs) throws SQLException {
		EmployeeLanguage language = new EmployeeLanguage();

		language.setEmployeeLanguageId(rs.getInt("EMPLOYEE_LANGUAGE_ID"));
		language.setEmployeeId(rs.getInt("EMPLOYEE_ID"));
		language.setLangName(rs.getString("LANG_NAME"));
		language.setTestName(rs.getString("TEST_NAME"));
		language.setScore(rs.getString("SCORE"));

		Timestamp acqTs = rs.getTimestamp("ACQ_DATE");
		if (acqTs != null) {
			language.setAcqDate(new java.util.Date(acqTs.getTime()));
		}

		language.setReadingLevel(rs.getString("READING_LEVEL"));
		language.setWritingLevel(rs.getString("WRITING_LEVEL"));
		language.setSpeakingLevel(rs.getString("SPEAKING_LEVEL"));

		return language;
	}
}