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

// 개인별연간통계 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 個人別年間統計の業務ルールとデータ変更トランザクションを処理する。
public class PersonalAnnualStatService {

    private PersonalAnnualStatDao statDao = new PersonalAnnualStatDao();

    // 개인별연간통계 처리에 필요한 연간통계화면 데이터를 조회하거나 계산하여 반환한다.
    // 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
    // 個人別年間統計処理に必要な年間統計画面データを照会または計算して返す。
    // 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
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
            // 支給額・控除額・期間値を業務基準に従って計算し、合計と最終金額へ反映する。
            int startYear = baseYearInt - 9;

            pageData.put("availableYears", availableYears);
            pageData.put("selectedYear", baseYear);
            pageData.put("selectedEmployeeNo", empNo);

            // 사원 검색 (전체 목록)
            // 検索語と選択条件を組み合わせ、利用者が指定した対象だけを照会する。
            List<Map<String, String>> empList = statDao.searchEmployees(conn, empKeyword);
            pageData.put("employeeOptions", empList);

            String realEmpName = "";
            Map<Integer, long[]> dbAnnualData = new HashMap<>();

            // 사원이 선택된 경우에만 DB 데이터 조회
            // 社員の識別情報と在職・雇用・所属条件を確認し、対象社員データへ反映する。
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
            // 検索語と選択条件を組み合わせ、利用者が指定した対象だけを照会する。
            for (int year = startYear; year <= baseYearInt; year++) {
                Map<String, Object> stat = new HashMap<>();
                stat.put("year", year);

                long currentGross = 0;
                long totalDeduct = 0;
                long prevGross = 0;

                // dbAnnualData에 해당 연도의 데이터가 있다면 값을 가져옴
                // 基準日と期間の境界を確認し、照会・計算に使用できる日付形式へ変換する。
                if (dbAnnualData.containsKey(year)) {
                    currentGross = dbAnnualData.get(year)[0];
                    totalDeduct = dbAnnualData.get(year)[1];
                }
                if (dbAnnualData.containsKey(year - 1)) {
                    prevGross = dbAnnualData.get(year - 1)[0];
                }

                long netPay = currentGross - totalDeduct;

                // JSP로 넘길 텍스트 세팅
                // 画面表示に必要なデータとメッセージをrequestまたはsessionへ保存し、JSPへ引き渡す。
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
                // 支給額・控除額・期間値を業務基準に従って計算し、合計と最終金額へ反映する。
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
