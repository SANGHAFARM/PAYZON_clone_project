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
    <title>人事管理>諸証明書発行</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/employees/certificate-issue.css?v=20260815-2">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/payzon-ui.css?v=20260821-1">
</head>
<body>
    <%@ include file="/WEB-INF/view/common/header.jspf" %>
    <main class="page-content">
        <div class="certificate-page">
            <header class="page-heading"><div><p>人事管理</p><h1>証明書の発行</h1></div></header>
            <div class="certificate-layout">
                <aside class="employee-panel">
                    <form class="employee-search" action="${pageContext.request.contextPath}/employees/certificate.do" method="get"><input type="hidden" name="mode" value="search"><input name="keyword" value="<c:out value='${param.keyword}' />" placeholder="検索キーワードを入力"><button type="submit">検索</button><a href="${pageContext.request.contextPath}/employees/certificate.do">全体を見る</a></form>
                    <div class="employee-list"><table><thead><tr><th>区分</th><th>氏名</th><th>部署</th><th>役職</th><th>ステータス</th></tr></thead><tbody><c:forEach var="employee" items="${employees}"><c:url var="employeeSelectUrl" value="/employees/certificate.do"><c:param name="employeeId" value="${employee.employeeId}" /><c:param name="keyword" value="${param.keyword}" /></c:url><tr class="${employee.employeeId eq selectedEmployee.employeeId ? 'is-selected' : ''}"><td><a class="employee-row-link" href="${employeeSelectUrl}"><ui:code-label value="${employee.employmentType}" /></a></td><td><a class="employee-row-link" href="${employeeSelectUrl}"><c:out value="${employee.name}" /></a></td><td><a class="employee-row-link" href="${employeeSelectUrl}"><c:out value="${employee.departmentName}" /></a></td><td><a class="employee-row-link" href="${employeeSelectUrl}"><c:out value="${employee.positionName}" /></a></td><td><a class="employee-row-link" href="${employeeSelectUrl}"><span class="status status--${employee.status eq 'WORK' ? 'work' : 'retired'}">${employee.status eq 'WORK' ? '在職' : '退職'}</span></a></td></tr></c:forEach><c:if test="${empty employees}"><tr><td colspan="5" class="empty-row">検索された社員はありません。</td></tr></c:if></tbody></table></div>
                </aside>

                <section class="certificate-workspace">
                    <c:url var="workingCertificateUrl" value="/employees/certificate.do"><c:param name="employeeId" value="${selectedEmployee.employeeId}"/><c:param name="certificateType" value="WORKING"/><c:param name="keyword" value="${param.keyword}"/></c:url><c:url var="careerCertificateUrl" value="/employees/certificate.do"><c:param name="employeeId" value="${selectedEmployee.employeeId}"/><c:param name="certificateType" value="CAREER"/><c:param name="keyword" value="${param.keyword}"/></c:url><c:url var="retirementCertificateUrl" value="/employees/certificate.do"><c:param name="employeeId" value="${selectedEmployee.employeeId}"/><c:param name="certificateType" value="RETIREMENT"/><c:param name="keyword" value="${param.keyword}"/></c:url>
                    <div class="certificate-tabs"><a class="${selectedCertificateType eq 'WORKING' ? 'is-active' : ''}" href="${workingCertificateUrl}">在職証明書</a><a class="${selectedCertificateType eq 'CAREER' ? 'is-active' : ''}" href="${careerCertificateUrl}">職歴証明書</a><a class="${selectedCertificateType eq 'RETIREMENT' ? 'is-active' : ''}" href="${retirementCertificateUrl}">退職証明書</a></div>

                    <form class="certificate-form" id="certificate-issue-form" action="${pageContext.request.contextPath}/employees/certificate-issue.do" method="post">
                        <input type="hidden" name="employeeId" value="${selectedEmployee.employeeId}">
                        <input type="hidden" name="certificateType" value="${selectedCertificateType}">
                        <input type="hidden" name="keyword" value="<c:out value='${param.keyword}' />">
                        <div class="certificate-document certificate-document--working ${selectedCertificateType eq 'WORKING' ? 'certificate-document--selected' : ''}">
                            <header class="document-title"><div class="company-logo"><c:choose><c:when test="${not empty company.logoImgPath}"><img src="${pageContext.request.contextPath}<c:out value='${company.logoImgPath}' />" alt="会社のロゴ"></c:when><c:otherwise><span>会社のロゴ</span></c:otherwise></c:choose></div><h2>在職証明書</h2></header>
                            <div class="document-grid"><h3>個人情報</h3><dl><div><dt>氏名</dt><dd><c:out value="${selectedEmployee.name}" /></dd></div><div><dt>住民登録番号</dt><dd><c:out value="${selectedEmployee.maskedResidentNo}" /></dd></div><div class="wide"><dt>住所</dt><dd><c:out value="${selectedEmployee.address}" /></dd></div></dl><h3>在職事項</h3><dl><div><dt>会社名</dt><dd><c:out value="${company.cmpnName}" /></dd></div><div><dt>事業者番号</dt><dd><c:out value="${company.bizRegNo}" /></dd></div><div><dt>部署</dt><dd><c:out value="${selectedEmployee.departmentName}" /></dd></div><div><dt>役職</dt><dd><c:out value="${selectedEmployee.positionName}" /></dd></div><div><dt>入社日</dt><dd><c:out value="${selectedEmployee.joinDate}" /></dd></div><div><dt>勤続期間</dt><dd><c:out value="${selectedEmployee.careerPeriod}" /></dd></div></dl></div>
                        </div>
                        <div class="certificate-document certificate-document--career ${selectedCertificateType eq 'CAREER' ? 'certificate-document--selected' : ''}">
                            <header class="document-title"><div class="company-logo"><c:choose><c:when test="${not empty company.logoImgPath}"><img src="${pageContext.request.contextPath}<c:out value='${company.logoImgPath}' />" alt="会社のロゴ"></c:when><c:otherwise><span>会社のロゴ</span></c:otherwise></c:choose></div><h2>職歴証明書</h2></header>
                            <div class="document-grid document-grid--career"><h3>個人情報</h3><dl><div><dt>氏名</dt><dd><c:out value="${selectedEmployee.name}" /></dd></div><div><dt>住民登録番号</dt><dd><c:out value="${selectedEmployee.maskedResidentNo}" /></dd></div><div class="wide"><dt>住所</dt><dd><c:out value="${selectedEmployee.address}" /></dd></div></dl><h3>職歴</h3><div class="career-history"><table><thead><tr><th>勤務期間</th><th>労働部署</th><th>役職（職責）</th><th>担当業務</th></tr></thead><tbody><c:forEach var="career" items="${careers}"><tr><td><c:out value="${career.joinDate}" /> ~ <c:out value="${career.retirementDate}" /></td><td><c:out value="${career.departmentName}" /></td><td><c:out value="${career.positionName}" /></td><td><c:out value="${career.duty}" /></td></tr></c:forEach><c:if test="${empty careers}"><c:forEach begin="1" end="4"><tr><td></td><td></td><td></td><td></td></tr></c:forEach></c:if></tbody></table><dl class="career-summary"><div><dt>勤続期間</dt><dd><c:out value="${selectedEmployee.careerPeriod}" /></dd></div><div><dt>退職理由</dt><dd><c:out value="${selectedEmployee.retirementReason}" /></dd></div></dl></div></div>
                        </div>
                        <div class="certificate-document certificate-document--retirement ${selectedCertificateType eq 'RETIREMENT' ? 'certificate-document--selected' : ''}">
                            <header class="document-title"><div class="company-logo"><c:choose><c:when test="${not empty company.logoImgPath}"><img src="${pageContext.request.contextPath}<c:out value='${company.logoImgPath}' />" alt="会社のロゴ"></c:when><c:otherwise><span>会社のロゴ</span></c:otherwise></c:choose></div><h2>退職証明書</h2></header>
                            <div class="document-grid"><h3>個人情報</h3><dl><div><dt>氏名</dt><dd><c:out value="${selectedEmployee.name}" /></dd></div><div><dt>住民登録番号</dt><dd><c:out value="${selectedEmployee.maskedResidentNo}" /></dd></div><div class="wide"><dt>住所</dt><dd><c:out value="${selectedEmployee.address}" /></dd></div></dl><h3>在職事項</h3><dl><div><dt>会社名</dt><dd><c:out value="${company.cmpnName}" /></dd></div><div><dt>事業者番号</dt><dd><c:out value="${company.bizRegNo}" /></dd></div><div><dt>部署</dt><dd><c:out value="${selectedEmployee.departmentName}" /></dd></div><div><dt>役職</dt><dd><c:out value="${selectedEmployee.positionName}" /></dd></div><div><dt>在職期間</dt><dd><c:if test="${not empty selectedEmployee.joinDate or not empty selectedEmployee.retirementDate}"><c:out value="${selectedEmployee.joinDate}" /> ~ <c:out value="${selectedEmployee.retirementDate}" /></c:if></dd></div><div><dt>勤続期間</dt><dd><c:out value="${selectedEmployee.careerPeriod}" /></dd></div></dl></div>
                        </div>

                        <section class="issue-options"><label class="use-field"><span>発行用途</span><select name="certificateUse"><option value="">選択</option><option>ビザ申請用</option><option>銀行提出用</option><option>役所提出用</option><option>学校提出用</option><option>会社提出用</option><option>兵務庁提出用</option><option>関連課題提出用</option><option value="DIRECT">直接入力</option></select></label><label class="direct-use-field"><span>直接入力</span><input name="certificateUseDirect"></label><label class="department-field"><span>発行部署</span><select name="issueDepartmentId"><option value="">選択</option><c:forEach var="department" items="${departments}"><option value="${department.departmentId}"><c:out value="${department.departmentName}" /></option></c:forEach></select></label><label class="contact-field"><span>連絡先</span><input name="companyPhone" value="${company.telNo}" readonly></label></section>
                        <textarea class="certificate-message" name="certificateMemo">上記は、上記のように当社に在職または勤務したことを証明します。</textarea>
                        <div class="issue-date"><input name="issueYear" value="${issueYear}" maxlength="4">年 <input name="issueMonth" value="${issueMonth}" maxlength="2">月 <input name="issueDay" value="${issueDay}" maxlength="2">日</div>
                        <footer class="certificate-footer"><div><strong><c:out value="${company.cmpnName}" /></strong><span><c:out value="${company.ceoTitle}" /> <c:out value="${company.ceoName}" /></span></div><div class="stamp-box"><c:choose><c:when test="${not empty company.stampImgPath}"><img src="${pageContext.request.contextPath}<c:out value='${company.stampImgPath}' />" alt="会社印"></c:when><c:otherwise><span>会社印<br>未登録</span></c:otherwise></c:choose></div></footer>
                        <div class="issue-actions">
                            <button type="submit" name="actionType" value="ISSUE">証明書発行</button>
                        </div>
                    </form>
                </section>
            </div>
            <c:url var="certificateReturnUrl" value="/employees/certificate.do"><c:param name="employeeId" value="${selectedEmployee.employeeId}"/><c:param name="keyword" value="${param.keyword}"/></c:url><c:if test="${not empty popupMessage}"><div class="certificate-alert" role="alertdialog" aria-modal="true" aria-labelledby="certificate-alert-message"><a class="certificate-alert__backdrop" href="${certificateReturnUrl}" aria-label="閉じる"></a><div class="certificate-alert__panel"><p id="certificate-alert-message"><ui:message-label value="${popupMessage}" /></p><a href="${certificateReturnUrl}">確認</a></div></div></c:if>
        </div>
    </main>
    <%@ include file="/WEB-INF/view/common/footer.jspf" %>
</body>
</html>
