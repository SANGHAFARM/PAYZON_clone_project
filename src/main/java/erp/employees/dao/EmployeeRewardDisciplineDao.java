package erp.employees.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.employees.model.EmployeeRewardDiscipline;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 상벌 내역 데이터베이스 접근(DAO) 클래스
public class EmployeeRewardDisciplineDao {

    // 싱글톤 인스턴스 생성
    private static EmployeeRewardDisciplineDao employeeRewardDisciplineDao = new EmployeeRewardDisciplineDao();

    // 싱글톤 접근 메서드
    public static EmployeeRewardDisciplineDao getInstance() {
        return employeeRewardDisciplineDao;
    }

    // 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
    private EmployeeRewardDisciplineDao() {}

    // 상벌 내역 등록 (INSERT)
    // 시퀀스를 사용하여 기본키 발급 및 데이터 저장
    public void insert(Connection conn, EmployeeRewardDiscipline rp) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO EMPLOYEE_REWARD_DISCIPLINE "
                       + "(EMPLOYEE_REWARD_DISCIPLINE_ID, EMPLOYEE_ID, RP_TYPE, RP_NAME, RP_AUTHORITY, RP_DATE, RP_CONTENT, NOTE) "
                       + "VALUES (SEQ_EMP_RP_ID.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, rp.getEmployeeId());
            pstmt.setString(2, rp.getRpType());
            pstmt.setString(3, rp.getRpName());
            pstmt.setString(4, rp.getRpAuthority());
            
            // 상벌 일자가 null일 경우를 대비한 방어 로직 적용
            if (rp.getRpDate() == null) {
                pstmt.setNull(5, Types.DATE);
            } else {
                pstmt.setTimestamp(5, new Timestamp(rp.getRpDate().getTime()));
            }
            
            pstmt.setString(6, rp.getRpContent());
            pstmt.setString(7, rp.getNote());
            
            pstmt.executeUpdate();
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 상벌 내역 단건 조회 (SELECT BY ID)
    // 기본키(EMPLOYEE_REWARD_DISCIPLINE_ID)를 기준으로 1건의 데이터 조회
    public EmployeeRewardDiscipline selectById(Connection conn, int rpId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT EMPLOYEE_REWARD_DISCIPLINE_ID, EMPLOYEE_ID, RP_TYPE, RP_NAME, RP_AUTHORITY, RP_DATE, RP_CONTENT, NOTE "
                       + "FROM EMPLOYEE_REWARD_DISCIPLINE WHERE EMPLOYEE_REWARD_DISCIPLINE_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, rpId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return makeEmpRewardPunishFromResultSet(rs);
            }
            return null; // 조회된 데이터가 없을 경우 null 반환
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 특정 사원의 상벌 내역 목록 조회
    // 사원번호(EMPLOYEE_ID)를 기준으로 연관된 상벌 내역 전체 반환 (최신순 정렬)
    public List<EmployeeRewardDiscipline> selectByEmpId(Connection conn, int empId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT EMPLOYEE_REWARD_DISCIPLINE_ID, EMPLOYEE_ID, RP_TYPE, RP_NAME, RP_AUTHORITY, RP_DATE, RP_CONTENT, NOTE "
                       + "FROM EMPLOYEE_REWARD_DISCIPLINE WHERE EMPLOYEE_ID = ? ORDER BY RP_DATE DESC, EMPLOYEE_REWARD_DISCIPLINE_ID DESC";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, empId);
            rs = pstmt.executeQuery();
            
            List<EmployeeRewardDiscipline> result = new ArrayList<>();
            while (rs.next()) {
                result.add(makeEmpRewardPunishFromResultSet(rs));
            }
            return result;
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 상벌 내역 수정 (UPDATE)
    // 기본키를 기준으로 상벌 세부 내용 데이터 수정
    public int update(Connection conn, EmployeeRewardDiscipline rp) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE EMPLOYEE_REWARD_DISCIPLINE SET "
                       + "EMPLOYEE_ID = ?, RP_TYPE = ?, RP_NAME = ?, RP_AUTHORITY = ?, RP_DATE = ?, RP_CONTENT = ?, NOTE = ? "
                       + "WHERE EMPLOYEE_REWARD_DISCIPLINE_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, rp.getEmployeeId());
            pstmt.setString(2, rp.getRpType());
            pstmt.setString(3, rp.getRpName());
            pstmt.setString(4, rp.getRpAuthority());
            
            // 날짜 null 방어 로직 적용
            if (rp.getRpDate() == null) {
                pstmt.setNull(5, Types.DATE);
            } else {
                pstmt.setTimestamp(5, new Timestamp(rp.getRpDate().getTime()));
            }
            
            pstmt.setString(6, rp.getRpContent());
            pstmt.setString(7, rp.getNote());
            pstmt.setLong(8, rp.getEmployeeRewardDisciplineId());
            
            return pstmt.executeUpdate(); // 수정된 행의 개수 반환
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 상벌 내역 삭제 (DELETE)
    // 기본키를 기준으로 해당 데이터 삭제
    public int delete(Connection conn, int rpId) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM EMPLOYEE_REWARD_DISCIPLINE WHERE EMPLOYEE_REWARD_DISCIPLINE_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, rpId);
            
            return pstmt.executeUpdate(); // 삭제된 행의 개수 반환
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // ResultSet 데이터를 EmployeeRewardDiscipline 객체로 변환
    // 코드 중복 방지를 위한 공통 매핑 처리
    private EmployeeRewardDiscipline makeEmpRewardPunishFromResultSet(ResultSet rs) throws SQLException {
        EmployeeRewardDiscipline rp = new EmployeeRewardDiscipline();
        rp.setEmployeeRewardDisciplineId(rs.getLong("EMPLOYEE_REWARD_DISCIPLINE_ID"));
        rp.setEmployeeId(rs.getLong("EMPLOYEE_ID"));
        rp.setRpType(rs.getString("RP_TYPE"));
        rp.setRpName(rs.getString("RP_NAME"));
        rp.setRpAuthority(rs.getString("RP_AUTHORITY"));
        
        // 데이터베이스의 Date 값이 null일 경우를 대비한 안전한 변환 처리
        Timestamp rpTs = rs.getTimestamp("RP_DATE");
        if (rpTs != null) {
            rp.setRpDate(new java.util.Date(rpTs.getTime()));
        }
        
        rp.setRpContent(rs.getString("RP_CONTENT"));
        rp.setNote(rs.getString("NOTE"));
        
        return rp;
    }
}
