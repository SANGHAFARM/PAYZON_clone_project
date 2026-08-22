package erp.attendance.service;

import java.sql.Connection;
import java.sql.SQLException;

import erp.attendance.dao.DailyWorkRecordDao;
import erp.attendance.dao.ProjectDao;
import erp.attendance.model.DailyWorkRecord;
import erp.attendance.model.Project;
import erp.attendance.service.request.DailyWorkUpdateRequest;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

// 일용직근무Update 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 日雇い勤務Updateの業務ルールとデータ変更トランザクションを処理する。
public class DailyWorkUpdateService {
	private DailyWorkRecordDao dailyWorkRecordDao = DailyWorkRecordDao.getInstance();
	private ProjectDao projectDao = ProjectDao.getInstance();
	
	// 입력값을 검증한 후 일용직근무Update 데이터를 트랜잭션으로 저장하거나 수정한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 入力値を検証した後、日雇い勤務Updateデータをトランザクションで登録または更新する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	public Integer update(DailyWorkUpdateRequest req) {
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
			DailyWorkRecord record = toDailyWorkRecord(req);
			int result = dailyWorkRecordDao.update(conn, record);
			if (result==0) {
				throw new RuntimeException("fail to update record for dailyWorkRecordId: " + record.getDailyWorkRecordId());
			}
			conn.commit();
			return result;
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
	private DailyWorkRecord toDailyWorkRecord(DailyWorkUpdateRequest req) {
		DailyWorkRecord record = new DailyWorkRecord();
		record.setDailyWorkRecordId(req.getDailyWorkRecordId());
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
