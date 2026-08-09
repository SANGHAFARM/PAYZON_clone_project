package erp.retire.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.retire.model.RetireCalcSalary;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 퇴직급여 산정자료 데이터베이스 접근(DAO) 클래스
public class RetireCalcSalaryDao {

    // 퇴직급여 산정자료 등록 (INSERT)
    // 시퀀스를 사용하여 PK 발급 및 데이터 저장
    public void insert(Connection conn, RetireCalcSalary salary) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO RETIRE_CALC_SALARY ("
                       + "RETIRE_CALC_SAL_ID, RETIRE_CALC_MST_ID, DATA_TYPE, PERIOD_START_DATE, "
                       + "PERIOD_END_DATE, CALC_DAYS, PAY_YM, ITEM_NAME, AMOUNT, THREE_MONTH_AMOUNT) "
                       + "VALUES (SEQ_RETIRE_CALC_SAL_ID.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, salary.getRetireCalcMstId());
            pstmt.setString(2, salary.getDataType());
            
            // 산정자료 구분에 따라 날짜 값이 null일 수 있으므로 null 방어 로직 적용
            if (salary.getPeriodStartDate() == null) {
                pstmt.setNull(3, Types.DATE);
            } else {
                pstmt.setTimestamp(3, new Timestamp(salary.getPeriodStartDate().getTime()));
            }
            
            if (salary.getPeriodEndDate() == null) {
                pstmt.setNull(4, Types.DATE);
            } else {
                pstmt.setTimestamp(4, new Timestamp(salary.getPeriodEndDate().getTime()));
            }
            
            pstmt.setDouble(5, salary.getCalcDays());
            pstmt.setString(6, salary.getPayYm());
            pstmt.setString(7, salary.getItemName());
            pstmt.setLong(8, salary.getAmount());
            pstmt.setLong(9, salary.getThreeMonthAmount());
            
            pstmt.executeUpdate();
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 퇴직급여 산정자료 단건 조회 (SELECT BY ID)
    // 기본키(RETIRE_CALC_SAL_ID)를 기준으로 1건의 데이터 조회
    public RetireCalcSalary selectById(Connection conn, int retireCalcSalId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT RETIRE_CALC_SAL_ID, RETIRE_CALC_MST_ID, DATA_TYPE, PERIOD_START_DATE, "
                       + "PERIOD_END_DATE, CALC_DAYS, PAY_YM, ITEM_NAME, AMOUNT, THREE_MONTH_AMOUNT "
                       + "FROM RETIRE_CALC_SALARY WHERE RETIRE_CALC_SAL_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, retireCalcSalId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return makeRetireCalcSalaryFromResultSet(rs);
            }
            return null; // 조회된 데이터가 없을 경우 null 반환
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 특정 퇴직급여 계산에 속한 산정자료 목록 조회
    // 계산 마스터 기본키(RETIRE_CALC_MST_ID)를 기준으로 연관된 산정자료 전체 조회
    public List<RetireCalcSalary> selectByRetireCalcMstId(Connection conn, int retireCalcMstId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT RETIRE_CALC_SAL_ID, RETIRE_CALC_MST_ID, DATA_TYPE, PERIOD_START_DATE, "
                       + "PERIOD_END_DATE, CALC_DAYS, PAY_YM, ITEM_NAME, AMOUNT, THREE_MONTH_AMOUNT "
                       + "FROM RETIRE_CALC_SALARY WHERE RETIRE_CALC_MST_ID = ? ORDER BY RETIRE_CALC_SAL_ID ASC";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, retireCalcMstId);
            rs = pstmt.executeQuery();
            
            List<RetireCalcSalary> result = new ArrayList<>();
            while (rs.next()) {
                result.add(makeRetireCalcSalaryFromResultSet(rs));
            }
            return result;
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 퇴직급여 산정자료 수정 (UPDATE)
    // 기본키를 기준으로 산정 기간 및 금액 데이터 수정
    public int update(Connection conn, RetireCalcSalary salary) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE RETIRE_CALC_SALARY SET "
                       + "RETIRE_CALC_MST_ID = ?, DATA_TYPE = ?, PERIOD_START_DATE = ?, PERIOD_END_DATE = ?, "
                       + "CALC_DAYS = ?, PAY_YM = ?, ITEM_NAME = ?, AMOUNT = ?, THREE_MONTH_AMOUNT = ? "
                       + "WHERE RETIRE_CALC_SAL_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, salary.getRetireCalcMstId());
            pstmt.setString(2, salary.getDataType());
            
            // 날짜 null 방어 로직 적용
            if (salary.getPeriodStartDate() == null) {
                pstmt.setNull(3, Types.DATE);
            } else {
                pstmt.setTimestamp(3, new Timestamp(salary.getPeriodStartDate().getTime()));
            }
            
            if (salary.getPeriodEndDate() == null) {
                pstmt.setNull(4, Types.DATE);
            } else {
                pstmt.setTimestamp(4, new Timestamp(salary.getPeriodEndDate().getTime()));
            }
            
            pstmt.setDouble(5, salary.getCalcDays());
            pstmt.setString(6, salary.getPayYm());
            pstmt.setString(7, salary.getItemName());
            pstmt.setLong(8, salary.getAmount());
            pstmt.setLong(9, salary.getThreeMonthAmount());
            pstmt.setInt(10, salary.getRetireCalcSalId());
            
            return pstmt.executeUpdate(); // 수정된 행의 개수 반환
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 퇴직급여 산정자료 삭제 (DELETE)
    // 기본키를 기준으로 데이터 삭제
    public int delete(Connection conn, int retireCalcSalId) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM RETIRE_CALC_SALARY WHERE RETIRE_CALC_SAL_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, retireCalcSalId);
            
            return pstmt.executeUpdate(); // 삭제된 행의 개수 반환
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // ResultSet 데이터를 RetireCalcSalary 객체로 변환
    // 코드 중복 방지를 위한 공통 매핑 처리
    private RetireCalcSalary makeRetireCalcSalaryFromResultSet(ResultSet rs) throws SQLException {
        RetireCalcSalary salary = new RetireCalcSalary();
        salary.setRetireCalcSalId(rs.getInt("RETIRE_CALC_SAL_ID"));
        salary.setRetireCalcMstId(rs.getInt("RETIRE_CALC_MST_ID"));
        salary.setDataType(rs.getString("DATA_TYPE"));
        
        // 데이터베이스의 Date/Timestamp 값이 null일 경우를 대비한 안전한 변환 처리
        Timestamp startTs = rs.getTimestamp("PERIOD_START_DATE");
        if (startTs != null) {
            salary.setPeriodStartDate(new java.util.Date(startTs.getTime()));
        }
        
        Timestamp endTs = rs.getTimestamp("PERIOD_END_DATE");
        if (endTs != null) {
            salary.setPeriodEndDate(new java.util.Date(endTs.getTime()));
        }
        
        salary.setCalcDays(rs.getDouble("CALC_DAYS"));
        salary.setPayYm(rs.getString("PAY_YM"));
        salary.setItemName(rs.getString("ITEM_NAME"));
        salary.setAmount(rs.getLong("AMOUNT"));
        salary.setThreeMonthAmount(rs.getLong("THREE_MONTH_AMOUNT"));
        
        return salary;
    }
}