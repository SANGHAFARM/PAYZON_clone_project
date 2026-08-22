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
    <title>給与台帳</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/payroll/payroll-register.css?v=20260815-1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/payzon-ui.css?v=20260821-1">
</head>
<body>
    <%@ include file="/WEB-INF/view/common/header.jspf" %>
    <main class="page-content payment-register-page">
        <header class="page-heading"><div><p>給与管理</p><h1>給与台帳</h1></div></header>

        <section class="register-card">
            <form class="register-search" method="get" action="${pageContext.request.contextPath}/payroll/register.do">
                <label for="registerYear">帰属年</label>
                <select id="registerYear" name="year">
                    <option value="">選択</option>
                    <c:forEach var="year" items="${paymentYears}"><option value="${year}" <c:if test="${year eq selectedYear}">selected</c:if>>${year}年</option></c:forEach>
                </select>
                <button type="submit" class="button button-primary">照会</button>
            </form>

            <div class="table-wrap">
                <table class="register-table">
                    <colgroup><col class="month-col"><col class="round-col"><col class="period-col"><col class="date-col"><col class="count-col"><col class="money-col"><col class="money-col"><col class="money-col"><col class="delete-col"></colgroup>
                    <thead><tr><th>帰属年月</th><th>給与回次</th><th>精算期間</th><th>支給日</th><th>人数</th><th>支給総額</th><th>控除総額</th><th>差引支給額</th><th>削除</th></tr></thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${not empty paymentRegisters}">
                            <c:forEach var="register" items="${paymentRegisters}">
                                <tr>
                                    <td><a href="${pageContext.request.contextPath}/payroll/register/detail.do?registerId=${register.registerId}">${register.paymentYearMonth}</a></td>
                                    <td><a href="${pageContext.request.contextPath}/payroll/register/detail.do?registerId=${register.registerId}"><ui:code-label value="${register.paymentRoundName}" /></a></td>
                                    <td>${register.calculationStart} ~ ${register.calculationEnd}</td><td>${register.paymentDate}</td><td>${register.employeeCount}</td>
                                    <td class="amount give">${register.totalPayment}</td><td class="amount deduction">${register.totalDeduction}</td><td class="amount">${register.netPayment}</td>
                                    <td><form method="post" action="${pageContext.request.contextPath}/payroll/register/delete.do"><input type="hidden" name="registerId" value="${register.registerId}"><input type="hidden" name="registerName" value="${register.paymentYearMonth} ${register.paymentRoundName}"><input type="hidden" name="year" value="${selectedYear}"><button type="submit" name="action" value="requestDelete" class="table-button">削除</button></form></td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise><tr><td colspan="9" class="empty-row">照会された給与台帳はありません。</td></tr></c:otherwise>
                    </c:choose>
                    </tbody>
                    <c:if test="${not empty paymentRegisters}"><tfoot><tr><th>合計</th><td colspan="4"></td><td class="amount give">${registerTotals.totalPayment}</td><td class="amount deduction">${registerTotals.totalDeduction}</td><td class="amount">${registerTotals.netPayment}</td><td></td></tr></tfoot></c:if>
                </table>
            </div>
            <c:if test="${page.totalPages gt 0}"><nav class="pagination" aria-label="ページ移動"><c:if test="${page.number gt 0}"><a href="?year=${selectedYear}&amp;page=${page.number - 1}">以前</a></c:if><c:forEach var="index" begin="0" end="${page.totalPages - 1}"><a class="${index eq page.number ? 'active' : ''}" href="?year=${selectedYear}&amp;page=${index}">${index + 1}</a></c:forEach><c:if test="${page.number + 1 lt page.totalPages}"><a href="?year=${selectedYear}&amp;page=${page.number + 1}">次へ</a></c:if></nav></c:if>
        </section>
    </main>

    <c:if test="${deleteConfirmation}">
        <div class="register-delete-alert" role="alertdialog" aria-modal="true"
            aria-labelledby="register-delete-message">
            <a class="register-delete-alert__backdrop"
                href="${pageContext.request.contextPath}/payroll/register.do?year=${selectedYear}"
                aria-label="削除のキャンセル"></a>
            <form class="register-delete-alert__panel" method="post"
                action="${pageContext.request.contextPath}/payroll/register/delete.do">
                <p id="register-delete-message">
                    <strong><c:out value="${deleteRegisterName}" /></strong> 給与台帳を削除してもよろしいですか？
                </p>
                <p class="register-delete-alert__warning">削除した給与台帳は復元できません。</p>
                <input type="hidden" name="registerId" value="${deleteRegisterId}">
                <input type="hidden" name="year" value="${selectedYear}">
                <div class="register-delete-alert__actions">
                    <button type="submit" name="action" value="confirmDelete">削除</button>
                    <a href="${pageContext.request.contextPath}/payroll/register.do?year=${selectedYear}">キャンセル</a>
                </div>
            </form>
        </div>
    </c:if>
    <%@ include file="/WEB-INF/view/common/footer.jspf" %>
</body>
</html>
