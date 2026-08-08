package erp.hr.model;

import java.util.Date;

// EMP_LANGUAGE: 어학
public class EmpLanguage {
	private int langId;
	private int empId;
	private String langName;
	private String testName;
	private String score;
	private Date acqDate;
	private String readingLevel;
	private String writingLevel;
	private String speakingLevel;

	// 기본 생성자
	public EmpLanguage() {
	}

	public int getLangId() {
		return langId;
	}

	public void setLangId(int langId) {
		this.langId = langId;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
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