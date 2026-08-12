package erp.employees.model;

import java.util.Date;

/**
 * [인사/사원관리] 사원 마스터 Model DB 테이블: EMPLOYEE
 */
public class Employee {

	// [DB 관리 항목]
	private int employeeId; // 사원 식별 번호 (PK)

	// [사원 기본정보]
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
	private String zipCode;
	private String address;
	private String telNo;
	private String mobileNo;
	private String email;
	private String snsAddress;
	private String memo;
	private String photoPath;

	// [급여 및 소득세 설정]
	private long basicPay; // 기본급
	private String incomeType;
	private int incomeTaxRate; // 소득세율
	private String youthTaxReduceYn;
	private Integer youthTaxRate; // 청년소득세 감면율

	// [4대보험 설정]
	private String npYn;
	private String hiYn;
	private String ltciYn;
	private String eiYn;
	private Integer hiReduceRate; // 건강보험 감면율
	private Integer ltciReduceRate; // 장기요양보험 감면율

	// [두루누리 사회보험 지원]
	private String durunuriSeparateYn;
	private Integer durunuriNpRate; // 두루누리 국민연금 지원율
	private Integer durunuriEiRate; // 두루누리 고용보험 지원율

	// [보험료 계산 기준 금액]
	private Long npMonthlyBase; // 국민연금 보수월액
	private Long hiMonthlyBase; // 건강보험 보수월액
	private Long eiMonthlyBase; // 고용보험 보수월액

	// [급여계좌]
	private String bankName;
	private String accountNo;

	// [병역정보]
	private String dischargeType;
	private String milBranch;
	private Date milServiceStart;
	private Date milServiceEnd;
	private String milRank;
	private String milSpecialty;
	private String milUnfinishedReason;

	// [퇴직정보]
	private String status;
	private String retireType;
	private Date retireDate;
	private String retireReason;
	private String afterRetireContact;

	public Employee() {
	}

	// 비즈니스 로직 예시
	public boolean isRetired() {
		return "퇴직".equals(this.status);
	}

