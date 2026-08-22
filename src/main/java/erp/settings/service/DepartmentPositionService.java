package erp.settings.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import erp.settings.dao.DepartmentDao;
import erp.settings.dao.JobPositionDao;
import erp.settings.model.Department;
import erp.settings.model.JobPosition;
import jdbc.connection.ConnectionProvider;
import jdbc.JdbcUtil;

// 부서직위 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 部署役職の業務ルールとデータ変更トランザクションを処理する。
public class DepartmentPositionService {

	// 싱글톤 인스턴스 생성
	// 同じサービスまたはDAOを共有できるよう、シングルトンインスタンスを生成して管理する。
	private static DepartmentPositionService departmentPositionService = new DepartmentPositionService();

	// 싱글톤 접근 메서드
	// 여러 서비스에서 공유할 수 있도록 생성된 싱글톤 인스턴스를 반환한다.
	// 객체 생성을 한 곳으로 제한하여 모든 호출자가 동일한 DAO 인스턴스를 재사용하게 한다.
	// 複数のサービスで共有できるように生成されたシングルトンインスタンスを返す。
	// オブジェクト生成を一箇所へ制限し、すべての呼び出し側が同じDAOインスタンスを再利用できるようにする。
	public static DepartmentPositionService getInstance() {
		return departmentPositionService;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	// 전달받은 값으로 부서직위 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で部署役職オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	private DepartmentPositionService() {
	}

	private DepartmentDao departmentDao = DepartmentDao.getInstance();
	private JobPositionDao jobPositionDao = JobPositionDao.getInstance();

