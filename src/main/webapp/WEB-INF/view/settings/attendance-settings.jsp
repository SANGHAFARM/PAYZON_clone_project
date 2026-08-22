<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html lang="ja-JP">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>基本設定>休暇/勤務設定</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/settings/attendance-settings.css?v=20260822-1">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common/payzon-ui.css?v=20260821-1">
</head>
<body>
	<%@ include file="/WEB-INF/view/common/header.jspf"%>
	<main class="page-content">
		<div class="attendance-page">
			<header class="page-heading">
				<div>
					<p>基本設定</p>
					<h1>休暇/勤労設定</h1>
				</div>
			</header>
			<section class="setting-card" id="leave-settings">
				<div class="card-title">
					<h2>休暇項目の設定</h2>
				</div>
				<div class="setting-layout">
					<div class="list-panel">
						<div class="table-wrap">
							<table class="leave-table">
								<thead>
									<tr>
										<th>休暇項目</th>
										<th>適用期間</th>
										<th>社員別休暇日数</th>
										<th>使用可</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="leave" items="${leaveItems}">
										<tr
											class="${leave.leaveItemId eq selectedLeaveItem.leaveItemId ? 'is-selected' : ''}">
											<td><a
												href="${pageContext.request.contextPath}/settings/attendance.do?leaveItemId=${leave.leaveItemId}#leave-settings"><c:out
														value="${leave.itemName}" /></a></td>
											<td><fmt:formatDate value="${leave.applyStartDate}"
													pattern="yyyy/MM/dd" /> ~ <fmt:formatDate
													value="${leave.applyEndDate}" pattern="yyyy/MM/dd" /></td>
											<td><a class="small-button"
												href="${pageContext.request.contextPath}/settings/attendance.do?leaveItemId=${leave.leaveItemId}#employee-leave-modal">管理</a></td>
											<td><span
												class="use-status use-status--${leave.useYn eq 'Y' ? 'on' : 'off'}">${leave.useYn eq 'Y' ? '使用' : '未使用'}</span></td>
										</tr>
									</c:forEach>
									<c:if test="${empty leaveItems}">
										<tr>
											<td colspan="4" class="empty-row">登録された休暇項目はありません。</td>
										</tr>
									</c:if>
								</tbody>
							</table>
						</div>
					</div>
					<form class="editor-panel"
						action="${pageContext.request.contextPath}/settings/leave-item.do"
						method="post">
						<input type="hidden" name="leaveItemId"
							value="<c:out value='${selectedLeaveItem.leaveItemId}' />">
						<h3>休暇項目情報</h3>
						<label class="editor-field"><span>休暇項目</span><input
							name="itemName"
							value="<c:out value='${selectedLeaveItem.itemName}' />"
							maxlength="100" placeholder="休暇項目を入力してください" required></label>
						<div class="editor-field editor-field--full">
							<span>適用期間</span>
							<div class="date-range">
								<input type="date" lang="ja-JP" name="applyStartDate"
									value="<fmt:formatDate value='${selectedLeaveItem.applyStartDate}' pattern='yyyy-MM-dd' />"
									required><i>~</i><input type="date" lang="ja-JP" name="applyEndDate"
									value="<fmt:formatDate value='${selectedLeaveItem.applyEndDate}' pattern='yyyy-MM-dd' />"
									required>
							</div>
						</div>
						<div class="editor-field editor-field--full">
							<span>使用可</span>
							<div class="radio-line">
								<label><input type="radio" name="useYn" value="Y"
									${empty selectedLeaveItem.useYn or selectedLeaveItem.useYn eq 'Y' ? 'checked' : ''}>
									使用</label><label><input type="radio" name="useYn" value="N"
									${selectedLeaveItem.useYn eq 'N' ? 'checked' : ''}>
									無効</label>
							</div>
						</div>
						<div class="editor-actions">
							<button name="action" value="insert">追加</button>
							<button name="action" value="update">修正</button>
							<button class="danger" name="action" value="requestDelete" formnovalidate>削除</button>
							<button class="clear" name="action" value="clear">内容を消去する</button>
						</div>
					</form>
				</div>
			</section>

			<section class="setting-card" id="attendance-item-settings">
				<div class="card-title">
					<h2>勤怠項目の設定</h2>
				</div>
				<div class="setting-layout">
					<div class="list-panel">
						<div class="table-wrap">
							<table class="attendance-table">
								<thead>
									<tr>
										<th>勤怠項目</th>
										<th>単位</th>
										<th>グループ管理</th>
										<th>休暇控除</th>
										<th>使用可</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="item" items="${attendItems}">
										<tr
											class="${item.attendanceItemId eq selectedAttendItem.attendanceItemId ? 'is-selected' : ''}">
											<td><a
												href="${pageContext.request.contextPath}/settings/attendance.do?attendItemId=${item.attendanceItemId}#attendance-item-settings"><c:out
														value="${item.attendName}" /></a></td>
											<td><ui:code-label value="${item.unitType}" /></td>
											<td><c:out value="${item.groupName}" /></td>
											<td><c:out value="${item.leaveName}" /></td>
											<td><span
												class="use-status use-status--${item.useYn eq 'Y' ? 'on' : 'off'}">${item.useYn eq 'Y' ? '使用' : '未使用'}</span></td>
										</tr>
									</c:forEach>
									<c:if test="${empty attendItems}">
										<tr>
											<td colspan="5" class="empty-row">登録された勤怠項目はありません。</td>
										</tr>
									</c:if>
								</tbody>
							</table>
						</div>
					</div>
					<form class="editor-panel"
						action="${pageContext.request.contextPath}/settings/attend-item.do"
						method="post">
						<input type="hidden" name="attendItemId"
							value="<c:out value='${selectedAttendItem.attendanceItemId}' />">
						<h3>勤怠項目情報</h3>
						<label class="editor-field"><span>勤怠項目</span><input
							name="attendName"
							value="<c:out value='${selectedAttendItem.attendName}' />"
							maxlength="100" placeholder="勤怠項目を入力してください" required></label> <label
							class="editor-field"><span>単位</span><select
							name="unitType"><option value="">選択してください。</option>
								<option value="일"
									${selectedAttendItem.unitType eq '일' ? 'selected' : ''}>日</option>
								<option value="시간"
									${selectedAttendItem.unitType eq '시간' ? 'selected' : ''}>時間</option></select></label>
						<div class="editor-field editor-field--group">
							<span>勤怠グループ</span><select name="attendanceGroupId" required><option
									value="">選択してください。</option>
								<c:forEach var="group" items="${attendGroups}">
									<option value="${group.attendanceGroupId}"
										${group.attendanceGroupId eq selectedAttendItem.attendanceGroupId ? 'selected' : ''}><c:out
											value="${group.groupName}" /></option>
								</c:forEach></select><a href="#attend-group-modal">グループ管理</a>
						</div>
						<label class="editor-field"><span>休暇控除</span><select
							name="deductLeaveId"><option value="">選択してください。</option>
								<c:forEach var="leave" items="${leaveItems}">
									<option value="${leave.leaveItemId}"
										${leave.leaveItemId eq selectedAttendItem.deductLeaveId ? 'selected' : ''}><c:out
											value="${leave.itemName}" /></option>
								</c:forEach></select></label> <label class="editor-field"><span>労働時間連携</span><select
							name="workHourType"><option value="">選択してください。</option>
								<option value="소정근로"
									${selectedAttendItem.workHourType eq '소정근로' ? 'selected' : ''}>所定の労働</option>
								<option value="연장근로"
									${selectedAttendItem.workHourType eq '연장근로' ? 'selected' : ''}>延長労働</option>
								<option value="야간근로"
									${selectedAttendItem.workHourType eq '야간근로' ? 'selected' : ''}>夜間労働</option>
								<option value="휴일근로"
									${selectedAttendItem.workHourType eq '휴일근로' ? 'selected' : ''}>休日労働</option></select></label>
						<div class="editor-field editor-field--full">
							<span>使用可</span>
							<div class="radio-line">
								<label><input type="radio" name="useYn" value="Y"
									${empty selectedAttendItem.useYn or selectedAttendItem.useYn eq 'Y' ? 'checked' : ''}>
									使用</label><label><input type="radio" name="useYn" value="N"
									${selectedAttendItem.useYn eq 'N' ? 'checked' : ''}>
									無効</label>
							</div>
						</div>
						<div class="editor-actions">
							<button name="action" value="insert">追加</button>
							<button name="action" value="update">修正</button>
							<button class="danger" name="action" value="requestDelete" formnovalidate>削除</button>
							<button class="clear" name="action" value="clear">内容を消去する</button>
						</div>
					</form>
				</div>
			</section>

			<div id="attend-group-modal" class="group-modal" role="dialog"
				aria-modal="true" aria-labelledby="group-modal-title">
				<a class="group-modal__backdrop" href="#attendance-item-settings"
					aria-label="閉じる"></a>
				<div class="group-modal__panel">
					<div class="group-modal__title">
						<h2 id="group-modal-title">勤労グループ管理</h2>
						<a href="#attendance-item-settings" aria-label="閉じる">×</a>
					</div>
					<form
						action="${pageContext.request.contextPath}/settings/attend-group.do"
						method="post">
						<ul class="group-list">
							<c:forEach var="group" items="${attendGroups}">
								<li><input
									name="groupNames" value="<c:out value='${group.groupName}' />"
									aria-label="勤労グループ名" maxlength="100" required><input type="hidden" name="groupIds"
									value="${group.attendanceGroupId}">
									<div>
										<button name="action"
											value="update:${group.attendanceGroupId}">修正</button>
										<button name="action" formnovalidate
											value="requestDelete:${group.attendanceGroupId}">削除</button>
									</div></li>
							</c:forEach>
							<c:if test="${empty attendGroups}">
								<li class="group-list__empty">登録された勤労グループはありません。</li>
							</c:if>
						</ul>
						<div class="group-add">
							<input name="newGroupName" placeholder="新しい勤労グループ名" maxlength="100" required>
							<button name="action" value="insert">＋追加</button>
						</div>
					</form>
				</div>
			</div>

			<div id="employee-leave-modal" class="employee-leave-modal"
				role="dialog" aria-modal="true"
				aria-labelledby="employee-leave-title">
				<a class="employee-leave-modal__backdrop" href="#leave-settings"
					aria-label="閉じる"></a>
				<div class="employee-leave-modal__panel">
					<div class="employee-leave-modal__title">
						<h2 id="employee-leave-title">休暇日数を設定</h2>
						<a href="#leave-settings" aria-label="閉じる">×</a>
					</div>
					<form
						action="${pageContext.request.contextPath}/settings/employee-leave.do"
						method="post">
						<input type="hidden" name="leaveItemId"
							value="${selectedLeaveItem.leaveItemId}">
						<div class="employee-leave-tools">
							<div>
								<input name="keyword" placeholder="社員検索"
									value="<c:out value='${param.keyword}' />">
								<button name="action" value="search">検索</button>
								<button name="action" value="showAll">全体を見る</button>
							</div>

							<select name="status">
								<option value="">ステータス別</option>
								<option value="재직" ${param.status eq '재직' ? 'selected' : ''}>在職</option>
								<option value="퇴직" ${param.status eq '퇴직' ? 'selected' : ''}>退職</option>
							</select>
						</div>
						<div class="employee-leave-table-wrap">
							<table class="employee-leave-table">
								<thead>
									<tr>
										<th>選択</th>
										<th>区分</th>
										<th>社員番号</th>
										<th>氏名</th>
										<th>部署</th>
										<th>役職</th>
										<th>入社日</th>
										<th>休暇日数</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="row" items="${employeeLeaveRows}">
										<tr>
											<td><input type="checkbox" name="checkedEmpIds"
												value="${row.employeeId}"></td>

											<td><ui:code-label value="${row.empType}" /></td>
											<td><c:out value="${row.empNo}" /></td>
											<td><c:out value="${row.empName}" /></td>
											<td><c:out value="${row.deptName}" /></td>
											<td><c:out value="${row.posName}" /></td>
											<td><c:out value="${row.joinDate}" /></td>

											<td><label> <input type="hidden"
													name="empLeaveId_${row.employeeId}"
													value="${row.empLeaveId}"> <input type="number"
													min="0" step="0.5" name="leaveDays_${row.employeeId}"
													value="${row.leaveDays}"> <span>日</span>
											</label></td>
										</tr>
									</c:forEach>
									<c:if test="${empty employeeLeaveRows}">
										<tr>
											<td colspan="8" class="empty-row">照会された社員はありません。</td>
										</tr>
									</c:if>
								</tbody>
							</table>
						</div>
						<div class="employee-leave-actions">
							<div>
								<button class="delete" name="action" value="requestDelete" formnovalidate>休暇日数
									削除</button>
								<button name="action" value="save">休暇日数を保存</button>
							</div>
							<div>
								<a href="#annual-leave-info-modal">年次休暇の計算方法</a>
							</div>
						</div>
					</form>
				</div>
			</div>

			<c:if test="${not empty deleteSettingType}">
				<div class="attendance-delete-modal" role="alertdialog" aria-modal="true"
					aria-labelledby="attendance-delete-title">
					<a class="attendance-modal__backdrop"
						href="${pageContext.request.contextPath}/settings/attendance.do${deleteReturnHash}"
						aria-label="削除のキャンセル"></a>
					<form class="attendance-delete-modal__panel" method="post"
						action="${pageContext.request.contextPath}${deleteActionUrl}">
						<p id="attendance-delete-title"><strong><c:out value="${deleteSettingName}" /></strong> 項目を削除してもよろしいですか？</p>
						<p class="attendance-delete-warning">削除した項目は復元できません。</p>
						<input type="hidden" name="action" value="confirmDelete">
						<input type="hidden" name="deleteId" value="${deleteSettingId}">
						<c:if test="${deleteSettingType eq 'leave'}"><input type="hidden" name="leaveItemId" value="${deleteSettingId}"></c:if>
						<c:if test="${deleteSettingType eq 'attendance'}"><input type="hidden" name="attendItemId" value="${deleteSettingId}"></c:if>
						<div><button type="submit">削除</button><a href="${pageContext.request.contextPath}/settings/attendance.do${deleteReturnHash}">キャンセル</a></div>
					</form>
				</div>
			</c:if>

			<c:if test="${not empty employeeLeaveDeleteCount}">
				<div class="attendance-delete-modal" role="alertdialog" aria-modal="true"
					aria-labelledby="employee-leave-delete-title">
					<a class="attendance-modal__backdrop" href="${pageContext.request.contextPath}/settings/attendance.do?cancelEmployeeLeaveDelete=true&amp;leaveItemId=${selectedLeaveItem.leaveItemId}#employee-leave-modal" aria-label="削除のキャンセル"></a>
					<form class="attendance-delete-modal__panel" method="post"
						action="${pageContext.request.contextPath}/settings/employee-leave.do">
						<p id="employee-leave-delete-title">選択 <strong><c:out value="${employeeLeaveDeleteCount}" />人</strong>の休日を削除してもよろしいですか？</p>
						<p class="attendance-delete-warning">削除した休暇日数は復元できません。</p>
						<input type="hidden" name="action" value="confirmDelete">
						<input type="hidden" name="leaveItemId" value="${selectedLeaveItem.leaveItemId}">
						<div><button type="submit">削除</button><a href="${pageContext.request.contextPath}/settings/attendance.do?cancelEmployeeLeaveDelete=true&amp;leaveItemId=${selectedLeaveItem.leaveItemId}#employee-leave-modal">キャンセル</a></div>
					</form>
				</div>
			</c:if>

			<c:if test="${not empty message}">
				<c:set var="messageHash" value="${messageReturnTarget eq 'group' ? '#attend-group-modal' : messageReturnTarget eq 'employeeLeave' ? '#employee-leave-modal' : ''}" />
				<div class="attendance-alert" role="alertdialog" aria-modal="true"
					aria-labelledby="attendance-alert-message">
					<a class="attendance-modal__backdrop"
						href="${pageContext.request.contextPath}/settings/attendance.do?dismissMessage=true${messageHash}"
						aria-label="確認"></a>
					<div class="attendance-alert__panel">
						<p id="attendance-alert-message"><ui:message-label value="${message}" /></p>
						<a href="${pageContext.request.contextPath}/settings/attendance.do?dismissMessage=true${messageHash}">確認</a>
					</div>
				</div>
				<c:remove var="message" scope="session" />
				<c:remove var="messageReturnTarget" scope="session" />
			</c:if>
		</div>
		<!-- ▼▼年次休暇計算方法モーダル（純粋CSS方式）▼▼ -->
		<div id="annual-leave-info-modal" class="info-modal" role="dialog"
			aria-modal="true" aria-labelledby="info-modal-title">
			<!-- 💡 閉じるを押すと前のウィンドウ（社員休暇日数設定ウィンドウ）にすっきり戻ります！ -->
			<a class="info-modal__backdrop" href="#employee-leave-modal"
				aria-label="閉じる"></a>
			<div class="info-modal__panel">
				<div class="info-modal__title">
					<h2 id="info-modal-title">年次休暇計算法</h2>
					<a href="#employee-leave-modal" aria-label="閉じる">×</a>
				</div>
				<div class="info-modal__body">

					<!-- 週40時間制 -->
					<div class="leave-calc-section">
						<h3>週40時間制適用事業場</h3>
						<p class="calc-desc">年次休暇は1年に15日に基づいており、3年以上労働を続けて初めて
							1年を超える継続勤労研修 2年に対して1日の加算年次休暇発生（最大25日）</p>
						<ul class="calc-list">
							<li><strong>1)入社1年未満の場合</strong>
								<p>年次休暇 = 基準日(15) * (勤務日数/365) = 小数 1 桁まで (小数 2 桁
									丸め)</p>
								<div class="calc-example">
									例）2023年2月14日〜2023年12月31日全日勤務時：勤務日数は322日<br> 使用期間
									：2024年1月1日〜2024年12月31日<br> 2024年1月1日年次計算時：
									13.2日 (15 * (322/365))
								</div></li>
							<li><strong>2) 入社1年以上の場合</strong>
								<p>
									 - 勤務年度 > 2 (3年以上の場合)<br> 年次休暇 = 基準日(15) + (労働年/2) (小数点
									捨て計算)
								</p>
								<div class="calc-example">例）2020年2月14日入社した社員の2024年1月
									1日年次計算時：16日（15+（2/2））</div>
								<p class="mt-2">
									 - 勤務年度< 3（3年未満の場合）<br> 年次休暇=基準日(15)
								</p></li>
						</ul>
					</div>

					<!-- 週44時間制 -->
					<div class="leave-calc-section">
						<h3>週44時間制適用事業場</h3>
						<p class="calc-desc">年次休暇は1年に10日に基づいており、2年以上労働を続けて初めて
							1年を超える継続勤労研修2年に対して1日の加算年次休暇発生</p>
						<ul class="calc-list">
							<li><strong>1)入社1年未満の場合</strong>
								<p>年次休暇 = 基準日(10) * (勤務日数/365) = 小数 1 桁まで (小数 2 桁
									丸め)</p>
								<div class="calc-example">
									例）2023年2月14日〜2023年12月31日全日勤務時：勤務日数は322日<br> 使用期間
									：2024年1月1日〜2024年12月31日<br> 2024年1月1日年次計算時：
									8.8日 (10 * (322/365))
								</div></li>
							<li><strong>2) 入社1年以上の場合</strong>
								<p>
									 - 勤務年度 > 2 (3年以上の場合)<br> 年次休暇 = 基準日(10) + (勤務年度)
								</p>
								<div class="calc-example">例）2020年2月14日入社した社員の2024年1月
									1日年次計算時：12日（10+2）</div>
								<p class="mt-2">
									 - 勤務年度< 3（3年未満の場合）<br> 年次休暇=基準日（10）
								</p></li>
						</ul>
					</div>

				</div>
			</div>
		</div>
		<!-- ▲▲年次休暇の計算方法モーダル終了▲▲ -->
	</main>
	<%@ include file="/WEB-INF/view/common/footer.jspf"%>
</body>
</html>
