package erp.settings.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import erp.settings.model.Project;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 현장/프로젝트 목록 데이터베이스 접근(DAO) 클래스
public class ProjectDao {

	// 싱글톤 인스턴스 생성
	private static ProjectDao projectDao = new ProjectDao();

	// 싱글톤 접근 메서드
	public static ProjectDao getInstance() {
		return projectDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	private ProjectDao() {
	}

	// 프로젝트 정보 등록
	public void insert(Connection conn, Project project) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO PROJECT (PROJECT_ID, PROJECT_NAME) VALUES (PROJECT_SEQ.NEXTVAL, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, project.getProjectName());
			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 프로젝트 정보 단건 조회
	public Project selectById(Connection conn, int projectId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM PROJECT WHERE PROJECT_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, projectId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				Project project = new Project();
				project.setProjectId(rs.getInt("PROJECT_ID"));
				project.setProjectName(rs.getString("PROJECT_NAME"));
				return project;
			}
			return null;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 프로젝트 목록 전체 조회
	public List<Project> selectAll(Connection conn) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM PROJECT ORDER BY PROJECT_ID ASC";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			List<Project> result = new ArrayList<>();
			while (rs.next()) {
				Project project = new Project();
				project.setProjectId(rs.getInt("PROJECT_ID"));
				project.setProjectName(rs.getString("PROJECT_NAME"));
				result.add(project);
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 프로젝트 정보 수정
	public int update(Connection conn, Project project) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "UPDATE PROJECT SET PROJECT_NAME = ? WHERE PROJECT_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, project.getProjectName());
			pstmt.setInt(2, project.getProjectId());
			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 프로젝트 정보 삭제
	public int delete(Connection conn, int projectId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM PROJECT WHERE PROJECT_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, projectId);
			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}
}