package erp.retirement.dto;

// 퇴직처리 화면의 퇴직구분 선택값을 전달한다.
// 퇴직급여구분항목 처리에 필요한 값을 계층 간에 전달한다.
// 退職給与区分項目処理に必要な値を各階層間で受け渡す。
public class RetirementTypeItem {
	private final String code;
	private final String name;
	public RetirementTypeItem(String code, String name) { this.code = code; this.name = name; }
	// 퇴직급여구분항목 객체에 저장된 코드 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与区分項目オブジェクトに保存されたコードの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getCode() { return code; }
	// 퇴직급여구분항목 객체에 저장된 명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与区分項目オブジェクトに保存された名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getName() { return name; }
}
