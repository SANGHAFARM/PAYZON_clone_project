package erp.retirement.dto;

// 퇴직처리 화면의 퇴직구분 선택값을 전달한다.
public class RetirementTypeItem {
	private final String code;
	private final String name;
	public RetirementTypeItem(String code, String name) { this.code = code; this.name = name; }
	public String getCode() { return code; }
	public String getName() { return name; }
}
