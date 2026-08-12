package erp.attendance.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import erp.settings.model.AttendanceItem;

/*import erp.attend.model.AttendItem;*/

public class AttendanceItemDao {

	private static AttendanceItemDao attendanceItemDao = new AttendanceItemDao();

	public static AttendanceItemDao getInstance() {
		return attendanceItemDao;
	}

	private AttendanceItemDao() {
	}

	public AttendanceItem selectById(Connection conn, long attendItemId) throws SQLException {
		String sql = "SELECT * FROM ATTENDANCE_ITEM WHERE ATEENDANCE_ITEM_ID=?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setLong(1, attendItemId);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return convertAttendanceItem(rs);
				}
			}
		}
		return null;
	}

	// 모든 근태항목 조회
	public List<AttendanceItem> selectAll(Connection conn) throws SQLException {
		String sql = "SELECT * FROM ATTENDANCE_ITEM";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			try (ResultSet rs = pstmt.executeQuery()) {
				List<AttendanceItem> list = new ArrayList<>();
				while (rs.next()) {
					list.add(convertAttendanceItem(rs));
				}
				return list;
			}
		}
	}

	private AttendanceItem convertAttendanceItem(ResultSet rs) throws SQLException {
	  AttendanceItem item = new AttendanceItem();
	  item.setAttendanceItemId(rs.getLong("ATTENDANCE_ITEM_ID"));
	  item.setAttendName(rs.getString("ATTEND_NAME")); //ATTENDANCE_NAME으로 바뀔수도
	  item.setUnitType(rs.getString("UNIT_TYPE"));
	  item.setAttendanceGroupId(rs.getLong("ATTENDANCE_GROUP_ID"));
	  
	  long deductLeaveId = rs.getLong("DEDUCT_LEAVE_ID");
	  item.setDeductLeaveId(rs.wasNull() ? null : deductLeaveId); // NULL 가능 컬럼이므로
	  
	  item.setWorkHourType(rs.getString("WORK_HOUR_TYPE"));
	  item.setUseYn(rs.getString("USE_YN")); return item; }

}
