package erp.retirement.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Date;
import java.util.List;
import java.util.Set;

import erp.employees.dao.EmployeeDao;
import erp.employees.dto.EmployeeListItem;
import erp.employees.dto.EmployeePageInfo;
import erp.employees.model.Employee;
import erp.employees.service.EmployeeSearchCondition;
import erp.retirement.dto.RetirementTypeItem;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

// 퇴직대상 사원 조회와 퇴직처리·취소 트랜잭션을 처리한다.
// 퇴직급여처리 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 退職給与処理の業務ルールとデータ変更トランザクションを処理する。
public class RetirementProcessService {
	private static final Set<String> RETIREMENT_TYPES = new HashSet<>(Arrays.asList(
			"自己都合退職", "勧奨退職", "契約期間満了", "定年退職", "解雇", "その他"));

	private final EmployeeDao employeeDao = EmployeeDao.getInstance();

	// 퇴직급여처리 처리에 필요한 사원화면 데이터를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 退職給与処理処理に必要な社員画面データを照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public RetirementEmployeePage getEmployeePage(EmployeeSearchCondition condition) {
		try (Connection conn = ConnectionProvider.getConnection()) {
			int totalCount = employeeDao.countByCondition(conn, condition);
			EmployeePageInfo pageInfo = new EmployeePageInfo(totalCount, condition.getPage(), condition.getPageSize());
			condition.setPage(pageInfo.getCurrentPage());
			List<EmployeeListItem> employees = employeeDao.selectListByCondition(conn, condition);
			return new RetirementEmployeePage(employees, pageInfo);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	// 선택한 사원의 퇴직정보를 검증하고 재직상태를 퇴직으로 변경한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 選択した社員の退職情報を検証し、在職状態を退職へ変更する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	public void retire(int employeeId, String retirementType, Date retirementDate, String reason, String afterContact) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			Employee employee = employeeDao.selectById(conn, employeeId);
			if (employee == null) {
				throw new IllegalArgumentException("존재하지 않는 사원입니다.");
			}
			if ("퇴직".equals(employee.getStatus())) {
				throw new IllegalArgumentException("이미 퇴직 처리된 사원입니다.");
			}
			if (!RETIREMENT_TYPES.contains(retirementType)) {
				throw new IllegalArgumentException("올바른 퇴직구분을 선택하세요.");
			}
			if (retirementDate == null) {
				throw new IllegalArgumentException("퇴직일자를 입력하세요.");
			}
			if (employee.getJoinDate() != null && retirementDate.before(employee.getJoinDate())) {
				throw new IllegalArgumentException("퇴직일자는 입사일보다 빠를 수 없습니다.");
			}
			employeeDao.updateRetirement(conn, employeeId, retirementType, retirementDate, reason, afterContact);
			conn.commit();
		} catch (SQLException | RuntimeException e) {
			JdbcUtil.rollback(conn);
			throw e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 완료된 퇴직처리를 취소하고 사원상태와 퇴직정보를 재직 상태로 복구한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 完了した退職処理を取り消し、社員状態と退職情報を在職状態へ戻す。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	public void cancel(int employeeId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			if (employeeDao.cancelRetirement(conn, employeeId) == 0) {
				throw new IllegalArgumentException("퇴직처리된 사원이 아닙니다.");
			}
			conn.commit();
		} catch (SQLException | RuntimeException e) {
			JdbcUtil.rollback(conn);
			throw e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 퇴직급여처리 처리에 필요한 퇴직급여Types를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 退職給与処理処理に必要な退職給与Typesを照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public List<RetirementTypeItem> getRetirementTypes() {
		return Arrays.asList(new RetirementTypeItem("自己都合退職", "自己都合退職"),
                new RetirementTypeItem("勧奨退職", "勧奨退職"),
                new RetirementTypeItem("契約期間満了", "契約期間満了"),
                new RetirementTypeItem("定年退職", "定年退職"),
                new RetirementTypeItem("解雇", "解雇"),
                new RetirementTypeItem("その他", "その他"));
	}

	public static class RetirementEmployeePage {
		private final List<EmployeeListItem> employees;
		private final EmployeePageInfo pageInfo;

		// 조회 목록과 건수·페이지 정보를 하나의 화면 결과 객체로 초기화한다.
		// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
		// 照会一覧と件数・ページ情報を一つの画面結果オブジェクトとして初期化する。
		// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
		public RetirementEmployeePage(List<EmployeeListItem> employees, EmployeePageInfo pageInfo) {
			this.employees = employees;
			this.pageInfo = pageInfo;
		}

		// 퇴직급여처리 처리에 필요한 사원를 조회하거나 계산하여 반환한다.
		// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
		// 退職給与処理処理に必要な社員を照会または計算して返す。
		// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
		public List<EmployeeListItem> getEmployees() { return employees; }
		// 퇴직급여처리 처리에 필요한 화면 데이터정보를 조회하거나 계산하여 반환한다.
		// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
		// 退職給与処理処理に必要な画面データ情報を照会または計算して返す。
		// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
		public EmployeePageInfo getPageInfo() { return pageInfo; }
	}
}
