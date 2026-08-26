package erp.settings.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import erp.settings.dao.AttendanceGroupDao;
import erp.settings.dao.AttendanceItemDao;
import erp.settings.dao.LeaveItemDao;
import erp.settings.dto.AttendanceGroupWithItemsDto;
import erp.settings.model.AttendanceGroup;
import erp.settings.model.AttendanceItem;
import erp.settings.model.LeaveItem;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

// 근태설정 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 勤怠設定の業務ルールとデータ変更トランザクションを処理する。
public class AttendanceSettingService {

	// 싱글톤 인스턴스 생성
	// 同じサービスまたはDAOを共有できるよう、シングルトンインスタンスを生成して管理する。
	private static AttendanceSettingService instance = new AttendanceSettingService();

	// 싱글톤 접근 메서드
	// 여러 서비스에서 공유할 수 있도록 생성된 싱글톤 인스턴스를 반환한다.
	// 객체 생성을 한 곳으로 제한하여 모든 호출자가 동일한 DAO 인스턴스를 재사용하게 한다.
	// 複数のサービスで共有できるように生成されたシングルトンインスタンスを返す。
	// オブジェクト生成を一箇所へ制限し、すべての呼び出し側が同じDAOインスタンスを再利用できるようにする。
	public static AttendanceSettingService getInstance() {
		return instance;
	}

