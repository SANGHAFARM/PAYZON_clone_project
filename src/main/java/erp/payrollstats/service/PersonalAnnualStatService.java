package erp.payrollstats.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import erp.payrollstats.dao.PersonalAnnualStatDao;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

public class PersonalAnnualStatService {

    private PersonalAnnualStatDao statDao = new PersonalAnnualStatDao();

    public Map<String, Object> getAnnualStatPage(String baseYear, String empNo, String empKeyword) {
        Connection conn = null;
        Map<String, Object> pageData = new HashMap<>();

        try {
            conn = ConnectionProvider.getConnection();

            List<String> availableYears = statDao.selectAvailableYears(conn);
            if (availableYears.isEmpty()) {
                availableYears.add(String.valueOf(LocalDate.now().getYear()));
            }

            int baseYearInt;
            if (baseYear == null || baseYear.isEmpty()) {
                baseYear = availableYears.get(0);
                baseYearInt = Integer.parseInt(baseYear);
            } else {
                baseYearInt = Integer.parseInt(baseYear);
            }

            // 최근 10개년 계산
            int startYear = baseYearInt - 9;

            pageData.put("availableYears", availableYears);
            pageData.put("selectedYear", baseYear);
            pageData.put("selectedEmployeeNo", empNo);

            // 사원 검색 (전체 목록)
            List<Map<String, String>> empList = statDao.searchEmployees(conn, empKeyword);
            pageData.put("employeeOptions", empList);

            String realEmpName = "";
            Map<Integer, long[]> dbAnnualData = new HashMap<>();

            // 사원이 선택된 경우에만 DB 데이터 조회
            if (empNo != null && !empNo.isEmpty()) {
                realEmpName = statDao.selectEmployeeName(conn, empNo);
                dbAnnualData = statDao.selectAnnualStat(conn, startYear - 1, baseYearInt, empNo);
            }
            pageData.put("selectedEmployeeName", realEmpName);

            long maxGrossPay = 0;
            for (int year = startYear; year <= baseYearInt; year++) {
                if (dbAnnualData.containsKey(year)) {
                    long gross = dbAnnualData.get(year)[0];
                    if (gross > maxGrossPay) maxGrossPay = gross;
                }
            }
            double chartScaleBase = maxGrossPay > 0 ? maxGrossPay * 1.05 : 1;

            List<Map<String, Object>> annualStats = new ArrayList<>();

            // 사원 선택 유무와 관계없이 무조건 10개년 루프를 돌려서 데이터를 생성합니다.
            for (int year = startYear; year <= baseYearInt; year++) {
                Map<String, Object> stat = new HashMap<>();
                stat.put("year", year);

                long currentGross = 0;
                long totalDeduct = 0;
                long prevGross = 0;

                // dbAnnualData에 해당 연도의 데이터가 있다면 값을 가져옴
                if (dbAnnualData.containsKey(year)) {
                    currentGross = dbAnnualData.get(year)[0];
                    totalDeduct = dbAnnualData.get(year)[1];
                }
                if (dbAnnualData.containsKey(year - 1)) {
                    prevGross = dbAnnualData.get(year - 1)[0];
                }

                long netPay = currentGross - totalDeduct;

                // JSP로 넘길 텍스트 세팅
                stat.put("annualSalaryText", String.format("%,d", currentGross / 1000));
                stat.put("deductionText", String.format("%,d", totalDeduct / 1000));
                stat.put("netSalaryText", String.format("%,d", netPay / 1000));

                double growthRate = 0;
                String growthText = "-";

                if (year == startYear) {
                    growthText = "-";
                } else if (prevGross > 0) {
                    growthRate = ((double)(currentGross - prevGross) / prevGross) * 100;
                    growthText = String.format("%.1f%%", growthRate);
                } else if (prevGross == 0 && currentGross > 0) {
                    growthRate = 100;
                    growthText = "100.0%";
                }

                stat.put("salaryGrowth", growthRate);
                stat.put("salaryGrowthText", growthText);

                // 💡 수정된 부분: 차트 높이 계산 (0원이면 기본 높이 10% 할당)
                int salaryBarRate = (int) Math.round((currentGross / chartScaleBase) * 100);
                stat.put("salaryBarRate", currentGross == 0 ? 10 : salaryBarRate);
                
                int deductionShareRate = currentGross > 0 ? (int) Math.round(((double) totalDeduct / currentGross) * 100) : 0;
                stat.put("deductionShareRate", deductionShareRate);
                
                annualStats.add(stat);
            }

            pageData.put("salaryStats", annualStats); // 핸들러로 전달

            return pageData;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtil.close(conn);
        }
    }
}