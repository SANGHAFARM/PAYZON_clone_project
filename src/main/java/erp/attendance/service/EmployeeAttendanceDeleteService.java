package erp.attendance.service;

import java.sql.Connection;
import java.sql.SQLException;

import erp.attendance.dao.EmployeeAttendanceDao;
import jdbc.connection.ConnectionProvider;

// 사원근태Delete 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 社員勤怠Deleteの業務ルールとデータ変更トランザクションを処理する。
public class EmployeeAttendanceDeleteService {
	private EmployeeAttendanceDao employeeAttendanceDao = EmployeeAttendanceDao.getInstance();
	// 선택되거나 식별된 사원근태Delete 데이터를 삭제하고 관련 상태를 정리한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 選択または識別された社員勤怠Deleteデータを削除し、関連状態を整理する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	public Integer delete(int deleteId) {
		try(Connection conn = ConnectionProvider.getConnection()){
			conn.setAutoCommit(false);
			int result = employeeAttendanceDao.delete(conn, deleteId);
			conn.commit();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
}
