package erp.attendance.dto;

import java.util.Date;
//일용직 근무 상세조회에서 데이터를 담을 클래스
// 일용직근무상세정보 처리에 필요한 값을 계층 간에 전달한다.
// 日雇い勤務詳細情報処理に必要な値を各階層間で受け渡す。
public class DailyWorkDetailDto {
	private Date workDate;//근무일자
	private String empNo;//사원번호
	private String empNameKr;//성명(EMPLOYEE)
	private String departmentName;//부서(DEPARTMNET)
	private String projectName;//현장/프로젝트(PROJECT)
	private long dailyPay;//일당
	private double payRate;//지급율
	private long incomeTax;//소득세
	private long localIncomeTax;//지방소득세
	private long actualPay;//실지급액
	// 전달받은 값으로 일용직근무상세정보 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で日雇い勤務詳細情報オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	public DailyWorkDetailDto() {
	}
	// 전달받은 값으로 일용직근무상세정보 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で日雇い勤務詳細情報オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	public DailyWorkDetailDto(Date workDate, String empNo, String empNameKr, String departmentName, String projectName,
			long dailyPay, double payRate, long incomeTax, long localIncomeTax, long actualPay) {
		super();
		this.workDate = workDate;
		this.empNo = empNo;
		this.empNameKr = empNameKr;
		this.departmentName = departmentName;
		this.projectName = projectName;
		this.dailyPay = dailyPay;
		this.payRate = payRate;
		this.incomeTax = incomeTax;
		this.localIncomeTax = localIncomeTax;
		this.actualPay = actualPay;
	}
	// 일용직근무상세정보 객체에 저장된 근무일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い勤務詳細情報オブジェクトに保存された勤務日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Date getWorkDate() {
		return workDate;
	}
	// 전달받은 근무일자 값을 일용직근무상세정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った勤務日付の値を日雇い勤務詳細情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}
	// 일용직근무상세정보 객체에 저장된 Emp번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い勤務詳細情報オブジェクトに保存されたEmp番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getEmpNo() {
		return empNo;
	}
	// 전달받은 Emp번호 값을 일용직근무상세정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったEmp番号の値を日雇い勤務詳細情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}
	// 일용직근무상세정보 객체에 저장된 Emp명칭Kr 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い勤務詳細情報オブジェクトに保存されたEmp名称Krの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getEmpNameKr() {
		return empNameKr;
	}
	// 전달받은 Emp명칭Kr 값을 일용직근무상세정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったEmp名称Krの値を日雇い勤務詳細情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmpNameKr(String empNameKr) {
		this.empNameKr = empNameKr;
	}
	// 일용직근무상세정보 객체에 저장된 부서명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い勤務詳細情報オブジェクトに保存された部署名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getDepartmentName() {
		return departmentName;
	}
	// 전달받은 부서명칭 값을 일용직근무상세정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った部署名称の値を日雇い勤務詳細情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	// 일용직근무상세정보 객체에 저장된 현장·프로젝트명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い勤務詳細情報オブジェクトに保存された現場・プロジェクト名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getProjectName() {
		return projectName;
	}
	// 전달받은 현장·프로젝트명칭 값을 일용직근무상세정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った現場・プロジェクト名称の値を日雇い勤務詳細情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	// 일용직근무상세정보 객체에 저장된 일용직지급 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い勤務詳細情報オブジェクトに保存された日雇い支給の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getDailyPay() {
		return dailyPay;
	}
	// 전달받은 일용직지급 값을 일용직근무상세정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った日雇い支給の値を日雇い勤務詳細情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setDailyPay(long dailyPay) {
		this.dailyPay = dailyPay;
	}
	// 일용직근무상세정보 객체에 저장된 지급비율 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い勤務詳細情報オブジェクトに保存された支給率の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public double getPayRate() {
		return payRate;
	}
	// 전달받은 지급비율 값을 일용직근무상세정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った支給率の値を日雇い勤務詳細情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setPayRate(double payRate) {
		this.payRate = payRate;
	}
	// 일용직근무상세정보 객체에 저장된 소득세금 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い勤務詳細情報オブジェクトに保存された所得税金の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getIncomeTax() {
		return incomeTax;
	}
	// 전달받은 소득세금 값을 일용직근무상세정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った所得税金の値を日雇い勤務詳細情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setIncomeTax(long incomeTax) {
		this.incomeTax = incomeTax;
	}
	// 일용직근무상세정보 객체에 저장된 Local소득세금 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い勤務詳細情報オブジェクトに保存されたLocal所得税金の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getLocalIncomeTax() {
		return localIncomeTax;
	}
	// 전달받은 Local소득세금 값을 일용직근무상세정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったLocal所得税金の値を日雇い勤務詳細情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setLocalIncomeTax(long localIncomeTax) {
		this.localIncomeTax = localIncomeTax;
	}
	// 일용직근무상세정보 객체에 저장된 Actual지급 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い勤務詳細情報オブジェクトに保存されたActual支給の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getActualPay() {
		return actualPay;
	}
	// 전달받은 Actual지급 값을 일용직근무상세정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったActual支給の値を日雇い勤務詳細情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setActualPay(long actualPay) {
		this.actualPay = actualPay;
	}
	
	
}
