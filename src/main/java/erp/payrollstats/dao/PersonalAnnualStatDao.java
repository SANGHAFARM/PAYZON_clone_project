package erp.payrollstats.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdbc.JdbcUtil;

// 개인별연간통계 데이터를 데이터베이스에서 조회하고 저장·수정·삭제한다.
// 個人別年間統計データをデータベースから照会し、登録・更新・削除する。
public class PersonalAnnualStatDao {

    // 조회 조건에 맞는 연간통계 데이터를 데이터베이스에서 조회한다.
    // Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
    // 検索条件に合う年間統計データをデータベースから照会する。
    // Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
    public Map<Integer, long[]> selectAnnualStat(Connection conn, int startYear, int endYear, String empNo) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Map<Integer, long[]> resultMap = new HashMap<>();

        String sql = "SELECT " +
                     "    TO_NUMBER(PR.PAY_YEAR) AS YEAR, " +
                     "    SUM(CASE WHEN PE.PAY_ITEM_ID IS NOT NULL THEN PE.AMOUNT ELSE 0 END) AS GROSS_PAY, " +
                     "    SUM(CASE WHEN PE.DEDUCT_ITEM_ID IS NOT NULL THEN PE.AMOUNT ELSE 0 END) AS TOTAL_DEDUCT " +
                     "FROM PAYROLL_RUN PR " +
                     "JOIN PAYROLL_EMPLOYEE PEM ON PR.PAYROLL_RUN_ID = PEM.PAYROLL_RUN_ID " +
                     "JOIN PAYROLL_ENTRY PE ON PEM.PAYROLL_EMPLOYEE_ID = PE.PAYROLL_EMPLOYEE_ID " +
                     "JOIN EMPLOYEE E ON PEM.EMPLOYEE_ID = E.EMPLOYEE_ID " +
                     "WHERE TO_NUMBER(PR.PAY_YEAR) BETWEEN ? AND ? " +
                     "  AND E.EMP_NO = ? " +
                     "GROUP BY PR.PAY_YEAR";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, startYear);
            pstmt.setInt(2, endYear);
            pstmt.setString(3, empNo);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                int year = rs.getInt("YEAR");
                long grossPay = rs.getLong("GROSS_PAY");
                long totalDeduct = rs.getLong("TOTAL_DEDUCT");
                resultMap.put(year, new long[]{grossPay, totalDeduct});
            }
            return resultMap;
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 조회 조건에 맞는 사용가능연도 목록 데이터를 데이터베이스에서 조회한다.
    // Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
    // 検索条件に合う利用可能年度一覧データをデータベースから照会する。
    // Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
    public List<String> selectAvailableYears(Connection conn) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<String> years = new ArrayList<>();
        String sql = "SELECT DISTINCT PAY_YEAR FROM PAYROLL_RUN ORDER BY PAY_YEAR DESC";

        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                years.add(rs.getString("PAY_YEAR"));
            }
            return years;
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 조회 조건에 맞는 사원 데이터를 데이터베이스에서 조회한다.
    // Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
    // 検索条件に合う社員データをデータベースから照会する。
    // Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
    public List<Map<String, String>> searchEmployees(Connection conn, String keyword) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Map<String, String>> empList = new ArrayList<>();
        
        boolean hasKeyword = (keyword != null && !keyword.trim().isEmpty());
        
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT E.EMP_NO, E.EMP_TYPE, E.EMP_NAME_KR, D.DEPARTMENT_NAME, J.JOB_POSITION_NAME, E.STATUS ")
           .append("FROM EMPLOYEE E ")
           .append("LEFT JOIN DEPARTMENT D ON E.DEPARTMENT_ID = D.DEPARTMENT_ID ")
           .append("LEFT JOIN JOB_POSITION J ON E.JOB_POSITION_ID = J.JOB_POSITION_ID ");
           
        // 💡 수정된 부분: 검색어가 있을 때만 WHERE 조건 추가, 없으면 전체 조회
        // 検索語と選択条件を組み合わせ、利用者が指定した対象だけを照会する。
        if (hasKeyword) {
            sql.append("WHERE E.EMP_NAME_KR LIKE ? ")
               .append("   OR E.EMP_NO LIKE ? ")
               .append("   OR D.DEPARTMENT_NAME LIKE ? ");
        }
        
        sql.append("ORDER BY E.EMP_NO");

        try {
            pstmt = conn.prepareStatement(sql.toString());
            
            // 💡 수정된 부분: 검색어가 있을 때만 파라미터(물음표) 세팅
            // リクエストから入力値を取得し、空白除去と形式変換を行って業務処理へ渡す。
            if (hasKeyword) {
                String searchVal = "%" + keyword.trim() + "%";
                pstmt.setString(1, searchVal);
                pstmt.setString(2, searchVal);
                pstmt.setString(3, searchVal);
            }
            
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Map<String, String> emp = new HashMap<>();
                emp.put("employeeNo", rs.getString("EMP_NO"));
                emp.put("type", rs.getString("EMP_TYPE"));
                emp.put("name", rs.getString("EMP_NAME_KR"));
                emp.put("department", rs.getString("DEPARTMENT_NAME") != null ? rs.getString("DEPARTMENT_NAME") : "-");
                emp.put("position", rs.getString("JOB_POSITION_NAME") != null ? rs.getString("JOB_POSITION_NAME") : "-");
                emp.put("status", rs.getString("STATUS"));
                empList.add(emp);
            }
            return empList;
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    // 조회 조건에 맞는 사원명칭 데이터를 데이터베이스에서 조회한다.
    // Connection과 조회조건으로 PreparedStatement를 실행하고 ResultSet의 각 컬럼을 Model 또는 DTO로 변환한다.
    // 検索条件に合う社員名称データをデータベースから照会する。
    // Connectionと検索条件でPreparedStatementを実行し、ResultSetの各カラムをModelまたはDTOへ変換する。
    public String selectEmployeeName(Connection conn, String empNo) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT EMP_NAME_KR FROM EMPLOYEE WHERE EMP_NO = ?";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, empNo);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("EMP_NAME_KR");
            }
            return "";
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }
}
