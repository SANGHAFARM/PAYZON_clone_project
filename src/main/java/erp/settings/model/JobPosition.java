package erp.settings.model;

/**
 * [기본환경설정] 직위 설정 Model DB 테이블: JOB_POSITION
 */
public class JobPosition {

	private int jobPositionId; // 직위 식별 번호 (PK)
	private String jobPositionName;

	public JobPosition() {
	}

	// Getter & Setter
	public int getJobPositionId() {
		return jobPositionId;
	}

	public void setJobPositionId(int jobPositionId) {
		this.jobPositionId = jobPositionId;
	}

	public String getJobPositionName() {
		return jobPositionName;
	}

	public void setJobPositionName(String jobPositionName) {
		this.jobPositionName = jobPositionName;
	}
}