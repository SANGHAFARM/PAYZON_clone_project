package erp.settings.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import erp.settings.dao.JobPositionDao;
import erp.settings.model.JobPosition;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

public class JobPositionListService {
	private JobPositionDao jobPositionDao = JobPositionDao.getInstance();
	
	public List<JobPosition> list() {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			// 단순 조회는 트랜잭션을 직접 제어하지 않는다.
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
