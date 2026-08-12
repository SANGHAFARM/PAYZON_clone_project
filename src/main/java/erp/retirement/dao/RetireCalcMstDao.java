package erp.retirement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import erp.retirement.model.RetirementCalculation;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 퇴직급여 계산 내역 데이터베이스 접근(DAO) 클래스
public class RetireCalcMstDao {
	
	// 싱글톤 인스턴스 생성
	private static RetireCalcMstDao retireCalcMstDao = new RetireCalcMstDao();
	
	// 싱글톤 접근 메서드
	public static RetireCalcMstDao getInstance() {
		return retireCalcMstDao;
	}
	
	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	private RetireCalcMstDao() {}

	// 퇴직급여 계산 내역 등록 (INSERT)
	// 시퀀스를 사용하여 PK 발급 및 데이터 저장
	public void insert(Connection conn, RetirementCalculation mst) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO RETIRE_CALC_MST ("
					+ "RETIRE_CALC_MST_ID, EMP_ID, CALC_TYPE, CALC_START_DATE, RETIRE_DATE, "
					+ "SERVICE_YEARS, SERVICE_DAYS, EXCLUDE_DAYS, COMPENSATION_AMT, DISMISSAL_AMT, "
					+ "TAX_FREE_RETIRE_AMT, PREPAID_TAX_AMT, TAX_CREDIT_AMT, THREE_MONTH_TOTAL, AVG_MONTH_WAGE, "
					+ "AVG_DAY_WAGE, ORDINARY_DAY_WAGE, RETIRE_INCOME, CALCULATED_TAX_AMT, INCOME_TAX, "
					+ "LOCAL_INCOME_TAX, DEFERRED_INCOME_TAX, DEFERRED_LOCAL_TAX, SPECIAL_RURAL_TAX, OTHER_DEDUCT_AMT, "
					+ "TAXABLE_RETIRE_AMT, WITHHOLDING_TAX_AMT, ACTUAL_PAY_AMT, PAY_METHOD, PAY_DATE) "
					+ "VALUES (SEQ_RETIRE_CALC_MST_ID.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, mst.getEmpId());
			pstmt.setString(2, mst.getCalcType());
			pstmt.setTimestamp(3, new Timestamp(mst.getCalcStartDate().getTime()));
			pstmt.setTimestamp(4, new Timestamp(mst.getRetireDate().getTime()));
			pstmt.setInt(5, mst.getServiceYears());
			pstmt.setInt(6, mst.getServiceDays());
			pstmt.setInt(7, mst.getExcludeDays());
			pstmt.setLong(8, mst.getCompensationAmt());
			pstmt.setLong(9, mst.getDismissalAmt());
			pstmt.setLong(10, mst.getTaxFreeRetireAmt());
			pstmt.setLong(11, mst.getPrepaidTaxAmt());
			pstmt.setLong(12, mst.getTaxCreditAmt());
			pstmt.setLong(13, mst.getThreeMonthTotal());
			pstmt.setLong(14, mst.getAvgMonthWage());
			pstmt.setLong(15, mst.getAvgDayWage());
			pstmt.setLong(16, mst.getOrdinaryDayWage());
			pstmt.setLong(17, mst.getRetireIncome());
			pstmt.setLong(18, mst.getCalculatedTaxAmt());
			pstmt.setLong(19, mst.getIncomeTax());
			pstmt.setLong(20, mst.getLocalIncomeTax());
			pstmt.setLong(21, mst.getDeferredIncomeTax());
			pstmt.setLong(22, mst.getDeferredLocalTax());
			pstmt.setLong(23, mst.getSpecialRuralTax());
			pstmt.setLong(24, mst.getOtherDeductAmt());
			pstmt.setLong(25, mst.getTaxableRetireAmt());
			pstmt.setLong(26, mst.getWithholdingTaxAmt());
			pstmt.setLong(27, mst.getActualPayAmt());
			pstmt.setString(28, mst.getPayMethod());
			pstmt.setTimestamp(29, new Timestamp(mst.getPayDate().getTime()));

			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 퇴직급여 계산 내역 단건 조회 (SELECT BY ID)
	// 기본키(RETIRE_CALC_MST_ID)를 기준으로 1건의 데이터 조회
	public RetirementCalculation selectById(Connection conn, int retireCalcMstId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM RETIRE_CALC_MST WHERE RETIRE_CALC_MST_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, retireCalcMstId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				return makeRetireCalcMstFromResultSet(rs);
			}
			return null;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 유니크 조건 기준 단건 조회 (SELECT BY UNIQUE KEY)
	// 사원번호, 정산구분, 입사일, 퇴직일 조합으로 특정 정산 내역 조회
	public RetirementCalculation selectByUniqueKey(Connection conn, int empId, String calcType, java.util.Date calcStartDate,
			java.util.Date retireDate) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM RETIRE_CALC_MST "
					+ "WHERE EMP_ID = ? AND CALC_TYPE = ? AND CALC_START_DATE = ? AND RETIRE_DATE = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, empId);
			pstmt.setString(2, calcType);
			pstmt.setTimestamp(3, new Timestamp(calcStartDate.getTime()));
			pstmt.setTimestamp(4, new Timestamp(retireDate.getTime()));
			rs = pstmt.executeQuery();

			if (rs.next()) {
				return makeRetireCalcMstFromResultSet(rs);
			}
			return null;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 사원별 퇴직급여 계산 내역 전체 조회
	// 특정 사원(EMP_ID)의 모든 퇴직급여 계산 내역을 최신순으로 정렬하여 조회
	public List<RetirementCalculation> selectByEmpId(Connection conn, int empId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM RETIRE_CALC_MST WHERE EMP_ID = ? ORDER BY RETIRE_CALC_MST_ID DESC";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, empId);
			rs = pstmt.executeQuery();

			List<RetirementCalculation> result = new ArrayList<>();
			while (rs.next()) {
				result.add(makeRetireCalcMstFromResultSet(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 퇴직급여 계산 내역 수정 (UPDATE)
	// 기본키를 기준으로 전체 계산 항목 및 금액 데이터 수정
	public int update(Connection conn, RetirementCalculation mst) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "UPDATE RETIRE_CALC_MST SET "
					+ "EMP_ID = ?, CALC_TYPE = ?, CALC_START_DATE = ?, RETIRE_DATE = ?, "
					+ "SERVICE_YEARS = ?, SERVICE_DAYS = ?, EXCLUDE_DAYS = ?, COMPENSATION_AMT = ?, DISMISSAL_AMT = ?, "
					+ "TAX_FREE_RETIRE_AMT = ?, PREPAID_TAX_AMT = ?, TAX_CREDIT_AMT = ?, THREE_MONTH_TOTAL = ?, AVG_MONTH_WAGE = ?, "
					+ "AVG_DAY_WAGE = ?, ORDINARY_DAY_WAGE = ?, RETIRE_INCOME = ?, CALCULATED_TAX_AMT = ?, INCOME_TAX = ?, "
					+ "LOCAL_INCOME_TAX = ?, DEFERRED_INCOME_TAX = ?, DEFERRED_LOCAL_TAX = ?, SPECIAL_RURAL_TAX = ?, OTHER_DEDUCT_AMT = ?, "
					+ "TAXABLE_RETIRE_AMT = ?, WITHHOLDING_TAX_AMT = ?, ACTUAL_PAY_AMT = ?, PAY_METHOD = ?, PAY_DATE = ? "
					+ "WHERE RETIRE_CALC_MST_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, mst.getEmpId());
			pstmt.setString(2, mst.getCalcType());
			pstmt.setTimestamp(3, new Timestamp(mst.getCalcStartDate().getTime()));
			pstmt.setTimestamp(4, new Timestamp(mst.getRetireDate().getTime()));
			pstmt.setInt(5, mst.getServiceYears());
			pstmt.setInt(6, mst.getServiceDays());
			pstmt.setInt(7, mst.getExcludeDays());
			pstmt.setLong(8, mst.getCompensationAmt());
			pstmt.setLong(9, mst.getDismissalAmt());
			pstmt.setLong(10, mst.getTaxFreeRetireAmt());
			pstmt.setLong(11, mst.getPrepaidTaxAmt());
			pstmt.setLong(12, mst.getTaxCreditAmt());
			pstmt.setLong(13, mst.getThreeMonthTotal());
			pstmt.setLong(14, mst.getAvgMonthWage());
			pstmt.setLong(15, mst.getAvgDayWage());
			pstmt.setLong(16, mst.getOrdinaryDayWage());
			pstmt.setLong(17, mst.getRetireIncome());
			pstmt.setLong(18, mst.getCalculatedTaxAmt());
			pstmt.setLong(19, mst.getIncomeTax());
			pstmt.setLong(20, mst.getLocalIncomeTax());
			pstmt.setLong(21, mst.getDeferredIncomeTax());
			pstmt.setLong(22, mst.getDeferredLocalTax());
			pstmt.setLong(23, mst.getSpecialRuralTax());
			pstmt.setLong(24, mst.getOtherDeductAmt());
			pstmt.setLong(25, mst.getTaxableRetireAmt());
			pstmt.setLong(26, mst.getWithholdingTaxAmt());
			pstmt.setLong(27, mst.getActualPayAmt());
			pstmt.setString(28, mst.getPayMethod());
			pstmt.setTimestamp(29, new Timestamp(mst.getPayDate().getTime()));
			pstmt.setInt(30, mst.getRetireCalcMstId());

			return pstmt.executeUpdate(); // 수정된 행의 개수 반환
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 퇴직급여 계산 내역 삭제 (DELETE)
	// 기본키를 기준으로 해당 계산 내역 삭제
	public int delete(Connection conn, int retireCalcMstId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM RETIRE_CALC_MST WHERE RETIRE_CALC_MST_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, retireCalcMstId);

			return pstmt.executeUpdate(); // 삭제된 행의 개수 반환
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// ResultSet 데이터를 RetirementCalculation 객체로 변환
	// 코드 중복 방지를 위한 공통 매핑 객체 반환
	private RetirementCalculation makeRetireCalcMstFromResultSet(ResultSet rs) throws SQLException {
		RetirementCalculation mst = new RetirementCalculation();
		mst.setRetireCalcMstId(rs.getInt("RETIRE_CALC_MST_ID"));
		mst.setEmpId(rs.getInt("EMP_ID"));
		mst.setCalcType(rs.getString("CALC_TYPE"));
		mst.setCalcStartDate(new java.util.Date(rs.getTimestamp("CALC_START_DATE").getTime()));
		mst.setRetireDate(new java.util.Date(rs.getTimestamp("RETIRE_DATE").getTime()));
		mst.setServiceYears(rs.getInt("SERVICE_YEARS"));
		mst.setServiceDays(rs.getInt("SERVICE_DAYS"));
		mst.setExcludeDays(rs.getInt("EXCLUDE_DAYS"));
		mst.setCompensationAmt(rs.getLong("COMPENSATION_AMT"));
		mst.setDismissalAmt(rs.getLong("DISMISSAL_AMT"));
		mst.setTaxFreeRetireAmt(rs.getLong("TAX_FREE_RETIRE_AMT"));
		mst.setPrepaidTaxAmt(rs.getLong("PREPAID_TAX_AMT"));
		mst.setTaxCreditAmt(rs.getLong("TAX_CREDIT_AMT"));
		mst.setThreeMonthTotal(rs.getLong("THREE_MONTH_TOTAL"));
		mst.setAvgMonthWage(rs.getLong("AVG_MONTH_WAGE"));
		mst.setAvgDayWage(rs.getLong("AVG_DAY_WAGE"));
		mst.setOrdinaryDayWage(rs.getLong("ORDINARY_DAY_WAGE"));
		mst.setRetireIncome(rs.getLong("RETIRE_INCOME"));
		mst.setCalculatedTaxAmt(rs.getLong("CALCULATED_TAX_AMT"));
		mst.setIncomeTax(rs.getLong("INCOME_TAX"));
		mst.setLocalIncomeTax(rs.getLong("LOCAL_INCOME_TAX"));
		mst.setDeferredIncomeTax(rs.getLong("DEFERRED_INCOME_TAX"));
		mst.setDeferredLocalTax(rs.getLong("DEFERRED_LOCAL_TAX"));
		mst.setSpecialRuralTax(rs.getLong("SPECIAL_RURAL_TAX"));
		mst.setOtherDeductAmt(rs.getLong("OTHER_DEDUCT_AMT"));
		mst.setTaxableRetireAmt(rs.getLong("TAXABLE_RETIRE_AMT"));
		mst.setWithholdingTaxAmt(rs.getLong("WITHHOLDING_TAX_AMT"));
		mst.setActualPayAmt(rs.getLong("ACTUAL_PAY_AMT"));
		mst.setPayMethod(rs.getString("PAY_METHOD"));
		mst.setPayDate(new java.util.Date(rs.getTimestamp("PAY_DATE").getTime()));
		return mst;
	}
}