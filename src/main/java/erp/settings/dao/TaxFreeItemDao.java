package erp.settings.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.settings.model.TaxFreeItem;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 비과세/감면 소득 설정 데이터베이스 접근(DAO) 클래스
// 세금비과세항목 데이터를 데이터베이스에서 조회하고 저장·수정·삭제한다.
// 税金非課税項目データをデータベースから照会し、登録・更新・削除する。
public class TaxFreeItemDao {

	// 싱글톤 인스턴스 생성
	// 同じサービスまたはDAOを共有できるよう、シングルトンインスタンスを生成して管理する。
	private static TaxFreeItemDao taxFreeItemDao = new TaxFreeItemDao();

	// 싱글톤 접근 메서드
	// 여러 서비스에서 공유할 수 있도록 생성된 싱글톤 인스턴스를 반환한다.
	// 객체 생성을 한 곳으로 제한하여 모든 호출자가 동일한 DAO 인스턴스를 재사용하게 한다.
	// 複数のサービスで共有できるように生成されたシングルトンインスタンスを返す。
	// オブジェクト生成を一箇所へ制限し、すべての呼び出し側が同じDAOインスタンスを再利用できるようにする。
	public static TaxFreeItemDao getInstance() {
		return taxFreeItemDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	// 전달받은 값으로 세금비과세항목 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で税金非課税項目オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	private TaxFreeItemDao() {
	}

	// 비과세 설정 항목 등록
	// 전달받은 세금비과세항목 데이터를 데이터베이스에 등록하고 처리 건수를 반환한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 受け取った税金非課税項目データをデータベースへ登録し、処理件数を返す。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public void insert(Connection conn, TaxFreeItem item) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO TAX_FREE_ITEM (TAX_FREE_CODE, LEGAL_CLAUSE, REPORT_FIELD, TAX_FREE_NAME, DEFAULT_LIMIT, PAY_STATEMENT_YN, INCOME_CATEGORY) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, item.getTaxFreeCode());
			pstmt.setString(2, item.getLegalClause());
			pstmt.setString(3, item.getReportField());
			pstmt.setString(4, item.getTaxFreeName());
			pstmt.setObject(5, item.getDefaultLimit(), Types.NUMERIC);
			pstmt.setString(6, item.getPayStatementYn());
			pstmt.setString(7, item.getIncomeCategory());

			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 비과세 설정 항목 전체 조회
	// 조회 조건에 맞는 전체 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合う全体データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public List<TaxFreeItem> selectAll(Connection conn) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM TAX_FREE_ITEM "
					+ "ORDER BY CASE WHEN INCOME_CATEGORY = '비과세' THEN 1 ELSE 2 END, TAX_FREE_CODE ASC";

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			List<TaxFreeItem> result = new ArrayList<>();
			while (rs.next()) {
				result.add(makeItemFromResultSet(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 비과세 코드를 기준으로 법정 항목과 기본 한도액을 조회
	// 조회 조건에 맞는 By코드 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合うByコードデータをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public TaxFreeItem selectByCode(Connection conn, String taxFreeCode) throws SQLException {
		String sql = "SELECT * FROM TAX_FREE_ITEM WHERE TAX_FREE_CODE = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, taxFreeCode);
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next() ? makeItemFromResultSet(rs) : null;
			}
		}
	}

	// 기본 법정 코드가 없는 경우에만 마스터 목록에 추가한다.
	// 전달받은 조건없을 때 데이터를 데이터베이스에 등록하고 처리 건수를 반환한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 受け取った条件不在時データをデータベースへ登録し、処理件数を返す。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public void insertIfAbsent(Connection conn, TaxFreeItem item) throws SQLException {
		String sql = "MERGE INTO TAX_FREE_ITEM T USING (SELECT ? TAX_FREE_CODE FROM DUAL) S "
				+ "ON (T.TAX_FREE_CODE = S.TAX_FREE_CODE) WHEN NOT MATCHED THEN INSERT "
				+ "(TAX_FREE_CODE, LEGAL_CLAUSE, REPORT_FIELD, TAX_FREE_NAME, DEFAULT_LIMIT, PAY_STATEMENT_YN, INCOME_CATEGORY) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, item.getTaxFreeCode());
			pstmt.setString(2, item.getTaxFreeCode());
			pstmt.setString(3, item.getLegalClause());
			pstmt.setString(4, item.getReportField());
			pstmt.setString(5, item.getTaxFreeName());
			pstmt.setLong(6, item.getDefaultLimit());
			pstmt.setString(7, item.getPayStatementYn());
			pstmt.setString(8, item.getIncomeCategory());
			pstmt.executeUpdate();
		}
	}

	// 사용자 정의 코드의 다음 번호를 발급한다.
	// 조회 조건에 맞는 다음User코드 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合う次Userコードデータをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public String selectNextUserCode(Connection conn) throws SQLException {
		String sql = "SELECT 'U' || LPAD(NVL(MAX(TO_NUMBER(SUBSTR(TAX_FREE_CODE, 2))), 0) + 1, 5, '0') "
				+ "FROM TAX_FREE_ITEM WHERE REGEXP_LIKE(TAX_FREE_CODE, '^U[0-9]{5}$')";
		try (PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {
			rs.next();
			return rs.getString(1);
		}
	}

	// ResultSet 데이터를 TaxFreeItem 객체로 변환
	// 조회값과 입력값을 조합하여 항목From처리결과Set 처리 데이터를 구성한다.
	// SQL 실행에 필요한 값과 NULL을 안전하게 바인딩하고 JDBC 자원은 사용이 끝난 뒤 정리한다.
	// 照会値と入力値を組み合わせて項目From処理結果Setの処理データを構成する。
	// SQL実行に必要な値とNULLを安全にバインドし、JDBCリソースは使用後に整理する。
	private TaxFreeItem makeItemFromResultSet(ResultSet rs) throws SQLException {
		TaxFreeItem item = new TaxFreeItem();

		item.setTaxFreeCode(rs.getString("TAX_FREE_CODE"));
		item.setLegalClause(rs.getString("LEGAL_CLAUSE"));
		item.setReportField(rs.getString("REPORT_FIELD"));
		item.setTaxFreeName(rs.getString("TAX_FREE_NAME"));

		long limit = rs.getLong("DEFAULT_LIMIT");
		item.setDefaultLimit(rs.wasNull() ? null : limit);

		item.setPayStatementYn(rs.getString("PAY_STATEMENT_YN"));
		item.setIncomeCategory(rs.getString("INCOME_CATEGORY"));

		return item;
	}
}
