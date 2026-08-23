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
import erp.attendance.dto.AttendanceEmployeeRecordDto;
import erp.attendance.dto.AttendanceMonthlyDto;
import erp.attendance.model.EmployeeAttendance;
import erp.attendance.service.request.AttendanceDetailSearchRequest;
import erp.attendance.service.request.AttendanceEmployeeSearchRequest;
import erp.attendance.service.request.AttendanceMonthlySearchRequest;
import jdbc.JdbcUtil;

//전체적으로 수정필요
// 사원근태 데이터를 데이터베이스에서 조회하고 저장·수정·삭제한다.
// 社員勤怠データをデータベースから照会し、登録・更新・削除する。
public class EmployeeAttendanceDao {

	private static EmployeeAttendanceDao employeeAttendanceDao = new EmployeeAttendanceDao();

	// 여러 서비스에서 공유할 수 있도록 생성된 싱글톤 인스턴스를 반환한다.
	// 객체 생성을 한 곳으로 제한하여 모든 호출자가 동일한 DAO 인스턴스를 재사용하게 한다.
	// 複数のサービスで共有できるように生成されたシングルトンインスタンスを返す。
	// オブジェクト生成を一箇所へ制限し、すべての呼び出し側が同じDAOインスタンスを再利用できるようにする。
	public static EmployeeAttendanceDao getInstance() {
		return employeeAttendanceDao;
	}

