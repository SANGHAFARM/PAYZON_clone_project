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

// 퇴직연금계좌의 과세이연 내역을 저장하고 조회한다.
public class RetirementTaxDeferralDao {

	// 싱글톤 인스턴스 생성
	private static RetirementTaxDeferralDao retirementTaxDeferralDao = new RetirementTaxDeferralDao();

	// 싱글톤 접근 메서드
	public static RetirementTaxDeferralDao getInstance() {
		return retirementTaxDeferralDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	private RetirementTaxDeferralDao() {
	}

	// 과세이연 내역 등록
	// 시퀀스를 사용하여 기본키 발급 및 자료 저장
	public void insert(Connection conn, RetirementTaxDeferral deferral) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO RETIREMENT_TAX_DEFERRAL "
					+ "(RETIREMENT_TAX_DEFERRAL_ID, RETIREMENT_CALCULATION_ID, BIZ_NAME, BIZ_REG_NO, ACCOUNT_NO, DEPOSIT_DATE, DEPOSIT_AMT) "
					+ "VALUES (RETIREMENT_TAX_DEFERRAL_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, deferral.getRetirementCalculationId());
			pstmt.setString(2, deferral.getBizName());
			pstmt.setString(3, deferral.getBizRegNo());
			pstmt.setString(4, deferral.getAccountNo());

			// 날짜 null 방어 로직 적용
			if (deferral.getDepositDate() == null) {
				pstmt.setNull(5, Types.DATE);
			} else {
				pstmt.setTimestamp(5, new Timestamp(deferral.getDepositDate().getTime()));
			}

			pstmt.setLong(6, deferral.getDepositAmt());

			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 특정 퇴직정산에 포함된 과세이연 내역을 조회한다.
	public List<RetirementTaxDeferral> selectByCalcId(Connection conn, int calcId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM RETIREMENT_TAX_DEFERRAL WHERE RETIREMENT_CALCULATION_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, calcId);
			rs = pstmt.executeQuery();

			List<RetirementTaxDeferral> result = new ArrayList<>();
			while (rs.next()) {
				result.add(makeDeferralFromResultSet(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 기본키로 과세이연 내역을 삭제한다.
	public int delete(Connection conn, int deferralId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM RETIREMENT_TAX_DEFERRAL WHERE RETIREMENT_TAX_DEFERRAL_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, deferralId);
			return pstmt.executeUpdate(); // 삭제된 행의 개수 반환
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 조회 결과를 과세이연 객체로 변환한다.
	private RetirementTaxDeferral makeDeferralFromResultSet(ResultSet rs) throws SQLException {
		RetirementTaxDeferral deferral = new RetirementTaxDeferral();

		deferral.setRetirementTaxDeferralId(rs.getInt("RETIREMENT_TAX_DEFERRAL_ID"));
		deferral.setRetirementCalculationId(rs.getInt("RETIREMENT_CALCULATION_ID"));
		deferral.setBizName(rs.getString("BIZ_NAME"));
		deferral.setBizRegNo(rs.getString("BIZ_REG_NO"));
		deferral.setAccountNo(rs.getString("ACCOUNT_NO"));

		Timestamp depTs = rs.getTimestamp("DEPOSIT_DATE");
		if (depTs != null) {
			deferral.setDepositDate(new java.util.Date(depTs.getTime()));
		}

		deferral.setDepositAmt(rs.getLong("DEPOSIT_AMT"));

		return deferral;
	}
}
