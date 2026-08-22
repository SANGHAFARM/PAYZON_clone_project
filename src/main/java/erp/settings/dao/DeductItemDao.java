package erp.settings.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.settings.model.DeductItem;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 급여 공제항목 설정 데이터베이스 접근(DAO) 클래스
// 공제항목 데이터를 데이터베이스에서 조회하고 저장·수정·삭제한다.
// 控除項目データをデータベースから照会し、登録・更新・削除する。
public class DeductItemDao {

	// 싱글톤 인스턴스 생성
	// 同じサービスまたはDAOを共有できるよう、シングルトンインスタンスを生成して管理する。
	private static DeductItemDao deductItemDao = new DeductItemDao();

	// 싱글톤 접근 메서드
	// 여러 서비스에서 공유할 수 있도록 생성된 싱글톤 인스턴스를 반환한다.
	// 객체 생성을 한 곳으로 제한하여 모든 호출자가 동일한 DAO 인스턴스를 재사용하게 한다.
	// 複数のサービスで共有できるように生成されたシングルトンインスタンスを返す。
	// オブジェクト生成を一箇所へ制限し、すべての呼び出し側が同じDAOインスタンスを再利用できるようにする。
	public static DeductItemDao getInstance() {
		return deductItemDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	// 전달받은 값으로 공제항목 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で控除項目オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	private DeductItemDao() {
	}

	// 공제항목 등록
	// 支給額・控除額・期間値を業務基準に従って計算し、合計と最終金額へ反映する。
	// 시퀀스를 사용하여 기본키 발급 및 항목 데이터 저장
	// 전달받은 공제항목 데이터를 데이터베이스에 등록하고 처리 건수를 반환한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 受け取った控除項目データをデータベースへ登録し、処理件数を返す。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public void insert(Connection conn, DeductItem item) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO DEDUCT_ITEM (DEDUCT_ITEM_ID, DEDUCT_NAME, CALC_METHOD, ROUND_UNIT, NOTE, USE_YN) "
					+ "VALUES (DEDUCT_ITEM_SEQ.NEXTVAL, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, item.getDeductName());
			pstmt.setString(2, item.getCalcMethod());
			pstmt.setObject(3, item.getRoundUnit(), Types.NUMERIC);
			pstmt.setString(4, item.getNote());
			pstmt.setString(5, item.getUseYn());

			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 공제항목 전체 목록 조회
	// 조회 조건에 맞는 전체 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合う全体データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public List<DeductItem> selectAll(Connection conn) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM DEDUCT_ITEM ORDER BY DEDUCT_ITEM_ID ASC";

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			List<DeductItem> result = new ArrayList<>();
			while (rs.next()) {
				result.add(makeItemFromResultSet(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// ResultSet 데이터를 DeductItem 객체로 변환
	// 조회값과 입력값을 조합하여 항목From처리결과Set 처리 데이터를 구성한다.
	// SQL 실행에 필요한 값과 NULL을 안전하게 바인딩하고 JDBC 자원은 사용이 끝난 뒤 정리한다.
	// 照会値と入力値を組み合わせて項目From処理結果Setの処理データを構成する。
	// SQL実行に必要な値とNULLを安全にバインドし、JDBCリソースは使用後に整理する。
	private DeductItem makeItemFromResultSet(ResultSet rs) throws SQLException {
		DeductItem item = new DeductItem();

		item.setDeductItemId(rs.getInt("DEDUCT_ITEM_ID"));
		item.setDeductName(rs.getString("DEDUCT_NAME"));
		item.setCalcMethod(rs.getString("CALC_METHOD"));

		int rUnit = rs.getInt("ROUND_UNIT");
		item.setRoundUnit(rs.wasNull() ? null : rUnit);

		item.setNote(rs.getString("NOTE"));
		item.setUseYn(rs.getString("USE_YN"));

		return item;
	}
	
	// 기본키(DEDUCT_ITEM_ID)를 기준으로 특정 공제항목 데이터를 단건 조회 처리
	// 조회 조건에 맞는 By식별번호 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合うBy識別番号データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public DeductItem selectById(Connection conn, int deductItemId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			// 기본키를 조건으로 공제항목을 조회하는 쿼리 작성
			// 業務条件に合うSQLを準備し、入力値をバインドしてデータベースで実行する。
			String sql = "SELECT * FROM DEDUCT_ITEM WHERE DEDUCT_ITEM_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, deductItemId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				DeductItem item = new DeductItem();
				item.setDeductItemId(rs.getInt("DEDUCT_ITEM_ID"));
				item.setDeductName(rs.getString("DEDUCT_NAME"));
				item.setCalcMethod(rs.getString("CALC_METHOD"));
				item.setRoundUnit(rs.getInt("ROUND_UNIT"));
				item.setNote(rs.getString("NOTE"));
				item.setUseYn(rs.getString("USE_YN"));
				return item;
			}
			return null;
		} finally {
			// 사용 완료된 객체 반환 처리
			// 使用済みのJDBCリソースを安全に閉じ、接続漏れやリソース漏れを防止する。
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 기존에 등록된 공제항목의 명칭, 계산방법, 절사단위 등 설정 데이터 갱신 처리
	// 식별조건에 해당하는 공제항목 데이터를 전달받은 값으로 수정한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 識別条件に該当する控除項目データを受け取った値で更新する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public void update(Connection conn, DeductItem item) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			// 공제항목 정보 갱신을 위한 UPDATE 쿼리문 작성
			// 業務条件に合うSQLを準備し、入力値をバインドしてデータベースで実行する。
			String sql = "UPDATE DEDUCT_ITEM SET "
					+ "DEDUCT_NAME = ?, CALC_METHOD = ?, ROUND_UNIT = ?, NOTE = ?, USE_YN = ? "
					+ "WHERE DEDUCT_ITEM_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, item.getDeductName());
			pstmt.setString(2, item.getCalcMethod());
			pstmt.setInt(3, item.getRoundUnit() != null ? item.getRoundUnit() : 0);
			pstmt.setString(4, item.getNote());
			pstmt.setString(5, item.getUseYn());
			pstmt.setInt(6, item.getDeductItemId());

			// 쿼리 실행 수행
			// 業務条件に合うSQLを準備し、入力値をバインドしてデータベースで実行する。
			pstmt.executeUpdate();
		} finally {
			// 자원 반환 처리
			// 使用済みのJDBCリソースを安全に閉じ、接続漏れやリソース漏れを防止する。
			JdbcUtil.close(pstmt);
		}
	}

	// 기본키를 기준으로 특정 공제항목 데이터 완전 삭제 처리
	// 선택되거나 식별된 공제항목 데이터를 삭제하고 관련 상태를 정리한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 選択または識別された控除項目データを削除し、関連状態を整理する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public void delete(Connection conn, int deductItemId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			// 기본키 기반 레코드 삭제 쿼리문 작성
			// 業務条件に合うSQLを準備し、入力値をバインドしてデータベースで実行する。
			String sql = "DELETE FROM DEDUCT_ITEM WHERE DEDUCT_ITEM_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, deductItemId);
			
			// 쿼리 실행 수행
			// 業務条件に合うSQLを準備し、入力値をバインドしてデータベースで実行する。
			pstmt.executeUpdate();
		} finally {
			// 자원 반환 처리
			// 使用済みのJDBCリソースを安全に閉じ、接続漏れやリソース漏れを防止する。
			JdbcUtil.close(pstmt);
		}
	}
}
