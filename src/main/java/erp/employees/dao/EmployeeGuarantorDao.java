package erp.employees.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.employees.model.EmployeeGuarantor;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 신원보증인 내역 데이터베이스 접근(DAO) 클래스
public class EmployeeGuarantorDao {

	// 싱글톤 인스턴스 생성
	private static EmployeeGuarantorDao employeeGuarantorDao = new EmployeeGuarantorDao();

	// 싱글톤 접근 메서드
	public static EmployeeGuarantorDao getInstance() {
		return employeeGuarantorDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	private EmployeeGuarantorDao() {
	}

	// 보증인 내역 등록
	public void insert(Connection conn, EmployeeGuarantor guarantor) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO EMPLOYEE_GUARANTOR "
					+ "(EMPLOYEE_GUARANTOR_ID, EMPLOYEE_ID, GUARANTOR_NAME, RELATION, JUMIN_NO, GUARANTEE_AMT, GUARANTEE_DATE, EXPIRE_DATE, TEL_NO) "
					+ "VALUES (EMPLOYEE_GUARANTOR_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, guarantor.getEmployeeId());
			pstmt.setString(2, guarantor.getGuarantorName());
			pstmt.setString(3, guarantor.getRelation());
			pstmt.setString(4, guarantor.getJuminNo());
			pstmt.setObject(5, guarantor.getGuaranteeAmt(), Types.NUMERIC);

			if (guarantor.getGuaranteeDate() == null) {
				pstmt.setNull(6, Types.DATE);
			} else {
				pstmt.setTimestamp(6, new Timestamp(guarantor.getGuaranteeDate().getTime()));
			}

			if (guarantor.getExpireDate() == null) {
				pstmt.setNull(7, Types.DATE);
			} else {
				pstmt.setTimestamp(7, new Timestamp(guarantor.getExpireDate().getTime()));
			}

			pstmt.setString(8, guarantor.getTelNo());

			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 보증인 내역 단건 조회
	public EmployeeGuarantor selectById(Connection conn, int guarantorId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT EMPLOYEE_GUARANTOR_ID, EMPLOYEE_ID, GUARANTOR_NAME, RELATION, JUMIN_NO, GUARANTEE_AMT, GUARANTEE_DATE, EXPIRE_DATE, TEL_NO "
					+ "FROM EMPLOYEE_GUARANTOR WHERE EMPLOYEE_GUARANTOR_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, guarantorId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				return makeGuarantorFromResultSet(rs);
			}
			return null;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 특정 사원의 보증인 내역 목록 조회
	public List<EmployeeGuarantor> selectByEmpId(Connection conn, int empId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT EMPLOYEE_GUARANTOR_ID, EMPLOYEE_ID, GUARANTOR_NAME, RELATION, JUMIN_NO, GUARANTEE_AMT, GUARANTEE_DATE, EXPIRE_DATE, TEL_NO "
					+ "FROM EMPLOYEE_GUARANTOR WHERE EMPLOYEE_ID = ? ORDER BY GUARANTEE_DATE DESC NULLS LAST, EMPLOYEE_GUARANTOR_ID DESC";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, empId);
			rs = pstmt.executeQuery();

			List<EmployeeGuarantor> result = new ArrayList<>();
			while (rs.next()) {
				result.add(makeGuarantorFromResultSet(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 보증인 내역 수정
	public int update(Connection conn, EmployeeGuarantor guarantor) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "UPDATE EMPLOYEE_GUARANTOR SET "
					+ "EMPLOYEE_ID = ?, GUARANTOR_NAME = ?, RELATION = ?, JUMIN_NO = ?, GUARANTEE_AMT = ?, GUARANTEE_DATE = ?, EXPIRE_DATE = ?, TEL_NO = ? "
					+ "WHERE EMPLOYEE_GUARANTOR_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, guarantor.getEmployeeId());
			pstmt.setString(2, guarantor.getGuarantorName());
			pstmt.setString(3, guarantor.getRelation());
			pstmt.setString(4, guarantor.getJuminNo());
			pstmt.setObject(5, guarantor.getGuaranteeAmt(), Types.NUMERIC);

			if (guarantor.getGuaranteeDate() == null) {
				pstmt.setNull(6, Types.DATE);
			} else {
				pstmt.setTimestamp(6, new Timestamp(guarantor.getGuaranteeDate().getTime()));
			}

			if (guarantor.getExpireDate() == null) {
				pstmt.setNull(7, Types.DATE);
			} else {
				pstmt.setTimestamp(7, new Timestamp(guarantor.getExpireDate().getTime()));
			}

			pstmt.setString(8, guarantor.getTelNo());
			pstmt.setInt(9, guarantor.getEmployeeGuarantorId());

			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 보증인 내역 삭제
	public int delete(Connection conn, int guarantorId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM EMPLOYEE_GUARANTOR WHERE EMPLOYEE_GUARANTOR_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, guarantorId);
			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 사원번호(EMPLOYEE_ID)를 기준으로 해당 사원의 신원보증인 전체 삭제
	public void deleteByEmpId(Connection conn, int empId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM EMPLOYEE_GUARANTOR WHERE EMPLOYEE_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, empId);
			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// ResultSet 데이터를 EmployeeGuarantor 객체로 변환
	private EmployeeGuarantor makeGuarantorFromResultSet(ResultSet rs) throws SQLException {
		EmployeeGuarantor guarantor = new EmployeeGuarantor();

		guarantor.setEmployeeGuarantorId(rs.getInt("EMPLOYEE_GUARANTOR_ID"));
		guarantor.setEmployeeId(rs.getInt("EMPLOYEE_ID"));
		guarantor.setGuarantorName(rs.getString("GUARANTOR_NAME"));
		guarantor.setRelation(rs.getString("RELATION"));
		guarantor.setJuminNo(rs.getString("JUMIN_NO"));

		long amt = rs.getLong("GUARANTEE_AMT");
		guarantor.setGuaranteeAmt(rs.wasNull() ? null : amt);

		Timestamp guaTs = rs.getTimestamp("GUARANTEE_DATE");
		if (guaTs != null) {
			guarantor.setGuaranteeDate(new java.util.Date(guaTs.getTime()));
		}

		Timestamp expireTs = rs.getTimestamp("EXPIRE_DATE");
		if (expireTs != null) {
			guarantor.setExpireDate(new java.util.Date(expireTs.getTime()));
		}

		guarantor.setTelNo(rs.getString("TEL_NO"));

		return guarantor;
	}
}