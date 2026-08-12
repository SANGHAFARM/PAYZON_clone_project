package erp.employees.model;

import java.util.Date;

/**
 * [인사/사원관리] 사원 상훈 및 징계(상벌) 이력 Model DB 테이블: EMPLOYEE_REWARD_DISCIPLINE
 */
public class EmployeeRewardDiscipline {

	// [DB 관리 항목]
	private Long employeeRewardDisciplineId; // 상벌 이력 식별 번호 (PK)
	private Long employeeId; // 사원 식별 번호 (FK)

	// [상벌 입력 항목]
	private String rpType; // 구분 (상/벌)
	private String rpName; // 상벌명
	private String rpAuthority; // 상벌권자
	private Date rpDate; // 상벌일자
	private String rpContent; // 상벌내용
	private String note; // 비고

	public EmployeeRewardDiscipline() {
	}

	// Getter & Setter
	public Long getEmployeeRewardDisciplineId() {
		return employeeRewardDisciplineId;
	}

	public void setEmployeeRewardDisciplineId(Long employeeRewardDisciplineId) {
		this.employeeRewardDisciplineId = employeeRewardDisciplineId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getRpType() {
		return rpType;
	}

	public void setRpType(String rpType) {
		this.rpType = rpType;
	}

	public String getRpName() {
		return rpName;
	}

	public void setRpName(String rpName) {
		this.rpName = rpName;
	}

	public String getRpAuthority() {
		return rpAuthority;
	}

	public void setRpAuthority(String rpAuthority) {
		this.rpAuthority = rpAuthority;
	}

	public Date getRpDate() {
		return rpDate;
	}

	public void setRpDate(Date rpDate) {
		this.rpDate = rpDate;
	}

	public String getRpContent() {
		return rpContent;
	}

	public void setRpContent(String rpContent) {
		this.rpContent = rpContent;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}