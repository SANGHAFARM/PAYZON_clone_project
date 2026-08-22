<%-- 조회 결과와 입력 양식을 표시하는 JSP 화면이다. --%>
<%-- 照会結果と入力フォームを表示するJSP画面である。 --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>사원 퇴직처리</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/retirement/retirement-process.css?v=20260821-1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/payzon-ui.css?v=20260821-1">
</head>
<body>
<%@ include file="/WEB-INF/view/common/header.jspf" %>

<main class="retirement-page page-content">
    <header class="page-heading">
        <div>
            <p>퇴직관리</p>
            <h1>사원 퇴직처리</h1>
        </div>
    </header>
    <%-- EMPLOYEE 중심 JOIN 결과와 퇴직정산 존재 여부를 함께 표시한다. --%>
    <section class="retirement-card">
        <div class="retirement-toolbar">
        <form class="retirement-keyword-search" method="get" action="${pageContext.request.contextPath}/retirement/process.do">
            <select name="searchTarget" aria-label="검색 항목">
                <option value="name" ${param.searchTarget eq 'name' ? 'selected' : ''}>성명</option>
                <option value="employeeNo" ${param.searchTarget eq 'employeeNo' ? 'selected' : ''}>사원번호</option>
                <option value="department" ${param.searchTarget eq 'department' ? 'selected' : ''}>부서</option>
                <option value="all" ${param.searchTarget eq 'all' ? 'selected' : ''}>전체</option>
            </select>
            <input type="search" name="keyword" value="${param.keyword}"
                    placeholder="검색어 입력" aria-label="검색어">
            <button type="submit" class="search-button">검색</button>
            <a class="all-view" href="${pageContext.request.contextPath}/retirement/process.do">전체보기</a>
            <select class="status-filter" name="status" aria-label="재직 상태">
                <option value="">상태별</option>
                <option value="ACTIVE" ${param.status eq 'ACTIVE' ? 'selected' : ''}>재직</option>
                <option value="RETIRED" ${param.status eq 'RETIRED' ? 'selected' : ''}>퇴직</option>
            </select>
            <button type="submit" class="search-button">조회</button>
        </form>
        </div>

        <div class="retirement-table-wrap">
            <table class="retirement-table data-table">
                <colgroup>
                    <col class="col-number"><col class="col-status"><col class="col-employee-no">
                    <col class="col-name"><col class="col-department"><col class="col-position">
                    <col class="col-date"><col class="col-date"><col class="col-years">
                    <col class="col-settlement"><col class="col-settlement">
                </colgroup>
                <thead>
                <tr>
                    <th>번호</th><th>상태</th><th>사원번호</th><th>성명</th><th>부서</th><th>직위</th>
                    <th>입사일</th><th>퇴직일</th><th>근속연수</th><th>중간정산</th><th>퇴직정산</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="employee" items="${employees}" varStatus="status">
                    <tr class="employee-row">
                        <td><a href="#retirement-${employee.employeeId}">${status.count}</a></td>
                        <td><a href="#retirement-${employee.employeeId}"
                                class="employee-status ${employee.status eq 'RETIRED' ? 'is-retired' : 'is-active'}">${employee.statusName}</a></td>
                        <td><a href="#retirement-${employee.employeeId}">${employee.employeeNo}</a></td>
                        <td><a href="#retirement-${employee.employeeId}">${employee.name}</a></td>
                        <td><a href="#retirement-${employee.employeeId}">${employee.departmentName}</a></td>
                        <td><a href="#retirement-${employee.employeeId}">${employee.positionName}</a></td>
                        <td><a href="#retirement-${employee.employeeId}">${employee.joinDate}</a></td>
                        <td><a href="#retirement-${employee.employeeId}">${employee.retirementDate}</a></td>
                        <td><a href="#retirement-${employee.employeeId}">${employee.serviceYears}</a></td>
                        <td><a href="#retirement-${employee.employeeId}">${employee.interimSettlement ? '○' : '×'}</a></td>
                        <td><a href="#retirement-${employee.employeeId}">${employee.retirementSettlement ? '○' : '×'}</a></td>
                    </tr>
                </c:forEach>
                <c:if test="${empty employees}"><tr><td colspan="11" class="empty-row">검색된 사원이 없습니다.</td></tr></c:if>
                </tbody>
            </table>
        </div>
        <div class="pagination">
            <c:if test="${pageInfo.hasPrevious}"><c:url var="previousUrl" value="/retirement/process.do"><c:param name="page" value="${pageInfo.previousPage}"/><c:param name="searchTarget" value="${param.searchTarget}"/><c:param name="keyword" value="${condition.keyword}"/><c:param name="status" value="${condition.status eq 'WORK' ? 'ACTIVE' : condition.status eq 'RETIRED' ? 'RETIRED' : ''}"/></c:url><a href="${previousUrl}">이전</a></c:if>
            <c:forEach var="pageNo" begin="${pageInfo.startPage}" end="${pageInfo.endPage}"><c:url var="pageUrl" value="/retirement/process.do"><c:param name="page" value="${pageNo}"/><c:param name="searchTarget" value="${param.searchTarget}"/><c:param name="keyword" value="${condition.keyword}"/><c:param name="status" value="${condition.status eq 'WORK' ? 'ACTIVE' : condition.status eq 'RETIRED' ? 'RETIRED' : ''}"/></c:url><a class="${pageNo eq pageInfo.currentPage ? 'is-current' : ''}" href="${pageUrl}">${pageNo}</a></c:forEach>
            <c:if test="${pageInfo.hasNext}"><c:url var="nextUrl" value="/retirement/process.do"><c:param name="page" value="${pageInfo.nextPage}"/><c:param name="searchTarget" value="${param.searchTarget}"/><c:param name="keyword" value="${condition.keyword}"/><c:param name="status" value="${condition.status eq 'WORK' ? 'ACTIVE' : condition.status eq 'RETIRED' ? 'RETIRED' : ''}"/></c:url><a href="${nextUrl}">다음</a></c:if>
        </div>
    </section>

    <%-- CSS :target 모달에서 퇴직정보를 입력하거나 기존 퇴직처리를 취소한다. --%>
    <c:forEach var="employee" items="${employees}">
        <section id="retirement-${employee.employeeId}" class="retirement-modal-overlay">
            <div class="retirement-modal" role="dialog" aria-modal="true" aria-labelledby="title-${employee.employeeId}">
                <header>
                    <h2 id="title-${employee.employeeId}">${employee.status eq 'RETIRED' ? '퇴직처리 취소' : '퇴직처리'}</h2>
                    <a class="modal-close" href="#" aria-label="닫기">×</a>
                </header>
                <form method="post" action="${pageContext.request.contextPath}/retirement/process.do">
                    <input type="hidden" name="employeeId" value="${employee.employeeId}">
                    <input type="hidden" name="processType" value="${employee.status eq 'RETIRED' ? 'CANCEL' : 'RETIRE'}">
                    <div class="retirement-modal__body">
                        <c:choose>
                            <c:when test="${employee.status eq 'RETIRED'}">
                                <label><span>퇴직구분</span><select disabled aria-disabled="true"><option>${employee.retirementTypeName}</option></select></label>
                                <input type="hidden" name="retirementType" value="${employee.retirementType}">
                                <label><span>퇴직일자</span><input type="date" name="retirementDate" value="${employee.retirementDate}" readonly></label>
                            </c:when>
                            <c:otherwise>
                                <label><span>퇴직구분</span><select name="retirementType"><option value="">선택</option><c:forEach var="type" items="${retirementTypes}"><option value="${type.code}">${type.name}</option></c:forEach></select></label>
                                <label><span>퇴직일자</span><input type="date" name="retirementDate" value="${empty employee.retirementDate ? currentDate : employee.retirementDate}"></label>
                            </c:otherwise>
                        </c:choose>
                        <label><span>퇴직사유</span><input type="text" name="retirementReason" value="${employee.retirementReason}" ${employee.status eq 'RETIRED' ? 'readonly' : ''}></label>
                        <label><span>퇴직 후 연락처</span><input type="text" name="afterContact" value="${employee.afterContact}" ${employee.status eq 'RETIRED' ? 'readonly' : ''}></label>
                    </div>
                    <div class="retirement-modal__actions"><button type="submit" class="button button-primary">저장</button></div>
                </form>
            </div>
        </section>
    </c:forEach>

</main>

<c:if test="${not empty retirementPopupMessage or not empty message}">
    <div class="retirement-alert" role="alertdialog" aria-modal="true" aria-labelledby="retirement-alert-message">
        <a class="retirement-alert__backdrop" href="${pageContext.request.contextPath}/retirement/process.do" aria-label="안내 닫기"></a>
        <div class="retirement-alert__panel">
            <p id="retirement-alert-message"><c:out value="${not empty retirementPopupMessage ? retirementPopupMessage : message}" /></p>
            <a href="${pageContext.request.contextPath}/retirement/process.do">확인</a>
        </div>
    </div>
</c:if>

<%@ include file="/WEB-INF/view/common/footer.jspf" %>
</body>
</html>
