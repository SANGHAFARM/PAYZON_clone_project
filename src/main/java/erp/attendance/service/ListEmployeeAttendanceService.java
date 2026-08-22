package erp.attendance.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import erp.attendance.dao.EmployeeAttendanceDao;
import erp.attendance.dto.AttendanceRecordDto;
import erp.attendance.model.EmployeeAttendance;
import jdbc.connection.ConnectionProvider;

//개별 사원의 근태 목록을 조회하는 서비스
// 목록사원근태 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 一覧社員勤怠の業務ルールとデータ変更トランザクションを処理する。
public class ListEmployeeAttendanceService {
	private EmployeeAttendanceDao employeeAttendanceDao = EmployeeAttendanceDao.getInstance();
	// 목록사원근태 처리에 필요한 사원근태를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 一覧社員勤怠処理に必要な社員勤怠を照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
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
