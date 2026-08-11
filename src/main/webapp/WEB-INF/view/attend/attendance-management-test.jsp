<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>근태관리 &gt; 근태기록/관리</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/attend/attendance-management.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common/payzon-ui.css">
</head>
<body>
	<%@ include file="/WEB-INF/view/common/header.jspf"%>

	<main class="page-content attendance-page">
		<header class="page-heading">
			<div>
				<p>근태관리</p>
				<h1>근태기록/관리</h1>
			</div>
		</header>
		<c:if test="${not empty message}">
			<p class="form-message">
				<c:out value="${message}" />
			</p>
		</c:if>

		<section class="attendance-card">
			<div class="employee-toolbar">
				<form action="${pageContext.request.contextPath}/attend/manage.do"
					method="get" class="employee-search">
					<input type="search" name="keyword" value="${param.keyword}"
						placeholder="검색어 입력">
					<button type="submit">검색</button>
					<a href="${pageContext.request.contextPath}/attend/manage.do">전체보기</a>
				</form>
				<form action="${pageContext.request.contextPath}/attend/manage.do"
					method="get">
					<select name="status" aria-label="사원 상태">
						<option value="">상태별</option>
						<option value="재직"
							${((param.status eq '재직')or (empty param.status)) ? 'selected' : ''}>재직</option>
						<option value="퇴직" ${param.status eq '퇴직' ? 'selected' : ''}>퇴직</option>
					</select>
					<button type="submit" class="status-search">조회</button>
				</form>
			</div>

			<div class="attendance-layout">
				<section class="employee-list-panel">
					<table class="employee-table">
						<thead>
							<tr>
								<th>선택</th>
								<th>구분</th>
								<th>사원번호</th>
								<th>성명</th>
								<th>부서</th>
								<th>직위</th>
								<th>근태기록</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="employee" items="${employees}">
								<tr>
									<td><input type="checkbox" name="empIds"
										value="${employee.empId}" form="attendance-form"></td>
									<td><c:out value="${employee.empType}" /></td>
									<td><c:out value="${employee.empNo}" /></td>
									<td><c:out value="${employee.empNameKr}" /></td>
									<td><c:out value="${employee.deptName}" /></td>
									<td><c:out value="${employee.posName}" /></td>
									<td><a class="manage-button"
										href="${pageContext.request.contextPath}/attend/manage.do?empId=${employee.empId}&recordYear=2026#attendance-record-modal">관리</a></td>
								</tr>
							</c:forEach>
							<c:if test="${empty employees}">
								<tr>
									<td colspan="7" class="empty-row">조회된 사원이 없습니다.</td>
								</tr>
							</c:if>
						</tbody>
					</table>
				</section>

				<section class="attendance-editor">
					<h2>근태기록 입력</h2>
					<form id="attendance-form"
						action="${pageContext.request.contextPath}/attendance/save.do"
						method="post">
						<label><span>입력일자</span><input type="date"
							name="inputDate" value="${today}"></label> <label><span>근태항목</span><select
							name="attendItemId"><option value="">선택하세요.</option>
								<c:forEach var="item" items="${attendanceItems}">
									<option value="${item.attendItemId}"><c:out
											value="${item.attendName}" /></option>
								</c:forEach></select></label> <label class="period-field"><span>기간</span><span
							class="period-inputs"><input type="date" name="startDate"><i>~</i><input
								type="date" name="endDate"></span></label> <label><span>근태일수</span><span
							class="days-field"><input type="number" name="attendValue"
								min="0" step="0.5"><em>일</em><a
								href="#holiday-status-modal">휴가일수 현황</a></span></label> <label><span>금액(수당)</span><input
							type="number" name="payAmount" min="0"
							placeholder="근태분류가 지급수당인 경우 입력"></label> <label><span>적요</span><input
							type="text" name="note" placeholder="적요가 있는 경우 입력해주세요."></label>
						<div class="editor-actions">
							<button type="submit">저장</button>
							<button type="reset" class="secondary">내용지우기</button>
						</div>
					</form>
				</section>
			</div>
		</section>
	</main>

	<div class="modal-overlay" id="attendance-record-modal">
		<section class="modal modal--record" role="dialog" aria-modal="true"
			aria-labelledby="record-title">
			<header>
				<h2 id="record-title">사원별 근태기록</h2>
				<a href="#" aria-label="닫기">&times;</a>
			</header>
			<div class="modal-body">
				<div class="record-summary">
					<span>성명 : <c:out value="${selectedEmployee.empNameKr}" /></span><span>부서
						: <c:out value="${selectedEmployee.deptName}" />
					</span><span>직위 : <c:out value="${selectedEmployee.posName}" /></span>
					<form
						action="${pageContext.request.contextPath}/attend/manage.do#attendance-record-modal"
						method="GET">
						<input type="hidden" name="empId"
							value="${selectedEmployee.empId}"> <select
							name="recordYear" aria-label="연도">
							<option value="" ${empty recordYear ? 'selected' : ''}>연도</option>
							<c:forEach var="y" begin="2000" end="2026">
								<option value="${y}" ${y == recordYear ? 'selected' : ''}><c:out
										value="${y}" />
								</option>
							</c:forEach>
						</select> <select name="recordMonth" aria-label="월">
							<option value="" ${empty recordMonth?'selected':'' }>전체</option>
							<c:forEach var="m" begin="1" end="12">
								<option value="${m}" ${m == recordMonth ? 'selected' : ''}><c:out
										value="${m}" />
								</option>
							</c:forEach>
						</select>
						<button type="submit">조회</button>
					</form>
				</div>
				<table>
					<thead>
						<tr>
							<th>번호</th>
							<th>입력일자</th>
							<th>근태항목</th>
							<th>근태기간</th>
							<th>근태일수</th>
							<th>금액</th>
							<th>적요</th>
							<th>수정/삭제</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="record" items="${attendanceRecords}">
							<tr>
								<td><c:out value="${record.rowNumber}" /></td>
								<td><c:out value="${record.inputDate}" /></td>
								<td><c:out value="${record.itemName}" /></td>
								<td><c:out value="${record.period}" /></td>
								<td><c:out value="${record.days}" /></td>
								<td><c:out value="${record.allowance}" /></td>
								<td><c:out value="${record.note}" /></td>
								<td><span class="record-buttons"><a
										href="${pageContext.request.contextPath}/attend/manage.do?editId=${record.recordId}"
										class="edit-button">수정</a><a
										href="${pageContext.request.contextPath}/attendance/delete.do?recordId=${record.recordId}"
										class="delete-button">삭제</a></span></td>
							</tr>
						</c:forEach>
						<c:if test="${empty attendanceRecords}">
							<tr>
								<td colspan="8" class="empty-row">등록된 근태기록이 없습니다.</td>
							</tr>
						</c:if>
					</tbody>
				</table>
			</div>
		</section>
	</div>

	<div class="modal-overlay" id="holiday-status-modal">
		<section class="modal modal--holiday" role="dialog" aria-modal="true"
			aria-labelledby="holiday-title">
			<header>
				<h2 id="holiday-title">휴가일수 현황</h2>
				<a href="#" aria-label="닫기">&times;</a>
			</header>
			<div class="modal-body">
				<table>
					<thead>
						<tr>
							<th>구분</th>
							<th>성명</th>
							<th>직위</th>
							<th>휴가항목</th>
							<th>전체</th>
							<th>사용</th>
							<th>잔여</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="holiday" items="${holidayStatuses}">
							<tr>
								<td><c:out value="${holiday.employmentType}" /></td>
								<td><c:out value="${holiday.employeeName}" /></td>
								<td><c:out value="${holiday.positionName}" /></td>
								<td><c:out value="${holiday.holidayName}" /></td>
								<td><c:out value="${holiday.totalDays}" /></td>
								<td class="used-days"><c:out value="${holiday.usedDays}" /></td>
								<td class="remaining-days"><c:out
										value="${holiday.remainingDays}" /></td>
							</tr>
						</c:forEach>
						<c:if test="${empty holidayStatuses}">
							<tr>
								<td colspan="7" class="empty-row">조회된 휴가일수 현황이 없습니다.</td>
							</tr>
						</c:if>
					</tbody>
				</table>
			</div>
		</section>
	</div>

	<%@ include file="/WEB-INF/view/common/footer.jspf"%>
</body>
</html>
