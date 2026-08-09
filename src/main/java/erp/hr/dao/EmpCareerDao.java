package erp.hr.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.hr.model.EmpCareer;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 사원 경력 내역 데이터베이스 접근(DAO) 클래스
public class EmpCareerDao {

    // 싱글톤 인스턴스 생성
    private static EmpCareerDao empCareerDao = new EmpCareerDao();

    // 싱글톤 접근 메서드
    public static EmpCareerDao getInstance() {
        return empCareerDao;
    }

    // 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
    private EmpCareerDao() {}

    // 경력 내역 등록
    // 시퀀스를 사용하여 기본키 발급 및 데이터 저장
    public void insert(Connection conn, EmpCareer career) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO EMP_CAREER "
                       + "(CAR_ID, EMP_ID, COMPANY_NAME, JOIN_DATE, QUIT_DATE, FINAL_POSITION, DUTY, QUIT_REASON) "
                       + "VALUES (SEQ_EMP_CAR_ID.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, career.getEmpId());
            pstmt.setString(2, career.getCompanyName());
            
            // 입사일자와 퇴사일자가 null일 경우를 대비한 데이터베이스 null 방어 로직 적용
            if (career.getJoinDate() == null) {
                pstmt.setNull(3, Types.DATE);
            } else {
                pstmt.setTimestamp(3, new Timestamp(career.getJoinDate().getTime()));
            }
            
            if (career.getQuitDate() == null) {
                pstmt.setNull(4, Types.DATE);
            } else {
                pstmt.setTimestamp(4, new Timestamp(career.getQuitDate().getTime()));
            }
            
            pstmt.setString(5, career.getFinalPosition());
            pstmt.setString(6, career.getDuty());
            pstmt.setString(7, career.getQuitReason());
            
            pstmt.executeUpdate();
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 경력 내역 단건 조회
    // 기본키(CAR_ID)를 기준으로 1건의 데이터 조회
    public EmpCareer selectById(Connection conn, int carId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT CAR_ID, EMP_ID, COMPANY_NAME, JOIN_DATE, QUIT_DATE, FINAL_POSITION, DUTY, QUIT_REASON "
                       + "FROM EMP_CAREER WHERE CAR_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, carId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return makeEmpCareerFromResultSet(rs);
            }
            return null; // 조회된 데이터가 없을 경우 null 반환
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 특정 사원의 경력 내역 목록 조회
    // 사원번호(EMP_ID)를 기준으로 연관된 경력 내역 전체 반환 (최근 입사일 기준 내림차순 정렬)
    public List<EmpCareer> selectByEmpId(Connection conn, int empId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT CAR_ID, EMP_ID, COMPANY_NAME, JOIN_DATE, QUIT_DATE, FINAL_POSITION, DUTY, QUIT_REASON "
                       + "FROM EMP_CAREER WHERE EMP_ID = ? ORDER BY JOIN_DATE DESC, CAR_ID DESC";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, empId);
            rs = pstmt.executeQuery();
            
            List<EmpCareer> result = new ArrayList<>();
            while (rs.next()) {
                result.add(makeEmpCareerFromResultSet(rs));
            }
            return result;
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 경력 내역 수정
    // 기본키를 기준으로 경력 상세 내용 및 날짜 데이터 수정
    public int update(Connection conn, EmpCareer career) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE EMP_CAREER SET "
                       + "EMP_ID = ?, COMPANY_NAME = ?, JOIN_DATE = ?, QUIT_DATE = ?, FINAL_POSITION = ?, DUTY = ?, QUIT_REASON = ? "
                       + "WHERE CAR_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, career.getEmpId());
            pstmt.setString(2, career.getCompanyName());
            
            // 날짜 null 방어 로직 적용
            if (career.getJoinDate() == null) {
                pstmt.setNull(3, Types.DATE);
            } else {
                pstmt.setTimestamp(3, new Timestamp(career.getJoinDate().getTime()));
            }
            
            if (career.getQuitDate() == null) {
                pstmt.setNull(4, Types.DATE);
            } else {
                pstmt.setTimestamp(4, new Timestamp(career.getQuitDate().getTime()));
            }
            
            pstmt.setString(5, career.getFinalPosition());
            pstmt.setString(6, career.getDuty());
            pstmt.setString(7, career.getQuitReason());
            pstmt.setInt(8, career.getCarId());
            
            return pstmt.executeUpdate(); // 수정된 행의 개수 반환
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 경력 내역 삭제
    // 기본키를 기준으로 해당 데이터 삭제
    public int delete(Connection conn, int carId) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM EMP_CAREER WHERE CAR_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, carId);
            
            return pstmt.executeUpdate(); // 삭제된 행의 개수 반환
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // ResultSet 데이터를 EmpCareer 객체로 변환
    // 코드 중복 방지를 위한 공통 매핑 처리
    private EmpCareer makeEmpCareerFromResultSet(ResultSet rs) throws SQLException {
        EmpCareer career = new EmpCareer();
        career.setCarId(rs.getInt("CAR_ID"));
        career.setEmpId(rs.getInt("EMP_ID"));
        career.setCompanyName(rs.getString("COMPANY_NAME"));
        
        // 데이터베이스의 Date 값이 null일 경우를 대비한 안전한 변환 처리
        Timestamp joinTs = rs.getTimestamp("JOIN_DATE");
        if (joinTs != null) {
            career.setJoinDate(new java.util.Date(joinTs.getTime()));
        }
        
        Timestamp quitTs = rs.getTimestamp("QUIT_DATE");
        if (quitTs != null) {
            career.setQuitDate(new java.util.Date(quitTs.getTime()));
        }
        
        career.setFinalPosition(rs.getString("FINAL_POSITION"));
        career.setDuty(rs.getString("DUTY"));
        career.setQuitReason(rs.getString("QUIT_REASON"));
        
        return career;
    }
}