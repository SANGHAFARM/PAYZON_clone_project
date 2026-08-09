package erp.hr.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.hr.model.Employee;
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
	private EmployeeDao() {}

	// 사원 정보 등록 (INSERT)
	// 시퀀스를 활용하여 PK 발급 및 사원의 전체 정보 저장
	public void insert(Connection conn, Employee emp) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO EMPLOYEE ("
					+ "EMP_ID, EMP_NO, EMP_TYPE, EMP_NAME_KR, EMP_NAME_EN, FOREIGN_YN, JOIN_DATE, DEPT_ID, POS_ID, "
					+ "JUMIN_NO, ZIP_CODE, ADDRESS, TEL_NO, MOBILE_NO, EMAIL, SNS_ADDRESS, MEMO, PHOTO_PATH, "
					+ "BASIC_PAY, INCOME_TYPE, INCOME_TAX_RATE, YOUTH_TAX_REDUCE_YN, YOUTH_TAX_RATE, NP_YN, HI_YN, "
					+ "LTCI_YN, EI_YN, HI_REDUCE_RATE, LTCI_REDUCE_RATE, DURUNURI_SEPARATE_YN, DURUNURI_NP_RATE, DURUNURI_EI_RATE, "
					+ "NP_MONTHLY_BASE, HI_MONTHLY_BASE, EI_MONTHLY_BASE, BANK_NAME, ACCOUNT_NO, DISCHARGE_TYPE, MIL_BRANCH, "
					+ "MIL_SERVICE_START, MIL_SERVICE_END, MIL_RANK, MIL_SPECIALTY, MIL_UNFINISHED_REASON, STATUS, RETIRE_TYPE, "
					+ "RETIRE_DATE, RETIRE_REASON, AFTER_RETIRE_CONTACT) "
					+ "VALUES (SEQ_EMP_ID.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, emp.getEmpNo());
			pstmt.setString(2, emp.getEmpType());
			pstmt.setString(3, emp.getEmpNameKr());
			pstmt.setString(4, emp.getEmpNameEn());
			pstmt.setString(5, emp.getForeignYn());
			pstmt.setTimestamp(6, new Timestamp(emp.getJoinDate().getTime()));

			// 외래키(부서, 직위)가 0으로 넘어올 경우 데이터베이스 무결성을 위해 NULL 처리
			if (emp.getDeptId() == 0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7, emp.getDeptId());

			if (emp.getPosId() == 0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8, emp.getPosId());

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
			pstmt.setInt(22, emp.getYouthTaxRate());

			pstmt.setString(23, emp.getNpYn());
			pstmt.setString(24, emp.getHiYn());
			pstmt.setString(25, emp.getLtciYn());
			pstmt.setString(26, emp.getEiYn());
			pstmt.setInt(27, emp.getHiReduceRate());
			pstmt.setInt(28, emp.getLtciReduceRate());
			pstmt.setString(29, emp.getDurunuriSeparateYn());
			pstmt.setInt(30, emp.getDurunuriNpRate());
			pstmt.setInt(31, emp.getDurunuriEiRate());

			pstmt.setLong(32, emp.getNpMonthlyBase());
			pstmt.setLong(33, emp.getHiMonthlyBase());
			pstmt.setLong(34, emp.getEiMonthlyBase());
			pstmt.setString(35, emp.getBankName());
			pstmt.setString(36, emp.getAccountNo());

			pstmt.setString(37, emp.getDischargeType());
			pstmt.setString(38, emp.getMilBranch());

			// 병역 복무기간 등 선택적 날짜 필드의 NULL 방어 로직
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

			// 재직 상태일 때 퇴직일이 없는 경우를 위한 NULL 방어 로직
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
	// 기본키(EMP_ID)를 기준으로 단일 사원 데이터 반환
	public Employee selectById(Connection conn, int empId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM EMPLOYEE WHERE EMP_ID = ?";
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
			String sql = "SELECT * FROM EMPLOYEE ORDER BY EMP_ID DESC";
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

	// 사원 정보 수정
	// 기본키를 기준으로 전체 48개 항목의 데이터 갱신
	public int update(Connection conn, Employee emp) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "UPDATE EMPLOYEE SET "
					+ "EMP_NO = ?, EMP_TYPE = ?, EMP_NAME_KR = ?, EMP_NAME_EN = ?, FOREIGN_YN = ?, JOIN_DATE = ?, DEPT_ID = ?, POS_ID = ?, "
					+ "JUMIN_NO = ?, ZIP_CODE = ?, ADDRESS = ?, TEL_NO = ?, MOBILE_NO = ?, EMAIL = ?, SNS_ADDRESS = ?, MEMO = ?, PHOTO_PATH = ?, "
					+ "BASIC_PAY = ?, INCOME_TYPE = ?, INCOME_TAX_RATE = ?, YOUTH_TAX_REDUCE_YN = ?, YOUTH_TAX_RATE = ?, NP_YN = ?, HI_YN = ?, "
					+ "LTCI_YN = ?, EI_YN = ?, HI_REDUCE_RATE = ?, LTCI_REDUCE_RATE = ?, DURUNURI_SEPARATE_YN = ?, DURUNURI_NP_RATE = ?, DURUNURI_EI_RATE = ?, "
					+ "NP_MONTHLY_BASE = ?, HI_MONTHLY_BASE = ?, EI_MONTHLY_BASE = ?, BANK_NAME = ?, ACCOUNT_NO = ?, DISCHARGE_TYPE = ?, MIL_BRANCH = ?, "
					+ "MIL_SERVICE_START = ?, MIL_SERVICE_END = ?, MIL_RANK = ?, MIL_SPECIALTY = ?, MIL_UNFINISHED_REASON = ?, STATUS = ?, RETIRE_TYPE = ?, "
					+ "RETIRE_DATE = ?, RETIRE_REASON = ?, AFTER_RETIRE_CONTACT = ? " + "WHERE EMP_ID = ?";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, emp.getEmpNo());
			pstmt.setString(2, emp.getEmpType());
			pstmt.setString(3, emp.getEmpNameKr());
			pstmt.setString(4, emp.getEmpNameEn());
			pstmt.setString(5, emp.getForeignYn());
			pstmt.setTimestamp(6, new Timestamp(emp.getJoinDate().getTime()));

			if (emp.getDeptId() == 0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7, emp.getDeptId());

			if (emp.getPosId() == 0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8, emp.getPosId());

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
			pstmt.setInt(22, emp.getYouthTaxRate());

			pstmt.setString(23, emp.getNpYn());
			pstmt.setString(24, emp.getHiYn());
			pstmt.setString(25, emp.getLtciYn());
			pstmt.setString(26, emp.getEiYn());
			pstmt.setInt(27, emp.getHiReduceRate());
			pstmt.setInt(28, emp.getLtciReduceRate());
			pstmt.setString(29, emp.getDurunuriSeparateYn());
			pstmt.setInt(30, emp.getDurunuriNpRate());
			pstmt.setInt(31, emp.getDurunuriEiRate());

			pstmt.setLong(32, emp.getNpMonthlyBase());
			pstmt.setLong(33, emp.getHiMonthlyBase());
			pstmt.setLong(34, emp.getEiMonthlyBase());
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

			// 49번째 파라미터는 조건문의 EMP_ID
			pstmt.setInt(49, emp.getEmpId());

			return pstmt.executeUpdate(); // 수정된 행의 개수 반환
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 사원 정보 삭제
	// 기본키를 기준으로 데이터 삭제
	public int delete(Connection conn, int empId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM EMPLOYEE WHERE EMP_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, empId);
			return pstmt.executeUpdate(); // 삭제된 행의 개수 반환
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// ResultSet 데이터를 Employee 객체로 변환
	// 코드 중복 방지를 위한 공통 매핑 처리
	private Employee makeEmployeeFromResultSet(ResultSet rs) throws SQLException {
		Employee emp = new Employee();
		emp.setEmpId(rs.getInt("EMP_ID"));
		emp.setEmpNo(rs.getString("EMP_NO"));
		emp.setEmpType(rs.getString("EMP_TYPE"));
		emp.setEmpNameKr(rs.getString("EMP_NAME_KR"));
		emp.setEmpNameEn(rs.getString("EMP_NAME_EN"));
		emp.setForeignYn(rs.getString("FOREIGN_YN"));

		Timestamp joinTs = rs.getTimestamp("JOIN_DATE");
		if (joinTs != null)
			emp.setJoinDate(new java.util.Date(joinTs.getTime()));

		emp.setDeptId(rs.getInt("DEPT_ID"));
		emp.setPosId(rs.getInt("POS_ID"));
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
		emp.setYouthTaxRate(rs.getInt("YOUTH_TAX_RATE"));

		emp.setNpYn(rs.getString("NP_YN"));
		emp.setHiYn(rs.getString("HI_YN"));
		emp.setLtciYn(rs.getString("LTCI_YN"));
		emp.setEiYn(rs.getString("EI_YN"));
		emp.setHiReduceRate(rs.getInt("HI_REDUCE_RATE"));
		emp.setLtciReduceRate(rs.getInt("LTCI_REDUCE_RATE"));
		emp.setDurunuriSeparateYn(rs.getString("DURUNURI_SEPARATE_YN"));
		emp.setDurunuriNpRate(rs.getInt("DURUNURI_NP_RATE"));
		emp.setDurunuriEiRate(rs.getInt("DURUNURI_EI_RATE"));

		emp.setNpMonthlyBase(rs.getLong("NP_MONTHLY_BASE"));
		emp.setHiMonthlyBase(rs.getLong("HI_MONTHLY_BASE"));
		emp.setEiMonthlyBase(rs.getLong("EI_MONTHLY_BASE"));
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