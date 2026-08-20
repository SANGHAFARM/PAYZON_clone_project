<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="viewMode"
	value="${empty param.viewMode ? 'MONTH' : param.viewMode}" />
<c:set var="currentPageUrl" value="${pageContext.request.requestURI}" />
<c:set var="monthDayCount" value="31" />
<c:if test="${not empty daysInMonth}">
	<c:set var="monthDayCount" value="${daysInMonth}" />
</c:if>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>근태관리 &gt; 근태조회</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/attendance/attendance-inquiry.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common/payzon-ui.css">
</head>
<body>
	<%@ include file="/WEB-INF/view/common/header.jspf"%>
	<main class="page-content attendance-inquiry-page">
		<header class="page-heading">
			<div>
				<p>근태관리</p>
				<h1>근태조회</h1>
			</div>
		</header>
		<section class="inquiry-card">
			<nav class="inquiry-tabs">
				<a class="${viewMode eq 'MONTH' ? 'is-active' : ''}"
					href="${pageContext.request.contextPath}/attendance/attendance-inquiry.do?viewMode=MONTH">월별 조회</a> <a
					class="${viewMode eq 'DETAIL' ? 'is-active' : ''}"
					href="${${pageContext.request.contextPath}/attendance/attendance-inquiry.do?viewMode=DETAIL">상세 조회</a>
			</nav>

			<c:choose>
				<c:when test="${viewMode eq 'MONTH'}">
					<form class="month-search"
						action="${pageContext.request.contextPath}/attendance/attendance-inquiry.do"
						method="get">
						<input type="hidden" name="viewMode" value="MONTH"> <select
							name="year" aria-label="조회 연도">
							<option value="">연도 선택</option>
							<%-- items를 지우고 begin, end만 쓰면 2015부터 2030까지 숫자가 차례대로 들어갑니다 --%>
							<c:forEach var="y" begin="2015" end="2030">
								<option value="${y}" ${year eq y ? 'selected' : ''}>
									<c:out value="${y}" />년
								</option>
							</c:forEach>
						</select><select name="month" aria-label="조회 월">
							<c:forEach var="m" begin="1" end="12">
								<option value="${m}" ${month eq m ? 'selected' : ''}>
									<c:out value="${m}" />월
								</option>
							</c:forEach>
						</select> <select name="status" aria-label="상태별"><option value="">상태별</option>
							<option value="재직" ${status eq '재직'?'selected' :'' }>재직</option>
							<option value="퇴직" ${status eq '퇴직'?'selected':'' }>퇴직</option></select> <select
							name="empType" aria-label="구분별"><option value="">구분별</option>
							<c:forEach var="type" items="${empTypes}">
								<option value="${type}" ${type eq empType ? 'selected' : '' }><c:out
										value="${type}" /></option>
							</c:forEach></select> <select name="departmentId" aria-label="부서별"><option
								value="">부서별</option>
							<c:forEach var="department" items="${departments}">
								<option value="${department.departmentId}"
									${department.departmentId eq departmentId ?'selected':'' }><c:out
										value="${department.departmentName}" /></option>
							</c:forEach></select> <select name="jobPositionId" aria-label="직위별"><option
								value="">직위별</option>
							<c:forEach var="jobPosition" items="${jobPositions}">
								<option value="${jobPosition.jobPositionId}"
									${jobPosition.jobPositionId eq jobPositionId ? 'selected':'' }><c:out
										value="${jobPosition.jobPositionName}" /></option>
							</c:forEach></select>
						<button type="submit">조회</button>
					</form>

					<div class="monthly-table-wrap">
						<table class="monthly-table">
							<thead>
								<tr>
									<th>구분</th>
									<th>사원번호</th>
									<th>성명</th>
									<th>부서</th>
									<th>직위</th>
									<c:forEach var="day" begin="1" end="${monthDayCount}">
										<th class="day-heading"><c:out value="${day}" /></th>
									</c:forEach>
									<th class="total-heading">합계</th>
									<th>휴가공제</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="employee" items="${monthlyEmployees}">
									<%-- 									<c:set var="detailUrl"
										value="${currentPageUrl}?viewMode=DETAIL&amp;employeeId=${employee.employeeId}&amp;startDate=${searchStartDate}&amp;endDate=${searchEndDate}" /> --%>
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
										<c:forEach var="day" begin="1" end="${monthDayCount}">
											<td class="day-cell"><a href="${detailUrl}"><c:if
														test="${not empty employee.dailyAttendance[day]}">
														<span class="attendance-dot"
															title="${employee.dailyAttendance[day]}"></span>
													</c:if></a></td>
										</c:forEach>
										<td class="summary-cell"><a href="${detailUrl}"><ul
													class="summary-list">
													<c:forEach var="entry" items="${employee.totalAttendValue}">
														<li><c:out value="${entry.key}: ${entry.value}" /> <c:choose>
																<c:when
																	test="${entry.key eq '연차' or entry.key eq '반차' or entry.key eq '포상휴가' or entry.key eq '청원휴가'}">
                (d)
            </c:when>
																<c:otherwise>
                (h)
            </c:otherwise>
															</c:choose></li>
													</c:forEach>
													<%-- 													<c:if
														test="${empty employee.attendanceSummaryItems and not empty employee.attendanceSummary}">
														<li><c:out value="${employee.attendanceSummary}" /></li>
													</c:if> --%>
												</ul></a></td>
										<td><a href="${detailUrl}"><c:out
													value="${employee.totalLeaveDeduction}" /></a></td>
									</tr>
								</c:forEach>
								<c:if test="${empty monthlyEmployees}">
									<tr>
										<td colspan="${monthDayCount + 7}" class="empty-row">조회된
											월별 근태내역이 없습니다.</td>
									</tr>
								</c:if>
							</tbody>
						</table>
					</div>
				</c:when>
				
				<c:otherwise>
					<div class="detail-layout">
						<form class="detail-search" action="${currentPageUrl}"
							method="get">
							<input type="hidden" name="viewMode" value="DETAIL"><label><input
								type="checkbox" name="useInputDate" value="Y"><span>입력일자</span><input
								type="date" name="inputDate" value="${param.inputDate}"></label><label><input
								type="checkbox" name="usePeriod" value="Y"><span>근태기간</span><span
								class="detail-period"><input type="date" name="startDate"
									value="${param.startDate}"><i>~</i><input type="date"
									name="endDate" value="${param.endDate}"></span></label><label><input
								type="checkbox" name="useDepartment" value="Y"><span>부서</span><select
								name="departmentId"><option value="">선택하세요.</option>
									<c:forEach var="department" items="${departments}">
										<option value="${department.departmentId}"><c:out
												value="${department.departmentName}" /></option>
									</c:forEach></select></label><label><input type="checkbox" name="useName" value="Y"><span>성명</span><input
								type="search" name="empNameKr" value="${param.empNameKr}"
								placeholder="성명을 입력하세요."></label><label><input
								type="checkbox" name="useGroup" value="Y"><span>근태그룹</span><select
								name="attendanceGroupId"><option value="">선택하세요.</option>
									<c:forEach var="group" items="${attendanceGroups}">
										<option value="${group.groupId}"><c:out
												value="${group.groupName}" /></option>
									</c:forEach></select></label><label><input type="checkbox" name="useItem" value="Y"><span>근태항목</span><select
								name="attendanceItemId"><option value="">선택하세요.</option>
									<c:forEach var="item" items="${attendanceItems}">
										<option value="${item.itemId}"><c:out
												value="${item.itemName}" /></option>
									</c:forEach></select></label><label><input type="checkbox" name="useHoliday"
								value="Y"><span>휴가항목</span><select name="holidayItemId"><option
										value="">선택하세요.</option>
									<c:forEach var="holiday" items="${holidayItems}">
										<option value="${holiday.itemId}"><c:out
												value="${holiday.itemName}" /></option>
									</c:forEach></select></label><label><input type="checkbox" name="useNote" value="Y"><span>적요</span><input
								type="text" name="note" value="${param.note}"></label>
							<div class="detail-actions">
								<button type="submit">검색</button>
								<a href="${currentPageUrl}?viewMode=DETAIL">전체보기</a>
							</div>
						</form>
						<div class="detail-table-wrap">
							<table class="detail-table">
								<thead>
									<tr>
										<th>입력일자</th>
										<th>구분</th>
										<th>성명</th>
										<th>부서</th>
										<th>직위</th>
										<th>근태항목</th>
										<th>근태기간</th>
										<th>근태일수</th>
										<th>금액</th>
										<th>적요</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="record" items="${attendanceRecords}">
										<tr>
											<td><c:out value="${record.inputDate}" /></td>
											<td><c:out value="${record.empType}" /></td>
											<td><c:out value="${record.empNameKr}" /></td>
											<td><c:out value="${record.departmentName}" /></td>
											<td><c:out value="${record.jobPositionName}" /></td>
											<td><c:out value="${record.itemName}" /></td>
											<td><c:out value="${record.period}" /></td>
											<td><c:out value="${record.amount}" /> <c:out
													value="${record.unit}" /></td>
											<td><c:out value="${record.allowance}" /></td>
											<td><c:out value="${record.note}" /></td>
										</tr>
									</c:forEach>
									<c:if test="${empty attendanceRecords}">
										<tr>
											<td colspan="10" class="empty-row">조회된 상세 근태내역이 없습니다.</td>
										</tr>
									</c:if>
								</tbody>
							</table>
						</div>
					</div>
				</c:otherwise>
			</c:choose>
		</section>
	</main>
	<%@ include file="/WEB-INF/view/common/footer.jspf"%>
</body>
</html>
