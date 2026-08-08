package erp.hr.model;

import java.util.Date;

// EMP_INSURANCE_HISTORY: 4대보험 자격정보
public class EmpInsuranceHistory {

	private int insHistId;
	private int empId;
	private String insuranceType;
	private String symbolNo;
	private Date acquireDate;
	private Date lossDate;

	// 기본 생성자
	public EmpInsuranceHistory() {
	}

	public int getInsHistId() {
		return insHistId;
	}

	public void setInsHistId(int insHistId) {
		this.insHistId = insHistId;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public String getInsuranceType() {
		return insuranceType;
	}

	public void setInsuranceType(String insuranceType) {
		this.insuranceType = insuranceType;
	}

	public String getSymbolNo() {
		return symbolNo;
	}

	public void setSymbolNo(String symbolNo) {
		this.symbolNo = symbolNo;
	}

	public Date getAcquireDate() {
		return acquireDate;
	}

	public void setAcquireDate(Date acquireDate) {
		this.acquireDate = acquireDate;
	}

	public Date getLossDate() {
		return lossDate;
	}

	public void setLossDate(Date lossDate) {
		this.lossDate = lossDate;
	}
}