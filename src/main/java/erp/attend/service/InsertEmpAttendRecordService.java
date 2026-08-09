package erp.attend.service;

import java.sql.Connection;
import java.sql.SQLException;

import erp.attend.dao.AttendItemDao;
import erp.attend.dao.EmpAttendRecordDao;
import erp.attend.model.AttendItem;
import erp.attend.model.EmpAttendRecord;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

/*
 * EmpAttendRecord테이블에 데이터를 입력하는 서비스 
*/
public class InsertEmpAttendRecordService {

	private EmpAttendRecordDao recordDao = EmpAttendRecordDao.getInstance();
	private AttendItemDao attendItemDao = AttendItemDao.getInstance();
	
	/*
	 * Request를 기반으로 EmpAttendRecord테이블에 데이터를 입력하는 메서드
	 */
	public Integer insert(AttendRecordRequest req) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			
			EmpAttendRecord record = toRecord(req, conn);
			
			int result = recordDao.insert(conn, record);
			if (result == 0) {
				throw new RuntimeException("fail to insert record");
			}
			conn.commit();
			
			return result;
		} catch (SQLException  e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} catch (RuntimeException e) {
			JdbcUtil.rollback(conn);
			throw e;
		} finally {
			JdbcUtil.close(conn);
		}
	}
	
	/*
	 * Request를 기반으로 EmpAttendRecord객체를 만들고 반환하는 메서드
	 */
	private EmpAttendRecord toRecord(AttendRecordRequest req, Connection conn) throws SQLException {
		EmpAttendRecord record = new EmpAttendRecord();
		record.setEmpId(req.getEmpId());
		record.setAttendItemId(req.getAttendItemId());
		record.setInputDate(req.getInputDate());
		record.setStartDate(req.getStartDate());
		record.setEndDate(req.getEndDate());
		record.setAttendValue(req.getAttendValue());
		record.setPayAmount(req.getPayAmount());
		record.setNote(req.getNote());
		
		//Request가 휴가관련 항목인지 확인하는 작업
		AttendItem attendItem = attendItemDao.selectById(conn, req.getAttendItemId());
		if (attendItem==null) {
			throw new RuntimeException("invalid attendItemId: " + req.getAttendItemId());
		}
		record.setLeaveItemId(attendItem.getDeductLeaveId());
		
		return record;
	}
}
