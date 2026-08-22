package erp.employees.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.employees.model.EmployeeInsuranceHistory;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 사원별 4대보험 취득/상실 이력 데이터베이스 접근(DAO) 클래스
// 사원보험이력 데이터를 데이터베이스에서 조회하고 저장·수정·삭제한다.
// 社員保険履歴データをデータベースから照会し、登録・更新・削除する。
public class EmployeeInsuranceHistoryDao {

	// 싱글톤 인스턴스 생성
	// 同じサービスまたはDAOを共有できるよう、シングルトンインスタンスを生成して管理する。
	private static EmployeeInsuranceHistoryDao employeeInsuranceHistoryDao = new EmployeeInsuranceHistoryDao();

	// 싱글톤 접근 메서드
	// 여러 서비스에서 공유할 수 있도록 생성된 싱글톤 인스턴스를 반환한다.
	// 객체 생성을 한 곳으로 제한하여 모든 호출자가 동일한 DAO 인스턴스를 재사용하게 한다.
	// 複数のサービスで共有できるように生成されたシングルトンインスタンスを返す。
	// オブジェクト生成を一箇所へ制限し、すべての呼び出し側が同じDAOインスタンスを再利用できるようにする。
	public static EmployeeInsuranceHistoryDao getInstance() {
		return employeeInsuranceHistoryDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	// 전달받은 값으로 사원보험이력 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で社員保険履歴オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	private EmployeeInsuranceHistoryDao() {
	}

	// 4대보험 이력 등록
	// 保険・税区分と適用基準を確認し、対象金額に合う控除または非課税処理を適用する。
	// 시퀀스를 사용하여 기본키 발급 및 취득/상실 데이터 저장
	// 전달받은 사원보험이력 데이터를 데이터베이스에 등록하고 처리 건수를 반환한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 受け取った社員保険履歴データをデータベースへ登録し、処理件数を返す。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public void insert(Connection conn, EmployeeInsuranceHistory history) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO EMPLOYEE_INSURANCE_HISTORY "
					+ "(EMPLOYEE_INSURANCE_HISTORY_ID, EMPLOYEE_ID, INSURANCE_TYPE, SYMBOL_NO, ACQUIRE_DATE, LOSS_DATE) "
					+ "VALUES (EMPLOYEE_INSURANCE_HISTORY_SEQ.NEXTVAL, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, history.getEmployeeId());
			pstmt.setString(2, history.getInsuranceType());
			pstmt.setString(3, history.getSymbolNo());

			if (history.getAcquireDate() == null) {
				pstmt.setNull(4, Types.DATE);
			} else {
				pstmt.setTimestamp(4, new Timestamp(history.getAcquireDate().getTime()));
			}

