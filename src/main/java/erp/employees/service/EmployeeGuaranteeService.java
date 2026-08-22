package erp.employees.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import erp.employees.dao.EmployeeGuarantorDao;
import erp.employees.dao.EmployeeRecommenderDao;
import erp.employees.dao.EmployeeSuretyInsuranceDao;
import erp.employees.model.EmployeeGuarantor;
import erp.employees.model.EmployeeRecommender;
import erp.employees.model.EmployeeSuretyInsurance;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

// 사원보증 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 社員保証の業務ルールとデータ変更トランザクションを処理する。
public class EmployeeGuaranteeService {

	// 싱글톤 인스턴스 생성
	// 同じサービスまたはDAOを共有できるよう、シングルトンインスタンスを生成して管理する。
	private static EmployeeGuaranteeService instance = new EmployeeGuaranteeService();

	// 싱글톤 접근 메서드
	// 여러 서비스에서 공유할 수 있도록 생성된 싱글톤 인스턴스를 반환한다.
	// 객체 생성을 한 곳으로 제한하여 모든 호출자가 동일한 DAO 인스턴스를 재사용하게 한다.
	// 複数のサービスで共有できるように生成されたシングルトンインスタンスを返す。
	// オブジェクト生成を一箇所へ制限し、すべての呼び出し側が同じDAOインスタンスを再利用できるようにする。
	public static EmployeeGuaranteeService getInstance() {
		return instance;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	// 전달받은 값으로 사원보증 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で社員保証オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	private EmployeeGuaranteeService() {
	}

	private EmployeeRecommenderDao recommenderDao = EmployeeRecommenderDao.getInstance();
	private EmployeeSuretyInsuranceDao suretyInsuranceDao = EmployeeSuretyInsuranceDao.getInstance();
	private EmployeeGuarantorDao guarantorDao = EmployeeGuarantorDao.getInstance();

	// 사원보증 처리에 필요한 추천인를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 社員保証処理に必要な推薦人を照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public EmployeeRecommender getRecommender(int empId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			List<EmployeeRecommender> rows = recommenderDao.selectByEmpId(conn, empId);
			return rows.isEmpty() ? null : rows.get(0);
		} catch (SQLException e) {
			throw new RuntimeException("사원 추천인 조회 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 사원보증 처리에 필요한 신원보증보험를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 社員保証処理に必要な身元保証保険を照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public EmployeeSuretyInsurance getSuretyInsurance(int empId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			List<EmployeeSuretyInsurance> rows = suretyInsuranceDao.selectByEmpId(conn, empId);
			return rows.isEmpty() ? null : rows.get(0);
		} catch (SQLException e) {
			throw new RuntimeException("사원 보증보험 조회 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 사원보증 처리에 필요한 보증인를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 社員保証処理に必要な保証人を照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public EmployeeGuarantor getGuarantor(int empId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			List<EmployeeGuarantor> rows = guarantorDao.selectByEmpId(conn, empId);
			return rows.isEmpty() ? null : rows.get(0);
		} catch (SQLException e) {
			throw new RuntimeException("사원 신원보증인 조회 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// [통합 저장] 추천 및 신원보증 내역을 일괄 갱신합니다. v5 스키마 기준 단일 행 데이터들을 처리
	// 입력값을 검증한 후 보증 목록 데이터를 트랜잭션으로 저장하거나 수정한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 入力値を検証した後、保証一覧データをトランザクションで登録または更新する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	public void saveGuarantees(int empId, EmployeeRecommender recommender, EmployeeSuretyInsurance surety,
			EmployeeGuarantor guarantor) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);

			// 기존 데이터 삭제 후 삽입 (1:1 테이블이더라도 확장성을 위해 Delete & Insert 적용)
			// 入力された内容を新しい業務データとして構成し、関連データとともに登録する。
			recommenderDao.deleteByEmpId(conn, empId);
			suretyInsuranceDao.deleteByEmpId(conn, empId);
			guarantorDao.deleteByEmpId(conn, empId);

			if (recommender != null && recommender.getRecommenderName() != null
					&& !recommender.getRecommenderName().isEmpty()) {
				recommenderDao.insert(conn, recommender);
			}
			if (surety != null && surety.getProviderName() != null && !surety.getProviderName().isEmpty()) {
				suretyInsuranceDao.insert(conn, surety);
			}
			if (guarantor != null && guarantor.getGuarantorName() != null && !guarantor.getGuarantorName().isEmpty()) {
				guarantorDao.insert(conn, guarantor);
			}

			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException("추천 및 보증정보 갱신 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}
}
