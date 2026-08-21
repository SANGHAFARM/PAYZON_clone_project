package erp.attendance.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import erp.attendance.dto.EmployeeLeaveRow;
import erp.attendance.dto.LeaveInquiryDto;
import erp.attendance.model.EmployeeLeaveBalance;
import erp.attendance.service.request.LeaveInquiryRequest;
import jdbc.JdbcUtil;
import oracle.net.aso.p;

/*import erp.attend.model.EmpLeave;*/
/*import erp.attend.model.EmpLeaveStatusItem;*/
/*import erp.attend.model.LeaveStatusCondition;*/

public class EmployeeLeaveBalanceDao {

	private static EmployeeLeaveBalanceDao employeeLeaveBalanceDao = new EmployeeLeaveBalanceDao();

	public static EmployeeLeaveBalanceDao getInstance() {
		return employeeLeaveBalanceDao;
	}

	private EmployeeLeaveBalanceDao() {

	}

	/*
	 * // 사원 1명의 휴가정보를 조회하는 메서드 public EmployeeLeaveBalance
	 * selectByEmpIdAndLeaveItemId(Connection conn, int employeeId, int leaveItemId)
	 * throws SQLException { String sql =
	 * "SELECT * FROM EMPLOYEE_LEAVE_BALANCE WHERE EMPLOYEE_ID = ? AND LEAVE_ITEM_ID = ?"
	 * ; try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	 * pstmt.setInt(1, employeeId); pstmt.setInt(2, leaveItemId); try (ResultSet rs
	 * = pstmt.executeQuery()) { if (rs.next()) { return convertEmpLeave(rs); } } }
	 * return null; }
	 */

