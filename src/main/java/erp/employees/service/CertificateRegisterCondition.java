package erp.employees.service;

// 발급대장의 검색 및 페이지 조건을 전달한다.
// 제증명서등록검색조건 처리에 필요한 값을 계층 간에 전달한다.
// 証明書登録検索条件処理に必要な値を各階層間で受け渡す。
public class CertificateRegisterCondition {
	private String certificateType;
	private String issueDateFrom;
	private String issueDateTo;
	private String keyword;
	private int page;
	private int pageSize;

	// 제증명서등록검색조건 객체에 저장된 제증명서구분 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 証明書登録検索条件オブジェクトに保存された証明書区分の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getCertificateType() { return certificateType; }
	public void setCertificateType(String certificateType) { this.certificateType = certificateType; }
	// 제증명서등록검색조건 객체에 저장된 발급일자From 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 証明書登録検索条件オブジェクトに保存された発行日付Fromの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getIssueDateFrom() { return issueDateFrom; }
	public void setIssueDateFrom(String issueDateFrom) { this.issueDateFrom = issueDateFrom; }
	// 제증명서등록검색조건 객체에 저장된 발급일자To 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 証明書登録検索条件オブジェクトに保存された発行日付Toの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getIssueDateTo() { return issueDateTo; }
	public void setIssueDateTo(String issueDateTo) { this.issueDateTo = issueDateTo; }
	// 제증명서등록검색조건 객체에 저장된 검색어 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 証明書登録検索条件オブジェクトに保存された検索語の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getKeyword() { return keyword; }
	public void setKeyword(String keyword) { this.keyword = keyword; }
	// 요청 조건에 맞는 제증명서등록검색조건 화면 데이터를 구성하여 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// リクエスト条件に合う証明書登録検索条件の画面データを構成して返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getPage() { return page; }
	public void setPage(int page) { this.page = page; }
	// 제증명서등록검색조건 객체에 저장된 화면 데이터Size 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 証明書登録検索条件オブジェクトに保存された画面データSizeの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getPageSize() { return pageSize; }
	public void setPageSize(int pageSize) { this.pageSize = pageSize; }
}
