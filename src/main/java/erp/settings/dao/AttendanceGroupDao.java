package erp.settings.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import erp.settings.dto.AttendanceGroupWithItemsDto;
import erp.settings.dto.AttendanceItemResponseDto;
import erp.settings.model.AttendanceGroup;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 근태그룹 설정 데이터베이스 접근(DAO) 클래스
// 근태그룹 데이터를 데이터베이스에서 조회하고 저장·수정·삭제한다.
// 勤怠グループデータをデータベースから照会し、登録・更新・削除する。
public class AttendanceGroupDao {

	// 싱글톤 인스턴스 생성
	// 同じサービスまたはDAOを共有できるよう、シングルトンインスタンスを生成して管理する。
	private static AttendanceGroupDao attendanceGroupDao = new AttendanceGroupDao();

	// 싱글톤 접근 메서드
	// 여러 서비스에서 공유할 수 있도록 생성된 싱글톤 인스턴스를 반환한다.
	// 객체 생성을 한 곳으로 제한하여 모든 호출자가 동일한 DAO 인스턴스를 재사용하게 한다.
	// 複数のサービスで共有できるように生成されたシングルトンインスタンスを返す。
	// オブジェクト生成を一箇所へ制限し、すべての呼び出し側が同じDAOインスタンスを再利用できるようにする。
	public static AttendanceGroupDao getInstance() {
		return attendanceGroupDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	// 전달받은 값으로 근태그룹 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で勤怠グループオブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	private AttendanceGroupDao() {
	}

