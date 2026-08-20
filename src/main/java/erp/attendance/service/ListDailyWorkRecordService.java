package erp.attendance.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import erp.attendance.dao.DailyWorkRecordDao;
import erp.attendance.dto.DailyWorkRecordDto;
import erp.attendance.service.request.DailyWorkRecordRequest;
import jdbc.connection.ConnectionProvider;


//일용직 개별 사원의 연월별 근무기록을 조회하는 서비스
public class ListDailyWorkRecordService {
	private DailyWorkRecordDao dailyWorkRecordDao = DailyWorkRecordDao.getInstance();
	public List<DailyWorkRecordDto> getDailyWorkRecordDto(DailyWorkRecordRequest req) {
		List<DailyWorkRecordDto> list = null;
		try(Connection conn = ConnectionProvider.getConnection()){
			list = dailyWorkRecordDao.selectByRequest(conn, req);
			return list;
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	
}
