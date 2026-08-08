package erp.hr.model;

import java.util.Date;

// EMP_TRAINING: 교육/훈련
public class EmpTraining {
	private int trainId;
	private int empId;
	private String trainType;
	private String trainName;
	private Date startDate;
	private Date endDate;
	private String trainInstitute;
	private long trainCost;
	private long refundCost;

	// 기본 생성자
	public EmpTraining() {
	}

	public int getTrainId() {
		return trainId;
	}

	public void setTrainId(int trainId) {
		this.trainId = trainId;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public String getTrainType() {
		return trainType;
	}

	public void setTrainType(String trainType) {
		this.trainType = trainType;
	}

	public String getTrainName() {
		return trainName;
	}

	public void setTrainName(String trainName) {
		this.trainName = trainName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getTrainInstitute() {
		return trainInstitute;
	}

	public void setTrainInstitute(String trainInstitute) {
		this.trainInstitute = trainInstitute;
	}

	public long getTrainCost() {
		return trainCost;
	}

	public void setTrainCost(long trainCost) {
		this.trainCost = trainCost;
	}

	public long getRefundCost() {
		return refundCost;
	}

	public void setRefundCost(long refundCost) {
		this.refundCost = refundCost;
	}
}