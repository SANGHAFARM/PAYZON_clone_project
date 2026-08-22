package erp.payrollstats.dto;

// 급여통계항목 처리에 필요한 값을 계층 간에 전달한다.
// 給与統計項目処理に必要な値を各階層間で受け渡す。
public class PayrollStatItem {
    // 1. DB에서 조회할 원본 데이터 (Service에서 계산용으로 사용)
    // 支給額・控除額・期間値を業務基準に従って計算し、合計と最終金額へ反映する。
    private long rawTotalPay;
    private double rawHeadcount;

    // 2. JSP(View)에서 사용할 데이터
    // 画面表示に必要なデータとメッセージをrequestまたはsessionへ保存し、JSPへ引き渡す。
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
    // 급여통계항목 객체에 저장된 원본합계지급 값을 반환한다.
    // 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
    // 給与統計項目オブジェクトに保存された元合計支給の値を返す。
    // 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
    public long getRawTotalPay() { return rawTotalPay; }
    public void setRawTotalPay(long rawTotalPay) { this.rawTotalPay = rawTotalPay; }
    // 급여통계항목 객체에 저장된 원본인원수 값을 반환한다.
    // 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
    // 給与統計項目オブジェクトに保存された元人数の値を返す。
    // 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
    public double getRawHeadcount() { return rawHeadcount; }
    public void setRawHeadcount(double rawHeadcount) { this.rawHeadcount = rawHeadcount; }
    // 급여통계항목 객체에 저장된 연도 값을 반환한다.
    // 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
    // 給与統計項目オブジェクトに保存された年度の値を返す。
    // 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    // 급여통계항목 객체에 저장된 월 값을 반환한다.
    // 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
    // 給与統計項目オブジェクトに保存された月の値を返す。
    // 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }
    // 급여통계항목 객체에 저장된 합계급여표시문자 값을 반환한다.
    // 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
    // 給与統計項目オブジェクトに保存された合計給与表示文字の値を返す。
    // 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
    public String getTotalPayrollText() { return totalPayrollText; }
    public void setTotalPayrollText(String totalPayrollText) { this.totalPayrollText = totalPayrollText; }
    // 급여통계항목 객체에 저장된 급여Growth 값을 반환한다.
    // 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
    // 給与統計項目オブジェクトに保存された給与Growthの値を返す。
    // 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
    public double getPayrollGrowth() { return payrollGrowth; }
    public void setPayrollGrowth(double payrollGrowth) { this.payrollGrowth = payrollGrowth; }
    // 급여통계항목 객체에 저장된 급여Growth표시문자 값을 반환한다.
    // 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
    // 給与統計項目オブジェクトに保存された給与Growth表示文字の値を返す。
    // 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
    public String getPayrollGrowthText() { return payrollGrowthText; }
    public void setPayrollGrowthText(String payrollGrowthText) { this.payrollGrowthText = payrollGrowthText; }
    // 급여통계항목 객체에 저장된 인원수표시문자 값을 반환한다.
    // 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
    // 給与統計項目オブジェクトに保存された人数表示文字の値を返す。
    // 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
    public String getHeadcountText() { return headcountText; }
    public void setHeadcountText(String headcountText) { this.headcountText = headcountText; }
    // 급여통계항목 객체에 저장된 인원수Growth 값을 반환한다.
    // 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
    // 給与統計項目オブジェクトに保存された人数Growthの値を返す。
    // 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
    public double getHeadcountGrowth() { return headcountGrowth; }
    public void setHeadcountGrowth(double headcountGrowth) { this.headcountGrowth = headcountGrowth; }
    // 급여통계항목 객체에 저장된 인원수Growth표시문자 값을 반환한다.
    // 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
    // 給与統計項目オブジェクトに保存された人数Growth表示文字の値を返す。
    // 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
    public String getHeadcountGrowthText() { return headcountGrowthText; }
    public void setHeadcountGrowthText(String headcountGrowthText) { this.headcountGrowthText = headcountGrowthText; }
    // 급여통계항목 객체에 저장된 급여Bar비율 값을 반환한다.
    // 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
    // 給与統計項目オブジェクトに保存された給与Bar率の値を返す。
    // 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
    public int getPayrollBarRate() { return payrollBarRate; }
    public void setPayrollBarRate(int payrollBarRate) { this.payrollBarRate = payrollBarRate; }
    // 급여통계항목 객체에 저장된 인원수Bar비율 값을 반환한다.
    // 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
    // 給与統計項目オブジェクトに保存された人数Bar率の値を返す。
    // 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
    public int getHeadcountBarRate() { return headcountBarRate; }
    public void setHeadcountBarRate(int headcountBarRate) { this.headcountBarRate = headcountBarRate; }
}
