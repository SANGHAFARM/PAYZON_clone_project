<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>

<!DOCTYPE html>
<html lang="ja-JP">
<head>
	<meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
	<title>給与明細構成統計</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/common.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/payzon-ui.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/payroll-stats/annual-payroll-statistics.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/payroll-stats/personal-annual-salary-statistics.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/payroll-stats/payroll-item-composition-statistics.css">
</head>
<body>
	<%@ include file="/WEB-INF/view/common/header.jspf" %>
	
	<!-- ⭐ここから完全なフォーム（Form）が始まります。 （メイン検索ウィンドウ+モーダルウィンドウ全体を包みます）⭐ -->
	<form action="${pageContext.request.contextPath}/payroll-stats/composition.do" method="GET">
		
		<main class="page-content annual-stat-page pay-composition-page">
			<header class="page-heading"><div><p>給与統計</p><h1>給与明細構成統計</h1></div></header>
			<section class="content-card">
				
				<!-- 既存のformタグをdivに変更して入れ子になったフォームエラーを回避する -->
				<div class="search-bar personal-search-bar"><div class="search-bar__controls">
					<label for="baseYear">帰属年月</label>
					<select id="baseYear" name="baseYear"><option value="">年</option><c:forEach var="year" items="${availableYears}"><option value="${year}" <c:if test="${year eq selectedYear}">selected</c:if>>${year}年</option></c:forEach></select>
					<select name="baseMonth" aria-label="帰属月"><option value="">月</option><c:forEach var="month" items="${availableMonths}"><option value="${month}" <c:if test="${month eq selectedMonth}">selected</c:if>>${month}月</option></c:forEach></select>
					
					<label for="employeeName">対象</label>
					<!-- ⭐隠しインプット削除！ JavaScriptなしでサーバーから受け取った名前だけを表示します⭐ -->
					<input id="employeeName" class="employee-name-field" type="text" value="${selectedEmployeeName}" placeholder="社員を選択してください。" readonly>
					<a class="ui-button ui-button--outline employee-select-link" href="#employeeSelectModal">社員を選択</a>
					
					<!-- このルックアップボタンを押すと、baseYear、baseMonth、およびモーダルでチェックされたemployeeIdがサーバーに移動します -->
					<button type="submit" class="ui-button ui-button--primary">照会</button>
				</div></div>
				
				<section class="donut-grid" aria-label="給与明細構成チャート">
					<article class="donut-card"><h2>支給項目+控除項目</h2><div class="interactive-donut"><svg class="donut-svg" viewBox="0 0 200 200" aria-label="支給項目と控除項目の構成"><circle class="donut-track" cx="100" cy="100" r="65" pathLength="100"/><c:forEach var="item" items="${summaryItems}"><g class="donut-segment-group"><circle class="donut-segment" cx="100" cy="100" r="65" pathLength="100" stroke="${item.color}" stroke-dasharray="${item.ratioValue} ${100 - item.ratioValue}" stroke-dashoffset="-${item.dashOffset}"/><g class="donut-svg-tooltip"><rect x="36" y="77" width="128" height="48" rx="4"/><text x="100" y="97"><ui:code-label value="${item.name}" /></text><text x="100" y="116">${item.ratioText}</text></g></g></c:forEach></svg><c:forEach var="item" items="${summaryItems}"><span class="donut-percentage-label" style="left:${item.labelLeft}%;top:${item.labelTop}%">${item.ratioText}</span></c:forEach></div><div class="donut-legend"><c:forEach var="item" items="${summaryItems}"><span><i style="background:${item.color}"></i><ui:code-label value="${item.name}" /></span></c:forEach></div></article>
					<article class="donut-card"><h2>支払詳細項目</h2><div class="interactive-donut"><svg class="donut-svg" viewBox="0 0 200 200" aria-label="支払詳細の設定"><circle class="donut-track" cx="100" cy="100" r="65" pathLength="100"/><c:forEach var="item" items="${paymentItems}"><c:if test="${item.ratioValue ne '0'}"><g class="donut-segment-group"><circle class="donut-segment" cx="100" cy="100" r="65" pathLength="100" stroke="${item.color}" stroke-dasharray="${item.ratioValue} ${100 - item.ratioValue}" stroke-dashoffset="-${item.dashOffset}"/><g class="donut-svg-tooltip"><rect x="36" y="77" width="128" height="48" rx="4"/><text x="100" y="97"><ui:code-label value="${item.name}" /></text><text x="100" y="116">${item.ratioText}</text></g></g></c:if></c:forEach></svg><c:forEach var="item" items="${paymentItems}"><c:if test="${item.ratioValue ne '0'}"><span class="donut-percentage-label" style="left:${item.labelLeft}%;top:${item.labelTop}%">${item.ratioText}</span></c:if></c:forEach></div><div class="donut-legend"><c:forEach var="item" items="${paymentItems}"><span class="${item.ratioText eq '0.0%' ? 'is-zero' : ''}"><i style="background:${item.color}"></i><ui:code-label value="${item.name}" /></span></c:forEach></div></article>
					<article class="donut-card"><h2>控除の詳細</h2><div class="interactive-donut"><svg class="donut-svg" viewBox="0 0 200 200" aria-label="控除詳細設定"><circle class="donut-track" cx="100" cy="100" r="65" pathLength="100"/><c:forEach var="item" items="${deductionItems}"><c:if test="${item.ratioValue ne '0'}"><g class="donut-segment-group"><circle class="donut-segment" cx="100" cy="100" r="65" pathLength="100" stroke="${item.color}" stroke-dasharray="${item.ratioValue} ${100 - item.ratioValue}" stroke-dashoffset="-${item.dashOffset}"/><g class="donut-svg-tooltip"><rect x="36" y="77" width="128" height="48" rx="4"/><text x="100" y="97"><ui:code-label value="${item.name}" /></text><text x="100" y="116">${item.ratioText}</text></g></g></c:if></c:forEach></svg><c:forEach var="item" items="${deductionItems}"><c:if test="${item.ratioValue ne '0'}"><span class="donut-percentage-label" style="left:${item.labelLeft}%;top:${item.labelTop}%">${item.ratioText}</span></c:if></c:forEach></div><div class="donut-legend"><c:forEach var="item" items="${deductionItems}"><span class="${item.ratioText eq '0.0%' ? 'is-zero' : ''}"><i style="background:${item.color}"></i><ui:code-label value="${item.name}" /></span></c:forEach></div></article>
				</section>
				<div class="composition-table-wrap"><table class="composition-table"><caption>給与支払明細の設定</caption>
					<thead><tr><th>支給項目</th><c:forEach var="item" items="${paymentItems}"><th><ui:code-label value="${item.name}" /></th></c:forEach><th>合計</th></tr></thead>
					<tbody><tr><th>└金額（ウォン）</th><c:forEach var="item" items="${paymentItems}"><td>${item.amountText}</td></c:forEach><td class="total-cell">${totalPaymentText}</td></tr><tr><th>└構成比率</th><c:forEach var="item" items="${paymentItems}"><td>${item.ratioText}</td></c:forEach><td class="total-cell">100%</td></tr></tbody>
				</table></div>
				<div class="composition-table-wrap"><table class="composition-table"><caption>給与控除項目の構成</caption>
					<thead><tr><th class="deduction-head">控除項目</th><c:forEach var="item" items="${deductionItems}"><th><ui:code-label value="${item.name}" /></th></c:forEach><th>合計</th></tr></thead>
					<tbody><tr><th>└金額（ウォン）</th><c:forEach var="item" items="${deductionItems}"><td>${item.amountText}</td></c:forEach><td class="total-cell">${totalDeductionText}</td></tr><tr><th>└構成比率</th><c:forEach var="item" items="${deductionItems}"><td>${item.ratioText}</td></c:forEach><td class="total-cell">100%</td></tr></tbody>
				</table></div>
				<div class="composition-summary"><div><span>支給総額</span><strong>${totalPaymentText}ウォン</strong></div><div><span>控除総額</span><strong>${totalDeductionText}ウォン</strong></div><div class="composition-summary__net"><span>差引支給額</span><strong>${netPaymentText}ウォン</strong></div></div>
			</section>
		</main>

		<!-- ⭐モーダルウィンドウ開始⭐ -->
		<div id="employeeSelectModal" class="css-modal" role="dialog" aria-modal="true" aria-labelledby="employee-modal-title"><a href="#" class="css-modal__backdrop" aria-label="ポップアップを閉じる"></a><section class="css-modal__dialog employee-modal"><header class="css-modal__header"><h2 id="employee-modal-title">給与項目構成照会社員を選択</h2><a href="#" class="css-modal__close" aria-label="閉じる">×</a></header>
			<!-- モーダル内の重複フォームタグ削除！ -->
			<div class="employee-modal__search"><input type="text" name="employeeKeyword" placeholder="社員検索"><button type="submit" class="ui-button ui-button--primary">検索</button></div>
			
			<div class="employee-modal__table-wrap"><table class="employee-modal__table"><thead><tr><th>選択</th><th>区分</th><th>社員番号</th><th>氏名</th><th>部署</th><th>役職</th><th>ステータス</th></tr></thead><tbody>
				<c:forEach var="employee" items="${employeeOptions}">
					<tr>
						<!-- ⭐JS用のコードをすべて減算し、name="employeeId"で固定します⭐ -->
						<!-- 選択した状態を維持するためにc：ifステートメントを追加（更新時にチェックを保持） -->
						<td><input type="radio" name="employeeId" value="${employee.employeeId}" <c:if test="${employee.employeeId eq selectedEmployeeId}">checked</c:if>></td>
						
						<td><ui:code-label value="${employee.type}" /></td><td>${employee.employeeNo}</td><td>${employee.name}</td><td>${employee.department}</td><td>${employee.position}</td><td><ui:code-label value="${employee.status}" /></td>
					</tr>
				</c:forEach>
			</tbody></table></div>
			
			<div class="employee-modal__actions">
				<!-- ⭐社員選択ボタンを押す瞬間フォームが丸ごとサーバー(Handler)に提出(submit)されます！ ⭐ -->
				<button type="submit" class="ui-button ui-button--primary">社員を選択</button>
				<a href="#" class="ui-button ui-button--secondary">選択解除</a>
			</div>
		</section></div>

	</form>
	<!-- ⭐フォーム終了⭐ -->

	<%@ include file="/WEB-INF/view/common/footer.jspf" %>
</body>
</html>