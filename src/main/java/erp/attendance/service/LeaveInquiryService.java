package erp.attendance.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import erp.attendance.dao.EmployeeLeaveBalanceDao;
import erp.attendance.dto.LeaveInquiryDto;
import erp.attendance.service.request.LeaveInquiryRequest;
import jdbc.connection.ConnectionProvider;

public class LeaveInquiryService {
	private EmployeeLeaveBalanceDao employeeLeaveBalanceDao = EmployeeLeaveBalanceDao.getInstance();

	public List<LeaveInquiryDto> getLeaveEmployees(LeaveInquiryRequest req) {
		List<LeaveInquiryDto> list = null;
		try(Connection conn = ConnectionProvider.getConnection()){
			list = employeeLeaveBalanceDao.selectLeaveBalance(conn, req);
			return list;
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
