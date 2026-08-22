package erp.employees.model;

/**
 * [인사/사원관리] 사원 입사 추천인 내역 Model DB 테이블: EMPLOYEE_RECOMMENDER
 */
// 사원추천인 정보를 보관하고 관련 기능에서 사용할 수 있도록 제공한다.
// 社員推薦人情報を保持し、関連機能から利用できるように提供する。
public class EmployeeRecommender {

	// [DB 관리 항목]
	// データベースの主キー・外部キー・基本属性を保持し、永続化対象を識別する。
	private int employeeRecommenderId; // 추천인 식별 번호 (PK)
	private int employeeId; // 사원 식별 번호 (FK)

	// [추천인 입력 항목]
	// 画面で入力または照会する業務属性を保持し、登録・詳細表示で共通利用する。
	private String recommenderName; // 추천인 성명
	private String relation; // 관계
	private String companyName; // 소속 회사명
	private String positionName; // 직위명
	private String telNo; // 전화번호

	// 전달받은 값으로 사원추천인 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で社員推薦人オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	public EmployeeRecommender() {
	}

	// Getter & Setter
	// 사원추천인 객체에 저장된 사원추천인식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員推薦人オブジェクトに保存された社員推薦人識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getEmployeeRecommenderId() {
		return employeeRecommenderId;
	}

	// 전달받은 사원추천인식별번호 값을 사원추천인 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った社員推薦人識別番号の値を社員推薦人オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmployeeRecommenderId(int employeeRecommenderId) {
		this.employeeRecommenderId = employeeRecommenderId;
	}

	// 사원추천인 객체에 저장된 사원식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員推薦人オブジェクトに保存された社員識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getEmployeeId() {
		return employeeId;
	}

	// 전달받은 사원식별번호 값을 사원추천인 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った社員識別番号の値を社員推薦人オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	// 사원추천인 객체에 저장된 추천인명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員推薦人オブジェクトに保存された推薦人名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getRecommenderName() {
		return recommenderName;
	}

	// 전달받은 추천인명칭 값을 사원추천인 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った推薦人名称の値を社員推薦人オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setRecommenderName(String recommenderName) {
		this.recommenderName = recommenderName;
	}

	// 사원추천인 객체에 저장된 관계 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員推薦人オブジェクトに保存された関係の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getRelation() {
		return relation;
	}

	// 전달받은 관계 값을 사원추천인 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った関係の値を社員推薦人オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setRelation(String relation) {
		this.relation = relation;
	}

	// 사원추천인 객체에 저장된 사업장명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員推薦人オブジェクトに保存された事業所名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getCompanyName() {
		return companyName;
	}

	// 전달받은 사업장명칭 값을 사원추천인 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った事業所名称の値を社員推薦人オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	// 사원추천인 객체에 저장된 직위명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員推薦人オブジェクトに保存された役職名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getPositionName() {
		return positionName;
	}

	// 전달받은 직위명칭 값을 사원추천인 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った役職名称の値を社員推薦人オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	// 사원추천인 객체에 저장된 Tel번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員推薦人オブジェクトに保存されたTel番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getTelNo() {
		return telNo;
	}

	// 전달받은 Tel번호 값을 사원추천인 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったTel番号の値を社員推薦人オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}
}