			if (history.getLossDate() == null) {
				pstmt.setNull(5, Types.DATE);
			} else {
				pstmt.setTimestamp(5, new Timestamp(history.getLossDate().getTime()));
			}

			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 4대보험 이력 단건 조회
	// 조회 조건에 맞는 By식별번호 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合うBy識別番号データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public EmployeeInsuranceHistory selectById(Connection conn, int historyId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT EMPLOYEE_INSURANCE_HISTORY_ID, EMPLOYEE_ID, INSURANCE_TYPE, SYMBOL_NO, ACQUIRE_DATE, LOSS_DATE "
					+ "FROM EMPLOYEE_INSURANCE_HISTORY WHERE EMPLOYEE_INSURANCE_HISTORY_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, historyId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				return makeHistoryFromResultSet(rs);
			}
			return null;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 특정 사원의 4대보험 이력 목록 조회
	// 조회 조건에 맞는 ByEmp식별번호 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合うByEmp識別番号データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public List<EmployeeInsuranceHistory> selectByEmpId(Connection conn, int empId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<EmployeeInsuranceHistory> list = new ArrayList<>();

		try {
			// EMPLOYEE_ID 기준으로 조회하며, 입력된 순서(PK 오름차순)대로 정렬하여 가져옵니다.
			// データを一意に識別するキーを発行または適用し、対象レコードを正確に処理する。
			String sql = "SELECT * FROM EMPLOYEE_INSURANCE_HISTORY WHERE EMPLOYEE_ID = ? ORDER BY EMPLOYEE_INSURANCE_HISTORY_ID ASC";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, empId);
			rs = pstmt.executeQuery();

			// 조회된 결과 행(Row)이 있을 때까지 반복하며 DTO에 담습니다.
			// 対象一覧を順番に処理し、各行の入力値または照会結果を同じ基準で構成する。
			while (rs.next()) {
				EmployeeInsuranceHistory ins = new EmployeeInsuranceHistory();

				// EMPLOYEE_INSURANCE_HISTORY_ID (PK)
				ins.setEmployeeInsuranceHistoryId(rs.getInt("EMPLOYEE_INSURANCE_HISTORY_ID"));

				// EMPLOYEE_ID (FK)
				ins.setEmployeeId(rs.getInt("EMPLOYEE_ID"));

				// INSURANCE_TYPE (구분 - 국민연금/건강보험 등)
				// 保険・税区分と適用基準を確認し、対象金額に合う控除または非課税処理を適用する。
				ins.setInsuranceType(rs.getString("INSURANCE_TYPE"));

				// SYMBOL_NO (기호번호)
				// 保険履歴の記号番号を文字列として読み取り、モデルへ設定する。
				ins.setSymbolNo(rs.getString("SYMBOL_NO"));

				// ACQUIRE_DATE (취득일 - DATE 타입)
				// 保険資格の取得日をDATE型で読み取り、モデルへ設定する。
				ins.setAcquireDate(rs.getDate("ACQUIRE_DATE"));

				// LOSS_DATE (상실일 - DATE 타입)
				// 保険資格の喪失日をDATE型で読み取り、モデルへ設定する。
				ins.setLossDate(rs.getDate("LOSS_DATE"));

				// 매핑이 완료된 DTO를 리스트에 추가
				// 入力された内容を新しい業務データとして構成し、関連データとともに登録する。
				list.add(ins);
			}
			return list;

		} finally {
			// PreparedStatement와 ResultSet 자원을 안전하게 반환합니다.
			// 使用済みのJDBCリソースを安全に閉じ、接続漏れやリソース漏れを防止する。
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 4대보험 이력 수정
	// 식별조건에 해당하는 사원보험이력 데이터를 전달받은 값으로 수정한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 識別条件に該当する社員保険履歴データを受け取った値で更新する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public int update(Connection conn, EmployeeInsuranceHistory history) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "UPDATE EMPLOYEE_INSURANCE_HISTORY SET "
					+ "EMPLOYEE_ID = ?, INSURANCE_TYPE = ?, SYMBOL_NO = ?, ACQUIRE_DATE = ?, LOSS_DATE = ? "
					+ "WHERE EMPLOYEE_INSURANCE_HISTORY_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, history.getEmployeeId());
			pstmt.setString(2, history.getInsuranceType());
			pstmt.setString(3, history.getSymbolNo());

			if (history.getAcquireDate() == null) {
				pstmt.setNull(4, Types.DATE);
			} else {
				pstmt.setTimestamp(4, new Timestamp(history.getAcquireDate().getTime()));
			}

			if (history.getLossDate() == null) {
				pstmt.setNull(5, Types.DATE);
			} else {
				pstmt.setTimestamp(5, new Timestamp(history.getLossDate().getTime()));
			}

			pstmt.setInt(6, history.getEmployeeInsuranceHistoryId());

			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 4대보험 이력 삭제
	// 선택되거나 식별된 사원보험이력 데이터를 삭제하고 관련 상태를 정리한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 選択または識別された社員保険履歴データを削除し、関連状態を整理する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public int delete(Connection conn, int historyId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM EMPLOYEE_INSURANCE_HISTORY WHERE EMPLOYEE_INSURANCE_HISTORY_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, historyId);
			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 사원번호(EMPLOYEE_ID)를 기준으로 해당 사원의 4대보험 이력 전체 삭제
	// 선택되거나 식별된 ByEmp식별번호 데이터를 삭제하고 관련 상태를 정리한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 選択または識別されたByEmp識別番号データを削除し、関連状態を整理する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public void deleteByEmpId(Connection conn, int empId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM EMPLOYEE_INSURANCE_HISTORY WHERE EMPLOYEE_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, empId);
			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// ResultSet 데이터를 EmployeeInsuranceHistory 객체로 변환
	// 조회값과 입력값을 조합하여 이력From처리결과Set 처리 데이터를 구성한다.
	// SQL 실행에 필요한 값과 NULL을 안전하게 바인딩하고 JDBC 자원은 사용이 끝난 뒤 정리한다.
	// 照会値と入力値を組み合わせて履歴From処理結果Setの処理データを構成する。
	// SQL実行に必要な値とNULLを安全にバインドし、JDBCリソースは使用後に整理する。
	private EmployeeInsuranceHistory makeHistoryFromResultSet(ResultSet rs) throws SQLException {
		EmployeeInsuranceHistory history = new EmployeeInsuranceHistory();

		history.setEmployeeInsuranceHistoryId(rs.getInt("EMPLOYEE_INSURANCE_HISTORY_ID"));
		history.setEmployeeId(rs.getInt("EMPLOYEE_ID"));
		history.setInsuranceType(rs.getString("INSURANCE_TYPE"));
		history.setSymbolNo(rs.getString("SYMBOL_NO"));

		Timestamp acqTs = rs.getTimestamp("ACQUIRE_DATE");
		if (acqTs != null) {
			history.setAcquireDate(new java.util.Date(acqTs.getTime()));
		}

		Timestamp lossTs = rs.getTimestamp("LOSS_DATE");
		if (lossTs != null) {
			history.setLossDate(new java.util.Date(lossTs.getTime()));
		}

		return history;
	}
}
