package erp.employees.service;

// 사원 목록과 선택창에서 공통으로 사용하는 검색조건을 전달한다.
// 사원검색검색조건 처리에 필요한 값을 계층 간에 전달한다.
// 社員検索検索条件処理に必要な値を各階層間で受け渡す。
public class EmployeeSearchCondition {
	private String searchTarget;
	private String keyword;
	private String employmentType;
	private String status;
	private int page;
	private int pageSize;
	private Integer departmentId;
	private Integer positionId;

	// 사원검색검색조건 객체에 저장된 검색Target 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員検索検索条件オブジェクトに保存された検索Targetの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getSearchTarget() { return searchTarget; }
	public void setSearchTarget(String searchTarget) { this.searchTarget = searchTarget; }
	// 사원검색검색조건 객체에 저장된 검색어 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員検索検索条件オブジェクトに保存された検索語の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getKeyword() { return keyword; }
	public void setKeyword(String keyword) { this.keyword = keyword; }
	// 사원검색검색조건 객체에 저장된 고용구분 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員検索検索条件オブジェクトに保存された雇用区分の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getEmploymentType() { return employmentType; }
	public void setEmploymentType(String employmentType) { this.employmentType = employmentType; }
	// 사원검색검색조건 객체에 저장된 상태 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員検索検索条件オブジェクトに保存された状態の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }
	// 요청 조건에 맞는 사원검색검색조건 화면 데이터를 구성하여 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// リクエスト条件に合う社員検索検索条件の画面データを構成して返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getPage() { return page; }
	public void setPage(int page) { this.page = page; }
	// 사원검색검색조건 객체에 저장된 화면 데이터Size 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員検索検索条件オブジェクトに保存された画面データSizeの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getPageSize() { return pageSize; }
	public void setPageSize(int pageSize) { this.pageSize = pageSize; }
	// 사원검색검색조건 객체에 저장된 부서식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員検索検索条件オブジェクトに保存された部署識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Integer getDepartmentId() { return departmentId; }
	public void setDepartmentId(Integer departmentId) { this.departmentId = departmentId; }
	// 사원검색검색조건 객체에 저장된 직위식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員検索検索条件オブジェクトに保存された役職識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Integer getPositionId() { return positionId; }
	public void setPositionId(Integer positionId) { this.positionId = positionId; }
}
