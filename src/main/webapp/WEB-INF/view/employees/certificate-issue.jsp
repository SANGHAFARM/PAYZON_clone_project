<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>인사관리 &gt; 제 증명서 발급</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/employees/certificate-issue.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/payzon-ui.css">
</head>
<body>
	<%@ include file="/WEB-INF/view/common/header.jspf" %>
	<main class="page-content">
		<div class="certificate-page">
			<header class="page-heading"><div><p>인사관리</p><h1>제 증명서 발급</h1></div></header>
			<c:if test="${not empty message}"><p class="form-message"><c:out value="${message}" /></p></c:if>

			<div class="certificate-layout">
				<aside class="employee-panel">
					<form class="employee-search" action="${pageContext.request.contextPath}/personnel/certificate.do" method="get"><input name="keyword" value="${param.keyword}" placeholder="검색어 입력"><button type="submit">검색</button><a href="${pageContext.request.contextPath}/personnel/certificate.do">전체보기</a></form>
					<div class="employee-list"><table><thead><tr><th>구분</th><th>성명</th><th>부서</th><th>직위</th><th>상태</th></tr></thead><tbody><c:forEach var="employee" items="${employees}"><tr class="${employee.employeeId eq selectedEmployee.employeeId ? 'is-selected' : ''}"><td><c:out value="${employee.employmentType}" /></td><td><a href="${pageContext.request.contextPath}/personnel/certificate.do?employeeId=${employee.employeeId}"><c:out value="${employee.name}" /></a></td><td><c:out value="${employee.departmentName}" /></td><td><c:out value="${employee.positionName}" /></td><td><span class="status status--${employee.status eq 'WORK' ? 'work' : 'retired'}">${employee.status eq 'WORK' ? '재직' : '퇴직'}</span></td></tr></c:forEach><c:if test="${empty employees}"><tr><td colspan="5" class="empty-row">검색된 사원이 없습니다.</td></tr></c:if></tbody></table></div>
				</aside>

				<section class="certificate-workspace">
					<input class="certificate-switch" type="radio" name="certificateType" value="WORKING" id="certificate-working" form="certificate-issue-form" checked>
					<input class="certificate-switch" type="radio" name="certificateType" value="CAREER" id="certificate-career" form="certificate-issue-form">
					<input class="certificate-switch" type="radio" name="certificateType" value="RETIREMENT" id="certificate-retirement" form="certificate-issue-form">
					<div class="certificate-tabs"><label for="certificate-working">재직증명서</label><label for="certificate-career">경력증명서</label><label for="certificate-retirement">퇴직증명서</label></div>

					<form class="certificate-form" id="certificate-issue-form" action="${pageContext.request.contextPath}/personnel/certificate-issue.do" method="post">
						<input type="hidden" name="employeeId" value="${selectedEmployee.employeeId}">
						<div class="certificate-document certificate-document--working">
							<input type="hidden" name="certificateTypeWorking" value="WORKING">
							<header class="document-title"><div class="company-logo"><c:choose><c:when test="${not empty company.logoUrl}"><img src="<c:out value='${company.logoUrl}' />" alt="회사 로고"></c:when><c:otherwise><span>회사 로고</span></c:otherwise></c:choose></div><h2>재 직 증 명 서</h2><label><input type="checkbox" name="hideResidentNoWorking" value="Y"> 주민등록번호 숨김</label></header>
							<div class="document-grid"><h3>인적사항</h3><dl><div><dt>성명</dt><dd><c:out value="${selectedEmployee.name}" /></dd></div><div><dt>주민등록번호</dt><dd><c:out value="${selectedEmployee.maskedResidentNo}" /></dd></div><div class="wide"><dt>주소</dt><dd><c:out value="${selectedEmployee.address}" /></dd></div></dl><h3>재직사항</h3><dl><div><dt>회사명</dt><dd><c:out value="${company.companyName}" /></dd></div><div><dt>사업자번호</dt><dd><c:out value="${company.businessNo}" /></dd></div><div><dt>부서</dt><dd><c:out value="${selectedEmployee.departmentName}" /></dd></div><div><dt>직위</dt><dd><c:out value="${selectedEmployee.positionName}" /></dd></div><div><dt>입사일</dt><dd><c:out value="${selectedEmployee.joinDate}" /></dd></div><div><dt>근속기간</dt><dd><c:out value="${selectedEmployee.careerPeriod}" /></dd></div></dl></div>
						</div>
						<div class="certificate-document certificate-document--career">
							<input type="hidden" name="certificateTypeCareer" value="CAREER">
							<header class="document-title"><div class="company-logo"><c:choose><c:when test="${not empty company.logoUrl}"><img src="<c:out value='${company.logoUrl}' />" alt="회사 로고"></c:when><c:otherwise><span>회사 로고</span></c:otherwise></c:choose></div><h2>경 력 증 명 서</h2><label><input type="checkbox" name="hideResidentNoCareer" value="Y"> 주민등록번호 숨김</label></header>
							<div class="document-grid document-grid--career"><h3>인적사항</h3><dl><div><dt>성명</dt><dd><c:out value="${selectedEmployee.name}" /></dd></div><div><dt>주민등록번호</dt><dd><c:out value="${selectedEmployee.maskedResidentNo}" /></dd></div><div class="wide"><dt>주소</dt><dd><c:out value="${selectedEmployee.address}" /></dd></div></dl><h3>경력사항</h3><div class="career-history"><table><thead><tr><th>근무기간</th><th>근무부서</th><th>직위(직책)</th><th>담당업무</th></tr></thead><tbody><c:forEach var="career" items="${careers}"><tr><td><c:out value="${career.joinDate}" /> ~ <c:out value="${career.retirementDate}" /></td><td><c:out value="${career.departmentName}" /></td><td><c:out value="${career.positionName}" /></td><td><c:out value="${career.duty}" /></td></tr></c:forEach><c:if test="${empty careers}"><c:forEach begin="1" end="4"><tr><td></td><td></td><td></td><td></td></tr></c:forEach></c:if></tbody></table><dl class="career-summary"><div><dt>근무연한</dt><dd><c:out value="${selectedEmployee.careerPeriod}" /></dd></div><div><dt>퇴직사유</dt><dd><c:out value="${selectedEmployee.retirementReason}" /></dd></div></dl></div></div>
						</div>
						<div class="certificate-document certificate-document--retirement">
							<input type="hidden" name="certificateTypeRetirement" value="RETIREMENT">
							<header class="document-title"><div class="company-logo"><c:choose><c:when test="${not empty company.logoUrl}"><img src="<c:out value='${company.logoUrl}' />" alt="회사 로고"></c:when><c:otherwise><span>회사 로고</span></c:otherwise></c:choose></div><h2>퇴 직 증 명 서</h2><label><input type="checkbox" name="hideResidentNoRetirement" value="Y"> 주민등록번호 숨김</label></header>
							<div class="document-grid"><h3>인적사항</h3><dl><div><dt>성명</dt><dd><c:out value="${selectedEmployee.name}" /></dd></div><div><dt>주민등록번호</dt><dd><c:out value="${selectedEmployee.maskedResidentNo}" /></dd></div><div class="wide"><dt>주소</dt><dd><c:out value="${selectedEmployee.address}" /></dd></div></dl><h3>재직사항</h3><dl><div><dt>회사명</dt><dd><c:out value="${company.companyName}" /></dd></div><div><dt>사업자번호</dt><dd><c:out value="${company.businessNo}" /></dd></div><div><dt>부서</dt><dd><c:out value="${selectedEmployee.departmentName}" /></dd></div><div><dt>직위</dt><dd><c:out value="${selectedEmployee.positionName}" /></dd></div><div><dt>재직기간</dt><dd><c:if test="${not empty selectedEmployee.joinDate or not empty selectedEmployee.retirementDate}"><c:out value="${selectedEmployee.joinDate}" /> ~ <c:out value="${selectedEmployee.retirementDate}" /></c:if></dd></div><div><dt>근속기간</dt><dd><c:out value="${selectedEmployee.careerPeriod}" /></dd></div></dl></div>
						</div>

						<section class="issue-options"><label class="use-field"><span>발급용도</span><select name="certificateUse"><option value="">선택</option><option>비자 신청용</option><option>은행 제출용</option><option>관공서 제출용</option><option>학교 제출용</option><option>회사 제출용</option><option>병무청 제출용</option><option>관련과제 제출용</option><option value="DIRECT">직접입력</option></select></label><label class="direct-use-field"><span>직접입력</span><input name="certificateUseDirect"></label><label class="department-field"><span>발급부서</span><select name="issueDepartmentId"><option value="">선택</option><c:forEach var="department" items="${departments}"><option value="${department.departmentId}"><c:out value="${department.departmentName}" /></option></c:forEach></select></label><label class="contact-field"><span>연락처</span><input name="companyPhone" value="${company.phone}" readonly></label></section>
						<textarea class="certificate-message" name="certificateMemo">상기인은 위와 같이 당사에 재직 또는 근무하였음을 증명합니다.</textarea>
						<div class="issue-date"><input name="issueYear" value="${issueDate.year}" maxlength="4">년 <input name="issueMonth" value="${issueDate.month}" maxlength="2">월 <input name="issueDay" value="${issueDate.day}" maxlength="2">일</div>
						<footer class="certificate-footer"><label><input type="checkbox" name="showCeo" value="Y" checked> 대표자 표기</label><div><strong><c:out value="${company.companyName}" /></strong><span>대표이사 <c:out value="${company.ceoName}" /></span></div><div class="stamp-box"><c:choose><c:when test="${not empty company.stampUrl}"><img src="<c:out value='${company.stampUrl}' />" alt="회사 도장"></c:when><c:otherwise><span>회사 도장을<br>넣어주세요</span></c:otherwise></c:choose></div></footer>
						<div class="issue-actions">
							<button type="submit" name="actionType" value="ISSUE">증명서 발급</button>
						</div>
					</form>
				</section>
			</div>
		</div>
	</main>
	<%@ include file="/WEB-INF/view/common/footer.jspf" %>
</body>
</html>
