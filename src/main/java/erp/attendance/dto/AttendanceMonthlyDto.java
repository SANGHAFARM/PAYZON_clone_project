package erp.attendance.dto;

import java.util.List;
import java.util.Map;

//근태조회 월별 조회에서 사용할 dto
// 월간근태 처리에 필요한 값을 계층 간에 전달한다.
// 月間勤怠処理に必要な値を各階層間で受け渡す。
public class AttendanceMonthlyDto {
	private String empType;
	private String empNo;
	private String empNameKr;
	private String departmentName;
	private String jobPositionName;
	private Map<Integer, String> dailyAttendance;
	private List<AttendanceSummaryItemDto> totalAttendValue;
	private double totalLeaveDeduction;

	// 전달받은 값으로 월간근태 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で月間勤怠オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	public AttendanceMonthlyDto() {
	}

	// 전달받은 값으로 월간근태 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で月間勤怠オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	public AttendanceMonthlyDto(String empType, String empNo, String empNameKr, String departmentName,
			String jobPositionName, Map<Integer, String> dailyAttendance, List<AttendanceSummaryItemDto> totalAttendValue,
			double totalLeaveDeduction) {
		super();
		this.empType = empType;
		this.empNo = empNo;
		this.empNameKr = empNameKr;
		this.departmentName = departmentName;
		this.jobPositionName = jobPositionName;
		this.dailyAttendance = dailyAttendance;
		this.totalAttendValue = totalAttendValue;
		this.totalLeaveDeduction = totalLeaveDeduction;
	}

	// 월간근태 객체에 저장된 Emp구분 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 月間勤怠オブジェクトに保存されたEmp区分の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getEmpType() {
		return empType;
	}

	// 전달받은 Emp구분 값을 월간근태 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったEmp区分の値を月間勤怠オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmpType(String empType) {
		this.empType = empType;
	}

	// 월간근태 객체에 저장된 Emp번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 月間勤怠オブジェクトに保存されたEmp番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getEmpNo() {
		return empNo;
	}

	// 전달받은 Emp번호 값을 월간근태 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったEmp番号の値を月間勤怠オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}

	// 월간근태 객체에 저장된 Emp명칭Kr 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 月間勤怠オブジェクトに保存されたEmp名称Krの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getEmpNameKr() {
		return empNameKr;
	}

	// 전달받은 Emp명칭Kr 값을 월간근태 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったEmp名称Krの値を月間勤怠オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmpNameKr(String empNameKr) {
		this.empNameKr = empNameKr;
	}

	// 월간근태 객체에 저장된 부서명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 月間勤怠オブジェクトに保存された部署名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getDepartmentName() {
		return departmentName;
	}

	// 전달받은 부서명칭 값을 월간근태 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った部署名称の値を月間勤怠オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	// 월간근태 객체에 저장된 직무직위명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 月間勤怠オブジェクトに保存された職務役職名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getJobPositionName() {
		return jobPositionName;
	}

	// 전달받은 직무직위명칭 값을 월간근태 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った職務役職名称の値を月間勤怠オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setJobPositionName(String jobPositionName) {
		this.jobPositionName = jobPositionName;
	}

	// 월간근태 객체에 저장된 일용직근태 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 月間勤怠オブジェクトに保存された日雇い勤怠の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Map<Integer, String> getDailyAttendance() {
		return dailyAttendance;
	}

	// 전달받은 일용직근태 값을 월간근태 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った日雇い勤怠の値を月間勤怠オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setDailyAttendance(Map<Integer, String> dailyAttendance) {
		this.dailyAttendance = dailyAttendance;
	}

	
	
	// 월간근태 객체에 저장된 합계Attend값 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 月間勤怠オブジェクトに保存された合計Attend値の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public List<AttendanceSummaryItemDto> getTotalAttendValue(){
		return totalAttendValue;
	}

	// 전달받은 합계Attend값 값을 월간근태 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った合計Attend値の値を月間勤怠オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setTotalAttendValue(List<AttendanceSummaryItemDto> totalAttendValue) {
		this.totalAttendValue = totalAttendValue;
	}

	// 월간근태 객체에 저장된 합계휴가공제 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 月間勤怠オブジェクトに保存された合計休暇控除の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public double getTotalLeaveDeduction() {
		return totalLeaveDeduction;
	}

	// 전달받은 합계휴가공제 값을 월간근태 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った合計休暇控除の値を月間勤怠オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setTotalLeaveDeduction(double totalLeaveDeduction) {
		this.totalLeaveDeduction = totalLeaveDeduction;
	}

}
