package erp.employees.model;

import java.util.Date;

/**
 * [인사/사원관리] 사원 마스터 Model DB 테이블: EMPLOYEE
 */
// 사원 정보를 보관하고 관련 기능에서 사용할 수 있도록 제공한다.
// 社員情報を保持し、関連機能から利用できるように提供する。
public class Employee {

	// [DB 관리 항목]
	// データベースの主キー・外部キー・基本属性を保持し、永続化対象を識別する。
	private int employeeId; // 사원 식별 번호 (PK)

	// [사원 기본정보]
	// 社員の識別情報と在職・雇用・所属条件を確認し、対象社員データへ反映する。
	private String empNo;
	private String empType;
	private String empNameKr;
	private String empNameEn;
	private String foreignYn;
	private Date joinDate;
	private Integer departmentId; // 부서 외래키
	private Integer jobPositionId; // 직위 외래키
	private String juminNo;

	// [연락처 및 주소]
	// 画面で入力または照会する業務属性を保持し、登録・詳細表示で共通利用する。
	private String zipCode;
	private String address;
	private String telNo;
	private String mobileNo;
	private String email;
	private String snsAddress;
	private String memo;
	private String photoPath;

	// [급여 및 소득세 설정]
	// 支給額・控除額・期間値を業務基準に従って計算し、合計と最終金額へ反映する。
	private long basicPay; // 기본급
	private String incomeType;
	private int incomeTaxRate; // 소득세율
	private String youthTaxReduceYn;
	private Integer youthTaxRate; // 청년소득세 감면율

	// [4대보험 설정]
	// 保険・税区分と適用基準を確認し、対象金額に合う控除または非課税処理を適用する。
	private String npYn;
	private String hiYn;
	private String ltciYn;
	private String eiYn;
	private Integer hiReduceRate; // 건강보험 감면율
	private Integer ltciReduceRate; // 장기요양보험 감면율

	// [두루누리 사회보험 지원]
	// 保険・税区分と適用基準を確認し、対象金額に合う控除または非課税処理を適用する。
	private String durunuriSeparateYn;
	private Integer durunuriNpRate; // 두루누리 국민연금 지원율
	private Integer durunuriEiRate; // 두루누리 고용보험 지원율

	// [보험료 계산 기준 금액]
	// 支給額・控除額・期間値を業務基準に従って計算し、合計と最終金額へ反映する。
	private Long npMonthlyBase; // 국민연금 보수월액
	private Long hiMonthlyBase; // 건강보험 보수월액
	private Long eiMonthlyBase; // 고용보험 보수월액

	// [급여계좌]
	// 支給額・控除額・期間値を業務基準に従って計算し、合計と最終金額へ反映する。
	private String bankName;
	private String accountNo;

	// [병역정보]
	// 画面で入力または照会する業務属性を保持し、登録・詳細表示で共通利用する。
	private String dischargeType;
	private String milBranch;
	private Date milServiceStart;
	private Date milServiceEnd;
	private String milRank;
	private String milSpecialty;
	private String milUnfinishedReason;

	// [퇴직정보]
	// 社員の識別情報と在職・雇用・所属条件を確認し、対象社員データへ反映する。
	private String status;
	private String retireType;
	private Date retireDate;
	private String retireReason;
	private String afterRetireContact;

	// 전달받은 값으로 사원 객체의 초기 상태를 구성한다.
	// 생성 시 전달된 필수값을 각 필드에 보관하여 이후 조회와 화면 출력에서 재사용한다.
	// 受け取った値で社員オブジェクトの初期状態を構成する。
	// 生成時に受け取った必須値を各フィールドへ保持し、後続の照会と画面表示で再利用する。
	public Employee() {
	}

