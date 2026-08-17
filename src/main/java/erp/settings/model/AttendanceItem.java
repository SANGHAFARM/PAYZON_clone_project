package erp.settings.model;

// [기본환경설정] 휴가/근태 설정 - 근태항목 설정 Model DB 테이블: ATTENDANCE_ITEM
public class AttendanceItem {

	private int attendanceItemId; // 근태항목 식별 번호 (PK)
	private int attendanceGroupId; // 근태그룹 외래키 (FK)
	private String attendName;
	private String unitType;
	private String useYn;

	private Integer deductLeaveId; // 휴가공제 외래키 (선택적 FK)
	private String workHourType; // 근로시간연계

	private String groupName;
	private String leaveName;

	public AttendanceItem() {
	}

	// 비즈니스 로직(예시): 휴가 차감 연동 여부 확인
	public boolean isLeaveDeductible() {
		return this.deductLeaveId != null && this.deductLeaveId > 0;
	}

	// Getter & Setter
	public int getAttendanceItemId() {
		return attendanceItemId;
	}

	public void setAttendanceItemId(int attendanceItemId) {
		this.attendanceItemId = attendanceItemId;
	}

	public int getAttendanceGroupId() {
		return attendanceGroupId;
	}

	public void setAttendanceGroupId(int attendanceGroupId) {
		this.attendanceGroupId = attendanceGroupId;
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

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getLeaveName() {
		return leaveName;
	}

	public void setLeaveName(String leaveName) {
		this.leaveName = leaveName;
	}
}