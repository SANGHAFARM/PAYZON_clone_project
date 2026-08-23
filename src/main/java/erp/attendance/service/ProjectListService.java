package erp.attendance.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import erp.attendance.dao.ProjectDao;
import erp.attendance.model.Project;
import jdbc.connection.ConnectionProvider;

public class ProjectListService {
	private ProjectDao projectDao = ProjectDao.getInstance();
	public List<Project> getProjects(){
		List<Project> list = null;
		try(Connection conn = ConnectionProvider.getConnection()){
			list=  projectDao.selectAll(conn);
			return list;
		}catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
