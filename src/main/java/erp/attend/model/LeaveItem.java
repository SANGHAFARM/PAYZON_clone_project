package erp.attend.model;

import java.util.Date;

// LEAVE_ITEM: 휴가항목 설정
public class LeaveItem {
	private int leaveItemId;
	private String itemName;
	private Date applyStartDate;
	private Date applyEndDate;
	private String useYn;

	// 기본 생성사
	public LeaveItem() {
	}

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