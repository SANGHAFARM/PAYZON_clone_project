<%-- 조회 결과와 입력 양식을 표시하는 JSP 화면이다. --%>
<%-- 照会結果と入力フォームを表示するJSP画面である。 --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html lang="ja-JP">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>勤怠管理>勤怠記録/管理</title>
<link rel="stylesheet"
    href="${pageContext.request.contextPath}/css/common/common.css">
<link rel="stylesheet"
    href="${pageContext.request.contextPath}/css/attendance/attendance-management.css">
<link rel="stylesheet"
    href="${pageContext.request.contextPath}/css/common/payzon-ui.css">
</head>
<body>
    <%@ include file="/WEB-INF/view/common/header.jspf"%>

    <main class="page-content attendance-page">
        <header class="page-heading">
            <div>
                <p>勤怠管理</p>
                <h1>勤怠記録/管理</h1>
            </div>
        </header>
        <c:if test="${not empty message}">
            <p class="form-message">
                <ui:message-label value="${message}" />
            </p>
        </c:if>

        <section class="attendance-card">
            <div class="employee-toolbar">
                <form
                    action="${pageContext.request.contextPath}/attendance/attendance-management.do"
                    method="get" class="employee-search">
                    <input type="hidden" name="status" value="${status }"> <input
                        type="search" name="keyword" value="${keyword}"
                        placeholder="検索キーワードを入力">
                    <button type="submit">検索</button>
                    <a
                        href="${pageContext.request.contextPath}/attendance/attendance-management.do">全体を見る</a>
                </form>
                <form
                    action="${pageContext.request.contextPath}/attendance/attendance-management.do"
                    method="get">
                    <input type="hidden" name="keyword" value="${keyword }"> <select
                        name="status" aria-label="社員の状態">
                        <option value="" ${status eq '' ? 'selected' : '' }>ステータス別</option>
                        <option value="재직" ${status eq '재직' ? 'selected' : ''}>在職</option>
                        <option value="퇴직" ${status eq '퇴직' ? 'selected' : ''}>退職</option>
                    </select>
                    <button type="submit" class="status-search">照会</button>
                </form>
            </div>

            <div class="attendance-layout">
                <section class="employee-list-panel">
                    <table class="employee-table">
                        <thead>
                            <tr>
                                <th>選択</th>
                                <th>区分</th>
                                <th>社員番号</th>
                                <th>氏名</th>
                                <th>部署</th>
                                <th>役職</th>
                                <th>勤怠記録</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="employee" items="${employees}">
                                <tr>
                                    <td><input type="checkbox" name="employeeIds"
                                        value="${employee.employeeId}"
                                        ${not empty editId ? 'disabled' : '' } form="attendance-form"></td>
                                    <td><ui:code-label value="${employee.empType}" /></td>
                                    <td><c:out value="${employee.empNo}" /></td>
                                    <td><c:out value="${employee.empNameKr}" /></td>
                                    <td><c:out value="${employee.departmentName}" /></td>
                                    <td><c:out value="${employee.jobPositionName}" /></td>
                                    <td>
                                        <!-- 管理ボタン：employeeIdとアンカーを一緒に渡す --> <a class="manage-button"
                                        href="${pageContext.request.contextPath}/attendance/attendance-management.do?employeeId=${employee.employeeId}#attendance-record-modal-${employee.employeeId}">管理</a>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty employees}">
                                <tr>
                                    <td colspan="7" class="empty-row">照会された社員はありません。</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </section>

                <!-- =========================================================== -->
                <!--                          勤怠記録入力フォーム                        -->
                <!-- =========================================================== -->
                <section class="attendance-editor">

                    <h2>勤怠記録を入力</h2>
                    <form id="attendance-form"
                        action="${pageContext.request.contextPath}/attendance/attendance-management.do"
                        method="post">

                        <c:if test="${not empty editId }">
                            <input type="hidden" name="editId" value="${editId }">
                        </c:if>

                        <label><span>入力日</span><input type="date" lang="ja-JP"
                            name="inputDate" value="${empty editId ? today : inputDate}"></label>
                        <label><span>勤怠項目</span> <select name="attendanceItemId">
                                <option value="">選択してください。</option>
                                <c:forEach var="item" items="${attendanceItems}">
                                    <option value="${item.attendanceItemId}"
                                        ${attendanceItemId eq item.attendanceItemId ? 'selected' : '' }><c:out
                                            value="${item.attendName}" /></option>
                                </c:forEach>
                        </select></label> <label class="period-field"><span>期間</span> <span
                            class="period-inputs"> <input type="date" lang="ja-JP" name="startDate" id="startDate"
                                value="${empty editId ? '' : startDate }"><i>~</i><input
                                type="date" lang="ja-JP" name="endDate" id="endDate"
                                value="${empty editId ? '' : endDate }"></span></label> <label><span>勤怠日数</span><span
                            class="days-field"> <input type="number"
                                name="attendValue" min="0" step="0.5"
                                value="${empty editId ? 0 : attendValue }"><em>日</em><a
                                href="#holiday-status-modal">休暇日数の状況</a></span></label> <label><span>金額（手当）</span>
                            <input type="number" name="payAmount" min="0"
                            value="${empty editId ? 0 : payAmount }"
                            placeholder="勤怠項目が支給手当の場合"></label> <label><span>摘要</span><input
                            type="text" name="note" value="${empty editId ? '' : note }"
                            placeholder="必要がある場合は入力してください。"></label>
                        <div class="editor-actions">

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
                                        href="${pageContext.request.contextPath}/attendance/attendance-management.do"
                                        class="button button-muted action-button">修正キャンセル</a>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </form>
                </section>
            </div>
        </section>
    </main>

    <!-- 社員別の勤怠記録 -->
    <!-- 社員別勤務記録：employeesリストでemployeeIdで見つけた社員のみを表示 -->
    <c:if test="${not empty employeeId and empty editId}">
        <c:forEach var="employee" items="${employees}">
            <c:if test="${employee.employeeId eq employeeId}">
                <div class="modal-overlay"
                    id="attendance-record-modal-${employee.employeeId}">
                    <section class="modal modal--record" role="dialog"
                        aria-modal="true" aria-labelledby="record-title">
                        <header>
                            <h2 id="record-title">社員別の勤怠記録</h2>
                            <a
                                href="${pageContext.request.contextPath}/attendance/attendance-management.do"
                                aria-label="閉じる">&times;</a>
                        </header>
                        <div class="modal-body">
                            <div class="record-summary">
                                <span>氏名： <c:out value="${employee.empNameKr}" /></span> <span>部署
                                    : <c:out value="${employee.departmentName}" />
                                </span> <span>役職： <c:out value="${employee.jobPositionName}" /></span>
                                <form method="get">
                                    <input type="hidden" name="employeeId"
                                        value="${employee.employeeId}"> <select name="year"
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
                            <table>
                                <thead>
                                    <tr>
                                        <th>番号</th>
                                        <th>入力日</th>
                                        <th>勤怠項目</th>
                                        <th>勤怠期間</th>
                                        <th>勤怠日数</th>
                                        <th>金額</th>
                                        <th>赤</th>
                                        <th>修正/削除</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="record" items="${attendanceRecords}"
                                        varStatus="status">
                                        <tr>
                                            <td><c:out value="${status.count}" /></td>
                                            <td><c:out value="${record.inputDate}" /></td>
                                            <td><c:out value="${record.attendName}" /></td>
                                            <td><c:out value="${record.startDate}" /> <c:if
                                                    test="${record.startDate ne record.endDate }">
                                                    <c:out value=" ~ ${record.endDate }" />
                                                </c:if></td>
                                            <td><c:out value="${record.attendValue}" /></td>
                                            <td><c:out value="${record.payAmount}" /></td>
                                            <td><c:out value="${record.note}" /></td>
                                            <td><a class="mini-button"
                                                href="?editId=${record.employeeAttendanceId}&employeeId=${employee.employeeId}&inputDate=${record.inputDate}
                                                &attendanceItemId=${record.attendanceItemId }&startDate=${record.startDate }&endDate=${record.endDate}
                                                &attendValue=${record.attendValue}&payAmount=${record.payAmount }&note=${record.note}">修正</a>

                                                <form
                                                    action="${pageContext.request.contextPath}/attendance/attendance-management.do"
                                                    method="post" style="display: inline;">
                                                    <input type="hidden" name="deleteId"
                                                        value="${record.employeeAttendanceId}"> <input
                                                        type="hidden" name="employeeId"
                                                        value="${employee.employeeId}"> <input
                                                        type="hidden" name="year" value="${year}"> <input
                                                        type="hidden" name="month" value="${month}">
                                                    <button type="submit" class="mini-button mini-delete">削除</button>
                                                </form></td>

                                        </tr>
                                    </c:forEach>
                                    <c:if test="${empty attendanceRecords}">
                                        <tr>
                                            <td colspan="8" class="empty-row">登録された勤怠記録はありません。</td>
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

    <div class="modal-overlay" id="holiday-status-modal">
        <section class="modal modal--holiday" role="dialog" aria-modal="true"
            aria-labelledby="holiday-title">
            <header>
                <h2 id="holiday-title">休暇日数の状況</h2>
                <a href="#" aria-label="閉じる">&times;</a>
            </header>
            <div class="modal-body">
                <table>
                    <thead>
                        <tr>
                            <th>区分</th>
                            <th>氏名</th>
                            <th>役職</th>
                            <th>休暇項目</th>
                            <th>全体</th>
                            <th>使用</th>
                            <th>残り</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="holiday" items="${holidayStatuses}">
                            <tr>
                                <td><ui:code-label value="${holiday.employmentType}" /></td>
                                <td><c:out value="${holiday.employeeName}" /></td>
                                <td><c:out value="${holiday.positionName}" /></td>
                                <td><c:out value="${holiday.holidayName}" /></td>
                                <td><c:out value="${holiday.totalDays}" /></td>
                                <td class="used-days"><c:out value="${holiday.usedDays}" /></td>
                                <td class="remaining-days"><c:out
                                        value="${holiday.remainingDays}" /></td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty holidayStatuses}">
                            <tr>
                                <td colspan="7" class="empty-row">照会された休暇日数の状況はありません。</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </section>
    </div>

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
</body>
</html>
