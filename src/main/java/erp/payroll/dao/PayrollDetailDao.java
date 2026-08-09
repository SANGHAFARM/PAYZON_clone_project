package erp.payroll.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.payroll.model.PayrollDetail;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 사원별 지급·공제 상세 데이터베이스 접근(DAO) 클래스
public class PayrollDetailDao {

    // 사원별 지급·공제 상세 등록 (INSERT)
    // SEQ_PAYROLL_DTL_ID 시퀀스를 사용하여 PK 발급 및 데이터 저장
    public void insert(Connection conn, PayrollDetail detail) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO PAYROLL_DETAIL "
                       + "(PAYROLL_DTL_ID, PAYROLL_EMP_ID, PAY_ITEM_ID, DEDUCT_ITEM_ID, AMOUNT) "
                       + "VALUES (SEQ_PAYROLL_DTL_ID.NEXTVAL, ?, ?, ?, ?)";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, detail.getPayrollEmpId());
            
            // Integer 타입인 지급항목 ID의 null 값 처리
            if (detail.getPayItemId() == null) {
                pstmt.setNull(2, Types.INTEGER);
            } else {
                pstmt.setInt(2, detail.getPayItemId());
            }
            
            // Integer 타입인 공제항목 ID의 null 값 처리
            if (detail.getDeductItemId() == null) {
                pstmt.setNull(3, Types.INTEGER);
            } else {
                pstmt.setInt(3, detail.getDeductItemId());
            }
            
            pstmt.setLong(4, detail.getAmount());
            
            pstmt.executeUpdate();
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 사원별 지급·공제 상세 단건 조회 (SELECT BY ID)
    // 기본키(PAYROLL_DTL_ID)를 기준으로 1건의 데이터 조회
    public PayrollDetail selectById(Connection conn, int payrollDtlId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT PAYROLL_DTL_ID, PAYROLL_EMP_ID, PAY_ITEM_ID, DEDUCT_ITEM_ID, AMOUNT "
                       + "FROM PAYROLL_DETAIL WHERE PAYROLL_DTL_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, payrollDtlId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return makePayrollDetailFromResultSet(rs);
            }
            return null; // 조회된 데이터가 없을 경우 null 반환
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 특정 사원의 급여결과(PAYROLL_EMP_ID)에 속한 상세 목록 조회
    // 사원별 급여결과 기본키를 기준으로 연관된 상세 내역 전체 조회
    public List<PayrollDetail> selectByPayrollEmpId(Connection conn, int payrollEmpId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT PAYROLL_DTL_ID, PAYROLL_EMP_ID, PAY_ITEM_ID, DEDUCT_ITEM_ID, AMOUNT "
                       + "FROM PAYROLL_DETAIL WHERE PAYROLL_EMP_ID = ? ORDER BY PAYROLL_DTL_ID ASC";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, payrollEmpId);
            rs = pstmt.executeQuery();
            
            List<PayrollDetail> result = new ArrayList<>();
            while (rs.next()) {
                result.add(makePayrollDetailFromResultSet(rs));
            }
            return result;
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 사원별 지급·공제 상세 수정 (UPDATE)
    // 기본키를 기준으로 항목의 금액 및 설정값 수정
    public int update(Connection conn, PayrollDetail detail) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE PAYROLL_DETAIL SET "
                       + "PAYROLL_EMP_ID = ?, PAY_ITEM_ID = ?, DEDUCT_ITEM_ID = ?, AMOUNT = ? "
                       + "WHERE PAYROLL_DTL_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, detail.getPayrollEmpId());
            
            // Integer 타입의 null 값 방어 로직 적용
            if (detail.getPayItemId() == null) {
                pstmt.setNull(2, Types.INTEGER);
            } else {
                pstmt.setInt(2, detail.getPayItemId());
            }
            
            if (detail.getDeductItemId() == null) {
                pstmt.setNull(3, Types.INTEGER);
            } else {
                pstmt.setInt(3, detail.getDeductItemId());
            }
            
            pstmt.setLong(4, detail.getAmount());
            pstmt.setInt(5, detail.getPayrollDtlId());
            
            return pstmt.executeUpdate(); // 수정된 행의 개수 반환
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 사원별 지급·공제 상세 삭제 (DELETE)
    // 기본키를 기준으로 데이터 삭제
    public int delete(Connection conn, int payrollDtlId) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM PAYROLL_DETAIL WHERE PAYROLL_DTL_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, payrollDtlId);
            
            return pstmt.executeUpdate(); // 삭제된 행의 개수 반환
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // ResultSet 데이터를 PayrollDetail 객체로 변환
    // 코드 중복 방지를 위한 ResultSet 매핑 공통 메서드 분리
    private PayrollDetail makePayrollDetailFromResultSet(ResultSet rs) throws SQLException {
        PayrollDetail detail = new PayrollDetail();
        detail.setPayrollDtlId(rs.getInt("PAYROLL_DTL_ID"));
        detail.setPayrollEmpId(rs.getInt("PAYROLL_EMP_ID"));
        
        // 데이터베이스의 숫자 컬럼이 null일 경우 자바의 0으로 자동 변환되는 현상 방지
        int payItemId = rs.getInt("PAY_ITEM_ID");
        if (!rs.wasNull()) {
            detail.setPayItemId(payItemId);
        }
        
        int deductItemId = rs.getInt("DEDUCT_ITEM_ID");
        if (!rs.wasNull()) {
            detail.setDeductItemId(deductItemId);
        }
        
        detail.setAmount(rs.getLong("AMOUNT"));
        
        return detail;
    }
}