<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html lang="ja-JP">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>PAYZON - HOME</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/common.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/home/home.css?v=20260815-6">
</head>
<body>
	<%@ include file="/WEB-INF/view/common/header.jspf" %>
	<main class="page-content home-page">
		<section class="home-hero">
			<div class="home-hero__copy">
				<p class="home-hero__date"><c:out value="${today}" /></p>
				<h1><c:out value="${empty dashboard.company.cmpnName ? 'PAYZON' : dashboard.company.cmpnName}" /><br>給与・人事業務を始める</h1>
				<p>社員情報から給与精算と退職管理まで必要な業務を一か所で確認できます。</p>
				<div class="home-hero__actions">
					<a class="home-primary-link" href="${pageContext.request.contextPath}/payroll/management.do">給与を入力する</a>
					<a class="home-secondary-link" href="${pageContext.request.contextPath}/employees/employees.do">社員の状況を見る</a>
				</div>
			</div>
			<div class="home-hero__summary">
				<span>今月の仕事の状況</span>
				<strong>${currentYear}.${currentMonth}</strong>
				<dl>
					<div><dt>在職社員</dt><dd><fmt:formatNumber value="${dashboard.employeeSummary.workingCount}" />人</dd></div>
					<div><dt>最近の給与支給人数</dt><dd><fmt:formatNumber value="${empty dashboard.latestPayroll ? 0 : dashboard.latestPayroll.employeeCount}" />人</dd></div>
					<div><dt>最近の差引支給額</dt><dd><fmt:formatNumber value="${empty dashboard.latestPayroll ? 0 : dashboard.latestPayroll.netPayment}" />ウォン</dd></div>
				</dl>
			</div>
		</section>

		<section class="home-stat-grid" aria-label="社員状況">
			<a class="home-stat-card home-stat-card--navy" href="${pageContext.request.contextPath}/employees/employees.do">
				<div><p>全社員</p><strong><fmt:formatNumber value="${dashboard.employeeSummary.totalCount}" /><em>人</em></strong><span>登録された全社員</span></div>
			</a>
			<a class="home-stat-card home-stat-card--blue" href="${pageContext.request.contextPath}/employees/employees.do?status=WORK">
				<div><p>在職社員</p><strong><fmt:formatNumber value="${dashboard.employeeSummary.workingCount}" /><em>人</em></strong><span>現在在職中の社員</span></div>
			</a>
			<a class="home-stat-card home-stat-card--green" href="${pageContext.request.contextPath}/payroll/day-worker-management.do">
				<div><p>日雇い社員</p><strong><fmt:formatNumber value="${dashboard.employeeSummary.dailyCount}" /><em>人</em></strong><span>日雇いとして登録された社員</span></div>
			</a>
			<a class="home-stat-card home-stat-card--orange" href="${pageContext.request.contextPath}/retirement/process.do?mode=status&amp;status=RETIRED">
				<div><p>退職社員</p><strong><fmt:formatNumber value="${dashboard.employeeSummary.retiredCount}" /><em>人</em></strong><span>退職社員</span></div>
			</a>
		</section>

		<div class="home-main-grid">
			<section class="home-panel home-quick-panel">
				<div class="home-panel__heading"><div><span>QUICK MENU</span><h2>よく使うメニュー</h2></div></div>
				<div class="home-quick-grid">
					<a href="${pageContext.request.contextPath}/employees/employees.do"><i>01</i><strong>社員状況/管理</strong><span>社員情報の照会と管理</span></a>
					<a href="${pageContext.request.contextPath}/payroll/management.do"><i>02</i><strong>給与入力/管理</strong><span>毎月の給与と控除の入力</span></a>
					<a href="${pageContext.request.contextPath}/payroll/register.do"><i>03</i><strong>給与台帳</strong><span>給与回次別給与確認</span></a>
					<a href="${pageContext.request.contextPath}/employees/certificate.do"><i>04</i><strong>証明書の発行</strong><span>在職・退職証明書発行</span></a>
					<a href="${pageContext.request.contextPath}/retirement/process.do"><i>05</i><strong>退職処理</strong><span>退職情報の登録とキャンセル</span></a>
					<a href="${pageContext.request.contextPath}/retirement/benefit.do"><i>06</i><strong>退職給付管理</strong><span>退職給付の計算と保存</span></a>
				</div>
			</section>

			<section class="home-panel home-company-panel">
				<div class="home-panel__heading"><div><span>COMPANY</span><h2>事業所情報</h2></div><a href="${pageContext.request.contextPath}/settings/user-info.do">情報を見る</a></div>
				<h3><c:out value="${empty dashboard.company.cmpnName ? '登録された会社情報がありません' : dashboard.company.cmpnName}" /></h3>
				<dl>
					<div><dt>代表者</dt><dd><c:out value="${empty dashboard.company.ceoName ? '-' : dashboard.company.ceoName}" /></dd></div>
					<div><dt>担当者</dt><dd><c:out value="${empty dashboard.company.managerName ? '-' : dashboard.company.managerName}" /></dd></div>
					<div><dt>連絡先</dt><dd><c:out value="${empty dashboard.company.telNo ? '-' : dashboard.company.telNo}" /></dd></div>
				</dl>
			</section>
		</div>

		<section class="home-panel home-payroll-panel">
			<div class="home-panel__heading"><div><span>RECENT PAYROLL</span><h2>最近の給与状況</h2></div><a href="${pageContext.request.contextPath}/payroll/register.do">給与台帳を見る</a></div>
			<div class="home-table-wrap">
				<table>
					<thead><tr><th>帰属年月</th><th>給与回次</th><th>精算期間</th><th>支給日</th><th>人数</th><th>支給総額</th><th>控除総額</th><th>差引支給額</th></tr></thead>
					<tbody>
					<c:forEach var="payroll" items="${dashboard.recentPayrolls}">
						<tr><td><a href="${pageContext.request.contextPath}/payroll/register/detail.do?registerId=${payroll.registerId}">${payroll.paymentYearMonth}</a></td><td><ui:code-label value="${payroll.paymentRoundName}" /></td><td>${payroll.calculationStart} ~ ${payroll.calculationEnd}</td><td>${payroll.paymentDate}</td><td>${payroll.employeeCount}名</td><td class="amount give"><fmt:formatNumber value="${payroll.totalPayment}" /></td><td class="amount deduct"><fmt:formatNumber value="${payroll.totalDeduction}" /></td><td class="amount"><fmt:formatNumber value="${payroll.netPayment}" /></td></tr>
					</c:forEach>
					<c:if test="${empty dashboard.recentPayrolls}"><tr><td colspan="8" class="empty-row">${currentYear}年に登録された給与台帳はありません。</td></tr></c:if>
					</tbody>
				</table>
			</div>
		</section>
	</main>
	<%@ include file="/WEB-INF/view/common/footer.jspf" %>
</body>
</html>
