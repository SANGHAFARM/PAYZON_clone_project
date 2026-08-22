package erp.retirement.command;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.employees.service.EmployeeSearchCondition;
import erp.retirement.service.RetirementProcessService;
import erp.retirement.service.RetirementProcessService.RetirementEmployeePage;
import mvc.command.CommandHandler;

// 사원 퇴직처리 화면의 조회·처리·취소 요청을 처리한다.
// 퇴직급여처리 화면의 HTTP 요청을 구분하고 적절한 서비스 호출과 화면 이동을 담당한다.
// 退職給与処理画面のHTTPリクエストを判別し、適切なサービス呼び出しと画面遷移を担当する。
public class RetirementProcessHandler implements CommandHandler {
	private static final String VIEW = "/WEB-INF/view/retirement/retirement-process.jsp";
	private final RetirementProcessService retirementService = new RetirementProcessService();

	@Override
	// 요청 방식과 작업 구분을 확인하여 퇴직급여처리 조회·저장 작업을 적절한 처리로 연결한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエスト方式と処理区分を確認し、退職給与処理の照会・保存処理へ適切に振り分ける。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		if (req.getMethod().equalsIgnoreCase("GET")) {
			return processList(req);
		}
		if (req.getMethod().equalsIgnoreCase("POST")) {
			return processSubmit(req, res);
		}
		res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		return null;
	}

	// 퇴직급여처리 화면에 필요한 데이터를 조회하여 request에 저장하고 JSP 경로를 반환한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// 退職給与処理画面に必要なデータを照会してrequestへ保存し、JSPパスを返す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	private String processList(HttpServletRequest req) {
		String keyword = trim(req.getParameter("keyword"));
		if (req.getParameterMap().containsKey("keyword") && !keyword.isEmpty() && keyword.length() < 2) {
			req.setAttribute("retirementPopupMessage", "검색어를 2자 이상 입력해주세요");
		}
		EmployeeSearchCondition condition = createCondition(req);
		RetirementEmployeePage employeePage = retirementService.getEmployeePage(condition);
		req.setAttribute("employees", employeePage.getEmployees());
		req.setAttribute("pageInfo", employeePage.getPageInfo());
		req.setAttribute("retirementTypes", retirementService.getRetirementTypes());
		req.setAttribute("condition", condition);
		req.setAttribute("currentDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		if ("retired".equals(req.getParameter("result"))) {
			req.setAttribute("message", "퇴직처리를 완료했습니다.");
		}
		if ("cancelled".equals(req.getParameter("result"))) {
			req.setAttribute("message", "퇴직처리를 취소했습니다.");
		}
		return VIEW;
	}

	// 퇴직급여처리 입력 요청을 검증한 뒤 서비스에 저장을 위임하고 처리 결과를 전달한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// 退職給与処理の入力リクエストを検証し、サービスへ保存を委譲して処理結果を渡す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	private String processSubmit(HttpServletRequest req, HttpServletResponse res) throws IOException {
		Integer employeeId = parseInteger(req.getParameter("employeeId"));
		if (employeeId == null) {
			req.setAttribute("message", "처리할 사원을 선택하세요.");
			return processList(req);
		}
		try {
			if ("CANCEL".equals(req.getParameter("processType"))) {
				retirementService.cancel(employeeId);
				redirect(req, res, "cancelled");
			} else {
				retirementService.retire(employeeId, trim(req.getParameter("retirementType")),
						parseDate(req.getParameter("retirementDate")), trim(req.getParameter("retirementReason")),
						trim(req.getParameter("afterContact")));
				redirect(req, res, "retired");
			}
			return null;
		} catch (IllegalArgumentException e) {
			req.setAttribute("message", e.getMessage());
			return processList(req);
		}
	}

	// 퇴직급여처리 처리에 사용할 검색조건 데이터나 객체를 생성한다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 退職給与処理処理で使用する検索条件データまたはオブジェクトを生成する。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private EmployeeSearchCondition createCondition(HttpServletRequest req) {
		EmployeeSearchCondition condition = new EmployeeSearchCondition();
		String target = req.getParameter("searchTarget");
		String keyword = trim(req.getParameter("keyword")).length() >= 2
				? trim(req.getParameter("keyword")) : "";
		condition.setSearchTarget("employeeNo".equals(target) ? "EMPLOYEE_NO"
				: "department".equals(target) ? "DEPARTMENT" : "name".equals(target) ? "NAME" : "ALL");
		condition.setKeyword(keyword);
		condition.setEmploymentType("");
		String status = req.getParameter("status");
		condition.setStatus("ACTIVE".equals(status) ? "WORK" : "RETIRED".equals(status) ? "RETIRED" : "");
		condition.setPage(Math.max(1, intValue(req.getParameter("page"), 1)));
		condition.setPageSize(30);
		return condition;
	}

	// 요청 문자열을 정리하고 정수값 처리에 필요한 안전한 값으로 변환한다.
	// 누락되거나 잘못된 파라미터가 500 오류로 이어지지 않도록 기본값과 형식 검사를 적용한다.
	// リクエスト文字列を整え、整数値処理に必要な安全な値へ変換する。
	// 不足または不正なパラメーターが500エラーにつながらないよう、初期値と形式検証を適用する。
	private int intValue(String value, int defaultValue) {
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	// 퇴직급여처리 처리 후 필요한 조회조건을 URL에 포함하여 목록 화면으로 이동시킨다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 退職給与処理処理後、必要な検索条件をURLへ含めて一覧画面にリダイレクトする。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private void redirect(HttpServletRequest req, HttpServletResponse res, String result) throws IOException {
		res.sendRedirect(req.getContextPath() + "/retirement/process.do?result=" + result);
	}
	// 입력 데이터를 일자 처리에 필요한 형식으로 변환한다.
	// 누락되거나 잘못된 파라미터가 500 오류로 이어지지 않도록 기본값과 형식 검사를 적용한다.
	// 入力データを日付処理に必要な形式へ変換する。
	// 不足または不正なパラメーターが500エラーにつながらないよう、初期値と形式検証を適用する。
	private Date parseDate(String value) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false);
			return dateFormat.parse(value);
		}
		catch (Exception e) {
			return null;
		}
	}
	// 입력 데이터를 정수 처리에 필요한 형식으로 변환한다.
	// 누락되거나 잘못된 파라미터가 500 오류로 이어지지 않도록 기본값과 형식 검사를 적용한다.
	// 入力データを整数処理に必要な形式へ変換する。
	// 不足または不正なパラメーターが500エラーにつながらないよう、初期値と形式検証を適用する。
	private Integer parseInteger(String value) {
		try {
			return Integer.valueOf(value);
		} catch (Exception e) {
			return null;
		}
	}
	private String trim(String value) { return value == null ? "" : value.trim(); }
}
