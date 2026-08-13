package erp.employees.service;

// 발급대장의 증명서 종류, 발급기간, 검색어 및 페이지 조건 객체
public class CertificateRegisterCondition {
	private String certificateType;
	private String issueDateFrom;
	private String issueDateTo;
	private String keyword;
	private int page;
	private int pageSize;

	public String getCertificateType() { return certificateType; }
	public void setCertificateType(String certificateType) { this.certificateType = certificateType; }
	public String getIssueDateFrom() { return issueDateFrom; }
	public void setIssueDateFrom(String issueDateFrom) { this.issueDateFrom = issueDateFrom; }
	public String getIssueDateTo() { return issueDateTo; }
	public void setIssueDateTo(String issueDateTo) { this.issueDateTo = issueDateTo; }
	public String getKeyword() { return keyword; }
	public void setKeyword(String keyword) { this.keyword = keyword; }
	public int getPage() { return page; }
	public void setPage(int page) { this.page = page; }
	public int getPageSize() { return pageSize; }
	public void setPageSize(int pageSize) { this.pageSize = pageSize; }
}
