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
<title>勤怠管理>休暇照会</title>
<link rel="stylesheet"
    href="${pageContext.request.contextPath}/css/attendance/holiday-inquiry.css">
<link rel="stylesheet"
    href="${pageContext.request.contextPath}/css/common/payzon-ui.css">
</head>
<body>
    <%@ include file="/WEB-INF/view/common/header.jspf"%>
    <main class="page-content holiday-page">
        <header class="page-heading">
            <div>
                <p>勤怠管理</p>
                <h1>休暇照会</h1>
            </div>
        </header>
        <!-- 照会フォーム -->
        <section class="holiday-card">
            <form class="holiday-search"
                action="${pageContext.request.contextPath}/attendance/leave-inquiry.do"
                method="get">
                <select name="leaveItemId" aria-label="休暇項目">
                    <option value="">休暇項目を選択</option>
                    <c:forEach var="item" items="${leaveItems}">
                        <option value="${item.leaveItemId}"
                            ${item.leaveItemId eq leaveItemId ? 'selected' : ''}>
                            <ui:code-label value="${item.itemName}" />
                        </option>
                    </c:forEach>
                </select> <input type="search" name="keyword" value="${keyword}"
                    placeholder="検索語を入力">
                <button type="submit">検索</button>
                <a
                    href="${pageContext.request.contextPath}/attendance/leave-inquiry.do">全体を見る</a>

                <div class="filter-group">
                    <select name="status" aria-label="ステータス別">
                        <option value="">ステータス別</option>
                        <option value="재직" ${status eq '재직' ? 'selected' : ''}>在職</option>
                        <option value="퇴직" ${status eq '퇴직' ? 'selected' : ''}>退職</option>
                    </select> <select name="empType" aria-label="区分別">
                        <option value="">区分別</option>
                        <c:forEach var="type" items="${empTypes}">
                            <option value="${type}" ${type eq empType ? 'selected' : ''}>
                                <ui:code-label value="${type}" />
                            </option>
                        </c:forEach>
                    </select> <select name="departmentId" aria-label="部署別">
                        <option value="">部署別</option>
                        <c:forEach var="department" items="${departments}">
                            <option value="${department.departmentId}"
                                ${department.departmentId eq departmentId ? 'selected' : ''}>
                                <c:out value="${department.departmentName}" />
                            </option>
                        </c:forEach>
                    </select> <select name="jobPositionId" aria-label="役職別">
                        <option value="">役職別</option>
                        <c:forEach var="jobPosition" items="${jobPositions}">
                            <option value="${jobPosition.jobPositionId}"
                                ${jobPosition.jobPositionId eq jobPositionId ? 'selected' : ''}>
                                <c:out value="${jobPosition.jobPositionName}" />
                            </option>
                        </c:forEach>
                    </select> <select name="pageSize" aria-label="リスト数">
                        <option value="10" ${pageSize eq 10 ? 'selected' : ''}>10個ずつ
                            見る</option>
                        <option value="30"
                            ${empty pageSize or pageSize eq 30 ? 'selected' : ''}>30個ずつ
                            見る</option>
                        <option value="50" ${pageSize eq 50 ? 'selected' : ''}>50個ずつ
                            見る</option>
                        <option value="100" ${pageSize eq 100 ? 'selected' : ''}>100個ずつ
                            見る</option>
                    </select>

                    <button type="submit" class="filter-button">照会</button>
                </div>
            </form>

            <div class="holiday-table-wrap">
                <table class="holiday-table">
                    <thead>
                        <tr>
                            <th>区分</th>
                            <th>社員番号</th>
                            <th>氏名</th>
                            <th>部署</th>
                            <th>役職</th>
                            <th>休暇項目</th>
                            <th>全体</th>
                            <th>使用</th>
                            <th>残り</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="employee" items="${leaveEmployees}">
                            <c:set var="detailUrl"
                                value="${pageContext.request.contextPath}/attendance/leave-inquiry.do?employeeId=${employee.employeeId}&amp;leaveItemId=${leaveItemId}#holiday-modal-${employee.employeeId}" />
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
                                <td><a href="${detailUrl}"><c:out
                                            value="${employee.itemName}" /></a></td>
                                <td class="total-days"><a href="${detailUrl}"><c:out
                                            value="${employee.totalDays}" /></a></td>
                                <td class="used-days"><a href="${detailUrl}"><c:out
                                            value="${employee.usedDays}" /></a></td>
                                <td class="remaining-days"><a href="${detailUrl}"><c:out
                                            value="${employee.remainingDays}" /></a></td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty leaveEmployees}">
                            <tr>
                                <td colspan="9" class="empty-row">照会された社員別の休暇履歴はありません。</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>

            <nav class="pagination" aria-label="ページ移動">
                <c:if test="${pageInfo.hasPrevious}">
                    <a href="?page=${pageInfo.previousPage}">以前</a>
                </c:if>
                <c:forEach var="pageNo" begin="${pageInfo.startPage}"
                    end="${pageInfo.endPage}">
                    <a class="${pageNo eq pageInfo.currentPage ? 'is-current' : ''}"
                        href="?page=${pageNo}"><c:out value="${pageNo}" /></a>
                </c:forEach>
                <c:if test="${pageInfo.hasNext}">
                    <a href="?page=${pageInfo.nextPage}">次へ</a>
                </c:if>
            </nav>
        </section>
    </main>

    <c:if test="${not empty employeeId}">
        <div class="holiday-modal-overlay" id="holiday-modal-${employeeId}">
            <section class="holiday-modal" role="dialog" aria-modal="true"
                aria-labelledby="holiday-title-${employeeId}">
                <header>
                    <h2>社員別の休暇状況</h2>
                    <a
                        href="${pageContext.request.contextPath}/attendance/leave-inquiry.do"
                        aria-label="閉じる">&times;</a>
                </header>
                <div class="holiday-modal-body">
                    <h3 id="holiday-title-${employeeId}">
                        [
                        <c:out value="${selectedEmployee.departmentName}" />
                        ]
                        <c:out value="${selectedEmployee.empNameKr}" />
                        <c:out value="${selectedEmployee.jobPositionName}" />
                        休暇状況
                    </h3>
                    <table>
                        <thead>
                            <tr>
                                <th>番号</th>
                                <th>入力日</th>
                                <th>休暇項目</th>
                                <th>勤怠項目</th>
                                <th>期間</th>
                                <th>日数</th>
                                <th>備考</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="record" items="${leaveRecords}"
                                varStatus="status">
                                <tr>
                                    <td><c:out value="${status.count}" /></td>
                                    <td><c:out value="${record.inputDate}" /></td>
                                    <td><ui:code-label value="${record.itemName}" /></td>
                                    <td><c:out value="${record.attendName}" /></td>
                                    <td><c:out value="${record.startDate}" /> <c:if
                                            test="${record.startDate ne record.endDate}">
                                            <i>~</i>
                                            <c:out value="${record.endDate}" />
                                        </c:if></td>
                                    <td class="record-days"><c:out
                                            value="${record.attendValue}" /></td>
                                    <td><c:out value="${record.note}" /></td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty leaveRecords}">
                                <tr>
                                    <td colspan="7" class="empty-row">登録された休暇の使用履歴はありません。</td>
                                </tr>
                            </c:if>
                        </tbody>
                        <tfoot>
                            <tr>
                                <th colspan="2">合計</th>
                                <td colspan="5"><span>総休暇日数： <strong><c:out
                                                value="${selectedEmployee.totalDays}" /></strong></span> <span>使用日数
                                        : <strong class="used-days"><c:out
                                                value="${selectedEmployee.usedDays}" /></strong>
                                </span> <span>残り日数： <strong class="remaining-days"><c:out
                                                value="${selectedEmployee.remainingDays}" /></strong></span></td>
                            </tr>
                        </tfoot>
                    </table>
                </div>
            </section>
        </div>
    </c:if>
    <%@ include file="/WEB-INF/view/common/footer.jspf"%>
</body>
</html>
