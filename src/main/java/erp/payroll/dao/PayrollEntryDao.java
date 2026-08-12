package erp.payroll.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.payroll.model.PayrollEntry;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 사원별 지급/공제 상세 내역 데이터베이스 접근(DAO) 클래스
public class PayrollEntryDao {

	// 싱글톤 인스턴스 생성
	private static PayrollEntryDao payrollEntryDao = new PayrollEntryDao();

	// 싱글톤 접근 메서드
	public static PayrollEntryDao getInstance() {
		return payrollEntryDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	private PayrollEntryDao() {
	}

	// 지급/공제 상세 내역 등록
	public void insert(Connection conn, PayrollEntry entry) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO PAYROLL_ENTRY "
					+ "(PAYROLL_ENTRY_ID, PAYROLL_EMPLOYEE_ID, PAY_ITEM_ID, DEDUCT_ITEM_ID, AMOUNT) "
					+ "VALUES (PAYROLL_ENTRY_SEQ.NEXTVAL, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, entry.getPayrollEmployeeId());

			pstmt.setObject(2, entry.getPayItemId(), Types.NUMERIC);
			pstmt.setObject(3, entry.getDeductItemId(), Types.NUMERIC);

			pstmt.setLong(4, entry.getAmount());

			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 지급/공제 상세 내역 단건 조회
	public PayrollEntry selectById(Connection conn, int entryId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT PAYROLL_ENTRY_ID, PAYROLL_EMPLOYEE_ID, PAY_ITEM_ID, DEDUCT_ITEM_ID, AMOUNT "
					+ "FROM PAYROLL_ENTRY WHERE PAYROLL_ENTRY_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, entryId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				return makePayrollEntryFromResultSet(rs);
			}
			return null;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 특정 사원결과(회차+사원)의 모든 상세 내역 조회
	public List<PayrollEntry> selectByPayrollEmployeeId(Connection conn, int peId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT PAYROLL_ENTRY_ID, PAYROLL_EMPLOYEE_ID, PAY_ITEM_ID, DEDUCT_ITEM_ID, AMOUNT "
					+ "FROM PAYROLL_ENTRY WHERE PAYROLL_EMPLOYEE_ID = ? ORDER BY PAYROLL_ENTRY_ID ASC";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, peId);
			rs = pstmt.executeQuery();

			List<PayrollEntry> result = new ArrayList<>();
			while (rs.next()) {
				result.add(makePayrollEntryFromResultSet(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 지급/공제 상세 내역 수정
	public int update(Connection conn, PayrollEntry entry) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "UPDATE PAYROLL_ENTRY SET "
					+ "PAYROLL_EMPLOYEE_ID = ?, PAY_ITEM_ID = ?, DEDUCT_ITEM_ID = ?, AMOUNT = ? "
					+ "WHERE PAYROLL_ENTRY_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, entry.getPayrollEmployeeId());

			pstmt.setObject(2, entry.getPayItemId(), Types.NUMERIC);
			pstmt.setObject(3, entry.getDeductItemId(), Types.NUMERIC);

			pstmt.setLong(4, entry.getAmount());
			pstmt.setInt(5, entry.getPayrollEntryId());

			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 지급/공제 상세 내역 삭제
	public int delete(Connection conn, int entryId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM PAYROLL_ENTRY WHERE PAYROLL_ENTRY_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, entryId);
			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// ResultSet 데이터를 PayrollEntry 객체로 변환
	private PayrollEntry makePayrollEntryFromResultSet(ResultSet rs) throws SQLException {
		PayrollEntry entry = new PayrollEntry();

		entry.setPayrollEntryId(rs.getInt("PAYROLL_ENTRY_ID"));
		entry.setPayrollEmployeeId(rs.getInt("PAYROLL_EMPLOYEE_ID"));

		int pItemId = rs.getInt("PAY_ITEM_ID");
		entry.setPayItemId(rs.wasNull() ? null : pItemId);

		int dItemId = rs.getInt("DEDUCT_ITEM_ID");
		entry.setDeductItemId(rs.wasNull() ? null : dItemId);

		entry.setAmount(rs.getLong("AMOUNT"));

		return entry;
	}
}