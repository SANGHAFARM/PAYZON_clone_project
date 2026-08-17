package erp.payroll.dto;

public class PayrollStatItem {
    // 1. DB에서 조회할 원본 데이터 (Service에서 계산용으로 사용)
    private long rawTotalPay;
    private double rawHeadcount;

    // 2. JSP(View)에서 사용할 데이터
    private int year;
    private int month;
    
    private String totalPayrollText;     // 전체 급여액 (천원)
    private double payrollGrowth;        // 급여 증가율 (CSS 클래스용)
    private String payrollGrowthText;    // 급여 증가율 텍스트
    
    private String headcountText;        // 인원
    private double headcountGrowth;      // 인원 증가율 (CSS 클래스용)
    private String headcountGrowthText;  // 인원 증가율 텍스트
    
    private int payrollBarRate;          // 급여액 막대 높이 비율 (0~100)
    private int headcountBarRate;        // 인원 막대 높이 비율 (0~100)

    // Getter & Setter (이클립스에서 자동 생성 기능으로 만드셔도 됩니다)
    public long getRawTotalPay() { return rawTotalPay; }
    public void setRawTotalPay(long rawTotalPay) { this.rawTotalPay = rawTotalPay; }
    public double getRawHeadcount() { return rawHeadcount; }
    public void setRawHeadcount(double rawHeadcount) { this.rawHeadcount = rawHeadcount; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }
    public String getTotalPayrollText() { return totalPayrollText; }
    public void setTotalPayrollText(String totalPayrollText) { this.totalPayrollText = totalPayrollText; }
    public double getPayrollGrowth() { return payrollGrowth; }
    public void setPayrollGrowth(double payrollGrowth) { this.payrollGrowth = payrollGrowth; }
    public String getPayrollGrowthText() { return payrollGrowthText; }
    public void setPayrollGrowthText(String payrollGrowthText) { this.payrollGrowthText = payrollGrowthText; }
    public String getHeadcountText() { return headcountText; }
    public void setHeadcountText(String headcountText) { this.headcountText = headcountText; }
    public double getHeadcountGrowth() { return headcountGrowth; }
    public void setHeadcountGrowth(double headcountGrowth) { this.headcountGrowth = headcountGrowth; }
    public String getHeadcountGrowthText() { return headcountGrowthText; }
    public void setHeadcountGrowthText(String headcountGrowthText) { this.headcountGrowthText = headcountGrowthText; }
    public int getPayrollBarRate() { return payrollBarRate; }
    public void setPayrollBarRate(int payrollBarRate) { this.payrollBarRate = payrollBarRate; }
    public int getHeadcountBarRate() { return headcountBarRate; }
    public void setHeadcountBarRate(int headcountBarRate) { this.headcountBarRate = headcountBarRate; }
}