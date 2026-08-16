package erp.retirement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import erp.retirement.model.RetirementCalculation;
import erp.retirement.dto.RetirementBenefitListItem;
import jdbc.JdbcUtil; // 자원 반환용 유틸리티 클래스

// 퇴직급여 계산내역을 저장하고 조회한다.
public class RetirementCalculationDao {

	// 싱글톤 인스턴스 생성
	private static RetirementCalculationDao retirementCalculationDao = new RetirementCalculationDao();

	// 싱글톤 접근 메서드
	public static RetirementCalculationDao getInstance() {
		return retirementCalculationDao;
	}

	// 외부에서 객체 생성을 못 하도록 생성자를 private으로 제한
	private RetirementCalculationDao() {
	}

	// 퇴직급여 계산내역 등록
	// 시퀀스를 사용하여 기본키 발급 및 퇴직금 정산 데이터 저장
	public void insert(Connection conn, RetirementCalculation calc) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO RETIREMENT_CALCULATION ("
					+ "RETIREMENT_CALCULATION_ID, EMPLOYEE_ID, CALC_TYPE, CALC_START_DATE, RETIRE_DATE, SERVICE_YEARS, SERVICE_DAYS, EXCLUDE_DAYS, "
					+ "COMPENSATION_AMT, DISMISSAL_AMT, TAX_FREE_RETIRE_AMT, PREPAID_TAX_AMT, TAX_CREDIT_AMT, THREE_MONTH_TOTAL, AVG_MONTH_WAGE, "
					+ "AVG_DAY_WAGE, ORDINARY_DAY_WAGE, RETIRE_INCOME, CALCULATED_TAX_AMT, INCOME_TAX, LOCAL_INCOME_TAX, DEFERRED_INCOME_TAX, "
					+ "DEFERRED_LOCAL_TAX, SPECIAL_RURAL_TAX, OTHER_DEDUCT_AMT, TAXABLE_RETIRE_AMT, WITHHOLDING_TAX_AMT, ACTUAL_PAY_AMT, PAY_METHOD, PAY_DATE) "
					+ "VALUES (RETIREMENT_CALCULATION_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, calc.getEmployeeId());
			pstmt.setString(2, calc.getCalcType());

			// 날짜 null 방어 로직 적용
			if (calc.getCalcStartDate() == null) {
				pstmt.setNull(3, Types.DATE);
			} else {
				pstmt.setTimestamp(3, new Timestamp(calc.getCalcStartDate().getTime()));
			}

			if (calc.getRetireDate() == null) {
				pstmt.setNull(4, Types.DATE);
			} else {
				pstmt.setTimestamp(4, new Timestamp(calc.getRetireDate().getTime()));
			}

			pstmt.setInt(5, calc.getServiceYears());
			pstmt.setInt(6, calc.getServiceDays());
			pstmt.setInt(7, calc.getExcludeDays());
			pstmt.setLong(8, calc.getCompensationAmt());
			pstmt.setLong(9, calc.getDismissalAmt());
			pstmt.setLong(10, calc.getTaxFreeRetireAmt());
			pstmt.setLong(11, calc.getPrepaidTaxAmt());
			pstmt.setLong(12, calc.getTaxCreditAmt());
			pstmt.setLong(13, calc.getThreeMonthTotal());
			pstmt.setLong(14, calc.getAvgMonthWage());
			pstmt.setLong(15, calc.getAvgDayWage());
			pstmt.setLong(16, calc.getOrdinaryDayWage());
			pstmt.setLong(17, calc.getRetireIncome());
			pstmt.setLong(18, calc.getCalculatedTaxAmt());
			pstmt.setLong(19, calc.getIncomeTax());
			pstmt.setLong(20, calc.getLocalIncomeTax());
			pstmt.setLong(21, calc.getDeferredIncomeTax());
			pstmt.setLong(22, calc.getDeferredLocalTax());
			pstmt.setLong(23, calc.getSpecialRuralTax());
			pstmt.setLong(24, calc.getOtherDeductAmt());
			pstmt.setLong(25, calc.getTaxableRetireAmt());
			pstmt.setLong(26, calc.getWithholdingTaxAmt());
			pstmt.setLong(27, calc.getActualPayAmt());
			pstmt.setString(28, calc.getPayMethod());

			if (calc.getPayDate() == null) {
				pstmt.setNull(29, Types.DATE);
			} else {
				pstmt.setTimestamp(29, new Timestamp(calc.getPayDate().getTime()));
			}

			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 상세 테이블 FK로 사용할 퇴직급여 계산 PK를 먼저 발급한다.
	public int nextId(Connection conn) throws SQLException {
		try (PreparedStatement pstmt = conn.prepareStatement("SELECT RETIREMENT_CALCULATION_SEQ.NEXTVAL FROM DUAL");
				ResultSet rs = pstmt.executeQuery()) {
			rs.next();
			return rs.getInt(1);
		}
	}

	// 미리 발급한 PK를 사용하여 마스터 계산내역을 저장한다.
	public void insertWithId(Connection conn, RetirementCalculation calc) throws SQLException {
		String sql = "INSERT INTO RETIREMENT_CALCULATION VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, calc.getRetirementCalculationId());
			pstmt.setInt(2, calc.getEmployeeId());
			pstmt.setString(3, calc.getCalcType());
			pstmt.setTimestamp(4, new Timestamp(calc.getCalcStartDate().getTime()));
			pstmt.setTimestamp(5, new Timestamp(calc.getRetireDate().getTime()));
			pstmt.setInt(6, calc.getServiceYears());
			pstmt.setInt(7, calc.getServiceDays());
			pstmt.setInt(8, calc.getExcludeDays());
			pstmt.setLong(9, calc.getCompensationAmt());
			pstmt.setLong(10, calc.getDismissalAmt());
			pstmt.setLong(11, calc.getTaxFreeRetireAmt());
			pstmt.setLong(12, calc.getPrepaidTaxAmt());
			pstmt.setLong(13, calc.getTaxCreditAmt());
			pstmt.setLong(14, calc.getThreeMonthTotal());
			pstmt.setLong(15, calc.getAvgMonthWage());
			pstmt.setLong(16, calc.getAvgDayWage());
			pstmt.setLong(17, calc.getOrdinaryDayWage());
			pstmt.setLong(18, calc.getRetireIncome());
			pstmt.setLong(19, calc.getCalculatedTaxAmt());
			pstmt.setLong(20, calc.getIncomeTax());
			pstmt.setLong(21, calc.getLocalIncomeTax());
			pstmt.setLong(22, calc.getDeferredIncomeTax());
			pstmt.setLong(23, calc.getDeferredLocalTax());
			pstmt.setLong(24, calc.getSpecialRuralTax());
			pstmt.setLong(25, calc.getOtherDeductAmt());
			pstmt.setLong(26, calc.getTaxableRetireAmt());
			pstmt.setLong(27, calc.getWithholdingTaxAmt());
			pstmt.setLong(28, calc.getActualPayAmt());
			pstmt.setString(29, calc.getPayMethod());
			pstmt.setTimestamp(30, new Timestamp(calc.getPayDate().getTime()));
			pstmt.executeUpdate();
		}
	}

