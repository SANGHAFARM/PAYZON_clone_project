package erp.retire.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.retire.model.RetireTaxDefer;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 퇴직소득세 과세이연 내역 데이터베이스 접근(DAO) 클래스
public class RetireTaxDeferDao {
	
	// 싱글톤 인스턴스 생성
	private static RetireTaxDeferDao retireTaxDeferDao = new RetireTaxDeferDao();
	
	// 싱글톤 접근 메서드
	public static RetireTaxDeferDao getInstance() {
		return retireTaxDeferDao;
	}
	
	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	private RetireTaxDeferDao() {}

    // 과세이연 내역 등록 (INSERT)
    // 시퀀스를 활용한 기본키 발급 및 데이터 저장
    public void insert(Connection conn, RetireTaxDefer defer) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO RETIRE_TAX_DEFER ("
                       + "RETIRE_TAX_DEF_ID, RETIRE_CALC_MST_ID, BIZ_NAME, BIZ_REG_NO, "
                       + "ACCOUNT_NO, DEPOSIT_DATE, DEPOSIT_AMT) "
                       + "VALUES (SEQ_RETIRE_TAX_DEF_ID.NEXTVAL, ?, ?, ?, ?, ?, ?)";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, defer.getRetireCalcMstId());
            pstmt.setString(2, defer.getBizName());
            pstmt.setString(3, defer.getBizRegNo());
            pstmt.setString(4, defer.getAccountNo());
            
            // 입금일이 셋팅되지 않은 경우(null)를 대비한 데이터베이스 null 처리 적용
            if (defer.getDepositDate() == null) {
                pstmt.setNull(5, Types.DATE);
            } else {
                pstmt.setTimestamp(5, new Timestamp(defer.getDepositDate().getTime()));
            }
            
            pstmt.setLong(6, defer.getDepositAmt());
            
            pstmt.executeUpdate();
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 과세이연 내역 단건 조회 (SELECT BY ID)
    // 기본키(RETIRE_TAX_DEF_ID)를 기준으로 1건의 데이터 조회
    public RetireTaxDefer selectById(Connection conn, int retireTaxDefId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT RETIRE_TAX_DEF_ID, RETIRE_CALC_MST_ID, BIZ_NAME, BIZ_REG_NO, "
                       + "ACCOUNT_NO, DEPOSIT_DATE, DEPOSIT_AMT "
                       + "FROM RETIRE_TAX_DEFER WHERE RETIRE_TAX_DEF_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, retireTaxDefId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return makeRetireTaxDeferFromResultSet(rs);
            }
            return null; // 조회된 데이터가 없을 경우 null 반환
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 특정 퇴직급여 계산에 속한 과세이연 내역 목록 조회
    // 계산 마스터 기본키(RETIRE_CALC_MST_ID)를 기준으로 연관된 이연 내역 전체 조회
    public List<RetireTaxDefer> selectByRetireCalcMstId(Connection conn, int retireCalcMstId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT RETIRE_TAX_DEF_ID, RETIRE_CALC_MST_ID, BIZ_NAME, BIZ_REG_NO, "
                       + "ACCOUNT_NO, DEPOSIT_DATE, DEPOSIT_AMT "
                       + "FROM RETIRE_TAX_DEFER WHERE RETIRE_CALC_MST_ID = ? ORDER BY RETIRE_TAX_DEF_ID ASC";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, retireCalcMstId);
            rs = pstmt.executeQuery();
            
            List<RetireTaxDefer> result = new ArrayList<>();
            while (rs.next()) {
                result.add(makeRetireTaxDeferFromResultSet(rs));
            }
            return result;
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 과세이연 내역 수정 (UPDATE)
    // 기본키를 기준으로 퇴직연금계좌 및 금액 정보 데이터 수정
    public int update(Connection conn, RetireTaxDefer defer) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE RETIRE_TAX_DEFER SET "
                       + "RETIRE_CALC_MST_ID = ?, BIZ_NAME = ?, BIZ_REG_NO = ?, "
                       + "ACCOUNT_NO = ?, DEPOSIT_DATE = ?, DEPOSIT_AMT = ? "
                       + "WHERE RETIRE_TAX_DEF_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, defer.getRetireCalcMstId());
            pstmt.setString(2, defer.getBizName());
            pstmt.setString(3, defer.getBizRegNo());
            pstmt.setString(4, defer.getAccountNo());
            
            // 날짜 null 방어 로직 적용
            if (defer.getDepositDate() == null) {
                pstmt.setNull(5, Types.DATE);
            } else {
                pstmt.setTimestamp(5, new Timestamp(defer.getDepositDate().getTime()));
            }
            
            pstmt.setLong(6, defer.getDepositAmt());
            pstmt.setInt(7, defer.getRetireTaxDefId());
            
            return pstmt.executeUpdate(); // 수정된 행의 개수 반환
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 과세이연 내역 삭제 (DELETE)
    // 기본키를 기준으로 해당 데이터 삭제
    public int delete(Connection conn, int retireTaxDefId) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM RETIRE_TAX_DEFER WHERE RETIRE_TAX_DEF_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, retireTaxDefId);
            
            return pstmt.executeUpdate(); // 삭제된 행의 개수 반환
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // ResultSet 데이터를 RetireTaxDefer 객체로 변환
    // 코드 중복 방지를 위한 공통 객체 매핑 처리
    private RetireTaxDefer makeRetireTaxDeferFromResultSet(ResultSet rs) throws SQLException {
        RetireTaxDefer defer = new RetireTaxDefer();
        defer.setRetireTaxDefId(rs.getInt("RETIRE_TAX_DEF_ID"));
        defer.setRetireCalcMstId(rs.getInt("RETIRE_CALC_MST_ID"));
        defer.setBizName(rs.getString("BIZ_NAME"));
        defer.setBizRegNo(rs.getString("BIZ_REG_NO"));
        defer.setAccountNo(rs.getString("ACCOUNT_NO"));
        
        // 데이터베이스의 Date 값이 null일 경우를 대비한 안전한 변환 처리
        Timestamp depositTs = rs.getTimestamp("DEPOSIT_DATE");
        if (depositTs != null) {
            defer.setDepositDate(new java.util.Date(depositTs.getTime()));
        }
        
        defer.setDepositAmt(rs.getLong("DEPOSIT_AMT"));
        
        return defer;
    }
}