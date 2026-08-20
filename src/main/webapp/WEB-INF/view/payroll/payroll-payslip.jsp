<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>급여명세서</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/common.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/payroll/payroll-payslip.css?v=20260815-2">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/payzon-ui.css?v=20260821-1">
</head>
<body>
	<%@ include file="/WEB-INF/view/common/header.jspf" %>
	<main class="page-content payment-payslip-page">
		<header class="page-heading">
			<div>
				<p>급여관리</p>
				<h1>급여명세서</h1>
			</div>
		</header>

		<form class="period-toolbar" method="get" action="${pageContext.request.contextPath}/payroll/payslip.do">
			<div class="period-information">
				<div><strong>정산기간</strong><span><c:choose><c:when test="${not empty calculationStart and not empty calculationEnd}">${calculationStart} ~ ${calculationEnd}</c:when><c:otherwise>-</c:otherwise></c:choose></span></div>
				<div><strong>급여지급일</strong><span>${empty paymentDate ? '-' : paymentDate}</span></div>
			</div>
			<div class="period-controls">
				<div class="period-field"><label for="paymentYear">귀속연도</label><select id="paymentYear" name="paymentYear"><option value="">선택</option><c:forEach var="year" items="${paymentYears}"><option value="${year}" <c:if test="${year eq selectedYear}">selected</c:if>>${year}년</option></c:forEach></select></div>
				<div class="period-field month-field"><label for="paymentMonth">귀속월</label><select id="paymentMonth" name="paymentMonth"><option value="">선택</option><c:forEach var="month" begin="1" end="12"><option value="${month}" <c:if test="${month eq selectedMonth}">selected</c:if>>${month}월</option></c:forEach></select></div>
				<div class="period-field"><label for="paymentRound">급여차수</label><select id="paymentRound" name="paymentRound"><option value="">차수 선택</option><c:forEach var="round" begin="1" end="10"><option value="${round}" <c:if test="${round eq selectedRound}">selected</c:if>>급여-${round}차</option></c:forEach></select></div>
				<button type="submit" class="button button-primary">조회</button>
			</div>
		</form>

		<section class="payslip-workspace">
			<aside class="employee-panel">
				<form class="employee-search" method="get" action="${pageContext.request.contextPath}/payroll/payslip.do">
					<input type="hidden" name="paymentYear" value="${selectedYear}"><input type="hidden" name="paymentMonth" value="${selectedMonth}"><input type="hidden" name="paymentRound" value="${selectedRound}">
					<input type="hidden" name="mode" value="search"><input type="search" name="keyword" value="${param.keyword}" placeholder="검색어 입력"><button type="submit" class="button button-primary">검색</button><a class="button button-outline" href="${pageContext.request.contextPath}/payroll/payslip.do?paymentYear=${selectedYear}&amp;paymentMonth=${selectedMonth}&amp;paymentRound=${selectedRound}">전체보기</a>
				</form>
				<div class="employee-table-wrap"><table class="employee-table"><thead><tr><th>구분</th><th>성명</th><th>실지급액</th></tr></thead><tbody>
				<c:choose><c:when test="${not empty payslipEmployees}"><c:forEach var="employee" items="${payslipEmployees}"><c:url var="payslipEmployeeUrl" value="/payroll/payslip.do"><c:param name="paymentYear" value="${selectedYear}"/><c:param name="paymentMonth" value="${selectedMonth}"/><c:param name="paymentRound" value="${selectedRound}"/><c:param name="keyword" value="${param.keyword}"/><c:param name="employeeId" value="${employee.employeeId}"/></c:url><tr class="${employee.employeeId eq selectedEmployee.employeeId ? 'selected-row' : ''}"><td><a class="employee-row-link" href="${payslipEmployeeUrl}">${employee.employmentTypeName}</a></td><td><a class="employee-row-link" href="${payslipEmployeeUrl}">${employee.employeeName}</a></td><td class="amount"><a class="employee-row-link" href="${payslipEmployeeUrl}">${employee.netPayment}</a></td></tr></c:forEach></c:when><c:otherwise>
					<tr><td colspan="3" class="empty-row">조회된 급여명세서 대상 사원이 없습니다.</td></tr>
				</c:otherwise></c:choose>
				</tbody></table></div>
			</aside>

			<article class="payslip-document">
				<header class="document-header"><div class="company-logo"><c:choose><c:when test="${not empty company.logoUrl}"><img src="${pageContext.request.contextPath}${company.logoUrl}" alt="회사 로고"></c:when><c:otherwise><span>회사 로고</span></c:otherwise></c:choose></div><h2>급 여 명 세 서</h2></header>
				<table class="employee-information"><tbody>
				<tr><th>성명</th><td>${selectedEmployee.employeeName}</td><th>생년월일</th><td>${selectedEmployee.birthDate}</td></tr>
				<tr><th>부서</th><td>${selectedEmployee.departmentName}</td><th>직급</th><td>${selectedEmployee.positionName}</td></tr>
				<tr><th>입사일</th><td>${selectedEmployee.hireDate}</td><th>급여지급일</th><td>${paymentDate}</td></tr>
				</tbody></table>

				<section class="pay-details"><h3>급여내역</h3><table class="pay-detail-table"><colgroup><col class="category-col"><col class="item-col"><col class="amount-col"><col></colgroup><thead><tr><th>구분</th><th>항목명</th><th>금액</th><th>산출식 또는 산출방법</th></tr></thead><tbody>
				<c:choose><c:when test="${not empty paymentItems}"><c:forEach var="item" items="${paymentItems}" varStatus="status"><tr><c:if test="${status.first}"><th class="category payment-category" rowspan="${fn:length(paymentItems) + 1}"><span>지급</span><span>항목</span></th></c:if><td>${item.itemName}</td><td class="amount">${selectedEmployee.paymentAmounts[item.itemId]}</td><td>${selectedEmployee.paymentCalculations[item.itemId]}</td></tr></c:forEach></c:when><c:otherwise>
				<tr><th class="category payment-category" rowspan="2"><span>지급</span><span>항목</span></th><td colspan="3" class="empty-item">등록된 지급항목이 없습니다.</td></tr>
				</c:otherwise></c:choose>
				<tr class="total-row payment-total"><th colspan="2">지급총액</th><td class="amount">${selectedEmployee.totalPayment}</td></tr>
				<c:choose><c:when test="${not empty deductionItems}"><c:forEach var="item" items="${deductionItems}" varStatus="status"><tr><c:if test="${status.first}"><th class="category deduction-category" rowspan="${fn:length(deductionItems) + 1}"><span>공제</span><span>항목</span></th></c:if><td>${item.itemName}</td><td class="amount">${selectedEmployee.deductionAmounts[item.itemId]}</td><td>${selectedEmployee.deductionCalculations[item.itemId]}</td></tr></c:forEach></c:when><c:otherwise>
				<tr><th class="category deduction-category" rowspan="2"><span>공제</span><span>항목</span></th><td colspan="3" class="empty-item">등록된 공제항목이 없습니다.</td></tr>
				</c:otherwise></c:choose>
				<tr class="total-row deduction-total"><th colspan="2">공제총액</th><td class="amount">${selectedEmployee.totalDeduction}</td></tr>
				<tr class="net-row"><th colspan="3">실수령액</th><td class="amount">${selectedEmployee.netPayment} 원</td></tr>
				</tbody></table></section>

				<p class="closing-message">귀하의 노고에 감사드리며, 수고 많으셨습니다.</p>
				<footer class="document-footer"><div class="approval-block"><div class="representative"><strong>${company.companyName}</strong><span>대표이사 ${company.representativeName}</span></div><div class="company-stamp"><c:choose><c:when test="${not empty company.stampUrl}"><img src="${pageContext.request.contextPath}${company.stampUrl}" alt="회사 도장"></c:when><c:otherwise><span>회사 도장</span></c:otherwise></c:choose></div></div></footer>
			</article>
		</section>
	</main>
	<c:if test="${not empty payslipPopupMessage}">
		<c:url var="payslipReturnUrl" value="/payroll/payslip.do"><c:param name="paymentYear" value="${selectedYear}"/><c:param name="paymentMonth" value="${selectedMonth}"/><c:param name="paymentRound" value="${selectedRound}"/></c:url>
		<div class="payslip-alert" role="alertdialog" aria-modal="true" aria-labelledby="payslip-alert-message"><a class="payslip-alert__backdrop" href="${payslipReturnUrl}" aria-label="닫기"></a><div class="payslip-alert__panel"><p id="payslip-alert-message"><c:out value="${payslipPopupMessage}"/></p><a href="${payslipReturnUrl}">확인</a></div></div>
	</c:if>
	<%@ include file="/WEB-INF/view/common/footer.jspf" %>
</body>
</html>
