package erp.payroll.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import erp.payroll.dto.FourInsuranceDeduction;
import erp.payroll.dto.FourInsurancePage;

// 급여 차수별 4대보험 공제액을 조회하는 DAO
public class FourInsuranceDao {

	public FourInsurancePage selectPage(Connection conn, String year, String month, String sequence)
			throws SQLException {
		FourInsurancePage page = new FourInsurancePage();
		selectPeriod(conn, year, month, sequence, page);
		page.setDeductions(selectDeductions(conn, year, month, sequence));
		return page;
	}

	private void selectPeriod(Connection conn, String year, String month, String sequence,
			FourInsurancePage page) throws SQLException {
		String sql = "SELECT MIN(CALC_START_DATE) CALC_START_DATE, MAX(CALC_END_DATE) CALC_END_DATE, "
				+ "MAX(PAY_DATE) PAY_DATE FROM PAYROLL_RUN WHERE PAY_YEAR = ? AND PAY_MONTH = ? AND PAY_SEQ = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, year);
			pstmt.setString(2, month);
			pstmt.setString(3, sequence);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					page.setCalculationStart(rs.getDate("CALC_START_DATE"));
					page.setCalculationEnd(rs.getDate("CALC_END_DATE"));
					page.setPaymentDate(rs.getDate("PAY_DATE"));
				}
			}
		}
	}

	private List<FourInsuranceDeduction> selectDeductions(Connection conn, String year, String month,
			String sequence) throws SQLException {
		String sql = "SELECT E.EMPLOYEE_ID, E.EMP_TYPE, E.EMP_NAME_KR, "
				+ "NVL(DP.DEPARTMENT_NAME, '-') DEPARTMENT_NAME, NVL(J.JOB_POSITION_NAME, '-') POSITION_NAME, "
				+ "SUM(CASE WHEN DI.DEDUCT_NAME = '국민연금' THEN EN.AMOUNT ELSE 0 END) PENSION, "
				+ "SUM(CASE WHEN DI.DEDUCT_NAME = '건강보험' THEN EN.AMOUNT ELSE 0 END) HEALTH, "
				+ "SUM(CASE WHEN DI.DEDUCT_NAME IN ('장기요양보험', '노인장기요양보험') THEN EN.AMOUNT ELSE 0 END) CARE, "
				+ "SUM(CASE WHEN DI.DEDUCT_NAME = '고용보험' THEN EN.AMOUNT ELSE 0 END) EMPLOYMENT "
				+ "FROM PAYROLL_RUN R JOIN PAYROLL_EMPLOYEE PE ON PE.PAYROLL_RUN_ID = R.PAYROLL_RUN_ID "
				+ "JOIN EMPLOYEE E ON E.EMPLOYEE_ID = PE.EMPLOYEE_ID "
				+ "LEFT JOIN PAYROLL_ENTRY EN ON EN.PAYROLL_EMPLOYEE_ID = PE.PAYROLL_EMPLOYEE_ID "
				+ "LEFT JOIN DEDUCT_ITEM DI ON DI.DEDUCT_ITEM_ID = EN.DEDUCT_ITEM_ID "
				+ "LEFT JOIN DEPARTMENT DP ON DP.DEPARTMENT_ID = E.DEPARTMENT_ID "
				+ "LEFT JOIN JOB_POSITION J ON J.JOB_POSITION_ID = E.JOB_POSITION_ID "
				+ "WHERE R.PAY_YEAR = ? AND R.PAY_MONTH = ? AND R.PAY_SEQ = ? "
				+ "GROUP BY E.EMPLOYEE_ID, E.EMP_TYPE, E.EMP_NAME_KR, DP.DEPARTMENT_NAME, J.JOB_POSITION_NAME "
				+ "HAVING SUM(CASE WHEN DI.DEDUCT_NAME IN ('국민연금', '건강보험', '장기요양보험', "
				+ "'노인장기요양보험', '고용보험') THEN EN.AMOUNT ELSE 0 END) > 0 "
				+ "ORDER BY E.EMP_NAME_KR, E.EMPLOYEE_ID";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, year);
			pstmt.setString(2, month);
			pstmt.setString(3, sequence);
			try (ResultSet rs = pstmt.executeQuery()) {
				List<FourInsuranceDeduction> result = new ArrayList<>();
				while (rs.next()) {
					result.add(makeDeduction(rs));
				}
				return result;
			}
		}
	}

	private FourInsuranceDeduction makeDeduction(ResultSet rs) throws SQLException {
		FourInsuranceDeduction deduction = new FourInsuranceDeduction();
		deduction.setEmploymentTypeName(rs.getString("EMP_TYPE"));
		deduction.setEmployeeName(rs.getString("EMP_NAME_KR"));
		deduction.setDepartmentName(rs.getString("DEPARTMENT_NAME"));
		deduction.setPositionName(rs.getString("POSITION_NAME"));
		// 현재 테이블에는 근로자 공제액만 있어 법정 공동 부담 보험은 같은 금액으로 계산한다.
		long pension = rs.getLong("PENSION");
		long health = rs.getLong("HEALTH");
		long care = rs.getLong("CARE");
		long employment = rs.getLong("EMPLOYMENT");
		deduction.setPensionEmployee(pension);
		deduction.setPensionEmployer(pension);
		deduction.setHealthEmployee(health);
		deduction.setHealthEmployer(health);
		deduction.setCareEmployee(care);
		deduction.setCareEmployer(care);
		deduction.setEmploymentEmployee(employment);
		deduction.setEmploymentEmployer(employment);
		return deduction;
	}
}
