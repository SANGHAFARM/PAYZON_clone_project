package erp.settings.dto;

import java.util.List;

/**
 * [기본환경설정] 근태그룹과 그에 속한 근태항목 목록을 계층형으로 담는 DTO 근태 설정 화면의 트리(Tree) 구조 UI 렌더링용
 */
public class AttendanceGroupWithItemsDto {

	private int attendanceGroupId; // 근태그룹 식별 번호 (PK)
	private String groupName; // 근태그룹명

	// 1대 다(1:N) 관계로 그룹에 소속된 근태항목들의 리스트
	private List<AttendanceItemResponseDto> items;

	public AttendanceGroupWithItemsDto() {
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

	public List<AttendanceItemResponseDto> getItems() {
		return items;
	}

	public void setItems(List<AttendanceItemResponseDto> items) {
		this.items = items;
	}
}