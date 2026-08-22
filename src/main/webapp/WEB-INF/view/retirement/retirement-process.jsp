<%-- 조회 결과와 입력 양식을 표시하는 JSP 화면이다. --%>
<%-- 照会結果と入力フォームを表示するJSP画面である。 --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html lang="ja-JP">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>退職処理</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/retirement/retirement-process.css?v=20260821-1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/payzon-ui.css?v=20260821-1">
</head>
<body>
<%@ include file="/WEB-INF/view/common/header.jspf" %>

<main class="retirement-page page-content">
    <header class="page-heading">
        <div>
            <p>退職管理</p>
            <h1>退職処理</h1>
        </div>
    </header>
    <%--  EMPLOYEE中心JOIN結果と退職決済の有無を併せて表示する。 --%>
    <section class="retirement-card">
        <div class="retirement-toolbar">
        <form class="retirement-keyword-search" method="get" action="${pageContext.request.contextPath}/retirement/process.do">
            <select name="searchTarget" aria-label="検索項目">
                <option value="name" ${param.searchTarget eq 'name' ? 'selected' : ''}>氏名</option>
                <option value="employeeNo" ${param.searchTarget eq 'employeeNo' ? 'selected' : ''}>社員番号</option>
                <option value="department" ${param.searchTarget eq 'department' ? 'selected' : ''}>部署</option>
                <option value="all" ${param.searchTarget eq 'all' ? 'selected' : ''}>全体</option>
            </select>
            <input type="search" name="keyword" value="${param.keyword}"
                    placeholder="検索キーワードを入力" aria-label="検索語">
            <button type="submit" class="search-button">検索</button>
            <a class="all-view" href="${pageContext.request.contextPath}/retirement/process.do">全体を見る</a>
            <select class="status-filter" name="status" aria-label="在職状況">
                <option value="">ステータス別</option>
                <option value="ACTIVE" ${param.status eq 'ACTIVE' ? 'selected' : ''}>在職</option>
                <option value="RETIRED" ${param.status eq 'RETIRED' ? 'selected' : ''}>退職</option>
            </select>
            <button type="submit" class="search-button">照会</button>
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
                    <th>番号</th><th>ステータス</th><th>社員番号</th><th>氏名</th><th>部署</th><th>役職</th>
                    <th>入社日</th><th>退職日</th><th>勤続年数</th><th>中間決済</th><th>退職精算</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="employee" items="${employees}" varStatus="status">
                    <tr class="employee-row">
                        <td><a href="#retirement-${employee.employeeId}">${status.count}</a></td>
                        <td><a href="#retirement-${employee.employeeId}"
                                class="employee-status ${employee.status eq 'RETIRED' ? 'is-retired' : 'is-active'}"><ui:code-label value="${employee.statusName}" /></a></td>
                        <td><a href="#retirement-${employee.employeeId}">${employee.employeeNo}</a></td>
                        <td><a href="#retirement-${employee.employeeId}">${employee.name}</a></td>
                        <td><a href="#retirement-${employee.employeeId}">${employee.departmentName}</a></td>
                        <td><a href="#retirement-${employee.employeeId}">${employee.positionName}</a></td>
                        <td><a href="#retirement-${employee.employeeId}">${employee.joinDate}</a></td>
                        <td><a href="#retirement-${employee.employeeId}">${employee.retirementDate}</a></td>
                        <td><a href="#retirement-${employee.employeeId}">${fn:replace(employee.serviceYears, '년', '年')}</a></td>
                        <td><a href="#retirement-${employee.employeeId}">${employee.interimSettlement ? '○' : '×'}</a></td>
                        <td><a href="#retirement-${employee.employeeId}">${employee.retirementSettlement ? '○' : '×'}</a></td>
                    </tr>
                </c:forEach>
                <c:if test="${empty employees}"><tr><td colspan="11" class="empty-row">検索された社員はありません。</td></tr></c:if>
                </tbody>
            </table>
        </div>
        <div class="pagination">
            <c:if test="${pageInfo.hasPrevious}"><c:url var="previousUrl" value="/retirement/process.do"><c:param name="page" value="${pageInfo.previousPage}"/><c:param name="searchTarget" value="${param.searchTarget}"/><c:param name="keyword" value="${condition.keyword}"/><c:param name="status" value="${condition.status eq 'WORK' ? 'ACTIVE' : condition.status eq 'RETIRED' ? 'RETIRED' : ''}"/></c:url><a href="${previousUrl}">以前</a></c:if>
            <c:forEach var="pageNo" begin="${pageInfo.startPage}" end="${pageInfo.endPage}"><c:url var="pageUrl" value="/retirement/process.do"><c:param name="page" value="${pageNo}"/><c:param name="searchTarget" value="${param.searchTarget}"/><c:param name="keyword" value="${condition.keyword}"/><c:param name="status" value="${condition.status eq 'WORK' ? 'ACTIVE' : condition.status eq 'RETIRED' ? 'RETIRED' : ''}"/></c:url><a class="${pageNo eq pageInfo.currentPage ? 'is-current' : ''}" href="${pageUrl}">${pageNo}</a></c:forEach>
            <c:if test="${pageInfo.hasNext}"><c:url var="nextUrl" value="/retirement/process.do"><c:param name="page" value="${pageInfo.nextPage}"/><c:param name="searchTarget" value="${param.searchTarget}"/><c:param name="keyword" value="${condition.keyword}"/><c:param name="status" value="${condition.status eq 'WORK' ? 'ACTIVE' : condition.status eq 'RETIRED' ? 'RETIRED' : ''}"/></c:url><a href="${nextUrl}">次へ</a></c:if>
        </div>
    </section>

    <%--  CSS：targetモーダルから退職情報を入力するか、既存の退職処理をキャンセルする。 --%>
    <c:forEach var="employee" items="${employees}">
        <section id="retirement-${employee.employeeId}" class="retirement-modal-overlay">
            <div class="retirement-modal" role="dialog" aria-modal="true" aria-labelledby="title-${employee.employeeId}">
                <header>
                    <h2 id="title-${employee.employeeId}">${employee.status eq 'RETIRED' ? '退職処理取消' : '退職処理'}</h2>
                    <a class="modal-close" href="#" aria-label="閉じる">×</a>
                </header>
                <form method="post" action="${pageContext.request.contextPath}/retirement/process.do">
                    <input type="hidden" name="employeeId" value="${employee.employeeId}">
                    <input type="hidden" name="processType" value="${employee.status eq 'RETIRED' ? 'CANCEL' : 'RETIRE'}">
                    <div class="retirement-modal__body">
                        <c:choose>
                            <c:when test="${employee.status eq 'RETIRED'}">
                                <label><span>退職区分</span><select disabled aria-disabled="true"><option>${employee.retirementTypeName}</option></select></label>
                                <input type="hidden" name="retirementType" value="${employee.retirementType}">
                                <label><span>退職日</span><input type="date" lang="ja-JP" name="retirementDate" value="${employee.retirementDate}" readonly></label>
                            </c:when>
                            <c:otherwise>
                                <label><span>退職区分</span><select name="retirementType"><option value="">選択</option><c:forEach var="type" items="${retirementTypes}"><option value="${type.code}">${type.name}</option></c:forEach></select></label>
                                <label><span>退職日</span><input type="date" lang="ja-JP" name="retirementDate" value="${empty employee.retirementDate ? currentDate : employee.retirementDate}"></label>
                            </c:otherwise>
                        </c:choose>
                        <label><span>退職理由</span><input type="text" name="retirementReason" value="${employee.retirementReason}" ${employee.status eq 'RETIRED' ? 'readonly' : ''}></label>
                        <label><span>退職後の連絡先</span><input type="text" name="afterContact" value="${employee.afterContact}" ${employee.status eq 'RETIRED' ? 'readonly' : ''}></label>
                    </div>
                    <div class="retirement-modal__actions"><button type="submit" class="button button-primary">退職処理取り消し</button></div>
                </form>
            </div>
        </section>
    </c:forEach>

</main>

<c:if test="${not empty retirementPopupMessage or not empty message}">
    <div class="retirement-alert" role="alertdialog" aria-modal="true" aria-labelledby="retirement-alert-message">
        <a class="retirement-alert__backdrop" href="${pageContext.request.contextPath}/retirement/process.do" aria-label="案内を閉じる"></a>
        <div class="retirement-alert__panel">
            <p id="retirement-alert-message"><ui:message-label value="${not empty retirementPopupMessage ? retirementPopupMessage : message}" /></p>
            <a href="${pageContext.request.contextPath}/retirement/process.do">確認</a>
        </div>
    </div>
</c:if>

<%@ include file="/WEB-INF/view/common/footer.jspf" %>
</body>
</html>
