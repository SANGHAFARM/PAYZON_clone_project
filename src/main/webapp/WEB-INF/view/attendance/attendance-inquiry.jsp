<%-- 조회 결과와 입력 양식을 표시하는 JSP 화면이다. --%>
<%-- 照会結果と入力フォームを表示するJSP画面である。 --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags"%>
<c:set var="view" value="${empty param.view ? 'MONTH' : param.view}" />
<c:set var="currentPageUrl" value="${pageContext.request.requestURI}" />
<c:set var="monthDayCount" value="31" />
<c:if test="${not empty daysInMonth}">
	<c:set var="monthDayCount" value="${daysInMonth}" />
</c:if>
<!DOCTYPE html>
<html lang="ja-JP">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>勤怠管理>勤怠照会</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/attendance/attendance-inquiry.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common/payzon-ui.css">
</head>
<body>
	<%@ include file="/WEB-INF/view/common/header.jspf"%>
	<main class="page-content attendance-inquiry-page">
		<header class="page-heading">
			<div>
				<p>勤怠管理</p>
				<h1>勤怠照会</h1>
			</div>
		</header>
		<section class="inquiry-card">
			<nav class="inquiry-tabs">
				<a class="${view eq 'MONTH' ? 'is-active' : ''}"
					href="${pageContext.request.contextPath}/attendance/attendance-inquiry.do?view=MONTH">月別照会</a>
				<a class="${view eq 'DETAIL' ? 'is-active' : ''}"
					href="${pageContext.request.contextPath}/attendance/attendance-inquiry.do?view=DETAIL">詳細照会</a>
			</nav>

			<c:choose>
				<c:when test="${view eq 'MONTH'}">
					<form class="month-search"
						action="${pageContext.request.contextPath}/attendance/attendance-inquiry.do"
						method="get">
						<input type="hidden" name="view" value="MONTH"> <select
							name="year" aria-label="照会年">
							<option value="">年を選択</option>
							<%-- itemsをクリアしてbegin、endだけ書くと2015から2030まで数字が順番に入ります --%>
							<c:forEach var="y" begin="2015" end="2030">
								<option value="${y}" ${year eq y ? 'selected' : ''}>
									<c:out value="${y}" />年
								</option>
							</c:forEach>
						</select> <select name="month" aria-label="照会月">
							<c:forEach var="m" begin="1" end="12">
								<option value="${m}" ${month eq m ? 'selected' : ''}>
									<c:out value="${m}" />月
								</option>
							</c:forEach>
						</select> <select name="status" aria-label="ステータス別">
							<option value="">ステータス別</option>
							<option value="재직" ${status eq '재직'?'selected' :'' }>在職</option>
							<option value="퇴직" ${status eq '퇴직'?'selected':'' }>退職</option>
						</select> <select name="empType" aria-label="区分別">
							<option value="">区分別</option>
							<c:forEach var="type" items="${empTypes}">
								<option value="${type}" ${type eq empType ? 'selected' : '' }><ui:code-label
										value="${type}" /></option>
							</c:forEach>
						</select> <select name="departmentId" aria-label="部署別">
							<option value="">部署別</option>
							<c:forEach var="department" items="${departments}">
								<option value="${department.departmentId}"
									${department.departmentId eq departmentId ?'selected':'' }><c:out
										value="${department.departmentName}" /></option>
							</c:forEach>
						</select> <select name="jobPositionId" aria-label="役職別">
							<option value="">役職別</option>
							<c:forEach var="jobPosition" items="${jobPositions}">
								<option value="${jobPosition.jobPositionId}"
									${jobPosition.jobPositionId eq jobPositionId ? 'selected':'' }><c:out
										value="${jobPosition.jobPositionName}" /></option>
							</c:forEach>
						</select>
						<button type="submit">照会</button>
					</form>

					<div class="monthly-table-wrap">
						<table class="monthly-table">
							<thead>
								<tr>
									<th>区分</th>
									<th>社員番号</th>
									<th>氏名</th>
									<th>部署</th>
									<th>役職</th>
									<c:forEach var="day" begin="1" end="${monthDayCount}">
										<th class="day-heading"><c:out value="${day}" /></th>
									</c:forEach>
									<th class="total-heading">合計</th>
									<th>休暇控除</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="employee" items="${monthlyEmployees}">
									<c:set var="detailUrl"
										value="${pageContext.request.contextPath}/attendance/attendance-inquiry.do?view=DETAIL&amp;empNameKr=${employee.empNameKr}&amp;useName=Y&amp;usePeriod=Y&amp;year=${year}&amp;month=${month}" />
									<tr>
										<td><a href="${detailUrl}"><ui:code-label
													value="${employee.empType}" /></a></td>
										<td><a href="${detailUrl}"><c:out
													value="${employee.empNo}" /></a></td>
										<td><a href="${detailUrl}"><c:out
													value="${employee.empNameKr}" /></a></td>
										<td><a href="${detailUrl}"><c:out
													value="${employee.departmentName}" /></a></td>
										<td><a href="${detailUrl}"><c:out
													value="${employee.jobPositionName}" /></a></td>
										<c:forEach var="day" begin="1" end="${monthDayCount}">
											<td class="day-cell"><a href="${detailUrl}"><c:if
														test="${not empty employee.dailyAttendance[day]}">
														<span class="attendance-dot"
															title="${employee.dailyAttendance[day]}"></span>
													</c:if></a></td>
										</c:forEach>
										<td class="summary-cell"><a href="${detailUrl}"><ul
													class="summary-list">
													<c:forEach var="item" items="${employee.totalAttendValue}">
														<li><c:out
																value="${item.attendName}: ${item.totalValue}" /> <c:choose>
																<c:when test="${item.unitType eq '일'}"> (d) </c:when>
																<c:otherwise> (h) </c:otherwise>
															</c:choose></li>
													</c:forEach>
												</ul></a></td>
										<td><a href="${detailUrl}"><c:out
													value="${employee.totalLeaveDeduction}" /></a></td>
									</tr>
								</c:forEach>
								<c:if test="${empty monthlyEmployees}">
									<tr>
										<td colspan="${monthDayCount + 7}" class="empty-row">照会された
											毎月の勤務履歴はありません。</td>
									</tr>
								</c:if>
							</tbody>
						</table>
					</div>
				</c:when>

				<c:otherwise>
					<div class="detail-layout">
						<form class="detail-search"
							action="${pageContext.request.contextPath}/attendance/attendance-inquiry.do?view=DETAIL"
							method="get">
							<input type="hidden" name="view" value="DETAIL"> <label>
								<input type="checkbox" name="useInputDate" value="Y"
								${param.useInputDate eq 'Y' ? 'checked' : ''}> <span>入力日</span>
								<input type="date" lang="ja-JP" name="inputDate"
								value="${inputDateStr}">
							</label> <label> <input type="checkbox" name="usePeriod"
								value="Y" ${param.usePeriod eq 'Y' ? 'checked' : ''}> <span>勤怠期間</span>
								<span class="detail-period"> <input type="date"
									lang="ja-JP" name="startDate" id="startDate"
									value="${startDateStr}"> <i>~</i> <input type="date"
									lang="ja-JP" name="endDate" id="endDate" value="${endDateStr}">
							</span>
							</label> <label> <input type="checkbox" name="useDepartment"
								value="Y" ${param.useDepartment eq 'Y' ? 'checked' : ''}>
								<span>部署</span> <select name="departmentId">
									<option value="">選択してください。</option>
									<c:forEach var="department" items="${departments}">
										<option value="${department.departmentId}"
											${department.departmentId eq departmentId ? 'selected' : ''}><c:out
												value="${department.departmentName}" /></option>
									</c:forEach>
							</select>
							</label> <label> <input type="checkbox" name="useName" value="Y"
								${param.useName eq 'Y' ? 'checked' : ''}> <span>氏名</span>
								<input type="search" name="empNameKr" value="${param.empNameKr}"
								placeholder="氏名を入力してください">
							</label> <label> <input type="checkbox" name="useGroup" value="Y"
								${param.useGroup eq 'Y' ? 'checked' : ''}> <span>勤怠グループ</span>
								<select name="attendanceGroupId">
									<option value="">選択してください。</option>
									<c:forEach var="group" items="${attendanceGroups}">
										<option value="${group.attendanceGroupId}"
											${group.attendanceGroupId eq attendanceGroupId ? 'selected' : ''}><c:out
												value="${group.groupName}" /></option>
									</c:forEach>
							</select>
							</label> <label> <input type="checkbox" name="useItem" value="Y"
								${param.useItem eq 'Y' ? 'checked' : ''}> <span>勤怠項目</span>
								<select name="attendanceItemId">
									<option value="">選択してください。</option>
									<c:forEach var="item" items="${attendanceItems}">
										<option value="${item.attendanceItemId}"
											${item.attendanceItemId eq attendanceItemId ? 'selected' : ''}><c:out
												value="${item.attendName}" /></option>
									</c:forEach>
							</select>
							</label> <label> <input type="checkbox" name="useHoliday"
								value="Y" ${param.useHoliday eq 'Y' ? 'checked' : ''}> <span>休暇項目</span>
								<select name="leaveItemId">
									<option value="">選択してください。</option>
									<c:forEach var="leaveItem" items="${leaveItems}">
										<option value="${leaveItem.leaveItemId}"
											${leaveItem.leaveItemId eq leaveItemId ? 'selected' : ''}><c:out
												value="${leaveItem.itemName}" /></option>
									</c:forEach>
							</select>
							</label> <label> <input type="checkbox" name="useNote" value="Y"
								${param.useNote eq 'Y' ? 'checked' : ''}> <span>摘要</span>
								<input type="text" name="note" value="${param.note}">
							</label>
							<div class="detail-actions">
								<button type="submit">検索</button>
								<a
									href="${pageContext.request.contextPath}/attendance/attendance-inquiry.do?view=DETAIL">全体を見る</a>
							</div>
						</form>

						<!--  //詳細な勤怠記録 -->
						<!--  //詳細な勤怠記録 -->
						<!--  //詳細な勤怠記録 -->
						<div>
							<div class="detail-table-wrap">
								<table class="detail-table">
									<thead>
										<tr>
											<th>入力日</th>
											<th>区分</th>
											<th>氏名</th>
											<th>部署</th>
											<th>役職</th>
											<th>勤怠項目</th>
											<th>勤怠期間</th>
											<th>勤怠日数</th>
											<th>金額</th>
											<th>備考</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach var="record" items="${attendanceDetail.content}">
											<tr>
												<td><c:out value="${record.inputDate}" /></td>
												<td><ui:code-label value="${record.empType}" /></td>
												<td><c:out value="${record.empNameKr}" /></td>
												<td><c:out value="${record.departmentName}" /></td>
												<td><c:out value="${record.jobPositionName}" /></td>
												<td><c:out value="${record.attendName}" /></td>
												<td><c:out value="${record.startDate}" /> <c:if
														test="${not empty record.endDate and record.startDate ne record.endDate}">
														<i>~</i>
														<c:out value="${record.endDate}" />
													</c:if></td>
												<td><c:out value="${record.attendValue}" /> <c:choose>
														<c:when test="${record.unitType eq '일'}"> (d) </c:when>
														<c:otherwise> (h) </c:otherwise>
													</c:choose></td>
												<td><c:out value="${record.payAmount}" /></td>
												<td><c:out value="${record.note}" /></td>
											</tr>
										</c:forEach>
										<c:if test="${empty attendanceDetail.content}">
											<tr>
												<td colspan="10" class="empty-row">照会された詳細な勤労履歴はありません。</td>
											</tr>
										</c:if>
									</tbody>
								</table>
							</div>

							<c:if
								test="${not empty attendanceDetail and attendanceDetail.total > 0}">
								<nav class="pagination" aria-label="ページ移動">
									<c:if test="${attendanceDetail.startPage > 10}">
										<a
											href="?view=DETAIL&pageNo=${attendanceDetail.startPage - 10}${param.useInputDate eq 'Y' ? '&useInputDate=Y&inputDate='.concat(param.inputDate) : ''}${param.usePeriod eq 'Y' ? '&usePeriod=Y&startDate='.concat(param.startDate).concat('&endDate=').concat(param.endDate) : ''}${param.useDepartment eq 'Y' ? '&useDepartment=Y&departmentId='.concat(param.departmentId) : ''}${param.useName eq 'Y' ? '&useName=Y&empNameKr='.concat(param.empNameKr) : ''}${param.useGroup eq 'Y' ? '&useGroup=Y&attendanceGroupId='.concat(param.attendanceGroupId) : ''}${param.useItem eq 'Y' ? '&useItem=Y&attendanceItemId='.concat(param.attendanceItemId) : ''}${param.useHoliday eq 'Y' ? '&useHoliday=Y&leaveItemId='.concat(param.leaveItemId) : ''}${param.useNote eq 'Y' ? '&useNote=Y&note='.concat(param.note) : ''}">[이전]</a>
									</c:if>

									<c:forEach var="pNo" begin="${attendanceDetail.startPage}"
										end="${attendanceDetail.endPage}">
										<c:choose>
											<c:when test="${pNo eq attendanceDetail.currentPage}">
												<strong
													style="margin: 0 4px; color: #007bff; font-weight: bold;">${pNo}</strong>
											</c:when>
											<c:otherwise>
												<a
													href="?view=DETAIL&pageNo=${pNo}${param.useInputDate eq 'Y' ? '&useInputDate=Y&inputDate='.concat(param.inputDate) : ''}${param.usePeriod eq 'Y' ? '&usePeriod=Y&startDate='.concat(param.startDate).concat('&endDate=').concat(param.endDate) : ''}${param.useDepartment eq 'Y' ? '&useDepartment=Y&departmentId='.concat(param.departmentId) : ''}${param.useName eq 'Y' ? '&useName=Y&empNameKr='.concat(param.empNameKr) : ''}${param.useGroup eq 'Y' ? '&useGroup=Y&attendanceGroupId='.concat(param.attendanceGroupId) : ''}${param.useItem eq 'Y' ? '&useItem=Y&attendanceItemId='.concat(param.attendanceItemId) : ''}${param.useHoliday eq 'Y' ? '&useHoliday=Y&leaveItemId='.concat(param.leaveItemId) : ''}${param.useNote eq 'Y' ? '&useNote=Y&note='.concat(param.note) : ''}"
													style="margin: 0 4px;">${pNo}</a>
											</c:otherwise>
										</c:choose>
									</c:forEach>

									<c:if
										test="${attendanceDetail.endPage < attendanceDetail.totalPages}">
										<a
											href="?view=DETAIL&pageNo=${attendanceDetail.startPage + 10}${param.useInputDate eq 'Y' ? '&useInputDate=Y&inputDate='.concat(param.inputDate) : ''}${param.usePeriod eq 'Y' ? '&usePeriod=Y&startDate='.concat(param.startDate).concat('&endDate=').concat(param.endDate) : ''}${param.useDepartment eq 'Y' ? '&useDepartment=Y&departmentId='.concat(param.departmentId) : ''}${param.useName eq 'Y' ? '&useName=Y&empNameKr='.concat(param.empNameKr) : ''}${param.useGroup eq 'Y' ? '&useGroup=Y&attendanceGroupId='.concat(param.attendanceGroupId) : ''}${param.useItem eq 'Y' ? '&useItem=Y&attendanceItemId='.concat(param.attendanceItemId) : ''}${param.useHoliday eq 'Y' ? '&useHoliday=Y&leaveItemId='.concat(param.leaveItemId) : ''}${param.useNote eq 'Y' ? '&useNote=Y&note='.concat(param.note) : ''}">[다음]</a>
									</c:if>

								</nav>
							</c:if>

						</div>
					</div>

				</c:otherwise>
			</c:choose>
		</section>
	</main>
	<%@ include file="/WEB-INF/view/common/footer.jspf"%>
	<script>
		//상세조회근무일의 설정관련 스크립트
		//詳細照会勤務日の設定関連スクリプト
		const startDateInput = document.getElementById('startDate');
		const endDateInput = document.getElementById('endDate');

		//시작일이 변경되었을 때
		// 開始日が変更されたとき
		startDateInput.addEventListener('change', function() {
			const startDate = startDateInput.value;
			
			//종료일의 최소치를 시작일로 설정
			// 終了日の最小値（min）を開始日に設定
			endDateInput.min = startDate;

			//이미 입력된 종료일이 새롭게 설정된 시작일보다 앞인 경우, 종료일 초기화
			// すでに入力された終了日が新しく設定された開始日より前の場合、終了日の初期化
			if (endDateInput.value && endDateInput.value < startDate) {
				endDateInput.value = '';
			}
		});

		//종료일이 변경되었을 때
		// 終了日が変更されたとき
		endDateInput.addEventListener('change', function() {
			const endDate = endDateInput.value;

			//시작일의 최대치를 종료일로 설정
			// 開始日の最大値（max）を終了日に設定
			startDateInput.max = endDate;

			//이미 입력된 시작일이 새롭게 설정된 종료일보다 뒤인 경우, 시작일 초기화
			// すでに入力された開始日が新しく設定された終了日より後の場合、開始日の初期化
			if (startDateInput.value && startDateInput.value > endDate) {
				startDateInput.value = '';
			}
		});
	</script>

	<script>
document.querySelectorAll('.detail-search label').forEach(label => {
const checkbox = label.querySelector('input[type="checkbox"]');
const fields = label.querySelectorAll('input:not([type="checkbox"]), select');

const syncDisabled = () => {
    fields.forEach(f => f.disabled = !checkbox.checked);
};

syncDisabled();
checkbox.addEventListener('change', syncDisabled);
});
</script>
</body>
</html>
