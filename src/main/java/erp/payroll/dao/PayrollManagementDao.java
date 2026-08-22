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

// 급여입력 화면에 필요한 복합 조회와 저장을 처리한다.
// 급여입력·관리 데이터를 데이터베이스에서 조회하고 저장·수정·삭제한다.
// 給与入力・管理データをデータベースから照会し、登録・更新・削除する。
public class PayrollManagementDao {

	// 조회 조건에 맞는 급여 회차 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合う給与回次データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
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

	// 전달받은 급여 회차 데이터를 데이터베이스에 등록하고 처리 건수를 반환한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 受け取った給与回次データをデータベースへ登録し、処理件数を返す。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
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

	// 식별조건에 해당하는 급여 회차Dates 데이터를 전달받은 값으로 수정한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 識別条件に該当する給与回次Datesデータを受け取った値で更新する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
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

	// 조회 조건에 맞는 급여사원 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合う給与社員データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
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
		return selectEmployees(conn, sql, runId, null, null, null, null, 0, 0);
	}

	// 조회 조건에 맞는 사용가능사원 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合う利用可能社員データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public List<PayrollManagementEmployee> selectAvailableEmployees(Connection conn, int runId, String keyword,
			Integer departmentId, Integer positionId, String status, int page, int size) throws SQLException {
		String sql = "SELECT * FROM (SELECT A.*, ROWNUM RNUM FROM ("
				+ "SELECT E.EMPLOYEE_ID, E.EMP_TYPE, E.EMP_NO, E.EMP_NAME_KR, "
				+ "NVL(D.DEPARTMENT_NAME, '-') DEPARTMENT_NAME, NVL(J.JOB_POSITION_NAME, '-') JOB_POSITION_NAME, E.STATUS, "
				+ "0 GROSS_PAYMENT, 0 TOTAL_DEDUCTION FROM EMPLOYEE E "
				+ "LEFT JOIN DEPARTMENT D ON D.DEPARTMENT_ID = E.DEPARTMENT_ID "
				+ "LEFT JOIN JOB_POSITION J ON J.JOB_POSITION_ID = E.JOB_POSITION_ID "
				+ "WHERE NOT EXISTS (SELECT 1 FROM PAYROLL_EMPLOYEE PE WHERE PE.PAYROLL_RUN_ID = ? AND PE.EMPLOYEE_ID = E.EMPLOYEE_ID) "
				+ "AND (? IS NULL OR E.EMP_NAME_KR LIKE '%' || ? || '%' OR E.EMP_NO LIKE '%' || ? || '%') "
				+ "AND (? IS NULL OR E.DEPARTMENT_ID = ?) AND (? IS NULL OR E.JOB_POSITION_ID = ?) "
				+ "AND (? IS NULL OR E.STATUS = ?) "
				+ "ORDER BY E.EMP_NAME_KR) A WHERE ROWNUM <= ?) WHERE RNUM >= ?";
		return selectEmployees(conn, sql, runId, keyword, departmentId, positionId, status, page, size);
	}

	// 조회 조건에 맞는 사용가능사원 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合う利用可能社員データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public int countAvailableEmployees(Connection conn, int runId, String keyword, Integer departmentId,
			Integer positionId, String status) throws SQLException {
		String sql = "SELECT COUNT(*) FROM EMPLOYEE E WHERE NOT EXISTS "
				+ "(SELECT 1 FROM PAYROLL_EMPLOYEE PE WHERE PE.PAYROLL_RUN_ID = ? AND PE.EMPLOYEE_ID = E.EMPLOYEE_ID) "
				+ "AND (? IS NULL OR E.EMP_NAME_KR LIKE '%' || ? || '%' OR E.EMP_NO LIKE '%' || ? || '%') "
				+ "AND (? IS NULL OR E.DEPARTMENT_ID = ?) AND (? IS NULL OR E.JOB_POSITION_ID = ?) "
				+ "AND (? IS NULL OR E.STATUS = ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, runId);
			setKeyword(pstmt, 2, keyword);
			setNullableInteger(pstmt, 5, departmentId);
			setNullableInteger(pstmt, 6, departmentId);
			setNullableInteger(pstmt, 7, positionId);
			setNullableInteger(pstmt, 8, positionId);
			pstmt.setString(9, emptyToNull(status));
			pstmt.setString(10, emptyToNull(status));
			try (ResultSet rs = pstmt.executeQuery()) {
				rs.next();
				return rs.getInt(1);
			}
		}
	}

	// 조회 조건에 맞는 지급항목 목록 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合う支給項目一覧データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public List<PayrollManagementItem> selectPayItems(Connection conn, Integer payrollEmployeeId) throws SQLException {
		String sql = "SELECT P.PAY_ITEM_ID ITEM_CODE, P.PAY_NAME ITEM_NAME, P.TAX_TYPE, P.CALC_METHOD, "
				+ "CASE WHEN PE.PAYROLL_EMPLOYEE_ID IS NULL THEN 0 "
				+ "WHEN E.PAYROLL_ENTRY_ID IS NOT NULL THEN NVL(E.AMOUNT, 0) "
				+ "WHEN P.PAY_NAME = '기본급' THEN NVL(EMP.BASIC_PAY, 0) "
				+ "WHEN P.PAY_METHOD = '일괄지급' THEN NVL(P.BULK_PAY_AMOUNT, NVL(P.TAX_FREE_LIMIT, 0)) "
				+ "ELSE 0 END AMOUNT "
				+ "FROM PAY_ITEM P "
				+ "LEFT JOIN PAYROLL_EMPLOYEE PE ON PE.PAYROLL_EMPLOYEE_ID = ? "
				+ "LEFT JOIN EMPLOYEE EMP ON EMP.EMPLOYEE_ID = PE.EMPLOYEE_ID "
				+ "LEFT JOIN PAYROLL_ENTRY E ON E.PAY_ITEM_ID = P.PAY_ITEM_ID "
				+ "AND E.PAYROLL_EMPLOYEE_ID = PE.PAYROLL_EMPLOYEE_ID "
				+ "WHERE P.USE_YN = 'Y' ORDER BY P.PAY_ITEM_ID";
		return selectItems(conn, sql, payrollEmployeeId, true);
	}

	// 조회 조건에 맞는 공제항목 목록 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合う控除項目一覧データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public List<PayrollManagementItem> selectDeductItems(Connection conn, Integer payrollEmployeeId)
			throws SQLException {
		String sql = "SELECT D.DEDUCT_ITEM_ID ITEM_CODE, D.DEDUCT_NAME ITEM_NAME, '전체과세' TAX_TYPE, D.CALC_METHOD, "
				+ "NVL(E.AMOUNT, 0) AMOUNT FROM DEDUCT_ITEM D LEFT JOIN PAYROLL_ENTRY E "
				+ "ON E.DEDUCT_ITEM_ID = D.DEDUCT_ITEM_ID AND E.PAYROLL_EMPLOYEE_ID = ? "
				+ "WHERE D.USE_YN = 'Y' ORDER BY D.DEDUCT_ITEM_ID";
		return selectItems(conn, sql, payrollEmployeeId, false);
	}

	// 조회 조건에 맞는 급여사원식별번호 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合う給与社員識別番号データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
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

	// 전달받은 급여사원 데이터를 데이터베이스에 등록하고 처리 건수를 반환한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 受け取った給与社員データをデータベースへ登録し、処理件数を返す。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public void insertPayrollEmployee(Connection conn, int runId, int employeeId) throws SQLException {
		String sql = "INSERT INTO PAYROLL_EMPLOYEE (PAYROLL_EMPLOYEE_ID, PAYROLL_RUN_ID, EMPLOYEE_ID) "
				+ "VALUES (PAYROLL_EMPLOYEE_SEQ.NEXTVAL, ?, ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, runId);
			pstmt.setInt(2, employeeId);
			pstmt.executeUpdate();
		}
	}

	// 선택되거나 식별된 급여사원 데이터를 삭제하고 관련 상태를 정리한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 選択または識別された給与社員データを削除し、関連状態を整理する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
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

	// 식별조건에 해당하는 상세내역 목록 데이터를 전달받은 값으로 수정한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 識別条件に該当する明細一覧データを受け取った値で更新する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public void replaceEntries(Connection conn, int payrollEmployeeId, List<PayrollManagementItem> payItems,
			List<PayrollManagementItem> deductItems) throws SQLException {
		mergeEntries(conn, payrollEmployeeId, payItems, true);
		mergeEntries(conn, payrollEmployeeId, deductItems, false);
		deleteUnusedEntries(conn, payrollEmployeeId);
	}

	// 조회 조건에 맞는 이전 회차급여 회차 목록 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合う前回給与回次一覧データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
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

	// 이전 급여 회차의 대상 사원과 지급·공제 금액을 현재 급여 회차로 복사한다.
	// SQL 실행에 필요한 값과 NULL을 안전하게 바인딩하고 JDBC 자원은 사용이 끝난 뒤 정리한다.
	// 前回給与回次の対象社員と支給・控除金額を現在の給与回次へコピーする。
	// SQL実行に必要な値とNULLを安全にバインドし、JDBCリソースは使用後に整理する。
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

	// 지급항목의 추가·수정·삭제 구분에 따라 데이터를 처리하고 결과를 반환한다.
	// SQL 실행에 필요한 값과 NULL을 안전하게 바인딩하고 JDBC 자원은 사용이 끝난 뒤 정리한다.
	// 支給項目の追加・更新・削除区分に従ってデータを処理し、結果を返す。
	// SQL実行に必要な値とNULLを安全にバインドし、JDBCリソースは使用後に整理する。
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

	// 공제항목의 추가·수정·삭제 구분에 따라 데이터를 처리하고 결과를 반환한다.
	// SQL 실행에 필요한 값과 NULL을 안전하게 바인딩하고 JDBC 자원은 사용이 끝난 뒤 정리한다.
	// 控除項目の追加・更新・削除区分に従ってデータを処理し、結果を返す。
	// SQL実行に必要な値とNULLを安全にバインドし、JDBCリソースは使用後に整理する。
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

	// 준비된 SQL과 매개변수를 실행하여 데이터 변경 건수를 반환한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 準備したSQLとパラメーターを実行し、データ変更件数を返す。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	private void executeUpdate(Connection conn, String sql, Object... values) throws SQLException {
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			for (int i = 0; i < values.length; i++) {
				pstmt.setObject(i + 1, values[i]);
			}
			pstmt.executeUpdate();
		}
	}

	// 시퀀스에서 다음 급여 회차식별번호를 발급하여 신규 데이터 저장에 사용한다.
	// SQL 실행에 필요한 값과 NULL을 안전하게 바인딩하고 JDBC 자원은 사용이 끝난 뒤 정리한다.
	// シーケンスから次の給与回次識別番号を発行し、新規データの登録に使用する。
	// SQL実行に必要な値とNULLを安全にバインドし、JDBCリソースは使用後に整理する。
	private int nextRunId(Connection conn) throws SQLException {
		try (PreparedStatement pstmt = conn.prepareStatement("SELECT PAYROLL_RUN_SEQ.NEXTVAL FROM DUAL");
				ResultSet rs = pstmt.executeQuery()) {
			rs.next();
			return rs.getInt(1);
		}
	}

	// 기존 급여 상세내역과 입력된 지급·공제 항목을 비교하여 추가·수정 내용을 반영한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 既存給与明細と入力された支給・控除項目を比較し、追加・更新内容を反映する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
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

	// 선택되거나 식별된 Unused상세내역 목록 데이터를 삭제하고 관련 상태를 정리한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 選択または識別されたUnused明細一覧データを削除し、関連状態を整理する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
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

	// 조회 조건에 맞는 사원 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合う社員データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	private List<PayrollManagementEmployee> selectEmployees(Connection conn, String sql, int runId,
			String keyword, Integer departmentId, Integer positionId, String status, int page, int size)
			throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, runId);
			if (size > 0) {
				setKeyword(pstmt, 2, keyword);
				setNullableInteger(pstmt, 5, departmentId);
				setNullableInteger(pstmt, 6, departmentId);
				setNullableInteger(pstmt, 7, positionId);
				setNullableInteger(pstmt, 8, positionId);
				pstmt.setString(9, emptyToNull(status));
				pstmt.setString(10, emptyToNull(status));
				pstmt.setInt(11, page * size);
				pstmt.setInt(12, (page - 1) * size + 1);
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

	// 조회 조건에 맞는 항목 목록 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合う項目一覧データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
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

	// 전달받은 검색어 값을 급여입력·관리 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った検索語の値を給与入力・管理オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	private void setKeyword(PreparedStatement pstmt, int index, String keyword) throws SQLException {
		String value = emptyToNull(keyword);
		pstmt.setString(index, value);
		pstmt.setString(index + 1, value);
		pstmt.setString(index + 2, value);
	}

	// 전달받은 Nullable정수 값을 급여입력·관리 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったNullable整数の値を給与入力・管理オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	private void setNullableInteger(PreparedStatement pstmt, int index, Integer value) throws SQLException {
		if (value == null) pstmt.setNull(index, java.sql.Types.NUMERIC);
		else pstmt.setInt(index, value);
	}

	// 요청 문자열을 정리하고 빈 값To빈 값 처리에 필요한 안전한 값으로 변환한다.
	// SQL 실행에 필요한 값과 NULL을 안전하게 바인딩하고 JDBC 자원은 사용이 끝난 뒤 정리한다.
	// リクエスト文字列を整え、空値To空値処理に必要な安全な値へ変換する。
	// SQL実行に必要な値とNULLを安全にバインドし、JDBCリソースは使用後に整理する。
	private String emptyToNull(String value) {
		return value == null || value.trim().isEmpty() ? null : value.trim();
	}

	// 조회값과 입력값을 조합하여 급여 회차 처리 데이터를 구성한다.
	// SQL 실행에 필요한 값과 NULL을 안전하게 바인딩하고 JDBC 자원은 사용이 끝난 뒤 정리한다.
	// 照会値と入力値を組み合わせて給与回次の処理データを構成する。
	// SQL実行に必要な値とNULLを安全にバインドし、JDBCリソースは使用後に整理する。
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
