package erp.settings.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.settings.model.AttendanceGroup;
import erp.settings.service.AttendanceSettingService;
import mvc.command.CommandHandler;

// 근태그룹 화면의 HTTP 요청을 구분하고 적절한 서비스 호출과 화면 이동을 담당한다.
// 勤怠グループ画面のHTTPリクエストを判別し、適切なサービス呼び出しと画面遷移を担当する。
public class AttendanceGroupHandler implements CommandHandler {

	// 설정 서비스 객체 할당
	// 担当業務を処理するServiceまたはDAOを共有フィールドへ割り当て、各処理から再利用する。
	private AttendanceSettingService settingService = AttendanceSettingService.getInstance();

	@Override
	// 요청 방식과 작업 구분을 확인하여 근태그룹 조회·저장 작업을 적절한 처리로 연결한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエスト方式と処理区分を確認し、勤怠グループの照会・保存処理へ適切に振り分ける。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		if (req.getMethod().equalsIgnoreCase("POST")) {
			return processAction(req, res);
		}
		return null;
	}

	// [POST] 근태그룹 추가 및 리스트 개별 아이템 액션 파싱 처리
	// 요청에서 작업구분 작업에 필요한 값을 읽고 검증한 뒤 해당 서비스 처리를 호출한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエストから処理区分処理に必要な値を取得・検証し、該当するサービス処理を呼び出す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
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
				// 入力された内容を新しい業務データとして構成し、関連データとともに登録する。
				group.setGroupName(req.getParameter("newGroupName"));
				settingService.processAttendGroupAction(group, "insert");
			} else if ("confirmDelete".equals(action)) {
				group.setAttendanceGroupId(Integer.parseInt(req.getParameter("deleteId")));
				settingService.processAttendGroupAction(group, "delete");
				req.getSession().setAttribute("message", "근태그룹이 삭제되었습니다.");
				req.getSession().setAttribute("messageReturnTarget", "group");
				
			} else if (action != null && action.contains(":")) {
				// 기존 목록의 개별 수정/삭제 로직 파싱 처리
				// 識別された既存データへ変更値を反映し、未変更項目は従来の値を維持する。
				String[] parts = action.split(":");
				String realAction = parts[0]; // "update" 또는 "delete"
				int groupId = Integer.parseInt(parts[1]); // 식별 번호 추출
				
				group.setAttendanceGroupId(groupId);
				
				if ("update".equals(realAction)) {
					// 화면에 배열로 넘어온 groupNames와 groupIds 중 일치하는 값 탐색 후 갱신
					// 画面表示に必要なデータとメッセージをrequestまたはsessionへ保存し、JSPへ引き渡す。
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
