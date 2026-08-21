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

public class PersonalStatDao {

    public Map<Integer, long[]> selectMonthlyStat(Connection conn, String year, String empNo) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Map<Integer, long[]> resultMap = new HashMap<>();

        String sql = "SELECT " +
                     "    TO_NUMBER(PR.PAY_MONTH) AS MONTH, " +
                     "    SUM(CASE WHEN PE.PAY_ITEM_ID IS NOT NULL THEN PE.AMOUNT ELSE 0 END) AS GROSS_PAY, " +
                     "    SUM(CASE WHEN PE.DEDUCT_ITEM_ID IS NOT NULL THEN PE.AMOUNT ELSE 0 END) AS TOTAL_DEDUCT " +
                     "FROM PAYROLL_RUN PR " +
                     "JOIN PAYROLL_EMPLOYEE PEM ON PR.PAYROLL_RUN_ID = PEM.PAYROLL_RUN_ID " +
                     "JOIN PAYROLL_ENTRY PE ON PEM.PAYROLL_EMPLOYEE_ID = PE.PAYROLL_EMPLOYEE_ID " +
                     "JOIN EMPLOYEE E ON PEM.EMPLOYEE_ID = E.EMPLOYEE_ID " +
                     "WHERE PR.PAY_YEAR = ? AND E.EMP_NO = ? " +
                     "GROUP BY PR.PAY_MONTH";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, year);
            pstmt.setString(2, empNo);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                resultMap.put(rs.getInt("MONTH"), new long[]{rs.getLong("GROSS_PAY"), rs.getLong("TOTAL_DEDUCT")});
            }
            return resultMap;
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

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

    public List<Map<String, String>> searchEmployees(Connection conn, String keyword) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Map<String, String>> empList = new ArrayList<>();
        
        boolean hasKeyword = (keyword != null && !keyword.trim().isEmpty());
        
        // 💡 수정됨: 검색어가 없을 때 빈 리스트를 반환하던 조건문을 삭제했습니다. (연도별과 동일하게 통일)
        
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT E.EMP_NO, E.EMP_TYPE, E.EMP_NAME_KR, D.DEPARTMENT_NAME, J.JOB_POSITION_NAME, E.STATUS ")
           .append("FROM EMPLOYEE E ")
           .append("LEFT JOIN DEPARTMENT D ON E.DEPARTMENT_ID = D.DEPARTMENT_ID ")
           .append("LEFT JOIN JOB_POSITION J ON E.JOB_POSITION_ID = J.JOB_POSITION_ID ");
           
        // 💡 검색어가 있을 때만 WHERE 절을 추가합니다.
        if (hasKeyword) {
            sql.append("WHERE E.EMP_NAME_KR LIKE ? ")
               .append("   OR E.EMP_NO LIKE ? ")
               .append("   OR D.DEPARTMENT_NAME LIKE ? ");
        }
        
        sql.append("ORDER BY E.EMP_NO");

        try {
            pstmt = conn.prepareStatement(sql.toString());
            
            // 💡 검색어가 있을 때만 ? 에 값을 세팅합니다.
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