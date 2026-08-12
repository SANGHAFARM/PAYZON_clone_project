package erp.employees.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.employees.model.EmployeeRewardDiscipline;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 상벌 이력 데이터베이스 접근(DAO) 클래스
public class EmployeeRewardDisciplineDao {

	// 싱글톤 인스턴스 생성
	private static EmployeeRewardDisciplineDao employeeRewardDisciplineDao = new EmployeeRewardDisciplineDao();

	// 싱글톤 접근 메서드
	public static EmployeeRewardDisciplineDao getInstance() {
		return employeeRewardDisciplineDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	private EmployeeRewardDisciplineDao() {
	}

	// 상벌 내역 등록
	public void insert(Connection conn, EmployeeRewardDiscipline rd) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO EMPLOYEE_REWARD_DISCIPLINE "
					+ "(EMPLOYEE_REWARD_DISCIPLINE_ID, EMPLOYEE_ID, RP_TYPE, RP_NAME, RP_AUTHORITY, RP_DATE, RP_CONTENT, NOTE) "
					+ "VALUES (EMPLOYEE_REWARD_DISCIPLINE_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, rd.getEmployeeId());
			pstmt.setString(2, rd.getRpType());
			pstmt.setString(3, rd.getRpName());
			pstmt.setString(4, rd.getRpAuthority());

			if (rd.getRpDate() == null) {
				pstmt.setNull(5, Types.DATE);
			} else {
				pstmt.setTimestamp(5, new Timestamp(rd.getRpDate().getTime()));
			}

			pstmt.setString(6, rd.getRpContent());
			pstmt.setString(7, rd.getNote());

			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 상벌 내역 단건 조회
	public EmployeeRewardDiscipline selectById(Connection conn, int rdId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT EMPLOYEE_REWARD_DISCIPLINE_ID, EMPLOYEE_ID, RP_TYPE, RP_NAME, RP_AUTHORITY, RP_DATE, RP_CONTENT, NOTE "
					+ "FROM EMPLOYEE_REWARD_DISCIPLINE WHERE EMPLOYEE_REWARD_DISCIPLINE_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, rdId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				return makeRewardDisciplineFromResultSet(rs);
			}
			return null;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 특정 사원의 상벌 내역 목록 조회
	public List<EmployeeRewardDiscipline> selectByEmpId(Connection conn, int empId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT EMPLOYEE_REWARD_DISCIPLINE_ID, EMPLOYEE_ID, RP_TYPE, RP_NAME, RP_AUTHORITY, RP_DATE, RP_CONTENT, NOTE "
					+ "FROM EMPLOYEE_REWARD_DISCIPLINE WHERE EMPLOYEE_ID = ? ORDER BY RP_DATE DESC NULLS LAST, EMPLOYEE_REWARD_DISCIPLINE_ID DESC";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, empId);
			rs = pstmt.executeQuery();

			List<EmployeeRewardDiscipline> result = new ArrayList<>();
			while (rs.next()) {
				result.add(makeRewardDisciplineFromResultSet(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 상벌 내역 수정
	public int update(Connection conn, EmployeeRewardDiscipline rd) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "UPDATE EMPLOYEE_REWARD_DISCIPLINE SET "
					+ "EMPLOYEE_ID = ?, RP_TYPE = ?, RP_NAME = ?, RP_AUTHORITY = ?, RP_DATE = ?, RP_CONTENT = ?, NOTE = ? "
					+ "WHERE EMPLOYEE_REWARD_DISCIPLINE_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, rd.getEmployeeId());
			pstmt.setString(2, rd.getRpType());
			pstmt.setString(3, rd.getRpName());
			pstmt.setString(4, rd.getRpAuthority());

			if (rd.getRpDate() == null) {
				pstmt.setNull(5, Types.DATE);
			} else {
				pstmt.setTimestamp(5, new Timestamp(rd.getRpDate().getTime()));
			}

			pstmt.setString(6, rd.getRpContent());
			pstmt.setString(7, rd.getNote());
			pstmt.setInt(8, rd.getEmployeeRewardDisciplineId());

			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 상벌 내역 삭제
	public int delete(Connection conn, int rdId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM EMPLOYEE_REWARD_DISCIPLINE WHERE EMPLOYEE_REWARD_DISCIPLINE_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, rdId);
			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// ResultSet 데이터를 EmployeeRewardDiscipline 객체로 변환
	private EmployeeRewardDiscipline makeRewardDisciplineFromResultSet(ResultSet rs) throws SQLException {
		EmployeeRewardDiscipline rd = new EmployeeRewardDiscipline();

		rd.setEmployeeRewardDisciplineId(rs.getInt("EMPLOYEE_REWARD_DISCIPLINE_ID"));
		rd.setEmployeeId(rs.getInt("EMPLOYEE_ID"));
		rd.setRpType(rs.getString("RP_TYPE"));
		rd.setRpName(rs.getString("RP_NAME"));
		rd.setRpAuthority(rs.getString("RP_AUTHORITY"));

		Timestamp rpTs = rs.getTimestamp("RP_DATE");
		if (rpTs != null) {
			rd.setRpDate(new java.util.Date(rpTs.getTime()));
		}

		rd.setRpContent(rs.getString("RP_CONTENT"));
		rd.setNote(rs.getString("NOTE"));

		return rd;
	}
}