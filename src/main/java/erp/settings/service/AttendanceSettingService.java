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

public class AttendanceSettingService {

	// 싱글톤 인스턴스 생성
	private static AttendanceSettingService instance = new AttendanceSettingService();

	// 싱글톤 접근 메서드
	public static AttendanceSettingService getInstance() {
		return instance;
	}

	// 외부 객체 생성 방지 처리
	private AttendanceSettingService() {
	}

	// 도메인별 DAO 연동 객체 할당
	private LeaveItemDao leaveItemDao = LeaveItemDao.getInstance();
	private AttendanceItemDao attendItemDao = AttendanceItemDao.getInstance();
	private AttendanceGroupDao attendGroupDao = AttendanceGroupDao.getInstance();

	// ==========================================
	// 1. 휴가항목(LEAVE_ITEM) 관련 로직
	// ==========================================

	/**
	 * [조회] 휴가항목 전체 리스트 조회
	 *
	 * @return 휴가항목 목록 반환
	 */
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
	 *
	 * @param leaveItemId 휴가항목 식별 번호
	 * @return 해당 휴가항목 객체 반환
	 */
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
	 */
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
	 *
	 * @param item   휴가항목 객체
	 * @param action 실행할 액션 (insert, update, delete)
	 */
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
	// ==========================================

	/**
	 * [조회] 근태항목 전체 리스트 조회
	 *
	 * @return 근태항목 목록 반환
	 */
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
	 *
	 * @param attendItemId 근태항목 식별 번호
	 * @return 해당 근태항목 객체 반환
	 */
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
	 *
	 * @param item   근태항목 객체
	 * @param action 실행할 액션 (insert, update, delete)
	 */
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
	// ==========================================

	/**
	 * [조회] 근태그룹 전체 리스트 조회 (셀렉트 박스 및 모달용)
	 *
	 * @return 근태그룹 목록 반환
	 */
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
	 *
	 * @param group  근태그룹 객체
	 * @param action 실행할 액션 (insert, update, delete)
	 */
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
	 */
	public List<AttendanceGroupWithItemsDto> getAttendanceGroupWithItems() {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			// DAO에서 JOIN 쿼리를 통해 그룹과 항목을 묶어서 반환하는 메서드를 호출합니다.
			// (attendGroupDao 또는 attendItemDao 중 쿼리를 작성하실 곳을 지정하세요)
			return attendGroupDao.selectGroupWithItems(conn);
		} catch (SQLException e) {
			throw new RuntimeException("근태그룹 및 항목 계층 목록 조회 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}
}