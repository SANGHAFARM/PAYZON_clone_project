package erp.employees.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.employees.model.EmployeeInsuranceHistory;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 사원별 4대보험 취득/상실 이력 데이터베이스 접근(DAO) 클래스
public class EmployeeInsuranceHistoryDao {

	// 싱글톤 인스턴스 생성
	private static EmployeeInsuranceHistoryDao employeeInsuranceHistoryDao = new EmployeeInsuranceHistoryDao();

	// 싱글톤 접근 메서드
	public static EmployeeInsuranceHistoryDao getInstance() {
		return employeeInsuranceHistoryDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	private EmployeeInsuranceHistoryDao() {
	}

	// 4대보험 이력 등록
	// 시퀀스를 사용하여 기본키 발급 및 취득/상실 데이터 저장
	public void insert(Connection conn, EmployeeInsuranceHistory history) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO EMPLOYEE_INSURANCE_HISTORY "
					+ "(EMPLOYEE_INSURANCE_HISTORY_ID, EMPLOYEE_ID, INSURANCE_TYPE, SYMBOL_NO, ACQUIRE_DATE, LOSS_DATE) "
					+ "VALUES (EMPLOYEE_INSURANCE_HISTORY_SEQ.NEXTVAL, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, history.getEmployeeId());
			pstmt.setString(2, history.getInsuranceType());
			pstmt.setString(3, history.getSymbolNo());

			if (history.getAcquireDate() == null) {
				pstmt.setNull(4, Types.DATE);
			} else {
				pstmt.setTimestamp(4, new Timestamp(history.getAcquireDate().getTime()));
			}

			if (history.getLossDate() == null) {
				pstmt.setNull(5, Types.DATE);
			} else {
				pstmt.setTimestamp(5, new Timestamp(history.getLossDate().getTime()));
			}

			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 4대보험 이력 단건 조회
	public EmployeeInsuranceHistory selectById(Connection conn, int historyId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT EMPLOYEE_INSURANCE_HISTORY_ID, EMPLOYEE_ID, INSURANCE_TYPE, SYMBOL_NO, ACQUIRE_DATE, LOSS_DATE "
					+ "FROM EMPLOYEE_INSURANCE_HISTORY WHERE EMPLOYEE_INSURANCE_HISTORY_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, historyId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				return makeHistoryFromResultSet(rs);
			}
			return null;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 특정 사원의 4대보험 이력 목록 조회
	public List<EmployeeInsuranceHistory> selectByEmpId(Connection conn, int empId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT EMPLOYEE_INSURANCE_HISTORY_ID, EMPLOYEE_ID, INSURANCE_TYPE, SYMBOL_NO, ACQUIRE_DATE, LOSS_DATE "
					+ "FROM EMPLOYEE_INSURANCE_HISTORY WHERE EMPLOYEE_ID = ? ORDER BY ACQUIRE_DATE DESC NULLS LAST, EMPLOYEE_INSURANCE_HISTORY_ID DESC";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, empId);
			rs = pstmt.executeQuery();

			List<EmployeeInsuranceHistory> result = new ArrayList<>();
			while (rs.next()) {
				result.add(makeHistoryFromResultSet(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 4대보험 이력 수정
	public int update(Connection conn, EmployeeInsuranceHistory history) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "UPDATE EMPLOYEE_INSURANCE_HISTORY SET "
					+ "EMPLOYEE_ID = ?, INSURANCE_TYPE = ?, SYMBOL_NO = ?, ACQUIRE_DATE = ?, LOSS_DATE = ? "
					+ "WHERE EMPLOYEE_INSURANCE_HISTORY_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, history.getEmployeeId());
			pstmt.setString(2, history.getInsuranceType());
			pstmt.setString(3, history.getSymbolNo());

			if (history.getAcquireDate() == null) {
				pstmt.setNull(4, Types.DATE);
			} else {
				pstmt.setTimestamp(4, new Timestamp(history.getAcquireDate().getTime()));
			}

			if (history.getLossDate() == null) {
				pstmt.setNull(5, Types.DATE);
			} else {
				pstmt.setTimestamp(5, new Timestamp(history.getLossDate().getTime()));
			}

			pstmt.setInt(6, history.getEmployeeInsuranceHistoryId());

			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 4대보험 이력 삭제
	public int delete(Connection conn, int historyId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM EMPLOYEE_INSURANCE_HISTORY WHERE EMPLOYEE_INSURANCE_HISTORY_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, historyId);
			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// ResultSet 데이터를 EmployeeInsuranceHistory 객체로 변환
	private EmployeeInsuranceHistory makeHistoryFromResultSet(ResultSet rs) throws SQLException {
		EmployeeInsuranceHistory history = new EmployeeInsuranceHistory();

		history.setEmployeeInsuranceHistoryId(rs.getInt("EMPLOYEE_INSURANCE_HISTORY_ID"));
		history.setEmployeeId(rs.getInt("EMPLOYEE_ID"));
		history.setInsuranceType(rs.getString("INSURANCE_TYPE"));
		history.setSymbolNo(rs.getString("SYMBOL_NO"));

		Timestamp acqTs = rs.getTimestamp("ACQUIRE_DATE");
		if (acqTs != null) {
			history.setAcquireDate(new java.util.Date(acqTs.getTime()));
		}

		Timestamp lossTs = rs.getTimestamp("LOSS_DATE");
		if (lossTs != null) {
			history.setLossDate(new java.util.Date(lossTs.getTime()));
		}

		return history;
	}
}