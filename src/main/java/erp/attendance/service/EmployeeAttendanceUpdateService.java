package erp.attendance.service;

import java.sql.Connection;
import java.sql.SQLException;

import erp.attendance.dao.EmployeeAttendanceDao;
import erp.attendance.model.EmployeeAttendance;
import erp.attendance.service.request.EmployeeAttendanceUpdateRequest;
import erp.settings.dao.AttendanceItemDao;
import erp.settings.model.AttendanceItem;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

public class EmployeeAttendanceUpdateService {
	private EmployeeAttendanceDao employeeAttendanceDao = EmployeeAttendanceDao.getInstance();
	private AttendanceItemDao attendanceItemDao = AttendanceItemDao.getInstance();
	public Integer update(EmployeeAttendanceUpdateRequest req) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			
			AttendanceItem attendanceItem = attendanceItemDao.selectById(conn, req.getAttendanceItemId());
            if (attendanceItem == null) {
                throw new RuntimeException("invalid attendItemId: " + req.getAttendanceItemId());
            }
            
            EmployeeAttendance record = toEmployeeAttendance(req, attendanceItem);
            System.out.println("업데이트 시도 ID=" + record.getEmployeeAttendanceId() 
            + ", leaveItemId=" + record.getLeaveItemId());
            int result = employeeAttendanceDao.update(conn, record);
            if (result==0) {
				throw new RuntimeException("fail to update record for recordId" + req.getEmployeeAttendanceId());
			}
            conn.commit();
            return result;
		} catch (SQLException e) {
            JdbcUtil.rollback(conn);
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            JdbcUtil.rollback(conn);
            throw e;
        } finally {
            JdbcUtil.close(conn);
        }
	}
	
	private EmployeeAttendance toEmployeeAttendance(EmployeeAttendanceUpdateRequest req, AttendanceItem attendanceItem) {
		EmployeeAttendance record = new EmployeeAttendance();
		record.setEmployeeAttendanceId(req.getEmployeeAttendanceId());
        record.setAttendanceItemId(req.getAttendanceItemId());
        record.setInputDate(req.getInputDate());
        record.setStartDate(req.getStartDate());
        record.setEndDate(req.getEndDate());
        record.setAttendValue(req.getAttendValue());
        record.setPayAmount(req.getPayAmount());
        record.setNote(req.getNote());
        record.setLeaveItemId(attendanceItem.getDeductLeaveId());
        return record;
		
		
	}
}
