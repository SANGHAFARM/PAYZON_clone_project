<%-- 조회 결과와 입력 양식을 표시하는 JSP 화면이다. --%>
<%-- 照会結果と入力フォームを表示するJSP画面である。 --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
    <title>급여항목 구성 통계</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/payzon-ui.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/payroll-stats/annual-payroll-statistics.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/payroll-stats/personal-annual-salary-statistics.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/payroll-stats/payroll-item-composition-statistics.css">
</head>
<body>
    <%@ include file="/WEB-INF/view/common/header.jspf" %>

    <!-- ⭐ 여기서부터 전체 폼(Form)이 시작됩니다. (메인 검색창 + 모달창 전체를 감쌉니다) ⭐ -->
    <form action="${pageContext.request.contextPath}/payroll-stats/composition.do" method="GET">

        <main class="page-content annual-stat-page pay-composition-page">
            <header class="page-heading"><div><p>급여통계</p><h1>급여항목 구성 통계</h1></div></header>
            <section class="content-card">

                <!-- 기존 form 태그를 div로 변경하여 중첩 폼 오류 방지 -->
                <div class="search-bar personal-search-bar"><div class="search-bar__controls">
                    <label for="baseYear">귀속연월</label>
                    <select id="baseYear" name="baseYear"><option value="">연도</option><c:forEach var="year" items="${availableYears}"><option value="${year}" <c:if test="${year eq selectedYear}">selected</c:if>>${year}년</option></c:forEach></select>
                    <select name="baseMonth" aria-label="귀속월"><option value="">월</option><c:forEach var="month" items="${availableMonths}"><option value="${month}" <c:if test="${month eq selectedMonth}">selected</c:if>>${month}월</option></c:forEach></select>

                    <label for="employeeName">대상자</label>
                    <!-- ⭐ 히든 인풋 삭제! 자바스크립트 없이 서버에서 받아온 이름만 띄워줍니다 ⭐ -->
                    <input id="employeeName" class="employee-name-field" type="text" value="${selectedEmployeeName}" placeholder="사원을 선택해 주세요." readonly>
                    <a class="ui-button ui-button--outline employee-select-link" href="#employeeSelectModal">사원선택</a>

                    <!-- 이 조회 버튼을 누르면 baseYear, baseMonth, 그리고 모달에 체크된 employeeId가 서버로 넘어갑니다 -->
                    <button type="submit" class="ui-button ui-button--primary">조회</button>
                </div></div>

                <section class="donut-grid" aria-label="급여항목 구성 차트">
                    <article class="donut-card"><h2>지급항목 + 공제항목</h2><div class="interactive-donut"><svg class="donut-svg" viewBox="0 0 200 200" aria-label="지급항목과 공제항목 구성"><circle class="donut-track" cx="100" cy="100" r="65" pathLength="100"/><c:forEach var="item" items="${summaryItems}"><g class="donut-segment-group"><circle class="donut-segment" cx="100" cy="100" r="65" pathLength="100" stroke="${item.color}" stroke-dasharray="${item.ratioValue} ${100 - item.ratioValue}" stroke-dashoffset="-${item.dashOffset}"/><g class="donut-svg-tooltip"><rect x="36" y="77" width="128" height="48" rx="4"/><text x="100" y="97">${item.name}</text><text x="100" y="116">${item.ratioText}</text></g></g></c:forEach></svg><c:forEach var="item" items="${summaryItems}"><span class="donut-percentage-label" style="left:${item.labelLeft}%;top:${item.labelTop}%">${item.ratioText}</span></c:forEach></div><div class="donut-legend"><c:forEach var="item" items="${summaryItems}"><span><i style="background:${item.color}"></i>${item.name}</span></c:forEach></div></article>
                    <article class="donut-card"><h2>지급 세부항목</h2><div class="interactive-donut"><svg class="donut-svg" viewBox="0 0 200 200" aria-label="지급 세부항목 구성"><circle class="donut-track" cx="100" cy="100" r="65" pathLength="100"/><c:forEach var="item" items="${paymentItems}"><c:if test="${item.ratioValue ne '0'}"><g class="donut-segment-group"><circle class="donut-segment" cx="100" cy="100" r="65" pathLength="100" stroke="${item.color}" stroke-dasharray="${item.ratioValue} ${100 - item.ratioValue}" stroke-dashoffset="-${item.dashOffset}"/><g class="donut-svg-tooltip"><rect x="36" y="77" width="128" height="48" rx="4"/><text x="100" y="97">${item.name}</text><text x="100" y="116">${item.ratioText}</text></g></g></c:if></c:forEach></svg><c:forEach var="item" items="${paymentItems}"><c:if test="${item.ratioValue ne '0'}"><span class="donut-percentage-label" style="left:${item.labelLeft}%;top:${item.labelTop}%">${item.ratioText}</span></c:if></c:forEach></div><div class="donut-legend"><c:forEach var="item" items="${paymentItems}"><span class="${item.ratioText eq '0.0%' ? 'is-zero' : ''}"><i style="background:${item.color}"></i>${item.name}</span></c:forEach></div></article>
                    <article class="donut-card"><h2>공제 세부항목</h2><div class="interactive-donut"><svg class="donut-svg" viewBox="0 0 200 200" aria-label="공제 세부항목 구성"><circle class="donut-track" cx="100" cy="100" r="65" pathLength="100"/><c:forEach var="item" items="${deductionItems}"><c:if test="${item.ratioValue ne '0'}"><g class="donut-segment-group"><circle class="donut-segment" cx="100" cy="100" r="65" pathLength="100" stroke="${item.color}" stroke-dasharray="${item.ratioValue} ${100 - item.ratioValue}" stroke-dashoffset="-${item.dashOffset}"/><g class="donut-svg-tooltip"><rect x="36" y="77" width="128" height="48" rx="4"/><text x="100" y="97">${item.name}</text><text x="100" y="116">${item.ratioText}</text></g></g></c:if></c:forEach></svg><c:forEach var="item" items="${deductionItems}"><c:if test="${item.ratioValue ne '0'}"><span class="donut-percentage-label" style="left:${item.labelLeft}%;top:${item.labelTop}%">${item.ratioText}</span></c:if></c:forEach></div><div class="donut-legend"><c:forEach var="item" items="${deductionItems}"><span class="${item.ratioText eq '0.0%' ? 'is-zero' : ''}"><i style="background:${item.color}"></i>${item.name}</span></c:forEach></div></article>
                </section>
                <div class="composition-table-wrap"><table class="composition-table"><caption>급여 지급항목 구성</caption>
                    <thead><tr><th>지급항목</th><c:forEach var="item" items="${paymentItems}"><th>${item.name}</th></c:forEach><th>합계</th></tr></thead>
                    <tbody><tr><th>└ 금액 (원)</th><c:forEach var="item" items="${paymentItems}"><td>${item.amountText}</td></c:forEach><td class="total-cell">${totalPaymentText}</td></tr><tr><th>└ 구성비율</th><c:forEach var="item" items="${paymentItems}"><td>${item.ratioText}</td></c:forEach><td class="total-cell">100%</td></tr></tbody>
                </table></div>
                <div class="composition-table-wrap"><table class="composition-table"><caption>급여 공제항목 구성</caption>
                    <thead><tr><th class="deduction-head">공제항목</th><c:forEach var="item" items="${deductionItems}"><th>${item.name}</th></c:forEach><th>합계</th></tr></thead>
                    <tbody><tr><th>└ 금액 (원)</th><c:forEach var="item" items="${deductionItems}"><td>${item.amountText}</td></c:forEach><td class="total-cell">${totalDeductionText}</td></tr><tr><th>└ 구성비율</th><c:forEach var="item" items="${deductionItems}"><td>${item.ratioText}</td></c:forEach><td class="total-cell">100%</td></tr></tbody>
                </table></div>
                <div class="composition-summary"><div><span>지급총액</span><strong>${totalPaymentText}원</strong></div><div><span>공제총액</span><strong>${totalDeductionText}원</strong></div><div class="composition-summary__net"><span>실지급액</span><strong>${netPaymentText}원</strong></div></div>
            </section>
        </main>

        <!-- ⭐ 모달창 시작 ⭐ -->
        <div id="employeeSelectModal" class="css-modal" role="dialog" aria-modal="true" aria-labelledby="employee-modal-title"><a href="#" class="css-modal__backdrop" aria-label="팝업 닫기"></a><section class="css-modal__dialog employee-modal"><header class="css-modal__header"><h2 id="employee-modal-title">급여항목 구성 조회 사원선택</h2><a href="#" class="css-modal__close" aria-label="닫기">×</a></header>
            <!-- 모달 안의 중복 form 태그 삭제! -->
            <div class="employee-modal__search"><input type="text" name="employeeKeyword" placeholder="사원검색"><button type="submit" class="ui-button ui-button--primary">검색</button></div>

            <div class="employee-modal__table-wrap"><table class="employee-modal__table"><thead><tr><th>선택</th><th>구분</th><th>사원번호</th><th>성명</th><th>부서</th><th>직위</th><th>상태</th></tr></thead><tbody>
                <c:forEach var="employee" items="${employeeOptions}">
                    <tr>
                        <!-- ⭐ JS용 코드를 모두 빼고, name="employeeId"로 고정합니다 ⭐ -->
                        <!-- 선택된 상태를 유지하기 위해 c:if 문 추가 (새로고침 시 체크 유지) -->
                        <td><input type="radio" name="employeeId" value="${employee.employeeId}" <c:if test="${employee.employeeId eq selectedEmployeeId}">checked</c:if>></td>

                        <td>${employee.type}</td><td>${employee.employeeNo}</td><td>${employee.name}</td><td>${employee.department}</td><td>${employee.position}</td><td>${employee.status}</td>
                    </tr>
                </c:forEach>
            </tbody></table></div>

            <div class="employee-modal__actions">
                <!-- ⭐ 사원선택 버튼을 누르는 순간 폼이 통째로 서버(Handler)로 제출(submit)됩니다! ⭐ -->
                <button type="submit" class="ui-button ui-button--primary">사원선택</button>
                <a href="#" class="ui-button ui-button--secondary">선택취소</a>
            </div>
        </section></div>

    </form>
    <!-- ⭐ 폼 종료 ⭐ -->

    <%@ include file="/WEB-INF/view/common/footer.jspf" %>
</body>
</html>
