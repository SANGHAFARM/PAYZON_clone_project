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
import erp.attendance.service.DailyWorkDetailRequest;
import erp.attendance.service.DailyWorkListRequest;
import erp.attendance.service.DailyWorkRecordRequest;

public class DailyWorkRecordDao {
	private static DailyWorkRecordDao dailyWorkRecordDao = new DailyWorkRecordDao();

	public static DailyWorkRecordDao getInstance() {
		return dailyWorkRecordDao;
	}

	private DailyWorkRecordDao() {
	}

	// 일용직 근무 기록 입력
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
	public int delete(Connection conn, int dailyWorkRecordId) throws SQLException {
		String sql = "DELETE FROM DAILY_WORK_RECORD WHERE DAILY_WORK_RECORD_ID=?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, dailyWorkRecordId);
			return pstmt.executeUpdate();
		}
	}

	private Timestamp dateToTimestamp(java.util.Date date) {
		return new Timestamp(date.getTime());
	}

	/*
	 * ResultSet으로 DailyWorkRecordDto를 만들어 반환하는 메서드 지급액(GROSS_PAY)은 DB에 저장하지 않고 여기서
	 * 계산
	 */
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
