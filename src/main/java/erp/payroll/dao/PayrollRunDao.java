package erp.payroll.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.payroll.model.PayrollRun;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 월별 급여계산 회차 마스터 데이터베이스 접근(DAO) 클래스
public class PayrollRunDao {

	// 싱글톤 인스턴스 생성
	private static PayrollRunDao payrollRunDao = new PayrollRunDao();

	// 싱글톤 접근 메서드
	public static PayrollRunDao getInstance() {
		return payrollRunDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	private PayrollRunDao() {
	}

	// 급여계산 회차 등록
	public void insert(Connection conn, PayrollRun run) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO PAYROLL_RUN "
					+ "(PAYROLL_RUN_ID, PAY_YEAR, PAY_MONTH, PAY_SEQ, INCOME_TYPE, CALC_START_DATE, CALC_END_DATE, PAY_DATE) "
					+ "VALUES (PAYROLL_RUN_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, run.getPayYear());
			pstmt.setString(2, run.getPayMonth());
			pstmt.setString(3, run.getPaySeq());
			pstmt.setString(4, run.getIncomeType());

			if (run.getCalcStartDate() == null) {
				pstmt.setNull(5, Types.DATE);
			} else {
				pstmt.setTimestamp(5, new Timestamp(run.getCalcStartDate().getTime()));
			}

			if (run.getCalcEndDate() == null) {
				pstmt.setNull(6, Types.DATE);
			} else {
				pstmt.setTimestamp(6, new Timestamp(run.getCalcEndDate().getTime()));
			}

			if (run.getPayDate() == null) {
				pstmt.setNull(7, Types.DATE);
			} else {
				pstmt.setTimestamp(7, new Timestamp(run.getPayDate().getTime()));
			}

			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 급여계산 회차 단건 조회
	public PayrollRun selectById(Connection conn, int runId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT PAYROLL_RUN_ID, PAY_YEAR, PAY_MONTH, PAY_SEQ, INCOME_TYPE, CALC_START_DATE, CALC_END_DATE, PAY_DATE "
					+ "FROM PAYROLL_RUN WHERE PAYROLL_RUN_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, runId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				return makePayrollRunFromResultSet(rs);
			}
			return null;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 급여계산 회차 전체 목록 조회
	public List<PayrollRun> selectAll(Connection conn) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT PAYROLL_RUN_ID, PAY_YEAR, PAY_MONTH, PAY_SEQ, INCOME_TYPE, CALC_START_DATE, CALC_END_DATE, PAY_DATE "
					+ "FROM PAYROLL_RUN ORDER BY PAY_YEAR DESC, PAY_MONTH DESC, PAY_SEQ DESC";

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			List<PayrollRun> result = new ArrayList<>();
			while (rs.next()) {
				result.add(makePayrollRunFromResultSet(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 급여계산 회차 수정
	public int update(Connection conn, PayrollRun run) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "UPDATE PAYROLL_RUN SET "
					+ "PAY_YEAR = ?, PAY_MONTH = ?, PAY_SEQ = ?, INCOME_TYPE = ?, CALC_START_DATE = ?, CALC_END_DATE = ?, PAY_DATE = ? "
					+ "WHERE PAYROLL_RUN_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, run.getPayYear());
			pstmt.setString(2, run.getPayMonth());
			pstmt.setString(3, run.getPaySeq());
			pstmt.setString(4, run.getIncomeType());

			if (run.getCalcStartDate() == null) {
				pstmt.setNull(5, Types.DATE);
			} else {
				pstmt.setTimestamp(5, new Timestamp(run.getCalcStartDate().getTime()));
			}

			if (run.getCalcEndDate() == null) {
				pstmt.setNull(6, Types.DATE);
			} else {
				pstmt.setTimestamp(6, new Timestamp(run.getCalcEndDate().getTime()));
			}

			if (run.getPayDate() == null) {
				pstmt.setNull(7, Types.DATE);
			} else {
				pstmt.setTimestamp(7, new Timestamp(run.getPayDate().getTime()));
			}

			pstmt.setInt(8, run.getPayrollRunId());

			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 급여계산 회차 삭제
	public int delete(Connection conn, int runId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM PAYROLL_RUN WHERE PAYROLL_RUN_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, runId);
			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// ResultSet 데이터를 PayrollRun 객체로 변환
	private PayrollRun makePayrollRunFromResultSet(ResultSet rs) throws SQLException {
		PayrollRun run = new PayrollRun();

		run.setPayrollRunId(rs.getInt("PAYROLL_RUN_ID"));
		run.setPayYear(rs.getString("PAY_YEAR"));
		run.setPayMonth(rs.getString("PAY_MONTH"));
		run.setPaySeq(rs.getString("PAY_SEQ"));
		run.setIncomeType(rs.getString("INCOME_TYPE"));

		Timestamp startTs = rs.getTimestamp("CALC_START_DATE");
		if (startTs != null) {
			run.setCalcStartDate(new java.util.Date(startTs.getTime()));
		}

		Timestamp endTs = rs.getTimestamp("CALC_END_DATE");
		if (endTs != null) {
			run.setCalcEndDate(new java.util.Date(endTs.getTime()));
		}

		Timestamp payTs = rs.getTimestamp("PAY_DATE");
		if (payTs != null) {
			run.setPayDate(new java.util.Date(payTs.getTime()));
		}

		return run;
	}
}