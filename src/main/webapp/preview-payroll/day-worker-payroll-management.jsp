<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="employeePage"
	value="${empty param.employeePage ? 1 : param.employeePage}" />
<c:set var="employeeTotalPages"
	value="${empty availableEmployeePage.totalPages ? 1 : availableEmployeePage.totalPages}" />

<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">

	<title>급여입력/관리(일용직)</title>

	<link rel="stylesheet"
		href="${pageContext.request.contextPath}/css/common/common.css">
	<link rel="stylesheet"
		href="${pageContext.request.contextPath}/css/common/payzon-ui.css">
	<link rel="stylesheet"
		href="${pageContext.request.contextPath}/css/payroll/payroll-management.css">
	<link rel="stylesheet"
		href="${pageContext.request.contextPath}/css/payroll/day-worker-payroll-management.css">
</head>

<body>
	<%@ include file="/WEB-INF/view/common/header.jspf" %>

	<main class="page-content payment-management-page day-worker-payment-page">
		<header class="page-heading">
			<div>
				<p>급여관리</p>
				<h1>급여입력/관리(일용직)</h1>
			</div>
		</header>

		<form class="period-panel"
			method="get"
			action="${pageContext.request.contextPath}/payroll/day-worker-payment-management">

			<div class="period-field">
				<label for="paymentYear">귀속연도</label>
				<select id="paymentYear" name="paymentYear">
					<c:forEach var="year" items="${paymentYears}">
						<option value="${year}"
							<c:if test="${year eq selectedYear}">selected</c:if>>
							${year}년
						</option>
					</c:forEach>
				</select>
			</div>

			<div class="period-field">
				<label for="paymentMonth">귀속월</label>
				<select id="paymentMonth" name="paymentMonth">
					<c:forEach var="month" begin="1" end="12">
						<option value="${month}"
							<c:if test="${month eq selectedMonth}">selected</c:if>>
							${month}월
						</option>
					</c:forEach>
				</select>
			</div>

			<div class="period-field">
				<label for="paymentRound">급여차수</label>
				<select id="paymentRound" name="paymentRound">
					<c:forEach var="round" begin="1" end="10">
						<option value="${round}"
							<c:if test="${round eq selectedRound}">selected</c:if>>
							급여-${round}차
						</option>
					</c:forEach>
				</select>
			</div>

			<div class="period-field period-range">
				<label>정산기간</label>
				<input type="date"
					name="calculationStart"
					value="${calculationStart}">
				<span>~</span>
				<input type="date"
					name="calculationEnd"
					value="${calculationEnd}">
			</div>

			<div class="period-field">
				<label for="paymentDate">급여지급일</label>
				<input id="paymentDate"
					type="date"
					name="paymentDate"
					value="${paymentDate}">
			</div>

			<button type="submit" class="button button-primary">조회</button>
		</form>

		<section class="payroll-workspace">
			<div class="employee-area">
				<div class="section-toolbar">
					<div class="toolbar-buttons toolbar-buttons-left">
						<a class="button button-primary" href="#employee-add">
							신규추가
						</a>
					</div>

					<div class="toolbar-buttons toolbar-buttons-right">
						<button type="submit"
							form="employeeForm"
							class="button button-neutral">
							선택삭제
						</button>

						<button type="submit"
							form="employeeForm"
							name="deleteType"
							value="ALL"
							class="button button-neutral">
							전체삭제
						</button>
					</div>
				</div>

				<form id="employeeForm"
					method="post"
					action="${pageContext.request.contextPath}/payroll/day-worker-payment-management/employees/delete">

					<div class="table-wrap employee-table-wrap">
						<table class="data-table employee-table">
							<colgroup>
								<col class="check-column">
								<col>
								<col>
								<col>
								<col class="money-column">
							</colgroup>

							<thead>
								<tr>
									<th>선택</th>
									<th>구분</th>
									<th>성명</th>
									<th>부서</th>
									<th>실지급액</th>
								</tr>
							</thead>

							<tbody>
								<c:choose>
									<c:when test="${not empty paymentEmployees}">
										<c:forEach var="employee"
											items="${paymentEmployees}">

											<tr class="${employee.employeeId eq selectedEmployeeId
												? 'selected-row' : ''}">

												<td>
													<input type="checkbox"
														name="employeeIds"
														value="${employee.employeeId}">
												</td>

												<td>
													<a href="?paymentYear=${selectedYear}&amp;paymentMonth=${selectedMonth}&amp;paymentRound=${selectedRound}&amp;employeeId=${employee.employeeId}">
														${employee.employmentTypeName}
													</a>
												</td>

												<td>
													<a href="?paymentYear=${selectedYear}&amp;paymentMonth=${selectedMonth}&amp;paymentRound=${selectedRound}&amp;employeeId=${employee.employeeId}">
														${employee.employeeName}
													</a>
												</td>

												<td>${employee.departmentName}</td>
												<td class="amount">${employee.netPayment}</td>
											</tr>
										</c:forEach>
									</c:when>

									<c:otherwise>
										<tr>
											<td class="empty-row" colspan="5">
												조회된 급여 대상 일용직 사원이 없습니다.
											</td>
										</tr>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
					</div>
				</form>
			</div>

			<div class="payment-area">
				<form method="post"
					action="${pageContext.request.contextPath}/payroll/day-worker-payment-management/save">

					<input type="hidden"
						name="employeeId"
						value="${selectedEmployee.employeeId}">

					<div class="day-payment-panels">
						<section class="amount-panel work-detail-panel">
							<header>
								<h2>근무별 지급내역</h2>
							</header>

							<div class="table-wrap">
								<table class="input-table work-payment-table">
									<thead>
										<tr>
											<th>일자</th>
											<th>지급률</th>
											<th>지급액</th>
											<th>소득세</th>
											<th>지방소득세</th>
										</tr>
									</thead>

									<tbody>
										<c:choose>
											<c:when test="${not empty selectedEmployee.workPayments}">
												<c:forEach var="work"
													items="${selectedEmployee.workPayments}">

													<tr>
														<td>
															<input type="date"
																value="${work.workDate}"
																readonly>
														</td>
														<td>
															<input type="text"
																value="${work.paymentRate}"
																readonly>
														</td>
														<td>
															<input type="text"
																value="${work.paymentAmount}"
																readonly>
														</td>
														<td>
															<input type="text"
																value="${work.incomeTax}"
																readonly>
														</td>
														<td>
															<input type="text"
																value="${work.localIncomeTax}"
																readonly>
														</td>
													</tr>
												</c:forEach>
											</c:when>

											<c:otherwise>
												<tr>
													<td class="empty-row" colspan="5">
														선택한 사원의 근무별 지급내역이 없습니다.
													</td>
												</tr>
											</c:otherwise>
										</c:choose>
									</tbody>
								</table>
							</div>
						</section>

						<section class="amount-panel deduction-panel">
							<header>
								<h2>공제항목</h2>

								<div class="deduction-tools">
									<button type="submit"
										name="calculationType"
										value="INSURANCE">
										4대보험
									</button>

									<button type="submit"
										name="calculationType"
										value="PERIOD_TAX">
										기간단위 소득세
									</button>
								</div>
							</header>

							<table class="input-table deduction-table">
								<thead>
									<tr>
										<th>항목</th>
										<th>금액</th>
									</tr>
								</thead>

								<tbody>
									<tr>
										<th>국민연금</th>
										<td>
											<input type="number"
												name="nationalPension"
												value="${selectedEmployee.nationalPension}">
										</td>
									</tr>
									<tr>
										<th>건강보험</th>
										<td>
											<input type="number"
												name="healthInsurance"
												value="${selectedEmployee.healthInsurance}">
										</td>
									</tr>
									<tr>
										<th>장기요양보험</th>
										<td>
											<input type="number"
												name="longTermCareInsurance"
												value="${selectedEmployee.longTermCareInsurance}">
										</td>
									</tr>
									<tr>
										<th>고용보험</th>
										<td>
											<input type="number"
												name="employmentInsurance"
												value="${selectedEmployee.employmentInsurance}">
										</td>
									</tr>
									<tr>
										<th>소득세</th>
										<td>
											<input type="number"
												name="incomeTax"
												value="${selectedEmployee.incomeTax}">
										</td>
									</tr>
									<tr>
										<th>지방소득세</th>
										<td>
											<input type="number"
												name="localIncomeTax"
												value="${selectedEmployee.localIncomeTax}">
										</td>
									</tr>
									<tr>
										<th>상조회비</th>
										<td>
											<input type="number"
												name="mutualAidFee"
												value="${selectedEmployee.mutualAidFee}">
										</td>
									</tr>
								</tbody>
							</table>
						</section>
					</div>

					<div class="day-worker-totals">
						<div>
							<strong>지급총액</strong>
							<span>${selectedEmployee.totalPayment}</span>
							<em>원</em>
						</div>

						<div>
							<strong>공제총액</strong>
							<span>${selectedEmployee.totalDeduction}</span>
							<em>원</em>
						</div>
					</div>

					<div class="net-payment">
						<span>실지급액:</span>
						<strong>${selectedEmployee.netPayment}</strong>
						<em>원</em>
					</div>

					<div class="form-actions">
						<button type="submit" class="button button-primary">
							저장
						</button>

						<button type="reset"
							class="button button-outline button-clear">
							내용지우기
						</button>
					</div>
				</form>
			</div>
		</section>
	</main>

	<div id="employee-add" class="modal-overlay">
		<section class="modal employee-modal">
			<header>
				<h2>급여 대상 일용직 사원 추가</h2>
				<a href="#" aria-label="닫기">×</a>
			</header>

			<form method="get"
				action="${pageContext.request.contextPath}/payroll/day-worker-payment-management">

				<div class="employee-search-row">
					<input type="search"
						name="employeeKeyword"
						placeholder="사원검색">

					<button class="button button-primary" type="submit">
						검색
					</button>

					<div class="employee-filters">
						<select name="departmentId">
							<option value="">부서별</option>
							<c:forEach var="department" items="${departments}">
								<option value="${department.departmentId}">
									${department.departmentName}
								</option>
							</c:forEach>
						</select>

						<select name="status">
							<option value="ACTIVE">재직</option>
						</select>
					</div>
				</div>
			</form>

			<form method="post"
				action="${pageContext.request.contextPath}/payroll/day-worker-payment-management/employees/add">

				<div class="modal-body">
					<table class="data-table">
						<thead>
							<tr>
								<th class="check-column">선택</th>
								<th>구분</th>
								<th>사원번호</th>
								<th>성명</th>
								<th>부서</th>
								<th>직위</th>
								<th>상태</th>
							</tr>
						</thead>

						<tbody>
							<c:choose>
								<c:when test="${not empty availableEmployeePage.content}">
									<c:forEach var="employee"
										items="${availableEmployeePage.content}">

										<tr>
											<td>
												<input type="checkbox"
													name="employeeIds"
													value="${employee.employeeId}">
											</td>
											<td>${employee.employmentTypeName}</td>
											<td>${employee.employeeNumber}</td>
											<td>${employee.employeeName}</td>
											<td>${employee.departmentName}</td>
											<td>${employee.positionName}</td>
											<td>${employee.statusName}</td>
										</tr>
									</c:forEach>
								</c:when>

								<c:otherwise>
									<tr>
										<td colspan="7" class="empty-row">
											추가할 수 있는 일용직 사원이 없습니다.
										</td>
									</tr>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>
				</div>

				<nav class="modal-pagination">
					<c:if test="${employeePage gt 1}">
						<a href="?employeePage=${employeePage - 1}#employee-add">
							‹ 이전
						</a>
					</c:if>

					<span>${employeePage}</span>

					<c:if test="${employeePage lt employeeTotalPages}">
						<a href="?employeePage=${employeePage + 1}#employee-add">
							다음 ›
						</a>
					</c:if>
				</nav>

				<div class="modal-actions">
					<button type="submit" class="button button-primary">
						사원선택
					</button>

					<a href="#" class="button button-neutral">
						선택취소
					</a>
				</div>
			</form>
		</section>
	</div>

	<%@ include file="/WEB-INF/view/common/footer.jspf" %>
</body>
</html>