package erp.employees.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import erp.employees.dao.EmployeeAppointmentDao;
import erp.employees.dao.EmployeeLanguageDao;
import erp.employees.dao.EmployeeLicenseDao;
import erp.employees.dao.EmployeeRewardDisciplineDao;
import erp.employees.dao.EmployeeTrainingDao;
import erp.employees.model.EmployeeAppointment;
import erp.employees.model.EmployeeLanguage;
import erp.employees.model.EmployeeLicense;
import erp.employees.model.EmployeeRewardDiscipline;
import erp.employees.model.EmployeeTraining;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

// 사원Skill기록 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 社員Skill記録の業務ルールとデータ変更トランザクションを処理する。
public class EmployeeSkillRecordService {

	// 싱글톤 인스턴스 생성
	// 同じサービスまたはDAOを共有できるよう、シングルトンインスタンスを生成して管理する。
	private static EmployeeSkillRecordService instance = new EmployeeSkillRecordService();

	// 싱글톤 접근 메서드
	// 여러 서비스에서 공유할 수 있도록 생성된 싱글톤 인스턴스를 반환한다.
	// 객체 생성을 한 곳으로 제한하여 모든 호출자가 동일한 DAO 인스턴스를 재사용하게 한다.
	// 複数のサービスで共有できるように生成されたシングルトンインスタンスを返す。
	// オブジェクト生成を一箇所へ制限し、すべての呼び出し側が同じDAOインスタンスを再利用できるようにする。
	public static EmployeeSkillRecordService getInstance() {
		return instance;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	// 전달받은 값으로 사원Skill기록 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で社員Skill記録オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	private EmployeeSkillRecordService() {
	}

	// 각 1:N 하위 테이블을 담당하는 DAO 객체들 (동일한 싱글톤 패턴 적용 가정)
	// 同じサービスまたはDAOを共有できるよう、シングルトンインスタンスを生成して管理する。
	private EmployeeLicenseDao licenseDao = EmployeeLicenseDao.getInstance();
	private EmployeeLanguageDao languageDao = EmployeeLanguageDao.getInstance();
	private EmployeeTrainingDao trainingDao = EmployeeTrainingDao.getInstance();
	private EmployeeRewardDisciplineDao rewardDao = EmployeeRewardDisciplineDao.getInstance();
	private EmployeeAppointmentDao appointmentDao = EmployeeAppointmentDao.getInstance();

	// [조회] 사원번호(empId)로 각 역량 및 인사기록 리스트를 조회
	// 사원Skill기록 처리에 필요한 자격증 목록를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 社員Skill記録処理に必要な資格一覧を照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public List<EmployeeLicense> getLicenses(int empId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			return licenseDao.selectByEmpId(conn, empId);
		} catch (SQLException e) {
			throw new RuntimeException("사원 자격/면허 조회 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 사원Skill기록 처리에 필요한 어학 목록를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 社員Skill記録処理に必要な語学一覧を照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public List<EmployeeLanguage> getLanguages(int empId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			return languageDao.selectByEmpId(conn, empId);
		} catch (SQLException e) {
			throw new RuntimeException("사원 어학능력 조회 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 사원Skill기록 처리에 필요한 교육훈련 목록를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 社員Skill記録処理に必要な教育訓練一覧を照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public List<EmployeeTraining> getTrainings(int empId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			return trainingDao.selectByEmpId(conn, empId);
		} catch (SQLException e) {
			throw new RuntimeException("사원 교육/훈련 조회 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 사원Skill기록 처리에 필요한 상벌Punishes를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 社員Skill記録処理に必要な賞罰Punishesを照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public List<EmployeeRewardDiscipline> getRewardPunishes(int empId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			return rewardDao.selectByEmpId(conn, empId);
		} catch (SQLException e) {
			throw new RuntimeException("사원 상벌 조회 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// 사원Skill기록 처리에 필요한 발령 목록를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 社員Skill記録処理に必要な発令一覧を照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public List<EmployeeAppointment> getAppointments(int empId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			return appointmentDao.selectByEmpId(conn, empId);
		} catch (SQLException e) {
			throw new RuntimeException("사원 발령 조회 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// [통합 저장] 폼에서 넘어온 1:N 리스트 데이터를 하나의 트랜잭션으로 일괄 갱신
	// 입력값을 검증한 후 전체SkillRecords 데이터를 트랜잭션으로 저장하거나 수정한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 入力値を検証した後、全体SkillRecordsデータをトランザクションで登録または更新する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	public void saveAllSkillRecords(int empId, List<EmployeeLicense> licenses, List<EmployeeLanguage> languages,
			List<EmployeeTraining> trainings, List<EmployeeRewardDiscipline> rewards,
			List<EmployeeAppointment> appointments) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false); // 트랜잭션 시작

			// 1. 기존 데이터 일괄 삭제
			// 選択された対象と関連範囲を確認して削除し、残りの一覧状態を再構成する。
			licenseDao.deleteByEmpId(conn, empId);
			languageDao.deleteByEmpId(conn, empId);
			trainingDao.deleteByEmpId(conn, empId);
			rewardDao.deleteByEmpId(conn, empId);
			appointmentDao.deleteByEmpId(conn, empId);

			// 2. 화면에서 넘어온 새로운 리스트 일괄 추가
			// 画面表示に必要なデータとメッセージをrequestまたはsessionへ保存し、JSPへ引き渡す。
			if (licenses != null) {
				for (EmployeeLicense item : licenses)
					licenseDao.insert(conn, item);
			}
			if (languages != null) {
				for (EmployeeLanguage item : languages)
					languageDao.insert(conn, item);
			}
			if (trainings != null) {
				for (EmployeeTraining item : trainings)
					trainingDao.insert(conn, item);
			}
			if (rewards != null) {
				for (EmployeeRewardDiscipline item : rewards)
					rewardDao.insert(conn, item);
			}
			if (appointments != null) {
				for (EmployeeAppointment item : appointments)
					appointmentDao.insert(conn, item);
			}

			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException("사원 역량 및 인사기록 일괄 갱신 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// [선택 삭제] 화면에서 체크박스 선택 후 삭제 버튼 클릭 시 개별 삭제를 수행
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
				case "license":
					licenseDao.delete(conn, id);
					break;
				case "language":
					languageDao.delete(conn, id);
					break;
				case "training":
					trainingDao.delete(conn, id);
					break;
				case "reward":
					rewardDao.delete(conn, id);
					break;
				case "appointment":
					appointmentDao.delete(conn, id);
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
}
