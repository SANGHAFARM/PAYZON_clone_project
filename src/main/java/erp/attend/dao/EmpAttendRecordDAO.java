package erp.attend.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import erp.attend.model.AttendDetailItem;
import erp.attend.model.AttendSearchCondition;
import erp.attend.model.EmpAttendRecord;
import jdbc.JdbcUtil;

public class EmpAttendRecordDAO {

	private static EmpAttendRecordDAO empAttendRecordDAO = new EmpAttendRecordDAO();

	public static EmpAttendRecordDAO getInstance() {
		return empAttendRecordDAO;
	}

	private EmpAttendRecordDAO() {
	}

	/*
	 * EmpAttendRecord테이블에 기록을 입력하는 메서드 EmpAttendRecordテーブルに記録を入力するメソッド
	 */
	public int insert(Connection conn, EmpAttendRecord ear) throws SQLException {
		String sql = "INSERT INTO EMP_ATTEND_RECORD VALUES (SEQ_EMP_ATTEND_REC_ID.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, ear.getEmpId());
			pstmt.setInt(2, ear.getAttendItemId());
			pstmt.setInt(3, ear.getLeaveItemId());
			pstmt.setDate(4, dateToSQLDate(ear.getInputDate()));
			pstmt.setDate(5, dateToSQLDate(ear.getStartDate()));
			pstmt.setDate(6, dateToSQLDate(ear.getEndDate()));
			pstmt.setDouble(7, ear.getAttendValue());
			pstmt.setLong(8, ear.getPayAmount());
			pstmt.setString(9, ear.getNote());
			return pstmt.executeUpdate();
		}
	}

	/*
	 * EmpAttendRecord테이블에 있는 기록을 근태기록ID로 조회하는 메서드
	 * EmpAttendRecordテーブルにある記録を勤怠記録IDで照会するメソッド
	 */
	public EmpAttendRecord selectById(Connection conn, int no) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM EMP_ATTEND_RECORD WHERE ATTEND_REC_ID=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, no);
			rs = pstmt.executeQuery();
			EmpAttendRecord ear = null;
			if (rs.next()) {
				ear = convertEmpAttendRecord(rs);
			}
			return ear;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	/*
	 * EmpAttendRecord테이블에 있는 기록을 사원ID와 연도와 월로 조회하는 메서드 month가 null일 시, month조건은
	 * 무시되고 해당년도 전체를 조회함 EmpAttendRecordテーブルにある記録を社員IDと年度と月で照会するメソッド
	 */
	public List<EmpAttendRecord> selectByEmpIdAndYearAndMonth(Connection conn, int empId, int year, Integer month)
			throws SQLException {
		String sql = "SELECT * FROM EMP_ATTEND_RECORD " + "WHERE EMP_ID=? " + "AND TO_CHAR(START_DATE, 'YYYY')=? "
				+ "AND (? IS NULL OR TO_CHAR(START_DATE, 'MM')=?)";
		List<EmpAttendRecord> list = new ArrayList<>();
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, empId);
			pstmt.setString(2, String.valueOf(year));
			if (month != null) {
				pstmt.setString(3, String.format("%02d", month));
				pstmt.setString(4, String.format("%02d", month));
			} else {
				pstmt.setString(3, null);
				pstmt.setString(4, null);
			}
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					list.add(convertEmpAttendRecord(rs));
				}
			}
		}
		return list;
	}

	/*
	 * EmpAttendRocrd테이블에서 사원ID와 휴가항목ID로 사용한 휴가의 합계를 조회하는 메서드
	 * EmpAttendRocrdで社員IDと休暇項目IDで使った休暇の合計を照会するメソッド
	 */
	public double selectUsedDaysByEmpIdAndLeaveItemId(Connection conn, int empId, int leaveItemId) throws SQLException {
		String sql = "SELECT NVL(SUM(ATTEND_VALUE),0) AS USED_DAYS FROM EMP_ATTEND_RECORD WHERE EMP_ID=? AND LEAVE_ITEM_ID=?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, empId);
			pstmt.setInt(2, leaveItemId);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return rs.getDouble("USED_DAYS");
				}
			}
		}
		return 0;
	}

	/*
	 * 근태관리 > 근태조회> 월별조회에서 사용하는 메서드
	 * 사원 1명의 해당 연월의 근태기록을 조회
	 */
	public List<EmpAttendRecord> selectByEmpIdAndMonthOverlap(Connection conn, int empId, int year, int month)
			throws SQLException {
		String sql = "SELECT * FROM EMP_ATTEND_RECORD " + "WHERE EMP_ID = ? "
				+ "AND START_DATE <= LAST_DAY(TO_DATE(?, 'YYYYMM')) " + "AND END_DATE >=TO_DATE(?, 'YYYYMM')";

		String yyyymm = String.format("%04d%02d", year, month);
		List<EmpAttendRecord> list = new ArrayList<>();
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, empId);
			pstmt.setString(2, yyyymm);
			pstmt.setString(3, yyyymm);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					list.add(convertEmpAttendRecord(rs));
				}
			}
		}
		return list;
	}

	/*
	 * 근태관리 > 근태조회 > 상세 조회에서 사용하는 메서드
	 * 입력일자, 근태기간, 부서, 성명, 근태그룹, 근태항목, 휴가항목, 적요로 상세 기록을 검색
	 */
	public List<AttendDetailItem> selectDetailList(Connection conn, AttendSearchCondition cond) throws SQLException {
		String sql = "SELECT ear.ATTEND_REC_ID, ear.INPUT_DATE, e.EMP_TYPE, e.EMP_NAME_KR, "
				+ "       d.DEPT_NAME, p.POS_NAME, ai.ITEM_NAME AS ATTEND_ITEM_NAME, "
				+ "       ear.START_DATE, ear.END_DATE, ear.ATTEND_VALUE, ear.PAY_AMOUNT, ear.NOTE "
				+ "FROM EMP_ATTEND_RECORD ear " + "JOIN EMPLOYEE e ON ear.EMP_ID = e.EMP_ID "
				+ "LEFT JOIN DEPARTMENT d ON e.DEPT_ID = d.DEPT_ID " + "LEFT JOIN POSITION p ON e.POS_ID = p.POS_ID "
				+ "LEFT JOIN ATTEND_ITEM ai ON ear.ATTEND_ITEM_ID = ai.ATTEND_ITEM_ID "
				+ "WHERE (? IS NULL OR ear.INPUT_DATE = ?) " + "AND (? IS NULL OR ear.END_DATE >= ?) "
				+ "AND (? IS NULL OR ear.START_DATE <= ?) " + "AND (? IS NULL OR e.DEPT_ID = ?) "
				+ "AND (? IS NULL OR e.EMP_NAME_KR LIKE '%' || ? || '%') " + "AND (? IS NULL OR e.EMP_TYPE = ?) "
				+ "AND (? IS NULL OR ear.ATTEND_ITEM_ID = ?) " + "AND (? IS NULL OR ear.LEAVE_ITEM_ID = ?) "
				+ "AND (? IS NULL OR ear.NOTE LIKE '%' || ? || '%') " + "ORDER BY ear.INPUT_DATE DESC";
		List<AttendDetailItem> list = new ArrayList<>();
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			java.sql.Date inputdate = dateToSQLDate(cond.getInputDate());
			pstmt.setDate(1, inputdate);
			pstmt.setDate(2, inputdate);

			java.sql.Date periodStart = dateToSQLDate(cond.getPeriodStart());
			pstmt.setDate(3, periodStart);
			pstmt.setDate(4, periodStart);

			java.sql.Date periodEnd = dateToSQLDate(cond.getPeriodEnd());
			pstmt.setDate(5, periodEnd);
			pstmt.setDate(6, periodEnd);

			setIntOrNull(pstmt, 7, 8, cond.getDeptId());

			pstmt.setString(9, cond.getEmpName());
			pstmt.setString(10, cond.getEmpName());

			pstmt.setString(11, cond.getEmpType());
			pstmt.setString(12, cond.getEmpType());

			setIntOrNull(pstmt, 13, 14, cond.getAttendItemId());
			setIntOrNull(pstmt, 15, 16, cond.getLeaveItemId());

			pstmt.setString(17, cond.getNote());
			pstmt.setString(18, cond.getNote());

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					list.add(convertAttendDetailItem(rs));
				}
			}
		}
		return list;
	}

	/*
	 * EmpAttendRecord테이블에 있는 기록을 수정하는 메서드 EmpAttendRecordテーブルにある記録を修正するメソッド
	 */
	public int update(Connection conn, EmpAttendRecord ear) throws SQLException {
		String sql = "UPDATE EMP_ATTEND_RECORD SET ATTEND_ITEM_ID=?, LEAVE_ITEM_ID=?, INPUT_DATE=?, START_DATE=?, END_DATE=?, ATTEND_VALUE=?, PAY_AMOUNT=?, NOTE=? WHERE ATTEND_REC_ID=?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, ear.getAttendItemId());
			pstmt.setInt(2, ear.getLeaveItemId());
			pstmt.setDate(3, dateToSQLDate(ear.getInputDate()));
			pstmt.setDate(4, dateToSQLDate(ear.getStartDate()));
			pstmt.setDate(5, dateToSQLDate(ear.getEndDate()));
			pstmt.setDouble(6, ear.getAttendValue());
			pstmt.setLong(7, ear.getPayAmount());
			pstmt.setString(8, ear.getNote());
			pstmt.setInt(9, ear.getAttendRecId());
			return pstmt.executeUpdate();
		}
	}

	/*
	 * EmpAttendRecord테이블에 있는 기록을 삭제하는 메서드 EmpAttendRecordテーブルにある記録を削除するメソッド
	 */
	public int delete(Connection conn, int no) throws SQLException {
		String sql = "DELETE FROM EMP_ATTEND_RECORD WHERE ATTEND_REC_ID=?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, no);
			return pstmt.executeUpdate();
		}
	}

	/*
	 * java.util.Date 타입을 java.sql.Date타입으로 변환하는 메서드
	 * java.util.Dateタイプをjava.sql.Dateタイプに変換するメソッド
	 */
	private java.sql.Date dateToSQLDate(java.util.Date date) {
		return (date != null) ? new java.sql.Date(date.getTime()) : null;
	}

	/*
	 * ResultSet으로 EmpAttendRecord객체를 만들어 반환하는 메서드
	 * ResultSetでEmpAttendRecordオブジェクトを作って返すメソッド
	 */
	private EmpAttendRecord convertEmpAttendRecord(ResultSet rs) throws SQLException {
		EmpAttendRecord ear = new EmpAttendRecord();
		ear.setAttendRecId(rs.getInt("ATTEND_REC_ID"));
		ear.setEmpId(rs.getInt("EMP_ID"));
		ear.setAttendItemId(rs.getInt("ATTEND_ITEM_ID"));
		ear.setLeaveItemId(rs.getInt("LEAVE_ITEM_ID"));
		ear.setInputDate(rs.getDate("INPUT_DATE"));
		ear.setStartDate(rs.getDate("START_DATE"));
		ear.setEndDate(rs.getDate("END_DATE"));
		ear.setAttendValue(rs.getDouble("ATTEND_VALUE"));
		ear.setPayAmount(rs.getLong("PAY_AMOUNT"));
		ear.setNote(rs.getString("NOTE"));
		return ear;
	}

	/*
	 * ResultSet으로 AttendDetailItem을 만들어 반환하는 메서드
	 */	
	private AttendDetailItem convertAttendDetailItem(ResultSet rs) throws SQLException {
		AttendDetailItem item = new AttendDetailItem();
		item.setAttendRecId(rs.getInt("ATTEND_REC_ID"));
		item.setInputDate(rs.getDate("INPUT_DATE"));
		item.setEmpType(rs.getString("EMP_TYPE"));
		item.setEmpNameKr(rs.getString("EMP_NAME_KR"));
		item.setDeptName(rs.getString("DEPT_NAME"));
		item.setPosName(rs.getString("POS_NAME"));
		item.setAttendItemName(rs.getString("ATTEND_ITEM_NAME"));
		item.setStartDate(rs.getDate("START_DATE"));
		item.setEndDate(rs.getDate("END_DATE"));
		item.setAttendValue(rs.getDouble("ATTEND_VALUE"));
		item.setPayAmount(rs.getLong("PAY_AMOUNT"));
		item.setNote(rs.getString("NOTE"));
		return item;
	}

	/*
	 * 값이 null인지 구분하고, null일 시, SQLNULL을 입력하는 메서드
	 */
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
