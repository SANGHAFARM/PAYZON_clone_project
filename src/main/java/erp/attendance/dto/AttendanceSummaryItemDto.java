package erp.attendance.dto;

// 근태항목별 합계, 표시단위, 휴가공제 여부를 담는 모델
// 勤怠項目別合計、表示単位、休暇控除の有無を保持するモデル
public class AttendanceSummaryItemDto {
	private String attendName;
	private double totalValue;
	private String unitType;
	private boolean leaveDeduct;

	public AttendanceSummaryItemDto() {
	}

	public AttendanceSummaryItemDto(String attendName, double totalValue, String unitType, boolean leaveDeduct) {
		this.attendName = attendName;
		this.totalValue = totalValue;
		this.unitType = unitType;
		this.leaveDeduct = leaveDeduct;
	}

	public String getAttendName() { return attendName; }
	public void setAttendName(String attendName) { this.attendName = attendName; }
	public double getTotalValue() { return totalValue; }
	public void setTotalValue(double totalValue) { this.totalValue = totalValue; }
	public String getUnitType() { return unitType; }
	public void setUnitType(String unitType) { this.unitType = unitType; }
	public boolean isLeaveDeduct() { return leaveDeduct; }
	public void setLeaveDeduct(boolean leaveDeduct) { this.leaveDeduct = leaveDeduct; }
}