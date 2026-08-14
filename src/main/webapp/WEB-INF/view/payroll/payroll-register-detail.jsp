<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="detailColumnCount" value="${7 + fn:length(paymentItems) + fn:length(deductionItems)}" />
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>급여대장 상세</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/common.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/payzon-ui.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/payroll/payroll-register.css">
</head>
<body>
	<%@ include file="/WEB-INF/view/common/header.jspf" %>
	<main class="page-content payment-register-page">
		<header class="page-heading"><div><p>급여관리</p><h1>급여대장 상세</h1></div></header>
		<section class="register-card detail-card">
			<form class="detail-topbar" method="get" action="${pageContext.request.contextPath}/payroll/register/detail.do">
				<input type="hidden" name="registerId" value="${register.registerId}">
				<dl class="register-information"><div><dt>귀속연월</dt><dd>${empty register.paymentYearMonth ? '-' : register.paymentYearMonth}</dd></div><div><dt>급여차수</dt><dd>${empty register.paymentRoundName ? '-' : register.paymentRoundName}</dd></div><div><dt>정산기간</dt><dd><c:choose><c:when test="${not empty register.calculationStart and not empty register.calculationEnd}">${register.calculationStart} ~ ${register.calculationEnd}</c:when><c:otherwise>-</c:otherwise></c:choose></dd></div><div><dt>지급일</dt><dd>${empty register.paymentDate ? '-' : register.paymentDate}</dd></div></dl>
				<div class="detail-filter"><select name="employmentType"><option value="">전체 구분</option><c:forEach var="type" items="${employmentTypes}"><option value="${type.code}" <c:if test="${type.code eq selectedEmploymentType}">selected</c:if>>${type.name}</option></c:forEach></select><select name="departmentId"><option value="">전체 부서</option><c:forEach var="department" items="${departments}"><option value="${department.departmentId}" <c:if test="${department.departmentId eq selectedDepartmentId}">selected</c:if>>${department.departmentName}</option></c:forEach></select><select name="incomeType"><option value="">전체 소득</option><option value="WORK" <c:if test="${selectedIncomeType eq 'WORK'}">selected</c:if>>근로소득자</option><option value="BUSINESS" <c:if test="${selectedIncomeType eq 'BUSINESS'}">selected</c:if>>사업소득자</option><option value="DAILY" <c:if test="${selectedIncomeType eq 'DAILY'}">selected</c:if>>일용근로자</option></select><button type="submit" class="button button-primary">조회</button></div>
			</form>

			<div class="detail-table-wrap">
				<table class="detail-table">
					<thead><tr>
						<th class="fixed type">구분</th><th class="fixed name">성명</th><th class="fixed department">부서</th><th class="fixed position">직위</th>
						<c:forEach var="item" items="${paymentItems}"><th class="give-item">${item.itemName}</th></c:forEach>
						<th class="give-total">지급총액</th>
						<c:forEach var="item" items="${deductionItems}"><th class="deduction-item">${item.itemName}</th></c:forEach>
						<th class="deduction-total">공제총액</th><th class="net-total">실지급액</th>
					</tr></thead>
					<tbody><c:choose><c:when test="${not empty registerEmployees}"><c:forEach var="employee" items="${registerEmployees}"><tr><td>${employee.employmentTypeName}</td><td>${employee.employeeName}</td><td>${employee.departmentName}</td><td>${employee.positionName}</td><c:forEach var="item" items="${paymentItems}"><td>${employee.paymentAmounts[item.itemId]}</td></c:forEach><td class="give">${employee.totalPayment}</td><c:forEach var="item" items="${deductionItems}"><td>${employee.deductionAmounts[item.itemId]}</td></c:forEach><td class="deduction">${employee.totalDeduction}</td><td class="net">${employee.netPayment}</td></tr></c:forEach></c:when><c:otherwise><tr><td colspan="${detailColumnCount}" class="empty-row">조회된 사원별 급여내역이 없습니다.</td></tr></c:otherwise></c:choose></tbody>
					<c:if test="${not empty registerEmployees}"><tfoot><tr><th colspan="4">합계</th><c:forEach var="item" items="${paymentItems}"><td>${registerTotals.paymentAmounts[item.itemId]}</td></c:forEach><td class="give">${registerTotals.totalPayment}</td><c:forEach var="item" items="${deductionItems}"><td>${registerTotals.deductionAmounts[item.itemId]}</td></c:forEach><td class="deduction">${registerTotals.totalDeduction}</td><td class="net">${registerTotals.netPayment}</td></tr></tfoot></c:if>
				</table>
			</div>
			<div class="detail-actions"><a class="button button-outline" href="${pageContext.request.contextPath}/payroll/register.do?year=${register.paymentYear}">급여대장 목록</a></div>
		</section>
	</main>
	<%@ include file="/WEB-INF/view/common/footer.jspf" %>
</body>
</html>
