<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ja-JP">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>給与明細書</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/common.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/payroll/payroll-payslip.css?v=20260815-2">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/payzon-ui.css?v=20260821-1">
</head>
<body>
	<%@ include file="/WEB-INF/view/common/header.jspf" %>
	<main class="page-content payment-payslip-page">
		<header class="page-heading">
			<div>
				<p>給与管理</p>
				<h1>給与明細書</h1>
			</div>
		</header>

		<form class="period-toolbar" method="get" action="${pageContext.request.contextPath}/payroll/payslip.do">
			<div class="period-information">
				<div><strong>精算期間</strong><span><c:choose><c:when test="${not empty calculationStart and not empty calculationEnd}">${calculationStart} ~ ${calculationEnd}</c:when><c:otherwise>-</c:otherwise></c:choose></span></div>
				<div><strong>給与支給日</strong><span>${empty paymentDate ? '-' : paymentDate}</span></div>
			</div>
			<div class="period-controls">
				<div class="period-field"><label for="paymentYear">帰属年</label><select id="paymentYear" name="paymentYear"><option value="">選択</option><c:forEach var="year" items="${paymentYears}"><option value="${year}" <c:if test="${year eq selectedYear}">selected</c:if>>${year}年</option></c:forEach></select></div>
				<div class="period-field month-field"><label for="paymentMonth">帰属月</label><select id="paymentMonth" name="paymentMonth"><option value="">選択</option><c:forEach var="month" begin="1" end="12"><option value="${month}" <c:if test="${month eq selectedMonth}">selected</c:if>>${month}月</option></c:forEach></select></div>
				<div class="period-field"><label for="paymentRound">給与回次</label><select id="paymentRound" name="paymentRound"><option value="">回次を選択</option><c:forEach var="round" begin="1" end="10"><option value="${round}" <c:if test="${round eq selectedRound}">selected</c:if>>給与 ${round}回</option></c:forEach></select></div>
				<button type="submit" class="button button-primary">照会</button>
			</div>
		</form>

		<section class="payslip-workspace">
			<aside class="employee-panel">
				<form class="employee-search" method="get" action="${pageContext.request.contextPath}/payroll/payslip.do">
					<input type="hidden" name="paymentYear" value="${selectedYear}"><input type="hidden" name="paymentMonth" value="${selectedMonth}"><input type="hidden" name="paymentRound" value="${selectedRound}">
					<input type="hidden" name="mode" value="search"><input type="search" name="keyword" value="${param.keyword}" placeholder="検索語を入力"><button type="submit" class="button button-primary">検索</button><a class="button button-outline" href="${pageContext.request.contextPath}/payroll/payslip.do?paymentYear=${selectedYear}&amp;paymentMonth=${selectedMonth}&amp;paymentRound=${selectedRound}">全体を見る</a>
				</form>
				<div class="employee-table-wrap"><table class="employee-table"><thead><tr><th>区分</th><th>氏名</th><th>差引支給額</th></tr></thead><tbody>
				<c:choose><c:when test="${not empty payslipEmployees}"><c:forEach var="employee" items="${payslipEmployees}"><c:url var="payslipEmployeeUrl" value="/payroll/payslip.do"><c:param name="paymentYear" value="${selectedYear}"/><c:param name="paymentMonth" value="${selectedMonth}"/><c:param name="paymentRound" value="${selectedRound}"/><c:param name="keyword" value="${param.keyword}"/><c:param name="employeeId" value="${employee.employeeId}"/></c:url><tr class="${employee.employeeId eq selectedEmployee.employeeId ? 'selected-row' : ''}"><td><a class="employee-row-link" href="${payslipEmployeeUrl}"><ui:code-label value="${employee.employmentTypeName}" /></a></td><td><a class="employee-row-link" href="${payslipEmployeeUrl}">${employee.employeeName}</a></td><td class="amount"><a class="employee-row-link" href="${payslipEmployeeUrl}">${employee.netPayment}</a></td></tr></c:forEach></c:when><c:otherwise>
					<tr><td colspan="3" class="empty-row">照会された給与明細書には、対象の社員がいません。</td></tr>
				</c:otherwise></c:choose>
				</tbody></table></div>
			</aside>

			<article class="payslip-document">
				<header class="document-header"><div class="company-logo"><c:choose><c:when test="${not empty company.logoUrl}"><img src="${pageContext.request.contextPath}${company.logoUrl}" alt="会社のロゴ"></c:when><c:otherwise><span>会社のロゴ</span></c:otherwise></c:choose></div><h2>クラス3人</h2></header>
				<table class="employee-information"><tbody>
				<tr><th>氏名</th><td>${selectedEmployee.employeeName}</td><th>生年月日</th><td>${selectedEmployee.birthDate}</td></tr>
				<tr><th>部署</th><td>${selectedEmployee.departmentName}</td><th>職級</th><td>${selectedEmployee.positionName}</td></tr>
				<tr><th>入社日</th><td>${selectedEmployee.hireDate}</td><th>給与支給日</th><td>${paymentDate}</td></tr>
				</tbody></table>

				<section class="pay-details"><h3>給与履歴</h3><table class="pay-detail-table"><colgroup><col class="category-col"><col class="item-col"><col class="amount-col"><col></colgroup><thead><tr><th>区分</th><th>項目名</th><th>金額</th><th>算出式または算出方法</th></tr></thead><tbody>
				<c:choose><c:when test="${not empty paymentItems}"><c:forEach var="item" items="${paymentItems}" varStatus="status"><tr><c:if test="${status.first}"><th class="category payment-category" rowspan="${fn:length(paymentItems) + 1}"><span>支給</span><span>項目</span></th></c:if><td><ui:code-label value="${item.itemName}" /></td><td class="amount">${selectedEmployee.paymentAmounts[item.itemId]}</td><td>${selectedEmployee.paymentCalculations[item.itemId]}</td></tr></c:forEach></c:when><c:otherwise>
				<tr><th class="category payment-category" rowspan="2"><span>支給</span><span>項目</span></th><td colspan="3" class="empty-item">登録された支払い項目はありません。</td></tr>
				</c:otherwise></c:choose>
				<tr class="total-row payment-total"><th colspan="2">支給総額</th><td class="amount">${selectedEmployee.totalPayment}</td></tr>
				<c:choose><c:when test="${not empty deductionItems}"><c:forEach var="item" items="${deductionItems}" varStatus="status"><tr><c:if test="${status.first}"><th class="category deduction-category" rowspan="${fn:length(deductionItems) + 1}"><span>控除</span><span>項目</span></th></c:if><td><ui:code-label value="${item.itemName}" /></td><td class="amount">${selectedEmployee.deductionAmounts[item.itemId]}</td><td>${selectedEmployee.deductionCalculations[item.itemId]}</td></tr></c:forEach></c:when><c:otherwise>
				<tr><th class="category deduction-category" rowspan="2"><span>控除</span><span>項目</span></th><td colspan="3" class="empty-item">登録された控除項目はありません。</td></tr>
				</c:otherwise></c:choose>
				<tr class="total-row deduction-total"><th colspan="2">控除総額</th><td class="amount">${selectedEmployee.totalDeduction}</td></tr>
				<tr class="net-row"><th colspan="3">差引支給額</th><td class="amount">${selectedEmployee.netPayment}円</td></tr>
				</tbody></table></section>

				<p class="closing-message">あなたの労苦に感謝し、お疲れ様でした。</p>
				<footer class="document-footer"><div class="approval-block"><div class="representative"><strong>${company.companyName}</strong><span>代表取締役${company.representativeName}</span></div><div class="company-stamp"><c:choose><c:when test="${not empty company.stampUrl}"><img src="${pageContext.request.contextPath}${company.stampUrl}" alt="会社印"></c:when><c:otherwise><span>会社印</span></c:otherwise></c:choose></div></div></footer>
			</article>
		</section>
	</main>
	<c:if test="${not empty payslipPopupMessage}">
		<c:url var="payslipReturnUrl" value="/payroll/payslip.do"><c:param name="paymentYear" value="${selectedYear}"/><c:param name="paymentMonth" value="${selectedMonth}"/><c:param name="paymentRound" value="${selectedRound}"/></c:url>
		<div class="payslip-alert" role="alertdialog" aria-modal="true" aria-labelledby="payslip-alert-message"><a class="payslip-alert__backdrop" href="${payslipReturnUrl}" aria-label="閉じる"></a><div class="payslip-alert__panel"><p id="payslip-alert-message"><ui:message-label value="${payslipPopupMessage}" /></p><a href="${payslipReturnUrl}">確認</a></div></div>
	</c:if>
	<%@ include file="/WEB-INF/view/common/footer.jspf" %>
</body>
</html>
