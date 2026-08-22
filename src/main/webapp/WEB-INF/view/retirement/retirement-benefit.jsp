<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ja-JP">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>退職給付の入力/管理</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/retirement/retirement-benefit.css?v=20260816-3">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/payzon-ui.css?v=20260821-1">
</head>
<body>
<%@ include file="/WEB-INF/view/common/header.jspf" %>
<main class="retirement-benefit-page page-content">
    <header class="page-heading"><div><p>退職管理</p><h1>退職給付の入力/管理</h1></div></header>

	<%-- 支払年度別退職給付計算履歴JOIN一覧 --%>
    <section class="benefit-card benefit-list-card">
        <div class="benefit-toolbar">
            <form method="get" action="${pageContext.request.contextPath}/retirement/benefit.do">
                <input type="hidden" name="mode" value="list">
                <label for="paymentYear">支払年</label>
                <select id="paymentYear" name="paymentYear">
					<c:forEach var="year" items="${paymentYears}"><option value="${year}" ${year eq selectedYear ? 'selected' : ''}>${year}年</option></c:forEach>
                </select>
                <button type="submit" class="search-button">照会</button>
            </form>
            <a class="button button-primary new-button" href="#employee-select-modal">新規追加</a>
        </div>
        <table class="data-table benefit-list-table">
            <colgroup>
                <col class="payment-date-col"><col class="type-col"><col class="name-col">
                <col class="position-col"><col class="department-col"><col class="period-col">
                <col class="service-days-col"><col class="net-payment-col"><col class="payment-method-col">
            </colgroup>
            <thead><tr><th>支給日</th><th>区分</th><th>氏名</th><th>役職</th><th>部署</th><th>算定期間</th><th>勤続日数</th><th>差引支給額</th><th>支給方法</th></tr></thead>
            <tbody>
            <c:forEach var="draftEmployee" items="${draftBenefitEmployees}">
                <c:set var="loadedBenefit" value="${draftBenefitForms[draftEmployee.employeeId]}" />
                <c:set var="loadedDraft" value="${not empty loadedBenefit}" />
                <tr class="benefit-list-row draft-benefit-row ${retirementBenefit.employeeId eq draftEmployee.employeeId ? 'is-selected' : ''}">
                    <td><button type="submit" name="activeEmployeeId" value="${draftEmployee.employeeId}" form="draftEmployeeForm">${loadedDraft ? loadedBenefit.paymentDate : '-'}</button></td>
                    <td><button type="submit" name="activeEmployeeId" value="${draftEmployee.employeeId}" form="draftEmployeeForm"><c:choose><c:when test="${loadedDraft and loadedBenefit.settlementType eq 'RETIREMENT'}">退職精算</c:when><c:when test="${loadedDraft and loadedBenefit.settlementType eq 'INTERIM'}">中間決済</c:when><c:otherwise>-</c:otherwise></c:choose></button></td>
                    <td><button type="submit" name="activeEmployeeId" value="${draftEmployee.employeeId}" form="draftEmployeeForm"><c:out value="${draftEmployee.name}" /></button></td>
                    <td><button type="submit" name="activeEmployeeId" value="${draftEmployee.employeeId}" form="draftEmployeeForm"><c:out value="${draftEmployee.positionName}" /></button></td>
                    <td><button type="submit" name="activeEmployeeId" value="${draftEmployee.employeeId}" form="draftEmployeeForm"><c:out value="${draftEmployee.departmentName}" /></button></td>
                    <td><button type="submit" name="activeEmployeeId" value="${draftEmployee.employeeId}" form="draftEmployeeForm">${loadedDraft ? loadedBenefit.startDate : '-'}<c:if test="${loadedDraft}"> ~ ${loadedBenefit.endDate}</c:if></button></td>
                    <td><button type="submit" name="activeEmployeeId" value="${draftEmployee.employeeId}" form="draftEmployeeForm">${loadedDraft ? loadedBenefit.serviceDays : '-'}<c:if test="${loadedDraft}">日</c:if></button></td>
                    <td class="amount"><button type="submit" name="activeEmployeeId" value="${draftEmployee.employeeId}" form="draftEmployeeForm"><c:choose><c:when test="${loadedDraft}"><fmt:formatNumber value="${loadedBenefit.netPayment}" />円</c:when><c:otherwise>0円</c:otherwise></c:choose></button></td>
                    <td><button type="submit" name="activeEmployeeId" value="${draftEmployee.employeeId}" form="draftEmployeeForm">${loadedDraft ? loadedBenefit.paymentMethod : '-'}</button></td>
                </tr>
            </c:forEach>
            <c:forEach var="item" items="${retirementBenefits}">
                <c:url var="employeeBenefitUrl" value="/retirement/benefit.do">
		<c:param name="calculationId" value="${item.calculationId}"></c:param>
                    <c:param name="paymentYear" value="${selectedYear}"></c:param>
                    <c:param name="mode" value="list"></c:param>
                </c:url>
				<tr class="benefit-list-row ${param.calculationId eq item.calculationId ? 'is-selected' : ''}">
                    <td><a href="${employeeBenefitUrl}">${item.paymentDate}</a></td>
                    <td><a href="${employeeBenefitUrl}"><ui:code-label value="${item.settlementType}" /></a></td>
                    <td><a href="${employeeBenefitUrl}">${item.employeeName}</a></td>
                    <td><a href="${employeeBenefitUrl}">${item.positionName}</a></td>
                    <td><a href="${employeeBenefitUrl}">${item.departmentName}</a></td>
                    <td><a href="${employeeBenefitUrl}">${item.calculationStartDate} ~ ${item.calculationEndDate}</a></td>
                    <td><a href="${employeeBenefitUrl}">${item.serviceDays}日</a></td>
                    <td class="amount"><a href="${employeeBenefitUrl}">${item.netPayment}円</a></td>
                    <td><a href="${employeeBenefitUrl}">${item.paymentMethod}</a></td>
                </tr>
            </c:forEach>
            <c:if test="${empty retirementBenefits and empty draftBenefitEmployees}"><tr><td colspan="9" class="empty-row">登録された退職給付履歴はありません。</td></tr></c:if>
            </tbody>
        </table>
		<form id="draftEmployeeForm" method="post" action="${pageContext.request.contextPath}/retirement/benefit/new.do">
			<input type="hidden" name="paymentYear" value="${selectedYear}">
			<c:forEach var="draftEmployeeId" items="${draftEmployeeIds}"><input type="hidden" name="employeeIds" value="${draftEmployeeId}"></c:forEach>
		</form>
        <div class="list-actions"><button type="submit" form="deleteForm" class="button button-muted">選択削除</button><button type="submit" form="deleteAllForm" class="button button-muted">完全削除</button></div>
		<form id="deleteForm" method="post" action="${pageContext.request.contextPath}/retirement/benefit/delete.do"><input type="hidden" name="calculationId" value="${retirementBenefit.calculationId}"><input type="hidden" name="activeEmployeeId" value="${retirementBenefit.employeeId}"><input type="hidden" name="paymentYear" value="${selectedYear}"><c:forEach var="draftEmployeeId" items="${draftEmployeeIds}"><input type="hidden" name="employeeIds" value="${draftEmployeeId}"></c:forEach></form>
		<form id="deleteAllForm" method="post" action="${pageContext.request.contextPath}/retirement/benefit/delete-all.do"><input type="hidden" name="paymentYear" value="${selectedYear}"><c:forEach var="draftEmployeeId" items="${draftEmployeeIds}"><input type="hidden" name="employeeIds" value="${draftEmployeeId}"></c:forEach></form>
    </section>

    <form class="benefit-form" method="post" action="${pageContext.request.contextPath}/retirement/benefit/save.do">
		<input type="hidden" name="calculationId" value="${retirementBenefit.calculationId}">
        <input type="hidden" name="employeeId" value="${retirementBenefit.employeeId}">
        <input type="hidden" name="paymentYear" value="${selectedYear}">
        <input type="hidden" name="mode" value="list">
		<c:forEach var="draftEmployeeId" items="${draftEmployeeIds}"><input type="hidden" name="employeeIds" value="${draftEmployeeId}"></c:forEach>

        <section class="retirement-calc-bar">
			<label><span>区分</span><select disabled aria-disabled="true"><option value="">選択</option><option value="RETIREMENT" ${retirementBenefit.settlementType eq 'RETIREMENT' ? 'selected' : ''}>退職精算</option><option value="INTERIM" ${retirementBenefit.settlementType eq 'INTERIM' ? 'selected' : ''}>中間決済</option></select><input type="hidden" name="settlementType" value="${retirementBenefit.settlementType}"></label>
            <label><span>入社日</span><input type="date" lang="ja-JP" name="startDate" value="${retirementBenefit.startDate}"></label>
            <label><span>退職日</span><input type="date" lang="ja-JP" name="endDate" value="${retirementBenefit.endDate}"></label>
            <div class="calc-value"><span>勤続年数</span><b>${retirementBenefit.serviceYears}</b><em>年</em></div>
            <div class="calc-value"><span>勤続日数</span><b>${retirementBenefit.serviceDays}</b><em>日</em></div>
            <label class="excluded-days"><span>除外日数</span><div class="unit-input"><input type="number" name="excludedDays" value="${retirementBenefit.excludedDays}"><em>日</em></div></label>
        </section>

        <div class="original-two-column">
            <section class="original-block">
                <div class="original-section-title"><h2>給与履歴</h2><span>理由発生日以前の3ヶ月の支給合計額</span><button type="submit" name="action" value="loadPay" formaction="${pageContext.request.contextPath}/retirement/benefit.do" class="small-button">給与履歴を呼び出す</button></div>
                <table class="original-table salary-history-table">
                    <thead><tr><th>算定期間</th><th>算定日数</th><th>給与総額</th></tr></thead>
					<tbody><c:forEach begin="0" end="3" var="row"><c:set var="salary" value="${retirementBenefit.salaryEntries[row]}"/><tr><td><div class="date-range"><input type="date" lang="ja-JP" name="salaryStartDate" value="<fmt:formatDate value='${salary.periodStartDate}' pattern='yyyy-MM-dd'/>"><i>~</i><input type="date" lang="ja-JP" name="salaryEndDate" value="<fmt:formatDate value='${salary.periodEndDate}' pattern='yyyy-MM-dd'/>"></div></td><td><input type="number" name="salaryDays" value="${empty salary.calcDays ? 0 : salary.calcDays}" readonly aria-readonly="true"></td><td><input type="text" name="salaryTotal" value="${salary.amount}"></td></tr></c:forEach></tbody>
                    <tfoot><tr><th>総合計</th><td>${retirementBenefit.salaryDaysTotal}</td><td>${retirementBenefit.salaryTotal}</td></tr></tfoot>
                </table>
                <p class="warning-note"> ただし、中間日付計算の場合、該当月の支給合計金額で日数で割った値を基本として表示</p>
                <table class="original-table two-field-table"><thead><tr><th>退職慰労金</th><th>解雇予告手当</th></tr></thead><tbody><tr><td><input type="text" name="compensation" value="${retirementBenefit.compensation}"></td><td><input type="text" name="dismissalAllowance" value="${retirementBenefit.dismissalAllowance}"></td></tr></tbody></table>
            </section>

            <section class="original-block">
                <div class="original-section-title"><h2>その他の課税所得</h2><span>理由発生日前の1年間の金額を入力</span></div>
                <table class="original-table other-income-table">
                    <thead><tr><th>支払年月</th><th>支給項目</th><th>金額</th><th>3ヶ月分</th></tr></thead>
					<tbody><c:forEach begin="0" end="4" var="row"><c:set var="income" value="${retirementBenefit.otherIncomeEntries[row]}"/><tr><td><input type="month" name="otherIncomeMonth" value="${income.payYmInput}"></td><td><input type="text" name="otherIncomeItem" value="${income.itemName}"></td><td><input type="text" name="otherIncomeAmount" value="${income.amount}"></td><td><input type="text" name="threeMonthAmount" readonly value="${empty income ? 0 : income.threeMonthAmount}"></td></tr></c:forEach></tbody>
                </table>
                <table class="original-table three-field-table"><thead><tr><th>非課税退職給付</th><th>既納付・課税繰延済み税額</th><th>税額控除</th></tr></thead><tbody><tr><td><input type="text" name="taxFreeRetirement" value="${retirementBenefit.taxFreeRetirement}"></td><td><input type="text" name="prepaidTax" value="${retirementBenefit.prepaidTax}"></td><td><input type="text" name="taxCredit" value="${retirementBenefit.taxCredit}"></td></tr></tbody></table>
            </section>
        </div>

        <section class="original-block deferred-block">
            <div class="original-section-title"><h2>課税連携口座</h2><span>該当しない場合は入力しません。</span></div>
			<table class="original-table deferred-table"><thead><tr><th>退職年金事業者名</th><th>事業者登録番号</th><th>口座番号</th><th>入金（振替）日</th><th>口座振込金額</th></tr></thead><tbody><c:forEach begin="0" end="1" var="row"><c:set var="deferral" value="${retirementBenefit.taxDeferrals[row]}"/><tr><td><input type="text" name="pensionProvider" value="${deferral.bizName}"></td><td><input type="text" name="pensionBusinessNo" value="${deferral.bizRegNo}"></td><td><input type="text" name="pensionAccount" value="${deferral.accountNo}"></td><td><input type="date" lang="ja-JP" name="pensionDate" value="<fmt:formatDate value='${deferral.depositDate}' pattern='yyyy-MM-dd'/>"></td><td><input type="text" name="pensionAmount" value="${deferral.depositAmt}"></td></tr></c:forEach></tbody></table>
            <p class="warning-note">年金口座に入金して退職所得税を徴収しない場合に作成します。</p>
        </section>

        <div class="calculate-action"><button type="submit" formaction="${pageContext.request.contextPath}/retirement/benefit/calculate.do" class="button button-primary calculate-button">退職金を計算する</button></div>

        <section class="original-block result-block">
            <table class="original-table result-table"><thead><tr><th>3ヶ月の合計</th><th> 1日平均賃金</th><th>1日通常賃金</th><th>退職所得</th><th>退職日が属する課税年度</th><th>算出税額</th></tr></thead><tbody><tr><td><input type="text" readonly value="${showCalculationResult ? retirementBenefit.threeMonthTotal : ''}"></td><td><input type="text" readonly value="${showCalculationResult ? retirementBenefit.dailyAverage : ''}"></td><td><input type="text" name="dailyOrdinary" readonly value="${showCalculationResult ? retirementBenefit.dailyOrdinary : ''}"></td><td><input type="text" name="retirementIncome" readonly value="${showCalculationResult ? retirementBenefit.retirementIncome : ''}"></td><td><input type="text" readonly value="${showCalculationResult ? retirementBenefit.taxYear : ''}"></td><td><input type="text" readonly value="${showCalculationResult ? retirementBenefit.calculatedTax : ''}"></td></tr></tbody><thead><tr><th>退職所得税</th><th>地方所得税</th><th>繰延退職所得税</th><th>繰延地方所得税</th><th>農漁村特別税</th><th>その他控除</th></tr></thead><tbody><tr><td><input type="text" name="incomeTax" readonly value="${showCalculationResult ? retirementBenefit.incomeTax : ''}"></td><td><input type="text" name="localIncomeTax" readonly value="${showCalculationResult ? retirementBenefit.localIncomeTax : ''}"></td><td><input type="text" readonly value="${showCalculationResult ? retirementBenefit.deferredIncomeTax : ''}"></td><td><input type="text" readonly value="${showCalculationResult ? retirementBenefit.deferredLocalTax : ''}"></td><td><input type="text" name="ruralTax" readonly value="${showCalculationResult ? retirementBenefit.ruralTax : ''}"></td><td><input type="text" name="otherDeduction" readonly value="${showCalculationResult ? retirementBenefit.otherDeduction : ''}"></td></tr></tbody></table>
            <p class="warning-note">通常賃金は、別途賃金資料がなく、計算された1日平均賃金と同じに適用します。</p>
        </section>

        <section class="original-block payment-block">
            <table class="original-table payment-table"><thead><tr><th>課税対象退職給付</th><th>差引源泉徴収税額</th><th>差引支給額</th><th>支給方法</th><th>支給日</th></tr></thead><tbody><tr><td><strong>${showCalculationResult ? retirementBenefit.taxablePayment : ''}</strong><c:if test="${showCalculationResult}"> 円</c:if></td><td><strong>${showCalculationResult ? retirementBenefit.withholdingTax : ''}</strong><c:if test="${showCalculationResult}"> 円</c:if></td><td><strong>${showCalculationResult ? retirementBenefit.netPayment : ''}</strong><c:if test="${showCalculationResult}"> 円</c:if></td><td><input type="text" name="paymentMethod" value="${retirementBenefit.paymentMethod}"></td><td><input type="date" lang="ja-JP" name="paymentDate" value="${retirementBenefit.paymentDate}"></td></tr></tbody></table>
        </section>
        <div class="bottom-actions"><button type="submit" class="button button-primary">保存</button><a href="${pageContext.request.contextPath}/retirement/benefit.do?paymentYear=${selectedYear}" class="button button-muted">内容を消去する</a></div>
    </form>

	<%-- CSS：target方式で新規退職給付対象社員を検索・選択する。 --%>
    <section id="employee-select-modal" class="employee-select-overlay">
        <div class="employee-select-modal" role="dialog" aria-modal="true" aria-labelledby="employee-select-title">
            <header><h2 id="employee-select-title">退職給付支払い社員選択</h2><a href="#" class="modal-close" aria-label="閉じる">×</a></header>
			<div class="employee-modal-search">
				<form method="get" action="${pageContext.request.contextPath}/retirement/benefit/employee-search.do#employee-select-modal" class="employee-keyword-form">
					<input type="hidden" name="searchMode" value="keyword">
					<c:forEach var="draftEmployeeId" items="${draftEmployeeIds}"><input type="hidden" name="employeeIds" value="${draftEmployeeId}"></c:forEach>
					<input type="search" name="employeeKeyword" value="${param.employeeKeyword}" placeholder="社員検索">
					<button type="submit" class="search-button">検索</button>
					<c:url var="employeeAllUrl" value="/retirement/benefit/employee-search.do"><c:forEach var="draftEmployeeId" items="${draftEmployeeIds}"><c:param name="employeeIds" value="${draftEmployeeId}"/></c:forEach></c:url>
					<a href="${employeeAllUrl}#employee-select-modal" class="all-view">全体を見る</a>
				</form>
				<form method="get" action="${pageContext.request.contextPath}/retirement/benefit/employee-search.do#employee-select-modal" class="employee-department-form">
					<input type="hidden" name="searchMode" value="department">
					<c:forEach var="draftEmployeeId" items="${draftEmployeeIds}"><input type="hidden" name="employeeIds" value="${draftEmployeeId}"></c:forEach>
					<select name="departmentId"><option value="">部署別</option><c:forEach var="department" items="${departments}"><option value="${department.departmentId}" ${param.departmentId eq department.departmentId ? 'selected' : ''}>${department.departmentName}</option></c:forEach></select>
					<button type="submit" class="search-button apply-button">適用</button>
				</form>
			</div>
            <form method="post" action="${pageContext.request.contextPath}/retirement/benefit/new.do">
				<input type="hidden" name="paymentYear" value="${selectedYear}">
				<c:forEach var="draftEmployeeId" items="${draftEmployeeIds}"><input type="hidden" name="employeeIds" value="${draftEmployeeId}"></c:forEach>
				<div class="employee-select-table-wrap"><table class="data-table employee-select-table"><colgroup><col class="select-col"><col><col><col><col><col><col></colgroup><thead><tr><th>選択</th><th>区分</th><th>社員番号</th><th>氏名</th><th>部署</th><th>役職</th><th>ステータス</th></tr></thead><tbody><c:forEach var="employee" items="${selectableEmployees}"><tr><td><input type="checkbox" name="newEmployeeIds" value="${employee.employeeId}"></td><td><ui:code-label value="${employee.employmentType}" /></td><td>${employee.employeeNo}</td><td>${employee.name}</td><td>${employee.departmentName}</td><td>${employee.positionName}</td><td><ui:code-label value="${employee.statusName}" /></td></tr></c:forEach><c:if test="${empty selectableEmployees}"><tr><td colspan="7" class="empty-row">選択する社員がいません。</td></tr></c:if></tbody></table></div>
                <div class="employee-select-actions"><button type="submit" class="button button-primary">社員を選択</button><a href="#" class="button button-muted">選択解除</a></div>
            </form>
        </div>
    </section>

    <c:if test="${not empty message}">
        <input type="checkbox" id="benefit-alert-close" class="benefit-alert__close-control">
        <div class="benefit-alert" role="alertdialog" aria-modal="true" aria-labelledby="benefit-alert-message">
            <label class="benefit-alert__backdrop" for="benefit-alert-close" aria-label="案内を閉じる"></label>
            <div class="benefit-alert__panel">
                <p id="benefit-alert-message"><ui:message-label value="${message}" /></p>
                <label class="benefit-alert__confirm" for="benefit-alert-close">確認</label>
            </div>
        </div>
    </c:if>
</main>
<%@ include file="/WEB-INF/view/common/footer.jspf" %>
</body>
</html>
