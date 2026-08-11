package erp.settings.model;

// PROJECT: 현장/프로젝트 목록
public class Project {
	
	// 기본 생성자
	public Project() {
	}
	
	private int projectId;
	private String projectName;
	
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