package erp.attendance.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import erp.attendance.dao.EmployeeAttendanceDao;
import erp.attendance.dto.AttendanceDetailDto;
import erp.attendance.service.request.AttendanceDetailRequest;
import jdbc.connection.ConnectionProvider;

public class ListAttendanceDetailService {
	private EmployeeAttendanceDao employeeAttendanceDao = EmployeeAttendanceDao.getInstance();
	public List<AttendanceDetailDto> getList(AttendanceDetailRequest req){
		List<AttendanceDetailDto> list = null;
		try(Connection conn = ConnectionProvider.getConnection()){
			list = employeeAttendanceDao.selectDetailList(conn, req);
			return list;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
}
