<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%
	/*
	 * ================================================================
	 * 미리보기 및 백엔드 연동 예시용 더미 데이터
	 * ================================================================
	 *
	 * [실제 구현 방법]
	 * 1. 사원선택 팝업에서 선택한 사원번호를 employeeNo로 전달합니다.
	 * 2. 선택한 귀속연도 baseYear를 포함한 최근 10개년 급여를 조회합니다.
	 *    예: 2026년 선택 시 2017년부터 2026년까지 조회
	 * 3. 급여 자료가 없는 연도도 값이 0인 항목으로 포함합니다.
	 * 4. 컨트롤러에서 아래 이름으로 request 속성을 전달합니다.
	 *
	 *    availableYears     : 귀속연도 선택 상자에 표시할 연도 목록
	 *    selectedYear       : 현재 선택된 귀속연도
	 *    selectedEmployeeNo : 선택된 사원번호
	 *    selectedEmployeeName: 선택된 사원명
	 *    employeeOptions    : 사원선택 팝업에 표시할 사원 목록
	 *    salaryStats        : 선택 사원의 최근 10개년 연봉 통계 목록
	 *
	 * [salaryStats 항목별 필드]
	 * year              : 귀속연도
	 * annualSalaryText  : 화면에 표시할 연봉액 문자열
	 * salaryGrowth      : 전년 대비 증가율 색상 판별용 숫자
	 * salaryGrowthText  : 화면에 표시할 증가율 문자열
	 * deductionText     : 화면에 표시할 공제금액 문자열
	 * netSalaryText     : 화면에 표시할 실지급액 문자열
	 * salaryBarRate       : 연봉 총액 막대 높이 비율(0~100)
	 * deductionShareRate  : 연봉 총액에서 공제금액이 차지하는 비율(0~100)
	 *
	 * salaryBarRate는 10개년 연봉 최댓값을 100으로 환산해 전달합니다.
	 * deductionShareRate는 공제금액 / 연봉액 × 100으로 계산합니다.
	 * 실제 salaryStats가 전달되면 아래 더미 데이터는 실행되지 않습니다.
	 * 백엔드 연동 완료 후에는 이 블록 전체를 제거해도 됩니다.
	 */
	if (request.getAttribute("salaryStats") == null) {
		int[] years = {2017, 2018, 2019, 2020, 2021, 2022, 2023, 2024, 2025, 2026};
		String[] salaries = {"31,200", "31,200", "31,200", "31,200", "31,200", "31,200", "32,400", "32,400", "32,400", "32,400"};
		double[] growths = {0, 0, 0, 0, 0, 0, 3.8, 0, 0, 0};
		String[] growthTexts = {"-", "0.0%", "0.0%", "0.0%", "0.0%", "0.0%", "3.8%", "0.0%", "0.0%", "0.0%"};
		String[] deductions = {"3,070", "3,099", "3,145", "3,145", "3,286", "3,316", "3,594", "3,291", "3,291", "3,384"};
		String[] netSalaries = {"28,130", "28,101", "28,055", "28,055", "27,914", "27,884", "28,806", "29,109", "29,109", "29,016"};
		int[] salaryBars = {96, 96, 96, 96, 96, 96, 100, 100, 100, 100};
		int[] deductionShares = {10, 10, 10, 10, 11, 11, 11, 10, 10, 10};

		List<Map<String, Object>> previewStats = new ArrayList<>();
		for (int i = 0; i < years.length; i++) {
			Map<String, Object> stat = new LinkedHashMap<>();
			stat.put("year", years[i]);
			stat.put("annualSalaryText", salaries[i]);
			stat.put("salaryGrowth", growths[i]);
			stat.put("salaryGrowthText", growthTexts[i]);
			stat.put("deductionText", deductions[i]);
			stat.put("netSalaryText", netSalaries[i]);
			stat.put("salaryBarRate", salaryBars[i]);
			stat.put("deductionShareRate", deductionShares[i]);
			previewStats.add(stat);
		}

		List<Map<String, String>> previewEmployees = new ArrayList<>();
		String[][] employeeRows = {
			{"No-140034", "정규직", "이용열", "사장실", "사장", "재직"},
			{"No-140001", "정규직", "김용", "콘텐츠팀", "사원", "재직"},
			{"No-140036", "계약직", "이영희", "콘텐츠팀", "사원", "재직"},
			{"No-140035", "정규직", "이수진", "디자인팀", "대리", "재직"}
		};
		for (String[] row : employeeRows) {
			Map<String, String> employee = new LinkedHashMap<>();
			employee.put("employeeNo", row[0]);
			employee.put("type", row[1]);
			employee.put("name", row[2]);
			employee.put("department", row[3]);
			employee.put("position", row[4]);
			employee.put("status", row[5]);
			previewEmployees.add(employee);
		}

		request.setAttribute("salaryStats", previewStats);
		request.setAttribute("employeeOptions", previewEmployees);
		request.setAttribute("availableYears", Arrays.asList(2017, 2018, 2019, 2020, 2021, 2022, 2023, 2024, 2025, 2026));
		request.setAttribute("selectedYear", 2026);
		request.setAttribute("selectedEmployeeNo", "No-140034");
		request.setAttribute("selectedEmployeeName", "이용열");
	}
	/* 미리보기 및 백엔드 연동 예시용 더미 데이터 끝 */
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>연도별 개인연봉 통계</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/common.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/payzon-ui.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/payroll-stats/annual-payroll-statistics.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/payroll-stats/personal-annual-salary-statistics.css">
</head>
<body>
	<%@ include file="/WEB-INF/view/common/header.jspf" %>

	<main class="page-content annual-stat-page personal-salary-page">
		<header class="page-heading">
			<div><p>급여통계</p><h1>연도별 개인연봉 통계</h1></div>
		</header>

		<section class="content-card">
			<form class="search-bar personal-search-bar" method="get">
				<div class="search-bar__controls">
					<label for="baseYear">귀속연도</label>
					<select id="baseYear" name="baseYear">
						<option value="">선택</option>
						<c:forEach var="year" items="${availableYears}">
							<option value="${year}" <c:if test="${year eq selectedYear}">selected</c:if>>${year}년</option>
						</c:forEach>
					</select>
					<label for="employeeName">대상자</label>
					<input type="hidden" name="employeeNo" value="${selectedEmployeeNo}">
					<input id="employeeName" class="employee-name-field" type="text" value="${selectedEmployeeName}" placeholder="사원을 선택해 주세요." readonly>
					<a class="ui-button ui-button--outline employee-select-link" href="#employeeSelectModal">사원선택</a>
					<button type="submit" class="ui-button ui-button--primary">조회</button>
				</div>
			</form>

			<section class="chart-panel">
				<div class="section-title-row">
					<h2 class="sr-only">연도별 개인연봉 차트</h2>
					<div class="chart-legend">
						<span><i class="net-legend"></i>실지급액</span>
						<span><i class="deduction-legend"></i>공제금액</span>
					</div>
				</div>
				<div class="annual-chart salary-chart">
					<c:forEach var="stat" items="${salaryStats}">
						<div class="annual-chart__item" tabindex="0">
							<div class="annual-chart__plot salary-chart__plot">
								<div class="salary-chart__stack" style="height:${stat.salaryBarRate}%">
									<div class="salary-chart__net" style="height:${100 - stat.deductionShareRate}%">
										<span>${stat.netSalaryText}</span>
									</div>
									<div class="salary-chart__deduction" style="height:${stat.deductionShareRate}%">
										<span>${stat.deductionText}</span>
									</div>
								</div>
							</div>
							<strong>${stat.year}</strong>
							<div class="annual-chart__tooltip" role="tooltip">
								<b>${stat.year}년 · ${selectedEmployeeName}</b>
								<span><i class="tooltip-dot tooltip-dot--net"></i>실지급액 (천원) <em>${stat.netSalaryText}</em></span>
								<span><i class="tooltip-dot tooltip-dot--deduction"></i>공제금액 (천원) <em>${stat.deductionText}</em></span>
							</div>
						</div>
					</c:forEach>
				</div>
			</section>

			<div class="statistics-table-wrap">
				<table class="statistics-table personal-salary-table">
					<caption>${selectedEmployeeName} 사원의 최근 10개년도 개인연봉 현황</caption>
					<thead><tr><th scope="col">구분</th><c:forEach var="stat" items="${salaryStats}"><th scope="col">${stat.year}년</th></c:forEach></tr></thead>
					<tbody>
						<tr class="statistics-table__main-row"><th scope="row">연봉액 (천원)</th><c:forEach var="stat" items="${salaryStats}"><td>${stat.annualSalaryText}</td></c:forEach></tr>
						<tr class="statistics-table__rate-row"><th scope="row">└ 증가율</th><c:forEach var="stat" items="${salaryStats}"><td class="${stat.salaryGrowth gt 0 ? 'rate-up' : stat.salaryGrowth lt 0 ? 'rate-down' : ''}">${stat.salaryGrowthText}</td></c:forEach></tr>
						<tr><th scope="row">공제금액 (천원)</th><c:forEach var="stat" items="${salaryStats}"><td>${stat.deductionText}</td></c:forEach></tr>
						<tr class="statistics-table__main-row"><th scope="row">실지급액 (천원)</th><c:forEach var="stat" items="${salaryStats}"><td>${stat.netSalaryText}</td></c:forEach></tr>
					</tbody>
				</table>
			</div>
		</section>
	</main>

	<div id="employeeSelectModal" class="css-modal" role="dialog" aria-modal="true" aria-labelledby="employee-modal-title">
		<a href="#" class="css-modal__backdrop" aria-label="팝업 닫기"></a>
		<section class="css-modal__dialog employee-modal">
			<header class="css-modal__header"><h2 id="employee-modal-title">개인연봉 조회 사원선택</h2><a href="#" class="css-modal__close" aria-label="닫기">×</a></header>
			<form method="get">
				<div class="employee-modal__search"><input type="text" name="employeeKeyword" placeholder="사원검색"><button type="submit" class="ui-button ui-button--primary">검색</button></div>
				<div class="employee-modal__table-wrap">
					<table class="employee-modal__table">
						<thead><tr><th>선택</th><th>구분</th><th>사원번호</th><th>성명</th><th>부서</th><th>직위</th><th>상태</th></tr></thead>
						<tbody><c:forEach var="employee" items="${employeeOptions}"><tr><td><input type="radio" name="employeeNo" value="${employee.employeeNo}"></td><td>${employee.type}</td><td>${employee.employeeNo}</td><td>${employee.name}</td><td>${employee.department}</td><td>${employee.position}</td><td>${employee.status}</td></tr></c:forEach></tbody>
					</table>
				</div>
				<div class="employee-modal__actions"><button type="submit" class="ui-button ui-button--primary">사원선택</button><a href="#" class="ui-button ui-button--secondary">선택취소</a></div>
			</form>
		</section>
	</div>

	<%@ include file="/WEB-INF/view/common/footer.jspf" %>
</body>
</html>
