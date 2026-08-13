package erp.retirement.dto;

// 퇴직처리 모달의 퇴직구분 선택값 DTO
public class RetirementTypeItem {
	private final String code;
	private final String name;
	public RetirementTypeItem(String code, String name) { this.code = code; this.name = name; }
	public String getCode() { return code; }
	public String getName() { return name; }
}
