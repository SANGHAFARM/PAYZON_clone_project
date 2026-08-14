package erp.payroll.dto;

import java.util.ArrayList;
import java.util.List;

// 일용직 급여입력 화면에 표시할 사원 정보
public class DayWorkerPaymentEmployee {

	private int employeeId;
	private String employmentTypeName;
	private String employeeNumber;
	private String employeeName;
	private String departmentName;
	private String positionName;
	private String statusName;
	private long nationalPension;
	private long healthInsurance;
	private long longTermCareInsurance;
	private long employmentInsurance;
	private long incomeTax;
	private long localIncomeTax;
	private long mutualAidFee;
	private long totalPayment;
	private List<DayWorkerPaymentWork> workPayments = new ArrayList<>();

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmploymentTypeName() {
		return employmentTypeName;
	}

	public void setEmploymentTypeName(String employmentTypeName) {
		this.employmentTypeName = employmentTypeName;
	}

	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
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

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public long getNationalPension() {
		return nationalPension;
	}

	public void setNationalPension(long nationalPension) {
		this.nationalPension = nationalPension;
	}

	public long getHealthInsurance() {
		return healthInsurance;
	}

	public void setHealthInsurance(long healthInsurance) {
		this.healthInsurance = healthInsurance;
	}

	public long getLongTermCareInsurance() {
		return longTermCareInsurance;
	}

	public void setLongTermCareInsurance(long longTermCareInsurance) {
		this.longTermCareInsurance = longTermCareInsurance;
	}

	public long getEmploymentInsurance() {
		return employmentInsurance;
	}

	public void setEmploymentInsurance(long employmentInsurance) {
		this.employmentInsurance = employmentInsurance;
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

	public long getMutualAidFee() {
		return mutualAidFee;
	}

	public void setMutualAidFee(long mutualAidFee) {
		this.mutualAidFee = mutualAidFee;
	}

	public long getTotalPayment() {
		return totalPayment;
	}

	public void setTotalPayment(long totalPayment) {
		this.totalPayment = totalPayment;
	}

	public List<DayWorkerPaymentWork> getWorkPayments() {
		return workPayments;
	}

	public void setWorkPayments(List<DayWorkerPaymentWork> workPayments) {
		this.workPayments = workPayments;
	}

	public long getTotalDeduction() {
		return nationalPension + healthInsurance + longTermCareInsurance + employmentInsurance + incomeTax
				+ localIncomeTax + mutualAidFee;
	}

	public long getNetPayment() {
		return totalPayment - getTotalDeduction();
	}
}
