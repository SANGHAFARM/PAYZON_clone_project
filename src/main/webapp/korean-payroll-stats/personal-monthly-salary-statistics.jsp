<%-- 조회 결과와 입력 양식을 표시하는 JSP 화면이다. --%>
<%-- 照会結果と入力フォームを表示するJSP画面である。 --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>월별 개인급여 통계</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/payzon-ui.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/payroll-stats/annual-payroll-statistics.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/payroll-stats/personal-annual-salary-statistics.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/payroll-stats/personal-monthly-salary-statistics.css">
</head>
<body>
    <%@ include file="/WEB-INF/view/common/header.jspf" %>

    <main class="page-content annual-stat-page personal-salary-page personal-monthly-page">
        <header class="page-heading"><div><p>급여통계</p><h1>월별 개인급여 통계</h1></div></header>
        <section class="content-card">
            <form id="mainSearchForm" class="search-bar personal-search-bar" method="get" action="${pageContext.request.contextPath}/payroll-stats/monthly-personal.do">
                <div class="search-bar__controls">
                    <label for="baseYear">귀속연도</label>
                    <select id="baseYear" name="baseYear">
                        <option value="">선택</option>
                        <c:forEach var="year" items="${availableYears}">
                            <option value="${year}" <c:if test="${year eq selectedYear}">selected</c:if>>${year}년</option>
                        </c:forEach>
                    </select>
                    <label for="employeeName">대상자</label>
                    <input type="hidden" id="mainEmployeeNo" name="employeeNo" value="${selectedEmployeeNo}">
                    <input id="employeeName" class="employee-name-field" type="text" value="${selectedEmployeeName}" placeholder="사원을 선택해 주세요." readonly>
                    <a class="ui-button ui-button--outline employee-select-link" href="#employeeSelectModal">사원선택</a>
                    <button type="submit" class="ui-button ui-button--primary">조회</button>
                </div>
            </form>

            <section class="chart-panel">
                <div class="section-title-row"><h2 class="sr-only">월별 개인급여 차트</h2><div class="chart-legend"><span><i class="net-legend"></i>실지급액</span><span><i class="deduction-legend"></i>공제액</span></div></div>
                <div class="annual-chart personal-monthly-chart">
                    <c:forEach var="stat" items="${monthlySalaryStats}">
                        <div class="annual-chart__item" tabindex="0">
                            <div class="annual-chart__plot salary-chart__plot">
                                <!-- 차트 비율 (Java Service에서 0원일 때 10% 높이를 주도록 설정된 값을 그대로 사용) -->
                                <div class="salary-chart__stack" style="height:${stat.salaryBarRate}%">
                                    <div class="salary-chart__net" style="height:${100 - stat.deductionShareRate}%"><span>${stat.netSalaryText}</span></div>
                                    <div class="salary-chart__deduction" style="height:${stat.deductionShareRate}%"><span>${stat.deductionText}</span></div>
                                </div>
                            </div>
                            <strong>${stat.month}월</strong>
                            <div class="annual-chart__tooltip" role="tooltip"><b>${stat.month}월</b><span><i class="tooltip-dot tooltip-dot--net"></i>실지급액 (천원) <em>${stat.netSalaryText}</em></span><span><i class="tooltip-dot tooltip-dot--deduction"></i>공제액 (천원) <em>${stat.deductionText}</em></span></div>
                        </div>
                    </c:forEach>
                </div>
            </section>

            <div class="statistics-table-wrap">
                <table class="statistics-table personal-monthly-table">
                    <caption>${selectedEmployeeName} 사원의 ${selectedYear}년 월별 개인급여 현황</caption>
                    <thead><tr><th scope="col">구분</th><c:forEach var="stat" items="${monthlySalaryStats}"><th scope="col">${stat.month}월</th></c:forEach><th scope="col">합계</th></tr></thead>
                    <tbody>
                        <tr class="statistics-table__main-row"><th scope="row">월급여액 (천원)</th><c:forEach var="stat" items="${monthlySalaryStats}"><td>${stat.monthlySalaryText}</td></c:forEach><td class="statistics-total">${totalSalaryYearText}</td></tr>
                        <tr><th scope="row">└ 공제액 (천원)</th><c:forEach var="stat" items="${monthlySalaryStats}"><td>${stat.deductionText}</td></c:forEach><td class="statistics-total">${totalDeductionYearText}</td></tr>
                        <tr class="statistics-table__main-row"><th scope="row">└ 실지급액 (천원)</th><c:forEach var="stat" items="${monthlySalaryStats}"><td>${stat.netSalaryText}</td></c:forEach><td class="statistics-total">${totalNetYearText}</td></tr>
                    </tbody>
                </table>
            </div>
        </section>
    </main>

    <!-- 사원선택 모달창 -->
    <div id="employeeSelectModal" class="css-modal" role="dialog" aria-modal="true" aria-labelledby="employee-modal-title">
        <a href="#_" class="css-modal__backdrop" aria-label="팝업 닫기"></a>
        <section class="css-modal__dialog employee-modal">
            <header class="css-modal__header"><h2 id="employee-modal-title">개인급여 조회 사원선택</h2><a href="#_" class="css-modal__close" aria-label="닫기">×</a></header>

            <form id="empSelectForm" onsubmit="return false;">
                <div class="employee-modal__search">
                    <input type="text" id="empKeywordInput" value="${param.employeeKeyword}" placeholder="사원검색" onkeypress="if(event.keyCode==13) { doModalSearch(); return false; }">
                    <button type="button" class="ui-button ui-button--primary" onclick="doModalSearch();">검색</button>
                </div>

                <!-- 💡 인라인 스타일로 모달 스크롤 및 헤더 고정 처리 -->
                <div class="employee-modal__table-wrap" style="max-height: 350px; overflow-y: auto; position: relative; border-bottom: 1px solid #ddd;">
                    <table class="employee-modal__table" style="width: 100%; border-collapse: collapse;">
                        <thead style="position: sticky; top: 0; z-index: 10;">
                            <tr>
                                <th style="background-color: #f4f6f8; border-bottom: 1px solid #ccc; padding: 10px;">선택</th>
                                <th style="background-color: #f4f6f8; border-bottom: 1px solid #ccc; padding: 10px;">구분</th>
                                <th style="background-color: #f4f6f8; border-bottom: 1px solid #ccc; padding: 10px;">사원번호</th>
                                <th style="background-color: #f4f6f8; border-bottom: 1px solid #ccc; padding: 10px;">성명</th>
                                <th style="background-color: #f4f6f8; border-bottom: 1px solid #ccc; padding: 10px;">부서</th>
                                <th style="background-color: #f4f6f8; border-bottom: 1px solid #ccc; padding: 10px;">직위</th>
                                <th style="background-color: #f4f6f8; border-bottom: 1px solid #ccc; padding: 10px;">상태</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="employee" items="${employeeOptions}">
                                <tr class="emp-row" style="cursor:pointer; border-bottom: 1px solid #eee;" onclick="this.querySelector('input[type=radio]').checked=true;">
                                    <td style="padding: 8px; text-align: center;"><input type="radio" name="modalEmpNo" value="${employee.employeeNo}" <c:if test="${employee.employeeNo eq selectedEmployeeNo}">checked</c:if>></td>
                                    <td style="padding: 8px; text-align: center;">${employee.type}</td>
                                    <td style="padding: 8px; text-align: center;">${employee.employeeNo}</td>
                                    <td style="padding: 8px; text-align: center;">${employee.name}</td>
                                    <td style="padding: 8px; text-align: center;">${employee.department}</td>
                                    <td style="padding: 8px; text-align: center;">${employee.position}</td>
                                    <td style="padding: 8px; text-align: center;">${employee.status}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <div class="employee-modal__actions">
                    <button type="button" class="ui-button ui-button--primary" onclick="doModalSelect();">사원선택</button>
                    <a href="#_" class="ui-button ui-button--secondary">선택취소</a>
                </div>
            </form>
        </section>
    </div>

    <script>
        var targetUrl = '${pageContext.request.contextPath}/payroll-stats/monthly-personal.do';

        function doModalSearch() {
            var keyword = document.getElementById('empKeywordInput').value || '';
            var yearSelect = document.getElementById('baseYear');
            var year = yearSelect ? yearSelect.value : '';

            var form = document.createElement('form');
            form.method = 'POST';
            form.action = targetUrl + '#employeeSelectModal';

            var inputYear = document.createElement('input');
            inputYear.type = 'hidden';
            inputYear.name = 'baseYear';
            inputYear.value = year;
            form.appendChild(inputYear);

            var inputKeyword = document.createElement('input');
            inputKeyword.type = 'hidden';
            inputKeyword.name = 'employeeKeyword';
            inputKeyword.value = keyword;
            form.appendChild(inputKeyword);

            document.body.appendChild(form);
            form.submit();
        }

        function doModalSelect() {
            var radios = document.getElementsByName('modalEmpNo');
            var selectedNo = "";
            for (var i = 0; i < radios.length; i++) {
                if (radios[i].checked) {
                    selectedNo = radios[i].value;
                    break;
                }
            }

            if (!selectedNo) {
                alert("사원을 선택해 주세요.");
                return;
            }

            var yearSelect = document.getElementById('baseYear');
            var year = yearSelect ? yearSelect.value : '';

            location.href = targetUrl + "?baseYear=" + encodeURIComponent(year) + "&employeeNo=" + encodeURIComponent(selectedNo);
        }
    </script>

    <%@ include file="/WEB-INF/view/common/footer.jspf" %>
</body>
</html>
