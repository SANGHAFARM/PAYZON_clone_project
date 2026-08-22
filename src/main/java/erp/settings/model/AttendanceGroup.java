package erp.settings.model;

/**
 * [기본환경설정] 휴가/근태 설정 - 근태그룹 설정 Model DB 테이블: ATTENDANCE_GROUP
 */
// 근태그룹 정보를 보관하고 관련 기능에서 사용할 수 있도록 제공한다.
// 勤怠グループ情報を保持し、関連機能から利用できるように提供する。
public class AttendanceGroup {

	private int attendanceGroupId; // 근태그룹 식별 번호 (PK)
	private String groupName;

	// 전달받은 값으로 근태그룹 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で勤怠グループオブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	public AttendanceGroup() {
	}

	// Getter & Setter
	// 근태그룹 객체에 저장된 근태그룹식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 勤怠グループオブジェクトに保存された勤怠グループ識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getAttendanceGroupId() {
		return attendanceGroupId;
	}

	// 전달받은 근태그룹식별번호 값을 근태그룹 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った勤怠グループ識別番号の値を勤怠グループオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setAttendanceGroupId(int attendanceGroupId) {
		this.attendanceGroupId = attendanceGroupId;
	}

	// 근태그룹 객체에 저장된 그룹명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 勤怠グループオブジェクトに保存されたグループ名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getGroupName() {
		return groupName;
	}

	// 전달받은 그룹명칭 값을 근태그룹 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったグループ名称の値を勤怠グループオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
}
