package erp.payroll.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import erp.payroll.dto.FourInsurancePage.FourInsuranceDeduction;
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
		Map<String, Integer> insuranceItemIds = selectInsuranceItemIds(conn);
		String sql = "SELECT E.EMPLOYEE_ID, E.EMP_TYPE, E.EMP_NAME_KR, "
				+ "NVL(DP.DEPARTMENT_NAME, '-') DEPARTMENT_NAME, NVL(J.JOB_POSITION_NAME, '-') POSITION_NAME, "
				+ "SUM(CASE WHEN EN.DEDUCT_ITEM_ID = ? THEN EN.AMOUNT ELSE 0 END) PENSION, "
				+ "SUM(CASE WHEN EN.DEDUCT_ITEM_ID = ? THEN EN.AMOUNT ELSE 0 END) HEALTH, "
				+ "SUM(CASE WHEN EN.DEDUCT_ITEM_ID = ? THEN EN.AMOUNT ELSE 0 END) CARE, "
				+ "SUM(CASE WHEN EN.DEDUCT_ITEM_ID = ? THEN EN.AMOUNT ELSE 0 END) EMPLOYMENT, "
				+ "SUM(CASE WHEN EN.PAY_ITEM_ID IS NOT NULL THEN EN.AMOUNT ELSE 0 END) GROSS_PAY, "
				+ "E.EI_MONTHLY_BASE "
				+ "FROM PAYROLL_RUN R JOIN PAYROLL_EMPLOYEE PE ON PE.PAYROLL_RUN_ID = R.PAYROLL_RUN_ID "
				+ "JOIN EMPLOYEE E ON E.EMPLOYEE_ID = PE.EMPLOYEE_ID "
				+ "LEFT JOIN PAYROLL_ENTRY EN ON EN.PAYROLL_EMPLOYEE_ID = PE.PAYROLL_EMPLOYEE_ID "
				+ "LEFT JOIN DEPARTMENT DP ON DP.DEPARTMENT_ID = E.DEPARTMENT_ID "
				+ "LEFT JOIN JOB_POSITION J ON J.JOB_POSITION_ID = E.JOB_POSITION_ID "
				+ "WHERE R.PAY_YEAR = ? AND R.PAY_MONTH = ? AND R.PAY_SEQ = ? "
				+ "GROUP BY E.EMPLOYEE_ID, E.EMP_TYPE, E.EMP_NAME_KR, DP.DEPARTMENT_NAME, "
				+ "J.JOB_POSITION_NAME, E.EI_MONTHLY_BASE "
				+ "HAVING SUM(CASE WHEN EN.DEDUCT_ITEM_ID IN (?, ?, ?, ?) THEN EN.AMOUNT ELSE 0 END) > 0 "
				+ "ORDER BY E.EMP_NAME_KR, E.EMPLOYEE_ID";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			int pensionId = itemId(insuranceItemIds, "PENSION");
			int healthId = itemId(insuranceItemIds, "HEALTH");
			int careId = itemId(insuranceItemIds, "CARE");
			int employmentId = itemId(insuranceItemIds, "EMPLOYMENT");
			pstmt.setInt(1, pensionId);
			pstmt.setInt(2, healthId);
			pstmt.setInt(3, careId);
			pstmt.setInt(4, employmentId);
			pstmt.setString(5, year);
			pstmt.setString(6, month);
			pstmt.setString(7, sequence);
			pstmt.setInt(8, pensionId);
			pstmt.setInt(9, healthId);
			pstmt.setInt(10, careId);
			pstmt.setInt(11, employmentId);
			try (ResultSet rs = pstmt.executeQuery()) {
				List<FourInsuranceDeduction> result = new ArrayList<>();
				while (rs.next()) {
					result.add(makeDeduction(rs));
				}
				return result;
			}
		}
	}

	// 항목명은 보험 항목의 ID를 찾을 때만 사용하고, 금액 집계는 고정된 ID로 처리한다.
	private Map<String, Integer> selectInsuranceItemIds(Connection conn) throws SQLException {
		String sql = "SELECT DEDUCT_ITEM_ID, DEDUCT_NAME FROM DEDUCT_ITEM "
				+ "WHERE DEDUCT_NAME IN ('국민연금', '건강보험', '장기요양보험', '노인장기요양보험', '고용보험') "
				+ "ORDER BY DEDUCT_ITEM_ID";
		Map<String, Integer> result = new HashMap<>();
		try (PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {
			while (rs.next()) {
				String name = rs.getString("DEDUCT_NAME");
				String type = insuranceType(name);
				if (type != null && !result.containsKey(type)) {
					result.put(type, rs.getInt("DEDUCT_ITEM_ID"));
				}
			}
		}
		return result;
	}

	private String insuranceType(String name) {
		if ("국민연금".equals(name)) return "PENSION";
		if ("건강보험".equals(name)) return "HEALTH";
		if ("장기요양보험".equals(name) || "노인장기요양보험".equals(name)) return "CARE";
		if ("고용보험".equals(name)) return "EMPLOYMENT";
		return null;
	}

	private int itemId(Map<String, Integer> itemIds, String type) {
		Integer itemId = itemIds.get(type);
		return itemId == null ? -1 : itemId;
	}

	private FourInsuranceDeduction makeDeduction(ResultSet rs) throws SQLException {
		FourInsuranceDeduction deduction = new FourInsuranceDeduction();
		deduction.setEmploymentTypeName(rs.getString("EMP_TYPE"));
		deduction.setEmployeeName(rs.getString("EMP_NAME_KR"));
		deduction.setDepartmentName(rs.getString("DEPARTMENT_NAME"));
		deduction.setPositionName(rs.getString("POSITION_NAME"));
		// 저장된 금액은 근로자 부담액이며 국민연금·건강보험 계열은 회사도 같은 금액을 부담한다.
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
		long employmentBase = rs.getLong("EI_MONTHLY_BASE");
		if (employmentBase <= 0) {
			employmentBase = rs.getLong("GROSS_PAY");
		}
		// 회사는 실업급여분 외에 150인 미만 사업장 기준 0.25%를 추가 부담한다.
		deduction.setEmploymentEmployer(employment + roundDownTen(employmentBase * 0.0025));
		return deduction;
	}

	private long roundDownTen(double amount) {
		return ((long) amount / 10) * 10;
	}
}
