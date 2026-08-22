package erp.payroll.dto;

// 급여대장 상세의 지급·공제 항목 열 정보를 전달한다.
// 급여등록표시항목 정보를 보관하고 관련 기능에서 사용할 수 있도록 제공한다.
// 給与登録表示項目情報を保持し、関連機能から利用できるように提供する。
public class PayrollRegisterColumn {

	private int itemId;
	private String itemName;

	// 전달받은 값으로 급여등록표시항목 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で給与登録表示項目オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	public PayrollRegisterColumn(int itemId, String itemName) {
		this.itemId = itemId;
		this.itemName = itemName;
	}

	// 급여등록표시항목 객체에 저장된 항목식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 給与登録表示項目オブジェクトに保存された項目識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getItemId() {
		return itemId;
	}

	// 급여등록표시항목 객체에 저장된 항목명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 給与登録表示項目オブジェクトに保存された項目名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getItemName() {
		return itemName;
	}
}
