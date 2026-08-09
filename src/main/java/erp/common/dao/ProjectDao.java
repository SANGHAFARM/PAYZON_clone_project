package erp.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import erp.common.model.Project;
import jdbc.JdbcUtil;

public class ProjectDao {
	private static ProjectDao projectDao = new ProjectDao();
	
	public static ProjectDao getInstance() {
		return projectDao;
	}
	
	private ProjectDao() {
	}
	
	/*
	 * Project테이블에 현장/프로젝트를 입력하는 메서드
	 * Projectテーブルに現場・プロジェクトを入力するメソッド
	 */
	public int insert(Connection conn, Project project) throws SQLException{
		String sql = "INSERT INTO PROJECT VALUES (SEQ_PROJECT_ID.NEXTVAL, ?)";
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setString(1, project.getProjectName());
			return pstmt.executeUpdate();
		}
	}
	
	/*
	 * Project테이블의 모든 데이터를 조회하는 메서드
	 * Projectテーブルの全てのデータを照会するメソッド
	 */	
	public List<Project> select(Connection conn) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement("SELECT * FROM PROJECT");
			rs = pstmt.executeQuery();
			List<Project> result = new ArrayList<>();
			while (rs.next()) {
				result.add(convertProject(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}
	
	/*
	 * Project테이블에 있는 현장/프로젝트 정보를 수정하는 메서드
	 * Projectテーブルにある現場・プロジェクト情報を修正するメソッド
	 */
	public int update(Connection conn, Project project) throws SQLException{
		String sql = "UPDATE PROJECT SET PROJECT_NAME=? WHERE PROJECT_ID=?";
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setString(1, project.getProjectName());
			pstmt.setInt(2, project.getProjectId());
			return pstmt.executeUpdate();
		}
		
	}
	
	/*
	 * Project테이블에 있는 현장/프로젝트를 삭제하는 메서드
	 * Projectテーブルにある現場・プロジェクトを削除するメソッド
	 */	
	public int delete(Connection conn, int no) throws SQLException {
		String sql = "DELETE FROM PROJECT WHERE PROJECT_ID=?";
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setInt(1, no);
			return pstmt.executeUpdate();
		}
	}
	
	/*
	 * ResultSet으로 Project객체를 만들어 반환하는 메서드
	 * ResultSetでProjectオブジェクトを作って返すメソッド
	 */
	private Project convertProject(ResultSet rs) throws SQLException{
		Project project = new Project();
		project.setProjectId(rs.getInt("PROJECT_ID"));
		project.setProjectName("PROJECT_NAME");
		return project;
	}
	

	
}
