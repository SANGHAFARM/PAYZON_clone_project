package erp.payroll.command;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.payroll.dto.DayWorkerPaymentPage;
import erp.payroll.model.PayrollRun;
import erp.payroll.service.DayWorkerPayrollService;
import erp.payroll.service.PayrollManagementService;
import mvc.command.CommandHandler;

// 일용직 급여입력 화면의 조회와 저장 요청을 처리한다.
// 일용직근로자급여 화면의 HTTP 요청을 구분하고 적절한 서비스 호출과 화면 이동을 담당한다.
// 日雇い労働者給与画面のHTTPリクエストを判別し、適切なサービス呼び出しと画面遷移を担当する。
public class DayWorkerPayrollHandler implements CommandHandler {

	private static final String VIEW = "/WEB-INF/view/payroll/day-worker-payroll-management.jsp";
	private DayWorkerPayrollService dayWorkerService = new DayWorkerPayrollService();
	private PayrollManagementService managementService = new PayrollManagementService();

	@Override
	// 요청 방식과 작업 구분을 확인하여 일용직근로자급여 조회·저장 작업을 적절한 처리로 연결한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエスト方式と処理区分を確認し、日雇い労働者給与の照会・保存処理へ適切に振り分ける。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String uri = req.getRequestURI();
		if (uri.endsWith("/day-worker-management.do") && req.getMethod().equalsIgnoreCase("GET")) {
			return processForm(req, res);
		} else if (uri.endsWith("/employees/add.do") && req.getMethod().equalsIgnoreCase("POST")) {
			return processAddEmployees(req, res);
		} else if (uri.endsWith("/employees/delete.do") && req.getMethod().equalsIgnoreCase("POST")) {
			return processDeleteEmployees(req, res);
		} else if (uri.endsWith("/day-worker/save.do") && req.getMethod().equalsIgnoreCase("POST")) {
			return processSave(req, res);
		}
		res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		return null;
	}

	// 일용직근로자급여 화면에 필요한 데이터를 조회하여 request에 저장하고 JSP 경로를 반환한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// 日雇い労働者給与画面に必要なデータを照会してrequestへ保存し、JSPパスを返す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	private String processForm(HttpServletRequest req, HttpServletResponse res) {
		PayrollRun run = makeRun(req);
		int employeePage = intValue(req.getParameter("employeePage"), 1);
		DayWorkerPaymentPage page = dayWorkerService.getPage(run, integerValue(req.getParameter("employeeId")),
				req.getParameter("employeeKeyword"), integerValue(req.getParameter("departmentId")), employeePage);
		setPageAttributes(req, page, run, employeePage);
		Object flashMessage = req.getSession().getAttribute("dayWorkerPayrollMessage");
		if (flashMessage != null) {
			req.setAttribute("payrollPopupMessage", flashMessage);
			req.getSession().removeAttribute("dayWorkerPayrollMessage");
		}
		return VIEW;
	}

	// 일용직근로자급여 입력 요청을 검증한 뒤 서비스에 저장을 위임하고 처리 결과를 전달한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// 日雇い労働者給与の入力リクエストを検証し、サービスへ保存を委譲して処理結果を渡す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	private String processAddEmployees(HttpServletRequest req, HttpServletResponse res) throws IOException {
		PayrollRun run = makeSafeRun(req);
		int[] employeeIds = intValues(req.getParameterValues("employeeIds"));
		if (employeeIds.length == 0) {
			return redirectWithMessage(req, res, run, "추가할 사원을 선택해주세요");
		}
		try {
			managementService.addEmployees(run, employeeIds);
		} catch (RuntimeException e) {
			return redirectWithMessage(req, res, run, makeFailureMessage(e));
		}
		redirect(req, res, run, null);
		return null;
	}

	// 요청에서 선택된 일용직근로자급여 대상을 확인하고 삭제 결과를 화면에 전달한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエストで選択された日雇い労働者給与対象を確認し、削除結果を画面へ渡す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	private String processDeleteEmployees(HttpServletRequest req, HttpServletResponse res) throws IOException {
		PayrollRun run = makeSafeRun(req);
		boolean deleteAll = "ALL".equals(req.getParameter("deleteType"));
		int[] employeeIds = intValues(req.getParameterValues("employeeIds"));
		if (!deleteAll && employeeIds.length == 0) {
			redirect(req, res, run, null);
			return null;
		}
		try {
			managementService.deleteEmployees(run, employeeIds, deleteAll);
		} catch (RuntimeException e) {
			return redirectWithMessage(req, res, run, makeFailureMessage(e));
		}
		redirect(req, res, run, null);
		return null;
	}

	// 일용직근로자급여 입력 요청을 검증한 뒤 서비스에 저장을 위임하고 처리 결과를 전달한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// 日雇い労働者給与の入力リクエストを検証し、サービスへ保存を委譲して処理結果を渡す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	private String processSave(HttpServletRequest req, HttpServletResponse res) throws IOException {
		try {
			return savePayroll(req, res);
		} catch (RuntimeException e) {
			// POST에서 JSP로 바로 포워드하지 않고 GET으로 돌아가 안내 팝업을 표시한다.
			// HTTPメソッドと処理区分を確認し、照会またはデータ変更に対応する処理へ分岐する。
			return redirectWithMessage(req, res, makeSafeRun(req), makeFailureMessage(e));
		}
	}

	// 입력값을 검증한 후 급여 데이터를 트랜잭션으로 저장하거나 수정한다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 入力値を検証した後、給与データをトランザクションで登録または更新する。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private String savePayroll(HttpServletRequest req, HttpServletResponse res) throws IOException {
		PayrollRun run = makeRun(req);
		int employeeId = intValue(req.getParameter("employeeId"), 0);
		String calculationType = req.getParameter("calculationType");
		if (employeeId == 0) {
			return redirectWithMessage(req, res, run, makeSelectionMessage(calculationType));
		}
		if (!dayWorkerService.hasWorkPayments(run, employeeId)) {
			return redirectWithMessage(req, res, run, "근무내역이 있는 사원을 선택해주세요", employeeId);
		}
		long[] amounts = readAmounts(req);
		if (calculationType != null) {
			long[] calculated = dayWorkerService.calculate(run, employeeId, amounts[6]);
			if ("INSURANCE".equals(calculationType)) {
				for (int i = 0; i < 4; i++) {
					amounts[i] = calculated[i];
				}
			} else if ("PERIOD_TAX".equals(calculationType)) {
				amounts[4] = calculated[4];
				amounts[5] = calculated[5];
			}
		}
		dayWorkerService.save(run, employeeId, amounts);
		redirect(req, res, run, employeeId);
		return null;
	}

	// 조회값과 입력값을 조합하여 Selection메시지 처리 데이터를 구성한다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 照会値と入力値を組み合わせてSelectionメッセージの処理データを構成する。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private String makeSelectionMessage(String calculationType) {
		if ("INSURANCE".equals(calculationType)) {
			return "4대보험을 계산할 사원을 선택해주세요";
		}
		if ("PERIOD_TAX".equals(calculationType)) {
			return "기간단위 소득세를 계산할 사원을 선택해주세요";
		}
		return "급여내역을 저장할 사원을 선택해주세요";
	}

	// 조회값과 입력값을 조합하여 오류메시지 처리 데이터를 구성한다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 照会値と入力値を組み合わせてエラーメッセージの処理データを構成する。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private String makeFailureMessage(RuntimeException e) {
		String message = e.getMessage();
		if (message != null && message.contains("unique constraint")) {
			return "화면을 다시 조회한 후 저장해주세요";
		}
		return "입력값과 급여 대상 내역을 확인해주세요";
	}

	// With메시지 처리 후 필요한 조회조건을 URL에 포함하여 목록 화면으로 이동시킨다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// Withメッセージ処理後、必要な検索条件をURLへ含めて一覧画面にリダイレクトする。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private String redirectWithMessage(HttpServletRequest req, HttpServletResponse res, PayrollRun run,
			String message) throws IOException {
		return redirectWithMessage(req, res, run, message, null);
	}

	// With메시지 처리 후 필요한 조회조건을 URL에 포함하여 목록 화면으로 이동시킨다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// Withメッセージ処理後、必要な検索条件をURLへ含めて一覧画面にリダイレクトする。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private String redirectWithMessage(HttpServletRequest req, HttpServletResponse res, PayrollRun run,
			String message, Integer employeeId) throws IOException {
		req.getSession().setAttribute("dayWorkerPayrollMessage", message);
		redirect(req, res, run, employeeId);
		return null;
	}

	// 조회값과 입력값을 조합하여 Safe급여 회차 처리 데이터를 구성한다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 照会値と入力値を組み合わせてSafe給与回次の処理データを構成する。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private PayrollRun makeSafeRun(HttpServletRequest req) {
		try {
			return makeRun(req);
		} catch (RuntimeException e) {
			return managementService.makeRun(String.valueOf(LocalDate.now().getYear()),
					String.valueOf(LocalDate.now().getMonthValue()), "1", "daily", null, null, null);
		}
	}

	// 조회조건과 화면 데이터를 request 속성에 저장하여 JSP에서 사용할 수 있게 한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 検索条件と画面データをrequest属性へ保存し、JSPから利用できるようにする。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	private void setPageAttributes(HttpServletRequest req, DayWorkerPaymentPage page, PayrollRun requestRun,
			int employeePage) {
		PayrollRun displayRun = page.getRun() == null ? requestRun : page.getRun();
		req.setAttribute("paymentYears", makePaymentYears());
		req.setAttribute("selectedYear", Integer.parseInt(displayRun.getPayYear()));
		req.setAttribute("selectedMonth", Integer.parseInt(displayRun.getPayMonth()));
		req.setAttribute("selectedRound", Integer.parseInt(displayRun.getPaySeq()));
		req.setAttribute("calculationStart", displayRun.getCalcStartDate());
		req.setAttribute("calculationEnd", displayRun.getCalcEndDate());
		req.setAttribute("paymentDate", displayRun.getPayDate());
		req.setAttribute("paymentEmployees", page.getPaymentEmployees());
		req.setAttribute("selectedEmployee", page.getSelectedEmployee());
		req.setAttribute("selectedEmployeeId",
				page.getSelectedEmployee() == null ? null : page.getSelectedEmployee().getEmployeeId());
		req.setAttribute("availableEmployeePage", page.getAvailableEmployeePage());
		req.setAttribute("departments", page.getDepartments());
		req.setAttribute("employeePage", employeePage);
	}

	// 조회값과 입력값을 조합하여 급여 회차 처리 데이터를 구성한다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 照会値と入力値を組み合わせて給与回次の処理データを構成する。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private PayrollRun makeRun(HttpServletRequest req) {
		String year = value(req.getParameter("paymentYear"), String.valueOf(LocalDate.now().getYear()));
		String month = value(req.getParameter("paymentMonth"), String.valueOf(LocalDate.now().getMonthValue()));
		String sequence = value(req.getParameter("paymentRound"), "1");
		return managementService.makeRun(year, month, sequence, "daily", req.getParameter("calculationStart"),
				req.getParameter("calculationEnd"), req.getParameter("paymentDate"));
	}

	// 일용직근로자급여 처리에 필요한 금액 목록 데이터를 조회하여 반환한다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 日雇い労働者給与処理に必要な金額一覧データを照会して返す。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private long[] readAmounts(HttpServletRequest req) {
		return new long[] { longValue(req.getParameter("nationalPension")),
				longValue(req.getParameter("healthInsurance")),
				longValue(req.getParameter("longTermCareInsurance")),
				longValue(req.getParameter("employmentInsurance")), longValue(req.getParameter("incomeTax")),
				longValue(req.getParameter("localIncomeTax")), longValue(req.getParameter("mutualAidFee")) };
	}

	// 조회값과 입력값을 조합하여 지급연도 목록 처리 데이터를 구성한다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 照会値と入力値を組み合わせて支給年度一覧の処理データを構成する。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private List<Integer> makePaymentYears() {
		List<Integer> years = new ArrayList<>();
		int currentYear = LocalDate.now().getYear();
		for (int year = currentYear + 1; year >= currentYear - 5; year--) {
			years.add(year);
		}
		return years;
	}

	// 일용직근로자급여 처리 후 필요한 조회조건을 URL에 포함하여 목록 화면으로 이동시킨다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 日雇い労働者給与処理後、必要な検索条件をURLへ含めて一覧画面にリダイレクトする。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private void redirect(HttpServletRequest req, HttpServletResponse res, PayrollRun run, Integer employeeId)
			throws IOException {
		String location = req.getContextPath() + "/payroll/day-worker-management.do?paymentYear="
				+ run.getPayYear() + "&paymentMonth=" + Integer.parseInt(run.getPayMonth()) + "&paymentRound="
				+ Integer.parseInt(run.getPaySeq());
		if (employeeId != null) {
			location += "&employeeId=" + employeeId;
		}
		res.sendRedirect(location);
	}

	// 요청 문자열을 정리하고 정수값 목록 처리에 필요한 안전한 값으로 변환한다.
	// 누락되거나 잘못된 파라미터가 500 오류로 이어지지 않도록 기본값과 형식 검사를 적용한다.
	// リクエスト文字列を整え、整数値一覧処理に必要な安全な値へ変換する。
	// 不足または不正なパラメーターが500エラーにつながらないよう、初期値と形式検証を適用する。
	private int[] intValues(String[] values) {
		if (values == null) {
			return new int[0];
		}
		int[] result = new int[values.length];
		for (int i = 0; i < values.length; i++) {
			result[i] = intValue(values[i], 0);
		}
		return result;
	}

	// 요청 문자열을 정리하고 정수값 처리에 필요한 안전한 값으로 변환한다.
	// 누락되거나 잘못된 파라미터가 500 오류로 이어지지 않도록 기본값과 형식 검사를 적용한다.
	// リクエスト文字列を整え、整数値処理に必要な安全な値へ変換する。
	// 不足または不正なパラメーターが500エラーにつながらないよう、初期値と形式検証を適用する。
	private Integer integerValue(String value) {
		try {
			return Integer.valueOf(value);
		} catch (Exception e) {
			return null;
		}
	}

	// 요청 문자열을 정리하고 정수값 처리에 필요한 안전한 값으로 변환한다.
	// 누락되거나 잘못된 파라미터가 500 오류로 이어지지 않도록 기본값과 형식 검사를 적용한다.
	// リクエスト文字列を整え、整数値処理に必要な安全な値へ変換する。
	// 不足または不正なパラメーターが500エラーにつながらないよう、初期値と形式検証を適用する。
	private int intValue(String value, int defaultValue) {
		Integer number = integerValue(value);
		return number == null ? defaultValue : number;
	}

	// 요청 문자열을 정리하고 정수값 처리에 필요한 안전한 값으로 변환한다.
	// 누락되거나 잘못된 파라미터가 500 오류로 이어지지 않도록 기본값과 형식 검사를 적용한다.
	// リクエスト文字列を整え、整数値処理に必要な安全な値へ変換する。
	// 不足または不正なパラメーターが500エラーにつながらないよう、初期値と形式検証を適用する。
	private long longValue(String value) {
		try {
			return Math.max(0, Long.parseLong(value == null ? "0" : value.replace(",", "").trim()));
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	// 요청 문자열을 정리하고 값 처리에 필요한 안전한 값으로 변환한다.
	// 누락되거나 잘못된 파라미터가 500 오류로 이어지지 않도록 기본값과 형식 검사를 적용한다.
	// リクエスト文字列を整え、値処理に必要な安全な値へ変換する。
	// 不足または不正なパラメーターが500エラーにつながらないよう、初期値と形式検証を適用する。
	private String value(String value, String defaultValue) {
		return value == null || value.trim().isEmpty() ? defaultValue : value;
	}
}
