package erp.retire.model;

import java.util.Date;

// RETIRE_TAX_DEFER: 퇴직소득세 과세이연 내역
public class RetireTaxDefer {
	private int retireTaxDefId;
	private int retireCalcMstId;
	private String bizName;
	private String bizRegNo;
	private String accountNo;
	private Date depositDate;
	private long depositAmt;

	// 기본 생성자
	public RetireTaxDefer() {
	}

	public int getRetireTaxDefId() {
		return retireTaxDefId;
	}

	public void setRetireTaxDefId(int retireTaxDefId) {
		this.retireTaxDefId = retireTaxDefId;
	}

	public int getRetireCalcMstId() {
		return retireCalcMstId;
	}

	public void setRetireCalcMstId(int retireCalcMstId) {
		this.retireCalcMstId = retireCalcMstId;
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

	public long getDepositAmt() {
		return depositAmt;
	}

	public void setDepositAmt(long depositAmt) {
		this.depositAmt = depositAmt;
	}
}