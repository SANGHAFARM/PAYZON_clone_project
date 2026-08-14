package erp.attendance.service;

import java.sql.Connection;
import java.sql.SQLException;

import erp.attendance.dao.DailyWorkRecordDao;
import erp.attendance.model.DailyWorkRecord;
import erp.settings.dao.ProjectDao;
import erp.settings.model.Project;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

public class DailyWorkInsertService {

	private DailyWorkRecordDao dailyWorkRecordDao = DailyWorkRecordDao.getInstance();
	private ProjectDao projectDao = ProjectDao.getInstance();
			
	public Integer insert(DailyWorkInsertRequest req) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			
			//올바르지 않은 프로젝트면 예외 발생
			Project project = projectDao.selectById(conn, req.getProjectId());
			if (project == null) {
				throw new RuntimeException("invalid projectId: " + req.getProjectId());
			}
			
			int successCount = 0;
			//반복문을 돌며 각 사원의 기록 입력
			for(Integer empId : req.getEmployeeIds()) {
				DailyWorkRecord record = toDailyWorkRecord(req, empId);
				int result = dailyWorkRecordDao.insert(conn, record);
				if (result==0) {
					throw new RuntimeException("fail to insert record for empId: " + empId);
				}
				successCount++;
			}
			conn.commit();
			return successCount;
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException();
		} catch (RuntimeException e) {
			JdbcUtil.rollback(conn);
			throw e;
		} finally {
			JdbcUtil.close(conn);
		}
	}
	
	private DailyWorkRecord toDailyWorkRecord(DailyWorkInsertRequest req, Integer empId) {
		DailyWorkRecord record = new DailyWorkRecord();
		record.setEmployeeId(empId);
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
