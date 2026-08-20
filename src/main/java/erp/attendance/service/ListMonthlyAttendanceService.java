package erp.attendance.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import erp.attendance.dao.EmployeeAttendanceDao;
import erp.attendance.dto.MonthlyAttendanceDto;
import jdbc.connection.ConnectionProvider;

public class ListMonthlyAttendanceService {
	private EmployeeAttendanceDao employeeAttendanceDao = EmployeeAttendanceDao.getInstance();
	public List<MonthlyAttendanceDto> getMonthlyAttendance(MonthlyAttendanceRequest request){
		List<MonthlyAttendanceDto> list = null;
		try(Connection conn = ConnectionProvider.getConnection()){
			list = employeeAttendanceDao.selectByCondition(conn, request);
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
}
