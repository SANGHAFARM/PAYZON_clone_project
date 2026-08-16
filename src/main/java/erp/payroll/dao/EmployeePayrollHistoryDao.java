package erp.payroll.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import erp.payroll.dto.EmployeePayrollHistoryPage.EmployeePayrollHistoryEmployee;
import erp.payroll.dto.EmployeePayrollHistoryPage.EmployeePayrollHistoryItem;

// 사원과 월별 급여 지급 내역을 조회한다.
public class EmployeePayrollHistoryDao {

	private static final String HISTORY_SQL =
			"SELECT R.PAYROLL_RUN_ID, R.PAY_YEAR, R.PAY_MONTH, R.PAY_SEQ, R.INCOME_TYPE, "
			+ "NVL(E.HI_MONTHLY_BASE, NVL(E.BASIC_PAY, 0)) MONTHLY_BASE, "
			+ "NVL((SELECT SUM(CASE WHEN EN.PAY_ITEM_ID IS NOT NULL THEN EN.AMOUNT ELSE 0 END) "
			+ "FROM PAYROLL_ENTRY EN WHERE EN.PAYROLL_EMPLOYEE_ID = PE.PAYROLL_EMPLOYEE_ID), 0) ENTRY_PAY, "
			+ "NVL((SELECT SUM(CASE WHEN EN.DEDUCT_ITEM_ID IS NOT NULL THEN EN.AMOUNT ELSE 0 END) "
			+ "FROM PAYROLL_ENTRY EN WHERE EN.PAYROLL_EMPLOYEE_ID = PE.PAYROLL_EMPLOYEE_ID), 0) TOTAL_DEDUCT, "
			+ "NVL((SELECT SUM(W.DAILY_PAY) FROM DAILY_WORK_RECORD W WHERE W.EMPLOYEE_ID = PE.EMPLOYEE_ID "
			+ "AND W.WORK_DATE BETWEEN R.CALC_START_DATE AND R.CALC_END_DATE), 0) DAILY_PAY, "
			+ "NVL((SELECT SUM(CASE WHEN D.DEDUCT_NAME = '국민연금' THEN EN.AMOUNT ELSE 0 END) "
			+ "FROM PAYROLL_ENTRY EN JOIN DEDUCT_ITEM D ON D.DEDUCT_ITEM_ID = EN.DEDUCT_ITEM_ID "
			+ "WHERE EN.PAYROLL_EMPLOYEE_ID = PE.PAYROLL_EMPLOYEE_ID), 0) PENSION, "
			+ "NVL((SELECT SUM(CASE WHEN D.DEDUCT_NAME = '건강보험' THEN EN.AMOUNT ELSE 0 END) "
			+ "FROM PAYROLL_ENTRY EN JOIN DEDUCT_ITEM D ON D.DEDUCT_ITEM_ID = EN.DEDUCT_ITEM_ID "
			+ "WHERE EN.PAYROLL_EMPLOYEE_ID = PE.PAYROLL_EMPLOYEE_ID), 0) HEALTH, "
			+ "NVL((SELECT SUM(CASE WHEN D.DEDUCT_NAME IN ('장기요양보험', '노인장기요양보험') THEN EN.AMOUNT ELSE 0 END) "
			+ "FROM PAYROLL_ENTRY EN JOIN DEDUCT_ITEM D ON D.DEDUCT_ITEM_ID = EN.DEDUCT_ITEM_ID "
			+ "WHERE EN.PAYROLL_EMPLOYEE_ID = PE.PAYROLL_EMPLOYEE_ID), 0) CARE, "
			+ "NVL((SELECT SUM(CASE WHEN D.DEDUCT_NAME = '고용보험' THEN EN.AMOUNT ELSE 0 END) "
			+ "FROM PAYROLL_ENTRY EN JOIN DEDUCT_ITEM D ON D.DEDUCT_ITEM_ID = EN.DEDUCT_ITEM_ID "
			+ "WHERE EN.PAYROLL_EMPLOYEE_ID = PE.PAYROLL_EMPLOYEE_ID), 0) EMPLOYMENT, "
			+ "NVL((SELECT SUM(CASE WHEN D.DEDUCT_NAME = '소득세' THEN EN.AMOUNT ELSE 0 END) "
			+ "FROM PAYROLL_ENTRY EN JOIN DEDUCT_ITEM D ON D.DEDUCT_ITEM_ID = EN.DEDUCT_ITEM_ID "
			+ "WHERE EN.PAYROLL_EMPLOYEE_ID = PE.PAYROLL_EMPLOYEE_ID), 0) INCOME_TAX, "
			+ "NVL((SELECT SUM(CASE WHEN D.DEDUCT_NAME IN ('지방소득세', '주민세') THEN EN.AMOUNT ELSE 0 END) "
			+ "FROM PAYROLL_ENTRY EN JOIN DEDUCT_ITEM D ON D.DEDUCT_ITEM_ID = EN.DEDUCT_ITEM_ID "
			+ "WHERE EN.PAYROLL_EMPLOYEE_ID = PE.PAYROLL_EMPLOYEE_ID), 0) LOCAL_TAX "
			+ "FROM PAYROLL_RUN R JOIN PAYROLL_EMPLOYEE PE ON PE.PAYROLL_RUN_ID = R.PAYROLL_RUN_ID "
			+ "JOIN EMPLOYEE E ON E.EMPLOYEE_ID = PE.EMPLOYEE_ID "
			+ "WHERE PE.EMPLOYEE_ID = ? AND R.PAY_YEAR || R.PAY_MONTH BETWEEN ? AND ? ";

