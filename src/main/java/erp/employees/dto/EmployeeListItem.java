package erp.employees.dto;

// 부서와 직위를 포함한 사원 목록 한 행을 전달한다.
// 사원목록항목 처리에 필요한 값을 계층 간에 전달한다.
// 社員一覧項目処理に必要な値を各階層間で受け渡す。
public class EmployeeListItem {

	private int employeeId;
	private String employmentType;
	private String joinDate;
	private String employeeNo;
	private String name;
	private String englishName;
	private String departmentName;
	private String positionName;
	private String maskedResidentNo;
	private String nationalityType;
	private String address;
	private String phone;
	private String mobile;
	private String email;
	private String sns;
	private String retirementDate;
	private String status;
	private String bankAccount;
	private String careerPeriod;
	private String retirementReason;
	private String retirementType;
	private String afterContact;
	private boolean interimSettlement;
	private boolean retirementSettlement;

	// 사원목록항목 객체에 저장된 사원식별번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員一覧項目オブジェクトに保存された社員識別番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public int getEmployeeId() { return employeeId; }
	public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
	// 사원목록항목 객체에 저장된 고용구분 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員一覧項目オブジェクトに保存された雇用区分の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getEmploymentType() { return employmentType; }
	public void setEmploymentType(String employmentType) { this.employmentType = employmentType; }
	// 사원목록항목 객체에 저장된 Join일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員一覧項目オブジェクトに保存されたJoin日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getJoinDate() { return joinDate; }
	public void setJoinDate(String joinDate) { this.joinDate = joinDate; }
	// 사원목록항목 객체에 저장된 사원번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員一覧項目オブジェクトに保存された社員番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getEmployeeNo() { return employeeNo; }
	public void setEmployeeNo(String employeeNo) { this.employeeNo = employeeNo; }
	// 사원목록항목 객체에 저장된 명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員一覧項目オブジェクトに保存された名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	// 사원목록항목 객체에 저장된 English명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員一覧項目オブジェクトに保存されたEnglish名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getEnglishName() { return englishName; }
	public void setEnglishName(String englishName) { this.englishName = englishName; }
	// 사원목록항목 객체에 저장된 부서명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員一覧項目オブジェクトに保存された部署名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getDepartmentName() { return departmentName; }
	public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
	// 사원목록항목 객체에 저장된 직위명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員一覧項目オブジェクトに保存された役職名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getPositionName() { return positionName; }
	public void setPositionName(String positionName) { this.positionName = positionName; }
	// 사원목록항목 객체에 저장된 MaskedResident번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員一覧項目オブジェクトに保存されたMaskedResident番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getMaskedResidentNo() { return maskedResidentNo; }
	public void setMaskedResidentNo(String maskedResidentNo) { this.maskedResidentNo = maskResidentNo(maskedResidentNo); }
	// 사원목록항목 객체에 저장된 Nationality구분 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員一覧項目オブジェクトに保存されたNationality区分の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getNationalityType() { return nationalityType; }
	public void setNationalityType(String nationalityType) { this.nationalityType = nationalityType; }
	// 사원목록항목 객체에 저장된 주소 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員一覧項目オブジェクトに保存された住所の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getAddress() { return address; }
	public void setAddress(String address) { this.address = address; }
	// 사원목록항목 객체에 저장된 전화번호 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員一覧項目オブジェクトに保存された電話番号の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getPhone() { return phone; }
	public void setPhone(String phone) { this.phone = phone; }
	// 사원목록항목 객체에 저장된 휴대전화 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員一覧項目オブジェクトに保存された携帯電話の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getMobile() { return mobile; }
	public void setMobile(String mobile) { this.mobile = mobile; }
	// 사원목록항목 객체에 저장된 이메일 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員一覧項目オブジェクトに保存されたメールの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	// 사원목록항목 객체에 저장된 SNS 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員一覧項目オブジェクトに保存されたSNSの値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getSns() { return sns; }
	public void setSns(String sns) { this.sns = sns; }
	// 사원목록항목 객체에 저장된 퇴직급여일자 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員一覧項目オブジェクトに保存された退職給与日付の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getRetirementDate() { return retirementDate; }
	public void setRetirementDate(String retirementDate) { this.retirementDate = retirementDate; }
	// 사원목록항목 객체에 저장된 상태 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員一覧項目オブジェクトに保存された状態の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = "퇴직".equals(status) ? "RETIRED" : "WORK"; }
	// 사원목록항목 객체에 저장된 은행계좌 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員一覧項目オブジェクトに保存された銀行口座の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getBankAccount() { return bankAccount; }
	public void setBankAccount(String bankAccount) { this.bankAccount = bankAccount; }
	// 사원목록항목 객체에 저장된 경력기간 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員一覧項目オブジェクトに保存された経歴期間の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getCareerPeriod() { return careerPeriod; }
	public void setCareerPeriod(String careerPeriod) { this.careerPeriod = careerPeriod; }
	// 사원목록항목 객체에 저장된 퇴직급여사유 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員一覧項目オブジェクトに保存された退職給与理由の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getRetirementReason() { return retirementReason; }
	public void setRetirementReason(String retirementReason) { this.retirementReason = retirementReason; }
	// 사원목록항목 객체에 저장된 퇴직급여구분 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員一覧項目オブジェクトに保存された退職給与区分の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getRetirementType() { return retirementType; }
	public void setRetirementType(String retirementType) { this.retirementType = retirementType; }
	// 사원목록항목 객체에 저장된 퇴직급여구분명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員一覧項目オブジェクトに保存された退職給与区分名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getRetirementTypeName() { return retirementType; }
	// 사원목록항목 객체에 저장된 퇴직 후연락처 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員一覧項目オブジェクトに保存された退職後連絡先の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getAfterContact() { return afterContact; }
	public void setAfterContact(String afterContact) { this.afterContact = afterContact; }
	// 중간정산 조건의 충족 여부를 확인하여 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 中間精算条件を満たしているか確認して返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public boolean isInterimSettlement() { return interimSettlement; }
	public void setInterimSettlement(boolean interimSettlement) { this.interimSettlement = interimSettlement; }
	// 퇴직급여정산 조건의 충족 여부를 확인하여 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 退職給与精算条件を満たしているか確認して返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public boolean isRetirementSettlement() { return retirementSettlement; }
	public void setRetirementSettlement(boolean retirementSettlement) { this.retirementSettlement = retirementSettlement; }
	// 사원목록항목 객체에 저장된 상태명칭 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員一覧項目オブジェクトに保存された状態名称の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getStatusName() { return "RETIRED".equals(status) ? "퇴직" : "재직"; }
	// 사원목록항목 객체에 저장된 근속연도 목록 값을 반환한다.
	// 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
	// 社員一覧項目オブジェクトに保存された勤続年度一覧の値を返す。
	// 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
	public String getServiceYears() {
		try {
			java.time.LocalDate start = java.time.LocalDate.parse(joinDate);
			java.time.LocalDate end = retirementDate == null || retirementDate.isEmpty() ? java.time.LocalDate.now() : java.time.LocalDate.parse(retirementDate);
			return java.time.Period.between(start, end).getYears() + "년";
		} catch (Exception e) { return ""; }
	}

	// 사원목록항목 객체에 저장된 maskResident번호 값을 반환한다.
	// Controller·Service·DAO·JSP 사이에서 동일한 데이터를 일관된 구조로 전달하기 위해 사용한다.
	// 社員一覧項目オブジェクトに保存されたmaskResident番号の値を返す。
	// Controller・Service・DAO・JSP間で同じデータを一貫した構造として受け渡すために使用する。
	private String maskResidentNo(String residentNo) {
		// 목록 화면에서 주민등록번호 뒷자리가 노출되지 않도록 마스킹한다.
		// 画面表示に必要なデータとメッセージをrequestまたはsessionへ保存し、JSPへ引き渡す。
		if (residentNo == null || residentNo.trim().isEmpty()) return "";
		String digits = residentNo.replaceAll("[^0-9]", "");
		if (digits.length() < 7) return "******-*******";
		return digits.substring(0, 6) + "-" + digits.substring(6, 7) + "******";
	}
}
