package erp.attendance.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import erp.employees.dao.EmployeeDao;
import erp.employees.dto.AttendanceEmployeeDto;
import jdbc.connection.ConnectionProvider;

//근태관리 탭에서 사원목록 조회에 사용할 서비스
// 목록근태사원 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 一覧勤怠社員の業務ルールとデータ変更トランザクションを処理する。
public class ListAttendanceEmployeeService {
	private EmployeeDao employeeDao = EmployeeDao.getInstance();

	// 목록근태사원 처리에 필요한 근태사원Dtos를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 一覧勤怠社員処理に必要な勤怠社員Dtosを照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
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
