package erp.attendance.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import erp.attendance.dao.DailyWorkRecordDao;
import erp.attendance.dto.DailyWorkDetailDto;
import erp.attendance.service.page.DailyWorkDetailPage;
import erp.attendance.service.request.DailyWorkDetailRequest;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

//일용직근무상세정보 처리에 필요한 일용직근무상세정보 데이터를 조회하여 반환한다.
// 조회 전용 Connection으로 관련 DAO 결과를 조합하며 데이터 변경이 없으므로 별도의 commit이나 rollback을 수행하지 않는다.
// 日雇い勤務詳細情報処理に必要な日雇い勤務詳細情報データを照会して返す。
// 照会専用Connectionで関連DAOの結果を組み合わせ、データ変更がないため個別のcommitやrollbackは実行しない。
public class DailyWorkDetailListService {
	private DailyWorkRecordDao dailyWorkRecordDao = DailyWorkRecordDao.getInstance();
	private int size = 30;
	
	public DailyWorkDetailPage getDetailPage(int pageNum, DailyWorkDetailRequest req) {
		try (Connection conn = ConnectionProvider.getConnection()){
			int total = dailyWorkRecordDao.selectDetailCountByRequest(conn, req);
			int firstRow = (pageNum-1)*size;
			int endRow = firstRow + size;
			List<DailyWorkDetailDto> content = dailyWorkRecordDao.selectDetailByRequest(conn, req, firstRow, endRow);
			return new DailyWorkDetailPage(total, pageNum, size, content);
		}
		catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		} 
	}
}
