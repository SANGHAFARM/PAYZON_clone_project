<%-- 조회 결과와 입력 양식을 표시하는 JSP 화면이다. --%>
<%-- 照会結果と入力フォームを表示するJSP画面である。 --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="detailColumnCount" value="${7 + fn:length(paymentItems) + fn:length(deductionItems)}" />
<!DOCTYPE html>
<html lang="ja-JP">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>給与台帳詳細</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/payzon-ui.css?v=20260821-1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/payroll/payroll-register.css">
</head>
<body>
    <%@ include file="/WEB-INF/view/common/header.jspf" %>
    <main class="page-content payment-register-page">
        <header class="page-heading"><div><p>給与管理</p><h1>給与台帳詳細</h1></div></header>
        <section class="register-card detail-card">
            <form class="detail-topbar" method="get" action="${pageContext.request.contextPath}/payroll/register/detail.do">
                <input type="hidden" name="registerId" value="${register.registerId}">
                <dl class="register-information"><div><dt>帰属年月</dt><dd>${empty register.paymentYearMonth ? '-' : register.paymentYearMonth}</dd></div><div><dt>給与回次</dt><dd><c:choose><c:when test="${not empty register.paymentRoundName}"><ui:code-label value="${register.paymentRoundName}" /></c:when><c:otherwise>-</c:otherwise></c:choose></dd></div><div><dt>精算期間</dt><dd><c:choose><c:when test="${not empty register.calculationStart and not empty register.calculationEnd}">${register.calculationStart} ~ ${register.calculationEnd}</c:when><c:otherwise>-</c:otherwise></c:choose></dd></div><div><dt>支給日</dt><dd>${empty register.paymentDate ? '-' : register.paymentDate}</dd></div></dl>
                <div class="detail-filter"><select name="employmentType"><option value="">全区分</option><c:forEach var="type" items="${employmentTypes}"><option value="${type.code}" <c:if test="${type.code eq selectedEmploymentType}">selected</c:if>>${type.name}</option></c:forEach></select><select name="departmentId"><option value="">全体部署</option><c:forEach var="department" items="${departments}"><option value="${department.departmentId}" <c:if test="${department.departmentId eq selectedDepartmentId}">selected</c:if>>${department.departmentName}</option></c:forEach></select><select name="incomeType"><option value="">全所得区分</option><option value="WORK" <c:if test="${selectedIncomeType eq 'WORK'}">selected</c:if>>給与所得者</option><option value="BUSINESS" <c:if test="${selectedIncomeType eq 'BUSINESS'}">selected</c:if>>事業所得者</option><option value="DAILY" <c:if test="${selectedIncomeType eq 'DAILY'}">selected</c:if>>日雇い労働者</option></select><button type="submit" class="button button-primary">照会</button></div>
            </form>

            <div class="detail-table-wrap">
                <table class="detail-table">
                    <thead><tr>
                        <th class="fixed type">区分</th><th class="fixed name">氏名</th><th class="fixed department">部署</th><th class="fixed position">役職</th>
                        <c:forEach var="item" items="${paymentItems}"><th class="give-item"><ui:code-label value="${item.itemName}" /></th></c:forEach>
                        <th class="give-total">支給総額</th>
                        <c:forEach var="item" items="${deductionItems}"><th class="deduction-item"><ui:code-label value="${item.itemName}" /></th></c:forEach>
                        <th class="deduction-total">控除総額</th><th class="net-total">差引支給額</th>
                    </tr></thead>
                    <tbody><c:choose><c:when test="${not empty registerEmployees}"><c:forEach var="employee" items="${registerEmployees}"><tr><td><ui:code-label value="${employee.employmentTypeName}" /></td><td>${employee.employeeName}</td><td>${employee.departmentName}</td><td>${employee.positionName}</td><c:forEach var="item" items="${paymentItems}"><td>${employee.paymentAmounts[item.itemId]}</td></c:forEach><td class="give">${employee.totalPayment}</td><c:forEach var="item" items="${deductionItems}"><td>${employee.deductionAmounts[item.itemId]}</td></c:forEach><td class="deduction">${employee.totalDeduction}</td><td class="net">${employee.netPayment}</td></tr></c:forEach></c:when><c:otherwise><tr><td colspan="${detailColumnCount}" class="empty-row">照会された社員別給与履歴はありません。</td></tr></c:otherwise></c:choose></tbody>
                    <c:if test="${not empty registerEmployees}"><tfoot><tr><th colspan="4">合計</th><c:forEach var="item" items="${paymentItems}"><td>${registerTotals.paymentAmounts[item.itemId]}</td></c:forEach><td class="give">${registerTotals.totalPayment}</td><c:forEach var="item" items="${deductionItems}"><td>${registerTotals.deductionAmounts[item.itemId]}</td></c:forEach><td class="deduction">${registerTotals.totalDeduction}</td><td class="net">${registerTotals.netPayment}</td></tr></tfoot></c:if>
                </table>
            </div>
            <div class="detail-actions"><a class="button button-outline" href="${pageContext.request.contextPath}/payroll/register.do?year=${register.paymentYear}">給与台帳リスト</a></div>
        </section>
    </main>
    <%@ include file="/WEB-INF/view/common/footer.jspf" %>
</body>
</html>
