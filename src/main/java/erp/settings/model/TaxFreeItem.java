package erp.settings.model;

/**
 * [기본환경설정] 비과세/감면 소득 목록 Model DB 테이블: TAX_FREE_ITEM
 */
public class TaxFreeItem {

	private String legalClause;
	private String taxFreeCode; // PK
	private String reportField;
	private String taxFreeName;
	private Long defaultLimit; // 한도금액 (Wrapper)
	private String payStatementYn;
	private String incomeCategory;

	public TaxFreeItem() {
	}

	// Getter & Setter
	public String getLegalClause() {
		return legalClause;
	}

	public void setLegalClause(String legalClause) {
		this.legalClause = legalClause;
	}

	public String getTaxFreeCode() {
		return taxFreeCode;
	}

	public void setTaxFreeCode(String taxFreeCode) {
		this.taxFreeCode = taxFreeCode;
	}

	public String getReportField() {
		return reportField;
	}

	public void setReportField(String reportField) {
		this.reportField = reportField;
	}

	public String getTaxFreeName() {
		return taxFreeName;
	}

	public void setTaxFreeName(String taxFreeName) {
		this.taxFreeName = taxFreeName;
	}

	public Long getDefaultLimit() {
		return defaultLimit;
	}

	public void setDefaultLimit(Long defaultLimit) {
		this.defaultLimit = defaultLimit;
	}

	public String getPayStatementYn() {
		return payStatementYn;
	}

	public void setPayStatementYn(String payStatementYn) {
		this.payStatementYn = payStatementYn;
	}

	public String getIncomeCategory() {
		return incomeCategory;
	}

	public void setIncomeCategory(String incomeCategory) {
		this.incomeCategory = incomeCategory;
	}
}