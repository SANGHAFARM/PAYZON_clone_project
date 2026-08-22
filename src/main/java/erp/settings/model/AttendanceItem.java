package erp.settings.model;

// [기본환경설정] 휴가/근태 설정 - 근태항목 설정 Model DB 테이블: ATTENDANCE_ITEM
// 근태항목 처리에 필요한 값을 계층 간에 전달한다.
// 勤怠項目処理に必要な値を各階層間で受け渡す。
public class AttendanceItem {

	private int attendanceItemId; // 근태항목 식별 번호 (PK)
	private int attendanceGroupId; // 근태그룹 외래키 (FK)
	private String attendName;
	private String unitType;
	private String useYn;

	private Integer deductLeaveId; // 휴가공제 외래키 (선택적 FK)
	private String workHourType; // 근로시간연계

	private String groupName;
	private String leaveName;

	// 전달받은 값으로 근태항목 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で勤怠項目オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	public AttendanceItem() {
	}

	// 비즈니스 로직(예시): 휴가 차감 연동 여부 확인
	// 휴가Deductible 조건의 충족 여부를 확인하여 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 休暇Deductible条件を満たしているか確認して返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public boolean isLeaveDeductible() {
		return this.deductLeaveId != null && this.deductLeaveId > 0;
	}

	// Getter & Setter
	// 근태항목 객체에 저장된 근태항목식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 勤怠項目オブジェクトに保存された勤怠項目識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getAttendanceItemId() {
		return attendanceItemId;
	}

	// 전달받은 근태항목식별번호 값을 근태항목 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った勤怠項目識別番号の値を勤怠項目オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setAttendanceItemId(int attendanceItemId) {
		this.attendanceItemId = attendanceItemId;
	}

	// 근태항목 객체에 저장된 근태그룹식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 勤怠項目オブジェクトに保存された勤怠グループ識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getAttendanceGroupId() {
		return attendanceGroupId;
	}

	// 전달받은 근태그룹식별번호 값을 근태항목 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った勤怠グループ識別番号の値を勤怠項目オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setAttendanceGroupId(int attendanceGroupId) {
		this.attendanceGroupId = attendanceGroupId;
	}

	// 근태항목 객체에 저장된 Attend명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 勤怠項目オブジェクトに保存されたAttend名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getAttendName() {
		return attendName;
	}

	// 전달받은 Attend명칭 값을 근태항목 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったAttend名称の値を勤怠項目オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setAttendName(String attendName) {
		this.attendName = attendName;
	}

	// 근태항목 객체에 저장된 단위구분 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 勤怠項目オブジェクトに保存された単位区分の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getUnitType() {
		return unitType;
	}

	// 전달받은 단위구분 값을 근태항목 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った単位区分の値を勤怠項目オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}

	// 근태항목 객체에 저장된 공제휴가식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 勤怠項目オブジェクトに保存された控除休暇識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Integer getDeductLeaveId() {
		return deductLeaveId;
	}

	// 전달받은 공제휴가식별번호 값을 근태항목 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った控除休暇識別番号の値を勤怠項目オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setDeductLeaveId(Integer deductLeaveId) {
		this.deductLeaveId = deductLeaveId;
	}

	// 근태항목 객체에 저장된 근무Hour구분 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 勤怠項目オブジェクトに保存された勤務Hour区分の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getWorkHourType() {
		return workHourType;
	}

	// 전달받은 근무Hour구분 값을 근태항목 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った勤務Hour区分の値を勤怠項目オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setWorkHourType(String workHourType) {
		this.workHourType = workHourType;
	}

	// 근태항목 객체에 저장된 사용여부 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 勤怠項目オブジェクトに保存された使用可否の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getUseYn() {
		return useYn;
	}

	// 전달받은 사용여부 값을 근태항목 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った使用可否の値を勤怠項目オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	// 근태항목 객체에 저장된 그룹명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 勤怠項目オブジェクトに保存されたグループ名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getGroupName() {
		return groupName;
	}

	// 전달받은 그룹명칭 값을 근태항목 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったグループ名称の値を勤怠項目オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	// 근태항목 객체에 저장된 휴가명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 勤怠項目オブジェクトに保存された休暇名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getLeaveName() {
		return leaveName;
	}

	// 전달받은 휴가명칭 값을 근태항목 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った休暇名称の値を勤怠項目オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setLeaveName(String leaveName) {
		this.leaveName = leaveName;
	}
}
