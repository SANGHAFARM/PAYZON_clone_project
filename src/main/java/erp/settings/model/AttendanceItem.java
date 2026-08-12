package erp.settings.model;

/**
 * [기본환경설정] 휴가/근태 설정 - 근태항목 설정 Model DB 테이블: ATTENDANCE_ITEM
 */
public class AttendanceItem {

	private Long attendanceItemId;
	private String attendName;
	private String unitType;
	private Long attendanceGroupId; // 근태그룹 외래키

	private Long deductLeaveId; // 휴가공제 외래키 (null 허용)
	private String workHourType; // 근로시간연계 (null 허용)
	private String useYn;

	public AttendanceItem() {
	}

	// 비즈니스 로직(예시): 휴가 차감 연동 여부 확인
	public boolean isLeaveDeductible() {
		return this.deductLeaveId != null && this.deductLeaveId > 0;
	}

	// Getter & Setter
	public Long getAttendanceItemId() {
		return attendanceItemId;
	}

	public void setAttendanceItemId(Long attendanceItemId) {
		this.attendanceItemId = attendanceItemId;
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

	public Long getAttendanceGroupId() {
		return attendanceGroupId;
	}

	public void setAttendanceGroupId(Long attendanceGroupId) {
		this.attendanceGroupId = attendanceGroupId;
	}

	public Long getDeductLeaveId() {
		return deductLeaveId;
	}

	public void setDeductLeaveId(Long deductLeaveId) {
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