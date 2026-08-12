package erp.settings.model;

/**
 * [기본환경설정] 직위 설정 Model DB 테이블: JOB_POSITION
 */
public class JobPosition {

	private Long jobPositionId;
	private String jobPositionName;

	public JobPosition() {
	}

	// Getter & Setter
	public Long getJobPositionId() {
		return jobPositionId;
	}

	public void setJobPositionId(Long jobPositionId) {
		this.jobPositionId = jobPositionId;
	}

	public String getJobPositionName() {
		return jobPositionName;
	}

	public void setJobPositionName(String jobPositionName) {
		this.jobPositionName = jobPositionName;
	}
}