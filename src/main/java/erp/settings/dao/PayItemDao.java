package erp.settings.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.settings.dto.PayItemRow;
import erp.settings.model.PayItem;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 급여 지급항목 설정 데이터베이스 접근(DAO) 클래스
// 지급항목 데이터를 데이터베이스에서 조회하고 저장·수정·삭제한다.
// 支給項目データをデータベースから照会し、登録・更新・削除する。
public class PayItemDao {

	// 싱글톤 인스턴스 생성
	// 同じサービスまたはDAOを共有できるよう、シングルトンインスタンスを生成して管理する。
	private static PayItemDao payItemDao = new PayItemDao();

	// 싱글톤 접근 메서드
	// 여러 서비스에서 공유할 수 있도록 생성된 싱글톤 인스턴스를 반환한다.
	// 객체 생성을 한 곳으로 제한하여 모든 호출자가 동일한 DAO 인스턴스를 재사용하게 한다.
	// 複数のサービスで共有できるように生成されたシングルトンインスタンスを返す。
	// オブジェクト生成を一箇所へ制限し、すべての呼び出し側が同じDAOインスタンスを再利用できるようにする。
	public static PayItemDao getInstance() {
		return payItemDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	// 전달받은 값으로 지급항목 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で支給項目オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	private PayItemDao() {
	}

