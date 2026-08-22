package erp.attendance.service;

import java.sql.Connection;
import java.sql.SQLException;

import erp.attendance.dao.EmployeeAttendanceDao;
import erp.attendance.model.EmployeeAttendance;
import erp.attendance.service.request.InsertEmployeeAttendanceRequest;
import erp.settings.dao.AttendanceItemDao;
import erp.settings.model.AttendanceItem;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

/*
 * EmpAttendRecord테이블에 데이터를 입력하는 서비스 
*/
// Insert사원근태 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// Insert社員勤怠の業務ルールとデータ変更トランザクションを処理する。
public class InsertEmployeeAttendanceService {

    private EmployeeAttendanceDao employeeAttendanceDao = EmployeeAttendanceDao.getInstance();
    private AttendanceItemDao attendanceItemDao = AttendanceItemDao.getInstance();

    // Insert사원근태 처리에 사용할 Insert사원근태 데이터나 객체를 생성한다.
    // 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
    // Insert社員勤怠処理で使用するInsert社員勤怠データまたはオブジェクトを生成する。
    // 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
    public Integer insert(InsertEmployeeAttendanceRequest req) {
        Connection conn = null;
        try {
            conn = ConnectionProvider.getConnection();
            conn.setAutoCommit(false);

            //올바르지 않은 id면 예외 발생
            // 処理中の例外を業務エラーとして整理し、トランザクションと画面案内を安全に終了する。
            AttendanceItem attendanceItem = attendanceItemDao.selectById(conn, req.getAttendanceItemId());
            if (attendanceItem == null) {
                throw new RuntimeException("invalid attendItemId: " + req.getAttendanceItemId());
            }
            
            int successCount = 0;
            for (Integer empId : req.getEmployeeIds()) {
                EmployeeAttendance record = toEmployeeAttendance(req, empId, attendanceItem);
                int result = employeeAttendanceDao.insert(conn, record);
                if (result == 0) {
                    throw new RuntimeException("fail to insert record for empId: " + empId);
                }
                successCount++;
            }

            conn.commit();
            return successCount;
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
    private EmployeeAttendance toEmployeeAttendance(InsertEmployeeAttendanceRequest req, int empId, AttendanceItem attendanceItem) {
        EmployeeAttendance record = new EmployeeAttendance();
        record.setEmployeeId(empId);
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
