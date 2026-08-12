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
public class AttendanceItemDao {

	// 싱글톤 인스턴스 생성
	private static AttendanceItemDao attendanceItemDao = new AttendanceItemDao();

	// 싱글톤 접근 메서드
	public static AttendanceItemDao getInstance() {
		return attendanceItemDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	private AttendanceItemDao() {
	}

	// 근태항목 등록
	// 시퀀스를 사용하여 기본키 발급 및 항목 데이터 저장
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
	public List<AttendanceItem> selectAll(Connection conn) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM ATTENDANCE_ITEM ORDER BY ATTENDANCE_GROUP_ID ASC, ATTENDANCE_ITEM_ID ASC";

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			List<AttendanceItem> result = new ArrayList<>();
			while (rs.next()) {
				result.add(makeItemFromResultSet(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// ResultSet 데이터를 AttendanceItem 객체로 변환
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
}