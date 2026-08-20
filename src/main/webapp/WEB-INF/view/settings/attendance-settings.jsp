<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>기본환경설정 &gt; 휴가/근태 설정</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/settings/attendance-settings.css?v=20260820-3">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common/payzon-ui.css?v=20260820-3">
</head>
<body>
	<%@ include file="/WEB-INF/view/common/header.jspf"%>
	<main class="page-content">
		<div class="attendance-page">
			<header class="page-heading">
				<div>
					<p>기본환경설정</p>
					<h1>휴가/근태 설정</h1>
				</div>
			</header>
			<section class="setting-card" id="leave-settings">
				<div class="card-title">
					<h2>휴가항목 설정</h2>
				</div>
				<div class="setting-layout">
					<div class="list-panel">
						<div class="table-wrap">
							<table class="leave-table">
								<thead>
									<tr>
										<th>휴가항목</th>
										<th>적용기간</th>
										<th>사원별 휴가일수</th>
										<th>사용여부</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="leave" items="${leaveItems}">
										<tr
											class="${leave.leaveItemId eq selectedLeaveItem.leaveItemId ? 'is-selected' : ''}">
											<td><a
												href="${pageContext.request.contextPath}/settings/attendance.do?leaveItemId=${leave.leaveItemId}#leave-settings"><c:out
														value="${leave.itemName}" /></a></td>
											<td><fmt:formatDate value="${leave.applyStartDate}"
													pattern="yyyy/MM/dd" /> ~ <fmt:formatDate
													value="${leave.applyEndDate}" pattern="yyyy/MM/dd" /></td>
											<td><a class="small-button"
												href="${pageContext.request.contextPath}/settings/attendance.do?leaveItemId=${leave.leaveItemId}#employee-leave-modal">관리</a></td>
											<td><span
												class="use-status use-status--${leave.useYn eq 'Y' ? 'on' : 'off'}">${leave.useYn eq 'Y' ? '사용' : '미사용'}</span></td>
										</tr>
									</c:forEach>
									<c:if test="${empty leaveItems}">
										<tr>
											<td colspan="4" class="empty-row">등록된 휴가항목이 없습니다.</td>
										</tr>
									</c:if>
								</tbody>
							</table>
						</div>
					</div>
					<form class="editor-panel"
						action="${pageContext.request.contextPath}/settings/leave-item.do"
						method="post">
						<input type="hidden" name="leaveItemId"
							value="<c:out value='${selectedLeaveItem.leaveItemId}' />">
						<h3>휴가항목 정보</h3>
						<label class="editor-field"><span>휴가항목</span><input
							name="itemName"
							value="<c:out value='${selectedLeaveItem.itemName}' />"
							maxlength="100" placeholder="휴가항목을 입력해주세요" required></label>
						<div class="editor-field editor-field--full">
							<span>적용기간</span>
							<div class="date-range">
								<input type="date" name="applyStartDate"
									value="<fmt:formatDate value='${selectedLeaveItem.applyStartDate}' pattern='yyyy-MM-dd' />"
									required><i>~</i><input type="date" name="applyEndDate"
									value="<fmt:formatDate value='${selectedLeaveItem.applyEndDate}' pattern='yyyy-MM-dd' />"
									required>
							</div>
						</div>
						<div class="editor-field editor-field--full">
							<span>사용여부</span>
							<div class="radio-line">
								<label><input type="radio" name="useYn" value="Y"
									${empty selectedLeaveItem.useYn or selectedLeaveItem.useYn eq 'Y' ? 'checked' : ''}>
									사용</label><label><input type="radio" name="useYn" value="N"
									${selectedLeaveItem.useYn eq 'N' ? 'checked' : ''}>
									사용안함</label>
							</div>
						</div>
						<div class="editor-actions">
							<button name="action" value="insert">추가</button>
							<button name="action" value="update">수정</button>
							<button class="danger" name="action" value="requestDelete" formnovalidate>삭제</button>
							<button class="clear" name="action" value="clear">내용지우기</button>
						</div>
					</form>
				</div>
			</section>

			<section class="setting-card" id="attendance-item-settings">
				<div class="card-title">
					<h2>근태항목 설정</h2>
				</div>
				<div class="setting-layout">
					<div class="list-panel">
						<div class="table-wrap">
							<table class="attendance-table">
								<thead>
									<tr>
										<th>근태항목</th>
										<th>단위</th>
										<th>그룹관리</th>
										<th>휴가공제</th>
										<th>사용여부</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="item" items="${attendItems}">
										<tr
											class="${item.attendanceItemId eq selectedAttendItem.attendanceItemId ? 'is-selected' : ''}">
											<td><a
												href="${pageContext.request.contextPath}/settings/attendance.do?attendItemId=${item.attendanceItemId}#attendance-item-settings"><c:out
														value="${item.attendName}" /></a></td>
											<td><c:out value="${item.unitType}" /></td>
											<td><c:out value="${item.groupName}" /></td>
											<td><c:out value="${item.leaveName}" /></td>
											<td><span
												class="use-status use-status--${item.useYn eq 'Y' ? 'on' : 'off'}">${item.useYn eq 'Y' ? '사용' : '미사용'}</span></td>
										</tr>
									</c:forEach>
									<c:if test="${empty attendItems}">
										<tr>
											<td colspan="5" class="empty-row">등록된 근태항목이 없습니다.</td>
										</tr>
									</c:if>
								</tbody>
							</table>
						</div>
					</div>
					<form class="editor-panel"
						action="${pageContext.request.contextPath}/settings/attend-item.do"
						method="post">
						<input type="hidden" name="attendItemId"
							value="<c:out value='${selectedAttendItem.attendanceItemId}' />">
						<h3>근태항목 정보</h3>
						<label class="editor-field"><span>근태항목</span><input
							name="attendName"
							value="<c:out value='${selectedAttendItem.attendName}' />"
							maxlength="100" placeholder="근태항목을 입력해주세요" required></label> <label
							class="editor-field"><span>단위</span><select
							name="unitType"><option value="">선택하세요.</option>
								<option value="일"
									${selectedAttendItem.unitType eq '일' ? 'selected' : ''}>일</option>
								<option value="시간"
									${selectedAttendItem.unitType eq '시간' ? 'selected' : ''}>시간</option></select></label>
						<div class="editor-field editor-field--group">
							<span>근태그룹</span><select name="attendanceGroupId" required><option
									value="">선택하세요.</option>
								<c:forEach var="group" items="${attendGroups}">
									<option value="${group.attendanceGroupId}"
										${group.attendanceGroupId eq selectedAttendItem.attendanceGroupId ? 'selected' : ''}><c:out
											value="${group.groupName}" /></option>
								</c:forEach></select><a href="#attend-group-modal">그룹관리</a>
						</div>
						<label class="editor-field"><span>휴가공제</span><select
							name="deductLeaveId"><option value="">선택하세요.</option>
								<c:forEach var="leave" items="${leaveItems}">
									<option value="${leave.leaveItemId}"
										${leave.leaveItemId eq selectedAttendItem.deductLeaveId ? 'selected' : ''}><c:out
											value="${leave.itemName}" /></option>
								</c:forEach></select></label> <label class="editor-field"><span>근로시간연계</span><select
							name="workHourType"><option value="">선택하세요.</option>
								<option value="소정근로"
									${selectedAttendItem.workHourType eq '소정근로' ? 'selected' : ''}>소정근로</option>
								<option value="연장근로"
									${selectedAttendItem.workHourType eq '연장근로' ? 'selected' : ''}>연장근로</option>
								<option value="야간근로"
									${selectedAttendItem.workHourType eq '야간근로' ? 'selected' : ''}>야간근로</option>
								<option value="휴일근로"
									${selectedAttendItem.workHourType eq '휴일근로' ? 'selected' : ''}>휴일근로</option></select></label>
						<div class="editor-field editor-field--full">
							<span>사용여부</span>
							<div class="radio-line">
								<label><input type="radio" name="useYn" value="Y"
									${empty selectedAttendItem.useYn or selectedAttendItem.useYn eq 'Y' ? 'checked' : ''}>
									사용</label><label><input type="radio" name="useYn" value="N"
									${selectedAttendItem.useYn eq 'N' ? 'checked' : ''}>
									사용안함</label>
							</div>
						</div>
						<div class="editor-actions">
							<button name="action" value="insert">추가</button>
							<button name="action" value="update">수정</button>
							<button class="danger" name="action" value="requestDelete" formnovalidate>삭제</button>
							<button class="clear" name="action" value="clear">내용지우기</button>
						</div>
					</form>
				</div>
			</section>

			<div id="attend-group-modal" class="group-modal" role="dialog"
				aria-modal="true" aria-labelledby="group-modal-title">
				<a class="group-modal__backdrop" href="#attendance-item-settings"
					aria-label="닫기"></a>
				<div class="group-modal__panel">
					<div class="group-modal__title">
						<h2 id="group-modal-title">근태그룹 관리</h2>
						<a href="#attendance-item-settings" aria-label="닫기">×</a>
					</div>
					<form
						action="${pageContext.request.contextPath}/settings/attend-group.do"
						method="post">
						<ul class="group-list">
							<c:forEach var="group" items="${attendGroups}">
								<li><input
									name="groupNames" value="<c:out value='${group.groupName}' />"
									aria-label="근태그룹명" maxlength="100" required><input type="hidden" name="groupIds"
									value="${group.attendanceGroupId}">
									<div>
										<button name="action"
											value="update:${group.attendanceGroupId}">수정</button>
										<button name="action" formnovalidate
											value="requestDelete:${group.attendanceGroupId}">삭제</button>
									</div></li>
							</c:forEach>
							<c:if test="${empty attendGroups}">
								<li class="group-list__empty">등록된 근태그룹이 없습니다.</li>
							</c:if>
						</ul>
						<div class="group-add">
							<input name="newGroupName" placeholder="새 근태그룹명" maxlength="100" required>
							<button name="action" value="insert">＋ 추가하기</button>
						</div>
					</form>
				</div>
			</div>

			<div id="employee-leave-modal" class="employee-leave-modal"
				role="dialog" aria-modal="true"
				aria-labelledby="employee-leave-title">
				<a class="employee-leave-modal__backdrop" href="#leave-settings"
					aria-label="닫기"></a>
				<div class="employee-leave-modal__panel">
					<div class="employee-leave-modal__title">
						<h2 id="employee-leave-title">휴가일수 설정</h2>
						<a href="#leave-settings" aria-label="닫기">×</a>
					</div>
					<form
						action="${pageContext.request.contextPath}/settings/employee-leave.do"
						method="post">
						<input type="hidden" name="leaveItemId"
							value="${selectedLeaveItem.leaveItemId}">
						<div class="employee-leave-tools">
							<div>
								<input name="keyword" placeholder="사원검색"
									value="<c:out value='${param.keyword}' />">
								<button name="action" value="search">검색</button>
								<button name="action" value="showAll">전체보기</button>
							</div>

							<select name="status">
								<option value="">상태별</option>
								<option value="재직" ${param.status eq '재직' ? 'selected' : ''}>재직</option>
								<option value="퇴직" ${param.status eq '퇴직' ? 'selected' : ''}>퇴직</option>
							</select>
						</div>
						<div class="employee-leave-table-wrap">
							<table class="employee-leave-table">
								<thead>
									<tr>
										<th>선택</th>
										<th>구분</th>
										<th>사원번호</th>
										<th>성명</th>
										<th>부서</th>
										<th>직위</th>
										<th>입사일</th>
										<th>휴가일수</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="row" items="${employeeLeaveRows}">
										<tr>
											<td><input type="checkbox" name="checkedEmpIds"
												value="${row.employeeId}"></td>

											<td><c:out value="${row.empType}" /></td>
											<td><c:out value="${row.empNo}" /></td>
											<td><c:out value="${row.empName}" /></td>
											<td><c:out value="${row.deptName}" /></td>
											<td><c:out value="${row.posName}" /></td>
											<td><c:out value="${row.joinDate}" /></td>

											<td><label> <input type="hidden"
													name="empLeaveId_${row.employeeId}"
													value="${row.empLeaveId}"> <input type="number"
													min="0" step="0.5" name="leaveDays_${row.employeeId}"
													value="${row.leaveDays}"> <span>일</span>
											</label></td>
										</tr>
									</c:forEach>
									<c:if test="${empty employeeLeaveRows}">
										<tr>
											<td colspan="8" class="empty-row">조회된 사원이 없습니다.</td>
										</tr>
									</c:if>
								</tbody>
							</table>
						</div>
						<div class="employee-leave-actions">
							<div>
								<button class="delete" name="action" value="requestDelete" formnovalidate>휴가일수
									삭제</button>
								<button name="action" value="save">휴가일수 저장</button>
							</div>
							<div>
								<a href="#annual-leave-info-modal">연차휴가 계산방법</a>
							</div>
						</div>
					</form>
				</div>
			</div>

			<c:if test="${not empty deleteSettingType}">
				<div class="attendance-delete-modal" role="alertdialog" aria-modal="true"
					aria-labelledby="attendance-delete-title">
					<a class="attendance-modal__backdrop"
						href="${pageContext.request.contextPath}/settings/attendance.do${deleteReturnHash}"
						aria-label="삭제 취소"></a>
					<form class="attendance-delete-modal__panel" method="post"
						action="${pageContext.request.contextPath}${deleteActionUrl}">
						<p id="attendance-delete-title"><strong><c:out value="${deleteSettingName}" /></strong> 항목을 삭제하시겠습니까?</p>
						<p class="attendance-delete-warning">삭제한 항목은 복구할 수 없습니다.</p>
						<input type="hidden" name="action" value="confirmDelete">
						<input type="hidden" name="deleteId" value="${deleteSettingId}">
						<c:if test="${deleteSettingType eq 'leave'}"><input type="hidden" name="leaveItemId" value="${deleteSettingId}"></c:if>
						<c:if test="${deleteSettingType eq 'attendance'}"><input type="hidden" name="attendItemId" value="${deleteSettingId}"></c:if>
						<div><button type="submit">삭제</button><a href="${pageContext.request.contextPath}/settings/attendance.do${deleteReturnHash}">취소</a></div>
					</form>
				</div>
			</c:if>

			<c:if test="${not empty employeeLeaveDeleteCount}">
				<div class="attendance-delete-modal" role="alertdialog" aria-modal="true"
					aria-labelledby="employee-leave-delete-title">
					<a class="attendance-modal__backdrop" href="${pageContext.request.contextPath}/settings/attendance.do?cancelEmployeeLeaveDelete=true&amp;leaveItemId=${selectedLeaveItem.leaveItemId}#employee-leave-modal" aria-label="삭제 취소"></a>
					<form class="attendance-delete-modal__panel" method="post"
						action="${pageContext.request.contextPath}/settings/employee-leave.do">
						<p id="employee-leave-delete-title">선택한 <strong><c:out value="${employeeLeaveDeleteCount}" />명</strong>의 휴가일수를 삭제하시겠습니까?</p>
						<p class="attendance-delete-warning">삭제한 휴가일수는 복구할 수 없습니다.</p>
						<input type="hidden" name="action" value="confirmDelete">
						<input type="hidden" name="leaveItemId" value="${selectedLeaveItem.leaveItemId}">
						<div><button type="submit">삭제</button><a href="${pageContext.request.contextPath}/settings/attendance.do?cancelEmployeeLeaveDelete=true&amp;leaveItemId=${selectedLeaveItem.leaveItemId}#employee-leave-modal">취소</a></div>
					</form>
				</div>
			</c:if>

			<c:if test="${not empty message}">
				<c:set var="messageHash" value="${messageReturnTarget eq 'group' ? '#attend-group-modal' : messageReturnTarget eq 'employeeLeave' ? '#employee-leave-modal' : ''}" />
				<div class="attendance-alert" role="alertdialog" aria-modal="true"
					aria-labelledby="attendance-alert-message">
					<a class="attendance-modal__backdrop"
						href="${pageContext.request.contextPath}/settings/attendance.do?dismissMessage=true${messageHash}"
						aria-label="확인"></a>
					<div class="attendance-alert__panel">
						<p id="attendance-alert-message"><c:out value="${message}" /></p>
						<a href="${pageContext.request.contextPath}/settings/attendance.do?dismissMessage=true${messageHash}">확인</a>
					</div>
				</div>
				<c:remove var="message" scope="session" />
				<c:remove var="messageReturnTarget" scope="session" />
			</c:if>
		</div>
		<!-- ▼▼ 연차휴가 계산방법 모달 (순수 CSS 방식) ▼▼ -->
		<div id="annual-leave-info-modal" class="info-modal" role="dialog"
			aria-modal="true" aria-labelledby="info-modal-title">
			<!-- 💡 닫기를 누르면 이전 창(사원 휴가일수 설정 창)으로 깔끔하게 돌아갑니다! -->
			<a class="info-modal__backdrop" href="#employee-leave-modal"
				aria-label="닫기"></a>
			<div class="info-modal__panel">
				<div class="info-modal__title">
					<h2 id="info-modal-title">연차휴가 계산법</h2>
					<a href="#employee-leave-modal" aria-label="닫기">×</a>
				</div>
				<div class="info-modal__body">

					<!-- 주 40시간제 -->
					<div class="leave-calc-section">
						<h3>주 40시간제 적용 사업장</h3>
						<p class="calc-desc">연차휴가는 1년에 15일을 기본으로 하며, 3년 이상 계속 근로 시 최초
							1년을 초과하는 계속 근로연수 2년에 대하여 1일의 가산연차 휴가 발생(최대 25일)</p>
						<ul class="calc-list">
							<li><strong>1) 입사 1년 미만자인 경우</strong>
								<p>연차휴가 = 기준일(15) * (근무일수/365) = 소수 첫 자리 까지 (소수 두번째 자리에서
									반올림)</p>
								<div class="calc-example">
									예) 2023년 2월 14일 ~ 2023년 12월 31일 만근 시 : 근무일수는 322일<br> 사용기간
									: 2024년 1월 1일 ~ 2024년 12월 31일<br> 2024년 1월 1일 연차계산 시 :
									13.2일 (15 * (322/365))
								</div></li>
							<li><strong>2) 입사 1년 이상자인 경우</strong>
								<p>
									- 근무연도 > 2 (3년 이상인 경우)<br> 연차휴가 = 기준일(15) + (근무 연도/2) (소수점
									버림 계산)
								</p>
								<div class="calc-example">예) 2020년 2월 14일 입사한 직원의 2024년 1월
									1일 연차계산 시 : 16일 (15 + (2/2))</div>
								<p class="mt-2">
									- 근무연도 &lt; 3 (3년 미만인 경우)<br> 연차휴가 = 기준일(15)
								</p></li>
						</ul>
					</div>

					<!-- 주 44시간제 -->
					<div class="leave-calc-section">
						<h3>주 44시간제 적용 사업장</h3>
						<p class="calc-desc">연차휴가는 1년에 10일을 기본으로 하며, 2년 이상 계속 근로 시 최초
							1년을 초과하는 계속 근로연수 2년에 대하여 1일의 가산연차 휴가 발생</p>
						<ul class="calc-list">
							<li><strong>1) 입사 1년 미만자인 경우</strong>
								<p>연차휴가 = 기준일(10) * (근무일수/365) = 소수 첫 자리 까지 (소수 두번째 자리에서
									반올림)</p>
								<div class="calc-example">
									예) 2023년 2월 14일 ~ 2023년 12월 31일 만근 시 : 근무일수는 322일<br> 사용기간
									: 2024년 1월 1일 ~ 2024년 12월 31일<br> 2024년 1월 1일 연차계산 시 :
									8.8일 (10 * (322/365))
								</div></li>
							<li><strong>2) 입사 1년 이상자인 경우</strong>
								<p>
									- 근무연도 > 2 (3년 이상인 경우)<br> 연차휴가 = 기준일(10) + (근무 연도)
								</p>
								<div class="calc-example">예) 2020년 2월 14일 입사한 직원의 2024년 1월
									1일 연차계산 시 : 12일 (10 + 2)</div>
								<p class="mt-2">
									- 근무연도 &lt; 3 (3년 미만인 경우)<br> 연차휴가 = 기준일(10)
								</p></li>
						</ul>
					</div>

				</div>
			</div>
		</div>
		<!-- ▲▲ 연차휴가 계산방법 모달 끝 ▲▲ -->
	</main>
	<%@ include file="/WEB-INF/view/common/footer.jspf"%>
</body>
</html>
