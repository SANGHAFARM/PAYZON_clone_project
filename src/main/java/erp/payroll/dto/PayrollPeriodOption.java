package erp.payroll.dto;

// 지난 급여 불러오기에서 사용하는 급여 회차 선택 항목
public class PayrollPeriodOption {

	private int periodId;
	private String periodName;

	public PayrollPeriodOption(int periodId, String periodName) {
		this.periodId = periodId;
		this.periodName = periodName;
	}

	public int getPeriodId() {
		return periodId;
	}

	public String getPeriodName() {
		return periodName;
	}
}
