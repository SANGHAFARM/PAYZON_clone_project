package erp.payroll.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import erp.payroll.model.PayrollEmployee;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 사원별 급여결과를 저장하고 조회한다.
public class PayrollEmployeeDao {

    // 싱글톤 인스턴스 생성
    private static PayrollEmployeeDao payrollEmployeeDao = new PayrollEmployeeDao();

    // 싱글톤 접근 메서드
    public static PayrollEmployeeDao getInstance() {
        return payrollEmployeeDao;
    }

    // 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
    private PayrollEmployeeDao() {}

    // 사원별 급여결과 등록
    public void insert(Connection conn, PayrollEmployee pe) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO PAYROLL_EMPLOYEE (PAYROLL_EMPLOYEE_ID, PAYROLL_RUN_ID, EMPLOYEE_ID) "
                       + "VALUES (PAYROLL_EMPLOYEE_SEQ.NEXTVAL, ?, ?)";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, pe.getPayrollRunId());
            pstmt.setInt(2, pe.getEmployeeId());
            
            pstmt.executeUpdate();
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 사원별 급여결과 단건 조회
    public PayrollEmployee selectById(Connection conn, int peId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT PAYROLL_EMPLOYEE_ID, PAYROLL_RUN_ID, EMPLOYEE_ID "
                       + "FROM PAYROLL_EMPLOYEE WHERE PAYROLL_EMPLOYEE_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, peId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return makePayrollEmployeeFromResultSet(rs);
            }
            return null;
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 특정 회차의 대상 사원 목록 조회
    public List<PayrollEmployee> selectByRunId(Connection conn, int runId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT PAYROLL_EMPLOYEE_ID, PAYROLL_RUN_ID, EMPLOYEE_ID "
                       + "FROM PAYROLL_EMPLOYEE WHERE PAYROLL_RUN_ID = ? ORDER BY EMPLOYEE_ID ASC";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, runId);
            rs = pstmt.executeQuery();
            
            List<PayrollEmployee> result = new ArrayList<>();
            while (rs.next()) {
                result.add(makePayrollEmployeeFromResultSet(rs));
            }
            return result;
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 사원별 급여결과 수정
    public int update(Connection conn, PayrollEmployee pe) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE PAYROLL_EMPLOYEE SET PAYROLL_RUN_ID = ?, EMPLOYEE_ID = ? "
                       + "WHERE PAYROLL_EMPLOYEE_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, pe.getPayrollRunId());
            pstmt.setInt(2, pe.getEmployeeId());
            pstmt.setInt(3, pe.getPayrollEmployeeId());
            
            return pstmt.executeUpdate();
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 사원별 급여결과 삭제
    public int delete(Connection conn, int peId) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM PAYROLL_EMPLOYEE WHERE PAYROLL_EMPLOYEE_ID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, peId);
            return pstmt.executeUpdate();
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 사원 삭제 시 해당 사원에게 연결된 급여결과를 함께 삭제
    public int deleteByEmployeeId(Connection conn, int employeeId) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM PAYROLL_EMPLOYEE WHERE EMPLOYEE_ID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, employeeId);
            return pstmt.executeUpdate();
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // ResultSet 데이터를 PayrollEmployee 객체로 변환
    private PayrollEmployee makePayrollEmployeeFromResultSet(ResultSet rs) throws SQLException {
        PayrollEmployee pe = new PayrollEmployee();
        
        pe.setPayrollEmployeeId(rs.getInt("PAYROLL_EMPLOYEE_ID"));
        pe.setPayrollRunId(rs.getInt("PAYROLL_RUN_ID"));
        pe.setEmployeeId(rs.getInt("EMPLOYEE_ID"));
        
        return pe;
    }
}
