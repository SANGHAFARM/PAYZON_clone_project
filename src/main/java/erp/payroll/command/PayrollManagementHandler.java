package erp.payroll.command;

import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.payroll.dto.PayrollManagementItem;
import erp.payroll.dto.PayrollManagementPage;
import erp.payroll.model.PayrollRun;
import erp.payroll.service.PayrollManagementService;
import mvc.command.CommandHandler;

// 급여입력 화면의 조회와 급여 저장 요청을 처리한다.
// 급여입력·관리 화면의 HTTP 요청을 구분하고 적절한 서비스 호출과 화면 이동을 담당한다.
// 給与入力・管理画面のHTTPリクエストを判別し、適切なサービス呼び出しと画面遷移を担当する。
public class PayrollManagementHandler implements CommandHandler {

	private static final String VIEW = "/WEB-INF/view/payroll/payroll-management.jsp";
	private PayrollManagementService managementService = new PayrollManagementService();

	@Override
	// 요청 방식과 작업 구분을 확인하여 급여입력·관리 조회·저장 작업을 적절한 처리로 연결한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエスト方式と処理区分を確認し、給与入力・管理の照会・保存処理へ適切に振り分ける。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String uri = req.getRequestURI();
		if (uri.endsWith("/management.do") && req.getMethod().equalsIgnoreCase("GET")) {
			return processForm(req, res);
		} else if (uri.endsWith("/employees/add.do") && req.getMethod().equalsIgnoreCase("POST")) {
			return processAddEmployees(req, res);
		} else if (uri.endsWith("/employees/delete.do") && req.getMethod().equalsIgnoreCase("POST")) {
			return processDeleteEmployees(req, res);
		} else if (uri.endsWith("/management/save.do") && req.getMethod().equalsIgnoreCase("POST")) {
			return processSave(req, res);
		} else if (uri.endsWith("/load-previous.do") && req.getMethod().equalsIgnoreCase("POST")) {
			return processLoadPrevious(req, res);
		} else if (uri.endsWith("/give-item/save.do") && req.getMethod().equalsIgnoreCase("POST")) {
			return processGiveItem(req, res);
		} else if (uri.endsWith("/deduction-item/save.do") && req.getMethod().equalsIgnoreCase("POST")) {
			return processDeductionItem(req, res);
		}
		res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		return null;
	}

	// 급여입력·관리 화면에 필요한 데이터를 조회하여 request에 저장하고 JSP 경로를 반환한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// 給与入力・管理画面に必要なデータを照会してrequestへ保存し、JSPパスを返す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	private String processForm(HttpServletRequest req, HttpServletResponse res) {
		String year = value(req.getParameter("paymentYear"), String.valueOf(LocalDate.now().getYear()));
		String month = value(req.getParameter("paymentMonth"), String.valueOf(LocalDate.now().getMonthValue()));
		String sequence = value(req.getParameter("paymentRound"), "1");
		String incomeMode = value(req.getParameter("incomeType"), "general");
		String incomeType = "business".equals(incomeMode) ? "1" : "0";
		int employeePage = intValue(req.getParameter("employeePage"), 1);

		PayrollManagementPage page = managementService.getPage(year, twoDigits(month), twoDigits(sequence),
				incomeType, integerValue(req.getParameter("employeeId")), req.getParameter("employeeKeyword"),
				integerValue(req.getParameter("departmentId")), integerValue(req.getParameter("positionId")),
				normalizeStatus(req.getParameter("status")), employeePage);
		setPageAttributes(req, page, year, month, sequence, incomeMode, employeePage);
		return VIEW;
	}

	// 급여입력·관리 입력 요청을 검증한 뒤 서비스에 저장을 위임하고 처리 결과를 전달한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// 給与入力・管理の入力リクエストを検証し、サービスへ保存を委譲して処理結果を渡す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	private String processAddEmployees(HttpServletRequest req, HttpServletResponse res) throws IOException {
		if ("search".equals(req.getParameter("action"))) {
			redirectEmployeeSearch(req, res);
			return null;
		}
		PayrollRun run = makeRun(req);
		managementService.addEmployees(run, intValues(req.getParameterValues("employeeIds")));
		redirect(req, res, run, req.getParameter("incomeType"));
		return null;
	}

	// 요청에서 선택된 급여입력·관리 대상을 확인하고 삭제 결과를 화면에 전달한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエストで選択された給与入力・管理対象を確認し、削除結果を画面へ渡す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	private String processDeleteEmployees(HttpServletRequest req, HttpServletResponse res) throws IOException {
		PayrollRun run = makeRun(req);
		boolean deleteAll = "ALL".equals(req.getParameter("deleteType"));
		managementService.deleteEmployees(run, intValues(req.getParameterValues("employeeIds")), deleteAll);
		redirect(req, res, run, req.getParameter("incomeType"));
		return null;
	}

	// 급여입력·관리 입력 요청을 검증한 뒤 서비스에 저장을 위임하고 처리 결과를 전달한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// 給与入力・管理の入力リクエストを検証し、サービスへ保存を委譲して処理結果を渡す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	private String processSave(HttpServletRequest req, HttpServletResponse res) throws IOException {
		try {
			return savePayroll(req, res);
		} catch (RuntimeException e) {
			// 저장 오류도 서버 오류 페이지로 넘기지 않고 급여 화면에서 안내한다.
			// 画面表示に必要なデータとメッセージをrequestまたはsessionへ保存し、JSPへ引き渡す。
			req.setAttribute("payrollPopupMessage", makeFailureMessage(e));
			return processForm(req, res);
		}
	}

	// 입력값을 검증한 후 급여 데이터를 트랜잭션으로 저장하거나 수정한다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 入力値を検証した後、給与データをトランザクションで登録または更新する。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private String savePayroll(HttpServletRequest req, HttpServletResponse res) throws IOException {
		PayrollRun run = makeRun(req);
		int employeeId = intValue(req.getParameter("employeeId"), 0);
		if (employeeId == 0) {
			req.setAttribute("payrollPopupMessage", "급여내역을 저장할 사원을 선택해주세요");
			return processForm(req, res);
		}
		List<PayrollManagementItem> payItems = readItems(req, "give_");
		List<PayrollManagementItem> deductItems = readItems(req, "deduction_");
		if ("business".equals(req.getParameter("incomeType"))) {
			PayrollManagementPage page = managementService.getPage(run.getPayYear(), run.getPayMonth(),
					run.getPaySeq(), run.getIncomeType(), employeeId, null, 1);
			payItems = page.getPaymentGiveItems();
			deductItems = page.getPaymentDeductionItems();
			setAmount(payItems, 0, req.getParameter("businessIncome"));
			setAmount(payItems, 1, req.getParameter("otherIncome"));
			setAmount(deductItems, 0, req.getParameter("businessTax"));
			setAmount(deductItems, 1, req.getParameter("businessLocalTax"));
		}
		managementService.save(run, employeeId, payItems, deductItems);
		redirect(req, res, run, req.getParameter("incomeType"), employeeId);
		return null;
	}

	// 조회값과 입력값을 조합하여 오류메시지 처리 데이터를 구성한다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 照会値と入力値を組み合わせてエラーメッセージの処理データを構成する。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private String makeFailureMessage(RuntimeException e) {
		String message = e.getMessage();
		if (message != null && message.contains("unique constraint")) {
			return "이미 저장된 급여항목과 충돌했습니다. 화면을 다시 조회한 후 시도해주세요.";
		}
		return "요청을 처리하지 못했습니다. 사원 선택과 입력내역을 확인해주세요.";
	}

	// 요청에서 불러오기이전 회차 작업에 필요한 값을 읽고 검증한 뒤 해당 서비스 처리를 호출한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエストから読込前回処理に必要な値を取得・検証し、該当するサービス処理を呼び出す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	private String processLoadPrevious(HttpServletRequest req, HttpServletResponse res) throws IOException {
		PayrollRun run = makeRun(req);
		managementService.loadPrevious(run, intValue(req.getParameter("previousPaymentPeriod"), 0));
		redirect(req, res, run, req.getParameter("incomeType"));
		return null;
	}

	// 요청에서 지급항목 작업에 필요한 값을 읽고 검증한 뒤 해당 서비스 처리를 호출한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエストから支給項目処理に必要な値を取得・検証し、該当するサービス処理を呼び出す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	private String processGiveItem(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String action = req.getParameter("action");
		if ("requestDelete".equals(action) && integerValue(req.getParameter("giveItemId")) == null) {
			req.setAttribute("payrollPopupMessage", "삭제할 지급항목을 선택해주세요.");
			return processForm(req, res);
		}
		if ("requestDelete".equals(action) || "requestDeleteAll".equals(action)) {
			req.setAttribute("itemDeleteConfirmation", "GIVE");
			req.setAttribute("itemDeleteAll", "requestDeleteAll".equals(action));
			req.setAttribute("deleteItemId", req.getParameter("giveItemId"));
			return processForm(req, res);
		}
		if ("confirmDelete".equals(action)) action = "delete";
		if ("confirmDeleteAll".equals(action)) action = "deleteAll";
		String taxType = "FREE".equals(req.getParameter("taxType")) ? "비과세" : "전체과세";
		String taxFreeCode = "비과세".equals(taxType) ? req.getParameter("taxFreeName") : null;
		if ("비과세".equals(taxType) && value(taxFreeCode, "").isEmpty()) {
			req.setAttribute("payrollPopupMessage", "비과세 항목을 선택해주세요.");
			return processForm(req, res);
		}
		long taxFreeLimit = "비과세".equals(taxType) ? longValue(req.getParameter("taxFreeLimit")) : 0;
		String attendanceLink = req.getParameter("attendanceLink");
		Integer attendanceItemId = "BATCH".equals(attendanceLink) ? null : integerValue(attendanceLink);
		String payMethod = "BATCH".equals(attendanceLink) ? "일괄지급"
				: attendanceItemId == null ? null : "근태연계";
		managementService.managePayItem(action, integerValue(req.getParameter("giveItemId")),
				req.getParameter("itemName"), taxType, taxFreeCode, taxFreeLimit,
				req.getParameter("calculationMethod"), intValue(req.getParameter("roundingUnit"), 0), payMethod,
				attendanceItemId,
				"BATCH".equals(attendanceLink) ? longValue(req.getParameter("batchAmount")) : null);
		PayrollRun run = makeRun(req);
		redirect(req, res, run, req.getParameter("incomeType"));
		return null;
	}

	// 요청에서 공제항목 작업에 필요한 값을 읽고 검증한 뒤 해당 서비스 처리를 호출한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエストから控除項目処理に必要な値を取得・検証し、該当するサービス処理を呼び出す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	private String processDeductionItem(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String action = req.getParameter("action");
		if ("requestDelete".equals(action) && integerValue(req.getParameter("deductionItemId")) == null) {
			req.setAttribute("payrollPopupMessage", "삭제할 공제항목을 선택해주세요.");
			return processForm(req, res);
		}
		if ("requestDelete".equals(action) || "requestDeleteAll".equals(action)) {
			req.setAttribute("itemDeleteConfirmation", "DEDUCTION");
			req.setAttribute("itemDeleteAll", "requestDeleteAll".equals(action));
			req.setAttribute("deleteItemId", req.getParameter("deductionItemId"));
			return processForm(req, res);
		}
		if ("confirmDelete".equals(action)) action = "delete";
		if ("confirmDeleteAll".equals(action)) action = "deleteAll";
		managementService.manageDeductItem(action,
				integerValue(req.getParameter("deductionItemId")), req.getParameter("itemName"),
				req.getParameter("calculationMethod"), intValue(req.getParameter("roundingUnit"), 0),
				req.getParameter("note"));
		PayrollRun run = makeRun(req);
		redirect(req, res, run, req.getParameter("incomeType"));
		return null;
	}

	// 급여입력·관리 처리에 필요한 항목 목록 데이터를 조회하여 반환한다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 給与入力・管理処理に必要な項目一覧データを照会して返す。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private List<PayrollManagementItem> readItems(HttpServletRequest req, String prefix) {
		List<PayrollManagementItem> result = new ArrayList<>();
		for (Map.Entry<String, String[]> parameter : req.getParameterMap().entrySet()) {
			if (!parameter.getKey().startsWith(prefix)) {
				continue;
			}
			Integer itemCode = integerValue(parameter.getKey().substring(prefix.length()));
			if (itemCode == null) {
				continue;
			}
			PayrollManagementItem item = new PayrollManagementItem();
			item.setItemCode(itemCode);
			item.setAmount(longValue(parameter.getValue()[0]));
			result.add(item);
		}
		return result;
	}

	// 전달받은 금액 값을 급여입력·관리 객체에 저장한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 受け取った金額の値を給与入力・管理オブジェクトに保存する。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	private void setAmount(List<PayrollManagementItem> items, int index, String amount) {
		if (index < items.size()) {
			items.get(index).setAmount(longValue(amount));
		}
	}

	// 조회조건과 화면 데이터를 request 속성에 저장하여 JSP에서 사용할 수 있게 한다.
	// 요청값이나 조회 결과로 전달된 값을 대응하는 필드에 반영하여 객체의 현재 상태를 갱신한다.
	// 検索条件と画面データをrequest属性へ保存し、JSPから利用できるようにする。
	// リクエスト値または照会結果として渡された値を対応フィールドへ反映し、オブジェクトの現在状態を更新する。
	private void setPageAttributes(HttpServletRequest req, PayrollManagementPage page, String year, String month,
			String sequence, String incomeMode, int employeePage) {
		req.setAttribute("paymentYears", makePaymentYears());
		req.setAttribute("selectedYear", Integer.parseInt(year));
		req.setAttribute("selectedMonth", Integer.parseInt(month));
		req.setAttribute("selectedRound", Integer.parseInt(sequence));
		req.setAttribute("incomeType", incomeMode);
		req.setAttribute("paymentEmployees", page.getPaymentEmployees());
		req.setAttribute("selectedEmployee", page.getSelectedEmployee());
		req.setAttribute("paymentGiveItems", page.getPaymentGiveItems());
		req.setAttribute("paymentDeductionItems", page.getPaymentDeductionItems());
		req.setAttribute("allGiveItems", page.getPaymentGiveItems());
		req.setAttribute("allDeductionItems", page.getPaymentDeductionItems());
		req.setAttribute("paymentTotals", page.getPaymentTotals());
		if ("business".equals(incomeMode)) {
			Map<String, Object> businessPayment = new HashMap<>();
			putBusinessItem(businessPayment, page.getPaymentGiveItems(), 0, "businessIncome",
					"businessCalculationMethod");
			putBusinessItem(businessPayment, page.getPaymentGiveItems(), 1, "otherIncome",
					"otherIncomeCalculationMethod");
			putBusinessItem(businessPayment, page.getPaymentDeductionItems(), 0, "incomeTax",
					"incomeTaxCalculationMethod");
			putBusinessItem(businessPayment, page.getPaymentDeductionItems(), 1, "localIncomeTax",
					"localIncomeTaxCalculationMethod");
			req.setAttribute("businessPayment", businessPayment);
		}
		req.setAttribute("availableEmployeePage", page.getAvailableEmployeePage());
		req.setAttribute("availableEmployees", page.getAvailableEmployeePage().getContent());
		req.setAttribute("departments", page.getDepartments());
		req.setAttribute("positions", page.getPositions());
		req.setAttribute("previousPaymentPeriods", page.getPreviousPaymentPeriods());
		req.setAttribute("taxFreeItems", page.getTaxFreeItems());
		req.setAttribute("attendanceItems", page.getAttendanceItems());
		req.setAttribute("employeePage", employeePage);
		if (page.getRun() != null) {
			req.setAttribute("calculationStart", page.getRun().getCalcStartDate());
			req.setAttribute("calculationEnd", page.getRun().getCalcEndDate());
			req.setAttribute("paymentDate", page.getRun().getPayDate());
		}
	}

	// 조회값과 입력값을 조합하여 급여 회차 처리 데이터를 구성한다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 照会値と入力値を組み合わせて給与回次の処理データを構成する。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private PayrollRun makeRun(HttpServletRequest req) {
		return managementService.makeRun(req.getParameter("paymentYear"), req.getParameter("paymentMonth"),
				req.getParameter("paymentRound"), value(req.getParameter("incomeType"), "general"),
				req.getParameter("calculationStart"), req.getParameter("calculationEnd"),
				req.getParameter("paymentDate"));
	}

	// 사업소득항목 값을 저장 요청에 사용할 Map에 항목별로 구성한다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 事業所得項目の値を保存リクエストで使用するMapへ項目別に設定する。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private void putBusinessItem(Map<String, Object> values, List<PayrollManagementItem> items, int index,
			String amountName, String calculationName) {
		if (index < items.size()) {
			values.put(amountName, items.get(index).getAmount());
			values.put(calculationName, items.get(index).getCalculationMethod());
		}
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

	// 급여입력·관리 처리 후 필요한 조회조건을 URL에 포함하여 목록 화면으로 이동시킨다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 給与入力・管理処理後、必要な検索条件をURLへ含めて一覧画面にリダイレクトする。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private void redirect(HttpServletRequest req, HttpServletResponse res, PayrollRun run, String incomeMode)
			throws IOException {
		redirect(req, res, run, incomeMode, null);
	}

	// 급여입력·관리 처리 후 필요한 조회조건을 URL에 포함하여 목록 화면으로 이동시킨다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 給与入力・管理処理後、必要な検索条件をURLへ含めて一覧画面にリダイレクトする。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private void redirect(HttpServletRequest req, HttpServletResponse res, PayrollRun run, String incomeMode,
			Integer employeeId) throws IOException {
		String location = req.getContextPath() + "/payroll/management.do?paymentYear=" + run.getPayYear()
				+ "&paymentMonth=" + Integer.parseInt(run.getPayMonth()) + "&paymentRound="
				+ Integer.parseInt(run.getPaySeq()) + "&incomeType=" + value(incomeMode, "general");
		if (employeeId != null) {
			location += "&employeeId=" + employeeId;
		}
		res.sendRedirect(location);
	}

	// 검색 후에도 사원선택 팝업이 열린 결과 화면으로 이동한다.
	// 사원검색 처리 후 필요한 조회조건을 URL에 포함하여 목록 화면으로 이동시킨다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 社員検索処理後、必要な検索条件をURLへ含めて一覧画面にリダイレクトする。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private void redirectEmployeeSearch(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String location = req.getContextPath() + "/payroll/management.do?paymentYear="
				+ req.getParameter("paymentYear") + "&paymentMonth=" + req.getParameter("paymentMonth")
				+ "&paymentRound=" + req.getParameter("paymentRound") + "&incomeType="
				+ value(req.getParameter("incomeType"), "general") + "&employeeKeyword="
				+ URLEncoder.encode(value(req.getParameter("employeeKeyword"), ""), "UTF-8")
				+ "&departmentId=" + value(req.getParameter("departmentId"), "")
				+ "&positionId=" + value(req.getParameter("positionId"), "")
				+ "&status=" + URLEncoder.encode(value(req.getParameter("status"), ""), "UTF-8")
				+ "#employee-add";
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

	// 요청 문자열을 정리하고 정규화상태 처리에 필요한 안전한 값으로 변환한다.
	// 누락되거나 잘못된 파라미터가 500 오류로 이어지지 않도록 기본값과 형식 검사를 적용한다.
	// リクエスト文字列を整え、正規化状態処理に必要な安全な値へ変換する。
	// 不足または不正なパラメーターが500エラーにつながらないよう、初期値と形式検証を適用する。
	private String normalizeStatus(String status) {
		if ("WORK".equals(status)) return "재직";
		if ("RETIRED".equals(status)) return "퇴직";
		return value(status, "");
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
			return Long.parseLong(value == null ? "0" : value.replace(",", "").trim());
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	// 요청 문자열을 정리하고 두자리 처리에 필요한 안전한 값으로 변환한다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// リクエスト文字列を整え、二桁処理に必要な安全な値へ変換する。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private String twoDigits(String value) {
		return String.format("%02d", Integer.parseInt(value));
	}

	// 요청 문자열을 정리하고 값 처리에 필요한 안전한 값으로 변환한다.
	// 누락되거나 잘못된 파라미터가 500 오류로 이어지지 않도록 기본값과 형식 검사를 적용한다.
	// リクエスト文字列を整え、値処理に必要な安全な値へ変換する。
	// 不足または不正なパラメーターが500エラーにつながらないよう、初期値と形式検証を適用する。
	private String value(String value, String defaultValue) {
		return value == null || value.trim().isEmpty() ? defaultValue : value;
	}
}