	// 외부 객체 생성 방지 처리
	// 전달받은 값으로 근태설정 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で勤怠設定オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	private AttendanceSettingService() {
	}

	// 도메인별 DAO 연동 객체 할당
	// 担当業務を処理するServiceまたはDAOを共有フィールドへ割り当て、各処理から再利用する。
	private LeaveItemDao leaveItemDao = LeaveItemDao.getInstance();
	private AttendanceItemDao attendItemDao = AttendanceItemDao.getInstance();
	private AttendanceGroupDao attendGroupDao = AttendanceGroupDao.getInstance();

	// ==========================================
	// 1. 휴가항목(LEAVE_ITEM) 관련 로직
	// 社員の勤務・休暇記録と適用期間を確認し、勤怠照会または残日数計算へ反映する。
	// ==========================================

	/**
	 * [조회] 휴가항목 전체 리스트 조회
	 *
	 * @return 휴가항목 목록 반환
	  * @return 呼び出し元で使用する処理結果を返す。
	 */
	// 근태설정 처리에 필요한 휴가항목 목록를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 勤怠設定処理に必要な休暇項目一覧を照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public List<LeaveItem> getLeaveItems() {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			return leaveItemDao.selectAll(conn);
		} catch (SQLException e) {
			throw new RuntimeException("휴가항목 목록 조회 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	/**
	 * [조회] 특정 휴가항목 단건 조회 (에디터 패널 표시용)
	  * 識別番号に該当する一件の詳細データを照会し、編集・詳細表示に使用する。
	 *
	 * @param leaveItemId 휴가항목 식별 번호
	 * @return 해당 휴가항목 객체 반환
	  * @return 呼び出し元で使用する処理結果を返す。
	 */
	// 근태설정 처리에 필요한 휴가항목를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 勤怠設定処理に必要な休暇項目を照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public LeaveItem getLeaveItem(int leaveItemId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			return leaveItemDao.selectById(conn, leaveItemId);
		} catch (SQLException e) {
			throw new RuntimeException("휴가항목 조회 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}
	
	/**
	 * [조회] 사용가능한 휴가항목 전체 리스트 조회
	 *
	 * @return 사용가능한 휴가항목 목록 반환
	  * @return 呼び出し元で使用する処理結果を返す。
	 */
	// 근태설정 처리에 필요한 Usable휴가항목 목록를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 勤怠設定処理に必要なUsable休暇項目一覧を照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public List<LeaveItem> getUsableLeaveItems(){
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			return leaveItemDao.selectUsableLeaveLists(conn);
		} catch (SQLException e) {
			throw new RuntimeException("사용가능한 휴가항목 조회 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}
	/**
	 * [통합저장] 휴가항목 삽입, 수정, 삭제 트랜잭션 분기 처리
	  * 複数のデータ変更を一つのトランザクションとして処理し、成功時はコミット、失敗時はロールバックする。
	 *
	 * @param item   휴가항목 객체
	 * @param action 실행할 액션 (insert, update, delete)
	  * @param action 処理に必要な値を受け取る。
	 */
	// 요청에서 휴가항목작업구분 작업에 필요한 값을 읽고 검증한 뒤 해당 서비스 처리를 호출한다.
	// 여러 DAO와 입력값을 조합해 화면 또는 다음 업무 단계에서 바로 사용할 수 있는 결과 객체를 만든다.
	// リクエストから休暇項目処理区分処理に必要な値を取得・検証し、該当するサービス処理を呼び出す。
	// 複数のDAO結果と入力値を組み合わせ、画面または次の業務段階で直ちに使用できる結果オブジェクトを作成する。
	public void processLeaveItemAction(LeaveItem item, String action) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false); // 수동 커밋 모드 전환 처리

			if ("insert".equals(action)) {
				leaveItemDao.insert(conn, item);
			} else if ("update".equals(action)) {
				leaveItemDao.update(conn, item);
			} else if ("delete".equals(action)) {
				leaveItemDao.delete(conn, item.getLeaveItemId());
			}

			conn.commit(); // 트랜잭션 정상 완료 시 커밋 처리
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException("휴가항목 액션 처리 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// ==========================================
	// 2. 근태항목(ATTENDANCE_ITEM) 관련 로직
	// 社員の勤務・休暇記録と適用期間を確認し、勤怠照会または残日数計算へ反映する。
	// ==========================================

	/**
	 * [조회] 근태항목 전체 리스트 조회
	 *
	 * @return 근태항목 목록 반환
	  * @return 呼び出し元で使用する処理結果を返す。
	 */
	// 근태설정 처리에 필요한 Attend항목 목록를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 勤怠設定処理に必要なAttend項目一覧を照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public List<AttendanceItem> getAttendItems() {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			return attendItemDao.selectAll(conn);
		} catch (SQLException e) {
			throw new RuntimeException("근태항목 목록 조회 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	/**
	 * [조회] 특정 근태항목 단건 조회 (에디터 패널 표시용)
	  * 識別番号に該当する一件の詳細データを照会し、編集・詳細表示に使用する。
	 *
	 * @param attendItemId 근태항목 식별 번호
	 * @return 해당 근태항목 객체 반환
	  * @return 呼び出し元で使用する処理結果を返す。
	 */
	// 근태설정 처리에 필요한 Attend항목를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 勤怠設定処理に必要なAttend項目を照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public AttendanceItem getAttendItem(int attendItemId) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			return attendItemDao.selectById(conn, attendItemId);
		} catch (SQLException e) {
			throw new RuntimeException("근태항목 조회 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	/**
	 * [통합저장] 근태항목 삽입, 수정, 삭제 트랜잭션 분기 처리
	  * 複数のデータ変更を一つのトランザクションとして処理し、成功時はコミット、失敗時はロールバックする。
	 *
	 * @param item   근태항목 객체
	 * @param action 실행할 액션 (insert, update, delete)
	  * @param action 処理に必要な値を受け取る。
	 */
	// 요청에서 Attend항목작업구분 작업에 필요한 값을 읽고 검증한 뒤 해당 서비스 처리를 호출한다.
	// 여러 DAO와 입력값을 조합해 화면 또는 다음 업무 단계에서 바로 사용할 수 있는 결과 객체를 만든다.
	// リクエストからAttend項目処理区分処理に必要な値を取得・検証し、該当するサービス処理を呼び出す。
	// 複数のDAO結果と入力値を組み合わせ、画面または次の業務段階で直ちに使用できる結果オブジェクトを作成する。
	public void processAttendItemAction(AttendanceItem item, String action) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false); // 수동 커밋 모드 전환 처리

			if ("insert".equals(action)) {
				attendItemDao.insert(conn, item);
			} else if ("update".equals(action)) {
				attendItemDao.update(conn, item);
			} else if ("delete".equals(action)) {
				attendItemDao.delete(conn, item.getAttendanceItemId());
			}

			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException("근태항목 액션 처리 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	// ==========================================
	// 3. 근태그룹(ATTENDANCE_GROUP) 관련 로직
	// 社員の勤務・休暇記録と適用期間を確認し、勤怠照会または残日数計算へ反映する。
	// ==========================================

	/**
	 * [조회] 근태그룹 전체 리스트 조회 (셀렉트 박스 및 모달용)
	 *
	 * @return 근태그룹 목록 반환
	  * @return 呼び出し元で使用する処理結果を返す。
	 */
	// 근태설정 처리에 필요한 AttendGroups를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 勤怠設定処理に必要なAttendGroupsを照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public List<AttendanceGroup> getAttendGroups() {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			return attendGroupDao.selectAll(conn);
		} catch (SQLException e) {
			throw new RuntimeException("근태그룹 목록 조회 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	/**
	 * [통합저장] 근태그룹 삽입, 수정, 삭제 트랜잭션 분기 처리
	  * 複数のデータ変更を一つのトランザクションとして処理し、成功時はコミット、失敗時はロールバックする。
	 *
	 * @param group  근태그룹 객체
	 * @param action 실행할 액션 (insert, update, delete)
	  * @param action 処理に必要な値を受け取る。
	 */
	// 요청에서 Attend그룹작업구분 작업에 필요한 값을 읽고 검증한 뒤 해당 서비스 처리를 호출한다.
	// 여러 DAO와 입력값을 조합해 화면 또는 다음 업무 단계에서 바로 사용할 수 있는 결과 객체를 만든다.
	// リクエストからAttendグループ処理区分処理に必要な値を取得・検証し、該当するサービス処理を呼び出す。
	// 複数のDAO結果と入力値を組み合わせ、画面または次の業務段階で直ちに使用できる結果オブジェクトを作成する。
	public void processAttendGroupAction(AttendanceGroup group, String action) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false); // 수동 커밋 모드 전환 처리

			if ("insert".equals(action)) {
				attendGroupDao.insert(conn, group);
			} else if ("update".equals(action)) {
				attendGroupDao.update(conn, group);
			} else if ("delete".equals(action)) {
				// 그룹에 포함된 근태항목과 연결 기록을 먼저 삭제한 뒤 그룹을 제거한다.
				// グループに含まれる勤怠項目と関連履歴を先に削除してからグループを削除する。
				for (AttendanceItem item : attendItemDao.selectAll(conn)) {
					if (item.getAttendanceGroupId() == group.getAttendanceGroupId()) {
						attendItemDao.delete(conn, item.getAttendanceItemId());
					}
				}
				attendGroupDao.delete(conn, group.getAttendanceGroupId());
			}

			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException("근태그룹 액션 처리 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	/**
	 * [조회] 근태그룹 및 하위 근태항목 계층형 리스트 조회 (화면 트리 렌더링용)
	 *
	 * @return 그룹별로 묶인 근태항목 DTO 목록 반환
	  * @return 呼び出し元で使用する処理結果を返す。
	 */
	// 근태설정 처리에 필요한 근태그룹With항목 목록를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 勤怠設定処理に必要な勤怠グループWith項目一覧を照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	public List<AttendanceGroupWithItemsDto> getAttendanceGroupWithItems() {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			// DAO에서 JOIN 쿼리를 통해 그룹과 항목을 묶어서 반환하는 메서드를 호출합니다.
			// (attendGroupDao 또는 attendItemDao 중 쿼리를 작성하실 곳을 지정하세요)
			// 業務条件に合うSQLを準備し、入力値をバインドしてデータベースで実行する。
			return attendGroupDao.selectGroupWithItems(conn);
		} catch (SQLException e) {
			throw new RuntimeException("근태그룹 및 항목 계층 목록 조회 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}
}
