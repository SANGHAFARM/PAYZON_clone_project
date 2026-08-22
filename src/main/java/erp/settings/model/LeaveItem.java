package erp.settings.model;

import java.util.Date;

/**
 * [기본환경설정] 휴가/근태 설정 - 휴가항목 설정 Model DB 테이블: LEAVE_ITEM
 */
// 휴가항목 처리에 필요한 값을 계층 간에 전달한다.
// 休暇項目処理に必要な値を各階層間で受け渡す。
public class LeaveItem {

	private int leaveItemId; // 휴가항목 식별 번호 (PK)
	private String itemName;
	private Date applyStartDate;
	private Date applyEndDate;
	private String useYn;

	// 전달받은 값으로 휴가항목 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で休暇項目オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	public LeaveItem() {
	}

	// Getter & Setter
	// 휴가항목 객체에 저장된 휴가항목식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 休暇項目オブジェクトに保存された休暇項目識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getLeaveItemId() {
		return leaveItemId;
	}

	// 전달받은 휴가항목식별번호 값을 휴가항목 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った休暇項目識別番号の値を休暇項目オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setLeaveItemId(int leaveItemId) {
		this.leaveItemId = leaveItemId;
	}

	// 휴가항목 객체에 저장된 항목명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 休暇項目オブジェクトに保存された項目名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getItemName() {
		return itemName;
	}

	// 전달받은 항목명칭 값을 휴가항목 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った項目名称の値を休暇項目オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	// 휴가항목 객체에 저장된 ApplyStart일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 休暇項目オブジェクトに保存されたApplyStart日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Date getApplyStartDate() {
		return applyStartDate;
	}

	// 전달받은 ApplyStart일자 값을 휴가항목 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったApplyStart日付の値を休暇項目オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setApplyStartDate(Date applyStartDate) {
		this.applyStartDate = applyStartDate;
	}

	// 휴가항목 객체에 저장된 ApplyEnd일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 休暇項目オブジェクトに保存されたApplyEnd日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Date getApplyEndDate() {
		return applyEndDate;
	}

	// 전달받은 ApplyEnd일자 값을 휴가항목 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったApplyEnd日付の値を休暇項目オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setApplyEndDate(Date applyEndDate) {
		this.applyEndDate = applyEndDate;
	}

	// 휴가항목 객체에 저장된 사용여부 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 休暇項目オブジェクトに保存された使用可否の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getUseYn() {
		return useYn;
	}

	// 전달받은 사용여부 값을 휴가항목 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った使用可否の値を休暇項目オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
}
