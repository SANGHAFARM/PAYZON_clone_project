package erp.payroll.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.payroll.model.PayrollEntry;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 사원별 지급·공제 상세 내역을 저장하고 조회한다.
// 급여상세내역 데이터를 데이터베이스에서 조회하고 저장·수정·삭제한다.
// 給与明細データをデータベースから照会し、登録・更新・削除する。
public class PayrollEntryDao {

	// 싱글톤 인스턴스 생성
	// 同じサービスまたはDAOを共有できるよう、シングルトンインスタンスを生成して管理する。
	private static PayrollEntryDao payrollEntryDao = new PayrollEntryDao();

	// 싱글톤 접근 메서드
	// 여러 서비스에서 공유할 수 있도록 생성된 싱글톤 인스턴스를 반환한다.
	// 객체 생성을 한 곳으로 제한하여 모든 호출자가 동일한 DAO 인스턴스를 재사용하게 한다.
	// 複数のサービスで共有できるように生成されたシングルトンインスタンスを返す。
	// オブジェクト生成を一箇所へ制限し、すべての呼び出し側が同じDAOインスタンスを再利用できるようにする。
	public static PayrollEntryDao getInstance() {
		return payrollEntryDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	// 전달받은 값으로 급여상세내역 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で給与明細オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	private PayrollEntryDao() {
	}

	// 지급/공제 상세 내역 등록
	// 전달받은 급여상세내역 데이터를 데이터베이스에 등록하고 처리 건수를 반환한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 受け取った給与明細データをデータベースへ登録し、処理件数を返す。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public void insert(Connection conn, PayrollEntry entry) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO PAYROLL_ENTRY "
					+ "(PAYROLL_ENTRY_ID, PAYROLL_EMPLOYEE_ID, PAY_ITEM_ID, DEDUCT_ITEM_ID, AMOUNT) "
					+ "VALUES (PAYROLL_ENTRY_SEQ.NEXTVAL, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, entry.getPayrollEmployeeId());

			pstmt.setObject(2, entry.getPayItemId(), Types.NUMERIC);
			pstmt.setObject(3, entry.getDeductItemId(), Types.NUMERIC);

			pstmt.setLong(4, entry.getAmount());

			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 지급/공제 상세 내역 단건 조회
	// 조회 조건에 맞는 By식별번호 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合うBy識別番号データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public PayrollEntry selectById(Connection conn, int entryId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT PAYROLL_ENTRY_ID, PAYROLL_EMPLOYEE_ID, PAY_ITEM_ID, DEDUCT_ITEM_ID, AMOUNT "
					+ "FROM PAYROLL_ENTRY WHERE PAYROLL_ENTRY_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, entryId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				return makePayrollEntryFromResultSet(rs);
			}
			return null;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 특정 사원결과(회차+사원)의 모든 상세 내역 조회
	// 조회 조건에 맞는 By급여사원식별번호 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合うBy給与社員識別番号データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public List<PayrollEntry> selectByPayrollEmployeeId(Connection conn, int peId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT PAYROLL_ENTRY_ID, PAYROLL_EMPLOYEE_ID, PAY_ITEM_ID, DEDUCT_ITEM_ID, AMOUNT "
					+ "FROM PAYROLL_ENTRY WHERE PAYROLL_EMPLOYEE_ID = ? ORDER BY PAYROLL_ENTRY_ID ASC";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, peId);
			rs = pstmt.executeQuery();

			List<PayrollEntry> result = new ArrayList<>();
			while (rs.next()) {
				result.add(makePayrollEntryFromResultSet(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 지급/공제 상세 내역 수정
	// 식별조건에 해당하는 급여상세내역 데이터를 전달받은 값으로 수정한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 識別条件に該当する給与明細データを受け取った値で更新する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public int update(Connection conn, PayrollEntry entry) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "UPDATE PAYROLL_ENTRY SET "
					+ "PAYROLL_EMPLOYEE_ID = ?, PAY_ITEM_ID = ?, DEDUCT_ITEM_ID = ?, AMOUNT = ? "
					+ "WHERE PAYROLL_ENTRY_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, entry.getPayrollEmployeeId());

			pstmt.setObject(2, entry.getPayItemId(), Types.NUMERIC);
			pstmt.setObject(3, entry.getDeductItemId(), Types.NUMERIC);

			pstmt.setLong(4, entry.getAmount());
			pstmt.setInt(5, entry.getPayrollEntryId());

			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 지급/공제 상세 내역 삭제
	// 선택되거나 식별된 급여상세내역 데이터를 삭제하고 관련 상태를 정리한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 選択または識別された給与明細データを削除し、関連状態を整理する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public int delete(Connection conn, int entryId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM PAYROLL_ENTRY WHERE PAYROLL_ENTRY_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, entryId);
			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// ResultSet 데이터를 PayrollEntry 객체로 변환
	// 조회값과 입력값을 조합하여 급여상세내역From처리결과Set 처리 데이터를 구성한다.
	// SQL 실행에 필요한 값과 NULL을 안전하게 바인딩하고 JDBC 자원은 사용이 끝난 뒤 정리한다.
	// 照会値と入力値を組み合わせて給与明細From処理結果Setの処理データを構成する。
	// SQL実行に必要な値とNULLを安全にバインドし、JDBCリソースは使用後に整理する。
	private PayrollEntry makePayrollEntryFromResultSet(ResultSet rs) throws SQLException {
		PayrollEntry entry = new PayrollEntry();

		entry.setPayrollEntryId(rs.getInt("PAYROLL_ENTRY_ID"));
		entry.setPayrollEmployeeId(rs.getInt("PAYROLL_EMPLOYEE_ID"));

		int pItemId = rs.getInt("PAY_ITEM_ID");
		entry.setPayItemId(rs.wasNull() ? null : pItemId);

		int dItemId = rs.getInt("DEDUCT_ITEM_ID");
		entry.setDeductItemId(rs.wasNull() ? null : dItemId);

		entry.setAmount(rs.getLong("AMOUNT"));

		return entry;
	}
}
