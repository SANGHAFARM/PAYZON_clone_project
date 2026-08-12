package erp.employees.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import erp.employees.model.EmployeeRecommender;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 추천인 내역 데이터베이스 접근(DAO) 클래스
public class EmployeeRecommenderDao {

    // 싱글톤 인스턴스 생성
    private static EmployeeRecommenderDao employeeRecommenderDao = new EmployeeRecommenderDao();

    // 싱글톤 접근 메서드
    public static EmployeeRecommenderDao getInstance() {
        return employeeRecommenderDao;
    }

    // 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
    private EmployeeRecommenderDao() {}

    // 추천인 내역 등록 (INSERT)
    // 시퀀스를 사용하여 기본키 발급 및 데이터 저장
    public void insert(Connection conn, EmployeeRecommender recommender) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO EMPLOYEE_RECOMMENDER "
                       + "(EMPLOYEE_RECOMMENDER_ID, EMPLOYEE_ID, RECOMMENDER_NAME, RELATION, COMPANY_NAME, POSITION_NAME, TEL_NO) "
                       + "VALUES (SEQ_EMP_RECOMMENDER_ID.NEXTVAL, ?, ?, ?, ?, ?, ?)";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, recommender.getEmployeeId());
            pstmt.setString(2, recommender.getRecommenderName());
            pstmt.setString(3, recommender.getRelation());
            pstmt.setString(4, recommender.getCompanyName());
            pstmt.setString(5, recommender.getPositionName());
            pstmt.setString(6, recommender.getTelNo());
            
            pstmt.executeUpdate();
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 추천인 내역 단건 조회 (SELECT BY ID)
    // 기본키(EMPLOYEE_RECOMMENDER_ID)를 기준으로 1건의 데이터 조회
    public EmployeeRecommender selectById(Connection conn, int recommenderId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT EMPLOYEE_RECOMMENDER_ID, EMPLOYEE_ID, RECOMMENDER_NAME, RELATION, COMPANY_NAME, POSITION_NAME, TEL_NO "
                       + "FROM EMPLOYEE_RECOMMENDER WHERE EMPLOYEE_RECOMMENDER_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, recommenderId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return makeEmpRecommenderFromResultSet(rs);
            }
            return null; // 조회된 데이터가 없을 경우 null 반환
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 특정 사원의 추천인 내역 목록 조회
    // 사원번호(EMPLOYEE_ID)를 기준으로 연관된 추천인 전체 목록 반환
    public List<EmployeeRecommender> selectByEmpId(Connection conn, int empId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT EMPLOYEE_RECOMMENDER_ID, EMPLOYEE_ID, RECOMMENDER_NAME, RELATION, COMPANY_NAME, POSITION_NAME, TEL_NO "
                       + "FROM EMPLOYEE_RECOMMENDER WHERE EMPLOYEE_ID = ? ORDER BY EMPLOYEE_RECOMMENDER_ID ASC";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, empId);
            rs = pstmt.executeQuery();
            
            List<EmployeeRecommender> result = new ArrayList<>();
            while (rs.next()) {
                result.add(makeEmpRecommenderFromResultSet(rs));
            }
            return result;
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 추천인 내역 수정 (UPDATE)
    // 기본키를 기준으로 인적사항 및 연락처 데이터 수정
    public int update(Connection conn, EmployeeRecommender recommender) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE EMPLOYEE_RECOMMENDER SET "
                       + "EMPLOYEE_ID = ?, RECOMMENDER_NAME = ?, RELATION = ?, COMPANY_NAME = ?, POSITION_NAME = ?, TEL_NO = ? "
                       + "WHERE EMPLOYEE_RECOMMENDER_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, recommender.getEmployeeId());
            pstmt.setString(2, recommender.getRecommenderName());
            pstmt.setString(3, recommender.getRelation());
            pstmt.setString(4, recommender.getCompanyName());
            pstmt.setString(5, recommender.getPositionName());
            pstmt.setString(6, recommender.getTelNo());
            pstmt.setLong(7, recommender.getEmployeeRecommenderId());
            
            return pstmt.executeUpdate(); // 수정된 행의 개수 반환
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 추천인 내역 삭제 (DELETE)
    // 기본키를 기준으로 해당 데이터 삭제
    public int delete(Connection conn, int recommenderId) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM EMPLOYEE_RECOMMENDER WHERE EMPLOYEE_RECOMMENDER_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, recommenderId);
            
            return pstmt.executeUpdate(); // 삭제된 행의 개수 반환
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // ResultSet 데이터를 EmployeeRecommender 객체로 변환
    // 코드 중복 방지를 위한 공통 매핑 처리
    private EmployeeRecommender makeEmpRecommenderFromResultSet(ResultSet rs) throws SQLException {
        EmployeeRecommender recommender = new EmployeeRecommender();
        recommender.setEmployeeRecommenderId(rs.getLong("EMPLOYEE_RECOMMENDER_ID"));
        recommender.setEmployeeId(rs.getLong("EMPLOYEE_ID"));
        recommender.setRecommenderName(rs.getString("RECOMMENDER_NAME"));
        recommender.setRelation(rs.getString("RELATION"));
        recommender.setCompanyName(rs.getString("COMPANY_NAME"));
        recommender.setPositionName(rs.getString("POSITION_NAME"));
        recommender.setTelNo(rs.getString("TEL_NO"));
        return recommender;
    }
}