	public List<EmployeePayrollHistoryEmployee> selectEmployees(Connection conn, String keyword,
			Integer departmentId, String status) throws SQLException {
		String sql = "SELECT E.EMPLOYEE_ID, E.EMP_TYPE, E.EMP_NO, E.EMP_NAME_KR, "
				+ "NVL(D.DEPARTMENT_NAME, '-') DEPARTMENT_NAME, NVL(J.JOB_POSITION_NAME, '-') POSITION_NAME, "
				+ "NVL(E.STATUS, CASE WHEN E.RETIRE_DATE IS NULL THEN '재직' ELSE '퇴직' END) EMP_STATUS "
				+ "FROM EMPLOYEE E LEFT JOIN DEPARTMENT D ON D.DEPARTMENT_ID = E.DEPARTMENT_ID "
				+ "LEFT JOIN JOB_POSITION J ON J.JOB_POSITION_ID = E.JOB_POSITION_ID "
				+ "WHERE (? IS NULL OR E.EMP_NAME_KR LIKE '%' || ? || '%' OR E.EMP_NO LIKE '%' || ? || '%') "
				+ "AND (? IS NULL OR E.DEPARTMENT_ID = ?) "
				+ "AND (? IS NULL OR (? = 'ACTIVE' AND NVL(E.STATUS, '재직') = '재직') "
				+ "OR (? = 'RETIRED' AND E.STATUS = '퇴직')) ORDER BY E.EMP_NAME_KR";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			String search = emptyToNull(keyword);
			pstmt.setString(1, search);
			pstmt.setString(2, search);
			pstmt.setString(3, search);
			if (departmentId == null) {
				pstmt.setNull(4, java.sql.Types.INTEGER);
				pstmt.setNull(5, java.sql.Types.INTEGER);
			} else {
				pstmt.setInt(4, departmentId);
				pstmt.setInt(5, departmentId);
			}
			String selectedStatus = emptyToNull(status);
			pstmt.setString(6, selectedStatus);
			pstmt.setString(7, selectedStatus);
			pstmt.setString(8, selectedStatus);
			try (ResultSet rs = pstmt.executeQuery()) {
				List<EmployeePayrollHistoryEmployee> result = new ArrayList<>();
				while (rs.next()) {
					result.add(makeEmployee(rs));
				}
				return result;
			}
		}
	}

	public EmployeePayrollHistoryEmployee selectEmployee(Connection conn, int employeeId) throws SQLException {
		String sql = "SELECT E.EMPLOYEE_ID, E.EMP_TYPE, E.EMP_NO, E.EMP_NAME_KR, "
				+ "NVL(D.DEPARTMENT_NAME, '-') DEPARTMENT_NAME, NVL(J.JOB_POSITION_NAME, '-') POSITION_NAME, "
				+ "NVL(E.STATUS, CASE WHEN E.RETIRE_DATE IS NULL THEN '재직' ELSE '퇴직' END) EMP_STATUS "
				+ "FROM EMPLOYEE E LEFT JOIN DEPARTMENT D ON D.DEPARTMENT_ID = E.DEPARTMENT_ID "
				+ "LEFT JOIN JOB_POSITION J ON J.JOB_POSITION_ID = E.JOB_POSITION_ID WHERE E.EMPLOYEE_ID = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, employeeId);
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next() ? makeEmployee(rs) : null;
			}
		}
	}

	public List<EmployeePayrollHistoryItem> selectHistories(Connection conn, int employeeId,
			String startMonth, String endMonth) throws SQLException {
		try (PreparedStatement pstmt = conn.prepareStatement(HISTORY_SQL +
				"ORDER BY R.PAY_YEAR DESC, R.PAY_MONTH DESC, R.PAY_SEQ DESC, R.INCOME_TYPE")) {
			pstmt.setInt(1, employeeId);
			pstmt.setString(2, startMonth.replace("-", ""));
			pstmt.setString(3, endMonth.replace("-", ""));
			try (ResultSet rs = pstmt.executeQuery()) {
				List<EmployeePayrollHistoryItem> result = new ArrayList<>();
				while (rs.next()) {
					result.add(makeHistory(rs));
				}
				return result;
			}
		}
	}

	private EmployeePayrollHistoryEmployee makeEmployee(ResultSet rs) throws SQLException {
		EmployeePayrollHistoryEmployee employee = new EmployeePayrollHistoryEmployee();
		employee.setEmployeeId(rs.getInt("EMPLOYEE_ID"));
		employee.setEmploymentTypeName(rs.getString("EMP_TYPE"));
		employee.setEmployeeNumber(rs.getString("EMP_NO"));
		employee.setEmployeeName(rs.getString("EMP_NAME_KR"));
		employee.setDepartmentName(rs.getString("DEPARTMENT_NAME"));
		employee.setPositionName(rs.getString("POSITION_NAME"));
		employee.setStatus(rs.getString("EMP_STATUS"));
		return employee;
	}

	private EmployeePayrollHistoryItem makeHistory(ResultSet rs) throws SQLException {
		EmployeePayrollHistoryItem item = new EmployeePayrollHistoryItem();
		item.setPaymentMonth(rs.getString("PAY_YEAR") + "-" + rs.getString("PAY_MONTH"));
		item.setPaymentRound(String.valueOf(Integer.parseInt(rs.getString("PAY_SEQ"))));
		item.setStandardMonthlyIncome(rs.getLong("MONTHLY_BASE"));
		long dailyPay = "2".equals(rs.getString("INCOME_TYPE")) ? rs.getLong("DAILY_PAY") : 0;
		item.setTotalPayment(rs.getLong("ENTRY_PAY") + dailyPay);
		item.setTotalDeduction(rs.getLong("TOTAL_DEDUCT"));
		item.setNationalPension(rs.getLong("PENSION"));
		item.setHealthInsurance(rs.getLong("HEALTH"));
		item.setLongTermCareInsurance(rs.getLong("CARE"));
		item.setEmploymentInsurance(rs.getLong("EMPLOYMENT"));
		item.setIncomeTax(rs.getLong("INCOME_TAX"));
		item.setLocalIncomeTax(rs.getLong("LOCAL_TAX"));
		return item;
	}

	private String emptyToNull(String value) {
		return value == null || value.trim().isEmpty() ? null : value.trim();
	}
}
