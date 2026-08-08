package erp.payroll.model;

// TAX_FREE_ITEM: 비과세 목록
public class TaxFreeItem {
	private String taxFreeCode;
	private String legalClause;
	private String reportField;
	private String taxFreeName;
	private long defaultLimit;
	private String payStatementYn;
	private String incomeCategory;

	// 기본 생성자
	public TaxFreeItem() {
	}

	public String getTaxFreeCode() {
		return taxFreeCode;
	}

	public void setTaxFreeCode(String taxFreeCode) {
		this.taxFreeCode = taxFreeCode;
	}

	public String getLegalClause() {
		return legalClause;
	}

	public void setLegalClause(String legalClause) {
		this.legalClause = legalClause;
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

	public long getDefaultLimit() {
		return defaultLimit;
	}

	public void setDefaultLimit(long defaultLimit) {
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