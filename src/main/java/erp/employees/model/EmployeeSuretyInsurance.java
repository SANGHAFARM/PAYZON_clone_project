package erp.employees.model;

import java.util.Date;

/**
 * [인사/사원관리] 사원 신원보증보험 가입 내역 Model DB 테이블: EMPLOYEE_SURETY_INSURANCE
 */
// 사원신원보증보험 정보를 보관하고 관련 기능에서 사용할 수 있도록 제공한다.
// 社員身元保証保険情報を保持し、関連機能から利用できるように提供する。
public class EmployeeSuretyInsurance {

	// [DB 관리 항목]
	// データベースの主キー・外部キー・基本属性を保持し、永続化対象を識別する。
	private int employeeSuretyInsuranceId; // 보증보험 식별 번호 (PK)
	private int employeeId; // 사원 식별 번호 (FK)

	// [보증보험 입력 항목]
	// 保険・税区分と適用基準を確認し、対象金額に合う控除または非課税処理を適用する。
	private String providerName; // 가입기관명
	private String insuranceNo; // 보험번호
	private Long insuranceAmt; // 보험금액
	private Date signupDate; // 가입일자
	private Date expireDate; // 만료일자
	private String note; // 비고

	// 전달받은 값으로 사원신원보증보험 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で社員身元保証保険オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	public EmployeeSuretyInsurance() {
	}

	// Getter & Setter
	// 사원신원보증보험 객체에 저장된 사원신원보증보험식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員身元保証保険オブジェクトに保存された社員身元保証保険識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getEmployeeSuretyInsuranceId() {
		return employeeSuretyInsuranceId;
	}

	// 전달받은 사원신원보증보험식별번호 값을 사원신원보증보험 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った社員身元保証保険識別番号の値を社員身元保証保険オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmployeeSuretyInsuranceId(int employeeSuretyInsuranceId) {
		this.employeeSuretyInsuranceId = employeeSuretyInsuranceId;
	}

	// 사원신원보증보험 객체에 저장된 사원식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員身元保証保険オブジェクトに保存された社員識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getEmployeeId() {
		return employeeId;
	}

	// 전달받은 사원식별번호 값을 사원신원보증보험 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った社員識別番号の値を社員身元保証保険オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	// 사원신원보증보험 객체에 저장된 제공자명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員身元保証保険オブジェクトに保存されたプロバイダー名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getProviderName() {
		return providerName;
	}

	// 전달받은 제공자명칭 값을 사원신원보증보험 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったプロバイダー名称の値を社員身元保証保険オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	// 사원신원보증보험 객체에 저장된 보험번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員身元保証保険オブジェクトに保存された保険番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getInsuranceNo() {
		return insuranceNo;
	}

	// 전달받은 보험번호 값을 사원신원보증보험 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った保険番号の値を社員身元保証保険オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setInsuranceNo(String insuranceNo) {
		this.insuranceNo = insuranceNo;
	}

	// 사원신원보증보험 객체에 저장된 보험금액 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員身元保証保険オブジェクトに保存された保険金額の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Long getInsuranceAmt() {
		return insuranceAmt;
	}

	// 전달받은 보험금액 값을 사원신원보증보험 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った保険金額の値を社員身元保証保険オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setInsuranceAmt(Long insuranceAmt) {
		this.insuranceAmt = insuranceAmt;
	}

	// 사원신원보증보험 객체에 저장된 Signup일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員身元保証保険オブジェクトに保存されたSignup日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Date getSignupDate() {
		return signupDate;
	}

	// 전달받은 Signup일자 값을 사원신원보증보험 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったSignup日付の値を社員身元保証保険オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setSignupDate(Date signupDate) {
		this.signupDate = signupDate;
	}

	// 사원신원보증보험 객체에 저장된 Expire일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員身元保証保険オブジェクトに保存されたExpire日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Date getExpireDate() {
		return expireDate;
	}

	// 전달받은 Expire일자 값을 사원신원보증보험 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったExpire日付の値を社員身元保証保険オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	// 사원신원보증보험 객체에 저장된 비고 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員身元保証保険オブジェクトに保存された備考の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getNote() {
		return note;
	}

	// 전달받은 비고 값을 사원신원보증보험 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った備考の値を社員身元保証保険オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setNote(String note) {
		this.note = note;
	}
}
