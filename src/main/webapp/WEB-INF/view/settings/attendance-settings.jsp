<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>기본환경설정 &gt; 휴가/근태 설정</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/settings/attendance-settings.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/payzon-ui.css">
</head>
<body>
	<%@ include file="/WEB-INF/view/common/header.jspf" %>
	<main class="page-content">
		<div class="attendance-page">
			<header class="page-heading"><div><p>기본환경설정</p><h1>휴가/근태 설정</h1></div></header>
			<c:if test="${not empty message}"><p class="form-message" role="status"><c:out value="${message}" /></p></c:if>

			<section class="setting-card" id="leave-settings">
				<div class="card-title"><h2>휴가항목 설정</h2></div>
				<div class="setting-layout">
					<div class="list-panel">
						<div class="table-wrap"><table class="leave-table"><thead><tr><th>휴가항목</th><th>적용기간</th><th>사원별 휴가일수</th><th>사용여부</th></tr></thead><tbody>
							<c:forEach var="leave" items="${leaveItems}"><tr class="${leave.leaveItemId eq selectedLeaveItem.leaveItemId ? 'is-selected' : ''}"><td><a href="${pageContext.request.contextPath}/settings/attendance.do?leaveItemId=${leave.leaveItemId}#leave-settings"><c:out value="${leave.leaveName}" /></a></td><td><c:out value="${leave.startDate}" /> ~ <c:out value="${leave.endDate}" /></td><td><a class="small-button" href="#employee-leave-modal">관리</a></td><td><span class="use-status use-status--${leave.useYn eq 'Y' ? 'on' : 'off'}">${leave.useYn eq 'Y' ? '사용' : '미사용'}</span></td></tr></c:forEach>
							<c:if test="${empty leaveItems}"><tr><td colspan="4" class="empty-row">등록된 휴가항목이 없습니다.</td></tr></c:if>
						</tbody></table></div>
					</div>
					<form class="editor-panel" action="${pageContext.request.contextPath}/settings/leave-item.do" method="post">
						<input type="hidden" name="leaveItemId" value="<c:out value='${selectedLeaveItem.leaveItemId}' />">
						<h3>휴가항목 정보</h3>
						<label class="editor-field"><span>휴가항목</span><input name="leaveName" value="<c:out value='${selectedLeaveItem.leaveName}' />" maxlength="100" placeholder="휴가항목을 입력해주세요"></label>
						<div class="editor-field editor-field--full"><span>적용기간</span><div class="date-range"><input type="date" name="startDate" value="${selectedLeaveItem.startDate}"><i>~</i><input type="date" name="endDate" value="${selectedLeaveItem.endDate}"></div></div>
						<div class="editor-field editor-field--full"><span>사용여부</span><div class="radio-line"><label><input type="radio" name="useYn" value="Y" ${empty selectedLeaveItem.useYn or selectedLeaveItem.useYn eq 'Y' ? 'checked' : ''}> 사용</label><label><input type="radio" name="useYn" value="N" ${selectedLeaveItem.useYn eq 'N' ? 'checked' : ''}> 사용안함</label></div></div>
						<div class="editor-actions"><button name="action" value="insert">추가</button><button name="action" value="update">수정</button><button class="danger" name="action" value="delete">삭제</button><button class="clear" name="action" value="clear">내용지우기</button></div>
					</form>
				</div>
			</section>

			<section class="setting-card" id="attendance-item-settings">
				<div class="card-title"><h2>근태항목 설정</h2></div>
				<div class="setting-layout">
					<div class="list-panel">
						<div class="table-wrap"><table class="attendance-table"><thead><tr><th>근태항목</th><th>단위</th><th>그룹관리</th><th>휴가공제</th><th>사용여부</th></tr></thead><tbody>
							<c:forEach var="item" items="${attendItems}"><tr class="${item.attendItemId eq selectedAttendItem.attendItemId ? 'is-selected' : ''}"><td><a href="${pageContext.request.contextPath}/settings/attendance.do?attendItemId=${item.attendItemId}#attendance-item-settings"><c:out value="${item.attendName}" /></a></td><td><c:out value="${item.unit}" /></td><td><c:out value="${item.groupName}" /></td><td><c:out value="${item.leaveName}" /></td><td><span class="use-status use-status--${item.useYn eq 'Y' ? 'on' : 'off'}">${item.useYn eq 'Y' ? '사용' : '미사용'}</span></td></tr></c:forEach>
							<c:if test="${empty attendItems}"><tr><td colspan="5" class="empty-row">등록된 근태항목이 없습니다.</td></tr></c:if>
						</tbody></table></div>
					</div>
					<form class="editor-panel" action="${pageContext.request.contextPath}/settings/attend-item.do" method="post">
						<input type="hidden" name="attendItemId" value="<c:out value='${selectedAttendItem.attendItemId}' />">
						<h3>근태항목 정보</h3>
						<label class="editor-field"><span>근태항목</span><input name="attendName" value="<c:out value='${selectedAttendItem.attendName}' />" maxlength="100" placeholder="근태항목을 입력해주세요"></label>
						<label class="editor-field"><span>단위</span><select name="unit"><option value="">선택하세요.</option><option value="일" ${selectedAttendItem.unit eq '일' ? 'selected' : ''}>일</option><option value="시간" ${selectedAttendItem.unit eq '시간' ? 'selected' : ''}>시간</option></select></label>
						<div class="editor-field editor-field--group"><span>근태그룹</span><select name="attendGroupId"><option value="">선택하세요.</option><c:forEach var="group" items="${attendGroups}"><option value="${group.attendGroupId}" ${group.attendGroupId eq selectedAttendItem.attendGroupId ? 'selected' : ''}><c:out value="${group.groupName}" /></option></c:forEach></select><a href="#attend-group-modal">그룹관리</a></div>
						<label class="editor-field"><span>휴가공제</span><select name="leaveItemId"><option value="">선택하세요.</option><c:forEach var="leave" items="${leaveItems}"><option value="${leave.leaveItemId}" ${leave.leaveItemId eq selectedAttendItem.leaveItemId ? 'selected' : ''}><c:out value="${leave.leaveName}" /></option></c:forEach></select></label>
						<label class="editor-field"><span>근로시간연계</span><select name="workingHoursType"><option value="">선택하세요.</option><option value="소정" ${selectedAttendItem.workingHoursType eq '소정' ? 'selected' : ''}>소정근로</option><option value="연장" ${selectedAttendItem.workingHoursType eq '연장' ? 'selected' : ''}>연장근로</option><option value="야간" ${selectedAttendItem.workingHoursType eq '야간' ? 'selected' : ''}>야간근로</option><option value="휴일" ${selectedAttendItem.workingHoursType eq '휴일' ? 'selected' : ''}>휴일근로</option></select></label>
						<div class="editor-field editor-field--full"><span>사용여부</span><div class="radio-line"><label><input type="radio" name="useYn" value="Y" ${empty selectedAttendItem.useYn or selectedAttendItem.useYn eq 'Y' ? 'checked' : ''}> 사용</label><label><input type="radio" name="useYn" value="N" ${selectedAttendItem.useYn eq 'N' ? 'checked' : ''}> 사용안함</label></div></div>
						<div class="editor-actions"><button name="action" value="insert">추가</button><button name="action" value="update">수정</button><button class="danger" name="action" value="delete">삭제</button><button class="clear" name="action" value="clear">내용지우기</button></div>
					</form>
				</div>
			</section>

			<div id="attend-group-modal" class="group-modal" role="dialog" aria-modal="true" aria-labelledby="group-modal-title">
				<a class="group-modal__backdrop" href="#attendance-item-settings" aria-label="닫기"></a>
				<div class="group-modal__panel">
					<div class="group-modal__title"><h2 id="group-modal-title">근태그룹 관리</h2><a href="#attendance-item-settings" aria-label="닫기">×</a></div>
					<form action="${pageContext.request.contextPath}/settings/attend-group.do" method="post">
						<ul class="group-list"><c:forEach var="group" items="${attendGroups}"><li><span class="group-list__handle">↕</span><input name="groupNames" value="<c:out value='${group.groupName}' />" aria-label="근태그룹명"><input type="hidden" name="groupIds" value="${group.attendGroupId}"><div><button name="action" value="update:${group.attendGroupId}">수정</button><button name="action" value="delete:${group.attendGroupId}">삭제</button></div></li></c:forEach><c:if test="${empty attendGroups}"><li class="group-list__empty">등록된 근태그룹이 없습니다.</li></c:if></ul>
						<div class="group-add"><input name="newGroupName" placeholder="새 근태그룹명"><button name="action" value="insert">＋ 추가하기</button></div>
						<p class="group-modal__notice">그룹의 표시 순서는 서버에서 저장된 순서를 따릅니다.</p>
						<div class="group-modal__actions"><button name="action" value="resetOrder">초기화</button></div>
					</form>
				</div>
			</div>

			<div id="employee-leave-modal" class="employee-leave-modal" role="dialog" aria-modal="true" aria-labelledby="employee-leave-title">
				<a class="employee-leave-modal__backdrop" href="#leave-settings" aria-label="닫기"></a>
				<div class="employee-leave-modal__panel">
					<div class="employee-leave-modal__title"><h2 id="employee-leave-title">휴가일수 설정</h2><a href="#leave-settings" aria-label="닫기">×</a></div>
					<form action="${pageContext.request.contextPath}/settings/employee-leave.do" method="post">
						<input type="hidden" name="leaveItemId" value="${selectedLeaveItem.leaveItemId}">
						<div class="employee-leave-tools"><div><input name="keyword" placeholder="사원검색"><button name="action" value="search">검색</button><button name="action" value="showAll">전체보기</button></div><select name="status"><option value="">상태별</option><option value="재직">재직</option><option value="퇴직">퇴직</option></select></div>
						<div class="employee-leave-table-wrap"><table class="employee-leave-table"><thead><tr><th>선택</th><th>구분</th><th>사원번호</th><th>성명</th><th>부서</th><th>직위</th><th>입사일</th><th>휴가일수</th></tr></thead><tbody>
							<c:forEach var="row" items="${employeeLeaveRows}"><tr><td><input type="checkbox" name="employeeLeaveIds" value="${row.empLeaveId}"></td><td><c:out value="${row.empType}" /></td><td><c:out value="${row.empNo}" /></td><td><c:out value="${row.empName}" /></td><td><c:out value="${row.deptName}" /></td><td><c:out value="${row.posName}" /></td><td><c:out value="${row.joinDate}" /></td><td><label><input type="number" min="0" step="0.5" name="leaveDays" value="${row.leaveDays}"><span>일</span></label></td></tr></c:forEach>
							<c:if test="${empty employeeLeaveRows}"><tr><td colspan="8" class="empty-row">조회된 사원이 없습니다.</td></tr></c:if>
						</tbody></table></div>
						<div class="employee-leave-actions"><div><button class="delete" name="action" value="delete">휴가일수 삭제</button><button name="action" value="save">휴가일수 저장</button></div><div><a href="${pageContext.request.contextPath}/help/annual-leave.do">연차휴가 계산방법</a><button name="action" value="autoCalculate">휴가일수 자동계산</button></div></div>
					</form>
				</div>
			</div>
		</div>
	</main>
	<%@ include file="/WEB-INF/view/common/footer.jspf" %>
</body>
</html>
