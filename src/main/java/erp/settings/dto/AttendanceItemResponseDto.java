package erp.settings.dto;

/**
 * [기본환경설정] 근태항목 상세 정보 및 그룹명·휴가공제명 포함 응답 DTO 근태 설정 화면 관리 그리드용
 */
public class AttendanceItemResponseDto {

	// [ATTENDANCE_ITEM 기본 정보]
	private int attendanceItemId; // 근태항목 식별 번호 (PK)
	private int attendanceGroupId; // 근태그룹 외래키
	private String attendName; // 근태항목명
	private String unitType; // 단위구분
	private Integer deductLeaveId; // 차감 연동 휴가항목 ID (외래키)
	private String workHourType; // 근로시간연계 유형
	private String useYn; // 사용여부 (Y/N)

	// [조인(Join)을 통해 가져오는 연관 명칭 데이터]
	private String groupName; // 소속된 근태그룹명 (예: 사무직군, 현장직군)
	private String deductLeaveName; // 차감 연동된 휴가항목명 (예: 연차, 보건휴가)

	public AttendanceItemResponseDto() {
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

	public String getDeductLeaveName() {
		return deductLeaveName;
	}

	public void setDeductLeaveName(String deductLeaveName) {
		this.deductLeaveName = deductLeaveName;
	}
}