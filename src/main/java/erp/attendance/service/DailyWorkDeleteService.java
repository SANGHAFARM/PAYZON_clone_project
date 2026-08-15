package erp.attendance.service;

import java.sql.Connection;
import java.sql.SQLException;

import erp.attendance.dao.DailyWorkRecordDao;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

public class DailyWorkDeleteService {

	private DailyWorkRecordDao dailyWorkRecordDao = DailyWorkRecordDao.getInstance();
	
	public Integer delete(int deleteId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			
			int successCount = dailyWorkRecordDao.delete(conn, deleteId);
			conn.commit();
			return successCount;
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
