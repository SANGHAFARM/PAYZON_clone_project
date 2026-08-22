package erp.payroll.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import erp.payroll.dto.PayrollItemLedgerPage.PayrollItemLedgerOption;
import erp.payroll.dto.PayrollItemLedgerPage.PayrollItemLedgerRow;

// 지급·공제 항목별 사원 금액을 조회한다.
// 급여항목Ledger 데이터를 데이터베이스에서 조회하고 저장·수정·삭제한다.
// 給与項目Ledgerデータをデータベースから照会し、登録・更新・削除する。
public class PayrollItemLedgerDao {

	// 조회 조건에 맞는 항목 목록 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合う項目一覧データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public List<PayrollItemLedgerOption> selectItems(Connection conn) throws SQLException {
		String sql = "SELECT ITEM_CODE, ITEM_NAME FROM ("
				+ "SELECT 'P' || PAY_ITEM_ID ITEM_CODE, PAY_NAME ITEM_NAME, 1 ITEM_TYPE, PAY_ITEM_ID ITEM_ID "
				+ "FROM PAY_ITEM WHERE USE_YN = 'Y' UNION ALL "
				+ "SELECT 'D' || DEDUCT_ITEM_ID, DEDUCT_NAME, 2, DEDUCT_ITEM_ID "
				+ "FROM DEDUCT_ITEM WHERE USE_YN = 'Y') ORDER BY ITEM_TYPE, ITEM_ID";
		try (PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {
			List<PayrollItemLedgerOption> result = new ArrayList<>();
			while (rs.next()) {
				result.add(new PayrollItemLedgerOption(rs.getString("ITEM_CODE"), rs.getString("ITEM_NAME")));
			}
			return result;
		}
	}

	// 조회 조건에 맞는 행 목록 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合う行一覧データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public List<PayrollItemLedgerRow> selectRows(Connection conn, String startMonth, String endMonth,
			String itemCode, List<String> months) throws SQLException {
		boolean paymentItem = itemCode.startsWith("P");
		int itemId = Integer.parseInt(itemCode.substring(1));
		String itemCondition = paymentItem ? "EN.PAY_ITEM_ID = ?" : "EN.DEDUCT_ITEM_ID = ?";
		String sql = "SELECT E.EMPLOYEE_ID, E.EMP_TYPE, E.EMP_NAME_KR, "
				+ "NVL(D.DEPARTMENT_NAME, '-') DEPARTMENT_NAME, NVL(J.JOB_POSITION_NAME, '-') POSITION_NAME, "
				+ "R.PAY_YEAR || '-' || R.PAY_MONTH PAY_MONTH, SUM(EN.AMOUNT) AMOUNT "
				+ "FROM PAYROLL_RUN R JOIN PAYROLL_EMPLOYEE PE ON PE.PAYROLL_RUN_ID = R.PAYROLL_RUN_ID "
				+ "JOIN PAYROLL_ENTRY EN ON EN.PAYROLL_EMPLOYEE_ID = PE.PAYROLL_EMPLOYEE_ID "
				+ "JOIN EMPLOYEE E ON E.EMPLOYEE_ID = PE.EMPLOYEE_ID "
				+ "LEFT JOIN DEPARTMENT D ON D.DEPARTMENT_ID = E.DEPARTMENT_ID "
				+ "LEFT JOIN JOB_POSITION J ON J.JOB_POSITION_ID = E.JOB_POSITION_ID "
				+ "WHERE R.PAY_YEAR || R.PAY_MONTH BETWEEN ? AND ? AND " + itemCondition + " "
				+ "GROUP BY E.EMPLOYEE_ID, E.EMP_TYPE, E.EMP_NAME_KR, D.DEPARTMENT_NAME, "
				+ "J.JOB_POSITION_NAME, R.PAY_YEAR, R.PAY_MONTH ORDER BY E.EMP_NAME_KR, E.EMPLOYEE_ID";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, startMonth.replace("-", ""));
			pstmt.setString(2, endMonth.replace("-", ""));
			pstmt.setInt(3, itemId);
			try (ResultSet rs = pstmt.executeQuery()) {
				Map<Integer, PayrollItemLedgerRow> rows = new LinkedHashMap<>();
				while (rs.next()) {
					int employeeId = rs.getInt("EMPLOYEE_ID");
					PayrollItemLedgerRow row = rows.get(employeeId);
					if (row == null) {
						row = makeRow(rs, months.size());
						rows.put(employeeId, row);
					}
					int monthIndex = months.indexOf(rs.getString("PAY_MONTH"));
					if (monthIndex >= 0) {
						row.getMonthlyAmounts().set(monthIndex,
								row.getMonthlyAmounts().get(monthIndex) + rs.getLong("AMOUNT"));
					}
				}
				return new ArrayList<>(rows.values());
			}
		}
	}

	// 조회값과 입력값을 조합하여 행 데이터 처리 데이터를 구성한다.
	// SQL 실행에 필요한 값과 NULL을 안전하게 바인딩하고 JDBC 자원은 사용이 끝난 뒤 정리한다.
	// 照会値と入力値を組み合わせて行データの処理データを構成する。
	// SQL実行に必要な値とNULLを安全にバインドし、JDBCリソースは使用後に整理する。
	private PayrollItemLedgerRow makeRow(ResultSet rs, int monthCount) throws SQLException {
		PayrollItemLedgerRow row = new PayrollItemLedgerRow();
		row.setEmployeeId(rs.getInt("EMPLOYEE_ID"));
		row.setEmploymentTypeName(rs.getString("EMP_TYPE"));
		row.setEmployeeName(rs.getString("EMP_NAME_KR"));
		row.setDepartmentName(rs.getString("DEPARTMENT_NAME"));
		row.setPositionName(rs.getString("POSITION_NAME"));
		List<Long> amounts = new ArrayList<>();
		for (int index = 0; index < monthCount; index++) {
			amounts.add(0L);
		}
		row.setMonthlyAmounts(amounts);
		return row;
	}
}
