package erp.payroll.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import erp.payroll.dao.PayrollStatDao;
import erp.payroll.dto.PayrollStatItem;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

public class PayrollStatService {
    private PayrollStatDao statDao = new PayrollStatDao();

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
    public List<PayrollStatItem> getAnnualStatistics(int baseYear) {
        Connection conn = null;
        try {
            conn = ConnectionProvider.getConnection();
            int startYear = baseYear - 9;
            List<PayrollStatItem> rawList = statDao.selectAnnualStatRaw(conn, String.valueOf(startYear), String.valueOf(baseYear));
            
            // 빈 연도 채워넣기 (데이터가 없는 연도도 0으로 표시하기 위함)
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
    public List<PayrollStatItem> getMonthlyStatistics(int baseYear) {
        Connection conn = null;
        try {
            conn = ConnectionProvider.getConnection();
            List<PayrollStatItem> rawList = statDao.selectMonthlyStatRaw(conn, String.valueOf(baseYear));
            
            // 1월~12월 빈 월 채워넣기
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
    private void calculateRatesAndFormats(List<PayrollStatItem> list, boolean isAnnual) {
        DecimalFormat commaFormat = new DecimalFormat("#,###");
        DecimalFormat percentFormat = new DecimalFormat("#,##0.0'%'");
        DecimalFormat floatFormat = new DecimalFormat("#,##0.0");

        long maxPay = 0;
        double maxHeadcount = 0;

        // 최댓값 구하기 (차트 막대 높이용)
        for (PayrollStatItem item : list) {
            if (item.getRawTotalPay() > maxPay) maxPay = item.getRawTotalPay();
            if (item.getRawHeadcount() > maxHeadcount) maxHeadcount = item.getRawHeadcount();
        }

        // 증감률 및 포맷팅
        for (int i = 0; i < list.size(); i++) {
            PayrollStatItem curr = list.get(i);
            
            // 차트 비율 계산 (0~100)
            curr.setPayrollBarRate(maxPay == 0 ? 0 : (int) ((double) curr.getRawTotalPay() / maxPay * 100));
            curr.setHeadcountBarRate(maxHeadcount == 0 ? 0 : (int) (curr.getRawHeadcount() / maxHeadcount * 100));

            // 텍스트 포맷팅 (급여는 천원 단위)
            curr.setTotalPayrollText(commaFormat.format(curr.getRawTotalPay() / 1000));
            if (isAnnual) {
                curr.setHeadcountText(floatFormat.format(curr.getRawHeadcount())); // 연도별은 소수점 1자리(평균)
            } else {
                curr.setHeadcountText(String.valueOf((int) curr.getRawHeadcount())); // 월별은 정수
            }

            // 증감률 계산
            if (i == 0) {
                curr.setPayrollGrowth(0);
                curr.setPayrollGrowthText("-");
                curr.setHeadcountGrowth(0);
                curr.setHeadcountGrowthText("-");
            } else {
                PayrollStatItem prev = list.get(i - 1);
                
                // 급여 증감률
                double payGrowth = prev.getRawTotalPay() == 0 ? (curr.getRawTotalPay() > 0 ? 100.0 : 0.0) 
                        : (double) (curr.getRawTotalPay() - prev.getRawTotalPay()) / prev.getRawTotalPay() * 100;
                curr.setPayrollGrowth(payGrowth);
                curr.setPayrollGrowthText(percentFormat.format(payGrowth));

                // 인원 증감률
                double headGrowth = prev.getRawHeadcount() == 0 ? (curr.getRawHeadcount() > 0 ? 100.0 : 0.0) 
                        : (curr.getRawHeadcount() - prev.getRawHeadcount()) / prev.getRawHeadcount() * 100;
                curr.setHeadcountGrowth(headGrowth);
                curr.setHeadcountGrowthText(percentFormat.format(headGrowth));
            }
        }
    }

    // 헬퍼 메서드
    private PayrollStatItem findItemByYear(List<PayrollStatItem> list, int year) {
        for (PayrollStatItem item : list) if (item.getYear() == year) return item;
        PayrollStatItem empty = new PayrollStatItem(); empty.setYear(year); return empty;
    }

    private PayrollStatItem findItemByMonth(List<PayrollStatItem> list, int month) {
        for (PayrollStatItem item : list) if (item.getMonth() == month) return item;
        PayrollStatItem empty = new PayrollStatItem(); empty.setMonth(month); return empty;
    }
}