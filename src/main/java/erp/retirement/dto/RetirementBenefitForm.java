package erp.retirement.dto;

import java.util.ArrayList;
import java.util.List;

import erp.retirement.model.RetirementIncomeEntry;
import erp.retirement.model.RetirementTaxDeferral;

// 퇴직급여 계산 화면의 입력값과 계산 결과를 전달한다.
// 퇴직급여정산입력화면 처리에 필요한 값을 계층 간에 전달한다.
// 退職給与精算入力画面処理に必要な値を各階層間で受け渡す。
public class RetirementBenefitForm {

	private int calculationId;
	private int employeeId;
	private int serviceYears;
	private int serviceDays;
	private int excludedDays;
	private int taxYear;
	private String settlementType;
	private String startDate;
	private String endDate;
	private String paymentMethod;
	private String paymentDate;
	private long compensation;
	private long dismissalAllowance;
	private long taxFreeRetirement;
	private long prepaidTax;
	private long taxCredit;
	private long threeMonthTotal;
	private long salaryDaysTotal;
	private long salaryTotal;
	private long dailyAverage;
	private long dailyOrdinary;
	private long retirementIncome;
	private long calculatedTax;
	private long incomeTax;
	private long localIncomeTax;
	private long deferredIncomeTax;
	private long deferredLocalTax;
	private long ruralTax;
	private long otherDeduction;
	private long taxablePayment;
	private long withholdingTax;
	private long netPayment;
	private List<RetirementIncomeEntry> incomeEntries = new ArrayList<>();
	private List<RetirementTaxDeferral> taxDeferrals = new ArrayList<>();