	/**
	 * [목록 조회] 부서 설정 팝업 및 Select Box 바인딩용
	  * 社員の識別情報と在職・雇用・所属条件を確認し、対象社員データへ反映する。
	 */
	// 부서직위 처리에 필요한 부서Options를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 部署役職処理に必要な部署Optionsを照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public List<Department> getDepartmentOptions() {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			return departmentDao.selectAll(conn);
		} catch (SQLException e) {
			throw new RuntimeException("부서 목록 조회 중 에러", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	/**
	 * [목록 조회] 직위 설정 팝업 및 Select Box 바인딩용
	  * 社員の識別情報と在職・雇用・所属条件を確認し、対象社員データへ反映する。
	 */
	// 부서직위 처리에 필요한 직무직위Options를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 部署役職処理に必要な職務役職Optionsを照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public List<JobPosition> getJobPositionOptions() {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			return jobPositionDao.selectAll(conn);
		} catch (SQLException e) {
			throw new RuntimeException("직위 목록 조회 중 에러", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	/**
	 * [추가] 새로운 부서 등록
	  * 社員の識別情報と在職・雇用・所属条件を確認し、対象社員データへ反映する。
	 */
	// 부서직위 처리에 사용할 부서 데이터나 객체를 생성한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 部署役職処理で使用する部署データまたはオブジェクトを生成する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	public void addDepartment(Department dept) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			validateDepartmentName(conn, dept.getDepartmentName(), 0);
			departmentDao.insert(conn, dept);
			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException("부서를 등록하지 못했습니다.", e);
		} catch (RuntimeException e) {
			JdbcUtil.rollback(conn);
			throw e;
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 사원등록에서 전달된 부서 ID가 실제 설정 테이블에 존재하는지 확인합니다.
	// 요청된 부서 또는 직위 식별번호가 실제 기준정보에 존재하는지 확인한다.
	// 여러 DAO와 입력값을 조합해 화면 또는 다음 업무 단계에서 바로 사용할 수 있는 결과 객체를 만든다.
	// 指定された部署または役職の識別番号がマスター情報に存在するか確認する。
	// 複数のDAO結果と入力値を組み合わせ、画面または次の業務段階で直ちに使用できる結果オブジェクトを作成する。
	public int requireDepartmentId(String value) {
		Connection conn = null;
		try {
			int departmentId = Integer.parseInt(value);
			conn = ConnectionProvider.getConnection();
			if (departmentDao.selectById(conn, departmentId) == null) {
				throw new IllegalArgumentException("선택한 부서가 존재하지 않습니다.");
			}
			return departmentId;
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("부서를 다시 선택해주세요.");
		} catch (SQLException e) {
			throw new RuntimeException("부서 확인 중 에러", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 사원등록에서 전달된 직위 ID가 실제 설정 테이블에 존재하는지 확인합니다.
	// 요청된 부서 또는 직위 식별번호가 실제 기준정보에 존재하는지 확인한다.
	// 여러 DAO와 입력값을 조합해 화면 또는 다음 업무 단계에서 바로 사용할 수 있는 결과 객체를 만든다.
	// 指定された部署または役職の識別番号がマスター情報に存在するか確認する。
	// 複数のDAO結果と入力値を組み合わせ、画面または次の業務段階で直ちに使用できる結果オブジェクトを作成する。
	public int requireJobPositionId(String value) {
		Connection conn = null;
		try {
			int positionId = Integer.parseInt(value);
			conn = ConnectionProvider.getConnection();
			if (jobPositionDao.selectById(conn, positionId) == null) {
				throw new IllegalArgumentException("선택한 직위가 존재하지 않습니다.");
			}
			return positionId;
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("직위를 다시 선택해주세요.");
		} catch (SQLException e) {
			throw new RuntimeException("직위 확인 중 에러", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 등록된 부서명을 수정한다.
	// 입력값을 검증한 후 부서 데이터를 트랜잭션으로 저장하거나 수정한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 入力値を検証した後、部署データをトランザクションで登録または更新する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	public void updateDepartment(Department dept) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			validateDepartmentName(conn, dept.getDepartmentName(), dept.getDepartmentId());
			if (departmentDao.update(conn, dept) == 0) {
				throw new IllegalArgumentException("수정할 부서가 없습니다.");
			}
			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException("부서를 수정하지 못했습니다.", e);
		} catch (RuntimeException e) {
			JdbcUtil.rollback(conn);
			throw e;
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 다른 데이터에서 사용하지 않는 부서를 삭제한다.
	// 선택되거나 식별된 부서 데이터를 삭제하고 관련 상태를 정리한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 選択または識別された部署データを削除し、関連状態を整理する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	public void deleteDepartment(int departmentId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			if (departmentDao.delete(conn, departmentId) == 0) {
				throw new IllegalArgumentException("삭제할 부서가 없습니다.");
			}
			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException("사용 중인 부서는 삭제할 수 없습니다.", e);
		} catch (RuntimeException e) {
			JdbcUtil.rollback(conn);
			throw e;
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 새로운 직위를 등록한다.
	// 부서직위 처리에 사용할 직무직위 데이터나 객체를 생성한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 部署役職処理で使用する職務役職データまたはオブジェクトを生成する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	public void addJobPosition(JobPosition position) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			validatePositionName(conn, position.getJobPositionName(), 0);
			jobPositionDao.insert(conn, position);
			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException("직위를 등록하지 못했습니다.", e);
		} catch (RuntimeException e) {
			JdbcUtil.rollback(conn);
			throw e;
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 등록된 직위명을 수정한다.
	// 입력값을 검증한 후 직무직위 데이터를 트랜잭션으로 저장하거나 수정한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 入力値を検証した後、職務役職データをトランザクションで登録または更新する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	public void updateJobPosition(JobPosition position) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			validatePositionName(conn, position.getJobPositionName(), position.getJobPositionId());
			if (jobPositionDao.update(conn, position) == 0) {
				throw new IllegalArgumentException("수정할 직위가 없습니다.");
			}
			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException("직위를 수정하지 못했습니다.", e);
		} catch (RuntimeException e) {
			JdbcUtil.rollback(conn);
			throw e;
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 다른 데이터에서 사용하지 않는 직위를 삭제한다.
	// 선택되거나 식별된 직무직위 데이터를 삭제하고 관련 상태를 정리한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 選択または識別された職務役職データを削除し、関連状態を整理する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	public void deleteJobPosition(int positionId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			if (jobPositionDao.delete(conn, positionId) == 0) {
				throw new IllegalArgumentException("삭제할 직위가 없습니다.");
			}
			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException("사용 중인 직위는 삭제할 수 없습니다.", e);
		} catch (RuntimeException e) {
			JdbcUtil.rollback(conn);
			throw e;
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 부서명칭 입력값과 업무 처리 가능 여부를 검증한다.
	// 필수값과 상태 조합을 확인해 잘못된 데이터가 DAO 또는 후속 계산으로 전달되지 않도록 차단한다.
	// 部署名称の入力値と業務処理の可否を検証する。
	// 必須値と状態の組み合わせを確認し、不正データがDAOまたは後続計算へ渡らないよう遮断する。
	private void validateDepartmentName(Connection conn, String name, int currentId) throws SQLException {
		String normalizedName = normalizeName(name, "부서명을 입력해주세요.");
		for (Department department : departmentDao.selectAll(conn)) {
			if (department.getDepartmentId() != currentId
					&& normalizedName.equalsIgnoreCase(department.getDepartmentName())) {
				throw new IllegalArgumentException("이미 등록된 부서명입니다.");
			}
		}
	}

	// 직위명칭 입력값과 업무 처리 가능 여부를 검증한다.
	// 필수값과 상태 조합을 확인해 잘못된 데이터가 DAO 또는 후속 계산으로 전달되지 않도록 차단한다.
	// 役職名称の入力値と業務処理の可否を検証する。
	// 必須値と状態の組み合わせを確認し、不正データがDAOまたは後続計算へ渡らないよう遮断する。
	private void validatePositionName(Connection conn, String name, int currentId) throws SQLException {
		String normalizedName = normalizeName(name, "직위명을 입력해주세요.");
		for (JobPosition position : jobPositionDao.selectAll(conn)) {
			if (position.getJobPositionId() != currentId
					&& normalizedName.equalsIgnoreCase(position.getJobPositionName())) {
				throw new IllegalArgumentException("이미 등록된 직위명입니다.");
			}
		}
	}

	// 요청 문자열을 정리하고 정규화명칭 처리에 필요한 안전한 값으로 변환한다.
	// 여러 DAO와 입력값을 조합해 화면 또는 다음 업무 단계에서 바로 사용할 수 있는 결과 객체를 만든다.
	// リクエスト文字列を整え、正規化名称処理に必要な安全な値へ変換する。
	// 複数のDAO結果と入力値を組み合わせ、画面または次の業務段階で直ちに使用できる結果オブジェクトを作成する。
	private String normalizeName(String name, String emptyMessage) {
		String normalizedName = name == null ? "" : name.trim();
		if (normalizedName.isEmpty()) {
			throw new IllegalArgumentException(emptyMessage);
		}
		if (normalizedName.length() > 100) {
			throw new IllegalArgumentException("이름은 100자 이하로 입력해주세요.");
		}
		return normalizedName;
	}
}
