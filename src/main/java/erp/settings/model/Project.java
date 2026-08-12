package erp.settings.model;

/**
 * [기본환경설정] 일용직 현장/프로젝트 목록 Model DB 테이블: PROJECT
 */
public class Project {

	private Long projectId;
	private String projectName;

	public Project() {
	}

	// Getter & Setter
	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
}