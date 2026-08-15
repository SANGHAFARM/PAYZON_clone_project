<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>항목별 대장</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/common.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/payzon-ui.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/payroll/payroll-item-ledger.css?v=20260815-1">
</head>
<body>
	<%@ include file="/WEB-INF/view/common/header.jspf" %>
	<main class="page-content payment-item-ledger-page">
		<header class="page-heading"><div><p>급여관리</p><h1>항목별 대장</h1></div></header>
		<section class="ledger-card">
			<form class="ledger-search" method="get" action="${pageContext.request.contextPath}/payroll/item-ledger.do">
				<input type="hidden" name="mode" value="search">
				<div class="search-field"><label for="startMonth">기간선택</label><div class="month-range"><input id="startMonth" name="startMonth" type="month" value="${startMonth}"><span>~</span><input id="endMonth" name="endMonth" type="month" value="${endMonth}"></div></div>
				<div class="search-field item-field"><label for="paymentItem">항목선택</label><select id="paymentItem" name="itemCode"><option value="">급여항목 선택</option><c:forEach var="item" items="${paymentItems}"><option value="${item.itemCode}" <c:if test="${item.itemCode eq selectedItemCode}">selected</c:if>>${item.itemName}</option></c:forEach></select></div>
				<button type="submit" class="button button-primary">조회</button>
			</form>
			<div class="ledger-table-wrap">
				<table class="ledger-table">
					<colgroup><col class="type-col"><col class="name-col"><col class="department-col"><col class="position-col"><c:forEach var="month" items="${ledgerMonths}"><col class="month-col"></c:forEach><col class="total-col"></colgroup>
					<thead><tr><th>구분</th><th>성명</th><th>부서</th><th>직위</th><c:forEach var="month" items="${ledgerMonths}"><th class="month-header">${month}</th></c:forEach><th class="total-header">합계</th></tr></thead>
					<tbody>
					<c:choose><c:when test="${not empty ledgerRows}"><c:forEach var="row" items="${ledgerRows}"><tr><td>${row.employmentTypeName}</td><td>${row.employeeName}</td><td>${row.departmentName}</td><td>${row.positionName}</td><c:forEach var="amount" items="${row.monthlyAmounts}"><td class="amount">${amount}</td></c:forEach><td class="amount row-total">${row.totalAmount}</td></tr></c:forEach></c:when><c:otherwise>
					<tr><td colspan="${5 + fn:length(ledgerMonths)}" class="empty-row">조회된 항목별 대장 내역이 없습니다.</td></tr>
					</c:otherwise></c:choose>
					</tbody>
					<c:if test="${not empty ledgerRows}"><tfoot><tr><th colspan="4">합계</th><c:forEach var="amount" items="${ledgerTotals.monthlyAmounts}"><td class="amount">${amount}</td></c:forEach><td class="amount grand-total">${ledgerTotals.totalAmount}</td></tr></tfoot></c:if>
				</table>
			</div>
			<div class="ledger-actions"><a href="${pageContext.request.contextPath}/payroll/register.do" class="button button-outline">급여대장 목록</a></div>
		</section>
	</main>
	<c:if test="${not empty ledgerPopupMessage}">
		<div class="ledger-alert" role="alertdialog" aria-modal="true" aria-labelledby="ledger-alert-message">
			<a class="ledger-alert__backdrop" href="${pageContext.request.contextPath}/payroll/item-ledger.do" aria-label="안내 닫기"></a>
			<div class="ledger-alert__panel">
				<p id="ledger-alert-message"><c:out value="${ledgerPopupMessage}" /></p>
				<a href="${pageContext.request.contextPath}/payroll/item-ledger.do">확인</a>
			</div>
		</div>
	</c:if>
	<%@ include file="/WEB-INF/view/common/footer.jspf" %>
</body>
</html>
