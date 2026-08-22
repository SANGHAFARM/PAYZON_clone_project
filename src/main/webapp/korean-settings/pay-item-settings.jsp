<%-- 조회 결과와 입력 양식을 표시하는 JSP 화면이다. --%>
<%-- 照会結果と入力フォームを表示するJSP画面である。 --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>기본환경설정 &gt; 급여항목 설정</title>
<link rel="stylesheet"
    href="${pageContext.request.contextPath}/css/settings/pay-item-settings.css?v=20260820-7">
<link rel="stylesheet"
    href="${pageContext.request.contextPath}/css/common/payzon-ui.css?v=20260821-1">
</head>
<body>
    <%@ include file="/WEB-INF/view/common/header.jspf"%>
    <main class="page-content">
        <div class="pay-item-page">
            <header class="page-heading">
                <div>
                    <p>기본환경설정</p>
                    <h1>급여항목 설정</h1>
                </div>
            </header>

            <section class="setting-card" id="payment-settings">
                <div class="card-title">
                    <h2>지급항목 설정</h2>
                </div>
                <div class="setting-layout">
                    <div class="list-panel">
                        <div class="table-wrap">
                            <table class="payment-table">
                                <colgroup>
                                    <col class="col-payment">
                                    <col class="col-tax">
                                    <col class="col-limit">
                                    <col class="col-round">
                                    <col class="col-link">
                                    <col class="col-use">
                                </colgroup>
                                <thead>
                                    <tr>
                                        <th>지급항목</th>
                                        <th>과세여부</th>
                                        <th>비과세한도액</th>
                                        <th>절사단위</th>
                                        <th>근태연결/일괄지급</th>
                                        <th>사용여부</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="item" items="${paymentItems}">
                                        <tr
                                            class="${item.payItemId eq selectedPaymentItem.payItemId ? 'is-selected' : ''}">
                                            <td><a
                                                href="${pageContext.request.contextPath}/settings/pay-item.do?payItemId=${item.payItemId}#payment-settings"><c:out
                                                        value="${item.payName}" /></a></td>
                                            <td><c:out value="${item.taxType}" />
                                                <c:if
                                                    test="${item.taxType eq '비과세' and not empty item.taxFreeName}">_<c:out
                                                        value="${item.taxFreeName}" />
                                                </c:if></td>
                                            <td class="number"><c:if
                                                    test="${not empty item.taxFreeLimit}">
                                                    <c:out value="${item.taxFreeLimit}" />원</c:if></td>
                                            <td><c:out value="${item.roundUnit}" /></td>
                                            <td><c:choose>
                                                <c:when test="${item.payMethod eq '일괄지급'}">일괄지급<c:if test="${not empty item.bulkPayAmount}">_<fmt:formatNumber value="${item.bulkPayAmount}" pattern="#,##0" />원</c:if></c:when>
                                                <c:when test="${not empty item.attendName}"><c:out value="${item.attendName}" /></c:when>
                                            </c:choose></td>
                                            <td><span
                                                class="use-status use-status--${item.useYn eq 'Y' ? 'on' : 'off'}">${item.useYn eq 'Y' ? '사용' : '미사용'}</span></td>
                                        </tr>
                                    </c:forEach>
                                    <c:if test="${empty paymentItems}">
                                        <tr>
                                            <td colspan="6" class="empty-row">등록된 지급항목이 없습니다.</td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <form id="payment-editor-form" class="editor-panel"
                        action="${pageContext.request.contextPath}/settings/pay-item.do"
                        method="post">
                        <input type="hidden" name="payItemId"
                            value="<c:out value='${selectedPaymentItem.payItemId}' />">
                        <input type="hidden" name="taxFreeCode"
                            value="<c:out value='${selectedPaymentItem.taxFreeCode}' />">
                        <h3>지급항목 정보</h3>
                        <label class="editor-field"><span>지급항목</span><input
                            name="payName" maxlength="50"
                            value="<c:out value='${selectedPaymentItem.payName}' />"
                            placeholder="지급항목을 입력해주세요" required></label>
                        <div class="editor-field">
                            <span>과세여부</span>
                            <div class="radio-line">
                                <label><input type="radio" name="taxType" value="전체과세"
                                    ${empty selectedPaymentItem.taxType or selectedPaymentItem.taxType eq '전체과세' ? 'checked' : ''}>
                                    전체과세</label><label><input type="radio" name="taxType"
                                    value="비과세"
                                    ${selectedPaymentItem.taxType eq '비과세' ? 'checked' : ''}>
                                    비과세</label>
                            </div>
                        </div>
                        <div class="editor-field tax-free-field tax-free-selector-field">
                            <span>비과세명</span>
                            <div class="input-with-button">
                                <input name="taxFreeName"
                                    value="<c:out value='${selectedPaymentItem.taxFreeName}' />"
                                    readonly><a href="#tax-free-modal">선택</a>
                            </div>
                        </div>
                        <label class="editor-field tax-free-field standard-tax-free-field"><span>비과세
                                한도액</span>
                        <div class="amount-input">
                                <input name="taxFreeLimit" inputmode="numeric" readonly
                                    value="<c:out value='${selectedPaymentItem.taxFreeLimit}' />"><i>원</i>
                            </div></label>
                        <label class="editor-field tax-free-field direct-tax-free-field"><span>비과세명 직접입력</span><input
                            name="directTaxFreeName" maxlength="300"
                            value="<c:out value='${selectedPaymentItem.directTaxFreeName}' />"
                            placeholder="비과세명을 입력해주세요"></label>
                        <label class="editor-field tax-free-field direct-tax-free-field"><span>비과세 한도액</span>
                            <div class="amount-input"><input name="directTaxFreeLimit" type="number" min="0" inputmode="numeric"
                                value="<c:out value='${selectedPaymentItem.directTaxFreeLimit}' />"><i>원</i></div></label>
                        <label class="editor-field"><span>계산방법</span><input
                            name="calcMethod"
                            value="<c:out value='${selectedPaymentItem.calcMethod}' />"
                            placeholder="계산방법을 입력해주세요"></label> <label class="editor-field"><span>절사단위</span><select
                            name="roundUnit"><option value="0"
                                    ${empty selectedPaymentItem.roundUnit or selectedPaymentItem.roundUnit eq '0' ? 'selected' : ''}>없음</option>
                                <option value="1"
                                    ${selectedPaymentItem.roundUnit eq '1' ? 'selected' : ''}>1원
                                    단위</option>
                                <option value="10"
                                    ${selectedPaymentItem.roundUnit eq '10' ? 'selected' : ''}>10원
                                    단위</option>
                                <option value="100"
                                    ${selectedPaymentItem.roundUnit eq '100' ? 'selected' : ''}>100원
                                    단위</option></select></label> <label class="editor-field"><span>근태연결/일괄지급</span><select
                            name="linkAttendId"><option value="">선택하세요.</option>
                                <c:forEach var="attend" items="${attendItems}">
                                    <option value="${attend.attendanceItemId}"
                                        ${attend.attendanceItemId eq selectedPaymentItem.linkAttendId ? 'selected' : ''}><c:out
                                            value="${attend.attendName}" /></option>
                                </c:forEach>
                                <option value="BATCH"
                                    ${selectedPaymentItem.payMethod eq '일괄지급' ? 'selected' : ''}>일괄지급</option></select></label>
                        <label class="editor-field batch-amount-field"><span>일괄지급액</span>
                        <div class="amount-input">
                                <input name="bulkPayAmount" inputmode="numeric"
                                    value="<c:out value='${selectedPaymentItem.bulkPayAmount}' />"><i>원</i>
                            </div></label>
                        <div class="editor-field">
                            <span>사용여부</span>
                            <div class="radio-line use-radio-line">
                                <label><input type="radio" name="useYn" value="Y"
                                    ${empty selectedPaymentItem.useYn or selectedPaymentItem.useYn eq 'Y' ? 'checked' : ''}>
                                    사용</label><label><input type="radio" name="useYn" value="N"
                                    ${selectedPaymentItem.useYn eq 'N' ? 'checked' : ''}>
                                    사용안함</label>
                            </div>
                        </div>
                        <div class="editor-actions">
                            <button name="action" value="insert">추가</button>
                            <button name="action" value="update">수정</button>
                            <button class="gray" name="action" value="requestDelete">삭제</button>
                            <button class="gray clear-button" name="action" value="clear">내용지우기</button>
                        </div>
                    </form>
                </div>
            </section>

            <section class="setting-card" id="deduction-settings">
                <div class="card-title">
                    <h2>공제항목 설정</h2>
                </div>
                <div class="setting-layout">
                    <div class="list-panel">
                        <div class="table-wrap">
                            <table class="deduction-table">
                                <colgroup>
                                    <col>
                                    <col>
                                    <col>
                                    <col class="col-note">
                                </colgroup>
                                <thead>
                                    <tr>
                                        <th>공제항목</th>
                                        <th>절사단위</th>
                                        <th>사용여부</th>
                                        <th>비고</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="item" items="${deductionItems}">
                                        <tr
                                            class="${item.deductItemId eq selectedDeductionItem.deductItemId ? 'is-selected' : ''}">
                                            <td><a
                                                href="${pageContext.request.contextPath}/settings/pay-item.do?deductItemId=${item.deductItemId}#deduction-settings"><c:out
                                                        value="${item.deductName}" /></a></td>
                                            <td><c:out value="${item.roundUnit}" /></td>
                                            <td><span
                                                class="use-status use-status--${item.useYn eq 'Y' ? 'on' : 'off'}">${item.useYn eq 'Y' ? '사용' : '미사용'}</span></td>
                                            <td><c:out value="${item.note}" /></td>
                                        </tr>
                                    </c:forEach>
                                    <c:if test="${empty deductionItems}">
                                        <tr>
                                            <td colspan="4" class="empty-row">등록된 공제항목이 없습니다.</td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <form class="editor-panel"
                        action="${pageContext.request.contextPath}/settings/deduction-item.do"
                        method="post">
                        <input type="hidden" name="deductItemId"
                            value="<c:out value='${selectedDeductionItem.deductItemId}' />">
                    <h3>공제항목 정보</h3>
                        <label class="editor-field"><span>공제항목</span><input
                            name="deductName" maxlength="50"
                            value="<c:out value='${selectedDeductionItem.deductName}' />"
                            placeholder="공제항목을 입력해주세요" required></label> <label
                            class="editor-field"><span>계산방법</span><input
                            name="calcMethod"
                            value="<c:out value='${selectedDeductionItem.calcMethod}' />"
                            placeholder="계산방법을 입력해주세요"></label> <label class="editor-field"><span>절사단위</span><select
                            name="roundUnit"><option value="0">없음</option>
                                <option value="1"
                                    ${selectedDeductionItem.roundUnit eq '1' ? 'selected' : ''}>1원
                                    단위</option>
                                <option value="10"
                                    ${selectedDeductionItem.roundUnit eq '10' ? 'selected' : ''}>10원
                                    단위</option>
                                <option value="100"
                                    ${selectedDeductionItem.roundUnit eq '100' ? 'selected' : ''}>100원
                                    단위</option></select></label> <label class="editor-field"><span>비고</span><input
                            name="note"
                            value="<c:out value='${selectedDeductionItem.note}' />"></label>
                        <div class="editor-field">
                            <span>사용여부</span>
                            <div class="radio-line use-radio-line">
                                <label><input type="radio" name="useYn" value="Y"
                                    ${empty selectedDeductionItem.useYn or selectedDeductionItem.useYn eq 'Y' ? 'checked' : ''}>
                                    사용</label><label><input type="radio" name="useYn" value="N"
                                    ${selectedDeductionItem.useYn eq 'N' ? 'checked' : ''}>
                                    사용안함</label>
                            </div>
                        </div>
                        <div class="editor-actions">
                            <button name="action" value="insert">추가</button>
                            <button name="action" value="update">수정</button>
                            <button class="gray" name="action" value="requestDelete">삭제</button>
                            <button class="gray clear-button" name="action" value="clear">내용지우기</button>
                        </div>
                    </form>
                </div>
            </section>

            <div id="tax-free-modal" class="tax-modal" role="dialog"
                aria-modal="true" aria-labelledby="tax-modal-title">
                <a class="tax-modal__backdrop" href="#payment-settings"
                    aria-label="닫기"></a>
                <div class="tax-modal__panel">
                    <div class="tax-modal__title">
                        <h2 id="tax-modal-title">비과세 항목 선택</h2>
                        <a href="#payment-settings" aria-label="닫기">×</a>
                    </div>
                    <div class="tax-modal__table">
                        <table>
                            <thead>
                                <tr>
                                    <th>법조문</th>
                                    <th>코드</th>
                                    <th>기재란</th>
                                    <th>비과세항목</th>
                                    <th>한도금액</th>
                                    <th>지급명세서<br>작성여부</th>
                                    <th>선택</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr class="tax-category-row">
                                    <td colspan="7"><div><strong>비과세</strong>
                                        <button type="submit" form="payment-editor-form" formmethod="get" formnovalidate
                                            formaction="${pageContext.request.contextPath}/settings/pay-item.do#payment-settings"
                                            name="selectedTaxFreeCode" value="DIRECT">직접입력</button></div></td>
                                </tr>
                                <c:set var="previousTaxCategory" value="비과세" />
                                <c:forEach var="tax" items="${taxFreeItems}">
                                    <c:if test="${tax.incomeCategory ne previousTaxCategory}">
                                        <tr class="tax-category-row"><td colspan="7"><strong><c:out value="${tax.incomeCategory}" /></strong></td></tr>
                                        <c:set var="previousTaxCategory" value="${tax.incomeCategory}" />
                                    </c:if>
                                    <tr>
                                        <td><c:out value="${tax.legalClause}" /></td>
                                        <td><c:out value="${tax.taxFreeCode}" /></td>
                                        <td><c:out value="${tax.reportField}" /></td>
                                        <td><c:out value="${tax.taxFreeName}" /></td>
                                        <td><c:choose><c:when test="${not empty tax.defaultLimit}"><fmt:formatNumber value="${tax.defaultLimit}" pattern="#,##0" />원</c:when><c:otherwise>-</c:otherwise></c:choose></td>
                                        <td><span class="statement-mark statement-mark--${tax.payStatementYn eq 'Y' ? 'yes' : 'no'}">${tax.payStatementYn eq 'Y' ? '○' : '×'}</span></td>
                                        <td><button type="submit" form="payment-editor-form" formmethod="get" formnovalidate
                                            formaction="${pageContext.request.contextPath}/settings/pay-item.do#payment-settings"
                                            name="selectedTaxFreeCode" value="${tax.taxFreeCode}">선택</button></td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty taxFreeItems}">
                                    <tr>
                                        <td colspan="7" class="empty-row">등록된 비과세 항목이 없습니다.</td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </main>
    <c:if test="${not empty deleteItemType and not empty deleteItemId}">
        <div class="setting-alert" role="alertdialog" aria-modal="true" aria-labelledby="delete-alert-message">
            <a class="setting-alert__backdrop" href="${pageContext.request.contextPath}/settings/pay-item.do?dismissDelete=true#${deleteItemType eq 'PAY' ? 'payment-settings' : 'deduction-settings'}" aria-label="삭제 취소"></a>
            <form class="setting-alert__panel" method="post" action="${pageContext.request.contextPath}/settings/${deleteItemType eq 'PAY' ? 'pay-item.do' : 'deduction-item.do'}">
                <p id="delete-alert-message">선택한 ${deleteItemType eq 'PAY' ? '지급항목' : '공제항목'}을 삭제하시겠습니까?</p>
                <p class="setting-alert__warning">삭제한 항목은 복구할 수 없습니다.</p>
                <input type="hidden" name="${deleteItemType eq 'PAY' ? 'payItemId' : 'deductItemId'}" value="<c:out value='${deleteItemId}' />">
                <div class="setting-alert__actions">
                    <button type="submit" name="action" value="delete">삭제</button>
                    <a href="${pageContext.request.contextPath}/settings/pay-item.do?dismissDelete=true#${deleteItemType eq 'PAY' ? 'payment-settings' : 'deduction-settings'}">취소</a>
                </div>
            </form>
        </div>
        <c:remove var="deleteItemType" scope="session" />
        <c:remove var="deleteItemId" scope="session" />
    </c:if>
    <c:if test="${not empty message}">
        <div class="setting-alert" role="alertdialog" aria-modal="true" aria-labelledby="setting-alert-message">
            <a class="setting-alert__backdrop" href="${pageContext.request.contextPath}/settings/pay-item.do?dismissMessage=true${empty messageAnchor ? '' : messageAnchor}" aria-label="닫기"></a>
            <div class="setting-alert__panel">
                <p id="setting-alert-message"><c:out value="${message}" /></p>
                <a class="setting-alert__confirm" href="${pageContext.request.contextPath}/settings/pay-item.do?dismissMessage=true${empty messageAnchor ? '' : messageAnchor}">확인</a>
            </div>
        </div>
        <c:remove var="message" scope="session" />
        <c:remove var="messageAnchor" scope="session" />
    </c:if>
    <%@ include file="/WEB-INF/view/common/footer.jspf"%>
</body>
</html>
