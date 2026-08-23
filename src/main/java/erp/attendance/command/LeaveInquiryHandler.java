package erp.attendance.command;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.attendance.dto.AttendanceRecordDto;
import erp.attendance.dto.LeaveInquiryDto;
import erp.attendance.service.LeaveInquiryService;
import erp.attendance.service.LeaveRecordInquiryService;
import erp.attendance.service.ListLeaveInquiryService;
import erp.attendance.service.page.LeaveInquiryPage;
import erp.attendance.service.request.LeaveInquiryRequest;
import erp.settings.model.LeaveItem;
import erp.settings.service.AttendanceSettingService;
import erp.settings.service.DepartmentPositionService;
import mvc.command.CommandHandler;

// 휴가조회 화면의 HTTP 요청을 구분하고 적절한 서비스 호출과 화면 이동을 담당한다.
// 休暇照会画面のHTTPリクエストを判別し、適切なサービス呼び出しと画面遷移を担当する。
public class LeaveInquiryHandler implements CommandHandler {
	final String FORM_VIEW = "/WEB-INF/view/attendance/leave-inquiry.jsp";
	AttendanceSettingService attendanceSettingService = AttendanceSettingService.getInstance();
	DepartmentPositionService departmentPositionService = DepartmentPositionService.getInstance();
	LeaveInquiryService leaveInquiryService = new LeaveInquiryService();
	LeaveRecordInquiryService leaveRecordInquiryService = new LeaveRecordInquiryService();
	
	@Override
	// 요청 방식과 작업 구분을 확인하여 휴가조회 조회·저장 작업을 적절한 처리로 연결한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエスト方式と処理区分を確認し、休暇照会の照会・保存処理へ適切に振り分ける。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		if (req.getMethod().equalsIgnoreCase("GET")) {
			return processForm(req, res);
		} else {
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
	}

	// 휴가조회 화면에 필요한 데이터를 조회하여 request에 저장하고 JSP 경로를 반환한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// 休暇照会画面に必要なデータを照会してrequestへ保存し、JSPパスを返す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	private String processForm(HttpServletRequest req, HttpServletResponse res) throws Exception {
		//현재 사용할 수 있는 휴가항목만 조회
		//現在使える休暇項目だけ照会
		List<LeaveItem> leaveItems = attendanceSettingService.getUsableLeaveItems();
		req.setAttribute("leaveItems", leaveItems);
		List<String> empTypes = new ArrayList<>();
		empTypes.add("정규직");
		empTypes.add("계약직");
		empTypes.add("임시직");
		empTypes.add("파견직");
		empTypes.add("위촉직");
		empTypes.add("일용직");
		req.setAttribute("empTypes", empTypes);
		req.setAttribute("departments", departmentPositionService.getDepartmentOptions());
		req.setAttribute("jobPositions", departmentPositionService.getJobPositionOptions());

		String empType = req.getParameter("empType");
		String keyword = req.getParameter("keyword");
		String status = req.getParameter("status");
		if (status == null) {
		    status = "재직";
		}
		String leaveItemIdStr = req.getParameter("leaveItemId");
		int leaveItemId = 0;
		if (leaveItemIdStr != null && !leaveItemIdStr.trim().isEmpty()) {
			leaveItemId = Integer.parseInt(leaveItemIdStr);
		} else {
			leaveItemId = leaveItems.get(0).getLeaveItemId();
		}
		String departmentIdStr = req.getParameter("departmentId");
		String jobPositionIdstr = req.getParameter("jobPositionId");
		Integer departmentId = (departmentIdStr != null && !departmentIdStr.trim().isEmpty())
				? Integer.parseInt(departmentIdStr)
				: null;
		Integer jobPositionId = (jobPositionIdstr != null && !jobPositionIdstr.trim().isEmpty())
				? Integer.parseInt(jobPositionIdstr)
				: null;
		String pageStr = req.getParameter("page");
		int pageNum = 1;
		if (pageStr!=null&&!pageStr.trim().isEmpty()) {
			pageNum = Integer.parseInt(pageStr);
		}
		
		String pageSizeStr = req.getParameter("pageSize");
		int pageSize = 30;
		if (pageSizeStr!=null&&!pageSizeStr.trim().isEmpty()) {
			pageSize = Integer.parseInt(pageSizeStr);
		}
		req.setAttribute("pageSize", pageSize);
		
		LeaveInquiryRequest request = new LeaveInquiryRequest(leaveItemId, keyword, status, empType, departmentId,
				jobPositionId, pageSize);
		ListLeaveInquiryService listLeaveInquiryService = new ListLeaveInquiryService();
		LeaveInquiryPage leaveInquiryPage = listLeaveInquiryService.getInquiryPage(pageNum, request);
		req.setAttribute("leaveInquiryPage", leaveInquiryPage);
		req.setAttribute("empType", empType);
		req.setAttribute("keyword", keyword);
		req.setAttribute("status", status);
		req.setAttribute("leaveItemId", leaveItemId);
		req.setAttribute("departmentId", departmentId);
		req.setAttribute("jobPositionId", jobPositionId);

		// 사원별 휴가현황 모달용 - employeeId 파라미터가 있을 때만 처리
		// リクエストから入力値を取得し、空白除去と形式変換を行って業務処理へ渡す。
		String employeeIdStr = req.getParameter("employeeId");
		if (employeeIdStr != null && !employeeIdStr.trim().isEmpty()) {
			int employeeId = Integer.parseInt(employeeIdStr);
			req.setAttribute("employeeId", employeeId);

			// leaveEmployees 목록에서 해당 사원 찾기
			// 社員の識別情報と在職・雇用・所属条件を確認し、対象社員データへ反映する。
			LeaveInquiryDto selectedEmployee = null;
			for (LeaveInquiryDto dto : leaveInquiryPage.getContent()) {
				if (dto.getEmployeeId() == employeeId) {
					selectedEmployee = dto;
					break;
				}
			}
			req.setAttribute("selectedEmployee", selectedEmployee);

			// 상세 사용내역 조회
			// 選択社員と休暇項目に該当する使用履歴を照会し、詳細一覧として画面へ渡す。
			List<AttendanceRecordDto> leaveRecords = leaveRecordInquiryService.getLeaveRecords(employeeId, leaveItemId);
			req.setAttribute("leaveRecords", leaveRecords);
		}

		return FORM_VIEW;
	}
}
