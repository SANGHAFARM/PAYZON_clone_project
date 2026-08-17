<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>근태관리 &gt; 일용직 근무조회</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/attendance/day-worker-inquiry.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common/payzon-ui.css">
</head>
<body>
	<%@ include file="/WEB-INF/view/common/header.jspf"%>

	<main class="page-content day-worker-inquiry-page">
		<header class="page-heading">
			<div>
				<p>근태관리</p>
				<h1>일용직 근무조회</h1>
			</div>
		</header>

		<section class="inquiry-card">
			<nav class="view-tabs" aria-label="조회 구분">
				<a
					class="${empty param.view or param.view eq 'month' ? 'is-active' : ''}"
					href="?view=month">월별 조회</a> <a
					class="${param.view eq 'detail' ? 'is-active' : ''}"
					href="?view=detail">상세 조회</a>
			</nav>
			<!-- =================================== -->
			<!-- =========      상세 조회    =========== -->
			<!-- =================================== -->
			<c:choose>
				<c:when test="${param.view eq 'detail'}">
					<div class="detail-layout">
						<form class="detail-filter" method="get">
							<input type="hidden" name="view" value="detail">
							<div class="filter-field">
								<span>근무일자</span>
								<div class="date-range">
									<input type="date" name="startDate" value="${param.startDate}"><i>~</i><input
										type="date" name="endDate" value="${param.endDate}">
								</div>
							</div>
							<label class="filter-field"><span>성명</span><input
								name="employeeName"
								value="<c:out value='${param.empNameKr}' />"
								placeholder="성명을 입력하세요."></label> <label class="filter-field"><span>부서</span>
								<select name="departmentId">
									<option value="">전체 부서</option>
									<c:forEach var="department" items="${departments}">
										<option value="${department.departmentId}"
											${param.departmentId eq department.departmentId ? 'selected' : ''}><c:out
												value="${department.departmentName}" /></option>
									</c:forEach>
								</select></label> <label class="filter-field"><span>현장/프로젝트</span> <select
									name="projectCode">
									<option value="">전체 현장/프로젝트</option>
									<c:forEach var="project" items="${projects}">
										<option value="${project.projectId}"
											${param.projectId eq project.projectId ? 'selected' : ''}><c:out
												value="${project.projectName}" /></option>
									</c:forEach>
								</select></label>
							<div class="filter-actions">
								<button type="submit">검색</button>
								<a href="?view=detail">전체보기</a>
							</div>
						</form>

						<div class="detail-table-wrap">
							<table class="detail-table">
								<thead>
									<tr>
										<th>근무일자</th>
										<th>사원번호</th>
										<th>성명</th>
										<th>부서</th>
										<th>현장/프로젝트</th>
										<th>일당</th>
										<th>지급율</th>
										<th>소득세</th>
										<th>지방소득세</th>
										<th>실지급액</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="record" items="${dailyWorkDetail}">
										<tr>
											<td><c:out value="${record.workDate}" /></td>
											<td><c:out value="${record.empNo}" /></td>
											<td><c:out value="${record.empNameKr}" /></td>
											<td><c:out value="${record.departmentName}" /></td>
											<td><c:out value="${record.projectName}" /></td>
											<td class="amount"><c:out value="${record.dailyPay}" /></td>
											<td><c:out value="${record.payRate}" /></td>
											<td class="amount tax"><c:out
													value="${record.incomeTax}" /></td>
											<td class="amount tax"><c:out
													value="${record.localIncomeTax}" /></td>
											<td class="amount net-pay"><c:out
													value="${record.actualPay}" /></td>
										</tr>
									</c:forEach>
									<c:if test="${empty dailyWorkDetail}">
										<tr>
											<td colspan="12" class="empty-row">조회된 일용직 근무내역이 없습니다.</td>
										</tr>
									</c:if>
								</tbody>
							</table>
						</div>
					</div>
				</c:when>

				<%--=========================== --%>
				<%--========   월별 조회  ======== --%>
				<%--=========================== --%>
				<c:otherwise>
					<form class="month-filter" method="get">
						<input type="hidden" name="view" value="month">
						<select name="year" aria-label="연도">
							<c:forEach var="y" begin="2015" end="2026">
								<option value="${y}"
									${y eq (empty param.year ? 2026 : param.year) ? 'selected' : ''}>${y}년</option>
							</c:forEach>
						</select>
						<select name="month" aria-label="월">
							<c:forEach var="monthNo" begin="1" end="12">
								<option value="${monthNo}"
									${monthNo eq (empty param.month ? 8 : param.month) ? 'selected' : ''}>${monthNo}월</option>
							</c:forEach>
						</select>
						<select name="departmentId" aria-label="부서">
							<option value="">전체 부서</option>
							<c:forEach var="department" items="${departments}">
								<option value="${department.departmentId}"
									${param.departmentId eq department.departmentId ? 'selected' : ''}>
									<c:out value="${department.departmentName}" />
								</option>
							</c:forEach>
						</select>
						<select name="jobPositionId" aria-label="직위">
							<option value="">전체 직위</option>
							<c:forEach var="jobPosition" items="${jobPositions}">
								<option value="${jobPosition.jobPositionId}"
									${param.jobPositionId eq jobPosition.jobPositionId ? 'selected' : ''}>
									<c:out value="${jobPosition.jobPositionName}" />
								</option>
							</c:forEach>
						</select>
						<button type="submit">검색</button>
						<a href="?view=month">전체보기</a>
					</form>

					<!-- 월별 조회 목록 -->
					<div class="month-table-wrap">
						<table class="month-table">
							<thead>
								<tr>
									<th>구분</th>
									<th>사원번호</th>
									<th>성명</th>
									<th>부서</th>
									<th class="days-heading"><span>근무일자</span>
										<div>
											<c:forEach var="dayNo" begin="1" end="31">
												<b>${dayNo}</b>
											</c:forEach>
										</div></th>
									<th>합계</th>
									<th>소득세</th>
									<th>지방소득세</th>
									<th>실지급합계</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="employee" items="${dailyWorkList}">
									<tr>
										<td><c:out value="${employee.empType}" /></td>
										<td><c:out value="${employee.empNo}" /></td>
										<td><c:out value="${employee.empNameKr}" /></td>
										<td><c:out value="${employee.departmentName}" /></td>
										<td class="day-cells"><c:forEach var="dayNo" begin="1"
												end="31">
												<c:set var="work" value="${employee.workDayMap[dayNo]}" />
												<span><c:if test="${not empty work}">
														<a href="#work-detail-${work.dailyWorkRecordId}"
															aria-label="${dayNo}일 근무기록">●</a>
													</c:if></span>
											</c:forEach></td>
										<td class="amount"><c:out value="${employee.totalDays}" /></td>
										<td class="amount tax"><c:out
												value="${employee.totalIncomeTax}" /></td>
										<td class="amount tax"><c:out
												value="${employee.totalLocalIncomeTax}" /></td>
										<td class="amount net-pay"><c:out
												value="${employee.totalActualPay}" /></td>
									</tr>
								</c:forEach>
								<c:if test="${empty dailyWorkList}">
									<tr>
										<td colspan="9" class="empty-row">조회된 일용직 월별 근무내역이 없습니다.</td>
									</tr>
								</c:if>
							</tbody>
						</table>
					</div>
				</c:otherwise>
			</c:choose>
		</section>
	</main>

	<%-- 일용직 근무 현황 --%>
	<c:forEach var="employee" items="${dailyWorkList}">
		<c:if test="${not empty employee.workDayMap}">
			<c:forEach var="entry" items="${employee.workDayMap}">
				<c:set var="work" value="${entry.value}" />
				<div id="work-detail-${work.dailyWorkRecordId}"
					class="modal-overlay">
					<section class="modal work-detail-modal" role="dialog"
						aria-modal="true"
						aria-labelledby="work-title-${work.dailyWorkRecordId}">
						<header>
							<h2 id="work-title-${work.dailyWorkRecordId}">일용직 근무현황</h2>
							<a href="#" aria-label="닫기">&times;</a>
						</header>
						<div class="modal-body">
							<dl>
								<div>
									<dt>근무자</dt>
									<dd style="text-align: right !important; display: block !important;">
										<c:out value="${employee.empNameKr}" />
									</dd>
								</div>
								<div>
									<dt>현장/프로젝트</dt>
									<dd style="text-align: right !important; display: block !important;">
										<c:out value="${work.projectName}" />
									</dd>
								</div>
								<div>
									<dt>근무일자</dt>
									<dd style="text-align: right !important; display: block !important;">
										<c:out value="${work.workDate}" />
									</dd>
								</div>
								<div class="money">
									<dt>일당</dt>
									<dd>
										<c:out value="${work.dailyPay}" />
										원
									</dd>
								</div>
								<div class="money">
									<dt>지급율</dt>
									<dd>
										<c:out value="${work.payRate}" />
									</dd>
								</div>
								<div class="money">
									<dt>소득세</dt>
									<dd>
										<c:out value="${work.incomeTax}" />
										원
									</dd>
								</div>
								<div class="money">
									<dt>지방소득세</dt>
									<dd>
										<c:out value="${work.localIncomeTax}" />
										원
									</dd>
								</div>
								<div class="money total">
									<dt>실지급액</dt>
									<dd>
										<c:out value="${work.actualPay}" />
										원
									</dd>
								</div>
							</dl>
						</div>
					</section>
				</div>
			</c:forEach>
		</c:if>
	</c:forEach>
	<%@ include file="/WEB-INF/view/common/footer.jspf"%>
</body>
</html>
