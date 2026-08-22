<%-- 조회 결과와 입력 양식을 표시하는 JSP 화면이다. --%>
<%-- 照会結果と入力フォームを表示するJSP画面である。 --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>
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
                    href="${pageContext.request.contextPath}/attendance/attendance-inquiry.do?view=MONTH">月別
                    照会</a> <a class="${view eq 'DETAIL' ? 'is-active' : ''}"
                    href="${pageContext.request.contextPath}/attendance/attendance-inquiry.do?view=DETAIL">詳細
                    照会</a>
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
                                                    <c:forEach var="entry" items="${employee.totalAttendValue}">
                                                        <li><c:out value="${entry.key}: ${entry.value}" /> <c:choose>
                                                                <c:when
                                                                    test="${entry.key eq '연차' or entry.key eq '반차' or entry.key eq '포상휴가' or entry.key eq '청원휴가'}">
                (d)
            </c:when>
                                                                <c:otherwise>
                (h)
            </c:otherwise>
                                                            </c:choose></li>
                                                    </c:forEach>
                                                    <%-- 																										<c:if
                                                        test="${empty employee.attendanceSummaryItems and not empty employee.attendanceSummary}">
                                                        <li><c:out value="${employee.attendanceSummary}" /></li>
                                                    </c:if> --%>
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
                                <input type="date" lang="ja-JP" name="inputDate" value="${param.inputDate}">
                            </label> <label> <input type="checkbox" name="usePeriod"
                                value="Y" ${param.usePeriod eq 'Y' ? 'checked' : ''}> <span>勤怠期間</span>
                                <span class="detail-period"> <input type="date" lang="ja-JP"
                                    name="startDate" value="${startDateStr}"> <i>~</i> <input
                                    type="date" lang="ja-JP" name="endDate" value="${endDateStr}">
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
                                    <c:forEach var="record" items="${attendanceRecords}">
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
                                                    <c:when
                                                        test="${record.attendName eq '연차' or record.attendName eq '반차' or record.attendName eq '포상휴가' or record.attendName eq '청원휴가'}">
                (d)
            </c:when>
                                                    <c:otherwise>
                (h)
            </c:otherwise>
                                                </c:choose> <%-- <c:out
                                                    value="${record.unit}" /></td> --%>
                                            <td><c:out value="${record.payAmount}" /></td>
                                            <td><c:out value="${record.note}" /></td>
                                        </tr>
                                    </c:forEach>
                                    <c:if test="${empty attendanceRecords}">
                                        <tr>
                                            <td colspan="10" class="empty-row">照会された詳細な勤労履歴はありません。</td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </section>
    </main>
    <%@ include file="/WEB-INF/view/common/footer.jspf"%>
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
