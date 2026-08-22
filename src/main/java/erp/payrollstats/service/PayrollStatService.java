package erp.payrollstats.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import erp.payrollstats.dao.PayrollStatDao;
import erp.payrollstats.dto.PayrollStatItem;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

// 급여통계 업무 규칙과 데이터 변경 트랜잭션을 처리한다.
// 給与統計の業務ルールとデータ変更トランザクションを処理する。
public class PayrollStatService {
    private PayrollStatDao statDao = new PayrollStatDao();

    // 급여통계 처리에 필요한 사용가능연도 목록를 조회하거나 계산하여 반환한다.
    // 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
    // 給与統計処理に必要な利用可能年度一覧を照会または計算して返す。
    // 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
    public List<Integer> getAvailableYears() {
        Connection conn = null;
        try {
            conn = ConnectionProvider.getConnection();
            return statDao.selectAvailableYears(conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtil.close(conn);
        }
    }

    // 연도별 통계 데이터 생성 로직
    // 급여통계 처리에 필요한 연간통계를 조회하거나 계산하여 반환한다.
    // 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
    // 給与統計処理に必要な年間統計を照会または計算して返す。
    // 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
    public List<PayrollStatItem> getAnnualStatistics(int baseYear) {
        Connection conn = null;
        try {
            conn = ConnectionProvider.getConnection();
            int startYear = baseYear - 9;
            List<PayrollStatItem> rawList = statDao.selectAnnualStatRaw(conn, String.valueOf(startYear), String.valueOf(baseYear));
            
            // 빈 연도 채워넣기 (데이터가 없는 연도도 0으로 표시하기 위함)
            // 基準日と期間の境界を確認し、照会・計算に使用できる日付形式へ変換する。
            List<PayrollStatItem> processedList = new ArrayList<>();
            for (int y = startYear; y <= baseYear; y++) {
                PayrollStatItem item = findItemByYear(rawList, y);
                processedList.add(item);
            }
            
            calculateRatesAndFormats(processedList, true);
            return processedList;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtil.close(conn);
        }
    }

    // 월별 통계 데이터 생성 로직
    // 급여통계 처리에 필요한 월간통계를 조회하거나 계산하여 반환한다.
    // 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
    // 給与統計処理に必要な月間統計を照会または計算して返す。
    // 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
    public List<PayrollStatItem> getMonthlyStatistics(int baseYear) {
        Connection conn = null;
        try {
            conn = ConnectionProvider.getConnection();
            List<PayrollStatItem> rawList = statDao.selectMonthlyStatRaw(conn, String.valueOf(baseYear));
            
            // 1월~12월 빈 월 채워넣기
            // 基準日と期間の境界を確認し、照会・計算に使用できる日付形式へ変換する。
            List<PayrollStatItem> processedList = new ArrayList<>();
            for (int m = 1; m <= 12; m++) {
                PayrollStatItem item = findItemByMonth(rawList, m);
                processedList.add(item);
            }
            
            calculateRatesAndFormats(processedList, false);
            return processedList;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtil.close(conn);
        }
    }

    // 증감률, 막대비율, 포맷팅 공통 계산 메서드
    // 조회된 금액과 업무 규칙을 이용해 비율 목록And표시형식 계산 결과를 생성한다.
    // 입력금액과 기간·요율·절사단위를 적용하고 계산 중 발생할 수 있는 NULL이나 음수 결과를 안전하게 보정한다.
    // 照会した金額と業務ルールを使用して比率一覧And表示形式の計算結果を生成する。
    // 入力金額と期間・料率・端数処理単位を適用し、計算中に発生し得るNULLや負数結果を安全に補正する。
    private void calculateRatesAndFormats(List<PayrollStatItem> list, boolean isAnnual) {
        DecimalFormat commaFormat = new DecimalFormat("#,###");
        DecimalFormat percentFormat = new DecimalFormat("#,##0.0'%'");
        DecimalFormat floatFormat = new DecimalFormat("#,##0.0");

        long maxPay = 0;
        double maxHeadcount = 0;

        // 최댓값 구하기 (차트 막대 높이용)
        // 金額が0の場合もグラフ項目を確認できるよう、表示用の最小高さを適用する。
        for (PayrollStatItem item : list) {
            if (item.getRawTotalPay() > maxPay) maxPay = item.getRawTotalPay();
            if (item.getRawHeadcount() > maxHeadcount) maxHeadcount = item.getRawHeadcount();
        }

        // 증감률 및 포맷팅
        // 対象データを順番に繰り返し処理し、各要素へ同じ業務基準を適用する。
        for (int i = 0; i < list.size(); i++) {
            PayrollStatItem curr = list.get(i);
            
            // 차트 비율 계산 (0~100)
            // 支給額・控除額・期間値を業務基準に従って計算し、合計と最終金額へ反映する。
            curr.setPayrollBarRate(maxPay == 0 ? 0 : (int) ((double) curr.getRawTotalPay() / maxPay * 100));
            curr.setHeadcountBarRate(maxHeadcount == 0 ? 0 : (int) (curr.getRawHeadcount() / maxHeadcount * 100));

            // 텍스트 포맷팅 (급여는 천원 단위)
            // 支給額・控除額・期間値を業務基準に従って計算し、合計と最終金額へ反映する。
            curr.setTotalPayrollText(commaFormat.format(curr.getRawTotalPay() / 1000));
            if (isAnnual) {
                curr.setHeadcountText(floatFormat.format(curr.getRawHeadcount())); // 연도별은 소수점 1자리(평균)
            } else {
                curr.setHeadcountText(String.valueOf((int) curr.getRawHeadcount())); // 월별은 정수
            }

            // 증감률 계산
            // 支給額・控除額・期間値を業務基準に従って計算し、合計と最終金額へ反映する。
            if (i == 0) {
                curr.setPayrollGrowth(0);
                curr.setPayrollGrowthText("-");
                curr.setHeadcountGrowth(0);
                curr.setHeadcountGrowthText("-");
            } else {
                PayrollStatItem prev = list.get(i - 1);
                
                // 급여 증감률
                // 支給額・控除額・期間値を業務基準に従って計算し、合計と最終金額へ反映する。
                double payGrowth = prev.getRawTotalPay() == 0 ? (curr.getRawTotalPay() > 0 ? 100.0 : 0.0) 
                        : (double) (curr.getRawTotalPay() - prev.getRawTotalPay()) / prev.getRawTotalPay() * 100;
                curr.setPayrollGrowth(payGrowth);
                curr.setPayrollGrowthText(percentFormat.format(payGrowth));

                // 인원 증감률
                // 前期間と当期間の人数差を基準値で割り、人数の増減率を算出する。
                double headGrowth = prev.getRawHeadcount() == 0 ? (curr.getRawHeadcount() > 0 ? 100.0 : 0.0) 
                        : (curr.getRawHeadcount() - prev.getRawHeadcount()) / prev.getRawHeadcount() * 100;
                curr.setHeadcountGrowth(headGrowth);
                curr.setHeadcountGrowthText(percentFormat.format(headGrowth));
            }
        }
    }

    // 헬퍼 메서드
    // 급여통계 처리에 필요한 항목By연도 데이터를 조회하여 반환한다.
    // 조회 전용 Connection으로 관련 DAO 결과를 조합하며 데이터 변경이 없으므로 별도의 commit이나 rollback을 수행하지 않는다.
    // 給与統計処理に必要な項目By年度データを照会して返す。
    // 照会専用Connectionで関連DAOの結果を組み合わせ、データ変更がないため個別のcommitやrollbackは実行しない。
    private PayrollStatItem findItemByYear(List<PayrollStatItem> list, int year) {
        for (PayrollStatItem item : list) if (item.getYear() == year) return item;
        PayrollStatItem empty = new PayrollStatItem(); empty.setYear(year); return empty;
    }

    // 급여통계 처리에 필요한 항목By월 데이터를 조회하여 반환한다.
    // 조회 전용 Connection으로 관련 DAO 결과를 조합하며 데이터 변경이 없으므로 별도의 commit이나 rollback을 수행하지 않는다.
    // 給与統計処理に必要な項目By月データを照会して返す。
    // 照会専用Connectionで関連DAOの結果を組み合わせ、データ変更がないため個別のcommitやrollbackは実行しない。
    private PayrollStatItem findItemByMonth(List<PayrollStatItem> list, int month) {
        for (PayrollStatItem item : list) if (item.getMonth() == month) return item;
        PayrollStatItem empty = new PayrollStatItem(); empty.setMonth(month); return empty;
    }
}
