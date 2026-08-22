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
	 * 1. 화면에서 선택한 귀속연도를 baseYear 파라미터로 전달합니다.
	 * 2. 선택 연도를 포함한 최근 10개년 자료를 오름차순으로 조회합니다.
	 *    예: 2026년 선택 시 2017년부터 2026년까지 조회
	 * 3. 컨트롤러에서 아래 이름으로 request 속성을 전달합니다.
	 *
	 *    availableYears     : 귀속연도 선택 상자에 표시할 연도 목록
	 *    selectedYear       : 현재 선택된 귀속연도
	 *    statisticsStartYear: 조회된 통계의 시작 연도
	 *    statisticsEndYear  : 조회된 통계의 마지막 연도
	 *    annualStats        : 최근 10개년 통계 목록
	 *
	 * [annualStats 항목별 필드]
	 * year                 : 귀속연도
	 * totalPayrollText     : 화면에 표시할 전체 급여액 문자열
	 * payrollGrowth        : 급여 증가율 색상 판별용 숫자
	 * payrollGrowthText    : 화면에 표시할 급여 증가율 문자열
	 * headcountText        : 화면에 표시할 인원 문자열
	 * headcountGrowth      : 인원 증가율 색상 판별용 숫자
	 * headcountGrowthText  : 화면에 표시할 인원 증가율 문자열
	 * payrollBarRate       : 전체 급여액 막대 높이 비율(0~100)
	 * headcountBarRate     : 인원 막대 높이 비율(0~100)
	 *
	 * payrollBarRate와 headcountBarRate는 각각의 최댓값을 100으로
	 * 환산해서 컨트롤러에서 계산한 뒤 전달합니다.
	 *
	 * 실제 annualStats가 전달되면 아래 더미 데이터는 실행되지 않습니다.
	 * 백엔드 연동 완료 후에는 이 주석과 더미 데이터 블록 전체를 제거해도 됩니다.
	 */
	if (request.getAttribute("annualStats") == null) {
		int[] years = {2017, 2018, 2019, 2020, 2021, 2022, 2023, 2024, 2025, 2026};
		String[] payrolls = {"350,122", "307,200", "307,200", "307,200", "307,200", "307,200", "315,600", "316,800", "316,800", "316,800"};
		double[] payrollGrowths = {0, -12.3, 0, 0, 0, 0, 2.7, 0.4, 0, 0};
		String[] payrollGrowthTexts = {"-", "-12.3%", "0.0%", "0.0%", "0.0%", "0.0%", "2.7%", "0.4%", "0.0%", "0.0%"};
		String[] headcounts = {"10.8", "8.0", "8.0", "8.0", "8.0", "8.0", "8.0", "8.0", "8.0", "8.0"};
		double[] headcountGrowths = {0, -25.6, 0, 0, 0, 0, 0, 0, 0, 0};
		String[] headcountGrowthTexts = {"-", "-25.6%", "0.0%", "0.0%", "0.0%", "0.0%", "0.0%", "0.0%", "0.0%", "0.0%"};
		int[] payrollBars = {100, 88, 88, 88, 88, 88, 90, 91, 91, 91};
		int[] headcountBars = {72, 53, 53, 53, 53, 53, 53, 53, 53, 53};

		List<Map<String, Object>> previewStats = new ArrayList<>();
		for (int i = 0; i < years.length; i++) {
			Map<String, Object> stat = new LinkedHashMap<>();
			stat.put("year", years[i]);
			stat.put("totalPayrollText", payrolls[i]);
			stat.put("payrollGrowth", payrollGrowths[i]);
			stat.put("payrollGrowthText", payrollGrowthTexts[i]);
			stat.put("headcountText", headcounts[i]);
			stat.put("headcountGrowth", headcountGrowths[i]);
			stat.put("headcountGrowthText", headcountGrowthTexts[i]);
			stat.put("payrollBarRate", payrollBars[i]);
			stat.put("headcountBarRate", headcountBars[i]);
			previewStats.add(stat);
		}

		request.setAttribute("annualStats", previewStats);
		request.setAttribute("availableYears", Arrays.asList(2017, 2018, 2019, 2020, 2021, 2022, 2023, 2024, 2025, 2026));
		request.setAttribute("selectedYear", 2026);
		request.setAttribute("statisticsStartYear", 2017);
		request.setAttribute("statisticsEndYear", 2026);
	}
	/* 미리보기 및 백엔드 연동 예시용 더미 데이터 끝 */
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>연도별 전체급여 통계</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/common.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/payzon-ui.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/payroll-stats/annual-payroll-statistics.css">
</head>
<body>
	<%@ include file="/WEB-INF/view/common/header.jspf" %>

	<main class="page-content annual-stat-page">
		<header class="page-heading">
			<div>
				<p>급여통계</p>
				<h1>연도별 전체급여 통계</h1>
			</div>
		</header>

		<section class="content-card" aria-labelledby="annual-stat-title">
			<h2 id="annual-stat-title" class="sr-only">연도별 전체급여 통계 조회</h2>

			<form class="search-bar" method="get">
				<div class="search-bar__controls">
					<label for="baseYear">귀속연도</label>
					<select id="baseYear" name="baseYear">
						<option value="">선택</option>
						<c:forEach var="year" items="${availableYears}">
							<option value="${year}" <c:if test="${year eq selectedYear}">selected</c:if>>${year}년</option>
						</c:forEach>
					</select>
					<button type="submit" class="ui-button ui-button--primary">조회</button>
				</div>
			</form>

			<c:choose>
				<c:when test="${not empty annualStats}">
					<section class="chart-panel" aria-labelledby="payroll-chart-title">
						<div class="section-title-row">
							<h3 id="payroll-chart-title" class="sr-only">연도별 급여 지급현황</h3>
							<div class="chart-legend" aria-label="차트 범례">
								<span><i class="chart-legend__pay"></i>전체 급여액</span>
								<span><i class="chart-legend__people"></i>인원</span>
							</div>
						</div>

						<div class="annual-chart">
							<c:forEach var="stat" items="${annualStats}">
								<div class="annual-chart__item" tabindex="0">
									<div class="annual-chart__plot">
										<div class="annual-chart__bar annual-chart__bar--pay"
											style="height:${stat.payrollBarRate}%" title="전체 급여액 ${stat.totalPayrollText}천원"></div>
										<div class="annual-chart__bar annual-chart__bar--people"
											style="height:${stat.headcountBarRate}%" title="인원 ${stat.headcountText}명"></div>
									</div>
									<strong>${stat.year}</strong>
									<div class="annual-chart__tooltip" role="tooltip">
										<b>${stat.year}년</b>
										<span><i class="tooltip-dot tooltip-dot--pay"></i>전체 급여액 (천원) <em>${stat.totalPayrollText}</em></span>
										<span><i class="tooltip-dot tooltip-dot--people"></i>인원 (명) <em>${stat.headcountText}</em></span>
									</div>
								</div>
							</c:forEach>
						</div>
					</section>

					<div class="statistics-table-wrap">
						<table class="statistics-table">
							<caption>최근 10개년도 전체 급여액과 인원 현황</caption>
							<thead>
								<tr>
									<th scope="col">구분</th>
									<c:forEach var="stat" items="${annualStats}">
										<th scope="col">${stat.year}년</th>
									</c:forEach>
								</tr>
							</thead>
							<tbody>
								<tr class="statistics-table__main-row">
									<th scope="row">전체 급여액 (천원)</th>
									<c:forEach var="stat" items="${annualStats}">
										<td>${stat.totalPayrollText}</td>
									</c:forEach>
								</tr>
								<tr class="statistics-table__rate-row">
									<th scope="row">└ 증가율</th>
									<c:forEach var="stat" items="${annualStats}">
										<td class="${stat.payrollGrowth gt 0 ? 'rate-up' : stat.payrollGrowth lt 0 ? 'rate-down' : ''}">
											${stat.payrollGrowthText}
										</td>
									</c:forEach>
								</tr>
								<tr class="statistics-table__main-row">
									<th scope="row">인원 (명)</th>
									<c:forEach var="stat" items="${annualStats}">
										<td>${stat.headcountText}</td>
									</c:forEach>
								</tr>
								<tr class="statistics-table__rate-row">
									<th scope="row">└ 증가율</th>
									<c:forEach var="stat" items="${annualStats}">
										<td class="${stat.headcountGrowth gt 0 ? 'rate-up' : stat.headcountGrowth lt 0 ? 'rate-down' : ''}">
											${stat.headcountGrowthText}
										</td>
									</c:forEach>
								</tr>
							</tbody>
						</table>
					</div>
				</c:when>
				<c:otherwise>
					<div class="empty-state">
						<p>조회된 연도별 급여 통계가 없습니다.</p>
						<span>귀속연도를 선택한 후 조회해 주세요.</span>
					</div>
				</c:otherwise>
			</c:choose>
		</section>
	</main>

	<%@ include file="/WEB-INF/view/common/footer.jspf" %>
</body>
</html>