	// 휴가조회(전체목록) 화면용 - 필터에 맞는 여러 사원의, 특정 휴가항목 전체/사용/잔여 조회
	public List<LeaveInquiryDto> selectLeaveBalance(Connection conn, LeaveInquiryRequest req) throws SQLException{
		String sql = "SELECT ELB.EMPLOYEE_ID, E.EMP_TYPE, E.EMP_NO, E.EMP_NAME_KR, D.DEPARTMENT_NAME, J.JOB_POSITION_NAME, "
		        + "LI.ITEM_NAME, ELB.TOTAL_DAYS "
		        + "FROM EMPLOYEE_LEAVE_BALANCE ELB "
		        + "LEFT JOIN EMPLOYEE E ON E.EMPLOYEE_ID = ELB.EMPLOYEE_ID "
		        + "LEFT JOIN DEPARTMENT D ON D.DEPARTMENT_ID = E.DEPARTMENT_ID "
		        + "LEFT JOIN JOB_POSITION J ON J.JOB_POSITION_ID = E.JOB_POSITION_ID "
		        + "LEFT JOIN LEAVE_ITEM LI ON LI.LEAVE_ITEM_ID = ELB.LEAVE_ITEM_ID "
		        + "WHERE ELB.LEAVE_ITEM_ID = ? "
		        + "AND (? IS NULL OR "
		        + "      E.EMP_TYPE LIKE '%' || ? || '%' OR "
		        + "      E.EMP_NO LIKE '%' || ? || '%' OR "
		        + "      E.EMP_NAME_KR LIKE '%' || ? || '%' OR "
		        + "      D.DEPARTMENT_NAME LIKE '%' || ? || '%' OR "
		        + "      J.JOB_POSITION_NAME LIKE '%' || ? || '%' OR "
		        + "      LI.ITEM_NAME LIKE '%' || ? || '%') "
		        + "AND (? IS NULL OR E.EMP_STATUS = ?) "
		        + "AND (? IS NULL OR E.EMP_TYPE = ?) "
		        + "AND (? IS NULL OR D.DEPARTMENT_ID = ?) "
		        + "AND (? IS NULL OR J.JOB_POSITION_ID = ?)";
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setInt(1, req.getLeaveItemId());
			pstmt.setString(2, req.getKeyword());
			pstmt.setString(3, req.getKeyword());
			pstmt.setString(4, req.getKeyword());
			pstmt.setString(5, req.getKeyword());
			pstmt.setString(6, req.getKeyword());
			pstmt.setString(7, req.getKeyword());
			pstmt.setString(8, req.getKeyword());
			pstmt.setString(9, req.getStatus());
			pstmt.setString(10, req.getStatus());
			pstmt.setString(11, req.getEmpType());
			pstmt.setString(12, req.getEmpType());
			setIntOrNull(pstmt, 13, 14, req.getDepartmentId());
			setIntOrNull(pstmt, 15, 16, req.getJobPositionId());
			List<LeaveInquiryDto> list = new ArrayList<>();
			try(ResultSet rs = pstmt.executeQuery()){
				while (rs.next()) {
					list.add(convertLeaveInquiryDto(conn, rs, req.getLeaveItemId()));
				}
			}
			return list;
			
		}
		
	}
	
	private LeaveInquiryDto convertLeaveInquiryDto(Connection conn, ResultSet rs, int leaveItemId) throws SQLException{
		LeaveInquiryDto dto = new LeaveInquiryDto();
		int employeeId = rs.getInt("EMPLOYEE_ID");
		dto.setEmployeeId(employeeId);
		dto.setEmpType(rs.getString("EMP_TYPE"));
		dto.setEmpNo(rs.getString("EMP_NO"));
		dto.setEmpNameKr(rs.getString("EMP_NAME_KR"));
		dto.setDepartmentName(rs.getString("DEPARTMENT_NAME"));
		dto.setJobPositionName(rs.getString("JOB_POSITION_NAME"));
		dto.setItemName(rs.getString("ITEM_NAME"));
		long totalDays = rs.getLong("TOTAL_DAYS");
		dto.setTotalDays(totalDays);
		
		double usedDays = 0;
		String sql = "SELECT SUM(ATTEND_VALUE) AS USED_DAYS FROM EMPLOYEE_ATTENDANCE WHERE EMPLOYEE_ID = ? AND LEAVE_ITEM_ID = ?";
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setInt(1, employeeId);
			pstmt.setInt(2, leaveItemId);
			try(ResultSet rsSub = pstmt.executeQuery()){
				if (rsSub.next()) {
					usedDays = rs.getDouble("USED_DAYS");
				}
			}
		}
		dto.setUsedDays(usedDays);
		dto.setRemainingDays(totalDays - usedDays);
		return dto;
		
	}

	private void setIntOrNull(PreparedStatement pstmt, int idx1, int idx2, Integer value) throws SQLException {
		if (value != null) {
			pstmt.setInt(idx1, value);
			pstmt.setInt(idx2, value);
		} else {
			pstmt.setNull(idx1, java.sql.Types.NUMERIC);
			pstmt.setNull(idx2, java.sql.Types.NUMERIC);
		}
	}

	// [조회] 특정 휴가항목에 대한 전체 사원의 휴가 부여 현황 조회
	// EMPLOYEE 테이블을 기준으로 LEFT JOIN을 수행하여 휴가 데이터가 없는 사원도 조회함
	public List<EmployeeLeaveRow> selectEmployeeLeaveRows(Connection conn, int leaveItemId, String keyword,
			String status) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<EmployeeLeaveRow> list = new ArrayList<>();

		try {
			// 동적 쿼리 구성을 위한 StringBuilder 사용 처리
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT elb.EMPLOYEE_LEAVE_BALANCE_ID, e.EMPLOYEE_ID, e.EMP_TYPE, e.EMP_NO, e.EMP_NAME_KR, ");
			sql.append("d.DEPARTMENT_NAME, p.JOB_POSITION_NAME, e.JOIN_DATE, NVL(elb.TOTAL_DAYS, 0) AS TOTAL_DAYS ");
			sql.append("FROM EMPLOYEE e ");
			// 해당 휴가항목(LEAVE_ITEM_ID)에 대해서만 아우터 조인 수행
			sql.append(
					"LEFT JOIN EMPLOYEE_LEAVE_BALANCE elb ON e.EMPLOYEE_ID = elb.EMPLOYEE_ID AND elb.LEAVE_ITEM_ID = ? ");
			sql.append("LEFT JOIN DEPARTMENT d ON e.DEPARTMENT_ID = d.DEPARTMENT_ID ");
			sql.append("LEFT JOIN JOB_POSITION p ON e.JOB_POSITION_ID = p.JOB_POSITION_ID ");
			sql.append("WHERE 1=1 ");

			// 재직/퇴직 상태 검색 조건 추가
			if (status != null && !status.isEmpty()) {
				sql.append("AND e.STATUS = ? ");
			}

			// 사원번호 또는 이름 검색 조건 추가
			if (keyword != null && !keyword.isEmpty()) {
				sql.append("AND (e.EMP_NAME_KR LIKE ? OR e.EMP_NO LIKE ?) ");
			}

			sql.append("ORDER BY e.EMP_NO ASC");

			pstmt = conn.prepareStatement(sql.toString());

			// 파라미터 인덱스 동적 할당 처리
			int index = 1;
			pstmt.setInt(index++, leaveItemId);

			if (status != null && !status.isEmpty()) {
				pstmt.setString(index++, status);
			}

			if (keyword != null && !keyword.isEmpty()) {
				pstmt.setString(index++, "%" + keyword + "%");
				pstmt.setString(index++, "%" + keyword + "%");
			}

			rs = pstmt.executeQuery();

			// 조회된 결과 매핑 처리
			while (rs.next()) {
				EmployeeLeaveRow row = new EmployeeLeaveRow();
				row.setEmpLeaveId(rs.getInt("EMPLOYEE_LEAVE_BALANCE_ID")); // 값이 없으면 0 반환됨
				row.setEmployeeId(rs.getInt("EMPLOYEE_ID"));
				row.setEmpType(rs.getString("EMP_TYPE"));
				row.setEmpNo(rs.getString("EMP_NO"));
				row.setEmpName(rs.getString("EMP_NAME_KR"));
				row.setDeptName(rs.getString("DEPARTMENT_NAME"));
				row.setPosName(rs.getString("JOB_POSITION_NAME"));
				row.setJoinDate(rs.getDate("JOIN_DATE"));
				row.setLeaveDays(rs.getDouble("TOTAL_DAYS"));

				list.add(row);
			}
			return list;

		} finally {
			// 자원 안전 반환 처리
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// [삽입] 새로운 사원별 휴가 부여 내역 추가 처리
	public void insert(Connection conn, EmployeeLeaveBalance balance) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			// v5 스키마 규격에 맞춘 INSERT 쿼리 작성 (시퀀스 적용)
			String sql = "INSERT INTO EMPLOYEE_LEAVE_BALANCE "
					+ "(EMPLOYEE_LEAVE_BALANCE_ID, EMPLOYEE_ID, LEAVE_ITEM_ID, TOTAL_DAYS) "
					+ "VALUES (EMPLOYEE_LEAVE_BALANCE_SEQ.NEXTVAL, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, balance.getEmployeeId());
			pstmt.setInt(2, balance.getLeaveItemId());
			pstmt.setDouble(3, balance.getTotalDays());

			pstmt.executeUpdate(); // 쿼리 실행 수행

		} finally {
			// 자원 누수 방지를 위한 객체 반환 처리
			JdbcUtil.close(pstmt);
		}
	}

	// [수정] 기존 사원의 부여된 휴가일수 갱신 처리
	public void update(Connection conn, EmployeeLeaveBalance balance) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			// 휴가일수 갱신 쿼리 작성
			String sql = "UPDATE EMPLOYEE_LEAVE_BALANCE " + "SET TOTAL_DAYS = ? "
					+ "WHERE EMPLOYEE_LEAVE_BALANCE_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setDouble(1, balance.getTotalDays());
			pstmt.setInt(2, balance.getEmployeeLeaveBalanceId()); // 식별 가능한 기본키 매핑

			pstmt.executeUpdate(); // 쿼리 실행 수행

		} finally {
			// 자원 누수 방지를 위한 객체 반환 처리
			JdbcUtil.close(pstmt);
		}
	}

	// [삭제] 사원에게 부여된 특정 휴가 내역 완전 삭제 처리
	public void delete(Connection conn, int empLeaveId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			// 기본키 기반 레코드 삭제 쿼리 작성
			String sql = "DELETE FROM EMPLOYEE_LEAVE_BALANCE WHERE EMPLOYEE_LEAVE_BALANCE_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, empLeaveId);

			pstmt.executeUpdate(); // 쿼리 실행 수행

		} finally {
			// 자원 누수 방지를 위한 객체 반환 처리
			JdbcUtil.close(pstmt);
		}
	}
}
