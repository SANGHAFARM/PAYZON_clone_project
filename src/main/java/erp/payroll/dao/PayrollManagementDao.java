package erp.payroll.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import erp.payroll.dto.PayrollManagementPage.PayrollManagementEmployee;
import erp.payroll.dto.PayrollManagementItem;
import erp.payroll.dto.PayrollManagementPage.PayrollPeriodOption;
import erp.payroll.model.PayrollRun;
import jdbc.JdbcUtil;

// 급여입력 화면에서 사용하는 복합 조회와 저장을 처리하는 DAO
public class PayrollManagementDao {

	public PayrollRun selectRun(Connection conn, String year, String month, String sequence, String incomeType)
			throws SQLException {
		String sql = "SELECT * FROM PAYROLL_RUN "
				+ "WHERE PAY_YEAR = ? AND PAY_MONTH = ? AND PAY_SEQ = ? AND INCOME_TYPE = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, year);
			pstmt.setString(2, month);
			pstmt.setString(3, sequence);
			pstmt.setString(4, incomeType);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return makeRun(rs);
				}
				return null;
			}
		}
	}

	public int insertRun(Connection conn, PayrollRun run) throws SQLException {
		int runId = nextRunId(conn);
		String sql = "INSERT INTO PAYROLL_RUN "
				+ "(PAYROLL_RUN_ID, PAY_YEAR, PAY_MONTH, PAY_SEQ, INCOME_TYPE, CALC_START_DATE, CALC_END_DATE, PAY_DATE) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, runId);
			pstmt.setString(2, run.getPayYear());
			pstmt.setString(3, run.getPayMonth());
			pstmt.setString(4, run.getPaySeq());
			pstmt.setString(5, run.getIncomeType());
			pstmt.setDate(6, new Date(run.getCalcStartDate().getTime()));
			pstmt.setDate(7, new Date(run.getCalcEndDate().getTime()));
			pstmt.setDate(8, new Date(run.getPayDate().getTime()));
			pstmt.executeUpdate();
			return runId;
		}
	}

	public void updateRunDates(Connection conn, int runId, PayrollRun run) throws SQLException {
		String sql = "UPDATE PAYROLL_RUN SET CALC_START_DATE = ?, CALC_END_DATE = ?, PAY_DATE = ? "
				+ "WHERE PAYROLL_RUN_ID = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setDate(1, new Date(run.getCalcStartDate().getTime()));
			pstmt.setDate(2, new Date(run.getCalcEndDate().getTime()));
			pstmt.setDate(3, new Date(run.getPayDate().getTime()));
			pstmt.setInt(4, runId);
			pstmt.executeUpdate();
		}
	}

	public List<PayrollManagementEmployee> selectPayrollEmployees(Connection conn, int runId) throws SQLException {
		String sql = "SELECT E.EMPLOYEE_ID, E.EMP_TYPE, E.EMP_NO, E.EMP_NAME_KR, "
				+ "NVL(D.DEPARTMENT_NAME, '-') DEPARTMENT_NAME, NVL(J.JOB_POSITION_NAME, '-') JOB_POSITION_NAME, E.STATUS, "
				+ "NVL(SUM(CASE WHEN EN.PAY_ITEM_ID IS NOT NULL THEN EN.AMOUNT ELSE 0 END), 0) GROSS_PAYMENT, "
				+ "NVL(SUM(CASE WHEN EN.DEDUCT_ITEM_ID IS NOT NULL THEN EN.AMOUNT ELSE 0 END), 0) TOTAL_DEDUCTION "
				+ "FROM PAYROLL_EMPLOYEE PE JOIN EMPLOYEE E ON E.EMPLOYEE_ID = PE.EMPLOYEE_ID "
				+ "LEFT JOIN DEPARTMENT D ON D.DEPARTMENT_ID = E.DEPARTMENT_ID "
				+ "LEFT JOIN JOB_POSITION J ON J.JOB_POSITION_ID = E.JOB_POSITION_ID "
				+ "LEFT JOIN PAYROLL_ENTRY EN ON EN.PAYROLL_EMPLOYEE_ID = PE.PAYROLL_EMPLOYEE_ID "
				+ "WHERE PE.PAYROLL_RUN_ID = ? "
				+ "GROUP BY E.EMPLOYEE_ID, E.EMP_TYPE, E.EMP_NO, E.EMP_NAME_KR, D.DEPARTMENT_NAME, J.JOB_POSITION_NAME, E.STATUS "
				+ "ORDER BY E.EMP_NAME_KR";
		return selectEmployees(conn, sql, runId, null, 0, 0);
	}

	public List<PayrollManagementEmployee> selectAvailableEmployees(Connection conn, int runId, String keyword,
			int page, int size) throws SQLException {
		String sql = "SELECT * FROM (SELECT A.*, ROWNUM RNUM FROM ("
				+ "SELECT E.EMPLOYEE_ID, E.EMP_TYPE, E.EMP_NO, E.EMP_NAME_KR, "
				+ "NVL(D.DEPARTMENT_NAME, '-') DEPARTMENT_NAME, NVL(J.JOB_POSITION_NAME, '-') JOB_POSITION_NAME, E.STATUS, "
				+ "0 GROSS_PAYMENT, 0 TOTAL_DEDUCTION FROM EMPLOYEE E "
				+ "LEFT JOIN DEPARTMENT D ON D.DEPARTMENT_ID = E.DEPARTMENT_ID "
				+ "LEFT JOIN JOB_POSITION J ON J.JOB_POSITION_ID = E.JOB_POSITION_ID "
				+ "WHERE NOT EXISTS (SELECT 1 FROM PAYROLL_EMPLOYEE PE WHERE PE.PAYROLL_RUN_ID = ? AND PE.EMPLOYEE_ID = E.EMPLOYEE_ID) "
				+ "AND (? IS NULL OR E.EMP_NAME_KR LIKE '%' || ? || '%' OR E.EMP_NO LIKE '%' || ? || '%') "
				+ "ORDER BY E.EMP_NAME_KR) A WHERE ROWNUM <= ?) WHERE RNUM >= ?";
		return selectEmployees(conn, sql, runId, keyword, page, size);
	}

	public int countAvailableEmployees(Connection conn, int runId, String keyword) throws SQLException {
		String sql = "SELECT COUNT(*) FROM EMPLOYEE E WHERE NOT EXISTS "
				+ "(SELECT 1 FROM PAYROLL_EMPLOYEE PE WHERE PE.PAYROLL_RUN_ID = ? AND PE.EMPLOYEE_ID = E.EMPLOYEE_ID) "
				+ "AND (? IS NULL OR E.EMP_NAME_KR LIKE '%' || ? || '%' OR E.EMP_NO LIKE '%' || ? || '%')";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, runId);
			setKeyword(pstmt, 2, keyword);
			try (ResultSet rs = pstmt.executeQuery()) {
				rs.next();
				return rs.getInt(1);
			}
		}
	}

	public List<PayrollManagementItem> selectPayItems(Connection conn, Integer payrollEmployeeId) throws SQLException {
		String sql = "SELECT P.PAY_ITEM_ID ITEM_CODE, P.PAY_NAME ITEM_NAME, P.TAX_TYPE, P.CALC_METHOD, "
				+ "NVL(E.AMOUNT, 0) AMOUNT FROM PAY_ITEM P LEFT JOIN PAYROLL_ENTRY E "
				+ "ON E.PAY_ITEM_ID = P.PAY_ITEM_ID AND E.PAYROLL_EMPLOYEE_ID = ? "
				+ "WHERE P.USE_YN = 'Y' ORDER BY P.PAY_ITEM_ID";
		return selectItems(conn, sql, payrollEmployeeId, true);
	}

	public List<PayrollManagementItem> selectDeductItems(Connection conn, Integer payrollEmployeeId)
			throws SQLException {
		String sql = "SELECT D.DEDUCT_ITEM_ID ITEM_CODE, D.DEDUCT_NAME ITEM_NAME, '전체과세' TAX_TYPE, D.CALC_METHOD, "
				+ "NVL(E.AMOUNT, 0) AMOUNT FROM DEDUCT_ITEM D LEFT JOIN PAYROLL_ENTRY E "
				+ "ON E.DEDUCT_ITEM_ID = D.DEDUCT_ITEM_ID AND E.PAYROLL_EMPLOYEE_ID = ? "
				+ "WHERE D.USE_YN = 'Y' ORDER BY D.DEDUCT_ITEM_ID";
		return selectItems(conn, sql, payrollEmployeeId, false);
	}

	public Integer selectPayrollEmployeeId(Connection conn, int runId, int employeeId) throws SQLException {
		String sql = "SELECT PAYROLL_EMPLOYEE_ID FROM PAYROLL_EMPLOYEE WHERE PAYROLL_RUN_ID = ? AND EMPLOYEE_ID = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, runId);
			pstmt.setInt(2, employeeId);
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next() ? rs.getInt(1) : null;
			}
		}
	}

	public void insertPayrollEmployee(Connection conn, int runId, int employeeId) throws SQLException {
		String sql = "INSERT INTO PAYROLL_EMPLOYEE (PAYROLL_EMPLOYEE_ID, PAYROLL_RUN_ID, EMPLOYEE_ID) "
				+ "VALUES (PAYROLL_EMPLOYEE_SEQ.NEXTVAL, ?, ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, runId);
			pstmt.setInt(2, employeeId);
			pstmt.executeUpdate();
		}
	}

	public void deletePayrollEmployees(Connection conn, int runId, int[] employeeIds, boolean deleteAll)
			throws SQLException {
		String sql = deleteAll ? "DELETE FROM PAYROLL_EMPLOYEE WHERE PAYROLL_RUN_ID = ?"
				: "DELETE FROM PAYROLL_EMPLOYEE WHERE PAYROLL_RUN_ID = ? AND EMPLOYEE_ID = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			if (deleteAll) {
				pstmt.setInt(1, runId);
				pstmt.executeUpdate();
				return;
			}
			for (int employeeId : employeeIds) {
				pstmt.setInt(1, runId);
				pstmt.setInt(2, employeeId);
				pstmt.addBatch();
			}
			pstmt.executeBatch();
		}
	}

	public void replaceEntries(Connection conn, int payrollEmployeeId, List<PayrollManagementItem> payItems,
			List<PayrollManagementItem> deductItems) throws SQLException {
		mergeEntries(conn, payrollEmployeeId, payItems, true);
		mergeEntries(conn, payrollEmployeeId, deductItems, false);
		deleteUnusedEntries(conn, payrollEmployeeId);
	}

	public List<PayrollPeriodOption> selectPreviousRuns(Connection conn, String incomeType) throws SQLException {
		String sql = "SELECT PAYROLL_RUN_ID, PAY_YEAR, PAY_MONTH, PAY_SEQ FROM PAYROLL_RUN "
				+ "WHERE INCOME_TYPE = ? ORDER BY PAY_YEAR DESC, PAY_MONTH DESC, PAY_SEQ DESC";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, incomeType);
			try (ResultSet rs = pstmt.executeQuery()) {
				List<PayrollPeriodOption> result = new ArrayList<>();
				while (rs.next()) {
					String name = rs.getString("PAY_YEAR") + "-" + rs.getString("PAY_MONTH") + " 급여-"
							+ Integer.parseInt(rs.getString("PAY_SEQ")) + "차";
					result.add(new PayrollPeriodOption(rs.getInt("PAYROLL_RUN_ID"), name));
				}
				return result;
			}
		}
	}

	public void copyPreviousRun(Connection conn, int previousRunId, int currentRunId) throws SQLException {
		try (PreparedStatement delete = conn.prepareStatement(
				"DELETE FROM PAYROLL_EMPLOYEE WHERE PAYROLL_RUN_ID = ?")) {
			delete.setInt(1, currentRunId);
			delete.executeUpdate();
		}
		String employeeSql = "INSERT INTO PAYROLL_EMPLOYEE (PAYROLL_EMPLOYEE_ID, PAYROLL_RUN_ID, EMPLOYEE_ID) "
				+ "SELECT PAYROLL_EMPLOYEE_SEQ.NEXTVAL, ?, EMPLOYEE_ID FROM PAYROLL_EMPLOYEE WHERE PAYROLL_RUN_ID = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(employeeSql)) {
			pstmt.setInt(1, currentRunId);
			pstmt.setInt(2, previousRunId);
			pstmt.executeUpdate();
		}
		String entrySql = "INSERT INTO PAYROLL_ENTRY "
				+ "(PAYROLL_ENTRY_ID, PAYROLL_EMPLOYEE_ID, PAY_ITEM_ID, DEDUCT_ITEM_ID, AMOUNT) "
				+ "SELECT PAYROLL_ENTRY_SEQ.NEXTVAL, CUR.PAYROLL_EMPLOYEE_ID, EN.PAY_ITEM_ID, EN.DEDUCT_ITEM_ID, EN.AMOUNT "
				+ "FROM PAYROLL_EMPLOYEE PRE JOIN PAYROLL_ENTRY EN ON EN.PAYROLL_EMPLOYEE_ID = PRE.PAYROLL_EMPLOYEE_ID "
				+ "JOIN PAYROLL_EMPLOYEE CUR ON CUR.EMPLOYEE_ID = PRE.EMPLOYEE_ID AND CUR.PAYROLL_RUN_ID = ? "
				+ "WHERE PRE.PAYROLL_RUN_ID = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(entrySql)) {
			pstmt.setInt(1, currentRunId);
			pstmt.setInt(2, previousRunId);
			pstmt.executeUpdate();
		}
	}

	public void managePayItem(Connection conn, String action, Integer itemId, String itemName, String taxType,
			String taxFreeCode, long taxFreeLimit, String calculationMethod, int roundUnit, String payMethod,
			Integer attendanceItemId, Long bulkAmount) throws SQLException {
		if ("deleteAll".equals(action)) {
			executeUpdate(conn, "UPDATE PAY_ITEM SET USE_YN = 'N'", new Object[0]);
		} else if ("delete".equals(action)) {
			executeUpdate(conn, "UPDATE PAY_ITEM SET USE_YN = 'N' WHERE PAY_ITEM_ID = ?", itemId);
		} else if ("update".equals(action)) {
			String sql = "UPDATE PAY_ITEM SET PAY_NAME = ?, TAX_TYPE = ?, TAX_FREE_CODE = ?, TAX_FREE_LIMIT = ?, "
					+ "CALC_METHOD = ?, ROUND_UNIT = ?, PAY_METHOD = ?, LINK_ATTEND_ID = ?, BULK_PAY_AMOUNT = ?, USE_YN = 'Y' "
					+ "WHERE PAY_ITEM_ID = ?";
			executeUpdate(conn, sql, itemName, taxType, taxFreeCode, taxFreeLimit, calculationMethod, roundUnit,
					payMethod, attendanceItemId, bulkAmount, itemId);
		} else {
			String sql = "INSERT INTO PAY_ITEM (PAY_ITEM_ID, PAY_NAME, TAX_TYPE, TAX_FREE_CODE, TAX_FREE_LIMIT, "
					+ "CALC_METHOD, ROUND_UNIT, PAY_METHOD, LINK_ATTEND_ID, BULK_PAY_AMOUNT, USE_YN) "
					+ "VALUES (PAY_ITEM_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'Y')";
			executeUpdate(conn, sql, itemName, taxType, taxFreeCode, taxFreeLimit, calculationMethod, roundUnit,
					payMethod, attendanceItemId, bulkAmount);
		}
	}

	public void manageDeductItem(Connection conn, String action, Integer itemId, String itemName,
			String calculationMethod, int roundUnit, String note) throws SQLException {
		if ("deleteAll".equals(action)) {
			executeUpdate(conn, "UPDATE DEDUCT_ITEM SET USE_YN = 'N'", new Object[0]);
		} else if ("delete".equals(action)) {
			executeUpdate(conn, "UPDATE DEDUCT_ITEM SET USE_YN = 'N' WHERE DEDUCT_ITEM_ID = ?", itemId);
		} else if ("update".equals(action)) {
			String sql = "UPDATE DEDUCT_ITEM SET DEDUCT_NAME = ?, CALC_METHOD = ?, ROUND_UNIT = ?, NOTE = ?, "
					+ "USE_YN = 'Y' WHERE DEDUCT_ITEM_ID = ?";
			executeUpdate(conn, sql, itemName, calculationMethod, roundUnit, note, itemId);
		} else {
			String sql = "INSERT INTO DEDUCT_ITEM (DEDUCT_ITEM_ID, DEDUCT_NAME, CALC_METHOD, ROUND_UNIT, NOTE, USE_YN) "
					+ "VALUES (DEDUCT_ITEM_SEQ.NEXTVAL, ?, ?, ?, ?, 'Y')";
			executeUpdate(conn, sql, itemName, calculationMethod, roundUnit, note);
		}
	}

	private void executeUpdate(Connection conn, String sql, Object... values) throws SQLException {
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			for (int i = 0; i < values.length; i++) {
				pstmt.setObject(i + 1, values[i]);
			}
			pstmt.executeUpdate();
		}
	}

	private int nextRunId(Connection conn) throws SQLException {
		try (PreparedStatement pstmt = conn.prepareStatement("SELECT PAYROLL_RUN_SEQ.NEXTVAL FROM DUAL");
				ResultSet rs = pstmt.executeQuery()) {
			rs.next();
			return rs.getInt(1);
		}
	}

	private void mergeEntries(Connection conn, int payrollEmployeeId, List<PayrollManagementItem> items,
			boolean payItem) throws SQLException {
		String itemColumn = payItem ? "PAY_ITEM_ID" : "DEDUCT_ITEM_ID";
		String otherColumn = payItem ? "DEDUCT_ITEM_ID" : "PAY_ITEM_ID";
		String sql = "MERGE INTO PAYROLL_ENTRY E USING (SELECT ? PAYROLL_EMPLOYEE_ID, ? ITEM_ID, ? AMOUNT FROM DUAL) S "
				+ "ON (E.PAYROLL_EMPLOYEE_ID = S.PAYROLL_EMPLOYEE_ID AND E." + itemColumn + " = S.ITEM_ID) "
				+ "WHEN MATCHED THEN UPDATE SET E.AMOUNT = S.AMOUNT "
				+ "WHEN NOT MATCHED THEN INSERT "
				+ "(PAYROLL_ENTRY_ID, PAYROLL_EMPLOYEE_ID, " + itemColumn + ", " + otherColumn + ", AMOUNT) "
				+ "VALUES (PAYROLL_ENTRY_SEQ.NEXTVAL, S.PAYROLL_EMPLOYEE_ID, S.ITEM_ID, NULL, S.AMOUNT)";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			Set<Integer> insertedItemIds = new HashSet<>();
			for (PayrollManagementItem item : items) {
				if (item.getItemCode() <= 0 || !insertedItemIds.add(item.getItemCode())) {
					continue;
				}
				pstmt.setInt(1, payrollEmployeeId);
				pstmt.setInt(2, item.getItemCode());
				pstmt.setLong(3, item.getAmount());
				pstmt.executeUpdate();
			}
		}
	}

	private void deleteUnusedEntries(Connection conn, int payrollEmployeeId) throws SQLException {
		String sql = "DELETE FROM PAYROLL_ENTRY E WHERE E.PAYROLL_EMPLOYEE_ID = ? AND ("
				+ "(E.PAY_ITEM_ID IS NOT NULL AND NOT EXISTS "
				+ "(SELECT 1 FROM PAY_ITEM P WHERE P.PAY_ITEM_ID = E.PAY_ITEM_ID AND P.USE_YN = 'Y')) OR "
				+ "(E.DEDUCT_ITEM_ID IS NOT NULL AND NOT EXISTS "
				+ "(SELECT 1 FROM DEDUCT_ITEM D WHERE D.DEDUCT_ITEM_ID = E.DEDUCT_ITEM_ID AND D.USE_YN = 'Y')))";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, payrollEmployeeId);
			pstmt.executeUpdate();
		}
	}

	private List<PayrollManagementEmployee> selectEmployees(Connection conn, String sql, int runId,
			String keyword, int page, int size) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, runId);
			if (size > 0) {
				setKeyword(pstmt, 2, keyword);
				pstmt.setInt(5, page * size);
				pstmt.setInt(6, (page - 1) * size + 1);
			}
			rs = pstmt.executeQuery();
			List<PayrollManagementEmployee> result = new ArrayList<>();
			while (rs.next()) {
				PayrollManagementEmployee employee = new PayrollManagementEmployee();
				employee.setEmployeeId(rs.getInt("EMPLOYEE_ID"));
				employee.setEmploymentType(rs.getString("EMP_TYPE"));
				employee.setEmployeeNo(rs.getString("EMP_NO"));
				employee.setName(rs.getString("EMP_NAME_KR"));
				employee.setDepartmentName(rs.getString("DEPARTMENT_NAME"));
				employee.setPositionName(rs.getString("JOB_POSITION_NAME"));
				employee.setStatusName(rs.getString("STATUS"));
				employee.setGrossPayment(rs.getLong("GROSS_PAYMENT"));
				employee.setTotalDeduction(rs.getLong("TOTAL_DEDUCTION"));
				result.add(employee);
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	private List<PayrollManagementItem> selectItems(Connection conn, String sql, Integer payrollEmployeeId,
			boolean payItem) throws SQLException {
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			if (payrollEmployeeId == null) {
				pstmt.setNull(1, java.sql.Types.NUMERIC);
			} else {
				pstmt.setInt(1, payrollEmployeeId);
			}
			try (ResultSet rs = pstmt.executeQuery()) {
				List<PayrollManagementItem> result = new ArrayList<>();
				while (rs.next()) {
					PayrollManagementItem item = new PayrollManagementItem();
					item.setItemCode(rs.getInt("ITEM_CODE"));
					item.setItemName(rs.getString("ITEM_NAME"));
					item.setTaxFree(payItem && "비과세".equals(rs.getString("TAX_TYPE")));
					item.setCalculationMethod(rs.getString("CALC_METHOD"));
					item.setAmount(rs.getLong("AMOUNT"));
					result.add(item);
				}
				return result;
			}
		}
	}

	private void setKeyword(PreparedStatement pstmt, int index, String keyword) throws SQLException {
		String value = keyword == null || keyword.trim().isEmpty() ? null : keyword.trim();
		pstmt.setString(index, value);
		pstmt.setString(index + 1, value);
		pstmt.setString(index + 2, value);
	}

	private PayrollRun makeRun(ResultSet rs) throws SQLException {
		PayrollRun run = new PayrollRun();
		run.setPayrollRunId(rs.getInt("PAYROLL_RUN_ID"));
		run.setPayYear(rs.getString("PAY_YEAR"));
		run.setPayMonth(rs.getString("PAY_MONTH"));
		run.setPaySeq(rs.getString("PAY_SEQ"));
		run.setIncomeType(rs.getString("INCOME_TYPE"));
		run.setCalcStartDate(rs.getDate("CALC_START_DATE"));
		run.setCalcEndDate(rs.getDate("CALC_END_DATE"));
		run.setPayDate(rs.getDate("PAY_DATE"));
		return run;
	}
}
