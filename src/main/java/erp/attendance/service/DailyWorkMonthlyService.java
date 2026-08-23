package erp.attendance.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import erp.attendance.dao.DailyWorkRecordDao;
import erp.attendance.dto.DailyWorkMonthlyDto;
import erp.attendance.service.request.DailyWorkMonthlySearchRequest;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

// 일용직근무목록 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 日雇い勤務一覧の業務ルールとデータ変更トランザクションを処理する。
public class DailyWorkMonthlyService {
	private DailyWorkRecordDao dailyWorkRecordDao = DailyWorkRecordDao.getInstance();

	// 일용직근무목록에서 사용할 전체 항목 목록을 구성하여 반환한다.
	// 조회 전용 Connection으로 관련 DAO 결과를 조합하며 데이터 변경이 없으므로 별도의 commit이나 rollback을 수행하지 않는다.
	// 日雇い勤務一覧で使用する全項目一覧を構成して返す。
	// 照会専用Connectionで関連DAOの結果を組み合わせ、データ変更がないため個別のcommitやrollbackは実行しない。
	public List<DailyWorkMonthlyDto> getDailyWorkMonthly(DailyWorkMonthlySearchRequest req) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			List<DailyWorkMonthlyDto> list = dailyWorkRecordDao.selectDailyWorkMonthly(conn, req);
			conn.commit();
			return list;
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

}
