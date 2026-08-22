package erp.employees.model;

import java.util.Date;

/**
 * [인사/사원관리] 사원 외국어 능력 이력 Model DB 테이블: EMPLOYEE_LANGUAGE
 */
// 사원어학 정보를 보관하고 관련 기능에서 사용할 수 있도록 제공한다.
// 社員語学情報を保持し、関連機能から利用できるように提供する。
public class EmployeeLanguage {

	// [DB 관리 항목]
	// データベースの主キー・外部キー・基本属性を保持し、永続化対象を識別する。
	private int employeeLanguageId; // 어학능력 식별 번호 (PK)
	private int employeeId; // 사원 식별 번호 (FK)

	// [어학능력 입력 항목]
	// 画面で入力または照会する業務属性を保持し、登録・詳細表示で共通利用する。
	private String langName; // 외국어명
	private String testName; // 시험명
	private String score; // 공인점수
	private Date acqDate; // 취득일
	private String readingLevel; // 독해 능력 수준
	private String writingLevel; // 작문 능력 수준
	private String speakingLevel; // 회화 능력 수준

	// 전달받은 값으로 사원어학 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で社員語学オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	public EmployeeLanguage() {
	}

	// Getter & Setter
	// 사원어학 객체에 저장된 사원어학식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員語学オブジェクトに保存された社員語学識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getEmployeeLanguageId() {
		return employeeLanguageId;
	}

	// 전달받은 사원어학식별번호 값을 사원어학 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った社員語学識別番号の値を社員語学オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmployeeLanguageId(int employeeLanguageId) {
		this.employeeLanguageId = employeeLanguageId;
	}

	// 사원어학 객체에 저장된 사원식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員語学オブジェクトに保存された社員識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getEmployeeId() {
		return employeeId;
	}

	// 전달받은 사원식별번호 값을 사원어학 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った社員識別番号の値を社員語学オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	// 사원어학 객체에 저장된 Lang명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員語学オブジェクトに保存されたLang名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getLangName() {
		return langName;
	}

	// 전달받은 Lang명칭 값을 사원어학 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったLang名称の値を社員語学オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setLangName(String langName) {
		this.langName = langName;
	}

	// 사원어학 객체에 저장된 Test명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員語学オブジェクトに保存されたTest名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getTestName() {
		return testName;
	}

	// 전달받은 Test명칭 값을 사원어학 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったTest名称の値を社員語学オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setTestName(String testName) {
		this.testName = testName;
	}

	// 사원어학 객체에 저장된 점수 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員語学オブジェクトに保存された点数の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getScore() {
		return score;
	}

	// 전달받은 점수 값을 사원어학 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った点数の値を社員語学オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setScore(String score) {
		this.score = score;
	}

	// 사원어학 객체에 저장된 Acq일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員語学オブジェクトに保存されたAcq日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Date getAcqDate() {
		return acqDate;
	}

	// 전달받은 Acq일자 값을 사원어학 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったAcq日付の値を社員語学オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setAcqDate(Date acqDate) {
		this.acqDate = acqDate;
	}

	// 사원어학 객체에 저장된 읽기Level 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員語学オブジェクトに保存された読解Levelの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getReadingLevel() {
		return readingLevel;
	}

	// 전달받은 읽기Level 값을 사원어학 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った読解Levelの値を社員語学オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setReadingLevel(String readingLevel) {
		this.readingLevel = readingLevel;
	}

	// 사원어학 객체에 저장된 쓰기Level 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員語学オブジェクトに保存された作文Levelの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getWritingLevel() {
		return writingLevel;
	}

	// 전달받은 쓰기Level 값을 사원어학 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った作文Levelの値を社員語学オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setWritingLevel(String writingLevel) {
		this.writingLevel = writingLevel;
	}

	// 사원어학 객체에 저장된 말하기Level 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員語学オブジェクトに保存された会話Levelの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getSpeakingLevel() {
		return speakingLevel;
	}

	// 전달받은 말하기Level 값을 사원어학 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った会話Levelの値を社員語学オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setSpeakingLevel(String speakingLevel) {
		this.speakingLevel = speakingLevel;
	}
}
