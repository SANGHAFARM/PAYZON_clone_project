package erp.employees.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.employees.service.EmployeeListService;
import erp.employees.service.EmployeeListService.EmployeeListResult;
import erp.employees.service.EmployeeSearchCondition;
import mvc.command.CommandHandler;

// 사원현황/관리 화면의 조회와 삭제 요청을 처리한다.
// 사원목록 화면의 HTTP 요청을 구분하고 적절한 서비스 호출과 화면 이동을 담당한다.
// 社員一覧画面のHTTPリクエストを判別し、適切なサービス呼び出しと画面遷移を担当する。
public class EmployeeListHandler implements CommandHandler {

	private static final String VIEW = "/WEB-INF/view/employees/employees-list.jsp";
	private EmployeeListService employeeListService = new EmployeeListService();

	@Override
	// 요청 방식과 작업 구분을 확인하여 사원목록 조회·저장 작업을 적절한 처리로 연결한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエスト方式と処理区分を確認し、社員一覧の照会・保存処理へ適切に振り分ける。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String uri = req.getRequestURI();
		if (uri.endsWith("/employee-action.do") && req.getMethod().equalsIgnoreCase("POST")) {
			return processEmployeeAction(req, res);
		} else if (uri.endsWith("/employee-columns.do") && req.getMethod().equalsIgnoreCase("POST")) {
			return processColumnSetting(req, res);
		} else if (!req.getMethod().equalsIgnoreCase("GET")) {
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
		return processList(req);
	}

	// 사원목록 화면에 필요한 데이터를 조회하여 request에 저장하고 JSP 경로를 반환한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// 社員一覧画面に必要なデータを照会してrequestへ保存し、JSPパスを返す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	private String processList(HttpServletRequest req) {
		EmployeeSearchCondition condition = createCondition(req);
		if ("search".equals(req.getParameter("mode")) && condition.getKeyword().length() < 2) {
			// 검색 버튼 요청은 공백을 제외한 검색어가 두 글자 이상일 때만 조회한다.
			// 未入力値とNULLを区別して正規化し、制約違反や不要な値の保存を防止する。
			req.setAttribute("searchValidationMessage", "검색어를 2자 이상 입력해주세요.");
			condition.setKeyword("");
		}
		EmployeeListResult result = employeeListService.getEmployeeList(condition);
		req.setAttribute("employees", result.getEmployees());
		req.setAttribute("employeeSummary", result.getSummary());
		req.setAttribute("pageInfo", result.getPageInfo());
		req.setAttribute("employmentTypes", employeeListService.getEmploymentTypes());
		req.setAttribute("condition", condition);
		req.setAttribute("visibleColumns", getVisibleColumns(req));

