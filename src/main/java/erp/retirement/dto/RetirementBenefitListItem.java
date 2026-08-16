package erp.retirement.dto;

// 퇴직급여 입력/관리 목록의 조회 결과 한 행을 전달한다.
public class RetirementBenefitListItem {
	private int calculationId, employeeId, serviceDays;
	private String paymentDate, settlementType, employeeName, positionName, departmentName,
			calculationStartDate, calculationEndDate, paymentMethod;
	private long netPayment;
	public int getCalculationId() { return calculationId; }
	public void setCalculationId(int value) { calculationId = value; }
	public int getEmployeeId() { return employeeId; }
	public void setEmployeeId(int value) { employeeId = value; }
	public int getServiceDays() { return serviceDays; }
	public void setServiceDays(int value) { serviceDays = value; }
	public String getPaymentDate() { return paymentDate; }
	public void setPaymentDate(String value) { paymentDate = value; }
	public String getSettlementType() { return settlementType; }
	public void setSettlementType(String value) { settlementType = value; }
	public String getEmployeeName() { return employeeName; }
	public void setEmployeeName(String value) { employeeName = value; }
	public String getPositionName() { return positionName; }
	public void setPositionName(String value) { positionName = value; }
	public String getDepartmentName() { return departmentName; }
	public void setDepartmentName(String value) { departmentName = value; }
	public String getCalculationStartDate() { return calculationStartDate; }
	public void setCalculationStartDate(String value) { calculationStartDate = value; }
	public String getCalculationEndDate() { return calculationEndDate; }
	public void setCalculationEndDate(String value) { calculationEndDate = value; }
	public String getPaymentMethod() { return paymentMethod; }
	public void setPaymentMethod(String value) { paymentMethod = value; }
	public long getNetPayment() { return netPayment; }
	public void setNetPayment(long value) { netPayment = value; }
}
