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
		List<EmployeeInsuranceHistory> list = new ArrayList<>();

		try {
			// EMPLOYEE_ID 기준으로 조회하며, 입력된 순서(PK 오름차순)대로 정렬하여 가져옵니다.
			String sql = "SELECT * FROM EMPLOYEE_INSURANCE_HISTORY WHERE EMPLOYEE_ID = ? ORDER BY EMPLOYEE_INSURANCE_HISTORY_ID ASC";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, empId);
			rs = pstmt.executeQuery();

			// 조회된 결과 행(Row)이 있을 때까지 반복하며 DTO에 담습니다.
			while (rs.next()) {
				EmployeeInsuranceHistory ins = new EmployeeInsuranceHistory();

				// EMPLOYEE_INSURANCE_HISTORY_ID (PK)
				ins.setEmployeeInsuranceHistoryId(rs.getInt("EMPLOYEE_INSURANCE_HISTORY_ID"));

				// EMPLOYEE_ID (FK)
				ins.setEmployeeId(rs.getInt("EMPLOYEE_ID"));

				// INSURANCE_TYPE (구분 - 국민연금/건강보험 등)
				ins.setInsuranceType(rs.getString("INSURANCE_TYPE"));

				// SYMBOL_NO (기호번호)
				ins.setSymbolNo(rs.getString("SYMBOL_NO"));

				// ACQUIRE_DATE (취득일 - DATE 타입)
				ins.setAcquireDate(rs.getDate("ACQUIRE_DATE"));

				// LOSS_DATE (상실일 - DATE 타입)
				ins.setLossDate(rs.getDate("LOSS_DATE"));

				// 매핑이 완료된 DTO를 리스트에 추가
				list.add(ins);
			}
			return list;

		} finally {
			// PreparedStatement와 ResultSet 자원을 안전하게 반환합니다.
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

	// 사원번호(EMPLOYEE_ID)를 기준으로 해당 사원의 4대보험 이력 전체 삭제
	public void deleteByEmpId(Connection conn, int empId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM EMPLOYEE_INSURANCE_HISTORY WHERE EMPLOYEE_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, empId);
			pstmt.executeUpdate();
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