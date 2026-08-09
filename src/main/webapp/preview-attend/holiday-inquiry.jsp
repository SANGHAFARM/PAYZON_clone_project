<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>근태관리 &gt; 휴가조회</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/attend/holiday-inquiry.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/payzon-ui.css">
</head>
<body>
	<%@ include file="/WEB-INF/view/common/header.jspf" %>
	<main class="page-content holiday-page">
		<header class="page-heading"><div><p>근태관리</p><h1>휴가조회</h1></div></header>
		<section class="holiday-card">
			<form class="holiday-search" action="${pageContext.request.requestURI}" method="get">
				<select name="holidayItemId" aria-label="휴가항목"><option value="">휴가항목 선택</option><c:forEach var="item" items="${holidayItems}"><option value="${item.itemId}" ${param.holidayItemId eq item.itemId ? 'selected' : ''}><c:out value="${item.itemName}" /></option></c:forEach></select>
				<input type="search" name="keyword" value="${param.keyword}" placeholder="검색어 입력">
				<button type="submit">검색</button>
				<a href="${pageContext.request.requestURI}">전체보기</a>
				<div class="filter-group">
					<select name="status" aria-label="상태별"><option value="">상태별</option><option value="WORK">재직</option><option value="RETIRED">퇴직</option></select>
					<select name="employmentType" aria-label="구분별"><option value="">구분별</option><c:forEach var="type" items="${employmentTypes}"><option value="${type}"><c:out value="${type}" /></option></c:forEach></select>
					<select name="departmentId" aria-label="부서별"><option value="">부서별</option><c:forEach var="department" items="${departments}"><option value="${department.departmentId}"><c:out value="${department.departmentName}" /></option></c:forEach></select>
					<select name="positionId" aria-label="직위별"><option value="">직위별</option><c:forEach var="position" items="${positions}"><option value="${position.positionId}"><c:out value="${position.positionName}" /></option></c:forEach></select>
					<select name="pageSize" aria-label="목록 수"><option value="10">10개씩 보기</option><option value="30" selected>30개씩 보기</option><option value="50">50개씩 보기</option><option value="100">100개씩 보기</option></select>
					<button type="submit" class="filter-button">조회</button>
				</div>
			</form>

			<div class="holiday-table-wrap">
				<table class="holiday-table">
					<thead><tr><th>구분</th><th>사원번호</th><th>성명</th><th>부서</th><th>직위</th><th>휴가항목</th><th>전체</th><th>사용</th><th>잔여</th></tr></thead>
					<tbody>
						<c:forEach var="employee" items="${holidayEmployees}">
							<c:set var="modalId" value="holiday-modal-${employee.employeeId}" />
							<tr><td><a href="#${modalId}"><c:out value="${employee.employmentType}" /></a></td><td><a href="#${modalId}"><c:out value="${employee.employeeNo}" /></a></td><td><a href="#${modalId}"><c:out value="${employee.name}" /></a></td><td><a href="#${modalId}"><c:out value="${employee.departmentName}" /></a></td><td><a href="#${modalId}"><c:out value="${employee.positionName}" /></a></td><td><a href="#${modalId}"><c:out value="${employee.holidayName}" /></a></td><td class="total-days"><a href="#${modalId}"><c:out value="${employee.totalDays}" /></a></td><td class="used-days"><a href="#${modalId}"><c:out value="${employee.usedDays}" /></a></td><td class="remaining-days"><a href="#${modalId}"><c:out value="${employee.remainingDays}" /></a></td></tr>
						</c:forEach>
						<c:if test="${empty holidayEmployees}">
							<tr><td colspan="9" class="empty-row">조회된 사원별 휴가 내역이 없습니다.</td></tr>
						</c:if>
					</tbody>
				</table>
			</div>

			<nav class="pagination" aria-label="페이지 이동"><c:if test="${pageInfo.hasPrevious}"><a href="?page=${pageInfo.previousPage}">이전</a></c:if><c:forEach var="pageNo" begin="${pageInfo.startPage}" end="${pageInfo.endPage}"><a class="${pageNo eq pageInfo.currentPage ? 'is-current' : ''}" href="?page=${pageNo}"><c:out value="${pageNo}" /></a></c:forEach><c:if test="${pageInfo.hasNext}"><a href="?page=${pageInfo.nextPage}">다음</a></c:if></nav>
		</section>
	</main>

	<c:forEach var="employee" items="${holidayEmployees}">
		<div class="holiday-modal-overlay" id="holiday-modal-${employee.employeeId}">
			<section class="holiday-modal" role="dialog" aria-modal="true" aria-labelledby="holiday-title-${employee.employeeId}">
				<header><h2>사원별 휴가현황</h2><a href="#" aria-label="닫기">&times;</a></header>
				<div class="holiday-modal-body">
					<h3 id="holiday-title-${employee.employeeId}">[<c:out value="${employee.departmentName}" />] <c:out value="${employee.name}" /> <c:out value="${employee.positionName}" /> 휴가현황</h3>
					<table><thead><tr><th>번호</th><th>입력일자</th><th>휴가항목</th><th>근태항목</th><th>기간</th><th>일수</th><th>적요</th></tr></thead><tbody><c:forEach var="record" items="${employee.holidayRecords}"><tr><td><c:out value="${record.rowNumber}" /></td><td><c:out value="${record.inputDate}" /></td><td><c:out value="${record.holidayName}" /></td><td><c:out value="${record.attendanceItemName}" /></td><td><c:out value="${record.period}" /></td><td class="record-days"><c:out value="${record.days}" /></td><td><c:out value="${record.note}" /></td></tr></c:forEach><c:if test="${empty employee.holidayRecords}"><tr><td colspan="7" class="empty-row">등록된 휴가 사용내역이 없습니다.</td></tr></c:if></tbody><tfoot><tr><th colspan="2">합계</th><td colspan="5"><span>총 휴가일수 : <strong><c:out value="${employee.totalDays}" /></strong></span><span>사용일수 : <strong class="used-days"><c:out value="${employee.usedDays}" /></strong></span><span>잔여일수 : <strong class="remaining-days"><c:out value="${employee.remainingDays}" /></strong></span></td></tr></tfoot></table>
				</div>
			</section>
		</div>
	</c:forEach>
	<%@ include file="/WEB-INF/view/common/footer.jspf" %>
</body>
</html>
