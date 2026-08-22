package erp.employees.dto;

// 제증명서 발급대장의 조회 결과 한 행을 전달한다.
// 제증명서등록항목 처리에 필요한 값을 계층 간에 전달한다.
// 証明書登録項目処理に必要な値を各階層間で受け渡す。
public class CertificateRegisterItem {
	private int certificateId;
	private String certificateNo;
	private String certificateType;
	private String certificateTypeName;
	private String certificateUse;
	private String employmentType;
	private String employeeName;
	private String departmentName;
	private String positionName;
	private String issueDate;

	// 제증명서등록항목 객체에 저장된 제증명서식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 証明書登録項目オブジェクトに保存された証明書識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getCertificateId() { return certificateId; }
	public void setCertificateId(int certificateId) { this.certificateId = certificateId; }
	// 제증명서등록항목 객체에 저장된 제증명서번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 証明書登録項目オブジェクトに保存された証明書番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getCertificateNo() { return certificateNo; }
	public void setCertificateNo(String certificateNo) { this.certificateNo = certificateNo; }
	// 제증명서등록항목 객체에 저장된 제증명서구분 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 証明書登録項目オブジェクトに保存された証明書区分の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getCertificateType() { return certificateType; }
	public void setCertificateType(String certificateType) { this.certificateType = certificateType; }
	// 제증명서등록항목 객체에 저장된 제증명서구분명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 証明書登録項目オブジェクトに保存された証明書区分名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getCertificateTypeName() { return certificateTypeName; }
	public void setCertificateTypeName(String certificateTypeName) { this.certificateTypeName = certificateTypeName; }
	// 제증명서등록항목 객체에 저장된 제증명서사용 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 証明書登録項目オブジェクトに保存された証明書使用の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getCertificateUse() { return certificateUse; }
	public void setCertificateUse(String certificateUse) { this.certificateUse = certificateUse; }
	// 제증명서등록항목 객체에 저장된 고용구분 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 証明書登録項目オブジェクトに保存された雇用区分の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getEmploymentType() { return employmentType; }
	public void setEmploymentType(String employmentType) { this.employmentType = employmentType; }
	// 제증명서등록항목 객체에 저장된 사원명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 証明書登録項目オブジェクトに保存された社員名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getEmployeeName() { return employeeName; }
	public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
	// 제증명서등록항목 객체에 저장된 부서명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 証明書登録項目オブジェクトに保存された部署名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getDepartmentName() { return departmentName; }
	public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
	// 제증명서등록항목 객체에 저장된 직위명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 証明書登録項目オブジェクトに保存された役職名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getPositionName() { return positionName; }
	public void setPositionName(String positionName) { this.positionName = positionName; }
	// 제증명서등록항목 객체에 저장된 발급일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 証明書登録項目オブジェクトに保存された発行日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getIssueDate() { return issueDate; }
	public void setIssueDate(String issueDate) { this.issueDate = issueDate; }
}
