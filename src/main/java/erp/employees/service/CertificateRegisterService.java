package erp.employees.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import erp.employees.dao.CertificateIssuanceDao;
import erp.employees.dto.CertificateRegisterItem;
import erp.employees.dto.EmployeePageInfo;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

// 제증명서 발급대장 조회와 삭제 트랜잭션을 처리한다.
// 제증명서등록 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 証明書登録の業務ルールとデータ変更トランザクションを処理する。
public class CertificateRegisterService {
	private final CertificateIssuanceDao certificateDao = CertificateIssuanceDao.getInstance();

	// 제증명서등록 처리에 필요한 등록를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 証明書登録処理に必要な登録を照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public CertificateRegisterResult getRegister(CertificateRegisterCondition condition) {
		try (Connection conn = ConnectionProvider.getConnection()) {
			int totalCount = certificateDao.countByCondition(conn, condition);
			EmployeePageInfo pageInfo = new EmployeePageInfo(totalCount, condition.getPage(), condition.getPageSize());
			condition.setPage(pageInfo.getCurrentPage());
			List<CertificateRegisterItem> certificates = certificateDao.selectRegisterByCondition(conn, condition);
			return new CertificateRegisterResult(certificates, totalCount, pageInfo);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	// 선택되거나 식별된 제증명서등록 데이터를 삭제하고 관련 상태를 정리한다.
	// 하나의 Connection에서 자동 커밋을 해제하고 관련 DAO 작업을 묶어 성공 시 commit, 실패 시 rollback한다.
	// 選択または識別された証明書登録データを削除し、関連状態を整理する。
	// 一つのConnectionで自動コミットを無効化して関連DAO処理をまとめ、成功時はcommit、失敗時はrollbackする。
	public int delete(String deleteMode, String[] certificateIds) {
		if (!"ALL".equals(deleteMode) && !"SELECTED".equals(deleteMode)) {
			throw new IllegalArgumentException("올바르지 않은 삭제 요청입니다.");
		}
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			int deletedCount = 0;
			if ("ALL".equals(deleteMode)) {
				deletedCount = certificateDao.deleteAll(conn);
			} else if (certificateIds != null) {
				for (String id : certificateIds) {
					deletedCount += certificateDao.delete(conn, Integer.parseInt(id));
				}
			}
			conn.commit();
			return deletedCount;
		} catch (SQLException | NumberFormatException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	public static class CertificateRegisterResult {
		private final List<CertificateRegisterItem> certificates;
		private final int totalCount;
		private final EmployeePageInfo pageInfo;
		// 조회 목록과 건수·페이지 정보를 하나의 화면 결과 객체로 초기화한다.
		// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
		// 照会一覧と件数・ページ情報を一つの画面結果オブジェクトとして初期化する。
		// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
		public CertificateRegisterResult(List<CertificateRegisterItem> certificates, int totalCount, EmployeePageInfo pageInfo) {
			this.certificates = certificates;
			this.totalCount = totalCount;
			this.pageInfo = pageInfo;
		}
		// 제증명서등록 처리에 필요한 제증명서 목록를 조회하거나 계산하여 반환한다.
		// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
		// 証明書登録処理に必要な証明書一覧を照会または計算して返す。
		// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
		public List<CertificateRegisterItem> getCertificates() { return certificates; }
		// 제증명서등록 처리에 필요한 합계건수를 조회하거나 계산하여 반환한다.
		// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
		// 証明書登録処理に必要な合計件数を照会または計算して返す。
		// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
		public int getTotalCount() { return totalCount; }
		// 제증명서등록 처리에 필요한 화면 데이터정보를 조회하거나 계산하여 반환한다.
		// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
		// 証明書登録処理に必要な画面データ情報を照会または計算して返す。
		// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
		public EmployeePageInfo getPageInfo() { return pageInfo; }
	}
}
