package erp.payroll.dto;

import java.sql.Date;
import java.util.List;

// 4대보험 공제내역 화면 조회 결과
public class FourInsurancePage {

	private Date calculationStart;
	private Date calculationEnd;
	private Date paymentDate;
	private List<FourInsuranceDeduction> deductions;
	private FourInsuranceTotals totals;

	public Date getCalculationStart() { return calculationStart; }
	public void setCalculationStart(Date value) { calculationStart = value; }
	public Date getCalculationEnd() { return calculationEnd; }
	public void setCalculationEnd(Date value) { calculationEnd = value; }
	public Date getPaymentDate() { return paymentDate; }
	public void setPaymentDate(Date value) { paymentDate = value; }
	public List<FourInsuranceDeduction> getDeductions() { return deductions; }
	public void setDeductions(List<FourInsuranceDeduction> value) { deductions = value; }
	public FourInsuranceTotals getTotals() { return totals; }
	public void setTotals(FourInsuranceTotals value) { totals = value; }
}
