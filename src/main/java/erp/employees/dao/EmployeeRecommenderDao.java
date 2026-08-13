package erp.employees.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import erp.employees.model.EmployeeRecommender;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 추천인 내역 데이터베이스 접근(DAO) 클래스
public class EmployeeRecommenderDao {

	// 싱글톤 인스턴스 생성
	private static EmployeeRecommenderDao employeeRecommenderDao = new EmployeeRecommenderDao();

	// 싱글톤 접근 메서드
	public static EmployeeRecommenderDao getInstance() {
		return employeeRecommenderDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	private EmployeeRecommenderDao() {
	}

	// 추천인 내역 등록
	public void insert(Connection conn, EmployeeRecommender recommender) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO EMPLOYEE_RECOMMENDER "
					+ "(EMPLOYEE_RECOMMENDER_ID, EMPLOYEE_ID, RECOMMENDER_NAME, RELATION, COMPANY_NAME, POSITION_NAME, TEL_NO) "
					+ "VALUES (EMPLOYEE_RECOMMENDER_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, recommender.getEmployeeId());
			pstmt.setString(2, recommender.getRecommenderName());
			pstmt.setString(3, recommender.getRelation());
			pstmt.setString(4, recommender.getCompanyName());
			pstmt.setString(5, recommender.getPositionName());
			pstmt.setString(6, recommender.getTelNo());

			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 추천인 내역 단건 조회
	public EmployeeRecommender selectById(Connection conn, int recId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT EMPLOYEE_RECOMMENDER_ID, EMPLOYEE_ID, RECOMMENDER_NAME, RELATION, COMPANY_NAME, POSITION_NAME, TEL_NO "
					+ "FROM EMPLOYEE_RECOMMENDER WHERE EMPLOYEE_RECOMMENDER_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, recId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				return makeRecommenderFromResultSet(rs);
			}
			return null;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 특정 사원의 추천인 내역 목록 조회
	public List<EmployeeRecommender> selectByEmpId(Connection conn, int empId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT EMPLOYEE_RECOMMENDER_ID, EMPLOYEE_ID, RECOMMENDER_NAME, RELATION, COMPANY_NAME, POSITION_NAME, TEL_NO "
					+ "FROM EMPLOYEE_RECOMMENDER WHERE EMPLOYEE_ID = ? ORDER BY EMPLOYEE_RECOMMENDER_ID ASC";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, empId);
			rs = pstmt.executeQuery();

			List<EmployeeRecommender> result = new ArrayList<>();
			while (rs.next()) {
				result.add(makeRecommenderFromResultSet(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 추천인 내역 수정
	public int update(Connection conn, EmployeeRecommender recommender) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "UPDATE EMPLOYEE_RECOMMENDER SET "
					+ "EMPLOYEE_ID = ?, RECOMMENDER_NAME = ?, RELATION = ?, COMPANY_NAME = ?, POSITION_NAME = ?, TEL_NO = ? "
					+ "WHERE EMPLOYEE_RECOMMENDER_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, recommender.getEmployeeId());
			pstmt.setString(2, recommender.getRecommenderName());
			pstmt.setString(3, recommender.getRelation());
			pstmt.setString(4, recommender.getCompanyName());
			pstmt.setString(5, recommender.getPositionName());
			pstmt.setString(6, recommender.getTelNo());
			pstmt.setInt(7, recommender.getEmployeeRecommenderId());

			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 추천인 내역 삭제
	public int delete(Connection conn, int recId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM EMPLOYEE_RECOMMENDER WHERE EMPLOYEE_RECOMMENDER_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, recId);
			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 사원번호(EMPLOYEE_ID)를 기준으로 해당 사원의 추천인 전체 삭제
	public void deleteByEmpId(Connection conn, int empId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM EMPLOYEE_RECOMMENDER WHERE EMPLOYEE_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, empId);
			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// ResultSet 데이터를 EmployeeRecommender 객체로 변환
	private EmployeeRecommender makeRecommenderFromResultSet(ResultSet rs) throws SQLException {
		EmployeeRecommender rec = new EmployeeRecommender();

		rec.setEmployeeRecommenderId(rs.getInt("EMPLOYEE_RECOMMENDER_ID"));
		rec.setEmployeeId(rs.getInt("EMPLOYEE_ID"));
		rec.setRecommenderName(rs.getString("RECOMMENDER_NAME"));
		rec.setRelation(rs.getString("RELATION"));
		rec.setCompanyName(rs.getString("COMPANY_NAME"));
		rec.setPositionName(rs.getString("POSITION_NAME"));
		rec.setTelNo(rs.getString("TEL_NO"));

		return rec;
	}
}