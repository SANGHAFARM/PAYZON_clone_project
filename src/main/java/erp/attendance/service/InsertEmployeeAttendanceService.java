package erp.attendance.service;

import java.sql.Connection;
import java.sql.SQLException;

import erp.attendance.dao.EmployeeAttendanceDao;
import erp.attendance.model.EmployeeAttendance;
import erp.attendance.service.request.InsertEmployeeAttendanceRequest;
import erp.settings.dao.AttendanceItemDao;
import erp.settings.model.AttendanceItem;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

/*
 * EmpAttendRecord테이블에 데이터를 입력하는 서비스 
*/
public class InsertEmployeeAttendanceService {

    private EmployeeAttendanceDao employeeAttendanceDao = EmployeeAttendanceDao.getInstance();
    private AttendanceItemDao attendanceItemDao = AttendanceItemDao.getInstance();

    public Integer insert(InsertEmployeeAttendanceRequest req) {
        Connection conn = null;
        try {
            conn = ConnectionProvider.getConnection();
            conn.setAutoCommit(false);

            //올바르지 않은 id면 예외 발생
            AttendanceItem attendanceItem = attendanceItemDao.selectById(conn, req.getAttendanceItemId());
            if (attendanceItem == null) {
                throw new RuntimeException("invalid attendItemId: " + req.getAttendanceItemId());
            }
            
            int successCount = 0;
            for (Integer empId : req.getEmployeeIds()) {
                EmployeeAttendance record = toEmployeeAttendance(req, empId, attendanceItem);
                int result = employeeAttendanceDao.insert(conn, record);
                if (result == 0) {
                    throw new RuntimeException("fail to insert record for empId: " + empId);
                }
                successCount++;
            }

            conn.commit();
            return successCount;
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

    private EmployeeAttendance toEmployeeAttendance(InsertEmployeeAttendanceRequest req, int empId, AttendanceItem attendanceItem) {
        EmployeeAttendance record = new EmployeeAttendance();
        record.setEmployeeId(empId);
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