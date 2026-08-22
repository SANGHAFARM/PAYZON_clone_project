package erp.settings.service;

import java.sql.Connection;
import java.sql.SQLException;

import erp.settings.dao.CompanyDao;
import erp.settings.model.Company;
import jdbc.connection.ConnectionProvider; // 커넥션 풀 제공자
import jdbc.JdbcUtil; // 자원 반환/롤백 유틸리티

// 사업장정보 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 事業所情報の業務ルールとデータ変更トランザクションを処理する。
public class CompanyInfoService {

	// 싱글톤 인스턴스 생성
	// 同じサービスまたはDAOを共有できるよう、シングルトンインスタンスを生成して管理する。
	private static CompanyInfoService companyInfoService = new CompanyInfoService();

	// 싱글톤 접근 메서드
	// 여러 서비스에서 공유할 수 있도록 생성된 싱글톤 인스턴스를 반환한다.
	// 객체 생성을 한 곳으로 제한하여 모든 호출자가 동일한 DAO 인스턴스를 재사용하게 한다.
	// 複数のサービスで共有できるように生成されたシングルトンインスタンスを返す。
	// オブジェクト生成を一箇所へ制限し、すべての呼び出し側が同じDAOインスタンスを再利用できるようにする。
	public static CompanyInfoService getInstance() {
		return companyInfoService;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	// 전달받은 값으로 사업장정보 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で事業所情報オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	private CompanyInfoService() {
	}

	private CompanyDao companyDao = CompanyDao.getInstance();

	/**
	 * [화면 로딩] 회사 기본환경설정 데이터 조회 페이지 진입 시 등록된 회사 정보를 불러와 input/select 박스에 바인딩하기 위해 사용
	  * 画面表示に必要なデータとメッセージをrequestまたはsessionへ保存し、JSPへ引き渡す。
	 */
	// 사업장정보 처리에 필요한 사업장Details를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 事業所情報処理に必要な事業所Detailsを照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public Company getCompanyDetails(int companyId) {
		Connection conn = null;
		try {
			// ConnectionProvider를 통해 커넥션 풀에서 커넥션 획득
			// DB接続を再利用するコネクションプールとファクトリーを構成し、JDBCドライバーへ登録する。
			conn = ConnectionProvider.getConnection();
			return companyDao.selectById(conn, companyId);
		} catch (SQLException e) {
			throw new RuntimeException("회사 정보 조회 중 오류가 발생했습니다.", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	/**
	 * [저장하기] 회사정보 폼 전체 저장 (btnAllSave 클릭 시) 회사, 담당자, 급여지급 탭의 모든 내용을 하나의 트랜잭션으로 덮어쓰기
	  * 複数のデータ変更を一つのトランザクションとして処理し、成功時はコミット、失敗時はロールバックする。
	 */
	// 입력값을 검증한 후 전체사업장정보 데이터를 트랜잭션으로 저장하거나 수정한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 入力値を検証した後、全体事業所情報データをトランザクションで登録または更新する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	public void saveAllCompanyInfo(Company company) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false); // 트랜잭션 시작

			// 기존 데이터 존재 여부 확인 후 INSERT 또는 UPDATE 분기 처리
			// 入力条件と必須値を検証し、不正なデータが後続処理へ渡らないようにする。
			Company existingCompany = companyDao.selectById(conn, company.getCompanyId());

			if (existingCompany == null) {
				companyDao.insert(conn, company);
			} else {
				companyDao.update(conn, company);
			}

			conn.commit(); // 정상 처리 시 커밋
		} catch (SQLException e) {
			JdbcUtil.rollback(conn); // 에러 발생 시 롤백
			throw new RuntimeException("회사 정보 저장 중 오류가 발생했습니다.", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}
}
