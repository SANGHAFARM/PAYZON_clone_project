package erp.settings.model;

import java.util.Date;

/**
 * [기본환경설정] 회사 기본 및 담당자, 급여/이체 정보 Model DB 테이블: COMPANY
 */
// 사업장 정보를 보관하고 관련 기능에서 사용할 수 있도록 제공한다.
// 事業所情報を保持し、関連機能から利用できるように提供する。
public class Company {

	private int companyId; // 식별자 PK

	// [기본 정보]
	// データベースの主キー・外部キー・基本属性を保持し、永続化対象を識別する。
	private String cmpnName;
	private String ceoTitle;
	private String ceoName;
	private String bizRegNo;
	private String corpRegNo;
	private Date foundationDate;
	private String homepageUrl;

	// [주소 및 연락처]
	// 画面で入力または照会する業務属性を保持し、登録・詳細表示で共通利用する。
	private String zipCode;
	private String address;
	private String telNo;
	private String faxNo;

	// [업태/종목]
	// 会社の業態と取扱種目を保持し、事業所情報画面と証明書で使用する。
	private String bizType;
	private String bizItem;

	// [담당자 정보]
	// 担当者の氏名・部署・役職・連絡先など、利用者案内に必要な情報を保持する。
	private String managerName;
	private String managerDeptName;
	private String managerPosName;
	private String managerTelNo;
	private String managerMobileNo;
	private String managerEmail;

	// [급여 지급 설정]
	// 支給額・控除額・期間値を業務基準に従って計算し、合計と最終金額へ反映する。
	private String payCalcStartScope;
	private String payCalcStartDay;
	private String payCalcEndScope;
	private String payCalcEndDay;
	private String payDateScope;
	private String payDateDay;

	private String payBankName;
	private String payAccountNo;
	private String payAccountHolder;

	// [이미지 경로]
	// 選択された画像識別値と保存パスを確認し、登録済み画像を各画面で再利用できるようにする。
	private String logoImgPath;
	private String stampImgPath;

