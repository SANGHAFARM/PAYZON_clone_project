package erp.payroll.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import erp.payroll.dto.PayrollRegisterColumn;
import erp.payroll.dto.PayrollRegisterPage.PayrollRegisterEmployee;
import erp.payroll.dto.PayrollRegisterPage.PayrollRegisterItem;
import jdbc.JdbcUtil;

// 급여대장 목록과 상세 집계를 조회하는 DAO
public class PayrollRegisterDao {

	public int countRuns(Connection conn, String year) throws SQLException {
		String sql = "SELECT COUNT(*) FROM PAYROLL_RUN WHERE PAY_YEAR = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, year);
			try (ResultSet rs = pstmt.executeQuery()) {
				rs.next();
				return rs.getInt(1);
			}
		}
	}

	public List<PayrollRegisterItem> selectRuns(Connection conn, String year, int startRow, int size)
			throws SQLException {
		String payTotal = "CASE WHEN R.INCOME_TYPE = '2' THEN NVL((SELECT SUM(W.DAILY_PAY) "
				+ "FROM PAYROLL_EMPLOYEE DP JOIN DAILY_WORK_RECORD W ON W.EMPLOYEE_ID = DP.EMPLOYEE_ID "
				+ "WHERE DP.PAYROLL_RUN_ID = R.PAYROLL_RUN_ID AND W.WORK_DATE BETWEEN R.CALC_START_DATE "
				+ "AND R.CALC_END_DATE), 0) ELSE NVL((SELECT SUM(E.AMOUNT) FROM PAYROLL_EMPLOYEE PP "
				+ "JOIN PAYROLL_ENTRY E ON E.PAYROLL_EMPLOYEE_ID = PP.PAYROLL_EMPLOYEE_ID "
				+ "WHERE PP.PAYROLL_RUN_ID = R.PAYROLL_RUN_ID AND E.PAY_ITEM_ID IS NOT NULL), 0) END";
		String inner = "SELECT R.PAYROLL_RUN_ID, R.PAY_YEAR, R.PAY_MONTH, R.PAY_SEQ, R.INCOME_TYPE, R.CALC_START_DATE, "
				+ "R.CALC_END_DATE, R.PAY_DATE, (SELECT COUNT(*) FROM PAYROLL_EMPLOYEE PE "
				+ "WHERE PE.PAYROLL_RUN_ID = R.PAYROLL_RUN_ID) EMPLOYEE_COUNT, " + payTotal
				+ " TOTAL_PAYMENT, NVL((SELECT SUM(E.AMOUNT) FROM PAYROLL_EMPLOYEE PE "
				+ "JOIN PAYROLL_ENTRY E ON E.PAYROLL_EMPLOYEE_ID = PE.PAYROLL_EMPLOYEE_ID "
				+ "WHERE PE.PAYROLL_RUN_ID = R.PAYROLL_RUN_ID AND E.DEDUCT_ITEM_ID IS NOT NULL), 0) TOTAL_DEDUCTION "
				+ "FROM PAYROLL_RUN R WHERE R.PAY_YEAR = ? "
				+ "ORDER BY R.PAY_MONTH DESC, R.PAY_SEQ DESC, R.INCOME_TYPE";
		String sql = "SELECT * FROM (SELECT A.*, ROWNUM RNUM FROM (" + inner
				+ ") A WHERE ROWNUM <= ?) WHERE RNUM >= ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, year);
			pstmt.setInt(2, startRow + size);
			pstmt.setInt(3, startRow + 1);
			rs = pstmt.executeQuery();
			List<PayrollRegisterItem> result = new ArrayList<>();
			while (rs.next()) {
				result.add(makeRegister(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	public PayrollRegisterItem selectRunById(Connection conn, int runId) throws SQLException {
		String sql = "SELECT R.PAYROLL_RUN_ID, R.PAY_YEAR, R.PAY_MONTH, R.PAY_SEQ, R.CALC_START_DATE, "
				+ "R.CALC_END_DATE, R.PAY_DATE, R.INCOME_TYPE, "
				+ "(SELECT COUNT(*) FROM PAYROLL_EMPLOYEE PE WHERE PE.PAYROLL_RUN_ID = R.PAYROLL_RUN_ID) EMPLOYEE_COUNT, "
				+ "CASE WHEN R.INCOME_TYPE = '2' THEN NVL((SELECT SUM(W.DAILY_PAY) FROM PAYROLL_EMPLOYEE DP "
				+ "JOIN DAILY_WORK_RECORD W ON W.EMPLOYEE_ID = DP.EMPLOYEE_ID WHERE DP.PAYROLL_RUN_ID = R.PAYROLL_RUN_ID "
				+ "AND W.WORK_DATE BETWEEN R.CALC_START_DATE AND R.CALC_END_DATE), 0) "
				+ "ELSE NVL((SELECT SUM(E.AMOUNT) FROM PAYROLL_EMPLOYEE PP JOIN PAYROLL_ENTRY E "
				+ "ON E.PAYROLL_EMPLOYEE_ID = PP.PAYROLL_EMPLOYEE_ID WHERE PP.PAYROLL_RUN_ID = R.PAYROLL_RUN_ID "
				+ "AND E.PAY_ITEM_ID IS NOT NULL), 0) END TOTAL_PAYMENT, "
				+ "NVL((SELECT SUM(E.AMOUNT) FROM PAYROLL_EMPLOYEE PE JOIN PAYROLL_ENTRY E "
				+ "ON E.PAYROLL_EMPLOYEE_ID = PE.PAYROLL_EMPLOYEE_ID WHERE PE.PAYROLL_RUN_ID = R.PAYROLL_RUN_ID "
				+ "AND E.DEDUCT_ITEM_ID IS NOT NULL), 0) TOTAL_DEDUCTION "
				+ "FROM PAYROLL_RUN R WHERE R.PAYROLL_RUN_ID = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, runId);
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next() ? makeRegister(rs) : null;
			}
		}
	}

	public List<PayrollRegisterColumn> selectPayColumns(Connection conn, int runId) throws SQLException {
		String sql = "SELECT DISTINCT P.PAY_ITEM_ID ITEM_ID, P.PAY_NAME ITEM_NAME FROM PAYROLL_EMPLOYEE PE "
				+ "JOIN PAYROLL_ENTRY E ON E.PAYROLL_EMPLOYEE_ID = PE.PAYROLL_EMPLOYEE_ID "
				+ "JOIN PAY_ITEM P ON P.PAY_ITEM_ID = E.PAY_ITEM_ID "
				+ "WHERE PE.PAYROLL_RUN_ID = ? ORDER BY P.PAY_ITEM_ID";
		return selectColumns(conn, sql, runId);
	}

	public List<PayrollRegisterColumn> selectDeductColumns(Connection conn, int runId) throws SQLException {
		String sql = "SELECT DISTINCT D.DEDUCT_ITEM_ID ITEM_ID, D.DEDUCT_NAME ITEM_NAME FROM PAYROLL_EMPLOYEE PE "
				+ "JOIN PAYROLL_ENTRY E ON E.PAYROLL_EMPLOYEE_ID = PE.PAYROLL_EMPLOYEE_ID "
				+ "JOIN DEDUCT_ITEM D ON D.DEDUCT_ITEM_ID = E.DEDUCT_ITEM_ID "
				+ "WHERE PE.PAYROLL_RUN_ID = ? ORDER BY D.DEDUCT_ITEM_ID";
		return selectColumns(conn, sql, runId);
	}

	public List<PayrollRegisterEmployee> selectEmployees(Connection conn, int runId, String employmentType,
			Integer departmentId) throws SQLException {
		String sql = "SELECT E.EMPLOYEE_ID, E.EMP_TYPE, E.EMP_NAME_KR, NVL(D.DEPARTMENT_NAME, '-') DEPARTMENT_NAME, "
				+ "NVL(J.JOB_POSITION_NAME, '-') JOB_POSITION_NAME FROM PAYROLL_EMPLOYEE PE "
				+ "JOIN EMPLOYEE E ON E.EMPLOYEE_ID = PE.EMPLOYEE_ID "
				+ "LEFT JOIN DEPARTMENT D ON D.DEPARTMENT_ID = E.DEPARTMENT_ID "
				+ "LEFT JOIN JOB_POSITION J ON J.JOB_POSITION_ID = E.JOB_POSITION_ID "
				+ "WHERE PE.PAYROLL_RUN_ID = ? AND (? IS NULL OR E.EMP_TYPE = ?) "
				+ "AND (? IS NULL OR E.DEPARTMENT_ID = ?) ORDER BY E.EMP_NAME_KR";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, runId);
			pstmt.setString(2, employmentType);
			pstmt.setString(3, employmentType);
			pstmt.setObject(4, departmentId);
			pstmt.setObject(5, departmentId);
			rs = pstmt.executeQuery();
			List<PayrollRegisterEmployee> result = new ArrayList<>();
			while (rs.next()) {
				PayrollRegisterEmployee employee = new PayrollRegisterEmployee();
				employee.setEmployeeId(rs.getInt("EMPLOYEE_ID"));
				employee.setEmploymentTypeName(rs.getString("EMP_TYPE"));
				employee.setEmployeeName(rs.getString("EMP_NAME_KR"));
				employee.setDepartmentName(rs.getString("DEPARTMENT_NAME"));
				employee.setPositionName(rs.getString("JOB_POSITION_NAME"));
				result.add(employee);
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	public void fillEntryAmounts(Connection conn, int runId, List<PayrollRegisterEmployee> employees)
			throws SQLException {
		String sql = "SELECT PE.EMPLOYEE_ID, E.PAY_ITEM_ID, E.DEDUCT_ITEM_ID, E.AMOUNT "
				+ "FROM PAYROLL_EMPLOYEE PE JOIN PAYROLL_ENTRY E "
				+ "ON E.PAYROLL_EMPLOYEE_ID = PE.PAYROLL_EMPLOYEE_ID WHERE PE.PAYROLL_RUN_ID = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, runId);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					PayrollRegisterEmployee employee = findEmployee(employees, rs.getInt("EMPLOYEE_ID"));
					if (employee == null) {
						continue;
					}
					int payItemId = rs.getInt("PAY_ITEM_ID");
					if (!rs.wasNull()) {
						employee.getPaymentAmounts().put(payItemId, rs.getLong("AMOUNT"));
					}
					int deductItemId = rs.getInt("DEDUCT_ITEM_ID");
					if (!rs.wasNull()) {
						employee.getDeductionAmounts().put(deductItemId, rs.getLong("AMOUNT"));
					}
				}
			}
		}
	}

	public void fillDailyPayments(Connection conn, int runId, List<PayrollRegisterEmployee> employees)
			throws SQLException {
		String sql = "SELECT PE.EMPLOYEE_ID, NVL(SUM(W.DAILY_PAY), 0) AMOUNT FROM PAYROLL_EMPLOYEE PE "
				+ "JOIN PAYROLL_RUN R ON R.PAYROLL_RUN_ID = PE.PAYROLL_RUN_ID "
				+ "LEFT JOIN DAILY_WORK_RECORD W ON W.EMPLOYEE_ID = PE.EMPLOYEE_ID "
				+ "AND W.WORK_DATE BETWEEN R.CALC_START_DATE AND R.CALC_END_DATE "
				+ "WHERE PE.PAYROLL_RUN_ID = ? GROUP BY PE.EMPLOYEE_ID";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, runId);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					PayrollRegisterEmployee employee = findEmployee(employees, rs.getInt("EMPLOYEE_ID"));
					if (employee != null) {
						employee.getPaymentAmounts().put(-1, rs.getLong("AMOUNT"));
					}
				}
			}
		}
	}

	public void deleteRun(Connection conn, int runId) throws SQLException {
		try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM PAYROLL_RUN WHERE PAYROLL_RUN_ID = ?")) {
			pstmt.setInt(1, runId);
			pstmt.executeUpdate();
		}
	}

	private List<PayrollRegisterColumn> selectColumns(Connection conn, String sql, int runId) throws SQLException {
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, runId);
			try (ResultSet rs = pstmt.executeQuery()) {
			List<PayrollRegisterColumn> result = new ArrayList<>();
			while (rs.next()) {
				result.add(new PayrollRegisterColumn(rs.getInt("ITEM_ID"), rs.getString("ITEM_NAME")));
			}
			return result;
			}
		}
	}

	private PayrollRegisterEmployee findEmployee(List<PayrollRegisterEmployee> employees, int employeeId) {
		for (PayrollRegisterEmployee employee : employees) {
			if (employee.getEmployeeId() == employeeId) {
				return employee;
			}
		}
		return null;
	}

	private PayrollRegisterItem makeRegister(ResultSet rs) throws SQLException {
		PayrollRegisterItem item = new PayrollRegisterItem();
		item.setRegisterId(rs.getInt("PAYROLL_RUN_ID"));
		item.setPaymentYear(rs.getString("PAY_YEAR"));
		item.setPaymentYearMonth(rs.getString("PAY_YEAR") + "-" + rs.getString("PAY_MONTH"));
		String incomeType = rs.getString("INCOME_TYPE");
		String incomeName = getIncomeName(incomeType);
		item.setPaymentRoundName("급여-" + Integer.parseInt(rs.getString("PAY_SEQ")) + "차 (" + incomeName + ")");
		item.setIncomeType(incomeType);
		item.setCalculationStart(rs.getDate("CALC_START_DATE"));
		item.setCalculationEnd(rs.getDate("CALC_END_DATE"));
		item.setPaymentDate(rs.getDate("PAY_DATE"));
		item.setEmployeeCount(rs.getInt("EMPLOYEE_COUNT"));
		item.setTotalPayment(rs.getLong("TOTAL_PAYMENT"));
		item.setTotalDeduction(rs.getLong("TOTAL_DEDUCTION"));
		return item;
	}

	private String getIncomeName(String incomeType) {
		if ("0".equals(incomeType)) {
			return "일반";
		} else if ("1".equals(incomeType)) {
			return "사업·기타";
		}
		return "일용직";
	}
}
