package erp.attendance.service;

import java.sql.Connection;
import java.sql.SQLException;

import erp.attendance.dao.EmployeeAttendanceDao;
import jdbc.connection.ConnectionProvider;

public class EmployeeAttendanceDeleteService {
	private EmployeeAttendanceDao employeeAttendanceDao = EmployeeAttendanceDao.getInstance();
	public Integer delete(int deleteId) {
		try(Connection conn = ConnectionProvider.getConnection()){
			conn.setAutoCommit(false);
			int result = employeeAttendanceDao.delete(conn, deleteId);
			conn.commit();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
}
