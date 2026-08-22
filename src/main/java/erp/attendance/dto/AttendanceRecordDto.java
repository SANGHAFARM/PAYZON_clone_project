package erp.attendance.dto;

import java.util.Date;

//근태기록 입력 폼에 사용할 dto
// 근태기록 처리에 필요한 값을 계층 간에 전달한다.
// 勤怠記録処理に必要な値を各階層間で受け渡す。
public class AttendanceRecordDto {
	private int employeeAttendanceId;
	private Date inputDate;
	private int attendanceItemId;
	private String attendName;
	private Date startDate;
	private Date endDate;
	private double attendValue;
	private long payAmount;
	private String note;
	private String itemName;


	
	// 전달받은 값으로 근태기록 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で勤怠記録オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	public AttendanceRecordDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	// 전달받은 값으로 근태기록 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で勤怠記録オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	public AttendanceRecordDto(int employeeAttendanceId, Date inputDate, int attendanceItemId, String attendName,
			Date startDate, Date endDate, double attendValue, long payAmount, String note, String itemName) {
		super();
		this.employeeAttendanceId = employeeAttendanceId;
		this.inputDate = inputDate;
		this.attendanceItemId = attendanceItemId;
		this.attendName = attendName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.attendValue = attendValue;
		this.payAmount = payAmount;
		this.note = note;
		this.itemName = itemName;
	}

	// 근태기록 객체에 저장된 사원근태식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 勤怠記録オブジェクトに保存された社員勤怠識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getEmployeeAttendanceId() {
		return employeeAttendanceId;
	}
	// 전달받은 사원근태식별번호 값을 근태기록 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った社員勤怠識別番号の値を勤怠記録オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmployeeAttendanceId(int employeeAttendanceId) {
		this.employeeAttendanceId = employeeAttendanceId;
	}
	// 근태기록 객체에 저장된 Input일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 勤怠記録オブジェクトに保存されたInput日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Date getInputDate() {
		return inputDate;
	}
	// 전달받은 Input일자 값을 근태기록 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったInput日付の値を勤怠記録オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setInputDate(Date inputDate) {
		this.inputDate = inputDate;
	}
	
	// 근태기록 객체에 저장된 근태항목식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 勤怠記録オブジェクトに保存された勤怠項目識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getAttendanceItemId() {
		return attendanceItemId;
	}
	// 전달받은 근태항목식별번호 값을 근태기록 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った勤怠項目識別番号の値を勤怠記録オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setAttendanceItemId(int attendanceItemId) {
		this.attendanceItemId = attendanceItemId;
	}
	// 근태기록 객체에 저장된 Attend명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 勤怠記録オブジェクトに保存されたAttend名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getAttendName() {
		return attendName;
	}
	// 전달받은 Attend명칭 값을 근태기록 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったAttend名称の値を勤怠記録オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setAttendName(String attendName) {
		this.attendName = attendName;
	}
	// 근태기록 객체에 저장된 Start일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 勤怠記録オブジェクトに保存されたStart日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Date getStartDate() {
		return startDate;
	}
	// 전달받은 Start일자 값을 근태기록 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったStart日付の値を勤怠記録オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	// 근태기록 객체에 저장된 End일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 勤怠記録オブジェクトに保存されたEnd日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Date getEndDate() {
		return endDate;
	}
	// 전달받은 End일자 값을 근태기록 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったEnd日付の値を勤怠記録オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEndDate(Date enDDate) {
		this.endDate = enDDate;
	}
	// 근태기록 객체에 저장된 Attend값 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 勤怠記録オブジェクトに保存されたAttend値の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public double getAttendValue() {
		return attendValue;
	}
	// 전달받은 Attend값 값을 근태기록 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったAttend値の値を勤怠記録オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setAttendValue(double attendValue) {
		this.attendValue = attendValue;
	}
	// 근태기록 객체에 저장된 지급금액 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 勤怠記録オブジェクトに保存された支給金額の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getPayAmount() {
		return payAmount;
	}
	// 전달받은 지급금액 값을 근태기록 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った支給金額の値を勤怠記録オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setPayAmount(long payAmount) {
		this.payAmount = payAmount;
	}
	// 근태기록 객체에 저장된 비고 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 勤怠記録オブジェクトに保存された備考の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getNote() {
		return note;
	}
	// 전달받은 비고 값을 근태기록 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った備考の値を勤怠記録オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setNote(String note) {
		this.note = note;
	}

	// 근태기록 객체에 저장된 항목명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 勤怠記録オブジェクトに保存された項目名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getItemName() {
		return itemName;
	}

	// 전달받은 항목명칭 값을 근태기록 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った項目名称の値を勤怠記録オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	
	
}
