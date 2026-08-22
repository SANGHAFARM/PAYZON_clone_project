package erp.attendance.service;

import java.sql.Connection;
import java.sql.SQLException;

import erp.attendance.dao.DailyWorkRecordDao;
import erp.attendance.dao.ProjectDao;
import erp.attendance.model.DailyWorkRecord;
import erp.attendance.model.Project;
import erp.attendance.service.request.DailyWorkInsertRequest;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

// 일용직근무Insert 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 日雇い勤務Insertの業務ルールとデータ変更トランザクションを処理する。
public class DailyWorkInsertService {

	private DailyWorkRecordDao dailyWorkRecordDao = DailyWorkRecordDao.getInstance();
	private ProjectDao projectDao = ProjectDao.getInstance();
			
	// 일용직근무Insert 처리에 사용할 일용직근무Insert 데이터나 객체를 생성한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 日雇い勤務Insert処理で使用する日雇い勤務Insertデータまたはオブジェクトを生成する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	public Integer insert(DailyWorkInsertRequest req) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			
			//올바르지 않은 프로젝트면 예외 발생
			// 処理中の例外を業務エラーとして整理し、トランザクションと画面案内を安全に終了する。
			Project project = projectDao.selectById(conn, req.getProjectId());
			if (project == null) {
				throw new RuntimeException("invalid projectId: " + req.getProjectId());
			}
			
			int successCount = 0;
			//반복문을 돌며 각 사원의 기록 입력
			// 社員の識別情報と在職・雇用・所属条件を確認し、対象社員データへ反映する。
			for(Integer empId : req.getEmployeeIds()) {
				DailyWorkRecord record = toDailyWorkRecord(req, empId);
				int result = dailyWorkRecordDao.insert(conn, record);
				if (result==0) {
					throw new RuntimeException("fail to insert record for empId: " + empId);
				}
				successCount++;
			}
			conn.commit();
			return successCount;
		} catch (SQLException e) {
			e.printStackTrace();
			JdbcUtil.rollback(conn);
			throw new RuntimeException();
		} catch (RuntimeException e) {
			JdbcUtil.rollback(conn);
			throw e;
		} finally {
			JdbcUtil.close(conn);
		}
	}
	
	// 입력 데이터를 일용직근무기록 처리에 필요한 형식으로 변환한다.
	// 여러 DAO와 입력값을 조합해 화면 또는 다음 업무 단계에서 바로 사용할 수 있는 결과 객체를 만든다.
	// 入力データを日雇い勤務記録処理に必要な形式へ変換する。
	// 複数のDAO結果と入力値を組み合わせ、画面または次の業務段階で直ちに使用できる結果オブジェクトを作成する。
	private DailyWorkRecord toDailyWorkRecord(DailyWorkInsertRequest req, Integer empId) {
		DailyWorkRecord record = new DailyWorkRecord();
		record.setEmployeeId(empId);
		record.setWorkDate(req.getWorkDate());
		record.setProjectId(req.getProjectId());
		record.setDailyPay(req.getDailyPay());
		record.setPayRate(req.getPayRate());
		record.setIncomeTax(req.getIncomeTax());
		record.setLocalIncomeTax(req.getLocalIncomeTax());
		record.setActualPay(req.getActualPay());
		return record;
	}
	

}
