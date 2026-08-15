<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>급여대장</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/common.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/payzon-ui.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/payroll/payroll-register.css?v=20260815-1">
</head>
<body>
	<%@ include file="/WEB-INF/view/common/header.jspf" %>
	<main class="page-content payment-register-page">
		<header class="page-heading"><div><p>급여관리</p><h1>급여대장</h1></div></header>

		<section class="register-card">
			<form class="register-search" method="get" action="${pageContext.request.contextPath}/payroll/register.do">
				<label for="registerYear">귀속연도</label>
				<select id="registerYear" name="year">
					<option value="">선택</option>
					<c:forEach var="year" items="${paymentYears}"><option value="${year}" <c:if test="${year eq selectedYear}">selected</c:if>>${year}년</option></c:forEach>
				</select>
				<button type="submit" class="button button-primary">조회</button>
			</form>

			<div class="table-wrap">
				<table class="register-table">
					<colgroup><col class="month-col"><col class="round-col"><col class="period-col"><col class="date-col"><col class="count-col"><col class="money-col"><col class="money-col"><col class="money-col"><col class="delete-col"></colgroup>
					<thead><tr><th>귀속연월</th><th>급여차수</th><th>정산기간</th><th>지급일</th><th>인원</th><th>지급총액</th><th>공제총액</th><th>실지급액</th><th>삭제</th></tr></thead>
					<tbody>
					<c:choose>
						<c:when test="${not empty paymentRegisters}">
							<c:forEach var="register" items="${paymentRegisters}">
								<tr>
									<td><a href="${pageContext.request.contextPath}/payroll/register/detail.do?registerId=${register.registerId}">${register.paymentYearMonth}</a></td>
									<td><a href="${pageContext.request.contextPath}/payroll/register/detail.do?registerId=${register.registerId}">${register.paymentRoundName}</a></td>
									<td>${register.calculationStart} ~ ${register.calculationEnd}</td><td>${register.paymentDate}</td><td>${register.employeeCount}</td>
									<td class="amount give">${register.totalPayment}</td><td class="amount deduction">${register.totalDeduction}</td><td class="amount">${register.netPayment}</td>
									<td><form method="post" action="${pageContext.request.contextPath}/payroll/register/delete.do"><input type="hidden" name="registerId" value="${register.registerId}"><input type="hidden" name="registerName" value="${register.paymentYearMonth} ${register.paymentRoundName}"><input type="hidden" name="year" value="${selectedYear}"><button type="submit" name="action" value="requestDelete" class="table-button">삭제</button></form></td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise><tr><td colspan="9" class="empty-row">조회된 급여대장이 없습니다.</td></tr></c:otherwise>
					</c:choose>
					</tbody>
					<c:if test="${not empty paymentRegisters}"><tfoot><tr><th>합계</th><td colspan="4"></td><td class="amount give">${registerTotals.totalPayment}</td><td class="amount deduction">${registerTotals.totalDeduction}</td><td class="amount">${registerTotals.netPayment}</td><td></td></tr></tfoot></c:if>
				</table>
			</div>
			<c:if test="${page.totalPages gt 0}"><nav class="pagination" aria-label="페이지 이동"><c:if test="${page.number gt 0}"><a href="?year=${selectedYear}&amp;page=${page.number - 1}">이전</a></c:if><c:forEach var="index" begin="0" end="${page.totalPages - 1}"><a class="${index eq page.number ? 'active' : ''}" href="?year=${selectedYear}&amp;page=${index}">${index + 1}</a></c:forEach><c:if test="${page.number + 1 lt page.totalPages}"><a href="?year=${selectedYear}&amp;page=${page.number + 1}">다음</a></c:if></nav></c:if>
		</section>
	</main>

	<c:if test="${deleteConfirmation}">
		<div class="register-delete-alert" role="alertdialog" aria-modal="true"
			aria-labelledby="register-delete-message">
			<a class="register-delete-alert__backdrop"
				href="${pageContext.request.contextPath}/payroll/register.do?year=${selectedYear}"
				aria-label="삭제 취소"></a>
			<form class="register-delete-alert__panel" method="post"
				action="${pageContext.request.contextPath}/payroll/register/delete.do">
				<p id="register-delete-message">
					<strong><c:out value="${deleteRegisterName}" /></strong> 급여대장을 삭제하시겠습니까?
				</p>
				<p class="register-delete-alert__warning">삭제한 급여대장은 복구할 수 없습니다.</p>
				<input type="hidden" name="registerId" value="${deleteRegisterId}">
				<input type="hidden" name="year" value="${selectedYear}">
				<div class="register-delete-alert__actions">
					<button type="submit" name="action" value="confirmDelete">삭제</button>
					<a href="${pageContext.request.contextPath}/payroll/register.do?year=${selectedYear}">취소</a>
				</div>
			</form>
		</div>
	</c:if>
	<%@ include file="/WEB-INF/view/common/footer.jspf" %>
</body>
</html>
