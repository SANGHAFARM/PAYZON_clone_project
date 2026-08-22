<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>근태관리 &gt; 휴가조회</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/attendance/holiday-inquiry.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common/payzon-ui.css">
</head>
<body>
	<%@ include file="/WEB-INF/view/common/header.jspf"%>
	<main class="page-content holiday-page">
		<header class="page-heading">
			<div>
				<p>근태관리</p>
				<h1>휴가조회</h1>
			</div>
		</header>
		<!-- 조회폼 -->
		<section class="holiday-card">
			<form class="holiday-search"
				action="${pageContext.request.contextPath}/attendance/leave-inquiry.do"
				method="get">
				<select name="leaveItemId" aria-label="휴가항목">
					<option value="">휴가항목 선택</option>
					<c:forEach var="item" items="${leaveItems}">
						<option value="${item.leaveItemId}"
							${item.leaveItemId eq leaveItemId ? 'selected' : ''}>
							<c:out value="${item.itemName}" />
						</option>
					</c:forEach>
				</select> <input type="search" name="keyword" value="${keyword}"
					placeholder="검색어 입력">
				<button type="submit">검색</button>
				<a
					href="${pageContext.request.contextPath}/attendance/leave-inquiry.do">전체보기</a>

				<div class="filter-group">
					<select name="status" aria-label="상태별">
						<option value="">상태별</option>
						<option value="재직" ${status eq '재직' ? 'selected' : ''}>재직</option>
						<option value="퇴직" ${status eq '퇴직' ? 'selected' : ''}>퇴직</option>
					</select> <select name="empType" aria-label="구분별">
						<option value="">구분별</option>
						<c:forEach var="type" items="${empTypes}">
							<option value="${type}" ${type eq empType ? 'selected' : ''}>
								<c:out value="${type}" />
							</option>
						</c:forEach>
					</select> <select name="departmentId" aria-label="부서별">
						<option value="">부서별</option>
						<c:forEach var="department" items="${departments}">
							<option value="${department.departmentId}"
								${department.departmentId eq departmentId ? 'selected' : ''}>
								<c:out value="${department.departmentName}" />
							</option>
						</c:forEach>
					</select> <select name="jobPositionId" aria-label="직위별">
						<option value="">직위별</option>
						<c:forEach var="jobPosition" items="${jobPositions}">
							<option value="${jobPosition.jobPositionId}"
								${jobPosition.jobPositionId eq jobPositionId ? 'selected' : ''}>
								<c:out value="${jobPosition.jobPositionName}" />
							</option>
						</c:forEach>
					</select> <select name="pageSize" aria-label="목록 수">
						<option value="10" ${pageSize eq 10 ? 'selected' : ''}>10개씩
							보기</option>
						<option value="30"
							${empty pageSize or pageSize eq 30 ? 'selected' : ''}>30개씩
							보기</option>
						<option value="50" ${pageSize eq 50 ? 'selected' : ''}>50개씩
							보기</option>
						<option value="100" ${pageSize eq 100 ? 'selected' : ''}>100개씩
							보기</option>
					</select>

					<button type="submit" class="filter-button">조회</button>
				</div>
			</form>

			<div class="holiday-table-wrap">
				<table class="holiday-table">
					<thead>
						<tr>
							<th>구분</th>
							<th>사원번호</th>
							<th>성명</th>
							<th>부서</th>
							<th>직위</th>
							<th>휴가항목</th>
							<th>전체</th>
							<th>사용</th>
							<th>잔여</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="employee" items="${leaveEmployees}">
							<c:set var="detailUrl"
								value="${pageContext.request.contextPath}/attendance/leave-inquiry.do?employeeId=${employee.employeeId}&amp;leaveItemId=${leaveItemId}#holiday-modal-${employee.employeeId}" />
							<tr>
								<td><a href="${detailUrl}"><c:out
											value="${employee.empType}" /></a></td>
								<td><a href="${detailUrl}"><c:out
											value="${employee.empNo}" /></a></td>
								<td><a href="${detailUrl}"><c:out
											value="${employee.empNameKr}" /></a></td>
								<td><a href="${detailUrl}"><c:out
											value="${employee.departmentName}" /></a></td>
								<td><a href="${detailUrl}"><c:out
											value="${employee.jobPositionName}" /></a></td>
								<td><a href="${detailUrl}"><c:out
											value="${employee.itemName}" /></a></td>
								<td class="total-days"><a href="${detailUrl}"><c:out
											value="${employee.totalDays}" /></a></td>
								<td class="used-days"><a href="${detailUrl}"><c:out
											value="${employee.usedDays}" /></a></td>
								<td class="remaining-days"><a href="${detailUrl}"><c:out
											value="${employee.remainingDays}" /></a></td>
							</tr>
						</c:forEach>
						<c:if test="${empty leaveEmployees}">
							<tr>
								<td colspan="9" class="empty-row">조회된 사원별 휴가 내역이 없습니다.</td>
							</tr>
						</c:if>
					</tbody>
				</table>
			</div>

			<nav class="pagination" aria-label="페이지 이동">
				<c:if test="${pageInfo.hasPrevious}">
					<a href="?page=${pageInfo.previousPage}">이전</a>
				</c:if>
				<c:forEach var="pageNo" begin="${pageInfo.startPage}"
					end="${pageInfo.endPage}">
					<a class="${pageNo eq pageInfo.currentPage ? 'is-current' : ''}"
						href="?page=${pageNo}"><c:out value="${pageNo}" /></a>
				</c:forEach>
				<c:if test="${pageInfo.hasNext}">
					<a href="?page=${pageInfo.nextPage}">다음</a>
				</c:if>
			</nav>
		</section>
	</main>

	<c:if test="${not empty employeeId}">
		<div class="holiday-modal-overlay" id="holiday-modal-${employeeId}">
			<section class="holiday-modal" role="dialog" aria-modal="true"
				aria-labelledby="holiday-title-${employeeId}">
				<header>
					<h2>사원별 휴가현황</h2>
					<a
						href="${pageContext.request.contextPath}/attendance/leave-inquiry.do"
						aria-label="닫기">&times;</a>
				</header>
				<div class="holiday-modal-body">
					<h3 id="holiday-title-${employeeId}">
						[
						<c:out value="${selectedEmployee.departmentName}" />
						]
						<c:out value="${selectedEmployee.empNameKr}" />
						<c:out value="${selectedEmployee.jobPositionName}" />
						휴가현황
					</h3>
					<table>
						<thead>
							<tr>
								<th>번호</th>
								<th>입력일자</th>
								<th>휴가항목</th>
								<th>근태항목</th>
								<th>기간</th>
								<th>일수</th>
								<th>적요</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="record" items="${leaveRecords}"
								varStatus="status">
								<tr>
									<td><c:out value="${status.count}" /></td>
									<td><c:out value="${record.inputDate}" /></td>
									<td><c:out value="${record.itemName}" /></td>
									<td><c:out value="${record.attendName}" /></td>
									<td><c:out value="${record.startDate}" /> <c:if
											test="${record.startDate ne record.endDate}">
											<i>~</i>
											<c:out value="${record.endDate}" />
										</c:if></td>
									<td class="record-days"><c:out
											value="${record.attendValue}" /></td>
									<td><c:out value="${record.note}" /></td>
								</tr>
							</c:forEach>
							<c:if test="${empty leaveRecords}">
								<tr>
									<td colspan="7" class="empty-row">등록된 휴가 사용내역이 없습니다.</td>
								</tr>
							</c:if>
						</tbody>
						<tfoot>
							<tr>
								<th colspan="2">합계</th>
								<td colspan="5"><span>총 휴가일수 : <strong><c:out
												value="${selectedEmployee.totalDays}" /></strong></span> <span>사용일수
										: <strong class="used-days"><c:out
												value="${selectedEmployee.usedDays}" /></strong>
								</span> <span>잔여일수 : <strong class="remaining-days"><c:out
												value="${selectedEmployee.remainingDays}" /></strong></span></td>
							</tr>
						</tfoot>
					</table>
				</div>
			</section>
		</div>
	</c:if>
	<%@ include file="/WEB-INF/view/common/footer.jspf"%>
</body>
</html>
