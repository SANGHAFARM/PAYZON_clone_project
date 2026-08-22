package erp.settings.model;

/**
 * [기본환경설정] 비과세/감면 소득 목록 Model DB 테이블: TAX_FREE_ITEM
 */
// 세금비과세항목 처리에 필요한 값을 계층 간에 전달한다.
// 税金非課税項目処理に必要な値を各階層間で受け渡す。
public class TaxFreeItem {

	private String taxFreeCode; // 비과세 코드 (PK)
	private String legalClause;
	private String reportField;
	private String taxFreeName;

	private Long defaultLimit; // 한도금액

	private String payStatementYn;
	private String incomeCategory;

	// 전달받은 값으로 세금비과세항목 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で税金非課税項目オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	public TaxFreeItem() {
	}

	// Getter & Setter
	// 세금비과세항목 객체에 저장된 세금비과세코드 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 税金非課税項目オブジェクトに保存された税金非課税コードの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getTaxFreeCode() {
		return taxFreeCode;
	}

	// 전달받은 세금비과세코드 값을 세금비과세항목 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った税金非課税コードの値を税金非課税項目オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setTaxFreeCode(String taxFreeCode) {
		this.taxFreeCode = taxFreeCode;
	}

	// 세금비과세항목 객체에 저장된 법률조문 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 税金非課税項目オブジェクトに保存された法令条文の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getLegalClause() {
		return legalClause;
	}

	// 전달받은 법률조문 값을 세금비과세항목 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った法令条文の値を税金非課税項目オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setLegalClause(String legalClause) {
		this.legalClause = legalClause;
	}

	// 세금비과세항목 객체에 저장된 신고항목 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 税金非課税項目オブジェクトに保存された届出項目の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getReportField() {
		return reportField;
	}

	// 전달받은 신고항목 값을 세금비과세항목 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った届出項目の値を税金非課税項目オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setReportField(String reportField) {
		this.reportField = reportField;
	}

	// 세금비과세항목 객체에 저장된 세금비과세명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 税金非課税項目オブジェクトに保存された税金非課税名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getTaxFreeName() {
		return taxFreeName;
	}

	// 전달받은 세금비과세명칭 값을 세금비과세항목 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った税金非課税名称の値を税金非課税項目オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setTaxFreeName(String taxFreeName) {
		this.taxFreeName = taxFreeName;
	}

	// 세금비과세항목 객체에 저장된 기본Limit 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 税金非課税項目オブジェクトに保存された初期Limitの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Long getDefaultLimit() {
		return defaultLimit;
	}

	// 전달받은 기본Limit 값을 세금비과세항목 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った初期Limitの値を税金非課税項目オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setDefaultLimit(Long defaultLimit) {
		this.defaultLimit = defaultLimit;
	}

	// 세금비과세항목 객체에 저장된 지급Statement여부 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 税金非課税項目オブジェクトに保存された支給Statement可否の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getPayStatementYn() {
		return payStatementYn;
	}

	// 전달받은 지급Statement여부 값을 세금비과세항목 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った支給Statement可否の値を税金非課税項目オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setPayStatementYn(String payStatementYn) {
		this.payStatementYn = payStatementYn;
	}

	// 세금비과세항목 객체에 저장된 소득Category 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 税金非課税項目オブジェクトに保存された所得Categoryの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getIncomeCategory() {
		return incomeCategory;
	}

	// 전달받은 소득Category 값을 세금비과세항목 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った所得Categoryの値を税金非課税項目オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setIncomeCategory(String incomeCategory) {
		this.incomeCategory = incomeCategory;
	}
}
