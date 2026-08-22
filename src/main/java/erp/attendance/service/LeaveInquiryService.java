package erp.attendance.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import erp.attendance.dao.EmployeeLeaveBalanceDao;
import erp.attendance.dto.LeaveInquiryDto;
import erp.attendance.service.request.LeaveInquiryRequest;
import jdbc.connection.ConnectionProvider;

// 휴가조회 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 休暇照会の業務ルールとデータ変更トランザクションを処理する。
public class LeaveInquiryService {
	private EmployeeLeaveBalanceDao employeeLeaveBalanceDao = EmployeeLeaveBalanceDao.getInstance();

	// 휴가조회 처리에 필요한 휴가사원를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 休暇照会処理に必要な休暇社員を照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
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
