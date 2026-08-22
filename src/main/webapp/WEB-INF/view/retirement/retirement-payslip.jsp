<%-- 조회 결과와 입력 양식을 표시하는 JSP 화면이다. --%>
<%-- 照会結果と入力フォームを表示するJSP画面である。 --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ja-JP">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>退職給付明細書</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/retirement/retirement-payslip.css?v=20260816-1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/payzon-ui.css?v=20260821-1">
</head>
<body>
<%@ include file="/WEB-INF/view/common/header.jspf" %>
<main class="retirement-payslip-page page-content">
    <header class="page-heading"><div><p>退職管理</p><h1>退職給付明細書</h1></div></header>

    <div class="payslip-layout">
        <aside class="payslip-employee-panel">
            <form class="payslip-filter" method="get" action="${pageContext.request.contextPath}/retirement/payslip.do">
                <label for="payslipYear">支給年</label>
                <select id="payslipYear" name="paymentYear">
                    <c:forEach var="year" items="${paymentYears}">
                        <option value="${year}" ${year eq selectedYear ? 'selected' : ''}>${year}年</option>
                    </c:forEach>
                </select>
                <input type="search" name="keyword" value="${param.keyword}" placeholder="氏名検索">
                <button type="submit" class="search-button">検索</button>
                <a class="all-view" href="${pageContext.request.contextPath}/retirement/payslip.do?paymentYear=${selectedYear}">全体を見る</a>
            </form>
            <div class="payslip-employee-list">
                <table class="data-table">
                    <colgroup><col><col><col></colgroup>
                    <thead><tr><th>区分</th><th>氏名</th><th>差引支給額</th></tr></thead>
                    <tbody>
                    <c:forEach var="item" items="${retirementPayslips}">
                        <c:url var="payslipUrl" value="/retirement/payslip.do">
                            <c:param name="calculationId" value="${item.calculationId}"/>
                            <c:param name="paymentYear" value="${selectedYear}"/>
                            <c:if test="${not empty param.keyword}"><c:param name="keyword" value="${param.keyword}"/></c:if>
                        </c:url>
                        <tr class="${selectedPayslip.calculationId eq item.calculationId ? 'is-selected' : ''}">
                            <td><a href="${payslipUrl}"><ui:code-label value="${item.settlementType}" /></a></td>
                            <td><a href="${payslipUrl}">${item.employeeName}</a></td>
                            <td><a href="${payslipUrl}"><fmt:formatNumber value="${item.netPayment}" pattern="#,##0"/></a></td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty retirementPayslips}">
                        <tr><td colspan="3" class="empty-row">照会された退職給与明細書はありません。</td></tr>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </aside>

        <section class="payslip-workspace">
            <article class="retirement-document">
                        <header class="payslip-document-title">
                            <div class="company-logo">
                                <c:choose>
                                    <c:when test="${not empty company.logoImgPath}"><img src="${pageContext.request.contextPath}${company.logoImgPath}" alt="会社のロゴ"></c:when>
                                    <c:otherwise><span>会社のロゴ</span></c:otherwise>
                                </c:choose>
                            </div>
                            <h2>退職給与明細書</h2>
                        </header>

                        <section class="document-section">
                            <h3>社員情報</h3>
                            <table class="document-table employee-info-table"><tbody>
                            <tr><th>氏名</th><td>${selectedPayslip.employeeName}</td><th>入社日</th><td>${selectedPayslip.joinDate}</td></tr>
                            <tr><th>部署</th><td>${selectedPayslip.departmentName}</td><th>退職日</th><td>${selectedPayslip.retirementDate}</td></tr>
                            <tr><th>役職</th><td>${selectedPayslip.positionName}</td><th>勤続日数</th><td><c:if test="${not empty selectedPayslip}"><fmt:formatNumber value="${selectedPayslip.serviceDays}"/>日</c:if></td></tr>
                            </tbody></table>
                        </section>

                        <section class="document-section">
                            <h3>給与履歴</h3>
                            <table class="document-table salary-detail-table">
                                <thead>
                                <tr><th rowspan="2">算定期間</th><c:forEach begin="0" end="3" var="row"><th>${selectedPayslip.salaryDetails[row].startDate}</th></c:forEach><th rowspan="2">系</th></tr>
                                <tr><c:forEach begin="0" end="3" var="row"><th>${selectedPayslip.salaryDetails[row].endDate}</th></c:forEach></tr>
                                </thead>
                                <tbody>
                                <tr><th>算定日数</th><c:forEach begin="0" end="3" var="row"><td><fmt:formatNumber value="${selectedPayslip.salaryDetails[row].days}" pattern="#0.##"/></td></c:forEach><td class="total-cell"><fmt:formatNumber value="${selectedPayslip.salaryDaysTotal}"/></td></tr>
                                <tr><th>給与総額</th><c:forEach begin="0" end="3" var="row"><td><fmt:formatNumber value="${selectedPayslip.salaryDetails[row].amount}"/></td></c:forEach><td class="total-cell"><fmt:formatNumber value="${selectedPayslip.salaryTotal}"/></td></tr>
                                </tbody>
                            </table>
                        </section>

                        <section class="document-section">
                            <h3>その他所得</h3>
                            <table class="document-table other-income-detail">
                                <thead><tr><th>支給項目</th><th> 1年間の支給額</th><th>3ヶ月分</th></tr></thead>
                                <tbody>
                                <c:forEach var="income" items="${selectedPayslip.otherIncomes}"><tr><td>${income.itemName}</td><td><fmt:formatNumber value="${income.annualAmount}"/></td><td><fmt:formatNumber value="${income.threeMonthAmount}"/></td></tr></c:forEach>
                                <c:if test="${empty selectedPayslip.otherIncomes}"><c:forEach begin="0" end="3"><tr><td>&nbsp;</td><td></td><td></td></tr></c:forEach></c:if>
                                </tbody>
                            </table>
                            <table class="document-table additional-pay-table"><tbody><tr><th>退職慰労金</th><td><fmt:formatNumber value="${selectedPayslip.compensation}"/></td><th>解雇予告手当</th><td><fmt:formatNumber value="${selectedPayslip.dismissalAllowance}"/></td></tr></tbody></table>
                        </section>

                        <section class="document-section">
                            <h3>退職所得</h3>
                            <table class="document-table income-calculation-table"><tbody>
                            <tr><th rowspan="2"> 1日平均賃金</th><th>3ヶ月の合計</th><td class="formula">給与総額計+ 3ヶ月分その他所得計</td><td><fmt:formatNumber value="${selectedPayslip.threeMonthTotal}"/></td></tr>
                            <tr><th> 1日平均賃金</th><td class="formula"> 3ヶ月総計/算定日数</td><td><fmt:formatNumber value="${selectedPayslip.dailyAverage}"/></td></tr>
                            <tr><th>1日通常賃金</th><td colspan="2" class="formula"> 1日の通常賃金が1日の平均賃金より高い場合に適用</td><td><fmt:formatNumber value="${selectedPayslip.dailyOrdinary}"/></td></tr>
                            <tr><th>退職所得</th><td colspan="2" class="formula">(1日平均賃金×30日×勤続日数 / 365) + 退職慰労金 + 解雇予告手当</td><td><fmt:formatNumber value="${selectedPayslip.retirementIncome}"/></td></tr>
                            </tbody></table>
                        </section>

                        <section class="document-section">
                            <h3>控除履歴</h3>
                            <table class="document-table deduction-table"><thead><tr><th>退職所得税</th><th>地方所得税</th><th>その他控除</th><th>控除総額</th></tr></thead><tbody><tr><td><fmt:formatNumber value="${selectedPayslip.incomeTax}"/></td><td><fmt:formatNumber value="${selectedPayslip.localIncomeTax}"/></td><td><fmt:formatNumber value="${selectedPayslip.otherDeduction}"/></td><td class="total-cell"><fmt:formatNumber value="${selectedPayslip.deductionTotal}"/></td></tr></tbody></table>
                        </section>

                        <div class="net-payment-row"><strong>差引支給額</strong><span>退職給与 - 控除総額</span><b><fmt:formatNumber value="${selectedPayslip.netPayment}"/></b></div>

                        <footer class="payslip-document-footer">
                            <p>上記の金額を、当該社員の退職金精算額で性格に領収した。</p>
                            <div class="document-date"><input name="issueYear" value="${issueYear}" maxlength="4">年 <input name="issueMonth" value="${issueMonth}" maxlength="2">月 <input name="issueDay" value="${issueDay}" maxlength="2">日</div>
                            <div class="company-signature"><div><strong>${company.cmpnName}</strong><span>${company.ceoTitle} ${company.ceoName}</span></div><div class="stamp-box"><c:choose><c:when test="${not empty company.stampImgPath}"><img src="${pageContext.request.contextPath}${company.stampImgPath}" alt="会社印"></c:when><c:otherwise><span>会社印<br>未登録</span></c:otherwise></c:choose></div></div>
                            <table class="signature-table"><tbody><tr><td><div class="signature-party"><strong>労働者</strong><span>${selectedPayslip.employeeName}</span><em></em></div></td><td><div class="signature-party"><strong>利用者</strong><span>${company.ceoName}</span><em></em></div></td></tr></tbody></table>
                        </footer>
            </article>
        </section>
    </div>
</main>
<c:if test="${not empty retirementPayslipPopupMessage}">
    <div class="retirement-payslip-alert" role="alertdialog" aria-modal="true" aria-labelledby="retirement-payslip-alert-message">
        <a class="retirement-payslip-alert__backdrop" href="${pageContext.request.contextPath}/retirement/payslip.do?paymentYear=${selectedYear}" aria-label="案内を閉じる"></a>
        <div class="retirement-payslip-alert__panel">
            <p id="retirement-payslip-alert-message"><ui:message-label value="${retirementPayslipPopupMessage}" /></p>
            <a href="${pageContext.request.contextPath}/retirement/payslip.do?paymentYear=${selectedYear}">確認</a>
        </div>
    </div>
</c:if>
<%@ include file="/WEB-INF/view/common/footer.jspf" %>
</body>
</html>
