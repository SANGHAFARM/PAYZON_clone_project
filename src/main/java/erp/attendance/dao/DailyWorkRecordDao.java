package erp.attendance.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import erp.attendance.dto.DailyWorkDetailDto;
import erp.attendance.dto.DailyWorkListDto;
import erp.attendance.dto.DailyWorkRecordDto;
import erp.attendance.model.DailyWorkRecord;
import erp.attendance.service.request.DailyWorkDetailRequest;
import erp.attendance.service.request.DailyWorkListRequest;
import erp.attendance.service.request.DailyWorkRecordRequest;

// 일용직근무기록 데이터를 데이터베이스에서 조회하고 저장·수정·삭제한다.
// 日雇い勤務記録データをデータベースから照会し、登録・更新・削除する。
public class DailyWorkRecordDao {
	private static DailyWorkRecordDao dailyWorkRecordDao = new DailyWorkRecordDao();

	// 여러 서비스에서 공유할 수 있도록 생성된 싱글톤 인스턴스를 반환한다.
	// 객체 생성을 한 곳으로 제한하여 모든 호출자가 동일한 DAO 인스턴스를 재사용하게 한다.
	// 複数のサービスで共有できるように生成されたシングルトンインスタンスを返す。
	// オブジェクト生成を一箇所へ制限し、すべての呼び出し側が同じDAOインスタンスを再利用できるようにする。
	public static DailyWorkRecordDao getInstance() {
		return dailyWorkRecordDao;
	}

