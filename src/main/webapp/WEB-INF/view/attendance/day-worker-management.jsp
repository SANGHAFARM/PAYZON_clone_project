<%-- 조회 결과와 입력 양식을 표시하는 JSP 화면이다. --%>
<%-- 照会結果と入力フォームを表示するJSP画面である。 --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html lang="ja-JP">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>日雇い勤務記録/管理</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common/common.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/attendance/day-worker-management.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common/payzon-ui.css">
</head>
<body>
	<%@ include file="/WEB-INF/view/common/header.jspf"%>
	<main class="page-content day-worker-page">
		<header class="page-heading">
			<div>
				<p>勤怠管理</p>
				<h1>日雇い勤務記録/管理</h1>
			</div>
		</header>

		<section class="worker-panel">
			<!-- =========================================================== -->
			<!--                  社員照会（検索語、在職状態）領域                   -->
			<!-- =========================================================== -->
			<div class="worker-search">
				<form method="get"
					action="${pageContext.request.contextPath}/attendance/day-worker-management.do">
					<!-- プロパティで修正が必要!!!!!!!!!!!!!!!!!! -->
					<input type="search" name="keyword"
						value="<c:out value='${keyword}'/>" placeholder="検索語を入力"
						aria-label="検索語">

					<!-- 検索時に現在選択されている「ステータス」が保持されるようにhiddenとして含める -->
					<c:if test="${not empty status }">
						<input type="hidden" name="status" value="${status}">
					</c:if>

					<button type="submit" class="button button-primary">検索</button>
					<a class="button button-outline"
						href="${pageContext.request.contextPath}/attendance/day-worker-management.do">全体を見る</a>
					<!-- プロパティで修正が必要!!!!!!!!!!!!!!!!!! -->
				</form>

				<form class="status-filter" method="get"
					action="${pageContext.request.contextPath}/attendance/day-worker-management.do">
					<!-- プロパティで修正が必要!!!!!!!!!!!!!!!!!! -->
					<select name="status" aria-label="ステータス別">
						<option value="">ステータス別</option>
						<option value="재직" ${ status eq '재직' ? 'selected' : ''}>在職</option>
						<option value="퇴직" ${status eq '퇴직' ? 'selected' : ''}>退職</option>
					</select>

					<!-- ステータスを照会するときに現在入力されているクエリが保持されるようにhiddenとして含める -->
					<c:if test="${not empty param.keyword }">
						<input type="hidden" name="keyword" value="${param.keyword }">
					</c:if>
					<button type="submit" class="button button-primary">照会</button>
				</form>
			</div>

			<!-- =========================================================== -->
			<!--                          社員一覧                              -->
			<!-- =========================================================== -->
			<div class="worker-layout">
				<div class="employee-list-wrap">
					<table class="data-table employee-table">
						<thead>
							<tr>
								<th class="check-cell"><input type="checkbox"
									aria-label="全選択"></th>
								<th>区分</th>
								<th>社員番号</th>
								<th>氏名</th>
								<th>部署</th>
								<th>勤務記録</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="dayworker" items="${dayWorkers}">
								<tr>
									<td class="check-cell"><input type="checkbox"
										name="employeeIds" value="${dayworker.employeeId}"
										aria-label="${dayworker.empNameKr}を選択"
										${not empty editId ? 'disabled' : '' } form="recordForm"></td>
									<td><ui:code-label value="${dayworker.empType}" /></td>
									<td><c:out value="${dayworker.empNo}" /></td>
									<td><c:out value="${dayworker.empNameKr}" /></td>
									<td><c:out value="${dayworker.departmentName}" /></td>
									<td><a class="button button-small"
										href="${pageContext.request.contextPath}/attendance/day-worker-management.do?employeeId=${dayworker.employeeId}&status=${status}&keyword=${keyword}#work-history-${dayworker.employeeId}">管理</a></td>
								</tr>
							</c:forEach>
							<c:if test="${empty dayWorkers}">
								<tr>
									<td colspan="6" class="empty-row">照会された日雇い社員はいません。</td>
								</tr>
							</c:if>
						</tbody>
					</table>
				</div>

				<!-- =========================================================== -->
				<!--                         日雇い勤務入力フォーム                        -->
				<!-- =========================================================== -->
				<form id="recordForm" class="record-form" method="post"
					action="${pageContext.request.contextPath}/attendance/day-worker-management.do">

					<c:if test="${not empty editId}">
						<input type="hidden" name="editId" value="${editId}">
					</c:if>

					<h2>日雇い勤務記録を入力</h2>
					<div class="form-fields">
						<label><span>勤務日</span> <input type="date" lang="ja-JP"
							name="workDate" required
							value="${empty editId ? today : workDate}"></label> <label><span>現場/プロジェクト</span><span
							class="project-control"><select name="projectId">
									<option value="">選択してください。</option>

									<!-- プロジェクトリストの管理 -->
									<c:forEach var="project" items="${projects}">
										<option value="${project.projectId}"
											${projectId eq project.projectId ? 'selected' : ''}><c:out
												value="${project.projectName}" /></option>
									</c:forEach>
							</select><a class="button button-project" href="#project-manager">リスト管理</a></span>

						</label> <label> <span>日当</span> <span class="amount-control">
								<input type="number" id="dailyPay" name="dailyPay" min="0"
								value="${empty dailyPay ? '' : dailyPay}"
								placeholder="日当を入力してください"> <em>円</em>
						</span></label> <label> <span>支給率</span> <input type="number"
							id="payRate" name="payRate" min="0" step="0.1"
							value="${empty editId ? 1.0 : payRate}"></label> <label
							class="calculated"> <span>所得税</span> <span
							class="amount-control"> <input type="text" id="incomeTax"
								name="incomeTax"
								value="${empty editId ? calculatedIncomeTax : incomeTax}"
								placeholder="自動計算されます" readonly> <em>円</em></span></label> <label
							class="calculated"> <span>地方所得税</span> <span
							class="amount-control"> <input type="text"
								id="localIncomeTax" name="localIncomeTax"
								value="${empty editId ? calculatedLocalIncomeTax : localIncomeTax}"
								placeholder="自動計算されます" readonly> <em>円</em></span></label> <label
							class="calculated"><span>差引支給額</span><span
							class="amount-control"> <input type="text" id="actualPay"
								name="actualPay"
								value="${empty editId ? calculatedActualPay : actualPay}"
								placeholder="自動計算されます" readonly><em>円</em></span> </label>
					</div>
					<div class="form-actions">

						<c:choose>
							<c:when test="${empty editId}">
								<button type="submit" id="saveBtn"
									class="button button-primary action-button">保存</button>
							</c:when>
							<c:otherwise>
								<button type="submit"
									class="button button-primary action-button">修正</button>
							</c:otherwise>
						</c:choose>

						<!-- 修正モード時に修正キャンセルボタンを有効にする -->
						<c:choose>
							<c:when test="${empty editId}">
								<button type="reset"
									class="button button-muted action-button clear-button">内容を消去する</button>
							</c:when>
							<c:otherwise>
								<a
									href="${pageContext.request.contextPath}/attendance/day-worker-management.do"
									class="button button-muted action-button">修正キャンセル</a>
							</c:otherwise>
						</c:choose>
					</div>
				</form>
			</div>
		</section>
	</main>

	<!-- =========================================================== -->
	<!--                         社員別勤務記録                          -->
	<!-- =========================================================== -->
	<c:if test="${not empty employeeId and empty editId}">
		<c:forEach var="dayworker" items="${dayWorkers}">
			<c:if test="${dayworker.employeeId eq employeeId}">
				<div id="work-history-${dayworker.employeeId}" class="modal-overlay">
					<section class="modal work-history-modal" role="dialog"
						aria-modal="true"
						aria-labelledby="history-title-${dayworker.employeeId}">
						<header>
							<h2 id="history-title-${dayworker.employeeId}">社員別勤務記録</h2>
							<a
								href="${pageContext.request.contextPath}/attendance/day-worker-management.do?status=${status}&keyword=${keyword}"
								aria-label="閉じる">&times;</a>
						</header>
						<div class="modal-body">
							<div class="record-summary">
								<p>
									氏名： <strong><c:out value="${dayworker.empNameKr}" /></strong>
									(
									<c:out value="${dayworker.empNo}" />
									) 部署:
									<c:out value="${dayworker.departmentName}" />
								</p>
								<form method="get">
									<input type="hidden" name="employeeId"
										value="${dayworker.employeeId}"> <select name="year"
										aria-label="年">
										<c:forEach var="y" begin="2015" end="2026">
											<option value="${y}" ${y eq year ? 'selected' : ''}>${y}年</option>
										</c:forEach>
									</select> <select name="month" aria-label="月">
										<c:forEach var="monthNo" begin="1" end="12">
											<option value="${monthNo}"
												${monthNo eq month ? 'selected' : ''}>${monthNo}月</option>
										</c:forEach>
									</select>
									<button type="submit">照会</button>
								</form>
							</div>
							<table class="data-table">
								<thead>
									<tr>
										<th>番号</th>
										<th>労働日</th>
										<th>現場/プロジェクト</th>
										<th>一日あたり</th>
										<th>支払い率</th>
										<th>支払額</th>
										<th>所得税</th>
										<th>地方所得税</th>
										<th>差引支給額</th>
										<th>修正/削除</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="record" items="${workRecords}"
										varStatus="status">
										<tr>
											<td><c:out value="${status.count }" /></td>
											<td><c:out value="${record.workDate}" /></td>
											<td><c:out value="${record.projectName}" /></td>
											<td><c:out value="${record.dailyPay}" /></td>
											<td><c:out value="${record.payRate}" /></td>
											<td><c:out value="${record.grossPay}" /></td>
											<td><c:out value="${record.incomeTax}" /></td>
											<td><c:out value="${record.localIncomeTax}" /></td>
											<td><c:out value="${record.actualPay}" /></td>
											<td><a class="mini-button"
												href="?editId=${record.dailyWorkRecordId}&employeeId=${dayworker.employeeId}&workDate=${record.workDate}&projectId=${record.projectId }&dailyPay=${record.dailyPay }&payRate=${record.payRate }&incomeTax=${record.incomeTax }&localIncomeTax=${record.localIncomeTax }&actualPay=${record.actualPay }">修正</a>
												<form
													action="${pageContext.request.contextPath}/attendance/day-worker-management.do"
													method="post" style="display: inline;">
													<input type="hidden" name="deleteId"
														value="${record.dailyWorkRecordId}"> <input
														type="hidden" name="employeeId"
														value="${dayworker.employeeId}"> <input
														type="hidden" name="year" value="${year}"> <input
														type="hidden" name="month" value="${month}">
													<button type="submit" class="mini-button mini-delete">削除</button>
												</form></td>
										</tr>
									</c:forEach>
									<c:if test="${empty workRecords}">
										<tr>
											<td colspan="10" class="empty-row">登録された勤務記録はありません。</td>
										</tr>
									</c:if>
								</tbody>
							</table>
						</div>
					</section>
				</div>
			</c:if>
		</c:forEach>
	</c:if>

	<div id="project-manager" class="modal-overlay">
		<section class="modal project-modal" role="dialog" aria-modal="true"
			aria-labelledby="project-title">
			<header>
				<h2 id="project-title">フィールド/プロジェクトリスト管理</h2>
				<a
					href="${pageContext.request.contextPath}/attendance/day-worker-management.do?status=${status}&keyword=${keyword}"
					aria-label="閉じる">&times;</a>
			</header>
			<div class="modal-body">
				<ul class="project-list">
					<c:forEach var="project" items="${projects}">
						<li><span><c:out value="${project.projectName}" /></span> <span
							style="display: inline-flex; gap: 4px;"> <!-- 修正リンクをクリックするとパラメータが含まれ、ページが更新され、下のフォームに値が入力されます -->
								<a class="mini-button"
								href="?projectId=${project.projectId}&projectName=${project.projectName}#project-manager">修正</a>

								<!-- 削除フォーム -->
								<form
									action="${pageContext.request.contextPath}/attendance/project-manage.do#project-manager"
									method="post" style="display: inline; margin: 0;">
									<input type="hidden" name="projectAction" value="delete">
									<input type="hidden" name="projectId"
										value="${project.projectId}">
									<button type="submit" class="mini-button mini-delete">削除</button>
								</form>
						</span></li>
					</c:forEach>
					<c:if test="${empty projects}">
						<li><span>登録された現場/プロジェクトはありません。</span></li>
					</c:if>
				</ul>

				<form class="project-add" method="post"
					action="${pageContext.request.contextPath}/attendance/project-manage.do#project-manager"
					style="display: flex; gap: 8px; align-items: center;">

					<!-- 修正モードのときは「edit」、そうでないときは「add」 -->
					<input type="hidden" name="projectAction"
						value="${empty param.projectId ? 'add' : 'edit'}">

					<!-- 修正モードの場合にのみprojectIdを渡す -->
					<c:if test="${not empty param.projectId}">
						<input type="hidden" name="projectId" value="${param.projectId}">
					</c:if>

					<!-- 入力ウィンドウ -->
					<input type="text" name="projectName"
						value="${empty param.projectId ? '' : param.projectName}"
						placeholder="新しいフィールド/プロジェクト名" required style="flex: 1;">

					<!-- 追加/編集ボタン -->
					<button type="submit" class="button button-primary"
						style="white-space: nowrap;">${empty param.projectId ? '追加' : '修正'}
					</button>

					<!-- 修正モードのときのみキャンセルボタンを表示 -->
					<c:if test="${not empty param.projectId}">
						<a href="?#project-manager" class="button button-muted"
							style="text-decoration: none; white-space: nowrap;">キャンセル</a>
					</c:if>

				</form>
			</div>
		</section>
	</div>

	<!--  所得税、地方所得税、差引支給額を自動計算するスクリプト -->
	<script>
