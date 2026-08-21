package erp.payrollstats.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import erp.payrollstats.dao.PersonalStatDao;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

public class PersonalMonthlyStatService {

    private PersonalStatDao statDao = new PersonalStatDao();

    public Map<String, Object> getMonthlyStatPage(String baseYear, String empNo, String empKeyword) {
        Connection conn = null;
        Map<String, Object> pageData = new HashMap<>();

        try {
            conn = ConnectionProvider.getConnection();

            List<String> availableYears = statDao.selectAvailableYears(conn);
            if (availableYears.isEmpty()) {
                availableYears.add(String.valueOf(LocalDate.now().getYear()));
            }
            if (baseYear == null || baseYear.isEmpty()) {
                baseYear = availableYears.get(0);
            }

            pageData.put("availableYears", availableYears);
            pageData.put("selectedYear", baseYear);
            pageData.put("selectedEmployeeNo", empNo);
            
            // 사원 검색 (전체 목록)
            List<Map<String, String>> empList = statDao.searchEmployees(conn, empKeyword);
            pageData.put("employeeOptions", empList);

            String realEmpName = "";
            Map<Integer, long[]> dbMonthlyData = new HashMap<>();

            // 사원이 선택된 경우에만 실제 데이터 조회
            if (empNo != null && !empNo.isEmpty()) {
                realEmpName = statDao.selectEmployeeName(conn, empNo);
                dbMonthlyData = statDao.selectMonthlyStat(conn, baseYear, empNo);
            }
            pageData.put("selectedEmployeeName", realEmpName);

            long maxGrossPay = 0;
            for (int i = 1; i <= 12; i++) {
                if (dbMonthlyData.containsKey(i)) {
                    long gross = dbMonthlyData.get(i)[0];
                    if (gross > maxGrossPay) maxGrossPay = gross;
                }
            }
            double chartScaleBase = maxGrossPay > 0 ? maxGrossPay * 1.1 : 1;

            List<Map<String, Object>> monthlyStats = new ArrayList<>();
            long sumGrossPay = 0;
            long sumDeduct = 0;
            long sumNetPay = 0;

            // 무조건 1월~12월 루프를 돌려서 빈 틀을 생성
            for (int month = 1; month <= 12; month++) {
                Map<String, Object> monthData = new HashMap<>();
                monthData.put("month", month);

                long grossPay = 0;
                long totalDeduct = 0;

                // 사원이 선택되어 데이터가 있을 때만 값 채우기
                if (dbMonthlyData.containsKey(month)) {
                    grossPay = dbMonthlyData.get(month)[0];
                    totalDeduct = dbMonthlyData.get(month)[1];
                }
                long netPay = grossPay - totalDeduct;

                sumGrossPay += grossPay;
                sumDeduct += totalDeduct;
                sumNetPay += netPay;

                monthData.put("monthlySalaryText", String.format("%,d", grossPay / 1000));
                monthData.put("deductionText", String.format("%,d", totalDeduct / 1000));
                monthData.put("netSalaryText", String.format("%,d", netPay / 1000));
                
                // 💡 수정된 부분: 차트 높이 계산 (0원이면 기본 높이 10% 할당)
                int barRate = (int) Math.round((grossPay / chartScaleBase) * 100);
                monthData.put("salaryBarRate", grossPay == 0 ? 10 : barRate);
                
                monthData.put("deductionShareRate", grossPay > 0 ? (int) Math.round(((double) totalDeduct / grossPay) * 100) : 0);

                monthlyStats.add(monthData);
            }
            
            pageData.put("monthlySalaryStats", monthlyStats);
            pageData.put("totalSalaryYearText", String.format("%,d", sumGrossPay / 1000));
            pageData.put("totalDeductionYearText", String.format("%,d", sumDeduct / 1000));
            pageData.put("totalNetYearText", String.format("%,d", sumNetPay / 1000));

            return pageData;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtil.close(conn);
        }
    }
}