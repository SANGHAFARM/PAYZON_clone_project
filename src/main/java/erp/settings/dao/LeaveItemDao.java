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
public class LeaveItemDao {

	// 싱글톤 인스턴스 생성
	private static LeaveItemDao leaveItemDao = new LeaveItemDao();

	// 싱글톤 접근 메서드
	public static LeaveItemDao getInstance() {
		return leaveItemDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	private LeaveItemDao() {
	}

	// 휴가항목 등록
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
	public int delete(Connection conn, int leaveItemId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM LEAVE_ITEM WHERE LEAVE_ITEM_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, leaveItemId);
			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// ResultSet 데이터를 LeaveItem 객체로 변환
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