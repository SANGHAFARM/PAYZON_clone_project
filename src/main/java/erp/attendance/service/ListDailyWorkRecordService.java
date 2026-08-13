package erp.attendance.service;

import java.sql.Connection;
import java.sql.SQLException;

import erp.attendance.dao.DailyWorkRecordDao;
import erp.attendance.dto.DailyWorkRecordDto;
import jdbc.connection.ConnectionProvider;

public class ListDailyWorkRecordService {
	
	private DailyWorkRecordDao dailyWorkRecordDao = DailyWorkRecordDao.getInstance();
	public DailyWorkRecordDto getDailyWorkRecordDto() {
		try(Connection conn = ConnectionProvider.getConnection()){
			
		} catch (SQLException e) {
		}
		return null;
	}
	
}
