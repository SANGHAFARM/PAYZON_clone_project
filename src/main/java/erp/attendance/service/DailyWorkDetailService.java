package erp.attendance.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import erp.attendance.dao.DailyWorkRecordDao;
import erp.attendance.dto.DailyWorkDetailDto;
import erp.attendance.service.request.DailyWorkDetailSearchRequest;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

// 일용직근무상세정보 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 日雇い勤務詳細情報の業務ルールとデータ変更トランザクションを処理する。
public class DailyWorkDetailService {
	private DailyWorkRecordDao dailyWorkRecordDao = DailyWorkRecordDao.getInstance();
	// 일용직근무상세정보 처리에 필요한 일용직근무상세정보 데이터를 조회하여 반환한다.
	// 조회 전용 Connection으로 관련 DAO 결과를 조합하며 데이터 변경이 없으므로 별도의 commit이나 rollback을 수행하지 않는다.
	// 日雇い勤務詳細情報処理に必要な日雇い勤務詳細情報データを照会して返す。
	// 照会専用Connectionで関連DAOの結果を組み合わせ、データ変更がないため個別のcommitやrollbackは実行しない。
	public List<DailyWorkDetailDto> getDailyWorkDetail(DailyWorkDetailSearchRequest req){
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			List<DailyWorkDetailDto> list = dailyWorkRecordDao.selectDailyWorkDetail(conn, req);
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
