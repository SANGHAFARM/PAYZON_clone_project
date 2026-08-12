<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>퇴직급여명세서</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/retirement/retirement-payslip.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/payzon-ui.css">
</head>
<body>
<%@ include file="/WEB-INF/view/common/header.jspf" %>
<main class="retirement-payslip-page page-content">
    <header class="page-heading"><div><p>퇴직관리</p><h1>퇴직급여명세서</h1></div></header>


    <div class="payslip-layout">
        <aside class="payslip-employee-panel">
            <form class="payslip-filter" method="get" action="${pageContext.request.contextPath}/retirement/payslip.do">
                <label for="payslipYear">지급년도</label>
                <select id="payslipYear" name="paymentYear"><c:forEach var="year" items="${paymentYears}"><option value="${year}">${year}년</option></c:forEach></select>
                <input type="search" name="keyword" value="${param.keyword}" placeholder="검색어 입력">
                <button type="submit" class="search-button">검색</button>
                <a class="all-view" href="${pageContext.request.contextPath}/retirement/payslip.do?paymentYear=${selectedYear}">전체보기</a>
            </form>
            <div class="payslip-employee-list">
                <table class="data-table">
                    <colgroup><col><col><col></colgroup>
                    <thead><tr><th>구분</th><th>성명</th><th>실지급액</th></tr></thead>
                    <tbody>
                    <c:forEach var="item" items="${retirementPayslips}">
                        <c:url var="payslipUrl" value="/retirement/payslip.do"><c:param name="employeeId" value="${item.employeeId}"/><c:param name="paymentYear" value="${selectedYear}"/></c:url>
                        <tr class="${param.employeeId eq item.employeeId ? 'is-selected' : ''}">
                            <td><a href="${payslipUrl}">${item.settlementType}</a></td><td><a href="${payslipUrl}">${item.employeeName}</a></td>
                            <td><a href="${payslipUrl}">${item.netPayment}</a></td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty retirementPayslips}"><tr><td colspan="3" class="empty-row">조회된 퇴직급여명세서가 없습니다.</td></tr></c:if>
                    </tbody>
                </table>
            </div>
        </aside>

        <section class="payslip-workspace">
            <article class="retirement-document">
                <header class="payslip-document-title">
                    <div class="company-logo"><c:choose><c:when test="${not empty company.logoUrl}"><img src="${company.logoUrl}" alt="회사 로고"></c:when><c:otherwise><span>회사 로고</span></c:otherwise></c:choose></div>
                    <h2>퇴 직 급 여 명 세 서</h2>
                </header>

                <section class="document-section">
                    <h3>사원정보</h3>
                    <table class="document-table employee-info-table"><tbody>
                    <tr><th>성명</th><td>${selectedPayslip.employeeName}</td><th>입사일</th><td>${selectedPayslip.joinDate}</td></tr>
                    <tr><th>부서</th><td>${selectedPayslip.departmentName}</td><th>퇴직일</th><td>${selectedPayslip.retirementDate}</td></tr>
                    <tr><th>직위</th><td>${selectedPayslip.positionName}</td><th>근속일수</th><td>${selectedPayslip.serviceDays}</td></tr>
                    </tbody></table>
                </section>

                <section class="document-section">
                    <h3>급여내역</h3>
                    <table class="document-table salary-detail-table">
                        <thead><tr><th rowspan="2">산정기간</th><c:forEach begin="0" end="3" var="row"><th>${selectedPayslip.salaryDetails[row].startDate}</th></c:forEach><th rowspan="2">계</th></tr><tr><c:forEach begin="0" end="3" var="row"><th>${selectedPayslip.salaryDetails[row].endDate}</th></c:forEach></tr></thead>
                        <tbody><tr><th>산정일수</th><c:forEach begin="0" end="3" var="row"><td>${selectedPayslip.salaryDetails[row].days}</td></c:forEach><td class="total-cell">${selectedPayslip.salaryDaysTotal}</td></tr><tr><th>급여총액</th><c:forEach begin="0" end="3" var="row"><td>${selectedPayslip.salaryDetails[row].amount}</td></c:forEach><td class="total-cell">${selectedPayslip.salaryTotal}</td></tr></tbody>
                    </table>
                </section>

                <section class="document-section">
                    <h3>기타소득</h3>
                    <table class="document-table other-income-detail"><thead><tr><th>지급항목</th><th>1년간 지급액</th><th>3개월분</th></tr></thead><tbody><c:forEach var="income" items="${selectedPayslip.otherIncomes}"><tr><td>${income.itemName}</td><td>${income.annualAmount}</td><td>${income.threeMonthAmount}</td></tr></c:forEach><c:if test="${empty selectedPayslip.otherIncomes}"><c:forEach begin="0" end="3"><tr><td></td><td></td><td></td></tr></c:forEach></c:if></tbody></table>
                    <table class="document-table additional-pay-table"><tbody><tr><th>퇴직위로금</th><td>${selectedPayslip.compensation}</td><th>해고예고수당</th><td>${selectedPayslip.dismissalAllowance}</td></tr></tbody></table>
                </section>

                <section class="document-section">
                    <h3>퇴직소득</h3>
                    <table class="document-table income-calculation-table"><tbody>
                    <tr><th rowspan="2">1일 평균임금</th><th>3개월 총계</th><td class="formula">급여총액 계 + 3개월분 기타소득 계</td><td>${selectedPayslip.threeMonthTotal}</td></tr>
                    <tr><th>1일 평균임금</th><td class="formula">3개월 총계 / 산정일수</td><td>${selectedPayslip.dailyAverage}</td></tr>
                    <tr><th>1일 통상임금</th><td colspan="2" class="formula">1일 통상임금이 1일 평균임금보다 높을 경우 적용</td><td>${selectedPayslip.dailyOrdinary}</td></tr>
                    <tr><th>퇴직소득</th><td colspan="2" class="formula">(1일 평균임금 × 30일 × 근속일수 / 365) + 퇴직위로금 + 해고예고수당</td><td>${selectedPayslip.retirementIncome}</td></tr>
                    </tbody></table>
                </section>

                <section class="document-section">
                    <h3>공제내역</h3>
                    <table class="document-table deduction-table"><thead><tr><th>퇴직소득세</th><th>지방소득세</th><th>기타공제</th><th>공제총액</th></tr></thead><tbody><tr><td>${selectedPayslip.incomeTax}</td><td>${selectedPayslip.localIncomeTax}</td><td>${selectedPayslip.otherDeduction}</td><td class="total-cell">${selectedPayslip.deductionTotal}</td></tr></tbody></table>
                </section>

                <div class="net-payment-row"><strong>실수령액</strong><span>퇴직급여 - 공제총액</span><b>${selectedPayslip.netPayment}</b></div>

                <footer class="payslip-document-footer">
                    <p>위 금액을 해당자의 퇴직금 정산액으로 정히 영수함.</p>
                    <div class="document-date"><input name="issueYear" value="${issueDate.year}" maxlength="4">년 <input name="issueMonth" value="${issueDate.month}" maxlength="2">월 <input name="issueDay" value="${issueDate.day}" maxlength="2">일</div>
                    <div class="company-signature"><label><input type="checkbox" name="showCeo" value="Y" checked> 대표자 표기</label><div><strong>${company.companyName}</strong><span>대표이사 ${company.ceoName}</span></div><div class="stamp-box"><c:choose><c:when test="${not empty company.stampUrl}"><img src="${company.stampUrl}" alt="회사 도장"></c:when><c:otherwise><span>회사 도장을<br>넣어주세요</span></c:otherwise></c:choose></div></div>
                    <table class="signature-table"><tbody><tr><td><div class="signature-party"><strong>근로자</strong><span>${selectedPayslip.employeeName}</span><em>인</em></div></td><td><div class="signature-party"><strong>사용자</strong><span>${company.ceoName}</span><em>인</em></div></td></tr></tbody></table>
                </footer>
            </article>
        </section>
    </div>
</main>
<%@ include file="/WEB-INF/view/common/footer.jspf" %>
</body>
</html>
