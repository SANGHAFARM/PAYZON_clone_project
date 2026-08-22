<%-- 조회 결과와 입력 양식을 표시하는 JSP 화면이다. --%>
<%-- 照会結果と入力フォームを表示するJSP画面である。 --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>근태관리 &gt; 근태기록/관리</title>
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
                <p>근태관리</p>
                <h1>근태기록/관리</h1>
            </div>
        </header>
        <c:if test="${not empty message}">
            <p class="form-message">
                <c:out value="${message}" />
            </p>
        </c:if>

        <section class="attendance-card">
            <div class="employee-toolbar">
                <form
                    action="${pageContext.request.contextPath}/attendance/attendance-management.do"
                    method="get" class="employee-search">
                    <input type="hidden" name="status" value="${status }"> <input
                        type="search" name="keyword" value="${keyword}"
                        placeholder="검색어 입력">
                    <button type="submit">검색</button>
                    <a
                        href="${pageContext.request.contextPath}/attendance/attendance-management.do">전체보기</a>
                </form>
                <form
                    action="${pageContext.request.contextPath}/attendance/attendance-management.do"
                    method="get">
                    <input type="hidden" name="keyword" value="${keyword }"> <select
                        name="status" aria-label="사원 상태">
                        <option value="" ${status eq '' ? 'selected' : '' }>상태별</option>
                        <option value="재직" ${status eq '재직' ? 'selected' : ''}>재직</option>
                        <option value="퇴직" ${status eq '퇴직' ? 'selected' : ''}>퇴직</option>
                    </select>
                    <button type="submit" class="status-search">조회</button>
                </form>
            </div>

            <div class="attendance-layout">
                <section class="employee-list-panel">
                    <table class="employee-table">
                        <thead>
                            <tr>
                                <th>선택</th>
                                <th>구분</th>
                                <th>사원번호</th>
                                <th>성명</th>
                                <th>부서</th>
                                <th>직위</th>
                                <th>근태기록</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="employee" items="${employees}">
                                <tr>
                                    <td><input type="checkbox" name="employeeIds"
                                        value="${employee.employeeId}"
                                        ${not empty editId ? 'disabled' : '' } form="attendance-form"></td>
                                    <td><c:out value="${employee.empType}" /></td>
                                    <td><c:out value="${employee.empNo}" /></td>
                                    <td><c:out value="${employee.empNameKr}" /></td>
                                    <td><c:out value="${employee.departmentName}" /></td>
                                    <td><c:out value="${employee.jobPositionName}" /></td>
                                    <td>
                                        <!-- 관리 버튼: employeeId와 앵커를 함께 전달 --> <a class="manage-button"
                                        href="${pageContext.request.contextPath}/attendance/attendance-management.do?employeeId=${employee.employeeId}#attendance-record-modal-${employee.employeeId}">관리</a>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty employees}">
                                <tr>
                                    <td colspan="7" class="empty-row">조회된 사원이 없습니다.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </section>

                <!-- =========================================================== -->
                <!--                          근태 기록 입력 폼                        -->
                <!-- =========================================================== -->
                <section class="attendance-editor">

                    <h2>근태기록 입력</h2>
                    <form id="attendance-form"
                        action="${pageContext.request.contextPath}/attendance/attendance-management.do"
                        method="post">

                        <c:if test="${not empty editId }">
                            <input type="hidden" name="editId" value="${editId }">
                        </c:if>

                        <label><span>입력일자</span><input type="date"
                            name="inputDate" value="${empty editId ? today : inputDate}"></label>
                        <label><span>근태항목</span> <select name="attendanceItemId">
                                <option value="">선택하세요.</option>
                                <c:forEach var="item" items="${attendanceItems}">
                                    <option value="${item.attendanceItemId}"
                                        ${attendanceItemId eq item.attendanceItemId ? 'selected' : '' }><c:out
                                            value="${item.attendName}" /></option>
                                </c:forEach>
                        </select></label> <label class="period-field"><span>기간</span> <span
                            class="period-inputs"> <input type="date" name="startDate"
                                value="${empty editId ? '' : startDate }"><i>~</i><input
                                type="date" name="endDate"
                                value="${empty editId ? '' : endDate }"></span></label> <label><span>근태일수</span><span
                            class="days-field"> <input type="number"
                                name="attendValue" min="0" step="0.5"
                                value="${empty editId ? 0 : attendValue }"><em>일</em><a
                                href="#holiday-status-modal">휴가일수 현황</a></span></label> <label><span>금액(수당)</span>
                            <input type="number" name="payAmount" min="0"
                            value="${empty editId ? 0 : payAmount }"
                            placeholder="근태분류가 지급수당인 경우 입력"></label> <label><span>적요</span><input
                            type="text" name="note" value="${empty editId ? '' : note }"
                            placeholder="적요가 있는 경우 입력해주세요."></label>
                        <div class="editor-actions">

                            <c:choose>
                                <c:when test="${empty editId}">
                                    <button type="submit" id="saveBtn"
                                        class="button button-primary action-button">저장</button>
                                </c:when>
                                <c:otherwise>
                                    <button type="submit"
                                        class="button button-primary action-button">수정</button>
                                </c:otherwise>
                            </c:choose>
                            <!-- 수정 모드일 시 수정취소 버튼 활성화 -->
                            <c:choose>
                                <c:when test="${empty editId}">
                                    <button type="reset"
                                        class="button button-muted action-button clear-button">내용지우기</button>
                                </c:when>
                                <c:otherwise>
                                    <a
                                        href="${pageContext.request.contextPath}/attendance/attendance-management.do"
                                        class="button button-muted action-button">수정취소</a>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </form>
                </section>
            </div>
        </section>
    </main>

    <!-- 사원별 근태기록 -->
    <!-- 사원별 근태기록: employees 목록에서 employeeId로 찾은 사원만 표시 -->
    <c:if test="${not empty employeeId and empty editId}">
        <c:forEach var="employee" items="${employees}">
            <c:if test="${employee.employeeId eq employeeId}">
                <div class="modal-overlay"
                    id="attendance-record-modal-${employee.employeeId}">
                    <section class="modal modal--record" role="dialog"
                        aria-modal="true" aria-labelledby="record-title">
                        <header>
                            <h2 id="record-title">사원별 근태기록</h2>
                            <a
                                href="${pageContext.request.contextPath}/attendance/attendance-management.do"
                                aria-label="닫기">&times;</a>
                        </header>
                        <div class="modal-body">
                            <div class="record-summary">
                                <span>성명 : <c:out value="${employee.empNameKr}" /></span> <span>부서
                                    : <c:out value="${employee.departmentName}" />
                                </span> <span>직위 : <c:out value="${employee.jobPositionName}" /></span>
                                <form method="get">
                                    <input type="hidden" name="employeeId"
                                        value="${employee.employeeId}"> <select name="year"
                                        aria-label="연도">
                                        <c:forEach var="y" begin="2015" end="2026">
                                            <option value="${y}" ${y eq year ? 'selected' : ''}>${y}년</option>
                                        </c:forEach>
                                    </select> <select name="month" aria-label="월">
                                        <c:forEach var="monthNo" begin="1" end="12">
                                            <option value="${monthNo}"
                                                ${monthNo eq month ? 'selected' : ''}>${monthNo}월</option>
                                        </c:forEach>
                                    </select>
                                    <button type="submit">조회</button>
                                </form>
                            </div>
                            <table>
                                <thead>
                                    <tr>
                                        <th>번호</th>
                                        <th>입력일자</th>
                                        <th>근태항목</th>
                                        <th>근태기간</th>
                                        <th>근태일수</th>
                                        <th>금액</th>
                                        <th>적요</th>
                                        <th>수정/삭제</th>
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
                                                &attendValue=${record.attendValue}&payAmount=${record.payAmount }&note=${record.note}">수정</a>

                                                <form
                                                    action="${pageContext.request.contextPath}/attendance/attendance-management.do"
                                                    method="post" style="display: inline;">
                                                    <input type="hidden" name="deleteId"
                                                        value="${record.employeeAttendanceId}"> <input
                                                        type="hidden" name="employeeId"
                                                        value="${employee.employeeId}"> <input
                                                        type="hidden" name="year" value="${year}"> <input
                                                        type="hidden" name="month" value="${month}">
                                                    <button type="submit" class="mini-button mini-delete">삭제</button>
                                                </form></td>

                                        </tr>
                                    </c:forEach>
                                    <c:if test="${empty attendanceRecords}">
                                        <tr>
                                            <td colspan="8" class="empty-row">등록된 근태기록이 없습니다.</td>
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
                <h2 id="holiday-title">휴가일수 현황</h2>
                <a href="#" aria-label="닫기">&times;</a>
            </header>
            <div class="modal-body">
                <table>
                    <thead>
                        <tr>
                            <th>구분</th>
                            <th>성명</th>
                            <th>직위</th>
                            <th>휴가항목</th>
                            <th>전체</th>
                            <th>사용</th>
                            <th>잔여</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="holiday" items="${holidayStatuses}">
                            <tr>
                                <td><c:out value="${holiday.employmentType}" /></td>
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
                                <td colspan="7" class="empty-row">조회된 휴가일수 현황이 없습니다.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </section>
    </div>

    <%@ include file="/WEB-INF/view/common/footer.jspf"%>
</body>
</html>
