package erp.attendance.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import erp.attendance.dao.EmployeeAttendanceDao;
import erp.attendance.dto.AttendanceRecordDto;
import erp.attendance.model.EmployeeAttendance;
import jdbc.connection.ConnectionProvider;

//개별 사원의 근태 목록을 조회하는 서비스
public class ListEmployeeAttendanceService {
	private EmployeeAttendanceDao employeeAttendanceDao = EmployeeAttendanceDao.getInstance();
	public List<AttendanceRecordDto> getEmployeeAttendance(int employeeId, int year, Integer month){
		List<AttendanceRecordDto> list = null;
		try(Connection conn = ConnectionProvider.getConnection()){
			list = employeeAttendanceDao.selectByEmpIdAndYearAndMonth(conn, employeeId, year, month);
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
}
