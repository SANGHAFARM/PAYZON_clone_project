package erp.attendance.service.request;

import java.util.Date;
import java.util.List;
import java.util.Map;

// Insert사원근태요청정보 처리에 필요한 값을 계층 간에 전달한다.
// Insert社員勤怠リクエスト情報処理に必要な値を各階層間で受け渡す。
public class AttendanceRecordInsertRequest {
	private List<Integer> employeeIds;
	private Date inputDate;
	private Integer attendanceItemId;
	private Date startDate;
	private Date endDate;
	private double attendValue;
	private long payAmount;
	private String note;
	// 전달받은 값으로 Insert사원근태요청정보 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値でInsert社員勤怠リクエスト情報オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	public AttendanceRecordInsertRequest() {
	}
	// 전달받은 값으로 Insert사원근태요청정보 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値でInsert社員勤怠リクエスト情報オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。

	// Insert사원근태요청정보 객체에 저장된 사원Ids 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// Insert社員勤怠リクエスト情報オブジェクトに保存された社員Idsの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public List<Integer> getEmployeeIds() {
		return employeeIds;
	}
	public AttendanceRecordInsertRequest(List<Integer> employeeIds, Date inputDate, Integer attendanceItemId,
			Date startDate, Date endDate, double attendValue, long payAmount, String note) {
		super();
		this.employeeIds = employeeIds;
		this.inputDate = inputDate;
		this.attendanceItemId = attendanceItemId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.attendValue = attendValue;
		this.payAmount = payAmount;
		this.note = note;
	}

	// 전달받은 사원Ids 값을 Insert사원근태요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った社員Idsの値をInsert社員勤怠リクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmployeeIds(List<Integer> employeeIds) {
		this.employeeIds = employeeIds;
	}
	// Insert사원근태요청정보 객체에 저장된 Input일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// Insert社員勤怠リクエスト情報オブジェクトに保存されたInput日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Date getInputDate() {
		return inputDate;
	}
	// 전달받은 Input일자 값을 Insert사원근태요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったInput日付の値をInsert社員勤怠リクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setInputDate(Date inputDate) {
		this.inputDate = inputDate;
	}
	// Insert사원근태요청정보 객체에 저장된 근태항목식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// Insert社員勤怠リクエスト情報オブジェクトに保存された勤怠項目識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	
	public Integer getAttendanceItemId() {
		return attendanceItemId;
	}
	// 전달받은 근태항목식별번호 값을 Insert사원근태요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った勤怠項目識別番号の値をInsert社員勤怠リクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setAttendanceItemId(Integer attendanceItemId) {
		this.attendanceItemId = attendanceItemId;
	}
	// Insert사원근태요청정보 객체에 저장된 Start일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// Insert社員勤怠リクエスト情報オブジェクトに保存されたStart日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Date getStartDate() {
		return startDate;
	}
	// 전달받은 Start일자 값을 Insert사원근태요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったStart日付の値をInsert社員勤怠リクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	// Insert사원근태요청정보 객체에 저장된 End일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// Insert社員勤怠リクエスト情報オブジェクトに保存されたEnd日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Date getEndDate() {
		return endDate;
	}
	// 전달받은 End일자 값을 Insert사원근태요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったEnd日付の値をInsert社員勤怠リクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	// Insert사원근태요청정보 객체에 저장된 Attend값 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// Insert社員勤怠リクエスト情報オブジェクトに保存されたAttend値の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public double getAttendValue() {
		return attendValue;
	}
	// 전달받은 Attend값 값을 Insert사원근태요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったAttend値の値をInsert社員勤怠リクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setAttendValue(double attendValue) {
		this.attendValue = attendValue;
	}
	// Insert사원근태요청정보 객체에 저장된 지급금액 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// Insert社員勤怠リクエスト情報オブジェクトに保存された支給金額の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getPayAmount() {
		return payAmount;
	}
	// 전달받은 지급금액 값을 Insert사원근태요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った支給金額の値をInsert社員勤怠リクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setPayAmount(long payAmount) {
		this.payAmount = payAmount;
	}
	// Insert사원근태요청정보 객체에 저장된 비고 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// Insert社員勤怠リクエスト情報オブジェクトに保存された備考の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getNote() {
		return note;
	}
	// 전달받은 비고 값을 Insert사원근태요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った備考の値をInsert社員勤怠リクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setNote(String note) {
		this.note = note;
	}
	// Insert사원근태요청정보 입력값과 업무 처리 가능 여부를 검증한다.
	// Controller·Service·DAO·JSP 사이에서 동일한 데이터를 일관된 구조로 전달하기 위해 사용한다.
	// Insert社員勤怠リクエスト情報の入力値と業務処理の可否を検証する。
	// Controller・Service・DAO・JSP間で同じデータを一貫した構造として受け渡すために使用する。
	public void validate(Map<String, Boolean> errors) {
		if (employeeIds==null||employeeIds.isEmpty()||employeeIds.size()==0) {
			errors.put("employeeIds", Boolean.TRUE);
		}
		if (attendanceItemId==null) {
			errors.put("attendanceItemId", Boolean.TRUE);
		}
		if (attendValue<=0) {
			errors.put("attendValue", Boolean.TRUE);
		}
	}
	
	
	
	
}
