package erp.employees.service;

import java.sql.Connection;
import java.sql.SQLException;

import erp.employees.dao.EmployeeDao;
import erp.employees.model.Employee;
import jdbc.connection.ConnectionProvider;
import jdbc.JdbcUtil;

// 사원 프로필 사진(이미지 파일) DB 경로 업데이트 및 삭제 처리를 전담하는 서비스
// 사원사진 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 社員写真の業務ルールとデータ変更トランザクションを処理する。
public class EmployeePhotoService {

	// 싱글톤 패턴 적용
	// 同じサービスまたはDAOを共有できるよう、シングルトンインスタンスを生成して管理する。
	private static EmployeePhotoService employeePhotoService = new EmployeePhotoService();

	// 여러 서비스에서 공유할 수 있도록 생성된 싱글톤 인스턴스를 반환한다.
	// 객체 생성을 한 곳으로 제한하여 모든 호출자가 동일한 DAO 인스턴스를 재사용하게 한다.
	// 複数のサービスで共有できるように生成されたシングルトンインスタンスを返す。
	// オブジェクト生成を一箇所へ制限し、すべての呼び出し側が同じDAOインスタンスを再利用できるようにする。
	public static EmployeePhotoService getInstance() {
		return employeePhotoService;
	}

	// 전달받은 값으로 사원사진 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で社員写真オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	private EmployeePhotoService() {
	}

	private EmployeeDao employeeDao = EmployeeDao.getInstance();

	// 사진 등록
	// 선택한 이미지 경로를 검증하여 사원사진의 등록 이미지로 저장한다.
	// 여러 DAO와 입력값을 조합해 화면 또는 다음 업무 단계에서 바로 사용할 수 있는 결과 객체를 만든다.
	// 選択した画像パスを検証し、社員写真の登録画像として保存する。
	// 複数のDAO結果と入力値を組み合わせ、画面または次の業務段階で直ちに使用できる結果オブジェクトを作成する。
	public void uploadPhoto(int empId, String savedFilePath) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);

			Employee employee = employeeDao.selectById(conn, empId);
			if (employee != null) {
				employee.setPhotoPath(savedFilePath);
				employeeDao.update(conn, employee); // 경로 업데이트
			}

			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException("사원 사진 경로 업데이트 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// [사진 삭제] 사원의 DB 사진 경로를 NULL로 초기화
	// 선택되거나 식별된 사진 데이터를 삭제하고 관련 상태를 정리한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 選択または識別された写真データを削除し、関連状態を整理する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	public void deletePhoto(int empId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);

			Employee employee = employeeDao.selectById(conn, empId);
			if (employee != null) {
				employee.setPhotoPath(null); // 경로 비우기
				employeeDao.update(conn, employee);
			}

			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException("사원 사진 삭제 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}
}
