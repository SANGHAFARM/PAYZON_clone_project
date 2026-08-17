package erp.settings.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import erp.settings.dao.JobPositionDao;
import erp.settings.model.JobPosition;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

public class JobPositionListService {
	JobPositionDao jobPositionDao = JobPositionDao.getInstance();
	
	public List<JobPosition> list(){
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			return jobPositionDao.selectAll(conn);
		}catch (SQLException e) {
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
