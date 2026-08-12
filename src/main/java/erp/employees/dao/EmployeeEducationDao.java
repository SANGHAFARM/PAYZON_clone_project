package erp.employees.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import erp.employees.model.EmployeeEducation;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 학력 내역 데이터베이스 접근(DAO) 클래스
public class EmployeeEducationDao {

    // 싱글톤 인스턴스 생성
    private static EmployeeEducationDao employeeEducationDao = new EmployeeEducationDao();

    // 싱글톤 접근 메서드
    public static EmployeeEducationDao getInstance() {
        return employeeEducationDao;
    }

    // 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
    private EmployeeEducationDao() {}

    // 학력 내역 등록
    // 시퀀스를 사용하여 기본키 발급 및 학력 데이터 저장
    public void insert(Connection conn, EmployeeEducation edu) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO EMPLOYEE_EDUCATION "
                       + "(EMPLOYEE_EDUCATION_ID, EMPLOYEE_ID, EDU_TYPE, ADMISSION_YM, GRAD_YM, SCHOOL_NAME, MAJOR_NAME, COMPLETE_TYPE) "
                       + "VALUES (SEQ_EMP_EDU_ID.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, edu.getEmployeeId());
            pstmt.setString(2, edu.getEduType());
            pstmt.setString(3, edu.getAdmissionYm());
            pstmt.setString(4, edu.getGradYm());
            pstmt.setString(5, edu.getSchoolName());
            pstmt.setString(6, edu.getMajorName());
            pstmt.setString(7, edu.getCompleteType());
            
            pstmt.executeUpdate();
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 학력 내역 단건 조회
    // 기본키(EMPLOYEE_EDUCATION_ID)를 기준으로 1건의 데이터 반환
    public EmployeeEducation selectById(Connection conn, int eduId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT EMPLOYEE_EDUCATION_ID, EMPLOYEE_ID, EDU_TYPE, ADMISSION_YM, GRAD_YM, SCHOOL_NAME, MAJOR_NAME, COMPLETE_TYPE "
                       + "FROM EMPLOYEE_EDUCATION WHERE EMPLOYEE_EDUCATION_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, eduId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return makeEmpEducationFromResultSet(rs);
            }
            return null; // 조회된 데이터가 없을 경우 null 반환
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 특정 사원의 학력 내역 목록 조회
    // 사원번호(EMPLOYEE_ID)를 기준으로 연관된 학력 내역 전체 반환 (입학년월 기준 내림차순 정렬)
    public List<EmployeeEducation> selectByEmpId(Connection conn, int empId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT EMPLOYEE_EDUCATION_ID, EMPLOYEE_ID, EDU_TYPE, ADMISSION_YM, GRAD_YM, SCHOOL_NAME, MAJOR_NAME, COMPLETE_TYPE "
                       + "FROM EMPLOYEE_EDUCATION WHERE EMPLOYEE_ID = ? ORDER BY ADMISSION_YM DESC NULLS LAST, EMPLOYEE_EDUCATION_ID DESC";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, empId);
            rs = pstmt.executeQuery();
            
            List<EmployeeEducation> result = new ArrayList<>();
            while (rs.next()) {
                result.add(makeEmpEducationFromResultSet(rs));
            }
            return result;
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 학력 내역 수정
    // 기본키를 기준으로 학교명, 전공 및 기간 데이터 수정
    public int update(Connection conn, EmployeeEducation edu) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE EMPLOYEE_EDUCATION SET "
                       + "EMPLOYEE_ID = ?, EDU_TYPE = ?, ADMISSION_YM = ?, GRAD_YM = ?, SCHOOL_NAME = ?, MAJOR_NAME = ?, COMPLETE_TYPE = ? "
                       + "WHERE EMPLOYEE_EDUCATION_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, edu.getEmployeeId());
            pstmt.setString(2, edu.getEduType());
            pstmt.setString(3, edu.getAdmissionYm());
            pstmt.setString(4, edu.getGradYm());
            pstmt.setString(5, edu.getSchoolName());
            pstmt.setString(6, edu.getMajorName());
            pstmt.setString(7, edu.getCompleteType());
            pstmt.setInt(8, edu.getEmployeeEducationId());
            
            return pstmt.executeUpdate(); // 수정된 행의 개수 반환
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 학력 내역 삭제
    // 기본키를 기준으로 해당 데이터 삭제
    public int delete(Connection conn, int eduId) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM EMPLOYEE_EDUCATION WHERE EMPLOYEE_EDUCATION_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, eduId);
            
            return pstmt.executeUpdate(); // 삭제된 행의 개수 반환
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // ResultSet 데이터를 EmployeeEducation 객체로 변환
    // 코드 중복 방지를 위한 공통 매핑 처리
    private EmployeeEducation makeEmpEducationFromResultSet(ResultSet rs) throws SQLException {
        EmployeeEducation edu = new EmployeeEducation();
        edu.setEmployeeEducationId(rs.getInt("EMPLOYEE_EDUCATION_ID"));
        edu.setEmployeeId(rs.getInt("EMPLOYEE_ID"));
        edu.setEduType(rs.getString("EDU_TYPE"));
        edu.setAdmissionYm(rs.getString("ADMISSION_YM"));
        edu.setGradYm(rs.getString("GRAD_YM"));
        edu.setSchoolName(rs.getString("SCHOOL_NAME"));
        edu.setMajorName(rs.getString("MAJOR_NAME"));
        edu.setCompleteType(rs.getString("COMPLETE_TYPE"));
        return edu;
    }
}
