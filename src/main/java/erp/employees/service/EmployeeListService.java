package erp.employees.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import erp.employees.dao.CertificateIssuanceDao;
import erp.employees.dao.EmployeeDao;
import erp.employees.dto.DayWorkerDto;
import erp.employees.dto.EmployeeListItem;
import erp.employees.dto.EmployeePageInfo;
import erp.employees.dto.EmployeeSummary;
import erp.payroll.dao.PayrollEmployeeDao;
import erp.retirement.dao.RetirementCalculationDao;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

// 사원 목록 조회와 현황 집계 및 삭제를 처리한다.
// 사원목록 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 社員一覧の業務ルールとデータ変更トランザクションを処理する。
public class EmployeeListService {
	private final EmployeeDao employeeDao = EmployeeDao.getInstance();
	private final CertificateIssuanceDao certificateIssuanceDao = CertificateIssuanceDao.getInstance();
	private final PayrollEmployeeDao payrollEmployeeDao = PayrollEmployeeDao.getInstance();
	private final RetirementCalculationDao retirementCalculationDao = RetirementCalculationDao.getInstance();

	// 사원목록 처리에 필요한 사원목록를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 社員一覧処理に必要な社員一覧を照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public EmployeeListResult getEmployeeList(EmployeeSearchCondition condition) {
		try (Connection conn = ConnectionProvider.getConnection()) {
			// 조건에 맞는 전체 행 수를 먼저 구해 현재 페이지 범위를 계산한다.
			// 検索語と選択条件を組み合わせ、利用者が指定した対象だけを照会する。
			int totalCount = employeeDao.countByCondition(conn, condition);
			EmployeePageInfo pageInfo = new EmployeePageInfo(totalCount, condition.getPage(), condition.getPageSize());
			condition.setPage(pageInfo.getCurrentPage());
			// 계산된 범위의 목록과 화면 상단에 표시할 전체 사원 현황을 조회한다.
			// 画面表示に必要なデータとメッセージをrequestまたはsessionへ保存し、JSPへ引き渡す。
			List<EmployeeListItem> employees = employeeDao.selectListByCondition(conn, condition);
			EmployeeSummary summary = employeeDao.selectSummary(conn);
			return new EmployeeListResult(employees, summary, pageInfo);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	// 전체 사원 중 일용직 사원만 조회한다
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 全社員の中で日雇い社員だけ照会する
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public List<DayWorkerDto> getDayWorkerList(String keyword, String status){
		try(Connection conn = ConnectionProvider.getConnection()){
			List<DayWorkerDto> list = employeeDao.selectDayWorkerListByKeywordAndStatus(conn, keyword, status);
			return list;
		}catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	// 사원목록 처리에 필요한 고용Types를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 社員一覧処理に必要な雇用Typesを照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public List<String> getEmploymentTypes() {
		return Arrays.asList("정규직", "계약직", "임시직", "파견직", "위촉직", "일용직");
	}

	// 연결된 업무 자료와 선택한 사원을 하나의 트랜잭션으로 영구 삭제한다.
	// 선택되거나 식별된 사원 데이터를 삭제하고 관련 상태를 정리한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 選択または識別された社員データを削除し、関連状態を整理する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	public int deleteEmployees(List<Integer> employeeIds) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			int deletedCount = 0;
			for (Integer employeeId : employeeIds) {
				// EMPLOYEE를 참조하는 NO ACTION 외래키 자료부터 정리한다.
				// 未入力値とNULLを区別して正規化し、制約違反や不要な値の保存を防止する。
				certificateIssuanceDao.deleteByEmployeeId(conn, employeeId);
				payrollEmployeeDao.deleteByEmployeeId(conn, employeeId);
				retirementCalculationDao.deleteByEmployeeId(conn, employeeId);
				deletedCount += employeeDao.delete(conn, employeeId);
			}
			conn.commit();
			return deletedCount;
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException("사원정보를 삭제하지 못했습니다. 잠시 후 다시 시도해주세요.", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	public static class EmployeeListResult {
		// 목록과 현황 집계 결과를 한 번에 반환한다.
		// 対象一覧を順番に処理し、各行の入力値または照会結果を同じ基準で構成する。
		private final List<EmployeeListItem> employees;
		private final EmployeeSummary summary;
		private final EmployeePageInfo pageInfo;

		// 조회 목록과 건수·페이지 정보를 하나의 화면 결과 객체로 초기화한다.
		// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
		// 照会一覧と件数・ページ情報を一つの画面結果オブジェクトとして初期化する。
		// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
		public EmployeeListResult(List<EmployeeListItem> employees, EmployeeSummary summary, EmployeePageInfo pageInfo) {
			this.employees = employees;
			this.summary = summary;
			this.pageInfo = pageInfo;
		}
		// 사원목록 처리에 필요한 사원를 조회하거나 계산하여 반환한다.
		// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
		// 社員一覧処理に必要な社員を照会または計算して返す。
		// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
		public List<EmployeeListItem> getEmployees() { return employees; }
		// 요청 조건에 맞는 사원목록 요약정보를 구성하여 반환한다.
		// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
		// リクエスト条件に合う社員一覧の集計情報を構成して返す。
		// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
		public EmployeeSummary getSummary() { return summary; }
		// 사원목록 처리에 필요한 화면 데이터정보를 조회하거나 계산하여 반환한다.
		// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
		// 社員一覧処理に必要な画面データ情報を照会または計算して返す。
		// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
		public EmployeePageInfo getPageInfo() { return pageInfo; }
	}
}
