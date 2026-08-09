<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>일용직 근무기록/관리</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/common.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/attend/day-worker-management.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/payzon-ui.css">
</head>
<body>
	<%@ include file="/WEB-INF/view/common/header.jspf" %>

	<main class="page-content day-worker-page">
		<header class="page-heading"><div><p>근태관리</p><h1>일용직 근무기록/관리</h1></div></header>

		<section class="worker-panel">
			<div class="worker-search">
				<form method="get" action="${pageContext.request.contextPath}/diligence/day-worker-management">
					<input type="search" name="keyword" value="<c:out value='${param.keyword}'/>" placeholder="검색어 입력" aria-label="검색어">
					<button type="submit" class="button button-primary">검색</button>
					<a class="button button-outline" href="${pageContext.request.contextPath}/diligence/day-worker-management">전체보기</a>
				</form>
				<form class="status-filter" method="get" action="${pageContext.request.contextPath}/diligence/day-worker-management">
					<select name="employmentStatus" aria-label="상태별">
						<option value="">상태별</option>
						<option value="active" ${param.employmentStatus eq 'active' ? 'selected' : ''}>재직</option>
						<option value="retired" ${param.employmentStatus eq 'retired' ? 'selected' : ''}>퇴직</option>
					</select>
					<button type="submit" class="button button-primary">조회</button>
				</form>
			</div>

			<div class="worker-layout">
				<div class="employee-list-wrap">
					<table class="data-table employee-table">
						<thead><tr><th class="check-cell"><input type="checkbox" aria-label="전체 선택"></th><th>구분</th><th>사원번호</th><th>성명</th><th>부서</th><th>근무기록</th></tr></thead>
						<tbody>
							<c:forEach var="employee" items="${dayWorkers}">
								<tr><td class="check-cell"><input type="checkbox" name="employeeIds" value="${employee.employeeId}" aria-label="${employee.name} 선택"></td><td><c:out value="${employee.employmentType}" /></td><td><c:out value="${employee.employeeNo}" /></td><td><c:out value="${employee.name}" /></td><td><c:out value="${employee.departmentName}" /></td><td><a class="button button-small" href="#work-history-${employee.employeeId}">관리</a></td></tr>
							</c:forEach>
							<c:if test="${empty dayWorkers}">
								<tr><td colspan="6" class="empty-row">조회된 일용직 사원이 없습니다.</td></tr>
							</c:if>
						</tbody>
					</table>
				</div>

				<form class="record-form" method="post" action="${pageContext.request.contextPath}/diligence/day-worker-management/save">
					<h2>일용직 근무기록 입력</h2>
					<div class="form-fields">
						<label><span>근무일자</span><input type="date" name="workDate" required></label>
						<label><span>현장/프로젝트</span><span class="project-control"><select name="projectCode"><option value="">선택하세요.</option><c:forEach var="project" items="${workProjects}"><option value="${project.projectCode}"><c:out value="${project.projectName}" /></option></c:forEach></select><a class="button button-project" href="#project-manager">목록관리</a></span></label>
						<label><span>일당</span><span class="amount-control"><input type="number" name="dailyPay" min="0" placeholder="일당을 입력해주세요"><em>원</em></span></label>
						<label><span>지급율</span><input type="number" name="paymentRate" min="0" step="0.1" value="1.0"></label>
						<label class="calculated"><span>소득세</span><span class="amount-control"><input type="text" name="incomeTax" value="${calculatedIncomeTax}" placeholder="자동 계산됩니다" readonly><em>원</em></span></label>
						<label class="calculated"><span>지방소득세</span><span class="amount-control"><input type="text" name="localIncomeTax" value="${calculatedLocalIncomeTax}" placeholder="자동 계산됩니다" readonly><em>원</em></span></label>
						<label class="calculated"><span>실지급액</span><span class="amount-control"><input type="text" name="netPay" value="${calculatedNetPay}" placeholder="자동 계산됩니다" readonly><em>원</em></span></label>
					</div>
					<div class="form-actions"><button type="submit" class="button button-primary action-button">저장</button><button type="reset" class="button button-muted action-button clear-button">내용지우기</button></div>
				</form>
			</div>
		</section>
	</main>

	<c:forEach var="employee" items="${dayWorkers}">
		<div id="work-history-${employee.employeeId}" class="modal-overlay">
			<section class="modal work-history-modal" role="dialog" aria-modal="true" aria-labelledby="history-title-${employee.employeeId}">
				<header><h2 id="history-title-${employee.employeeId}">사원별 근무기록</h2><a href="#" aria-label="닫기">&times;</a></header>
				<div class="modal-body">
					<div class="record-summary"><p>성명 : <strong><c:out value="${employee.name}" /></strong> (<c:out value="${employee.employeeNo}" />)　 부서 : <c:out value="${employee.departmentName}" />　 직위 : <c:out value="${employee.positionName}" /></p><form method="get"><select name="year" aria-label="연도"><option value="2026">2026년</option></select><select name="month" aria-label="월"><c:forEach var="monthNo" begin="1" end="12"><option value="${monthNo}" ${monthNo eq 8 ? 'selected' : ''}>${monthNo}월</option></c:forEach></select></form></div>
					<table class="data-table"><thead><tr><th>번호</th><th>근무일자</th><th>현장/프로젝트</th><th>일당</th><th>지급율</th><th>지급액</th><th>소득세</th><th>지방소득세</th><th>실지급액</th><th>수정/삭제</th></tr></thead><tbody><c:forEach var="record" items="${employee.workRecords}"><tr><td><c:out value="${record.rowNumber}" /></td><td><c:out value="${record.workDate}" /></td><td><c:out value="${record.projectName}" /></td><td><c:out value="${record.dailyPay}" /></td><td><c:out value="${record.paymentRate}" /></td><td><c:out value="${record.paymentAmount}" /></td><td><c:out value="${record.incomeTax}" /></td><td><c:out value="${record.localIncomeTax}" /></td><td><c:out value="${record.netPay}" /></td><td><a class="mini-button" href="?editId=${record.recordId}">수정</a><a class="mini-button mini-delete" href="?deleteId=${record.recordId}">삭제</a></td></tr></c:forEach><c:if test="${empty employee.workRecords}"><tr><td colspan="10" class="empty-row">등록된 근무기록이 없습니다.</td></tr></c:if></tbody></table>
				</div>
			</section>
		</div>
	</c:forEach>
	<div id="project-manager" class="modal-overlay">
		<section class="modal project-modal" role="dialog" aria-modal="true" aria-labelledby="project-title">
			<header><h2 id="project-title">현장/프로젝트 목록관리</h2><a href="#" aria-label="닫기">&times;</a></header>
			<div class="modal-body">
				<ul class="project-list"><c:forEach var="project" items="${workProjects}"><li><span><c:out value="${project.projectName}" /></span><span><a href="?projectEdit=${project.projectCode}">수정</a><a href="?projectDelete=${project.projectCode}">삭제</a></span></li></c:forEach><c:if test="${empty workProjects}"><li><span>등록된 현장/프로젝트가 없습니다.</span></li></c:if></ul>
				<form class="project-add" method="post" action="${pageContext.request.contextPath}/diligence/day-worker-management/project/save"><input type="text" name="projectName" placeholder="새 현장/프로젝트명" required><button type="submit" class="button button-primary">추가하기</button></form>
			</div>
		</section>
	</div>

	<%@ include file="/WEB-INF/view/common/footer.jspf" %>
</body>
</html>
