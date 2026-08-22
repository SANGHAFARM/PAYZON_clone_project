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
	 *プレビューとバックエンド連動の例のダミーデータ
	 * ================================================================
	 *
	 * [実際の実装方法]
	 * 1. 画面で選択した帰属年を baseYear パラメータに渡します。
	 * 2. 選択した年の1月から12月までの給与額と人数を集計します。
	 * 3. 給与のない月も欠けておらず、値が0の項目に渡します。
	 *4. コントローラから以下の名前でリクエスト属性を渡します。
	 *
	 * availableYears：帰属年選択ボックスに表示する年のリスト
	 * selectedYear : 現在選択されている帰属年度
	 * monthlyStats：1月から12月までの月別統計のリスト
	 *totalPayrollYearText: 選択年の総給与額の合計
	 * averageHeadcountYearText: 選択年の月平均人数
	 *
	 * [monthlyStats項目別フィールド]
	 * month : 帰属月
	 * totalPayrollText：画面に表示する月全体の給与額文字列
	 * payrollGrowth：前月比給与増加率カラー判別用数字
	 * payrollGrowthText：画面に表示する給与増加率文字列
	 * headcountText : 画面に表示する月の人数文字列
	 * headcountGrowth : 前月比人数増加率 色判別用数字
	 * headcountGrowthText : 画面に表示する人数増加率文字列
	 * payrollBarRate : 全給与額棒高さ比率(0～100)
	 * headcountBarRate : 人員バー高さ比率(0～100)
	 *
	 * 2つのバー比率は、それぞれの月ごとの最大値を100に換算して渡します。
	 *実際のmonthlyStatsが渡されると、下のダミーデータは実行されません。
	 *バックエンド連動完了後は、このブロック全体を削除してもかまいません。
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
	/* プレビューとバックエンド連動の例のダミーデータの終わり */
%>
<!DOCTYPE html>
<html lang="ja-JP">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>月別総給与統計</title>
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
				<p>給与統計</p>
				<h1>月別総給与統計</h1>
			</div>
		</header>

		<section class="content-card" aria-labelledby="monthly-stat-title">
			<h2 id="monthly-stat-title" class="sr-only">月別総給与統計の照会</h2>

			<form class="search-bar" method="get">
				<div class="search-bar__controls">
					<label for="baseYear">帰属年</label>
					<select id="baseYear" name="baseYear">
						<option value="">選択</option>
						<c:forEach var="year" items="${availableYears}">
							<option value="${year}" <c:if test="${year eq selectedYear}">selected</c:if>>${year}年</option>
						</c:forEach>
					</select>
					<button type="submit" class="ui-button ui-button--primary">照会</button>
				</div>
			</form>

			<section class="chart-panel" aria-labelledby="monthly-chart-title">
				<div class="section-title-row">
					<h3 id="monthly-chart-title" class="sr-only">月別給与支給状況</h3>
					<div class="chart-legend" aria-label="チャートの凡例">
						<span><i class="chart-legend__pay"></i>給与総額</span>
						<span><i class="chart-legend__people"></i>人数</span>
					</div>
				</div>

				<div class="annual-chart monthly-chart">
					<c:forEach var="stat" items="${monthlyStats}">
						<div class="annual-chart__item" tabindex="0">
							<div class="annual-chart__plot">
								<div class="annual-chart__bar annual-chart__bar--pay" style="height:${stat.payrollBarRate}%"></div>
								<div class="annual-chart__bar annual-chart__bar--people" style="height:${stat.headcountBarRate}%"></div>
							</div>
							<strong>${stat.month}月</strong>
							<div class="annual-chart__tooltip" role="tooltip">
								<b>${stat.month}月</b>
								<span><i class="tooltip-dot tooltip-dot--pay"></i>給与総額（千ウォン） <em>${stat.totalPayrollText}</em></span>
								<span><i class="tooltip-dot tooltip-dot--people"></i>人数（人） <em>${stat.headcountText}</em></span>
							</div>
						</div>
					</c:forEach>
				</div>
			</section>

			<div class="statistics-table-wrap">
				<table class="statistics-table monthly-statistics-table">
					<caption>${selectedYear}年毎月の全給与額と人員の現状</caption>
					<thead>
						<tr>
							<th scope="col">区分</th>
							<c:forEach var="stat" items="${monthlyStats}">
								<th scope="col">${stat.month}月</th>
							</c:forEach>
							<th scope="col">合計</th>
						</tr>
					</thead>
					<tbody>
						<tr class="statistics-table__main-row">
							<th scope="row">給与総額（千ウォン）</th>
							<c:forEach var="stat" items="${monthlyStats}"><td>${stat.totalPayrollText}</td></c:forEach>
							<td class="statistics-total">${totalPayrollYearText}</td>
						</tr>
						<tr class="statistics-table__rate-row">
							<th scope="row">└増加率</th>
							<c:forEach var="stat" items="${monthlyStats}">
								<td class="${stat.payrollGrowth gt 0 ? 'rate-up' : stat.payrollGrowth lt 0 ? 'rate-down' : ''}">${stat.payrollGrowthText}</td>
							</c:forEach>
							<td></td>
						</tr>
						<tr class="statistics-table__main-row">
							<th scope="row">人数（人）</th>
							<c:forEach var="stat" items="${monthlyStats}"><td>${stat.headcountText}</td></c:forEach>
							<td class="statistics-total">${averageHeadcountYearText}</td>
						</tr>
						<tr class="statistics-table__rate-row">
							<th scope="row">└増加率</th>
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
