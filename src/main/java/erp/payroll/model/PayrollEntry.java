package erp.payroll.model;

/**
 * [급여관리] 사원별 지급 및 공제 항목 상세 내역 Model DB 테이블: PAYROLL_ENTRY
 */
public class PayrollEntry {

	// [연결 정보]
	private Long payrollEntryId; // 지급·공제 상세내역 식별 번호 (PK)
	private Long payrollEmployeeId; // 사원별 급여결과 식별 번호 (FK)

	// [지급·공제 항목]
	// ※ 하나는 값이 있고, 다른 하나는 반드시 NULL이어야 하는 상호 배타적 구조
	private Long payItemId; // 지급행일 경우 지급항목(PayItem) 식별 번호 (FK, null 허용)
	private Long deductItemId; // 공제행일 경우 공제항목(DeductItem) 식별 번호 (FK, null 허용)

	// [항목별 금액]
	private Long amount; // 지급액 또는 공제액

	public PayrollEntry() {
	}

	// [비즈니스 로직] 지급 항목인지 공제 항목인지 스스로 판단
	public boolean isPaymentEntry() {
		return this.payItemId != null && this.payItemId > 0;
	}

	public boolean isDeductEntry() {
		return this.deductItemId != null && this.deductItemId > 0;
	}

	// Getter & Setter
	public Long getPayrollEntryId() {
		return payrollEntryId;
	}

	public void setPayrollEntryId(Long payrollEntryId) {
		this.payrollEntryId = payrollEntryId;
	}

	public Long getPayrollEmployeeId() {
		return payrollEmployeeId;
	}

	public void setPayrollEmployeeId(Long payrollEmployeeId) {
		this.payrollEmployeeId = payrollEmployeeId;
	}

	public Long getPayItemId() {
		return payItemId;
	}

	public void setPayItemId(Long payItemId) {
		this.payItemId = payItemId;
	}

	public Long getDeductItemId() {
		return deductItemId;
	}

	public void setDeductItemId(Long deductItemId) {
		this.deductItemId = deductItemId;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}
}