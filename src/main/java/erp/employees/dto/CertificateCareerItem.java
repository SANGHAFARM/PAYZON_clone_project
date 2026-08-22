package erp.employees.dto;

// 경력증명서에 표시할 근무기간 한 행을 전달한다.
// 제증명서경력항목 처리에 필요한 값을 계층 간에 전달한다.
// 証明書経歴項目処理に必要な値を各階層間で受け渡す。
public class CertificateCareerItem {
	private String joinDate;
	private String retirementDate;
	private String departmentName;
	private String positionName;
	private String duty;

	// 제증명서경력항목 객체에 저장된 Join일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 証明書経歴項目オブジェクトに保存されたJoin日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getJoinDate() { return joinDate; }
	public void setJoinDate(String joinDate) { this.joinDate = joinDate; }
	// 제증명서경력항목 객체에 저장된 퇴직급여일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 証明書経歴項目オブジェクトに保存された退職給与日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getRetirementDate() { return retirementDate; }
	public void setRetirementDate(String retirementDate) { this.retirementDate = retirementDate; }
	// 제증명서경력항목 객체에 저장된 부서명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 証明書経歴項目オブジェクトに保存された部署名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getDepartmentName() { return departmentName; }
	public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
	// 제증명서경력항목 객체에 저장된 직위명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 証明書経歴項目オブジェクトに保存された役職名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getPositionName() { return positionName; }
	public void setPositionName(String positionName) { this.positionName = positionName; }
	// 제증명서경력항목 객체에 저장된 담당업무 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 証明書経歴項目オブジェクトに保存された担当業務の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getDuty() { return duty; }
	public void setDuty(String duty) { this.duty = duty; }
}
