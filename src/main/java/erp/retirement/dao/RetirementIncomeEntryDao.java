package erp.retirement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.retirement.model.RetirementIncomeEntry;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 퇴직급여 산정을 위한 급여내역 및 기타소득 데이터베이스 접근(DAO) 클래스
public class RetirementIncomeEntryDao {

	// 싱글톤 인스턴스 생성
	private static RetirementIncomeEntryDao retirementIncomeEntryDao = new RetirementIncomeEntryDao();

	// 싱글톤 접근 메서드
	public static RetirementIncomeEntryDao getInstance() {
		return retirementIncomeEntryDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	private RetirementIncomeEntryDao() {
	}

	// 퇴직급여 산정자료 등록
	// 시퀀스를 사용하여 기본키 발급 및 자료 저장
	public void insert(Connection conn, RetirementIncomeEntry entry) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO RETIREMENT_INCOME_ENTRY "
					+ "(RETIREMENT_INCOME_ENTRY_ID, RETIREMENT_CALCULATION_ID, DATA_TYPE, PERIOD_START_DATE, PERIOD_END_DATE, CALC_DAYS, PAY_YM, ITEM_NAME, AMOUNT, THREE_MONTH_AMOUNT) "
					+ "VALUES (RETIREMENT_INCOME_ENTRY_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, entry.getRetirementCalculationId());
			pstmt.setString(2, entry.getDataType());

			// 날짜 null 방어 로직 적용
			if (entry.getPeriodStartDate() == null) {
				pstmt.setNull(3, Types.DATE);
			} else {
				pstmt.setTimestamp(3, new Timestamp(entry.getPeriodStartDate().getTime()));
			}

			if (entry.getPeriodEndDate() == null) {
				pstmt.setNull(4, Types.DATE);
			} else {
				pstmt.setTimestamp(4, new Timestamp(entry.getPeriodEndDate().getTime()));
			}

			pstmt.setObject(5, entry.getCalcDays(), Types.NUMERIC);
			pstmt.setString(6, entry.getPayYm());
			pstmt.setString(7, entry.getItemName());
			pstmt.setLong(8, entry.getAmount());
			pstmt.setLong(9, entry.getThreeMonthAmount());

			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 특정 퇴직정산에 포함된 산정자료 목록 조회
	// 정산ID(RETIREMENT_CALCULATION_ID)를 기준으로 전체 반환
	public List<RetirementIncomeEntry> selectByCalcId(Connection conn, int calcId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM RETIREMENT_INCOME_ENTRY WHERE RETIREMENT_CALCULATION_ID = ? ORDER BY RETIREMENT_INCOME_ENTRY_ID ASC";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, calcId);
			rs = pstmt.executeQuery();

			List<RetirementIncomeEntry> result = new ArrayList<>();
			while (rs.next()) {
				result.add(makeEntryFromResultSet(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 퇴직급여 산정자료 삭제
	// 기본키를 기준으로 해당 데이터 삭제
	public int delete(Connection conn, int entryId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM RETIREMENT_INCOME_ENTRY WHERE RETIREMENT_INCOME_ENTRY_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, entryId);
			return pstmt.executeUpdate(); // 삭제된 행의 개수 반환
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// ResultSet 데이터를 RetirementIncomeEntry 객체로 변환
	private RetirementIncomeEntry makeEntryFromResultSet(ResultSet rs) throws SQLException {
		RetirementIncomeEntry entry = new RetirementIncomeEntry();

		entry.setRetirementIncomeEntryId(rs.getInt("RETIREMENT_INCOME_ENTRY_ID"));
		entry.setRetirementCalculationId(rs.getInt("RETIREMENT_CALCULATION_ID"));
		entry.setDataType(rs.getString("DATA_TYPE"));

		Timestamp startTs = rs.getTimestamp("PERIOD_START_DATE");
		if (startTs != null) {
			entry.setPeriodStartDate(new java.util.Date(startTs.getTime()));
		}

		Timestamp endTs = rs.getTimestamp("PERIOD_END_DATE");
		if (endTs != null) {
			entry.setPeriodEndDate(new java.util.Date(endTs.getTime()));
		}

		double days = rs.getDouble("CALC_DAYS");
		entry.setCalcDays(rs.wasNull() ? null : days);

		entry.setPayYm(rs.getString("PAY_YM"));
		entry.setItemName(rs.getString("ITEM_NAME"));
		entry.setAmount(rs.getLong("AMOUNT"));
		entry.setThreeMonthAmount(rs.getLong("THREE_MONTH_AMOUNT"));

		return entry;
	}
}