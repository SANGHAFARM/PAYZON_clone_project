package erp.attendance.service.request;

// 월간근태요청정보 처리에 필요한 값을 계층 간에 전달한다.
// 月間勤怠リクエスト情報処理に必要な値を各階層間で受け渡す。
public class AttendanceMonthlySearchRequest {
	int year;
	int month;
	String status;
	String empType;
	Integer departmentId;
	Integer jobPositionId;
	// 전달받은 값으로 월간근태요청정보 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で月間勤怠リクエスト情報オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	public AttendanceMonthlySearchRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	// 전달받은 값으로 월간근태요청정보 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で月間勤怠リクエスト情報オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	public AttendanceMonthlySearchRequest(int year, int month, String status, String empType, Integer departmentId,
			Integer jobPositionId) {
		super();
		this.year = year;
		this.month = month;
		this.status = status;
		this.empType = empType;
		this.departmentId = departmentId;
		this.jobPositionId = jobPositionId;
	}
	// 월간근태요청정보 객체에 저장된 연도 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 月間勤怠リクエスト情報オブジェクトに保存された年度の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getYear() {
		return year;
	}
	// 전달받은 연도 값을 월간근태요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った年度の値を月間勤怠リクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setYear(int year) {
		this.year = year;
	}
	// 월간근태요청정보 객체에 저장된 월 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 月間勤怠リクエスト情報オブジェクトに保存された月の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getMonth() {
		return month;
	}
	// 전달받은 월 값을 월간근태요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った月の値を月間勤怠リクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setMonth(int month) {
		this.month = month;
	}
	// 월간근태요청정보 객체에 저장된 상태 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 月間勤怠リクエスト情報オブジェクトに保存された状態の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getStatus() {
		return status;
	}
	// 전달받은 상태 값을 월간근태요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った状態の値を月間勤怠リクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setStatus(String status) {
		this.status = status;
	}
	// 월간근태요청정보 객체에 저장된 Emp구분 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 月間勤怠リクエスト情報オブジェクトに保存されたEmp区分の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getEmpType() {
		return empType;
	}
	// 전달받은 Emp구분 값을 월간근태요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったEmp区分の値を月間勤怠リクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmpType(String empType) {
		this.empType = empType;
	}
	// 월간근태요청정보 객체에 저장된 부서식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 月間勤怠リクエスト情報オブジェクトに保存された部署識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Integer getDepartmentId() {
		return departmentId;
	}
	// 전달받은 부서식별번호 값을 월간근태요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った部署識別番号の値を月間勤怠リクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}
	// 월간근태요청정보 객체에 저장된 직무직위식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 月間勤怠リクエスト情報オブジェクトに保存された職務役職識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Integer getJobPositionId() {
		return jobPositionId;
	}
	// 전달받은 직무직위식별번호 값을 월간근태요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った職務役職識別番号の値を月間勤怠リクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setJobPositionId(Integer jobPositionId) {
		this.jobPositionId = jobPositionId;
	}
	
}
