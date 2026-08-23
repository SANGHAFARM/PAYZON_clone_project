package erp.attendance.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import erp.attendance.dao.EmployeeLeaveBalanceDao;
import erp.attendance.dto.LeaveInquiryDto;
import erp.attendance.service.page.LeaveInquiryPage;
import erp.attendance.service.request.LeaveInquiryRequest;
import jdbc.connection.ConnectionProvider;

public class LeaveInquiryPageService {
	private EmployeeLeaveBalanceDao leaveDao = EmployeeLeaveBalanceDao.getInstance();

	public LeaveInquiryPage getInquiryPage(int pageNum, LeaveInquiryRequest req) {
		try(Connection conn = ConnectionProvider.getConnection()){
			int size = req.getPageSize();
			int total = leaveDao.selectCountLeaveBalance(conn, req);
			int firstRow = (pageNum-1)*size;
			int endRow = firstRow + size;
			List<LeaveInquiryDto> content = leaveDao.selectLeaveBalance(conn, req, firstRow, endRow);
			return new LeaveInquiryPage(total, pageNum, size, content);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
}
