package erp.settings.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.settings.model.AttendanceItem;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 근태항목 설정 데이터베이스 접근(DAO) 클래스
// 근태항목 데이터를 데이터베이스에서 조회하고 저장·수정·삭제한다.
// 勤怠項目データをデータベースから照会し、登録・更新・削除する。
public class AttendanceItemDao {

	// 싱글톤 인스턴스 생성
	// 同じサービスまたはDAOを共有できるよう、シングルトンインスタンスを生成して管理する。
	private static AttendanceItemDao attendanceItemDao = new AttendanceItemDao();

	// 싱글톤 접근 메서드
	// 여러 서비스에서 공유할 수 있도록 생성된 싱글톤 인스턴스를 반환한다.
	// 객체 생성을 한 곳으로 제한하여 모든 호출자가 동일한 DAO 인스턴스를 재사용하게 한다.
	// 複数のサービスで共有できるように生成されたシングルトンインスタンスを返す。
	// オブジェクト生成を一箇所へ制限し、すべての呼び出し側が同じDAOインスタンスを再利用できるようにする。
	public static AttendanceItemDao getInstance() {
		return attendanceItemDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	// 전달받은 값으로 근태항목 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で勤怠項目オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	private AttendanceItemDao() {
	}

	// 근태항목 등록
	// 入力された内容を新しい業務データとして構成し、関連データとともに登録する。
	// 시퀀스를 사용하여 기본키 발급 및 항목 데이터 저장
	// 전달받은 근태항목 데이터를 데이터베이스에 등록하고 처리 건수를 반환한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 受け取った勤怠項目データをデータベースへ登録し、処理件数を返す。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public void insert(Connection conn, AttendanceItem item) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO ATTENDANCE_ITEM (ATTENDANCE_ITEM_ID, ATTENDANCE_GROUP_ID, ATTEND_NAME, UNIT_TYPE, DEDUCT_LEAVE_ID, WORK_HOUR_TYPE, USE_YN) "
					+ "VALUES (ATTENDANCE_ITEM_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, item.getAttendanceGroupId());
			pstmt.setString(2, item.getAttendName());
			pstmt.setString(3, item.getUnitType());
			pstmt.setObject(4, item.getDeductLeaveId(), Types.NUMERIC);
			pstmt.setString(5, item.getWorkHourType());
			pstmt.setString(6, item.getUseYn());

			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 근태항목 전체 목록 조회
	// 조회 조건에 맞는 전체 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合う全体データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public List<AttendanceItem> selectAll(Connection conn) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			// 그룹 테이블(G)과 휴가 테이블(L)을 LEFT JOIN
			// 関連テーブルを識別キーで結合し、画面表示に必要な名称と詳細情報をまとめて取得する。
			String sql = "SELECT " + "    A.*, " + "    G.GROUP_NAME, " + "    L.ITEM_NAME AS LEAVE_NAME " // 휴가항목 이름에
																											// LEAVE_NAME
																											// 이라는 별칭 부여
																											// 照会列へ明確な別名を付け、ResultSetから同じ名称で安全に読み取れるようにする。
					+ "FROM ATTENDANCE_ITEM A "
					+ "LEFT JOIN ATTENDANCE_GROUP G ON A.ATTENDANCE_GROUP_ID = G.ATTENDANCE_GROUP_ID "
					+ "LEFT JOIN LEAVE_ITEM L ON A.DEDUCT_LEAVE_ID = L.LEAVE_ITEM_ID "
					+ "ORDER BY A.ATTENDANCE_GROUP_ID ASC, A.ATTENDANCE_ITEM_ID ASC";

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			List<AttendanceItem> result = new ArrayList<>();
			while (rs.next()) {
				// 기존 도우미 메서드로 기본 데이터를 세팅
				// 共通変換メソッドを利用して照会列をDTOの基本属性へ一貫して設定する。
				AttendanceItem item = makeItemFromResultSet(rs);

				//  조인(JOIN)해서 가져온 이름 2개를 추가
				// 入力された内容を新しい業務データとして構成し、関連データとともに登録する。
				item.setGroupName(rs.getString("GROUP_NAME"));
				item.setLeaveName(rs.getString("LEAVE_NAME"));

				result.add(item);
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 조회 조건에 맞는 By식별번호 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合うBy識別番号データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public AttendanceItem selectById(Connection conn, int id) throws SQLException {
		String sql = "SELECT * FROM ATTENDANCE_ITEM WHERE ATTENDANCE_ITEM_ID = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return makeItemFromResultSet(rs);
				}
			}
		}
		return null;
	}

	// ResultSet 데이터를 AttendanceItem 객체로 변환
	// 조회값과 입력값을 조합하여 항목From처리결과Set 처리 데이터를 구성한다.
	// SQL 실행에 필요한 값과 NULL을 안전하게 바인딩하고 JDBC 자원은 사용이 끝난 뒤 정리한다.
	// 照会値と入力値を組み合わせて項目From処理結果Setの処理データを構成する。
	// SQL実行に必要な値とNULLを安全にバインドし、JDBCリソースは使用後に整理する。
	private AttendanceItem makeItemFromResultSet(ResultSet rs) throws SQLException {
		AttendanceItem item = new AttendanceItem();

		item.setAttendanceItemId(rs.getInt("ATTENDANCE_ITEM_ID"));
		item.setAttendanceGroupId(rs.getInt("ATTENDANCE_GROUP_ID"));
		item.setAttendName(rs.getString("ATTEND_NAME"));
		item.setUnitType(rs.getString("UNIT_TYPE"));

		int deductId = rs.getInt("DEDUCT_LEAVE_ID");
		item.setDeductLeaveId(rs.wasNull() ? null : deductId);

		item.setWorkHourType(rs.getString("WORK_HOUR_TYPE"));
		item.setUseYn(rs.getString("USE_YN"));

		return item;
	}

	// 기존에 등록된 근태항목 정보 수정 처리
	// 식별조건에 해당하는 근태항목 데이터를 전달받은 값으로 수정한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 識別条件に該当する勤怠項目データを受け取った値で更新する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public void update(Connection conn, AttendanceItem item) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			// 근태항목 정보 갱신을 위한 UPDATE 쿼리문 작성
			// 業務条件に合うSQLを準備し、入力値をバインドしてデータベースで実行する。
			String sql = "UPDATE ATTENDANCE_ITEM " + "SET ATTEND_NAME = ?, UNIT_TYPE = ?, ATTENDANCE_GROUP_ID = ?, "
					+ "DEDUCT_LEAVE_ID = ?, WORK_HOUR_TYPE = ?, USE_YN = ? " + "WHERE ATTENDANCE_ITEM_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, item.getAttendName());
			pstmt.setString(2, item.getUnitType());
			pstmt.setInt(3, item.getAttendanceGroupId());

			// 외래키인 휴가공제 식별 번호가 0보다 크면 정수 할당, 아니면 DB에 NULL 세팅
			// データを一意に識別するキーを発行または適用し、対象レコードを正確に処理する。
			if (item.getDeductLeaveId() != null && item.getDeductLeaveId() > 0) {
				pstmt.setInt(4, item.getDeductLeaveId());
			} else {
				pstmt.setNull(4, java.sql.Types.NUMERIC);
			}

			// 근로시간연계 속성이 존재하면 문자열 할당, 아니면 DB에 NULL 세팅
			// 未入力値とNULLを区別して正規化し、制約違反や不要な値の保存を防止する。
			if (item.getWorkHourType() != null && !item.getWorkHourType().trim().isEmpty()) {
				pstmt.setString(5, item.getWorkHourType());
			} else {
				pstmt.setNull(5, java.sql.Types.VARCHAR);
			}

			pstmt.setString(6, item.getUseYn());
			pstmt.setInt(7, item.getAttendanceItemId()); // 식별 가능한 기본키 매핑

			// 쿼리 실행 수행
			// 業務条件に合うSQLを準備し、入力値をバインドしてデータベースで実行する。
			pstmt.executeUpdate();

		} finally {
			// 자원 누수 방지를 위한 객체 반환 처리
			// 使用済みのJDBCリソースを安全に閉じ、接続漏れやリソース漏れを防止する。
			JdbcUtil.close(pstmt);
		}
	}

	// 기본키를 기준으로 특정 근태항목 데이터 완전 삭제 처리
	// 선택되거나 식별된 근태항목 데이터를 삭제하고 관련 상태를 정리한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 選択または識別された勤怠項目データを削除し、関連状態を整理する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public void delete(Connection conn, int attendItemId) throws SQLException {
		// 선택한 근태항목이 처음부터 없었던 것처럼 보이도록 근태기록을 삭제하고 지급항목 연동을 해제한다.
		// 選択した勤怠項目が最初から存在しなかった状態になるよう、勤怠履歴を削除して支給項目との連携を解除する。
		executeReferenceCleanup(conn, "DELETE FROM EMPLOYEE_ATTENDANCE WHERE ATTENDANCE_ITEM_ID = ?", attendItemId);
		executeReferenceCleanup(conn, "UPDATE PAY_ITEM SET LINK_ATTEND_ID = NULL WHERE LINK_ATTEND_ID = ?", attendItemId);
		executeReferenceCleanup(conn, "DELETE FROM ATTENDANCE_ITEM WHERE ATTENDANCE_ITEM_ID = ?", attendItemId);
	}

	// 동일한 근태항목 식별번호를 참조하는 데이터를 Service의 단일 트랜잭션 안에서 순서대로 정리한다.
	// 同じ勤怠項目IDを参照するデータをServiceの単一トランザクション内で順番に整理する。
	private int executeReferenceCleanup(Connection conn, String sql, int attendItemId) throws SQLException {
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, attendItemId);
			return pstmt.executeUpdate();
		}
	}
}
