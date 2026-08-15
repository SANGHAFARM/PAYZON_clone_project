package erp.settings.command;

import java.text.SimpleDateFormat;
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

public class LeaveItemHandler implements CommandHandler {

	// 서비스 객체 할당 처리
	private AttendanceSettingService settingService = AttendanceSettingService.getInstance();
	private EmployeeLeaveBalanceService leaveBalanceService = EmployeeLeaveBalanceService.getInstance();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		if (req.getMethod().equalsIgnoreCase("GET")) {
			return processForm(req, res);
		} else if (req.getMethod().equalsIgnoreCase("POST")) {
			return processAction(req, res);
		}
		return null;
	}

	// [GET] 휴가/근태 설정 페이지 전체 화면 렌더링 처리
	private String processForm(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// 마스터 데이터 리스트 일괄 조회
		List<LeaveItem> leaveItems = settingService.getLeaveItems();
		List<AttendanceItem> attendItems = settingService.getAttendItems();
		List<AttendanceGroup> attendGroups = settingService.getAttendGroups();

		req.setAttribute("leaveItems", leaveItems);
		req.setAttribute("attendItems", attendItems);
		req.setAttribute("attendGroups", attendGroups);

		// 선택된 휴가항목 파라미터 확인 및 단건 조회
		String leaveItemIdStr = req.getParameter("leaveItemId");
		if (leaveItemIdStr != null && !leaveItemIdStr.isEmpty()) {
			int leaveItemId = Integer.parseInt(leaveItemIdStr);
			LeaveItem selectedLeaveItem = settingService.getLeaveItem(leaveItemId);
			req.setAttribute("selectedLeaveItem", selectedLeaveItem);

			// 선택된 휴가항목이 있을 경우 사원별 휴가일수 목록 동시 조회 (모달용)
			String keyword = req.getParameter("keyword");
			String status = req.getParameter("status");
			List<EmployeeLeaveRow> employeeLeaveRows = leaveBalanceService.getEmployeeLeaveRows(leaveItemId, keyword,
					status);
			req.setAttribute("employeeLeaveRows", employeeLeaveRows);
		}

		// 선택된 근태항목 파라미터 확인 및 단건 조회
		String attendItemIdStr = req.getParameter("attendItemId");
		if (attendItemIdStr != null && !attendItemIdStr.isEmpty()) {
			int attendItemId = Integer.parseInt(attendItemIdStr);
			AttendanceItem selectedAttendItem = settingService.getAttendItem(attendItemId);
			req.setAttribute("selectedAttendItem", selectedAttendItem);
		}

		// JSP 뷰 포워딩 처리
		return "/WEB-INF/view/settings/attendance-settings.jsp";
	}

	// [POST] 휴가항목 추가/수정/삭제 액션 분기 처리
	private String processAction(HttpServletRequest req, HttpServletResponse res) throws Exception {
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");

		try {
			// 파라미터 파싱 및 DTO 세팅
			LeaveItem item = new LeaveItem();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			String idStr = req.getParameter("leaveItemId");
			if (idStr != null && !idStr.isEmpty()) {
				item.setLeaveItemId(Integer.parseInt(idStr));
			}

			item.setItemName(req.getParameter("itemName"));
			item.setUseYn(req.getParameter("useYn"));

			String startDate = req.getParameter("applyStartDate");
			if (startDate != null && !startDate.isEmpty()) {
				item.setApplyStartDate(sdf.parse(startDate));
			}

			String endDate = req.getParameter("applyEndDate");
			if (endDate != null && !endDate.isEmpty()) {
				item.setApplyEndDate(sdf.parse(endDate));
			}

			// 내용 지우기 액션 외에는 서비스 로직 호출
			if (!"clear".equals(action)) {
				settingService.processLeaveItemAction(item, action);
				req.getSession().setAttribute("message", "휴가항목 설정이 완료되었습니다.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			req.getSession().setAttribute("message", "오류 발생: " + e.getMessage());
		}

		// PRG 패턴 적용 리다이렉트 처리
		res.sendRedirect(req.getContextPath() + "/settings/attendance.do");
		return null;
	}
}