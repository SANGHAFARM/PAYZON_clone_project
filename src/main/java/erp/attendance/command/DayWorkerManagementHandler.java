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

	private DailyWorkEmployeeListService listService = new DailyWorkEmployeeListService();
	private DailyWorkInsertService insertService = new DailyWorkInsertService();
	private DailyWorkUpdateService updateService = new DailyWorkUpdateService();
	private EmployeeListService employeeListService = new EmployeeListService();
	private ProjectListService projectListService = new ProjectListService();

	// 요청 방식과 작업 구분을 확인하여 일용직근로자입력·관리 조회·저장 작업을 적절한 처리로 연결한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
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
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// 日雇い労働者入力・管理画面に必要なデータを照会してrequestへ保存し、JSPパスを返す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	private String processForm(HttpServletRequest req, HttpServletResponse res) throws Exception {

		// 사원별 근무기록 기능인지 확인
		// 入力条件と必須値を検証し、不正なデータが後続処理へ渡らないようにする。
		String employeeId = req.getParameter("employeeId");
		if (employeeId != null) {

			String editId = req.getParameter("editId");
			if (editId != null) {
				req.setAttribute("editId", editId);
				req.setAttribute("workDate", req.getParameter("workDate"));
				req.setAttribute("projectId", req.getParameter("projectId"));
				req.setAttribute("dailyPay", req.getParameter("dailyPay"));
				req.setAttribute("payRate", req.getParameter("payRate"));
				req.setAttribute("incomeTax", req.getParameter("incomeTax"));
				req.setAttribute("localIncomeTax", req.getParameter("localIncomeTax"));
				req.setAttribute("actualPay", req.getParameter("actualPay"));
			} 
			//사원별 근무기록
			else {
				req.setAttribute("employeeId", employeeId);
				DailyWorkRecordSearchRequest request = new DailyWorkRecordSearchRequest();
				String yearParam = req.getParameter("year");
				String monthParam = req.getParameter("month");
				int year = (yearParam != null && !yearParam.isEmpty()) ? Integer.parseInt(yearParam)
						: LocalDate.now().getYear();
				int month = (monthParam != null && !monthParam.isEmpty()) ? Integer.parseInt(monthParam)
						: LocalDate.now().getMonthValue();
				request.setEmployeeId(Integer.parseInt(employeeId));
				request.setYear(year);
				request.setMonth(month);
				
				req.setAttribute("year", year);
				req.setAttribute("month", month);
				List<DailyWorkRecordDto> workRecords = listService.getDailyWorkEmployee(request);
				
					req.setAttribute("workRecords", workRecords);
			}
		}
		String status = req.getParameter("status");
		if (status == null) {
			status = "재직";
		}
		req.setAttribute("status", status);
		String keyword = req.getParameter("keyword");
		req.setAttribute("keyword", keyword);

		List<DayWorkerDto> dayWorkers = employeeListService.getDayWorkerList(keyword, status);
		req.setAttribute("dayWorkers", dayWorkers);
		List<Project> projects = projectListService.getProjects();
		req.setAttribute("projects", projects);
		
		LocalDate today = LocalDate.now();
		req.setAttribute("today", today);

		return FORM_VIEW;

	}

	// 일용직근로자입력·관리 입력 요청을 검증한 뒤 서비스에 저장을 위임하고 처리 결과를 전달한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// 日雇い労働者入力・管理の入力リクエストを検証し、サービスへ保存を委譲して処理結果を渡す。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	private String processSubmit(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String deleteId = req.getParameter("deleteId");
		if (deleteId != null) {
			DailyWorkDeleteService dailyWorkDeleteService = new DailyWorkDeleteService();
			int no = Integer.parseInt(deleteId);
			dailyWorkDeleteService.delete(no);
			// 삭제 후 파라미터를유지한 채 원래 페이지로 리다이렉트 (모달 앵커 포함)
			// リクエストから入力値を取得し、空白除去と形式変換を行って業務処理へ渡す。
			String empId = req.getParameter("employeeId");
			String year = req.getParameter("year");
			String month = req.getParameter("month");

			res.sendRedirect(req.getContextPath() + "/attendance/day-worker-management.do?employeeId=" + empId
					+ "&year=" + year + "&month=" + month + "#work-history-" + empId);
			return null; // 리다이렉트를 직접 처리했으므로 뷰 경로는 null 리턴
		}
		String editId = req.getParameter("editId");
		Map<String, Boolean> errors = new HashMap<>();
		req.setAttribute("errors", errors);
		if (editId != null) {
			DailyWorkUpdateRequest request = createDailyWorkUpdateRequest(req, errors);
			request.validate(errors);
			if (!errors.isEmpty()) {
				return processForm(req, res); // <-수정해야 할지도
			}
			updateService.update(request);
		} else {
			DailyWorkInsertRequest request = createDailyWorkInsertRequest(req, errors);
			request.validate(errors);
			if (!errors.isEmpty()) {
				return processForm(req, res); // <-수정해야 할지도
			}
			insertService.insert(request);
		}
		return processForm(req, res);
	}

	// 일용직근로자입력·관리 처리에 사용할 일용직근무Update요청정보 데이터나 객체를 생성한다.
	// 화면에서 전달된 값과 현재 조회조건을 유지하면서 필요한 request 속성 또는 이동 URL을 구성한다.
	// 日雇い労働者入力・管理処理で使用する日雇い勤務Updateリクエスト情報データまたはオブジェクトを生成する。
	// 画面から渡された値と現在の検索条件を維持しながら、必要なrequest属性または遷移URLを構成する。
	private DailyWorkUpdateRequest createDailyWorkUpdateRequest(HttpServletRequest req, Map<String, Boolean> errors) {
		DailyWorkUpdateRequest request = new DailyWorkUpdateRequest();
		request.setDailyWorkRecordId(Integer.parseInt(req.getParameter("editId")));
		String date = req.getParameter("workDate");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date workDate = null;
		try {
			workDate = formatter.parse(date);
		} catch (ParseException e) {
			errors.put("ParseException", Boolean.TRUE);
		}

		request.setWorkDate(workDate);
		request.setProjectId(Integer.parseInt(req.getParameter("projectId")));
		
		String dailyPayStr = req.getParameter("dailyPay");
		long dailyPay = (dailyPayStr!=null&&!dailyPayStr.trim().isEmpty())?Long.parseLong(dailyPayStr):0L;
		request.setDailyPay(dailyPay);
		
		String payRateStr = req.getParameter("payRate");
		double payRate = (payRateStr!=null&&!payRateStr.trim().isEmpty())?Double.parseDouble(payRateStr):0.0;
		request.setPayRate(payRate);

		String incomeTaxStr = req.getParameter("incomeTax").replaceAll(",", "");
		String localIncomTaxStr = req.getParameter("localIncomeTax").replaceAll(",", "");
		String actualPayStr = req.getParameter("actualPay").replaceAll(",", "");
		Long incomTax = (incomeTaxStr!=null&&!incomeTaxStr.trim().isEmpty())?Long.parseLong(incomeTaxStr):0L;
		Long localIncomeTax = (localIncomTaxStr!=null&&!localIncomTaxStr.trim().isEmpty())?Long.parseLong(localIncomTaxStr):0L;
		Long actualPay = (actualPayStr!=null&&!actualPayStr.trim().isEmpty())?Long.parseLong(actualPayStr):0L;
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
		DailyWorkInsertRequest request = new DailyWorkInsertRequest();
		// 사원id 목록을 문자열 배열로 받기
		// 社員の識別情報と在職・雇用・所属条件を確認し、対象社員データへ反映する。
		String[] employeeIdsStr = req.getParameterValues("employeeIds");

		// 문자열 배열을 List<Integer>로 변환
		// 画面から送信された繰り返し入力値を型変換し、保存可能なオブジェクト一覧として構成する。
		List<Integer> employeeIds = new ArrayList<>();
		if (employeeIdsStr != null) {
			for (String idStr : employeeIdsStr) {
				employeeIds.add(Integer.parseInt(idStr));
			}
		}
		String date = req.getParameter("workDate");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date workDate = null;
		try {
			workDate = formatter.parse(date);
		} catch (ParseException e) {
			errors.put("ParseException", Boolean.TRUE);
		}
		request.setEmployeeIds(employeeIds);
		request.setWorkDate(workDate);
		request.setProjectId(Integer.parseInt(req.getParameter("projectId")));
		
		String dailyPayStr = req.getParameter("dailyPay");
		long dailyPay = (dailyPayStr!=null&&!dailyPayStr.trim().isEmpty())?Long.parseLong(dailyPayStr):0L;
		request.setDailyPay(dailyPay);
		
		String payRateStr = req.getParameter("payRate");
		double payRate = (payRateStr!=null&&!payRateStr.trim().isEmpty())?Double.parseDouble(payRateStr):0.0;
		request.setPayRate(payRate);

		String incomeTaxStr = req.getParameter("incomeTax").replaceAll(",", "");
		String localIncomTaxStr = req.getParameter("localIncomeTax").replaceAll(",", "");
		String actualPayStr = req.getParameter("actualPay").replaceAll(",", "");
		Long incomTax = (incomeTaxStr!=null&&!incomeTaxStr.trim().isEmpty())?Long.parseLong(incomeTaxStr):0L;
		Long localIncomeTax = (localIncomTaxStr!=null&&!localIncomTaxStr.trim().isEmpty())?Long.parseLong(localIncomTaxStr):0L;
		Long actualPay = (actualPayStr!=null&&!actualPayStr.trim().isEmpty())?Long.parseLong(actualPayStr):0L;
		request.setIncomeTax(incomTax);
		request.setLocalIncomeTax(localIncomeTax);
		request.setActualPay(actualPay);

		return request;
	}

}