	// 퇴직급여정산입력화면 객체에 저장된 계산식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算入力画面オブジェクトに保存された計算識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getCalculationId() {
		return calculationId;
	}

	// 전달받은 계산식별번호 값을 퇴직급여정산입력화면 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った計算識別番号の値を退職給与精算入力画面オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setCalculationId(int calculationId) {
		this.calculationId = calculationId;
	}

	// 퇴직급여정산입력화면 객체에 저장된 사원식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算入力画面オブジェクトに保存された社員識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getEmployeeId() {
		return employeeId;
	}

	// 전달받은 사원식별번호 값을 퇴직급여정산입력화면 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った社員識別番号の値を退職給与精算入力画面オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	// 퇴직급여정산입력화면 객체에 저장된 근속연도 목록 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算入力画面オブジェクトに保存された勤続年度一覧の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getServiceYears() {
		return serviceYears;
	}

	// 전달받은 근속연도 목록 값을 퇴직급여정산입력화면 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った勤続年度一覧の値を退職給与精算入力画面オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setServiceYears(int serviceYears) {
		this.serviceYears = serviceYears;
	}

	// 퇴직급여정산입력화면 객체에 저장된 근속일수 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算入力画面オブジェクトに保存された勤続日数の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getServiceDays() {
		return serviceDays;
	}

	// 전달받은 근속일수 값을 퇴직급여정산입력화면 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った勤続日数の値を退職給与精算入力画面オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setServiceDays(int serviceDays) {
		this.serviceDays = serviceDays;
	}

	// 퇴직급여정산입력화면 객체에 저장된 제외일수 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算入力画面オブジェクトに保存された除外日数の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getExcludedDays() {
		return excludedDays;
	}

	// 전달받은 제외일수 값을 퇴직급여정산입력화면 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った除外日数の値を退職給与精算入力画面オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setExcludedDays(int excludedDays) {
		this.excludedDays = excludedDays;
	}

	// 퇴직급여정산입력화면 객체에 저장된 세금연도 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算入力画面オブジェクトに保存された税金年度の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getTaxYear() {
		return taxYear;
	}

	// 전달받은 세금연도 값을 퇴직급여정산입력화면 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った税金年度の値を退職給与精算入力画面オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setTaxYear(int taxYear) {
		this.taxYear = taxYear;
	}

	// 퇴직급여정산입력화면 객체에 저장된 정산구분 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算入力画面オブジェクトに保存された精算区分の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getSettlementType() {
		return settlementType;
	}

	// 전달받은 정산구분 값을 퇴직급여정산입력화면 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った精算区分の値を退職給与精算入力画面オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setSettlementType(String settlementType) {
		this.settlementType = settlementType;
	}

	// 퇴직급여정산입력화면 객체에 저장된 Start일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算入力画面オブジェクトに保存されたStart日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getStartDate() {
		return startDate;
	}

	// 전달받은 Start일자 값을 퇴직급여정산입력화면 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったStart日付の値を退職給与精算入力画面オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	// 퇴직급여정산입력화면 객체에 저장된 End일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算入力画面オブジェクトに保存されたEnd日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getEndDate() {
		return endDate;
	}

	// 전달받은 End일자 값을 퇴직급여정산입력화면 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったEnd日付の値を退職給与精算入力画面オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	// 퇴직급여정산입력화면 객체에 저장된 지급방법 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算入力画面オブジェクトに保存された支給方法の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getPaymentMethod() {
		return paymentMethod;
	}

	// 전달받은 지급방법 값을 퇴직급여정산입력화면 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った支給方法の値を退職給与精算入力画面オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	// 퇴직급여정산입력화면 객체에 저장된 지급일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算入力画面オブジェクトに保存された支給日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getPaymentDate() {
		return paymentDate;
	}

	// 전달받은 지급일자 값을 퇴직급여정산입력화면 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った支給日付の値を退職給与精算入力画面オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	// 퇴직급여정산입력화면 객체에 저장된 퇴직위로금 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算入力画面オブジェクトに保存された退職慰労金の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getCompensation() {
		return compensation;
	}

	// 전달받은 퇴직위로금 값을 퇴직급여정산입력화면 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った退職慰労金の値を退職給与精算入力画面オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setCompensation(long compensation) {
		this.compensation = compensation;
	}

	// 퇴직급여정산입력화면 객체에 저장된 해고예고수당 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算入力画面オブジェクトに保存された解雇予告手当の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getDismissalAllowance() {
		return dismissalAllowance;
	}

	// 전달받은 해고예고수당 값을 퇴직급여정산입력화면 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った解雇予告手当の値を退職給与精算入力画面オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setDismissalAllowance(long dismissalAllowance) {
		this.dismissalAllowance = dismissalAllowance;
	}

	// 퇴직급여정산입력화면 객체에 저장된 세금비과세퇴직급여 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算入力画面オブジェクトに保存された税金非課税退職給与の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getTaxFreeRetirement() {
		return taxFreeRetirement;
	}

	// 전달받은 세금비과세퇴직급여 값을 퇴직급여정산입력화면 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った税金非課税退職給与の値を退職給与精算入力画面オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setTaxFreeRetirement(long taxFreeRetirement) {
		this.taxFreeRetirement = taxFreeRetirement;
	}

	// 퇴직급여정산입력화면 객체에 저장된 Prepaid세금 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算入力画面オブジェクトに保存されたPrepaid税金の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getPrepaidTax() {
		return prepaidTax;
	}

	// 전달받은 Prepaid세금 값을 퇴직급여정산입력화면 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったPrepaid税金の値を退職給与精算入力画面オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setPrepaidTax(long prepaidTax) {
		this.prepaidTax = prepaidTax;
	}

	// 퇴직급여정산입력화면 객체에 저장된 세금Credit 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算入力画面オブジェクトに保存された税金Creditの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getTaxCredit() {
		return taxCredit;
	}

	// 전달받은 세금Credit 값을 퇴직급여정산입력화면 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った税金Creditの値を退職給与精算入力画面オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setTaxCredit(long taxCredit) {
		this.taxCredit = taxCredit;
	}

	// 퇴직급여정산입력화면 객체에 저장된 Three월합계 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算入力画面オブジェクトに保存されたThree月合計の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getThreeMonthTotal() {
		return threeMonthTotal;
	}

	// 전달받은 Three월합계 값을 퇴직급여정산입력화면 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったThree月合計の値を退職給与精算入力画面オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setThreeMonthTotal(long threeMonthTotal) {
		this.threeMonthTotal = threeMonthTotal;
	}

	// 퇴직급여정산입력화면 객체에 저장된 급여일수합계 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算入力画面オブジェクトに保存された給与日数合計の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getSalaryDaysTotal() {
		return salaryDaysTotal;
	}

	// 전달받은 급여일수합계 값을 퇴직급여정산입력화면 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った給与日数合計の値を退職給与精算入力画面オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setSalaryDaysTotal(long salaryDaysTotal) {
		this.salaryDaysTotal = salaryDaysTotal;
	}

	// 퇴직급여정산입력화면 객체에 저장된 급여합계 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算入力画面オブジェクトに保存された給与合計の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getSalaryTotal() {
		return salaryTotal;
	}

	// 전달받은 급여합계 값을 퇴직급여정산입력화면 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った給与合計の値を退職給与精算入力画面オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setSalaryTotal(long salaryTotal) {
		this.salaryTotal = salaryTotal;
	}

	// 퇴직급여정산입력화면 객체에 저장된 일용직Average 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算入力画面オブジェクトに保存された日雇いAverageの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getDailyAverage() {
		return dailyAverage;
	}

	// 전달받은 일용직Average 값을 퇴직급여정산입력화면 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った日雇いAverageの値を退職給与精算入力画面オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setDailyAverage(long dailyAverage) {
		this.dailyAverage = dailyAverage;
	}

	// 퇴직급여정산입력화면 객체에 저장된 일용직Ordinary 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算入力画面オブジェクトに保存された日雇いOrdinaryの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getDailyOrdinary() {
		return dailyOrdinary;
	}

	// 전달받은 일용직Ordinary 값을 퇴직급여정산입력화면 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った日雇いOrdinaryの値を退職給与精算入力画面オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setDailyOrdinary(long dailyOrdinary) {
		this.dailyOrdinary = dailyOrdinary;
	}

	// 퇴직급여정산입력화면 객체에 저장된 퇴직급여소득 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算入力画面オブジェクトに保存された退職給与所得の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getRetirementIncome() {
		return retirementIncome;
	}

	// 전달받은 퇴직급여소득 값을 퇴직급여정산입력화면 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った退職給与所得の値を退職給与精算入力画面オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setRetirementIncome(long retirementIncome) {
		this.retirementIncome = retirementIncome;
	}

	// 퇴직급여정산입력화면 객체에 저장된 Calculated세금 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算入力画面オブジェクトに保存されたCalculated税金の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getCalculatedTax() {
		return calculatedTax;
	}

	// 전달받은 Calculated세금 값을 퇴직급여정산입력화면 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったCalculated税金の値を退職給与精算入力画面オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setCalculatedTax(long calculatedTax) {
		this.calculatedTax = calculatedTax;
	}

	// 퇴직급여정산입력화면 객체에 저장된 소득세금 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算入力画面オブジェクトに保存された所得税金の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getIncomeTax() {
		return incomeTax;
	}

	// 전달받은 소득세금 값을 퇴직급여정산입력화면 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った所得税金の値を退職給与精算入力画面オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setIncomeTax(long incomeTax) {
		this.incomeTax = incomeTax;
	}

	// 퇴직급여정산입력화면 객체에 저장된 Local소득세금 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算入力画面オブジェクトに保存されたLocal所得税金の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getLocalIncomeTax() {
		return localIncomeTax;
	}

	// 전달받은 Local소득세금 값을 퇴직급여정산입력화면 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったLocal所得税金の値を退職給与精算入力画面オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setLocalIncomeTax(long localIncomeTax) {
		this.localIncomeTax = localIncomeTax;
	}

	// 퇴직급여정산입력화면 객체에 저장된 Deferred소득세금 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算入力画面オブジェクトに保存されたDeferred所得税金の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getDeferredIncomeTax() {
		return deferredIncomeTax;
	}

	// 전달받은 Deferred소득세금 값을 퇴직급여정산입력화면 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったDeferred所得税金の値を退職給与精算入力画面オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setDeferredIncomeTax(long deferredIncomeTax) {
		this.deferredIncomeTax = deferredIncomeTax;
	}

	// 퇴직급여정산입력화면 객체에 저장된 DeferredLocal세금 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算入力画面オブジェクトに保存されたDeferredLocal税金の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getDeferredLocalTax() {
		return deferredLocalTax;
	}

	// 전달받은 DeferredLocal세금 값을 퇴직급여정산입력화면 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったDeferredLocal税金の値を退職給与精算入力画面オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setDeferredLocalTax(long deferredLocalTax) {
		this.deferredLocalTax = deferredLocalTax;
	}

	// 퇴직급여정산입력화면 객체에 저장된 Rural세금 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算入力画面オブジェクトに保存されたRural税金の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getRuralTax() {
		return ruralTax;
	}

	// 전달받은 Rural세금 값을 퇴직급여정산입력화면 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったRural税金の値を退職給与精算入力画面オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setRuralTax(long ruralTax) {
		this.ruralTax = ruralTax;
	}

	// 퇴직급여정산입력화면 객체에 저장된 기타공제 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算入力画面オブジェクトに保存されたその他控除の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getOtherDeduction() {
		return otherDeduction;
	}

	// 전달받은 기타공제 값을 퇴직급여정산입력화면 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったその他控除の値を退職給与精算入力画面オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setOtherDeduction(long otherDeduction) {
		this.otherDeduction = otherDeduction;
	}

	// 퇴직급여정산입력화면 객체에 저장된 과세대상지급 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算入力画面オブジェクトに保存された課税対象支給の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getTaxablePayment() {
		return taxablePayment;
	}

	// 전달받은 과세대상지급 값을 퇴직급여정산입력화면 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った課税対象支給の値を退職給与精算入力画面オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setTaxablePayment(long taxablePayment) {
		this.taxablePayment = taxablePayment;
	}

	// 퇴직급여정산입력화면 객체에 저장된 Withholding세금 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算入力画面オブジェクトに保存されたWithholding税金の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getWithholdingTax() {
		return withholdingTax;
	}

	// 전달받은 Withholding세금 값을 퇴직급여정산입력화면 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったWithholding税金の値を退職給与精算入力画面オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setWithholdingTax(long withholdingTax) {
		this.withholdingTax = withholdingTax;
	}

	// 퇴직급여정산입력화면 객체에 저장된 Net지급 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算入力画面オブジェクトに保存されたNet支給の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getNetPayment() {
		return netPayment;
	}

	// 전달받은 Net지급 값을 퇴직급여정산입력화면 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったNet支給の値を退職給与精算入力画面オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setNetPayment(long netPayment) {
		this.netPayment = netPayment;
	}

	// 퇴직급여정산입력화면 객체에 저장된 소득상세내역 목록 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算入力画面オブジェクトに保存された所得明細一覧の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public List<RetirementIncomeEntry> getIncomeEntries() {
		return incomeEntries;
	}

	// 퇴직급여정산입력화면 객체에 저장된 세금과세이연 목록 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算入力画面オブジェクトに保存された税金課税繰延一覧の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public List<RetirementTaxDeferral> getTaxDeferrals() {
		return taxDeferrals;
	}

	// 퇴직급여정산입력화면 객체에 저장된 급여상세내역 목록 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算入力画面オブジェクトに保存された給与明細一覧の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public List<RetirementIncomeEntry> getSalaryEntries() {
		List<RetirementIncomeEntry> result = new ArrayList<>();
		for (RetirementIncomeEntry entry : incomeEntries) {
			if (entry.isSalaryData()) {
				result.add(entry);
			}
		}
		return result;
	}

	// 퇴직급여정산입력화면 객체에 저장된 기타소득상세내역 목록 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算入力画面オブジェクトに保存されたその他所得明細一覧の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public List<RetirementIncomeEntry> getOtherIncomeEntries() {
		List<RetirementIncomeEntry> result = new ArrayList<>();
		for (RetirementIncomeEntry entry : incomeEntries) {
			if (entry.isEtcIncomeData()) {
				result.add(entry);
			}
		}
		return result;
	}
}
