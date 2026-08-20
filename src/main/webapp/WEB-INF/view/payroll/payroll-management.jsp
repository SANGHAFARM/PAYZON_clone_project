<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="incomeMode" value="${empty param.incomeType ? (empty incomeType ? 'general' : incomeType) : param.incomeType}" />
<c:set var="employeePage" value="${empty param.employeePage ? 1 : param.employeePage}" />
<c:set var="employeeTotalPages" value="${empty availableEmployeePage.totalPages ? 1 : availableEmployeePage.totalPages}" />
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>급여입력/관리</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/common.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/payroll/payroll-management.css?v=20260815-2">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/payzon-ui.css?v=20260821-1">
</head>
<body>
	<%@ include file="/WEB-INF/view/common/header.jspf" %>

	<main class="page-content payment-management-page">
		<header class="page-heading">
			<div><p>급여관리</p><h1>급여입력/관리</h1></div>
		</header>

		<form class="period-panel" method="get" action="${pageContext.request.contextPath}/payroll/management.do">
			<div class="period-field">
				<label for="paymentYear">귀속연도</label>
				<select id="paymentYear" name="paymentYear">
					<c:forEach var="year" items="${paymentYears}">
						<option value="${year}" <c:if test="${year eq selectedYear}">selected</c:if>>${year}년</option>
					</c:forEach>
				</select>
			</div>
			<div class="period-field">
				<label for="paymentMonth">귀속월</label>
				<select id="paymentMonth" name="paymentMonth">
					<c:forEach var="month" begin="1" end="12">
						<option value="${month}" <c:if test="${month eq selectedMonth}">selected</c:if>>${month}월</option>
					</c:forEach>
				</select>
			</div>
			<div class="period-field">
				<label for="paymentRound">급여차수</label>
				<select id="paymentRound" name="paymentRound">
					<c:forEach var="round" begin="1" end="10">
						<option value="${round}" <c:if test="${round eq selectedRound}">selected</c:if>>급여-${round}차</option>
					</c:forEach>
				</select>
			</div>
			<div class="period-field period-range">
				<label>정산기간</label>
				<input type="date" name="calculationStart" value="${calculationStart}">
				<span>~</span>
				<input type="date" name="calculationEnd" value="${calculationEnd}">
			</div>
			<div class="period-field">
				<label for="paymentDate">급여지급일</label>
				<input id="paymentDate" type="date" name="paymentDate" value="${paymentDate}">
			</div>
			<button type="submit" class="button button-primary">조회</button>
			<div class="calc-switch-field">
				<span>계산방법</span>
				<input type="checkbox" id="calculationSwitch" name="calculationEnabled" value="Y">
				<label for="calculationSwitch"><b>ON</b><i>OFF</i></label>
			</div>
		</form>

		<section class="payroll-workspace">
			<div class="employee-area">
				<div class="section-toolbar">
					<div class="toolbar-buttons toolbar-buttons-left">
						<a class="button button-outline" href="#previous-payment-modal">지난급여 불러오기</a>
						<a class="button button-primary" href="#employee-add">신규추가</a>
					</div>
					<div class="toolbar-buttons toolbar-buttons-right">
						<button type="submit" form="employeeForm" class="button button-neutral">선택삭제</button>
						<button type="submit" form="employeeForm" name="deleteType" value="ALL" class="button button-neutral">전체삭제</button>
					</div>
				</div>
				<form id="employeeForm" method="post" action="${pageContext.request.contextPath}/payroll/management/employees/delete.do">
					<input type="hidden" name="paymentYear" value="${selectedYear}">
					<input type="hidden" name="paymentMonth" value="${selectedMonth}">
					<input type="hidden" name="paymentRound" value="${selectedRound}">
					<input type="hidden" name="incomeType" value="${incomeMode}">
					<div class="table-wrap employee-table-wrap">
						<table class="data-table employee-table">
							<thead><tr><th class="check-column">선택</th><th>구분</th><th>성명</th><th>부서</th><th>지급총액</th><th>공제총액</th><th>실지급액</th></tr></thead>
							<tbody>
								<c:forEach var="employee" items="${paymentEmployees}">
									<c:url var="payrollEmployeeUrl" value="/payroll/management.do"><c:param name="paymentYear" value="${selectedYear}"/><c:param name="paymentMonth" value="${selectedMonth}"/><c:param name="paymentRound" value="${selectedRound}"/><c:param name="incomeType" value="${incomeMode}"/><c:param name="employeeId" value="${employee.employeeId}"/></c:url>
									<tr class="<c:if test='${employee.employeeId eq selectedEmployee.employeeId}'>selected-row</c:if>">
										<td><input type="checkbox" name="employeeIds" value="${employee.employeeId}" aria-label="${employee.name} 선택"></td>
										<td><a class="employee-row-link" href="${payrollEmployeeUrl}"><c:out value="${employee.employmentType}" /></a></td>
										<td><a class="employee-row-link" href="${payrollEmployeeUrl}"><c:out value="${employee.name}" /></a></td>
										<td><a class="employee-row-link" href="${payrollEmployeeUrl}"><c:out value="${employee.departmentName}" /></a></td>
										<td class="amount give"><a class="employee-row-link" href="${payrollEmployeeUrl}"><c:out value="${employee.grossPayment}" /></a></td>
										<td class="amount deduction"><a class="employee-row-link" href="${payrollEmployeeUrl}"><c:out value="${employee.totalDeduction}" /></a></td>
										<td class="amount"><a class="employee-row-link" href="${payrollEmployeeUrl}"><c:out value="${employee.netPayment}" /></a></td>
									</tr>
								</c:forEach>
								<c:if test="${empty paymentEmployees}">
									<tr><td colspan="7" class="empty-row">조회된 급여 대상 사원이 없습니다.</td></tr>
								</c:if>
							</tbody>
						</table>
					</div>
				</form>
			</div>

			<div class="payment-area">
				<nav class="income-tabs" aria-label="소득구분">
					<a class="${incomeMode eq 'general' ? 'active' : ''}" href="?paymentYear=${selectedYear}&amp;paymentMonth=${selectedMonth}&amp;paymentRound=${selectedRound}&amp;employeeId=${selectedEmployee.employeeId}&amp;incomeType=general">일반소득</a>
					<a class="${incomeMode eq 'business' ? 'active' : ''}" href="?paymentYear=${selectedYear}&amp;paymentMonth=${selectedMonth}&amp;paymentRound=${selectedRound}&amp;employeeId=${selectedEmployee.employeeId}&amp;incomeType=business">사업소득/기타소득</a>
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
							<header><h2>지급항목</h2><a class="item-manage-button" href="#give-item-manager">항목관리</a></header>
							<table class="input-table">
								<thead><tr><th>항목</th><th>금액</th></tr></thead>
								<tbody>
									<c:forEach var="item" items="${paymentGiveItems}">
										<tr class="amount-row"><th><c:out value="${item.itemName}" /><c:if test="${item.taxFree}"><em>[비]</em></c:if></th><td><input type="number" name="give_${item.itemCode}" value="${item.amount}" min="0"></td></tr>
										<tr class="calculation-row"><th>계산방법</th><td><input type="text" name="giveCalc_${item.itemCode}" value="${item.calculationMethod}" placeholder="계산방법"></td></tr>
									</c:forEach>
									<c:if test="${empty paymentGiveItems}">
										<tr><td colspan="2" class="empty-row">등록된 지급항목이 없습니다.</td></tr>
									</c:if>
								</tbody>
							</table>
							<div class="panel-total"><strong>지급총액</strong><span><c:out value="${paymentTotals.grossPayment}" /> 원</span></div>
						</section>

						<section class="amount-panel deduction-panel">
							<header><h2>공제항목</h2><a class="item-manage-button" href="#deduction-item-manager">항목관리</a></header>
							<table class="input-table">
								<thead><tr><th>항목</th><th>금액</th></tr></thead>
								<tbody>
									<c:forEach var="item" items="${paymentDeductionItems}">
										<tr class="amount-row"><th><c:out value="${item.itemName}" /></th><td><input type="number" name="deduction_${item.itemCode}" value="${item.amount}" min="0"></td></tr><tr class="calculation-row"><th>계산방법</th><td><input type="text" name="deductionCalc_${item.itemCode}" value="${item.calculationMethod}" placeholder="계산방법"></td></tr>
									</c:forEach>
									<c:if test="${empty paymentDeductionItems}">
										<tr><td colspan="2" class="empty-row">등록된 공제항목이 없습니다.</td></tr>
									</c:if>
								</tbody>
							</table>
							<div class="panel-total"><strong>공제총액</strong><span><c:out value="${paymentTotals.totalDeduction}" /> 원</span></div>
						</section>
					</div>
					</c:when>
					<c:otherwise>
					<div class="amount-panels business-panels">
						<section class="amount-panel give-panel"><header><h2>지급항목</h2><a class="item-manage-button" href="#give-item-manager">항목관리</a></header><table class="input-table"><thead><tr><th>항목</th><th>금액</th></tr></thead><tbody><tr class="amount-row"><th>사업소득</th><td><input type="number" name="businessIncome" value="${businessPayment.businessIncome}"></td></tr><tr class="calculation-row"><th>계산방법</th><td><input type="text" name="businessCalc" value="${businessPayment.businessCalculationMethod}" placeholder="계산방법"></td></tr><tr class="amount-row"><th>기타소득</th><td><input type="number" name="otherIncome" value="${businessPayment.otherIncome}"></td></tr><tr class="calculation-row"><th>계산방법</th><td><input type="text" name="otherCalc" value="${businessPayment.otherCalculationMethod}" placeholder="계산방법"></td></tr></tbody></table><div class="panel-total"><strong>지급총액</strong><span><c:out value="${paymentTotals.grossPayment}" /> 원</span></div></section>
						<section class="amount-panel deduction-panel"><header><h2>공제항목</h2><a class="item-manage-button" href="#deduction-item-manager">항목관리</a></header><table class="input-table"><thead><tr><th>항목</th><th>금액</th></tr></thead><tbody><tr class="amount-row"><th>소득세</th><td><input type="number" name="businessTax" value="${businessPayment.incomeTax}"></td></tr><tr class="calculation-row"><th>계산방법</th><td><input type="text" name="businessTaxCalc" value="${businessPayment.incomeTaxCalculationMethod}" placeholder="계산방법"></td></tr><tr class="amount-row"><th>지방소득세</th><td><input type="number" name="businessLocalTax" value="${businessPayment.localIncomeTax}"></td></tr><tr class="calculation-row"><th>계산방법</th><td><input type="text" name="businessLocalTaxCalc" value="${businessPayment.localIncomeTaxCalculationMethod}" placeholder="계산방법"></td></tr></tbody></table><div class="panel-total"><strong>공제총액</strong><span><c:out value="${paymentTotals.totalDeduction}" /> 원</span></div></section>
					</div>
					</c:otherwise>
					</c:choose>

					<div class="net-payment"><span>실지급액:</span><strong><c:out value="${paymentTotals.netPayment}" /></strong><em>원</em></div>
					<div class="form-actions"><button type="submit" class="button button-primary">저장</button><button type="reset" class="button button-neutral button-clear">내용지우기</button></div>
				</form>
			</div>
		</section>
	</main>

	<div id="employee-add" class="modal-overlay">
		<section class="modal employee-modal" role="dialog" aria-modal="true" aria-labelledby="employee-add-title">
			<header><h2 id="employee-add-title">급여 대상 사원 추가</h2><a href="#" aria-label="닫기">&times;</a></header>
			<form method="post" action="${pageContext.request.contextPath}/payroll/management/employees/add.do">
				<input type="hidden" name="paymentYear" value="${selectedYear}">
				<input type="hidden" name="paymentMonth" value="${selectedMonth}">
				<input type="hidden" name="paymentRound" value="${selectedRound}">
				<input type="hidden" name="incomeType" value="${incomeMode}">
				<input type="hidden" name="calculationStart" value="${calculationStart}">
				<input type="hidden" name="calculationEnd" value="${calculationEnd}">
				<input type="hidden" name="paymentDate" value="${paymentDate}">
				<div class="modal-body">
					<div class="employee-modal-search"><input type="search" name="employeeKeyword" value="<c:out value='${param.employeeKeyword}' />" placeholder="사원검색"><button type="submit" name="action" value="search" class="modal-search-button">검색</button><select name="departmentId"><option value="">부서별</option><c:forEach var="department" items="${departments}"><option value="${department.departmentId}" ${param.departmentId eq department.departmentId ? 'selected' : ''}><c:out value="${department.departmentName}" /></option></c:forEach></select><select name="positionId"><option value="">직위별</option><c:forEach var="position" items="${positions}"><option value="${position.positionId}" ${param.positionId eq position.positionId ? 'selected' : ''}><c:out value="${position.positionName}" /></option></c:forEach></select><select name="status"><option value="WORK" ${empty param.status or param.status eq 'WORK' ? 'selected' : ''}>재직</option><option value="RETIRED" ${param.status eq 'RETIRED' ? 'selected' : ''}>퇴직</option></select></div>
					<table class="data-table"><thead><tr><th class="check-column">선택</th><th>구분</th><th>사원번호</th><th>성명</th><th>부서</th><th>직위</th><th>상태</th></tr></thead><tbody>
						<c:forEach var="employee" items="${availableEmployees}"><tr><td><input type="checkbox" name="employeeIds" value="${employee.employeeId}"></td><td><c:out value="${employee.employmentType}" /></td><td><c:out value="${employee.employeeNo}" /></td><td><c:out value="${employee.name}" /></td><td><c:out value="${employee.departmentName}" /></td><td><c:out value="${employee.positionName}" /></td><td><c:out value="${employee.statusName}" /></td></tr></c:forEach>
						<c:if test="${empty availableEmployees}"><tr><td colspan="7" class="empty-row">추가할 사원이 없습니다.</td></tr></c:if>
					</tbody></table>
					<nav class="modal-pagination" aria-label="페이지 이동">
						<c:if test="${employeePage gt 1}"><c:url var="previousEmployeePageUrl" value="/payroll/management.do"><c:param name="paymentYear" value="${selectedYear}" /><c:param name="paymentMonth" value="${selectedMonth}" /><c:param name="paymentRound" value="${selectedRound}" /><c:param name="incomeType" value="${incomeMode}" /><c:param name="employeeKeyword" value="${param.employeeKeyword}" /><c:param name="departmentId" value="${param.departmentId}" /><c:param name="positionId" value="${param.positionId}" /><c:param name="status" value="${param.status}" /><c:param name="employeePage" value="${employeePage - 1}" /></c:url><a href="${previousEmployeePageUrl}#employee-add">‹ 이전</a></c:if>
						<strong><c:out value="${employeePage}" /></strong>
						<c:if test="${employeePage lt employeeTotalPages}"><c:url var="nextEmployeePageUrl" value="/payroll/management.do"><c:param name="paymentYear" value="${selectedYear}" /><c:param name="paymentMonth" value="${selectedMonth}" /><c:param name="paymentRound" value="${selectedRound}" /><c:param name="incomeType" value="${incomeMode}" /><c:param name="employeeKeyword" value="${param.employeeKeyword}" /><c:param name="departmentId" value="${param.departmentId}" /><c:param name="positionId" value="${param.positionId}" /><c:param name="status" value="${param.status}" /><c:param name="employeePage" value="${employeePage + 1}" /></c:url><a href="${nextEmployeePageUrl}#employee-add">다음 ›</a></c:if>
					</nav>
				</div>
				<div class="modal-actions"><button type="submit" class="button button-primary">사원선택</button><a href="#" class="button button-neutral">선택취소</a></div>
			</form>
		</section>
	</div>

	<div id="give-item-manager" class="modal-overlay">
		<section class="modal item-modal" role="dialog" aria-modal="true" aria-labelledby="give-item-title">
			<header><h2 id="give-item-title">지급항목 변경</h2><a href="#" aria-label="닫기">&times;</a></header>
			<form method="post" action="${pageContext.request.contextPath}/payroll/management/give-item/save.do">
				<input type="hidden" name="paymentYear" value="${selectedYear}">
				<input type="hidden" name="paymentMonth" value="${selectedMonth}">
				<input type="hidden" name="paymentRound" value="${selectedRound}">
				<input type="hidden" name="incomeType" value="${incomeMode}">
				<div class="item-modal-body">
					<p class="item-guide">지급항목별로 수정하실 수 있습니다. 계산 수정은 급여항목 설정에서 진행하세요.</p>
					<div class="item-select-line"><select name="giveItemId"><option value="">지급항목 선택</option><c:forEach var="item" items="${allGiveItems}"><option value="${item.itemCode}"><c:out value="${item.itemName}" /></option></c:forEach></select><button type="submit" name="action" value="requestDeleteAll" class="text-delete">전체항목 삭제</button></div>
					<label><span>지급항목</span><input type="text" name="itemName" placeholder="지급 항목을 입력해주세요."></label>
					<div class="item-radio-line"><span>과세여부</span><label><input type="radio" name="taxType" value="TAX" checked> 전체과세</label><label><input type="radio" name="taxType" value="FREE"> 비과세</label></div>
					<label class="tax-free-field"><span>비과세명</span><select name="taxFreeName"><option value="">비과세/감면코드 선택</option><c:forEach var="taxFreeItem" items="${taxFreeItems}"><option value="${taxFreeItem.taxFreeCode}"><c:out value="${taxFreeItem.taxFreeName}" /> (<c:out value="${taxFreeItem.taxFreeCode}" />)</option></c:forEach></select></label>
					<label class="tax-free-field"><span>비과세 한도액</span><span class="unit-field"><input type="number" name="taxFreeLimit" value="0" min="0"><em>원</em></span></label>
					<label><span>계산방법</span><input type="text" name="calculationMethod" placeholder="계산방법을 입력해주세요."></label>
					<label><span>절사단위</span><select name="roundingUnit"><option value="">선택하세요.</option><option value="1">1원</option><option value="10">10원</option><option value="100">100원</option></select></label>
					<label><span>근태연계/일괄지급</span><select name="attendanceLink"><option value="">선택하세요.</option><c:forEach var="attendanceItem" items="${attendanceItems}"><c:if test="${attendanceItem.useYn eq 'Y'}"><option value="${attendanceItem.attendanceItemId}"><c:out value="${attendanceItem.attendName}" /></option></c:if></c:forEach><option value="BATCH">일괄지급</option></select></label>
					<label class="batch-amount-field"><span>일괄지급액</span><span class="unit-field"><input type="number" name="batchAmount" value="0" min="0"><em>원</em></span></label>
				</div>
				<div class="modal-actions"><button class="button button-primary" name="action" value="insert">추가</button><button class="button button-neutral" name="action" value="update">수정</button><button class="button button-neutral" name="action" value="requestDelete">삭제</button></div>
			</form>
		</section>
	</div>

	<div id="previous-payment-modal" class="modal-overlay">
		<section class="modal previous-payment-modal" role="dialog" aria-modal="true" aria-labelledby="previous-payment-title">
			<header><h2 id="previous-payment-title">급여연월 선택</h2><a href="#" aria-label="닫기">&times;</a></header>
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
						<option value="">귀속년월 차수 선택</option>
						<c:forEach var="period" items="${previousPaymentPeriods}"><option value="${period.periodId}"><c:out value="${period.periodName}" /></option></c:forEach>
					</select>
					<button type="submit" class="button button-primary">급여정보 불러오기</button>
				</div>
			</form>
		</section>
	</div>

	<div id="deduction-item-manager" class="modal-overlay">
		<section class="modal item-modal deduction-item-modal" role="dialog" aria-modal="true" aria-labelledby="deduction-item-title">
			<header><h2 id="deduction-item-title">공제항목 변경</h2><a href="#" aria-label="닫기">&times;</a></header>
			<form method="post" action="${pageContext.request.contextPath}/payroll/management/deduction-item/save.do">
				<input type="hidden" name="paymentYear" value="${selectedYear}">
				<input type="hidden" name="paymentMonth" value="${selectedMonth}">
				<input type="hidden" name="paymentRound" value="${selectedRound}">
				<input type="hidden" name="incomeType" value="${incomeMode}">
				<div class="item-modal-body"><p class="item-guide">공제항목별로 수정하실 수 있습니다. 계산 변경은 급여항목 설정에서 진행하세요.</p><div class="item-select-line"><select name="deductionItemId"><option value="">공제항목 선택</option><c:forEach var="item" items="${allDeductionItems}"><option value="${item.itemCode}"><c:out value="${item.itemName}" /></option></c:forEach></select><button type="submit" name="action" value="requestDeleteAll" class="text-delete">전체항목 삭제</button></div><label><span>공제항목</span><input type="text" name="itemName" placeholder="공제항목을 입력해주세요."></label><label><span>계산방법</span><input type="text" name="calculationMethod" placeholder="계산방법을 입력해주세요."></label><label><span>절사단위</span><select name="roundingUnit"><option value="">선택하세요.</option><option value="1">1원</option><option value="10">10원</option><option value="100">100원</option></select></label><label><span>비고</span><input type="text" name="note"></label></div>
				<div class="modal-actions"><button class="button button-primary" name="action" value="insert">추가</button><button class="button button-neutral" name="action" value="update">수정</button><button class="button button-neutral" name="action" value="requestDelete">삭제</button></div>
			</form>
		</section>
	</div>

	<c:if test="${not empty itemDeleteConfirmation}"><div class="item-delete-confirmation" role="alertdialog" aria-modal="true"><a class="item-delete-confirmation__backdrop" href="${pageContext.request.contextPath}/payroll/management.do?paymentYear=${selectedYear}&amp;paymentMonth=${selectedMonth}&amp;paymentRound=${selectedRound}&amp;incomeType=${incomeMode}" aria-label="삭제 취소"></a><form class="item-delete-confirmation__panel" method="post" action="${pageContext.request.contextPath}/payroll/management/${itemDeleteConfirmation eq 'GIVE' ? 'give-item' : 'deduction-item'}/save.do"><p><c:out value="${itemDeleteAll ? '전체 항목을 삭제하시겠습니까?' : '선택한 항목을 삭제하시겠습니까?'}" /></p><p class="warning">삭제한 항목은 복구할 수 없습니다.</p><input type="hidden" name="paymentYear" value="${selectedYear}"><input type="hidden" name="paymentMonth" value="${selectedMonth}"><input type="hidden" name="paymentRound" value="${selectedRound}"><input type="hidden" name="incomeType" value="${incomeMode}"><c:if test="${not itemDeleteAll}"><input type="hidden" name="${itemDeleteConfirmation eq 'GIVE' ? 'giveItemId' : 'deductionItemId'}" value="${deleteItemId}"></c:if><div><button type="submit" name="action" value="${itemDeleteAll ? 'confirmDeleteAll' : 'confirmDelete'}">삭제</button><a href="${pageContext.request.contextPath}/payroll/management.do?paymentYear=${selectedYear}&amp;paymentMonth=${selectedMonth}&amp;paymentRound=${selectedRound}&amp;incomeType=${incomeMode}">취소</a></div></form></div></c:if>
	<c:if test="${not empty payrollPopupMessage}"><div class="item-delete-confirmation" role="alertdialog" aria-modal="true"><a class="item-delete-confirmation__backdrop" href="${pageContext.request.contextPath}/payroll/management.do?paymentYear=${selectedYear}&amp;paymentMonth=${selectedMonth}&amp;paymentRound=${selectedRound}&amp;incomeType=${incomeMode}" aria-label="닫기"></a><div class="item-delete-confirmation__panel"><p><c:out value="${payrollPopupMessage}" /></p><div><a class="popup-confirm" href="${pageContext.request.contextPath}/payroll/management.do?paymentYear=${selectedYear}&amp;paymentMonth=${selectedMonth}&amp;paymentRound=${selectedRound}&amp;incomeType=${incomeMode}">확인</a></div></div></div></c:if>

	<%@ include file="/WEB-INF/view/common/footer.jspf" %>
</body>
</html>
