package erp.employees.model;

import java.util.Date;

/**
 * [인사/사원관리] 사원 외국어 능력 이력 Model DB 테이블: EMPLOYEE_LANGUAGE
 */
public class EmployeeLanguage {

	// [DB 관리 항목]
	private int employeeLanguageId; // 어학능력 식별 번호 (PK)
	private int employeeId; // 사원 식별 번호 (FK)

	// [어학능력 입력 항목]
	private String langName; // 외국어명
	private String testName; // 시험명
	private String score; // 공인점수
	private Date acqDate; // 취득일
	private String readingLevel; // 독해 능력 수준
	private String writingLevel; // 작문 능력 수준
	private String speakingLevel; // 회화 능력 수준

	public EmployeeLanguage() {
	}

	// Getter & Setter
	public int getEmployeeLanguageId() {
		return employeeLanguageId;
	}

	public void setEmployeeLanguageId(int employeeLanguageId) {
		this.employeeLanguageId = employeeLanguageId;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getLangName() {
		return langName;
	}

	public void setLangName(String langName) {
		this.langName = langName;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public Date getAcqDate() {
		return acqDate;
	}

	public void setAcqDate(Date acqDate) {
		this.acqDate = acqDate;
	}

	public String getReadingLevel() {
		return readingLevel;
	}

	public void setReadingLevel(String readingLevel) {
		this.readingLevel = readingLevel;
	}

	public String getWritingLevel() {
		return writingLevel;
	}

	public void setWritingLevel(String writingLevel) {
		this.writingLevel = writingLevel;
	}

	public String getSpeakingLevel() {
		return speakingLevel;
	}

	public void setSpeakingLevel(String speakingLevel) {
		this.speakingLevel = speakingLevel;
	}
}