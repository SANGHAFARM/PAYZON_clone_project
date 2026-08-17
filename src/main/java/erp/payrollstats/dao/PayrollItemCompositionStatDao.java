package erp.payrollstats.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import erp.payrollstats.dto.PayrollCompositionStatPage.StatEmployee;
import erp.payrollstats.dto.PayrollCompositionStatPage.StatItem;

public class PayrollItemCompositionStatDao {

	// 사원 검색 모달창용 데이터 조회 (공식 DB 스키마 완벽 적용)
	public List<StatEmployee> selectEmployeeSearchList(Connection conn, String keyword) throws SQLException {
		String sql = "SELECT E.EMPLOYEE_ID, E.EMP_NO, E.EMP_TYPE, E.EMP_NAME_KR, "
				   + "       D.DEPARTMENT_NAME, P.JOB_POSITION_NAME, E.STATUS "
				   + "FROM EMPLOYEE E "
				   + "LEFT JOIN DEPARTMENT D ON E.DEPARTMENT_ID = D.DEPARTMENT_ID "
				   + "LEFT JOIN JOB_POSITION P ON E.JOB_POSITION_ID = P.JOB_POSITION_ID "
				   + "WHERE E.EMP_NAME_KR LIKE ?";
				   
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, "%" + (keyword == null ? "" : keyword) + "%");
			try (ResultSet rs = pstmt.executeQuery()) {
				List<StatEmployee> list = new ArrayList<>();
				while (rs.next()) {
					StatEmployee emp = new StatEmployee();
					emp.setEmployeeId(rs.getInt("EMPLOYEE_ID"));
					emp.setEmployeeNo(rs.getString("EMP_NO"));
					emp.setType(rs.getString("EMP_TYPE"));
					emp.setName(rs.getString("EMP_NAME_KR")); // 공식 컬럼명 적용
					emp.setDepartment(rs.getString("DEPARTMENT_NAME"));
					emp.setPosition(rs.getString("JOB_POSITION_NAME"));
					emp.setStatus(rs.getString("STATUS"));
					list.add(emp);
				}
				return list;
			}
		}
	}

	// 급여 항목 구성 통계 데이터 조회 (지급/공제 항목 완벽 통합 쿼리)
	// 급여 항목 구성 통계 데이터 조회 (실제 컬럼명 PAY_NAME, DEDUCT_NAME 적용 완벽 통합 쿼리)
		public List<StatItem> selectCompositionItems(Connection conn, int payYear, int payMonth, int employeeId) throws SQLException {
			String sql = "SELECT "
					   + "    CASE WHEN EN.PAY_ITEM_ID IS NOT NULL THEN PI.PAY_NAME "
					   + "         ELSE DI.DEDUCT_NAME END AS ITEM_NAME, "
					   + "    EN.AMOUNT, "
					   + "    CASE WHEN EN.PAY_ITEM_ID IS NOT NULL THEN 'P' "
					   + "         ELSE 'D' END AS ITEM_TYPE "
					   + "FROM PAYROLL_RUN R "
					   + "JOIN PAYROLL_EMPLOYEE PE ON R.PAYROLL_RUN_ID = PE.PAYROLL_RUN_ID "
					   + "JOIN PAYROLL_ENTRY EN ON PE.PAYROLL_EMPLOYEE_ID = EN.PAYROLL_EMPLOYEE_ID "
					   + "LEFT JOIN PAY_ITEM PI ON EN.PAY_ITEM_ID = PI.PAY_ITEM_ID "
					   + "LEFT JOIN DEDUCT_ITEM DI ON EN.DEDUCT_ITEM_ID = DI.DEDUCT_ITEM_ID "
					   + "WHERE R.PAY_YEAR = TO_CHAR(?) AND R.PAY_MONTH = LPAD(?, 2, '0') AND PE.EMPLOYEE_ID = ?";
			
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setInt(1, payYear);
				pstmt.setInt(2, payMonth);
				pstmt.setInt(3, employeeId);
				try (ResultSet rs = pstmt.executeQuery()) {
					List<StatItem> list = new ArrayList<>();
					while (rs.next()) {
						StatItem item = new StatItem();
						item.setItemName(rs.getString("ITEM_NAME"));
						item.setAmount(rs.getLong("AMOUNT"));
						item.setType(rs.getString("ITEM_TYPE")); 
						list.add(item);
					}
					return list;
				}
			}
		}
}