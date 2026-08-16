package erp.payroll.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import erp.payroll.dto.DayWorkerPaymentEmployee;
import erp.payroll.dto.DayWorkerPaymentPage.DayWorkerPaymentWork;
import erp.payroll.dto.PayrollManagementItem;
import jdbc.JdbcUtil;

// 일용직 근무기록과 급여 공제내역을 조회한다.
public class DayWorkerPayrollDao {

	public List<DayWorkerPaymentEmployee> selectPaymentEmployees(Connection conn, int runId, Date startDate,
			Date endDate) throws SQLException {
		String sql = "SELECT E.EMPLOYEE_ID, E.EMP_TYPE, E.EMP_NO, E.EMP_NAME_KR, "
				+ "NVL(D.DEPARTMENT_NAME, '-') DEPARTMENT_NAME, NVL(J.JOB_POSITION_NAME, '-') JOB_POSITION_NAME, E.STATUS, "
				+ "NVL((SELECT SUM(W.DAILY_PAY) FROM DAILY_WORK_RECORD W WHERE W.EMPLOYEE_ID = E.EMPLOYEE_ID "
				+ "AND W.WORK_DATE BETWEEN ? AND ?), 0) TOTAL_PAYMENT "
				+ "FROM PAYROLL_EMPLOYEE PE JOIN EMPLOYEE E ON E.EMPLOYEE_ID = PE.EMPLOYEE_ID "
				+ "LEFT JOIN DEPARTMENT D ON D.DEPARTMENT_ID = E.DEPARTMENT_ID "
				+ "LEFT JOIN JOB_POSITION J ON J.JOB_POSITION_ID = E.JOB_POSITION_ID "
				+ "WHERE PE.PAYROLL_RUN_ID = ? ORDER BY E.EMP_NAME_KR";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setDate(1, new java.sql.Date(startDate.getTime()));
			pstmt.setDate(2, new java.sql.Date(endDate.getTime()));
			pstmt.setInt(3, runId);
			rs = pstmt.executeQuery();
			List<DayWorkerPaymentEmployee> result = new ArrayList<>();
			while (rs.next()) {
				result.add(makeEmployee(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	public List<DayWorkerPaymentEmployee> selectAvailableEmployees(Connection conn, int runId, String keyword,
			Integer departmentId, int page, int size) throws SQLException {
		String sql = "SELECT * FROM (SELECT A.*, ROWNUM RNUM FROM ("
				+ "SELECT E.EMPLOYEE_ID, E.EMP_TYPE, E.EMP_NO, E.EMP_NAME_KR, "
				+ "NVL(D.DEPARTMENT_NAME, '-') DEPARTMENT_NAME, NVL(J.JOB_POSITION_NAME, '-') JOB_POSITION_NAME, E.STATUS, "
				+ "0 TOTAL_PAYMENT FROM EMPLOYEE E "
				+ "LEFT JOIN DEPARTMENT D ON D.DEPARTMENT_ID = E.DEPARTMENT_ID "
				+ "LEFT JOIN JOB_POSITION J ON J.JOB_POSITION_ID = E.JOB_POSITION_ID "
				+ "WHERE E.EMP_TYPE = '일용직' AND E.STATUS = '재직' "
				+ "AND NOT EXISTS (SELECT 1 FROM PAYROLL_EMPLOYEE PE WHERE PE.PAYROLL_RUN_ID = ? "
				+ "AND PE.EMPLOYEE_ID = E.EMPLOYEE_ID) "
				+ "AND (? IS NULL OR E.EMP_NAME_KR LIKE '%' || ? || '%' OR E.EMP_NO LIKE '%' || ? || '%') "
				+ "AND (? IS NULL OR E.DEPARTMENT_ID = ?) ORDER BY E.EMP_NAME_KR) A WHERE ROWNUM <= ?) "
				+ "WHERE RNUM >= ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			setAvailableParameters(pstmt, runId, keyword, departmentId);
			pstmt.setInt(7, page * size);
			pstmt.setInt(8, (page - 1) * size + 1);
			rs = pstmt.executeQuery();
			List<DayWorkerPaymentEmployee> result = new ArrayList<>();
			while (rs.next()) {
				result.add(makeEmployee(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	public int countAvailableEmployees(Connection conn, int runId, String keyword, Integer departmentId)
			throws SQLException {
		String sql = "SELECT COUNT(*) FROM EMPLOYEE E WHERE E.EMP_TYPE = '일용직' AND E.STATUS = '재직' "
				+ "AND NOT EXISTS (SELECT 1 FROM PAYROLL_EMPLOYEE PE WHERE PE.PAYROLL_RUN_ID = ? "
				+ "AND PE.EMPLOYEE_ID = E.EMPLOYEE_ID) "
				+ "AND (? IS NULL OR E.EMP_NAME_KR LIKE '%' || ? || '%' OR E.EMP_NO LIKE '%' || ? || '%') "
				+ "AND (? IS NULL OR E.DEPARTMENT_ID = ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			setAvailableParameters(pstmt, runId, keyword, departmentId);
			try (ResultSet rs = pstmt.executeQuery()) {
				rs.next();
				return rs.getInt(1);
			}
		}
	}

	public List<DayWorkerPaymentWork> selectWorkPayments(Connection conn, int employeeId, Date startDate,
			Date endDate) throws SQLException {
		String sql = "SELECT WORK_DATE, PAY_RATE, DAILY_PAY, INCOME_TAX, LOCAL_INCOME_TAX "
				+ "FROM DAILY_WORK_RECORD WHERE EMPLOYEE_ID = ? AND WORK_DATE BETWEEN ? AND ? ORDER BY WORK_DATE";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, employeeId);
			pstmt.setDate(2, new java.sql.Date(startDate.getTime()));
			pstmt.setDate(3, new java.sql.Date(endDate.getTime()));
			try (ResultSet rs = pstmt.executeQuery()) {
				List<DayWorkerPaymentWork> result = new ArrayList<>();
				while (rs.next()) {
					DayWorkerPaymentWork work = new DayWorkerPaymentWork();
					work.setWorkDate(rs.getDate("WORK_DATE"));
					work.setPaymentRate(rs.getInt("PAY_RATE"));
					work.setPaymentAmount(rs.getLong("DAILY_PAY"));
					work.setIncomeTax(rs.getLong("INCOME_TAX"));
					work.setLocalIncomeTax(rs.getLong("LOCAL_INCOME_TAX"));
					result.add(work);
				}
				return result;
			}
		}
	}

	public List<PayrollManagementItem> selectDeductionEntries(Connection conn, int payrollEmployeeId)
			throws SQLException {
		String sql = "SELECT D.DEDUCT_ITEM_ID, D.DEDUCT_NAME, NVL(E.AMOUNT, 0) AMOUNT "
				+ "FROM DEDUCT_ITEM D LEFT JOIN PAYROLL_ENTRY E ON E.DEDUCT_ITEM_ID = D.DEDUCT_ITEM_ID "
				+ "AND E.PAYROLL_EMPLOYEE_ID = ? WHERE D.USE_YN = 'Y' ORDER BY D.DEDUCT_ITEM_ID";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, payrollEmployeeId);
			try (ResultSet rs = pstmt.executeQuery()) {
				List<PayrollManagementItem> result = new ArrayList<>();
				while (rs.next()) {
					PayrollManagementItem item = new PayrollManagementItem();
					item.setItemCode(rs.getInt("DEDUCT_ITEM_ID"));
					item.setItemName(rs.getString("DEDUCT_NAME"));
					item.setAmount(rs.getLong("AMOUNT"));
					result.add(item);
				}
				return result;
			}
		}
	}

	public long[] selectAutomaticDeductions(Connection conn, int employeeId, Date startDate, Date endDate)
			throws SQLException {
		String sql = "SELECT E.NP_YN, E.HI_YN, E.LTCI_YN, E.EI_YN, E.NP_MONTHLY_BASE, E.HI_MONTHLY_BASE, "
				+ "E.EI_MONTHLY_BASE, NVL((SELECT SUM(W.DAILY_PAY) FROM DAILY_WORK_RECORD W "
				+ "WHERE W.EMPLOYEE_ID = E.EMPLOYEE_ID AND W.WORK_DATE BETWEEN ? AND ?), 0) GROSS_PAY, "
				+ "NVL((SELECT SUM(W.INCOME_TAX) FROM DAILY_WORK_RECORD W "
				+ "WHERE W.EMPLOYEE_ID = E.EMPLOYEE_ID AND W.WORK_DATE BETWEEN ? AND ?), 0) INCOME_TAX, "
				+ "NVL((SELECT SUM(W.LOCAL_INCOME_TAX) FROM DAILY_WORK_RECORD W "
				+ "WHERE W.EMPLOYEE_ID = E.EMPLOYEE_ID AND W.WORK_DATE BETWEEN ? AND ?), 0) LOCAL_INCOME_TAX "
				+ "FROM EMPLOYEE E WHERE E.EMPLOYEE_ID = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setDate(1, new java.sql.Date(startDate.getTime()));
			pstmt.setDate(2, new java.sql.Date(endDate.getTime()));
			pstmt.setDate(3, new java.sql.Date(startDate.getTime()));
			pstmt.setDate(4, new java.sql.Date(endDate.getTime()));
			pstmt.setDate(5, new java.sql.Date(startDate.getTime()));
			pstmt.setDate(6, new java.sql.Date(endDate.getTime()));
			pstmt.setInt(7, employeeId);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (!rs.next()) {
					return new long[6];
				}
				long grossPay = rs.getLong("GROSS_PAY");
				long pensionBase = positiveBase(rs.getLong("NP_MONTHLY_BASE"), grossPay);
				long healthBase = positiveBase(rs.getLong("HI_MONTHLY_BASE"), grossPay);
				long employmentBase = positiveBase(rs.getLong("EI_MONTHLY_BASE"), grossPay);

				// 등록된 보수월액이 없으면 해당 기간의 실제 지급총액을 계산 기준으로 사용한다.
				long nationalPension = "Y".equals(rs.getString("NP_YN"))
						? roundDownTen(pensionBase * 0.045) : 0;
				long healthInsurance = "Y".equals(rs.getString("HI_YN"))
						? roundDownTen(healthBase * 0.03545) : 0;
				long longTermCare = "Y".equals(rs.getString("LTCI_YN"))
						? roundDownTen(healthInsurance * 0.1295) : 0;
				long employmentInsurance = "Y".equals(rs.getString("EI_YN"))
						? roundDownTen(employmentBase * 0.009) : 0;

				// 근무기록에 세액이 없으면 일용근로소득 공제액을 반영한 간이세액을 사용한다.
				long incomeTax = rs.getLong("INCOME_TAX");
				if (incomeTax == 0) {
					incomeTax = selectEstimatedIncomeTax(conn, employeeId, startDate, endDate);
				}
				long localIncomeTax = rs.getLong("LOCAL_INCOME_TAX");
				if (localIncomeTax == 0 && incomeTax > 0) {
					localIncomeTax = roundDownTen(incomeTax * 0.1);
				}
				return new long[] { nationalPension, healthInsurance, longTermCare, employmentInsurance,
						incomeTax, localIncomeTax };
			}
		}
	}

	private long selectEstimatedIncomeTax(Connection conn, int employeeId, Date startDate, Date endDate)
			throws SQLException {
		// 일 15만원 공제 후 6% 세율과 55% 근로소득세액공제를 적용한 간이 계산이다.
		String sql = "SELECT NVL(SUM(FLOOR(GREATEST(DAILY_PAY - 150000, 0) * 0.027 / 10) * 10), 0) "
				+ "FROM DAILY_WORK_RECORD WHERE EMPLOYEE_ID = ? AND WORK_DATE BETWEEN ? AND ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, employeeId);
			pstmt.setDate(2, new java.sql.Date(startDate.getTime()));
			pstmt.setDate(3, new java.sql.Date(endDate.getTime()));
			try (ResultSet rs = pstmt.executeQuery()) {
				rs.next();
				return rs.getLong(1);
			}
		}
	}

