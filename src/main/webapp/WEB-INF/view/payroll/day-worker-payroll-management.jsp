<%-- 조회 결과와 입력 양식을 표시하는 JSP 화면이다. --%>
<%-- 照会結果と入力フォームを表示するJSP画面である。 --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>

<c:set var="employeePage"
    value="${empty param.employeePage ? 1 : param.employeePage}" />
<c:set var="employeeTotalPages"
    value="${empty availableEmployeePage.totalPages ? 1 : availableEmployeePage.totalPages}" />

<!DOCTYPE html>
<html lang="ja-JP">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>給与入力/管理（日雇い）</title>

    <link rel="stylesheet"
        href="${pageContext.request.contextPath}/css/common/common.css">
    <link rel="stylesheet"
        href="${pageContext.request.contextPath}/css/common/payzon-ui.css?v=20260821-1">
    <link rel="stylesheet"
        href="${pageContext.request.contextPath}/css/payroll/payroll-management.css">
    <link rel="stylesheet"
        href="${pageContext.request.contextPath}/css/payroll/day-worker-payroll-management.css?v=20260815-4">
</head>

<body>
    <%@ include file="/WEB-INF/view/common/header.jspf" %>

    <main class="page-content payment-management-page day-worker-payment-page">
        <header class="page-heading">
            <div>
                <p>給与管理</p>
                <h1>給与入力/管理（日雇い）</h1>
            </div>
        </header>

        <form class="period-panel"
            method="get"
            action="${pageContext.request.contextPath}/payroll/day-worker-management.do">

            <div class="period-field">
                <label for="paymentYear">帰属年</label>
                <select id="paymentYear" name="paymentYear">
                    <c:forEach var="year" items="${paymentYears}">
                        <option value="${year}"
                            <c:if test="${year eq selectedYear}">selected</c:if>>
                            ${year}年
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="period-field">
                <label for="paymentMonth">帰属月</label>
                <select id="paymentMonth" name="paymentMonth">
                    <c:forEach var="month" begin="1" end="12">
                        <option value="${month}"
                            <c:if test="${month eq selectedMonth}">selected</c:if>>
                            ${month}月
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="period-field">
                <label for="paymentRound">給与回数</label>
                <select id="paymentRound" name="paymentRound">
                    <c:forEach var="round" begin="1" end="10">
                        <option value="${round}"
                            <c:if test="${round eq selectedRound}">selected</c:if>>
                            給与-${round}次
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="period-field period-range">
                <label>精算期間</label>
                <input type="date" lang="ja-JP"
                    name="calculationStart"
                    value="${calculationStart}">
                <span>~</span>
                <input type="date" lang="ja-JP"
                    name="calculationEnd"
                    value="${calculationEnd}">
            </div>

            <div class="period-field">
                <label for="paymentDate">給与支給日</label>
                <input id="paymentDate"
                    type="date" lang="ja-JP"
                    name="paymentDate"
                    value="${paymentDate}">
            </div>

            <button type="submit" class="button button-primary">照会</button>
        </form>

        <section class="payroll-workspace">
            <div class="employee-area">
                <div class="section-toolbar">
                    <div class="toolbar-buttons toolbar-buttons-left">
                        <a class="button button-primary" href="#employee-add">
                            新規追加
                        </a>
                    </div>

                    <div class="toolbar-buttons toolbar-buttons-right">
                        <button type="submit"
                            form="employeeForm"
                            class="button button-neutral">
                            選択削除
                        </button>

                        <button type="submit"
                            form="employeeForm"
                            name="deleteType"
                            value="ALL"
                            class="button button-neutral">
                            完全削除
                        </button>
                    </div>
                </div>

                <form id="employeeForm"
                    method="post"
                    action="${pageContext.request.contextPath}/payroll/day-worker/employees/delete.do">
                    <input type="hidden" name="paymentYear" value="${selectedYear}">
                    <input type="hidden" name="paymentMonth" value="${selectedMonth}">
                    <input type="hidden" name="paymentRound" value="${selectedRound}">
                    <input type="hidden" name="calculationStart" value="${calculationStart}">
                    <input type="hidden" name="calculationEnd" value="${calculationEnd}">
                    <input type="hidden" name="paymentDate" value="${paymentDate}">

                    <div class="table-wrap employee-table-wrap">
                        <table class="data-table employee-table">
                            <colgroup>
                                <col class="check-column">
                                <col>
                                <col>
                                <col>
                                <col class="money-column">
                            </colgroup>

                            <thead>
                                <tr>
                                    <th>選択</th>
                                    <th>区分</th>
                                    <th>氏名</th>
                                    <th>部署</th>
                                    <th>差引支給額</th>
                                </tr>
                            </thead>

                            <tbody>
                                <c:choose>
                                    <c:when test="${not empty paymentEmployees}">
                                        <c:forEach var="employee"
                                            items="${paymentEmployees}">

                                            <tr class="${employee.employeeId eq selectedEmployeeId
                                                ? 'selected-row' : ''}">

                                                <td>
                                                    <input type="checkbox"
                                                        name="employeeIds"
                                                        value="${employee.employeeId}">
                                                </td>

                                                <td>
                                                    <a href="?paymentYear=${selectedYear}&amp;paymentMonth=${selectedMonth}&amp;paymentRound=${selectedRound}&amp;employeeId=${employee.employeeId}">
                                                        <ui:code-label value="${employee.employmentTypeName}" />
                                                    </a>
                                                </td>

                                                <td>
                                                    <a href="?paymentYear=${selectedYear}&amp;paymentMonth=${selectedMonth}&amp;paymentRound=${selectedRound}&amp;employeeId=${employee.employeeId}">
                                                        ${employee.employeeName}
                                                    </a>
                                                </td>

                                                <td>${employee.departmentName}</td>
                                                <td class="amount employee-net-payment">${employee.netPayment}</td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>

                                    <c:otherwise>
                                        <tr>
                                            <td class="empty-row" colspan="5">
                                                照会された給与の対象日雇い社員はありません。
                                            </td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
                </form>
            </div>

            <div class="payment-area">
                <form method="post"
                    action="${pageContext.request.contextPath}/payroll/day-worker/save.do">

                    <input type="hidden"
                        name="employeeId"
                        value="${selectedEmployee.employeeId}">
                    <input type="hidden" name="paymentYear" value="${selectedYear}">
                    <input type="hidden" name="paymentMonth" value="${selectedMonth}">
                    <input type="hidden" name="paymentRound" value="${selectedRound}">
                    <input type="hidden" name="calculationStart" value="${calculationStart}">
                    <input type="hidden" name="calculationEnd" value="${calculationEnd}">
                    <input type="hidden" name="paymentDate" value="${paymentDate}">

                    <div class="day-payment-panels">
                        <section class="amount-panel work-detail-panel">
                            <header>
                                <h2>勤務別支給履歴</h2>
                            </header>

                            <div class="table-wrap">
                                <table class="input-table work-payment-table">
                                    <thead>
                                        <tr>
                                            <th>日付</th>
                                            <th>支給率</th>
                                            <th>支給額</th>
                                            <th>所得税</th>
                                            <th>地方所得税</th>
                                        </tr>
                                    </thead>

                                    <tbody>
                                        <c:choose>
                                            <c:when test="${not empty selectedEmployee.workPayments}">
                                                <c:forEach var="work"
                                                    items="${selectedEmployee.workPayments}">

                                                    <tr>
                                                        <td>
                                                            <input type="date" lang="ja-JP"
                                                                value="${work.workDate}"
                                                                readonly>
                                                        </td>
                                                        <td>
                                                            <input type="text"
                                                                value="${work.paymentRate}"
                                                                readonly>
                                                        </td>
                                                        <td>
                                                            <input type="text"
                                                                value="${work.paymentAmount}"
                                                                readonly>
                                                        </td>
                                                        <td>
                                                            <input type="text"
                                                                value="${work.incomeTax}"
                                                                readonly>
                                                        </td>
                                                        <td>
                                                            <input type="text"
                                                                value="${work.localIncomeTax}"
                                                                readonly>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </c:when>

                                            <c:otherwise>
                                                <tr>
                                                    <td class="empty-row" colspan="5">
                                                        選択した社員の勤務別支給内訳がありません。
                                                    </td>
                                                </tr>
                                            </c:otherwise>
                                        </c:choose>
                                    </tbody>
                                </table>
                            </div>
                        </section>

                        <section class="amount-panel deduction-panel">
                            <header>
                                <h2>控除項目</h2>

                                <div class="deduction-tools">
                                    <button type="submit"
                                        name="calculationType"
                                        value="INSURANCE">
                                        4大保険
                                    </button>

                                    <button type="submit"
                                        name="calculationType"
                                        value="PERIOD_TAX">
                                        期間単位所得税
                                    </button>
                                </div>
                            </header>

                            <table class="input-table deduction-table">
                                <thead>
                                    <tr>
                                        <th>項目</th>
                                        <th>金額</th>
                                    </tr>
                                </thead>

                                <tbody>
                                    <tr>
                                        <th>国民年金</th>
                                        <td>
                                    <input type="number"
                                        name="nationalPension"
                                        min="0"
                                                value="${selectedEmployee.nationalPension}">
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>健康保険</th>
                                        <td>
                                    <input type="number"
                                        name="healthInsurance"
                                        min="0"
                                                value="${selectedEmployee.healthInsurance}">
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>長期療養保険</th>
                                        <td>
                                    <input type="number"
                                        name="longTermCareInsurance"
                                        min="0"
                                                value="${selectedEmployee.longTermCareInsurance}">
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>雇用保険</th>
                                        <td>
                                    <input type="number"
                                        name="employmentInsurance"
                                        min="0"
                                                value="${selectedEmployee.employmentInsurance}">
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>所得税</th>
                                        <td>
                                    <input type="number"
                                        name="incomeTax"
                                        min="0"
                                                value="${selectedEmployee.incomeTax}">
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>地方所得税</th>
                                        <td>
                                    <input type="number"
                                        name="localIncomeTax"
                                        min="0"
                                                value="${selectedEmployee.localIncomeTax}">
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>互助会費</th>
                                        <td>
                                    <input type="number"
                                        name="mutualAidFee"
                                        min="0"
                                                value="${selectedEmployee.mutualAidFee}">
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </section>
                    </div>

                    <div class="day-worker-totals">
                        <div>
                            <strong>支給総額</strong>
                            <span>${selectedEmployee.totalPayment}</span>
                            <em>円</em>
                        </div>

                        <div>
                            <strong>控除総額</strong>
                            <span>${selectedEmployee.totalDeduction}</span>
                            <em>円</em>
                        </div>
                    </div>

                    <div class="net-payment">
                        <span>差引支給額：</span>
                        <strong>${selectedEmployee.netPayment}</strong>
                        <em>円</em>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="button button-primary">
                            保存
                        </button>

                        <button type="reset"
                            class="button button-outline button-clear">
                            内容を消去する
                        </button>
                    </div>
                </form>
            </div>
        </section>
    </main>

    <div id="employee-add" class="modal-overlay">
        <section class="modal employee-modal">
            <header>
                <h2>給与対象日雇い社員追加</h2>
                <a href="#" aria-label="閉じる">×</a>
            </header>

            <form method="get"
                action="${pageContext.request.contextPath}/payroll/day-worker-management.do#employee-add">
                <input type="hidden" name="paymentYear" value="${selectedYear}">
                <input type="hidden" name="paymentMonth" value="${selectedMonth}">
                <input type="hidden" name="paymentRound" value="${selectedRound}">
                <input type="hidden" name="calculationStart" value="${calculationStart}">
                <input type="hidden" name="calculationEnd" value="${calculationEnd}">
                <input type="hidden" name="paymentDate" value="${paymentDate}">

                <div class="employee-search-row">
                    <input type="search"
                        name="employeeKeyword"
                        value="<c:out value='${param.employeeKeyword}' />"
                        placeholder="社員検索">

                    <button class="button button-primary" type="submit">
                        検索
                    </button>

                    <div class="employee-filters">
                        <select name="departmentId">
                            <option value="">部署別</option>
                            <c:forEach var="department" items="${departments}">
                                <option value="${department.departmentId}" ${param.departmentId eq department.departmentId ? 'selected' : ''}>
                                    ${department.departmentName}
                                </option>
                            </c:forEach>
                        </select>

                        <select name="status">
                            <option value="ACTIVE">在職</option>
                        </select>
                    </div>
                </div>
            </form>

            <form method="post"
                action="${pageContext.request.contextPath}/payroll/day-worker/employees/add.do">
                <input type="hidden" name="paymentYear" value="${selectedYear}">
                <input type="hidden" name="paymentMonth" value="${selectedMonth}">
                <input type="hidden" name="paymentRound" value="${selectedRound}">
                <input type="hidden" name="calculationStart" value="${calculationStart}">
                <input type="hidden" name="calculationEnd" value="${calculationEnd}">
                <input type="hidden" name="paymentDate" value="${paymentDate}">

                <div class="modal-body">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th class="check-column">選択</th>
                                <th>区分</th>
                                <th>社員番号</th>
                                <th>氏名</th>
                                <th>部署</th>
                                <th>役職</th>
                                <th>ステータス</th>
                            </tr>
                        </thead>

                        <tbody>
                            <c:choose>
                                <c:when test="${not empty availableEmployeePage.content}">
                                    <c:forEach var="employee"
                                        items="${availableEmployeePage.content}">

                                        <tr>
                                            <td>
                                                <input type="checkbox"
                                                    name="employeeIds"
                                                    value="${employee.employeeId}">
                                            </td>
                                            <td><ui:code-label value="${employee.employmentTypeName}" /></td>
                                            <td>${employee.employeeNumber}</td>
                                            <td>${employee.employeeName}</td>
                                            <td>${employee.departmentName}</td>
                                            <td>${employee.positionName}</td>
                                            <td><ui:code-label value="${employee.statusName}" /></td>
                                        </tr>
                                    </c:forEach>
                                </c:when>

                                <c:otherwise>
                                    <tr>
                                        <td colspan="7" class="empty-row">
                                            追加できる日雇い社員はいません。
                                        </td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>

                <nav class="modal-pagination">
                    <c:if test="${employeePage gt 1}">
                        <c:url var="previousDayWorkerEmployeePageUrl" value="/payroll/day-worker-management.do"><c:param name="paymentYear" value="${selectedYear}" /><c:param name="paymentMonth" value="${selectedMonth}" /><c:param name="paymentRound" value="${selectedRound}" /><c:param name="employeeKeyword" value="${param.employeeKeyword}" /><c:param name="departmentId" value="${param.departmentId}" /><c:param name="status" value="${param.status}" /><c:param name="employeePage" value="${employeePage - 1}" /></c:url>
                        <a href="${previousDayWorkerEmployeePageUrl}#employee-add">
                            ‹前
                        </a>
                    </c:if>

                    <span>${employeePage}</span>

                    <c:if test="${employeePage lt employeeTotalPages}">
                        <c:url var="nextDayWorkerEmployeePageUrl" value="/payroll/day-worker-management.do"><c:param name="paymentYear" value="${selectedYear}" /><c:param name="paymentMonth" value="${selectedMonth}" /><c:param name="paymentRound" value="${selectedRound}" /><c:param name="employeeKeyword" value="${param.employeeKeyword}" /><c:param name="departmentId" value="${param.departmentId}" /><c:param name="status" value="${param.status}" /><c:param name="employeePage" value="${employeePage + 1}" /></c:url>
                        <a href="${nextDayWorkerEmployeePageUrl}#employee-add">
                            次の›
                        </a>
                    </c:if>
                </nav>

                <div class="modal-actions">
                    <button type="submit" class="button button-primary">
                        社員を選択
                    </button>

                    <a href="#" class="button button-neutral">
                        選択解除
                    </a>
                </div>
            </form>
        </section>
    </div>

    <c:if test="${not empty payrollPopupMessage}">
        <c:url var="dayWorkerReturnUrl" value="/payroll/day-worker-management.do">
            <c:param name="paymentYear" value="${selectedYear}" />
            <c:param name="paymentMonth" value="${selectedMonth}" />
            <c:param name="paymentRound" value="${selectedRound}" />
            <c:if test="${not empty selectedEmployeeId}">
                <c:param name="employeeId" value="${selectedEmployeeId}" />
            </c:if>
        </c:url>
        <div class="day-worker-alert" role="alertdialog" aria-modal="true"
            aria-labelledby="day-worker-alert-message">
            <a class="day-worker-alert__backdrop" href="${dayWorkerReturnUrl}" aria-label="閉じる"></a>
            <div class="day-worker-alert__panel">
                <p id="day-worker-alert-message"><ui:message-label value="${payrollPopupMessage}" /></p>
                <a href="${dayWorkerReturnUrl}">確認</a>
            </div>
        </div>
    </c:if>

    <%@ include file="/WEB-INF/view/common/footer.jspf" %>
</body>
</html>
