<%-- 조회 결과와 입력 양식을 표시하는 JSP 화면이다. --%>
<%-- 照会結果と入力フォームを表示するJSP画面である。 --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html lang="ja-JP">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>年別の個人給与統計</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/payzon-ui.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/payroll-stats/annual-payroll-statistics.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/payroll-stats/personal-annual-salary-statistics.css">
</head>
<body>
    <%@ include file="/WEB-INF/view/common/header.jspf" %>

    <main class="page-content annual-stat-page personal-salary-page">
        <header class="page-heading">
            <div><p>給与統計</p><h1>年別の個人給与統計</h1></div>
        </header>

        <section class="content-card">
            <form id="mainSearchForm" class="search-bar personal-search-bar" method="get" action="${pageContext.request.contextPath}/payroll-stats/annual-personal.do">
                <div class="search-bar__controls">
                    <label for="baseYear">帰属年</label>
                    <select id="baseYear" name="baseYear">
                        <option value="">選択</option>
                        <c:forEach var="year" items="${availableYears}">
                            <option value="${year}" <c:if test="${year eq selectedYear}">selected</c:if>>${year}年</option>
                        </c:forEach>
                    </select>
                    <label for="employeeName">対象</label>
                    <input type="hidden" id="mainEmployeeNo" name="employeeNo" value="${selectedEmployeeNo}">
                    <input id="employeeName" class="employee-name-field" type="text" value="${selectedEmployeeName}" placeholder="社員を選択してください。" readonly>
                    <a class="ui-button ui-button--outline employee-select-link" href="#employeeSelectModal">社員を選択</a>
                    <button type="submit" class="ui-button ui-button--primary">照会</button>
                </div>
            </form>

            <section class="chart-panel">
                <div class="section-title-row">
                    <h2 class="sr-only">年度別個人給与チャート</h2>
                    <div class="chart-legend">
                        <span><i class="net-legend"></i>差引支給額</span>
                        <span><i class="deduction-legend"></i>控除金額</span>
                    </div>
                </div>
                <div class="annual-chart salary-chart">
                    <c:forEach var="stat" items="${salaryStats}">
                        <div class="annual-chart__item" tabindex="0">
                            <div class="annual-chart__plot salary-chart__plot">
                                <!-- Serviceで0円のときに10%の高さを与えるように設定された値をそのまま使用します -->
                                <div class="salary-chart__stack" style="height:${stat.salaryBarRate}%">
                                    <div class="salary-chart__net" style="height:${100 - stat.deductionShareRate}%">
                                        <span>${stat.netSalaryText}</span>
                                    </div>
                                    <div class="salary-chart__deduction" style="height:${stat.deductionShareRate}%">
                                        <span>${stat.deductionText}</span>
                                    </div>
                                </div>
                            </div>
                            <strong>${stat.year}</strong>
                            <div class="annual-chart__tooltip" role="tooltip">
                                <b>${stat.year}年・${selectedEmployeeName}</b>
                                <span><i class="tooltip-dot tooltip-dot--net"></i>差引支給額（千円） <em>${stat.netSalaryText}</em></span>
                                <span><i class="tooltip-dot tooltip-dot--deduction"></i>控除金額（千円） <em>${stat.deductionText}</em></span>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </section>

            <div class="statistics-table-wrap">
                <table class="statistics-table personal-salary-table">
                    <caption> ${selectedEmployeeName}社員の最近10カ年の個人年俸の現状</caption>
                    <thead><tr><th scope="col">区分</th><c:forEach var="stat" items="${salaryStats}"><th scope="col">${stat.year}年</th></c:forEach></tr></thead>
                    <tbody>
                        <tr class="statistics-table__main-row"><th scope="row">年収額（千円）</th><c:forEach var="stat" items="${salaryStats}"><td>${stat.annualSalaryText}</td></c:forEach></tr>
                        <tr class="statistics-table__rate-row"><th scope="row">└増加率</th><c:forEach var="stat" items="${salaryStats}"><td class="${stat.salaryGrowth gt 0 ? 'rate-up' : stat.salaryGrowth lt 0 ? 'rate-down' : ''}">${stat.salaryGrowthText}</td></c:forEach></tr>
                        <tr><th scope="row">控除金額（千円）</th><c:forEach var="stat" items="${salaryStats}"><td>${stat.deductionText}</td></c:forEach></tr>
                        <tr class="statistics-table__main-row"><th scope="row">差引支給額（千円）</th><c:forEach var="stat" items="${salaryStats}"><td>${stat.netSalaryText}</td></c:forEach></tr>
                    </tbody>
                </table>
            </div>
        </section>
    </main>

    <!-- 社員選択モーダルウィンドウ -->
    <div id="employeeSelectModal" class="css-modal" role="dialog" aria-modal="true" aria-labelledby="employee-modal-title">
        <a href="#_" class="css-modal__backdrop" aria-label="ポップアップを閉じる"></a>
        <section class="css-modal__dialog employee-modal">
            <header class="css-modal__header"><h2 id="employee-modal-title">個人年俸照会社員選択</h2><a href="#_" class="css-modal__close" aria-label="閉じる">×</a></header>

            <form id="empSelectForm" onsubmit="return false;">
                <div class="employee-modal__search">
                    <input type="text" id="empKeywordInput" value="${param.employeeKeyword}" placeholder="社員検索" onkeypress="if(event.keyCode==13) { doModalSearch(); return false; }">
                    <button type="button" class="ui-button ui-button--primary" onclick="doModalSearch();">検索</button>
                </div>

                <!-- インラインスタイルでモーダルスクロールとヘッダ固定処理 -->
                <div class="employee-modal__table-wrap" style="max-height: 350px; overflow-y: auto; position: relative; border-bottom: 1px solid #ddd;">
                    <table class="employee-modal__table" style="width: 100%; border-collapse: collapse;">
                        <thead style="position: sticky; top: 0; z-index: 10;">
                            <tr>
                                <th style="background-color: #f4f6f8; border-bottom: 1px solid #ccc; padding: 10px;">選択</th>
                                <th style="background-color: #f4f6f8; border-bottom: 1px solid #ccc; padding: 10px;">区分</th>
                                <th style="background-color: #f4f6f8; border-bottom: 1px solid #ccc; padding: 10px;">社員番号</th>
                                <th style="background-color: #f4f6f8; border-bottom: 1px solid #ccc; padding: 10px;">氏名</th>
                                <th style="background-color: #f4f6f8; border-bottom: 1px solid #ccc; padding: 10px;">部署</th>
                                <th style="background-color: #f4f6f8; border-bottom: 1px solid #ccc; padding: 10px;">役職</th>
                                <th style="background-color: #f4f6f8; border-bottom: 1px solid #ccc; padding: 10px;">ステータス</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="employee" items="${employeeOptions}">
                                <tr class="emp-row" style="cursor:pointer; border-bottom: 1px solid #eee;" onclick="this.querySelector('input[type=radio]').checked=true;">
                                    <td style="padding: 8px; text-align: center;"><input type="radio" name="modalEmpNo" value="${employee.employeeNo}" <c:if test="${employee.employeeNo eq selectedEmployeeNo}">checked</c:if>></td>
                                    <td style="padding: 8px; text-align: center;"><ui:code-label value="${employee.type}" /></td>
                                    <td style="padding: 8px; text-align: center;">${employee.employeeNo}</td>
                                    <td style="padding: 8px; text-align: center;">${employee.name}</td>
                                    <td style="padding: 8px; text-align: center;">${employee.department}</td>
                                    <td style="padding: 8px; text-align: center;">${employee.position}</td>
                                    <td style="padding: 8px; text-align: center;"><ui:code-label value="${employee.status}" /></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <div class="employee-modal__actions">
                    <button type="button" class="ui-button ui-button--primary" onclick="doModalSelect();">社員を選択</button>
                    <a href="#_" class="ui-button ui-button--secondary">選択解除</a>
                </div>
            </form>
        </section>
    </div>

    <script>
        var targetUrl = '${pageContext.request.contextPath}/payroll-stats/annual-personal.do';

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
                alert("社員を選択してください。");
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
