package erp.attendance.service.request;

import java.util.Date;
import java.util.Map;

/*
 * 근태 조회탭의 상세 조회 검색에 사용할 모델
*/
// 근태상세정보요청정보 처리에 필요한 값을 계층 간에 전달한다.
// 勤怠詳細情報リクエスト情報処理に必要な値を各階層間で受け渡す。
public class AttendanceDetailRequest {
	private Date inputDate;
	private Date startDate;
	private Date endDate;
	private Integer departmentId;
	private Integer attendanceGroupId;
    private Integer attendanceItemId;
    private Integer leaveItemId;
	private String empNameKr;
    private String note;
	// 전달받은 값으로 근태상세정보요청정보 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で勤怠詳細情報リクエスト情報オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	public AttendanceDetailRequest() {
	}
	// 전달받은 값으로 근태상세정보요청정보 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で勤怠詳細情報リクエスト情報オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	public AttendanceDetailRequest(Date inputDate, Date startDate, Date endDate, Integer departmentId,
			Integer attendanceGroupId, Integer attendanceItemId, Integer leaveItemId, String empNameKr, String note) {
		super();
		this.inputDate = inputDate;
		this.startDate = startDate;
		this.endDate = endDate;
		this.departmentId = departmentId;
		this.attendanceGroupId = attendanceGroupId;
		this.attendanceItemId = attendanceItemId;
		this.leaveItemId = leaveItemId;
		this.empNameKr = empNameKr;
		this.note = note;
	}
	// 근태상세정보요청정보 객체에 저장된 Input일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 勤怠詳細情報リクエスト情報オブジェクトに保存されたInput日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Date getInputDate() {
		return inputDate;
	}
	// 전달받은 Input일자 값을 근태상세정보요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったInput日付の値を勤怠詳細情報リクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setInputDate(Date inputDate) {
		this.inputDate = inputDate;
	}
	// 근태상세정보요청정보 객체에 저장된 Start일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 勤怠詳細情報リクエスト情報オブジェクトに保存されたStart日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Date getStartDate() {
		return startDate;
	}
	// 전달받은 Start일자 값을 근태상세정보요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったStart日付の値を勤怠詳細情報リクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	// 근태상세정보요청정보 객체에 저장된 End일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 勤怠詳細情報リクエスト情報オブジェクトに保存されたEnd日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Date getEndDate() {
		return endDate;
	}
	// 전달받은 End일자 값을 근태상세정보요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったEnd日付の値を勤怠詳細情報リクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	// 근태상세정보요청정보 객체에 저장된 부서식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 勤怠詳細情報リクエスト情報オブジェクトに保存された部署識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Integer getDepartmentId() {
		return departmentId;
	}
	// 전달받은 부서식별번호 값을 근태상세정보요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った部署識別番号の値を勤怠詳細情報リクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}
	
	// 근태상세정보요청정보 객체에 저장된 근태그룹식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 勤怠詳細情報リクエスト情報オブジェクトに保存された勤怠グループ識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Integer getAttendanceGroupId() {
		return attendanceGroupId;
	}
	// 전달받은 근태그룹식별번호 값을 근태상세정보요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った勤怠グループ識別番号の値を勤怠詳細情報リクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setAttendanceGroupId(Integer attendanceGroupId) {
		this.attendanceGroupId = attendanceGroupId;
	}
	// 근태상세정보요청정보 객체에 저장된 근태항목식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 勤怠詳細情報リクエスト情報オブジェクトに保存された勤怠項目識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Integer getAttendanceItemId() {
		return attendanceItemId;
	}
	// 전달받은 근태항목식별번호 값을 근태상세정보요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った勤怠項目識別番号の値を勤怠詳細情報リクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setAttendanceItemId(Integer attendanceItemId) {
		this.attendanceItemId = attendanceItemId;
	}
	// 근태상세정보요청정보 객체에 저장된 휴가항목식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 勤怠詳細情報リクエスト情報オブジェクトに保存された休暇項目識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Integer getLeaveItemId() {
		return leaveItemId;
	}
	// 전달받은 휴가항목식별번호 값을 근태상세정보요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った休暇項目識別番号の値を勤怠詳細情報リクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setLeaveItemId(Integer leaveItemId) {
		this.leaveItemId = leaveItemId;
	}
	// 근태상세정보요청정보 객체에 저장된 Emp명칭Kr 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 勤怠詳細情報リクエスト情報オブジェクトに保存されたEmp名称Krの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getEmpNameKr() {
		return empNameKr;
	}
	// 전달받은 Emp명칭Kr 값을 근태상세정보요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったEmp名称Krの値を勤怠詳細情報リクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmpNameKr(String empNameKr) {
		this.empNameKr = empNameKr;
	}
	// 근태상세정보요청정보 객체에 저장된 비고 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 勤怠詳細情報リクエスト情報オブジェクトに保存された備考の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getNote() {
		return note;
	}
	// 전달받은 비고 값을 근태상세정보요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った備考の値を勤怠詳細情報リクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setNote(String note) {
		this.note = note;
	}
	// 근태상세정보요청정보 입력값과 업무 처리 가능 여부를 검증한다.
	// Controller·Service·DAO·JSP 사이에서 동일한 데이터를 일관된 구조로 전달하기 위해 사용한다.
	// 勤怠詳細情報リクエスト情報の入力値と業務処理の可否を検証する。
	// Controller・Service・DAO・JSP間で同じデータを一貫した構造として受け渡すために使用する。
	public void validate(Map<String, Boolean> errors) {
		if (startDate!=null&&endDate!=null) {
			if (endDate.before(startDate)) {
				errors.put("DateError", Boolean.TRUE);
			}
		}
	}
    
    
	
    
}
