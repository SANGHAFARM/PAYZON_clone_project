package erp.settings.model;

import java.util.Date;

/**
 * [기본환경설정] 회사 기본 및 담당자, 급여/이체 정보 Model DB 테이블: COMPANY
 */
public class Company {

	private Long companyId;

	private String cmpnName;
	private String ceoTitle;
	private String ceoName;
	private String bizRegNo;
	private String corpRegNo;
	private Date foundationDate;
	private String homepageUrl;

	private String zipCode;
	private String address;
	private String telNo;
	private String faxNo;

	private String bizType;
	private String bizItem;

	private String managerName;
	private String managerDeptName;
	private String managerPosName;
	private String managerTelNo;
	private String managerMobileNo;
	private String managerEmail;

	private String payCalcStartScope;
	private String payCalcStartDay;
	private String payCalcEndScope;
	private String payCalcEndDay;
	private String payDateScope;
	private String payDateDay;

	private String payBankName;
	private String payAccountNo;
	private String payAccountHolder;

	private String logoImgPath;
	private String stampImgPath;

	public Company() {
	}

	// Getter & Setter
	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getCmpnName() {
		return cmpnName;
	}

	public void setCmpnName(String cmpnName) {
		this.cmpnName = cmpnName;
	}

	public String getCeoTitle() {
		return ceoTitle;
	}

	public void setCeoTitle(String ceoTitle) {
		this.ceoTitle = ceoTitle;
	}

	public String getCeoName() {
		return ceoName;
	}

	public void setCeoName(String ceoName) {
		this.ceoName = ceoName;
	}

	public String getBizRegNo() {
		return bizRegNo;
	}

	public void setBizRegNo(String bizRegNo) {
		this.bizRegNo = bizRegNo;
	}

	public String getCorpRegNo() {
		return corpRegNo;
	}

	public void setCorpRegNo(String corpRegNo) {
		this.corpRegNo = corpRegNo;
	}

	public Date getFoundationDate() {
		return foundationDate;
	}

	public void setFoundationDate(Date foundationDate) {
		this.foundationDate = foundationDate;
	}

	public String getHomepageUrl() {
		return homepageUrl;
	}

	public void setHomepageUrl(String homepageUrl) {
		this.homepageUrl = homepageUrl;
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

	public String getFaxNo() {
		return faxNo;
	}

	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public String getBizItem() {
		return bizItem;
	}

	public void setBizItem(String bizItem) {
		this.bizItem = bizItem;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public String getManagerDeptName() {
		return managerDeptName;
	}

	public void setManagerDeptName(String managerDeptName) {
		this.managerDeptName = managerDeptName;
	}

	public String getManagerPosName() {
		return managerPosName;
	}

	public void setManagerPosName(String managerPosName) {
		this.managerPosName = managerPosName;
	}

	public String getManagerTelNo() {
		return managerTelNo;
	}

	public void setManagerTelNo(String managerTelNo) {
		this.managerTelNo = managerTelNo;
	}

	public String getManagerMobileNo() {
		return managerMobileNo;
	}

	public void setManagerMobileNo(String managerMobileNo) {
		this.managerMobileNo = managerMobileNo;
	}

	public String getManagerEmail() {
		return managerEmail;
	}

	public void setManagerEmail(String managerEmail) {
		this.managerEmail = managerEmail;
	}

	public String getPayCalcStartScope() {
		return payCalcStartScope;
	}

	public void setPayCalcStartScope(String payCalcStartScope) {
		this.payCalcStartScope = payCalcStartScope;
	}

	public String getPayCalcStartDay() {
		return payCalcStartDay;
	}

	public void setPayCalcStartDay(String payCalcStartDay) {
		this.payCalcStartDay = payCalcStartDay;
	}

	public String getPayCalcEndScope() {
		return payCalcEndScope;
	}

	public void setPayCalcEndScope(String payCalcEndScope) {
		this.payCalcEndScope = payCalcEndScope;
	}

	public String getPayCalcEndDay() {
		return payCalcEndDay;
	}

	public void setPayCalcEndDay(String payCalcEndDay) {
		this.payCalcEndDay = payCalcEndDay;
	}

	public String getPayDateScope() {
		return payDateScope;
	}

	public void setPayDateScope(String payDateScope) {
		this.payDateScope = payDateScope;
	}

	public String getPayDateDay() {
		return payDateDay;
	}

	public void setPayDateDay(String payDateDay) {
		this.payDateDay = payDateDay;
	}

	public String getPayBankName() {
		return payBankName;
	}

	public void setPayBankName(String payBankName) {
		this.payBankName = payBankName;
	}

	public String getPayAccountNo() {
		return payAccountNo;
	}

	public void setPayAccountNo(String payAccountNo) {
		this.payAccountNo = payAccountNo;
	}

	public String getPayAccountHolder() {
		return payAccountHolder;
	}

	public void setPayAccountHolder(String payAccountHolder) {
		this.payAccountHolder = payAccountHolder;
	}

	public String getLogoImgPath() {
		return logoImgPath;
	}

	public void setLogoImgPath(String logoImgPath) {
		this.logoImgPath = logoImgPath;
	}

	public String getStampImgPath() {
		return stampImgPath;
	}

	public void setStampImgPath(String stampImgPath) {
		this.stampImgPath = stampImgPath;
	}
}