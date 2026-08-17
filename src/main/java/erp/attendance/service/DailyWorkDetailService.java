package erp.attendance.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import erp.attendance.dao.DailyWorkRecordDao;
import erp.attendance.dto.DailyWorkDetailDto;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

public class DailyWorkDetailService {
	private DailyWorkRecordDao dailyWorkRecordDao = DailyWorkRecordDao.getInstance();
	public List<DailyWorkDetailDto> search(DailyWorkDetailRequest req){
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			List<DailyWorkDetailDto> list = dailyWorkRecordDao.selectDetailByRequest(conn, req);
			conn.commit();
			return list;
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
