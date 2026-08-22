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
    <title>社員別給与履歴</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/payroll/employees-payroll-history.css?v=20260815-1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/payzon-ui.css?v=20260821-1">
</head>
<body>
    <%@ include file="/WEB-INF/view/common/header.jspf" %>
    <main class="page-content employee-payment-history-page">
        <header class="page-heading"><div><p>給与管理</p><h1>社員別給与履歴</h1></div></header>
        <section class="history-card">
            <form class="history-search" method="get" action="${pageContext.request.contextPath}/payroll/employee-history.do">
                <div class="search-field employee-field">
                    <label for="employeeName">社員を選択</label>
                    <div class="employee-selector"><input type="hidden" name="employeeId" value="${selectedEmployee.employeeId}"><input id="employeeName" type="text" value="${selectedEmployee.employeeName}" placeholder="社員を選択してください" readonly><a href="#employeeSelectModal" class="button button-outline">社員を選択</a><button type="submit" name="mode" value="history" class="button button-primary history-submit">給与履歴の照会</button></div>
                </div>
                <div class="period-search-group"><div class="search-field period-field"><label for="startMonth">期間選択</label><div class="month-range"><input id="startMonth" name="startMonth" type="month" value="${startMonth}"><span>~</span><input id="endMonth" name="endMonth" type="month" value="${endMonth}"></div></div><button type="submit" name="mode" value="history" class="button button-primary period-submit">照会</button></div>
            </form>
            <div class="history-table-wrap">
                <table class="history-table">
                    <colgroup><col class="month-col"><col><col><col><col><col><col><col><col><col><col></colgroup>
                    <thead>
                        <tr><th colspan="5" class="group-title payment-group">月別給与履歴</th><th colspan="6" class="group-title deduction-group">四大保険・給与所得税内訳</th></tr>
                        <tr><th>給与月（回数）</th><th>報酬月額</th><th>支給合計</th><th>控除合計</th><th>差引支給額</th><th>国民年金</th><th>健康保険</th><th>長期療養保険</th><th>雇用保険</th><th>所得税</th><th>住民税</th></tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${not empty paymentHistories}">
                            <c:forEach var="history" items="${paymentHistories}"><tr><td>${history.paymentMonth}(${history.paymentRound})</td><td class="amount">${history.standardMonthlyIncome}</td><td class="amount payment-amount">${history.totalPayment}</td><td class="amount deduction-amount">${history.totalDeduction}</td><td class="amount net-amount">${history.netPayment}</td><td class="amount">${history.nationalPension}</td><td class="amount">${history.healthInsurance}</td><td class="amount">${history.longTermCareInsurance}</td><td class="amount">${history.employmentInsurance}</td><td class="amount">${history.incomeTax}</td><td class="amount">${history.localIncomeTax}</td></tr></c:forEach>
                        </c:when>
                        <c:otherwise><tr><td colspan="11" class="empty-row">照会された社員別給与履歴はありません。</td></tr></c:otherwise>
                    </c:choose>
                    </tbody>
                    <c:if test="${not empty paymentHistories}"><tfoot><tr><th>合計</th><td class="amount">${paymentHistoryTotal.standardMonthlyIncome}</td><td class="amount payment-amount">${paymentHistoryTotal.totalPayment}</td><td class="amount deduction-amount">${paymentHistoryTotal.totalDeduction}</td><td class="amount net-amount">${paymentHistoryTotal.netPayment}</td><td class="amount">${paymentHistoryTotal.nationalPension}</td><td class="amount">${paymentHistoryTotal.healthInsurance}</td><td class="amount">${paymentHistoryTotal.longTermCareInsurance}</td><td class="amount">${paymentHistoryTotal.employmentInsurance}</td><td class="amount">${paymentHistoryTotal.incomeTax}</td><td class="amount">${paymentHistoryTotal.localIncomeTax}</td></tr></tfoot></c:if>
                </table>
            </div>
            <nav class="pagination" aria-label="給与履歴ページ">
                <c:if test="${not empty pageInfo}">
                <c:if test="${pageInfo.hasPrevious}"><a href="?mode=history&amp;page=${pageInfo.previousPage}&amp;startMonth=${startMonth}&amp;endMonth=${endMonth}&amp;employeeId=${selectedEmployee.employeeId}">以前</a></c:if>
                <c:forEach var="pageNumber" begin="${pageInfo.startPage}" end="${pageInfo.endPage}"><a class="${pageNumber eq pageInfo.currentPage ? 'active' : ''}" href="?mode=history&amp;page=${pageNumber}&amp;startMonth=${startMonth}&amp;endMonth=${endMonth}&amp;employeeId=${selectedEmployee.employeeId}">${pageNumber}</a></c:forEach>
                <c:if test="${pageInfo.hasNext}"><a href="?mode=history&amp;page=${pageInfo.nextPage}&amp;startMonth=${startMonth}&amp;endMonth=${endMonth}&amp;employeeId=${selectedEmployee.employeeId}">次へ</a></c:if>
                </c:if>
            </nav>
            <div class="history-actions"><a class="button button-outline" href="${pageContext.request.contextPath}/payroll/register.do">給与台帳リスト</a></div>
        </section>
    </main>
    <section id="employeeSelectModal" class="modal-layer" role="dialog" aria-modal="true" aria-labelledby="employeeModalTitle">
        <a href="#" class="modal-backdrop" aria-label="ポップアップを閉じる"></a>
        <div class="modal-dialog employee-modal">
            <header class="modal-header"><h2 id="employeeModalTitle">給与履歴照会社員を選択</h2><a href="#" class="modal-close" aria-label="閉じる">×</a></header>
            <div class="modal-body">
                <form class="modal-search" method="get" action="${pageContext.request.contextPath}/payroll/employee-history.do#employeeSelectModal">
                    <input type="hidden" name="employeeId" value="${selectedEmployee.employeeId}">
                    <input type="hidden" name="startMonth" value="${startMonth}">
                    <input type="hidden" name="endMonth" value="${endMonth}">
                    <input type="search" name="employeeKeyword" value="${param.employeeKeyword}" placeholder="社員検索"><button type="submit" class="button button-primary">検索</button>
                    <div class="modal-filters"><select name="departmentId"><option value="">部署別</option><c:forEach var="department" items="${departments}"><option value="${department.departmentId}" ${param.departmentId eq department.departmentId ? 'selected' : ''}>${department.departmentName}</option></c:forEach></select><select name="status"><option value="">ステータス別</option><option value="ACTIVE" ${param.status eq 'ACTIVE' ? 'selected' : ''}>在職</option><option value="RETIRED" ${param.status eq 'RETIRED' ? 'selected' : ''}>退職</option></select></div>
                </form>
                <form class="employee-selection-form" method="get" action="${pageContext.request.contextPath}/payroll/employee-history.do">
                <input type="hidden" name="mode" value="select">
                <input type="hidden" name="startMonth" value="${startMonth}"><input type="hidden" name="endMonth" value="${endMonth}">
                <div class="modal-table-wrap"><table class="employee-select-table"><thead><tr><th>選択</th><th>区分</th><th>社員番号</th><th>氏名</th><th>部署</th><th>役職</th><th>ステータス</th></tr></thead><tbody>
                <c:choose><c:when test="${not empty employees}"><c:forEach var="employee" items="${employees}"><tr><td><input type="radio" name="employeeId" value="${employee.employeeId}" aria-label="${employee.employeeName}を選択"></td><td><ui:code-label value="${employee.employmentTypeName}" /></td><td>${employee.employeeNumber}</td><td>${employee.employeeName}</td><td>${employee.departmentName}</td><td>${employee.positionName}</td><td class="${employee.status eq '퇴직' ? 'retired' : ''}"><ui:code-label value="${employee.status}" /></td></tr></c:forEach></c:when><c:otherwise><tr><td colspan="7" class="empty-row">照会された社員はありません。</td></tr></c:otherwise></c:choose>
                </tbody></table></div>
                <footer class="modal-actions"><button type="submit" class="button button-primary">社員を選択</button><a href="#" class="button button-secondary">選択解除</a></footer>
                </form>
            </div>
        </div>
    </section>
    <c:if test="${not empty historyPopupMessage}">
        <c:url var="historyReturnUrl" value="/payroll/employee-history.do"><c:param name="startMonth" value="${startMonth}"/><c:param name="endMonth" value="${endMonth}"/></c:url>
        <div class="history-alert" role="alertdialog" aria-modal="true" aria-labelledby="history-alert-message"><a class="history-alert__backdrop" href="${historyReturnUrl}" aria-label="閉じる"></a><div class="history-alert__panel"><p id="history-alert-message"><ui:message-label value="${historyPopupMessage}" /></p><a href="${historyReturnUrl}">確認</a></div></div>
    </c:if>
    <%@ include file="/WEB-INF/view/common/footer.jspf" %>
</body>
</html>
