package erp.settings.model;

/**
 * [기본환경설정] 급여 지급항목 설정 Model DB 테이블: PAY_ITEM
 */
// 지급항목 처리에 필요한 값을 계층 간에 전달한다.
// 支給項目処理に必要な値を各階層間で受け渡す。
public class PayItem {

	private int payItemId; // 지급항목 식별 번호 (PK)
	private String payName;
	private String taxType;
	private String calcMethod;
	private String payMethod;
	private String useYn;

	private String taxFreeCode; // 비과세 코드 외래키
	private String taxFreeName; // 비과세명을 임시로 담을 변수
	private Long taxFreeLimit; // 비과세 한도
	private Integer roundUnit; // 절사단위
	private Integer linkAttendId; // 근태항목 외래키
	private Long bulkPayAmount; // 일괄지급액
	private String directTaxFreeName; // 사용자 직접입력 비과세명
	private Long directTaxFreeLimit; // 사용자 직접입력 한도액

	// 전달받은 값으로 지급항목 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で支給項目オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	public PayItem() {
	}

	// Getter & Setter
	// 지급항목 객체에 저장된 지급항목식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 支給項目オブジェクトに保存された支給項目識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getPayItemId() {
		return payItemId;
	}

	// 전달받은 지급항목식별번호 값을 지급항목 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った支給項目識別番号の値を支給項目オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setPayItemId(int payItemId) {
		this.payItemId = payItemId;
	}

	// 지급항목 객체에 저장된 지급명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 支給項目オブジェクトに保存された支給名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getPayName() {
		return payName;
	}

	// 전달받은 지급명칭 값을 지급항목 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った支給名称の値を支給項目オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setPayName(String payName) {
		this.payName = payName;
	}

	// 지급항목 객체에 저장된 세금구분 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 支給項目オブジェクトに保存された税金区分の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getTaxType() {
		return taxType;
	}

	// 전달받은 세금구분 값을 지급항목 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った税金区分の値を支給項目オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}

	// 지급항목 객체에 저장된 세금비과세코드 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 支給項目オブジェクトに保存された税金非課税コードの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getTaxFreeCode() {
		return taxFreeCode;
	}

	// 전달받은 세금비과세코드 값을 지급항목 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った税金非課税コードの値を支給項目オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setTaxFreeCode(String taxFreeCode) {
		this.taxFreeCode = taxFreeCode;
	}

	// 지급항목 객체에 저장된 세금비과세명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 支給項目オブジェクトに保存された税金非課税名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getTaxFreeName() {
		return taxFreeName;
	}

	// 전달받은 세금비과세명칭 값을 지급항목 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った税金非課税名称の値を支給項目オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setTaxFreeName(String taxFreeName) {
		this.taxFreeName = taxFreeName;
	}

	// 지급항목 객체에 저장된 세금비과세Limit 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 支給項目オブジェクトに保存された税金非課税Limitの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Long getTaxFreeLimit() {
		return taxFreeLimit;
	}

	// 전달받은 세금비과세Limit 값을 지급항목 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った税金非課税Limitの値を支給項目オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setTaxFreeLimit(Long taxFreeLimit) {
		this.taxFreeLimit = taxFreeLimit;
	}

	// 지급항목 객체에 저장된 계산방법 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 支給項目オブジェクトに保存された計算方法の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getCalcMethod() {
		return calcMethod;
	}

	// 전달받은 계산방법 값을 지급항목 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った計算方法の値を支給項目オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setCalcMethod(String calcMethod) {
		this.calcMethod = calcMethod;
	}

	// 지급항목 객체에 저장된 절사단위 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 支給項目オブジェクトに保存された端数処理単位の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Integer getRoundUnit() {
		return roundUnit;
	}

	// 전달받은 절사단위 값을 지급항목 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った端数処理単位の値を支給項目オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setRoundUnit(Integer roundUnit) {
		this.roundUnit = roundUnit;
	}

	// 지급항목 객체에 저장된 지급방법 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 支給項目オブジェクトに保存された支給方法の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getPayMethod() {
		return payMethod;
	}

	// 전달받은 지급방법 값을 지급항목 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った支給方法の値を支給項目オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	// 지급항목 객체에 저장된 LinkAttend식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 支給項目オブジェクトに保存されたLinkAttend識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Integer getLinkAttendId() {
		return linkAttendId;
	}

	// 전달받은 LinkAttend식별번호 값을 지급항목 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったLinkAttend識別番号の値を支給項目オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setLinkAttendId(Integer linkAttendId) {
		this.linkAttendId = linkAttendId;
	}

	// 지급항목 객체에 저장된 Bulk지급금액 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 支給項目オブジェクトに保存されたBulk支給金額の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Long getBulkPayAmount() {
		return bulkPayAmount;
	}

	// 전달받은 Bulk지급금액 값을 지급항목 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったBulk支給金額の値を支給項目オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setBulkPayAmount(Long bulkPayAmount) {
		this.bulkPayAmount = bulkPayAmount;
	}

	// 지급항목 객체에 저장된 사용여부 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 支給項目オブジェクトに保存された使用可否の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getUseYn() {
		return useYn;
	}

	// 전달받은 사용여부 값을 지급항목 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った使用可否の値を支給項目オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	// 지급항목 객체에 저장된 Direct세금비과세명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 支給項目オブジェクトに保存されたDirect税金非課税名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getDirectTaxFreeName() {
		return directTaxFreeName;
	}

	// 전달받은 Direct세금비과세명칭 값을 지급항목 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったDirect税金非課税名称の値を支給項目オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setDirectTaxFreeName(String directTaxFreeName) {
		this.directTaxFreeName = directTaxFreeName;
	}

	// 지급항목 객체에 저장된 Direct세금비과세Limit 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 支給項目オブジェクトに保存されたDirect税金非課税Limitの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Long getDirectTaxFreeLimit() {
		return directTaxFreeLimit;
	}

	// 전달받은 Direct세금비과세Limit 값을 지급항목 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったDirect税金非課税Limitの値を支給項目オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setDirectTaxFreeLimit(Long directTaxFreeLimit) {
		this.directTaxFreeLimit = directTaxFreeLimit;
	}
}
