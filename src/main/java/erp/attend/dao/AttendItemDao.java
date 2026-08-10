package erp.attend.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import erp.attend.model.AttendItem;

public class AttendItemDao {

	private static AttendItemDao attendItemDao = new AttendItemDao();
	public static AttendItemDao getInstance() {
		return attendItemDao;
	}
	private AttendItemDao() {
	}
	public AttendItem selectById(Connection conn, int attendItemId) throws SQLException{
		String sql = "SELECT * FROM ATTEND_ITEM WHERE ATEEND_ITEM_ID=?";
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setInt(1, attendItemId);
			try(ResultSet rs = pstmt.executeQuery()){
				if (rs.next()) {
					return convertAttendItem(rs);
				}
			}
		}
		return null;
	}
	
	public List<AttendItem> selectAll(Connection conn) throws SQLException{
		String sql = "SELECT * FROM ATTEND_ITEM";
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){
			try(ResultSet rs = pstmt.executeQuery()){
				List<AttendItem> list = new ArrayList<>();
				while(rs.next()) {
					list.add(convertAttendItem(rs));
				}
				return list;
			}
		}
	}
	
    private AttendItem convertAttendItem(ResultSet rs) throws SQLException {
        AttendItem item = new AttendItem();
        item.setAttendItemId(rs.getInt("ATTEND_ITEM_ID"));
        item.setAttendName(rs.getString("ATTEND_NAME"));
        item.setUnitType(rs.getString("UNIT_TYPE"));
        item.setAttendGroupId(rs.getInt("ATTEND_GROUP_ID"));

        int deductLeaveId = rs.getInt("DEDUCT_LEAVE_ID");
        item.setDeductLeaveId(rs.wasNull() ? null : deductLeaveId);   // NULL 가능 컬럼이므로 wasNull() 체크

        item.setWorkHourType(rs.getString("WORK_HOUR_TYPE"));
        item.setUseYn(rs.getString("USE_YN"));
        return item;
    }
}