	// 전달받은 값으로 일용직근무기록 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で日雇い勤務記録オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	private DailyWorkRecordDao() {
	}

	// 일용직 근무 기록 입력
	// 전달받은 일용직근무기록 데이터를 데이터베이스에 등록하고 처리 건수를 반환한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 受け取った日雇い勤務記録データをデータベースへ登録し、処理件数を返す。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public int insert(Connection conn, DailyWorkRecord dailyWorkRecord) throws SQLException {
		String sql = "INSERT INTO DAILY_WORK_RECORD VALUES (DAILY_WORK_RECORD_SEQ.NEXTVAL,?,?,?,?,?,?,?,? )";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, dailyWorkRecord.getEmployeeId());
			pstmt.setTimestamp(2, dateToTimestamp(dailyWorkRecord.getWorkDate()));
			setIntOrNull(pstmt, 3, dailyWorkRecord.getProjectId());
			pstmt.setLong(4, dailyWorkRecord.getDailyPay());
			pstmt.setDouble(5, dailyWorkRecord.getPayRate());
			pstmt.setLong(6, dailyWorkRecord.getIncomeTax());
			pstmt.setLong(7, dailyWorkRecord.getLocalIncomeTax());
			pstmt.setLong(8, dailyWorkRecord.getActualPay());
			return pstmt.executeUpdate();
		}
	}

	// (1)일용직 근무기록 조회(ID, 연도, 월)
	// 조회 조건에 맞는 By요청정보 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合うByリクエスト情報データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public List<DailyWorkRecordDto> selectByRequest(Connection conn, DailyWorkRecordRequest req) throws SQLException {
		String sql = "SELECT dwr.DAILY_WORK_RECORD_ID, e.EMP_NAME_KR, dwr.WORK_DATE, "
				+ "       dwr.PROJECT_ID, p.PROJECT_NAME, " + "       dwr.DAILY_PAY, dwr.PAY_RATE, "
				+ "       dwr.INCOME_TAX, dwr.LOCAL_INCOME_TAX, dwr.ACTUAL_PAY " + "FROM DAILY_WORK_RECORD dwr "
				+ "LEFT JOIN PROJECT p ON dwr.PROJECT_ID = p.PROJECT_ID "
				+ "JOIN EMPLOYEE e ON dwr.EMPLOYEE_ID = e.EMPLOYEE_ID " + "WHERE dwr.EMPLOYEE_ID = ? "
				+ "AND TO_CHAR(dwr.WORK_DATE, 'YYYY') = ? " + "AND (? IS NULL OR TO_CHAR(dwr.WORK_DATE, 'MM') = ?) "
				+ "ORDER BY dwr.WORK_DATE";

		List<DailyWorkRecordDto> list = new ArrayList<>();
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, req.getEmployeeId());
			pstmt.setString(2, String.valueOf(req.getYear()));

			if (req.getMonth() != null) {
				String mm = String.format("%02d", req.getMonth());
				pstmt.setString(3, mm);
				pstmt.setString(4, mm);
			} else {
				pstmt.setNull(3, java.sql.Types.VARCHAR);
				pstmt.setNull(4, java.sql.Types.VARCHAR);
			}

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					list.add(convertDailyWorkRecordDto(rs));
				}
			}
		}
		return list;
	}

	// (2) 연/월/부서/직급 목록 (월별조회에서 사용)
	// 조회 조건에 맞는 목록By요청정보 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合う一覧Byリクエスト情報データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public List<DailyWorkListDto> selectListByRequest(Connection conn, DailyWorkListRequest req) throws SQLException {
		String sql = "SELECT e.EMPLOYEE_ID, e.EMP_TYPE, e.EMP_NO, e.EMP_NAME_KR, d.department_name, COUNT(dwr.work_date) AS TOTAL_DAYS,"
				+ " SUM(dwr.income_tax) AS TOTAL_INCOME_TAX, SUM(dwr.local_income_tax) AS TOTAL_LOCAL_INCOME_TAX, SUM(dwr.actual_pay) AS TOTAL_ACTUAL_PAY "
				+ "FROM DAILY_WORK_RECORD dwr " + "LEFT JOIN EMPLOYEE e ON e.EMPLOYEE_ID = dwr.EMPLOYEE_ID "
				+ "LEFT JOIN DEPARTMENT d ON e.DEPARTMENT_ID = d.department_id "
				+ "WHERE dwr.work_date BETWEEN TO_DATE(?, 'YYYYMMDD') AND TO_DATE(?, 'YYYYMMDD') "
				+ "AND (? IS NULL OR d.department_id = ?) " + "AND (? IS NULL OR e.job_position_id = ?) "
				+ "GROUP BY e.EMPLOYEE_ID, e.EMP_TYPE, e.EMP_NO, e.EMP_NAME_KR, d.department_name ";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			int year = req.getYear();
			int month = req.getMonth();
			
			//해당 연도, 월의 시작일과 마지막일을 구하는 작업
			// 基準日と期間の境界を確認し、照会・計算に使用できる日付形式へ変換する。
			String start = String.format("%04d%02d01", year,month);
			java.time.YearMonth yearMonth = java.time.YearMonth.of(year,month );
			String end = yearMonth.atEndOfMonth().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
			
			pstmt.setString(1, start);
			pstmt.setString(2, end);
			setIntOrNull(pstmt, 3, 4, req.getDeptId());
			setIntOrNull(pstmt, 5, 6, req.getPosId());
			List<DailyWorkListDto> list= new ArrayList<>();
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					int empoyeeId = rs.getInt("EMPLOYEE_ID");
					DailyWorkListDto dailyWorkListDto = convertDailyWorkListDto(conn, rs, empoyeeId, start, end);
					list.add(dailyWorkListDto);
				}
			}
			return list;

		}

	}

	/*
	 * // (3) 근무일자/성명/부서/현장 상세 (상세조회에서 사용) public List<DailyWorkDetailDto>
	  * 基準日と期間の境界を確認し、照会・計算に使用できる日付形式へ変換する。
	 * selectDetailByRequest(Connection conn, DailyWorkDetailRequest req, int
	 * firstRow, int endRow) throws SQLException { String sql =
	 * "select * from (select rownum as rnum, a.* from (SELECT p.project_name, dwr.work_date, e.employee_id, e.emp_name_kr, d.department_name, dwr.daily_pay, "
	 * + "dwr.pay_rate, dwr.income_tax, dwr.local_income_tax, dwr.actual_pay " +
	 * "FROM daily_work_record dwr " +
	 * "LEFT JOIN employee e ON dwr.employee_id = e.employee_id " +
	 * "LEFT JOIN department d ON e.department_id = d.department_id " +
	 * "LEFT JOIN project p ON dwr.project_id = p.project_id " +
	 * "WHERE (? IS NULL OR dwr.work_date BETWEEN TO_DATE(?, 'YYYYMMDD') AND TO_DATE(?, 'YYYYMMDD')) "
	 * + "AND (? IS NULL OR e.emp_name_kr LIKE '%' || ? || '%') " +
	 * "AND (? IS NULL OR d.department_id = ?) " +
	 * "AND (? IS NULL OR p.project_id = ?) " +
	 * "ORDER BY dwr.work_date desc) a where rownum <= ?)where rnum >= ?"; try
	 * (PreparedStatement pstmt = conn.prepareStatement(sql)) { SimpleDateFormat sdf
	 * = new SimpleDateFormat("yyyyMMdd"); String startStr = (req.getStartDate() !=
	 * null) ? sdf.format(req.getStartDate()) : null; String endStr =
	 * (req.getEndDate() != null) ? sdf.format(req.getEndDate()) : null;
	 * 
	 * pstmt.setString(1, startStr); pstmt.setString(2, startStr);
	 * pstmt.setString(3, endStr);
	 * 
	 * pstmt.setString(4, req.getEmpNameKr()); pstmt.setString(5,
	 * req.getEmpNameKr());
	 * 
	 * setIntOrNull(pstmt, 6, 7, req.getDepartmentId()); setIntOrNull(pstmt, 8, 9,
	 * req.getProjectId());
	 * 
	 * pstmt.setInt(10, endRow); pstmt.setInt(11, firstRow);
	 * List<DailyWorkDetailDto> list = new ArrayList<>(); try (ResultSet rs =
	 * pstmt.executeQuery()) { while (rs.next()) {
	 * list.add(convertDailyWorkDetailDto(rs)); } } return list; } }
	 */
	
	// (3) 근무일자/성명/부서/현장 상세 (상세조회에서 사용 - 페이징 제거)
	// 조회 조건에 맞는 상세정보By요청정보 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合う詳細情報Byリクエスト情報データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public List<DailyWorkDetailDto> selectDetailByRequest(Connection conn, DailyWorkDetailRequest req) throws SQLException {
		String sql = "SELECT p.project_name AS PROJECT_NAME, dwr.work_date AS WORK_DATE, e.emp_no AS EMP_NO, e.emp_name_kr AS EMP_NAME_KR, "
	            + "d.department_name AS DEPARTMENT_NAME, dwr.daily_pay AS DAILY_PAY, dwr.pay_rate AS PAY_RATE, "
	            + "dwr.income_tax AS INCOME_TAX, dwr.local_income_tax AS LOCAL_INCOME_TAX, dwr.actual_pay AS ACTUAL_PAY " 
	            + "FROM daily_work_record dwr "
	            + "LEFT JOIN employee e ON dwr.employee_id = e.employee_id "
	            + "LEFT JOIN department d ON e.department_id = d.department_id "
	            + "LEFT JOIN project p ON dwr.project_id = p.project_id "
	            + "WHERE (? IS NULL OR dwr.work_date BETWEEN TO_DATE(?, 'YYYYMMDD') AND TO_DATE(?, 'YYYYMMDD')) "
	            + "AND (? IS NULL OR e.emp_name_kr LIKE '%' || ? || '%') " 
	            + "AND (? IS NULL OR d.department_id = ?) "
	            + "AND (? IS NULL OR p.project_id = ?) "
	            + "ORDER BY dwr.work_date DESC";

	    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	        String startStr = (req.getStartDate() != null) ? sdf.format(req.getStartDate()) : null;
	        String endStr = (req.getEndDate() != null) ? sdf.format(req.getEndDate()) : null;

	        pstmt.setString(1, startStr);
	        pstmt.setString(2, startStr);
	        pstmt.setString(3, endStr);

	        pstmt.setString(4, req.getEmpNameKr());
	        pstmt.setString(5, req.getEmpNameKr());

	        setIntOrNull(pstmt, 6, 7, req.getDepartmentId());
	        setIntOrNull(pstmt, 8, 9, req.getProjectId());

	        List<DailyWorkDetailDto> list = new ArrayList<>();
	        try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	                list.add(convertDailyWorkDetailDto(rs));
	            }
	        }
	        return list;
	    }
	}
	
	//일용직 근무 기록 수정 메서드
	// 식별조건에 해당하는 일용직근무기록 데이터를 전달받은 값으로 수정한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 識別条件に該当する日雇い勤務記録データを受け取った値で更新する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public int update(Connection conn, DailyWorkRecord dwr) throws SQLException {
	    String sql = "UPDATE DAILY_WORK_RECORD SET WORK_DATE=?, PROJECT_ID=?, DAILY_PAY=?, PAY_RATE=?, "
	               + "INCOME_TAX=?, LOCAL_INCOME_TAX=?, ACTUAL_PAY=? WHERE DAILY_WORK_RECORD_ID=?";
	    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setTimestamp(1, dateToTimestamp(dwr.getWorkDate()));
	        setIntOrNull(pstmt, 2, dwr.getProjectId());
	        pstmt.setLong(3, dwr.getDailyPay());
	        pstmt.setDouble(4, dwr.getPayRate());
	        pstmt.setLong(5, dwr.getIncomeTax());
	        pstmt.setLong(6, dwr.getLocalIncomeTax());
	        pstmt.setLong(7, dwr.getActualPay());
	        pstmt.setInt(8, dwr.getDailyWorkRecordId());
	        return pstmt.executeUpdate();
	    }
	}

	// 기록 삭제 메서드
	// 선택되거나 식별된 일용직근무기록 데이터를 삭제하고 관련 상태를 정리한다.
	// 전달받은 Connection 안에서 SQL 매개변수를 바인딩해 실행하며 commit과 rollback은 호출한 Service가 제어한다.
	// 選択または識別された日雇い勤務記録データを削除し、関連状態を整理する。
	// 受け取ったConnection内でSQLパラメーターをバインドして実行し、commitとrollbackは呼び出し元のServiceが制御する。
	public int delete(Connection conn, int dailyWorkRecordId) throws SQLException {
		String sql = "DELETE FROM DAILY_WORK_RECORD WHERE DAILY_WORK_RECORD_ID=?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, dailyWorkRecordId);
			return pstmt.executeUpdate();
		}
	}

	// Java 날짜값을 PreparedStatement에서 사용할 SQL 날짜 또는 Timestamp로 변환한다.
	// SQL 실행에 필요한 값과 NULL을 안전하게 바인딩하고 JDBC 자원은 사용이 끝난 뒤 정리한다.
	// Javaの日付値をPreparedStatementで使用するSQL日付またはTimestampへ変換する。
	// SQL実行に必要な値とNULLを安全にバインドし、JDBCリソースは使用後に整理する。
	private Timestamp dateToTimestamp(java.util.Date date) {
		return new Timestamp(date.getTime());
	}

	/*
	 * ResultSet으로 DailyWorkRecordDto를 만들어 반환하는 메서드 지급액(GROSS_PAY)은 DB에 저장하지 않고 여기서
	 * 계산
	  * 支給額・控除額・期間値を業務基準に従って計算し、合計と最終金額へ反映する。
	 */
	// 입력 데이터를 일용직근무기록전달 데이터 처리에 필요한 형식으로 변환한다.
	// SQL 실행에 필요한 값과 NULL을 안전하게 바인딩하고 JDBC 자원은 사용이 끝난 뒤 정리한다.
	// 入力データを日雇い勤務記録転送データ処理に必要な形式へ変換する。
	// SQL実行に必要な値とNULLを安全にバインドし、JDBCリソースは使用後に整理する。
	private DailyWorkRecordDto convertDailyWorkRecordDto(ResultSet rs) throws SQLException {
		DailyWorkRecordDto dto = new DailyWorkRecordDto();
		dto.setDailyWorkRecordId(rs.getInt("DAILY_WORK_RECORD_ID"));
		dto.setEmpNameKr(rs.getString("EMP_NAME_KR"));
		dto.setWorkDate(rs.getDate("WORK_DATE"));

		int projectId = rs.getInt("PROJECT_ID");
		dto.setProjectId(rs.wasNull() ? null : projectId);
		dto.setProjectName(rs.getString("PROJECT_NAME"));

		long dailyPay = rs.getLong("DAILY_PAY");
		double payRate = rs.getDouble("PAY_RATE");
		dto.setDailyPay(dailyPay);
		dto.setPayRate(payRate);
		dto.setGrossPay(Math.round(dailyPay * payRate)); // 지급액 = 일당 × 지급율

		dto.setIncomeTax(rs.getLong("INCOME_TAX"));
		dto.setLocalIncomeTax(rs.getLong("LOCAL_INCOME_TAX"));
		dto.setActualPay(rs.getLong("ACTUAL_PAY"));
		return dto;
	}

	// 입력 데이터를 일용직근무상세정보전달 데이터 처리에 필요한 형식으로 변환한다.
	// SQL 실행에 필요한 값과 NULL을 안전하게 바인딩하고 JDBC 자원은 사용이 끝난 뒤 정리한다.
	// 入力データを日雇い勤務詳細情報転送データ処理に必要な形式へ変換する。
	// SQL実行に必要な値とNULLを安全にバインドし、JDBCリソースは使用後に整理する。
	private DailyWorkDetailDto convertDailyWorkDetailDto(ResultSet rs) throws SQLException {
		DailyWorkDetailDto dto = new DailyWorkDetailDto();
		dto.setWorkDate(rs.getDate("WORK_DATE"));
		dto.setEmpNo(rs.getString("EMP_NO"));
		dto.setEmpNameKr(rs.getString("EMP_NAME_KR"));
		dto.setDepartmentName(rs.getString("DEPARTMENT_NAME"));
		dto.setProjectName(rs.getString("PROJECT_NAME"));
		dto.setDailyPay(rs.getLong("DAILY_PAY"));
		dto.setPayRate(rs.getDouble("PAY_RATE"));
		dto.setIncomeTax(rs.getLong("INCOME_TAX"));
		dto.setLocalIncomeTax(rs.getLong("LOCAL_INCOME_TAX"));
		dto.setActualPay(rs.getLong("ACTUAL_PAY"));
		return dto;
	}

	//rs로 일용직 근무 월별 기록 dto을 반환하는 메서드
	// 입력 데이터를 일용직근무목록전달 데이터 처리에 필요한 형식으로 변환한다.
	// SQL 실행에 필요한 값과 NULL을 안전하게 바인딩하고 JDBC 자원은 사용이 끝난 뒤 정리한다.
	// 入力データを日雇い勤務一覧転送データ処理に必要な形式へ変換する。
	// SQL実行に必要な値とNULLを安全にバインドし、JDBCリソースは使用後に整理する。
	private DailyWorkListDto convertDailyWorkListDto(Connection conn, ResultSet rs, int employeeId,String start, String end) throws SQLException {
		DailyWorkListDto dto = new DailyWorkListDto();
		dto.setEmpType(rs.getString("EMP_TYPE"));
		dto.setEmpNo(rs.getString("EMP_NO"));		
		dto.setEmpNameKr(rs.getString("EMP_NAME_KR"));
		dto.setDepartmentName(rs.getString("DEPARTMENT_NAME"));
		dto.setTotalDays(rs.getInt("TOTAL_DAYS"));
		dto.setTotalIncomeTax(rs.getLong("TOTAL_INCOME_TAX"));
		dto.setTotalLocalIncomeTax(rs.getLong("TOTAL_LOCAL_INCOME_TAX"));
		dto.setTotalActualPay(rs.getLong("TOTAL_ACTUAL_PAY"));
		String sql = "SELECT dwr.*, e.EMP_NAME_KR, p.PROJECT_NAME "
		           + "FROM DAILY_WORK_RECORD dwr "
		           + "LEFT JOIN EMPLOYEE e ON e.EMPLOYEE_ID = dwr.EMPLOYEE_ID "
		           + "LEFT JOIN PROJECT p ON p.PROJECT_ID = dwr.PROJECT_ID "
		           + "WHERE dwr.employee_id = ? AND dwr.work_date BETWEEN TO_DATE(?, 'YYYYMMDD') AND TO_DATE(?, 'YYYYMMDD')";
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setInt(1, employeeId);
			pstmt.setString(2, start);
			pstmt.setString(3, end);
			Map<Integer, DailyWorkRecordDto> workmap = new HashMap<>();
			try(ResultSet subRs = pstmt.executeQuery()){
				while (subRs.next()) {
					java.sql.Date sqlDate = subRs.getDate("WORK_DATE");
					if (sqlDate!=null) {
						int day = sqlDate.toLocalDate().getDayOfMonth();						
						DailyWorkRecordDto record = convertDailyWorkRecordDto(subRs);
						workmap.put(day, record);
					}
				}
			}
			dto.setWorkDayMap(workmap);
		}

		
		return dto;
	}

	// 전달받은 정수Or빈 값 값을 일용직근무기록 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った整数Or空値の値を日雇い勤務記録オブジェクトに保存する。
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

	// 전달받은 정수Or빈 값 값을 일용직근무기록 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った整数Or空値の値を日雇い勤務記録オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	private void setIntOrNull(PreparedStatement pstmt, int idx, Integer value) throws SQLException {
		if (value != null) {
			pstmt.setInt(idx, value);
		} else {
			pstmt.setNull(idx, java.sql.Types.NUMERIC);
		}
	}
}
