package erp.hr.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import erp.hr.model.EmpEducation;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 학력 내역 데이터베이스 접근(DAO) 클래스
public class EmpEducationDao {

    // 싱글톤 인스턴스 생성
    private static EmpEducationDao empEducationDao = new EmpEducationDao();

    // 싱글톤 접근 메서드
    public static EmpEducationDao getInstance() {
        return empEducationDao;
    }

    // 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
    private EmpEducationDao() {}

    // 학력 내역 등록
    // 시퀀스를 사용하여 기본키 발급 및 학력 데이터 저장
    public void insert(Connection conn, EmpEducation edu) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO EMP_EDUCATION "
                       + "(EDU_ID, EMP_ID, EDU_TYPE, ADMISSION_YM, GRAD_YM, SCHOOL_NAME, MAJOR_NAME, COMPLETE_TYPE) "
                       + "VALUES (SEQ_EMP_EDU_ID.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, edu.getEmpId());
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
    // 기본키(EDU_ID)를 기준으로 1건의 데이터 반환
    public EmpEducation selectById(Connection conn, int eduId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT EDU_ID, EMP_ID, EDU_TYPE, ADMISSION_YM, GRAD_YM, SCHOOL_NAME, MAJOR_NAME, COMPLETE_TYPE "
                       + "FROM EMP_EDUCATION WHERE EDU_ID = ?";
            
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
    // 사원번호(EMP_ID)를 기준으로 연관된 학력 내역 전체 반환 (입학년월 기준 내림차순 정렬)
    public List<EmpEducation> selectByEmpId(Connection conn, int empId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT EDU_ID, EMP_ID, EDU_TYPE, ADMISSION_YM, GRAD_YM, SCHOOL_NAME, MAJOR_NAME, COMPLETE_TYPE "
                       + "FROM EMP_EDUCATION WHERE EMP_ID = ? ORDER BY ADMISSION_YM DESC NULLS LAST, EDU_ID DESC";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, empId);
            rs = pstmt.executeQuery();
            
            List<EmpEducation> result = new ArrayList<>();
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
    public int update(Connection conn, EmpEducation edu) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE EMP_EDUCATION SET "
                       + "EMP_ID = ?, EDU_TYPE = ?, ADMISSION_YM = ?, GRAD_YM = ?, SCHOOL_NAME = ?, MAJOR_NAME = ?, COMPLETE_TYPE = ? "
                       + "WHERE EDU_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, edu.getEmpId());
            pstmt.setString(2, edu.getEduType());
            pstmt.setString(3, edu.getAdmissionYm());
            pstmt.setString(4, edu.getGradYm());
            pstmt.setString(5, edu.getSchoolName());
            pstmt.setString(6, edu.getMajorName());
            pstmt.setString(7, edu.getCompleteType());
            pstmt.setInt(8, edu.getEduId());
            
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
            String sql = "DELETE FROM EMP_EDUCATION WHERE EDU_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, eduId);
            
            return pstmt.executeUpdate(); // 삭제된 행의 개수 반환
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // ResultSet 데이터를 EmpEducation 객체로 변환
    // 코드 중복 방지를 위한 공통 매핑 처리
    private EmpEducation makeEmpEducationFromResultSet(ResultSet rs) throws SQLException {
        EmpEducation edu = new EmpEducation();
        edu.setEduId(rs.getInt("EDU_ID"));
        edu.setEmpId(rs.getInt("EMP_ID"));
        edu.setEduType(rs.getString("EDU_TYPE"));
        edu.setAdmissionYm(rs.getString("ADMISSION_YM"));
        edu.setGradYm(rs.getString("GRAD_YM"));
        edu.setSchoolName(rs.getString("SCHOOL_NAME"));
        edu.setMajorName(rs.getString("MAJOR_NAME"));
        edu.setCompleteType(rs.getString("COMPLETE_TYPE"));
        return edu;
    }
}