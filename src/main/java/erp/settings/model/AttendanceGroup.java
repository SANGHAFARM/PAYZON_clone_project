package erp.settings.model;

/**
 * [기본환경설정] 휴가/근태 설정 - 근태그룹 설정 Model DB 테이블: ATTENDANCE_GROUP
 */
public class AttendanceGroup {

	private int attendanceGroupId; // 근태그룹 식별 번호 (PK)
	private String groupName;

	public AttendanceGroup() {
	}

	// Getter & Setter
	public int getAttendanceGroupId() {
		return attendanceGroupId;
	}

	public void setAttendanceGroupId(int attendanceGroupId) {
		this.attendanceGroupId = attendanceGroupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
}