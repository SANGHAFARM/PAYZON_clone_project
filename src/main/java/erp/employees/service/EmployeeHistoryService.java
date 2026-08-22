package erp.employees.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import erp.employees.dao.EmployeeCareerDao;
import erp.employees.dao.EmployeeDependentDao;
import erp.employees.dao.EmployeeEducationDao;
import erp.employees.dao.EmployeeInsuranceHistoryDao;
import erp.employees.model.EmployeeCareer;
import erp.employees.model.EmployeeDependent;
import erp.employees.model.EmployeeEducation;
import erp.employees.model.EmployeeInsuranceHistory;
import jdbc.connection.ConnectionProvider;
import jdbc.JdbcUtil;

// 사원 하위에 1:N으로 달리는 부양가족, 학력, 경력, 4대보험 이력을 통합 관리하는 서비스
// 사원이력 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 社員履歴の業務ルールとデータ変更トランザクションを処理する。
public class EmployeeHistoryService {

	// 싱글톤 패턴 적용
	// 同じサービスまたはDAOを共有できるよう、シングルトンインスタンスを生成して管理する。
	private static EmployeeHistoryService employeeHistoryService = new EmployeeHistoryService();

	// 여러 서비스에서 공유할 수 있도록 생성된 싱글톤 인스턴스를 반환한다.
	// 객체 생성을 한 곳으로 제한하여 모든 호출자가 동일한 DAO 인스턴스를 재사용하게 한다.
	// 複数のサービスで共有できるように生成されたシングルトンインスタンスを返す。
	// オブジェクト生成を一箇所へ制限し、すべての呼び出し側が同じDAOインスタンスを再利用できるようにする。
	public static EmployeeHistoryService getInstance() {
		return employeeHistoryService;
	}

	// 전달받은 값으로 사원이력 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で社員履歴オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	private EmployeeHistoryService() {
	}

	// 1:N 하위 테이블을 담당하는 DAO들
	// 社員一人に複数件登録できる履歴テーブルごとのDAOを共有し、詳細照会と保存で使用する。
	private EmployeeDependentDao dependentDao = EmployeeDependentDao.getInstance();
	private EmployeeEducationDao educationDao = EmployeeEducationDao.getInstance();
	private EmployeeCareerDao careerDao = EmployeeCareerDao.getInstance();
	private EmployeeInsuranceHistoryDao insuranceDao = EmployeeInsuranceHistoryDao.getInstance();

	// [조회] 사원번호(EMPLOYEE_ID)를 기준으로 부양가족 목록을 조회
	// 사원이력 처리에 필요한 부양가족 목록를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 社員履歴処理に必要な扶養家族一覧を照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public List<EmployeeDependent> getDependents(int empId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			return dependentDao.selectByEmpId(conn, empId);
		} catch (SQLException e) {
			throw new RuntimeException("사원 부양가족 목록 조회 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// [조회] 사원번호(EMPLOYEE_ID)를 기준으로 학력 목록을 조회
	// 사원이력 처리에 필요한 학력 목록를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 社員履歴処理に必要な学歴一覧を照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public List<EmployeeEducation> getEducations(int empId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			return educationDao.selectByEmpId(conn, empId);
		} catch (SQLException e) {
			throw new RuntimeException("사원 학력 목록 조회 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// [조회] 사원번호(EMPLOYEE_ID)를 기준으로 경력 목록을 조회
	// 사원이력 처리에 필요한 Careers를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 社員履歴処理に必要なCareersを照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public List<EmployeeCareer> getCareers(int empId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			return careerDao.selectByEmpId(conn, empId);
		} catch (SQLException e) {
			throw new RuntimeException("사원 경력 목록 조회 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// [통합 저장] 폼에서 넘어온 1:N 리스트 데이터 일괄 갱신 (action="save" 시 호출)
	// 입력값을 검증한 후 전체이력 목록 데이터를 트랜잭션으로 저장하거나 수정한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 入力値を検証した後、全体履歴一覧データをトランザクションで登録または更新する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	public void saveAllHistories(int empId, List<EmployeeDependent> deps, List<EmployeeEducation> edus,
			List<EmployeeCareer> cars, List<EmployeeInsuranceHistory> insurances) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false); // 하나의 거대한 트랜잭션으로 묶기

			// 1. 기존 이력 일괄 삭제 (사원번호 기준)
			// 社員の識別情報と在職・雇用・所属条件を確認し、対象社員データへ反映する。
			dependentDao.deleteByEmpId(conn, empId);
			educationDao.deleteByEmpId(conn, empId);
			careerDao.deleteByEmpId(conn, empId);
			insuranceDao.deleteByEmpId(conn, empId);

			// 2. 화면에서 넘어온 새로운 리스트 일괄 추가 (반복문 사용)
			// 画面表示に必要なデータとメッセージをrequestまたはsessionへ保存し、JSPへ引き渡す。
			if (deps != null) {
				for (EmployeeDependent dep : deps) {
					dependentDao.insert(conn, dep);
				}
			}
			if (edus != null) {
				for (EmployeeEducation edu : edus) {
					educationDao.insert(conn, edu);
				}
			}
			if (cars != null) {
				for (EmployeeCareer car : cars) {
					careerDao.insert(conn, car);
				}
			}
			if (insurances != null) {
				for (EmployeeInsuranceHistory ins : insurances) {
					insuranceDao.insert(conn, ins);
				}
			}

			conn.commit(); // 모두 성공 시 커밋
		} catch (SQLException e) {
			JdbcUtil.rollback(conn); // 단 하나라도 실패하면 롤백 (이력 꼬임 방지)
			throw new RuntimeException("사원 이력 정보 일괄 갱신 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// [선택 삭제] 화면에서 체크박스 선택 후 "선택삭제" 버튼 클릭 시 개별 삭제 수행
	// 선택되거나 식별된 Selected항목 목록 데이터를 삭제하고 관련 상태를 정리한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 選択または識別されたSelected項目一覧データを削除し、関連状態を整理する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	public void deleteSelectedItems(String type, List<Integer> deleteIds) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);

			for (Integer id : deleteIds) {
				switch (type) {
				case "dependent":
					dependentDao.delete(conn, id);
					break;
				case "education":
					educationDao.delete(conn, id);
					break;
				case "career":
					careerDao.delete(conn, id);
					break;
				}
			}
			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException("선택 항목 삭제 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// [조회] 사원번호(EMPLOYEE_ID)를 기준으로 4대보험 취득/상실 이력 목록을 조회
	// 사원이력 처리에 필요한 보험이력 목록를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 社員履歴処理に必要な保険履歴一覧を照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public List<EmployeeInsuranceHistory> getInsuranceHistories(int empId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			return insuranceDao.selectByEmpId(conn, empId);
		} catch (SQLException e) {
			throw new RuntimeException("사원 4대보험 이력 조회 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}
}