	// 지급년도별 퇴직급여 목록을 사원, 부서, 직위와 JOIN하여 조회한다.
	public List<RetirementBenefitListItem> selectBenefitList(Connection conn, int paymentYear) throws SQLException {
		String sql = "SELECT R.RETIREMENT_CALCULATION_ID, R.EMPLOYEE_ID, "
				+ "TO_CHAR(R.PAY_DATE, 'YYYY-MM-DD') PAY_DATE, R.CALC_TYPE, E.EMP_NAME_KR, "
				+ "J.JOB_POSITION_NAME, D.DEPARTMENT_NAME, "
				+ "TO_CHAR(R.CALC_START_DATE, 'YYYY-MM-DD') START_DATE, "
				+ "TO_CHAR(R.RETIRE_DATE, 'YYYY-MM-DD') END_DATE, R.SERVICE_DAYS, "
				+ "R.ACTUAL_PAY_AMT, R.PAY_METHOD FROM RETIREMENT_CALCULATION R "
				+ "JOIN EMPLOYEE E ON R.EMPLOYEE_ID = E.EMPLOYEE_ID "
				+ "LEFT JOIN DEPARTMENT D ON E.DEPARTMENT_ID = D.DEPARTMENT_ID "
				+ "LEFT JOIN JOB_POSITION J ON E.JOB_POSITION_ID = J.JOB_POSITION_ID "
				+ "WHERE EXTRACT(YEAR FROM R.PAY_DATE) = ? "
				+ "ORDER BY R.PAY_DATE DESC, R.RETIREMENT_CALCULATION_ID DESC";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, paymentYear);
			try (ResultSet rs = pstmt.executeQuery()) {
				List<RetirementBenefitListItem> items = new ArrayList<>();
				while (rs.next()) {
					RetirementBenefitListItem item = new RetirementBenefitListItem();
					item.setCalculationId(rs.getInt("RETIREMENT_CALCULATION_ID"));
					item.setEmployeeId(rs.getInt("EMPLOYEE_ID"));
					item.setPaymentDate(rs.getString("PAY_DATE"));
					item.setSettlementType(rs.getString("CALC_TYPE"));
					item.setEmployeeName(rs.getString("EMP_NAME_KR"));
					item.setPositionName(rs.getString("JOB_POSITION_NAME"));
					item.setDepartmentName(rs.getString("DEPARTMENT_NAME"));
					item.setCalculationStartDate(rs.getString("START_DATE"));
					item.setCalculationEndDate(rs.getString("END_DATE"));
					item.setServiceDays(rs.getInt("SERVICE_DAYS"));
					item.setNetPayment(rs.getLong("ACTUAL_PAY_AMT"));
					item.setPaymentMethod(rs.getString("PAY_METHOD"));
					items.add(item);
				}
				return items;
			}
		}
	}

	// 화면에서 선택한 지급년도의 퇴직급여만 전체 삭제한다.
	public int deleteAllByPaymentYear(Connection conn, int paymentYear) throws SQLException {
		String sql = "DELETE FROM RETIREMENT_CALCULATION WHERE EXTRACT(YEAR FROM PAY_DATE) = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, paymentYear);
			return pstmt.executeUpdate();
		}
	}

	// 기본키로 퇴직급여 계산내역 한 건을 조회한다.
	public RetirementCalculation selectById(Connection conn, int calcId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM RETIREMENT_CALCULATION WHERE RETIREMENT_CALCULATION_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, calcId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				return makeCalcFromResultSet(rs);
			}
			return null; // 조회된 데이터가 없을 경우 null 반환
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 특정 사원의 계산내역을 최근 정산 순서로 조회한다.
	public List<RetirementCalculation> selectByEmpId(Connection conn, int empId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM RETIREMENT_CALCULATION WHERE EMPLOYEE_ID = ? ORDER BY RETIREMENT_CALCULATION_ID DESC";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, empId);
			rs = pstmt.executeQuery();

			List<RetirementCalculation> result = new ArrayList<>();
			while (rs.next()) {
				result.add(makeCalcFromResultSet(rs));
			}
			return result;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 기본키로 퇴직급여 계산내역을 삭제한다.
	public int delete(Connection conn, int calcId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM RETIREMENT_CALCULATION WHERE RETIREMENT_CALCULATION_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, calcId);
			return pstmt.executeUpdate(); // 삭제된 행의 개수 반환
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 사원 삭제 시 해당 사원의 퇴직급여 계산내역을 함께 삭제
	public int deleteByEmployeeId(Connection conn, int employeeId) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String sql = "DELETE FROM RETIREMENT_CALCULATION WHERE EMPLOYEE_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, employeeId);
			return pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 조회 결과를 퇴직급여 계산 객체로 변환한다.
	private RetirementCalculation makeCalcFromResultSet(ResultSet rs) throws SQLException {
		RetirementCalculation calc = new RetirementCalculation();

		calc.setRetirementCalculationId(rs.getInt("RETIREMENT_CALCULATION_ID"));
		calc.setEmployeeId(rs.getInt("EMPLOYEE_ID"));
		calc.setCalcType(rs.getString("CALC_TYPE"));

		Timestamp startTs = rs.getTimestamp("CALC_START_DATE");
		if (startTs != null) {
			calc.setCalcStartDate(new java.util.Date(startTs.getTime()));
		}

		Timestamp retireTs = rs.getTimestamp("RETIRE_DATE");
		if (retireTs != null) {
			calc.setRetireDate(new java.util.Date(retireTs.getTime()));
		}

		calc.setServiceYears(rs.getInt("SERVICE_YEARS"));
		calc.setServiceDays(rs.getInt("SERVICE_DAYS"));
		calc.setExcludeDays(rs.getInt("EXCLUDE_DAYS"));
		calc.setCompensationAmt(rs.getLong("COMPENSATION_AMT"));
		calc.setDismissalAmt(rs.getLong("DISMISSAL_AMT"));
		calc.setTaxFreeRetireAmt(rs.getLong("TAX_FREE_RETIRE_AMT"));
		calc.setPrepaidTaxAmt(rs.getLong("PREPAID_TAX_AMT"));
		calc.setTaxCreditAmt(rs.getLong("TAX_CREDIT_AMT"));
		calc.setThreeMonthTotal(rs.getLong("THREE_MONTH_TOTAL"));
		calc.setAvgMonthWage(rs.getLong("AVG_MONTH_WAGE"));
		calc.setAvgDayWage(rs.getLong("AVG_DAY_WAGE"));
		calc.setOrdinaryDayWage(rs.getLong("ORDINARY_DAY_WAGE"));
		calc.setRetireIncome(rs.getLong("RETIRE_INCOME"));
		calc.setCalculatedTaxAmt(rs.getLong("CALCULATED_TAX_AMT"));
		calc.setIncomeTax(rs.getLong("INCOME_TAX"));
		calc.setLocalIncomeTax(rs.getLong("LOCAL_INCOME_TAX"));
		calc.setDeferredIncomeTax(rs.getLong("DEFERRED_INCOME_TAX"));
		calc.setDeferredLocalTax(rs.getLong("DEFERRED_LOCAL_TAX"));
		calc.setSpecialRuralTax(rs.getLong("SPECIAL_RURAL_TAX"));
		calc.setOtherDeductAmt(rs.getLong("OTHER_DEDUCT_AMT"));
		calc.setTaxableRetireAmt(rs.getLong("TAXABLE_RETIRE_AMT"));
		calc.setWithholdingTaxAmt(rs.getLong("WITHHOLDING_TAX_AMT"));
		calc.setActualPayAmt(rs.getLong("ACTUAL_PAY_AMT"));
		calc.setPayMethod(rs.getString("PAY_METHOD"));

		Timestamp payTs = rs.getTimestamp("PAY_DATE");
		if (payTs != null) {
			calc.setPayDate(new java.util.Date(payTs.getTime()));
		}

		return calc;
	}
}
