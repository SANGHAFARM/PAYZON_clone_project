package erp.payroll.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import erp.payroll.model.PayrollEmp;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 급여회차별 사원 급여 결과 데이터베이스 접근(DAO) 클래스
public class PayrollEmpDao {

    // 사원별 급여 결과 등록 (INSERT)
    // 시퀀스를 사용하여 PK 발급 및 데이터 저장
    public void insert(Connection conn, PayrollEmp payrollEmp) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO PAYROLL_EMP (PAYROLL_EMP_ID, PAYROLL_MST_ID, EMP_ID) "
                       + "VALUES (SEQ_PAYROLL_EMP_ID.NEXTVAL, ?, ?)";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, payrollEmp.getPayrollMstId());
            pstmt.setInt(2, payrollEmp.getEmpId());
            
            pstmt.executeUpdate();
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 사원별 급여 결과 단건 조회 (SELECT BY ID)
    // 기본키(PAYROLL_EMP_ID)를 기준으로 1건의 데이터 조회
    public PayrollEmp selectById(Connection conn, int payrollEmpId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT PAYROLL_EMP_ID, PAYROLL_MST_ID, EMP_ID "
                       + "FROM PAYROLL_EMP WHERE PAYROLL_EMP_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, payrollEmpId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return makePayrollEmpFromResultSet(rs);
            }
            return null;
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 특정 급여회차에 속한 대상 사원 목록 조회
    // 급여회차 기본키(PAYROLL_MST_ID)를 기준으로 연관된 사원 목록 전체 조회
    public List<PayrollEmp> selectByPayrollMstId(Connection conn, int payrollMstId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT PAYROLL_EMP_ID, PAYROLL_MST_ID, EMP_ID "
                       + "FROM PAYROLL_EMP WHERE PAYROLL_MST_ID = ? ORDER BY PAYROLL_EMP_ID ASC";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, payrollMstId);
            rs = pstmt.executeQuery();
            
            List<PayrollEmp> result = new ArrayList<>();
            while (rs.next()) {
                result.add(makePayrollEmpFromResultSet(rs));
            }
            return result;
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 사원별 급여 결과 수정 (UPDATE)
    // 기본키를 기준으로 데이터 수정
    public int update(Connection conn, PayrollEmp payrollEmp) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE PAYROLL_EMP SET "
                       + "PAYROLL_MST_ID = ?, EMP_ID = ? "
                       + "WHERE PAYROLL_EMP_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, payrollEmp.getPayrollMstId());
            pstmt.setInt(2, payrollEmp.getEmpId());
            pstmt.setInt(3, payrollEmp.getPayrollEmpId());
            
            return pstmt.executeUpdate();
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 사원별 급여 결과 삭제 (DELETE)
    // 기본키를 기준으로 데이터 삭제
    public int delete(Connection conn, int payrollEmpId) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM PAYROLL_EMP WHERE PAYROLL_EMP_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, payrollEmpId);
            
            return pstmt.executeUpdate();
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // ResultSet 데이터를 PayrollEmp 객체로 변환
    // 코드 중복 방지를 위한 ResultSet 매핑 공통 메서드 분리
    private PayrollEmp makePayrollEmpFromResultSet(ResultSet rs) throws SQLException {
        PayrollEmp pe = new PayrollEmp();
        pe.setPayrollEmpId(rs.getInt("PAYROLL_EMP_ID"));
        pe.setPayrollMstId(rs.getInt("PAYROLL_MST_ID"));
        pe.setEmpId(rs.getInt("EMP_ID"));
        return pe;
    }
}