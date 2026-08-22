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
	 * 2. 選択年度を含む最近10ヶ年の資料を昇順で照会します。
	 * 例: 2026 年選択時 2017 年から 2026 年まで照会
	 *3. コントローラから以下の名前でリクエスト属性を渡します。
	 *
	 * availableYears：帰属年選択ボックスに表示する年のリスト
	 * selectedYear : 現在選択されている帰属年度
	 * statisticsStartYear: 照会された統計の開始年
	 * statisticsEndYear : 照会された統計の最後の年
	 * annualStats：最近10カ年の統計リスト
	 *
	 * [annualStats 項目別フィールド]
	 * year : 帰属年度
	 * totalPayrollText : 画面に表示する全給与額文字列
	 * payrollGrowth：給与増加率の色判別のための数
	 * payrollGrowthText：画面に表示する給与増加率文字列
	 * headcountText : 画面に表示する人数文字列
	 * headcountGrowth : 人数増加率 色判別用数値
	 * headcountGrowthText : 画面に表示する人数増加率文字列
	 * payrollBarRate : 全給与額棒高さ比率(0～100)
	 * headcountBarRate : 人員バー高さ比率(0～100)
	 *
	 * payrollBarRateとheadcountBarRateはそれぞれの最大値を100に
	 *換算してコントローラで計算した後渡します。
	 *
	 *実際のannualStatsが渡されると、下のダミーデータは実行されません。
	 * バックエンド連動完了後は、このコメントとダミーデータブロック全体を削除してもかまいません。
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
	/* プレビューとバックエンド連動の例のダミーデータの終わり */
%>
<!DOCTYPE html>
<html lang="ja-JP">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>年別総給与統計</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/common.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/payzon-ui.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/payroll-stats/annual-payroll-statistics.css">
</head>
<body>
	<%@ include file="/WEB-INF/view/common/header.jspf" %>

	<main class="page-content annual-stat-page">
		<header class="page-heading">
			<div>
				<p>給与統計</p>
				<h1>年別総給与統計</h1>
			</div>
		</header>

		<section class="content-card" aria-labelledby="annual-stat-title">
			<h2 id="annual-stat-title" class="sr-only">年ごとの総給与統計の照会</h2>

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

			<c:choose>
				<c:when test="${not empty annualStats}">
					<section class="chart-panel" aria-labelledby="payroll-chart-title">
						<div class="section-title-row">
							<h3 id="payroll-chart-title" class="sr-only">年別給与支払状況</h3>
							<div class="chart-legend" aria-label="チャートの凡例">
								<span><i class="chart-legend__pay"></i>給与総額</span>
								<span><i class="chart-legend__people"></i>人数</span>
							</div>
						</div>

						<div class="annual-chart">
							<c:forEach var="stat" items="${annualStats}">
								<div class="annual-chart__item" tabindex="0">
									<div class="annual-chart__plot">
										<div class="annual-chart__bar annual-chart__bar--pay"
											style="height:${stat.payrollBarRate}%" title="全給与額 ${stat.totalPayrollText}千ウォン"></div>
										<div class="annual-chart__bar annual-chart__bar--people"
											style="height:${stat.headcountBarRate}%" title="人員${stat.headcountText}名"></div>
									</div>
									<strong>${stat.year}</strong>
									<div class="annual-chart__tooltip" role="tooltip">
										<b>${stat.year}年</b>
										<span><i class="tooltip-dot tooltip-dot--pay"></i>給与総額（千ウォン） <em>${stat.totalPayrollText}</em></span>
										<span><i class="tooltip-dot tooltip-dot--people"></i>人数（人） <em>${stat.headcountText}</em></span>
									</div>
								</div>
							</c:forEach>
						</div>
					</section>

					<div class="statistics-table-wrap">
						<table class="statistics-table">
							<caption>最近の10年間の総給与額と人員の現状</caption>
							<thead>
								<tr>
									<th scope="col">区分</th>
									<c:forEach var="stat" items="${annualStats}">
										<th scope="col">${stat.year}年</th>
									</c:forEach>
								</tr>
							</thead>
							<tbody>
								<tr class="statistics-table__main-row">
									<th scope="row">給与総額（千ウォン）</th>
									<c:forEach var="stat" items="${annualStats}">
										<td>${stat.totalPayrollText}</td>
									</c:forEach>
								</tr>
								<tr class="statistics-table__rate-row">
									<th scope="row">└増加率</th>
									<c:forEach var="stat" items="${annualStats}">
										<td class="${stat.payrollGrowth gt 0 ? 'rate-up' : stat.payrollGrowth lt 0 ? 'rate-down' : ''}">
											${stat.payrollGrowthText}
										</td>
									</c:forEach>
								</tr>
								<tr class="statistics-table__main-row">
									<th scope="row">人数（人）</th>
									<c:forEach var="stat" items="${annualStats}">
										<td>${stat.headcountText}</td>
									</c:forEach>
								</tr>
								<tr class="statistics-table__rate-row">
									<th scope="row">└増加率</th>
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
						<p>照会された年ごとの給与統計はありません。</p>
						<span>帰属年度を選択して検索してください。</span>
					</div>
				</c:otherwise>
			</c:choose>
		</section>
	</main>

	<%@ include file="/WEB-INF/view/common/footer.jspf" %>
</body>
</html>
