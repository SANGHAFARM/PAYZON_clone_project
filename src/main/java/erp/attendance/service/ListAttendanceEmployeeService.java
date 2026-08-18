package erp.attendance.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import erp.employees.dao.EmployeeDao;
import erp.employees.dto.AttendanceEmployeeDto;
import jdbc.connection.ConnectionProvider;

//근태관리 탭에서 사원목록 조회에 사용할 서비스
public class ListAttendanceEmployeeService {
	private EmployeeDao employeeDao = EmployeeDao.getInstance();

	public List<AttendanceEmployeeDto> getAttendanceEmployeeDtos(String keyword, String status) {
		List<AttendanceEmployeeDto> list = null;
		try (Connection conn = ConnectionProvider.getConnection()) {
			list = employeeDao.selectAttendanceEmployees(conn, keyword, status);
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
}