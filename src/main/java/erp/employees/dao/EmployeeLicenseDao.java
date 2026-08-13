package erp.employees.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.employees.model.EmployeeLicense;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 자격/면허 이력 데이터베이스 접근(DAO) 클래스
public class EmployeeLicenseDao {

	// 싱글톤 인스턴스 생성
	private static EmployeeLicenseDao employeeLicenseDao = new EmployeeLicenseDao();

	// 싱글톤 접근 메서드
	public static EmployeeLicenseDao getInstance() {
		return employeeLicenseDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	private EmployeeLicenseDao() {
	}

	// 자격/면허 내역 등록
	public void insert(Connection conn, EmployeeLicense license) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO EMPLOYEE_LICENSE "
					+ "(EMPLOYEE_LICENSE_ID, EMPLOYEE_ID, LIC_NAME, ACQ_DATE, ISSUER, LICENSE_NO, NOTE) "
					+ "VALUES (EMPLOYEE_LICENSE_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, license.getEmployeeId());
			pstmt.setString(2, license.getLicName());

			if (license.getAcqDate() == null) {
				pstmt.setNull(3, Types.DATE);
			} else {
				pstmt.setTimestamp(3, new Timestamp(license.getAcqDate().getTime()));
			}

			pstmt.setString(4, license.getIssuer());
			pstmt.setString(5, license.getLicenseNo());
			pstmt.setString(6, license.getNote());

			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 자격/면허 내역 단건 조회
	public EmployeeLicense selectById(Connection conn, int licenseId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT EMPLOYEE_LICENSE_ID, EMPLOYEE_ID, LIC_NAME, ACQ_DATE, ISSUER, LICENSE_NO, NOTE "
					+ "FROM EMPLOYEE_LICENSE WHERE EMPLOYEE_LICENSE_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, licenseId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				return makeLicenseFromResultSet(rs);
			}
			return null;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 특정 사원의 자격/면허 내역 목록 조회
	public List<EmployeeLicense> selectByEmpId(Connection conn, int empId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT EMPLOYEE_LICENSE_ID, EMPLOYEE_ID, LIC_NAME, ACQ_DATE, ISSUER, LICENSE_NO, NOTE "
					+ "FROM EMPLOYEE_LICENSE WHERE EMPLOYEE_ID = ? ORDER BY ACQ_DATE DESC NULLS LAST, EMPLOYEE_LICENSE_ID DESC";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, empId);
			rs = pstmt.executeQuery();

			List<EmployeeLicense> result = new ArrayList<>();
			while (rs.next()) {
				result.add(makeLicenseFromResultSet(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 자격/면허 내역 수정
	public int update(Connection conn, EmployeeLicense license) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "UPDATE EMPLOYEE_LICENSE SET "
					+ "EMPLOYEE_ID = ?, LIC_NAME = ?, ACQ_DATE = ?, ISSUER = ?, LICENSE_NO = ?, NOTE = ? "
					+ "WHERE EMPLOYEE_LICENSE_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, license.getEmployeeId());
			pstmt.setString(2, license.getLicName());

			if (license.getAcqDate() == null) {
				pstmt.setNull(3, Types.DATE);
			} else {
				pstmt.setTimestamp(3, new Timestamp(license.getAcqDate().getTime()));
			}

			pstmt.setString(4, license.getIssuer());
			pstmt.setString(5, license.getLicenseNo());
			pstmt.setString(6, license.getNote());
			pstmt.setInt(7, license.getEmployeeLicenseId());

			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 자격/면허 내역 삭제
	public int delete(Connection conn, int licenseId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM EMPLOYEE_LICENSE WHERE EMPLOYEE_LICENSE_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, licenseId);
			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}
	
	// 사원번호(EMPLOYEE_ID)를 기준으로 해당 사원의 자격/면허 전체 삭제
	public void deleteByEmpId(Connection conn, int empId) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM EMPLOYEE_LICENSE WHERE EMPLOYEE_ID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, empId);
            pstmt.executeUpdate();
        } finally {
            JdbcUtil.close(pstmt);
        }
    }

	// ResultSet 데이터를 EmployeeLicense 객체로 변환
	private EmployeeLicense makeLicenseFromResultSet(ResultSet rs) throws SQLException {
		EmployeeLicense license = new EmployeeLicense();

		license.setEmployeeLicenseId(rs.getInt("EMPLOYEE_LICENSE_ID"));
		license.setEmployeeId(rs.getInt("EMPLOYEE_ID"));
		license.setLicName(rs.getString("LIC_NAME"));

		Timestamp acqTs = rs.getTimestamp("ACQ_DATE");
		if (acqTs != null) {
			license.setAcqDate(new java.util.Date(acqTs.getTime()));
		}

		license.setIssuer(rs.getString("ISSUER"));
		license.setLicenseNo(rs.getString("LICENSE_NO"));
		license.setNote(rs.getString("NOTE"));

		return license;
	}
}