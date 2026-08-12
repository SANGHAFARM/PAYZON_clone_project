package erp.attendance.model;

// ATTEND_GROUP: 근태그룹 관리
public class AttendGroup {
	private int attendGroupId;
	private String groupName;

	// 기본 생성자
	public AttendGroup() {
	}

	public int getAttendGroupId() {
		return attendGroupId;
	}

	public void setAttendGroupId(int attendGroupId) {
		this.attendGroupId = attendGroupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
}