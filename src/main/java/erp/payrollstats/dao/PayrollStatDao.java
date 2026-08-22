package erp.payrollstats.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import erp.payrollstats.dto.PayrollStatItem;

// 급여통계 데이터를 데이터베이스에서 조회하고 저장·수정·삭제한다.
// 給与統計データをデータベースから照会し、登録・更新・削除する。
public class PayrollStatDao {

    // 검색 콤보박스용 귀속연도 목록 조회
    // 조회 조건에 맞는 사용가능연도 목록 데이터를 데이터베이스에서 조회한다.
    // Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
    // 検索条件に合う利用可能年度一覧データをデータベースから照会する。
    // Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
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
    // 조회 조건에 맞는 연간통계원본 데이터를 데이터베이스에서 조회한다.
    // Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
    // 検索条件に合う年間統計元データをデータベースから照会する。
    // Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
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
                    // 基準日と期間の境界を確認し、照会・計算に使用できる日付形式へ変換する。
                    item.setRawHeadcount(rs.getInt("TOTAL_SLIPS") / 12.0); 
                    list.add(item);
                }
                return list;
            }
        }
    }

    // 월별 데이터 조회 (특정 연도)
    // 조회 조건에 맞는 월간통계원본 데이터를 데이터베이스에서 조회한다.
    // Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
    // 検索条件に合う月間統計元データをデータベースから照会する。
    // Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
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
