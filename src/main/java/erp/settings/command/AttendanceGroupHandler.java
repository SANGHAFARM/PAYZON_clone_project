package erp.settings.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.settings.model.AttendanceGroup;
import erp.settings.service.AttendanceSettingService;
import mvc.command.CommandHandler;

public class AttendanceGroupHandler implements CommandHandler {

	// 설정 서비스 객체 할당
	private AttendanceSettingService settingService = AttendanceSettingService.getInstance();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		if (req.getMethod().equalsIgnoreCase("POST")) {
			return processAction(req, res);
		}
		return null;
	}

	// [POST] 근태그룹 추가 및 리스트 개별 아이템 액션 파싱 처리
	private String processAction(HttpServletRequest req, HttpServletResponse res) throws Exception {
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action"); // 예: "insert" 또는 "update:5"

		if (action != null && action.startsWith("requestDelete:")) {
			String groupId = action.substring(action.indexOf(':') + 1);
			res.sendRedirect(req.getContextPath()
					+ "/settings/attendance.do?deleteType=group&deleteId=" + groupId + "#attend-group-modal");
			return null;
		}

		try {
			AttendanceGroup group = new AttendanceGroup();

			if ("insert".equals(action)) {
				// 신규 등록 로직 (하단 input 사용)
				group.setGroupName(req.getParameter("newGroupName"));
				settingService.processAttendGroupAction(group, "insert");
			} else if ("confirmDelete".equals(action)) {
				group.setAttendanceGroupId(Integer.parseInt(req.getParameter("deleteId")));
				settingService.processAttendGroupAction(group, "delete");
				req.getSession().setAttribute("message", "근태그룹이 삭제되었습니다.");
				req.getSession().setAttribute("messageReturnTarget", "group");
				
			} else if (action != null && action.contains(":")) {
				// 기존 목록의 개별 수정/삭제 로직 파싱 처리
				String[] parts = action.split(":");
				String realAction = parts[0]; // "update" 또는 "delete"
				int groupId = Integer.parseInt(parts[1]); // 식별 번호 추출
				
				group.setAttendanceGroupId(groupId);
				
				if ("update".equals(realAction)) {
					// 화면에 배열로 넘어온 groupNames와 groupIds 중 일치하는 값 탐색 후 갱신
					String[] groupIds = req.getParameterValues("groupIds");
					String[] groupNames = req.getParameterValues("groupNames");
					
					for (int i = 0; i < groupIds.length; i++) {
						if (Integer.parseInt(groupIds[i]) == groupId) {
							group.setGroupName(groupNames[i]);
							break;
						}
					}
				}
				
				settingService.processAttendGroupAction(group, realAction);
				req.getSession().setAttribute("message", "근태그룹이 수정되었습니다.");
				req.getSession().setAttribute("messageReturnTarget", "group");
			}

		} catch (Exception e) {
			e.printStackTrace();
			req.getSession().setAttribute("message", "오류 발생: " + e.getMessage());
			req.getSession().setAttribute("messageReturnTarget", "group");
		}

		res.sendRedirect(req.getContextPath() + "/settings/attendance.do#attend-group-modal");
		return null;
	}
}
