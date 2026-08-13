package erp.employees.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.employees.dto.EmployeeListItem;
import erp.employees.dto.EmployeeSummary;
import erp.employees.model.Employee;
import erp.employees.service.EmployeeSearchCondition;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 사원 기본정보 데이터베이스 접근(DAO) 클래스
public class EmployeeDao {

	// 싱글톤 인스턴스 생성
	private static EmployeeDao employeeDao = new EmployeeDao();

	// 싱글톤 접근 메서드
	public static EmployeeDao getInstance() {
		return employeeDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	private EmployeeDao() {
	}

	// 사원 정보 등록 (INSERT)
	// 시퀀스를 활용하여 PK 발급 및 사원의 전체 정보 저장
	public void insert(Connection conn, Employee emp) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO EMPLOYEE ("
					+ "EMPLOYEE_ID, EMP_NO, EMP_TYPE, EMP_NAME_KR, EMP_NAME_EN, FOREIGN_YN, JOIN_DATE, DEPARTMENT_ID, JOB_POSITION_ID, "
					+ "JUMIN_NO, ZIP_CODE, ADDRESS, TEL_NO, MOBILE_NO, EMAIL, SNS_ADDRESS, MEMO, PHOTO_PATH, "
					+ "BASIC_PAY, INCOME_TYPE, INCOME_TAX_RATE, YOUTH_TAX_REDUCE_YN, YOUTH_TAX_RATE, NP_YN, HI_YN, "
					+ "LTCI_YN, EI_YN, HI_REDUCE_RATE, LTCI_REDUCE_RATE, DURUNURI_SEPARATE_YN, DURUNURI_NP_RATE, DURUNURI_EI_RATE, "
					+ "NP_MONTHLY_BASE, HI_MONTHLY_BASE, EI_MONTHLY_BASE, BANK_NAME, ACCOUNT_NO, DISCHARGE_TYPE, MIL_BRANCH, "
					+ "MIL_SERVICE_START, MIL_SERVICE_END, MIL_RANK, MIL_SPECIALTY, MIL_UNFINISHED_REASON, STATUS, RETIRE_TYPE, "
					+ "RETIRE_DATE, RETIRE_REASON, AFTER_RETIRE_CONTACT) "
					+ "VALUES (EMPLOYEE_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, emp.getEmpNo());
			pstmt.setString(2, emp.getEmpType());
			pstmt.setString(3, emp.getEmpNameKr());
			pstmt.setString(4, emp.getEmpNameEn());
			pstmt.setString(5, emp.getForeignYn());
			pstmt.setTimestamp(6, new Timestamp(emp.getJoinDate().getTime()));

			pstmt.setObject(7, emp.getDepartmentId(), Types.NUMERIC);
			pstmt.setObject(8, emp.getJobPositionId(), Types.NUMERIC);

			pstmt.setString(9, emp.getJuminNo());
			pstmt.setString(10, emp.getZipCode());
			pstmt.setString(11, emp.getAddress());
			pstmt.setString(12, emp.getTelNo());
			pstmt.setString(13, emp.getMobileNo());
			pstmt.setString(14, emp.getEmail());
			pstmt.setString(15, emp.getSnsAddress());
			pstmt.setString(16, emp.getMemo());
			pstmt.setString(17, emp.getPhotoPath());

			pstmt.setLong(18, emp.getBasicPay());
			pstmt.setString(19, emp.getIncomeType());
			pstmt.setInt(20, emp.getIncomeTaxRate());
			pstmt.setString(21, emp.getYouthTaxReduceYn());
			pstmt.setObject(22, emp.getYouthTaxRate(), Types.NUMERIC);

			pstmt.setString(23, emp.getNpYn());
			pstmt.setString(24, emp.getHiYn());
			pstmt.setString(25, emp.getLtciYn());
			pstmt.setString(26, emp.getEiYn());
			pstmt.setObject(27, emp.getHiReduceRate(), Types.NUMERIC);
			pstmt.setObject(28, emp.getLtciReduceRate(), Types.NUMERIC);
			pstmt.setString(29, emp.getDurunuriSeparateYn());
			pstmt.setObject(30, emp.getDurunuriNpRate(), Types.NUMERIC);
			pstmt.setObject(31, emp.getDurunuriEiRate(), Types.NUMERIC);

			pstmt.setObject(32, emp.getNpMonthlyBase(), Types.NUMERIC);
			pstmt.setObject(33, emp.getHiMonthlyBase(), Types.NUMERIC);
			pstmt.setObject(34, emp.getEiMonthlyBase(), Types.NUMERIC);
			pstmt.setString(35, emp.getBankName());
			pstmt.setString(36, emp.getAccountNo());

			pstmt.setString(37, emp.getDischargeType());
			pstmt.setString(38, emp.getMilBranch());

			if (emp.getMilServiceStart() == null)
				pstmt.setNull(39, Types.DATE);
			else
				pstmt.setTimestamp(39, new Timestamp(emp.getMilServiceStart().getTime()));

			if (emp.getMilServiceEnd() == null)
				pstmt.setNull(40, Types.DATE);
			else
				pstmt.setTimestamp(40, new Timestamp(emp.getMilServiceEnd().getTime()));

			pstmt.setString(41, emp.getMilRank());
			pstmt.setString(42, emp.getMilSpecialty());
			pstmt.setString(43, emp.getMilUnfinishedReason());
			pstmt.setString(44, emp.getStatus());
			pstmt.setString(45, emp.getRetireType());

			if (emp.getRetireDate() == null)
				pstmt.setNull(46, Types.DATE);
			else
				pstmt.setTimestamp(46, new Timestamp(emp.getRetireDate().getTime()));

			pstmt.setString(47, emp.getRetireReason());
			pstmt.setString(48, emp.getAfterRetireContact());

			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 사원 정보 단건 조회
	// 기본키(EMPLOYEE_ID)를 기준으로 단일 사원 데이터 반환
	public Employee selectById(Connection conn, int empId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM EMPLOYEE WHERE EMPLOYEE_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, empId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				return makeEmployeeFromResultSet(rs);
			}
			return null;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 사원 정보 전체 조회
	// 가장 최근에 등록된 사원부터 내림차순 정렬하여 목록 반환
	public List<Employee> selectAll(Connection conn) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM EMPLOYEE ORDER BY EMPLOYEE_ID DESC";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			List<Employee> result = new ArrayList<>();
			while (rs.next()) {
				result.add(makeEmployeeFromResultSet(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 사원현황 검색조건에 맞는 전체 행 수 조회 (페이징 계산용)
	public int countByCondition(Connection conn, EmployeeSearchCondition condition) throws SQLException {
		StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM EMPLOYEE E ");
		sql.append("LEFT JOIN DEPARTMENT D ON E.DEPARTMENT_ID = D.DEPARTMENT_ID ");
		sql.append("LEFT JOIN JOB_POSITION J ON E.JOB_POSITION_ID = J.JOB_POSITION_ID WHERE 1=1 ");
		List<Object> params = new ArrayList<>();
		appendConditions(sql, params, condition);
		try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
			setParameters(pstmt, params);
			try (ResultSet rs = pstmt.executeQuery()) { return rs.next() ? rs.getInt(1) : 0; }
		}
	}

	// EMPLOYEE를 중심으로 부서와 직위를 JOIN하여 화면용 사원 목록 조회
	public List<EmployeeListItem> selectListByCondition(Connection conn, EmployeeSearchCondition condition)
			throws SQLException {
		StringBuilder inner = new StringBuilder();
		inner.append("SELECT E.EMPLOYEE_ID, E.EMP_TYPE, TO_CHAR(E.JOIN_DATE, 'YYYY-MM-DD') JOIN_DATE, ");
		inner.append("E.EMP_NO, E.EMP_NAME_KR, E.EMP_NAME_EN, D.DEPARTMENT_NAME, J.JOB_POSITION_NAME, ");
		inner.append("E.JUMIN_NO, E.FOREIGN_YN, E.ADDRESS, E.TEL_NO, E.MOBILE_NO, E.EMAIL, E.SNS_ADDRESS, ");
		inner.append("TO_CHAR(E.RETIRE_DATE, 'YYYY-MM-DD') RETIRE_DATE, E.RETIRE_TYPE, E.RETIRE_REASON, E.AFTER_RETIRE_CONTACT, E.STATUS, E.BANK_NAME, E.ACCOUNT_NO, ");
		inner.append("CASE WHEN EXISTS (SELECT 1 FROM RETIREMENT_CALCULATION RC WHERE RC.EMPLOYEE_ID = E.EMPLOYEE_ID AND RC.CALC_TYPE = '중간정산') THEN 'Y' ELSE 'N' END INTERIM_SETTLEMENT, ");
		inner.append("CASE WHEN EXISTS (SELECT 1 FROM RETIREMENT_CALCULATION RC WHERE RC.EMPLOYEE_ID = E.EMPLOYEE_ID AND RC.CALC_TYPE = '퇴직정산') THEN 'Y' ELSE 'N' END RETIREMENT_SETTLEMENT ");
		inner.append("FROM EMPLOYEE E LEFT JOIN DEPARTMENT D ON E.DEPARTMENT_ID = D.DEPARTMENT_ID ");
		inner.append("LEFT JOIN JOB_POSITION J ON E.JOB_POSITION_ID = J.JOB_POSITION_ID WHERE 1=1 ");
		List<Object> params = new ArrayList<>();
		appendConditions(inner, params, condition);
		inner.append("ORDER BY E.EMPLOYEE_ID DESC");

		String sql = "SELECT * FROM (SELECT LIST_DATA.*, ROWNUM RN FROM (" + inner
				+ ") LIST_DATA WHERE ROWNUM <= ?) WHERE RN >= ?";
		int startRow = (condition.getPage() - 1) * condition.getPageSize() + 1;
		params.add(condition.getPage() * condition.getPageSize());
		params.add(startRow);

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			setParameters(pstmt, params);
			try (ResultSet rs = pstmt.executeQuery()) {
				List<EmployeeListItem> result = new ArrayList<>();
				while (rs.next()) result.add(makeEmployeeListItem(rs));
				return result;
			}
		}
	}

	// 전체 사원을 상태 및 고용형태별로 집계하여 상단 현황 카드에 표시
	public EmployeeSummary selectSummary(Connection conn) throws SQLException {
		String sql = "SELECT COUNT(*) TOTAL_COUNT, "
				+ "SUM(CASE WHEN STATUS = '재직' THEN 1 ELSE 0 END) WORKING_COUNT, "
				+ "SUM(CASE WHEN STATUS = '퇴직' THEN 1 ELSE 0 END) RETIRED_COUNT, "
				+ "SUM(CASE WHEN EMP_TYPE = '정규직' THEN 1 ELSE 0 END) REGULAR_COUNT, "
				+ "SUM(CASE WHEN EMP_TYPE = '계약직' THEN 1 ELSE 0 END) CONTRACT_COUNT, "
				+ "SUM(CASE WHEN EMP_TYPE = '임시직' THEN 1 ELSE 0 END) TEMPORARY_COUNT, "
				+ "SUM(CASE WHEN EMP_TYPE = '파견직' THEN 1 ELSE 0 END) DISPATCHED_COUNT, "
				+ "SUM(CASE WHEN EMP_TYPE = '위촉직' THEN 1 ELSE 0 END) COMMISSIONED_COUNT, "
				+ "SUM(CASE WHEN EMP_TYPE = '일용직' THEN 1 ELSE 0 END) DAILY_COUNT FROM EMPLOYEE";
		try (PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
			EmployeeSummary summary = new EmployeeSummary();
			if (rs.next()) {
				summary.setTotalCount(rs.getInt("TOTAL_COUNT"));
				summary.setWorkingCount(rs.getInt("WORKING_COUNT"));
				summary.setRetiredCount(rs.getInt("RETIRED_COUNT"));
				summary.setRegularCount(rs.getInt("REGULAR_COUNT"));
				summary.setContractCount(rs.getInt("CONTRACT_COUNT"));
				summary.setTemporaryCount(rs.getInt("TEMPORARY_COUNT"));
				summary.setDispatchedCount(rs.getInt("DISPATCHED_COUNT"));
				summary.setCommissionedCount(rs.getInt("COMMISSIONED_COUNT"));
				summary.setDailyCount(rs.getInt("DAILY_COUNT"));
			}
			return summary;
		}
	}

	private void appendConditions(StringBuilder sql, List<Object> params, EmployeeSearchCondition condition) {
		// 목록과 COUNT 쿼리에 동일한 WHERE 조건을 적용하기 위한 공통 메서드
		if (condition.getDepartmentId() != null) { sql.append("AND E.DEPARTMENT_ID = ? "); params.add(condition.getDepartmentId()); }
		if (condition.getPositionId() != null) { sql.append("AND E.JOB_POSITION_ID = ? "); params.add(condition.getPositionId()); }
		if (!condition.getEmploymentType().isEmpty()) {
			sql.append("AND E.EMP_TYPE = ? "); params.add(condition.getEmploymentType());
		}
		if ("WORK".equals(condition.getStatus())) { sql.append("AND E.STATUS = '재직' "); }
		else if ("RETIRED".equals(condition.getStatus())) { sql.append("AND E.STATUS = '퇴직' "); }

		if (!condition.getKeyword().isEmpty()) {
			String keyword = "%" + condition.getKeyword() + "%";
			if ("NAME".equals(condition.getSearchTarget())) { sql.append("AND E.EMP_NAME_KR LIKE ? "); params.add(keyword); }
			else if ("DEPARTMENT".equals(condition.getSearchTarget())) { sql.append("AND D.DEPARTMENT_NAME LIKE ? "); params.add(keyword); }
			else if ("POSITION".equals(condition.getSearchTarget())) { sql.append("AND J.JOB_POSITION_NAME LIKE ? "); params.add(keyword); }
			else if ("EMPLOYEE_NO".equals(condition.getSearchTarget())) { sql.append("AND E.EMP_NO LIKE ? "); params.add(keyword); }
			else {
				sql.append("AND (E.EMP_NAME_KR LIKE ? OR D.DEPARTMENT_NAME LIKE ? OR J.JOB_POSITION_NAME LIKE ? OR E.EMP_NO LIKE ?) ");
				params.add(keyword); params.add(keyword); params.add(keyword); params.add(keyword);
			}
		}
	}

	private void setParameters(PreparedStatement pstmt, List<Object> params) throws SQLException {
		for (int i = 0; i < params.size(); i++) pstmt.setObject(i + 1, params.get(i));
	}

	private EmployeeListItem makeEmployeeListItem(ResultSet rs) throws SQLException {
		// JOIN 조회 결과를 JSP 전용 DTO로 변환한다.
		EmployeeListItem item = new EmployeeListItem();
		item.setEmployeeId(rs.getInt("EMPLOYEE_ID"));
		item.setEmploymentType(rs.getString("EMP_TYPE"));
		item.setJoinDate(rs.getString("JOIN_DATE"));
		item.setEmployeeNo(rs.getString("EMP_NO"));
		item.setName(rs.getString("EMP_NAME_KR"));
		item.setEnglishName(rs.getString("EMP_NAME_EN"));
		item.setDepartmentName(rs.getString("DEPARTMENT_NAME"));
		item.setPositionName(rs.getString("JOB_POSITION_NAME"));
		item.setMaskedResidentNo(rs.getString("JUMIN_NO"));
		item.setNationalityType("Y".equals(rs.getString("FOREIGN_YN")) ? "외국인" : "내국인");
		item.setAddress(rs.getString("ADDRESS"));
		item.setPhone(rs.getString("TEL_NO"));
		item.setMobile(rs.getString("MOBILE_NO"));
		item.setEmail(rs.getString("EMAIL"));
		item.setSns(rs.getString("SNS_ADDRESS"));
		item.setRetirementDate(rs.getString("RETIRE_DATE"));
		item.setRetirementType(rs.getString("RETIRE_TYPE"));
		item.setRetirementReason(rs.getString("RETIRE_REASON"));
		item.setAfterContact(rs.getString("AFTER_RETIRE_CONTACT"));
		item.setInterimSettlement("Y".equals(rs.getString("INTERIM_SETTLEMENT")));
		item.setRetirementSettlement("Y".equals(rs.getString("RETIREMENT_SETTLEMENT")));
		item.setStatus(rs.getString("STATUS"));
		String bank = rs.getString("BANK_NAME");
		String account = rs.getString("ACCOUNT_NO");
		item.setBankAccount((bank == null ? "" : bank) + (account == null || account.isEmpty() ? "" : " " + account));
		return item;
	}

	// 사원 퇴직처리 시 상태와 퇴직 관련 컬럼만 갱신한다.
	public int updateRetirement(Connection conn, int employeeId, String retireType, java.util.Date retireDate,
			String retireReason, String afterContact) throws SQLException {
		String sql = "UPDATE EMPLOYEE SET STATUS = '퇴직', RETIRE_TYPE = ?, RETIRE_DATE = ?, RETIRE_REASON = ?, AFTER_RETIRE_CONTACT = ? WHERE EMPLOYEE_ID = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, retireType);
			pstmt.setTimestamp(2, new Timestamp(retireDate.getTime()));
			pstmt.setString(3, retireReason);
			pstmt.setString(4, afterContact);
			pstmt.setInt(5, employeeId);
			return pstmt.executeUpdate();
		}
	}

	// 퇴직처리를 취소하면 재직 상태로 복원하고 퇴직정보를 초기화한다.
	public int cancelRetirement(Connection conn, int employeeId) throws SQLException {
		String sql = "UPDATE EMPLOYEE SET STATUS = '재직', RETIRE_TYPE = NULL, RETIRE_DATE = NULL, RETIRE_REASON = NULL, AFTER_RETIRE_CONTACT = NULL WHERE EMPLOYEE_ID = ? AND STATUS = '퇴직'";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, employeeId);
			return pstmt.executeUpdate();
		}
	}

