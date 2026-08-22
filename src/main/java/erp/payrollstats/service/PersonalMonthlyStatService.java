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

// 개인별월간통계 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 個人別月間統計の業務ルールとデータ変更トランザクションを処理する。
public class PersonalMonthlyStatService {

    private PersonalStatDao statDao = new PersonalStatDao();

    // 개인별월간통계 처리에 필요한 월간통계화면 데이터를 조회하거나 계산하여 반환한다.
    // 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
    // 個人別月間統計処理に必要な月間統計画面データを照会または計算して返す。
    // 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
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
            // 検索語と選択条件を組み合わせ、利用者が指定した対象だけを照会する。
            List<Map<String, String>> empList = statDao.searchEmployees(conn, empKeyword);
            pageData.put("employeeOptions", empList);

            String realEmpName = "";
            Map<Integer, long[]> dbMonthlyData = new HashMap<>();

            // 사원이 선택된 경우에만 실제 데이터 조회
            // 社員の識別情報と在職・雇用・所属条件を確認し、対象社員データへ反映する。
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
            // 検索語と選択条件を組み合わせ、利用者が指定した対象だけを照会する。
            for (int month = 1; month <= 12; month++) {
                Map<String, Object> monthData = new HashMap<>();
                monthData.put("month", month);

                long grossPay = 0;
                long totalDeduct = 0;

                // 사원이 선택되어 데이터가 있을 때만 값 채우기
                // 社員の識別情報と在職・雇用・所属条件を確認し、対象社員データへ反映する。
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
                // 支給額・控除額・期間値を業務基準に従って計算し、合計と最終金額へ反映する。
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
