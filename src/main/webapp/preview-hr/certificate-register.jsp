<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>인사관리 &gt; 제증명서 발급대장</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/hr/certificate-register.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/payzon-ui.css">
</head>
<body>
	<%@ include file="/WEB-INF/view/common/header.jspf" %>

	<main class="page-content certificate-register-page">
		<header class="page-heading">
			<div><p>인사관리</p><h1>제증명서 발급대장</h1></div>
		</header>

		<c:if test="${not empty message}">
			<p class="form-message"><c:out value="${message}" /></p>
		</c:if>

		<section class="register-card">
			<form class="register-search" action="${pageContext.request.contextPath}/personnel/certificate-register.do" method="get">
				<label>
					<span class="sr-only">증명서 구분</span>
					<select name="certificateType">
						<option value="">전체</option>
						<option value="WORKING" ${param.certificateType eq 'WORKING' ? 'selected' : ''}>재직증명서</option>
						<option value="CAREER" ${param.certificateType eq 'CAREER' ? 'selected' : ''}>경력증명서</option>
						<option value="RETIREMENT" ${param.certificateType eq 'RETIREMENT' ? 'selected' : ''}>퇴직증명서</option>
					</select>
				</label>
				<label class="date-range">
					<span class="sr-only">발급일</span>
					<input type="date" name="issueDateFrom" value="${param.issueDateFrom}">
					<i>~</i>
					<input type="date" name="issueDateTo" value="${param.issueDateTo}">
				</label>
				<label class="keyword-field">
					<span class="sr-only">검색어</span>
					<input type="search" name="keyword" value="${param.keyword}" placeholder="검색어 입력">
				</label>
				<button type="submit" class="search-button">검색</button>
				<a class="all-button" href="${pageContext.request.contextPath}/personnel/certificate-register.do">전체보기</a>
				<p class="result-count">총 발급건수 <strong><c:out value="${totalCount}" default="0" /></strong>건</p>
			</form>

			<form action="${pageContext.request.contextPath}/personnel/certificate-register-delete.do" method="post">
				<div class="register-table-wrap">
					<table class="register-table">
						<colgroup>
							<col class="check-col"><col class="number-col"><col class="type-col"><col class="use-col">
							<col class="employment-col"><col class="name-col"><col class="department-col">
							<col class="position-col"><col class="date-col">
						</colgroup>
						<thead>
							<tr>
								<th>선택</th>
								<th>발급번호</th><th>발급대장</th><th>발급용도</th><th>구분</th>
								<th>성명</th><th>부서</th><th>직위</th><th>발급일</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="certificate" items="${certificates}">
								<tr>
									<td><input type="checkbox" name="certificateIds" value="${certificate.certificateId}" aria-label="${certificate.certificateNo} 선택"></td>
									<td><c:out value="${certificate.certificateNo}" /></td>
									<td><span class="certificate-type certificate-type--${certificate.certificateType}"><c:out value="${certificate.certificateTypeName}" /></span></td>
									<td><c:out value="${certificate.certificateUse}" /></td>
									<td><c:out value="${certificate.employmentType}" /></td>
									<td><c:out value="${certificate.employeeName}" /></td>
									<td><c:out value="${certificate.departmentName}" /></td>
									<td><c:out value="${certificate.positionName}" /></td>
									<td><c:out value="${certificate.issueDate}" /></td>
								</tr>
							</c:forEach>
							<c:if test="${empty certificates}"><tr><td colspan="9" class="empty-row">조회된 증명서 발급 내역이 없습니다.</td></tr></c:if>
						</tbody>
					</table>
				</div>

				<nav class="pagination" aria-label="페이지 이동">
					<c:if test="${pageInfo.hasPrevious}"><a href="?page=${pageInfo.previousPage}">이전</a></c:if>
					<c:forEach var="pageNo" begin="${pageInfo.startPage}" end="${pageInfo.endPage}">
						<a class="${pageNo eq pageInfo.currentPage ? 'is-current' : ''}" href="?page=${pageNo}"><c:out value="${pageNo}" /></a>
					</c:forEach>
					<c:if test="${pageInfo.hasNext}"><a href="?page=${pageInfo.nextPage}">다음</a></c:if>
				</nav>

				<div class="delete-actions">
					<button type="submit" name="deleteMode" value="SELECTED">선택삭제</button>
					<button type="submit" name="deleteMode" value="ALL" class="danger">전체삭제</button>
				</div>
			</form>
		</section>
	</main>

	<%@ include file="/WEB-INF/view/common/footer.jspf" %>
</body>
</html>
