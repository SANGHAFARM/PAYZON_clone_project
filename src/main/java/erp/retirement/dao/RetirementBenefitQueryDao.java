package erp.retirement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import erp.retirement.model.RetirementIncomeEntry;

// 퇴직급여 계산에 사용할 최근 급여 지급액을 조회한다.
public class RetirementBenefitQueryDao {

	public List<RetirementIncomeEntry> selectRecentSalaryEntries(Connection conn, int employeeId,
			String endDate) throws SQLException {
		// 정산 종료일 기준 직전 3개월을 달력월 경계로 나누고, 첫·마지막 월은 일할 계산한다.
		String sql = "WITH PARAMS AS ("
				+ " SELECT TO_DATE(?, 'YYYY-MM-DD') END_DATE, "
				+ " ADD_MONTHS(TO_DATE(?, 'YYYY-MM-DD'), -3) + 1 START_DATE FROM DUAL"
				+ "), PERIODS AS ("
				+ " SELECT GREATEST(ADD_MONTHS(TRUNC(P.START_DATE, 'MM'), LEVEL - 1), P.START_DATE) PERIOD_START_DATE, "
				+ " LEAST(LAST_DAY(ADD_MONTHS(TRUNC(P.START_DATE, 'MM'), LEVEL - 1)), P.END_DATE) PERIOD_END_DATE "
				+ " FROM PARAMS P CONNECT BY LEVEL <= MONTHS_BETWEEN(TRUNC(P.END_DATE, 'MM'), TRUNC(P.START_DATE, 'MM')) + 1"
				+ "), PAY_DATA AS ("
				+ " SELECT R.CALC_START_DATE, R.CALC_END_DATE, "
				+ " SUM(CASE WHEN EN.PAY_ITEM_ID IS NOT NULL THEN EN.AMOUNT ELSE 0 END) AMOUNT "
				+ " FROM PAYROLL_RUN R JOIN PAYROLL_EMPLOYEE PE ON PE.PAYROLL_RUN_ID = R.PAYROLL_RUN_ID "
				+ " LEFT JOIN PAYROLL_ENTRY EN ON EN.PAYROLL_EMPLOYEE_ID = PE.PAYROLL_EMPLOYEE_ID "
				+ " WHERE PE.EMPLOYEE_ID = ? GROUP BY R.PAYROLL_RUN_ID, R.CALC_START_DATE, R.CALC_END_DATE"
				+ ") SELECT P.PERIOD_START_DATE, P.PERIOD_END_DATE, "
				+ " NVL(SUM(ROUND(D.AMOUNT * (LEAST(D.CALC_END_DATE, P.PERIOD_END_DATE) "
				+ " - GREATEST(D.CALC_START_DATE, P.PERIOD_START_DATE) + 1) "
				+ " / (D.CALC_END_DATE - D.CALC_START_DATE + 1))), 0) AMOUNT "
				+ " FROM PERIODS P LEFT JOIN PAY_DATA D ON D.CALC_START_DATE <= P.PERIOD_END_DATE "
				+ " AND D.CALC_END_DATE >= P.PERIOD_START_DATE "
				+ " GROUP BY P.PERIOD_START_DATE, P.PERIOD_END_DATE ORDER BY P.PERIOD_START_DATE";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, endDate);
			pstmt.setString(2, endDate);
			pstmt.setInt(3, employeeId);
			try (ResultSet rs = pstmt.executeQuery()) {
				List<RetirementIncomeEntry> result = new ArrayList<>();
				while (rs.next()) {
					RetirementIncomeEntry entry = new RetirementIncomeEntry();
					entry.setDataType("SALARY");
					entry.setPeriodStartDate(rs.getDate("PERIOD_START_DATE"));
					entry.setPeriodEndDate(rs.getDate("PERIOD_END_DATE"));
					long days = java.time.temporal.ChronoUnit.DAYS.between(
							rs.getDate("PERIOD_START_DATE").toLocalDate(),
							rs.getDate("PERIOD_END_DATE").toLocalDate()) + 1;
					entry.setCalcDays((double) days);
					entry.setAmount(rs.getLong("AMOUNT"));
					entry.setThreeMonthAmount(0);
					result.add(entry);
				}
				return result;
			}
		}
	}
}