	// 근태그룹 등록
	// 전달받은 근태그룹 데이터를 데이터베이스에 등록하고 처리 건수를 반환한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 受け取った勤怠グループデータをデータベースへ登録し、処理件数を返す。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public void insert(Connection conn, AttendanceGroup group) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO ATTENDANCE_GROUP (ATTENDANCE_GROUP_ID, GROUP_NAME) VALUES (ATTENDANCE_GROUP_SEQ.NEXTVAL, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, group.getGroupName());
			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 근태그룹 단건 조회
	// 조회 조건에 맞는 By식별번호 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合うBy識別番号データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public AttendanceGroup selectById(Connection conn, int groupId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM ATTENDANCE_GROUP WHERE ATTENDANCE_GROUP_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, groupId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				AttendanceGroup group = new AttendanceGroup();
				group.setAttendanceGroupId(rs.getInt("ATTENDANCE_GROUP_ID"));
				group.setGroupName(rs.getString("GROUP_NAME"));
				return group;
			}
			return null;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 근태그룹 전체 목록 조회
	// 조회 조건에 맞는 전체 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合う全体データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public List<AttendanceGroup> selectAll(Connection conn) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM ATTENDANCE_GROUP ORDER BY ATTENDANCE_GROUP_ID ASC";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			List<AttendanceGroup> result = new ArrayList<>();
			while (rs.next()) {
				AttendanceGroup group = new AttendanceGroup();
				group.setAttendanceGroupId(rs.getInt("ATTENDANCE_GROUP_ID"));
				group.setGroupName(rs.getString("GROUP_NAME"));
				result.add(group);
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 근태그룹 수정
	// 식별조건에 해당하는 근태그룹 데이터를 전달받은 값으로 수정한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 識別条件に該当する勤怠グループデータを受け取った値で更新する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public int update(Connection conn, AttendanceGroup group) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "UPDATE ATTENDANCE_GROUP SET GROUP_NAME = ? WHERE ATTENDANCE_GROUP_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, group.getGroupName());
			pstmt.setInt(2, group.getAttendanceGroupId());
			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 근태그룹 삭제
	// 선택되거나 식별된 근태그룹 데이터를 삭제하고 관련 상태를 정리한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 選択または識別された勤怠グループデータを削除し、関連状態を整理する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public int delete(Connection conn, int groupId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM ATTENDANCE_GROUP WHERE ATTENDANCE_GROUP_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, groupId);
			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 근태그룹과 하위 근태항목을 계층형(Tree)으로 묶어서 조회
	// 조회 조건에 맞는 그룹With항목 목록 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合うグループWith項目一覧データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public List<AttendanceGroupWithItemsDto> selectGroupWithItems(Connection conn) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<AttendanceGroupWithItemsDto> resultList = new ArrayList<>();

		try {
			// ATTENDANCE_GROUP을 기준으로 ATTENDANCE_ITEM과 LEAVE_ITEM을 LEFT JOIN
			// 関連テーブルを識別キーで結合し、画面表示に必要な名称と詳細情報をまとめて取得する。
			String sql = "SELECT " + " g.ATTENDANCE_GROUP_ID, g.GROUP_NAME, "
					+ "    i.ATTENDANCE_ITEM_ID, i.ATTEND_NAME, i.UNIT_TYPE, "
					+ "    i.DEDUCT_LEAVE_ID, i.WORK_HOUR_TYPE, i.USE_YN, " + "    l.ITEM_NAME "
					+ "FROM ATTENDANCE_GROUP g "
					+ "LEFT JOIN ATTENDANCE_ITEM i ON g.ATTENDANCE_GROUP_ID = i.ATTENDANCE_GROUP_ID "
					+ "LEFT JOIN LEAVE_ITEM l ON i.DEDUCT_LEAVE_ID = l.LEAVE_ITEM_ID "
					+ "ORDER BY g.ATTENDANCE_GROUP_ID ASC, i.ATTENDANCE_ITEM_ID ASC";

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			int currentGroupId = -1;
			AttendanceGroupWithItemsDto currentGroup = null;

			while (rs.next()) {
				int groupId = rs.getInt("ATTENDANCE_GROUP_ID");

				// 1. 그룹이 바뀔 때마다 새로운 DTO 생성 후 리스트에 추가
				// 入力された内容を新しい業務データとして構成し、関連データとともに登録する。
				if (groupId != currentGroupId) {
					currentGroup = new AttendanceGroupWithItemsDto();
					currentGroup.setAttendanceGroupId(groupId);
					currentGroup.setGroupName(rs.getString("GROUP_NAME"));
					currentGroup.setItems(new ArrayList<>()); // 하위 항목을 담을 빈 리스트 초기화

					resultList.add(currentGroup);
					currentGroupId = groupId;
				}

				// 2. 조인된 하위 근태항목(ATTENDANCE_ITEM)이 존재하면 리스트에 담기
				// 対象一覧を順番に処理し、各行の入力値または照会結果を同じ基準で構成する。
				int itemId = rs.getInt("ATTENDANCE_ITEM_ID");
				if (!rs.wasNull()) {
					AttendanceItemResponseDto item = new AttendanceItemResponseDto();
					item.setAttendanceItemId(itemId);
					item.setAttendanceGroupId(groupId);
					item.setAttendName(rs.getString("ATTEND_NAME"));
					item.setUnitType(rs.getString("UNIT_TYPE"));

					int leaveId = rs.getInt("DEDUCT_LEAVE_ID");
					item.setDeductLeaveId(rs.wasNull() ? null : leaveId);

					item.setWorkHourType(rs.getString("WORK_HOUR_TYPE"));
					item.setUseYn(rs.getString("USE_YN"));
					item.setGroupName(rs.getString("GROUP_NAME")); // 상위 그룹명 세팅

					// 분석된 스키마에 맞게 l.ITEM_NAME 컬럼의 값을 꺼내어 DTO에 세팅
					// 結合結果の列を分析済みスキーマに合わせて読み取り、対応するDTO属性へ設定する。
					item.setDeductLeaveName(rs.getString("ITEM_NAME"));

					// 생성한 하위 항목을 현재 그룹의 리스트에 추가
					// 入力された内容を新しい業務データとして構成し、関連データとともに登録する。
					currentGroup.getItems().add(item);
				}
			}
			return resultList;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}
}
