package erp.retirement.command;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.retirement.dto.RetirementBenefitForm;
import erp.retirement.model.RetirementIncomeEntry;
import erp.retirement.model.RetirementTaxDeferral;
import erp.retirement.service.RetirementBenefitService;
import erp.retirement.service.RetirementBenefitService.BenefitPageData;
import mvc.command.CommandHandler;

// 퇴직급여 조회, 계산, 저장과 삭제 요청을 처리한다.
// 퇴직급여정산 화면의 HTTP 요청을 구분하고 적절한 서비스 호출과 화면 이동을 담당한다.
// 退職給与精算画面のHTTPリクエストを判別し、適切なサービス呼び出しと画面遷移を担当する。
public class RetirementBenefitHandler implements CommandHandler {

	private static final String VIEW = "/WEB-INF/view/retirement/retirement-benefit.jsp";
	private final RetirementBenefitService service = new RetirementBenefitService();

	@Override
	// 요청 방식과 작업 구분을 확인하여 퇴직급여정산 조회·저장 작업을 적절한 처리로 연결한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエスト方式と処理区分を確認し、退職給与精算の照会・保存処理へ適切に振り分ける。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String uri = req.getRequestURI();
		boolean get = "GET".equalsIgnoreCase(req.getMethod());
		boolean post = "POST".equalsIgnoreCase(req.getMethod());

		if (get && (uri.endsWith("/benefit.do") || uri.endsWith("/employee-search.do"))) {
			return showPage(req, null);
		}
		if (post && uri.endsWith("/benefit.do") && "loadPay".equals(req.getParameter("action"))) {
			return processLoadPay(req);
		}
		if (post && uri.endsWith("/new.do")) {
			try {
				List<Integer> employeeIds = integerValues(req.getParameterValues("employeeIds"));
				List<Integer> newEmployeeIds = integerValues(req.getParameterValues("newEmployeeIds"));
				Integer activeEmployeeId = parseInt(req.getParameter("activeEmployeeId"));
				if (newEmployeeIds.isEmpty() && activeEmployeeId == null) {
					throw new IllegalArgumentException("추가할 사원을 선택해주세요");
				}
				for (Integer newEmployeeId : newEmployeeIds) {
					if (!employeeIds.contains(newEmployeeId)) {
						employeeIds.add(newEmployeeId);
					}
				}
				if (employeeIds.isEmpty()) {
					throw new IllegalArgumentException("추가할 사원을 선택해주세요");
				}
				req.setAttribute("draftEmployeeIds", employeeIds);
				// 신규 추가 후에는 첫 사원을 자동 선택하지 않고, 행을 누른 경우에만 상세를 연다.
				// 社員の識別情報と在職・雇用・所属条件を確認し、対象社員データへ反映する。
				RetirementBenefitForm form = activeEmployeeId != null && employeeIds.contains(activeEmployeeId)
						? service.prepareForManagement(activeEmployeeId) : null;
				return showPage(req, form);
			} catch (IllegalArgumentException e) {
				req.setAttribute("message", e.getMessage());
				return showPage(req, null);
			}
		}
		if (post && uri.endsWith("/load-pay.do")) {
			return processLoadPay(req);
		}
		if (post && uri.endsWith("/calculate.do")) {
			return processCalculate(req);
		}
		if (post && uri.endsWith("/save.do")) {
			return processSave(req, res);
		}
		if (post && uri.endsWith("/delete-all.do")) {
			List<Integer> draftEmployeeIds = integerValues(req.getParameterValues("employeeIds"));
			if (!draftEmployeeIds.isEmpty()) {
				// 신규 정산 임시 목록은 DB 삭제 없이 화면에서만 전체 제거한다.
				// 画面表示に必要なデータとメッセージをrequestまたはsessionへ保存し、JSPへ引き渡す。
				req.setAttribute("draftEmployeeIds", new ArrayList<Integer>());
				return showPage(req, null);
			}
			service.delete(null, true, parseInt(req.getParameter("paymentYear")));
			redirectList(req, res);
			return null;
		}
		if (post && uri.endsWith("/delete.do")) {
			Integer calculationId = parseInt(req.getParameter("calculationId"));
			List<Integer> draftEmployeeIds = integerValues(req.getParameterValues("employeeIds"));
			if (!draftEmployeeIds.isEmpty()) {
				// 선택한 임시 사원만 목록에서 제거하고 나머지는 그대로 유지한다.
				// 社員の識別情報と在職・雇用・所属条件を確認し、対象社員データへ反映する。
				Integer activeEmployeeId = parseInt(req.getParameter("activeEmployeeId"));
				if (activeEmployeeId == null || !draftEmployeeIds.contains(activeEmployeeId)) {
					// 선택된 행이 없으면 목록을 변경하지 않는다.
					// 識別された既存データへ変更値を反映し、未変更項目は従来の値を維持する。
					req.setAttribute("draftEmployeeIds", draftEmployeeIds);
					return showPage(req, null);
				}
				draftEmployeeIds.remove(activeEmployeeId);
				// 마지막 임시 행을 삭제해도 요청에 남은 계산ID로 저장 목록을 불러오지 않는다.
				// 支給額・控除額・期間値を業務基準に従って計算し、合計と最終金額へ反映する。
				req.setAttribute("draftMutation", Boolean.TRUE);
				req.setAttribute("draftEmployeeIds", draftEmployeeIds);
				return showPage(req, null);
			}
			if (calculationId == null || calculationId <= 0) {
				return showPage(req, null);
			}
			service.delete(calculationId, false, null);
			redirectList(req, res);
			return null;
		}

