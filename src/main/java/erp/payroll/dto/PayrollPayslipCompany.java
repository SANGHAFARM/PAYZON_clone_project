package erp.payroll.dto;

// 급여명세서에 표시할 회사 정보
public class PayrollPayslipCompany {

	private String companyName;
	private String representativeName;
	private String logoUrl;
	private String stampUrl;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getRepresentativeName() {
		return representativeName;
	}

	public void setRepresentativeName(String representativeName) {
		this.representativeName = representativeName;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getStampUrl() {
		return stampUrl;
	}

	public void setStampUrl(String stampUrl) {
		this.stampUrl = stampUrl;
	}
}
