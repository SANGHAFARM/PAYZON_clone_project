<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.util.*" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="jdbc.connection.ConnectionProvider" %>
<%@ page import="jdbc.JdbcUtil" %>
<%@ page import="erp.payrollstats.dao.PersonalAnnualStatDao" %>
<%
	request.setCharacterEncoding("UTF-8");

	String baseYear = request.getParameter("baseYear");
	String empNo = request.getParameter("employeeNo");
	String empKeyword = request.getParameter("employeeKeyword");

	Connection conn = null;
	PersonalAnnualStatDao statDao = new PersonalAnnualStatDao();

	// List<String>으로 선언하여 타입 불일치 에러 원천 차단
	List<String> availableYears = new ArrayList<>();
	List<Map<String, String>> employeeOptions = new ArrayList<>();
	List<Map<String, Object>> annualStats = new ArrayList<>();
	String selectedEmployeeName = "";

	try {
		conn = ConnectionProvider.getConnection();

		availableYears = statDao.selectAvailableYears(conn);
		int currentYear = LocalDate.now().getYear();
		if (availableYears.isEmpty()) {
			availableYears.add(String.valueOf(currentYear));
		}
		
		int baseYearInt = (baseYear == null || baseYear.isEmpty()) ? Integer.parseInt(availableYears.get(0)) : Integer.parseInt(baseYear);
		int startYear = baseYearInt - 9; // 최근 10개년

		if (baseYear == null || baseYear.isEmpty()) {
			baseYear = String.valueOf(baseYearInt);
		}

		// 사원 검색 실행
		employeeOptions = statDao.searchEmployees(conn, empKeyword);

		if (empNo != null && !empNo.isEmpty()) {
			selectedEmployeeName = statDao.selectEmployeeName(conn, empNo);
			Map<Integer, long[]> dbAnnualData = statDao.selectAnnualStat(conn, startYear - 1, baseYearInt, empNo);

			long maxGrossPay = 0;
			for (int year = startYear; year <= baseYearInt; year++) {
				if (dbAnnualData.containsKey(year)) {
					long gross = dbAnnualData.get(year)[0];
					if (gross > maxGrossPay) maxGrossPay = gross;
				}
			}
			double chartScaleBase = maxGrossPay > 0 ? maxGrossPay * 1.05 : 1;

			for (int year = startYear; year <= baseYearInt; year++) {
				Map<String, Object> stat = new HashMap<>();
				stat.put("year", year);

				long currentGross = 0;
				long totalDeduct = 0;
				long prevGross = 0;

				if (dbAnnualData.containsKey(year)) {
					currentGross = dbAnnualData.get(year)[0];
					totalDeduct = dbAnnualData.get(year)[1];
				}
				
				if (dbAnnualData.containsKey(year - 1)) {
					prevGross = dbAnnualData.get(year - 1)[0];
				}

				long netPay = currentGross - totalDeduct;

				stat.put("annualSalaryText", String.format("%,d", currentGross / 1000));
				stat.put("deductionText", String.format("%,d", totalDeduct / 1000));
				stat.put("netSalaryText", String.format("%,d", netPay / 1000));

				double growthRate = 0;
				String growthText = "-";
				
				if (year == startYear) {
					growthText = "-";
				} else if (prevGross > 0) {
					growthRate = ((double)(currentGross - prevGross) / prevGross) * 100;
					growthText = String.format("%.1f%%", growthRate);
				} else if (prevGross == 0 && currentGross > 0) {
					growthRate = 100;
					growthText = "100.0%";
				}

				stat.put("salaryGrowth", growthRate);
				stat.put("salaryGrowthText", growthText);

				int salaryBarRate = (int) Math.round((currentGross / chartScaleBase) * 100);
				int deductionShareRate = currentGross > 0 ? (int) Math.round(((double) totalDeduct / currentGross) * 100) : 0;

				stat.put("salaryBarRate", salaryBarRate);
				stat.put("deductionShareRate", deductionShareRate);

				annualStats.add(stat);
			}
		}

		request.setAttribute("availableYears", availableYears);
		request.setAttribute("selectedYear", baseYear);
		request.setAttribute("selectedEmployeeNo", empNo);
		request.setAttribute("selectedEmployeeName", selectedEmployeeName);
		request.setAttribute("employeeOptions", employeeOptions);
		request.setAttribute("salaryStats", annualStats);

	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		JdbcUtil.close(conn);
	}
%>

