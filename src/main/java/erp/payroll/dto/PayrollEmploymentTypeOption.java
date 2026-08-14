package erp.payroll.dto;

// 급여대장 상세의 고용형태 선택 항목
public class PayrollEmploymentTypeOption {

	private String code;
	private String name;

	public PayrollEmploymentTypeOption(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
}
