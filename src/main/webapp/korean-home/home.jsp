<%-- 조회 결과와 입력 양식을 표시하는 JSP 화면이다. --%>
<%-- 照会結果と入力フォームを表示するJSP画面である。 --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>PAYZON - HOME</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home/home.css?v=20260815-6">
</head>
<body>
    <%@ include file="/WEB-INF/view/common/header.jspf" %>
    <main class="page-content home-page">
        <section class="home-hero">
            <div class="home-hero__copy">
                <p class="home-hero__date"><c:out value="${today}" /></p>
                <h1><c:out value="${empty dashboard.company.cmpnName ? 'PAYZON' : dashboard.company.cmpnName}" />의<br>급여·인사 업무를 시작하세요</h1>
                <p>사원정보부터 급여 정산과 퇴직관리까지 필요한 업무를 한곳에서 확인할 수 있습니다.</p>
                <div class="home-hero__actions">
                    <a class="home-primary-link" href="${pageContext.request.contextPath}/payroll/management.do">급여 입력하기</a>
                    <a class="home-secondary-link" href="${pageContext.request.contextPath}/employees/employees.do">사원현황 보기</a>
                </div>
            </div>
            <div class="home-hero__summary">
                <span>이번 달 업무 현황</span>
                <strong>${currentYear}.${currentMonth}</strong>
                <dl>
                    <div><dt>재직 사원</dt><dd><fmt:formatNumber value="${dashboard.employeeSummary.workingCount}" />명</dd></div>
                    <div><dt>최근 급여 인원</dt><dd><fmt:formatNumber value="${empty dashboard.latestPayroll ? 0 : dashboard.latestPayroll.employeeCount}" />명</dd></div>
                    <div><dt>최근 실지급액</dt><dd><fmt:formatNumber value="${empty dashboard.latestPayroll ? 0 : dashboard.latestPayroll.netPayment}" />원</dd></div>
                </dl>
            </div>
        </section>

        <section class="home-stat-grid" aria-label="사원 현황">
            <a class="home-stat-card home-stat-card--navy" href="${pageContext.request.contextPath}/employees/employees.do">
                <div><p>전체 사원</p><strong><fmt:formatNumber value="${dashboard.employeeSummary.totalCount}" /><em>명</em></strong><span>등록된 전체 사원</span></div>
            </a>
            <a class="home-stat-card home-stat-card--blue" href="${pageContext.request.contextPath}/employees/employees.do?status=WORK">
                <div><p>재직 사원</p><strong><fmt:formatNumber value="${dashboard.employeeSummary.workingCount}" /><em>명</em></strong><span>현재 재직 중인 사원</span></div>
            </a>
            <a class="home-stat-card home-stat-card--green" href="${pageContext.request.contextPath}/payroll/day-worker-management.do">
                <div><p>일용직 사원</p><strong><fmt:formatNumber value="${dashboard.employeeSummary.dailyCount}" /><em>명</em></strong><span>일용직으로 등록된 사원</span></div>
            </a>
            <a class="home-stat-card home-stat-card--orange" href="${pageContext.request.contextPath}/retirement/process.do?mode=status&amp;status=RETIRED">
                <div><p>퇴직 사원</p><strong><fmt:formatNumber value="${dashboard.employeeSummary.retiredCount}" /><em>명</em></strong><span>퇴직 처리된 사원</span></div>
            </a>
        </section>

        <div class="home-main-grid">
            <section class="home-panel home-quick-panel">
                <div class="home-panel__heading"><div><span>QUICK MENU</span><h2>자주 찾는 업무</h2></div></div>
                <div class="home-quick-grid">
                    <a href="${pageContext.request.contextPath}/employees/employees.do"><i>01</i><strong>사원현황/관리</strong><span>사원정보 조회 및 관리</span></a>
                    <a href="${pageContext.request.contextPath}/payroll/management.do"><i>02</i><strong>급여입력/관리</strong><span>월별 급여와 공제 입력</span></a>
                    <a href="${pageContext.request.contextPath}/payroll/register.do"><i>03</i><strong>급여대장</strong><span>지급 회차별 급여 확인</span></a>
                    <a href="${pageContext.request.contextPath}/employees/certificate.do"><i>04</i><strong>제증명서 발급</strong><span>재직·퇴직증명서 발급</span></a>
                    <a href="${pageContext.request.contextPath}/retirement/process.do"><i>05</i><strong>사원 퇴직처리</strong><span>퇴직정보 등록 및 취소</span></a>
                    <a href="${pageContext.request.contextPath}/retirement/benefit.do"><i>06</i><strong>퇴직급여 관리</strong><span>퇴직급여 계산 및 저장</span></a>
                </div>
            </section>

            <section class="home-panel home-company-panel">
                <div class="home-panel__heading"><div><span>COMPANY</span><h2>사업장 정보</h2></div><a href="${pageContext.request.contextPath}/settings/user-info.do">정보 보기</a></div>
                <h3><c:out value="${empty dashboard.company.cmpnName ? '등록된 회사정보가 없습니다' : dashboard.company.cmpnName}" /></h3>
                <dl>
                    <div><dt>대표자</dt><dd><c:out value="${empty dashboard.company.ceoName ? '-' : dashboard.company.ceoName}" /></dd></div>
                    <div><dt>담당자</dt><dd><c:out value="${empty dashboard.company.managerName ? '-' : dashboard.company.managerName}" /></dd></div>
                    <div><dt>연락처</dt><dd><c:out value="${empty dashboard.company.telNo ? '-' : dashboard.company.telNo}" /></dd></div>
                </dl>
            </section>
        </div>

        <section class="home-panel home-payroll-panel">
            <div class="home-panel__heading"><div><span>RECENT PAYROLL</span><h2>최근 급여현황</h2></div><a href="${pageContext.request.contextPath}/payroll/register.do">급여대장 전체보기</a></div>
            <div class="home-table-wrap">
                <table>
                    <thead><tr><th>귀속연월</th><th>급여차수</th><th>정산기간</th><th>지급일</th><th>인원</th><th>지급총액</th><th>공제총액</th><th>실지급액</th></tr></thead>
                    <tbody>
                    <c:forEach var="payroll" items="${dashboard.recentPayrolls}">
                        <tr><td><a href="${pageContext.request.contextPath}/payroll/register/detail.do?registerId=${payroll.registerId}">${payroll.paymentYearMonth}</a></td><td>${payroll.paymentRoundName}</td><td>${payroll.calculationStart} ~ ${payroll.calculationEnd}</td><td>${payroll.paymentDate}</td><td>${payroll.employeeCount}명</td><td class="amount give"><fmt:formatNumber value="${payroll.totalPayment}" /></td><td class="amount deduct"><fmt:formatNumber value="${payroll.totalDeduction}" /></td><td class="amount"><fmt:formatNumber value="${payroll.netPayment}" /></td></tr>
                    </c:forEach>
                    <c:if test="${empty dashboard.recentPayrolls}"><tr><td colspan="8" class="empty-row">${currentYear}년에 등록된 급여대장이 없습니다.</td></tr></c:if>
                    </tbody>
                </table>
            </div>
        </section>
    </main>
    <%@ include file="/WEB-INF/view/common/footer.jspf" %>
</body>
</html>