	// 전달받은 값으로 사업장 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で事業所オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	public Company() {
	}

	// Getter & Setter
	// 사업장 객체에 저장된 사업장식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 事業所オブジェクトに保存された事業所識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getCompanyId() {
		return companyId;
	}

	// 전달받은 사업장식별번호 값을 사업장 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った事業所識別番号の値を事業所オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	// 사업장 객체에 저장된 Cmpn명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 事業所オブジェクトに保存されたCmpn名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getCmpnName() {
		return cmpnName;
	}

	// 전달받은 Cmpn명칭 값을 사업장 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったCmpn名称の値を事業所オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setCmpnName(String cmpnName) {
		this.cmpnName = cmpnName;
	}

	// 사업장 객체에 저장된 대표자직함 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 事業所オブジェクトに保存された代表者役職名の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getCeoTitle() {
		return ceoTitle;
	}

	// 전달받은 대표자직함 값을 사업장 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った代表者役職名の値を事業所オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setCeoTitle(String ceoTitle) {
		this.ceoTitle = ceoTitle;
	}

	// 사업장 객체에 저장된 대표자명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 事業所オブジェクトに保存された代表者名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getCeoName() {
		return ceoName;
	}

	// 전달받은 대표자명칭 값을 사업장 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った代表者名称の値を事業所オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setCeoName(String ceoName) {
		this.ceoName = ceoName;
	}

	// 사업장 객체에 저장된 BizReg번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 事業所オブジェクトに保存されたBizReg番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getBizRegNo() {
		return bizRegNo;
	}

	// 전달받은 BizReg번호 값을 사업장 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったBizReg番号の値を事業所オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setBizRegNo(String bizRegNo) {
		this.bizRegNo = bizRegNo;
	}

	// 사업장 객체에 저장된 CorpReg번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 事業所オブジェクトに保存されたCorpReg番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getCorpRegNo() {
		return corpRegNo;
	}

	// 전달받은 CorpReg번호 값을 사업장 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったCorpReg番号の値を事業所オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setCorpRegNo(String corpRegNo) {
		this.corpRegNo = corpRegNo;
	}

	// 사업장 객체에 저장된 Foundation일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 事業所オブジェクトに保存されたFoundation日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Date getFoundationDate() {
		return foundationDate;
	}

	// 전달받은 Foundation일자 값을 사업장 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったFoundation日付の値を事業所オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setFoundationDate(Date foundationDate) {
		this.foundationDate = foundationDate;
	}

	// 사업장 객체에 저장된 홈페이지주소 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 事業所オブジェクトに保存されたホームページアドレスの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getHomepageUrl() {
		return homepageUrl;
	}

	// 전달받은 홈페이지주소 값을 사업장 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったホームページアドレスの値を事業所オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setHomepageUrl(String homepageUrl) {
		this.homepageUrl = homepageUrl;
	}

	// 사업장 객체에 저장된 Zip코드 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 事業所オブジェクトに保存されたZipコードの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getZipCode() {
		return zipCode;
	}

	// 전달받은 Zip코드 값을 사업장 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったZipコードの値を事業所オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	// 사업장 객체에 저장된 주소 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 事業所オブジェクトに保存された住所の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getAddress() {
		return address;
	}

	// 전달받은 주소 값을 사업장 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った住所の値を事業所オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setAddress(String address) {
		this.address = address;
	}

	// 사업장 객체에 저장된 Tel번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 事業所オブジェクトに保存されたTel番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getTelNo() {
		return telNo;
	}

	// 전달받은 Tel번호 값을 사업장 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったTel番号の値を事業所オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	// 사업장 객체에 저장된 Fax번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 事業所オブジェクトに保存されたFax番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getFaxNo() {
		return faxNo;
	}

	// 전달받은 Fax번호 값을 사업장 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったFax番号の値を事業所オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}

	// 사업장 객체에 저장된 Biz구분 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 事業所オブジェクトに保存されたBiz区分の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getBizType() {
		return bizType;
	}

	// 전달받은 Biz구분 값을 사업장 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったBiz区分の値を事業所オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	// 사업장 객체에 저장된 Biz항목 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 事業所オブジェクトに保存されたBiz項目の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getBizItem() {
		return bizItem;
	}

	// 전달받은 Biz항목 값을 사업장 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったBiz項目の値を事業所オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setBizItem(String bizItem) {
		this.bizItem = bizItem;
	}

	// 사업장 객체에 저장된 관리명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 事業所オブジェクトに保存された管理名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getManagerName() {
		return managerName;
	}

	// 전달받은 관리명칭 값을 사업장 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った管理名称の値を事業所オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	// 사업장 객체에 저장된 관리Dept명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 事業所オブジェクトに保存された管理Dept名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getManagerDeptName() {
		return managerDeptName;
	}

	// 전달받은 관리Dept명칭 값을 사업장 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った管理Dept名称の値を事業所オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setManagerDeptName(String managerDeptName) {
		this.managerDeptName = managerDeptName;
	}

	// 사업장 객체에 저장된 관리Pos명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 事業所オブジェクトに保存された管理Pos名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getManagerPosName() {
		return managerPosName;
	}

	// 전달받은 관리Pos명칭 값을 사업장 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った管理Pos名称の値を事業所オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setManagerPosName(String managerPosName) {
		this.managerPosName = managerPosName;
	}

	// 사업장 객체에 저장된 관리Tel번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 事業所オブジェクトに保存された管理Tel番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getManagerTelNo() {
		return managerTelNo;
	}

	// 전달받은 관리Tel번호 값을 사업장 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った管理Tel番号の値を事業所オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setManagerTelNo(String managerTelNo) {
		this.managerTelNo = managerTelNo;
	}

	// 사업장 객체에 저장된 관리휴대전화번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 事業所オブジェクトに保存された管理携帯電話番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getManagerMobileNo() {
		return managerMobileNo;
	}

	// 전달받은 관리휴대전화번호 값을 사업장 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った管理携帯電話番号の値を事業所オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setManagerMobileNo(String managerMobileNo) {
		this.managerMobileNo = managerMobileNo;
	}

	// 사업장 객체에 저장된 관리이메일 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 事業所オブジェクトに保存された管理メールの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getManagerEmail() {
		return managerEmail;
	}

	// 전달받은 관리이메일 값을 사업장 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った管理メールの値を事業所オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setManagerEmail(String managerEmail) {
		this.managerEmail = managerEmail;
	}

	// 사업장 객체에 저장된 지급계산StartScope 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 事業所オブジェクトに保存された支給計算StartScopeの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getPayCalcStartScope() {
		return payCalcStartScope;
	}

	// 전달받은 지급계산StartScope 값을 사업장 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った支給計算StartScopeの値を事業所オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setPayCalcStartScope(String payCalcStartScope) {
		this.payCalcStartScope = payCalcStartScope;
	}

	// 사업장 객체에 저장된 지급계산Start일용직 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 事業所オブジェクトに保存された支給計算Start日雇いの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getPayCalcStartDay() {
		return payCalcStartDay;
	}

	// 전달받은 지급계산Start일용직 값을 사업장 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った支給計算Start日雇いの値を事業所オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setPayCalcStartDay(String payCalcStartDay) {
		this.payCalcStartDay = payCalcStartDay;
	}

	// 사업장 객체에 저장된 지급계산EndScope 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 事業所オブジェクトに保存された支給計算EndScopeの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getPayCalcEndScope() {
		return payCalcEndScope;
	}

	// 전달받은 지급계산EndScope 값을 사업장 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った支給計算EndScopeの値を事業所オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setPayCalcEndScope(String payCalcEndScope) {
		this.payCalcEndScope = payCalcEndScope;
	}

	// 사업장 객체에 저장된 지급계산End일용직 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 事業所オブジェクトに保存された支給計算End日雇いの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getPayCalcEndDay() {
		return payCalcEndDay;
	}

	// 전달받은 지급계산End일용직 값을 사업장 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った支給計算End日雇いの値を事業所オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setPayCalcEndDay(String payCalcEndDay) {
		this.payCalcEndDay = payCalcEndDay;
	}

	// 사업장 객체에 저장된 지급일자Scope 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 事業所オブジェクトに保存された支給日付Scopeの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getPayDateScope() {
		return payDateScope;
	}

	// 전달받은 지급일자Scope 값을 사업장 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った支給日付Scopeの値を事業所オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setPayDateScope(String payDateScope) {
		this.payDateScope = payDateScope;
	}

	// 사업장 객체에 저장된 지급일자일용직 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 事業所オブジェクトに保存された支給日付日雇いの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getPayDateDay() {
		return payDateDay;
	}

	// 전달받은 지급일자일용직 값을 사업장 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った支給日付日雇いの値を事業所オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setPayDateDay(String payDateDay) {
		this.payDateDay = payDateDay;
	}

	// 사업장 객체에 저장된 지급은행명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 事業所オブジェクトに保存された支給銀行名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getPayBankName() {
		return payBankName;
	}

	// 전달받은 지급은행명칭 값을 사업장 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った支給銀行名称の値を事業所オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setPayBankName(String payBankName) {
		this.payBankName = payBankName;
	}

	// 사업장 객체에 저장된 지급계좌번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 事業所オブジェクトに保存された支給口座番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getPayAccountNo() {
		return payAccountNo;
	}

	// 전달받은 지급계좌번호 값을 사업장 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った支給口座番号の値を事業所オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setPayAccountNo(String payAccountNo) {
		this.payAccountNo = payAccountNo;
	}

	// 사업장 객체에 저장된 지급계좌Holder 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 事業所オブジェクトに保存された支給口座Holderの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getPayAccountHolder() {
		return payAccountHolder;
	}

	// 전달받은 지급계좌Holder 값을 사업장 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った支給口座Holderの値を事業所オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setPayAccountHolder(String payAccountHolder) {
		this.payAccountHolder = payAccountHolder;
	}

	// 사업장 객체에 저장된 로고이미지경로 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 事業所オブジェクトに保存されたロゴ画像パスの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getLogoImgPath() {
		return logoImgPath;
	}

	// 전달받은 로고이미지경로 값을 사업장 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったロゴ画像パスの値を事業所オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setLogoImgPath(String logoImgPath) {
		this.logoImgPath = logoImgPath;
	}

	// 사업장 객체에 저장된 도장이미지경로 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 事業所オブジェクトに保存された印鑑画像パスの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getStampImgPath() {
		return stampImgPath;
	}

	// 전달받은 도장이미지경로 값을 사업장 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った印鑑画像パスの値を事業所オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setStampImgPath(String stampImgPath) {
		this.stampImgPath = stampImgPath;
	}
}
