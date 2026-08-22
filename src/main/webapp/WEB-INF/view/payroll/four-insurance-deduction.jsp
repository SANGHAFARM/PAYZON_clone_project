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
    <title> 4大保険控除履歴</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/payzon-ui.css?v=20260821-1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/payroll/four-insurance-deduction.css">
</head>
<body>
    <%@ include file="/WEB-INF/view/common/header.jspf" %>
    <main class="page-content four-insurance-page">
        <header class="page-heading"><div><p>給与管理</p><h1> 4大保険控除履歴</h1></div></header>
        <section class="insurance-card">
            <form class="insurance-search" method="get" action="${pageContext.request.contextPath}/payroll/four-insurance.do">
                <div class="search-field year-field"><label for="paymentYear">帰属年</label><select id="paymentYear" name="year"><option value="">選択</option><c:forEach var="year" items="${paymentYears}"><option value="${year}" <c:if test="${year eq selectedYear}">selected</c:if>>${year}年</option></c:forEach></select></div>
                <div class="search-field month-field"><label for="paymentMonth">帰属月</label><select id="paymentMonth" name="month"><option value="">選択</option><c:forEach var="month" begin="1" end="12"><option value="${month}" <c:if test="${month eq selectedMonth}">selected</c:if>>${month}月</option></c:forEach></select></div>
                <div class="search-field round-field"><label for="paymentRound">給与回数</label><select id="paymentRound" name="round"><option value="">回目を選択</option><c:forEach var="round" begin="1" end="10"><option value="${round}" <c:if test="${round eq selectedRound}">selected</c:if>>${round}給与回目</option></c:forEach></select></div>
                <button type="submit" class="button button-primary">控除履歴の照会</button>
                <div class="period-information"><div><strong>精算期間</strong><span><c:choose><c:when test="${not empty calculationStart and not empty calculationEnd}">${calculationStart} ~ ${calculationEnd}</c:when><c:otherwise>-</c:otherwise></c:choose></span></div><div><strong>給与支給日</strong><span>${empty paymentDate ? '-' : paymentDate}</span></div></div>
            </form>
            <div class="insurance-table-wrap">
                <table class="insurance-table">
                    <colgroup><col class="type-col"><col class="name-col"><col class="department-col"><col class="position-col"><c:forEach begin="1" end="15"><col class="money-col"></c:forEach></colgroup>
                    <thead>
                        <tr><th colspan="4" class="employee-group">社員情報</th><th colspan="3" class="insurance-group pension-group">国民年金</th><th colspan="3" class="insurance-group health-group">健康保険</th><th colspan="3" class="insurance-group care-group">長期療養保険</th><th colspan="3" class="insurance-group employment-group">雇用保険</th><th colspan="3" class="insurance-group total-group">総合計</th></tr>
                        <tr><th>区分</th><th>氏名</th><th>部署</th><th>役職</th><c:forEach begin="1" end="5"><th>事業主</th><th>労働者</th><th class="subtotal-header">合計</th></c:forEach></tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${not empty insuranceDeductions}">
                            <c:forEach var="row" items="${insuranceDeductions}"><tr><td><ui:code-label value="${row.employmentTypeName}" /></td><td>${row.employeeName}</td><td>${row.departmentName}</td><td>${row.positionName}</td><td class="amount">${row.pensionEmployer}</td><td class="amount">${row.pensionEmployee}</td><td class="amount subtotal">${row.pensionTotal}</td><td class="amount">${row.healthEmployer}</td><td class="amount">${row.healthEmployee}</td><td class="amount subtotal">${row.healthTotal}</td><td class="amount">${row.careEmployer}</td><td class="amount">${row.careEmployee}</td><td class="amount subtotal">${row.careTotal}</td><td class="amount">${row.employmentEmployer}</td><td class="amount">${row.employmentEmployee}</td><td class="amount subtotal">${row.employmentTotal}</td><td class="amount total-value">${row.totalEmployer}</td><td class="amount total-value">${row.totalEmployee}</td><td class="amount grand-value">${row.grandTotal}</td></tr></c:forEach>
                        </c:when>
                        <c:otherwise><tr><td colspan="19" class="empty-row">照会された4大保険控除履歴はありません。</td></tr></c:otherwise>
                    </c:choose>
                    </tbody>
                    <c:if test="${not empty insuranceDeductions}"><tfoot><tr><th colspan="4">合計</th><td class="amount">${insuranceTotals.pensionEmployer}</td><td class="amount">${insuranceTotals.pensionEmployee}</td><td class="amount subtotal">${insuranceTotals.pensionTotal}</td><td class="amount">${insuranceTotals.healthEmployer}</td><td class="amount">${insuranceTotals.healthEmployee}</td><td class="amount subtotal">${insuranceTotals.healthTotal}</td><td class="amount">${insuranceTotals.careEmployer}</td><td class="amount">${insuranceTotals.careEmployee}</td><td class="amount subtotal">${insuranceTotals.careTotal}</td><td class="amount">${insuranceTotals.employmentEmployer}</td><td class="amount">${insuranceTotals.employmentEmployee}</td><td class="amount subtotal">${insuranceTotals.employmentTotal}</td><td class="amount total-value">${insuranceTotals.totalEmployer}</td><td class="amount total-value">${insuranceTotals.totalEmployee}</td><td class="amount grand-value">${insuranceTotals.grandTotal}</td></tr></tfoot></c:if>
                </table>
            </div>
            <div class="insurance-actions"><a class="button button-outline" href="${pageContext.request.contextPath}/payroll/register.do">給与台帳リスト</a></div>
        </section>
    </main>
    <%@ include file="/WEB-INF/view/common/footer.jspf" %>
</body>
</html>
