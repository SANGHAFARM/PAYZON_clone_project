<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html lang="ja-JP">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>勤怠管理>日雇い勤務照会</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/attendance/day-worker-inquiry.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common/payzon-ui.css">
</head>
<body>
	<%@ include file="/WEB-INF/view/common/header.jspf"%>

	<main class="page-content day-worker-inquiry-page">
		<header class="page-heading">
			<div>
				<p>勤怠管理</p>
				<h1>日雇い勤務照会</h1>
			</div>
		</header>

		<section class="inquiry-card">
			<nav class="view-tabs" aria-label="照会区分">
				<a
					class="${empty param.view or param.view eq 'month' ? 'is-active' : ''}"
					href="?view=month">月別照会</a> <a
					class="${param.view eq 'detail' ? 'is-active' : ''}"
					href="?view=detail">詳細ビュー</a>
			</nav>
			<!-- =================================== -->
			<!-- =========詳細検索=========== -->
			<!-- =================================== -->
			<c:choose>
				<c:when test="${param.view eq 'detail'}">
					<div class="detail-layout">
						<form class="detail-filter" method="get">
							<input type="hidden" name="view" value="detail">
							<div class="filter-field">

								<span>労働日</span>
								<div class="date-range">
									<input type="date" lang="ja-JP" id="startDate" name="startDate"
										value="${startDate}"><i>~</i><input type="date" lang="ja-JP"
										id="endDate" name="endDate" value="${endDate}">
								</div>
							</div>
							<label class="filter-field"><span>氏名</span><input
								name="empNameKr" value="<c:out value='${param.empNameKr}' />"
								placeholder="氏名を入力してください。"></label> <label class="filter-field"><span>部署</span>
								<select name="departmentId">
									<option value="">全体部署</option>
									<c:forEach var="department" items="${departments}">
										<option value="${department.departmentId}"
											${param.departmentId eq department.departmentId ? 'selected' : ''}><c:out
												value="${department.departmentName}" /></option>
									</c:forEach>
							</select></label> <label class="filter-field"><span>現場/プロジェクト</span> <select
								name="projectId">
									<option value="">全現場/プロジェクト</option>
									<c:forEach var="project" items="${projects}">
										<option value="${project.projectId}"
											${param.projectId eq project.projectId ? 'selected' : ''}><c:out
												value="${project.projectName}" /></option>
									</c:forEach>
							</select></label>
							<div class="filter-actions">
								<button type="submit">検索</button>
								<a href="?view=detail">全体を見る</a>
							</div>
						</form>

						<div class="detail-table-wrap">
							<table class="detail-table">
								<thead>
									<tr>
										<th>労働日</th>
										<th>社員番号</th>
										<th>氏名</th>
										<th>部署</th>
										<th>現場/プロジェクト</th>
										<th>一日あたり</th>
										<th>支払い率</th>
										<th>所得税</th>
										<th>地方所得税</th>
										<th>差引支給額</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="record" items="${dailyWorkDetail}">
										<tr>
											<td><c:out value="${record.workDate}" /></td>
											<td><c:out value="${record.empNo}" /></td>
											<td><c:out value="${record.empNameKr}" /></td>
											<td><c:out value="${record.departmentName}" /></td>
											<td><c:out value="${record.projectName}" /></td>
											<td class="amount"><c:out value="${record.dailyPay}" /></td>
											<td><c:out value="${record.payRate}" /></td>
											<td class="amount tax"><c:out
													value="${record.incomeTax}" /></td>
											<td class="amount tax"><c:out
													value="${record.localIncomeTax}" /></td>
											<td class="amount net-pay"><c:out
													value="${record.actualPay}" /></td>
										</tr>
									</c:forEach>
									<c:if test="${empty dailyWorkDetail}">
										<tr>
											<td colspan="12" class="empty-row">照会された日雇い勤務履歴はありません。</td>
										</tr>
									</c:if>
								</tbody>
							</table>
						</div>
					</div>
				</c:when>

				<%--=========================== --%>
				<%--========毎月の照会======== --%>
				<%--=========================== --%>
				<c:otherwise>
					<form class="month-filter" method="get">
						<input type="hidden" name="view" value="month"> <select
							name="year" aria-label="年">
							<c:forEach var="y" begin="2015" end="2026">
								<option value="${y}"
									${y eq (empty param.year ? 2026 : param.year) ? 'selected' : ''}>${y}年</option>
							</c:forEach>
						</select> <select name="month" aria-label="月">
							<c:forEach var="monthNo" begin="1" end="12">
								<option value="${monthNo}"
									${monthNo eq (empty param.month ? 8 : param.month) ? 'selected' : ''}>${monthNo}月</option>
							</c:forEach>
						</select> <select name="departmentId" aria-label="部署">
							<option value="">全体部署</option>
							<c:forEach var="department" items="${departments}">
								<option value="${department.departmentId}"
									${param.departmentId eq department.departmentId ? 'selected' : ''}>
									<c:out value="${department.departmentName}" />
								</option>
							</c:forEach>
						</select> <select name="jobPositionId" aria-label="役職">
							<option value="">全役職</option>
							<c:forEach var="jobPosition" items="${jobPositions}">
								<option value="${jobPosition.jobPositionId}"
									${param.jobPositionId eq jobPosition.jobPositionId ? 'selected' : ''}>
									<c:out value="${jobPosition.jobPositionName}" />
								</option>
							</c:forEach>
						</select>
						<button type="submit">検索</button>
						<a href="?view=month">全体を見る</a>
					</form>

					<!-- 月別検索リスト -->
					<div class="month-table-wrap">
						<table class="month-table">
							<thead>
								<tr>
									<th>区分</th>
									<th>社員番号</th>
									<th>氏名</th>
									<th>部署</th>
									<th class="days-heading"><span>労働日</span>
										<div>
											<c:forEach var="dayNo" begin="1" end="31">
												<b>${dayNo}</b>
											</c:forEach>
										</div></th>
									<th>合計</th>
									<th>所得税</th>
									<th>地方所得税</th>
									<th>実際の給与合計</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="employee" items="${dailyWorkList}">
									<tr>
										<td><ui:code-label value="${employee.empType}" /></td>
										<td><c:out value="${employee.empNo}" /></td>
										<td><c:out value="${employee.empNameKr}" /></td>
										<td><c:out value="${employee.departmentName}" /></td>
										<td class="day-cells"><c:forEach var="dayNo" begin="1"
												end="31">
												<c:set var="work" value="${employee.workDayMap[dayNo]}" />
												<span><c:if test="${not empty work}">
														<a href="#work-detail-${work.dailyWorkRecordId}"
															aria-label="${dayNo}日勤務記録">●</a>
													</c:if></span>
											</c:forEach></td>
										<td class="amount"><c:out value="${employee.totalDays}" /></td>
										<td class="amount tax"><c:out
												value="${employee.totalIncomeTax}" /></td>
										<td class="amount tax"><c:out
												value="${employee.totalLocalIncomeTax}" /></td>
										<td class="amount net-pay"><c:out
												value="${employee.totalActualPay}" /></td>
									</tr>
								</c:forEach>
								<c:if test="${empty dailyWorkList}">
									<tr>
										<td colspan="9" class="empty-row">照会された日雇いの毎月の勤務履歴はありません。</td>
									</tr>
								</c:if>
							</tbody>
						</table>
					</div>
				</c:otherwise>
			</c:choose>
		</section>
	</main>

	<%-- 日雇い勤務状況 --%>
	<c:forEach var="employee" items="${dailyWorkList}">
		<c:if test="${not empty employee.workDayMap}">
			<c:forEach var="entry" items="${employee.workDayMap}">
				<c:set var="work" value="${entry.value}" />
				<div id="work-detail-${work.dailyWorkRecordId}"
					class="modal-overlay">
					<section class="modal work-detail-modal" role="dialog"
						aria-modal="true"
						aria-labelledby="work-title-${work.dailyWorkRecordId}">
						<header>
							<h2 id="work-title-${work.dailyWorkRecordId}">日雇い勤務状況</h2>
							<a href="#" aria-label="閉じる">&times;</a>
						</header>
						<div class="modal-body">
							<dl>
								<div>
									<dt>労働者</dt>
									<dd
										style="text-align: right !important; display: block !important;">
										<c:out value="${employee.empNameKr}" />
									</dd>
								</div>
								<div>
									<dt>現場/プロジェクト</dt>
									<dd
										style="text-align: right !important; display: block !important;">
										<c:out value="${work.projectName}" />
									</dd>
								</div>
								<div>
									<dt>労働日</dt>
									<dd
										style="text-align: right !important; display: block !important;">
										<c:out value="${work.workDate}" />
									</dd>
								</div>
								<div class="money">
									<dt>一日あたり</dt>
									<dd>
										<c:out value="${work.dailyPay}" />
										ウォン
									</dd>
								</div>
								<div class="money">
									<dt>支払い率</dt>
									<dd>
										<c:out value="${work.payRate}" />
									</dd>
								</div>
								<div class="money">
									<dt>所得税</dt>
									<dd>
										<c:out value="${work.incomeTax}" />
										ウォン
									</dd>
								</div>
								<div class="money">
									<dt>地方所得税</dt>
									<dd>
										<c:out value="${work.localIncomeTax}" />
										ウォン
									</dd>
								</div>
								<div class="money total">
									<dt>差引支給額</dt>
									<dd>
										<c:out value="${work.actualPay}" />
										ウォン
									</dd>
								</div>
							</dl>
						</div>
					</section>
				</div>
			</c:forEach>
		</c:if>
	</c:forEach>


	<script>
	//詳細照会勤務日の設定関連スクリプト
		const startDateInput = document.getElementById('startDate');
		const endDateInput = document.getElementById('endDate');

		// 開始日が変更されたとき
		startDateInput.addEventListener('change', function() {
			const startDate = startDateInput.value;

			// 終了日の最小値（min）を開始日に設定
			endDateInput.min = startDate;

			// すでに入力された終了日が新しく設定された開始日より前の場合、終了日の初期化
			if (endDateInput.value && endDateInput.value < startDate) {
				endDateInput.value = '';
			}
		});

		// 終了日が変更されたとき
		endDateInput.addEventListener('change', function() {
			const endDate = endDateInput.value;

			// 開始日の最大値（max）を終了日に設定
			startDateInput.max = endDate;

			// すでに入力された開始日が新しく設定された終了日より後の場合、開始日の初期化
			if (startDateInput.value && startDateInput.value > endDate) {
				startDateInput.value = '';
			}
		});
	</script>

	<%@ include file="/WEB-INF/view/common/footer.jspf"%>
</body>
</html>
