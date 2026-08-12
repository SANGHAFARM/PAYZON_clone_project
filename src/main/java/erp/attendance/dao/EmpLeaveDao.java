package erp.attendance.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import erp.attendance.model.EmpLeave;
import erp.attendance.model.EmpLeaveStatusItem;
import erp.attendance.model.LeaveStatusCondition;

public class EmpLeaveDao {
	private static EmpLeaveDao empLeaveDao = new EmpLeaveDao();

	public static EmpLeaveDao getInstance() {
		return empLeaveDao;
	}

	private EmpLeaveDao() {

	}
	
	/*
	 * 사원 1명의 휴가정보를 조회하는 메서드
	 */	
	public EmpLeave selectByEmpIdAndLeaveItemId(Connection conn, int empId, int leaveItemId) throws SQLException {
		String sql = "SELECT * FROM EMP_LEAVE WHERE EMP_ID = ? AND LEAVE_ITEM_ID = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, empId);
			pstmt.setInt(2, leaveItemId);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return convertEmpLeave(rs);
				}
			}
		}
		return null;
	}
	
	/*
	 * 휴가조회(전체목록) 화면용 - 필터에 맞는 여러 사원의, 특정 휴가항목 전체/사용/잔여 조회
	 */
	public List<EmpLeaveStatusItem> selectLeaveStatusList(Connection conn, LeaveStatusCondition cond) throws SQLException {
		String sql = "SELECT e.EMP_ID, e.EMP_TYPE, e.EMP_NO, e.EMP_NAME_KR, d.DEPT_NAME, p.POS_NAME, "
				+ "       li.ITEM_NAME, NVL(el.TOTAL_DAYS, 0) AS TOTAL_DAYS, NVL(u.USED_DAYS, 0) AS USED_DAYS "
				+ "FROM EMPLOYEE e "
				+ "LEFT JOIN DEPARTMENT d ON e.DEPT_ID = d.DEPT_ID "
				+ "LEFT JOIN POSITION p ON e.POS_ID = p.POS_ID "
				+ "JOIN LEAVE_ITEM li ON li.LEAVE_ITEM_ID = ? "
				+ "LEFT JOIN EMP_LEAVE el ON el.EMP_ID = e.EMP_ID AND el.LEAVE_ITEM_ID = li.LEAVE_ITEM_ID "
				+ "LEFT JOIN (SELECT EMP_ID, SUM(ATTEND_VALUE) AS USED_DAYS FROM EMP_ATTEND_RECORD "
				+ "           WHERE LEAVE_ITEM_ID = ? GROUP BY EMP_ID) u ON u.EMP_ID = e.EMP_ID "
				+ "WHERE (? IS NULL OR e.STATUS = ?) "
				+ "AND (? IS NULL OR e.EMP_TYPE = ?) "
				+ "AND (? IS NULL OR e.DEPT_ID = ?) "
				+ "AND (? IS NULL OR e.POS_ID = ?) "
				+ "AND (e.EMP_NAME_KR LIKE ? OR e.EMP_NO LIKE ?) "
				+ "ORDER BY e.EMP_ID";

		List<EmpLeaveStatusItem> list = new ArrayList<>();
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, cond.getLeaveItemId());
			pstmt.setInt(2, cond.getLeaveItemId());
			pstmt.setString(3, cond.getStatus());
			pstmt.setString(4, cond.getStatus());
			pstmt.setString(5, cond.getEmpType());
			pstmt.setString(6, cond.getEmpType());
			setIntOrNull(pstmt, 7, 8, cond.getDeptId());
			setIntOrNull(pstmt, 9, 10, cond.getPosId());
			String keyword = (cond.getKeyword() != null) ? cond.getKeyword() : "";
			pstmt.setString(11, "%" + keyword + "%");
			pstmt.setString(12, "%" + keyword + "%");

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					list.add(convertEmpLeaveStatusItem(rs));
				}
			}
		}
		return list;
	}
	
	private EmpLeave convertEmpLeave(ResultSet rs) throws SQLException {
		EmpLeave empLeave = new EmpLeave();
		empLeave.setEmpLeaveId(rs.getInt("EMP_LEAVE_ID"));
		empLeave.setEmpId(rs.getInt("EMP_ID"));
		empLeave.setLeaveItemId(rs.getInt("LEAVE_ITEM_ID"));
		empLeave.setTotalDays(rs.getDouble("TOTAL_DAYS"));
		return empLeave;
	}
	
	
	private EmpLeaveStatusItem convertEmpLeaveStatusItem(ResultSet rs) throws SQLException {
		EmpLeaveStatusItem item = new EmpLeaveStatusItem();
		item.setEmpId(rs.getInt("EMP_ID"));
		item.setEmpType(rs.getString("EMP_TYPE"));
		item.setEmpNo(rs.getString("EMP_NO"));
		item.setEmpNameKr(rs.getString("EMP_NAME_KR"));
		item.setDeptName(rs.getString("DEPT_NAME"));
		item.setPosName(rs.getString("POS_NAME"));
		item.setItemName(rs.getString("ITEM_NAME"));
		double total = rs.getDouble("TOTAL_DAYS");
		double used = rs.getDouble("USED_DAYS");
		item.setTotalDays(total);
		item.setUsedDays(used);
		item.setRemainDays(total - used);
		return item;
	}
	
	private void setIntOrNull(PreparedStatement pstmt, int idx1, int idx2, Integer value) throws SQLException {
		if (value != null) {
			pstmt.setInt(idx1, value);
			pstmt.setInt(idx2, value);
		} else {
			pstmt.setNull(idx1, java.sql.Types.NUMERIC);
			pstmt.setNull(idx2, java.sql.Types.NUMERIC);
		}
	}
	
	
}
