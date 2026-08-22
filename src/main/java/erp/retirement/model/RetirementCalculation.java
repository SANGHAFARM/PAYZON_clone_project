package erp.retirement.model;

import java.util.Date;

/**
 * [퇴직관리] 사원별 퇴직급여 산정, 퇴직소득세, 지급정보 마스터 Model DB 테이블: RETIREMENT_CALCULATION
 */
// 퇴직급여계산 정보를 보관하고 관련 기능에서 사용할 수 있도록 제공한다.
// 退職給与計算情報を保持し、関連機能から利用できるように提供する。
public class RetirementCalculation {

	// [DB 관리 항목]
	// データベースの主キー・外部キー・基本属性を保持し、永続化対象を識別する。
	private int retirementCalculationId; // 퇴직급여 계산내역 식별 번호 (PK)
	private int employeeId; // 퇴직 대상 사원 식별 번호 (FK)

	// [퇴직급여 계산구분]
	// 支給額・控除額・期間値を業務基準に従って計算し、合計と最終金額へ反映する。
	private String calcType; // 구분 (퇴직정산 / 중간정산)

	// [입사일·퇴직일·근속기간]
	// 基準日と期間の境界を確認し、照会・計算に使用できる日付形式へ変換する。
	private Date calcStartDate; // 입사일 (정산 시작일)
	private Date retireDate; // 퇴직일
	private int serviceYears; // 근속년수
	private int serviceDays; // 근속일수
	private int excludeDays; // 제외일수

	// [추가 지급·공제 입력액]
	// 支給額・控除額・期間値を業務基準に従って計算し、合計と最終金額へ反映する。
	private long compensationAmt; // 퇴직위로금
	private long dismissalAmt; // 해고예고수당
	private long taxFreeRetireAmt; // 비과세 퇴직급여
	private long prepaidTaxAmt; // 기납부(또는 기과세이연) 세액
	private long taxCreditAmt; // 세액공제

	// [평균임금·퇴직급여 계산결과]
	// 支給額・控除額・期間値を業務基準に従って計算し、合計と最終金額へ反映する。
	private long threeMonthTotal; // 3개월 급여 총계
	private long avgMonthWage; // 평균임금 (월)
	private long avgDayWage; // 1일 평균임금
	private long ordinaryDayWage; // 1일 통상임금

	// [퇴직소득세 계산과정]
	// 支給額・控除額・期間値を業務基準に従って計算し、合計と最終金額へ反映する。
	private long retireIncome; // 퇴직소득
	private long calculatedTaxAmt; // 산출세액

	// [세금·공제 및 실지급액]
	// 支給額・控除額・期間値を業務基準に従って計算し、合計と最終金額へ反映する。
	private long incomeTax; // 퇴직소득세
	private long localIncomeTax; // 지방소득세
	private long deferredIncomeTax; // 이연 퇴직소득세
	private long deferredLocalTax; // 이연 지방소득세
	private long specialRuralTax; // 농어촌특별세
	private long otherDeductAmt; // 기타공제
	private long taxableRetireAmt; // 과세대상 퇴직급여 (자동 계산)
	private long withholdingTaxAmt; // 차감원천징수세액 (자동 계산)
	private long actualPayAmt; // 실수령액 (자동 계산)

	// [지급정보]
	// 画面で入力または照会する業務属性を保持し、登録・詳細表示で共通利用する。
	private String payMethod; // 지급방법 (현금, 계좌이체 등)
	private Date payDate; // 지급일

