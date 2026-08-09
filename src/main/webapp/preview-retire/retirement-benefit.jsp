<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>퇴직급여 입력/관리</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/retire/retirement-benefit.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/payzon-ui.css">
</head>
<body>
<%@ include file="/WEB-INF/view/common/header.jspf" %>
<main class="retirement-benefit-page page-content">
    <header class="page-heading"><div><p>퇴직관리</p><h1>퇴직급여 입력/관리</h1></div></header>

    <section class="benefit-card benefit-list-card">
        <div class="benefit-toolbar">
            <form method="get" action="${pageContext.request.contextPath}/retirement/benefit.do">
                <label for="paymentYear">지급년도</label>
                <select id="paymentYear" name="paymentYear">
                    <c:forEach var="year" items="${paymentYears}"><option value="${year}">${year}년</option></c:forEach>
                </select>
                <button type="submit" class="search-button">조회</button>
            </form>
            <a class="button button-primary new-button" href="#employee-select-modal">신규추가</a>
        </div>
        <table class="data-table benefit-list-table">
            <colgroup>
                <col class="payment-date-col"><col class="type-col"><col class="name-col">
                <col class="position-col"><col class="department-col"><col class="period-col">
                <col class="service-days-col"><col class="net-payment-col"><col class="payment-method-col">
            </colgroup>
            <thead><tr><th>지급일</th><th>구분</th><th>성명</th><th>직위</th><th>부서</th><th>산정기간</th><th>근속일수</th><th>실지급액</th><th>지급방법</th></tr></thead>
            <tbody>
            <c:forEach var="item" items="${retirementBenefits}">
                <c:url var="employeeBenefitUrl" value="/retirement/benefit.do">
                    <c:param name="employeeId" value="${item.employeeId}"></c:param>
                    <c:param name="paymentYear" value="${selectedYear}"></c:param>
                </c:url>
                <tr class="benefit-list-row ${param.employeeId eq item.employeeId ? 'is-selected' : ''}">
                    <td><a href="${employeeBenefitUrl}">${item.paymentDate}</a></td>
                    <td><a href="${employeeBenefitUrl}">${item.settlementType}</a></td>
                    <td><a href="${employeeBenefitUrl}">${item.employeeName}</a></td>
                    <td><a href="${employeeBenefitUrl}">${item.positionName}</a></td>
                    <td><a href="${employeeBenefitUrl}">${item.departmentName}</a></td>
                    <td><a href="${employeeBenefitUrl}">${item.calculationStartDate} ~ ${item.calculationEndDate}</a></td>
                    <td><a href="${employeeBenefitUrl}">${item.serviceDays}일</a></td>
                    <td class="amount"><a href="${employeeBenefitUrl}">${item.netPayment}원</a></td>
                    <td><a href="${employeeBenefitUrl}">${item.paymentMethod}</a></td>
                </tr>
            </c:forEach>
            <c:if test="${empty retirementBenefits}"><tr><td colspan="9" class="empty-row">등록된 퇴직급여 내역이 없습니다.</td></tr></c:if>
            </tbody>
        </table>
        <div class="list-actions"><button type="submit" form="deleteForm" class="button button-muted">선택삭제</button><button type="submit" form="deleteAllForm" class="button button-muted">전체삭제</button></div>
        <form id="deleteForm" method="post" action="${pageContext.request.contextPath}/retirement/benefit/delete.do"></form>
        <form id="deleteAllForm" method="post" action="${pageContext.request.contextPath}/retirement/benefit/delete-all.do"></form>
    </section>

    <form class="benefit-form" method="post" action="${pageContext.request.contextPath}/retirement/benefit/save.do">
        <input type="hidden" name="employeeId" value="${retirementBenefit.employeeId}">

        <section class="retirement-calc-bar">
            <label><span>구분</span><select name="settlementType"><option value="">선택</option><option value="RETIREMENT">퇴직정산</option><option value="INTERIM">중간정산</option></select></label>
            <label><span>입사일</span><input type="date" name="startDate" value="${retirementBenefit.startDate}"></label>
            <label><span>퇴직일</span><input type="date" name="endDate" value="${retirementBenefit.endDate}"></label>
            <div class="calc-value"><span>근속년수</span><b>${retirementBenefit.serviceYears}</b><em>년</em></div>
            <div class="calc-value"><span>근속일수</span><b>${retirementBenefit.serviceDays}</b><em>일</em></div>
            <label class="excluded-days"><span>제외일수</span><div class="unit-input"><input type="number" name="excludedDays" value="${retirementBenefit.excludedDays}"><em>일</em></div></label>
        </section>

        <div class="original-two-column">
            <section class="original-block">
                <div class="original-section-title"><h2>급여내역</h2><span>사유발생일 이전 최근 3개월 지급합계 금액</span><button type="submit" formaction="${pageContext.request.contextPath}/retirement/benefit/load-pay.do" class="small-button">급여내역 불러오기</button></div>
                <table class="original-table salary-history-table">
                    <thead><tr><th>산정기간</th><th>산정일수</th><th>급여총액</th></tr></thead>
                    <tbody><c:forEach begin="0" end="3"><tr><td><div class="date-range"><input type="date" name="salaryStartDate"><i>~</i><input type="date" name="salaryEndDate"></div></td><td><input type="number" name="salaryDays" value="0" readonly aria-readonly="true"></td><td><input type="text" name="salaryTotal"></td></tr></c:forEach></tbody>
                    <tfoot><tr><th>총 합계</th><td>${retirementBenefit.salaryDaysTotal}</td><td>${retirementBenefit.salaryTotal}</td></tr></tfoot>
                </table>
                <p class="warning-note">단, 중간일자 계산일 경우 해당월의 지급합계 금액에서 일수로 나눈 값을 기본으로 표시</p>
                <table class="original-table two-field-table"><thead><tr><th>퇴직위로금</th><th>해고예고수당</th></tr></thead><tbody><tr><td><input type="text" name="compensation" value="${retirementBenefit.compensation}"></td><td><input type="text" name="dismissalAllowance" value="${retirementBenefit.dismissalAllowance}"></td></tr></tbody></table>
            </section>

            <section class="original-block">
                <div class="original-section-title"><h2>기타 과세소득</h2><span>사유 발생일 이전 1년간의 금액 입력</span></div>
                <table class="original-table other-income-table">
                    <thead><tr><th>지급년월</th><th>지급항목</th><th>금액</th><th>3개월분</th></tr></thead>
                    <tbody><c:forEach begin="0" end="4"><tr><td><input type="month" name="otherIncomeMonth"></td><td><input type="text" name="otherIncomeItem"></td><td><input type="text" name="otherIncomeAmount"></td><td><input type="text" name="threeMonthAmount" readonly value="0"></td></tr></c:forEach></tbody>
                </table>
                <table class="original-table three-field-table"><thead><tr><th>비과세 퇴직급여</th><th>기납부(또는 기과세이연) 세액</th><th>세액공제</th></tr></thead><tbody><tr><td><input type="text" name="taxFreeRetirement"></td><td><input type="text" name="prepaidTax"></td><td><input type="text" name="taxCredit"></td></tr></tbody></table>
            </section>
        </div>

        <section class="original-block deferred-block">
            <div class="original-section-title"><h2>과세이연계좌</h2><span>해당 사항이 없는 경우 입력하지 않습니다.</span></div>
            <table class="original-table deferred-table"><thead><tr><th>퇴직연금사업자명</th><th>사업자등록번호</th><th>계좌번호</th><th>입금(이체)일</th><th>계좌입금금액</th></tr></thead><tbody><c:forEach begin="0" end="1"><tr><td><input type="text" name="pensionProvider"></td><td><input type="text" name="pensionBusinessNo"></td><td><input type="text" name="pensionAccount"></td><td><input type="date" name="pensionDate"></td><td><input type="text" name="pensionAmount"></td></tr></c:forEach></tbody></table>
            <p class="warning-note">연금계좌에 입금하여 퇴직소득세를 징수하지 않는 경우에 작성합니다.</p>
        </section>

        <div class="calculate-action"><button type="submit" formaction="${pageContext.request.contextPath}/retirement/benefit/calculate.do" class="button button-primary calculate-button">퇴직금 계산하기</button></div>

        <section class="original-block result-block">
            <table class="original-table result-table"><thead><tr><th>3개월 총계</th><th>1일 평균임금</th><th>1일 통상임금</th><th>퇴직소득</th><th>퇴직일이 속하는 과세연도</th><th>산출세액</th></tr></thead><tbody><tr><td>${retirementBenefit.threeMonthTotal}</td><td>${retirementBenefit.dailyAverage}</td><td><input type="text" name="dailyOrdinary" value="${retirementBenefit.dailyOrdinary}"></td><td><input type="text" name="retirementIncome" value="${retirementBenefit.retirementIncome}"></td><td>${retirementBenefit.taxYear}</td><td>${retirementBenefit.calculatedTax}</td></tr></tbody><thead><tr><th>퇴직소득세</th><th>지방소득세</th><th>이연 퇴직소득세</th><th>이연 지방소득세</th><th>농어촌특별세</th><th>기타공제</th></tr></thead><tbody><tr><td><input type="text" name="incomeTax" value="${retirementBenefit.incomeTax}"></td><td><input type="text" name="localIncomeTax" value="${retirementBenefit.localIncomeTax}"></td><td>${retirementBenefit.deferredIncomeTax}</td><td>${retirementBenefit.deferredLocalTax}</td><td><input type="text" name="ruralTax" value="${retirementBenefit.ruralTax}"></td><td><input type="text" name="otherDeduction" value="${retirementBenefit.otherDeduction}"></td></tr></tbody></table>
            <p class="warning-note">통상임금 입력 시 통상임금이 우선 적용되어 퇴직금이 계산됩니다.</p>
        </section>

        <section class="original-block payment-block">
            <table class="original-table payment-table"><thead><tr><th>과세대상 퇴직급여</th><th>차감원천징수세액</th><th>실수령액</th><th>지급방법</th><th>지급일</th></tr></thead><tbody><tr><td><strong>${retirementBenefit.taxablePayment}</strong> 원</td><td><strong>${retirementBenefit.withholdingTax}</strong> 원</td><td><strong>${retirementBenefit.netPayment}</strong> 원</td><td><input type="text" name="paymentMethod" value="${retirementBenefit.paymentMethod}"></td><td><input type="date" name="paymentDate" value="${retirementBenefit.paymentDate}"></td></tr></tbody></table>
        </section>
        <div class="bottom-actions"><button type="submit" class="button button-primary">저장</button><button type="reset" class="button button-muted">취소</button></div>
    </form>

    <section id="employee-select-modal" class="employee-select-overlay">
        <div class="employee-select-modal" role="dialog" aria-modal="true" aria-labelledby="employee-select-title">
            <header><h2 id="employee-select-title">퇴직급여지급 사원선택</h2><a href="#" class="modal-close" aria-label="닫기">×</a></header>
            <form method="get" action="${pageContext.request.contextPath}/retirement/benefit/employee-search.do"><div class="employee-modal-search"><input type="search" name="employeeKeyword" placeholder="사원검색"><button type="submit" class="search-button">검색</button><a href="#employee-select-modal" class="all-view">전체보기</a><select name="departmentId"><option value="">부서별</option><c:forEach var="department" items="${departments}"><option value="${department.id}">${department.name}</option></c:forEach></select></div></form>
            <form method="post" action="${pageContext.request.contextPath}/retirement/benefit/new.do">
                <div class="employee-select-table-wrap"><table class="data-table employee-select-table"><colgroup><col class="select-col"><col><col><col><col><col><col></colgroup><thead><tr><th>선택</th><th>구분</th><th>사원번호</th><th>성명</th><th>부서</th><th>직위</th><th>상태</th></tr></thead><tbody><c:forEach var="employee" items="${selectableEmployees}"><tr><td><input type="radio" name="employeeId" value="${employee.employeeId}"></td><td>${employee.employmentTypeName}</td><td>${employee.employeeNo}</td><td>${employee.name}</td><td>${employee.departmentName}</td><td>${employee.positionName}</td><td>${employee.statusName}</td></tr></c:forEach><c:if test="${empty selectableEmployees}"><tr><td colspan="7" class="empty-row">선택할 사원이 없습니다.</td></tr></c:if></tbody></table></div>
                <div class="employee-select-actions"><button type="submit" class="button button-primary">사원선택</button><a href="#" class="button button-muted">선택취소</a></div>
            </form>
        </div>
    </section>
</main>
<%@ include file="/WEB-INF/view/common/footer.jspf" %>
</body>
</html>