	private long positiveBase(long registeredBase, long grossPay) {
		return registeredBase > 0 ? registeredBase : grossPay;
	}

	private long roundDownTen(double amount) {
		return ((long) amount / 10) * 10;
	}

	private void setAvailableParameters(PreparedStatement pstmt, int runId, String keyword, Integer departmentId)
			throws SQLException {
		String searchKeyword = keyword == null || keyword.trim().isEmpty() ? null : keyword.trim();
		pstmt.setInt(1, runId);
		pstmt.setString(2, searchKeyword);
		pstmt.setString(3, searchKeyword);
		pstmt.setString(4, searchKeyword);
		pstmt.setObject(5, departmentId);
		pstmt.setObject(6, departmentId);
	}

	private DayWorkerPaymentEmployee makeEmployee(ResultSet rs) throws SQLException {
		DayWorkerPaymentEmployee employee = new DayWorkerPaymentEmployee();
		employee.setEmployeeId(rs.getInt("EMPLOYEE_ID"));
		employee.setEmploymentTypeName(rs.getString("EMP_TYPE"));
		employee.setEmployeeNumber(rs.getString("EMP_NO"));
		employee.setEmployeeName(rs.getString("EMP_NAME_KR"));
		employee.setDepartmentName(rs.getString("DEPARTMENT_NAME"));
		employee.setPositionName(rs.getString("JOB_POSITION_NAME"));
		employee.setStatusName(rs.getString("STATUS"));
		employee.setTotalPayment(rs.getLong("TOTAL_PAYMENT"));
		return employee;
	}
}
