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

// 급여대장 목록과 상세 집계를 조회한다.
// 급여등록 데이터를 데이터베이스에서 조회하고 저장·수정·삭제한다.
// 給与登録データをデータベースから照会し、登録・更新・削除する。
public class PayrollRegisterDao {

	// 조회 조건에 맞는 급여 회차 목록 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合う給与回次一覧データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
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

	// 조회 조건에 맞는 급여 회차 목록 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合う給与回次一覧データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
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

	// 조회 조건에 맞는 급여 회차By식별번호 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合う給与回次By識別番号データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
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

	// 조회 조건에 맞는 지급표시항목 목록 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合う支給表示項目一覧データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public List<PayrollRegisterColumn> selectPayColumns(Connection conn, int runId) throws SQLException {
		String sql = "SELECT DISTINCT P.PAY_ITEM_ID ITEM_ID, P.PAY_NAME ITEM_NAME FROM PAYROLL_EMPLOYEE PE "
				+ "JOIN PAYROLL_ENTRY E ON E.PAYROLL_EMPLOYEE_ID = PE.PAYROLL_EMPLOYEE_ID "
				+ "JOIN PAY_ITEM P ON P.PAY_ITEM_ID = E.PAY_ITEM_ID "
				+ "WHERE PE.PAYROLL_RUN_ID = ? ORDER BY P.PAY_ITEM_ID";
		return selectColumns(conn, sql, runId);
	}

	// 조회 조건에 맞는 공제표시항목 목록 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合う控除表示項目一覧データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public List<PayrollRegisterColumn> selectDeductColumns(Connection conn, int runId) throws SQLException {
		String sql = "SELECT DISTINCT D.DEDUCT_ITEM_ID ITEM_ID, D.DEDUCT_NAME ITEM_NAME FROM PAYROLL_EMPLOYEE PE "
				+ "JOIN PAYROLL_ENTRY E ON E.PAYROLL_EMPLOYEE_ID = PE.PAYROLL_EMPLOYEE_ID "
				+ "JOIN DEDUCT_ITEM D ON D.DEDUCT_ITEM_ID = E.DEDUCT_ITEM_ID "
				+ "WHERE PE.PAYROLL_RUN_ID = ? ORDER BY D.DEDUCT_ITEM_ID";
		return selectColumns(conn, sql, runId);
	}

	// 조회 조건에 맞는 사원 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合う社員データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
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

	// 누락된 상세내역금액 목록 값을 기본값으로 채워 화면 계산과 합계 처리를 안정화한다.
	// SQL 실행에 필요한 값과 NULL을 안전하게 바인딩하고 JDBC 자원은 사용이 끝난 뒤 정리한다.
	// 不足している明細金額一覧の値を初期値で補い、画面計算と合計処理を安定させる。
	// SQL実行に必要な値とNULLを安全にバインドし、JDBCリソースは使用後に整理する。
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

	// 누락된 일용직지급내역 값을 기본값으로 채워 화면 계산과 합계 처리를 안정화한다.
	// SQL 실행에 필요한 값과 NULL을 안전하게 바인딩하고 JDBC 자원은 사용이 끝난 뒤 정리한다.
	// 不足している日雇い支給明細の値を初期値で補い、画面計算と合計処理を安定させる。
	// SQL実行に必要な値とNULLを安全にバインドし、JDBCリソースは使用後に整理する。
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

	// 선택되거나 식별된 급여 회차 데이터를 삭제하고 관련 상태를 정리한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 選択または識別された給与回次データを削除し、関連状態を整理する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public void deleteRun(Connection conn, int runId) throws SQLException {
		try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM PAYROLL_RUN WHERE PAYROLL_RUN_ID = ?")) {
			pstmt.setInt(1, runId);
			pstmt.executeUpdate();
		}
	}

	// 조회 조건에 맞는 표시항목 목록 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合う表示項目一覧データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
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

	// 조회 조건에 맞는 사원 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合う社員データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	private PayrollRegisterEmployee findEmployee(List<PayrollRegisterEmployee> employees, int employeeId) {
		for (PayrollRegisterEmployee employee : employees) {
			if (employee.getEmployeeId() == employeeId) {
				return employee;
			}
		}
		return null;
	}

	// 조회값과 입력값을 조합하여 등록 처리 데이터를 구성한다.
	// SQL 실행에 필요한 값과 NULL을 안전하게 바인딩하고 JDBC 자원은 사용이 끝난 뒤 정리한다.
	// 照会値と入力値を組み合わせて登録の処理データを構成する。
	// SQL実行に必要な値とNULLを安全にバインドし、JDBCリソースは使用後に整理する。
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

	// 급여등록 처리에 필요한 소득명칭를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 給与登録処理に必要な所得名称を照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	private String getIncomeName(String incomeType) {
		if ("0".equals(incomeType)) {
			return "일반";
		} else if ("1".equals(incomeType)) {
			return "사업·기타";
		}
		return "일용직";
	}
}
