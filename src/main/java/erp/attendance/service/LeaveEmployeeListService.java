package erp.attendance.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import erp.attendance.dao.EmployeeAttendanceDao;
import erp.attendance.dto.AttendanceEmployeeRecordDto;
import jdbc.connection.ConnectionProvider;

// 휴가기록조회 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 休暇記録照会の業務ルールとデータ変更トランザクションを処理する。
public class LeaveEmployeeListService {

	private EmployeeAttendanceDao employeeAttendanceDao = EmployeeAttendanceDao.getInstance();

	// 사원별 휴가현황 모달용 - 사원 1명의 특정 휴가항목에 대한 사용 내역 조회
	// 휴가기록조회 처리에 필요한 휴가Records를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 休暇記録照会処理に必要な休暇Recordsを照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public List<AttendanceEmployeeRecordDto> getLeaveRecords(int employeeId, int leaveItemId) {
		List<AttendanceEmployeeRecordDto> list = null;
		try (Connection conn = ConnectionProvider.getConnection()) {
			list = employeeAttendanceDao.selectByEmpIdAndLeaveItemId(conn, employeeId, leaveItemId);
			return list;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
