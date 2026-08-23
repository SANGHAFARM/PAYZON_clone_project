package erp.attendance.service;

import java.sql.Connection;
import java.sql.SQLException;

import erp.attendance.dao.EmployeeAttendanceDao;
import erp.attendance.model.EmployeeAttendance;
import erp.attendance.service.request.AttendanceRecordUpdateRequest;
import erp.settings.dao.AttendanceItemDao;
import erp.settings.model.AttendanceItem;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

// 사원근태Update 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 社員勤怠Updateの業務ルールとデータ変更トランザクションを処理する。
public class EmployeeAttendanceUpdateService {
	private EmployeeAttendanceDao employeeAttendanceDao = EmployeeAttendanceDao.getInstance();
	private AttendanceItemDao attendanceItemDao = AttendanceItemDao.getInstance();
	// 입력값을 검증한 후 사원근태Update 데이터를 트랜잭션으로 저장하거나 수정한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 入力値を検証した後、社員勤怠Updateデータをトランザクションで登録または更新する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	public Integer update(AttendanceRecordUpdateRequest req) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			
			AttendanceItem attendanceItem = attendanceItemDao.selectById(conn, req.getAttendanceItemId());
            if (attendanceItem == null) {
                throw new RuntimeException("invalid attendItemId: " + req.getAttendanceItemId());
            }
            
            EmployeeAttendance record = toEmployeeAttendance(req, attendanceItem);
            System.out.println("업데이트 시도 ID=" + record.getEmployeeAttendanceId() 
            + ", leaveItemId=" + record.getLeaveItemId());
            int result = employeeAttendanceDao.update(conn, record);
            if (result==0) {
				throw new RuntimeException("fail to update record for recordId" + req.getEmployeeAttendanceId());
			}
            conn.commit();
            return result;
		} catch (SQLException e) {
            JdbcUtil.rollback(conn);
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            JdbcUtil.rollback(conn);
            throw e;
        } finally {
            JdbcUtil.close(conn);
        }
	}
	
	// 입력 데이터를 사원근태 처리에 필요한 형식으로 변환한다.
	// 여러 DAO와 입력값을 조합해 화면 또는 다음 업무 단계에서 바로 사용할 수 있는 결과 객체를 만든다.
	// 入力データを社員勤怠処理に必要な形式へ変換する。
	// 複数のDAO結果と入力値を組み合わせ、画面または次の業務段階で直ちに使用できる結果オブジェクトを作成する。
	private EmployeeAttendance toEmployeeAttendance(AttendanceRecordUpdateRequest req, AttendanceItem attendanceItem) {
		EmployeeAttendance record = new EmployeeAttendance();
		record.setEmployeeAttendanceId(req.getEmployeeAttendanceId());
        record.setAttendanceItemId(req.getAttendanceItemId());
        record.setInputDate(req.getInputDate());
        record.setStartDate(req.getStartDate());
        record.setEndDate(req.getEndDate());
        record.setAttendValue(req.getAttendValue());
        record.setPayAmount(req.getPayAmount());
        record.setNote(req.getNote());
        record.setLeaveItemId(attendanceItem.getDeductLeaveId());
        return record;
		
		
	}
}
