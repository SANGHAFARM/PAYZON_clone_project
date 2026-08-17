package erp.payrollstats.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import erp.payrollstats.dto.PayrollStatItem;

public class PayrollStatDao {

    // 검색 콤보박스용 귀속연도 목록 조회
    public List<Integer> selectAvailableYears(Connection conn) throws SQLException {
        String sql = "SELECT DISTINCT PAY_YEAR FROM PAYROLL_RUN ORDER BY PAY_YEAR DESC";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            List<Integer> years = new ArrayList<>();
            while (rs.next()) {
                years.add(rs.getInt("PAY_YEAR"));
            }
            return years;
        }
    }

    // 연도별 데이터 조회 (최근 10년)
    public List<PayrollStatItem> selectAnnualStatRaw(Connection conn, String startYear, String endYear) throws SQLException {
        String sql = "SELECT R.PAY_YEAR, "
                   + "       NVL(SUM(CASE WHEN EN.PAY_ITEM_ID IS NOT NULL THEN EN.AMOUNT ELSE 0 END), 0) AS TOTAL_PAY, "
                   + "       COUNT(DISTINCT PE.PAYROLL_EMPLOYEE_ID) AS TOTAL_SLIPS " // 1년간 발급된 총 급여명세서 수
                   + "FROM PAYROLL_RUN R "
                   + "JOIN PAYROLL_EMPLOYEE PE ON R.PAYROLL_RUN_ID = PE.PAYROLL_RUN_ID "
                   + "LEFT JOIN PAYROLL_ENTRY EN ON PE.PAYROLL_EMPLOYEE_ID = EN.PAYROLL_EMPLOYEE_ID "
                   + "WHERE R.PAY_YEAR BETWEEN ? AND ? "
                   + "GROUP BY R.PAY_YEAR "
                   + "ORDER BY R.PAY_YEAR ASC";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, startYear);
            pstmt.setString(2, endYear);
            try (ResultSet rs = pstmt.executeQuery()) {
                List<PayrollStatItem> list = new ArrayList<>();
                while (rs.next()) {
                    PayrollStatItem item = new PayrollStatItem();
                    item.setYear(rs.getInt("PAY_YEAR"));
                    item.setRawTotalPay(rs.getLong("TOTAL_PAY"));
                    // 연도별 평균 인원 = 1년간 발급된 총 명세서 수 / 12개월
                    item.setRawHeadcount(rs.getInt("TOTAL_SLIPS") / 12.0); 
                    list.add(item);
                }
                return list;
            }
        }
    }

    // 월별 데이터 조회 (특정 연도)
    public List<PayrollStatItem> selectMonthlyStatRaw(Connection conn, String year) throws SQLException {
        String sql = "SELECT R.PAY_MONTH, "
                   + "       NVL(SUM(CASE WHEN EN.PAY_ITEM_ID IS NOT NULL THEN EN.AMOUNT ELSE 0 END), 0) AS TOTAL_PAY, "
                   + "       COUNT(DISTINCT PE.EMPLOYEE_ID) AS EMP_COUNT "
                   + "FROM PAYROLL_RUN R "
                   + "JOIN PAYROLL_EMPLOYEE PE ON R.PAYROLL_RUN_ID = PE.PAYROLL_RUN_ID "
                   + "LEFT JOIN PAYROLL_ENTRY EN ON PE.PAYROLL_EMPLOYEE_ID = EN.PAYROLL_EMPLOYEE_ID "
                   + "WHERE R.PAY_YEAR = ? "
                   + "GROUP BY R.PAY_MONTH "
                   + "ORDER BY R.PAY_MONTH ASC";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, year);
            try (ResultSet rs = pstmt.executeQuery()) {
                List<PayrollStatItem> list = new ArrayList<>();
                while (rs.next()) {
                    PayrollStatItem item = new PayrollStatItem();
                    item.setMonth(rs.getInt("PAY_MONTH"));
                    item.setRawTotalPay(rs.getLong("TOTAL_PAY"));
                    item.setRawHeadcount(rs.getInt("EMP_COUNT"));
                    list.add(item);
                }
                return list;
            }
        }
    }
}