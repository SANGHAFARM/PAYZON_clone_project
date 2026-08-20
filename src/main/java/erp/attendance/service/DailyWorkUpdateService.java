package erp.attendance.service;

import java.sql.Connection;
import java.sql.SQLException;

import erp.attendance.dao.DailyWorkRecordDao;
import erp.attendance.dao.ProjectDao;
import erp.attendance.model.DailyWorkRecord;
import erp.attendance.model.Project;
import erp.attendance.service.request.DailyWorkUpdateRequest;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

public class DailyWorkUpdateService {
	private DailyWorkRecordDao dailyWorkRecordDao = DailyWorkRecordDao.getInstance();
	private ProjectDao projectDao = ProjectDao.getInstance();
	
	public Integer update(DailyWorkUpdateRequest req) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			
			//올바르지 않은 프로젝트면 예외 발생
			Project project = projectDao.selectById(conn, req.getProjectId());
			if (project == null) {
				throw new RuntimeException("invalid projectId: " + req.getProjectId());
			}
			DailyWorkRecord record = toDailyWorkRecord(req);
			int result = dailyWorkRecordDao.update(conn, record);
			if (result==0) {
				throw new RuntimeException("fail to update record for dailyWorkRecordId: " + record.getDailyWorkRecordId());
			}
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
	
	private DailyWorkRecord toDailyWorkRecord(DailyWorkUpdateRequest req) {
		DailyWorkRecord record = new DailyWorkRecord();
		record.setDailyWorkRecordId(req.getDailyWorkRecordId());
		record.setWorkDate(req.getWorkDate());
		record.setProjectId(req.getProjectId());
		record.setDailyPay(req.getDailyPay());
		record.setPayRate(req.getPayRate());
		record.setIncomeTax(req.getIncomeTax());
		record.setLocalIncomeTax(req.getLocalIncomeTax());
		record.setActualPay(req.getActualPay());
		return record;
	}
	
	
}
