package erp.retirement.dto;

import java.util.ArrayList;
import java.util.List;

// 퇴직급여명세서 화면에 필요한 값을 전달한다.
public class RetirementPayslip {

	private int calculationId;
	private int employeeId;
	private int serviceDays;
	private String settlementType;
	private String employeeName;
	private String departmentName;
	private String positionName;
	private String joinDate;
	private String retirementDate;
	private long compensation;
	private long dismissalAllowance;
	private long threeMonthTotal;
	private long dailyAverage;
	private long dailyOrdinary;
	private long retirementIncome;
	private long incomeTax;
	private long localIncomeTax;
	private long otherDeduction;
	private long deductionTotal;
	private long netPayment;
	private long salaryDaysTotal;
	private long salaryTotal;
	private List<SalaryDetail> salaryDetails = new ArrayList<>();
	private List<OtherIncome> otherIncomes = new ArrayList<>();

	public int getCalculationId() {
		return calculationId;
	}

	public void setCalculationId(int calculationId) {
		this.calculationId = calculationId;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public int getServiceDays() {
		return serviceDays;
	}

	public void setServiceDays(int serviceDays) {
		this.serviceDays = serviceDays;
	}

	public String getSettlementType() {
		return settlementType;
	}

	public void setSettlementType(String settlementType) {
		this.settlementType = settlementType;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	public String getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}

	public String getRetirementDate() {
		return retirementDate;
	}

	public void setRetirementDate(String retirementDate) {
		this.retirementDate = retirementDate;
	}

	public long getCompensation() {
		return compensation;
	}

	public void setCompensation(long compensation) {
		this.compensation = compensation;
	}

	public long getDismissalAllowance() {
		return dismissalAllowance;
	}

	public void setDismissalAllowance(long dismissalAllowance) {
		this.dismissalAllowance = dismissalAllowance;
	}

	public long getThreeMonthTotal() {
		return threeMonthTotal;
	}

	public void setThreeMonthTotal(long threeMonthTotal) {
		this.threeMonthTotal = threeMonthTotal;
	}

	public long getDailyAverage() {
		return dailyAverage;
	}

	public void setDailyAverage(long dailyAverage) {
		this.dailyAverage = dailyAverage;
	}

	public long getDailyOrdinary() {
		return dailyOrdinary;
	}

	public void setDailyOrdinary(long dailyOrdinary) {
		this.dailyOrdinary = dailyOrdinary;
	}

	public long getRetirementIncome() {
		return retirementIncome;
	}

	public void setRetirementIncome(long retirementIncome) {
		this.retirementIncome = retirementIncome;
	}

	public long getIncomeTax() {
		return incomeTax;
	}

	public void setIncomeTax(long incomeTax) {
		this.incomeTax = incomeTax;
	}

	public long getLocalIncomeTax() {
		return localIncomeTax;
	}

	public void setLocalIncomeTax(long localIncomeTax) {
		this.localIncomeTax = localIncomeTax;
	}

	public long getOtherDeduction() {
		return otherDeduction;
	}

	public void setOtherDeduction(long otherDeduction) {
		this.otherDeduction = otherDeduction;
	}

	public long getDeductionTotal() {
		return deductionTotal;
	}

	public void setDeductionTotal(long deductionTotal) {
		this.deductionTotal = deductionTotal;
	}

	public long getNetPayment() {
		return netPayment;
	}

	public void setNetPayment(long netPayment) {
		this.netPayment = netPayment;
	}

	public long getSalaryDaysTotal() {
		return salaryDaysTotal;
	}

	public void setSalaryDaysTotal(long salaryDaysTotal) {
		this.salaryDaysTotal = salaryDaysTotal;
	}

	public long getSalaryTotal() {
		return salaryTotal;
	}

	public void setSalaryTotal(long salaryTotal) {
		this.salaryTotal = salaryTotal;
	}

	public List<SalaryDetail> getSalaryDetails() {
		return salaryDetails;
	}

	public List<OtherIncome> getOtherIncomes() {
		return otherIncomes;
	}

	public static class SalaryDetail {
		private String startDate;
		private String endDate;
		private double days;
		private long amount;

		public String getStartDate() {
			return startDate;
		}

		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}

		public String getEndDate() {
			return endDate;
		}

		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}

		public double getDays() {
			return days;
		}

		public void setDays(double days) {
			this.days = days;
		}

		public long getAmount() {
			return amount;
		}

		public void setAmount(long amount) {
			this.amount = amount;
		}
	}

	public static class OtherIncome {
		private String itemName;
		private long annualAmount;
		private long threeMonthAmount;

		public String getItemName() {
			return itemName;
		}

		public void setItemName(String itemName) {
			this.itemName = itemName;
		}

		public long getAnnualAmount() {
			return annualAmount;
		}

		public void setAnnualAmount(long annualAmount) {
			this.annualAmount = annualAmount;
		}

		public long getThreeMonthAmount() {
			return threeMonthAmount;
		}

		public void setThreeMonthAmount(long threeMonthAmount) {
			this.threeMonthAmount = threeMonthAmount;
		}
	}
}
