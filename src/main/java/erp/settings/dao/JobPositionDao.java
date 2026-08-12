package erp.settings.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import erp.settings.model.JobPosition;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 직위 설정 데이터베이스 접근(DAO) 클래스
public class JobPositionDao {

	// 싱글톤 인스턴스 생성
	private static JobPositionDao jobPositionDao = new JobPositionDao();

	// 싱글톤 접근 메서드
	public static JobPositionDao getInstance() {
		return jobPositionDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	private JobPositionDao() {
	}

	// 직위 정보 등록
	public void insert(Connection conn, JobPosition pos) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO JOB_POSITION (JOB_POSITION_ID, JOB_POSITION_NAME) VALUES (JOB_POSITION_SEQ.NEXTVAL, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pos.getJobPositionName());
			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 직위 정보 단건 조회
	public JobPosition selectById(Connection conn, int posId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM JOB_POSITION WHERE JOB_POSITION_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, posId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				JobPosition pos = new JobPosition();
				pos.setJobPositionId(rs.getInt("JOB_POSITION_ID"));
				pos.setJobPositionName(rs.getString("JOB_POSITION_NAME"));
				return pos;
			}
			return null;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 직위 목록 전체 조회
	public List<JobPosition> selectAll(Connection conn) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM JOB_POSITION ORDER BY JOB_POSITION_ID ASC";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			List<JobPosition> result = new ArrayList<>();
			while (rs.next()) {
				JobPosition pos = new JobPosition();
				pos.setJobPositionId(rs.getInt("JOB_POSITION_ID"));
				pos.setJobPositionName(rs.getString("JOB_POSITION_NAME"));
				result.add(pos);
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 직위 정보 수정
	public int update(Connection conn, JobPosition pos) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "UPDATE JOB_POSITION SET JOB_POSITION_NAME = ? WHERE JOB_POSITION_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pos.getJobPositionName());
			pstmt.setInt(2, pos.getJobPositionId());
			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 직위 정보 삭제
	public int delete(Connection conn, int posId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM JOB_POSITION WHERE JOB_POSITION_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, posId);
			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}
}