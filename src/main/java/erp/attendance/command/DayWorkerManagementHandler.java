package erp.attendance.command;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.attendance.dto.DailyWorkRecordDto;
import erp.attendance.model.Project;
import erp.attendance.service.DailyWorkDeleteService;
import erp.attendance.service.DailyWorkEmployeeListService;
import erp.attendance.service.DailyWorkInsertService;
import erp.attendance.service.DailyWorkUpdateService;
import erp.attendance.service.ProjectListService;
import erp.attendance.service.request.DailyWorkRecordSearchRequest;
import erp.attendance.service.request.DailyWorkInsertRequest;
import erp.attendance.service.request.DailyWorkUpdateRequest;
import erp.employees.dto.DayWorkerDto;
import erp.employees.service.EmployeeListService;
import mvc.command.CommandHandler;

// 일용직근로자입력·관리 화면의 HTTP 요청을 구분하고 적절한 서비스 호출과 화면 이동을 담당한다.
// 日雇い労働者入力・管理画面のHTTPリクエストを判別し、適切なサービス呼び出しと画面遷移を担当する。
public class DayWorkerManagementHandler implements CommandHandler {
	private static final String FORM_VIEW = "/WEB-INF/view/attendance/day-worker-management.jsp";

	// 일용직 사원 근무 기록 조회 서비스
	private DailyWorkEmployeeListService listService = new DailyWorkEmployeeListService();
	// 일용직 사원 근무 기록 입력 서비스
	private DailyWorkInsertService insertService = new DailyWorkInsertService();
	// 일용직 사원 근무 기록 수정 서비스
	private DailyWorkUpdateService updateService = new DailyWorkUpdateService();
	// 일용직 사원 근무 기록 삭제 서비스
	private DailyWorkDeleteService dailyWorkDeleteService = new DailyWorkDeleteService();
	// 일용직 사원 조회 서비스
	private EmployeeListService employeeListService = new EmployeeListService();
	// 현장, 프로젝트 목록 조회 서비스
	private ProjectListService projectListService = new ProjectListService();

