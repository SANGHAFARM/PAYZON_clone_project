package erp.attendance.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import erp.attendance.dao.EmployeeAttendanceDao;
import erp.attendance.dto.AttendanceDetailDto;
import erp.attendance.service.request.AttendanceDetailRequest;
import jdbc.connection.ConnectionProvider;

// 목록근태상세정보 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 一覧勤怠詳細情報の業務ルールとデータ変更トランザクションを処理する。
public class ListAttendanceDetailService {
	private EmployeeAttendanceDao employeeAttendanceDao = EmployeeAttendanceDao.getInstance();
	// 요청 조건에 맞는 목록근태상세정보 목록를 구성하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// リクエスト条件に合う一覧勤怠詳細情報の一覧を構成して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
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