	// Getter & Setter
	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmpNo() {
		return empNo;
	}

	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}

	public String getEmpType() {
		return empType;
	}

	public void setEmpType(String empType) {
		this.empType = empType;
	}

	public String getEmpNameKr() {
		return empNameKr;
	}

	public void setEmpNameKr(String empNameKr) {
		this.empNameKr = empNameKr;
	}

	public String getEmpNameEn() {
		return empNameEn;
	}

	public void setEmpNameEn(String empNameEn) {
		this.empNameEn = empNameEn;
	}

	public String getForeignYn() {
		return foreignYn;
	}

	public void setForeignYn(String foreignYn) {
		this.foreignYn = foreignYn;
	}

	public Date getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getJobPositionId() {
		return jobPositionId;
	}

	public void setJobPositionId(Integer jobPositionId) {
		this.jobPositionId = jobPositionId;
	}

	public String getJuminNo() {
		return juminNo;
	}

	public void setJuminNo(String juminNo) {
		this.juminNo = juminNo;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelNo() {
		return telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSnsAddress() {
		return snsAddress;
	}

	public void setSnsAddress(String snsAddress) {
		this.snsAddress = snsAddress;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public long getBasicPay() {
		return basicPay;
	}

	public void setBasicPay(long basicPay) {
		this.basicPay = basicPay;
	}

	public String getIncomeType() {
		return incomeType;
	}

	public void setIncomeType(String incomeType) {
		this.incomeType = incomeType;
	}

	public int getIncomeTaxRate() {
		return incomeTaxRate;
	}

	public void setIncomeTaxRate(int incomeTaxRate) {
		this.incomeTaxRate = incomeTaxRate;
	}

	public String getYouthTaxReduceYn() {
		return youthTaxReduceYn;
	}

	public void setYouthTaxReduceYn(String youthTaxReduceYn) {
		this.youthTaxReduceYn = youthTaxReduceYn;
	}

	public Integer getYouthTaxRate() {
		return youthTaxRate;
	}

	public void setYouthTaxRate(Integer youthTaxRate) {
		this.youthTaxRate = youthTaxRate;
	}

	public String getNpYn() {
		return npYn;
	}

	public void setNpYn(String npYn) {
		this.npYn = npYn;
	}

	public String getHiYn() {
		return hiYn;
	}

	public void setHiYn(String hiYn) {
		this.hiYn = hiYn;
	}

	public String getLtciYn() {
		return ltciYn;
	}

	public void setLtciYn(String ltciYn) {
		this.ltciYn = ltciYn;
	}

	public String getEiYn() {
		return eiYn;
	}

	public void setEiYn(String eiYn) {
		this.eiYn = eiYn;
	}

	public Integer getHiReduceRate() {
		return hiReduceRate;
	}

	public void setHiReduceRate(Integer hiReduceRate) {
		this.hiReduceRate = hiReduceRate;
	}

	public Integer getLtciReduceRate() {
		return ltciReduceRate;
	}

	public void setLtciReduceRate(Integer ltciReduceRate) {
		this.ltciReduceRate = ltciReduceRate;
	}

	public String getDurunuriSeparateYn() {
		return durunuriSeparateYn;
	}

	public void setDurunuriSeparateYn(String durunuriSeparateYn) {
		this.durunuriSeparateYn = durunuriSeparateYn;
	}

	public Integer getDurunuriNpRate() {
		return durunuriNpRate;
	}

	public void setDurunuriNpRate(Integer durunuriNpRate) {
		this.durunuriNpRate = durunuriNpRate;
	}

	public Integer getDurunuriEiRate() {
		return durunuriEiRate;
	}

	public void setDurunuriEiRate(Integer durunuriEiRate) {
		this.durunuriEiRate = durunuriEiRate;
	}

	public Long getNpMonthlyBase() {
		return npMonthlyBase;
	}

	public void setNpMonthlyBase(Long npMonthlyBase) {
		this.npMonthlyBase = npMonthlyBase;
	}

	public Long getHiMonthlyBase() {
		return hiMonthlyBase;
	}

	public void setHiMonthlyBase(Long hiMonthlyBase) {
		this.hiMonthlyBase = hiMonthlyBase;
	}

	public Long getEiMonthlyBase() {
		return eiMonthlyBase;
	}

	public void setEiMonthlyBase(Long eiMonthlyBase) {
		this.eiMonthlyBase = eiMonthlyBase;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getDischargeType() {
		return dischargeType;
	}

	public void setDischargeType(String dischargeType) {
		this.dischargeType = dischargeType;
	}

	public String getMilBranch() {
		return milBranch;
	}

	public void setMilBranch(String milBranch) {
		this.milBranch = milBranch;
	}

	public Date getMilServiceStart() {
		return milServiceStart;
	}

	public void setMilServiceStart(Date milServiceStart) {
		this.milServiceStart = milServiceStart;
	}

	public Date getMilServiceEnd() {
		return milServiceEnd;
	}

	public void setMilServiceEnd(Date milServiceEnd) {
		this.milServiceEnd = milServiceEnd;
	}

	public String getMilRank() {
		return milRank;
	}

	public void setMilRank(String milRank) {
		this.milRank = milRank;
	}

	public String getMilSpecialty() {
		return milSpecialty;
	}

	public void setMilSpecialty(String milSpecialty) {
		this.milSpecialty = milSpecialty;
	}

	public String getMilUnfinishedReason() {
		return milUnfinishedReason;
	}

	public void setMilUnfinishedReason(String milUnfinishedReason) {
		this.milUnfinishedReason = milUnfinishedReason;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRetireType() {
		return retireType;
	}

	public void setRetireType(String retireType) {
		this.retireType = retireType;
	}

	public Date getRetireDate() {
		return retireDate;
	}

	public void setRetireDate(Date retireDate) {
		this.retireDate = retireDate;
	}

	public String getRetireReason() {
		return retireReason;
	}

	public void setRetireReason(String retireReason) {
		this.retireReason = retireReason;
	}

	public String getAfterRetireContact() {
		return afterRetireContact;
	}

	public void setAfterRetireContact(String afterRetireContact) {
		this.afterRetireContact = afterRetireContact;
	}
}