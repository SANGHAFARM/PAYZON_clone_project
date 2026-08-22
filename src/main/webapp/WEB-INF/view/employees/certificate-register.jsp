<%-- 조회 결과와 입력 양식을 표시하는 JSP 화면이다. --%>
<%-- 照会結果と入力フォームを表示するJSP画面である。 --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html lang="ja-JP">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>人事管理>諸証明書発行台帳</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/employees/certificate-register.css?v=20260815-2">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/payzon-ui.css?v=20260821-1">
</head>
<body>
    <%@ include file="/WEB-INF/view/common/header.jspf" %>

    <main class="page-content certificate-register-page">
        <header class="page-heading">
            <div><p>人事管理</p><h1>諸証明書発行台帳</h1></div>
        </header>

        <%--  CERTIFICATE_ISSUANCE中心JOIN結果の検索と発行履歴のリスト --%>
        <section class="register-card">
            <form class="register-search" action="${pageContext.request.contextPath}/employees/certificate-register.do" method="get">
                <input type="hidden" name="mode" value="search">
                <label>
                    <span class="sr-only">証明書の区分</span>
                    <select name="certificateType">
                        <option value="">全体</option>
                        <option value="WORKING" ${condition.certificateType eq 'WORKING' ? 'selected' : ''}>在職証明書</option>
                        <option value="CAREER" ${condition.certificateType eq 'CAREER' ? 'selected' : ''}>職歴証明書</option>
                        <option value="RETIREMENT" ${condition.certificateType eq 'RETIREMENT' ? 'selected' : ''}>退職証明書</option>
                    </select>
                </label>
                <label class="date-range">
                    <span class="sr-only">発行日</span>
                    <input type="date" lang="ja-JP" name="issueDateFrom" value="${condition.issueDateFrom}">
                    <i>~</i>
                    <input type="date" lang="ja-JP" name="issueDateTo" value="${condition.issueDateTo}">
                </label>
                <label class="keyword-field">
                    <span class="sr-only">検索語</span>
                    <input type="search" name="keyword" value="<c:out value='${condition.keyword}' />" placeholder="検索キーワードを入力">
                </label>
                <button type="submit" class="search-button">検索</button>
                <a class="all-button" href="${pageContext.request.contextPath}/employees/certificate-register.do">全体を見る</a>
                <p class="result-count">総発行件数 <strong><c:out value="${totalCount}" default="0" /></strong>件</p>
            </form>

            <form action="${pageContext.request.contextPath}/employees/certificate-register-delete.do" method="post">
                <div class="register-table-wrap">
                    <table class="register-table">
                        <colgroup>
                            <col class="check-col"><col class="number-col"><col class="type-col"><col class="use-col">
                            <col class="employment-col"><col class="name-col"><col class="department-col">
                            <col class="position-col"><col class="date-col">
                        </colgroup>
                        <thead>
                            <tr>
                                <th>選択</th>
                                <th>発行番号</th><th>証明書区分</th><th>発行用途</th><th>雇用区分</th>
                                <th>氏名</th><th>部署</th><th>役職</th><th>発行日</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="certificate" items="${certificates}">
                                <tr>
                                    <td><input type="checkbox" name="certificateIds" value="${certificate.certificateId}" aria-label="${certificate.certificateNo}を選択"></td>
                                    <td><c:out value="${certificate.certificateNo}" /></td>
                                    <td><span class="certificate-type certificate-type--${certificate.certificateType}"><ui:code-label value="${certificate.certificateTypeName}" /></span></td>
                                    <td><c:out value="${certificate.certificateUse}" /></td>
                                    <td><ui:code-label value="${certificate.employmentType}" /></td>
                                    <td><c:out value="${certificate.employeeName}" /></td>
                                    <td><c:out value="${certificate.departmentName}" /></td>
                                    <td><c:out value="${certificate.positionName}" /></td>
                                    <td><c:out value="${certificate.issueDate}" /></td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty certificates}"><tr><td colspan="9" class="empty-row">照会された証明書の発行履歴はありません。</td></tr></c:if>
                        </tbody>
                    </table>
                </div>

                <nav class="pagination" aria-label="ページ移動">
                    <c:if test="${pageInfo.hasPrevious}"><c:url var="previousUrl" value="/employees/certificate-register.do"><c:param name="page" value="${pageInfo.previousPage}"/><c:param name="certificateType" value="${condition.certificateType}"/><c:param name="issueDateFrom" value="${condition.issueDateFrom}"/><c:param name="issueDateTo" value="${condition.issueDateTo}"/><c:param name="keyword" value="${condition.keyword}"/></c:url><a href="${previousUrl}">以前</a></c:if>
                    <c:forEach var="pageNo" begin="${pageInfo.startPage}" end="${pageInfo.endPage}">
                        <c:url var="pageUrl" value="/employees/certificate-register.do"><c:param name="page" value="${pageNo}"/><c:param name="certificateType" value="${condition.certificateType}"/><c:param name="issueDateFrom" value="${condition.issueDateFrom}"/><c:param name="issueDateTo" value="${condition.issueDateTo}"/><c:param name="keyword" value="${condition.keyword}"/></c:url><a class="${pageNo eq pageInfo.currentPage ? 'is-current' : ''}" href="${pageUrl}"><c:out value="${pageNo}" /></a>
                    </c:forEach>
                    <c:if test="${pageInfo.hasNext}"><c:url var="nextUrl" value="/employees/certificate-register.do"><c:param name="page" value="${pageInfo.nextPage}"/><c:param name="certificateType" value="${condition.certificateType}"/><c:param name="issueDateFrom" value="${condition.issueDateFrom}"/><c:param name="issueDateTo" value="${condition.issueDateTo}"/><c:param name="keyword" value="${condition.keyword}"/></c:url><a href="${nextUrl}">次へ</a></c:if>
                </nav>

                <div class="delete-actions">
                    <button type="submit" name="deleteAction" value="requestSelected">選択削除</button>
                    <button type="submit" name="deleteAction" value="requestAll" class="danger">完全削除</button>
                </div>
            </form>
        </section>

        <c:if test="${not empty popupMessage}"><div class="register-alert" role="alertdialog" aria-modal="true" aria-labelledby="register-alert-message"><a class="register-alert__backdrop" href="${pageContext.request.contextPath}/employees/certificate-register.do" aria-label="閉じる"></a><div class="register-alert__panel"><p id="register-alert-message"><ui:message-label value="${popupMessage}" /></p><a href="${pageContext.request.contextPath}/employees/certificate-register.do">確認</a></div></div></c:if>

        <c:if test="${deleteConfirmation}"><div class="register-alert" role="alertdialog" aria-modal="true" aria-labelledby="delete-confirmation-message"><a class="register-alert__backdrop" href="${pageContext.request.contextPath}/employees/certificate-register.do" aria-label="削除のキャンセル"></a><form class="register-alert__panel" action="${pageContext.request.contextPath}/employees/certificate-register-delete.do" method="post"><c:choose><c:when test="${deleteMode eq 'ALL'}"><p id="delete-confirmation-message">すべての <strong><c:out value="${totalCount}" />件</strong>の発行履歴を削除してもよろしいですか？</p><p class="register-alert__warning">削除した発行履歴は復元できません。</p><input type="hidden" name="deleteAction" value="confirmAll"></c:when><c:otherwise><p id="delete-confirmation-message">選択した <strong><c:out value="${deleteCertificateCount}" />件</strong>の発行履歴を削除してもよろしいですか？</p><p class="register-alert__warning">削除した発行履歴は復元できません。</p><input type="hidden" name="deleteAction" value="confirmSelected"><c:forEach var="certificateId" items="${deleteCertificateIds}"><input type="hidden" name="certificateIds" value="${certificateId}"></c:forEach></c:otherwise></c:choose><div class="register-alert__actions"><button type="submit">削除</button><a href="${pageContext.request.contextPath}/employees/certificate-register.do">キャンセル</a></div></form></div></c:if>
    </main>

    <%@ include file="/WEB-INF/view/common/footer.jspf" %>
</body>
</html>
