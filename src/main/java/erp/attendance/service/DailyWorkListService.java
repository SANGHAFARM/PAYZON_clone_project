package erp.attendance.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import erp.attendance.dao.DailyWorkRecordDao;
import erp.attendance.dto.DailyWorkListDto;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

public class DailyWorkListService {
	private DailyWorkRecordDao dailyWorkRecordDao = DailyWorkRecordDao.getInstance();

	public List<DailyWorkListDto> list(DailyWorkListRequest request) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			List<DailyWorkListDto> list = dailyWorkRecordDao.selectListByRequest(conn, request);
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
