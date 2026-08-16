package erp.settings.model;

/**
 * [기본환경설정] 현장/프로젝트 목록 Model DB 테이블: PROJECT
 */
public class Project {

	private int projectId; // 현장/프로젝트 식별 번호 (PK)
	private String projectName;

	public Project() {
	}

	public Project(int projectId, String projectName) {
		super();
		this.projectId = projectId;
		this.projectName = projectName;
	}

	// Getter & Setter
	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
}