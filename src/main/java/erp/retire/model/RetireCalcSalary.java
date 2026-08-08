package erp.retire.model;

import java.util.Date;

// RETIRE_CALC_SALARY: 퇴직급여 산정자료
public class RetireCalcSalary {
	private int retireCalcSalId;
	private int retireCalcMstId;
	private String dataType;
	private Date periodStartDate;
	private Date periodEndDate;
	private double calcDays;
	private String payYm;
	private String itemName;
	private long amount;
	private long threeMonthAmount;

	// 기본 생성자
	public RetireCalcSalary() {
	}

	public int getRetireCalcSalId() {
		return retireCalcSalId;
	}

	public void setRetireCalcSalId(int retireCalcSalId) {
		this.retireCalcSalId = retireCalcSalId;
	}

	public int getRetireCalcMstId() {
		return retireCalcMstId;
	}

	public void setRetireCalcMstId(int retireCalcMstId) {
		this.retireCalcMstId = retireCalcMstId;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Date getPeriodStartDate() {
		return periodStartDate;
	}

	public void setPeriodStartDate(Date periodStartDate) {
		this.periodStartDate = periodStartDate;
	}

	public Date getPeriodEndDate() {
		return periodEndDate;
	}

	public void setPeriodEndDate(Date periodEndDate) {
		this.periodEndDate = periodEndDate;
	}

	public double getCalcDays() {
		return calcDays;
	}

	public void setCalcDays(double calcDays) {
		this.calcDays = calcDays;
	}

	public String getPayYm() {
		return payYm;
	}

	public void setPayYm(String payYm) {
		this.payYm = payYm;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public long getThreeMonthAmount() {
		return threeMonthAmount;
	}

	public void setThreeMonthAmount(long threeMonthAmount) {
		this.threeMonthAmount = threeMonthAmount;
	}
}
