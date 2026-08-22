package erp.payroll.dto;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// 급여명세서 화면에 필요한 회차와 사원 정보를 전달한다.
// 급여명세서화면 데이터 처리에 필요한 값을 계층 간에 전달한다.
// 給与明細書画面データ処理に必要な値を各階層間で受け渡す。
public class PayrollPayslipPage {

	private Date calculationStart;
	private Date calculationEnd;
	private Date paymentDate;
	private List<PayrollPayslipEmployee> employees;
	private PayrollPayslipEmployee selectedEmployee;
	private List<PayrollRegisterColumn> paymentItems;
	private List<PayrollRegisterColumn> deductionItems;
	private PayrollPayslipCompany company;

	// 급여명세서화면 데이터 객체에 저장된 계산Start 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 給与明細書画面データオブジェクトに保存された計算Startの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Date getCalculationStart() {
		return calculationStart;
	}

	// 전달받은 계산Start 값을 급여명세서화면 데이터 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った計算Startの値を給与明細書画面データオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setCalculationStart(Date calculationStart) {
		this.calculationStart = calculationStart;
	}

	// 급여명세서화면 데이터 객체에 저장된 계산End 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 給与明細書画面データオブジェクトに保存された計算Endの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Date getCalculationEnd() {
		return calculationEnd;
	}

	// 전달받은 계산End 값을 급여명세서화면 데이터 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った計算Endの値を給与明細書画面データオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setCalculationEnd(Date calculationEnd) {
		this.calculationEnd = calculationEnd;
	}

	// 급여명세서화면 데이터 객체에 저장된 지급일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 給与明細書画面データオブジェクトに保存された支給日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Date getPaymentDate() {
		return paymentDate;
	}

	// 전달받은 지급일자 값을 급여명세서화면 데이터 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った支給日付の値を給与明細書画面データオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	// 급여명세서화면 데이터 객체에 저장된 사원 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 給与明細書画面データオブジェクトに保存された社員の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public List<PayrollPayslipEmployee> getEmployees() {
		return employees;
	}

	// 전달받은 사원 값을 급여명세서화면 데이터 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った社員の値を給与明細書画面データオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmployees(List<PayrollPayslipEmployee> employees) {
		this.employees = employees;
	}

	// 급여명세서화면 데이터 객체에 저장된 Selected사원 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 給与明細書画面データオブジェクトに保存されたSelected社員の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public PayrollPayslipEmployee getSelectedEmployee() {
		return selectedEmployee;
	}

	// 전달받은 Selected사원 값을 급여명세서화면 데이터 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったSelected社員の値を給与明細書画面データオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setSelectedEmployee(PayrollPayslipEmployee selectedEmployee) {
		this.selectedEmployee = selectedEmployee;
	}

	// 급여명세서화면 데이터 객체에 저장된 지급항목 목록 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 給与明細書画面データオブジェクトに保存された支給項目一覧の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public List<PayrollRegisterColumn> getPaymentItems() {
		return paymentItems;
	}

	// 전달받은 지급항목 목록 값을 급여명세서화면 데이터 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った支給項目一覧の値を給与明細書画面データオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setPaymentItems(List<PayrollRegisterColumn> paymentItems) {
		this.paymentItems = paymentItems;
	}

	// 급여명세서화면 데이터 객체에 저장된 공제항목 목록 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 給与明細書画面データオブジェクトに保存された控除項目一覧の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public List<PayrollRegisterColumn> getDeductionItems() {
		return deductionItems;
	}

	// 전달받은 공제항목 목록 값을 급여명세서화면 데이터 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った控除項目一覧の値を給与明細書画面データオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setDeductionItems(List<PayrollRegisterColumn> deductionItems) {
		this.deductionItems = deductionItems;
	}

	// 급여명세서화면 데이터 객체에 저장된 사업장 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 給与明細書画面データオブジェクトに保存された事業所の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public PayrollPayslipCompany getCompany() {
		return company;
	}

	// 전달받은 사업장 값을 급여명세서화면 데이터 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った事業所の値を給与明細書画面データオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setCompany(PayrollPayslipCompany company) {
		this.company = company;
	}

	public static class PayrollPayslipCompany {
		private String companyName;
		private String representativeName;
		private String logoUrl;
		private String stampUrl;
		// 급여명세서화면 데이터 객체에 저장된 사업장명칭 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与明細書画面データオブジェクトに保存された事業所名称の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getCompanyName() { return companyName; }
		public void setCompanyName(String value) { companyName = value; }
		// 급여명세서화면 데이터 객체에 저장된 Representative명칭 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与明細書画面データオブジェクトに保存されたRepresentative名称の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getRepresentativeName() { return representativeName; }
		public void setRepresentativeName(String value) { representativeName = value; }
		// 급여명세서화면 데이터 객체에 저장된 로고주소 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与明細書画面データオブジェクトに保存されたロゴアドレスの値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getLogoUrl() { return logoUrl; }
		public void setLogoUrl(String value) { logoUrl = value; }
		// 급여명세서화면 데이터 객체에 저장된 도장주소 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与明細書画面データオブジェクトに保存された印鑑アドレスの値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getStampUrl() { return stampUrl; }
		public void setStampUrl(String value) { stampUrl = value; }
	}

	// 급여명세서의 사원 기본정보와 지급·공제 내역
	// 支給額・控除額・期間値を業務基準に従って計算し、合計と最終金額へ反映する。
	public static class PayrollPayslipEmployee {
		private int employeeId;
		private String employmentTypeName;
		private String employeeName;
		private String birthDate;
		private String departmentName;
		private String positionName;
		private Date hireDate;
		private Map<Integer, Long> paymentAmounts = new LinkedHashMap<>();
		private Map<Integer, Long> deductionAmounts = new LinkedHashMap<>();
		private Map<Integer, String> paymentCalculations = new LinkedHashMap<>();
		private Map<Integer, String> deductionCalculations = new LinkedHashMap<>();
		// 급여명세서화면 데이터 객체에 저장된 사원식별번호 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与明細書画面データオブジェクトに保存された社員識別番号の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public int getEmployeeId() { return employeeId; }
		public void setEmployeeId(int value) { employeeId = value; }
		// 급여명세서화면 데이터 객체에 저장된 고용구분명칭 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与明細書画面データオブジェクトに保存された雇用区分名称の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getEmploymentTypeName() { return employmentTypeName; }
		public void setEmploymentTypeName(String value) { employmentTypeName = value; }
		// 급여명세서화면 데이터 객체에 저장된 사원명칭 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与明細書画面データオブジェクトに保存された社員名称の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getEmployeeName() { return employeeName; }
		public void setEmployeeName(String value) { employeeName = value; }
		// 급여명세서화면 데이터 객체에 저장된 Birth일자 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与明細書画面データオブジェクトに保存されたBirth日付の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getBirthDate() { return birthDate; }
		public void setBirthDate(String value) { birthDate = value; }
		// 급여명세서화면 데이터 객체에 저장된 부서명칭 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与明細書画面データオブジェクトに保存された部署名称の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getDepartmentName() { return departmentName; }
		public void setDepartmentName(String value) { departmentName = value; }
		// 급여명세서화면 데이터 객체에 저장된 직위명칭 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与明細書画面データオブジェクトに保存された役職名称の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public String getPositionName() { return positionName; }
		public void setPositionName(String value) { positionName = value; }
		// 급여명세서화면 데이터 객체에 저장된 Hire일자 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与明細書画面データオブジェクトに保存されたHire日付の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public Date getHireDate() { return hireDate; }
		public void setHireDate(Date value) { hireDate = value; }
		// 급여명세서화면 데이터 객체에 저장된 지급금액 목록 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与明細書画面データオブジェクトに保存された支給金額一覧の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public Map<Integer, Long> getPaymentAmounts() { return paymentAmounts; }
		// 급여명세서화면 데이터 객체에 저장된 공제금액 목록 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与明細書画面データオブジェクトに保存された控除金額一覧の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public Map<Integer, Long> getDeductionAmounts() { return deductionAmounts; }
		// 급여명세서화면 데이터 객체에 저장된 지급Calculations 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与明細書画面データオブジェクトに保存された支給Calculationsの値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public Map<Integer, String> getPaymentCalculations() { return paymentCalculations; }
		// 급여명세서화면 데이터 객체에 저장된 공제Calculations 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与明細書画面データオブジェクトに保存された控除Calculationsの値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public Map<Integer, String> getDeductionCalculations() { return deductionCalculations; }
		// 급여명세서화면 데이터 객체에 저장된 합계지급 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与明細書画面データオブジェクトに保存された合計支給の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getTotalPayment() { return sum(paymentAmounts); }
		// 급여명세서화면 데이터 객체에 저장된 합계공제 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与明細書画面データオブジェクトに保存された合計控除の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getTotalDeduction() { return sum(deductionAmounts); }
		// 급여명세서화면 데이터 객체에 저장된 Net지급 값을 반환한다.
		// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
		// 給与明細書画面データオブジェクトに保存されたNet支給の値を返す。
		// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
		public long getNetPayment() { return getTotalPayment() - getTotalDeduction(); }
		private long sum(Map<Integer, Long> values) { long total = 0; for (Long value : values.values()) total += value == null ? 0 : value; return total; }
	}
}
