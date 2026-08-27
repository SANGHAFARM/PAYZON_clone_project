package erp.attendance.dto;

import java.util.Map;

//일용직 근무 월별 조회 결과를 담을 클래스
// 일용직근무목록 처리에 필요한 값을 계층 간에 전달한다.
// 日雇い勤務一覧処理に必要な値を各階層間で受け渡す。
public class DailyWorkMonthlyDto {
	// 구분, 사원번호, 성명, 부서, 근무기록, 합계, 소득세합계, 지방소득세 합계,실지급합계 
	// 支給額・控除額・期間値を業務基準に従って計算し、合計と最終金額へ反映する。
	private String empType;              // 구분(社員区分)
	private String empNo;                // 사원번호(社員番号)
	private String empNameKr;             // 사원명(氏名)
	private String departmentName;        // 부서(部署)
	private Map<Integer, DailyWorkRecordDto> workDayMap;  // 일별 근무기록 맵(日別勤怠記録)
	private int totalDays;                // 총 근무일수(総勤務日数)
	private long totalIncomeTax;          // 소득세 합계(所得税合計)
	private long totalLocalIncomeTax;     // 지방소득세 합계(地方所得税合計)
	private long totalActualPay;          // 실지급액(差引支給額合計)
	// 전달받은 값으로 일용직근무목록 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で日雇い勤務一覧オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	public DailyWorkMonthlyDto() {
	}
	// 전달받은 값으로 일용직근무목록 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で日雇い勤務一覧オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	public DailyWorkMonthlyDto(String empType, String empNo, String empNameKr, String departmentName,
			Map<Integer, DailyWorkRecordDto> workDayMap, int totalDays, long totalIncomeTax, long totalLocalIncomeTax,
			long totalActualPay) {
		super();
		this.empType = empType;
		this.empNo = empNo;
		this.empNameKr = empNameKr;
		this.departmentName = departmentName;
		this.workDayMap = workDayMap;
		this.totalDays = totalDays;
		this.totalIncomeTax = totalIncomeTax;
		this.totalLocalIncomeTax = totalLocalIncomeTax;
		this.totalActualPay = totalActualPay;
	}
	// 일용직근무목록 객체에 저장된 Emp구분 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い勤務一覧オブジェクトに保存されたEmp区分の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getEmpType() {
		return empType;
	}
	// 전달받은 Emp구분 값을 일용직근무목록 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったEmp区分の値を日雇い勤務一覧オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmpType(String empType) {
		this.empType = empType;
	}
	// 일용직근무목록 객체에 저장된 Emp번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い勤務一覧オブジェクトに保存されたEmp番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getEmpNo() {
		return empNo;
	}
	// 전달받은 Emp번호 값을 일용직근무목록 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったEmp番号の値を日雇い勤務一覧オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}
	// 일용직근무목록 객체에 저장된 Emp명칭Kr 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い勤務一覧オブジェクトに保存されたEmp名称Krの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getEmpNameKr() {
		return empNameKr;
	}
	// 전달받은 Emp명칭Kr 값을 일용직근무목록 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったEmp名称Krの値を日雇い勤務一覧オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmpNameKr(String empNameKr) {
		this.empNameKr = empNameKr;
	}
	// 일용직근무목록 객체에 저장된 부서명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い勤務一覧オブジェクトに保存された部署名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getDepartmentName() {
		return departmentName;
	}
	// 전달받은 부서명칭 값을 일용직근무목록 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った部署名称の値を日雇い勤務一覧オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	// 일용직근무목록 객체에 저장된 근무일용직Map 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い勤務一覧オブジェクトに保存された勤務日雇いMapの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Map<Integer, DailyWorkRecordDto> getWorkDayMap() {
		return workDayMap;
	}
	// 전달받은 근무일용직Map 값을 일용직근무목록 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った勤務日雇いMapの値を日雇い勤務一覧オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setWorkDayMap(Map<Integer, DailyWorkRecordDto> workDayMap) {
		this.workDayMap = workDayMap;
	}
	// 일용직근무목록 객체에 저장된 합계일수 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い勤務一覧オブジェクトに保存された合計日数の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getTotalDays() {
		return totalDays;
	}
	// 전달받은 합계일수 값을 일용직근무목록 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った合計日数の値を日雇い勤務一覧オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setTotalDays(int totalDays) {
		this.totalDays = totalDays;
	}
	// 일용직근무목록 객체에 저장된 합계소득세금 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い勤務一覧オブジェクトに保存された合計所得税金の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getTotalIncomeTax() {
		return totalIncomeTax;
	}
	// 전달받은 합계소득세금 값을 일용직근무목록 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った合計所得税金の値を日雇い勤務一覧オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setTotalIncomeTax(long totalIncomeTax) {
		this.totalIncomeTax = totalIncomeTax;
	}
	// 일용직근무목록 객체에 저장된 합계Local소득세금 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い勤務一覧オブジェクトに保存された合計Local所得税金の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getTotalLocalIncomeTax() {
		return totalLocalIncomeTax;
	}
	// 전달받은 합계Local소득세금 값을 일용직근무목록 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った合計Local所得税金の値を日雇い勤務一覧オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setTotalLocalIncomeTax(long totalLocalIncomeTax) {
		this.totalLocalIncomeTax = totalLocalIncomeTax;
	}
	// 일용직근무목록 객체에 저장된 합계Actual지급 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い勤務一覧オブジェクトに保存された合計Actual支給の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getTotalActualPay() {
		return totalActualPay;
	}
	// 전달받은 합계Actual지급 값을 일용직근무목록 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った合計Actual支給の値を日雇い勤務一覧オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setTotalActualPay(long totalActualPay) {
		this.totalActualPay = totalActualPay;
	}
	
	
}
