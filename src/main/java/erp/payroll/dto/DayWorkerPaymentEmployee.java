package erp.payroll.dto;

import java.util.ArrayList;
import java.util.List;

// 일용직 급여입력 화면에 표시할 사원 정보를 전달한다.
// 일용직근로자지급사원 정보를 보관하고 관련 기능에서 사용할 수 있도록 제공한다.
// 日雇い労働者支給社員情報を保持し、関連機能から利用できるように提供する。
public class DayWorkerPaymentEmployee {

	private int employeeId;
	private String employmentTypeName;
	private String employeeNumber;
	private String employeeName;
	private String departmentName;
	private String positionName;
	private String statusName;
	private long nationalPension;
	private long healthInsurance;
	private long longTermCareInsurance;
	private long employmentInsurance;
	private long incomeTax;
	private long localIncomeTax;
	private long mutualAidFee;
	private long totalPayment;
	private List<DayWorkerPaymentPage.DayWorkerPaymentWork> workPayments = new ArrayList<>();

	// 일용직근로자지급사원 객체에 저장된 사원식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い労働者支給社員オブジェクトに保存された社員識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getEmployeeId() {
		return employeeId;
	}

	// 전달받은 사원식별번호 값을 일용직근로자지급사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った社員識別番号の値を日雇い労働者支給社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	// 일용직근로자지급사원 객체에 저장된 고용구분명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い労働者支給社員オブジェクトに保存された雇用区分名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getEmploymentTypeName() {
		return employmentTypeName;
	}

	// 전달받은 고용구분명칭 값을 일용직근로자지급사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った雇用区分名称の値を日雇い労働者支給社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmploymentTypeName(String employmentTypeName) {
		this.employmentTypeName = employmentTypeName;
	}

	// 일용직근로자지급사원 객체에 저장된 사원번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い労働者支給社員オブジェクトに保存された社員番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getEmployeeNumber() {
		return employeeNumber;
	}

	// 전달받은 사원번호 값을 일용직근로자지급사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った社員番号の値を日雇い労働者支給社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	// 일용직근로자지급사원 객체에 저장된 사원명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い労働者支給社員オブジェクトに保存された社員名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getEmployeeName() {
		return employeeName;
	}

	// 전달받은 사원명칭 값을 일용직근로자지급사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った社員名称の値を日雇い労働者支給社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	// 일용직근로자지급사원 객체에 저장된 부서명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い労働者支給社員オブジェクトに保存された部署名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getDepartmentName() {
		return departmentName;
	}

	// 전달받은 부서명칭 값을 일용직근로자지급사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った部署名称の値を日雇い労働者支給社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	// 일용직근로자지급사원 객체에 저장된 직위명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い労働者支給社員オブジェクトに保存された役職名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getPositionName() {
		return positionName;
	}

	// 전달받은 직위명칭 값을 일용직근로자지급사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った役職名称の値を日雇い労働者支給社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	// 일용직근로자지급사원 객체에 저장된 상태명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い労働者支給社員オブジェクトに保存された状態名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getStatusName() {
		return statusName;
	}

	// 전달받은 상태명칭 값을 일용직근로자지급사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った状態名称の値を日雇い労働者支給社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	// 일용직근로자지급사원 객체에 저장된 국민연금 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い労働者支給社員オブジェクトに保存された国民年金の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getNationalPension() {
		return nationalPension;
	}

	// 전달받은 국민연금 값을 일용직근로자지급사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った国民年金の値を日雇い労働者支給社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setNationalPension(long nationalPension) {
		this.nationalPension = nationalPension;
	}

	// 일용직근로자지급사원 객체에 저장된 건강보험 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い労働者支給社員オブジェクトに保存された健康保険の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getHealthInsurance() {
		return healthInsurance;
	}

	// 전달받은 건강보험 값을 일용직근로자지급사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った健康保険の値を日雇い労働者支給社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setHealthInsurance(long healthInsurance) {
		this.healthInsurance = healthInsurance;
	}

	// 일용직근로자지급사원 객체에 저장된 정수Term장기요양보험 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い労働者支給社員オブジェクトに保存された整数Term介護保険の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getLongTermCareInsurance() {
		return longTermCareInsurance;
	}

	// 전달받은 정수Term장기요양보험 값을 일용직근로자지급사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った整数Term介護保険の値を日雇い労働者支給社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setLongTermCareInsurance(long longTermCareInsurance) {
		this.longTermCareInsurance = longTermCareInsurance;
	}

	// 일용직근로자지급사원 객체에 저장된 고용보험 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い労働者支給社員オブジェクトに保存された雇用保険の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getEmploymentInsurance() {
		return employmentInsurance;
	}

	// 전달받은 고용보험 값을 일용직근로자지급사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った雇用保険の値を日雇い労働者支給社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmploymentInsurance(long employmentInsurance) {
		this.employmentInsurance = employmentInsurance;
	}

	// 일용직근로자지급사원 객체에 저장된 소득세금 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い労働者支給社員オブジェクトに保存された所得税金の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getIncomeTax() {
		return incomeTax;
	}

	// 전달받은 소득세금 값을 일용직근로자지급사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った所得税金の値を日雇い労働者支給社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setIncomeTax(long incomeTax) {
		this.incomeTax = incomeTax;
	}

	// 일용직근로자지급사원 객체에 저장된 Local소득세금 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い労働者支給社員オブジェクトに保存されたLocal所得税金の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getLocalIncomeTax() {
		return localIncomeTax;
	}

	// 전달받은 Local소득세금 값을 일용직근로자지급사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったLocal所得税金の値を日雇い労働者支給社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setLocalIncomeTax(long localIncomeTax) {
		this.localIncomeTax = localIncomeTax;
	}

	// 일용직근로자지급사원 객체에 저장된 상조회지원회비 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い労働者支給社員オブジェクトに保存された親睦支援会費の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getMutualAidFee() {
		return mutualAidFee;
	}

	// 전달받은 상조회지원회비 값을 일용직근로자지급사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った親睦支援会費の値を日雇い労働者支給社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setMutualAidFee(long mutualAidFee) {
		this.mutualAidFee = mutualAidFee;
	}

	// 일용직근로자지급사원 객체에 저장된 합계지급 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い労働者支給社員オブジェクトに保存された合計支給の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getTotalPayment() {
		return totalPayment;
	}

	// 전달받은 합계지급 값을 일용직근로자지급사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った合計支給の値を日雇い労働者支給社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setTotalPayment(long totalPayment) {
		this.totalPayment = totalPayment;
	}

	// 일용직근로자지급사원 객체에 저장된 근무지급내역 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い労働者支給社員オブジェクトに保存された勤務支給明細の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public List<DayWorkerPaymentPage.DayWorkerPaymentWork> getWorkPayments() {
		return workPayments;
	}

	// 전달받은 근무지급내역 값을 일용직근로자지급사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った勤務支給明細の値を日雇い労働者支給社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setWorkPayments(List<DayWorkerPaymentPage.DayWorkerPaymentWork> workPayments) {
		this.workPayments = workPayments;
	}

	// 일용직근로자지급사원 객체에 저장된 합계공제 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い労働者支給社員オブジェクトに保存された合計控除の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getTotalDeduction() {
		return nationalPension + healthInsurance + longTermCareInsurance + employmentInsurance + incomeTax
				+ localIncomeTax + mutualAidFee;
	}

	// 일용직근로자지급사원 객체에 저장된 Net지급 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い労働者支給社員オブジェクトに保存されたNet支給の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getNetPayment() {
		return totalPayment - getTotalDeduction();
	}
}
