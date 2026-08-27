package erp.attendance.command;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.attendance.dto.AttendanceEmployeeRecordDto;
import erp.attendance.dto.LeaveInquiryDto;
import erp.attendance.service.LeaveEmployeeListService;
import erp.attendance.service.LeaveInquiryPageService;
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
	// 근태 관련 서비스
	// 勤怠関連サービス
	private AttendanceSettingService attendanceSettingService = AttendanceSettingService.getInstance();
	// 부서, 직위 관련 서비스
	// 部署、役職関連サービス
	private DepartmentPositionService departmentPositionService = DepartmentPositionService.getInstance();
	// 사원별 휴가 기록 서비스
	// 社員別休暇記録サービス
	private LeaveEmployeeListService leaveRecordInquiryService = new LeaveEmployeeListService();
	// 휴가 현황 조회 페이지 서비스
	// 休暇状況照会ページサービス
	LeaveInquiryPageService listLeaveInquiryService = new LeaveInquiryPageService();

	@Override
	// 요청 방식과 작업 구분을 확인하여 휴가조회 조회·저장 작업을 적절한 처리로 연결한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트
	// 경로를 결정한다.
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
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트
	// 경로를 결정한다.
	// 休暇照会画面に必要なデータを照会してrequestへ保存し、JSPパスを返す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	private String processForm(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// 1. 검색란 설정
		// 1. 検索欄設定
		// 현재 사용가능한 휴가 목록 조회 후 request 영역에 저장
		// 現在使える休暇目録を照会し、リクエストスコープに保存
		List<LeaveItem> leaveItems = attendanceSettingService.getUsableLeaveItems();
		req.setAttribute("leaveItems", leaveItems);
		// 사원 구분 리스트를 생성 후 request 영역에 저장
		// 社員区分リストを生成し、リクエストスコープに保存
		List<String> empTypes = new ArrayList<>();
		empTypes.add("정규직");
		empTypes.add("계약직");
		empTypes.add("임시직");
		empTypes.add("파견직");
		empTypes.add("위촉직");
		empTypes.add("일용직");
		req.setAttribute("empTypes", empTypes);
		// 부서와 직위 목록 조회 후 request 영역에 저장
		// 部署と役職目録を照会し、リクエストスコープに保存
		req.setAttribute("departments", departmentPositionService.getDepartmentOptions());
		req.setAttribute("jobPositions", departmentPositionService.getJobPositionOptions());

		// 2. 파라미터 분석
		// 2. パラメータ分析
		String empType = req.getParameter("empType"); // 구분(区分)
		String keyword = req.getParameter("keyword"); // 검색 키워드(検索キーワード)
		String status = req.getParameter("status"); // 재직 상태(在職ステータス)
		String leaveItemIdStr = req.getParameter("leaveItemId"); // 휴가항목ID(休暇項目ID)
		String departmentIdStr = req.getParameter("departmentId"); // 부서ID(部署ID)
		String jobPositionIdstr = req.getParameter("jobPositionId"); // 직위ID(役職ID)
		String pageStr = req.getParameter("page"); // 페이지 번호(ページ番号)
		String pageSizeStr = req.getParameter("pageSize"); // 페이지 사이즈(ページサイズ)
		// 재직 상태 파라미터가 null일 시, 기본값을 "재직"으로 설정
		// 在職ステータスパラメータがnullの場合、デフォルト値を「在職」に設定
		if (status == null) {
			status = "재직";
		}
		int leaveItemId = 0;
		if (leaveItemIdStr != null && !leaveItemIdStr.trim().isEmpty()) {
			leaveItemId = Integer.parseInt(leaveItemIdStr);
		} else {
			// 만약 휴가 항목 파라미터가 null일 시, 사용가능한 두개의 휴가 항목 중 ID가 빠른 휴가항목ID를 기본값으로 설정
			// もし休暇項目パラメータがnullの場合、使える二つの休暇項目の中でIDが早い方の休暇項目IDをデフォルト値に設定
			leaveItemId = leaveItems.get(0).getLeaveItemId();
		}
		Integer departmentId = (departmentIdStr != null && !departmentIdStr.trim().isEmpty())
				? Integer.parseInt(departmentIdStr)
				: null;
		Integer jobPositionId = (jobPositionIdstr != null && !jobPositionIdstr.trim().isEmpty())
				? Integer.parseInt(jobPositionIdstr)
				: null;

		// 페이지 번호 파라미터가 null인 경우, 기본값 1로 설정
		// ページ番号パラメータがnullの場合、デフォルト値を1に設定
		int pageNum = 1;
		if (pageStr != null && !pageStr.trim().isEmpty()) {
			pageNum = Integer.parseInt(pageStr);
		}

		// 페이지 사이즈 파라미터가 null인 경우, 기본값 30으로 설정
		// ページサイズパラメータがnullの場合、デフォルト値を３０に設定
		int pageSize = 30;
		if (pageSizeStr != null && !pageSizeStr.trim().isEmpty()) {
			pageSize = Integer.parseInt(pageSizeStr);
		}

		// 3. 휴가 조회 요청 객체 생성 및 값 세팅
		// 3. 休暇照会要請オブジェクトを生成し値をセット
		LeaveInquiryRequest request = new LeaveInquiryRequest(leaveItemId, keyword, status, empType, departmentId,
				jobPositionId, pageSize);

		// 4. 요청 객체를 기반으로 휴가 조회 페이지 생성
		// 4. 要請オブジェクトを基に休暇照会ページを生成
		LeaveInquiryPage leaveInquiryPage = listLeaveInquiryService.getInquiryPage(pageNum, request);

		// 5. request 영역에 조회란 정보 저장
		// 5. リクエストスコープに照会欄の情報を保存
		req.setAttribute("leaveInquiryPage", leaveInquiryPage);
		req.setAttribute("empType", empType);
		req.setAttribute("keyword", keyword);
		req.setAttribute("status", status);
		req.setAttribute("leaveItemId", leaveItemId);
		req.setAttribute("departmentId", departmentId);
		req.setAttribute("jobPositionId", jobPositionId);
		req.setAttribute("pageSize", pageSize);

		// 사원별 휴가 현황(사원ID 파라미터가 있을 경우에만 작동)
		// 社員別休暇状況(社員IDパラメータがある時だけ作動)
		// 1. 사원ID 파라미터 분석
		// 1. 社員ID パラメータ分析
		String employeeIdStr = req.getParameter("employeeId");
		if (employeeIdStr != null && !employeeIdStr.trim().isEmpty()) {
			int employeeId = Integer.parseInt(employeeIdStr);

			// 2. 사원ID로 휴가조회 페이지 객체에서 사원별 휴가 현황 dto를 검색
			// 2. 社員IDで休暇照会ページオブジェクトから社員別休暇状況dtoを検索
			LeaveInquiryDto selectedEmployee = null;
			for (LeaveInquiryDto dto : leaveInquiryPage.getContent()) {
				if (dto.getEmployeeId() == employeeId) {
					selectedEmployee = dto;
					break;
				}
			}

			// 3. 휴가항목 ID와 사원ID를 기반으로 해당 사원의 근태 기록을 조회
			// 3. 休暇項目IDと社員IDを基に該当社員の勤怠記録を照会
			List<AttendanceEmployeeRecordDto> leaveRecords = leaveRecordInquiryService.getLeaveRecords(employeeId,
					leaveItemId);

			// 4. request영역에 정보 저장
			// 4. リクエストスコープに情報保存
			req.setAttribute("selectedEmployee", selectedEmployee);
			req.setAttribute("employeeId", employeeId);
			req.setAttribute("leaveRecords", leaveRecords);
		}

		return FORM_VIEW;
	}
}
