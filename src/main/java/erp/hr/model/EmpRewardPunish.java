package erp.hr.model;

import java.util.Date;

// EMP_REWARD_PUNISH: 상벌
public class EmpRewardPunish {
	private int rpId;
	private int empId;
	private String rpType;
	private String rpName;
	private String rpAuthority;
	private Date rpDate;
	private String rpContent;
	private String note;

	// 기본 생성자
	public EmpRewardPunish() {
	}

	public int getRpId() {
		return rpId;
	}

	public void setRpId(int rpId) {
		this.rpId = rpId;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
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