		res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		return null;
	}

	// 요청에서 불러오기지급 작업에 필요한 값을 읽고 검증한 뒤 해당 서비스 처리를 호출한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエストから読込支給処理に必要な値を取得・検証し、該当するサービス処理を呼び出す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	private String processLoadPay(HttpServletRequest req) {
		RetirementBenefitForm form = null;
		try {
			form = readForm(req, true);
			validateCalculationDates(form);
			service.loadRecentSalaryEntries(form);
			if (hasSalaryHistory(form)) {
				service.calculate(form);
			} else {
				// 조회 기간에 지급된 급여가 없을 때만 안내한다.
				// 基準日と期間の境界を確認し、照会・計算に使用できる日付形式へ変換する。
				req.setAttribute("message", "급여내역이 없습니다.");
			}
		} catch (IllegalArgumentException e) {
			req.setAttribute("message", e.getMessage());
		}
		return showPage(req, form);
	}

	// 급여이력 조건의 충족 여부를 확인하여 반환한다.
	// 호출자가 전달한 조회조건을 적용하고 결과가 없을 때도 빈 값이나 빈 목록을 안전하게 반환한다.
	// 給与履歴条件を満たしているか確認して返す。
	// 呼び出し側から受け取った検索条件を適用し、結果がない場合も空値または空一覧を安全に返す。
	private boolean hasSalaryHistory(RetirementBenefitForm form) {
		for (RetirementIncomeEntry entry : form.getIncomeEntries()) {
			if (entry.isSalaryData() && entry.getAmount() > 0) {
				return true;
			}
		}
		return false;
	}

	// 요청에서 계산 작업에 필요한 값을 읽고 검증한 뒤 해당 서비스 처리를 호출한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエストから計算処理に必要な値を取得・検証し、該当するサービス処理を呼び出す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	private String processCalculate(HttpServletRequest req) {
		RetirementBenefitForm form = null;
		try {
			form = readForm(req, true);
			validateCalculationDates(form);
			service.calculate(form);
			req.setAttribute("showCalculationResult", true);
		} catch (IllegalArgumentException e) {
			req.setAttribute("message", e.getMessage());
		}
		return showPage(req, form);
	}

	// 퇴직급여정산 입력 요청을 검증한 뒤 서비스에 저장을 위임하고 처리 결과를 전달한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// 退職給与精算の入力リクエストを検証し、サービスへ保存を委譲して処理結果を渡す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	private String processSave(HttpServletRequest req, HttpServletResponse res) throws IOException {
		RetirementBenefitForm form = null;
		try {
			form = readForm(req, true);
			validateCalculationDates(form);
			int calculationId = service.save(form);
			redirect(req, res, "saved", calculationId);
			return null;
		} catch (IllegalArgumentException e) {
			req.setAttribute("message", e.getMessage());
			// 계산은 끝났지만 지급정보가 빠진 경우 결과 영역을 유지한다.
			// 支給額・控除額・期間値を業務基準に従って計算し、合計と最終金額へ反映する。
			if (form != null && form.getTaxYear() > 0) {
				req.setAttribute("showCalculationResult", true);
			}
			return showPage(req, form);
		}
	}

	// 퇴직급여정산 조회 결과와 선택 상태를 request에 저장하고 화면을 표시한다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 退職給与精算の照会結果と選択状態をrequestへ保存して画面を表示する。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private String showPage(HttpServletRequest req, RetirementBenefitForm override) {
		boolean draftMutation = Boolean.TRUE.equals(req.getAttribute("draftMutation"));
		boolean employeeSearchRequest = req.getRequestURI().endsWith("/employee-search.do");
		String employeeKeyword = req.getParameter("employeeKeyword");
		// 사원 검색어는 공백을 제외하고 두 글자 이상 입력해야 한다.
		// 未入力値とNULLを区別して正規化し、制約違反や不要な値の保存を防止する。
		if (employeeSearchRequest && "keyword".equals(req.getParameter("searchMode"))
				&& (employeeKeyword == null || employeeKeyword.trim().length() < 2)) {
			req.setAttribute("message", "검색어를 2자 이상 입력해주세요");
			employeeKeyword = "";
		}
		Integer requestedYear = parseInt(req.getParameter("paymentYear"));
		int year = requestedYear == null ? LocalDate.now().getYear() : requestedYear;
		Integer calculationId = draftMutation ? null : parseInt(req.getParameter("calculationId"));
		BenefitPageData data = service.getPage(year, calculationId,
				employeeKeyword, parseInt(req.getParameter("departmentId")));
		@SuppressWarnings("unchecked")
		List<Integer> draftEmployeeIds = (List<Integer>) req.getAttribute("draftEmployeeIds");
		if (draftEmployeeIds == null) {
			draftEmployeeIds = integerValues(req.getParameterValues("employeeIds"));
		}

		req.setAttribute("paymentYears", service.getPaymentYears());
		req.setAttribute("selectedYear", year);
		boolean newRequest = req.getRequestURI().endsWith("/new.do");
		// 신규 정산 임시 목록이 있으면 DB에 저장된 전체 목록을 섞지 않는다.
		// 対象一覧を順番に処理し、各行の入力値または照会結果を同じ基準で構成する。
		boolean loadList = !draftMutation && !newRequest && draftEmployeeIds.isEmpty()
				&& ("list".equals(req.getParameter("mode"))
				|| calculationId != null || req.getParameter("result") != null
				|| override != null);
		req.setAttribute("retirementBenefits", loadList ? data.getBenefits() : java.util.Collections.emptyList());
		req.setAttribute("selectableEmployees", data.getEmployees());
		req.setAttribute("departments", data.getDepartments());
		req.setAttribute("retirementBenefit", override == null ? data.getForm() : override);
		RetirementBenefitForm displayedForm = override == null ? data.getForm() : override;
		if (displayedForm != null && displayedForm.getCalculationId() > 0) {
			// 저장된 정산을 수정할 때는 기존 계산 결과를 바로 표시한다.
			// 支給額・控除額・期間値を業務基準に従って計算し、合計と最終金額へ反映する。
			req.setAttribute("showCalculationResult", true);
		}
		if (!draftEmployeeIds.isEmpty()) {
			req.setAttribute("draftEmployeeIds", draftEmployeeIds);
			req.setAttribute("draftBenefitEmployees", service.getEmployees(draftEmployeeIds));
			req.setAttribute("draftBenefitForms", service.getLatestBenefits(draftEmployeeIds));
		}
		if (req.getAttribute("message") == null && "saved".equals(req.getParameter("result"))) {
			req.setAttribute("message", "퇴직급여 내역을 저장했습니다.");
		}
		if (req.getAttribute("message") == null && "deleted".equals(req.getParameter("result"))) {
			req.setAttribute("message", "퇴직급여 내역을 삭제했습니다.");
		}
		return VIEW;
	}

	// 퇴직급여정산 처리에 필요한 입력화면 데이터를 조회하여 반환한다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 退職給与精算処理に必要な入力画面データを照会して返す。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private RetirementBenefitForm readForm(HttpServletRequest req, boolean readRows) {
		RetirementBenefitForm form = new RetirementBenefitForm();
		form.setCalculationId(intValue(req.getParameter("calculationId")));
		form.setEmployeeId(requiredInt(req, "employeeId"));
		form.setSettlementType(req.getParameter("settlementType"));
		form.setStartDate(req.getParameter("startDate"));
		form.setEndDate(req.getParameter("endDate"));
		form.setExcludedDays(intValue(req.getParameter("excludedDays")));
		form.setCompensation(longValue(req.getParameter("compensation")));
		form.setDismissalAllowance(longValue(req.getParameter("dismissalAllowance")));
		form.setTaxFreeRetirement(longValue(req.getParameter("taxFreeRetirement")));
		form.setPrepaidTax(longValue(req.getParameter("prepaidTax")));
		form.setTaxCredit(longValue(req.getParameter("taxCredit")));
		form.setDailyOrdinary(longValue(req.getParameter("dailyOrdinary")));
		form.setRetirementIncome(longValue(req.getParameter("retirementIncome")));
		form.setIncomeTax(longValue(req.getParameter("incomeTax")));
		form.setLocalIncomeTax(longValue(req.getParameter("localIncomeTax")));
		form.setRuralTax(longValue(req.getParameter("ruralTax")));
		form.setOtherDeduction(longValue(req.getParameter("otherDeduction")));
		form.setPaymentMethod(req.getParameter("paymentMethod"));
		form.setPaymentDate(req.getParameter("paymentDate"));

		if (readRows) {
			readSalaryRows(req, form);
			readOtherRows(req, form);
			readDeferrals(req, form);
		}
		return form;
	}

	// 퇴직급여정산 처리에 필요한 급여행 목록 데이터를 조회하여 반환한다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 退職給与精算処理に必要な給与行一覧データを照会して返す。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private void readSalaryRows(HttpServletRequest req, RetirementBenefitForm form) {
		String[] starts = req.getParameterValues("salaryStartDate");
		String[] ends = req.getParameterValues("salaryEndDate");
		String[] amounts = req.getParameterValues("salaryTotal");
		if (starts == null) {
			return;
		}
		for (int i = 0; i < starts.length; i++) {
			String end = at(ends, i);
			if (blank(starts[i]) || blank(end)) {
				continue;
			}
			RetirementIncomeEntry entry = new RetirementIncomeEntry();
			entry.setDataType("SALARY");
			entry.setPeriodStartDate(date(starts[i]));
			entry.setPeriodEndDate(date(end));
			entry.setCalcDays((double) (ChronoUnit.DAYS.between(LocalDate.parse(starts[i]), LocalDate.parse(end)) + 1));
			entry.setAmount(longAt(amounts, i));
			entry.setThreeMonthAmount(0);
			form.getIncomeEntries().add(entry);
		}
	}

	// 퇴직급여정산 처리에 필요한 기타행 목록 데이터를 조회하여 반환한다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 退職給与精算処理に必要なその他行一覧データを照会して返す。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private void readOtherRows(HttpServletRequest req, RetirementBenefitForm form) {
		String[] months = req.getParameterValues("otherIncomeMonth");
		String[] names = req.getParameterValues("otherIncomeItem");
		String[] amounts = req.getParameterValues("otherIncomeAmount");
		String[] threeMonthAmounts = req.getParameterValues("threeMonthAmount");
		if (months == null) {
			return;
		}
		for (int i = 0; i < months.length; i++) {
			if (blank(months[i]) || blank(at(names, i))) {
				continue;
			}
			RetirementIncomeEntry entry = new RetirementIncomeEntry();
			entry.setDataType("ETC_INCOME");
			entry.setCalcDays(0d);
			entry.setPayYm(months[i].replace("-", ""));
			entry.setItemName(at(names, i));
			entry.setAmount(longAt(amounts, i));
			entry.setThreeMonthAmount(longAt(threeMonthAmounts, i));
			form.getIncomeEntries().add(entry);
		}
	}

	// 퇴직급여정산 처리에 필요한 과세이연 목록 데이터를 조회하여 반환한다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 退職給与精算処理に必要な課税繰延一覧データを照会して返す。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private void readDeferrals(HttpServletRequest req, RetirementBenefitForm form) {
		String[] names = req.getParameterValues("pensionProvider");
		String[] businessNumbers = req.getParameterValues("pensionBusinessNo");
		String[] accounts = req.getParameterValues("pensionAccount");
		String[] dates = req.getParameterValues("pensionDate");
		String[] amounts = req.getParameterValues("pensionAmount");
		if (names == null) {
			return;
		}
		for (int i = 0; i < names.length; i++) {
			if (blank(names[i]) || blank(at(accounts, i))) {
				continue;
			}
			RetirementTaxDeferral deferral = new RetirementTaxDeferral();
			deferral.setBizName(names[i]);
			deferral.setBizRegNo(at(businessNumbers, i));
			deferral.setAccountNo(at(accounts, i));
			deferral.setDepositDate(blank(at(dates, i)) ? null : date(at(dates, i)));
			deferral.setDepositAmt(longAt(amounts, i));
			form.getTaxDeferrals().add(deferral);
		}
	}

	// 퇴직급여정산 처리 후 필요한 조회조건을 URL에 포함하여 목록 화면으로 이동시킨다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 退職給与精算処理後、必要な検索条件をURLへ含めて一覧画面にリダイレクトする。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private void redirect(HttpServletRequest req, HttpServletResponse res,
			String result, Integer calculationId) throws IOException {
		String query = "?result=" + result;
		if (calculationId != null) {
			query += "&calculationId=" + calculationId;
		}
		res.sendRedirect(req.getContextPath() + "/retirement/benefit.do" + query);
	}

	// 목록 처리 후 필요한 조회조건을 URL에 포함하여 목록 화면으로 이동시킨다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 一覧処理後、必要な検索条件をURLへ含めて一覧画面にリダイレクトする。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private void redirectList(HttpServletRequest req, HttpServletResponse res) throws IOException {
		Integer paymentYear = parseInt(req.getParameter("paymentYear"));
		String query = paymentYear == null ? "" : "?mode=list&paymentYear=" + paymentYear;
		res.sendRedirect(req.getContextPath() + "/retirement/benefit.do" + query);
	}

	// 요청 문자열을 정리하고 필수값정수 처리에 필요한 안전한 값으로 변환한다.
	// 누락되거나 잘못된 파라미터가 500 오류로 이어지지 않도록 기본값과 형식 검사를 적용한다.
	// リクエスト文字列を整え、必須値整数処理に必要な安全な値へ変換する。
	// 不足または不正なパラメーターが500エラーにつながらないよう、初期値と形式検証を適用する。
	private int requiredInt(HttpServletRequest req, String name) {
		Integer value = parseInt(req.getParameter(name));
		if (value == null) {
			throw new IllegalArgumentException("사원을 선택해주세요");
		}
		return value;
	}

	// 요청 문자열을 정리하고 정수값 목록 처리에 필요한 안전한 값으로 변환한다.
	// 누락되거나 잘못된 파라미터가 500 오류로 이어지지 않도록 기본값과 형식 검사를 적용한다.
	// リクエスト文字列を整え、整数値一覧処理に必要な安全な値へ変換する。
	// 不足または不正なパラメーターが500エラーにつながらないよう、初期値と形式検証を適用する。
	private List<Integer> integerValues(String[] values) {
		List<Integer> result = new ArrayList<>();
		if (values == null) {
			return result;
		}
		for (String value : values) {
			Integer number = parseInt(value);
			if (number != null && !result.contains(number)) {
				result.add(number);
			}
		}
		return result;
	}

	// 계산Dates 입력값과 업무 처리 가능 여부를 검증한다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 計算Datesの入力値と業務処理の可否を検証する。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private void validateCalculationDates(RetirementBenefitForm form) {
		try {
			LocalDate.parse(form.getStartDate());
			LocalDate.parse(form.getEndDate());
		} catch (Exception e) {
			throw new IllegalArgumentException("정산 시작일과 종료일을 입력하세요.");
		}
	}

	// 입력 데이터를 정수 처리에 필요한 형식으로 변환한다.
	// 누락되거나 잘못된 파라미터가 500 오류로 이어지지 않도록 기본값과 형식 검사를 적용한다.
	// 入力データを整数処理に必要な形式へ変換する。
	// 不足または不正なパラメーターが500エラーにつながらないよう、初期値と形式検証を適用する。
	private Integer parseInt(String value) {
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
	private int intValue(String value) {
		Integer number = parseInt(value);
		return number == null ? 0 : number;
	}

	// 요청 문자열을 정리하고 정수값 처리에 필요한 안전한 값으로 변환한다.
	// 누락되거나 잘못된 파라미터가 500 오류로 이어지지 않도록 기본값과 형식 검사를 적용한다.
	// リクエスト文字列を整え、整数値処理に必要な安全な値へ変換する。
	// 不足または不正なパラメーターが500エラーにつながらないよう、初期値と形式検証を適用する。
	private long longValue(String value) {
		try {
			return Long.parseLong(value == null ? "0" : value.replace(",", "").trim());
		} catch (Exception e) {
			return 0;
		}
	}

	// 반복 전송된 요청값에서 지정한 위치의 값을 안전하게 읽고 필요한 자료형으로 변환한다.
	// 누락되거나 잘못된 파라미터가 500 오류로 이어지지 않도록 기본값과 형식 검사를 적용한다.
	// 繰り返し送信されたリクエスト値から指定位置の値を安全に取得し、必要な型へ変換する。
	// 不足または不正なパラメーターが500エラーにつながらないよう、初期値と形式検証を適用する。
	private Date date(String value) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			format.setLenient(false);
			return format.parse(value);
		} catch (ParseException e) {
			throw new IllegalArgumentException("날짜 형식을 확인하세요.");
		}
	}

	// 반복 전송된 요청값에서 지정한 위치의 값을 안전하게 읽고 필요한 자료형으로 변환한다.
	// 누락되거나 잘못된 파라미터가 500 오류로 이어지지 않도록 기본값과 형식 검사를 적용한다.
	// 繰り返し送信されたリクエスト値から指定位置の値を安全に取得し、必要な型へ変換する。
	// 不足または不正なパラメーターが500エラーにつながらないよう、初期値と形式検証を適用する。
	private boolean blank(String value) {
		return value == null || value.trim().isEmpty();
	}

	// 반복 전송된 요청값에서 지정한 위치의 값을 안전하게 읽고 필요한 자료형으로 변환한다.
	// 누락되거나 잘못된 파라미터가 500 오류로 이어지지 않도록 기본값과 형식 검사를 적용한다.
	// 繰り返し送信されたリクエスト値から指定位置の値を安全に取得し、必要な型へ変換する。
	// 不足または不正なパラメーターが500エラーにつながらないよう、初期値と形式検証を適用する。
	private String at(String[] values, int index) {
		return values != null && index < values.length ? values[index] : "";
	}

	// 반복 전송된 요청값에서 지정한 위치의 값을 안전하게 읽고 필요한 자료형으로 변환한다.
	// 누락되거나 잘못된 파라미터가 500 오류로 이어지지 않도록 기본값과 형식 검사를 적용한다.
	// 繰り返し送信されたリクエスト値から指定位置の値を安全に取得し、必要な型へ変換する。
	// 不足または不正なパラメーターが500エラーにつながらないよう、初期値と形式検証を適用する。
	private long longAt(String[] values, int index) {
		return longValue(at(values, index));
	}
}