	// 전달받은 값으로 사원근태 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で社員勤怠オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	private EmployeeAttendanceDao() {

	};

	/*
	 * 근태기록 입력 社員の勤務・休暇記録と適用期間を確認し、勤怠照会または残日数計算へ反映する。
	 */
	// 전달받은 사원근태 데이터를 데이터베이스에 등록하고 처리 건수를 반환한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 受け取った社員勤怠データをデータベースへ登録し、処理件数を返す。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
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

	// 조회 조건에 맞는 By식별번호 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로
	// 변환한다.
	// 検索条件に合うBy識別番号データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public EmployeeAttendance selectEmployeeAttendanceById(Connection conn, int no) throws SQLException {
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
	 * 月条件は適用せず、指定社員と年度に該当する勤怠記録全体を照会する。
	 */

	// 조회 조건에 맞는 ByEmp식별번호And연도And월 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로
	// 변환한다.
	// 検索条件に合うByEmp識別番号And年度And月データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public List<AttendanceEmployeeRecordDto> selectAttendanceEmployee(Connection conn, AttendanceEmployeeSearchRequest req)
			throws SQLException {
		String sql = "SELECT A.EMPLOYEE_ATTENDANCE_ID, A.INPUT_DATE, A.ATTENDANCE_ITEM_ID, I.ATTEND_NAME, A.START_DATE, A.END_DATE, A.ATTEND_VALUE, A.PAY_AMOUNT, A.NOTE "
				+ "FROM EMPLOYEE_ATTENDANCE A LEFT JOIN ATTENDANCE_ITEM I ON I.ATTENDANCE_ITEM_ID = A.ATTENDANCE_ITEM_ID "
				+ "WHERE EMPLOYEE_ID = ? " + "AND TO_CHAR(START_DATE, 'YYYY') = ? "
				+ "AND (? IS NULL OR TO_CHAR(START_DATE, 'MM') = ?)";
		List<AttendanceEmployeeRecordDto> list = new ArrayList<>();
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, req.getEmployeeId());
			pstmt.setString(2, String.valueOf(req.getYear()));
			if (req.getMonth() != null) {
				pstmt.setString(3, String.format("%02d", req.getMonth()));
				pstmt.setString(4, String.format("%02d", req.getMonth()));

			} else {
				pstmt.setString(3, null);
				pstmt.setString(4, null);
			}
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					AttendanceEmployeeRecordDto dto = new AttendanceEmployeeRecordDto();
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

	// 조회 조건에 맞는 사용일수ByEmp식별번호And휴가항목식별번호 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로
	// 변환한다.
	// 検索条件に合う使用日数ByEmp識別番号And休暇項目識別番号データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
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
	 * 월별조회에서 사용하는 메서드 사원 1 명의 해당 연월의 근태기록을 조회 基準日と期間の境界を確認し、照会・計算に使用できる日付形式へ変換する。
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
	// 조회 조건에 맞는 By검색조건 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로
	// 변환한다.
	// 検索条件に合うBy検索条件データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public List<AttendanceMonthlyDto> selectAttendanceMonthly(Connection conn, AttendanceMonthlySearchRequest req)
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
			List<AttendanceMonthlyDto> list = new ArrayList<>();
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					AttendanceMonthlyDto dto = new AttendanceMonthlyDto();
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

	// 조회 조건에 맞는 사원근태 데이터를 한달 단위로 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로
	// 변환한다.
	// 検索条件に合う社員勤怠データを一か月単位でデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	private Map<Integer, String> selectDailyAttendance(Connection conn, int empId, int year, int month)
			throws SQLException {
		// 해당 연월의 시작일과 마지막 날 계산 (예: 2026-08-01 ~ 2026-08-31)
		// 基準日と期間の境界を確認し、照会・計算に使用できる日付形式へ変換する。
		String targetYearMonth = String.format("%d-%02d", year, month);

		// SQL 쿼리: 해당 사원의 데이터 중, 조회하려는 월과 겹치는 기간의 데이터만 조회
		// 業務条件に合うSQLを準備し、入力値をバインドしてデータベースで実行する。
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
					// 基準日と期間の境界を確認し、照会・計算に使用できる日付形式へ変換する。
					for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
						// 조회하는 연월에 해당하는 날짜만 맵에 담기 (전월/익월로 넘어간 날짜 제외)
						// 基準日と期間の境界を確認し、照会・計算に使用できる日付形式へ変換する。
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

	// 조회 조건에 맞는 합계Attend값 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로
	// 변환한다.
	// 検索条件に合う合計Attend値データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public Map<String, Double> selectTotalAttendValue(Connection conn, int empId, int year, int month,
			AttendanceMonthlyDto dto) throws SQLException {
		// 해당 연월 문자열 생성 (예: "2026-08")
		// 基準日と期間の境界を確認し、照会・計算に使用できる日付形式へ変換する。
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
	 * 検索語と選択条件を組み合わせ、利用者が指定した対象だけを照会する。
	 */

	public int selectAttendanceDetailCount(Connection conn, AttendanceDetailSearchRequest req) throws SQLException {
		String sql = "SELECT COUNT(*) " + "FROM EMPLOYEE_ATTENDANCE ea "
				+ "JOIN EMPLOYEE e ON ea.EMPLOYEE_ID = e.EMPLOYEE_ID "
				+ "LEFT JOIN DEPARTMENT d ON e.DEPARTMENT_ID = d.DEPARTMENT_ID "
				+ "LEFT JOIN JOB_POSITION p ON e.JOB_POSITION_ID = p.JOB_POSITION_ID "
				+ "LEFT JOIN ATTENDANCE_ITEM ai ON ea.ATTENDANCE_ITEM_ID = ai.ATTENDANCE_ITEM_ID "
				+ "WHERE (? IS NULL OR ea.INPUT_DATE = ?) " + "AND (? IS NULL OR ea.END_DATE >= ?) "
				+ "AND (? IS NULL OR ea.START_DATE <= ?) " + "AND (? IS NULL OR e.DEPARTMENT_ID = ?) "
				+ "AND (? IS NULL OR e.EMP_NAME_KR LIKE '%' || ? || '%') "
				+ "AND (? IS NULL OR ai.ATTENDANCE_GROUP_ID = ?) " + "AND (? IS NULL OR ea.ATTENDANCE_ITEM_ID = ?) "
				+ "AND (? IS NULL OR ai.DEDUCT_LEAVE_ID = ?) " + "AND (? IS NULL OR ea.NOTE LIKE '%' || ? || '%') ";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			setDateOrNull(pstmt, 1, 2, req.getInputDate());
			setDateOrNull(pstmt, 3,4, req.getStartDate());
			setDateOrNull(pstmt, 5, 6, req.getEndDate());
			setIntOrNull(pstmt, 7, 8, req.getDepartmentId());

			pstmt.setString(9, req.getEmpNameKr());
			pstmt.setString(10, req.getEmpNameKr());

			setIntOrNull(pstmt, 11, 12, req.getAttendanceGroupId());

			setIntOrNull(pstmt, 13, 14, req.getAttendanceItemId());
			setIntOrNull(pstmt, 15, 16, req.getLeaveItemId());

			pstmt.setString(17, req.getNote());
			pstmt.setString(18, req.getNote());
			int result = 0;
			try(ResultSet rs = pstmt.executeQuery()){
				if (rs.next()) {
					result =  rs.getInt("COUNT(*)");
				}
			}
			return result;
		}
	}

	// 조회 조건에 맞는 상세정보목록 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로
	// 변환한다.
	// 検索条件に合う詳細情報一覧データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	
	public List<AttendanceDetailDto> selectDetailList(Connection conn, AttendanceDetailSearchRequest req, int firstRow, int endRow)
			throws SQLException {
		String sql = "select * from (select rownum as rnum, a.* from (SELECT ea.INPUT_DATE, e.EMP_TYPE, e.EMP_NAME_KR, "
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
				+ "ORDER BY ea.INPUT_DATE DESC) a where rownum <= ? ) where rnum > ?";
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
			
			pstmt.setInt(19, endRow);
			pstmt.setInt(20, firstRow);

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					list.add(convertAttendanceDetailDto(rs));
				}
			}
		}
		return list;
	}
		
	public List<AttendanceDetailDto> selectDetailList(Connection conn, AttendanceDetailSearchRequest req)
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

	// 조회 조건에 맞는 ByEmp식별번호And휴가항목식별번호 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로
	// 변환한다.
	// 検索条件に合うByEmp識別番号And休暇項目識別番号データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public List<AttendanceEmployeeRecordDto> selectByEmpIdAndLeaveItemId(Connection conn, int empId, int leaveItemId)
			throws SQLException {
		String sql = "SELECT A.INPUT_DATE, I.ATTEND_NAME, "
				+ "L.ITEM_NAME, A.START_DATE, A.END_DATE, A.ATTEND_VALUE, A.NOTE " + "FROM EMPLOYEE_ATTENDANCE A "
				+ "LEFT JOIN ATTENDANCE_ITEM I ON I.ATTENDANCE_ITEM_ID = A.ATTENDANCE_ITEM_ID "
				+ "LEFT JOIN LEAVE_ITEM L ON L.LEAVE_ITEM_ID = A.LEAVE_ITEM_ID "
				+ "WHERE A.EMPLOYEE_ID = ? AND A.LEAVE_ITEM_ID = ? " + "ORDER BY A.START_DATE ASC";
		List<AttendanceEmployeeRecordDto> list = new ArrayList<>();
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, empId);
			pstmt.setInt(2, leaveItemId);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					AttendanceEmployeeRecordDto dto = new AttendanceEmployeeRecordDto();
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

	// Java 날짜값을 PreparedStatement에서 사용할 SQL 날짜 또는 Timestamp로 변환한다.
	// SQL 실행에 필요한 값과 NULL을 안전하게 바인딩하고 JDBC 자원은 사용이 끝난 뒤 정리한다.
	// Javaの日付値をPreparedStatementで使用するSQL日付またはTimestampへ変換する。
	// SQL実行に必要な値とNULLを安全にバインドし、JDBCリソースは使用後に整理する。
	private java.sql.Date dateToSQLDate(java.util.Date date) {
		return date != null ? new java.sql.Date(date.getTime()) : null;
	}

	private void setDateOrNull(PreparedStatement pstmt, int idx1, int idx2, java.util.Date value) throws SQLException {
		if (value != null) {
			pstmt.setDate(idx1, new java.sql.Date(value.getTime()));
			pstmt.setDate(idx2, new java.sql.Date(value.getTime()));
		} else {
			pstmt.setNull(idx1, java.sql.Types.DATE);
			pstmt.setNull(idx2, java.sql.Types.DATE);
		}
	}

	/* EmpAttendRecord테이블에 있는 기록을 수정하는 메서드 EmpAttendRecordテーブルにある記録を修正するメソッド */
	// 識別された既存データへ変更値を反映し、未変更項目は従来の値を維持する。
	// 근태기록 수정
	// 식별조건에 해당하는 사원근태 데이터를 전달받은 값으로 수정한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 識別条件に該当する社員勤怠データを受け取った値で更新する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
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
	 * 選択された対象と関連範囲を確認して削除し、残りの一覧状態を再構成する。
	 */

	// 선택되거나 식별된 사원근태 데이터를 삭제하고 관련 상태를 정리한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 選択または識別された社員勤怠データを削除し、関連状態を整理する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
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

	// Java 날짜값을 PreparedStatement에서 사용할 SQL 날짜 또는 Timestamp로 변환한다.
	// SQL 실행에 필요한 값과 NULL을 안전하게 바인딩하고 JDBC 자원은 사용이 끝난 뒤 정리한다.
	// Javaの日付値をPreparedStatementで使用するSQL日付またはTimestampへ変換する。
	// SQL実行に必要な値とNULLを安全にバインドし、JDBCリソースは使用後に整理する。
	private Timestamp dateToTimestamp(java.util.Date date) {
		return new Timestamp(date.getTime());
	}

	/*
	 * ResultSet으로 EmpAttendRecord객체를 만들어 반환하는 메서드
	 * ResultSetでEmpAttendRecordオブジェクトを作って返すメソッド
	 */
	// 입력 데이터를 사원근태 처리에 필요한 형식으로 변환한다.
	// SQL 실행에 필요한 값과 NULL을 안전하게 바인딩하고 JDBC 자원은 사용이 끝난 뒤 정리한다.
	// 入力データを社員勤怠処理に必要な形式へ変換する。
	// SQL実行に必要な値とNULLを安全にバインドし、JDBCリソースは使用後に整理する。
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
	 * 照会結果を列ごとに読み取り、画面またはサービスで使用するオブジェクトへ変換する。
	 */
	// 입력 데이터를 근태상세정보전달 데이터 처리에 필요한 형식으로 변환한다.
	// SQL 실행에 필요한 값과 NULL을 안전하게 바인딩하고 JDBC 자원은 사용이 끝난 뒤 정리한다.
	// 入力データを勤怠詳細情報転送データ処理に必要な形式へ変換する。
	// SQL実行に必要な値とNULLを安全にバインドし、JDBCリソースは使用後に整理する。
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
	 * 業務条件に合うSQLを準備し、入力値をバインドしてデータベースで実行する。
	 */
	// 전달받은 정수Or빈 값 값을 사원근태 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った整数Or空値の値を社員勤怠オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	private void setIntOrNull(PreparedStatement pstmt, int idx1, int idx2, Integer value) throws SQLException {
		if (value != null) {
			pstmt.setInt(idx1, value);
			pstmt.setInt(idx2, value);
		} else {
			pstmt.setNull(idx1, java.sql.Types.NUMERIC);
			pstmt.setNull(idx2, java.sql.Types.NUMERIC);
		}
	}

	// 전달받은 정수Or빈 값 값을 사원근태 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った整数Or空値の値を社員勤怠オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	private void setIntOrNull(PreparedStatement pstmt, int idx, Integer value) throws SQLException {
		if (value != null) {
			pstmt.setInt(idx, value);
		} else {
			pstmt.setNull(idx, java.sql.Types.NUMERIC);
		}
	}

}
