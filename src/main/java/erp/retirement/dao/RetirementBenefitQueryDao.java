package erp.retirement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import erp.retirement.model.RetirementIncomeEntry;

// 퇴직급여 계산에 사용할 최근 급여 지급액을 조회하는 전용 DAO
public class RetirementBenefitQueryDao {

	public List<RetirementIncomeEntry> selectRecentSalaryEntries(Connection conn, int employeeId,
			String endDate) throws SQLException {
		String sql = "SELECT MIN(R.CALC_START_DATE) PERIOD_START_DATE, "
				+ "MAX(R.CALC_END_DATE) PERIOD_END_DATE, "
				+ "SUM(CASE WHEN EN.PAY_ITEM_ID IS NOT NULL THEN EN.AMOUNT ELSE 0 END) AMOUNT "
				+ "FROM PAYROLL_RUN R JOIN PAYROLL_EMPLOYEE PE ON PE.PAYROLL_RUN_ID = R.PAYROLL_RUN_ID "
				+ "LEFT JOIN PAYROLL_ENTRY EN ON EN.PAYROLL_EMPLOYEE_ID = PE.PAYROLL_EMPLOYEE_ID "
				+ "WHERE PE.EMPLOYEE_ID = ? AND R.CALC_END_DATE BETWEEN "
				+ "ADD_MONTHS(TO_DATE(?, 'YYYY-MM-DD'), -3) AND TO_DATE(?, 'YYYY-MM-DD') "
				+ "GROUP BY R.PAY_YEAR, R.PAY_MONTH ORDER BY R.PAY_YEAR, R.PAY_MONTH";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, employeeId);
			pstmt.setString(2, endDate);
			pstmt.setString(3, endDate);
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