	// 비즈니스 로직 예시
	// 퇴직자 조건의 충족 여부를 확인하여 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職者条件を満たしているか確認して返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public boolean isRetired() {
		return "퇴직".equals(this.status);
	}

	// Getter & Setter
	// 사원 객체에 저장된 사원식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存された社員識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getEmployeeId() {
		return employeeId;
	}

	// 전달받은 사원식별번호 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った社員識別番号の値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	// 사원 객체에 저장된 Emp번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存されたEmp番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getEmpNo() {
		return empNo;
	}

	// 전달받은 Emp번호 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったEmp番号の値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}

	// 사원 객체에 저장된 Emp구분 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存されたEmp区分の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getEmpType() {
		return empType;
	}

	// 전달받은 Emp구분 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったEmp区分の値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmpType(String empType) {
		this.empType = empType;
	}

	// 사원 객체에 저장된 Emp명칭Kr 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存されたEmp名称Krの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getEmpNameKr() {
		return empNameKr;
	}

	// 전달받은 Emp명칭Kr 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったEmp名称Krの値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmpNameKr(String empNameKr) {
		this.empNameKr = empNameKr;
	}

	// 사원 객체에 저장된 Emp명칭En 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存されたEmp名称Enの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getEmpNameEn() {
		return empNameEn;
	}

	// 전달받은 Emp명칭En 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったEmp名称Enの値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmpNameEn(String empNameEn) {
		this.empNameEn = empNameEn;
	}

	// 사원 객체에 저장된 외국인여부 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存された外国人可否の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getForeignYn() {
		return foreignYn;
	}

	// 전달받은 외국인여부 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った外国人可否の値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setForeignYn(String foreignYn) {
		this.foreignYn = foreignYn;
	}

	// 사원 객체에 저장된 Join일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存されたJoin日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Date getJoinDate() {
		return joinDate;
	}

	// 전달받은 Join일자 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったJoin日付の値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}

	// 사원 객체에 저장된 부서식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存された部署識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Integer getDepartmentId() {
		return departmentId;
	}

	// 전달받은 부서식별번호 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った部署識別番号の値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	// 사원 객체에 저장된 직무직위식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存された職務役職識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Integer getJobPositionId() {
		return jobPositionId;
	}

	// 전달받은 직무직위식별번호 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った職務役職識別番号の値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setJobPositionId(Integer jobPositionId) {
		this.jobPositionId = jobPositionId;
	}

	// 사원 객체에 저장된 주민번호번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存された住民番号番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getJuminNo() {
		return juminNo;
	}

	// 전달받은 주민번호번호 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った住民番号番号の値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setJuminNo(String juminNo) {
		this.juminNo = juminNo;
	}

	// 사원 객체에 저장된 Zip코드 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存されたZipコードの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getZipCode() {
		return zipCode;
	}

	// 전달받은 Zip코드 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったZipコードの値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	// 사원 객체에 저장된 주소 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存された住所の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getAddress() {
		return address;
	}

	// 전달받은 주소 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った住所の値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setAddress(String address) {
		this.address = address;
	}

	// 사원 객체에 저장된 Tel번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存されたTel番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getTelNo() {
		return telNo;
	}

	// 전달받은 Tel번호 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったTel番号の値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	// 사원 객체에 저장된 휴대전화번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存された携帯電話番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getMobileNo() {
		return mobileNo;
	}

	// 전달받은 휴대전화번호 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った携帯電話番号の値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	// 사원 객체에 저장된 이메일 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存されたメールの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getEmail() {
		return email;
	}

	// 전달받은 이메일 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったメールの値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEmail(String email) {
		this.email = email;
	}

	// 사원 객체에 저장된 SNS주소 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存されたSNS住所の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getSnsAddress() {
		return snsAddress;
	}

	// 전달받은 SNS주소 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったSNS住所の値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setSnsAddress(String snsAddress) {
		this.snsAddress = snsAddress;
	}

	// 사원 객체에 저장된 메모 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存されたメモの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getMemo() {
		return memo;
	}

	// 전달받은 메모 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったメモの値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setMemo(String memo) {
		this.memo = memo;
	}

	// 사원 객체에 저장된 사진경로 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存された写真パスの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getPhotoPath() {
		return photoPath;
	}

	// 전달받은 사진경로 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った写真パスの値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	// 사원 객체에 저장된 Basic지급 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存されたBasic支給の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public long getBasicPay() {
		return basicPay;
	}

	// 전달받은 Basic지급 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったBasic支給の値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setBasicPay(long basicPay) {
		this.basicPay = basicPay;
	}

	// 사원 객체에 저장된 소득구분 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存された所得区分の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getIncomeType() {
		return incomeType;
	}

	// 전달받은 소득구분 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った所得区分の値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setIncomeType(String incomeType) {
		this.incomeType = incomeType;
	}

	// 사원 객체에 저장된 소득세금비율 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存された所得税金率の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getIncomeTaxRate() {
		return incomeTaxRate;
	}

	// 전달받은 소득세금비율 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った所得税金率の値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setIncomeTaxRate(int incomeTaxRate) {
		this.incomeTaxRate = incomeTaxRate;
	}

	// 사원 객체에 저장된 Youth세금감면여부 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存されたYouth税金減免可否の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getYouthTaxReduceYn() {
		return youthTaxReduceYn;
	}

	// 전달받은 Youth세금감면여부 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったYouth税金減免可否の値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setYouthTaxReduceYn(String youthTaxReduceYn) {
		this.youthTaxReduceYn = youthTaxReduceYn;
	}

	// 사원 객체에 저장된 Youth세금비율 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存されたYouth税金率の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Integer getYouthTaxRate() {
		return youthTaxRate;
	}

	// 전달받은 Youth세금비율 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったYouth税金率の値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setYouthTaxRate(Integer youthTaxRate) {
		this.youthTaxRate = youthTaxRate;
	}

	// 사원 객체에 저장된 국민연금여부 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存された国民年金可否の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getNpYn() {
		return npYn;
	}

	// 전달받은 국민연금여부 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った国民年金可否の値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setNpYn(String npYn) {
		this.npYn = npYn;
	}

	// 사원 객체에 저장된 건강보험여부 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存された健康保険可否の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getHiYn() {
		return hiYn;
	}

	// 전달받은 건강보험여부 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った健康保険可否の値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setHiYn(String hiYn) {
		this.hiYn = hiYn;
	}

	// 사원 객체에 저장된 장기요양보험여부 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存された介護保険可否の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getLtciYn() {
		return ltciYn;
	}

	// 전달받은 장기요양보험여부 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った介護保険可否の値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setLtciYn(String ltciYn) {
		this.ltciYn = ltciYn;
	}

	// 사원 객체에 저장된 고용보험여부 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存された雇用保険可否の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getEiYn() {
		return eiYn;
	}

	// 전달받은 고용보험여부 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った雇用保険可否の値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEiYn(String eiYn) {
		this.eiYn = eiYn;
	}

	// 사원 객체에 저장된 건강보험감면비율 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存された健康保険減免率の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Integer getHiReduceRate() {
		return hiReduceRate;
	}

	// 전달받은 건강보험감면비율 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った健康保険減免率の値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setHiReduceRate(Integer hiReduceRate) {
		this.hiReduceRate = hiReduceRate;
	}

	// 사원 객체에 저장된 장기요양보험감면비율 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存された介護保険減免率の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Integer getLtciReduceRate() {
		return ltciReduceRate;
	}

	// 전달받은 장기요양보험감면비율 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った介護保険減免率の値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setLtciReduceRate(Integer ltciReduceRate) {
		this.ltciReduceRate = ltciReduceRate;
	}

	// 사원 객체에 저장된 두루누리Separate여부 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存されたトゥルヌリSeparate可否の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getDurunuriSeparateYn() {
		return durunuriSeparateYn;
	}

	// 전달받은 두루누리Separate여부 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったトゥルヌリSeparate可否の値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setDurunuriSeparateYn(String durunuriSeparateYn) {
		this.durunuriSeparateYn = durunuriSeparateYn;
	}

	// 사원 객체에 저장된 두루누리국민연금비율 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存されたトゥルヌリ国民年金率の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Integer getDurunuriNpRate() {
		return durunuriNpRate;
	}

	// 전달받은 두루누리국민연금비율 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったトゥルヌリ国民年金率の値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setDurunuriNpRate(Integer durunuriNpRate) {
		this.durunuriNpRate = durunuriNpRate;
	}

	// 사원 객체에 저장된 두루누리고용보험비율 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存されたトゥルヌリ雇用保険率の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Integer getDurunuriEiRate() {
		return durunuriEiRate;
	}

	// 전달받은 두루누리고용보험비율 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったトゥルヌリ雇用保険率の値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setDurunuriEiRate(Integer durunuriEiRate) {
		this.durunuriEiRate = durunuriEiRate;
	}

	// 사원 객체에 저장된 국민연금월간Base 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存された国民年金月間Baseの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Long getNpMonthlyBase() {
		return npMonthlyBase;
	}

	// 전달받은 국민연금월간Base 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った国民年金月間Baseの値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setNpMonthlyBase(Long npMonthlyBase) {
		this.npMonthlyBase = npMonthlyBase;
	}

	// 사원 객체에 저장된 건강보험월간Base 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存された健康保険月間Baseの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Long getHiMonthlyBase() {
		return hiMonthlyBase;
	}

	// 전달받은 건강보험월간Base 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った健康保険月間Baseの値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setHiMonthlyBase(Long hiMonthlyBase) {
		this.hiMonthlyBase = hiMonthlyBase;
	}

	// 사원 객체에 저장된 고용보험월간Base 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存された雇用保険月間Baseの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Long getEiMonthlyBase() {
		return eiMonthlyBase;
	}

	// 전달받은 고용보험월간Base 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った雇用保険月間Baseの値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setEiMonthlyBase(Long eiMonthlyBase) {
		this.eiMonthlyBase = eiMonthlyBase;
	}

	// 사원 객체에 저장된 은행명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存された銀行名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getBankName() {
		return bankName;
	}

	// 전달받은 은행명칭 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った銀行名称の値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	// 사원 객체에 저장된 계좌번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存された口座番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getAccountNo() {
		return accountNo;
	}

	// 전달받은 계좌번호 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った口座番号の値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	// 사원 객체에 저장된 Discharge구분 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存されたDischarge区分の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getDischargeType() {
		return dischargeType;
	}

	// 전달받은 Discharge구분 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取ったDischarge区分の値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setDischargeType(String dischargeType) {
		this.dischargeType = dischargeType;
	}

	// 사원 객체에 저장된 병역군별 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存された兵役軍種の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getMilBranch() {
		return milBranch;
	}

	// 전달받은 병역군별 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った兵役軍種の値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setMilBranch(String milBranch) {
		this.milBranch = milBranch;
	}

	// 사원 객체에 저장된 병역근속Start 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存された兵役勤続Startの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Date getMilServiceStart() {
		return milServiceStart;
	}

	// 전달받은 병역근속Start 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った兵役勤続Startの値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setMilServiceStart(Date milServiceStart) {
		this.milServiceStart = milServiceStart;
	}

	// 사원 객체에 저장된 병역근속End 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存された兵役勤続Endの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Date getMilServiceEnd() {
		return milServiceEnd;
	}

	// 전달받은 병역근속End 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った兵役勤続Endの値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setMilServiceEnd(Date milServiceEnd) {
		this.milServiceEnd = milServiceEnd;
	}

	// 사원 객체에 저장된 병역계급 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存された兵役階級の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getMilRank() {
		return milRank;
	}

	// 전달받은 병역계급 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った兵役階級の値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setMilRank(String milRank) {
		this.milRank = milRank;
	}

	// 사원 객체에 저장된 병역특기 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存された兵役特技の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getMilSpecialty() {
		return milSpecialty;
	}

	// 전달받은 병역특기 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った兵役特技の値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setMilSpecialty(String milSpecialty) {
		this.milSpecialty = milSpecialty;
	}

	// 사원 객체에 저장된 병역미필사유 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存された兵役未完了理由の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getMilUnfinishedReason() {
		return milUnfinishedReason;
	}

	// 전달받은 병역미필사유 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った兵役未完了理由の値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setMilUnfinishedReason(String milUnfinishedReason) {
		this.milUnfinishedReason = milUnfinishedReason;
	}

	// 사원 객체에 저장된 상태 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存された状態の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getStatus() {
		return status;
	}

	// 전달받은 상태 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った状態の値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setStatus(String status) {
		this.status = status;
	}

	// 사원 객체에 저장된 퇴직구분 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存された退職区分の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getRetireType() {
		return retireType;
	}

	// 전달받은 퇴직구분 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った退職区分の値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setRetireType(String retireType) {
		this.retireType = retireType;
	}

	// 사원 객체에 저장된 퇴직일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存された退職日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public Date getRetireDate() {
		return retireDate;
	}

	// 전달받은 퇴직일자 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った退職日付の値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setRetireDate(Date retireDate) {
		this.retireDate = retireDate;
	}

	// 사원 객체에 저장된 퇴직사유 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存された退職理由の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getRetireReason() {
		return retireReason;
	}

	// 전달받은 퇴직사유 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った退職理由の値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setRetireReason(String retireReason) {
		this.retireReason = retireReason;
	}

	// 사원 객체에 저장된 퇴직 후퇴직연락처 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員オブジェクトに保存された退職後退職連絡先の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getAfterRetireContact() {
		return afterRetireContact;
	}

	// 전달받은 퇴직 후퇴직연락처 값을 사원 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った退職後退職連絡先の値を社員オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	public void setAfterRetireContact(String afterRetireContact) {
		this.afterRetireContact = afterRetireContact;
	}
}
