package erp.payroll.dto;

// 급여 대상 사원 검색에서 사용하는 직위 선택 항목
public class PayrollPositionOption {

	private int positionId;
	private String positionName;

	public PayrollPositionOption(int positionId, String positionName) {
		this.positionId = positionId;
		this.positionName = positionName;
	}

	public int getPositionId() {
		return positionId;
	}

	public String getPositionName() {
		return positionName;
	}
}
