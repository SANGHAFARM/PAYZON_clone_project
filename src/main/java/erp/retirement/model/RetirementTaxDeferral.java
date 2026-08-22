package erp.retirement.model;

import java.util.Date;

/**
 * [퇴직관리] 퇴직연금계좌 입금에 따른 퇴직소득세 과세이연 내역 Model DB 테이블: RETIREMENT_TAX_DEFERRAL
 */
// 퇴직급여세금과세이연 정보를 보관하고 관련 기능에서 사용할 수 있도록 제공한다.
// 退職給与税金課税繰延情報を保持し、関連機能から利用できるように提供する。
public class RetirementTaxDeferral {

	// [DB 관리 항목]
	// データベースの主キー・外部キー・基本属性を保持し、永続化対象を識別する。
	private int retirementTaxDeferralId; // 과세이연 내역 식별 번호 (PK)
	private int retirementCalculationId; // 퇴직급여 계산내역 식별 번호 (FK)

	// [퇴직연금계좌 입금정보]
	// 保険・税区分と適用基準を確認し、対象金額に合う控除または非課税処理を適用する。
	private String bizName; // 퇴직연금사업자명
	private String bizRegNo; // 사업자등록번호
	private String accountNo; // 계좌번호
	private Date depositDate; // 입금(이체)일
	private long depositAmt; // 계좌입금금액

	// 전달받은 값으로 퇴직급여세금과세이연 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で退職給与税金課税繰延オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	public RetirementTaxDeferral() {
	}

	// Getter & Setter
	// 퇴직급여세금과세이연 객체에 저장된 퇴직급여세금과세이연식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与税金課税繰延オブジェクトに保存された退職給与税金課税繰延識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getRetirementTaxDeferralId() {
		return retirementTaxDeferralId;
	}

	// 전달받은 퇴직급여세금과세이연식별번호 값을 퇴직급여세금과세이연 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った退職給与税金課税繰延識別番号の値を退職給与税金課税繰延オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setRetirementTaxDeferralId(int retirementTaxDeferralId) {
		this.retirementTaxDeferralId = retirementTaxDeferralId;
	}

	// 퇴직급여세금과세이연 객체에 저장된 퇴직급여계산식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与税金課税繰延オブジェクトに保存された退職給与計算識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getRetirementCalculationId() {
		return retirementCalculationId;
	}

	// 전달받은 퇴직급여계산식별번호 값을 퇴직급여세금과세이연 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った退職給与計算識別番号の値を退職給与税金課税繰延オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setRetirementCalculationId(int retirementCalculationId) {
		this.retirementCalculationId = retirementCalculationId;
	}

	// 퇴직급여세금과세이연 객체에 저장된 Biz명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与税金課税繰延オブジェクトに保存されたBiz名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getBizName() {
		return bizName;
	}

	// 전달받은 Biz명칭 값을 퇴직급여세금과세이연 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったBiz名称の値を退職給与税金課税繰延オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setBizName(String bizName) {
		this.bizName = bizName;
	}

	// 퇴직급여세금과세이연 객체에 저장된 BizReg번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与税金課税繰延オブジェクトに保存されたBizReg番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getBizRegNo() {
		return bizRegNo;
	}

	// 전달받은 BizReg번호 값을 퇴직급여세금과세이연 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったBizReg番号の値を退職給与税金課税繰延オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setBizRegNo(String bizRegNo) {
		this.bizRegNo = bizRegNo;
	}

	// 퇴직급여세금과세이연 객체에 저장된 계좌번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与税金課税繰延オブジェクトに保存された口座番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getAccountNo() {
		return accountNo;
	}

	// 전달받은 계좌번호 값을 퇴직급여세금과세이연 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った口座番号の値を退職給与税金課税繰延オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	// 퇴직급여세금과세이연 객체에 저장된 입금일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与税金課税繰延オブジェクトに保存された入金日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Date getDepositDate() {
		return depositDate;
	}

	// 전달받은 입금일자 값을 퇴직급여세금과세이연 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った入金日付の値を退職給与税金課税繰延オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setDepositDate(Date depositDate) {
		this.depositDate = depositDate;
	}

	// 퇴직급여세금과세이연 객체에 저장된 입금금액 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与税金課税繰延オブジェクトに保存された入金金額の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getDepositAmt() {
		return depositAmt;
	}

	// 전달받은 입금금액 값을 퇴직급여세금과세이연 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った入金金額の値を退職給与税金課税繰延オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setDepositAmt(long depositAmt) {
		this.depositAmt = depositAmt;
	}
}