	// 요청 방식과 작업 구분을 확인하여 일용직근로자입력·관리 조회·저장 작업을 적절한 처리로 연결한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트
	// 경로를 결정한다.
	// リクエスト方式と処理区分を確認し、日雇い労働者入力・管理の照会・保存処理へ適切に振り分ける。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		if (req.getMethod().equalsIgnoreCase("GET")) {
			return processForm(req, res);
		} else if (req.getMethod().equalsIgnoreCase("POST")) {
			return processSubmit(req, res);
		} else {
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
	}

	// 일용직근로자입력·관리 화면에 필요한 데이터를 조회하여 request에 저장하고 JSP 경로를 반환한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트
	// 경로를 결정한다.
	// 日雇い労働者入力・管理画面に必要なデータを照会してrequestへ保存し、JSPパスを返す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	private String processForm(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// 사원별 근무기록 기능인지 확인
		// 社員別勤務記録機能なのか確認
		String employeeId = req.getParameter("employeeId");
		// 사원ID가 존재하면 수정모드 또는 근무기록 조회 모드
		// 社員IDがあったら、修正モードまたは勤務記録照会モード
		if (employeeId != null) {
			//request영역에 수정할 근무 기록ID 추가
			String editId = req.getParameter("editId");
			// 수정 모드
			// 修正モード
			if (editId != null) {
				// 근무기록ID와 근무기록 정보를 request 영역에 저장
				// 勤務記録IDと勤務記録情報をリクエストスコープに保存
				req.setAttribute("editId", editId);	//수정할 근무기록 ID(修正する勤務記録ID)
				req.setAttribute("workDate", req.getParameter("workDate"));//근무일(勤務日)
				req.setAttribute("projectId", req.getParameter("projectId"));//프로젝트ID(プロジェクトID)
				req.setAttribute("dailyPay", req.getParameter("dailyPay"));//일당(日当)
				req.setAttribute("payRate", req.getParameter("payRate"));//지급율(支給率)
				req.setAttribute("incomeTax", req.getParameter("incomeTax"));//소득세(所得税)
				req.setAttribute("localIncomeTax", req.getParameter("localIncomeTax"));//지방소득세(地方所得税)
				req.setAttribute("actualPay", req.getParameter("actualPay"));//실지급액(差引支給額)
			}
			// 사원별 근무기록
			// 社員別勤務記録
			else {
				// 사원ID를 request영역에 저장
				// 社員IDをリクエストスコープに保存
				req.setAttribute("employeeId", employeeId);

				// 연월 파라미터 확인 및 request 영역에 저장
				// 年月パラメータ確認とリクエストスコープに保存
				String yearParam = req.getParameter("year");
				String monthParam = req.getParameter("month");
				int year = (yearParam != null && !yearParam.isEmpty()) ? Integer.parseInt(yearParam)
						: LocalDate.now().getYear();
				int month = (monthParam != null && !monthParam.isEmpty()) ? Integer.parseInt(monthParam)
						: LocalDate.now().getMonthValue();
				// 연월 파라미터가 null인 경우, 오늘 날짜로 설정
				// 年月パラメータがnullの場合、今日の日付に設定
				req.setAttribute("year", year);
				req.setAttribute("month", month);

				// 개별 근무 기록 조회 요청 객체 생성 및 값 세팅
				// 個人勤務記録照会要請オブジェクト生成と値セット
				DailyWorkRecordSearchRequest request = new DailyWorkRecordSearchRequest();
				request.setEmployeeId(Integer.parseInt(employeeId));
				request.setYear(year);
				request.setMonth(month);

				// 요청 객체를 기반으로 개별 근무 기록 조회하고 request 영역에 저장
				// 要請オブジェクトを基に個人勤務記録を照会しリクエストスコープに保存
				List<DailyWorkRecordDto> workRecords = listService.getDailyWorkEmployee(request);
				req.setAttribute("workRecords", workRecords);
			}
		}
		// 기본 모드
		// 基本モード
		
		// 1. 검색란 설정, 재직, 검색 키워드 파라미터 확인 후 request영역에 저장
		// 1. 検索欄設定、在職、検索キーワードパラメータを確認し、リクエストスコープに保存
		String status = req.getParameter("status");
		// 재직 상태 파라미터가 null인 경우, 기본값 "재직"으로 설정
		// 在職ステータスパラメータがnullの場合、デフォルト値を「在職」に設定
		if (status == null) {
			status = "재직";
		}
		String keyword = req.getParameter("keyword");
		req.setAttribute("status", status);
		req.setAttribute("keyword", keyword);

		// 2. 조건에 맞는 일용직 사원 목록을 조회하고 request 영역에 저장
		// 2. 条件に合う日雇い社員目録を照会し、リクエストスコープに保存
		List<DayWorkerDto> dayWorkers = employeeListService.getDayWorkerList(keyword, status);
		req.setAttribute("dayWorkers", dayWorkers);

		// 3. 현장, 프로젝트 목록을 조회하고 request 영역에 저장
		// 3. 現場、プロジェクト目録を照会し、リクエストスコープに保存
		List<Project> projects = projectListService.getProjects();
		req.setAttribute("projects", projects);

		// 4. 오늘 날짜를 request영역에 저장
		// 4. 今日の日付をリクエストスコープに保存
		LocalDate today = LocalDate.now();
		req.setAttribute("today", today);

		return FORM_VIEW;

	}

	// 일용직근로자입력·관리 입력 요청을 검증한 뒤 서비스에 저장을 위임하고 처리 결과를 전달한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트
	// 경로를 결정한다.
	// 日雇い労働者入力・管理の入力リクエストを検証し、サービスへ保存を委譲して処理結果を渡す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	private String processSubmit(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// 삭제 작업
		// 削除作業
		String deleteId = req.getParameter("deleteId");
		if (deleteId != null) {
			// 근무기록ID를 기반으로 삭제 서비스 수행 후 리다이렉트
			// 勤務記録IDを基に削除サービスを修行してからリダイレクト
			int no = Integer.parseInt(deleteId);
			dailyWorkDeleteService.delete(no);
			String empId = req.getParameter("employeeId");
			String year = req.getParameter("year");
			String month = req.getParameter("month");

			res.sendRedirect(req.getContextPath() + "/attendance/day-worker-management.do?employeeId=" + empId
					+ "&year=" + year + "&month=" + month + "#work-history-" + empId);
			return null;
		}

		// 수정 작업
		// 修正作業
		String editId = req.getParameter("editId");
		Map<String, Boolean> errors = new HashMap<>();
		req.setAttribute("errors", errors);
		if (editId != null) {
			// 일용직 근무 기록 수정 요청 객체 생성
			// 日雇い勤務記録修正要請オブジェクト生成
			DailyWorkUpdateRequest request = createDailyWorkUpdateRequest(req, errors);
			request.validate(errors);
			// 필수값에 문제가 있을 경우 기본화면으로 돌려보냄
			// 必須値に問題がある場合、基本画面に返す
			if (!errors.isEmpty()) {
				return processForm(req, res);
			}
			// 수정작업 수행
			// 修正作業修行
			updateService.update(request);
			// request영역에 결과 저장
			// リクエストスコープに結果保存
			req.setAttribute("successMessage", "修正しました");
		}
		// 입력 작업
		// 入力作業
		else {
			// 일용직 근무 기록 입력 요청 객체 생성
			// 日雇い勤務記録入力要請オブジェクト生成
			DailyWorkInsertRequest request = createDailyWorkInsertRequest(req, errors);
			request.validate(errors);
			// 필수값에 문제가 있을 경우 기본화면으로 돌려보냄
			// 必須値に問題がある場合、基本画面に返す
			if (!errors.isEmpty()) {
				return processForm(req, res);
			}
			// 입력작업 수행
			// 入力作業修行
			insertService.insert(request);
			// request영역에 결과 저장
			// リクエストスコープに結果保存
			req.setAttribute("successMessage", "保存しました");
		}
		return processForm(req, res);
	}

	// 일용직근로자입력·관리 처리에 사용할 일용직근무Update요청정보 데이터나 객체를 생성한다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 日雇い労働者入力・管理処理で使用する日雇い勤務Updateリクエスト情報データまたはオブジェクトを生成する。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private DailyWorkUpdateRequest createDailyWorkUpdateRequest(HttpServletRequest req, Map<String, Boolean> errors) {
		// 1. 일용직 근무 수정 요청 객체 생성
		// 1. 日雇い勤務記録修正要請オブジェクト生成
		DailyWorkUpdateRequest request = new DailyWorkUpdateRequest();

		// 2. 파라미터 분석
		// 2. パラメータ分析
		int editId = Integer.parseInt(req.getParameter("editId")); // 삭제할 근무기록 ID(削除する勤務記録ID)
		String date = req.getParameter("workDate"); // 근무일(勤務日)
		String proejctIdStr = req.getParameter("projectId"); // 현장,프로젝트ID(現場・プロジェクトID)
		String dailyPayStr = req.getParameter("dailyPay"); // 일당(日当)
		String payRateStr = req.getParameter("payRate"); // 지급율(支給率)
		String incomeTaxStr = req.getParameter("incomeTax").replaceAll(",", "");// 소득세(所得税)
		String localIncomTaxStr = req.getParameter("localIncomeTax").replaceAll(",", "");// 지방소득세(地方所得税)
		String actualPayStr = req.getParameter("actualPay").replaceAll(",", "");// 실지급액(差引支給額)

		// 3. 요청 객체에 값 세팅
		// 3. 要請オブジェクトに値セット
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date workDate = null;
		try {
			workDate = formatter.parse(date);
		} catch (ParseException e) {
			errors.put("ParseException", Boolean.TRUE);
		}
		long dailyPay = (dailyPayStr != null && !dailyPayStr.trim().isEmpty()) ? Long.parseLong(dailyPayStr) : 0L;
		double payRate = (payRateStr != null && !payRateStr.trim().isEmpty()) ? Double.parseDouble(payRateStr) : 0.0;

		Long incomTax = (incomeTaxStr != null && !incomeTaxStr.trim().isEmpty()) ? Long.parseLong(incomeTaxStr) : 0L;
		Long localIncomeTax = (localIncomTaxStr != null && !localIncomTaxStr.trim().isEmpty())
				? Long.parseLong(localIncomTaxStr)
				: 0L;
		Long actualPay = (actualPayStr != null && !actualPayStr.trim().isEmpty()) ? Long.parseLong(actualPayStr) : 0L;

		request.setDailyWorkRecordId(editId);
		request.setWorkDate(workDate);
		request.setProjectId(
				(proejctIdStr != null && !proejctIdStr.trim().isEmpty()) ? Integer.parseInt(proejctIdStr) : null);
		request.setDailyPay(dailyPay);
		request.setPayRate(payRate);
		request.setIncomeTax(incomTax);
		request.setLocalIncomeTax(localIncomeTax);
		request.setActualPay(actualPay);

		return request;
	}

	// 일용직근로자입력·관리 처리에 사용할 일용직근무Insert요청정보 데이터나 객체를 생성한다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 日雇い労働者入力・管理処理で使用する日雇い勤務Insertリクエスト情報データまたはオブジェクトを生成する。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private DailyWorkInsertRequest createDailyWorkInsertRequest(HttpServletRequest req, Map<String, Boolean> errors) {
		// 1. 일용직 근무 입력 요청 객체 생성
		// 1. 日雇い勤務記録入力要請オブジェクト生成
		DailyWorkInsertRequest request = new DailyWorkInsertRequest();

		
		// 2.파라미터 분석
		// 2. パラメータ分析
		String[] employeeIdsStr = req.getParameterValues("employeeIds");		// 사원id 목록을 문자열 배열로(社員ID目録を文字列配列に)
		String date = req.getParameter("workDate");//근무일(勤務日)
		String proejctIdStr = req.getParameter("projectId");// 현장,프로젝트ID(現場・プロジェクトID)
		String dailyPayStr = req.getParameter("dailyPay");// 일당(日当)
		String payRateStr = req.getParameter("payRate");// 지급율(支給率)
		String incomeTaxStr = req.getParameter("incomeTax").replaceAll(",", "");// 소득세(所得税)
		String localIncomTaxStr = req.getParameter("localIncomeTax").replaceAll(",", "");// 지방소득세(地方所得税)
		String actualPayStr = req.getParameter("actualPay").replaceAll(",", "");// 실지급액(差引支給額)
		
		//3. 요청 객체에 값 세팅
		//3. 要請オブジェクトに値セット
		// 문자열 배열을 List<Integer>로 변환
		// 文字列配列をList<Integer>に変換
		List<Integer> employeeIds = new ArrayList<>();
		if (employeeIdsStr != null) {
			for (String idStr : employeeIdsStr) {
				employeeIds.add(Integer.parseInt(idStr));
			}
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date workDate = null;
		try {
			workDate = formatter.parse(date);
		} catch (ParseException e) {
			errors.put("ParseException", Boolean.TRUE);
		}
		long dailyPay = (dailyPayStr != null && !dailyPayStr.trim().isEmpty()) ? Long.parseLong(dailyPayStr) : 0L;
		double payRate = (payRateStr != null && !payRateStr.trim().isEmpty()) ? Double.parseDouble(payRateStr) : 0.0;
		Long incomTax = (incomeTaxStr != null && !incomeTaxStr.trim().isEmpty()) ? Long.parseLong(incomeTaxStr) : 0L;
		Long localIncomeTax = (localIncomTaxStr != null && !localIncomTaxStr.trim().isEmpty())
				? Long.parseLong(localIncomTaxStr)
				: 0L;
		Long actualPay = (actualPayStr != null && !actualPayStr.trim().isEmpty()) ? Long.parseLong(actualPayStr) : 0L;
		
		request.setEmployeeIds(employeeIds);
		request.setWorkDate(workDate);
		request.setProjectId(
				(proejctIdStr != null && !proejctIdStr.trim().isEmpty()) ? Integer.parseInt(proejctIdStr) : null);
		request.setDailyPay(dailyPay);
		request.setPayRate(payRate);
		request.setIncomeTax(incomTax);
		request.setLocalIncomeTax(localIncomeTax);
		request.setActualPay(actualPay);

		return request;
	}

}
