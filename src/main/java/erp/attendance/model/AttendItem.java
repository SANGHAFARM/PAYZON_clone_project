package erp.attendance.model;

// ATTEND_ITEM: 근태항목 설정
public class AttendItem {
	private int attendItemId;
	private String attendName;
	private String unitType;
	private int attendGroupId;
	private Integer deductLeaveId;
	private String workHourType;
	private String useYn;

	// 기본 생성자
	public AttendItem() {
	}

	public int getAttendItemId() {
		return attendItemId;
	}

	public void setAttendItemId(int attendItemId) {
		this.attendItemId = attendItemId;
	}

	public String getAttendName() {
		return attendName;
	}

	public void setAttendName(String attendName) {
		this.attendName = attendName;
	}

	public String getUnitType() {
		return unitType;
	}

	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}

	public int getAttendGroupId() {
		return attendGroupId;
	}

	public void setAttendGroupId(int attendGroupId) {
		this.attendGroupId = attendGroupId;
	}

	public Integer getDeductLeaveId() {
		return deductLeaveId;
	}

	public void setDeductLeaveId(Integer deductLeaveId) {
		this.deductLeaveId = deductLeaveId;
	}

	public String getWorkHourType() {
		return workHourType;
	}

	public void setWorkHourType(String workHourType) {
		this.workHourType = workHourType;
	}

	public String getUseYn() {
		return useYn;
	}

	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
}