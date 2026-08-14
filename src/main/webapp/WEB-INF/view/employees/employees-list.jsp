<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>인사관리 &gt; 사원현황/관리</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/employees/employees-list.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/payzon-ui.css">
	<style>
		<c:if test="${not visibleColumns.contains('employmentType')}">.employee-table th:nth-child(2), .employee-table td:nth-child(2) { display:none; }</c:if>
		<c:if test="${not visibleColumns.contains('joinDate')}">.employee-table th:nth-child(3), .employee-table td:nth-child(3) { display:none; }</c:if>
		<c:if test="${not visibleColumns.contains('employeeNo')}">.employee-table th:nth-child(4), .employee-table td:nth-child(4) { display:none; }</c:if>
		<c:if test="${not visibleColumns.contains('name')}">.employee-table th:nth-child(5), .employee-table td:nth-child(5) { display:none; }</c:if>
		<c:if test="${not visibleColumns.contains('englishName')}">.employee-table th:nth-child(6), .employee-table td:nth-child(6) { display:none; }</c:if>
		<c:if test="${not visibleColumns.contains('department')}">.employee-table th:nth-child(7), .employee-table td:nth-child(7) { display:none; }</c:if>
		<c:if test="${not visibleColumns.contains('position')}">.employee-table th:nth-child(8), .employee-table td:nth-child(8) { display:none; }</c:if>
		<c:if test="${not visibleColumns.contains('residentNo')}">.employee-table th:nth-child(9), .employee-table td:nth-child(9) { display:none; }</c:if>
		<c:if test="${not visibleColumns.contains('nationalityType')}">.employee-table th:nth-child(10), .employee-table td:nth-child(10) { display:none; }</c:if>
		<c:if test="${not visibleColumns.contains('address')}">.employee-table th:nth-child(11), .employee-table td:nth-child(11) { display:none; }</c:if>
		<c:if test="${not visibleColumns.contains('phone')}">.employee-table th:nth-child(12), .employee-table td:nth-child(12) { display:none; }</c:if>
		<c:if test="${not visibleColumns.contains('mobile')}">.employee-table th:nth-child(13), .employee-table td:nth-child(13) { display:none; }</c:if>
		<c:if test="${not visibleColumns.contains('email')}">.employee-table th:nth-child(14), .employee-table td:nth-child(14) { display:none; }</c:if>
		<c:if test="${not visibleColumns.contains('sns')}">.employee-table th:nth-child(15), .employee-table td:nth-child(15) { display:none; }</c:if>
		<c:if test="${not visibleColumns.contains('retirementDate')}">.employee-table th:nth-child(16), .employee-table td:nth-child(16) { display:none; }</c:if>
		<c:if test="${not visibleColumns.contains('status')}">.employee-table th:nth-child(17), .employee-table td:nth-child(17) { display:none; }</c:if>
		<c:if test="${not visibleColumns.contains('bankAccount')}">.employee-table th:nth-child(18), .employee-table td:nth-child(18) { display:none; }</c:if>
	</style>
