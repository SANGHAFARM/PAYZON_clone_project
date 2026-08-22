package erp.settings.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import erp.settings.dao.JobPositionDao;
import erp.settings.model.JobPosition;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

// 직무직위목록 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 職務役職一覧の業務ルールとデータ変更トランザクションを処理する。
public class JobPositionListService {
	private JobPositionDao jobPositionDao = JobPositionDao.getInstance();
	
	// 직무직위목록에서 사용할 전체 항목 목록을 구성하여 반환한다.
	// 조회 전용 Connection으로 관련 DAO 결과를 조합하며 데이터 변경이 없으므로 별도의 commit이나 rollback을 수행하지 않는다.
	// 職務役職一覧で使用する全項目一覧を構成して返す。
	// 照会専用Connectionで関連DAOの結果を組み合わせ、データ変更がないため個別のcommitやrollbackは実行しない。
	public List<JobPosition> list() {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			// 단순 조회는 트랜잭션을 직접 제어하지 않는다.
			// 複数のデータ変更を一つのトランザクションとして処理し、成功時はコミット、失敗時はロールバックする。
			return jobPositionDao.selectAll(conn);
		} catch (SQLException e) {
			throw new RuntimeException("직위 목록 조회 중 오류가 발생했습니다.", e);
		} catch (RuntimeException e) {
			throw e;
		} finally {
			JdbcUtil.close(conn);
		}
	}
}
