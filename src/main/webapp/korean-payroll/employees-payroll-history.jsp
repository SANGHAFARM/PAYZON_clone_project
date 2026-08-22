<%-- 조회 결과와 입력 양식을 표시하는 JSP 화면이다. --%>
<%-- 照会結果と入力フォームを表示するJSP画面である。 --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>사원별 급여내역</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/payroll/employees-payroll-history.css?v=20260815-1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/payzon-ui.css?v=20260821-1">
</head>
<body>
    <%@ include file="/WEB-INF/view/common/header.jspf" %>
    <main class="page-content employee-payment-history-page">
        <header class="page-heading"><div><p>급여관리</p><h1>사원별 급여내역</h1></div></header>
        <section class="history-card">
            <form class="history-search" method="get" action="${pageContext.request.contextPath}/payroll/employee-history.do">
                <div class="search-field employee-field">
                    <label for="employeeName">사원선택</label>
                    <div class="employee-selector"><input type="hidden" name="employeeId" value="${selectedEmployee.employeeId}"><input id="employeeName" type="text" value="${selectedEmployee.employeeName}" placeholder="사원을 선택해주세요" readonly><a href="#employeeSelectModal" class="button button-outline">사원선택</a><button type="submit" name="mode" value="history" class="button button-primary history-submit">급여내역 조회</button></div>
                </div>
                <div class="period-search-group"><div class="search-field period-field"><label for="startMonth">기간선택</label><div class="month-range"><input id="startMonth" name="startMonth" type="month" value="${startMonth}"><span>~</span><input id="endMonth" name="endMonth" type="month" value="${endMonth}"></div></div><button type="submit" name="mode" value="history" class="button button-primary period-submit">조회</button></div>
            </form>
            <div class="history-table-wrap">
                <table class="history-table">
                    <colgroup><col class="month-col"><col><col><col><col><col><col><col><col><col><col></colgroup>
                    <thead>
                        <tr><th colspan="5" class="group-title payment-group">월별 급여내역</th><th colspan="6" class="group-title deduction-group">4대보험 및 갑근세 내역</th></tr>
                        <tr><th>급여월(차수)</th><th>보수월액</th><th>지급합계</th><th>공제합계</th><th>실지급액</th><th>국민연금</th><th>건강보험</th><th>노인장기요양보험</th><th>고용보험</th><th>소득세</th><th>주민세</th></tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${not empty paymentHistories}">
                            <c:forEach var="history" items="${paymentHistories}"><tr><td>${history.paymentMonth}(${history.paymentRound})</td><td class="amount">${history.standardMonthlyIncome}</td><td class="amount payment-amount">${history.totalPayment}</td><td class="amount deduction-amount">${history.totalDeduction}</td><td class="amount net-amount">${history.netPayment}</td><td class="amount">${history.nationalPension}</td><td class="amount">${history.healthInsurance}</td><td class="amount">${history.longTermCareInsurance}</td><td class="amount">${history.employmentInsurance}</td><td class="amount">${history.incomeTax}</td><td class="amount">${history.localIncomeTax}</td></tr></c:forEach>
                        </c:when>
                        <c:otherwise><tr><td colspan="11" class="empty-row">조회된 사원별 급여내역이 없습니다.</td></tr></c:otherwise>
                    </c:choose>
                    </tbody>
                    <c:if test="${not empty paymentHistories}"><tfoot><tr><th>합계</th><td class="amount">${paymentHistoryTotal.standardMonthlyIncome}</td><td class="amount payment-amount">${paymentHistoryTotal.totalPayment}</td><td class="amount deduction-amount">${paymentHistoryTotal.totalDeduction}</td><td class="amount net-amount">${paymentHistoryTotal.netPayment}</td><td class="amount">${paymentHistoryTotal.nationalPension}</td><td class="amount">${paymentHistoryTotal.healthInsurance}</td><td class="amount">${paymentHistoryTotal.longTermCareInsurance}</td><td class="amount">${paymentHistoryTotal.employmentInsurance}</td><td class="amount">${paymentHistoryTotal.incomeTax}</td><td class="amount">${paymentHistoryTotal.localIncomeTax}</td></tr></tfoot></c:if>
                </table>
            </div>
            <nav class="pagination" aria-label="급여내역 페이지">
                <c:if test="${not empty pageInfo}">
                <c:if test="${pageInfo.hasPrevious}"><a href="?mode=history&amp;page=${pageInfo.previousPage}&amp;startMonth=${startMonth}&amp;endMonth=${endMonth}&amp;employeeId=${selectedEmployee.employeeId}">이전</a></c:if>
                <c:forEach var="pageNumber" begin="${pageInfo.startPage}" end="${pageInfo.endPage}"><a class="${pageNumber eq pageInfo.currentPage ? 'active' : ''}" href="?mode=history&amp;page=${pageNumber}&amp;startMonth=${startMonth}&amp;endMonth=${endMonth}&amp;employeeId=${selectedEmployee.employeeId}">${pageNumber}</a></c:forEach>
                <c:if test="${pageInfo.hasNext}"><a href="?mode=history&amp;page=${pageInfo.nextPage}&amp;startMonth=${startMonth}&amp;endMonth=${endMonth}&amp;employeeId=${selectedEmployee.employeeId}">다음</a></c:if>
                </c:if>
            </nav>
            <div class="history-actions"><a class="button button-outline" href="${pageContext.request.contextPath}/payroll/register.do">급여대장 목록</a></div>
        </section>
    </main>
    <section id="employeeSelectModal" class="modal-layer" role="dialog" aria-modal="true" aria-labelledby="employeeModalTitle">
        <a href="#" class="modal-backdrop" aria-label="팝업 닫기"></a>
        <div class="modal-dialog employee-modal">
            <header class="modal-header"><h2 id="employeeModalTitle">급여내역 조회 사원선택</h2><a href="#" class="modal-close" aria-label="닫기">×</a></header>
            <div class="modal-body">
                <form class="modal-search" method="get" action="${pageContext.request.contextPath}/payroll/employee-history.do#employeeSelectModal">
                    <input type="hidden" name="employeeId" value="${selectedEmployee.employeeId}">
                    <input type="hidden" name="startMonth" value="${startMonth}">
                    <input type="hidden" name="endMonth" value="${endMonth}">
                    <input type="search" name="employeeKeyword" value="${param.employeeKeyword}" placeholder="사원검색"><button type="submit" class="button button-primary">검색</button>
                    <div class="modal-filters"><select name="departmentId"><option value="">부서별</option><c:forEach var="department" items="${departments}"><option value="${department.departmentId}" ${param.departmentId eq department.departmentId ? 'selected' : ''}>${department.departmentName}</option></c:forEach></select><select name="status"><option value="">상태별</option><option value="ACTIVE" ${param.status eq 'ACTIVE' ? 'selected' : ''}>재직</option><option value="RETIRED" ${param.status eq 'RETIRED' ? 'selected' : ''}>퇴직</option></select></div>
                </form>
                <form class="employee-selection-form" method="get" action="${pageContext.request.contextPath}/payroll/employee-history.do">
                <input type="hidden" name="mode" value="select">
                <input type="hidden" name="startMonth" value="${startMonth}"><input type="hidden" name="endMonth" value="${endMonth}">
                <div class="modal-table-wrap"><table class="employee-select-table"><thead><tr><th>선택</th><th>구분</th><th>사원번호</th><th>성명</th><th>부서</th><th>직위</th><th>상태</th></tr></thead><tbody>
                <c:choose><c:when test="${not empty employees}"><c:forEach var="employee" items="${employees}"><tr><td><input type="radio" name="employeeId" value="${employee.employeeId}" aria-label="${employee.employeeName} 선택"></td><td>${employee.employmentTypeName}</td><td>${employee.employeeNumber}</td><td>${employee.employeeName}</td><td>${employee.departmentName}</td><td>${employee.positionName}</td><td class="${employee.status eq '퇴직' ? 'retired' : ''}">${employee.status}</td></tr></c:forEach></c:when><c:otherwise><tr><td colspan="7" class="empty-row">조회된 사원이 없습니다.</td></tr></c:otherwise></c:choose>
                </tbody></table></div>
                <footer class="modal-actions"><button type="submit" class="button button-primary">사원선택</button><a href="#" class="button button-secondary">선택취소</a></footer>
                </form>
            </div>
        </div>
    </section>
    <c:if test="${not empty historyPopupMessage}">
        <c:url var="historyReturnUrl" value="/payroll/employee-history.do"><c:param name="startMonth" value="${startMonth}"/><c:param name="endMonth" value="${endMonth}"/></c:url>
        <div class="history-alert" role="alertdialog" aria-modal="true" aria-labelledby="history-alert-message"><a class="history-alert__backdrop" href="${historyReturnUrl}" aria-label="닫기"></a><div class="history-alert__panel"><p id="history-alert-message"><c:out value="${historyPopupMessage}"/></p><a href="${historyReturnUrl}">확인</a></div></div>
    </c:if>
    <%@ include file="/WEB-INF/view/common/footer.jspf" %>
</body>
</html>
