package erp.employees.model;

import java.util.Date;

/**
 * [인사/사원관리] 사원별 4대보험 취득/상실 이력 Model DB 테이블: EMPLOYEE_INSURANCE_HISTORY
 */
// 사원보험이력 정보를 보관하고 관련 기능에서 사용할 수 있도록 제공한다.
// 社員保険履歴情報を保持し、関連機能から利用できるように提供する。
public class EmployeeInsuranceHistory {

	// [DB 관리 항목]
	// データベースの主キー・外部キー・基本属性を保持し、永続化対象を識別する。
	private int employeeInsuranceHistoryId; // 4대보험 자격정보 식별 번호 (PK)
	private int employeeId; // 사원 식별 번호 (FK)

	// [4대보험 취득·상실 이력]
	// 保険・税区分と適用基準を確認し、対象金額に合う控除または非課税処理を適用する。
	private String insuranceType; // 구분 (국민연금 등)
	private String symbolNo; // 기호번호
	private Date acquireDate; // 취득일
	private Date lossDate; // 상실일

	// 전달받은 값으로 사원보험이력 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で社員保険履歴オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	public EmployeeInsuranceHistory() {
	}

	// Getter & Setter
	// 사원보험이력 객체에 저장된 사원보험이력식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員保険履歴オブジェクトに保存された社員保険履歴識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getEmployeeInsuranceHistoryId() {
		return employeeInsuranceHistoryId;
	}

	// 전달받은 사원보험이력식별번호 값을 사원보험이력 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った社員保険履歴識別番号の値を社員保険履歴オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmployeeInsuranceHistoryId(int employeeInsuranceHistoryId) {
		this.employeeInsuranceHistoryId = employeeInsuranceHistoryId;
	}

	// 사원보험이력 객체에 저장된 사원식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員保険履歴オブジェクトに保存された社員識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getEmployeeId() {
		return employeeId;
	}

	// 전달받은 사원식별번호 값을 사원보험이력 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った社員識別番号の値を社員保険履歴オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	// 사원보험이력 객체에 저장된 보험구분 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員保険履歴オブジェクトに保存された保険区分の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getInsuranceType() {
		return insuranceType;
	}

	// 전달받은 보험구분 값을 사원보험이력 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った保険区分の値を社員保険履歴オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setInsuranceType(String insuranceType) {
		this.insuranceType = insuranceType;
	}

	// 사원보험이력 객체에 저장된 Symbol번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員保険履歴オブジェクトに保存されたSymbol番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getSymbolNo() {
		return symbolNo;
	}

	// 전달받은 Symbol번호 값을 사원보험이력 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったSymbol番号の値を社員保険履歴オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setSymbolNo(String symbolNo) {
		this.symbolNo = symbolNo;
	}

	// 사원보험이력 객체에 저장된 Acquire일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員保険履歴オブジェクトに保存されたAcquire日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Date getAcquireDate() {
		return acquireDate;
	}

	// 전달받은 Acquire일자 값을 사원보험이력 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったAcquire日付の値を社員保険履歴オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setAcquireDate(Date acquireDate) {
		this.acquireDate = acquireDate;
	}

	// 사원보험이력 객체에 저장된 Loss일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員保険履歴オブジェクトに保存されたLoss日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Date getLossDate() {
		return lossDate;
	}

	// 전달받은 Loss일자 값을 사원보험이력 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったLoss日付の値を社員保険履歴オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setLossDate(Date lossDate) {
		this.lossDate = lossDate;
	}
}
