package erp.employees.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.employees.model.EmployeeCareer;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 사원 경력 내역 데이터베이스 접근(DAO) 클래스
public class EmployeeCareerDao {

	// 싱글톤 인스턴스 생성
	private static EmployeeCareerDao employeeCareerDao = new EmployeeCareerDao();

	// 싱글톤 접근 메서드
	public static EmployeeCareerDao getInstance() {
		return employeeCareerDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	private EmployeeCareerDao() {
	}

	// 경력 내역 등록
	// 시퀀스를 사용하여 기본키 발급 및 데이터 저장
	public void insert(Connection conn, EmployeeCareer career) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO EMPLOYEE_CAREER "
					+ "(EMPLOYEE_CAREER_ID, EMPLOYEE_ID, COMPANY_NAME, JOIN_DATE, QUIT_DATE, FINAL_POSITION, DUTY, QUIT_REASON) "
					+ "VALUES (EMPLOYEE_CAREER_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, career.getEmployeeId());
			pstmt.setString(2, career.getCompanyName());

			if (career.getJoinDate() == null) {
				pstmt.setNull(3, Types.DATE);
			} else {
				pstmt.setTimestamp(3, new Timestamp(career.getJoinDate().getTime()));
			}

			if (career.getQuitDate() == null) {
				pstmt.setNull(4, Types.DATE);
			} else {
				pstmt.setTimestamp(4, new Timestamp(career.getQuitDate().getTime()));
			}

			pstmt.setString(5, career.getFinalPosition());
			pstmt.setString(6, career.getDuty());
			pstmt.setString(7, career.getQuitReason());

			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 경력 내역 단건 조회
	// 기본키(EMPLOYEE_CAREER_ID)를 기준으로 1건의 데이터 조회
	public EmployeeCareer selectById(Connection conn, int carId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT EMPLOYEE_CAREER_ID, EMPLOYEE_ID, COMPANY_NAME, JOIN_DATE, QUIT_DATE, FINAL_POSITION, DUTY, QUIT_REASON "
					+ "FROM EMPLOYEE_CAREER WHERE EMPLOYEE_CAREER_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, carId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				return makeEmpCareerFromResultSet(rs);
			}
			return null;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 특정 사원의 경력 내역 목록 조회
	public List<EmployeeCareer> selectByEmpId(Connection conn, int empId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT EMPLOYEE_CAREER_ID, EMPLOYEE_ID, COMPANY_NAME, JOIN_DATE, QUIT_DATE, FINAL_POSITION, DUTY, QUIT_REASON "
					+ "FROM EMPLOYEE_CAREER WHERE EMPLOYEE_ID = ? ORDER BY JOIN_DATE DESC, EMPLOYEE_CAREER_ID DESC";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, empId);
			rs = pstmt.executeQuery();

			List<EmployeeCareer> result = new ArrayList<>();
			while (rs.next()) {
				result.add(makeEmpCareerFromResultSet(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 경력 내역 수정
	public int update(Connection conn, EmployeeCareer career) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "UPDATE EMPLOYEE_CAREER SET "
					+ "EMPLOYEE_ID = ?, COMPANY_NAME = ?, JOIN_DATE = ?, QUIT_DATE = ?, FINAL_POSITION = ?, DUTY = ?, QUIT_REASON = ? "
					+ "WHERE EMPLOYEE_CAREER_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, career.getEmployeeId());
			pstmt.setString(2, career.getCompanyName());

			if (career.getJoinDate() == null) {
				pstmt.setNull(3, Types.DATE);
			} else {
				pstmt.setTimestamp(3, new Timestamp(career.getJoinDate().getTime()));
			}

			if (career.getQuitDate() == null) {
				pstmt.setNull(4, Types.DATE);
			} else {
				pstmt.setTimestamp(4, new Timestamp(career.getQuitDate().getTime()));
			}

			pstmt.setString(5, career.getFinalPosition());
			pstmt.setString(6, career.getDuty());
			pstmt.setString(7, career.getQuitReason());
			pstmt.setInt(8, career.getEmployeeCareerId());

			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 경력 내역 삭제
	public int delete(Connection conn, int carId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM EMPLOYEE_CAREER WHERE EMPLOYEE_CAREER_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, carId);
			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 사원번호(EMPLOYEE_ID)를 기준으로 해당 사원의 경력 전체 삭제
	public void deleteByEmpId(Connection conn, int empId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM EMPLOYEE_CAREER WHERE EMPLOYEE_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, empId);
			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// ResultSet 데이터를 EmployeeCareer 객체로 변환
	private EmployeeCareer makeEmpCareerFromResultSet(ResultSet rs) throws SQLException {
		EmployeeCareer career = new EmployeeCareer();

		career.setEmployeeCareerId(rs.getInt("EMPLOYEE_CAREER_ID"));
		career.setEmployeeId(rs.getInt("EMPLOYEE_ID"));
		career.setCompanyName(rs.getString("COMPANY_NAME"));

		Timestamp joinTs = rs.getTimestamp("JOIN_DATE");
		if (joinTs != null) {
			career.setJoinDate(new java.util.Date(joinTs.getTime()));
		}

		Timestamp quitTs = rs.getTimestamp("QUIT_DATE");
		if (quitTs != null) {
			career.setQuitDate(new java.util.Date(quitTs.getTime()));
		}

		career.setFinalPosition(rs.getString("FINAL_POSITION"));
		career.setDuty(rs.getString("DUTY"));
		career.setQuitReason(rs.getString("QUIT_REASON"));

		return career;
	}
}