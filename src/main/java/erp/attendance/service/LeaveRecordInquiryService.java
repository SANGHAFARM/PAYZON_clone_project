package erp.attendance.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import erp.attendance.dao.EmployeeAttendanceDao;
import erp.attendance.dto.AttendanceRecordDto;
import jdbc.connection.ConnectionProvider;

public class LeaveRecordInquiryService {

	private EmployeeAttendanceDao employeeAttendanceDao = EmployeeAttendanceDao.getInstance();

	// 사원별 휴가현황 모달용 - 사원 1명의 특정 휴가항목에 대한 사용 내역 조회
	public List<AttendanceRecordDto> getLeaveRecords(int employeeId, int leaveItemId) {
		List<AttendanceRecordDto> list = null;
		try (Connection conn = ConnectionProvider.getConnection()) {
			list = employeeAttendanceDao.selectByEmpIdAndLeaveItemId(conn, employeeId, leaveItemId);
			return list;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}