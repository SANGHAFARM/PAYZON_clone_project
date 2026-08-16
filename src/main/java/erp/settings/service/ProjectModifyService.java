package erp.settings.service;

import java.sql.Connection;
import java.sql.SQLException;

import erp.settings.dao.ProjectDao;
import erp.settings.model.Project;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

public class ProjectModifyService {
	private ProjectDao projectDao = ProjectDao.getInstance();

	public Integer modify(Project projectReq) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			Project project = projectDao.selectByName(conn, projectReq.getProjectName());
			//만약 해당 이름의 프로젝트가 존재하면 중복 오류 발생
			if (project!=null) {
				throw new RuntimeException("Project name already exists : " + projectReq.getProjectName() );
			}
			int result = projectDao.update(conn, projectReq);
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
