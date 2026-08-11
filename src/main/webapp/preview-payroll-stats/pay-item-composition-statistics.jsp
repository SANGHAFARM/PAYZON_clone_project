<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%
	/*
	 * ================================================================
	 * 미리보기 및 백엔드 연동 예시용 더미 데이터
	 * ================================================================
	 *
	 * [실제 구현 방법]
	 * 1. 귀속연도(baseYear), 귀속월(baseMonth), 사원번호(employeeNo)를 전달합니다.
	 * 2. 선택한 사원의 해당 귀속연월 급여 지급·공제 항목을 조회합니다.
	 * 3. 컨트롤러에서 아래 이름으로 request 속성을 전달합니다.
	 *
	 *    availableYears       : 귀속연도 선택 목록
	 *    availableMonths      : 귀속월 선택 목록
	 *    selectedYear         : 현재 선택된 귀속연도
	 *    selectedMonth        : 현재 선택된 귀속월
	 *    selectedEmployeeNo   : 선택된 사원번호
	 *    selectedEmployeeName : 선택된 사원명
	 *    employeeOptions      : 사원선택 팝업 목록
	 *    paymentItems         : 지급항목 목록
	 *    deductionItems       : 공제항목 목록
	 *    totalPaymentText     : 지급항목 합계
	 *    totalDeductionText   : 공제항목 합계
	 *    netPaymentText       : 실지급액
	 *
	 * [paymentItems / deductionItems 항목별 필드]
	 * name       : 항목명
	 * amountText : 화면에 표시할 금액
	 * ratioText  : 각 합계에서 해당 항목이 차지하는 구성비율
	 * color      : 도넛 차트와 범례에 사용할 색상
	 * ratioValue : SVG 도넛 조각 길이에 사용할 숫자 비율
	 * dashOffset : 앞선 항목 비율을 누적한 SVG 조각 시작 위치
	 * labelLeft  : 도넛 위 구성비율 글씨의 가로 위치(%)
	 * labelTop   : 도넛 위 구성비율 글씨의 세로 위치(%)
	 *
	 * 항목은 회사의 급여항목 설정에 따라 유동적으로 전달합니다.
	 * 실제 paymentItems가 전달되면 아래 더미 데이터는 실행되지 않습니다.
	 * 백엔드 연동 완료 후에는 이 블록 전체를 제거해도 됩니다.
	 */
	if (request.getAttribute("paymentItems") == null) {
		List<Map<String, String>> paymentItems = new ArrayList<>();
		String[][] paymentRows = {
			{"기본급", "2,500,000", "92.6%", "#075f9f", "92.6", "0", "56", "82"}, {"식비", "200,000", "7.4%", "#72b9e6", "7.4", "92.6", "42", "17"},
			{"보육수당", "0", "0.0%", "#f1b65c", "0", "100", "50", "50"}, {"직책수당", "0", "0.0%", "#a58bd0", "0", "100", "50", "50"},
			{"차량유지비", "0", "0.0%", "#e88686", "0", "100", "50", "50"}, {"근속수당", "0", "0.0%", "#84a7bd", "0", "100", "50", "50"},
			{"당직수당", "0", "0.0%", "#c3cf75", "0", "100", "50", "50"}, {"상여금", "0", "0.0%", "#de94bd", "0", "100", "50", "50"}, {"휴일수당", "0", "0.0%", "#89bdd7", "0", "100", "50", "50"}
		};
		for (String[] row : paymentRows) { Map<String, String> item = new LinkedHashMap<>(); item.put("name", row[0]); item.put("amountText", row[1]); item.put("ratioText", row[2]); item.put("color", row[3]); item.put("ratioValue", row[4]); item.put("dashOffset", row[5]); item.put("labelLeft", row[6]); item.put("labelTop", row[7]); paymentItems.add(item); }
		List<Map<String, String>> deductionItems = new ArrayList<>();
		String[][] deductionRows = {
			{"국민연금", "118,750", "42.1%", "#ef4e00", "42.1", "0", "79", "50"}, {"건강보험", "89,870", "31.9%", "#f36f00", "31.9", "42.1", "46", "82"},
			{"장기요양보험", "11,800", "4.2%", "#ffab24", "4.2", "74.0", "23", "68"}, {"고용보험", "22,500", "8.0%", "#ff8a00", "8.0", "78.2", "18", "50"},
			{"소득세", "35,600", "12.6%", "#ff9700", "12.6", "86.2", "29", "25"}, {"지방소득세", "3,560", "1.3%", "#ffc15a", "1.3", "98.8", "48", "16"}, {"상조회비", "0", "0.0%", "#ffd89b", "0", "100", "50", "50"}
		};
		for (String[] row : deductionRows) { Map<String, String> item = new LinkedHashMap<>(); item.put("name", row[0]); item.put("amountText", row[1]); item.put("ratioText", row[2]); item.put("color", row[3]); item.put("ratioValue", row[4]); item.put("dashOffset", row[5]); item.put("labelLeft", row[6]); item.put("labelTop", row[7]); deductionItems.add(item); }
		List<Map<String, String>> summaryItems = new ArrayList<>();
		String[][] summaryRows = {{"지급항목", "2,700,000", "90.5%", "#149bd7", "90.5", "0", "56", "82"}, {"공제항목", "282,080", "9.5%", "#ff8a00", "9.5", "90.5", "39", "17"}};
		for (String[] row : summaryRows) { Map<String, String> item = new LinkedHashMap<>(); item.put("name", row[0]); item.put("amountText", row[1]); item.put("ratioText", row[2]); item.put("color", row[3]); item.put("ratioValue", row[4]); item.put("dashOffset", row[5]); item.put("labelLeft", row[6]); item.put("labelTop", row[7]); summaryItems.add(item); }
		List<Map<String, String>> previewEmployees = new ArrayList<>();
		String[][] employeeRows = {{"No-140034", "정규직", "이용열", "사장실", "사장", "재직"}, {"No-140001", "정규직", "김용", "콘텐츠팀", "사원", "재직"}, {"No-140036", "계약직", "이영희", "콘텐츠팀", "사원", "재직"}, {"No-140035", "정규직", "이수진", "디자인팀", "대리", "재직"}};
		for (String[] row : employeeRows) { Map<String, String> employee = new LinkedHashMap<>(); employee.put("employeeNo", row[0]); employee.put("type", row[1]); employee.put("name", row[2]); employee.put("department", row[3]); employee.put("position", row[4]); employee.put("status", row[5]); previewEmployees.add(employee); }
		request.setAttribute("paymentItems", paymentItems);
		request.setAttribute("deductionItems", deductionItems);
		request.setAttribute("summaryItems", summaryItems);
		request.setAttribute("employeeOptions", previewEmployees);
		request.setAttribute("availableYears", Arrays.asList(2017, 2018, 2019, 2020, 2021, 2022, 2023, 2024, 2025, 2026));
		request.setAttribute("availableMonths", Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"));
		request.setAttribute("selectedYear", 2026); request.setAttribute("selectedMonth", "08");
		request.setAttribute("selectedEmployeeNo", "No-140034"); request.setAttribute("selectedEmployeeName", "이용열");
		request.setAttribute("totalPaymentText", "2,700,000"); request.setAttribute("totalDeductionText", "282,080"); request.setAttribute("netPaymentText", "2,417,920");
	}
	/* 미리보기 및 백엔드 연동 예시용 더미 데이터 끝 */
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
	<title>급여항목 구성 통계</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/common.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/payzon-ui.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/payroll-stats/annual-payroll-statistics.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/payroll-stats/personal-annual-salary-statistics.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/payroll-stats/pay-item-composition-statistics.css">
</head>
<body>
	<%@ include file="/WEB-INF/view/common/header.jspf" %>
	<main class="page-content annual-stat-page pay-composition-page">
		<header class="page-heading"><div><p>급여통계</p><h1>급여항목 구성 통계</h1></div></header>
		<section class="content-card">
			<form class="search-bar personal-search-bar" method="get"><div class="search-bar__controls">
				<label for="baseYear">귀속연월</label>
				<select id="baseYear" name="baseYear"><option value="">연도</option><c:forEach var="year" items="${availableYears}"><option value="${year}" <c:if test="${year eq selectedYear}">selected</c:if>>${year}년</option></c:forEach></select>
				<select name="baseMonth" aria-label="귀속월"><option value="">월</option><c:forEach var="month" items="${availableMonths}"><option value="${month}" <c:if test="${month eq selectedMonth}">selected</c:if>>${month}월</option></c:forEach></select>
				<label for="employeeName">대상자</label><input type="hidden" name="employeeNo" value="${selectedEmployeeNo}">
				<input id="employeeName" class="employee-name-field" type="text" value="${selectedEmployeeName}" placeholder="사원을 선택해 주세요." readonly>
				<a class="ui-button ui-button--outline employee-select-link" href="#employeeSelectModal">사원선택</a><button type="submit" class="ui-button ui-button--primary">조회</button>
			</div></form>
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
	<div id="employeeSelectModal" class="css-modal" role="dialog" aria-modal="true" aria-labelledby="employee-modal-title"><a href="#" class="css-modal__backdrop" aria-label="팝업 닫기"></a><section class="css-modal__dialog employee-modal"><header class="css-modal__header"><h2 id="employee-modal-title">급여항목 구성 조회 사원선택</h2><a href="#" class="css-modal__close" aria-label="닫기">×</a></header><form method="get"><div class="employee-modal__search"><input type="text" name="employeeKeyword" placeholder="사원검색"><button type="submit" class="ui-button ui-button--primary">검색</button></div><div class="employee-modal__table-wrap"><table class="employee-modal__table"><thead><tr><th>선택</th><th>구분</th><th>사원번호</th><th>성명</th><th>부서</th><th>직위</th><th>상태</th></tr></thead><tbody><c:forEach var="employee" items="${employeeOptions}"><tr><td><input type="radio" name="employeeNo" value="${employee.employeeNo}"></td><td>${employee.type}</td><td>${employee.employeeNo}</td><td>${employee.name}</td><td>${employee.department}</td><td>${employee.position}</td><td>${employee.status}</td></tr></c:forEach></tbody></table></div><div class="employee-modal__actions"><button type="submit" class="ui-button ui-button--primary">사원선택</button><a href="#" class="ui-button ui-button--secondary">선택취소</a></div></form></section></div>
	<%@ include file="/WEB-INF/view/common/footer.jspf" %>