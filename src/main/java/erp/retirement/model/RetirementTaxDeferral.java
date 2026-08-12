package erp.retirement.model;

import java.util.Date;

/**
 * [퇴직관리] 퇴직연금계좌 입금에 따른 퇴직소득세 과세이연 내역 Model DB 테이블: RETIREMENT_TAX_DEFERRAL
 */
public class RetirementTaxDeferral {

	// [DB 관리 항목]
	private Long retirementTaxDeferralId; // 과세이연 내역 식별 번호 (PK)
	private Long retirementCalculationId; // 퇴직급여 계산내역 식별 번호 (FK)

	// [퇴직연금계좌 입금정보]
	private String bizName; // 퇴직연금사업자명
	private String bizRegNo; // 사업자등록번호
	private String accountNo; // 계좌번호
	private Date depositDate; // 입금(이체)일
	private Long depositAmt; // 계좌입금금액

	public RetirementTaxDeferral() {
	}

	// Getter & Setter
	public Long getRetirementTaxDeferralId() {
		return retirementTaxDeferralId;
	}

	public void setRetirementTaxDeferralId(Long retirementTaxDeferralId) {
		this.retirementTaxDeferralId = retirementTaxDeferralId;
	}

	public Long getRetirementCalculationId() {
		return retirementCalculationId;
	}

	public void setRetirementCalculationId(Long retirementCalculationId) {
		this.retirementCalculationId = retirementCalculationId;
	}

	public String getBizName() {
		return bizName;
	}

	public void setBizName(String bizName) {
		this.bizName = bizName;
	}

	public String getBizRegNo() {
		return bizRegNo;
	}

	public void setBizRegNo(String bizRegNo) {
		this.bizRegNo = bizRegNo;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public Date getDepositDate() {
		return depositDate;
	}

	public void setDepositDate(Date depositDate) {
		this.depositDate = depositDate;
	}

	public Long getDepositAmt() {
		return depositAmt;
	}

	public void setDepositAmt(Long depositAmt) {
		this.depositAmt = depositAmt;
	}
}