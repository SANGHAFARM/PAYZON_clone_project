package erp.attendance.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import erp.attendance.dto.AttendanceDetailDto;
import erp.attendance.dto.AttendanceRecordDto;
import erp.attendance.dto.MonthlyAttendanceDto;
import erp.attendance.model.EmployeeAttendance;
import erp.attendance.service.request.AttendanceDetailRequest;
import erp.attendance.service.request.MonthlyAttendanceRequest;
/*import erp.attend.model.AttendDetailItem;
import erp.attend.model.AttendSearchCondition;*/
/*import erp.attend.model.EmpAttendRecord;*/
import jdbc.JdbcUtil;

//전체적으로 수정필요
public class EmployeeAttendanceDao {

	private static EmployeeAttendanceDao employeeAttendanceDao = new EmployeeAttendanceDao();

	public static EmployeeAttendanceDao getInstance() {
		return employeeAttendanceDao;
	}

	private EmployeeAttendanceDao() {

	};

	/*
	 * 근태기록 입력
	 */
	public int insert(Connection conn, EmployeeAttendance empAt) throws SQLException {
		String sql = "INSERT INTO EMPLOYEE_ATTENDANCE VALUES (EMPLOYEE_ATTENDANCE_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, empAt.getEmployeeId());
			pstmt.setInt(2, empAt.getAttendanceItemId());
			setIntOrNull(pstmt, 3, empAt.getLeaveItemId());
			pstmt.setTimestamp(4, new Timestamp(empAt.getInputDate().getTime()));
			pstmt.setTimestamp(5, new Timestamp(empAt.getStartDate().getTime()));
			pstmt.setTimestamp(6, new Timestamp(empAt.getEndDate().getTime()));
			pstmt.setDouble(7, empAt.getAttendValue());
			pstmt.setLong(8, empAt.getPayAmount());
			pstmt.setString(9, empAt.getNote());
			return pstmt.executeUpdate();
		}
	}

	/*
	 * EmpAttendRecord테이블에 있는 기록을 근태기록ID로 조회하는 메서드
	 * EmpAttendRecordテーブルにある記録を勤怠記録IDで照会するメソッド
	 */

	public EmployeeAttendance selectById(Connection conn, int no) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM EMPLOYEE_ATTENDANCE WHERE EMPLOYEE_ATTENDANCE_ID=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, no);
			rs = pstmt.executeQuery();
			EmployeeAttendance empAt = null;
			if (rs.next()) {
				empAt = convertEmployeeAttendance(rs);
			}
			return empAt;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	/*
	 * EmpAttendRecord테이블에 있는 기록을 사원ID와 연도와 월로 조회하는 메서드 month가 null일 시, month조건은
	 * 무시되고 해당년도 전체를 조회함 EmpAttendRecordテーブルにある記録を社員IDと年度と月で照会するメソッド
	 */

	public List<AttendanceRecordDto> selectByEmpIdAndYearAndMonth(Connection conn, int empId, int year, Integer month)
			throws SQLException {
		String sql = "SELECT A.EMPLOYEE_ATTENDANCE_ID, A.INPUT_DATE, A.ATTENDANCE_ITEM_ID, I.ATTEND_NAME, A.START_DATE, A.END_DATE, A.ATTEND_VALUE, A.PAY_AMOUNT, A.NOTE "
				+ "FROM EMPLOYEE_ATTENDANCE A LEFT JOIN ATTENDANCE_ITEM I ON I.ATTENDANCE_ITEM_ID = A.ATTENDANCE_ITEM_ID "
				+ "WHERE EMPLOYEE_ID = ? " + "AND TO_CHAR(START_DATE, 'YYYY') = ? "
				+ "AND (? IS NULL OR TO_CHAR(START_DATE, 'MM') = ?)";
		List<AttendanceRecordDto> list = new ArrayList<>();
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
					AttendanceRecordDto dto = new AttendanceRecordDto();
					dto.setEmployeeAttendanceId(rs.getInt("EMPLOYEE_ATTENDANCE_ID"));
					dto.setInputDate(rs.getDate("INPUT_DATE"));
					dto.setAttendanceItemId(rs.getInt("ATTENDANCE_ITEM_ID"));
					dto.setAttendName(rs.getString("ATTEND_NAME"));
					dto.setStartDate(rs.getDate("START_DATE"));
					dto.setEndDate(rs.getDate("END_DATE"));
					dto.setAttendValue(rs.getDouble("ATTEND_VALUE"));
					dto.setPayAmount(rs.getLong("PAY_AMOUNT"));
					dto.setNote(rs.getString("NOTE"));
					list.add(dto);
				}
			}
		}
		return list;

	}

	/*
	 * public List<EmployeeAttendance> selectByEmpIdAndYearAndMonth(Connection conn,
	 * int empId, int year, Integer month) throws SQLException { String sql =
	 * "SELECT * FROM EMPLOYEE_ATTENDANCE " + "WHERE EMP_ID=? " +
	 * "AND TO_CHAR(START_DATE, 'YYYY')=? " +
	 * "AND (? IS NULL OR TO_CHAR(START_DATE, 'MM')=?)"; List<EmployeeAttendance>
	 * list = new ArrayList<>(); try (PreparedStatement pstmt =
	 * conn.prepareStatement(sql)) { pstmt.setInt(1, empId); pstmt.setString(2,
	 * String.valueOf(year)); if (month != null) { pstmt.setString(3,
	 * String.format("%02d", month)); pstmt.setString(4, String.format("%02d",
	 * month)); } else { pstmt.setString(3, null); pstmt.setString(4, null); } try
	 * (ResultSet rs = pstmt.executeQuery()) { while (rs.next()) {
	 * list.add(convertEmployeeAttendance(rs)); } } } return list; }
	 */

	/*
	 * EmpAttendRocrd테이블에서 사원ID와 휴가항목ID로 사용한 휴가의 합계를 조회하는 메서드
	 * EmpAttendRocrdで社員IDと休暇項目IDで使った休暇の合計を照会するメソッド
	 */

	public double selectUsedDaysByEmpIdAndLeaveItemId(Connection conn, int empId, Integer leaveItemId)
			throws SQLException {
		String sql = "SELECT NVL(SUM(ATTEND_VALUE),0) AS USED_DAYS FROM EMPLOYEE_ATTENDANCE WHERE EMPLOYEE_ID=? AND LEAVE_ITEM_ID=?";
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
	 * 근태관리>근태조회>
	 * 
	 * 월별조회에서 사용하는 메서드 사원 1 명의 해당 연월의 근태기록을 조회
	 */
	/*
	 * public List<EmployeeAttendance> selectByEmpIdAndMonthOverlap(Connection conn,
	 * Long empId, int year, int month) throws SQLException { String sql =
	 * "SELECT * FROM EMPLOYEE_ATTENDANCE " + "WHERE EMPLOYEE_ID = ? " +
	 * "AND START_DATE <= LAST_DAY(TO_DATE(?, 'YYYYMM')) " +
	 * "AND END_DATE >=TO_DATE(?, 'YYYYMM')";
	 * 
	 * String yyyymm = String.format("%04d%02d", year, month);
	 * List<EmployeeAttendance> list = new ArrayList<>(); try (PreparedStatement
	 * pstmt = conn.prepareStatement(sql)) { pstmt.setLong(1, empId);
	 * pstmt.setString(2, yyyymm); pstmt.setString(3, yyyymm); try (ResultSet rs =
	 * pstmt.executeQuery()) { while (rs.next()) {
	 * list.add(convertEmployeeAttendance(rs)); } } } return list; }
	 */

	// 월별조회 사원목록
	public List<MonthlyAttendanceDto> selectByCondition(Connection conn, MonthlyAttendanceRequest req)
			throws SQLException {
		String sql = "SELECT E.EMPLOYEE_ID, E.EMP_TYPE, E.EMP_NO, E.EMP_NAME_KR, D.DEPARTMENT_NAME, J.JOB_POSITION_NAME "
				+ "FROM EMPLOYEE E " + "LEFT JOIN DEPARTMENT D ON D.DEPARTMENT_ID = E.DEPARTMENT_ID "
				+ "LEFT JOIN JOB_POSITION J ON J.JOB_POSITION_ID = E.JOB_POSITION_ID "
				+ "WHERE (? IS NULL OR E.STATUS = ? ) " + "AND (? IS NULL OR E.EMP_TYPE = ?) "
				+ "AND (? IS NULL OR D.DEPARTMENT_ID = ? ) " + "AND (? IS NULL OR J.JOB_POSITION_ID = ? )"
				+ "ORDER BY EMPLOYEE_ID DESC";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, req.getStatus());
			pstmt.setString(2, req.getStatus());
			pstmt.setString(3, req.getEmpType());
			pstmt.setString(4, req.getEmpType());
			setIntOrNull(pstmt, 5, 6, req.getDepartmentId());
			setIntOrNull(pstmt, 7, 8, req.getJobPositionId());
			List<MonthlyAttendanceDto> list = new ArrayList<>();
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					MonthlyAttendanceDto dto = new MonthlyAttendanceDto();
					dto.setEmpType(rs.getString("EMP_TYPE"));
					dto.setEmpNo(rs.getString("EMP_NO"));
					dto.setEmpNameKr(rs.getString("EMP_NAME_KR"));
					dto.setDepartmentName(rs.getString("DEPARTMENT_NAME"));
					dto.setJobPositionName(rs.getString("JOB_POSITION_NAME"));
					int empId = rs.getInt("EMPLOYEE_ID");
					int year = req.getYear();
					int month = req.getMonth();
					dto.setDailyAttendance(selectDailyAttendance(conn, empId, year, month));
					dto.setTotalAttendValue(selectTotalAttendValue(conn, empId, year, month, dto));
					list.add(dto);
				}
			}
			return list;
		}

	}

	private Map<Integer, String> selectDailyAttendance(Connection conn, int empId, int year, int month)
			throws SQLException {
		// 해당 연월의 시작일과 마지막 날 계산 (예: 2026-08-01 ~ 2026-08-31)
		String targetYearMonth = String.format("%d-%02d", year, month);

		// SQL 쿼리: 해당 사원의 데이터 중, 조회하려는 월과 겹치는 기간의 데이터만 조회
		String sql = "SELECT START_DATE, END_DATE FROM EMPLOYEE_ATTENDANCE " + "WHERE EMPLOYEE_ID = ? "
				+ "  AND START_DATE <= LAST_DAY(TO_DATE(?, 'YYYY-MM')) " + "  AND END_DATE >= TO_DATE(?, 'YYYY-MM')";

		Map<Integer, String> dailyAttendance = new HashMap<>();

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, empId);
			pstmt.setString(2, targetYearMonth); // START_DATE 비교용
			pstmt.setString(3, targetYearMonth); // END_DATE 비교용

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					java.sql.Date dbStartDate = rs.getDate("START_DATE");
					java.sql.Date dbEndDate = rs.getDate("END_DATE");

					if (dbStartDate == null || dbEndDate == null) {
						continue;
					}

					LocalDate startDate = dbStartDate.toLocalDate();
					LocalDate endDate = dbEndDate.toLocalDate();

					// 기간을 하루씩 돌면서 맵에 담기
					for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
						// 조회하는 연월에 해당하는 날짜만 맵에 담기 (전월/익월로 넘어간 날짜 제외)
						if (date.getYear() == year && date.getMonthValue() == month) {
							int day = date.getDayOfMonth();
							dailyAttendance.put(day, "O");
						}
					}
				}
			}
		}
		return dailyAttendance;
	}

	public Map<String, Double> selectTotalAttendValue(Connection conn, int empId, int year, int month,
			MonthlyAttendanceDto dto) throws SQLException {
		// 해당 연월 문자열 생성 (예: "2026-08")
		String targetYearMonth = String.format("%d-%02d", year, month);

		String sql = "SELECT i.attend_name, SUM(e.attend_value) AS TOTAL_ATTEND_VALUE " + "FROM employee_attendance e "
				+ "JOIN attendance_item i " + "  ON i.attendance_item_id = e.attendance_item_id "
				+ "WHERE e.employee_id = ? " + "  AND e.start_date <= LAST_DAY(TO_DATE(?, 'YYYY-MM')) "
				+ "  AND e.end_date >= TO_DATE(?, 'YYYY-MM') " + "GROUP BY i.attend_name";

		Map<String, Double> map = new HashMap<>();

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, empId);
			pstmt.setString(2, targetYearMonth); // start_date 비교용
			pstmt.setString(3, targetYearMonth); // end_date 비교용

			try (ResultSet rs = pstmt.executeQuery()) {
				double sum = 0;
				while (rs.next()) {
					String key = rs.getString("ATTEND_NAME");
					double value = rs.getDouble("TOTAL_ATTEND_VALUE");
					if (key.equals("연차") || key.equals("반차") || key.equals("포상휴가")) {
						sum += value;
						dto.setTotalLeaveDeduction(sum);
					}
					map.put(key, value);
				}
			}
		}
		return map;
	}

	/*
	 * 근태관리>근태조회>
	 * 
	 * 상세 조회에서 사용하는 메서드 입력일자,근태기간,부서,성명,근태그룹,근태항목,휴가항목, 적요로 상세 기록을 검색
	 */

	public List<AttendanceDetailDto> selectDetailList(Connection conn, AttendanceDetailRequest req)
			throws SQLException {
		String sql = "SELECT ea.INPUT_DATE, e.EMP_TYPE, e.EMP_NAME_KR, "
				+ "       d.DEPARTMENT_NAME, p.JOB_POSITION_NAME, ai.ATTEND_NAME, "
				+ "       ea.START_DATE, ea.END_DATE, ea.ATTEND_VALUE, ea.PAY_AMOUNT, ea.NOTE "
				+ "FROM EMPLOYEE_ATTENDANCE ea " + "JOIN EMPLOYEE e ON ea.EMPLOYEE_ID = e.EMPLOYEE_ID "
				+ "LEFT JOIN DEPARTMENT d ON e.DEPARTMENT_ID = d.DEPARTMENT_ID "
				+ "LEFT JOIN JOB_POSITION p ON e.JOB_POSITION_ID = p.JOB_POSITION_ID "
				+ "LEFT JOIN ATTENDANCE_ITEM ai ON ea.ATTENDANCE_ITEM_ID = ai.ATTENDANCE_ITEM_ID "
				+ "WHERE (? IS NULL OR ea.INPUT_DATE = ?) " + "AND (? IS NULL OR ea.END_DATE >= ?) "
				+ "AND (? IS NULL OR ea.START_DATE <= ?) " + "AND (? IS NULL OR e.DEPARTMENT_ID = ?) "
				+ "AND (? IS NULL OR e.EMP_NAME_KR LIKE '%' || ? || '%') "
				+ "AND (? IS NULL OR ai.ATTENDANCE_GROUP_ID = ?) " + "AND (? IS NULL OR ea.ATTENDANCE_ITEM_ID = ?) "
				+ "AND (? IS NULL OR ai.DEDUCT_LEAVE_ID = ?) " + "AND (? IS NULL OR ea.NOTE LIKE '%' || ? || '%') "
				+ "ORDER BY ea.INPUT_DATE DESC";
		List<AttendanceDetailDto> list = new ArrayList<>();
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			java.sql.Date inputdate = dateToSQLDate(req.getInputDate());
			pstmt.setDate(1, inputdate);
			pstmt.setDate(2, inputdate);

			java.sql.Date periodStart = dateToSQLDate(req.getStartDate());
			pstmt.setDate(3, periodStart);
			pstmt.setDate(4, periodStart);

			java.sql.Date periodEnd = dateToSQLDate(req.getEndDate());
			pstmt.setDate(5, periodEnd);
			pstmt.setDate(6, periodEnd);

			setIntOrNull(pstmt, 7, 8, req.getDepartmentId());

			pstmt.setString(9, req.getEmpNameKr());
			pstmt.setString(10, req.getEmpNameKr());

			setIntOrNull(pstmt, 11, 12, req.getAttendanceGroupId());

			setIntOrNull(pstmt, 13, 14, req.getAttendanceItemId());
			setIntOrNull(pstmt, 15, 16, req.getLeaveItemId());

			pstmt.setString(17, req.getNote());
			pstmt.setString(18, req.getNote());

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					list.add(convertAttendanceDetailDto(rs));
				}
			}
		}
		return list;
	}
	
	public List<AttendanceRecordDto> selectByEmpIdAndLeaveItemId(Connection conn, int empId, int leaveItemId)
			throws SQLException {
		String sql = "SELECT A.INPUT_DATE, I.ATTEND_NAME, "
				+ "L.ITEM_NAME, A.START_DATE, A.END_DATE, A.ATTEND_VALUE, A.NOTE "
				+ "FROM EMPLOYEE_ATTENDANCE A "
				+ "LEFT JOIN ATTENDANCE_ITEM I ON I.ATTENDANCE_ITEM_ID = A.ATTENDANCE_ITEM_ID "
				+ "LEFT JOIN LEAVE_ITEM L ON L.LEAVE_ITEM_ID = A.LEAVE_ITEM_ID "
				+ "WHERE A.EMPLOYEE_ID = ? AND A.LEAVE_ITEM_ID = ? "
				+ "ORDER BY A.START_DATE ASC";
		List<AttendanceRecordDto> list = new ArrayList<>();
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, empId);
			pstmt.setInt(2, leaveItemId);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					AttendanceRecordDto dto = new AttendanceRecordDto();
					dto.setInputDate(rs.getDate("INPUT_DATE"));
					dto.setAttendName(rs.getString("ATTEND_NAME"));
					dto.setItemName(rs.getString("ITEM_NAME"));
					dto.setStartDate(rs.getDate("START_DATE"));
					dto.setEndDate(rs.getDate("END_DATE"));
					dto.setAttendValue(rs.getDouble("ATTEND_VALUE"));
					dto.setNote(rs.getString("NOTE"));
					list.add(dto);
				}
			}
		}
		return list;
	}

	private java.sql.Date dateToSQLDate(java.util.Date date) {
		return date != null ? new java.sql.Date(date.getTime()) : null;
	}

	/* EmpAttendRecord테이블에 있는 기록을 수정하는 메서드 EmpAttendRecordテーブルにある記録を修正するメソッド */
	// 근태기록 수정
	public int update(Connection conn, EmployeeAttendance empAt) throws SQLException {
		String sql = "UPDATE EMPLOYEE_ATTENDANCE SET ATTENDANCE_ITEM_ID=?, LEAVE_ITEM_ID=?, INPUT_DATE=?, START_DATE=?, END_DATE=?, ATTEND_VALUE=?, PAY_AMOUNT=?, NOTE=? WHERE EMPLOYEE_ATTENDANCE_ID=?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, empAt.getAttendanceItemId());
			setIntOrNull(pstmt, 2, empAt.getLeaveItemId());
			pstmt.setTimestamp(3, dateToTimestamp(empAt.getInputDate()));
			pstmt.setTimestamp(4, dateToTimestamp(empAt.getStartDate()));
			pstmt.setTimestamp(5, dateToTimestamp(empAt.getEndDate()));
			pstmt.setDouble(6, empAt.getAttendValue());
			pstmt.setLong(7, empAt.getPayAmount());
			pstmt.setString(8, empAt.getNote());
			pstmt.setInt(9, empAt.getEmployeeAttendanceId());
			return pstmt.executeUpdate();
		}
	}

	/*
	 * EmpAttendRecord테이블에 있는 기록을 삭제하는 메서드 EmpAttendRecordテーブルにある記録を削除するメソッド
	 */

	public int delete(Connection conn, int no) throws SQLException {
		String sql = "DELETE FROM EMPLOYEE_ATTENDANCE WHERE EMPLOYEE_ATTENDANCE_ID=?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, no);
			return pstmt.executeUpdate();
		}
	}

	/*
	 * java.util.Date 타입을 java.sql.Date타입으로 변환하는 메서드
	 * java.util.Dateタイプをjava.sql.Dateタイプに変換するメソッド
	 */

	private Timestamp dateToTimestamp(java.util.Date date) {
		return new Timestamp(date.getTime());
	}

	/*
	 * ResultSet으로 EmpAttendRecord객체를 만들어 반환하는 메서드
	 * ResultSetでEmpAttendRecordオブジェクトを作って返すメソッド
	 */
	private EmployeeAttendance convertEmployeeAttendance(ResultSet rs) throws SQLException {
		EmployeeAttendance empAt = new EmployeeAttendance();
		empAt.setEmployeeAttendanceId(rs.getInt("EMPLOYEE_ATTENDANCE_ID"));
		empAt.setEmployeeId(rs.getInt("EMPLOYEE_ID"));
		empAt.setAttendanceItemId(rs.getInt("ATTENDANCE_ITEM_ID"));
		empAt.setLeaveItemId((Integer) rs.getObject("LEAVE_ITEM_ID"));
		empAt.setInputDate(rs.getDate("INPUT_DATE"));
		empAt.setStartDate(rs.getDate("START_DATE"));
		empAt.setEndDate(rs.getDate("END_DATE"));
		empAt.setAttendValue(rs.getDouble("ATTEND_VALUE"));
		empAt.setPayAmount(rs.getLong("PAY_AMOUNT"));
		empAt.setNote(rs.getString("NOTE"));
		return empAt;
	}

	/*
	 * ResultSet으로 AttendDetailItem을 만들어 반환하는 메서드*
	 */
	private AttendanceDetailDto convertAttendanceDetailDto(ResultSet rs) throws SQLException {
		AttendanceDetailDto dto = new AttendanceDetailDto();
		dto.setInputDate(rs.getDate("INPUT_DATE"));
		dto.setEmpType(rs.getString("EMP_TYPE"));
		dto.setEmpNameKr(rs.getString("EMP_NAME_KR"));
		dto.setDepartmentName(rs.getString("DEPARTMENT_NAME"));
		dto.setJobPositionName(rs.getString("JOB_POSITION_NAME"));
		dto.setAttendName(rs.getString("ATTEND_NAME"));
		dto.setStartDate(rs.getDate("START_DATE"));
		dto.setEndDate(rs.getDate("END_DATE"));
		dto.setAttendValue(rs.getDouble("ATTEND_VALUE"));
		dto.setPayAmount(rs.getLong("PAY_AMOUNT"));
		dto.setNote(rs.getString("NOTE"));
		return dto;
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

	private void setIntOrNull(PreparedStatement pstmt, int idx, Integer value) throws SQLException {
		if (value != null) {
			pstmt.setInt(idx, value);
		} else {
			pstmt.setNull(idx, java.sql.Types.NUMERIC);
		}
	}

}
