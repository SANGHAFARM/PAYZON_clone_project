<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>
<c:set var="incomeMode" value="${empty param.incomeType ? (empty incomeType ? 'general' : incomeType) : param.incomeType}" />
<c:set var="employeePage" value="${empty param.employeePage ? 1 : param.employeePage}" />
<c:set var="employeeTotalPages" value="${empty availableEmployeePage.totalPages ? 1 : availableEmployeePage.totalPages}" />
<!DOCTYPE html>
<html lang="ja-JP">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>給与入力/管理</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/common.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/payroll/payroll-management.css?v=20260815-2">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/payzon-ui.css?v=20260821-1">
</head>
<body>
	<%@ include file="/WEB-INF/view/common/header.jspf" %>

	<main class="page-content payment-management-page">
		<header class="page-heading">
			<div><p>給与管理</p><h1>給与入力/管理</h1></div>
		</header>

		<form class="period-panel" method="get" action="${pageContext.request.contextPath}/payroll/management.do">
			<div class="period-field">
				<label for="paymentYear">帰属年</label>
				<select id="paymentYear" name="paymentYear">
					<c:forEach var="year" items="${paymentYears}">
						<option value="${year}" <c:if test="${year eq selectedYear}">selected</c:if>>${year}年</option>
					</c:forEach>
				</select>
			</div>
			<div class="period-field">
				<label for="paymentMonth">帰属月</label>
				<select id="paymentMonth" name="paymentMonth">
					<c:forEach var="month" begin="1" end="12">
						<option value="${month}" <c:if test="${month eq selectedMonth}">selected</c:if>>${month}月</option>
					</c:forEach>
				</select>
			</div>
			<div class="period-field">
				<label for="paymentRound">給与回次</label>
				<select id="paymentRound" name="paymentRound">
					<c:forEach var="round" begin="1" end="10">
						<option value="${round}" <c:if test="${round eq selectedRound}">selected</c:if>>給与 ${round}回</option>
					</c:forEach>
				</select>
			</div>
			<div class="period-field period-range">
				<label>精算期間</label>
				<input type="date" lang="ja-JP" name="calculationStart" value="${calculationStart}">
				<span>~</span>
				<input type="date" lang="ja-JP" name="calculationEnd" value="${calculationEnd}">
			</div>
			<div class="period-field">
				<label for="paymentDate">給与支給日</label>
				<input id="paymentDate" type="date" lang="ja-JP" name="paymentDate" value="${paymentDate}">
			</div>
			<button type="submit" class="button button-primary">照会</button>
			<div class="calc-switch-field">
				<span>計算方法</span>
				<input type="checkbox" id="calculationSwitch" name="calculationEnabled" value="Y">
				<label for="calculationSwitch"><b>ON</b><i>OFF</i></label>
			</div>
		</form>

		<section class="payroll-workspace">
			<div class="employee-area">
				<div class="section-toolbar">
					<div class="toolbar-buttons toolbar-buttons-left">
						<a class="button button-outline" href="#previous-payment-modal">過去の給与を呼び出す</a>
						<a class="button button-primary" href="#employee-add">新規追加</a>
					</div>
					<div class="toolbar-buttons toolbar-buttons-right">
						<button type="submit" form="employeeForm" class="button button-neutral">選択削除</button>
						<button type="submit" form="employeeForm" name="deleteType" value="ALL" class="button button-neutral">完全削除</button>
					</div>
				</div>
				<form id="employeeForm" method="post" action="${pageContext.request.contextPath}/payroll/management/employees/delete.do">
					<input type="hidden" name="paymentYear" value="${selectedYear}">
					<input type="hidden" name="paymentMonth" value="${selectedMonth}">
					<input type="hidden" name="paymentRound" value="${selectedRound}">
					<input type="hidden" name="incomeType" value="${incomeMode}">
					<div class="table-wrap employee-table-wrap">
						<table class="data-table employee-table">
							<thead><tr><th class="check-column">選択</th><th>区分</th><th>氏名</th><th>部署</th><th>支給総額</th><th>控除総額</th><th>差引支給額</th></tr></thead>
							<tbody>
								<c:forEach var="employee" items="${paymentEmployees}">
									<c:url var="payrollEmployeeUrl" value="/payroll/management.do"><c:param name="paymentYear" value="${selectedYear}"/><c:param name="paymentMonth" value="${selectedMonth}"/><c:param name="paymentRound" value="${selectedRound}"/><c:param name="incomeType" value="${incomeMode}"/><c:param name="employeeId" value="${employee.employeeId}"/></c:url>
									<tr class="<c:if test='${employee.employeeId eq selectedEmployee.employeeId}'>selected-row</c:if>">
										<td><input type="checkbox" name="employeeIds" value="${employee.employeeId}" aria-label="${employee.name}を選択"></td>
										<td><a class="employee-row-link" href="${payrollEmployeeUrl}"><ui:code-label value="${employee.employmentType}" /></a></td>
										<td><a class="employee-row-link" href="${payrollEmployeeUrl}"><c:out value="${employee.name}" /></a></td>
										<td><a class="employee-row-link" href="${payrollEmployeeUrl}"><c:out value="${employee.departmentName}" /></a></td>
										<td class="amount give"><a class="employee-row-link" href="${payrollEmployeeUrl}"><c:out value="${employee.grossPayment}" /></a></td>
										<td class="amount deduction"><a class="employee-row-link" href="${payrollEmployeeUrl}"><c:out value="${employee.totalDeduction}" /></a></td>
										<td class="amount"><a class="employee-row-link" href="${payrollEmployeeUrl}"><c:out value="${employee.netPayment}" /></a></td>
									</tr>
								</c:forEach>
								<c:if test="${empty paymentEmployees}">
									<tr><td colspan="7" class="empty-row">照会された給与対象の社員はありません。</td></tr>
								</c:if>
							</tbody>
						</table>
					</div>
				</form>
			</div>

			<div class="payment-area">
				<nav class="income-tabs" aria-label="所得区分">
					<a class="${incomeMode eq 'general' ? 'active' : ''}" href="?paymentYear=${selectedYear}&amp;paymentMonth=${selectedMonth}&amp;paymentRound=${selectedRound}&amp;employeeId=${selectedEmployee.employeeId}&amp;incomeType=general">一般所得</a>
					<a class="${incomeMode eq 'business' ? 'active' : ''}" href="?paymentYear=${selectedYear}&amp;paymentMonth=${selectedMonth}&amp;paymentRound=${selectedRound}&amp;employeeId=${selectedEmployee.employeeId}&amp;incomeType=business">事業所得/その他所得</a>
				</nav>

				<form class="payment-form" method="post" action="${pageContext.request.contextPath}/payroll/management/save.do">
					<input type="hidden" name="employeeId" value="${selectedEmployee.employeeId}">
					<input type="hidden" name="paymentYear" value="${selectedYear}">
					<input type="hidden" name="paymentMonth" value="${selectedMonth}">
					<input type="hidden" name="paymentRound" value="${selectedRound}">
					<input type="hidden" name="incomeType" value="${incomeMode}">
					<input type="hidden" name="calculationStart" value="${calculationStart}">
					<input type="hidden" name="calculationEnd" value="${calculationEnd}">
					<input type="hidden" name="paymentDate" value="${paymentDate}">

					<c:choose>
					<c:when test="${incomeMode eq 'general'}">
					<div class="amount-panels">
						<section class="amount-panel give-panel">
							<header><h2>支給項目</h2><a class="item-manage-button" href="#give-item-manager">項目管理</a></header>
							<table class="input-table">
								<thead><tr><th>項目</th><th>金額</th></tr></thead>
								<tbody>
									<c:forEach var="item" items="${paymentGiveItems}">
										<tr class="amount-row"><th><ui:code-label value="${item.itemName}" /><c:if test="${item.taxFree}"><em>[非]</em></c:if></th><td><input type="number" name="give_${item.itemCode}" value="${item.amount}" min="0"></td></tr>
										<tr class="calculation-row"><th>計算方法</th><td><input type="text" name="giveCalc_${item.itemCode}" value="${item.calculationMethod}" placeholder="計算方法"></td></tr>
									</c:forEach>
									<c:if test="${empty paymentGiveItems}">
										<tr><td colspan="2" class="empty-row">登録された支払い項目はありません。</td></tr>
									</c:if>
								</tbody>
							</table>
							<div class="panel-total"><strong>支給総額</strong><span><c:out value="${paymentTotals.grossPayment}" /> 円</span></div>
						</section>

						<section class="amount-panel deduction-panel">
							<header><h2>控除項目</h2><a class="item-manage-button" href="#deduction-item-manager">項目管理</a></header>
							<table class="input-table">
								<thead><tr><th>項目</th><th>金額</th></tr></thead>
								<tbody>
									<c:forEach var="item" items="${paymentDeductionItems}">
										<tr class="amount-row"><th><ui:code-label value="${item.itemName}" /></th><td><input type="number" name="deduction_${item.itemCode}" value="${item.amount}" min="0"></td></tr><tr class="calculation-row"><th>計算方法</th><td><input type="text" name="deductionCalc_${item.itemCode}" value="${item.calculationMethod}" placeholder="計算方法"></td></tr>
									</c:forEach>
									<c:if test="${empty paymentDeductionItems}">
										<tr><td colspan="2" class="empty-row">登録された控除項目はありません。</td></tr>
									</c:if>
								</tbody>
							</table>
							<div class="panel-total"><strong>控除総額</strong><span><c:out value="${paymentTotals.totalDeduction}" /> 円</span></div>
						</section>
					</div>
					</c:when>
					<c:otherwise>
					<div class="amount-panels business-panels">
						<section class="amount-panel give-panel"><header><h2>支給項目</h2><a class="item-manage-button" href="#give-item-manager">項目管理</a></header><table class="input-table"><thead><tr><th>項目</th><th>金額</th></tr></thead><tbody><tr class="amount-row"><th>事業所得</th><td><input type="number" name="businessIncome" value="${businessPayment.businessIncome}"></td></tr><tr class="calculation-row"><th>計算方法</th><td><input type="text" name="businessCalc" value="${businessPayment.businessCalculationMethod}" placeholder="計算方法"></td></tr><tr class="amount-row"><th>その他の収入</th><td><input type="number" name="otherIncome" value="${businessPayment.otherIncome}"></td></tr><tr class="calculation-row"><th>計算方法</th><td><input type="text" name="otherCalc" value="${businessPayment.otherCalculationMethod}" placeholder="計算方法"></td></tr></tbody></table><div class="panel-total"><strong>支給総額</strong><span><c:out value="${paymentTotals.grossPayment}" /> 円</span></div></section>
						<section class="amount-panel deduction-panel"><header><h2>控除項目</h2><a class="item-manage-button" href="#deduction-item-manager">項目管理</a></header><table class="input-table"><thead><tr><th>項目</th><th>金額</th></tr></thead><tbody><tr class="amount-row"><th>所得税</th><td><input type="number" name="businessTax" value="${businessPayment.incomeTax}"></td></tr><tr class="calculation-row"><th>計算方法</th><td><input type="text" name="businessTaxCalc" value="${businessPayment.incomeTaxCalculationMethod}" placeholder="計算方法"></td></tr><tr class="amount-row"><th>地方所得税</th><td><input type="number" name="businessLocalTax" value="${businessPayment.localIncomeTax}"></td></tr><tr class="calculation-row"><th>計算方法</th><td><input type="text" name="businessLocalTaxCalc" value="${businessPayment.localIncomeTaxCalculationMethod}" placeholder="計算方法"></td></tr></tbody></table><div class="panel-total"><strong>控除総額</strong><span><c:out value="${paymentTotals.totalDeduction}" /> 円</span></div></section>
					</div>
					</c:otherwise>
					</c:choose>

					<div class="net-payment"><span>差引支給額：</span><strong><c:out value="${paymentTotals.netPayment}" /></strong><em>円</em></div>
					<div class="form-actions"><button type="submit" class="button button-primary">保存</button><button type="reset" class="button button-neutral button-clear">内容を消去する</button></div>
				</form>
			</div>
		</section>
	</main>

	<div id="employee-add" class="modal-overlay">
		<section class="modal employee-modal" role="dialog" aria-modal="true" aria-labelledby="employee-add-title">
			<header><h2 id="employee-add-title">給与対象の社員を追加</h2><a href="#" aria-label="閉じる">&times;</a></header>
			<form method="post" action="${pageContext.request.contextPath}/payroll/management/employees/add.do">
				<input type="hidden" name="paymentYear" value="${selectedYear}">
				<input type="hidden" name="paymentMonth" value="${selectedMonth}">
				<input type="hidden" name="paymentRound" value="${selectedRound}">
				<input type="hidden" name="incomeType" value="${incomeMode}">
				<input type="hidden" name="calculationStart" value="${calculationStart}">
				<input type="hidden" name="calculationEnd" value="${calculationEnd}">
				<input type="hidden" name="paymentDate" value="${paymentDate}">
				<div class="modal-body">
					<div class="employee-modal-search"><input type="search" name="employeeKeyword" value="<c:out value='${param.employeeKeyword}' />" placeholder="社員検索"><button type="submit" name="action" value="search" class="modal-search-button">検索</button><select name="departmentId"><option value="">部署別</option><c:forEach var="department" items="${departments}"><option value="${department.departmentId}" ${param.departmentId eq department.departmentId ? 'selected' : ''}><c:out value="${department.departmentName}" /></option></c:forEach></select><select name="positionId"><option value="">役職別</option><c:forEach var="position" items="${positions}"><option value="${position.positionId}" ${param.positionId eq position.positionId ? 'selected' : ''}><c:out value="${position.positionName}" /></option></c:forEach></select><select name="status"><option value="WORK" ${empty param.status or param.status eq 'WORK' ? 'selected' : ''}>在職</option><option value="RETIRED" ${param.status eq 'RETIRED' ? 'selected' : ''}>退職</option></select></div>
					<table class="data-table"><thead><tr><th class="check-column">選択</th><th>区分</th><th>社員番号</th><th>氏名</th><th>部署</th><th>役職</th><th>ステータス</th></tr></thead><tbody>
						<c:forEach var="employee" items="${availableEmployees}"><tr><td><input type="checkbox" name="employeeIds" value="${employee.employeeId}"></td><td><ui:code-label value="${employee.employmentType}" /></td><td><c:out value="${employee.employeeNo}" /></td><td><c:out value="${employee.name}" /></td><td><c:out value="${employee.departmentName}" /></td><td><c:out value="${employee.positionName}" /></td><td><ui:code-label value="${employee.statusName}" /></td></tr></c:forEach>
						<c:if test="${empty availableEmployees}"><tr><td colspan="7" class="empty-row">追加する社員はありません。</td></tr></c:if>
					</tbody></table>
					<nav class="modal-pagination" aria-label="ページ移動">
						<c:if test="${employeePage gt 1}"><c:url var="previousEmployeePageUrl" value="/payroll/management.do"><c:param name="paymentYear" value="${selectedYear}" /><c:param name="paymentMonth" value="${selectedMonth}" /><c:param name="paymentRound" value="${selectedRound}" /><c:param name="incomeType" value="${incomeMode}" /><c:param name="employeeKeyword" value="${param.employeeKeyword}" /><c:param name="departmentId" value="${param.departmentId}" /><c:param name="positionId" value="${param.positionId}" /><c:param name="status" value="${param.status}" /><c:param name="employeePage" value="${employeePage - 1}" /></c:url><a href="${previousEmployeePageUrl}#employee-add">‹前</a></c:if>
						<strong><c:out value="${employeePage}" /></strong>
						<c:if test="${employeePage lt employeeTotalPages}"><c:url var="nextEmployeePageUrl" value="/payroll/management.do"><c:param name="paymentYear" value="${selectedYear}" /><c:param name="paymentMonth" value="${selectedMonth}" /><c:param name="paymentRound" value="${selectedRound}" /><c:param name="incomeType" value="${incomeMode}" /><c:param name="employeeKeyword" value="${param.employeeKeyword}" /><c:param name="departmentId" value="${param.departmentId}" /><c:param name="positionId" value="${param.positionId}" /><c:param name="status" value="${param.status}" /><c:param name="employeePage" value="${employeePage + 1}" /></c:url><a href="${nextEmployeePageUrl}#employee-add">次の›</a></c:if>
					</nav>
				</div>
				<div class="modal-actions"><button type="submit" class="button button-primary">社員を選択</button><a href="#" class="button button-neutral">選択解除</a></div>
			</form>
		</section>
	</div>

	<div id="give-item-manager" class="modal-overlay">
		<section class="modal item-modal" role="dialog" aria-modal="true" aria-labelledby="give-item-title">
			<header><h2 id="give-item-title">支払い項目の変更</h2><a href="#" aria-label="閉じる">&times;</a></header>
			<form method="post" action="${pageContext.request.contextPath}/payroll/management/give-item/save.do">
				<input type="hidden" name="paymentYear" value="${selectedYear}">
				<input type="hidden" name="paymentMonth" value="${selectedMonth}">
				<input type="hidden" name="paymentRound" value="${selectedRound}">
				<input type="hidden" name="incomeType" value="${incomeMode}">
				<div class="item-modal-body">
					<p class="item-guide">お支払い項目別に編集できます。計算の変更は、給与明細の設定で行ってください。</p>
					<div class="item-select-line"><select name="giveItemId"><option value="">支給項目を選択</option><c:forEach var="item" items="${allGiveItems}"><option value="${item.itemCode}"><ui:code-label value="${item.itemName}" /></option></c:forEach></select><button type="submit" name="action" value="requestDeleteAll" class="text-delete">すべての項目を削除</button></div>
					<label><span>支給項目</span><input type="text" name="itemName" placeholder="お支払い項目を入力してください。"></label>
					<div class="item-radio-line"><span>課税可</span><label><input type="radio" name="taxType" value="TAX" checked> 全体課税</label><label><input type="radio" name="taxType" value="FREE"> 非課税</label></div>
					<label class="tax-free-field"><span>非課税</span><select name="taxFreeName"><option value="">非課税/減免コードを選択</option><c:forEach var="taxFreeItem" items="${taxFreeItems}"><option value="${taxFreeItem.taxFreeCode}"><c:out value="${taxFreeItem.taxFreeName}" /> (<c:out value="${taxFreeItem.taxFreeCode}" />)</option></c:forEach></select></label>
					<label class="tax-free-field"><span>非課税限度額</span><span class="unit-field"><input type="number" name="taxFreeLimit" value="0" min="0"><em>円</em></span></label>
					<label><span>計算方法</span><input type="text" name="calculationMethod" placeholder="計算方法を入力してください。"></label>
					<label><span>端数処理単位</span><select name="roundingUnit"><option value="">選択してください。</option><option value="1">1円</option><option value="10">10円</option><option value="100">100円</option></select></label>
					<label><span>勤怠連携/一括支給</span><select name="attendanceLink"><option value="">選択してください。</option><c:forEach var="attendanceItem" items="${attendanceItems}"><c:if test="${attendanceItem.useYn eq 'Y'}"><option value="${attendanceItem.attendanceItemId}"><c:out value="${attendanceItem.attendName}" /></option></c:if></c:forEach><option value="BATCH">一括支給</option></select></label>
					<label class="batch-amount-field"><span>一括支払額</span><span class="unit-field"><input type="number" name="batchAmount" value="0" min="0"><em>円</em></span></label>
				</div>
				<div class="modal-actions"><button class="button button-primary" name="action" value="insert">追加</button><button class="button button-neutral" name="action" value="update">修正</button><button class="button button-neutral" name="action" value="requestDelete">削除</button></div>
			</form>
		</section>
	</div>

	<div id="previous-payment-modal" class="modal-overlay">
		<section class="modal previous-payment-modal" role="dialog" aria-modal="true" aria-labelledby="previous-payment-title">
			<header><h2 id="previous-payment-title">給与年月の選択</h2><a href="#" aria-label="閉じる">&times;</a></header>
			<form method="post" action="${pageContext.request.contextPath}/payroll/management/load-previous.do">
				<input type="hidden" name="paymentYear" value="${selectedYear}">
				<input type="hidden" name="paymentMonth" value="${selectedMonth}">
				<input type="hidden" name="paymentRound" value="${selectedRound}">
				<input type="hidden" name="incomeType" value="${incomeMode}">
				<input type="hidden" name="calculationStart" value="${calculationStart}">
				<input type="hidden" name="calculationEnd" value="${calculationEnd}">
				<input type="hidden" name="paymentDate" value="${paymentDate}">
				<div class="previous-payment-body">
					<select name="previousPaymentPeriod" required>
						<option value="">対象年月・回次を選択</option>
						<c:forEach var="period" items="${previousPaymentPeriods}"><option value="${period.periodId}"><c:out value="${period.periodName}" /></option></c:forEach>
					</select>
					<button type="submit" class="button button-primary">給与情報を読み込む</button>
				</div>
			</form>
		</section>
	</div>

	<div id="deduction-item-manager" class="modal-overlay">
		<section class="modal item-modal deduction-item-modal" role="dialog" aria-modal="true" aria-labelledby="deduction-item-title">
			<header><h2 id="deduction-item-title">控除項目の変更</h2><a href="#" aria-label="閉じる">&times;</a></header>
			<form method="post" action="${pageContext.request.contextPath}/payroll/management/deduction-item/save.do">
				<input type="hidden" name="paymentYear" value="${selectedYear}">
				<input type="hidden" name="paymentMonth" value="${selectedMonth}">
				<input type="hidden" name="paymentRound" value="${selectedRound}">
				<input type="hidden" name="incomeType" value="${incomeMode}">
				<div class="item-modal-body"><p class="item-guide">控除項目別に編集できます。計算の変更は、給与明細の設定で行ってください。</p><div class="item-select-line"><select name="deductionItemId"><option value="">控除項目の選択</option><c:forEach var="item" items="${allDeductionItems}"><option value="${item.itemCode}"><ui:code-label value="${item.itemName}" /></option></c:forEach></select><button type="submit" name="action" value="requestDeleteAll" class="text-delete">すべての項目を削除</button></div><label><span>控除項目</span><input type="text" name="itemName" placeholder="控除項目を入力してください。"></label><label><span>計算方法</span><input type="text" name="calculationMethod" placeholder="計算方法を入力してください。"></label><label><span>端数処理単位</span><select name="roundingUnit"><option value="">選択してください。</option><option value="1">1円</option><option value="10">10円</option><option value="100">100円</option></select></label><label><span>備考</span><input type="text" name="note"></label></div>
				<div class="modal-actions"><button class="button button-primary" name="action" value="insert">追加</button><button class="button button-neutral" name="action" value="update">修正</button><button class="button button-neutral" name="action" value="requestDelete">削除</button></div>
			</form>
		</section>
	</div>

	<c:if test="${not empty itemDeleteConfirmation}"><div class="item-delete-confirmation" role="alertdialog" aria-modal="true"><a class="item-delete-confirmation__backdrop" href="${pageContext.request.contextPath}/payroll/management.do?paymentYear=${selectedYear}&amp;paymentMonth=${selectedMonth}&amp;paymentRound=${selectedRound}&amp;incomeType=${incomeMode}" aria-label="削除のキャンセル"></a><form class="item-delete-confirmation__panel" method="post" action="${pageContext.request.contextPath}/payroll/management/${itemDeleteConfirmation eq 'GIVE' ? 'give-item' : 'deduction-item'}/save.do"><p><c:out value="${itemDeleteAll ? 'すべての項目を削除しますか？' : '選択した項目を削除しますか？'}" /></p><p class="warning">削除した項目は復元できません。</p><input type="hidden" name="paymentYear" value="${selectedYear}"><input type="hidden" name="paymentMonth" value="${selectedMonth}"><input type="hidden" name="paymentRound" value="${selectedRound}"><input type="hidden" name="incomeType" value="${incomeMode}"><c:if test="${not itemDeleteAll}"><input type="hidden" name="${itemDeleteConfirmation eq 'GIVE' ? 'giveItemId' : 'deductionItemId'}" value="${deleteItemId}"></c:if><div><button type="submit" name="action" value="${itemDeleteAll ? 'confirmDeleteAll' : 'confirmDelete'}">削除</button><a href="${pageContext.request.contextPath}/payroll/management.do?paymentYear=${selectedYear}&amp;paymentMonth=${selectedMonth}&amp;paymentRound=${selectedRound}&amp;incomeType=${incomeMode}">キャンセル</a></div></form></div></c:if>
	<c:if test="${not empty payrollPopupMessage}"><div class="item-delete-confirmation" role="alertdialog" aria-modal="true"><a class="item-delete-confirmation__backdrop" href="${pageContext.request.contextPath}/payroll/management.do?paymentYear=${selectedYear}&amp;paymentMonth=${selectedMonth}&amp;paymentRound=${selectedRound}&amp;incomeType=${incomeMode}" aria-label="閉じる"></a><div class="item-delete-confirmation__panel"><p><ui:message-label value="${payrollPopupMessage}" /></p><div><a class="popup-confirm" href="${pageContext.request.contextPath}/payroll/management.do?paymentYear=${selectedYear}&amp;paymentMonth=${selectedMonth}&amp;paymentRound=${selectedRound}&amp;incomeType=${incomeMode}">確認</a></div></div></div></c:if>

	<%@ include file="/WEB-INF/view/common/footer.jspf" %>
</body>
</html>
