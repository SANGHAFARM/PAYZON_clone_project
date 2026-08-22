<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>인사관리 &gt; 제증명서 발급대장</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/employees/certificate-register.css?v=20260815-2">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/payzon-ui.css?v=20260821-1">
</head>
<body>
	<%@ include file="/WEB-INF/view/common/header.jspf" %>

	<main class="page-content certificate-register-page">
		<header class="page-heading">
			<div><p>인사관리</p><h1>제증명서 발급대장</h1></div>
		</header>

		<%-- CERTIFICATE_ISSUANCE 중심 JOIN 결과의 검색 및 발급내역 목록 --%>
		<section class="register-card">
			<form class="register-search" action="${pageContext.request.contextPath}/employees/certificate-register.do" method="get">
				<input type="hidden" name="mode" value="search">
				<label>
					<span class="sr-only">증명서 구분</span>
					<select name="certificateType">
						<option value="">전체</option>
						<option value="WORKING" ${condition.certificateType eq 'WORKING' ? 'selected' : ''}>재직증명서</option>
						<option value="CAREER" ${condition.certificateType eq 'CAREER' ? 'selected' : ''}>경력증명서</option>
						<option value="RETIREMENT" ${condition.certificateType eq 'RETIREMENT' ? 'selected' : ''}>퇴직증명서</option>
					</select>
				</label>
				<label class="date-range">
					<span class="sr-only">발급일</span>
					<input type="date" name="issueDateFrom" value="${condition.issueDateFrom}">
					<i>~</i>
					<input type="date" name="issueDateTo" value="${condition.issueDateTo}">
				</label>
				<label class="keyword-field">
					<span class="sr-only">검색어</span>
					<input type="search" name="keyword" value="<c:out value='${condition.keyword}' />" placeholder="검색어 입력">
				</label>
				<button type="submit" class="search-button">검색</button>
				<a class="all-button" href="${pageContext.request.contextPath}/employees/certificate-register.do">전체보기</a>
				<p class="result-count">총 발급건수 <strong><c:out value="${totalCount}" default="0" /></strong>건</p>
			</form>

			<form action="${pageContext.request.contextPath}/employees/certificate-register-delete.do" method="post">
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
					<c:if test="${pageInfo.hasPrevious}"><c:url var="previousUrl" value="/employees/certificate-register.do"><c:param name="page" value="${pageInfo.previousPage}"/><c:param name="certificateType" value="${condition.certificateType}"/><c:param name="issueDateFrom" value="${condition.issueDateFrom}"/><c:param name="issueDateTo" value="${condition.issueDateTo}"/><c:param name="keyword" value="${condition.keyword}"/></c:url><a href="${previousUrl}">이전</a></c:if>
					<c:forEach var="pageNo" begin="${pageInfo.startPage}" end="${pageInfo.endPage}">
						<c:url var="pageUrl" value="/employees/certificate-register.do"><c:param name="page" value="${pageNo}"/><c:param name="certificateType" value="${condition.certificateType}"/><c:param name="issueDateFrom" value="${condition.issueDateFrom}"/><c:param name="issueDateTo" value="${condition.issueDateTo}"/><c:param name="keyword" value="${condition.keyword}"/></c:url><a class="${pageNo eq pageInfo.currentPage ? 'is-current' : ''}" href="${pageUrl}"><c:out value="${pageNo}" /></a>
					</c:forEach>
					<c:if test="${pageInfo.hasNext}"><c:url var="nextUrl" value="/employees/certificate-register.do"><c:param name="page" value="${pageInfo.nextPage}"/><c:param name="certificateType" value="${condition.certificateType}"/><c:param name="issueDateFrom" value="${condition.issueDateFrom}"/><c:param name="issueDateTo" value="${condition.issueDateTo}"/><c:param name="keyword" value="${condition.keyword}"/></c:url><a href="${nextUrl}">다음</a></c:if>
				</nav>

				<div class="delete-actions">
					<button type="submit" name="deleteAction" value="requestSelected">선택삭제</button>
					<button type="submit" name="deleteAction" value="requestAll" class="danger">전체삭제</button>
				</div>
			</form>
		</section>

		<c:if test="${not empty popupMessage}"><div class="register-alert" role="alertdialog" aria-modal="true" aria-labelledby="register-alert-message"><a class="register-alert__backdrop" href="${pageContext.request.contextPath}/employees/certificate-register.do" aria-label="닫기"></a><div class="register-alert__panel"><p id="register-alert-message"><c:out value="${popupMessage}" /></p><a href="${pageContext.request.contextPath}/employees/certificate-register.do">확인</a></div></div></c:if>

		<c:if test="${deleteConfirmation}"><div class="register-alert" role="alertdialog" aria-modal="true" aria-labelledby="delete-confirmation-message"><a class="register-alert__backdrop" href="${pageContext.request.contextPath}/employees/certificate-register.do" aria-label="삭제 취소"></a><form class="register-alert__panel" action="${pageContext.request.contextPath}/employees/certificate-register-delete.do" method="post"><c:choose><c:when test="${deleteMode eq 'ALL'}"><p id="delete-confirmation-message">전체 <strong><c:out value="${totalCount}" />건</strong>의 발급내역을 삭제하시겠습니까?</p><p class="register-alert__warning">삭제한 발급내역은 복구할 수 없습니다.</p><input type="hidden" name="deleteAction" value="confirmAll"></c:when><c:otherwise><p id="delete-confirmation-message">선택한 <strong><c:out value="${deleteCertificateCount}" />건</strong>의 발급내역을 삭제하시겠습니까?</p><p class="register-alert__warning">삭제한 발급내역은 복구할 수 없습니다.</p><input type="hidden" name="deleteAction" value="confirmSelected"><c:forEach var="certificateId" items="${deleteCertificateIds}"><input type="hidden" name="certificateIds" value="${certificateId}"></c:forEach></c:otherwise></c:choose><div class="register-alert__actions"><button type="submit">삭제</button><a href="${pageContext.request.contextPath}/employees/certificate-register.do">취소</a></div></form></div></c:if>
	</main>

	<%@ include file="/WEB-INF/view/common/footer.jspf" %>
</body>
</html>
