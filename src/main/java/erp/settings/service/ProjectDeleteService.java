package erp.settings.service;

import java.sql.Connection;
import java.sql.SQLException;

import erp.settings.dao.ProjectDao;
import erp.settings.model.Project;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

public class ProjectDeleteService {
	private ProjectDao projectDao = ProjectDao.getInstance();
	
	public Integer delete(int projectId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			Project project = projectDao.selectById(conn, projectId);
			if (project==null) {
				throw new RuntimeException("Invalid projectId : " + projectId);
			}
			int result = projectDao.delete(conn, projectId);
			conn.commit();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			JdbcUtil.rollback(conn);
			throw new RuntimeException();
		} catch (RuntimeException e) {
			JdbcUtil.rollback(conn);
			throw e;
		} finally {
			JdbcUtil.close(conn);
		}
	}
}
