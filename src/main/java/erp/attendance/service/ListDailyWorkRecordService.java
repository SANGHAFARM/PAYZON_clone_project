package erp.attendance.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import erp.attendance.dao.DailyWorkRecordDao;
import erp.attendance.dto.DailyWorkRecordDto;
import erp.attendance.service.request.DailyWorkRecordRequest;
import jdbc.connection.ConnectionProvider;


//일용직 개별 사원의 연월별 근무기록을 조회하는 서비스
// 목록일용직근무기록 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 一覧日雇い勤務記録の業務ルールとデータ変更トランザクションを処理する。
public class ListDailyWorkRecordService {
	private DailyWorkRecordDao dailyWorkRecordDao = DailyWorkRecordDao.getInstance();
	// 목록일용직근무기록 처리에 필요한 일용직근무기록전달 데이터를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 一覧日雇い勤務記録処理に必要な日雇い勤務記録転送データを照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public List<DailyWorkRecordDto> getDailyWorkRecordDto(DailyWorkRecordRequest req) {
		List<DailyWorkRecordDto> list = null;
		try(Connection conn = ConnectionProvider.getConnection()){
			list = dailyWorkRecordDao.selectByRequest(conn, req);
			return list;
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	
}
