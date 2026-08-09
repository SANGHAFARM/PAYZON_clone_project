package erp.payroll.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import erp.payroll.model.PayrollMaster;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 월별 급여계산 회차 데이터베이스 접근(DAO) 클래스
public class PayrollMasterDao {
	
	// 싱글톤 인스턴스 생성
	private static PayrollMasterDao payrollMasterDao = new PayrollMasterDao();
	
	// 싱글톤 접근 메서드
	public static PayrollMasterDao getInstance() {
		return payrollMasterDao;
	}
	
	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	private PayrollMasterDao() {}

    // 급여계산 회차 등록 (INSERT)
    // 시퀀스를 사용하여 PK 발급 및 데이터 저장
    public void insert(Connection conn, PayrollMaster master) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO PAYROLL_MASTER "
                       + "(PAYROLL_MST_ID, PAY_YEAR, PAY_MONTH, PAY_SEQ, INCOME_TYPE, "
                       + "CALC_START_DATE, CALC_END_DATE, PAY_DATE) "
                       + "VALUES (SEQ_PAYROLL_MST_ID.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, master.getPayYear());
            pstmt.setString(2, master.getPayMonth());
            pstmt.setString(3, master.getPaySeq());
            pstmt.setString(4, master.getIncomeType());
            
            // java.util.Date를 JDBC 연동을 위해 java.sql.Timestamp로 변환하여 할당
            pstmt.setTimestamp(5, new Timestamp(master.getCalcStartDate().getTime()));
            pstmt.setTimestamp(6, new Timestamp(master.getCalcEndDate().getTime()));
            pstmt.setTimestamp(7, new Timestamp(master.getPayDate().getTime()));
            
            pstmt.executeUpdate();
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 급여계산 회차 단건 조회 (SELECT BY ID)
    // 기본키(PAYROLL_MST_ID)를 기준으로 1건의 데이터 조회
    public PayrollMaster selectById(Connection conn, int payrollMstId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT PAYROLL_MST_ID, PAY_YEAR, PAY_MONTH, PAY_SEQ, INCOME_TYPE, "
                       + "CALC_START_DATE, CALC_END_DATE, PAY_DATE "
                       + "FROM PAYROLL_MASTER WHERE PAYROLL_MST_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, payrollMstId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return makePayrollMasterFromResultSet(rs);
            }
            return null; // 조회된 데이터가 없을 경우 null 반환
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 특정 귀속연월 및 차수 기준 단건 조회 (SELECT BY PERIOD)
    // 유니크 제약조건(UK_PAYROLL_PERIOD) 컬럼들을 조합하여 특정 급여회차 정보 정확히 조회
    public PayrollMaster selectByPeriod(Connection conn, String payYear, String payMonth, String paySeq, String incomeType) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT PAYROLL_MST_ID, PAY_YEAR, PAY_MONTH, PAY_SEQ, INCOME_TYPE, "
                       + "CALC_START_DATE, CALC_END_DATE, PAY_DATE "
                       + "FROM PAYROLL_MASTER "
                       + "WHERE PAY_YEAR = ? AND PAY_MONTH = ? AND PAY_SEQ = ? AND INCOME_TYPE = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, payYear);
            pstmt.setString(2, payMonth);
            pstmt.setString(3, paySeq);
            pstmt.setString(4, incomeType);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return makePayrollMasterFromResultSet(rs);
            }
            return null; // 해당하는 회차가 없을 경우 null 반환
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 급여계산 회차 전체 목록 조회 (SELECT ALL)
    // 최신 급여 회차가 먼저 보이도록 내림차순 정렬하여 전체 목록 조회
    public List<PayrollMaster> selectAll(Connection conn) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT PAYROLL_MST_ID, PAY_YEAR, PAY_MONTH, PAY_SEQ, INCOME_TYPE, "
                       + "CALC_START_DATE, CALC_END_DATE, PAY_DATE "
                       + "FROM PAYROLL_MASTER ORDER BY PAYROLL_MST_ID DESC";
            
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            List<PayrollMaster> result = new ArrayList<>();
            while (rs.next()) {
                result.add(makePayrollMasterFromResultSet(rs));
            }
            return result;
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 급여계산 회차 수정 (UPDATE)
    // 기본키를 기준으로 데이터 수정
    public int update(Connection conn, PayrollMaster master) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE PAYROLL_MASTER SET "
                       + "PAY_YEAR = ?, PAY_MONTH = ?, PAY_SEQ = ?, INCOME_TYPE = ?, "
                       + "CALC_START_DATE = ?, CALC_END_DATE = ?, PAY_DATE = ? "
                       + "WHERE PAYROLL_MST_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, master.getPayYear());
            pstmt.setString(2, master.getPayMonth());
            pstmt.setString(3, master.getPaySeq());
            pstmt.setString(4, master.getIncomeType());
            pstmt.setTimestamp(5, new Timestamp(master.getCalcStartDate().getTime()));
            pstmt.setTimestamp(6, new Timestamp(master.getCalcEndDate().getTime()));
            pstmt.setTimestamp(7, new Timestamp(master.getPayDate().getTime()));
            pstmt.setInt(8, master.getPayrollMstId());
            
            return pstmt.executeUpdate(); // 수정된 행의 개수 반환
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // 급여계산 회차 삭제 (DELETE)
    // 기본키를 기준으로 데이터 삭제
    public int delete(Connection conn, int payrollMstId) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM PAYROLL_MASTER WHERE PAYROLL_MST_ID = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, payrollMstId);
            
            return pstmt.executeUpdate(); // 삭제된 행의 개수 반환
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

    // ResultSet 데이터를 PayrollMaster 객체로 변환
    // 코드 중복 방지를 위한 ResultSet 매핑 공통 메서드 분리
    private PayrollMaster makePayrollMasterFromResultSet(ResultSet rs) throws SQLException {
        PayrollMaster pm = new PayrollMaster();
        pm.setPayrollMstId(rs.getInt("PAYROLL_MST_ID"));
        pm.setPayYear(rs.getString("PAY_YEAR"));
        pm.setPayMonth(rs.getString("PAY_MONTH"));
        pm.setPaySeq(rs.getString("PAY_SEQ"));
        pm.setIncomeType(rs.getString("INCOME_TYPE"));
        
        // 데이터베이스의 Date/Timestamp 값을 java.util.Date 타입으로 안전하게 변환하여 할당
        pm.setCalcStartDate(new java.util.Date(rs.getTimestamp("CALC_START_DATE").getTime()));
        pm.setCalcEndDate(new java.util.Date(rs.getTimestamp("CALC_END_DATE").getTime()));
        pm.setPayDate(new java.util.Date(rs.getTimestamp("PAY_DATE").getTime()));
        
        return pm;
    }
}