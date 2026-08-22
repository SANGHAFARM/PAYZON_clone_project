<%-- 조회 결과와 입력 양식을 표시하는 JSP 화면이다. --%>
<%-- 照会結果と入力フォームを表示するJSP画面である。 --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html lang="ja-JP">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>人事管理>社員状況/管理</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/employees/employees-list.css?v=20260822-1">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/payzon-ui.css?v=20260821-1">
    <style>
        <c:if test="${not visibleColumns.contains('employmentType')}">.employee-table th:nth-child(2), .employee-table td:nth-child(2) { display:none; }</c:if>
        <c:if test="${not visibleColumns.contains('joinDate')}">.employee-table th:nth-child(3), .employee-table td:nth-child(3) { display:none; }</c:if>
        <c:if test="${not visibleColumns.contains('employeeNo')}">.employee-table th:nth-child(4), .employee-table td:nth-child(4) { display:none; }</c:if>
        <c:if test="${not visibleColumns.contains('name')}">.employee-table th:nth-child(5), .employee-table td:nth-child(5) { display:none; }</c:if>
        <c:if test="${not visibleColumns.contains('englishName')}">.employee-table th:nth-child(6), .employee-table td:nth-child(6) { display:none; }</c:if>
        <c:if test="${not visibleColumns.contains('department')}">.employee-table th:nth-child(7), .employee-table td:nth-child(7) { display:none; }</c:if>
        <c:if test="${not visibleColumns.contains('position')}">.employee-table th:nth-child(8), .employee-table td:nth-child(8) { display:none; }</c:if>
        <c:if test="${not visibleColumns.contains('residentNo')}">.employee-table th:nth-child(9), .employee-table td:nth-child(9) { display:none; }</c:if>
        <c:if test="${not visibleColumns.contains('nationalityType')}">.employee-table th:nth-child(10), .employee-table td:nth-child(10) { display:none; }</c:if>
        <c:if test="${not visibleColumns.contains('address')}">.employee-table th:nth-child(11), .employee-table td:nth-child(11) { display:none; }</c:if>
        <c:if test="${not visibleColumns.contains('phone')}">.employee-table th:nth-child(12), .employee-table td:nth-child(12) { display:none; }</c:if>
        <c:if test="${not visibleColumns.contains('mobile')}">.employee-table th:nth-child(13), .employee-table td:nth-child(13) { display:none; }</c:if>
        <c:if test="${not visibleColumns.contains('email')}">.employee-table th:nth-child(14), .employee-table td:nth-child(14) { display:none; }</c:if>
        <c:if test="${not visibleColumns.contains('sns')}">.employee-table th:nth-child(15), .employee-table td:nth-child(15) { display:none; }</c:if>
        <c:if test="${not visibleColumns.contains('retirementDate')}">.employee-table th:nth-child(16), .employee-table td:nth-child(16) { display:none; }</c:if>
        <c:if test="${not visibleColumns.contains('status')}">.employee-table th:nth-child(17), .employee-table td:nth-child(17) { display:none; }</c:if>
        <c:if test="${not visibleColumns.contains('bankAccount')}">.employee-table th:nth-child(18), .employee-table td:nth-child(18) { display:none; }</c:if>
        .delete-confirmation-panel { width:430px; max-width:calc(100vw - 40px); box-sizing:border-box; }
        .delete-confirmation-text { margin:0; color:#555; line-height:1.7; text-align:center; }
        .delete-confirmation-text span { display:block; white-space:nowrap; }
        .delete-confirmation-text span + span { margin-top:8px; }
        .delete-confirmation-text strong { color:#d64040; }
        .delete-confirmation-text .delete-confirmation-warning { margin-top:12px; color:#d64040; font-weight:400; }
        .delete-confirmation-actions { display:flex; justify-content:center; gap:8px; margin-top:24px; }
        .delete-confirmation-actions button, .delete-confirmation-actions a { min-width:90px; padding:9px 16px; border:0; border-radius:4px; font-weight:700; text-align:center; text-decoration:none; }
        .delete-confirmation-actions button { color:#fff; background:#d64040; }
        .delete-confirmation-actions a { color:#555; background:#e9edf2; }
        .search-validation-actions a { color:#fff; background:#347fc3; }
        .search-validation-panel { width:430px; }
        .search-validation-text { margin:0; font-size:13px; font-weight:400; }
    </style>
</head>
<body>
    <%@ include file="/WEB-INF/view/common/header.jspf" %>
    <main class="page-content">
        <div class="employee-list-page">
            <c:url var="currentEmployeeListUrl" value="/employees/employees.do"><c:param name="page" value="${condition.page}"/><c:param name="pageSize" value="${condition.pageSize}"/><c:param name="searchTarget" value="${condition.searchTarget}"/><c:param name="keyword" value="${condition.keyword}"/><c:param name="employmentType" value="${condition.employmentType}"/><c:param name="status" value="${condition.status}"/></c:url>
            <header class="page-heading"><div><p>人事管理</p><h1>社員状況/管理</h1></div></header>

            <%--  DBで集計した在職/退職および雇用形態別社員数 --%>
            <nav class="summary-grid" aria-label="社員状況のまとめ">
                <div class="summary-card summary-card--work"><span>在職者</span><strong><c:out value="${employeeSummary.workingCount}" default="0" /></strong></div>
                <div class="summary-card"><span>正規職</span><strong><c:out value="${employeeSummary.regularCount}" default="0" /></strong></div>
                <div class="summary-card"><span>契約会社</span><strong><c:out value="${employeeSummary.contractCount}" default="0" /></strong></div>
                <div class="summary-card"><span>臨時職</span><strong><c:out value="${employeeSummary.temporaryCount}" default="0" /></strong></div>
                <div class="summary-card"><span>派遣職</span><strong><c:out value="${employeeSummary.dispatchedCount}" default="0" /></strong></div>
                <div class="summary-card"><span>委任職</span><strong><c:out value="${employeeSummary.commissionedCount}" default="0" /></strong></div>
                <div class="summary-card"><span>日雇い</span><strong><c:out value="${employeeSummary.dailyCount}" default="0" /></strong></div>
                <div class="summary-card summary-card--retired"><span>退職者</span><strong><c:out value="${employeeSummary.retiredCount}" default="0" /></strong></div>
                <div class="summary-card summary-card--total"><span>全体</span><strong><c:out value="${employeeSummary.totalCount}" default="0" /></strong></div>
            </nav>

            <%-- JavaScriptなしでGETリクエストに検索、フィルタ、リスト数条件を渡す。 --%>
            <section class="employee-card">
                <form class="search-toolbar" action="${pageContext.request.contextPath}/employees/employees.do" method="get">
                    <div class="search-group"><select name="searchTarget" aria-label="検索項目"><option value="ALL" ${condition.searchTarget eq 'ALL' ? 'selected' : ''}>全体</option><option value="NAME" ${condition.searchTarget eq 'NAME' ? 'selected' : ''}>名前</option><option value="DEPARTMENT" ${condition.searchTarget eq 'DEPARTMENT' ? 'selected' : ''}>部署</option><option value="POSITION" ${condition.searchTarget eq 'POSITION' ? 'selected' : ''}>役職</option><option value="EMPLOYEE_NO" ${condition.searchTarget eq 'EMPLOYEE_NO' ? 'selected' : ''}>社員番号</option></select><input name="keyword" value="<c:out value='${condition.keyword}' />" placeholder="検索キーワードを入力"><button type="submit" name="mode" value="search">検索</button><a href="${pageContext.request.contextPath}/employees/employees.do">全体を見る</a></div>
                    <div class="filter-group"><select name="employmentType" aria-label="雇用形態"><option value="">雇用形態別</option><c:forEach var="type" items="${employmentTypes}"><option value="${type}" ${condition.employmentType eq type ? 'selected' : ''}><ui:code-label value="${type}" /></option></c:forEach></select><select name="status" aria-label="ステータス"><option value="" ${empty condition.status ? 'selected' : ''}>ステータス別</option><option value="WORK" ${condition.status eq 'WORK' ? 'selected' : ''}>在職</option><option value="RETIRED" ${condition.status eq 'RETIRED' ? 'selected' : ''}>退職</option></select><select name="pageSize" aria-label="リスト数"><option value="">リスト数</option><option value="10" ${condition.pageSize eq 10 ? 'selected' : ''}>10個ずつ見る</option><option value="30" ${condition.pageSize eq 30 ? 'selected' : ''}>30個ずつ見る</option><option value="50" ${condition.pageSize eq 50 ? 'selected' : ''}> 50個ずつ見る</option><option value="100" ${condition.pageSize eq 100 ? 'selected' : ''}>100個ずつ見る</option></select><button type="submit" name="mode" value="filter">適用</button></div>
                    <div class="setting-buttons"><a href="#column-modal">表示項目の設定</a></div>
                </form>

                <%--  EmployeeDaoのJOIN結果をJSTLに繰り返し出力する。 --%>
                <form action="${pageContext.request.contextPath}/employees/employee-action.do" method="post">
                    <input type="hidden" name="searchTarget" value="<c:out value='${condition.searchTarget}' />">
                    <input type="hidden" name="keyword" value="<c:out value='${condition.keyword}' />">
                    <input type="hidden" name="employmentType" value="<c:out value='${condition.employmentType}' />">
                    <input type="hidden" name="status" value="<c:out value='${condition.status}' />">
                    <input type="hidden" name="page" value="${condition.page}">
                    <input type="hidden" name="pageSize" value="${condition.pageSize}">
                    <div class="employee-table-wrap"><table class="employee-table"><thead><tr><th>選択</th><th>区分</th><th>入社日</th><th>社員番号</th><th>氏名</th><th>氏名（英語）</th><th>部署</th><th>役職</th><th>住民番号</th><th>内・外国人</th><th>住所</th><th>電話番号</th><th>携帯電話</th><th>メール</th><th>SNS</th><th>退社日</th><th>ステータス</th><th>銀行口座</th></tr></thead><tbody>
                        <c:forEach var="employee" items="${employees}"><c:url var="employeeEditUrl" value="/settings/register1.do"><c:param name="empId" value="${employee.employeeId}" /></c:url><tr><td><input type="checkbox" name="employeeIds" value="${employee.employeeId}" aria-label="<c:out value='${employee.name}' /> 選択"></td><td><a class="employee-row-link" href="${employeeEditUrl}"><ui:code-label value="${employee.employmentType}" /></a></td><td><a class="employee-row-link" href="${employeeEditUrl}"><c:out value="${employee.joinDate}" /></a></td><td><a class="employee-row-link" href="${employeeEditUrl}"><c:out value="${employee.employeeNo}" /></a></td><td><a class="employee-row-link" href="${employeeEditUrl}"><c:out value="${employee.name}" /></a></td><td><a class="employee-row-link" href="${employeeEditUrl}"><c:out value="${employee.englishName}" /></a></td><td><a class="employee-row-link" href="${employeeEditUrl}"><c:out value="${employee.departmentName}" /></a></td><td><a class="employee-row-link" href="${employeeEditUrl}"><c:out value="${employee.positionName}" /></a></td><td><a class="employee-row-link" href="${employeeEditUrl}"><c:out value="${employee.maskedResidentNo}" /></a></td><td><a class="employee-row-link" href="${employeeEditUrl}"><ui:code-label value="${employee.nationalityType}" /></a></td><td class="long-text"><a class="employee-row-link" href="${employeeEditUrl}"><c:out value="${employee.address}" /></a></td><td><a class="employee-row-link" href="${employeeEditUrl}"><c:out value="${employee.phone}" /></a></td><td><a class="employee-row-link" href="${employeeEditUrl}"><c:out value="${employee.mobile}" /></a></td><td class="email"><a class="employee-row-link" href="${employeeEditUrl}"><c:out value="${employee.email}" /></a></td><td><a class="employee-row-link" href="${employeeEditUrl}"><c:out value="${employee.sns}" /></a></td><td><a class="employee-row-link" href="${employeeEditUrl}"><c:out value="${employee.retirementDate}" /></a></td><td><a class="employee-row-link" href="${employeeEditUrl}"><span class="status-badge status-badge--${employee.status eq 'WORK' ? 'work' : 'retired'}">${employee.status eq 'WORK' ? '在職' : '退職'}</span></a></td><td><a class="employee-row-link" href="${employeeEditUrl}"><c:out value="${employee.bankAccount}" /></a></td></tr></c:forEach>
                        <c:if test="${empty employees}"><tr><td colspan="18" class="empty-row">検索された社員はありません。</td></tr></c:if>
                    </tbody></table></div>
                    <div class="pagination">
                        <c:if test="${pageInfo.hasPrevious}"><c:url var="previousUrl" value="/employees/employees.do"><c:param name="page" value="${pageInfo.previousPage}"/><c:param name="pageSize" value="${condition.pageSize}"/><c:param name="searchTarget" value="${condition.searchTarget}"/><c:param name="keyword" value="${condition.keyword}"/><c:param name="employmentType" value="${condition.employmentType}"/><c:param name="status" value="${condition.status}"/></c:url><a href="${previousUrl}">以前</a></c:if>
                        <c:forEach var="pageNo" begin="${pageInfo.startPage}" end="${pageInfo.endPage}"><c:url var="pageUrl" value="/employees/employees.do"><c:param name="page" value="${pageNo}"/><c:param name="pageSize" value="${condition.pageSize}"/><c:param name="searchTarget" value="${condition.searchTarget}"/><c:param name="keyword" value="${condition.keyword}"/><c:param name="employmentType" value="${condition.employmentType}"/><c:param name="status" value="${condition.status}"/></c:url><a class="${pageNo eq pageInfo.currentPage ? 'is-current' : ''}" href="${pageUrl}">${pageNo}</a></c:forEach>
                        <c:if test="${pageInfo.hasNext}"><c:url var="nextUrl" value="/employees/employees.do"><c:param name="page" value="${pageInfo.nextPage}"/><c:param name="pageSize" value="${condition.pageSize}"/><c:param name="searchTarget" value="${condition.searchTarget}"/><c:param name="keyword" value="${condition.keyword}"/><c:param name="employmentType" value="${condition.employmentType}"/><c:param name="status" value="${condition.status}"/></c:url><a href="${nextUrl}">次へ</a></c:if>
                    </div>
                    <div class="bottom-actions"><a class="primary" href="${pageContext.request.contextPath}/settings/register1.do">新規社員登録</a><button class="danger" name="action" value="requestDelete">選択を削除</button></div>
                </form>
            </section>

            <c:if test="${deleteConfirmation}">
                <div class="settings-modal" style="display:flex" role="dialog" aria-modal="true" aria-labelledby="delete-confirmation-title">
                    <a class="modal-backdrop" href="${pageContext.request.contextPath}/employees/employees.do" aria-label="削除のキャンセル"></a>
                    <form class="modal-panel delete-confirmation-panel" action="${pageContext.request.contextPath}/employees/employee-action.do" method="post">
                        <div class="modal-title"><h2 id="delete-confirmation-title">社員情報の削除</h2><a href="${pageContext.request.contextPath}/employees/employees.do">×</a></div>
                        <p class="delete-confirmation-text"><span>選択した <strong><c:out value="${deleteEmployeeCount}" />人</strong>の社員情報を削除してもよろしいですか？</span><span>給与・諸証明書・退職給付資料も同時に完全削除されます。</span><span class="delete-confirmation-warning">削除した社員情報は復元できません。</span></p>
                        <c:forEach var="employeeId" items="${deleteEmployeeIds}"><input type="hidden" name="employeeIds" value="${employeeId}"></c:forEach>
                        <div class="delete-confirmation-actions"><button type="submit" name="action" value="confirmDelete">削除</button><a href="${pageContext.request.contextPath}/employees/employees.do">キャンセル</a></div>
                    </form>
                </div>
            </c:if>

            <c:if test="${not empty searchValidationMessage}">
                <div class="settings-modal" style="display:flex" role="alertdialog" aria-modal="true" aria-labelledby="search-validation-title">
                    <a class="modal-backdrop" href="${pageContext.request.contextPath}/employees/employees.do" aria-label="閉じる"></a>
                    <div class="modal-panel delete-confirmation-panel search-validation-panel">
                        <p id="search-validation-title" class="delete-confirmation-text search-validation-text"><span><ui:message-label value="${searchValidationMessage}" /></span></p>
                        <div class="delete-confirmation-actions search-validation-actions"><a href="${pageContext.request.contextPath}/employees/employees.do">確認</a></div>
                    </div>
                </div>
            </c:if>

            <c:if test="${not empty message}">
                <div class="settings-modal" style="display:flex" role="alertdialog" aria-modal="true" aria-labelledby="employee-list-message">
                    <a class="modal-backdrop" href="${currentEmployeeListUrl}" aria-label="閉じる"></a>
                    <div class="modal-panel delete-confirmation-panel search-validation-panel">
                        <p id="employee-list-message" class="delete-confirmation-text search-validation-text"><span><ui:message-label value="${message}" /></span></p>
                        <div class="delete-confirmation-actions search-validation-actions"><a href="${currentEmployeeListUrl}">確認</a></div>
                    </div>
                </div>
            </c:if>

            <div id="column-modal" class="settings-modal" role="dialog" aria-modal="true"><a class="modal-backdrop" href="${currentEmployeeListUrl}" aria-label="表示項目の設定をキャンセル"></a><form class="modal-panel column-panel" action="${pageContext.request.contextPath}/employees/employee-columns.do" method="post"><div class="modal-title"><h2>表示項目の設定</h2><a href="${currentEmployeeListUrl}" aria-label="表示項目の設定をキャンセル">×</a></div><div class="column-options">
                <label><input id="column-type" type="checkbox" name="columns" value="employmentType" ${visibleColumns.contains('employmentType') ? 'checked' : ''}> 区分</label><label><input id="column-join-date" type="checkbox" name="columns" value="joinDate" ${visibleColumns.contains('joinDate') ? 'checked' : ''}> 入社日</label><label><input id="column-number" type="checkbox" name="columns" value="employeeNo" ${visibleColumns.contains('employeeNo') ? 'checked' : ''}> 社員番号</label><label><input id="column-name" type="checkbox" name="columns" value="name" ${visibleColumns.contains('name') ? 'checked' : ''}> 氏名</label><label><input id="column-english-name" type="checkbox" name="columns" value="englishName" ${visibleColumns.contains('englishName') ? 'checked' : ''}> 氏名（英語）</label><label><input id="column-department" type="checkbox" name="columns" value="department" ${visibleColumns.contains('department') ? 'checked' : ''}> 部署</label><label><input id="column-position" type="checkbox" name="columns" value="position" ${visibleColumns.contains('position') ? 'checked' : ''}> 役職</label><label><input id="column-resident" type="checkbox" name="columns" value="residentNo" ${visibleColumns.contains('residentNo') ? 'checked' : ''}> 住民番号</label><label><input id="column-nationality" type="checkbox" name="columns" value="nationalityType" ${visibleColumns.contains('nationalityType') ? 'checked' : ''}> 内・外国人</label><label><input id="column-address" type="checkbox" name="columns" value="address" ${visibleColumns.contains('address') ? 'checked' : ''}> 住所</label><label><input id="column-phone" type="checkbox" name="columns" value="phone" ${visibleColumns.contains('phone') ? 'checked' : ''}> 電話番号</label><label><input id="column-mobile" type="checkbox" name="columns" value="mobile" ${visibleColumns.contains('mobile') ? 'checked' : ''}> 携帯電話</label><label><input id="column-email" type="checkbox" name="columns" value="email" ${visibleColumns.contains('email') ? 'checked' : ''}> メール</label><label><input id="column-sns" type="checkbox" name="columns" value="sns" ${visibleColumns.contains('sns') ? 'checked' : ''}> SNS</label><label><input id="column-retirement" type="checkbox" name="columns" value="retirementDate" ${visibleColumns.contains('retirementDate') ? 'checked' : ''}> 退社日</label><label><input id="column-status" type="checkbox" name="columns" value="status" ${visibleColumns.contains('status') ? 'checked' : ''}> ステータス</label><label><input id="column-account" type="checkbox" name="columns" value="bankAccount" ${visibleColumns.contains('bankAccount') ? 'checked' : ''}> 銀行口座</label>
            </div><div class="modal-actions"><button>表示項目を保存</button></div></form></div>
        </div>
    </main>
    <%@ include file="/WEB-INF/view/common/footer.jspf" %>
</body>
</html>
