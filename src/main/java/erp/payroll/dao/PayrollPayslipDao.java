package erp.payroll.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import erp.payroll.dto.PayrollPayslipEmployee;
import erp.payroll.dto.PayrollRegisterColumn;
import erp.payroll.model.PayrollRun;
import jdbc.JdbcUtil;

// 급여명세서의 회차, 사원, 지급·공제 내역을 조회하는 DAO
public class PayrollPayslipDao {

	public List<PayrollRun> selectRuns(Connection conn, String year, String month, String sequence)
			throws SQLException {
		String sql = "SELECT * FROM PAYROLL_RUN WHERE PAY_YEAR = ? AND PAY_MONTH = ? AND PAY_SEQ = ? "
				+ "ORDER BY INCOME_TYPE";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, year);
			pstmt.setString(2, month);
			pstmt.setString(3, sequence);
			try (ResultSet rs = pstmt.executeQuery()) {
				List<PayrollRun> result = new ArrayList<>();
				while (rs.next()) {
					PayrollRun run = new PayrollRun();
					run.setPayrollRunId(rs.getInt("PAYROLL_RUN_ID"));
					run.setPayYear(rs.getString("PAY_YEAR"));
					run.setPayMonth(rs.getString("PAY_MONTH"));
					run.setPaySeq(rs.getString("PAY_SEQ"));
					run.setIncomeType(rs.getString("INCOME_TYPE"));
					run.setCalcStartDate(rs.getDate("CALC_START_DATE"));
					run.setCalcEndDate(rs.getDate("CALC_END_DATE"));
					run.setPayDate(rs.getDate("PAY_DATE"));
					result.add(run);
				}
				return result;
			}
		}
	}

	public List<PayrollPayslipEmployee> selectEmployees(Connection conn, String year, String month, String sequence,
			String keyword) throws SQLException {
		String sql = "SELECT DISTINCT E.EMPLOYEE_ID, E.EMP_TYPE, E.EMP_NAME_KR, "
				+ "CASE WHEN LENGTH(E.JUMIN_NO) >= 6 THEN SUBSTR(E.JUMIN_NO, 1, 6) ELSE E.JUMIN_NO END BIRTH_DATE, "
				+ "NVL(D.DEPARTMENT_NAME, '-') DEPARTMENT_NAME, NVL(J.JOB_POSITION_NAME, '-') JOB_POSITION_NAME, E.JOIN_DATE "
				+ "FROM PAYROLL_RUN R JOIN PAYROLL_EMPLOYEE PE ON PE.PAYROLL_RUN_ID = R.PAYROLL_RUN_ID "
				+ "JOIN EMPLOYEE E ON E.EMPLOYEE_ID = PE.EMPLOYEE_ID "
				+ "LEFT JOIN DEPARTMENT D ON D.DEPARTMENT_ID = E.DEPARTMENT_ID "
				+ "LEFT JOIN JOB_POSITION J ON J.JOB_POSITION_ID = E.JOB_POSITION_ID "
				+ "WHERE R.PAY_YEAR = ? AND R.PAY_MONTH = ? AND R.PAY_SEQ = ? "
				+ "AND (? IS NULL OR E.EMP_NAME_KR LIKE '%' || ? || '%' OR E.EMP_NO LIKE '%' || ? || '%') "
				+ "ORDER BY E.EMP_NAME_KR";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, year);
			pstmt.setString(2, month);
			pstmt.setString(3, sequence);
			String searchKeyword = keyword == null || keyword.trim().isEmpty() ? null : keyword.trim();
			pstmt.setString(4, searchKeyword);
			pstmt.setString(5, searchKeyword);
			pstmt.setString(6, searchKeyword);
			rs = pstmt.executeQuery();
			List<PayrollPayslipEmployee> result = new ArrayList<>();
			while (rs.next()) {
				PayrollPayslipEmployee employee = new PayrollPayslipEmployee();
				employee.setEmployeeId(rs.getInt("EMPLOYEE_ID"));
				employee.setEmploymentTypeName(rs.getString("EMP_TYPE"));
				employee.setEmployeeName(rs.getString("EMP_NAME_KR"));
				employee.setBirthDate(rs.getString("BIRTH_DATE"));
				employee.setDepartmentName(rs.getString("DEPARTMENT_NAME"));
				employee.setPositionName(rs.getString("JOB_POSITION_NAME"));
				employee.setHireDate(rs.getDate("JOIN_DATE"));
				result.add(employee);
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	public void fillAmounts(Connection conn, String year, String month, String sequence,
			List<PayrollPayslipEmployee> employees) throws SQLException {
		String sql = "SELECT PE.EMPLOYEE_ID, EN.PAY_ITEM_ID, EN.DEDUCT_ITEM_ID, EN.AMOUNT, "
				+ "P.PAY_NAME, P.CALC_METHOD PAY_CALC_METHOD, D.DEDUCT_NAME, D.CALC_METHOD DEDUCT_CALC_METHOD "
				+ "FROM PAYROLL_RUN R JOIN PAYROLL_EMPLOYEE PE ON PE.PAYROLL_RUN_ID = R.PAYROLL_RUN_ID "
				+ "JOIN PAYROLL_ENTRY EN ON EN.PAYROLL_EMPLOYEE_ID = PE.PAYROLL_EMPLOYEE_ID "
				+ "LEFT JOIN PAY_ITEM P ON P.PAY_ITEM_ID = EN.PAY_ITEM_ID "
				+ "LEFT JOIN DEDUCT_ITEM D ON D.DEDUCT_ITEM_ID = EN.DEDUCT_ITEM_ID "
				+ "WHERE R.PAY_YEAR = ? AND R.PAY_MONTH = ? AND R.PAY_SEQ = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, year);
			pstmt.setString(2, month);
			pstmt.setString(3, sequence);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					PayrollPayslipEmployee employee = findEmployee(employees, rs.getInt("EMPLOYEE_ID"));
					if (employee == null) {
						continue;
					}
					int payItemId = rs.getInt("PAY_ITEM_ID");
					if (!rs.wasNull()) {
						addAmount(employee.getPaymentAmounts(), payItemId, rs.getLong("AMOUNT"));
						employee.getPaymentCalculations().put(payItemId, rs.getString("PAY_CALC_METHOD"));
					}
					int deductItemId = rs.getInt("DEDUCT_ITEM_ID");
					if (!rs.wasNull()) {
						addAmount(employee.getDeductionAmounts(), deductItemId, rs.getLong("AMOUNT"));
						employee.getDeductionCalculations().put(deductItemId,
								rs.getString("DEDUCT_CALC_METHOD"));
					}
				}
			}
		}
	}

	public void fillDailyPayments(Connection conn, String year, String month, String sequence,
			List<PayrollPayslipEmployee> employees) throws SQLException {
		String sql = "SELECT PE.EMPLOYEE_ID, NVL(SUM(W.DAILY_PAY), 0) AMOUNT FROM PAYROLL_RUN R "
				+ "JOIN PAYROLL_EMPLOYEE PE ON PE.PAYROLL_RUN_ID = R.PAYROLL_RUN_ID "
				+ "LEFT JOIN DAILY_WORK_RECORD W ON W.EMPLOYEE_ID = PE.EMPLOYEE_ID "
				+ "AND W.WORK_DATE BETWEEN R.CALC_START_DATE AND R.CALC_END_DATE "
				+ "WHERE R.PAY_YEAR = ? AND R.PAY_MONTH = ? AND R.PAY_SEQ = ? AND R.INCOME_TYPE = '2' "
				+ "GROUP BY PE.EMPLOYEE_ID";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, year);
			pstmt.setString(2, month);
			pstmt.setString(3, sequence);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					PayrollPayslipEmployee employee = findEmployee(employees, rs.getInt("EMPLOYEE_ID"));
					if (employee != null) {
						employee.getPaymentAmounts().put(-1, rs.getLong("AMOUNT"));
						employee.getPaymentCalculations().put(-1, "근무일별 일급 합계");
					}
				}
			}
		}
	}

	public List<PayrollRegisterColumn> selectPaymentColumns(Connection conn, String year, String month,
			String sequence, int employeeId) throws SQLException {
		String sql = "SELECT DISTINCT P.PAY_ITEM_ID ITEM_ID, P.PAY_NAME ITEM_NAME FROM PAYROLL_RUN R "
				+ "JOIN PAYROLL_EMPLOYEE PE ON PE.PAYROLL_RUN_ID = R.PAYROLL_RUN_ID "
				+ "JOIN PAYROLL_ENTRY E ON E.PAYROLL_EMPLOYEE_ID = PE.PAYROLL_EMPLOYEE_ID "
				+ "JOIN PAY_ITEM P ON P.PAY_ITEM_ID = E.PAY_ITEM_ID WHERE R.PAY_YEAR = ? AND R.PAY_MONTH = ? "
				+ "AND R.PAY_SEQ = ? AND PE.EMPLOYEE_ID = ? ORDER BY P.PAY_ITEM_ID";
		return selectColumns(conn, sql, year, month, sequence, employeeId);
	}

	public List<PayrollRegisterColumn> selectDeductionColumns(Connection conn, String year, String month,
			String sequence, int employeeId) throws SQLException {
		String sql = "SELECT DISTINCT D.DEDUCT_ITEM_ID ITEM_ID, D.DEDUCT_NAME ITEM_NAME FROM PAYROLL_RUN R "
				+ "JOIN PAYROLL_EMPLOYEE PE ON PE.PAYROLL_RUN_ID = R.PAYROLL_RUN_ID "
				+ "JOIN PAYROLL_ENTRY E ON E.PAYROLL_EMPLOYEE_ID = PE.PAYROLL_EMPLOYEE_ID "
				+ "JOIN DEDUCT_ITEM D ON D.DEDUCT_ITEM_ID = E.DEDUCT_ITEM_ID WHERE R.PAY_YEAR = ? AND R.PAY_MONTH = ? "
				+ "AND R.PAY_SEQ = ? AND PE.EMPLOYEE_ID = ? ORDER BY D.DEDUCT_ITEM_ID";
		return selectColumns(conn, sql, year, month, sequence, employeeId);
	}

	private List<PayrollRegisterColumn> selectColumns(Connection conn, String sql, String year, String month,
			String sequence, int employeeId) throws SQLException {
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, year);
			pstmt.setString(2, month);
			pstmt.setString(3, sequence);
			pstmt.setInt(4, employeeId);
			try (ResultSet rs = pstmt.executeQuery()) {
				List<PayrollRegisterColumn> result = new ArrayList<>();
				while (rs.next()) {
					result.add(new PayrollRegisterColumn(rs.getInt("ITEM_ID"), rs.getString("ITEM_NAME")));
				}
				return result;
			}
		}
	}

	private void addAmount(java.util.Map<Integer, Long> amounts, int itemId, long amount) {
		long current = amounts.containsKey(itemId) ? amounts.get(itemId) : 0;
		amounts.put(itemId, current + amount);
	}

	private PayrollPayslipEmployee findEmployee(List<PayrollPayslipEmployee> employees, int employeeId) {
		for (PayrollPayslipEmployee employee : employees) {
			if (employee.getEmployeeId() == employeeId) {
				return employee;
			}
		}
		return null;
	}

}
