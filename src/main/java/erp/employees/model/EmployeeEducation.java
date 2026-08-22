package erp.employees.model;

/**
 * [인사/사원관리] 사원 학력 이력 Model DB 테이블: EMPLOYEE_EDUCATION
 */
// 사원학력 정보를 보관하고 관련 기능에서 사용할 수 있도록 제공한다.
// 社員学歴情報を保持し、関連機能から利用できるように提供する。
public class EmployeeEducation {

	// [DB 관리 항목]
	// データベースの主キー・外部キー・基本属性を保持し、永続化対象を識別する。
	private int employeeEducationId; // 학력 이력 식별 번호 (PK)
	private int employeeId; // 사원 식별 번호 (FK)

	// [학력 입력 항목]
	// 画面で入力または照会する業務属性を保持し、登録・詳細表示で共通利用する。
	private String eduType; // 구분 (초등학교, 대학교 등)
	private String admissionYm; // 입학년월
	private String gradYm; // 졸업년월
	private String schoolName; // 학교명
	private String majorName; // 전공
	private String completeType; // 이수 (졸업, 재학 등)

	// 전달받은 값으로 사원학력 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で社員学歴オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	public EmployeeEducation() {
	}

	// Getter & Setter
	// 사원학력 객체에 저장된 사원학력식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員学歴オブジェクトに保存された社員学歴識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getEmployeeEducationId() {
		return employeeEducationId;
	}

	// 전달받은 사원학력식별번호 값을 사원학력 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った社員学歴識別番号の値を社員学歴オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmployeeEducationId(int employeeEducationId) {
		this.employeeEducationId = employeeEducationId;
	}

	// 사원학력 객체에 저장된 사원식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員学歴オブジェクトに保存された社員識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getEmployeeId() {
		return employeeId;
	}

	// 전달받은 사원식별번호 값을 사원학력 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った社員識別番号の値を社員学歴オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	// 사원학력 객체에 저장된 Edu구분 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員学歴オブジェクトに保存されたEdu区分の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getEduType() {
		return eduType;
	}

	// 전달받은 Edu구분 값을 사원학력 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったEdu区分の値を社員学歴オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEduType(String eduType) {
		this.eduType = eduType;
	}

	// 사원학력 객체에 저장된 입학연월 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員学歴オブジェクトに保存された入学年月の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getAdmissionYm() {
		return admissionYm;
	}

	// 전달받은 입학연월 값을 사원학력 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った入学年月の値を社員学歴オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setAdmissionYm(String admissionYm) {
		this.admissionYm = admissionYm;
	}

	// 사원학력 객체에 저장된 졸업연월 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員学歴オブジェクトに保存された卒業年月の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getGradYm() {
		return gradYm;
	}

	// 전달받은 졸업연월 값을 사원학력 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った卒業年月の値を社員学歴オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setGradYm(String gradYm) {
		this.gradYm = gradYm;
	}

	// 사원학력 객체에 저장된 School명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員学歴オブジェクトに保存されたSchool名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getSchoolName() {
		return schoolName;
	}

	// 전달받은 School명칭 값을 사원학력 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったSchool名称の値を社員学歴オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	// 사원학력 객체에 저장된 Major명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員学歴オブジェクトに保存されたMajor名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getMajorName() {
		return majorName;
	}

	// 전달받은 Major명칭 값을 사원학력 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったMajor名称の値を社員学歴オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setMajorName(String majorName) {
		this.majorName = majorName;
	}

	// 사원학력 객체에 저장된 Complete구분 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員学歴オブジェクトに保存されたComplete区分の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getCompleteType() {
		return completeType;
	}

	// 전달받은 Complete구분 값을 사원학력 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったComplete区分の値を社員学歴オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setCompleteType(String completeType) {
		this.completeType = completeType;
	}
}