/*  ウェブページのすべてのHTML構造が完全にロードされた安全な時点でスクリプトが実行されるように包む役割 */
document.addEventListener('DOMContentLoaded', function() {

    //変数を保存
    const dailyPayInput = document.getElementById('dailyPay'); //一日あたり
    const payRateInput = document.getElementById('payRate');//支払い率
    const incomeTaxInput = document.getElementById('incomeTax');//所得税
    const localIncomeTaxInput = document.getElementById('localIncomeTax');//地方所得税
    const actualPayInput = document.getElementById('actualPay');//差引支給額

    //税計算方法
    function calculateTaxes() {
    // 入力された日給と支給率を数値に変換し、未入力の場合はそれぞれ0と1.0を使用
        const dailyPay = parseFloat(dailyPayInput.value) || 0;
        const payRate = parseFloat(payRateInput.value) || 1.0;

        // 総支払額（1日あたり*支払率）
        const totalPay = dailyPay * payRate;

        // 非課税15万円控除後2.7%適用
        let taxableAmount = totalPay - 150000;
        if (taxableAmount < 0) taxableAmount = 0;

        let incomeTax = 0;
        if (totalPay > 150000) {
            incomeTax = Math.floor(taxableAmount * 0.027 / 10) * 10; // 10円未満の切り捨て
        }

        let localIncomeTax = Math.floor(incomeTax * 0.1 / 10) * 10;
        let actualPay = Math.floor(totalPay - incomeTax - localIncomeTax);

        // 結果入力ウィンドウに反映
        incomeTaxInput.value = incomeTax.toLocaleString();
        localIncomeTaxInput.value = localIncomeTax.toLocaleString();
        actualPayInput.value = actualPay.toLocaleString();
    }

    dailyPayInput.addEventListener('input', calculateTaxes);
    payRateInput.addEventListener('input', calculateTaxes);

});
</script>
	<!-- 필수 항목이 입력되지 않았을 경우 에러 메시지 출력 -->
	<!-- 必須項目が入力されない場合、エラーメッセージを出力 -->

	<c:if test="${not empty errors }">
		<script>
		document.addEventListener('DOMContentLoaded', function(){
			<c:choose>
		    <c:when test="${errors.employeeIds}">
		        alert("社員を選択してください");
		    </c:when>
		    <c:when test="${errors.projectId}">
		        alert("現場・プロジェクトを選択してください");
		    </c:when>
		    <c:when test="${errors.workDate}">
		        alert("勤務日を入力してください");
		    </c:when>
		</c:choose>		
		});
	</script>
	</c:if>
	
	<%
	String projectError = (String) session.getAttribute("projectError");
	if (projectError != null) {
		session.removeAttribute("projectError");
		pageContext.setAttribute("projectError", projectError);
	}
	%>
	<c:if test="${not empty projectError}">
		<script>
        document.addEventListener('DOMContentLoaded', function () {
            alert("<c:out value='${projectError}' />");
        });
    </script>
	</c:if>
		<c:if test="${not empty successMessage}">
		<script>
			document.addEventListener('DOMContentLoaded', function() {
				alert("${successMessage}");
			});
		</script>
	</c:if>
	
	<%@ include file="/WEB-INF/view/common/footer.jspf"%>
</body>
</html>
