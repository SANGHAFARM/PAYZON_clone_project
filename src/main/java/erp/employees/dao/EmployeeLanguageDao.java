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

// 어학 내역 데이터베이스 접근(DAO) 클래스
public class EmployeeLanguageDao {

    // 싱글톤 인스턴스 생성
    private static EmployeeLanguageDao employeeLanguageDao = new EmployeeLanguageDao();

    // 싱글톤 접근 메서드
    public static EmployeeLanguageDao getInstance() {
        return employeeLanguageDao;
    }

    // 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
    private EmployeeLanguageDao() {}

    // 어학 내역 등록
    // 시퀀스를 사용하여 기본키 발급 및 어학 성적 데이터 저장
    public void insert(Connection conn, EmployeeLanguage language) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO EMPLOYEE_LANGUAGE "
                       + "(EMPLOYEE_LANGUAGE_ID, EMPLOYEE_ID, LANG_NAME, TEST_NAME, SCORE, ACQ_DATE, READING_LEVEL, WRITING_LEVEL, SPEAKING_LEVEL) "
                       + "VALUES (SEQ_EMP_LANG_ID.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, language.getEmployeeId());
            pstmt.setString(2, language.getLangName());
            pstmt.setString(3, language.getTestName());
            pstmt.setString(4, language.getScore());
            
            // 취득일자가 null일 경우를 대비한 데이터베이스 null 방어 로직 적용
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
    // 기본키(EMPLOYEE_LANGUAGE_ID)를 기준으로 1건의 데이터 반환
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
                return makeEmpLanguageFromResultSet(rs);
            }
            return null; // 조회된 데이터가 없을 경우 null 반환
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 특정 사원의 어학 내역 목록 조회
    // 사원번호(EMPLOYEE_ID)를 기준으로 연관된 어학 내역 전체 반환 (최근 취득일 기준 내림차순 정렬)
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
                result.add(makeEmpLanguageFromResultSet(rs));
            }
            return result;
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 어학 내역 수정
    // 기본키를 기준으로 시험명, 점수, 등급 및 날짜 데이터 수정
    public int update(Connection conn, EmployeeLanguage language) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE EMPLOYEE_LANGUAGE SET "
                       + "EMPLOYEE_ID = ?, LANG_NAME = ?, TEST_NAME = ?, SCORE = ?, ACQ_DATE = ?, READING_LEVEL = ?, WRITING_LEVEL = ?, SPEAKING_LEVEL = ? "
                       + "WHERE EMPLOYEE_LANGUAGE_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, language.getEmployeeId());
            pstmt.setString(2, language.getLangName());
            pstmt.setString(3, language.getTestName());
            pstmt.setString(4, language.getScore());
            
            // 날짜 null 방어 로직 적용
            if (language.getAcqDate() == null) {
                pstmt.setNull(5, Types.DATE);
            } else {
                pstmt.setTimestamp(5, new Timestamp(language.getAcqDate().getTime()));
            }
            
            pstmt.setString(6, language.getReadingLevel());
            pstmt.setString(7, language.getWritingLevel());
            pstmt.setString(8, language.getSpeakingLevel());
            pstmt.setLong(9, language.getEmployeeLanguageId());
            
            return pstmt.executeUpdate(); // 수정된 행의 개수 반환
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 어학 내역 삭제
    // 기본키를 기준으로 해당 데이터 삭제
    public int delete(Connection conn, int langId) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM EMPLOYEE_LANGUAGE WHERE EMPLOYEE_LANGUAGE_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, langId);
            
            return pstmt.executeUpdate(); // 삭제된 행의 개수 반환
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // ResultSet 데이터를 EmployeeLanguage 객체로 변환
    // 코드 중복 방지를 위한 공통 매핑 처리
    private EmployeeLanguage makeEmpLanguageFromResultSet(ResultSet rs) throws SQLException {
        EmployeeLanguage language = new EmployeeLanguage();
        language.setEmployeeLanguageId(rs.getLong("EMPLOYEE_LANGUAGE_ID"));
        language.setEmployeeId(rs.getLong("EMPLOYEE_ID"));
        language.setLangName(rs.getString("LANG_NAME"));
        language.setTestName(rs.getString("TEST_NAME"));
        language.setScore(rs.getString("SCORE"));
        
        // 데이터베이스의 Date 값이 null일 경우를 대비한 안전한 변환 처리
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
