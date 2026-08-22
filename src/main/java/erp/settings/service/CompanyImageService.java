package erp.settings.service;

import java.sql.Connection;
import java.sql.SQLException;

import erp.settings.dao.CompanyDao;
import erp.settings.model.Company;
import jdbc.connection.ConnectionProvider;
import jdbc.JdbcUtil;

// 사업장이미지 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 事業所画像の業務ルールとデータ変更トランザクションを処理する。
public class CompanyImageService {

	// 싱글톤 인스턴스 생성
	// 同じサービスまたはDAOを共有できるよう、シングルトンインスタンスを生成して管理する。
	private static CompanyImageService companyImageService = new CompanyImageService();

	// 싱글톤 접근 메서드
	// 여러 서비스에서 공유할 수 있도록 생성된 싱글톤 인스턴스를 반환한다.
	// 객체 생성을 한 곳으로 제한하여 모든 호출자가 동일한 DAO 인스턴스를 재사용하게 한다.
	// 複数のサービスで共有できるように生成されたシングルトンインスタンスを返す。
	// オブジェクト生成を一箇所へ制限し、すべての呼び出し側が同じDAOインスタンスを再利用できるようにする。
	public static CompanyImageService getInstance() {
		return companyImageService;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	// 전달받은 값으로 사업장이미지 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で事業所画像オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	private CompanyImageService() {
	}

	private CompanyDao companyDao = CompanyDao.getInstance();

	/**
	 * [이미지 등록] 로고 또는 도장 이미지 경로 DB 업데이트 컨트롤러에서 물리적 파일 저장(가로 150px 썸네일 변환 등)을 완료한 후,
	 * 최종 웹 접근 경로(savedFilePath)를 DB에 기록
	  * 選択された画像識別値と保存パスを確認し、登録済み画像を各画面で再利用できるようにする。
	 */
	// 선택한 이미지 경로를 검증하여 사업장이미지의 등록 이미지로 저장한다.
	// 여러 DAO와 입력값을 조합해 화면 또는 다음 업무 단계에서 바로 사용할 수 있는 결과 객체를 만든다.
	// 選択した画像パスを検証し、事業所画像の登録画像として保存する。
	// 複数のDAO結果と入力値を組み合わせ、画面または次の業務段階で直ちに使用できる結果オブジェクトを作成する。
	public void uploadImage(int companyId, String imageType, String savedFilePath) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);

			Company company = companyDao.selectById(conn, companyId);
			if (company != null) {
				// 타입에 따라 해당하는 필드 업데이트
				// 業務処理で共有する値をフィールドへ保持し、必要な階層から参照・変更できるようにする。
				if ("logo".equals(imageType)) {
					company.setLogoImgPath(savedFilePath);
				} else if ("stamp".equals(imageType)) {
					company.setStampImgPath(savedFilePath);
				}
				companyDao.update(conn, company);
			}

			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException("이미지 경로 업데이트 중 오류가 발생했습니다.", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	/**
	 * [이미지 삭제] 로고 또는 도장 이미지 DB 경로 초기화 (btnLogoDel, btnStampDel)
	  * 選択された画像識別値と保存パスを確認し、登録済み画像を各画面で再利用できるようにする。
	 */
	// 선택되거나 식별된 이미지 데이터를 삭제하고 관련 상태를 정리한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 選択または識別された画像データを削除し、関連状態を整理する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	public void deleteImage(int companyId, String imageType) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);

			Company company = companyDao.selectById(conn, companyId);
			if (company != null) {
				// 삭제 시 경로를 비워줌
				// 選択された画像識別値と保存パスを確認し、登録済み画像を各画面で再利用できるようにする。
				if ("logo".equals(imageType)) {
					company.setLogoImgPath(null);
				} else if ("stamp".equals(imageType)) {
					company.setStampImgPath(null);
				}
				companyDao.update(conn, company);
			}

			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException("이미지 삭제 중 오류가 발생했습니다.", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}
}