</head>
<body>
	<%@ include file="/WEB-INF/view/common/header.jspf" %>
	<main class="page-content">
		<div class="employee-list-page">
			<header class="page-heading"><div><p>인사관리</p><h1>사원현황/관리</h1></div></header>
			<c:if test="${not empty message}"><p class="form-message" role="status"><c:out value="${message}" /></p></c:if>

			<%-- DB에서 집계한 재직/퇴직 및 고용형태별 사원 수 --%>
			<nav class="summary-grid" aria-label="사원 현황 요약">
				<div class="summary-card summary-card--work"><span>재직자</span><strong><c:out value="${employeeSummary.workingCount}" default="0" /></strong></div>
				<div class="summary-card"><span>정규직</span><strong><c:out value="${employeeSummary.regularCount}" default="0" /></strong></div>
				<div class="summary-card"><span>계약직</span><strong><c:out value="${employeeSummary.contractCount}" default="0" /></strong></div>
				<div class="summary-card"><span>임시직</span><strong><c:out value="${employeeSummary.temporaryCount}" default="0" /></strong></div>
				<div class="summary-card"><span>파견직</span><strong><c:out value="${employeeSummary.dispatchedCount}" default="0" /></strong></div>
				<div class="summary-card"><span>위촉직</span><strong><c:out value="${employeeSummary.commissionedCount}" default="0" /></strong></div>
				<div class="summary-card"><span>일용직</span><strong><c:out value="${employeeSummary.dailyCount}" default="0" /></strong></div>
				<div class="summary-card summary-card--retired"><span>퇴사자</span><strong><c:out value="${employeeSummary.retiredCount}" default="0" /></strong></div>
				<div class="summary-card summary-card--total"><span>전체</span><strong><c:out value="${employeeSummary.totalCount}" default="0" /></strong></div>
			</nav>

			<%-- JavaScript 없이 GET 요청으로 검색, 필터, 목록 수 조건을 전달한다. --%>
			<section class="employee-card">
				<form class="search-toolbar" action="${pageContext.request.contextPath}/employees/employees.do" method="get">
					<div class="search-group"><select name="searchTarget" aria-label="검색항목"><option value="ALL" ${condition.searchTarget eq 'ALL' ? 'selected' : ''}>전체</option><option value="NAME" ${condition.searchTarget eq 'NAME' ? 'selected' : ''}>이름</option><option value="DEPARTMENT" ${condition.searchTarget eq 'DEPARTMENT' ? 'selected' : ''}>부서</option><option value="POSITION" ${condition.searchTarget eq 'POSITION' ? 'selected' : ''}>직위</option><option value="EMPLOYEE_NO" ${condition.searchTarget eq 'EMPLOYEE_NO' ? 'selected' : ''}>사원번호</option></select><input name="keyword" value="<c:out value='${condition.keyword}' />" placeholder="검색어 입력"><button type="submit">검색</button><a href="${pageContext.request.contextPath}/employees/employees.do">전체보기</a></div>
					<div class="filter-group"><select name="employmentType" aria-label="고용형태"><option value="">고용형태별</option><c:forEach var="type" items="${employmentTypes}"><option value="${type}" ${condition.employmentType eq type ? 'selected' : ''}><c:out value="${type}" /></option></c:forEach></select><select name="status" aria-label="상태"><option value="" ${empty condition.status ? 'selected' : ''}>상태별</option><option value="WORK" ${condition.status eq 'WORK' ? 'selected' : ''}>재직</option><option value="RETIRED" ${condition.status eq 'RETIRED' ? 'selected' : ''}>퇴직</option></select><select name="pageSize" aria-label="목록 수"><option value="">리스트 수</option><option value="10" ${condition.pageSize eq 10 ? 'selected' : ''}>10개씩 보기</option><option value="30" ${condition.pageSize eq 30 ? 'selected' : ''}>30개씩 보기</option><option value="50" ${condition.pageSize eq 50 ? 'selected' : ''}>50개씩 보기</option><option value="100" ${condition.pageSize eq 100 ? 'selected' : ''}>100개씩 보기</option></select></div>
					<div class="setting-buttons"><a href="#column-modal">표시항목 설정</a></div>
				</form>

				<%-- EmployeeDao의 JOIN 결과를 JSTL로 반복 출력한다. --%>
				<form action="${pageContext.request.contextPath}/employees/employee-action.do" method="post">
					<div class="employee-table-wrap"><table class="employee-table"><thead><tr><th>선택</th><th>구분</th><th>입사일</th><th>사원번호</th><th>성명(한글)</th><th>성명(영문)</th><th>부서</th><th>직위</th><th>주민번호</th><th>내/외국인</th><th>주소</th><th>전화번호</th><th>휴대폰</th><th>이메일</th><th>SNS</th><th>퇴사일</th><th>상태</th><th>은행계좌</th></tr></thead><tbody>
						<c:forEach var="employee" items="${employees}"><tr><td><input type="checkbox" name="employeeIds" value="${employee.employeeId}" aria-label="<c:out value='${employee.name}' /> 선택"></td><td><c:out value="${employee.employmentType}" /></td><td><c:out value="${employee.joinDate}" /></td><td><a href="${pageContext.request.contextPath}/personnel/employee-register-1.do?employeeId=${employee.employeeId}"><c:out value="${employee.employeeNo}" /></a></td><td><a href="${pageContext.request.contextPath}/personnel/employee-register-1.do?employeeId=${employee.employeeId}"><c:out value="${employee.name}" /></a></td><td><c:out value="${employee.englishName}" /></td><td><c:out value="${employee.departmentName}" /></td><td><c:out value="${employee.positionName}" /></td><td><c:out value="${employee.maskedResidentNo}" /></td><td><c:out value="${employee.nationalityType}" /></td><td class="long-text"><c:out value="${employee.address}" /></td><td><c:out value="${employee.phone}" /></td><td><c:out value="${employee.mobile}" /></td><td class="email"><c:out value="${employee.email}" /></td><td><c:out value="${employee.sns}" /></td><td><c:out value="${employee.retirementDate}" /></td><td><span class="status-badge status-badge--${employee.status eq 'WORK' ? 'work' : 'retired'}">${employee.status eq 'WORK' ? '재직' : '퇴직'}</span></td><td><c:out value="${employee.bankAccount}" /></td></tr></c:forEach>
						<c:if test="${empty employees}"><tr><td colspan="18" class="empty-row">검색된 사원이 없습니다.</td></tr></c:if>
					</tbody></table></div>
					<div class="pagination">
						<c:if test="${pageInfo.hasPrevious}"><c:url var="previousUrl" value="/employees/employees.do"><c:param name="page" value="${pageInfo.previousPage}"/><c:param name="pageSize" value="${condition.pageSize}"/><c:param name="searchTarget" value="${condition.searchTarget}"/><c:param name="keyword" value="${condition.keyword}"/><c:param name="employmentType" value="${condition.employmentType}"/><c:param name="status" value="${condition.status}"/></c:url><a href="${previousUrl}">이전</a></c:if>
						<c:forEach var="pageNo" begin="${pageInfo.startPage}" end="${pageInfo.endPage}"><c:url var="pageUrl" value="/employees/employees.do"><c:param name="page" value="${pageNo}"/><c:param name="pageSize" value="${condition.pageSize}"/><c:param name="searchTarget" value="${condition.searchTarget}"/><c:param name="keyword" value="${condition.keyword}"/><c:param name="employmentType" value="${condition.employmentType}"/><c:param name="status" value="${condition.status}"/></c:url><a class="${pageNo eq pageInfo.currentPage ? 'is-current' : ''}" href="${pageUrl}">${pageNo}</a></c:forEach>
						<c:if test="${pageInfo.hasNext}"><c:url var="nextUrl" value="/employees/employees.do"><c:param name="page" value="${pageInfo.nextPage}"/><c:param name="pageSize" value="${condition.pageSize}"/><c:param name="searchTarget" value="${condition.searchTarget}"/><c:param name="keyword" value="${condition.keyword}"/><c:param name="employmentType" value="${condition.employmentType}"/><c:param name="status" value="${condition.status}"/></c:url><a href="${nextUrl}">다음</a></c:if>
					</div>
					<div class="bottom-actions"><a class="primary" href="${pageContext.request.contextPath}/personnel/employee-register-1.do">신규 사원등록</a><button class="danger" name="action" value="deleteSelected">선택 삭제</button></div>
				</form>
			</section>

			<div id="column-modal" class="settings-modal" role="dialog" aria-modal="true"><a class="modal-backdrop" href="#"></a><form class="modal-panel column-panel" action="${pageContext.request.contextPath}/employees/employee-columns.do" method="post"><div class="modal-title"><h2>표시항목 설정</h2><a href="#">×</a></div><div class="column-options">
				<label><input id="column-type" type="checkbox" name="columns" value="employmentType" checked> 구분</label><label><input id="column-join-date" type="checkbox" name="columns" value="joinDate" checked> 입사일</label><label><input id="column-number" type="checkbox" name="columns" value="employeeNo" checked> 사원번호</label><label><input id="column-name" type="checkbox" name="columns" value="name" checked> 성명(한글)</label><label><input id="column-english-name" type="checkbox" name="columns" value="englishName"> 성명(영문)</label><label><input id="column-department" type="checkbox" name="columns" value="department" checked> 부서</label><label><input id="column-position" type="checkbox" name="columns" value="position" checked> 직위</label><label><input id="column-resident" type="checkbox" name="columns" value="residentNo" checked> 주민번호</label><label><input id="column-nationality" type="checkbox" name="columns" value="nationalityType"> 내/외국인</label><label><input id="column-address" type="checkbox" name="columns" value="address"> 주소</label><label><input id="column-phone" type="checkbox" name="columns" value="phone"> 전화번호</label><label><input id="column-mobile" type="checkbox" name="columns" value="mobile" checked> 휴대폰</label><label><input id="column-email" type="checkbox" name="columns" value="email" checked> 이메일</label><label><input id="column-sns" type="checkbox" name="columns" value="sns"> SNS</label><label><input id="column-retirement" type="checkbox" name="columns" value="retirementDate" checked> 퇴사일</label><label><input id="column-status" type="checkbox" name="columns" value="status" checked> 상태</label><label><input id="column-account" type="checkbox" name="columns" value="bankAccount"> 은행계좌</label>
			</div><div class="modal-actions"><button>표시항목 저장</button></div></form></div>
		</div>
	</main>
	<%@ include file="/WEB-INF/view/common/footer.jspf" %>
</body>
</html>
