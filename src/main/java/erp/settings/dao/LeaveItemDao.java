package erp.settings.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.settings.model.LeaveItem;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 휴가항목 설정 데이터베이스 접근(DAO) 클래스
// 휴가항목 데이터를 데이터베이스에서 조회하고 저장·수정·삭제한다.
// 休暇項目データをデータベースから照会し、登録・更新・削除する。
public class LeaveItemDao {

	// 싱글톤 인스턴스 생성
	// 同じサービスまたはDAOを共有できるよう、シングルトンインスタンスを生成して管理する。
	private static LeaveItemDao leaveItemDao = new LeaveItemDao();

	// 싱글톤 접근 메서드
	// 여러 서비스에서 공유할 수 있도록 생성된 싱글톤 인스턴스를 반환한다.
	// 객체 생성을 한 곳으로 제한하여 모든 호출자가 동일한 DAO 인스턴스를 재사용하게 한다.
	// 複数のサービスで共有できるように生成されたシングルトンインスタンスを返す。
	// オブジェクト生成を一箇所へ制限し、すべての呼び出し側が同じDAOインスタンスを再利用できるようにする。
	public static LeaveItemDao getInstance() {
		return leaveItemDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	// 전달받은 값으로 휴가항목 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で休暇項目オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	private LeaveItemDao() {
	}

	// 휴가항목 등록
	// 전달받은 휴가항목 데이터를 데이터베이스에 등록하고 처리 건수를 반환한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 受け取った休暇項目データをデータベースへ登録し、処理件数を返す。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public void insert(Connection conn, LeaveItem item) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO LEAVE_ITEM (LEAVE_ITEM_ID, ITEM_NAME, APPLY_START_DATE, APPLY_END_DATE, USE_YN) "
					+ "VALUES (LEAVE_ITEM_SEQ.NEXTVAL, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, item.getItemName());

			if (item.getApplyStartDate() == null) {
				pstmt.setNull(2, Types.DATE);
			} else {
				pstmt.setTimestamp(2, new Timestamp(item.getApplyStartDate().getTime()));
			}

			if (item.getApplyEndDate() == null) {
				pstmt.setNull(3, Types.DATE);
			} else {
				pstmt.setTimestamp(3, new Timestamp(item.getApplyEndDate().getTime()));
			}

			pstmt.setString(4, item.getUseYn());
			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 휴가항목 단건 조회
	// 조회 조건에 맞는 By식별번호 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合うBy識別番号データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public LeaveItem selectById(Connection conn, int leaveItemId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM LEAVE_ITEM WHERE LEAVE_ITEM_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, leaveItemId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				return makeLeaveItemFromResultSet(rs);
			}
			return null;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 휴가항목 전체 목록 조회
	// 조회 조건에 맞는 전체 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合う全体データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public List<LeaveItem> selectAll(Connection conn) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM LEAVE_ITEM ORDER BY LEAVE_ITEM_ID ASC";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			List<LeaveItem> result = new ArrayList<>();
			while (rs.next()) {
				result.add(makeLeaveItemFromResultSet(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}
	
	//근태관리 - 휴가조회 항목에서 사용할 사용가능한 휴가 목록 조회
	// 조회 조건에 맞는 Usable휴가Lists 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合うUsable休暇Listsデータをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public List<LeaveItem> selectUsableLeaveLists(Connection conn) throws SQLException{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM LEAVE_ITEM WHERE USE_YN = \'Y\' ORDER BY LEAVE_ITEM_ID ASC";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			List<LeaveItem> result = new ArrayList<>();
			while (rs.next()) {
				result.add(makeLeaveItemFromResultSet(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 휴가항목 수정
	// 식별조건에 해당하는 휴가항목 데이터를 전달받은 값으로 수정한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 識別条件に該当する休暇項目データを受け取った値で更新する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public int update(Connection conn, LeaveItem item) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "UPDATE LEAVE_ITEM SET ITEM_NAME = ?, APPLY_START_DATE = ?, APPLY_END_DATE = ?, USE_YN = ? WHERE LEAVE_ITEM_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, item.getItemName());

			if (item.getApplyStartDate() == null) {
				pstmt.setNull(2, Types.DATE);
			} else {
				pstmt.setTimestamp(2, new Timestamp(item.getApplyStartDate().getTime()));
			}

			if (item.getApplyEndDate() == null) {
				pstmt.setNull(3, Types.DATE);
			} else {
				pstmt.setTimestamp(3, new Timestamp(item.getApplyEndDate().getTime()));
			}

			pstmt.setString(4, item.getUseYn());
			pstmt.setInt(5, item.getLeaveItemId());
			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 휴가항목 삭제
	// 선택되거나 식별된 휴가항목 데이터를 삭제하고 관련 상태를 정리한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 選択または識別された休暇項目データを削除し、関連状態を整理する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public int delete(Connection conn, int leaveItemId) throws SQLException {
		// 선택한 휴가항목이 처음부터 없었던 것처럼 보이도록 사용 기록과 잔여일수 및 근태 연동을 함께 정리한다.
		// 選択した休暇項目が最初から存在しなかった状態になるよう、利用履歴・残日数・勤怠連携をまとめて整理する。
		executeReferenceCleanup(conn, "DELETE FROM EMPLOYEE_ATTENDANCE WHERE LEAVE_ITEM_ID = ?", leaveItemId);
		executeReferenceCleanup(conn, "DELETE FROM EMPLOYEE_LEAVE_BALANCE WHERE LEAVE_ITEM_ID = ?", leaveItemId);
		executeReferenceCleanup(conn, "UPDATE ATTENDANCE_ITEM SET DEDUCT_LEAVE_ID = NULL WHERE DEDUCT_LEAVE_ID = ?",
				leaveItemId);
		return executeReferenceCleanup(conn, "DELETE FROM LEAVE_ITEM WHERE LEAVE_ITEM_ID = ?", leaveItemId);
	}

	// 동일한 휴가항목 식별번호를 참조하는 데이터를 Service의 단일 트랜잭션 안에서 순서대로 정리한다.
	// 同じ休暇項目IDを参照するデータをServiceの単一トランザクション内で順番に整理する。
	private int executeReferenceCleanup(Connection conn, String sql, int leaveItemId) throws SQLException {
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, leaveItemId);
			return pstmt.executeUpdate();
		}
	}

	// ResultSet 데이터를 LeaveItem 객체로 변환
	// 조회값과 입력값을 조합하여 휴가항목From처리결과Set 처리 데이터를 구성한다.
	// SQL 실행에 필요한 값과 NULL을 안전하게 바인딩하고 JDBC 자원은 사용이 끝난 뒤 정리한다.
	// 照会値と入力値を組み合わせて休暇項目From処理結果Setの処理データを構成する。
	// SQL実行に必要な値とNULLを安全にバインドし、JDBCリソースは使用後に整理する。
	private LeaveItem makeLeaveItemFromResultSet(ResultSet rs) throws SQLException {
		LeaveItem item = new LeaveItem();
		item.setLeaveItemId(rs.getInt("LEAVE_ITEM_ID"));
		item.setItemName(rs.getString("ITEM_NAME"));

		Timestamp startTs = rs.getTimestamp("APPLY_START_DATE");
		if (startTs != null) {
			item.setApplyStartDate(new java.util.Date(startTs.getTime()));
		}

		Timestamp endTs = rs.getTimestamp("APPLY_END_DATE");
		if (endTs != null) {
			item.setApplyEndDate(new java.util.Date(endTs.getTime()));
		}

		item.setUseYn(rs.getString("USE_YN"));
		return item;
	}
}
