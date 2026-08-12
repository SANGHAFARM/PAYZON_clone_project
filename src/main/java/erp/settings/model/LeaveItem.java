package erp.settings.model;

import java.util.Date;

/**
 * [기본환경설정] 휴가/근태 설정 - 휴가항목 설정 Model DB 테이블: LEAVE_ITEM
 */
public class LeaveItem {

	private int leaveItemId; // 휴가항목 식별 번호 (PK)
	private String itemName;
	private Date applyStartDate;
	private Date applyEndDate;
	private String useYn;

	public LeaveItem() {
	}

	// Getter & Setter
	public int getLeaveItemId() {
		return leaveItemId;
	}

	public void setLeaveItemId(int leaveItemId) {
		this.leaveItemId = leaveItemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Date getApplyStartDate() {
		return applyStartDate;
	}

	public void setApplyStartDate(Date applyStartDate) {
		this.applyStartDate = applyStartDate;
	}

	public Date getApplyEndDate() {
		return applyEndDate;
	}

	public void setApplyEndDate(Date applyEndDate) {
		this.applyEndDate = applyEndDate;
	}

	public String getUseYn() {
		return useYn;
	}

	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
}