package erp.attendance.service.request;
/*
 * 근태관리>휴가조회에서 검색조건을 담을 클래스
 * 勤怠管理→休暇照会で検索条件を入れるクラス
*/
// 휴가조회요청정보 처리에 필요한 값을 계층 간에 전달한다.
// 休暇照会リクエスト情報処理に必要な値を各階層間で受け渡す。
public class LeaveInquiryRequest {
	private int leaveItemId;         // 휴가항목ID(休暇項目ID)
	private String keyword;          // 검색어(検索キーワード)
	private String status;           // 상태(状態)
	private String empType;          // 사원구분(社員区分)
	private Integer departmentId;    // 부서ID(部署ID)
	private Integer jobPositionId;   // 직위ID(役職ID)
	private int pageSize;            // 페이지 크기(ページサイズ)
	// 전달받은 값으로 휴가조회요청정보 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で休暇照会リクエスト情報オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	public LeaveInquiryRequest(int leaveItemId, String keyword, String status, String empType, Integer departmentId,
			Integer jobPositionId, int pageSize) {
		super();
		this.leaveItemId = leaveItemId;
		this.keyword = keyword;
		this.status = status;
		this.empType = empType;
		this.departmentId = departmentId;
		this.jobPositionId = jobPositionId;
		this.pageSize = pageSize;
	}
	// 전달받은 값으로 휴가조회요청정보 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で休暇照会リクエスト情報オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	public LeaveInquiryRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	// 휴가조회요청정보 객체에 저장된 휴가항목식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 休暇照会リクエスト情報オブジェクトに保存された休暇項目識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getLeaveItemId() {
		return leaveItemId;
	}
	// 전달받은 휴가항목식별번호 값을 휴가조회요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った休暇項目識別番号の値を休暇照会リクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setLeaveItemId(int leaveItemId) {
		this.leaveItemId = leaveItemId;
	}
	// 휴가조회요청정보 객체에 저장된 검색어 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 休暇照会リクエスト情報オブジェクトに保存された検索語の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getKeyword() {
		return keyword;
	}
	// 전달받은 검색어 값을 휴가조회요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った検索語の値を休暇照会リクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	// 휴가조회요청정보 객체에 저장된 상태 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 休暇照会リクエスト情報オブジェクトに保存された状態の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getStatus() {
		return status;
	}
	// 전달받은 상태 값을 휴가조회요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った状態の値を休暇照会リクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setStatus(String status) {
		this.status = status;
	}
	// 휴가조회요청정보 객체에 저장된 Emp구분 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 休暇照会リクエスト情報オブジェクトに保存されたEmp区分の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getEmpType() {
		return empType;
	}
	// 전달받은 Emp구분 값을 휴가조회요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったEmp区分の値を休暇照会リクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmpType(String empType) {
		this.empType = empType;
	}
	// 휴가조회요청정보 객체에 저장된 부서식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 休暇照会リクエスト情報オブジェクトに保存された部署識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Integer getDepartmentId() {
		return departmentId;
	}
	// 전달받은 부서식별번호 값을 휴가조회요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った部署識別番号の値を休暇照会リクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}
	// 휴가조회요청정보 객체에 저장된 직무직위식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 休暇照会リクエスト情報オブジェクトに保存された職務役職識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Integer getJobPositionId() {
		return jobPositionId;
	}
	// 전달받은 직무직위식별번호 값을 휴가조회요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った職務役職識別番号の値を休暇照会リクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setJobPositionId(Integer jobPositionId) {
		this.jobPositionId = jobPositionId;
	}
	// 휴가조회요청정보 객체에 저장된 페이지 사이즈 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 休暇照会リクエスト情報オブジェクトに保存されたページサイズの値の返す
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。

	public int getPageSize() {
		return pageSize;
	}
	//전달받은 페이지 사이즈 값을 휴가조회요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったページサイズの値を休暇照会リクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	



}
