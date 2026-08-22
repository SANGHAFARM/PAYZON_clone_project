package erp.payroll.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import erp.payroll.dto.DayWorkerPaymentEmployee;
import erp.payroll.dto.DayWorkerPaymentPage.DayWorkerPaymentWork;
import erp.payroll.dto.PayrollManagementItem;
import jdbc.JdbcUtil;

// 일용직 근무기록과 급여 공제내역을 조회한다.
// 일용직근로자급여 데이터를 데이터베이스에서 조회하고 저장·수정·삭제한다.
// 日雇い労働者給与データをデータベースから照会し、登録・更新・削除する。
public class DayWorkerPayrollDao {

	// 조회 조건에 맞는 지급사원 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合う支給社員データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public List<DayWorkerPaymentEmployee> selectPaymentEmployees(Connection conn, int runId, Date startDate,
			Date endDate) throws SQLException {
		String sql = "SELECT E.EMPLOYEE_ID, E.EMP_TYPE, E.EMP_NO, E.EMP_NAME_KR, "
				+ "NVL(D.DEPARTMENT_NAME, '-') DEPARTMENT_NAME, NVL(J.JOB_POSITION_NAME, '-') JOB_POSITION_NAME, E.STATUS, "
				+ "NVL((SELECT SUM(W.DAILY_PAY) FROM DAILY_WORK_RECORD W WHERE W.EMPLOYEE_ID = E.EMPLOYEE_ID "
				+ "AND W.WORK_DATE BETWEEN ? AND ?), 0) TOTAL_PAYMENT "
				+ "FROM PAYROLL_EMPLOYEE PE JOIN EMPLOYEE E ON E.EMPLOYEE_ID = PE.EMPLOYEE_ID "
				+ "LEFT JOIN DEPARTMENT D ON D.DEPARTMENT_ID = E.DEPARTMENT_ID "
				+ "LEFT JOIN JOB_POSITION J ON J.JOB_POSITION_ID = E.JOB_POSITION_ID "
				+ "WHERE PE.PAYROLL_RUN_ID = ? ORDER BY E.EMP_NAME_KR";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setDate(1, new java.sql.Date(startDate.getTime()));
			pstmt.setDate(2, new java.sql.Date(endDate.getTime()));
			pstmt.setInt(3, runId);
			rs = pstmt.executeQuery();
			List<DayWorkerPaymentEmployee> result = new ArrayList<>();
			while (rs.next()) {
				result.add(makeEmployee(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 조회 조건에 맞는 사용가능사원 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合う利用可能社員データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public List<DayWorkerPaymentEmployee> selectAvailableEmployees(Connection conn, int runId, String keyword,
			Integer departmentId, int page, int size) throws SQLException {
		String sql = "SELECT * FROM (SELECT A.*, ROWNUM RNUM FROM ("
				+ "SELECT E.EMPLOYEE_ID, E.EMP_TYPE, E.EMP_NO, E.EMP_NAME_KR, "
				+ "NVL(D.DEPARTMENT_NAME, '-') DEPARTMENT_NAME, NVL(J.JOB_POSITION_NAME, '-') JOB_POSITION_NAME, E.STATUS, "
				+ "0 TOTAL_PAYMENT FROM EMPLOYEE E "
				+ "LEFT JOIN DEPARTMENT D ON D.DEPARTMENT_ID = E.DEPARTMENT_ID "
				+ "LEFT JOIN JOB_POSITION J ON J.JOB_POSITION_ID = E.JOB_POSITION_ID "
				+ "WHERE E.EMP_TYPE = '일용직' AND E.STATUS = '재직' "
				+ "AND NOT EXISTS (SELECT 1 FROM PAYROLL_EMPLOYEE PE WHERE PE.PAYROLL_RUN_ID = ? "
				+ "AND PE.EMPLOYEE_ID = E.EMPLOYEE_ID) "
				+ "AND (? IS NULL OR E.EMP_NAME_KR LIKE '%' || ? || '%' OR E.EMP_NO LIKE '%' || ? || '%') "
				+ "AND (? IS NULL OR E.DEPARTMENT_ID = ?) ORDER BY E.EMP_NAME_KR) A WHERE ROWNUM <= ?) "
				+ "WHERE RNUM >= ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			setAvailableParameters(pstmt, runId, keyword, departmentId);
			pstmt.setInt(7, page * size);
			pstmt.setInt(8, (page - 1) * size + 1);
			rs = pstmt.executeQuery();
			List<DayWorkerPaymentEmployee> result = new ArrayList<>();
			while (rs.next()) {
				result.add(makeEmployee(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 조회 조건에 맞는 사용가능사원 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合う利用可能社員データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public int countAvailableEmployees(Connection conn, int runId, String keyword, Integer departmentId)
			throws SQLException {
		String sql = "SELECT COUNT(*) FROM EMPLOYEE E WHERE E.EMP_TYPE = '일용직' AND E.STATUS = '재직' "
				+ "AND NOT EXISTS (SELECT 1 FROM PAYROLL_EMPLOYEE PE WHERE PE.PAYROLL_RUN_ID = ? "
				+ "AND PE.EMPLOYEE_ID = E.EMPLOYEE_ID) "
				+ "AND (? IS NULL OR E.EMP_NAME_KR LIKE '%' || ? || '%' OR E.EMP_NO LIKE '%' || ? || '%') "
				+ "AND (? IS NULL OR E.DEPARTMENT_ID = ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			setAvailableParameters(pstmt, runId, keyword, departmentId);
			try (ResultSet rs = pstmt.executeQuery()) {
				rs.next();
				return rs.getInt(1);
			}
		}
	}

	// 조회 조건에 맞는 근무지급내역 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合う勤務支給明細データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public List<DayWorkerPaymentWork> selectWorkPayments(Connection conn, int employeeId, Date startDate,
			Date endDate) throws SQLException {
		String sql = "SELECT WORK_DATE, PAY_RATE, DAILY_PAY, INCOME_TAX, LOCAL_INCOME_TAX "
				+ "FROM DAILY_WORK_RECORD WHERE EMPLOYEE_ID = ? AND WORK_DATE BETWEEN ? AND ? ORDER BY WORK_DATE";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, employeeId);
			pstmt.setDate(2, new java.sql.Date(startDate.getTime()));
			pstmt.setDate(3, new java.sql.Date(endDate.getTime()));
			try (ResultSet rs = pstmt.executeQuery()) {
				List<DayWorkerPaymentWork> result = new ArrayList<>();
				while (rs.next()) {
					DayWorkerPaymentWork work = new DayWorkerPaymentWork();
					work.setWorkDate(rs.getDate("WORK_DATE"));
					work.setPaymentRate(rs.getInt("PAY_RATE"));
					work.setPaymentAmount(rs.getLong("DAILY_PAY"));
					work.setIncomeTax(rs.getLong("INCOME_TAX"));
					work.setLocalIncomeTax(rs.getLong("LOCAL_INCOME_TAX"));
					result.add(work);
				}
				return result;
			}
		}
	}

	// 조회 조건에 맞는 공제상세내역 목록 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合う控除明細一覧データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public List<PayrollManagementItem> selectDeductionEntries(Connection conn, int payrollEmployeeId)
			throws SQLException {
		String sql = "SELECT D.DEDUCT_ITEM_ID, D.DEDUCT_NAME, NVL(E.AMOUNT, 0) AMOUNT "
				+ "FROM DEDUCT_ITEM D LEFT JOIN PAYROLL_ENTRY E ON E.DEDUCT_ITEM_ID = D.DEDUCT_ITEM_ID "
				+ "AND E.PAYROLL_EMPLOYEE_ID = ? WHERE D.USE_YN = 'Y' ORDER BY D.DEDUCT_ITEM_ID";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, payrollEmployeeId);
			try (ResultSet rs = pstmt.executeQuery()) {
				List<PayrollManagementItem> result = new ArrayList<>();
				while (rs.next()) {
					PayrollManagementItem item = new PayrollManagementItem();
					item.setItemCode(rs.getInt("DEDUCT_ITEM_ID"));
					item.setItemName(rs.getString("DEDUCT_NAME"));
					item.setAmount(rs.getLong("AMOUNT"));
					result.add(item);
				}
				return result;
			}
		}
	}

	// 조회 조건에 맞는 Automatic공제 목록 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合うAutomatic控除一覧データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public long[] selectAutomaticDeductions(Connection conn, int employeeId, Date startDate, Date endDate)
			throws SQLException {
		String sql = "SELECT E.NP_YN, E.HI_YN, E.LTCI_YN, E.EI_YN, E.NP_MONTHLY_BASE, E.HI_MONTHLY_BASE, "
				+ "E.EI_MONTHLY_BASE, NVL((SELECT SUM(W.DAILY_PAY) FROM DAILY_WORK_RECORD W "
				+ "WHERE W.EMPLOYEE_ID = E.EMPLOYEE_ID AND W.WORK_DATE BETWEEN ? AND ?), 0) GROSS_PAY, "
				+ "NVL((SELECT SUM(W.INCOME_TAX) FROM DAILY_WORK_RECORD W "
				+ "WHERE W.EMPLOYEE_ID = E.EMPLOYEE_ID AND W.WORK_DATE BETWEEN ? AND ?), 0) INCOME_TAX, "
				+ "NVL((SELECT SUM(W.LOCAL_INCOME_TAX) FROM DAILY_WORK_RECORD W "
				+ "WHERE W.EMPLOYEE_ID = E.EMPLOYEE_ID AND W.WORK_DATE BETWEEN ? AND ?), 0) LOCAL_INCOME_TAX "
				+ "FROM EMPLOYEE E WHERE E.EMPLOYEE_ID = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setDate(1, new java.sql.Date(startDate.getTime()));
			pstmt.setDate(2, new java.sql.Date(endDate.getTime()));
			pstmt.setDate(3, new java.sql.Date(startDate.getTime()));
			pstmt.setDate(4, new java.sql.Date(endDate.getTime()));
			pstmt.setDate(5, new java.sql.Date(startDate.getTime()));
			pstmt.setDate(6, new java.sql.Date(endDate.getTime()));
			pstmt.setInt(7, employeeId);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (!rs.next()) {
					return new long[6];
				}
				long grossPay = rs.getLong("GROSS_PAY");
				long pensionBase = positiveBase(rs.getLong("NP_MONTHLY_BASE"), grossPay);
				long healthBase = positiveBase(rs.getLong("HI_MONTHLY_BASE"), grossPay);
				long employmentBase = positiveBase(rs.getLong("EI_MONTHLY_BASE"), grossPay);

				// 등록된 보수월액이 없으면 해당 기간의 실제 지급총액을 계산 기준으로 사용한다.
				// 基準日と期間の境界を確認し、照会・計算に使用できる日付形式へ変換する。
				long nationalPension = "Y".equals(rs.getString("NP_YN"))
						? roundDownTen(pensionBase * 0.045) : 0;
				long healthInsurance = "Y".equals(rs.getString("HI_YN"))
						? roundDownTen(healthBase * 0.03545) : 0;
				long longTermCare = "Y".equals(rs.getString("LTCI_YN"))
						? roundDownTen(healthInsurance * 0.1295) : 0;
				long employmentInsurance = "Y".equals(rs.getString("EI_YN"))
						? roundDownTen(employmentBase * 0.009) : 0;

				// 근무기록에 세액이 없으면 일용근로소득 공제액을 반영한 간이세액을 사용한다.
				// 支給額・控除額・期間値を業務基準に従って計算し、合計と最終金額へ反映する。
				long incomeTax = rs.getLong("INCOME_TAX");
				if (incomeTax == 0) {
					incomeTax = selectEstimatedIncomeTax(conn, employeeId, startDate, endDate);
				}
				long localIncomeTax = rs.getLong("LOCAL_INCOME_TAX");
				if (localIncomeTax == 0 && incomeTax > 0) {
					localIncomeTax = roundDownTen(incomeTax * 0.1);
				}
				return new long[] { nationalPension, healthInsurance, longTermCare, employmentInsurance,
						incomeTax, localIncomeTax };
			}
		}
	}

	// 조회 조건에 맞는 Estimated소득세금 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合うEstimated所得税金データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	private long selectEstimatedIncomeTax(Connection conn, int employeeId, Date startDate, Date endDate)
			throws SQLException {
		// 일 15만원 공제 후 6% 세율과 55% 근로소득세액공제를 적용한 간이 계산이다.
		// 支給額・控除額・期間値を業務基準に従って計算し、合計と最終金額へ反映する。
		String sql = "SELECT NVL(SUM(FLOOR(GREATEST(DAILY_PAY - 150000, 0) * 0.027 / 10) * 10), 0) "
				+ "FROM DAILY_WORK_RECORD WHERE EMPLOYEE_ID = ? AND WORK_DATE BETWEEN ? AND ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, employeeId);
			pstmt.setDate(2, new java.sql.Date(startDate.getTime()));
			pstmt.setDate(3, new java.sql.Date(endDate.getTime()));
			try (ResultSet rs = pstmt.executeQuery()) {
				rs.next();
				return rs.getLong(1);
			}
		}
	}

	// 보험료 계산에 사용할 기준금액이 음수가 되지 않도록 유효한 금액으로 보정한다.
	// SQL 실행에 필요한 값과 NULL을 안전하게 바인딩하고 JDBC 자원은 사용이 끝난 뒤 정리한다.
	// 保険料計算に使用する基準金額が負数にならないよう有効な金額へ補正する。
	// SQL実行に必要な値とNULLを安全にバインドし、JDBCリソースは使用後に整理する。
	private long positiveBase(long registeredBase, long grossPay) {
		return registeredBase > 0 ? registeredBase : grossPay;
	}

	// 계산된 보험료와 세액을 10단위로 절사하여 반환한다.
	// SQL 실행에 필요한 값과 NULL을 안전하게 바인딩하고 JDBC 자원은 사용이 끝난 뒤 정리한다.
	// 計算した保険料と税額を10単位で切り捨てて返す。
	// SQL実行に必要な値とNULLを安全にバインドし、JDBCリソースは使用後に整理する。
	private long roundDownTen(double amount) {
		return ((long) amount / 10) * 10;
	}

	// 전달받은 사용가능매개변수 값을 일용직근로자급여 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った利用可能パラメーターの値を日雇い労働者給与オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	private void setAvailableParameters(PreparedStatement pstmt, int runId, String keyword, Integer departmentId)
			throws SQLException {
		String searchKeyword = keyword == null || keyword.trim().isEmpty() ? null : keyword.trim();
		pstmt.setInt(1, runId);
		pstmt.setString(2, searchKeyword);
		pstmt.setString(3, searchKeyword);
		pstmt.setString(4, searchKeyword);
		pstmt.setObject(5, departmentId);
		pstmt.setObject(6, departmentId);
	}

	// 조회값과 입력값을 조합하여 사원 처리 데이터를 구성한다.
	// SQL 실행에 필요한 값과 NULL을 안전하게 바인딩하고 JDBC 자원은 사용이 끝난 뒤 정리한다.
	// 照会値と入力値を組み合わせて社員の処理データを構成する。
	// SQL実行に必要な値とNULLを安全にバインドし、JDBCリソースは使用後に整理する。
	private DayWorkerPaymentEmployee makeEmployee(ResultSet rs) throws SQLException {
		DayWorkerPaymentEmployee employee = new DayWorkerPaymentEmployee();
		employee.setEmployeeId(rs.getInt("EMPLOYEE_ID"));
		employee.setEmploymentTypeName(rs.getString("EMP_TYPE"));
		employee.setEmployeeNumber(rs.getString("EMP_NO"));
		employee.setEmployeeName(rs.getString("EMP_NAME_KR"));
		employee.setDepartmentName(rs.getString("DEPARTMENT_NAME"));
		employee.setPositionName(rs.getString("JOB_POSITION_NAME"));
		employee.setStatusName(rs.getString("STATUS"));
		employee.setTotalPayment(rs.getLong("TOTAL_PAYMENT"));
		return employee;
	}
}
