package erp.attend.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import erp.attend.model.LeaveItem;
import jdbc.JdbcUtil;

public class LeaveItemDao {
	private static LeaveItemDao leaveItemDao = new LeaveItemDao();

	public LeaveItemDao getInstance() {
		return leaveItemDao;
	}

	private LeaveItemDao() {

	}

	public LeaveItem selectUsableByCategory(Connection conn, String category) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM LEAVE_ITEM WHERE ITEM_NAME LIKE ? AND USE_YN='Y'";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + category);
			rs = pstmt.executeQuery();
			LeaveItem li = null;
			if (rs.next()) {
				li = convertLeaveItem(rs);
			}
			return li;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	private LeaveItem convertLeaveItem(ResultSet rs) throws SQLException {
		LeaveItem li = new LeaveItem();
		li.setLeaveItemId(rs.getInt("LEAVE_ITEM_ID"));
		li.setItemName(rs.getString("ITEM_NAME"));
		li.setApplyStartDate(rs.getDate("APPLY_START_DATE"));
		li.setApplyEndDate(rs.getDate("APPLY_END_DATE"));
		li.setUseYn(rs.getString("USE_YN"));
		return li;
	}

}
