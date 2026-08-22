package erp.attendance.model;

// 사원휴가Balance 정보를 보관하고 관련 기능에서 사용할 수 있도록 제공한다.
// 社員休暇Balance情報を保持し、関連機能から利用できるように提供する。
public class EmployeeLeaveBalance {
	int employeeLeaveBalanceId;
	int employeeId;
	int leaveItemId;
	double totalDays;
	// 전달받은 값으로 사원휴가Balance 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で社員休暇Balanceオブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	public EmployeeLeaveBalance() {
	}
	// 전달받은 값으로 사원휴가Balance 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で社員休暇Balanceオブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	public EmployeeLeaveBalance(int employeeLeaveBalanceId, int employeeId, int leaveItemId, double totalDays) {
		super();
		this.employeeLeaveBalanceId = employeeLeaveBalanceId;
		this.employeeId = employeeId;
		this.leaveItemId = leaveItemId;
		this.totalDays = totalDays;
	}
	// 사원휴가Balance 객체에 저장된 사원휴가Balance식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員休暇Balanceオブジェクトに保存された社員休暇Balance識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getEmployeeLeaveBalanceId() {
		return employeeLeaveBalanceId;
	}
	// 전달받은 사원휴가Balance식별번호 값을 사원휴가Balance 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った社員休暇Balance識別番号の値を社員休暇Balanceオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmployeeLeaveBalanceId(int employeeLeaveBalanceId) {
		this.employeeLeaveBalanceId = employeeLeaveBalanceId;
	}
	// 사원휴가Balance 객체에 저장된 사원식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員休暇Balanceオブジェクトに保存された社員識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getEmployeeId() {
		return employeeId;
	}
	// 전달받은 사원식별번호 값을 사원휴가Balance 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った社員識別番号の値を社員休暇Balanceオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	// 사원휴가Balance 객체에 저장된 휴가항목식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員休暇Balanceオブジェクトに保存された休暇項目識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getLeaveItemId() {
		return leaveItemId;
	}
	// 전달받은 휴가항목식별번호 값을 사원휴가Balance 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った休暇項目識別番号の値を社員休暇Balanceオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setLeaveItemId(int leaveItemId) {
		this.leaveItemId = leaveItemId;
	}
	// 사원휴가Balance 객체에 저장된 합계일수 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員休暇Balanceオブジェクトに保存された合計日数の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public double getTotalDays() {
		return totalDays;
	}
	// 전달받은 합계일수 값을 사원휴가Balance 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った合計日数の値を社員休暇Balanceオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setTotalDays(double totalDays) {
		this.totalDays = totalDays;
	}

}
