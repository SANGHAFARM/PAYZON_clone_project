package erp.hr.model;

import java.util.Date;

// EMPLOYEE: 사원 기본정보, 급여설정, 병역, 퇴직 관리
public class Employee {
	private int empId;
	private String empNo;
	private String empType;
	private String empNameKr;
	private String empNameEn;
	private String foreignYn;
	private Date joinDate;
	private int deptId;
	private int posId;
	private String juminNo;
	private String zipCode;
	private String address;
	private String telNo;
	private String mobileNo;
	private String email;
	private String snsAddress;
	private String memo;
	private String photoPath;

	// 급여 및 4대보험 관련
	private long basicPay;
	private String incomeType;
	private int incomeTaxRate;
	private String youthTaxReduceYn;
	private int youthTaxRate;
	private String npYn;
	private String hiYn;
	private String ltciYn;
	private String eiYn;
	private int hiReduceRate;
	private int ltciReduceRate;
	private String durunuriSeparateYn;
	private int durunuriNpRate;
	private int durunuriEiRate;
	private long npMonthlyBase;
	private long hiMonthlyBase;
	private long eiMonthlyBase;

	// 계좌 및 기타
	private String bankName;
	private String accountNo;
	private String dischargeType;
	private String milBranch;
	private Date milServiceStart;
	private Date milServiceEnd;
	private String milRank;
	private String milSpecialty;
	private String milUnfinishedReason;
	private String status;
	private String retireType;
	private Date retireDate;
	private String retireReason;
	private String afterRetireContact;

	// 기본 생성자
	public Employee() {
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
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

	public int getDeptId() {
		return deptId;
	}

	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}

	public int getPosId() {
		return posId;
	}

	public void setPosId(int posId) {
		this.posId = posId;
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

	public int getYouthTaxRate() {
		return youthTaxRate;
	}

	public void setYouthTaxRate(int youthTaxRate) {
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

	public int getHiReduceRate() {
		return hiReduceRate;
	}

	public void setHiReduceRate(int hiReduceRate) {
		this.hiReduceRate = hiReduceRate;
	}

	public int getLtciReduceRate() {
		return ltciReduceRate;
	}

	public void setLtciReduceRate(int ltciReduceRate) {
		this.ltciReduceRate = ltciReduceRate;
	}

	public String getDurunuriSeparateYn() {
		return durunuriSeparateYn;
	}

	public void setDurunuriSeparateYn(String durunuriSeparateYn) {
		this.durunuriSeparateYn = durunuriSeparateYn;
	}

	public int getDurunuriNpRate() {
		return durunuriNpRate;
	}

	public void setDurunuriNpRate(int durunuriNpRate) {
		this.durunuriNpRate = durunuriNpRate;
	}

	public int getDurunuriEiRate() {
		return durunuriEiRate;
	}

	public void setDurunuriEiRate(int durunuriEiRate) {
		this.durunuriEiRate = durunuriEiRate;
	}

	public long getNpMonthlyBase() {
		return npMonthlyBase;
	}

	public void setNpMonthlyBase(long npMonthlyBase) {
		this.npMonthlyBase = npMonthlyBase;
	}

	public long getHiMonthlyBase() {
		return hiMonthlyBase;
	}

	public void setHiMonthlyBase(long hiMonthlyBase) {
		this.hiMonthlyBase = hiMonthlyBase;
	}

	public long getEiMonthlyBase() {
		return eiMonthlyBase;
	}

	public void setEiMonthlyBase(long eiMonthlyBase) {
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