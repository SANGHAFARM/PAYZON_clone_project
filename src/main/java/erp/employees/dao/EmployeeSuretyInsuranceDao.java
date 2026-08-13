package erp.employees.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.employees.model.EmployeeSuretyInsurance;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 신원보증보험 내역 데이터베이스 접근(DAO) 클래스
public class EmployeeSuretyInsuranceDao {

	// 싱글톤 인스턴스 생성
	private static EmployeeSuretyInsuranceDao employeeSuretyInsuranceDao = new EmployeeSuretyInsuranceDao();

	// 싱글톤 접근 메서드
	public static EmployeeSuretyInsuranceDao getInstance() {
		return employeeSuretyInsuranceDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	private EmployeeSuretyInsuranceDao() {
	}

	// 보증보험 내역 등록
	public void insert(Connection conn, EmployeeSuretyInsurance insurance) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO EMPLOYEE_SURETY_INSURANCE "
					+ "(EMPLOYEE_SURETY_INSURANCE_ID, EMPLOYEE_ID, PROVIDER_NAME, INSURANCE_NO, INSURANCE_AMT, SIGNUP_DATE, EXPIRE_DATE, NOTE) "
					+ "VALUES (EMPLOYEE_SURETY_INSURANCE_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, insurance.getEmployeeId());
			pstmt.setString(2, insurance.getProviderName());
			pstmt.setString(3, insurance.getInsuranceNo());
			pstmt.setObject(4, insurance.getInsuranceAmt(), Types.NUMERIC);

			if (insurance.getSignupDate() == null) {
				pstmt.setNull(5, Types.DATE);
			} else {
				pstmt.setTimestamp(5, new Timestamp(insurance.getSignupDate().getTime()));
			}

			if (insurance.getExpireDate() == null) {
				pstmt.setNull(6, Types.DATE);
			} else {
				pstmt.setTimestamp(6, new Timestamp(insurance.getExpireDate().getTime()));
			}

			pstmt.setString(7, insurance.getNote());

			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 보증보험 내역 단건 조회
	public EmployeeSuretyInsurance selectById(Connection conn, int insuranceId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT EMPLOYEE_SURETY_INSURANCE_ID, EMPLOYEE_ID, PROVIDER_NAME, INSURANCE_NO, INSURANCE_AMT, SIGNUP_DATE, EXPIRE_DATE, NOTE "
					+ "FROM EMPLOYEE_SURETY_INSURANCE WHERE EMPLOYEE_SURETY_INSURANCE_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, insuranceId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				return makeInsuranceFromResultSet(rs);
			}
			return null;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 특정 사원의 보증보험 내역 목록 조회
	public List<EmployeeSuretyInsurance> selectByEmpId(Connection conn, int empId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT EMPLOYEE_SURETY_INSURANCE_ID, EMPLOYEE_ID, PROVIDER_NAME, INSURANCE_NO, INSURANCE_AMT, SIGNUP_DATE, EXPIRE_DATE, NOTE "
					+ "FROM EMPLOYEE_SURETY_INSURANCE WHERE EMPLOYEE_ID = ? ORDER BY SIGNUP_DATE DESC NULLS LAST, EMPLOYEE_SURETY_INSURANCE_ID DESC";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, empId);
			rs = pstmt.executeQuery();

			List<EmployeeSuretyInsurance> result = new ArrayList<>();
			while (rs.next()) {
				result.add(makeInsuranceFromResultSet(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 보증보험 내역 수정
	public int update(Connection conn, EmployeeSuretyInsurance insurance) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "UPDATE EMPLOYEE_SURETY_INSURANCE SET "
					+ "EMPLOYEE_ID = ?, PROVIDER_NAME = ?, INSURANCE_NO = ?, INSURANCE_AMT = ?, SIGNUP_DATE = ?, EXPIRE_DATE = ?, NOTE = ? "
					+ "WHERE EMPLOYEE_SURETY_INSURANCE_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, insurance.getEmployeeId());
			pstmt.setString(2, insurance.getProviderName());
			pstmt.setString(3, insurance.getInsuranceNo());
			pstmt.setObject(4, insurance.getInsuranceAmt(), Types.NUMERIC);

			if (insurance.getSignupDate() == null) {
				pstmt.setNull(5, Types.DATE);
			} else {
				pstmt.setTimestamp(5, new Timestamp(insurance.getSignupDate().getTime()));
			}

			if (insurance.getExpireDate() == null) {
				pstmt.setNull(6, Types.DATE);
			} else {
				pstmt.setTimestamp(6, new Timestamp(insurance.getExpireDate().getTime()));
			}

			pstmt.setString(7, insurance.getNote());
			pstmt.setInt(8, insurance.getEmployeeSuretyInsuranceId());

			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 보증보험 내역 삭제
	public int delete(Connection conn, int insuranceId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM EMPLOYEE_SURETY_INSURANCE WHERE EMPLOYEE_SURETY_INSURANCE_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, insuranceId);
			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}
	
	// 사원번호(EMPLOYEE_ID)를 기준으로 해당 사원의 보증보험 전체 삭제
	public void deleteByEmpId(Connection conn, int empId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM EMPLOYEE_SURETY_INSURANCE WHERE EMPLOYEE_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, empId);
			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// ResultSet 데이터를 EmployeeSuretyInsurance 객체로 변환
	private EmployeeSuretyInsurance makeInsuranceFromResultSet(ResultSet rs) throws SQLException {
		EmployeeSuretyInsurance insurance = new EmployeeSuretyInsurance();

		insurance.setEmployeeSuretyInsuranceId(rs.getInt("EMPLOYEE_SURETY_INSURANCE_ID"));
		insurance.setEmployeeId(rs.getInt("EMPLOYEE_ID"));
		insurance.setProviderName(rs.getString("PROVIDER_NAME"));
		insurance.setInsuranceNo(rs.getString("INSURANCE_NO"));

		long amt = rs.getLong("INSURANCE_AMT");
		insurance.setInsuranceAmt(rs.wasNull() ? null : amt);

		Timestamp signTs = rs.getTimestamp("SIGNUP_DATE");
		if (signTs != null) {
			insurance.setSignupDate(new java.util.Date(signTs.getTime()));
		}

		Timestamp expireTs = rs.getTimestamp("EXPIRE_DATE");
		if (expireTs != null) {
			insurance.setExpireDate(new java.util.Date(expireTs.getTime()));
		}

		insurance.setNote(rs.getString("NOTE"));

		return insurance;
	}
}