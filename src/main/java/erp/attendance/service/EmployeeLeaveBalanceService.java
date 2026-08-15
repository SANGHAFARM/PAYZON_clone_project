package erp.attendance.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import erp.attendance.dao.EmployeeLeaveBalanceDao;
import erp.attendance.model.EmployeeLeaveBalance;
import erp.attendance.dto.EmployeeLeaveRow;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

public class EmployeeLeaveBalanceService {

	// 싱글톤 인스턴스 생성
	private static EmployeeLeaveBalanceService instance = new EmployeeLeaveBalanceService();

	// 싱글톤 접근 메서드
	public static EmployeeLeaveBalanceService getInstance() {
		return instance;
	}

	// 외부 객체 생성 방지 처리
	private EmployeeLeaveBalanceService() {
	}

	// 휴가 현황 DAO 연동 객체 할당
	private EmployeeLeaveBalanceDao leaveBalanceDao = EmployeeLeaveBalanceDao.getInstance();

	/**
	 * [조회] 검색 조건에 따른 사원별 휴가일수 목록 조회
	 *
	 * @param leaveItemId 휴가항목 식별 번호
	 * @param keyword     사원명/사번 검색어
	 * @param status      재직/퇴직 상태
	 * @return 커스텀 조인 DTO 목록 반환
	 */
	public List<EmployeeLeaveRow> getEmployeeLeaveRows(int leaveItemId, String keyword, String status) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			return leaveBalanceDao.selectEmployeeLeaveRows(conn, leaveItemId, keyword, status);
		} catch (SQLException e) {
			throw new RuntimeException("사원별 휴가 현황 조회 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	/**
	 * [통합저장] 화면에서 입력받은 사원별 휴가일수 일괄 업데이트 트랜잭션 처리
	 *
	 * @param balances 휴가현황 객체 리스트 (기존 PK 유무에 따라 내부 DAO에서 분기 처리)
	 */
	public void saveLeaveBalances(List<EmployeeLeaveBalance> balances) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false); // 수동 커밋 모드 전환 처리

			for (EmployeeLeaveBalance balance : balances) {
				if (balance.getEmployeeLeaveBalanceId() > 0) {
					// 기존 식별 번호가 있으면 업데이트 수행
					leaveBalanceDao.update(conn, balance);
				} else {
					// 기존 식별 번호가 없으면 신규 삽입 수행
					leaveBalanceDao.insert(conn, balance);
				}
			}

			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException("사원별 휴가 현황 일괄 저장 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	/**
	 * [선택삭제] 선택된 사원의 휴가 부여 내역 완전 삭제 트랜잭션 처리
	 *
	 * @param empLeaveIds 삭제할 휴가 현황 식별 번호 리스트
	 */
	public void deleteLeaveBalances(List<Integer> empLeaveIds) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false); // 수동 커밋 모드 전환 처리

			for (int id : empLeaveIds) {
				leaveBalanceDao.delete(conn, id);
			}

			conn.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException("선택된 휴가 현황 삭제 중 오류 발생", e);
		} finally {
			JdbcUtil.close(conn);
		}
	}

	/**
	 * [자동계산] 사원들의 입사일을 기준으로 연차 일수 자동 계산 후 DB 일괄 갱신 처리 (핵심 비즈니스 로직)
	 *
	 * @param leaveItemId 대상 휴가항목 식별 번호
	 */
	public void autoCalculateLeaveDays(int leaveItemId) {
		// 근로기준법에 따른 연차 자동 산정 로직 구현 예정 영역
		// 사원의 입사일을 가져와 근속년수 계산 후 휴가일수 부여
	}
}