package erp.settings.command;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.attendance.dto.EmployeeLeaveRow;
import erp.attendance.service.EmployeeLeaveBalanceService;
import erp.settings.model.AttendanceGroup;
import erp.settings.model.AttendanceItem;
import erp.settings.model.LeaveItem;
import erp.settings.service.AttendanceSettingService;
import mvc.command.CommandHandler;

// 근태설정 화면의 HTTP 요청을 구분하고 적절한 서비스 호출과 화면 이동을 담당한다.
// 勤怠設定画面のHTTPリクエストを判別し、適切なサービス呼び出しと画面遷移を担当する。
public class AttendanceSettingsHandler implements CommandHandler {

	private AttendanceSettingService settingService = AttendanceSettingService.getInstance();
	private EmployeeLeaveBalanceService leaveBalanceService = EmployeeLeaveBalanceService.getInstance();

	@Override
	// 요청 방식과 작업 구분을 확인하여 근태설정 조회·저장 작업을 적절한 처리로 연결한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエスト方式と処理区分を確認し、勤怠設定の照会・保存処理へ適切に振り分ける。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// 이 핸들러는 화면을 보여주는 용도(GET)로만 작동
		// HTTPメソッドと処理区分を確認し、照会またはデータ変更に対応する処理へ分岐する。
		if ("true".equals(req.getParameter("cancelEmployeeLeaveDelete"))) {
			req.getSession().removeAttribute("employeeLeaveDeleteIds");
			req.getSession().removeAttribute("employeeLeaveDeleteCount");
		}

		// 1. 마스터 데이터 리스트 일괄 조회
		// 対象一覧を順番に処理し、各行の入力値または照会結果を同じ基準で構成する。
		List<LeaveItem> leaveItems = settingService.getLeaveItems();
		List<AttendanceItem> attendItems = settingService.getAttendItems();
		List<AttendanceGroup> attendGroups = settingService.getAttendGroups();

		req.setAttribute("leaveItems", leaveItems);
		req.setAttribute("attendItems", attendItems);
		req.setAttribute("attendGroups", attendGroups);

		// 삭제 요청은 실제 삭제 전에 확인 팝업에 필요한 정보만 구성한다.
		// 入力条件と必須値を検証し、不正なデータが後続処理へ渡らないようにする。
		String deleteType = req.getParameter("deleteType");
		String deleteIdStr = req.getParameter("deleteId");
		if (deleteType != null && deleteIdStr != null && !deleteIdStr.isEmpty()) {
			int deleteId = Integer.parseInt(deleteIdStr);
			req.setAttribute("deleteSettingType", deleteType);
			req.setAttribute("deleteSettingId", deleteId);

			if ("leave".equals(deleteType)) {
				LeaveItem item = settingService.getLeaveItem(deleteId);
				req.setAttribute("deleteSettingName", item == null ? "휴가항목" : item.getItemName());
				req.setAttribute("deleteActionUrl", "/settings/leave-item.do");
				req.setAttribute("deleteReturnHash", "#leave-settings");
			} else if ("attendance".equals(deleteType)) {
				AttendanceItem item = settingService.getAttendItem(deleteId);
				req.setAttribute("deleteSettingName", item == null ? "근태항목" : item.getAttendName());
				req.setAttribute("deleteActionUrl", "/settings/attend-item.do");
				req.setAttribute("deleteReturnHash", "#attendance-item-settings");
			} else if ("group".equals(deleteType)) {
				String groupName = "근태그룹";
				for (AttendanceGroup group : attendGroups) {
					if (group.getAttendanceGroupId() == deleteId) {
						groupName = group.getGroupName();
						break;
					}
				}
				req.setAttribute("deleteSettingName", groupName);
				req.setAttribute("deleteActionUrl", "/settings/attend-group.do");
				req.setAttribute("deleteReturnHash", "#attend-group-modal");
			}
		}

		// 2. 선택된 휴가항목 단건 조회 (수정 폼용)
		// 識別された既存データへ変更値を反映し、未変更項目は従来の値を維持する。
		String leaveItemIdStr = req.getParameter("leaveItemId");
		if (leaveItemIdStr != null && !leaveItemIdStr.isEmpty()) {
			int leaveItemId = Integer.parseInt(leaveItemIdStr);
			LeaveItem selectedLeaveItem = settingService.getLeaveItem(leaveItemId);
			req.setAttribute("selectedLeaveItem", selectedLeaveItem);

			// 모달용 사원별 휴가일수 목록 동시 조회
			// 社員の識別情報と在職・雇用・所属条件を確認し、対象社員データへ反映する。
			String keyword = req.getParameter("keyword");
			String status = req.getParameter("status");
			List<EmployeeLeaveRow> employeeLeaveRows = leaveBalanceService.getEmployeeLeaveRows(leaveItemId, keyword,
					status);
			req.setAttribute("employeeLeaveRows", employeeLeaveRows);
		}

		// 3. 선택된 근태항목 단건 조회 (수정 폼용)
		// 識別された既存データへ変更値を反映し、未変更項目は従来の値を維持する。
		String attendItemIdStr = req.getParameter("attendItemId");
		if (attendItemIdStr != null && !attendItemIdStr.isEmpty()) {
			int attendItemId = Integer.parseInt(attendItemIdStr);
			AttendanceItem selectedAttendItem = settingService.getAttendItem(attendItemId);
			req.setAttribute("selectedAttendItem", selectedAttendItem);
		}

		// 4. JSP 뷰 포워딩
		// 画面表示に必要なデータとメッセージをrequestまたはsessionへ保存し、JSPへ引き渡す。
		return "/WEB-INF/view/settings/attendance-settings.jsp";
	}
}