	// 사원 정보 수정
	// 기본키를 기준으로 전체 48개 항목의 데이터 갱신
	public int update(Connection conn, Employee emp) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "UPDATE EMPLOYEE SET "
					+ "EMP_NO = ?, EMP_TYPE = ?, EMP_NAME_KR = ?, EMP_NAME_EN = ?, FOREIGN_YN = ?, JOIN_DATE = ?, DEPARTMENT_ID = ?, JOB_POSITION_ID = ?, "
					+ "JUMIN_NO = ?, ZIP_CODE = ?, ADDRESS = ?, TEL_NO = ?, MOBILE_NO = ?, EMAIL = ?, SNS_ADDRESS = ?, MEMO = ?, PHOTO_PATH = ?, "
					+ "BASIC_PAY = ?, INCOME_TYPE = ?, INCOME_TAX_RATE = ?, YOUTH_TAX_REDUCE_YN = ?, YOUTH_TAX_RATE = ?, NP_YN = ?, HI_YN = ?, "
					+ "LTCI_YN = ?, EI_YN = ?, HI_REDUCE_RATE = ?, LTCI_REDUCE_RATE = ?, DURUNURI_SEPARATE_YN = ?, DURUNURI_NP_RATE = ?, DURUNURI_EI_RATE = ?, "
					+ "NP_MONTHLY_BASE = ?, HI_MONTHLY_BASE = ?, EI_MONTHLY_BASE = ?, BANK_NAME = ?, ACCOUNT_NO = ?, DISCHARGE_TYPE = ?, MIL_BRANCH = ?, "
					+ "MIL_SERVICE_START = ?, MIL_SERVICE_END = ?, MIL_RANK = ?, MIL_SPECIALTY = ?, MIL_UNFINISHED_REASON = ?, STATUS = ?, RETIRE_TYPE = ?, "
					+ "RETIRE_DATE = ?, RETIRE_REASON = ?, AFTER_RETIRE_CONTACT = ? " + "WHERE EMPLOYEE_ID = ?";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, emp.getEmpNo());
			pstmt.setString(2, emp.getEmpType());
			pstmt.setString(3, emp.getEmpNameKr());
			pstmt.setString(4, emp.getEmpNameEn());
			pstmt.setString(5, emp.getForeignYn());
			pstmt.setTimestamp(6, new Timestamp(emp.getJoinDate().getTime()));

			pstmt.setObject(7, emp.getDepartmentId(), Types.NUMERIC);
			pstmt.setObject(8, emp.getJobPositionId(), Types.NUMERIC);

			pstmt.setString(9, emp.getJuminNo());
			pstmt.setString(10, emp.getZipCode());
			pstmt.setString(11, emp.getAddress());
			pstmt.setString(12, emp.getTelNo());
			pstmt.setString(13, emp.getMobileNo());
			pstmt.setString(14, emp.getEmail());
			pstmt.setString(15, emp.getSnsAddress());
			pstmt.setString(16, emp.getMemo());
			pstmt.setString(17, emp.getPhotoPath());

			pstmt.setLong(18, emp.getBasicPay());
			pstmt.setString(19, emp.getIncomeType());
			pstmt.setInt(20, emp.getIncomeTaxRate());
			pstmt.setString(21, emp.getYouthTaxReduceYn());
			pstmt.setObject(22, emp.getYouthTaxRate(), Types.NUMERIC);

			pstmt.setString(23, emp.getNpYn());
			pstmt.setString(24, emp.getHiYn());
			pstmt.setString(25, emp.getLtciYn());
			pstmt.setString(26, emp.getEiYn());
			pstmt.setObject(27, emp.getHiReduceRate(), Types.NUMERIC);
			pstmt.setObject(28, emp.getLtciReduceRate(), Types.NUMERIC);
			pstmt.setString(29, emp.getDurunuriSeparateYn());
			pstmt.setObject(30, emp.getDurunuriNpRate(), Types.NUMERIC);
			pstmt.setObject(31, emp.getDurunuriEiRate(), Types.NUMERIC);

			pstmt.setObject(32, emp.getNpMonthlyBase(), Types.NUMERIC);
			pstmt.setObject(33, emp.getHiMonthlyBase(), Types.NUMERIC);
			pstmt.setObject(34, emp.getEiMonthlyBase(), Types.NUMERIC);
			pstmt.setString(35, emp.getBankName());
			pstmt.setString(36, emp.getAccountNo());

			pstmt.setString(37, emp.getDischargeType());
			pstmt.setString(38, emp.getMilBranch());

			if (emp.getMilServiceStart() == null)
				pstmt.setNull(39, Types.DATE);
			else
				pstmt.setTimestamp(39, new Timestamp(emp.getMilServiceStart().getTime()));

			if (emp.getMilServiceEnd() == null)
				pstmt.setNull(40, Types.DATE);
			else
				pstmt.setTimestamp(40, new Timestamp(emp.getMilServiceEnd().getTime()));

			pstmt.setString(41, emp.getMilRank());
			pstmt.setString(42, emp.getMilSpecialty());
			pstmt.setString(43, emp.getMilUnfinishedReason());
			pstmt.setString(44, emp.getStatus());
			pstmt.setString(45, emp.getRetireType());

			if (emp.getRetireDate() == null)
				pstmt.setNull(46, Types.DATE);
			else
				pstmt.setTimestamp(46, new Timestamp(emp.getRetireDate().getTime()));

			pstmt.setString(47, emp.getRetireReason());
			pstmt.setString(48, emp.getAfterRetireContact());

			pstmt.setInt(49, emp.getEmployeeId());

			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 사원 정보 삭제
	// 기본키를 기준으로 데이터 삭제
	public int delete(Connection conn, int empId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM EMPLOYEE WHERE EMPLOYEE_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, empId);
			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// ResultSet 데이터를 Employee 객체로 변환
	private Employee makeEmployeeFromResultSet(ResultSet rs) throws SQLException {
		Employee emp = new Employee();

		emp.setEmployeeId(rs.getInt("EMPLOYEE_ID"));
		emp.setEmpNo(rs.getString("EMP_NO"));
		emp.setEmpType(rs.getString("EMP_TYPE"));
		emp.setEmpNameKr(rs.getString("EMP_NAME_KR"));
		emp.setEmpNameEn(rs.getString("EMP_NAME_EN"));
		emp.setForeignYn(rs.getString("FOREIGN_YN"));

		Timestamp joinTs = rs.getTimestamp("JOIN_DATE");
		if (joinTs != null)
			emp.setJoinDate(new java.util.Date(joinTs.getTime()));

		int deptId = rs.getInt("DEPARTMENT_ID");
		emp.setDepartmentId(rs.wasNull() ? null : deptId);

		int posId = rs.getInt("JOB_POSITION_ID");
		emp.setJobPositionId(rs.wasNull() ? null : posId);

		emp.setJuminNo(rs.getString("JUMIN_NO"));
		emp.setZipCode(rs.getString("ZIP_CODE"));
		emp.setAddress(rs.getString("ADDRESS"));
		emp.setTelNo(rs.getString("TEL_NO"));
		emp.setMobileNo(rs.getString("MOBILE_NO"));
		emp.setEmail(rs.getString("EMAIL"));
		emp.setSnsAddress(rs.getString("SNS_ADDRESS"));
		emp.setMemo(rs.getString("MEMO"));
		emp.setPhotoPath(rs.getString("PHOTO_PATH"));

		emp.setBasicPay(rs.getLong("BASIC_PAY"));
		emp.setIncomeType(rs.getString("INCOME_TYPE"));
		emp.setIncomeTaxRate(rs.getInt("INCOME_TAX_RATE"));
		emp.setYouthTaxReduceYn(rs.getString("YOUTH_TAX_REDUCE_YN"));

		int youthTax = rs.getInt("YOUTH_TAX_RATE");
		emp.setYouthTaxRate(rs.wasNull() ? null : youthTax);

		emp.setNpYn(rs.getString("NP_YN"));
		emp.setHiYn(rs.getString("HI_YN"));
		emp.setLtciYn(rs.getString("LTCI_YN"));
		emp.setEiYn(rs.getString("EI_YN"));

		int hiReduce = rs.getInt("HI_REDUCE_RATE");
		emp.setHiReduceRate(rs.wasNull() ? null : hiReduce);

		int ltciReduce = rs.getInt("LTCI_REDUCE_RATE");
		emp.setLtciReduceRate(rs.wasNull() ? null : ltciReduce);

		emp.setDurunuriSeparateYn(rs.getString("DURUNURI_SEPARATE_YN"));

		int durunuriNp = rs.getInt("DURUNURI_NP_RATE");
		emp.setDurunuriNpRate(rs.wasNull() ? null : durunuriNp);

		int durunuriEi = rs.getInt("DURUNURI_EI_RATE");
		emp.setDurunuriEiRate(rs.wasNull() ? null : durunuriEi);

		long npBase = rs.getLong("NP_MONTHLY_BASE");
		emp.setNpMonthlyBase(rs.wasNull() ? null : npBase);

		long hiBase = rs.getLong("HI_MONTHLY_BASE");
		emp.setHiMonthlyBase(rs.wasNull() ? null : hiBase);

		long eiBase = rs.getLong("EI_MONTHLY_BASE");
		emp.setEiMonthlyBase(rs.wasNull() ? null : eiBase);

		emp.setBankName(rs.getString("BANK_NAME"));
		emp.setAccountNo(rs.getString("ACCOUNT_NO"));

		emp.setDischargeType(rs.getString("DISCHARGE_TYPE"));
		emp.setMilBranch(rs.getString("MIL_BRANCH"));

		Timestamp milStartTs = rs.getTimestamp("MIL_SERVICE_START");
		if (milStartTs != null)
			emp.setMilServiceStart(new java.util.Date(milStartTs.getTime()));

		Timestamp milEndTs = rs.getTimestamp("MIL_SERVICE_END");
		if (milEndTs != null)
			emp.setMilServiceEnd(new java.util.Date(milEndTs.getTime()));

		emp.setMilRank(rs.getString("MIL_RANK"));
		emp.setMilSpecialty(rs.getString("MIL_SPECIALTY"));
		emp.setMilUnfinishedReason(rs.getString("MIL_UNFINISHED_REASON"));
		emp.setStatus(rs.getString("STATUS"));
		emp.setRetireType(rs.getString("RETIRE_TYPE"));

		Timestamp retireTs = rs.getTimestamp("RETIRE_DATE");
		if (retireTs != null)
			emp.setRetireDate(new java.util.Date(retireTs.getTime()));

		emp.setRetireReason(rs.getString("RETIRE_REASON"));
		emp.setAfterRetireContact(rs.getString("AFTER_RETIRE_CONTACT"));

		return emp;
	}
}