<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>연도별 개인연봉 통계</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/common.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/payzon-ui.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/payroll-stats/annual-payroll-statistics.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/payroll-stats/personal-annual-salary-statistics.css">
</head>
<body>
	<%@ include file="/WEB-INF/view/common/header.jspf" %>

	<main class="page-content annual-stat-page personal-salary-page">
		<header class="page-heading">
			<div><p>급여통계</p><h1>연도별 개인연봉 통계</h1></div>
		</header>

		<section class="content-card">
			<form id="mainSearchForm" class="search-bar personal-search-bar" method="get" action="personal-annual-salary-statistics.jsp">
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
				<div class="section-title-row">
					<h2 class="sr-only">연도별 개인연봉 차트</h2>
					<div class="chart-legend">
						<span><i class="net-legend"></i>실지급액</span>
						<span><i class="deduction-legend"></i>공제금액</span>
					</div>
				</div>
				<div class="annual-chart salary-chart">
					<c:forEach var="stat" items="${salaryStats}">
						<div class="annual-chart__item" tabindex="0">
							<div class="annual-chart__plot salary-chart__plot">
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
								<b>${stat.year}년 · ${selectedEmployeeName}</b>
								<span><i class="tooltip-dot tooltip-dot--net"></i>실지급액 (천원) <em>${stat.netSalaryText}</em></span>
								<span><i class="tooltip-dot tooltip-dot--deduction"></i>공제금액 (천원) <em>${stat.deductionText}</em></span>
							</div>
						</div>
					</c:forEach>
				</div>
			</section>

			<div class="statistics-table-wrap">
				<table class="statistics-table personal-salary-table">
					<caption>${selectedEmployeeName} 사원의 최근 10개년도 개인연봉 현황</caption>
					<thead><tr><th scope="col">구분</th><c:forEach var="stat" items="${salaryStats}"><th scope="col">${stat.year}년</th></c:forEach></tr></thead>
					<tbody>
						<tr class="statistics-table__main-row"><th scope="row">연봉액 (천원)</th><c:forEach var="stat" items="${salaryStats}"><td>${stat.annualSalaryText}</td></c:forEach></tr>
						<tr class="statistics-table__rate-row"><th scope="row">└ 증가율</th><c:forEach var="stat" items="${salaryStats}"><td class="${stat.salaryGrowth gt 0 ? 'rate-up' : stat.salaryGrowth lt 0 ? 'rate-down' : ''}">${stat.salaryGrowthText}</td></c:forEach></tr>
						<tr><th scope="row">공제금액 (천원)</th><c:forEach var="stat" items="${salaryStats}"><td>${stat.deductionText}</td></c:forEach></tr>
						<tr class="statistics-table__main-row"><th scope="row">실지급액 (천원)</th><c:forEach var="stat" items="${salaryStats}"><td>${stat.netSalaryText}</td></c:forEach></tr>
					</tbody>
				</table>
			</div>
		</section>
	</main>

	<!-- 사원선택 모달창 -->
	<div id="employeeSelectModal" class="css-modal" role="dialog" aria-modal="true" aria-labelledby="employee-modal-title">
		<a href="#_" class="css-modal__backdrop" aria-label="팝업 닫기"></a>
		<section class="css-modal__dialog employee-modal">
			<header class="css-modal__header"><h2 id="employee-modal-title">개인연봉 조회 사원선택</h2><a href="#_" class="css-modal__close" aria-label="닫기">×</a></header>
			
			<form id="empSelectForm" onsubmit="return false;">
				<div class="employee-modal__search">
					<input type="text" id="empKeywordInput" value="${param.employeeKeyword}" placeholder="사원검색" onkeypress="if(event.keyCode==13) { doModalSearch(); return false; }">
					<button type="button" class="ui-button ui-button--primary" onclick="doModalSearch();">검색</button>
				</div>
				<div class="employee-modal__table-wrap">
					<table class="employee-modal__table">
						<thead><tr><th>선택</th><th>구분</th><th>사원번호</th><th>성명</th><th>부서</th><th>직위</th><th>상태</th></tr></thead>
						<tbody>
							<c:forEach var="employee" items="${employeeOptions}">
								<tr class="emp-row" style="cursor:pointer;" onclick="this.querySelector('input[type=radio]').checked=true;">
									<td><input type="radio" name="modalEmpNo" value="${employee.employeeNo}" <c:if test="${employee.employeeNo eq selectedEmployeeNo}">checked</c:if>></td>
									<td>${employee.type}</td>
									<td>${employee.employeeNo}</td>
									<td>${employee.name}</td>
									<td>${employee.department}</td>
									<td>${employee.position}</td>
									<td>${employee.status}</td>
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
		var currentPath = window.location.pathname.split('/').pop();

		function doModalSearch() {
			var keyword = document.getElementById('empKeywordInput').value || '';
			var yearSelect = document.getElementById('baseYear');
			var year = yearSelect ? yearSelect.value : '';
			
			var form = document.createElement('form');
			form.method = 'POST';
			form.action = currentPath + '#employeeSelectModal';

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
			
			location.href = currentPath + "?baseYear=" + encodeURIComponent(year) + "&employeeNo=" + encodeURIComponent(selectedNo);
		}
	</script>

	<%@ include file="/WEB-INF/view/common/footer.jspf" %>
</body>
</html>