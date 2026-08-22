package erp.employees.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.employees.service.EmployeeRecordCardService;
import erp.employees.service.EmployeeRecordCardService.EmployeeRecordCardData;
import erp.employees.service.EmployeeSearchCondition;
import mvc.command.CommandHandler;

// 인사기록카드의 사원 검색과 상세 조회 요청을 처리한다.
// 사원기록카드 화면의 HTTP 요청을 구분하고 적절한 서비스 호출과 화면 이동을 담당한다.
// 社員記録カード画面のHTTPリクエストを判別し、適切なサービス呼び出しと画面遷移を担当する。
public class EmployeeRecordCardHandler implements CommandHandler {
	private static final String VIEW = "/WEB-INF/view/employees/employees-record-card.jsp";
	private final EmployeeRecordCardService recordCardService = new EmployeeRecordCardService();

	@Override
	// 요청 방식과 작업 구분을 확인하여 사원기록카드 조회·저장 작업을 적절한 처리로 연결한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエスト方式と処理区分を確認し、社員記録カードの照会・保存処理へ適切に振り分ける。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	public String process(HttpServletRequest req, HttpServletResponse res) {
		if (!req.getMethod().equalsIgnoreCase("GET")) {
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}

		// 사원 선택창에서 사용할 부서, 직위, 검색어 조건을 구성한다.
		// 検索語と選択条件を組み合わせ、利用者が指定した対象だけを照会する。
		EmployeeSearchCondition condition = new EmployeeSearchCondition();
		condition.setSearchTarget("ALL");
		String keyword = trim(req.getParameter("keyword"));
		Integer departmentId = parseInteger(req.getParameter("departmentId"));
		Integer positionId = parseInteger(req.getParameter("positionId"));
		condition.setKeyword(keyword);
		condition.setEmploymentType("");
		condition.setStatus("");
		condition.setPage(1);
		condition.setPageSize(10000);
		condition.setDepartmentId(departmentId);
		condition.setPositionId(positionId);

		String mode = trim(req.getParameter("mode"));
		boolean hasSearchCondition = departmentId != null || positionId != null || !keyword.isEmpty();
		if ("search".equals(mode) && !hasSearchCondition) {
			// 검색 조건이 없으면 결과창 대신 조건 입력 안내창을 표시한다.
			// 検索語と選択条件を組み合わせ、利用者が指定した対象だけを照会する。
			req.setAttribute("searchConditionMessage", "검색 조건을 하나 이상 설정해주세요.");
		} else if ("search".equals(mode) || "all".equals(mode)) {
			req.setAttribute("showEmployeeModal", true);
		}

		// 기록카드 한 화면에 필요한 모든 이력을 조회한다.
		// 画面表示に必要なデータとメッセージをrequestまたはsessionへ保存し、JSPへ引き渡す。
		EmployeeRecordCardData data = recordCardService.getRecordCard(parseInteger(req.getParameter("employeeId")), condition);
		// JSP의 각 표에서 바로 사용할 수 있도록 항목별 request 속성으로 저장한다.
		// 画面表示に必要なデータとメッセージをrequestまたはsessionへ保存し、JSPへ引き渡す。
		req.setAttribute("selectedEmployee", data.getEmployee());
		req.setAttribute("employees", data.getEmployees());
		req.setAttribute("departments", data.getDepartments());
		req.setAttribute("positions", data.getPositions());
		req.setAttribute("company", data.getCompany());
		req.setAttribute("families", data.getFamilies());
		req.setAttribute("insurances", data.getInsurances());
		req.setAttribute("educations", data.getEducations());
		req.setAttribute("careers", data.getCareers());
		req.setAttribute("licenses", data.getLicenses());
		req.setAttribute("languages", data.getLanguages());
		req.setAttribute("trainings", data.getTrainings());
		req.setAttribute("awards", data.getAwards());
		req.setAttribute("appointments", data.getAppointments());
		req.setAttribute("retirementCalculations", data.getRetirementCalculations());
		return VIEW;
	}

	// 입력 데이터를 정수 처리에 필요한 형식으로 변환한다.
	// 누락되거나 잘못된 파라미터가 500 오류로 이어지지 않도록 기본값과 형식 검사를 적용한다.
	// 入力データを整数処理に必要な形式へ変換する。
	// 不足または不正なパラメーターが500エラーにつながらないよう、初期値と形式検証を適用する。
	private Integer parseInteger(String value) {
		// 선택값이 없거나 숫자가 아니면 조건을 적용하지 않는다.
		// 検索語と選択条件を組み合わせ、利用者が指定した対象だけを照会する。
		try { return value == null || value.trim().isEmpty() ? null : Integer.valueOf(value); }
		catch (NumberFormatException e) {
			return null;
		}
	}
	private String trim(String value) { return value == null ? "" : value.trim(); }
}
