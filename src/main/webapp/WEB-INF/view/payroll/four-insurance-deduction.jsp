<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>4대보험 공제내역</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/common.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/payzon-ui.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/payroll/four-insurance-deduction.css">
</head>
<body>
	<%@ include file="/WEB-INF/view/common/header.jspf" %>
	<main class="page-content four-insurance-page">
		<header class="page-heading"><div><p>급여관리</p><h1>4대보험 공제내역</h1></div></header>
		<section class="insurance-card">
			<form class="insurance-search" method="get" action="${pageContext.request.contextPath}/payroll/four-insurance.do">
				<div class="search-field year-field"><label for="paymentYear">귀속연도</label><select id="paymentYear" name="year"><option value="">선택</option><c:forEach var="year" items="${paymentYears}"><option value="${year}" <c:if test="${year eq selectedYear}">selected</c:if>>${year}년</option></c:forEach></select></div>
				<div class="search-field month-field"><label for="paymentMonth">귀속월</label><select id="paymentMonth" name="month"><option value="">선택</option><c:forEach var="month" begin="1" end="12"><option value="${month}" <c:if test="${month eq selectedMonth}">selected</c:if>>${month}월</option></c:forEach></select></div>
				<div class="search-field round-field"><label for="paymentRound">급여차수</label><select id="paymentRound" name="round"><option value="">차수 선택</option><c:forEach var="round" begin="1" end="10"><option value="${round}" <c:if test="${round eq selectedRound}">selected</c:if>>급여-${round}차</option></c:forEach></select></div>
				<button type="submit" class="button button-primary">공제내역 조회</button>
				<div class="period-information"><div><strong>정산기간</strong><span><c:choose><c:when test="${not empty calculationStart and not empty calculationEnd}">${calculationStart} ~ ${calculationEnd}</c:when><c:otherwise>-</c:otherwise></c:choose></span></div><div><strong>급여지급일</strong><span>${empty paymentDate ? '-' : paymentDate}</span></div></div>
			</form>
			<div class="insurance-table-wrap">
				<table class="insurance-table">
					<colgroup><col class="type-col"><col class="name-col"><col class="department-col"><col class="position-col"><c:forEach begin="1" end="15"><col class="money-col"></c:forEach></colgroup>
					<thead>
						<tr><th colspan="4" class="employee-group">사원정보</th><th colspan="3" class="insurance-group pension-group">국민연금</th><th colspan="3" class="insurance-group health-group">건강보험</th><th colspan="3" class="insurance-group care-group">노인장기요양보험</th><th colspan="3" class="insurance-group employment-group">고용보험</th><th colspan="3" class="insurance-group total-group">총 합계</th></tr>
						<tr><th>구분</th><th>성명</th><th>부서</th><th>직위</th><c:forEach begin="1" end="5"><th>사업주</th><th>근로자</th><th class="subtotal-header">합계</th></c:forEach></tr>
					</thead>
					<tbody>
					<c:choose>
						<c:when test="${not empty insuranceDeductions}">
							<c:forEach var="row" items="${insuranceDeductions}"><tr><td>${row.employmentTypeName}</td><td>${row.employeeName}</td><td>${row.departmentName}</td><td>${row.positionName}</td><td class="amount">${row.pensionEmployer}</td><td class="amount">${row.pensionEmployee}</td><td class="amount subtotal">${row.pensionTotal}</td><td class="amount">${row.healthEmployer}</td><td class="amount">${row.healthEmployee}</td><td class="amount subtotal">${row.healthTotal}</td><td class="amount">${row.careEmployer}</td><td class="amount">${row.careEmployee}</td><td class="amount subtotal">${row.careTotal}</td><td class="amount">${row.employmentEmployer}</td><td class="amount">${row.employmentEmployee}</td><td class="amount subtotal">${row.employmentTotal}</td><td class="amount total-value">${row.totalEmployer}</td><td class="amount total-value">${row.totalEmployee}</td><td class="amount grand-value">${row.grandTotal}</td></tr></c:forEach>
						</c:when>
						<c:otherwise><tr><td colspan="19" class="empty-row">조회된 4대보험 공제내역이 없습니다.</td></tr></c:otherwise>
					</c:choose>
					</tbody>
					<c:if test="${not empty insuranceDeductions}"><tfoot><tr><th colspan="4">합계</th><td class="amount">${insuranceTotals.pensionEmployer}</td><td class="amount">${insuranceTotals.pensionEmployee}</td><td class="amount subtotal">${insuranceTotals.pensionTotal}</td><td class="amount">${insuranceTotals.healthEmployer}</td><td class="amount">${insuranceTotals.healthEmployee}</td><td class="amount subtotal">${insuranceTotals.healthTotal}</td><td class="amount">${insuranceTotals.careEmployer}</td><td class="amount">${insuranceTotals.careEmployee}</td><td class="amount subtotal">${insuranceTotals.careTotal}</td><td class="amount">${insuranceTotals.employmentEmployer}</td><td class="amount">${insuranceTotals.employmentEmployee}</td><td class="amount subtotal">${insuranceTotals.employmentTotal}</td><td class="amount total-value">${insuranceTotals.totalEmployer}</td><td class="amount total-value">${insuranceTotals.totalEmployee}</td><td class="amount grand-value">${insuranceTotals.grandTotal}</td></tr></tfoot></c:if>
				</table>
			</div>
			<div class="insurance-actions"><a class="button button-outline" href="${pageContext.request.contextPath}/payroll/register.do">급여대장 목록</a></div>
		</section>
	</main>
	<%@ include file="/WEB-INF/view/common/footer.jspf" %>
</body>
</html>