	// 지급항목 등록
	// 入力された内容を新しい業務データとして構成し、関連データとともに登録する。
	// 시퀀스를 사용하여 기본키 발급 및 항목 데이터 저장
	// 전달받은 지급항목 데이터를 데이터베이스에 등록하고 처리 건수를 반환한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 受け取った支給項目データをデータベースへ登録し、処理件数を返す。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public void insert(Connection conn, PayItem item) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO PAY_ITEM "
					+ "(PAY_ITEM_ID, PAY_NAME, TAX_TYPE, TAX_FREE_CODE, TAX_FREE_LIMIT, CALC_METHOD, ROUND_UNIT, PAY_METHOD, LINK_ATTEND_ID, BULK_PAY_AMOUNT, USE_YN) "
					+ "VALUES (PAY_ITEM_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, item.getPayName());
			pstmt.setString(2, item.getTaxType());
			pstmt.setString(3, item.getTaxFreeCode());
			pstmt.setObject(4, item.getTaxFreeLimit(), Types.NUMERIC);
			pstmt.setString(5, item.getCalcMethod());
			pstmt.setObject(6, item.getRoundUnit(), Types.NUMERIC);
			pstmt.setString(7, item.getPayMethod());
			pstmt.setObject(8, item.getLinkAttendId(), Types.NUMERIC);
			pstmt.setObject(9, item.getBulkPayAmount(), Types.NUMERIC);
			pstmt.setString(10, item.getUseYn());

			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 지급항목 전체 목록 조회
	// 조회 조건에 맞는 전체 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合う全体データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public List<PayItem> selectAll(Connection conn) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM PAY_ITEM ORDER BY PAY_ITEM_ID ASC";

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			List<PayItem> result = new ArrayList<>();
			while (rs.next()) {
				result.add(makeItemFromResultSet(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// ResultSet 데이터를 PayItem 객체로 변환
	// 조회값과 입력값을 조합하여 항목From처리결과Set 처리 데이터를 구성한다.
	// SQL 실행에 필요한 값과 NULL을 안전하게 바인딩하고 JDBC 자원은 사용이 끝난 뒤 정리한다.
	// 照会値と入力値を組み合わせて項目From処理結果Setの処理データを構成する。
	// SQL実行に必要な値とNULLを安全にバインドし、JDBCリソースは使用後に整理する。
	private PayItem makeItemFromResultSet(ResultSet rs) throws SQLException {
		PayItem item = new PayItem();

		item.setPayItemId(rs.getInt("PAY_ITEM_ID"));
		item.setPayName(rs.getString("PAY_NAME"));
		item.setTaxType(rs.getString("TAX_TYPE"));
		item.setTaxFreeCode(rs.getString("TAX_FREE_CODE"));

		long tLimit = rs.getLong("TAX_FREE_LIMIT");
		item.setTaxFreeLimit(rs.wasNull() ? null : tLimit);

		item.setCalcMethod(rs.getString("CALC_METHOD"));

		int rUnit = rs.getInt("ROUND_UNIT");
		item.setRoundUnit(rs.wasNull() ? null : rUnit);

		item.setPayMethod(rs.getString("PAY_METHOD"));

		int lId = rs.getInt("LINK_ATTEND_ID");
		item.setLinkAttendId(rs.wasNull() ? null : lId);

		long bAmt = rs.getLong("BULK_PAY_AMOUNT");
		item.setBulkPayAmount(rs.wasNull() ? null : bAmt);

		item.setUseYn(rs.getString("USE_YN"));

		return item;
	}

	// 지급항목 전체 목록과 연결된 비과세명, 근태항목명을 조인(Join)하여 조회 처리
	// 조회 조건에 맞는 지급항목행 목록 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合う支給項目行一覧データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public List<PayItemRow> selectPayItemRows(Connection conn) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<PayItemRow> list = new ArrayList<>();

		try {
			// PAY_ITEM을 기준으로 TAX_FREE_ITEM과 ATTENDANCE_ITEM을 LEFT JOIN 하는 쿼리 작성
			// 業務条件に合うSQLを準備し、入力値をバインドしてデータベースで実行する。
			String sql = "SELECT p.*, t.TAX_FREE_NAME, a.ATTEND_NAME " + "FROM PAY_ITEM p "
					+ "LEFT JOIN TAX_FREE_ITEM t ON p.TAX_FREE_CODE = t.TAX_FREE_CODE "
					+ "LEFT JOIN ATTENDANCE_ITEM a ON p.LINK_ATTEND_ID = a.ATTENDANCE_ITEM_ID "
					+ "ORDER BY p.PAY_ITEM_ID ASC";

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				PayItemRow row = new PayItemRow();
				row.setPayItemId(rs.getInt("PAY_ITEM_ID"));
				row.setPayName(rs.getString("PAY_NAME"));
				row.setTaxType(rs.getString("TAX_TYPE"));
				row.setTaxFreeCode(rs.getString("TAX_FREE_CODE"));
				row.setTaxFreeLimit(rs.getLong("TAX_FREE_LIMIT"));
				row.setCalcMethod(rs.getString("CALC_METHOD"));
				row.setRoundUnit(rs.getInt("ROUND_UNIT"));
				row.setPayMethod(rs.getString("PAY_METHOD"));

				// 외래키 값이 0(또는 NULL)인 경우에 대한 안전한 매핑
				// 未入力値とNULLを区別して正規化し、制約違反や不要な値の保存を防止する。
				int linkId = rs.getInt("LINK_ATTEND_ID");
				row.setLinkAttendId(rs.wasNull() ? null : linkId);

				long bulkAmount = rs.getLong("BULK_PAY_AMOUNT");
				row.setBulkPayAmount(rs.wasNull() ? null : bulkAmount);

				row.setUseYn(rs.getString("USE_YN"));
				row.setTaxFreeName(rs.getString("TAX_FREE_NAME"));
				row.setAttendName(rs.getString("ATTEND_NAME"));

				list.add(row);
			}
			return list;
		} finally {
			// 자원 누수 방지를 위한 반환 처리
			// 使用済みのJDBCリソースを安全に閉じ、接続漏れやリソース漏れを防止する。
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 기본키(PAY_ITEM_ID)를 기준으로 특정 지급항목 데이터를 단건 조회 처리
	// 조회 조건에 맞는 By식별번호 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合うBy識別番号データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public PayItem selectById(Connection conn, int payItemId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT p.*, t.TAX_FREE_NAME " + "FROM PAY_ITEM p "
					+ "LEFT JOIN TAX_FREE_ITEM t ON p.TAX_FREE_CODE = t.TAX_FREE_CODE " + "WHERE p.PAY_ITEM_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, payItemId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				PayItem item = new PayItem();
				item.setPayItemId(rs.getInt("PAY_ITEM_ID"));
				item.setPayName(rs.getString("PAY_NAME"));
				item.setTaxType(rs.getString("TAX_TYPE"));
				item.setTaxFreeCode(rs.getString("TAX_FREE_CODE"));
				item.setTaxFreeLimit(rs.getLong("TAX_FREE_LIMIT"));
				item.setCalcMethod(rs.getString("CALC_METHOD"));
				item.setRoundUnit(rs.getInt("ROUND_UNIT"));
				item.setPayMethod(rs.getString("PAY_METHOD"));

				int linkId = rs.getInt("LINK_ATTEND_ID");
				item.setLinkAttendId(rs.wasNull() ? null : linkId);

				long bulkAmount = rs.getLong("BULK_PAY_AMOUNT");
				item.setBulkPayAmount(rs.wasNull() ? null : bulkAmount);

				item.setUseYn(rs.getString("USE_YN"));

				item.setTaxFreeName(rs.getString("TAX_FREE_NAME"));

				return item;
			}
			return null;
		} finally {
			// 자원 반환 처리
			// 使用済みのJDBCリソースを安全に閉じ、接続漏れやリソース漏れを防止する。
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 기존에 등록된 지급항목의 설정 데이터 갱신 처리
	// 入力された内容を新しい業務データとして構成し、関連データとともに登録する。
	// NULL이 가능한 항목(비과세코드, 근태연결, 일괄지급액)에 대한 분기 처리 포함
	// 식별조건에 해당하는 지급항목 데이터를 전달받은 값으로 수정한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 識別条件に該当する支給項目データを受け取った値で更新する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public void update(Connection conn, PayItem item) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "UPDATE PAY_ITEM SET " + "PAY_NAME = ?, TAX_TYPE = ?, TAX_FREE_CODE = ?, TAX_FREE_LIMIT = ?, "
					+ "CALC_METHOD = ?, ROUND_UNIT = ?, PAY_METHOD = ?, LINK_ATTEND_ID = ?, "
					+ "BULK_PAY_AMOUNT = ?, USE_YN = ? " + "WHERE PAY_ITEM_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, item.getPayName());
			pstmt.setString(2, item.getTaxType());

			// 비과세 코드 NULL 처리
			// 未入力値とNULLを区別して正規化し、制約違反や不要な値の保存を防止する。
			if (item.getTaxFreeCode() != null && !item.getTaxFreeCode().isEmpty()) {
				pstmt.setString(3, item.getTaxFreeCode());
			} else {
				pstmt.setNull(3, java.sql.Types.VARCHAR);
			}

			pstmt.setLong(4, item.getTaxFreeLimit() != null ? item.getTaxFreeLimit() : 0L);
			pstmt.setString(5, item.getCalcMethod());
			pstmt.setInt(6, item.getRoundUnit() != null ? item.getRoundUnit() : 0);

			// 지급 방법 NULL 처리
			// 未入力値とNULLを区別して正規化し、制約違反や不要な値の保存を防止する。
			if (item.getPayMethod() != null && !item.getPayMethod().isEmpty()) {
				pstmt.setString(7, item.getPayMethod());
			} else {
				pstmt.setNull(7, java.sql.Types.VARCHAR);
			}

			// 근태항목 외래키 NULL 처리
			// 未入力値とNULLを区別して正規化し、制約違反や不要な値の保存を防止する。
			if (item.getLinkAttendId() != null && item.getLinkAttendId() > 0) {
				pstmt.setInt(8, item.getLinkAttendId());
			} else {
				pstmt.setNull(8, java.sql.Types.NUMERIC);
			}

			// 일괄지급액 NULL 처리
			// 未入力値とNULLを区別して正規化し、制約違反や不要な値の保存を防止する。
			if (item.getBulkPayAmount() != null) {
				pstmt.setLong(9, item.getBulkPayAmount());
			} else {
				pstmt.setNull(9, java.sql.Types.NUMERIC);
			}

			pstmt.setString(10, item.getUseYn());
			pstmt.setInt(11, item.getPayItemId());

			pstmt.executeUpdate();
		} finally {
			// 자원 반환 처리
			// 使用済みのJDBCリソースを安全に閉じ、接続漏れやリソース漏れを防止する。
			JdbcUtil.close(pstmt);
		}
	}

	// 기본키를 기준으로 특정 지급항목 데이터 완전 삭제 처리
	// 선택되거나 식별된 지급항목 데이터를 삭제하고 관련 상태를 정리한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 選択または識別された支給項目データを削除し、関連状態を整理する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public void delete(Connection conn, int payItemId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM PAY_ITEM WHERE PAY_ITEM_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, payItemId);
			pstmt.executeUpdate();
		} finally {
			// 자원 반환 처리
			// 使用済みのJDBCリソースを安全に閉じ、接続漏れやリソース漏れを防止する。
			JdbcUtil.close(pstmt);
		}
	}
}
