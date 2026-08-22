package erp.payrollstats.dto;

import java.util.List;

// 급여구성비통계화면 데이터 처리에 필요한 값을 계층 간에 전달한다.
// 給与構成比統計画面データ処理に必要な値を各階層間で受け渡す。
public class PayrollCompositionStatPage {

	private List<StatEmployee> employeeOptions;
	private List<ChartItem> paymentItems;
	private List<ChartItem> deductionItems;
	private List<ChartItem> summaryItems;
	private String totalPaymentText;
	private String totalDeductionText;
	private String netPaymentText;

	// 급여구성비통계화면 데이터 객체에 저장된 사원Options 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 給与構成比統計画面データオブジェクトに保存された社員Optionsの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public List<StatEmployee> getEmployeeOptions() { return employeeOptions; }
	public void setEmployeeOptions(List<StatEmployee> value) { employeeOptions = value; }
	// 급여구성비통계화면 데이터 객체에 저장된 지급항목 목록 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 給与構成比統計画面データオブジェクトに保存された支給項目一覧の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public List<ChartItem> getPaymentItems() { return paymentItems; }
	public void setPaymentItems(List<ChartItem> value) { paymentItems = value; }
	// 급여구성비통계화면 데이터 객체에 저장된 공제항목 목록 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 給与構成比統計画面データオブジェクトに保存された控除項目一覧の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public List<ChartItem> getDeductionItems() { return deductionItems; }
	public void setDeductionItems(List<ChartItem> value) { deductionItems = value; }
	// 급여구성비통계화면 데이터 객체에 저장된 요약정보항목 목록 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 給与構成比統計画面データオブジェクトに保存された集計情報項目一覧の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public List<ChartItem> getSummaryItems() { return summaryItems; }
	public void setSummaryItems(List<ChartItem> value) { summaryItems = value; }
	// 급여구성비통계화면 데이터 객체에 저장된 합계지급표시문자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 給与構成比統計画面データオブジェクトに保存された合計支給表示文字の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getTotalPaymentText() { return totalPaymentText; }
	public void setTotalPaymentText(String value) { totalPaymentText = value; }
	// 급여구성비통계화면 데이터 객체에 저장된 합계공제표시문자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 給与構成比統計画面データオブジェクトに保存された合計控除表示文字の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getTotalDeductionText() { return totalDeductionText; }
	public void setTotalDeductionText(String value) { totalDeductionText = value; }
	// 급여구성비통계화면 데이터 객체에 저장된 Net지급표시문자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 給与構成比統計画面データオブジェクトに保存されたNet支給表示文字の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getNetPaymentText() { return netPaymentText; }
	public void setNetPaymentText(String value) { netPaymentText = value; }

	// 모달창 및 선택된 사원 정보
	// 社員の識別情報と在職・雇用・所属条件を確認し、対象社員データへ反映する。
	public static class StatEmployee {
		private int employeeId;
		private String employeeNo;
		private String type;
		private String name;
		private String department;
		private String position;
		private String status;

		// 급여구성비통계화면 데이터 객체에 저장된 사원식별번호 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与構成比統計画面データオブジェクトに保存された社員識別番号の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public int getEmployeeId() { return employeeId; }
		public void setEmployeeId(int value) { employeeId = value; }
		// 급여구성비통계화면 데이터 객체에 저장된 사원번호 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与構成比統計画面データオブジェクトに保存された社員番号の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getEmployeeNo() { return employeeNo; }
		public void setEmployeeNo(String value) { employeeNo = value; }
		// 급여구성비통계화면 데이터 객체에 저장된 구분 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与構成比統計画面データオブジェクトに保存された区分の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getType() { return type; }
		public void setType(String value) { type = value; }
		// 급여구성비통계화면 데이터 객체에 저장된 명칭 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与構成比統計画面データオブジェクトに保存された名称の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getName() { return name; }
		public void setName(String value) { name = value; }
		// 급여구성비통계화면 데이터 객체에 저장된 부서 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与構成比統計画面データオブジェクトに保存された部署の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getDepartment() { return department; }
		public void setDepartment(String value) { department = value; }
		// 급여구성비통계화면 데이터 객체에 저장된 직위 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与構成比統計画面データオブジェクトに保存された役職の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getPosition() { return position; }
		public void setPosition(String value) { position = value; }
		// 급여구성비통계화면 데이터 객체에 저장된 상태 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与構成比統計画面データオブジェクトに保存された状態の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getStatus() { return status; }
		public void setStatus(String value) { status = value; }
	}

	// DB 원본 데이터
	// データベースから照会した項目別金額と構成比計算に必要な元データを保持する。
	public static class StatItem {
		private String itemName;
		private long amount;
		private String type;

		// 급여구성비통계화면 데이터 객체에 저장된 항목명칭 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与構成比統計画面データオブジェクトに保存された項目名称の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getItemName() { return itemName; }
		public void setItemName(String value) { itemName = value; }
		// 급여구성비통계화면 데이터 객체에 저장된 금액 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与構成比統計画面データオブジェクトに保存された金額の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getAmount() { return amount; }
		public void setAmount(long value) { amount = value; }
		// 급여구성비통계화면 데이터 객체에 저장된 구분 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与構成比統計画面データオブジェクトに保存された区分の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getType() { return type; }
		public void setType(String value) { type = value; }
	}

	// SVG 차트 렌더링 데이터
	// 金額が0の場合もグラフ項目を確認できるよう、表示用の最小高さを適用する。
	public static class ChartItem {
		private String name;
		private String amountText;
		private String ratioText;
		private String color;
		private String ratioValue;
		private String dashOffset;
		private String labelLeft;
		private String labelTop;

		// 급여구성비통계화면 데이터 객체에 저장된 명칭 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与構成比統計画面データオブジェクトに保存された名称の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getName() { return name; }
		public void setName(String value) { name = value; }
		// 급여구성비통계화면 데이터 객체에 저장된 금액표시문자 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与構成比統計画面データオブジェクトに保存された金額表示文字の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getAmountText() { return amountText; }
		public void setAmountText(String value) { amountText = value; }
		// 급여구성비통계화면 데이터 객체에 저장된 비율표시문자 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与構成比統計画面データオブジェクトに保存された比率表示文字の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getRatioText() { return ratioText; }
		public void setRatioText(String value) { ratioText = value; }
		// 급여구성비통계화면 데이터 객체에 저장된 색상 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与構成比統計画面データオブジェクトに保存された色の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getColor() { return color; }
		public void setColor(String value) { color = value; }
		// 급여구성비통계화면 데이터 객체에 저장된 비율값 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与構成比統計画面データオブジェクトに保存された比率値の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getRatioValue() { return ratioValue; }
		public void setRatioValue(String value) { ratioValue = value; }
		// 급여구성비통계화면 데이터 객체에 저장된 점선간격 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与構成比統計画面データオブジェクトに保存された破線間隔の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getDashOffset() { return dashOffset; }
		public void setDashOffset(String value) { dashOffset = value; }
		// 급여구성비통계화면 데이터 객체에 저장된 라벨좌측위치 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与構成比統計画面データオブジェクトに保存されたラベル左位置の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getLabelLeft() { return labelLeft; }
		public void setLabelLeft(String value) { labelLeft = value; }
		// 급여구성비통계화면 데이터 객체에 저장된 라벨상단위치 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与構成比統計画面データオブジェクトに保存されたラベル上位置の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getLabelTop() { return labelTop; }
		public void setLabelTop(String value) { labelTop = value; }
	}
}
