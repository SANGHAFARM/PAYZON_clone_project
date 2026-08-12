package erp.settings.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
}