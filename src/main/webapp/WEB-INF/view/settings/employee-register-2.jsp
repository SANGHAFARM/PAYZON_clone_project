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
<title>基本設定>社員登録2</title>
<link rel="stylesheet"
    href="${pageContext.request.contextPath}/css/settings/employee-register.css?v=20260820-12">
<link rel="stylesheet"
    href="${pageContext.request.contextPath}/css/common/payzon-ui.css?v=20260821-1">
</head>
<body>
    <%@ include file="/WEB-INF/view/common/header.jspf"%>
    <main class="page-content">
        <div class="employee-page">
            <c:set var="defaultDepartments"
                value="${fn:split('社長室,開発部,コンテンツ部,業務支援部,デザイン部,管理部,企画戦略部', ',')}" />
            <c:set var="defaultPositions"
                value="${fn:split('取締役,次長,社長,部長,課長,係長,主任,社員,室長', ',')}" />
            <c:set var="employmentTypes"
                value="${fn:split('정규직,계약직,임시직,파견직,위촉직,일용직', ',')}" />
            <c:set var="trainingTypes"
                value="${fn:split('社内職務研修,社外職務研修,階層別研修,語学研修,その他', ',')}" />
            <c:set var="rewardTypes"
                value="${fn:split('褒賞,表彰,授賞,免職,停職,減給,譴責,注意,警告,処分なし,解雇', ',')}" />
            <c:set var="appointmentTypes"
                value="${fn:split('採用,配置転換,昇進,昇格,昇給,派遣', ',')}" />
            <c:set var="retireTypes"
                value="${fn:split('定年退職,整理解雇,自己都合退職,役員退職,中間精算,その他', ',')}" />

            <header class="page-heading">
                <div>
                    <p>基本設定</p>
                    <h1>社員登録</h1>
                </div>
                <p class="page-heading__notice">
                    <strong>*</strong> 表示は必須入力です。
                </p>
            </header>
            <form
                action="${pageContext.request.contextPath}/settings/register2.do"
                method="post" enctype="multipart/form-data">
                <input type="hidden" name="empId"
                    value="<c:out value='${employee.employeeId}' />">
                <input type="hidden" name="licenseRowCount" value="${licenseRowCount}">
                <input type="hidden" name="languageRowCount" value="${languageRowCount}">
                <input type="hidden" name="trainingRowCount" value="${trainingRowCount}">
                <input type="hidden" name="rewardRowCount" value="${rewardRowCount}">
                <input type="hidden" name="appointmentRowCount" value="${appointmentRowCount}">
                <div class="employee-layout">
                    <aside class="employee-summary">
                        <div class="photo-box">
                            <c:choose>
                                <c:when test="${not empty employee.photoPath}">
                                    <img
                                        src="${pageContext.request.contextPath}<c:out value='${employee.photoPath}' />"
                                        alt="社員の写真">
                                </c:when>
                                <c:otherwise>
                                    <span>写真待ち</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="summary-actions">
                            <a href="#photo-upload-modal">登録</a>
                            <button name="action" value="deletePhoto" formnovalidate>削除</button>
                        </div>
                        <dl>
                            <div>
                                <dt>社員番号</dt>
                                <dd>
                                    <c:out value="${employee.empNo}" />
                                </dd>
                            </div>
                            <div>
                                <dt>氏名</dt>
                                <dd>
                                    <c:out value="${employee.empNameKr}" />
                                </dd>
                            </div>
                            <div>
                                <dt>部署</dt>
                                <dd>
                                    <%-- 全部署のリストを回しながら、現在の社員の部署IDと一致する部署名を出力 --%>
                                    <c:forEach var="dept" items="${departmentList}">
                                        <c:if test="${dept.departmentId eq employee.departmentId}">
                                            <c:out value="${dept.departmentName}" />
                                        </c:if>
                                    </c:forEach>
                                </dd>
                            </div>
                            <div>
                                <dt>役職</dt>
                                <dd>
                                    <%-- 全役職リストを回しながら現在の社員の役職IDと一致する役職名を出力 --%>
                                    <c:forEach var="pos" items="${positionList}">
                                        <c:if test="${pos.jobPositionId eq employee.jobPositionId}">
                                            <c:out value="${pos.jobPositionName}" />
                                        </c:if>
                                    </c:forEach>
                                </dd>
                            </div>
                            <div>
                                <dt>入社日</dt>
                                <dd>
                                    <fmt:formatDate value="${employee.joinDate}" pattern="yyyy/MM/dd" />
                                </dd>
                            </div>
                        </dl>
                        <nav class="section-shortcuts" aria-label="社員情報のショートカット">
                            <section>
                                <p class="shortcut-title">
                                    <span>社員情報</span><em>01</em>
                                </p>
                                <div class="section-links">
                                    <a
                                        href="${pageContext.request.contextPath}/settings/register1.do?empId=${employee.employeeId}#salary-insurance">給与/ 4大保険</a><a
                                        href="${pageContext.request.contextPath}/settings/register1.do?empId=${employee.employeeId}#dependents">扶養家族</a><a
                                        href="${pageContext.request.contextPath}/settings/register1.do?empId=${employee.employeeId}#education">学歴</a><a
                                        href="${pageContext.request.contextPath}/settings/register1.do?empId=${employee.employeeId}#career">職歴</a><a
                                        href="${pageContext.request.contextPath}/settings/register1.do?empId=${employee.employeeId}#military">兵役</a>
                                </div>
                            </section>
                            <section>
                                <p class="shortcut-title">
                                    <span>社員情報</span><em>02</em>
                                </p>
                                <div class="section-links">
                                    <a href="#license">資格/ライセンス</a><a href="#training">教育/訓練</a><a
                                        href="#reward-punish">罰</a><a href="#appointment">発令</a><a
                                        href="#recommendation">おすすめ/身元保証</a><a href="#retirement">退職</a>
                                </div>
                            </section>
                        </nav>
                    </aside>

                    <div class="employee-form">
                        <section class="form-card">
                            <h2>基本情報</h2>
                            <div class="form-grid">
                                <label class="field"><span>社員番号</span><input
                                    name="empNo" value="<c:out value='${employee.empNo}' />"
                                    readonly></label><label class="field"><span><b>*</b>
                                        雇用形態</span><select name="empType" required><option value="">選択してください。</option>
                                        <c:forEach var="type" items="${employmentTypes}">
                                            <option value="${type}"
                                            ${type eq employee.empType ? 'selected' : ''}><c:choose><c:when test="${type eq '정규직'}">正社員</c:when><c:when test="${type eq '계약직'}">契約社員</c:when><c:when test="${type eq '임시직'}">臨時社員</c:when><c:when test="${type eq '파견직'}">派遣社員</c:when><c:when test="${type eq '위촉직'}">委嘱社員</c:when><c:when test="${type eq '일용직'}">日雇い</c:when><c:otherwise><c:out value="${type}" /></c:otherwise></c:choose></option>
                                        </c:forEach></select></label> <label class="field"><span><b>*</b> 氏名</span><input
                                    name="empNameKr"
                                    value="<c:out value='${employee.empNameKr}' />" required
                                    maxlength="50"></label><label class="field"><span>氏名（英語）</span><input
                                    name="empNameEn"
                                    value="<c:out value='${employee.empNameEn}' />" maxlength="100"></label>
                                <label class="field"><span><b>*</b> 入社日</span><input
                                    type="date" lang="ja-JP" name="joinDate"
                                    value="<fmt:formatDate value='${employee.joinDate}' pattern='yyyy-MM-dd' />"
                                    required></label><label class="field"><span>退社日</span><input
                                    type="date" lang="ja-JP"
                                    value="<fmt:formatDate value='${employee.retireDate}' pattern='yyyy-MM-dd' />"
                                    readonly></label> <label class="field"><span>部署</span><select
                                    name="deptId"><option value="">選択してください。</option>
                                        <c:choose>
                                            <c:when test="${not empty departmentList}">
                                                <c:forEach var="dept" items="${departmentList}">
                                                    <option value="${dept.departmentId}"
                                                        ${dept.departmentId eq employee.departmentId ? 'selected' : ''}>${dept.departmentName}</option>
                                                </c:forEach>
                                            </c:when>
                                        <c:otherwise>
                                            <option value="" disabled>登録された部署はありません。</option>
                                            </c:otherwise>
                                        </c:choose></select></label><label class="field"><span>役職</span><select
                                    name="posId"><option value="">選択してください。</option>
                                        <c:choose>
                                            <c:when test="${not empty positionList}">
                                                <c:forEach var="pos" items="${positionList}">
                                                    <option value="${pos.jobPositionId}"
                                                        ${pos.jobPositionId eq employee.jobPositionId ? 'selected' : ''}>${pos.jobPositionName}</option>
                                                </c:forEach>
                                            </c:when>
                                        <c:otherwise>
                                            <option value="" disabled>登録された役職はありません。</option>
                                            </c:otherwise>
                                    </c:choose></select></label> <label class="field"><span>内・外国人</span><select
                                    name="foreignYn"><option value="">選択してください。</option>
                                        <option value="N"
                                            ${employee.foreignYn eq 'N' ? 'selected' : ''}>内国人</option>
                                        <option value="Y"
                                            ${employee.foreignYn eq 'Y' ? 'selected' : ''}>外国人</option></select></label><label
                                    class="field"><span>住民番号</span><input name="juminNo"
                                    value="<c:out value='${employee.juminNo}' />"></label>
                                <div class="field field--wide">
                                    <span>住所</span>
                                    <div class="address-row">
                                        <input name="zipCode" value="${employee.zipCode}"
                                            placeholder="郵便番号"><input name="address"
                                            value="<c:out value='${employee.address}' />"
                                            placeholder="住所">
                                    </div>
                                </div>
                                <label class="field"><span>電話番号</span><input
                                    name="telNo" value="${employee.telNo}"></label><label
                                    class="field"><span>携帯電話</span><input name="mobileNo"
                                    value="${employee.mobileNo}"></label><label class="field"><span>メール</span><input
                                    type="email" name="email" value="${employee.email}"></label><label
                                    class="field"><span>SNS</span><input name="snsAddress"
                                    value="${employee.snsAddress}"></label><label
                                    class="field field--wide"><span>その他</span> <textarea
                                        name="memo"><c:out value="${employee.memo}" /></textarea></label>
                            </div>
                        </section>

                        <div class="part-divider">社員情報2</div>
                        <section class="form-card" id="license">
                            <h2>資格・免許＆語学能力</h2>
                            <div class="sub-card-title">
                                <h3>資格＆免許</h3>
                                <div>
                                    <button name="action" value="addLicense" formnovalidate>追加する</button>
                                    <button name="action" value="deleteLicenses" formnovalidate>選択削除</button>
                                </div>
                            </div>
                            <div class="table-wrap">
                                <table>
                                    <thead>
                                        <tr>
                                            <th>選択</th>
                                            <th>資格/ライセンス</th>
                                            <th>取得日</th>
                                            <th>発行機関</th>
                                            <th>証明書番号</th>
                                            <th>備考</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach begin="0" end="${licenseRowCount - 1}" varStatus="row">
                                            <c:set var="item" value="${licenses[row.index]}" />
                                            <tr>
                                                <td><input type="checkbox" name="licenseDeleteIds"
                                                    value="${item.employeeLicenseId}"></td>
                                                <td><input name="licenses[${row.index}].licenseName"
                                                    value="<c:out value='${item.licName}' />"></td>
                                                <td><input type="date" lang="ja-JP"
                                                    name="licenses[${row.index}].acquireDate"
                                                    value="<fmt:formatDate value='${item.acqDate}' pattern='yyyy-MM-dd' />"></td>
                                                <td><input name="licenses[${row.index}].issuer"
                                                    value="${item.issuer}"></td>
                                                <td><input name="licenses[${row.index}].licenseNo"
                                                    value="${item.licenseNo}"></td>
                                                <td><input name="licenses[${row.index}].note"
                                                    value="${item.note}"></td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            <div class="sub-card-title">
                                <h3>語学能力</h3>
                                <div>
                                    <button name="action" value="addLanguage" formnovalidate>追加する</button>
                                    <button name="action" value="deleteLanguages" formnovalidate>選択削除</button>
                                </div>
                            </div>
                            <div class="table-wrap">
                                <table>
                                    <thead>
                                        <tr>
                                            <th>選択</th>
                                            <th>外国語名</th>
                                            <th>試験</th>
                                            <th>認定スコア</th>
                                            <th>取得日</th>
                                            <th>読解</th>
                                            <th>作文</th>
                                            <th>絵画</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach begin="0" end="${languageRowCount - 1}" varStatus="row">
                                        <c:set var="lang" value="${languages[row.index]}" />
                                        <tr>
                                            <td><input type="checkbox" name="languageDeleteIds"
                                                value="${lang.employeeLanguageId}"></td>
                                            <td><input name="languages[${row.index}].languageName"
                                                value="${lang.langName}"></td>
                                            <td><input name="languages[${row.index}].testName"
                                                value="${lang.testName}"></td>
                                            <td><input type="text" name="languages[${row.index}].score"
                                                value="${lang.score}" maxlength="50" inputmode="numeric"></td>
                                            <td><input type="date" lang="ja-JP" name="languages[${row.index}].acquireDate"
                                                value="<fmt:formatDate value='${lang.acqDate}' pattern='yyyy-MM-dd' />"></td>
                                            <c:forEach var="ability"
                                                items="${fn:split('reading,writing,speaking', ',')}">
                                                <td><select name="languages[${row.index}].${ability}"><option
                                                            value="">選択</option>
                                                <option value="상" ${(ability eq 'reading' and lang.readingLevel eq '상') or (ability eq 'writing' and lang.writingLevel eq '상') or (ability eq 'speaking' and lang.speakingLevel eq '상') ? 'selected' : ''}>上</option>
                                                <option value="중" ${(ability eq 'reading' and lang.readingLevel eq '중') or (ability eq 'writing' and lang.writingLevel eq '중') or (ability eq 'speaking' and lang.speakingLevel eq '중') ? 'selected' : ''}>中</option>
                                                <option value="하" ${(ability eq 'reading' and lang.readingLevel eq '하') or (ability eq 'writing' and lang.writingLevel eq '하') or (ability eq 'speaking' and lang.speakingLevel eq '하') ? 'selected' : ''}>下</option></select></td>
                                            </c:forEach>
                                        </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </section>

                        <section class="form-card" id="training">
                            <div class="card-title">
                                <h2>教育/訓練</h2>
                                <div>
                                    <button name="action" value="addTraining" formnovalidate>追加する</button>
                                    <button name="action" value="deleteTrainings" formnovalidate>選択削除</button>
                                </div>
                            </div>
                            <div class="table-wrap">
                                <table>
                                    <thead>
                                        <tr>
                                            <th>選択</th>
                                            <th>教育区分</th>
                                            <th>教育名</th>
                                            <th>教育期間（から）</th>
                                            <th>教育期間（まで）</th>
                                            <th>教育機関</th>
                                            <th>教育費</th>
                                            <th>払い戻し教育費</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach begin="0" end="${trainingRowCount - 1}" varStatus="row">
                                            <c:set var="item" value="${trainings[row.index]}" />
                                            <tr>
                                                <td><input type="checkbox" name="trainingDeleteIds"
                                                    value="${item.employeeTrainingId}"></td>
                                                <td><select name="trainings[${row.index}].trainingType"><option
                                                            value="">選択</option>
                                                        <c:forEach var="type" items="${trainingTypes}">
                                                            <option ${type eq item.trainType ? 'selected' : ''}>${type}</option>
                                                        </c:forEach></select></td>
                                                <td><input name="trainings[${row.index}].trainingName"
                                            value="${item.trainName}" maxlength="200"></td>
                                                <td><input type="date" lang="ja-JP"
                                                    name="trainings[${row.index}].startDate"
                                                    value="<fmt:formatDate value='${item.startDate}' pattern='yyyy-MM-dd' />"></td>
                                                <td><input type="date" lang="ja-JP"
                                                    name="trainings[${row.index}].endDate"
                                                    value="<fmt:formatDate value='${item.endDate}' pattern='yyyy-MM-dd' />"></td>
                                                <td><input name="trainings[${row.index}].institution"
                                            value="${item.trainInstitute}" maxlength="150"></td>
                                                <td><label class="money-cell"><input
                                                        type="number" name="trainings[${row.index}].trainingCost"
                                                        value="${item.trainCost}"><span>円</span></label></td>
                                                <td><label class="money-cell"><input
                                                        type="number" name="trainings[${row.index}].refundCost"
                                                        value="${item.refundCost}"><span>円</span></label></td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </section>

                        <section class="form-card" id="reward-punish">
                            <div class="card-title">
                                <h2>罰</h2>
                                <div>
                                    <button name="action" value="addRewardPunish" formnovalidate>追加する</button>
                                    <button name="action" value="deleteRewardPunishes" formnovalidate>選択削除</button>
                                </div>
                            </div>
                            <div class="table-wrap">
                                <table>
                                    <thead>
                                        <tr>
                                            <th>選択</th>
                                            <th>区分</th>
                                            <th>商罰名</th>
                                            <th>商罰権者</th>
                                            <th>相反日</th>
                                            <th>商罰内容</th>
                                            <th>備考</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach begin="0" end="${rewardRowCount - 1}" varStatus="row">
                                            <c:set var="item" value="${rewardPunishes[row.index]}" />
                                            <tr>
                                                <td><input type="checkbox" name="rewardDeleteIds"
                                                    value="${item.employeeRewardDisciplineId}"></td>
                                                <td><select name="rewardPunishes[${row.index}].rpType"><option
                                                            value="">選択</option>
                                                        <c:forEach var="type" items="${rewardTypes}">
                                                            <option ${type eq item.rpType ? 'selected' : ''}>${type}</option>
                                                        </c:forEach></select></td>
                                                <td><input name="rewardPunishes[${row.index}].rpName"
                                            value="${item.rpName}" maxlength="100"></td>
                                                <td><input name="rewardPunishes[${row.index}].grantor"
                                            value="${item.rpAuthority}" maxlength="100"></td>
                                                <td><input type="date" lang="ja-JP"
                                                    name="rewardPunishes[${row.index}].rpDate"
                                                    value="<fmt:formatDate value='${item.rpDate}' pattern='yyyy-MM-dd' />"></td>
                                                <td><input name="rewardPunishes[${row.index}].content"
                                            value="${item.rpContent}" maxlength="500"></td>
                                                <td><input name="rewardPunishes[${row.index}].note"
                                            value="${item.note}" maxlength="300"></td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </section>

                        <section class="form-card" id="appointment">
                            <div class="card-title">
                                <h2>発令</h2>
                                <div>
                                    <button name="action" value="addAppointment" formnovalidate>追加する</button>
                                    <button name="action" value="deleteAppointments" formnovalidate>選択削除</button>
                                </div>
                            </div>
                            <div class="table-wrap">
                                <table>
                                    <thead>
                                        <tr>
                                            <th>選択</th>
                                            <th>発令区分</th>
                                            <th>発令日</th>
                                            <th>部署</th>
                                            <th>役職</th>
                                            <th>役職</th>
                                            <th>備考</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach begin="0" end="${appointmentRowCount - 1}" varStatus="row">
                                            <c:set var="item" value="${appointments[row.index]}" />
                                            <tr>
                                                <td><input type="checkbox" name="appointmentDeleteIds"
                                                    value="${item.employeeAppointmentId}"></td>
                                                <td><select
                                                    name="appointments[${row.index}].appointmentType"><option
                                                            value="">選択</option>
                                                        <c:forEach var="type" items="${appointmentTypes}">
                                                            <option ${type eq item.appType ? 'selected' : ''}>${type}</option>
                                                        </c:forEach></select></td>
                                                <td><input type="date" lang="ja-JP"
                                                    name="appointments[${row.index}].appointmentDate"
                                                    value="<fmt:formatDate value='${item.appDate}' pattern='yyyy-MM-dd' />"></td>
                                                <td><input name="appointments[${row.index}].deptName"
                                            value="${item.departmentName}" maxlength="100"></td>
                                                <td><input name="appointments[${row.index}].posName"
                                            value="${item.jobPositionName}" maxlength="100"></td>
                                                <td><input name="appointments[${row.index}].dutyName"
                                            value="${item.jobTitleDuty}" maxlength="300"></td>
                                                <td><input name="appointments[${row.index}].note"
                                            value="${item.note}" maxlength="300"></td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </section>

                        <section class="form-card" id="recommendation">
                            <h2>おすすめ/身元保証</h2>
                            <div class="sub-card-title">
                                <h3>おすすめ</h3>
                            </div>
                            <div class="table-wrap">
                                <table>
                                    <thead>
                                        <tr>
                                            <th>氏名</th>
                                            <th>関係</th>
                                            <th>会社名</th>
                                            <th>役職</th>
                                            <th>電話番号</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td><input name="recommender.recommenderName"
                                                value="${recommender.recommenderName}"></td>
                                            <td><input name="recommender.relation"
                                                value="${recommender.relation}"></td>
                                            <td><input name="recommender.companyName"
                                                value="${recommender.companyName}"></td>
                                            <td><input name="recommender.positionName"
                                                value="${recommender.positionName}"></td>
                                            <td><input name="recommender.telNo"
                                                value="${recommender.telNo}"></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div class="sub-card-title">
                                <h3>保証保険</h3>
                            </div>
                            <div class="table-wrap">
                                <table>
                                    <thead>
                                        <tr>
                                            <th>加入機関</th>
                                            <th>保険番号</th>
                                            <th>保険金額</th>
                                            <th>登録日</th>
                                            <th>有効期限</th>
                                            <th>備考</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td><input name="suretyInsurance.institution"
                                                value="${suretyInsurance.providerName}"></td>
                                            <td><input name="suretyInsurance.insuranceNo"
                                                value="${suretyInsurance.insuranceNo}"></td>
                                            <td><label class="money-cell"><input
                                                    type="number" name="suretyInsurance.amount"
                                                    value="${suretyInsurance.insuranceAmt}"><span>円</span></label></td>
                                            <td><input type="date" lang="ja-JP" name="suretyInsurance.startDate"
                                                value="<fmt:formatDate value='${suretyInsurance.signupDate}' pattern='yyyy-MM-dd' />"></td>
                                            <td><input type="date" lang="ja-JP" name="suretyInsurance.endDate"
                                                value="<fmt:formatDate value='${suretyInsurance.expireDate}' pattern='yyyy-MM-dd' />"></td>
                                            <td><input name="suretyInsurance.note"
                                                value="${suretyInsurance.note}"></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div class="sub-card-title">
                                <h3>身元保証人</h3>
                            </div>
                            <div class="table-wrap">
                                <table>
                                    <thead>
                                        <tr>
                                            <th>氏名</th>
                                            <th>関係</th>
                                            <th>住民登録番号</th>
                                            <th>保証金額</th>
                                            <th>保証日</th>
                                            <th>有効期限</th>
                                            <th>電話番号</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td><input name="guarantor.guarantorName"
                                                value="${guarantor.guarantorName}"></td>
                                            <td><input name="guarantor.relation"
                                                value="${guarantor.relation}"></td>
                                            <td><input name="guarantor.juminNo"
                                                value="${guarantor.juminNo}"></td>
                                            <td><label class="money-cell"><input
                                                    type="number" name="guarantor.amount"
                                                    value="${guarantor.guaranteeAmt}"><span>円</span></label></td>
                                            <td><input type="date" lang="ja-JP" name="guarantor.startDate"
                                                value="<fmt:formatDate value='${guarantor.guaranteeDate}' pattern='yyyy-MM-dd' />"></td>
                                            <td><input type="date" lang="ja-JP" name="guarantor.endDate"
                                                value="<fmt:formatDate value='${guarantor.expireDate}' pattern='yyyy-MM-dd' />"></td>
                                            <td><input name="guarantor.telNo"
                                                value="${guarantor.telNo}"></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </section>

                        <section class="form-card" id="retirement">
                            <h2>退職</h2>
                            <div class="table-wrap">
                                <table>
                                    <thead>
                                        <tr>
                                            <th>退職区分</th>
                                            <th>退職日</th>
                                            <th>退職理由</th>
                                            <th>退職後の連絡先</th>
                                            <th>退職金</th>
                                            <th>退職金明細書</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td><select name="retireType"><option value="">選択</option>
                                                    <c:forEach var="type" items="${retireTypes}">
                                                        <option ${type eq employee.retireType ? 'selected' : ''}>${type}</option>
                                                    </c:forEach></select></td>
                                            <td><input type="date" lang="ja-JP" name="retireDate"
                                                value="<fmt:formatDate value='${employee.retireDate}' pattern='yyyy-MM-dd' />"></td>
                                            <td><input name="retireReason"
                                                value="${employee.retireReason}"></td>
                                            <td><input name="afterRetireContact"
                                                value="${employee.afterRetireContact}"></td>
                                            <td><label class="money-cell"><input
                                                    value="<fmt:formatNumber value='${latestRetirementBenefit.netPayment}' pattern='#,##0' />" readonly><span>円</span></label></td>
                                            <td><a class="table-button"
                                                href="${pageContext.request.contextPath}/retirement/payslip.do?empId=${employee.employeeId}">仕様書
                                                    ダウンロード</a></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </section>

                        <div class="form-actions">
                            <button class="button button--primary" name="action" value="save">保存する</button>
                            <a class="button"
                                href="${pageContext.request.contextPath}/settings/register2.do?empId=${employee.employeeId}">キャンセル</a>
                        </div>
                    </div>
                </div>
                <div id="photo-upload-modal" class="upload-modal" role="dialog"
                    aria-modal="true" aria-labelledby="photo-upload-title">
                    <a class="upload-modal__backdrop" href="${pageContext.request.contextPath}/settings/register2.do?empId=${employee.employeeId}" aria-label="閉じる"></a>
                    <div class="upload-modal__panel">
                        <div class="upload-modal__title">
                            <h2 id="photo-upload-title">画像を登録する</h2>
                            <a href="${pageContext.request.contextPath}/settings/register2.do?empId=${employee.employeeId}" aria-label="閉じる">×</a>
                        </div>
                        <div class="upload-modal__body">
                            <div class="photo-preset-grid">
                                <label><input type="radio" name="photoPreset" value="01"><img src="${pageContext.request.contextPath}/images/settings/employee-presets/employee-01.png" alt="イエス社員の写真1"><span>写真1</span></label>
                                <label><input type="radio" name="photoPreset" value="02"><img src="${pageContext.request.contextPath}/images/settings/employee-presets/employee-02.png" alt="イエス社員の写真2"><span>写真2</span></label>
                                <label><input type="radio" name="photoPreset" value="03"><img src="${pageContext.request.contextPath}/images/settings/employee-presets/employee-03.png" alt="イエス社員の写真3"><span>写真3</span></label>
                                <label><input type="radio" name="photoPreset" value="04"><img src="${pageContext.request.contextPath}/images/settings/employee-presets/employee-04.png" alt="イエス社員の写真4"><span>写真4</span></label>
                                <label><input type="radio" name="photoPreset" value="05"><img src="${pageContext.request.contextPath}/images/settings/employee-presets/employee-05.png" alt="イエス社員の写真5"><span>写真5</span></label>
                            </div>
                        </div>
                        <button class="upload-modal__confirm" name="action"
                            value="savePhoto">確認</button>
                    </div>
                </div>
            </form>
        </div>
    </main>
    <c:if test="${not empty message}">
        <div class="employee-setting-alert" role="alertdialog" aria-modal="true" aria-labelledby="employee-setting-alert-message">
            <a class="employee-setting-alert__backdrop" href="${pageContext.request.contextPath}/settings/register2.do?empId=${employee.employeeId}&amp;dismissMessage=true" aria-label="確認"></a>
            <div class="employee-setting-alert__panel">
                <p id="employee-setting-alert-message"><ui:message-label value="${message}" /></p>
                <a href="${pageContext.request.contextPath}/settings/register2.do?empId=${employee.employeeId}&amp;dismissMessage=true">確認</a>
            </div>
        </div>
        <c:remove var="message" scope="session" />
    </c:if>
    <%@ include file="/WEB-INF/view/common/footer.jspf"%>
</body>
</html>
