package erp.hr.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.hr.model.EmpLanguage;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 어학 내역 데이터베이스 접근(DAO) 클래스
public class EmpLanguageDao {

    // 싱글톤 인스턴스 생성
    private static EmpLanguageDao empLanguageDao = new EmpLanguageDao();

    // 싱글톤 접근 메서드
    public static EmpLanguageDao getInstance() {
        return empLanguageDao;
    }

    // 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
    private EmpLanguageDao() {}

    // 어학 내역 등록
    // 시퀀스를 사용하여 기본키 발급 및 어학 성적 데이터 저장
    public void insert(Connection conn, EmpLanguage language) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO EMP_LANGUAGE "
                       + "(LANG_ID, EMP_ID, LANG_NAME, TEST_NAME, SCORE, ACQ_DATE, READING_LEVEL, WRITING_LEVEL, SPEAKING_LEVEL) "
                       + "VALUES (SEQ_EMP_LANG_ID.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, language.getEmpId());
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
    // 기본키(LANG_ID)를 기준으로 1건의 데이터 반환
    public EmpLanguage selectById(Connection conn, int langId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT LANG_ID, EMP_ID, LANG_NAME, TEST_NAME, SCORE, ACQ_DATE, READING_LEVEL, WRITING_LEVEL, SPEAKING_LEVEL "
                       + "FROM EMP_LANGUAGE WHERE LANG_ID = ?";
            
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
    // 사원번호(EMP_ID)를 기준으로 연관된 어학 내역 전체 반환 (최근 취득일 기준 내림차순 정렬)
    public List<EmpLanguage> selectByEmpId(Connection conn, int empId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT LANG_ID, EMP_ID, LANG_NAME, TEST_NAME, SCORE, ACQ_DATE, READING_LEVEL, WRITING_LEVEL, SPEAKING_LEVEL "
                       + "FROM EMP_LANGUAGE WHERE EMP_ID = ? ORDER BY ACQ_DATE DESC NULLS LAST, LANG_ID DESC";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, empId);
            rs = pstmt.executeQuery();
            
            List<EmpLanguage> result = new ArrayList<>();
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
    public int update(Connection conn, EmpLanguage language) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE EMP_LANGUAGE SET "
                       + "EMP_ID = ?, LANG_NAME = ?, TEST_NAME = ?, SCORE = ?, ACQ_DATE = ?, READING_LEVEL = ?, WRITING_LEVEL = ?, SPEAKING_LEVEL = ? "
                       + "WHERE LANG_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, language.getEmpId());
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
            pstmt.setInt(9, language.getLangId());
            
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
            String sql = "DELETE FROM EMP_LANGUAGE WHERE LANG_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, langId);
            
            return pstmt.executeUpdate(); // 삭제된 행의 개수 반환
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // ResultSet 데이터를 EmpLanguage 객체로 변환
    // 코드 중복 방지를 위한 공통 매핑 처리
    private EmpLanguage makeEmpLanguageFromResultSet(ResultSet rs) throws SQLException {
        EmpLanguage language = new EmpLanguage();
        language.setLangId(rs.getInt("LANG_ID"));
        language.setEmpId(rs.getInt("EMP_ID"));
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