	// 전달받은 값으로 퇴직급여계산 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で退職給与計算オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	public RetirementCalculation() {
	}

	// Getter & Setter
	// 퇴직급여계산 객체에 저장된 퇴직급여계산식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与計算オブジェクトに保存された退職給与計算識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getRetirementCalculationId() {
		return retirementCalculationId;
	}

	// 전달받은 퇴직급여계산식별번호 값을 퇴직급여계산 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った退職給与計算識別番号の値を退職給与計算オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setRetirementCalculationId(int retirementCalculationId) {
		this.retirementCalculationId = retirementCalculationId;
	}

	// 퇴직급여계산 객체에 저장된 사원식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与計算オブジェクトに保存された社員識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getEmployeeId() {
		return employeeId;
	}

	// 전달받은 사원식별번호 값을 퇴직급여계산 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った社員識別番号の値を退職給与計算オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	// 퇴직급여계산 객체에 저장된 계산구분 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与計算オブジェクトに保存された計算区分の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getCalcType() {
		return calcType;
	}

	// 전달받은 계산구분 값을 퇴직급여계산 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った計算区分の値を退職給与計算オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setCalcType(String calcType) {
		this.calcType = calcType;
	}

	// 퇴직급여계산 객체에 저장된 계산Start일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与計算オブジェクトに保存された計算Start日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Date getCalcStartDate() {
		return calcStartDate;
	}

	// 전달받은 계산Start일자 값을 퇴직급여계산 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った計算Start日付の値を退職給与計算オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setCalcStartDate(Date calcStartDate) {
		this.calcStartDate = calcStartDate;
	}

	// 퇴직급여계산 객체에 저장된 퇴직일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与計算オブジェクトに保存された退職日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Date getRetireDate() {
		return retireDate;
	}

	// 전달받은 퇴직일자 값을 퇴직급여계산 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った退職日付の値を退職給与計算オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setRetireDate(Date retireDate) {
		this.retireDate = retireDate;
	}

	// 퇴직급여계산 객체에 저장된 근속연도 목록 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与計算オブジェクトに保存された勤続年度一覧の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getServiceYears() {
		return serviceYears;
	}

	// 전달받은 근속연도 목록 값을 퇴직급여계산 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った勤続年度一覧の値を退職給与計算オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setServiceYears(int serviceYears) {
		this.serviceYears = serviceYears;
	}

	// 퇴직급여계산 객체에 저장된 근속일수 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与計算オブジェクトに保存された勤続日数の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getServiceDays() {
		return serviceDays;
	}

	// 전달받은 근속일수 값을 퇴직급여계산 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った勤続日数の値を退職給与計算オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setServiceDays(int serviceDays) {
		this.serviceDays = serviceDays;
	}

	// 퇴직급여계산 객체에 저장된 제외일수 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与計算オブジェクトに保存された除外日数の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getExcludeDays() {
		return excludeDays;
	}

	// 전달받은 제외일수 값을 퇴직급여계산 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った除外日数の値を退職給与計算オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setExcludeDays(int excludeDays) {
		this.excludeDays = excludeDays;
	}

	// 퇴직급여계산 객체에 저장된 퇴직위로금금액 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与計算オブジェクトに保存された退職慰労金金額の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getCompensationAmt() {
		return compensationAmt;
	}

	// 전달받은 퇴직위로금금액 값을 퇴직급여계산 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った退職慰労金金額の値を退職給与計算オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setCompensationAmt(long compensationAmt) {
		this.compensationAmt = compensationAmt;
	}

	// 퇴직급여계산 객체에 저장된 해고예고금액 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与計算オブジェクトに保存された解雇予告金額の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getDismissalAmt() {
		return dismissalAmt;
	}

	// 전달받은 해고예고금액 값을 퇴직급여계산 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った解雇予告金額の値を退職給与計算オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setDismissalAmt(long dismissalAmt) {
		this.dismissalAmt = dismissalAmt;
	}

	// 퇴직급여계산 객체에 저장된 세금비과세퇴직금액 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与計算オブジェクトに保存された税金非課税退職金額の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getTaxFreeRetireAmt() {
		return taxFreeRetireAmt;
	}

	// 전달받은 세금비과세퇴직금액 값을 퇴직급여계산 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った税金非課税退職金額の値を退職給与計算オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setTaxFreeRetireAmt(long taxFreeRetireAmt) {
		this.taxFreeRetireAmt = taxFreeRetireAmt;
	}

	// 퇴직급여계산 객체에 저장된 Prepaid세금금액 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与計算オブジェクトに保存されたPrepaid税金金額の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getPrepaidTaxAmt() {
		return prepaidTaxAmt;
	}

	// 전달받은 Prepaid세금금액 값을 퇴직급여계산 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったPrepaid税金金額の値を退職給与計算オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setPrepaidTaxAmt(long prepaidTaxAmt) {
		this.prepaidTaxAmt = prepaidTaxAmt;
	}

	// 퇴직급여계산 객체에 저장된 세금Credit금액 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与計算オブジェクトに保存された税金Credit金額の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getTaxCreditAmt() {
		return taxCreditAmt;
	}

	// 전달받은 세금Credit금액 값을 퇴직급여계산 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った税金Credit金額の値を退職給与計算オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setTaxCreditAmt(long taxCreditAmt) {
		this.taxCreditAmt = taxCreditAmt;
	}

	// 퇴직급여계산 객체에 저장된 Three월합계 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与計算オブジェクトに保存されたThree月合計の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getThreeMonthTotal() {
		return threeMonthTotal;
	}

	// 전달받은 Three월합계 값을 퇴직급여계산 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったThree月合計の値を退職給与計算オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setThreeMonthTotal(long threeMonthTotal) {
		this.threeMonthTotal = threeMonthTotal;
	}

	// 퇴직급여계산 객체에 저장된 Avg월임금 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与計算オブジェクトに保存されたAvg月賃金の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getAvgMonthWage() {
		return avgMonthWage;
	}

	// 전달받은 Avg월임금 값을 퇴직급여계산 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったAvg月賃金の値を退職給与計算オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setAvgMonthWage(long avgMonthWage) {
		this.avgMonthWage = avgMonthWage;
	}

	// 퇴직급여계산 객체에 저장된 Avg일용직임금 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与計算オブジェクトに保存されたAvg日雇い賃金の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getAvgDayWage() {
		return avgDayWage;
	}

	// 전달받은 Avg일용직임금 값을 퇴직급여계산 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったAvg日雇い賃金の値を退職給与計算オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setAvgDayWage(long avgDayWage) {
		this.avgDayWage = avgDayWage;
	}

	// 퇴직급여계산 객체에 저장된 Ordinary일용직임금 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与計算オブジェクトに保存されたOrdinary日雇い賃金の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getOrdinaryDayWage() {
		return ordinaryDayWage;
	}

	// 전달받은 Ordinary일용직임금 값을 퇴직급여계산 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったOrdinary日雇い賃金の値を退職給与計算オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setOrdinaryDayWage(long ordinaryDayWage) {
		this.ordinaryDayWage = ordinaryDayWage;
	}

	// 퇴직급여계산 객체에 저장된 퇴직소득 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与計算オブジェクトに保存された退職所得の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getRetireIncome() {
		return retireIncome;
	}

	// 전달받은 퇴직소득 값을 퇴직급여계산 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った退職所得の値を退職給与計算オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setRetireIncome(long retireIncome) {
		this.retireIncome = retireIncome;
	}

	// 퇴직급여계산 객체에 저장된 Calculated세금금액 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与計算オブジェクトに保存されたCalculated税金金額の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getCalculatedTaxAmt() {
		return calculatedTaxAmt;
	}

	// 전달받은 Calculated세금금액 값을 퇴직급여계산 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったCalculated税金金額の値を退職給与計算オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setCalculatedTaxAmt(long calculatedTaxAmt) {
		this.calculatedTaxAmt = calculatedTaxAmt;
	}

	// 퇴직급여계산 객체에 저장된 소득세금 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与計算オブジェクトに保存された所得税金の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getIncomeTax() {
		return incomeTax;
	}

	// 전달받은 소득세금 값을 퇴직급여계산 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った所得税金の値を退職給与計算オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setIncomeTax(long incomeTax) {
		this.incomeTax = incomeTax;
	}

	// 퇴직급여계산 객체에 저장된 Local소득세금 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与計算オブジェクトに保存されたLocal所得税金の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getLocalIncomeTax() {
		return localIncomeTax;
	}

	// 전달받은 Local소득세금 값을 퇴직급여계산 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったLocal所得税金の値を退職給与計算オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setLocalIncomeTax(long localIncomeTax) {
		this.localIncomeTax = localIncomeTax;
	}

	// 퇴직급여계산 객체에 저장된 Deferred소득세금 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与計算オブジェクトに保存されたDeferred所得税金の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getDeferredIncomeTax() {
		return deferredIncomeTax;
	}

	// 전달받은 Deferred소득세금 값을 퇴직급여계산 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったDeferred所得税金の値を退職給与計算オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setDeferredIncomeTax(long deferredIncomeTax) {
		this.deferredIncomeTax = deferredIncomeTax;
	}

	// 퇴직급여계산 객체에 저장된 DeferredLocal세금 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与計算オブジェクトに保存されたDeferredLocal税金の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getDeferredLocalTax() {
		return deferredLocalTax;
	}

	// 전달받은 DeferredLocal세금 값을 퇴직급여계산 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったDeferredLocal税金の値を退職給与計算オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setDeferredLocalTax(long deferredLocalTax) {
		this.deferredLocalTax = deferredLocalTax;
	}

	// 퇴직급여계산 객체에 저장된 SpecialRural세금 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与計算オブジェクトに保存されたSpecialRural税金の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getSpecialRuralTax() {
		return specialRuralTax;
	}

	// 전달받은 SpecialRural세금 값을 퇴직급여계산 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったSpecialRural税金の値を退職給与計算オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setSpecialRuralTax(long specialRuralTax) {
		this.specialRuralTax = specialRuralTax;
	}

	// 퇴직급여계산 객체에 저장된 기타공제금액 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与計算オブジェクトに保存されたその他控除金額の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getOtherDeductAmt() {
		return otherDeductAmt;
	}

	// 전달받은 기타공제금액 값을 퇴직급여계산 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったその他控除金額の値を退職給与計算オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setOtherDeductAmt(long otherDeductAmt) {
		this.otherDeductAmt = otherDeductAmt;
	}

	// 퇴직급여계산 객체에 저장된 과세대상퇴직금액 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与計算オブジェクトに保存された課税対象退職金額の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getTaxableRetireAmt() {
		return taxableRetireAmt;
	}

	// 전달받은 과세대상퇴직금액 값을 퇴직급여계산 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った課税対象退職金額の値を退職給与計算オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setTaxableRetireAmt(long taxableRetireAmt) {
		this.taxableRetireAmt = taxableRetireAmt;
	}

	// 퇴직급여계산 객체에 저장된 Withholding세금금액 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与計算オブジェクトに保存されたWithholding税金金額の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getWithholdingTaxAmt() {
		return withholdingTaxAmt;
	}

	// 전달받은 Withholding세금금액 값을 퇴직급여계산 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったWithholding税金金額の値を退職給与計算オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setWithholdingTaxAmt(long withholdingTaxAmt) {
		this.withholdingTaxAmt = withholdingTaxAmt;
	}

	// 퇴직급여계산 객체에 저장된 Actual지급금액 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与計算オブジェクトに保存されたActual支給金額の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getActualPayAmt() {
		return actualPayAmt;
	}

	// 전달받은 Actual지급금액 값을 퇴직급여계산 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったActual支給金額の値を退職給与計算オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setActualPayAmt(long actualPayAmt) {
		this.actualPayAmt = actualPayAmt;
	}

	// 퇴직급여계산 객체에 저장된 지급방법 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与計算オブジェクトに保存された支給方法の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getPayMethod() {
		return payMethod;
	}

	// 전달받은 지급방법 값을 퇴직급여계산 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った支給方法の値を退職給与計算オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	// 퇴직급여계산 객체에 저장된 지급일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与計算オブジェクトに保存された支給日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Date getPayDate() {
		return payDate;
	}

	// 전달받은 지급일자 값을 퇴직급여계산 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った支給日付の値を退職給与計算オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
}
