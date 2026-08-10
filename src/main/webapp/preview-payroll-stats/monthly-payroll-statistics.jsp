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
	 * 2. 선택한 연도의 1월부터 12월까지 급여액과 인원을 집계합니다.
	 * 3. 급여가 없는 월도 누락하지 말고 값이 0인 항목으로 전달합니다.
	 * 4. 컨트롤러에서 아래 이름으로 request 속성을 전달합니다.
	 *
	 *    availableYears          : 귀속연도 선택 상자에 표시할 연도 목록
	 *    selectedYear            : 현재 선택된 귀속연도
	 *    monthlyStats            : 1월부터 12월까지의 월별 통계 목록
	 *    totalPayrollYearText    : 선택 연도의 전체 급여액 합계
	 *    averageHeadcountYearText: 선택 연도의 월평균 인원
	 *
	 * [monthlyStats 항목별 필드]
	 * month               : 귀속월
	 * totalPayrollText    : 화면에 표시할 월 전체 급여액 문자열
	 * payrollGrowth       : 전월 대비 급여 증가율 색상 판별용 숫자
	 * payrollGrowthText   : 화면에 표시할 급여 증가율 문자열
	 * headcountText       : 화면에 표시할 월 인원 문자열
	 * headcountGrowth     : 전월 대비 인원 증가율 색상 판별용 숫자
	 * headcountGrowthText : 화면에 표시할 인원 증가율 문자열
	 * payrollBarRate      : 전체 급여액 막대 높이 비율(0~100)
	 * headcountBarRate    : 인원 막대 높이 비율(0~100)
	 *
	 * 두 막대 비율은 각각의 월별 최댓값을 100으로 환산하여 전달합니다.
	 * 실제 monthlyStats가 전달되면 아래 더미 데이터는 실행되지 않습니다.
	 * 백엔드 연동 완료 후에는 이 블록 전체를 제거해도 됩니다.
	 */
	if (request.getAttribute("monthlyStats") == null) {
		int[] months = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
		String[] payrolls = {"24,800", "25,300", "25,300", "26,100", "26,100", "26,400", "26,400", "27,200", "27,200", "27,500", "27,500", "28,100"};
		double[] payrollGrowths = {0, 2.0, 0, 3.2, 0, 1.1, 0, 3.0, 0, 1.1, 0, 2.2};
		String[] payrollGrowthTexts = {"-", "2.0%", "0.0%", "3.2%", "0.0%", "1.1%", "0.0%", "3.0%", "0.0%", "1.1%", "0.0%", "2.2%"};
		String[] headcounts = {"7", "7", "7", "8", "8", "8", "8", "8", "8", "8", "8", "8"};
		double[] headcountGrowths = {0, 0, 0, 14.3, 0, 0, 0, 0, 0, 0, 0, 0};
		String[] headcountGrowthTexts = {"-", "0.0%", "0.0%", "14.3%", "0.0%", "0.0%", "0.0%", "0.0%", "0.0%", "0.0%", "0.0%", "0.0%"};
		int[] payrollBars = {88, 90, 90, 93, 93, 94, 94, 97, 97, 98, 98, 100};
		int[] headcountBars = {88, 88, 88, 100, 100, 100, 100, 100, 100, 100, 100, 100};

		List<Map<String, Object>> previewStats = new ArrayList<>();
		for (int i = 0; i < months.length; i++) {
			Map<String, Object> stat = new LinkedHashMap<>();
			stat.put("month", months[i]);
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

		request.setAttribute("monthlyStats", previewStats);
		request.setAttribute("availableYears", Arrays.asList(2017, 2018, 2019, 2020, 2021, 2022, 2023, 2024, 2025, 2026));
		request.setAttribute("selectedYear", 2026);
		request.setAttribute("totalPayrollYearText", "318,300");
		request.setAttribute("averageHeadcountYearText", "7.8");
	}
	/* 미리보기 및 백엔드 연동 예시용 더미 데이터 끝 */
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>월별 전체급여 통계</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/common.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/payzon-ui.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/payroll-stats/annual-payroll-statistics.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/payroll-stats/monthly-payroll-statistics.css">
</head>
<body>
	<%@ include file="/WEB-INF/view/common/header.jspf" %>

	<main class="page-content annual-stat-page monthly-stat-page">
		<header class="page-heading">
			<div>
				<p>급여통계</p>
				<h1>월별 전체급여 통계</h1>
			</div>
		</header>

		<section class="content-card" aria-labelledby="monthly-stat-title">
			<h2 id="monthly-stat-title" class="sr-only">월별 전체급여 통계 조회</h2>

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

			<section class="chart-panel" aria-labelledby="monthly-chart-title">
				<div class="section-title-row">
					<h3 id="monthly-chart-title" class="sr-only">월별 급여 지급현황</h3>
					<div class="chart-legend" aria-label="차트 범례">
						<span><i class="chart-legend__pay"></i>전체 급여액</span>
						<span><i class="chart-legend__people"></i>인원</span>
					</div>
				</div>

				<div class="annual-chart monthly-chart">
					<c:forEach var="stat" items="${monthlyStats}">
						<div class="annual-chart__item" tabindex="0">
							<div class="annual-chart__plot">
								<div class="annual-chart__bar annual-chart__bar--pay" style="height:${stat.payrollBarRate}%"></div>
								<div class="annual-chart__bar annual-chart__bar--people" style="height:${stat.headcountBarRate}%"></div>
							</div>
							<strong>${stat.month}월</strong>
							<div class="annual-chart__tooltip" role="tooltip">
								<b>${stat.month}월</b>
								<span><i class="tooltip-dot tooltip-dot--pay"></i>전체 급여액 (천원) <em>${stat.totalPayrollText}</em></span>
								<span><i class="tooltip-dot tooltip-dot--people"></i>인원 (명) <em>${stat.headcountText}</em></span>
							</div>
						</div>
					</c:forEach>
				</div>
			</section>

			<div class="statistics-table-wrap">
				<table class="statistics-table monthly-statistics-table">
					<caption>${selectedYear}년 월별 전체 급여액과 인원 현황</caption>
					<thead>
						<tr>
							<th scope="col">구분</th>
							<c:forEach var="stat" items="${monthlyStats}">
								<th scope="col">${stat.month}월</th>
							</c:forEach>
							<th scope="col">합계</th>
						</tr>
					</thead>
					<tbody>
						<tr class="statistics-table__main-row">
							<th scope="row">전체 급여액 (천원)</th>
							<c:forEach var="stat" items="${monthlyStats}"><td>${stat.totalPayrollText}</td></c:forEach>
							<td class="statistics-total">${totalPayrollYearText}</td>
						</tr>
						<tr class="statistics-table__rate-row">
							<th scope="row">└ 증가율</th>
							<c:forEach var="stat" items="${monthlyStats}">
								<td class="${stat.payrollGrowth gt 0 ? 'rate-up' : stat.payrollGrowth lt 0 ? 'rate-down' : ''}">${stat.payrollGrowthText}</td>
							</c:forEach>
							<td></td>
						</tr>
						<tr class="statistics-table__main-row">
							<th scope="row">인원 (명)</th>
							<c:forEach var="stat" items="${monthlyStats}"><td>${stat.headcountText}</td></c:forEach>
							<td class="statistics-total">${averageHeadcountYearText}</td>
						</tr>
						<tr class="statistics-table__rate-row">
							<th scope="row">└ 증가율</th>
							<c:forEach var="stat" items="${monthlyStats}">
								<td class="${stat.headcountGrowth gt 0 ? 'rate-up' : stat.headcountGrowth lt 0 ? 'rate-down' : ''}">${stat.headcountGrowthText}</td>
							</c:forEach>
							<td></td>
						</tr>
					</tbody>
				</table>
			</div>
		</section>
	</main>

	<%@ include file="/WEB-INF/view/common/footer.jspf" %>
</body>
</html>
