package erp.employees.model;

import java.util.Date;

/**
 * [인사/사원관리] 제증명서 발급 내역 및 출력 설정 Model DB 테이블: CERTIFICATE_ISSUANCE
 */
// 제증명서Issuance 정보를 보관하고 관련 기능에서 사용할 수 있도록 제공한다.
// 証明書Issuance情報を保持し、関連機能から利用できるように提供する。
public class CertificateIssuance {

	// [DB 관리 항목]
	// データベースの主キー・外部キー・基本属性を保持し、永続化対象を識別する。
	private int certificateIssuanceId; // 증명서 발급내역 식별 번호 (PK)
	private int employeeId; // 발급 대상 사원 식별 번호 (FK)

	// [증명서 발급정보]
	// 証明書番号・区分・用途・発行日など、発行台帳で管理する情報を保持する。
	private String certDocNo; // 문서번호
	private String certType; // 증명서 종류 (재직경력서 등)
	private String purpose; // 발급용도
	private String certMemo; // 증명문구
	private Date issueDate; // 발급일
	private Integer issueDeptId; // 발급부서 식별 번호 (FK)

	// [증명서 출력설정]
	// 代表者・住民番号・用途など、証明書へ表示する項目の設定値を保持する。
	private String showCeoYn; // 대표자 표기 여부
	private String hideJuminYn; // 주민등록번호 숨김 여부
	private String showLogoYn; // 회사 로고 표시 여부
	private String showStampYn; // 회사 도장 표시 여부

	// 전달받은 값으로 제증명서Issuance 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で証明書Issuanceオブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	public CertificateIssuance() {
	}

	// Getter & Setter
	// 제증명서Issuance 객체에 저장된 제증명서Issuance식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 証明書Issuanceオブジェクトに保存された証明書Issuance識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getCertificateIssuanceId() {
		return certificateIssuanceId;
	}

	// 전달받은 제증명서Issuance식별번호 값을 제증명서Issuance 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った証明書Issuance識別番号の値を証明書Issuanceオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setCertificateIssuanceId(int certificateIssuanceId) {
		this.certificateIssuanceId = certificateIssuanceId;
	}

	// 제증명서Issuance 객체에 저장된 사원식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 証明書Issuanceオブジェクトに保存された社員識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getEmployeeId() {
		return employeeId;
	}

	// 전달받은 사원식별번호 값을 제증명서Issuance 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った社員識別番号の値を証明書Issuanceオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	// 제증명서Issuance 객체에 저장된 제증명서Doc번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 証明書Issuanceオブジェクトに保存された証明書Doc番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getCertDocNo() {
		return certDocNo;
	}

	// 전달받은 제증명서Doc번호 값을 제증명서Issuance 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った証明書Doc番号の値を証明書Issuanceオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setCertDocNo(String certDocNo) {
		this.certDocNo = certDocNo;
	}

	// 제증명서Issuance 객체에 저장된 제증명서구분 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 証明書Issuanceオブジェクトに保存された証明書区分の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getCertType() {
		return certType;
	}

	// 전달받은 제증명서구분 값을 제증명서Issuance 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った証明書区分の値を証明書Issuanceオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setCertType(String certType) {
		this.certType = certType;
	}

	// 제증명서Issuance 객체에 저장된 발급용도 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 証明書Issuanceオブジェクトに保存された発行用途の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getPurpose() {
		return purpose;
	}

	// 전달받은 발급용도 값을 제증명서Issuance 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った発行用途の値を証明書Issuanceオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	// 제증명서Issuance 객체에 저장된 제증명서메모 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 証明書Issuanceオブジェクトに保存された証明書メモの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getCertMemo() {
		return certMemo;
	}

	// 전달받은 제증명서메모 값을 제증명서Issuance 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った証明書メモの値を証明書Issuanceオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setCertMemo(String certMemo) {
		this.certMemo = certMemo;
	}

	// 제증명서Issuance 객체에 저장된 발급일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 証明書Issuanceオブジェクトに保存された発行日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Date getIssueDate() {
		return issueDate;
	}

	// 전달받은 발급일자 값을 제증명서Issuance 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った発行日付の値を証明書Issuanceオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	// 제증명서Issuance 객체에 저장된 발급Dept식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 証明書Issuanceオブジェクトに保存された発行Dept識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Integer getIssueDeptId() {
		return issueDeptId;
	}

	// 전달받은 발급Dept식별번호 값을 제증명서Issuance 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った発行Dept識別番号の値を証明書Issuanceオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setIssueDeptId(Integer issueDeptId) {
		this.issueDeptId = issueDeptId;
	}

	// 제증명서Issuance 객체에 저장된 표시대표자여부 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 証明書Issuanceオブジェクトに保存された表示代表者可否の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getShowCeoYn() {
		return showCeoYn;
	}

	// 전달받은 표시대표자여부 값을 제증명서Issuance 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った表示代表者可否の値を証明書Issuanceオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setShowCeoYn(String showCeoYn) {
		this.showCeoYn = showCeoYn;
	}

	// 제증명서Issuance 객체에 저장된 숨김주민번호여부 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 証明書Issuanceオブジェクトに保存された非表示住民番号可否の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getHideJuminYn() {
		return hideJuminYn;
	}

	// 전달받은 숨김주민번호여부 값을 제증명서Issuance 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った非表示住民番号可否の値を証明書Issuanceオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setHideJuminYn(String hideJuminYn) {
		this.hideJuminYn = hideJuminYn;
	}

	// 제증명서Issuance 객체에 저장된 표시로고여부 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 証明書Issuanceオブジェクトに保存された表示ロゴ可否の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getShowLogoYn() {
		return showLogoYn;
	}

	// 전달받은 표시로고여부 값을 제증명서Issuance 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った表示ロゴ可否の値を証明書Issuanceオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setShowLogoYn(String showLogoYn) {
		this.showLogoYn = showLogoYn;
	}

	// 제증명서Issuance 객체에 저장된 표시도장여부 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 証明書Issuanceオブジェクトに保存された表示印鑑可否の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getShowStampYn() {
		return showStampYn;
	}

	// 전달받은 표시도장여부 값을 제증명서Issuance 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った表示印鑑可否の値を証明書Issuanceオブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setShowStampYn(String showStampYn) {
		this.showStampYn = showStampYn;
	}
}
