package erp.employees.service;

import java.sql.Connection;
import java.sql.SQLException;

import erp.employees.dao.EmployeeDao;
import erp.employees.model.Employee;
import jdbc.connection.ConnectionProvider;
import jdbc.JdbcUtil;

// 사원 기본정보, 급여/4대보험 설정, 병역사항 등 1:1 핵심 데이터를 관리하는 서비스
// 사원등록 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 社員登録の業務ルールとデータ変更トランザクションを処理する。
public class EmployeeRegisterService {

	// 싱글톤 패턴 적용
	// 同じサービスまたはDAOを共有できるよう、シングルトンインスタンスを生成して管理する。
	private static EmployeeRegisterService employeeRegisterService = new EmployeeRegisterService();

	// 여러 서비스에서 공유할 수 있도록 생성된 싱글톤 인스턴스를 반환한다.
	// 객체 생성을 한 곳으로 제한하여 모든 호출자가 동일한 DAO 인스턴스를 재사용하게 한다.
	// 複数のサービスで共有できるように生成されたシングルトンインスタンスを返す。
	// オブジェクト生成を一箇所へ制限し、すべての呼び出し側が同じDAOインスタンスを再利用できるようにする。
	public static EmployeeRegisterService getInstance() {
		return employeeRegisterService;
	}

	// 전달받은 값으로 사원등록 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で社員登録オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	private EmployeeRegisterService() {
	}

	private EmployeeDao employeeDao = EmployeeDao.getInstance();

	// [조회] 사원 기본 프로필 정보 가져오기 (화면 렌더링용)
	// 사원등록 처리에 필요한 사원BasicProfile를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 社員登録処理に必要な社員BasicProfileを照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public Employee getEmployeeBasicProfile(int empId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			return employeeDao.selectById(conn, empId);
		} catch (SQLException e) {
			throw new RuntimeException("사원 기본정보 조회 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// [저장] 사원 기본정보 통합 저장
	// 입력값을 검증한 후 사원Basic정보 데이터를 트랜잭션으로 저장하거나 수정한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 入力値を検証した後、社員Basic情報データをトランザクションで登録または更新する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	public int saveEmployeeBasicInfo(Employee employee) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false); // 트랜잭션 시작

			// 기존 사원 존재 여부 확인
			// 入力条件と必須値を検証し、不正なデータが後続処理へ渡らないようにする。
			Employee existingEmp = employee.getEmployeeId() > 0
					? employeeDao.selectById(conn, employee.getEmployeeId()) : null;

			if (existingEmp == null) {
				// PK를 먼저 확보해 사원번호와 모든 하위 이력이 동일한 사원을 가리키게 합니다.
				// データを一意に識別するキーを発行または適用し、対象レコードを正確に処理する。
				int employeeId = employee.getEmployeeId() > 0
						? employee.getEmployeeId() : employeeDao.nextEmployeeId(conn);
				employee.setEmployeeId(employeeId);
				employee.setEmpNo(createEmployeeNumber(employeeId));
				employeeDao.insert(conn, employee);
			} else {
				// 외부 사원번호와 사진은 별도 관리 항목이므로 일반 정보 수정 시 기존 값을 유지합니다.
				// 社員の識別情報と在職・雇用・所属条件を確認し、対象社員データへ反映する。
				employee.setEmpNo(existingEmp.getEmpNo());
				employee.setPhotoPath(existingEmp.getPhotoPath());
				employeeDao.update(conn, employee);
			}

			conn.commit();
			return employee.getEmployeeId();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException("사원 기본정보 저장 중 오류 발생", e);
		} catch (RuntimeException e) {
			JdbcUtil.rollback(conn);
			throw e;
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 신규 화면에서 예상 사원번호를 보여주기 위해 실제 시퀀스 값을 미리 예약합니다.
	// 신규 사원과 하위 이력이 동일한 키를 사용하도록 사원 식별번호를 미리 발급한다.
	// 여러 DAO와 입력값을 조합해 화면 또는 다음 업무 단계에서 바로 사용할 수 있는 결과 객체를 만든다.
	// 新規社員と下位履歴が同じキーを使用できるよう社員識別番号を事前発行する。
	// 複数のDAO結果と入力値を組み合わせ、画面または次の業務段階で直ちに使用できる結果オブジェクトを作成する。
	public int reserveEmployeeId() {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			return employeeDao.nextEmployeeId(conn);
		} catch (SQLException e) {
			throw new RuntimeException("사원번호 예약 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 사원등록 처리에 필요한 사원번호Preview를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 社員登録処理に必要な社員番号Previewを照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public String getEmployeeNumberPreview(int employeeId) {
		return createEmployeeNumber(employeeId);
	}

	// 1로 시작하는 7자리 사원번호이며 뒤 6자리는 내부 식별번호와 함께 증가합니다.
	// 사원등록 처리에 사용할 사원번호 데이터나 객체를 생성한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 社員登録処理で使用する社員番号データまたはオブジェクトを生成する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	private String createEmployeeNumber(int employeeId) {
		if (employeeId < 0 || employeeId > 999999) {
			throw new IllegalStateException("사원번호 생성 범위를 초과했습니다.");
		}
		return String.format("1%06d", employeeId);
	}
}
