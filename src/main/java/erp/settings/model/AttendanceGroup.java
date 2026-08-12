package erp.settings.model;

/**
 * [기본환경설정] 휴가/근태 설정 - 근태항목 설정 (근태그룹) Model DB 테이블: ATTENDANCE_GROUP
 */
public class AttendanceGroup {

	private Long attendanceGroupId;
	private String groupName;

	public AttendanceGroup() {
	}

	// Getter & Setter
	public Long getAttendanceGroupId() {
		return attendanceGroupId;
	}

	public void setAttendanceGroupId(Long attendanceGroupId) {
		this.attendanceGroupId = attendanceGroupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
}