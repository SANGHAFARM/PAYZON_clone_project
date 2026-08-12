package erp.employees.model;

import java.util.Date;

/**
 * [인사/사원관리] 사원 교육 및 훈련 이력 Model DB 테이블: EMPLOYEE_TRAINING
 */
public class EmployeeTraining {

	// [DB 관리 항목]
	private Long employeeTrainingId; // 교육/훈련 이력 식별 번호 (PK)
	private Long employeeId; // 사원 식별 번호 (FK)

	// [교육·훈련 입력 항목]
	private String trainType; // 교육구분
	private String trainName; // 교육명
	private Date startDate; // 교육기간(시작)
	private Date endDate; // 교육기간(종료)
	private String trainInstitute; // 교육기관
	private Long trainCost; // 교육비 (Wrapper)
	private Long refundCost; // 환급교육비 (Wrapper)

	public EmployeeTraining() {
	}

	// Getter & Setter
	public Long getEmployeeTrainingId() {
		return employeeTrainingId;
	}

	public void setEmployeeTrainingId(Long employeeTrainingId) {
		this.employeeTrainingId = employeeTrainingId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
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

	public Long getTrainCost() {
		return trainCost;
	}

	public void setTrainCost(Long trainCost) {
		this.trainCost = trainCost;
	}

	public Long getRefundCost() {
		return refundCost;
	}

	public void setRefundCost(Long refundCost) {
		this.refundCost = refundCost;
	}
}