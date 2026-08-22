package erp.payroll.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import erp.payroll.dto.FourInsurancePage.FourInsuranceDeduction;
import erp.payroll.dto.FourInsurancePage;

// 급여 차수별 4대보험 공제액을 조회한다.
// 4대보험보험 데이터를 데이터베이스에서 조회하고 저장·수정·삭제한다.
// 四大保険保険データをデータベースから照会し、登録・更新・削除する。
public class FourInsuranceDao {

	// 조회 조건에 맞는 화면 데이터 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合う画面データデータをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	public FourInsurancePage selectPage(Connection conn, String year, String month, String sequence)
			throws SQLException {
		FourInsurancePage page = new FourInsurancePage();
		selectPeriod(conn, year, month, sequence, page);
		page.setDeductions(selectDeductions(conn, year, month, sequence));
		return page;
	}

	// 조회 조건에 맞는 기간 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合う期間データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	private void selectPeriod(Connection conn, String year, String month, String sequence,
			FourInsurancePage page) throws SQLException {
		String sql = "SELECT MIN(CALC_START_DATE) CALC_START_DATE, MAX(CALC_END_DATE) CALC_END_DATE, "
				+ "MAX(PAY_DATE) PAY_DATE FROM PAYROLL_RUN WHERE PAY_YEAR = ? AND PAY_MONTH = ? AND PAY_SEQ = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, year);
			pstmt.setString(2, month);
			pstmt.setString(3, sequence);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					page.setCalculationStart(rs.getDate("CALC_START_DATE"));
					page.setCalculationEnd(rs.getDate("CALC_END_DATE"));
					page.setPaymentDate(rs.getDate("PAY_DATE"));
				}
			}
		}
	}

	// 조회 조건에 맞는 공제 목록 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合う控除一覧データをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	private List<FourInsuranceDeduction> selectDeductions(Connection conn, String year, String month,
			String sequence) throws SQLException {
		Map<String, Integer> insuranceItemIds = selectInsuranceItemIds(conn);
		String sql = "SELECT E.EMPLOYEE_ID, E.EMP_TYPE, E.EMP_NAME_KR, "
				+ "NVL(DP.DEPARTMENT_NAME, '-') DEPARTMENT_NAME, NVL(J.JOB_POSITION_NAME, '-') POSITION_NAME, "
				+ "SUM(CASE WHEN EN.DEDUCT_ITEM_ID = ? THEN EN.AMOUNT ELSE 0 END) PENSION, "
				+ "SUM(CASE WHEN EN.DEDUCT_ITEM_ID = ? THEN EN.AMOUNT ELSE 0 END) HEALTH, "
				+ "SUM(CASE WHEN EN.DEDUCT_ITEM_ID = ? THEN EN.AMOUNT ELSE 0 END) CARE, "
				+ "SUM(CASE WHEN EN.DEDUCT_ITEM_ID = ? THEN EN.AMOUNT ELSE 0 END) EMPLOYMENT, "
				+ "SUM(CASE WHEN EN.PAY_ITEM_ID IS NOT NULL THEN EN.AMOUNT ELSE 0 END) GROSS_PAY, "
				+ "E.EI_MONTHLY_BASE "
				+ "FROM PAYROLL_RUN R JOIN PAYROLL_EMPLOYEE PE ON PE.PAYROLL_RUN_ID = R.PAYROLL_RUN_ID "
				+ "JOIN EMPLOYEE E ON E.EMPLOYEE_ID = PE.EMPLOYEE_ID "
				+ "LEFT JOIN PAYROLL_ENTRY EN ON EN.PAYROLL_EMPLOYEE_ID = PE.PAYROLL_EMPLOYEE_ID "
				+ "LEFT JOIN DEPARTMENT DP ON DP.DEPARTMENT_ID = E.DEPARTMENT_ID "
				+ "LEFT JOIN JOB_POSITION J ON J.JOB_POSITION_ID = E.JOB_POSITION_ID "
				+ "WHERE R.PAY_YEAR = ? AND R.PAY_MONTH = ? AND R.PAY_SEQ = ? "
				+ "GROUP BY E.EMPLOYEE_ID, E.EMP_TYPE, E.EMP_NAME_KR, DP.DEPARTMENT_NAME, "
				+ "J.JOB_POSITION_NAME, E.EI_MONTHLY_BASE "
				+ "HAVING SUM(CASE WHEN EN.DEDUCT_ITEM_ID IN (?, ?, ?, ?) THEN EN.AMOUNT ELSE 0 END) > 0 "
				+ "ORDER BY E.EMP_NAME_KR, E.EMPLOYEE_ID";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			int pensionId = itemId(insuranceItemIds, "PENSION");
			int healthId = itemId(insuranceItemIds, "HEALTH");
			int careId = itemId(insuranceItemIds, "CARE");
			int employmentId = itemId(insuranceItemIds, "EMPLOYMENT");
			pstmt.setInt(1, pensionId);
			pstmt.setInt(2, healthId);
			pstmt.setInt(3, careId);
			pstmt.setInt(4, employmentId);
			pstmt.setString(5, year);
			pstmt.setString(6, month);
			pstmt.setString(7, sequence);
			pstmt.setInt(8, pensionId);
			pstmt.setInt(9, healthId);
			pstmt.setInt(10, careId);
			pstmt.setInt(11, employmentId);
			try (ResultSet rs = pstmt.executeQuery()) {
				List<FourInsuranceDeduction> result = new ArrayList<>();
				while (rs.next()) {
					result.add(makeDeduction(rs));
				}
				return result;
			}
		}
	}

	// 항목명은 보험 항목의 ID를 찾을 때만 사용하고, 금액 집계는 고정된 ID로 처리한다.
	// 조회 조건에 맞는 보험항목Ids 데이터를 데이터베이스에서 조회한다.
	// Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
	// 検索条件に合う保険項目Idsデータをデータベースから照会する。
	// Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
	private Map<String, Integer> selectInsuranceItemIds(Connection conn) throws SQLException {
		String sql = "SELECT DEDUCT_ITEM_ID, DEDUCT_NAME FROM DEDUCT_ITEM "
				+ "WHERE DEDUCT_NAME IN ('국민연금', '건강보험', '장기요양보험', '노인장기요양보험', '고용보험') "
				+ "ORDER BY DEDUCT_ITEM_ID";
		Map<String, Integer> result = new HashMap<>();
		try (PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {
			while (rs.next()) {
				String name = rs.getString("DEDUCT_NAME");
				String type = insuranceType(name);
				if (type != null && !result.containsKey(type)) {
					result.put(type, rs.getInt("DEDUCT_ITEM_ID"));
				}
			}
		}
		return result;
	}

	// 공제항목명을 국민연금·건강보험 등 화면에서 사용할 보험 구분으로 변환한다.
	// SQL 실행에 필요한 값과 NULL을 안전하게 바인딩하고 JDBC 자원은 사용이 끝난 뒤 정리한다.
	// 控除項目名を国民年金・健康保険など画面で使用する保険区分へ変換する。
	// SQL実行に必要な値とNULLを安全にバインドし、JDBCリソースは使用後に整理する。
	private String insuranceType(String name) {
		if ("국민연금".equals(name)) return "PENSION";
		if ("건강보험".equals(name)) return "HEALTH";
		if ("장기요양보험".equals(name) || "노인장기요양보험".equals(name)) return "CARE";
		if ("고용보험".equals(name)) return "EMPLOYMENT";
		return null;
	}

	// 4대보험보험 데이터의 내부 식별번호를 반환한다.
	// SQL 실행에 필요한 값과 NULL을 안전하게 바인딩하고 JDBC 자원은 사용이 끝난 뒤 정리한다.
	// 四大保険保険データの内部識別番号を返す。
	// SQL実行に必要な値とNULLを安全にバインドし、JDBCリソースは使用後に整理する。
	private int itemId(Map<String, Integer> itemIds, String type) {
		Integer itemId = itemIds.get(type);
		return itemId == null ? -1 : itemId;
	}

	// 조회값과 입력값을 조합하여 공제 처리 데이터를 구성한다.
	// SQL 실행에 필요한 값과 NULL을 안전하게 바인딩하고 JDBC 자원은 사용이 끝난 뒤 정리한다.
	// 照会値と入力値を組み合わせて控除の処理データを構成する。
	// SQL実行に必要な値とNULLを安全にバインドし、JDBCリソースは使用後に整理する。
	private FourInsuranceDeduction makeDeduction(ResultSet rs) throws SQLException {
		FourInsuranceDeduction deduction = new FourInsuranceDeduction();
		deduction.setEmploymentTypeName(rs.getString("EMP_TYPE"));
		deduction.setEmployeeName(rs.getString("EMP_NAME_KR"));
		deduction.setDepartmentName(rs.getString("DEPARTMENT_NAME"));
		deduction.setPositionName(rs.getString("POSITION_NAME"));
		// 저장된 금액은 근로자 부담액이며 국민연금·건강보험 계열은 회사도 같은 금액을 부담한다.
		// 支給額・控除額・期間値を業務基準に従って計算し、合計と最終金額へ反映する。
		long pension = rs.getLong("PENSION");
		long health = rs.getLong("HEALTH");
		long care = rs.getLong("CARE");
		long employment = rs.getLong("EMPLOYMENT");
		deduction.setPensionEmployee(pension);
		deduction.setPensionEmployer(pension);
		deduction.setHealthEmployee(health);
		deduction.setHealthEmployer(health);
		deduction.setCareEmployee(care);
		deduction.setCareEmployer(care);
		deduction.setEmploymentEmployee(employment);
		long employmentBase = rs.getLong("EI_MONTHLY_BASE");
		if (employmentBase <= 0) {
			employmentBase = rs.getLong("GROSS_PAY");
		}
		// 회사는 실업급여분 외에 150인 미만 사업장 기준 0.25%를 추가 부담한다.
		// 支給額・控除額・期間値を業務基準に従って計算し、合計と最終金額へ反映する。
		deduction.setEmploymentEmployer(employment + roundDownTen(employmentBase * 0.0025));
		return deduction;
	}

	// 계산된 보험료와 세액을 10단위로 절사하여 반환한다.
	// SQL 실행에 필요한 값과 NULL을 안전하게 바인딩하고 JDBC 자원은 사용이 끝난 뒤 정리한다.
	// 計算した保険料と税額を10単位で切り捨てて返す。
	// SQL実行に必要な値とNULLを安全にバインドし、JDBCリソースは使用後に整理する。
	private long roundDownTen(double amount) {
		return ((long) amount / 10) * 10;
	}
}