		Object message = req.getSession().getAttribute("employeeListMessage");
		if (message != null) {
			req.setAttribute("message", message);
			req.getSession().removeAttribute("employeeListMessage");
		}
		return VIEW;
	}

	// 요청에서 사원작업구분 작업에 필요한 값을 읽고 검증한 뒤 해당 서비스 처리를 호출한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエストから社員処理区分処理に必要な値を取得・検証し、該当するサービス処理を呼び出す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	private String processEmployeeAction(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String[] selectedIds = req.getParameterValues("employeeIds");
		if (selectedIds == null || selectedIds.length == 0) {
			req.getSession().setAttribute("employeeListMessage", "삭제할 사원을 선택해주세요.");
			res.sendRedirect(req.getContextPath() + "/employees/employees.do");
			return null;
		}

		List<Integer> employeeIds;
		try {
			employeeIds = toEmployeeIds(selectedIds);
		} catch (IllegalArgumentException e) {
			req.getSession().setAttribute("employeeListMessage", e.getMessage());
			res.sendRedirect(req.getContextPath() + "/employees/employees.do");
			return null;
		}
		if ("requestDelete".equals(req.getParameter("action"))) {
			// 첫 요청에서는 삭제하지 않고 서버가 확인 화면을 표시한다.
			// 画面表示に必要なデータとメッセージをrequestまたはsessionへ保存し、JSPへ引き渡す。
			req.setAttribute("deleteConfirmation", true);
			req.setAttribute("deleteEmployeeIds", employeeIds);
			req.setAttribute("deleteEmployeeCount", employeeIds.size());
			return processList(req);
		}

		if ("confirmDelete".equals(req.getParameter("action"))) {
			try {
				int deletedCount = employeeListService.deleteEmployees(employeeIds);
				req.getSession().setAttribute("employeeListMessage",
						deletedCount + "명의 사원정보를 삭제했습니다.");
			} catch (RuntimeException e) {
				req.getSession().setAttribute("employeeListMessage", e.getMessage());
			}
		}
		res.sendRedirect(req.getContextPath() + "/employees/employees.do");
		return null;
	}

	// 입력 데이터를 사원Ids 처리에 필요한 형식으로 변환한다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 入力データを社員Ids処理に必要な形式へ変換する。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private List<Integer> toEmployeeIds(String[] selectedIds) {
		List<Integer> employeeIds = new ArrayList<>();
		for (String selectedId : selectedIds) {
			try {
				employeeIds.add(Integer.valueOf(selectedId));
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("잘못된 사원번호가 포함되어 있습니다.", e);
			}
		}
		return employeeIds;
	}

	// 요청에서 표시항목설정 작업에 필요한 값을 읽고 검증한 뒤 해당 서비스 처리를 호출한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエストから表示項目設定処理に必要な値を取得・検証し、該当するサービス処理を呼び出す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	private String processColumnSetting(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String[] columns = req.getParameterValues("columns");
		req.getSession().setAttribute("employeeVisibleColumns",
				columns == null ? new ArrayList<String>() : Arrays.asList(columns));
		res.sendRedirect(req.getContextPath() + "/employees/employees.do");
		return null;
	}

	@SuppressWarnings("unchecked")
	// 사원목록 처리에 필요한 Visible표시항목 목록를 조회하거나 계산하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 社員一覧処理に必要なVisible表示項目一覧を照会または計算して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	private List<String> getVisibleColumns(HttpServletRequest req) {
		List<String> columns = (List<String>) req.getSession().getAttribute("employeeVisibleColumns");
		if (columns == null) {
			columns = Arrays.asList("employmentType", "joinDate", "employeeNo", "name", "department",
					"position", "residentNo", "mobile", "email", "retirementDate", "status");
		}
		return columns;
	}

	// 사원목록 처리에 사용할 검색조건 데이터나 객체를 생성한다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 社員一覧処理で使用する検索条件データまたはオブジェクトを生成する。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private EmployeeSearchCondition createCondition(HttpServletRequest req) {
		EmployeeSearchCondition condition = new EmployeeSearchCondition();
		condition.setSearchTarget(valueOrDefault(req.getParameter("searchTarget"), "ALL"));
		condition.setKeyword(trim(req.getParameter("keyword")));
		condition.setEmploymentType(trim(req.getParameter("employmentType")));
		condition.setStatus(req.getParameter("status") == null ? "WORK" : trim(req.getParameter("status")));
		condition.setPage(parsePositiveInt(req.getParameter("page"), 1));
		int pageSize = parsePositiveInt(req.getParameter("pageSize"), 30);
		condition.setPageSize(pageSize == 10 || pageSize == 30 || pageSize == 50 || pageSize == 100 ? pageSize : 30);
		return condition;
	}

	// 요청 문자열을 정리하고 값Or기본 처리에 필요한 안전한 값으로 변환한다.
	// 누락되거나 잘못된 파라미터가 500 오류로 이어지지 않도록 기본값과 형식 검사를 적용한다.
	// リクエスト文字列を整え、値Or初期処理に必要な安全な値へ変換する。
	// 不足または不正なパラメーターが500エラーにつながらないよう、初期値と形式検証を適用する。
	private String valueOrDefault(String value, String defaultValue) {
		String trimmed = trim(value);
		return trimmed.isEmpty() ? defaultValue : trimmed;
	}

	// 요청 문자열을 정리하고 공백제거 처리에 필요한 안전한 값으로 변환한다.
	// 누락되거나 잘못된 파라미터가 500 오류로 이어지지 않도록 기본값과 형식 검사를 적용한다.
	// リクエスト文字列を整え、空白除去処理に必要な安全な値へ変換する。
	// 不足または不正なパラメーターが500エラーにつながらないよう、初期値と形式検証を適用する。
	private String trim(String value) {
		return value == null ? "" : value.trim();
	}

	// 입력 데이터를 Positive정수 처리에 필요한 형식으로 변환한다.
	// 누락되거나 잘못된 파라미터가 500 오류로 이어지지 않도록 기본값과 형식 검사를 적용한다.
	// 入力データをPositive整数処理に必要な形式へ変換する。
	// 不足または不正なパラメーターが500エラーにつながらないよう、初期値と形式検証を適用する。
	private int parsePositiveInt(String value, int defaultValue) {
		try {
			int parsed = Integer.parseInt(value);
			return parsed > 0 ? parsed : defaultValue;
		} catch (Exception e) {
			return defaultValue;
		}
	}
}
