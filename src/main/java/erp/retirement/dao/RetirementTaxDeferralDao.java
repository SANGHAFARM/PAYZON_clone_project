package erp.retirement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.retirement.model.RetirementTaxDeferral;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 퇴직소득세 과세이연 내역 데이터베이스 접근(DAO) 클래스
public class RetirementTaxDeferralDao {
	
	// 싱글톤 인스턴스 생성
	private static RetirementTaxDeferralDao retirementTaxDeferralDao = new RetirementTaxDeferralDao();
	
	// 싱글톤 접근 메서드
	public static RetirementTaxDeferralDao getInstance() {
		return retirementTaxDeferralDao;
	}
	
	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	private RetirementTaxDeferralDao() {}

    // 과세이연 내역 등록 (INSERT)
    // 시퀀스를 활용한 기본키 발급 및 데이터 저장
    public void insert(Connection conn, RetirementTaxDeferral defer) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO RETIREMENT_TAX_DEFERRAL ("
                       + "RETIREMENT_TAX_DEFERRAL_ID, RETIREMENT_CALCULATION_ID, BIZ_NAME, BIZ_REG_NO, "
                       + "ACCOUNT_NO, DEPOSIT_DATE, DEPOSIT_AMT) "
                       + "VALUES (SEQ_RETIRE_TAX_DEF_ID.NEXTVAL, ?, ?, ?, ?, ?, ?)";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, defer.getRetirementCalculationId());
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
    // 기본키(RETIREMENT_TAX_DEFERRAL_ID)를 기준으로 1건의 데이터 조회
    public RetirementTaxDeferral selectById(Connection conn, int retireTaxDefId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT RETIREMENT_TAX_DEFERRAL_ID, RETIREMENT_CALCULATION_ID, BIZ_NAME, BIZ_REG_NO, "
                       + "ACCOUNT_NO, DEPOSIT_DATE, DEPOSIT_AMT "
                       + "FROM RETIREMENT_TAX_DEFERRAL WHERE RETIREMENT_TAX_DEFERRAL_ID = ?";
            
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
    // 계산 마스터 기본키(RETIREMENT_CALCULATION_ID)를 기준으로 연관된 이연 내역 전체 조회
    public List<RetirementTaxDeferral> selectByRetireCalcMstId(Connection conn, int retireCalcMstId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT RETIREMENT_TAX_DEFERRAL_ID, RETIREMENT_CALCULATION_ID, BIZ_NAME, BIZ_REG_NO, "
                       + "ACCOUNT_NO, DEPOSIT_DATE, DEPOSIT_AMT "
                       + "FROM RETIREMENT_TAX_DEFERRAL WHERE RETIREMENT_CALCULATION_ID = ? ORDER BY RETIREMENT_TAX_DEFERRAL_ID ASC";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, retireCalcMstId);
            rs = pstmt.executeQuery();
            
            List<RetirementTaxDeferral> result = new ArrayList<>();
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
    public int update(Connection conn, RetirementTaxDeferral defer) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE RETIREMENT_TAX_DEFERRAL SET "
                       + "RETIREMENT_CALCULATION_ID = ?, BIZ_NAME = ?, BIZ_REG_NO = ?, "
                       + "ACCOUNT_NO = ?, DEPOSIT_DATE = ?, DEPOSIT_AMT = ? "
                       + "WHERE RETIREMENT_TAX_DEFERRAL_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, defer.getRetirementCalculationId());
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
            pstmt.setLong(7, defer.getRetirementTaxDeferralId());
            
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
            String sql = "DELETE FROM RETIREMENT_TAX_DEFERRAL WHERE RETIREMENT_TAX_DEFERRAL_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, retireTaxDefId);
            
            return pstmt.executeUpdate(); // 삭제된 행의 개수 반환
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // ResultSet 데이터를 RetirementTaxDeferral 객체로 변환
    // 코드 중복 방지를 위한 공통 객체 매핑 처리
    private RetirementTaxDeferral makeRetireTaxDeferFromResultSet(ResultSet rs) throws SQLException {
        RetirementTaxDeferral defer = new RetirementTaxDeferral();
        defer.setRetirementTaxDeferralId(rs.getLong("RETIREMENT_TAX_DEFERRAL_ID"));
        defer.setRetirementCalculationId(rs.getLong("RETIREMENT_CALCULATION_ID"));
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
