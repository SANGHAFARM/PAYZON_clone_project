package erp.retirement.dto;

import java.util.ArrayList;
import java.util.List;

// 퇴직급여명세서 화면에 필요한 값을 전달한다.
// 퇴직급여명세서 정보를 보관하고 관련 기능에서 사용할 수 있도록 제공한다.
// 退職給与明細書情報を保持し、関連機能から利用できるように提供する。
public class RetirementPayslip {

	private int calculationId;
	private int employeeId;
	private int serviceDays;
	private String settlementType;
	private String employeeName;
	private String departmentName;
	private String positionName;
	private String joinDate;
	private String retirementDate;
	private long compensation;
	private long dismissalAllowance;
	private long threeMonthTotal;
	private long dailyAverage;
	private long dailyOrdinary;
	private long retirementIncome;
	private long incomeTax;
	private long localIncomeTax;
	private long otherDeduction;
	private long deductionTotal;
	private long netPayment;
	private long salaryDaysTotal;
	private long salaryTotal;
	private List<SalaryDetail> salaryDetails = new ArrayList<>();
	private List<OtherIncome> otherIncomes = new ArrayList<>();

	// 퇴직급여명세서 객체에 저장된 계산식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与明細書オブジェクトに保存された計算識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getCalculationId() {
		return calculationId;
	}

	// 전달받은 계산식별번호 값을 퇴직급여명세서 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った計算識別番号の値を退職給与明細書オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setCalculationId(int calculationId) {
		this.calculationId = calculationId;
	}

	// 퇴직급여명세서 객체에 저장된 사원식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与明細書オブジェクトに保存された社員識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getEmployeeId() {
		return employeeId;
	}

	// 전달받은 사원식별번호 값을 퇴직급여명세서 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った社員識別番号の値を退職給与明細書オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	// 퇴직급여명세서 객체에 저장된 근속일수 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与明細書オブジェクトに保存された勤続日数の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getServiceDays() {
		return serviceDays;
	}

	// 전달받은 근속일수 값을 퇴직급여명세서 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った勤続日数の値を退職給与明細書オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setServiceDays(int serviceDays) {
		this.serviceDays = serviceDays;
	}

	// 퇴직급여명세서 객체에 저장된 정산구분 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与明細書オブジェクトに保存された精算区分の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getSettlementType() {
		return settlementType;
	}

	// 전달받은 정산구분 값을 퇴직급여명세서 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った精算区分の値を退職給与明細書オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setSettlementType(String settlementType) {
		this.settlementType = settlementType;
	}

	// 퇴직급여명세서 객체에 저장된 사원명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与明細書オブジェクトに保存された社員名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getEmployeeName() {
		return employeeName;
	}

	// 전달받은 사원명칭 값을 퇴직급여명세서 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った社員名称の値を退職給与明細書オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	// 퇴직급여명세서 객체에 저장된 부서명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与明細書オブジェクトに保存された部署名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getDepartmentName() {
		return departmentName;
	}

	// 전달받은 부서명칭 값을 퇴직급여명세서 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った部署名称の値を退職給与明細書オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	// 퇴직급여명세서 객체에 저장된 직위명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与明細書オブジェクトに保存された役職名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getPositionName() {
		return positionName;
	}

	// 전달받은 직위명칭 값을 퇴직급여명세서 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った役職名称の値を退職給与明細書オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	// 퇴직급여명세서 객체에 저장된 Join일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与明細書オブジェクトに保存されたJoin日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getJoinDate() {
		return joinDate;
	}

	// 전달받은 Join일자 값을 퇴직급여명세서 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったJoin日付の値を退職給与明細書オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}

	// 퇴직급여명세서 객체에 저장된 퇴직급여일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与明細書オブジェクトに保存された退職給与日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getRetirementDate() {
		return retirementDate;
	}

	// 전달받은 퇴직급여일자 값을 퇴직급여명세서 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った退職給与日付の値を退職給与明細書オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setRetirementDate(String retirementDate) {
		this.retirementDate = retirementDate;
	}

	// 퇴직급여명세서 객체에 저장된 퇴직위로금 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与明細書オブジェクトに保存された退職慰労金の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getCompensation() {
		return compensation;
	}

	// 전달받은 퇴직위로금 값을 퇴직급여명세서 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った退職慰労金の値を退職給与明細書オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setCompensation(long compensation) {
		this.compensation = compensation;
	}

	// 퇴직급여명세서 객체에 저장된 해고예고수당 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与明細書オブジェクトに保存された解雇予告手当の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getDismissalAllowance() {
		return dismissalAllowance;
	}

	// 전달받은 해고예고수당 값을 퇴직급여명세서 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った解雇予告手当の値を退職給与明細書オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setDismissalAllowance(long dismissalAllowance) {
		this.dismissalAllowance = dismissalAllowance;
	}

	// 퇴직급여명세서 객체에 저장된 Three월합계 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与明細書オブジェクトに保存されたThree月合計の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getThreeMonthTotal() {
		return threeMonthTotal;
	}

	// 전달받은 Three월합계 값을 퇴직급여명세서 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったThree月合計の値を退職給与明細書オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setThreeMonthTotal(long threeMonthTotal) {
		this.threeMonthTotal = threeMonthTotal;
	}

	// 퇴직급여명세서 객체에 저장된 일용직Average 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与明細書オブジェクトに保存された日雇いAverageの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getDailyAverage() {
		return dailyAverage;
	}

	// 전달받은 일용직Average 값을 퇴직급여명세서 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った日雇いAverageの値を退職給与明細書オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setDailyAverage(long dailyAverage) {
		this.dailyAverage = dailyAverage;
	}

	// 퇴직급여명세서 객체에 저장된 일용직Ordinary 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与明細書オブジェクトに保存された日雇いOrdinaryの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getDailyOrdinary() {
		return dailyOrdinary;
	}

	// 전달받은 일용직Ordinary 값을 퇴직급여명세서 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った日雇いOrdinaryの値を退職給与明細書オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setDailyOrdinary(long dailyOrdinary) {
		this.dailyOrdinary = dailyOrdinary;
	}

	// 퇴직급여명세서 객체에 저장된 퇴직급여소득 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与明細書オブジェクトに保存された退職給与所得の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getRetirementIncome() {
		return retirementIncome;
	}

	// 전달받은 퇴직급여소득 값을 퇴직급여명세서 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った退職給与所得の値を退職給与明細書オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setRetirementIncome(long retirementIncome) {
		this.retirementIncome = retirementIncome;
	}

	// 퇴직급여명세서 객체에 저장된 소득세금 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与明細書オブジェクトに保存された所得税金の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getIncomeTax() {
		return incomeTax;
	}

	// 전달받은 소득세금 값을 퇴직급여명세서 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った所得税金の値を退職給与明細書オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setIncomeTax(long incomeTax) {
		this.incomeTax = incomeTax;
	}

	// 퇴직급여명세서 객체에 저장된 Local소득세금 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与明細書オブジェクトに保存されたLocal所得税金の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getLocalIncomeTax() {
		return localIncomeTax;
	}

	// 전달받은 Local소득세금 값을 퇴직급여명세서 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったLocal所得税金の値を退職給与明細書オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setLocalIncomeTax(long localIncomeTax) {
		this.localIncomeTax = localIncomeTax;
	}

	// 퇴직급여명세서 객체에 저장된 기타공제 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与明細書オブジェクトに保存されたその他控除の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getOtherDeduction() {
		return otherDeduction;
	}

	// 전달받은 기타공제 값을 퇴직급여명세서 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったその他控除の値を退職給与明細書オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setOtherDeduction(long otherDeduction) {
		this.otherDeduction = otherDeduction;
	}

	// 퇴직급여명세서 객체에 저장된 공제합계 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与明細書オブジェクトに保存された控除合計の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getDeductionTotal() {
		return deductionTotal;
	}

	// 전달받은 공제합계 값을 퇴직급여명세서 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った控除合計の値を退職給与明細書オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setDeductionTotal(long deductionTotal) {
		this.deductionTotal = deductionTotal;
	}

	// 퇴직급여명세서 객체에 저장된 Net지급 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与明細書オブジェクトに保存されたNet支給の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getNetPayment() {
		return netPayment;
	}

	// 전달받은 Net지급 값을 퇴직급여명세서 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったNet支給の値を退職給与明細書オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setNetPayment(long netPayment) {
		this.netPayment = netPayment;
	}

	// 퇴직급여명세서 객체에 저장된 급여일수합계 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与明細書オブジェクトに保存された給与日数合計の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getSalaryDaysTotal() {
		return salaryDaysTotal;
	}

	// 전달받은 급여일수합계 값을 퇴직급여명세서 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った給与日数合計の値を退職給与明細書オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setSalaryDaysTotal(long salaryDaysTotal) {
		this.salaryDaysTotal = salaryDaysTotal;
	}

	// 퇴직급여명세서 객체에 저장된 급여합계 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与明細書オブジェクトに保存された給与合計の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getSalaryTotal() {
		return salaryTotal;
	}

	// 전달받은 급여합계 값을 퇴직급여명세서 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った給与合計の値を退職給与明細書オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setSalaryTotal(long salaryTotal) {
		this.salaryTotal = salaryTotal;
	}

	// 퇴직급여명세서 객체에 저장된 급여Details 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与明細書オブジェクトに保存された給与Detailsの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public List<SalaryDetail> getSalaryDetails() {
		return salaryDetails;
	}

	// 퇴직급여명세서 객체에 저장된 기타소득 목록 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与明細書オブジェクトに保存されたその他所得一覧の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public List<OtherIncome> getOtherIncomes() {
		return otherIncomes;
	}

	public static class SalaryDetail {
		private String startDate;
		private String endDate;
		private double days;
		private long amount;

		// 퇴직급여명세서 객체에 저장된 Start일자 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 退職給与明細書オブジェクトに保存されたStart日付の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getStartDate() {
			return startDate;
		}

		// 전달받은 Start일자 값을 퇴직급여명세서 객체에 저장한다.
		// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
		// 受け取ったStart日付の値を退職給与明細書オブジェクトに保存する。
		// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}

		// 퇴직급여명세서 객체에 저장된 End일자 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 退職給与明細書オブジェクトに保存されたEnd日付の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getEndDate() {
			return endDate;
		}

		// 전달받은 End일자 값을 퇴직급여명세서 객체에 저장한다.
		// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
		// 受け取ったEnd日付の値を退職給与明細書オブジェクトに保存する。
		// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}

		// 퇴직급여명세서 객체에 저장된 일수 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 退職給与明細書オブジェクトに保存された日数の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public double getDays() {
			return days;
		}

		// 전달받은 일수 값을 퇴직급여명세서 객체에 저장한다.
		// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
		// 受け取った日数の値を退職給与明細書オブジェクトに保存する。
		// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
		public void setDays(double days) {
			this.days = days;
		}

		// 퇴직급여명세서 객체에 저장된 금액 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 退職給与明細書オブジェクトに保存された金額の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getAmount() {
			return amount;
		}

		// 전달받은 금액 값을 퇴직급여명세서 객체에 저장한다.
		// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
		// 受け取った金額の値を退職給与明細書オブジェクトに保存する。
		// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
		public void setAmount(long amount) {
			this.amount = amount;
		}
	}

	public static class OtherIncome {
		private String itemName;
		private long annualAmount;
		private long threeMonthAmount;

		// 퇴직급여명세서 객체에 저장된 항목명칭 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 退職給与明細書オブジェクトに保存された項目名称の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getItemName() {
			return itemName;
		}

		// 전달받은 항목명칭 값을 퇴직급여명세서 객체에 저장한다.
		// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
		// 受け取った項目名称の値を退職給与明細書オブジェクトに保存する。
		// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
		public void setItemName(String itemName) {
			this.itemName = itemName;
		}

		// 퇴직급여명세서 객체에 저장된 연간금액 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 退職給与明細書オブジェクトに保存された年間金額の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getAnnualAmount() {
			return annualAmount;
		}

		// 전달받은 연간금액 값을 퇴직급여명세서 객체에 저장한다.
		// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
		// 受け取った年間金額の値を退職給与明細書オブジェクトに保存する。
		// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
		public void setAnnualAmount(long annualAmount) {
			this.annualAmount = annualAmount;
		}

		// 퇴직급여명세서 객체에 저장된 Three월금액 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 退職給与明細書オブジェクトに保存されたThree月金額の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getThreeMonthAmount() {
			return threeMonthAmount;
		}

		// 전달받은 Three월금액 값을 퇴직급여명세서 객체에 저장한다.
		// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
		// 受け取ったThree月金額の値を退職給与明細書オブジェクトに保存する。
		// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
		public void setThreeMonthAmount(long threeMonthAmount) {
			this.threeMonthAmount = threeMonthAmount;
		}
	}
}
