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
public class AttendanceGroupDao {

	// 싱글톤 인스턴스 생성
	private static AttendanceGroupDao attendanceGroupDao = new AttendanceGroupDao();

	// 싱글톤 접근 메서드
	public static AttendanceGroupDao getInstance() {
		return attendanceGroupDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	private AttendanceGroupDao() {
	}

	// 근태그룹 등록
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
	public List<AttendanceGroupWithItemsDto> selectGroupWithItems(Connection conn) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<AttendanceGroupWithItemsDto> resultList = new ArrayList<>();

		try {
			// ATTENDANCE_GROUP을 기준으로 ATTENDANCE_ITEM과 LEAVE_ITEM을 LEFT JOIN
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
				if (groupId != currentGroupId) {
					currentGroup = new AttendanceGroupWithItemsDto();
					currentGroup.setAttendanceGroupId(groupId);
					currentGroup.setGroupName(rs.getString("GROUP_NAME"));
					currentGroup.setItems(new ArrayList<>()); // 하위 항목을 담을 빈 리스트 초기화

					resultList.add(currentGroup);
					currentGroupId = groupId;
				}

				// 2. 조인된 하위 근태항목(ATTENDANCE_ITEM)이 존재하면 리스트에 담기
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
					item.setDeductLeaveName(rs.getString("ITEM_NAME"));

					// 생성한 하위 항목을 현재 그룹의 리스트에 추가
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