<%-- 조회 결과와 입력 양식을 표시하는 JSP 화면이다. --%>
<%-- 照会結果と入力フォームを表示するJSP画面である。 --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="ja-JP">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>基本設定>ユーザー情報</title>
<link rel="stylesheet"
    href="${pageContext.request.contextPath}/css/settings/user-info.css?v=20260820-8">
<link rel="stylesheet"
    href="${pageContext.request.contextPath}/css/common/payzon-ui.css?v=20260821-1">
</head>
<body>
    <%@ include file="/WEB-INF/view/common/header.jspf"%>

    <main class="page-content">
        <div class="user-info-page">
            <c:set var="defaultBanks"
                value="${fn:split('三菱UFJ銀行,三井住友銀行,みずほ銀行,りそな銀行,ゆうちょ銀行,楽天銀行,住信SBIネット銀行,PayPay銀行,ソニー銀行,auじぶん銀行,イオン銀行,セブン銀行,ローソン銀行,横浜銀行,千葉銀行,静岡銀行,福岡銀行,西日本シティ銀行,北海道銀行,京都銀行,七十七銀行,群馬銀行,常陽銀行,八十二銀行,広島銀行,中国銀行,伊予銀行,百五銀行,大垣共立銀行,北洋銀行', ',')}" />
            <c:set var="payDays"
                value="${fn:split('01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,99', ',')}" />
            <header class="page-heading">
                <div>
                    <p class="page-heading__path">基本設定</p>
                    <h1>ユーザー情報</h1>
                </div>
                <p class="page-heading__notice">
                    <strong>*</strong> 表示は必須入力です。
                </p>
            </header>

            <form class="company-form"
                action="${pageContext.request.contextPath}/settings/user-info.do"
                method="post" enctype="multipart/form-data">
                <input type="hidden" name="companyId"
                    value="<c:out value='${company.companyId}' />">

                <section class="form-section">
                    <h2>会社情報</h2>
                    <div class="form-grid">
                        <label class="field"><span><b>*</b> 相互</span><input
                            name="cmpnName" value="<c:out value='${company.cmpnName}' />"
                            required maxlength="100"></label>
                        <div class="field">
                            <span><b>*</b> 代表者職級/代表者</span>
                            <div class="field-pair">
                                <input name="ceoTitle"
                                    value="<c:out value='${company.ceoTitle}' />" required
                                    maxlength="50" aria-label="代表者職級"><i>/</i><input
                                    name="ceoName" value="<c:out value='${company.ceoName}' />"
                                    required maxlength="50" aria-label="代表者の氏名">
                            </div>
                        </div>
                        <label class="field"><span><b>*</b> 事業者番号</span><input
                            name="bizRegNo" value="<c:out value='${company.bizRegNo}' />"
                            required maxlength="20"></label> <label class="field"><span>法人登録番号</span><input
                            name="corpRegNo" value="<c:out value='${company.corpRegNo}' />"
                            maxlength="20"></label> <label class="field"><span>設立日</span><input
                            type="date" lang="ja-JP" name="foundationDate"
                            value="<fmt:formatDate value='${company.foundationDate}' pattern='yyyy-MM-dd' />"></label>
                        <label class="field"><span>ホームページ</span><input type="url"
                            name="homepageUrl"
                            value="<c:out value='${company.homepageUrl}' />" maxlength="300"
                            placeholder="https://example.com"></label>
                        <div class="field field--wide">
                            <span><b>*</b> 事業所の住所</span>
                            <div class="address-fields">
                                <input name="zipCode"
                                    value="<c:out value='${company.zipCode}' />" required
                                    maxlength="10" placeholder="郵便番号" aria-label="郵便番号"><input
                                    name="address" value="<c:out value='${company.address}' />"
                                    required maxlength="500" placeholder="住所" aria-label="事業所の住所">
                            </div>
                        </div>
                        <label class="field"><span><b>*</b> 電話番号</span><input
                            type="tel" name="telNo"
                            value="<c:out value='${company.telNo}' />" required
                            maxlength="30" placeholder="02-0000-0000"></label> <label
                            class="field"><span>ファックス番号</span><input type="tel"
                            name="faxNo" value="<c:out value='${company.faxNo}' />"
                            maxlength="30"></label> <label class="field"><span>業態</span><input
                            name="bizType" value="<c:out value='${company.bizType}' />"
                            maxlength="100"></label> <label class="field"><span>種目</span><input
                            name="bizItem" value="<c:out value='${company.bizItem}' />"
                            maxlength="100"></label>
                    </div>
                </section>

                <section class="form-section">
                    <div class="section-title-row">
                        <h2>担当者情報</h2>
                    </div>
                    <div class="form-grid form-grid--three">
                        <label class="field"><span><b>*</b> 氏名</span><input
                            name="managerName"
                            value="<c:out value='${company.managerName}' />" required
                            maxlength="50"></label> <div class="field"><span>部署</span><div class="managed-select"><select
                            name="managerDeptName"><option value="">選択</option>
                                <c:forEach var="dept" items="${departmentList}">
                                    <option value="<c:out value='${dept.departmentName}' />"
                                        ${dept.departmentName eq company.managerDeptName ? 'selected' : ''}><c:out
                                            value="${dept.departmentName}" /></option>
                                </c:forEach></select><a href="#department-manager-modal">管理</a></div></div> <div class="field"><span>役職</span><div class="managed-select"><select
                            name="managerPosName"><option value="">選択</option>
                                <c:forEach var="pos" items="${positionList}">
                                    <option value="<c:out value='${pos.jobPositionName}' />"
                                        ${pos.jobPositionName eq company.managerPosName ? 'selected' : ''}><c:out
                                            value="${pos.jobPositionName}" /></option>
                                </c:forEach></select><a href="#position-manager-modal">管理</a></div></div> <label class="field"><span>電話番号</span><input type="tel"
                            name="managerTelNo"
                            value="<c:out value='${company.managerTelNo}' />" maxlength="30"></label>
                        <label class="field"><span>携帯電話番号</span><input type="tel"
                            name="managerMobileNo"
                            value="<c:out value='${company.managerMobileNo}' />"
                            maxlength="30"></label> <label class="field"><span>メール</span><input
                            type="email" name="managerEmail"
                            value="<c:out value='${company.managerEmail}' />" maxlength="150"></label>
                    </div>
                </section>

                <section class="form-section">
                    <h2>給与支払情報</h2>
                    <div class="pay-info-table">
                        <div class="pay-info-label"><b>*</b> 給与算定期間</div>
                        <div class="pay-period">
                            <select name="payCalcStartScope" required><option
                                    value="P" ${company.payCalcStartScope eq 'P' ? 'selected' : ''}>前月</option>
                                <option value="N"
                                    ${empty company.payCalcStartScope or company.payCalcStartScope eq 'N' ? 'selected' : ''}>当月</option></select><select
                                name="payCalcStartDay" required><c:forEach var="day"
                                    items="${payDays}">
                                    <option value="${day}"
                                        ${day eq company.payCalcStartDay or (empty company.payCalcStartDay and day eq '01') ? 'selected' : ''}>${day eq '99' ? '月末' : day}</option>
                                </c:forEach></select><span>~</span><select name="payCalcEndScope" required><option
                                    value="P" ${company.payCalcEndScope eq 'P' ? 'selected' : ''}>前月</option>
                                <option value="N"
                                    ${empty company.payCalcEndScope or company.payCalcEndScope eq 'N' ? 'selected' : ''}>当月</option></select><select
                                name="payCalcEndDay" required><c:forEach var="day"
                                    items="${payDays}">
                                    <option value="${day}"
                                        ${day eq company.payCalcEndDay or (empty company.payCalcEndDay and day eq '99') ? 'selected' : ''}>${day eq '99' ? '月末' : day}</option>
                                </c:forEach></select>
                        </div>
                        <div class="pay-info-label"><b>*</b> 給与支給日</div>
                        <div class="pay-day">
                            <select name="payDateScope" required><option value="0"
                                    ${company.payDateScope eq '0' ? 'selected' : ''}>当月</option>
                                <option value="1"
                                    ${empty company.payDateScope or company.payDateScope eq '1' ? 'selected' : ''}>翼月</option></select><select
                                name="payDateDay" required><c:forEach var="day"
                                    items="${payDays}">
                                    <option value="${day}"
                                        ${day eq company.payDateDay or (empty company.payDateDay and day eq '05') ? 'selected' : ''}>${day eq '99' ? '月末' : day}</option>
                                </c:forEach></select><span>日</span>
                        </div>
                        <div class="pay-info-label">金融機関</div>
                        <div class="pay-bank">
                            <select name="payBankName"><option value="">選択してください</option>
                                <c:forEach var="bank"
                                    items="${empty bankList ? defaultBanks : bankList}">
                                    <option value="<c:out value='${bank}' />"
                                        ${bank eq company.payBankName ? 'selected' : ''}><c:out
                                            value="${bank}" /></option>
                                </c:forEach></select>
                        </div>
                        <div class="pay-info-label">口座番号</div>
                        <div class="pay-account">
                            <input name="payAccountNo"
                                value="<c:out value='${company.payAccountNo}' />"
                                maxlength="100">
                        </div>
                        <div class="pay-info-label">預金株</div>
                        <div class="pay-holder">
                            <input name="payAccountHolder"
                                value="<c:out value='${company.payAccountHolder}' />"
                                maxlength="100">
                        </div>
                    </div>
                </section>

                <section class="asset-grid">
                    <article class="asset-card">
                        <div class="asset-card__title">
                            <h2>ロゴ登録</h2>
                        </div>
                        <div class="asset-card__body">
                            <div class="asset-preview">
                                <c:choose>
                                    <c:when test="${not empty company.logoImgPath}">
                                        <img
                                            src="${pageContext.request.contextPath}<c:out value='${company.logoImgPath}' />"
                                            alt="会社のロゴ">
                                    </c:when>
                                    <c:otherwise>
                                        <span>登録されたロゴはありません。</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div>
                                <p>ロゴは、横150px以下のPNGで保存されます。</p>
                                <p>透明なPNG画像を使用することをお勧めします。</p>
                                <div class="mini-actions">
                                    <a href="#logo-upload-modal">登録</a>
                                    <button type="submit" name="action" value="deleteLogo">削除</button>
                                </div>
                            </div>
                        </div>
                    </article>
                    <article class="asset-card">
                        <div class="asset-card__title">
                            <h2>塗装登録</h2>
                        </div>
                        <div class="asset-card__body">
                            <div class="asset-preview">
                                <c:choose>
                                    <c:when test="${not empty company.stampImgPath}">
                                        <img
                                            src="${pageContext.request.contextPath}<c:out value='${company.stampImgPath}' />"
                                            alt="会社印">
                                    </c:when>
                                    <c:otherwise>
                                        <span>登録された塗装はありません。</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div>
                                <p>塗装は、横150px以下のPNGで保存されます。</p>
                                <p>透明なPNG画像を使用することをお勧めします。</p>
                                <div class="mini-actions">
                                    <a href="#stamp-upload-modal">登録</a>
                                    <button type="submit" name="action" value="deleteStamp">削除</button>
                                </div>
                            </div>
                        </div>
                    </article>
                </section>

                <div id="logo-upload-modal" class="upload-modal" role="dialog"
                    aria-modal="true" aria-labelledby="logo-upload-title">
                    <a class="upload-modal__backdrop" href="${pageContext.request.contextPath}/settings/user-info.do" aria-label="閉じる"></a>
                    <div class="upload-modal__panel">
                        <div class="upload-modal__title">
                            <h2 id="logo-upload-title">画像を登録する</h2>
                            <a href="${pageContext.request.contextPath}/settings/user-info.do" aria-label="閉じる">×</a>
                        </div>
                        <div class="upload-modal__body">
                            <p class="upload-modal__subtitle">基本画像を選択</p>
                            <label class="preset-image-option">
                                <input type="radio" name="logoPreset" value="payzon">
                                <img src="${pageContext.request.contextPath}/images/settings/presets/payzon-logo.png" alt="PAYZON基本ロゴ">
                                <span>PAYZON基本ロゴ</span>
                            </label>
                            <p class="upload-modal__divider"><span>または直接アップロード</span></p>
                            <label class="upload-file-choice"><input type="radio" name="logoPreset" value="upload" checked> 直接アップロードファイルを使用する</label>
                            <input type="file" name="logoFile" accept="image/png,image/jpeg">
                            <p>
                                * ファイル容量: <strong>1MB未満</strong>でなければなりません。<br>* ファイル形式: <strong> PNGまたはJPG</strong>のみ登録できます。
                            </p>
                        </div>
                        <button class="upload-modal__confirm" type="submit" name="action"
                            value="saveLogo">確認</button>
                    </div>
                </div>
                <div id="stamp-upload-modal" class="upload-modal" role="dialog"
                    aria-modal="true" aria-labelledby="stamp-upload-title">
                    <a class="upload-modal__backdrop" href="${pageContext.request.contextPath}/settings/user-info.do" aria-label="閉じる"></a>
                    <div class="upload-modal__panel">
                        <div class="upload-modal__title">
                            <h2 id="stamp-upload-title">画像を登録する</h2>
                            <a href="${pageContext.request.contextPath}/settings/user-info.do" aria-label="閉じる">×</a>
                        </div>
                        <div class="upload-modal__body">
                            <p class="upload-modal__subtitle">基本画像を選択</p>
                            <label class="preset-image-option preset-image-option--stamp">
                                <input type="radio" name="stampPreset" value="payzon">
                                <img src="${pageContext.request.contextPath}/images/settings/presets/payzon-stamp.png" alt="PAYZON基本塗装">
                                <span>PAYZON基本塗装</span>
                            </label>
                            <p class="upload-modal__divider"><span>または直接アップロード</span></p>
                            <label class="upload-file-choice"><input type="radio" name="stampPreset" value="upload" checked> 直接アップロードファイルを使用する</label>
                            <input type="file" name="stampFile" accept="image/png,image/jpeg">
                            <p>
                                * ファイル容量: <strong>1MB未満</strong>でなければなりません。<br>* ファイル形式: <strong> PNGまたはJPG</strong>のみ登録できます。
                            </p>
                        </div>
                        <button class="upload-modal__confirm" type="submit" name="action"
                            value="saveStamp">確認</button>
                    </div>
                </div>

                <div class="form-actions">
                    <button class="button button--primary" type="submit" name="action"
                        value="save">保存する</button>
                    <a class="button"
                        href="${pageContext.request.contextPath}/settings/user-info.do">キャンセル</a>
                </div>
            </form>

            <div id="department-manager-modal" class="setting-manager-modal" role="dialog" aria-modal="true" aria-labelledby="department-manager-title">
                <a class="setting-manager-modal__backdrop" href="${pageContext.request.contextPath}/settings/user-info.do" aria-label="部署管理を閉じる"></a>
                <section class="setting-manager-modal__panel">
                    <header><h2 id="department-manager-title">部署リスト管理</h2><a href="${pageContext.request.contextPath}/settings/user-info.do" aria-label="閉じる">×</a></header>
                    <div class="setting-manager-list">
                        <c:forEach var="dept" items="${departmentList}">
                            <form method="post" action="${pageContext.request.contextPath}/settings/user-info.do" class="setting-manager-row">
                                <input type="hidden" name="departmentId" value="${dept.departmentId}">
                                <input type="text" name="departmentName" value="<c:out value='${dept.departmentName}' />" maxlength="100" required aria-label="部署名">
                                <button type="submit" name="action" value="updateDepartment">修正</button>
                                <button type="submit" name="action" value="requestDeleteDepartment" formnovalidate>削除</button>
                            </form>
                        </c:forEach>
                        <c:if test="${empty departmentList}"><p class="setting-manager-empty">登録された部署はありません。</p></c:if>
                    </div>
                    <form method="post" action="${pageContext.request.contextPath}/settings/user-info.do" class="setting-manager-add">
                        <input type="text" name="departmentName" maxlength="100" placeholder="新しい部署名" required>
                        <button type="submit" name="action" value="addDepartment">追加する</button>
                    </form>
                </section>
            </div>

            <div id="position-manager-modal" class="setting-manager-modal" role="dialog" aria-modal="true" aria-labelledby="position-manager-title">
                <a class="setting-manager-modal__backdrop" href="${pageContext.request.contextPath}/settings/user-info.do" aria-label="職務管理を閉じる"></a>
                <section class="setting-manager-modal__panel">
                    <header><h2 id="position-manager-title">役職リスト管理</h2><a href="${pageContext.request.contextPath}/settings/user-info.do" aria-label="閉じる">×</a></header>
                    <div class="setting-manager-list">
                        <c:forEach var="pos" items="${positionList}">
                            <form method="post" action="${pageContext.request.contextPath}/settings/user-info.do" class="setting-manager-row">
                                <input type="hidden" name="positionId" value="${pos.jobPositionId}">
                                <input type="text" name="positionName" value="<c:out value='${pos.jobPositionName}' />" maxlength="100" required aria-label="職名">
                                <button type="submit" name="action" value="updatePosition">修正</button>
                                <button type="submit" name="action" value="requestDeletePosition" formnovalidate>削除</button>
                            </form>
                        </c:forEach>
                        <c:if test="${empty positionList}"><p class="setting-manager-empty">登録された役職はありません。</p></c:if>
                    </div>
                    <form method="post" action="${pageContext.request.contextPath}/settings/user-info.do" class="setting-manager-add">
                        <input type="text" name="positionName" maxlength="100" placeholder="新しい役職名" required>
                        <button type="submit" name="action" value="addPosition">追加する</button>
                    </form>
                </section>
            </div>

            <c:if test="${not empty deleteSettingType}">
                <div class="setting-delete-modal" role="alertdialog" aria-modal="true" aria-labelledby="setting-delete-title">
                    <a class="setting-manager-modal__backdrop" href="${pageContext.request.contextPath}/settings/user-info.do" aria-label="削除のキャンセル"></a>
                    <form class="setting-delete-modal__panel" method="post" action="${pageContext.request.contextPath}/settings/user-info.do">
                        <p id="setting-delete-title"><strong><c:out value="${deleteSettingName}" /></strong> 項目を削除してもよろしいですか？</p>
                        <p class="setting-delete-warning">削除した項目は復元できません。</p>
                        <c:choose><c:when test="${deleteSettingType eq 'department'}"><input type="hidden" name="departmentId" value="${deleteSettingId}"><input type="hidden" name="action" value="deleteDepartment"></c:when><c:otherwise><input type="hidden" name="positionId" value="${deleteSettingId}"><input type="hidden" name="action" value="deletePosition"></c:otherwise></c:choose>
                        <div><button type="submit">削除</button><a href="${pageContext.request.contextPath}/settings/user-info.do">キャンセル</a></div>
                    </form>
                </div>
            </c:if>

            <c:if test="${not empty message}">
                <div class="user-info-alert" role="alertdialog" aria-modal="true" aria-labelledby="user-info-alert-message">
                    <a class="user-info-alert__backdrop" href="${pageContext.request.contextPath}/settings/user-info.do?dismissMessage=true" aria-label="確認"></a>
                    <div class="user-info-alert__panel">
                        <p id="user-info-alert-message"><ui:message-label value="${message}" /></p>
                        <a href="${pageContext.request.contextPath}/settings/user-info.do?dismissMessage=true">確認</a>
                    </div>
                </div>
                <c:remove var="message" scope="session" />
            </c:if>

            <c:if test="${not empty managerMessage}">
                <c:set var="managerReturnHash" value="${managerType eq 'position' ? '#position-manager-modal' : '#department-manager-modal'}" />
                <div class="user-info-alert" role="alertdialog" aria-modal="true" aria-labelledby="manager-alert-message">
                    <a class="user-info-alert__backdrop" href="${pageContext.request.contextPath}/settings/user-info.do?dismissMessage=true${managerReturnHash}" aria-label="確認"></a>
                    <div class="user-info-alert__panel">
                        <p id="manager-alert-message"><ui:message-label value="${managerMessage}" /></p>
                        <a href="${pageContext.request.contextPath}/settings/user-info.do?dismissMessage=true${managerReturnHash}">確認</a>
                    </div>
                </div>
            </c:if>
        </div>
    </main>

    <%@ include file="/WEB-INF/view/common/footer.jspf"%>
</body>
</html>
