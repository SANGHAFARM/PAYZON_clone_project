package erp.attendance.service.request;

import java.util.Date;

// 일용직근무상세정보요청정보 처리에 필요한 값을 계층 간에 전달한다.
// 日雇い勤務詳細情報リクエスト情報処理に必要な値を各階層間で受け渡す。
public class DailyWorkDetailSearchRequest {
	private Date startDate;
	private Date endDate;
	private String empNameKr;
	private Integer departmentId;
	private Integer projectId;
	// 전달받은 값으로 일용직근무상세정보요청정보 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で日雇い勤務詳細情報リクエスト情報オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	public DailyWorkDetailSearchRequest() {
	}
	// 전달받은 값으로 일용직근무상세정보요청정보 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で日雇い勤務詳細情報リクエスト情報オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	public DailyWorkDetailSearchRequest(Date startDate, Date endDate, String empNameKr, Integer departmentId,
			Integer projectId) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
		this.empNameKr = empNameKr;
		this.departmentId = departmentId;
		this.projectId = projectId;
	}
	// 일용직근무상세정보요청정보 객체에 저장된 Start일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い勤務詳細情報リクエスト情報オブジェクトに保存されたStart日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Date getStartDate() {
		return startDate;
	}
	// 전달받은 Start일자 값을 일용직근무상세정보요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったStart日付の値を日雇い勤務詳細情報リクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	// 일용직근무상세정보요청정보 객체에 저장된 End일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い勤務詳細情報リクエスト情報オブジェクトに保存されたEnd日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Date getEndDate() {
		return endDate;
	}
	// 전달받은 End일자 값을 일용직근무상세정보요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったEnd日付の値を日雇い勤務詳細情報リクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	// 일용직근무상세정보요청정보 객체에 저장된 Emp명칭Kr 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い勤務詳細情報リクエスト情報オブジェクトに保存されたEmp名称Krの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getEmpNameKr() {
		return empNameKr;
	}
	// 전달받은 Emp명칭Kr 값을 일용직근무상세정보요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったEmp名称Krの値を日雇い勤務詳細情報リクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmpNameKr(String empNameKr) {
		this.empNameKr = empNameKr;
	}
	// 일용직근무상세정보요청정보 객체에 저장된 부서식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い勤務詳細情報リクエスト情報オブジェクトに保存された部署識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Integer getDepartmentId() {
		return departmentId;
	}
	// 전달받은 부서식별번호 값을 일용직근무상세정보요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った部署識別番号の値を日雇い勤務詳細情報リクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}
	// 일용직근무상세정보요청정보 객체에 저장된 현장·프로젝트식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 日雇い勤務詳細情報リクエスト情報オブジェクトに保存された現場・プロジェクト識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Integer getProjectId() {
		return projectId;
	}
	// 전달받은 현장·프로젝트식별번호 값을 일용직근무상세정보요청정보 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った現場・プロジェクト識別番号の値を日雇い勤務詳細情報リクエスト情報オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}
	
}
