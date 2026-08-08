package erp.hr.model;

import java.util.Date;

// EMP_CAREER: 경력
public class EmpCareer {

	private int carId;
	private int empId;
	private String companyName;
	private Date joinDate;
	private Date quitDate;
	private String finalPosition;
	private String duty;
	private String quitReason;

	// 기본 생성자
	public EmpCareer() {
	}
	
	public int getCarId() {
		return carId;
	}

	public void setCarId(int carId) {
		this.carId = carId;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Date getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}

	public Date getQuitDate() {
		return quitDate;
	}

	public void setQuitDate(Date quitDate) {
		this.quitDate = quitDate;
	}

	public String getFinalPosition() {
		return finalPosition;
	}

	public void setFinalPosition(String finalPosition) {
		this.finalPosition = finalPosition;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public String getQuitReason() {
		return quitReason;
	}

	public void setQuitReason(String quitReason) {
		this.quitReason = quitReason;
	}

}