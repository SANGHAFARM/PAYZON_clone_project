package erp.employees.dto;

// 부서와 직위를 포함한 사원 목록 한 행을 전달한다.
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

	public int getEmployeeId() { return employeeId; }
	public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
	public String getEmploymentType() { return employmentType; }
	public void setEmploymentType(String employmentType) { this.employmentType = employmentType; }
	public String getJoinDate() { return joinDate; }
	public void setJoinDate(String joinDate) { this.joinDate = joinDate; }
	public String getEmployeeNo() { return employeeNo; }
	public void setEmployeeNo(String employeeNo) { this.employeeNo = employeeNo; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public String getEnglishName() { return englishName; }
	public void setEnglishName(String englishName) { this.englishName = englishName; }
	public String getDepartmentName() { return departmentName; }
	public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
	public String getPositionName() { return positionName; }
	public void setPositionName(String positionName) { this.positionName = positionName; }
	public String getMaskedResidentNo() { return maskedResidentNo; }
	public void setMaskedResidentNo(String maskedResidentNo) { this.maskedResidentNo = maskResidentNo(maskedResidentNo); }
	public String getNationalityType() { return nationalityType; }
	public void setNationalityType(String nationalityType) { this.nationalityType = nationalityType; }
	public String getAddress() { return address; }
	public void setAddress(String address) { this.address = address; }
	public String getPhone() { return phone; }
	public void setPhone(String phone) { this.phone = phone; }
	public String getMobile() { return mobile; }
	public void setMobile(String mobile) { this.mobile = mobile; }
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	public String getSns() { return sns; }
	public void setSns(String sns) { this.sns = sns; }
	public String getRetirementDate() { return retirementDate; }
	public void setRetirementDate(String retirementDate) { this.retirementDate = retirementDate; }
	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = "퇴직".equals(status) ? "RETIRED" : "WORK"; }
	public String getBankAccount() { return bankAccount; }
	public void setBankAccount(String bankAccount) { this.bankAccount = bankAccount; }
	public String getCareerPeriod() { return careerPeriod; }
	public void setCareerPeriod(String careerPeriod) { this.careerPeriod = careerPeriod; }
	public String getRetirementReason() { return retirementReason; }
	public void setRetirementReason(String retirementReason) { this.retirementReason = retirementReason; }
	public String getRetirementType() { return retirementType; }
	public void setRetirementType(String retirementType) { this.retirementType = retirementType; }
	public String getRetirementTypeName() { return retirementType; }
	public String getAfterContact() { return afterContact; }
	public void setAfterContact(String afterContact) { this.afterContact = afterContact; }
	public boolean isInterimSettlement() { return interimSettlement; }
	public void setInterimSettlement(boolean interimSettlement) { this.interimSettlement = interimSettlement; }
	public boolean isRetirementSettlement() { return retirementSettlement; }
	public void setRetirementSettlement(boolean retirementSettlement) { this.retirementSettlement = retirementSettlement; }
	public String getStatusName() { return "RETIRED".equals(status) ? "퇴직" : "재직"; }
	public String getServiceYears() {
		try {
			java.time.LocalDate start = java.time.LocalDate.parse(joinDate);
			java.time.LocalDate end = retirementDate == null || retirementDate.isEmpty() ? java.time.LocalDate.now() : java.time.LocalDate.parse(retirementDate);
			return java.time.Period.between(start, end).getYears() + "년";
		} catch (Exception e) { return ""; }
	}

	private String maskResidentNo(String residentNo) {
		// 목록 화면에서 주민등록번호 뒷자리가 노출되지 않도록 마스킹한다.
		if (residentNo == null || residentNo.trim().isEmpty()) return "";
		String digits = residentNo.replaceAll("[^0-9]", "");
		if (digits.length() < 7) return "******-*******";
		return digits.substring(0, 6) + "-" + digits.substring(6, 7) + "******";
	}
}
