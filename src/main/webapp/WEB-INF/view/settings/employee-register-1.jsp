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
<title>基本設定 > ${employee.employeeId gt 0 ? '社員情報修正' : '社員登録1'}</title>
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
            <c:set var="relations"
                value="${fn:split('配偶者,息子,娘,父,母,兄弟,姉妹,義父,義母,祖父,祖母,孫息子,孫娘', ',')}" />
            <c:set var="schoolTypes"
                value="${fn:split('小学校,中学校,高等学校,大学,修士課程,博士課程', ',')}" />
            <c:set var="schoolStates" value="${fn:split('卒業,修了,中退,在学中', ',')}" />
            <c:set var="banks"
                value="${fn:split('三菱UFJ銀行,三井住友銀行,みずほ銀行,りそな銀行,ゆうちょ銀行,楽天銀行,住信SBIネット銀行,PayPay銀行,ソニー銀行,auじぶん銀行,イオン銀行,セブン銀行,ローソン銀行,横浜銀行,千葉銀行,静岡銀行,福岡銀行,西日本シティ銀行,北海道銀行,京都銀行', ',')}" />

            <header class="page-heading">
                <div>
                    <p>基本設定</p>
                    <h1>${employee.employeeId gt 0 ? '社員情報修正' : '社員登録'}</h1>
                </div>
                <p class="page-heading__notice">
                    <strong>*</strong> 表示は必須入力です。
                </p>
            </header>
            <form
                action="${pageContext.request.contextPath}/settings/register1.do"
                method="post" enctype="multipart/form-data">
                <input type="hidden" name="empId"
                    value="<c:out value='${employee.employeeId}' />">
                <input type="hidden" name="dependentRowCount" value="${dependentRowCount}">
                <input type="hidden" name="educationRowCount" value="${educationRowCount}">
                <input type="hidden" name="careerRowCount" value="${careerRowCount}">
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
                                    <span>写真を登録してください</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="summary-actions">
                            <a href="#photo-upload-modal">登録</a>
                            <input class="photo-preset-confirmed" type="radio" id="photoConfirmed01" name="photoPreset" value="01" ${draftPhotoPreset eq '01' ? 'checked' : ''}>
                            <input class="photo-preset-confirmed" type="radio" id="photoConfirmed02" name="photoPreset" value="02" ${draftPhotoPreset eq '02' ? 'checked' : ''}>
                            <input class="photo-preset-confirmed" type="radio" id="photoConfirmed03" name="photoPreset" value="03" ${draftPhotoPreset eq '03' ? 'checked' : ''}>
                            <input class="photo-preset-confirmed" type="radio" id="photoConfirmed04" name="photoPreset" value="04" ${draftPhotoPreset eq '04' ? 'checked' : ''}>
                            <input class="photo-preset-confirmed" type="radio" id="photoConfirmed05" name="photoPreset" value="05" ${draftPhotoPreset eq '05' ? 'checked' : ''}>
                            <input class="photo-preset-clear" type="radio" id="photoPresetNone" name="photoPreset" value="">
                            <label class="preview-photo-delete" for="photoPresetNone">削除</label>
                            <c:choose>
                                <c:when test="${not empty employee.photoPath}"><button class="saved-photo-delete" type="submit" name="action" value="deletePhoto" formnovalidate>削除</button></c:when>
                                <c:otherwise><span class="summary-action--disabled saved-photo-delete">削除</span></c:otherwise>
                            </c:choose>
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
                                    <fmt:formatDate value="${employee.joinDate}"
                                        pattern="yyyy/MM/dd" />
                                </dd>
                            </div>
                        </dl>
                        <nav class="section-shortcuts" aria-label="社員情報のショートカット">
                            <section>
                                <p class="shortcut-title">
                                    <span>社員情報</span><em>01</em>
                                </p>
                                <div class="section-links">
                                    <a href="#salary-insurance">給与/ 4大保険</a><a href="#dependents">扶養家族</a><a
                                        href="#education">学歴</a><a href="#career">職歴</a><a
                                        href="#military">兵役</a>
                                </div>
                            </section>
                            <section>
                                <p class="shortcut-title">
                                    <span>社員情報</span><em>02</em>
                                </p>
                                <div class="section-links">
                                    <c:choose>
                                        <%-- 社員番号が存在し、0より大きい場合（DBに保存されている通常の社員） --%>
                                        <c:when
                                            test="${not empty employee.employeeId and employee.employeeId > 0}">
                                            <a
                                                href="${pageContext.request.contextPath}/settings/register2.do?empId=<c:out value='${employee.employeeId}' />#license">資格/ライセンス</a>
                                            <a
                                                href="${pageContext.request.contextPath}/settings/register2.do?empId=<c:out value='${employee.employeeId}' />#training">教育/訓練</a>
                                            <a
                                                href="${pageContext.request.contextPath}/settings/register2.do?empId=<c:out value='${employee.employeeId}' />#reward-punish">上罰</a>
                                            <a
                                                href="${pageContext.request.contextPath}/settings/register2.do?empId=<c:out value='${employee.employeeId}' />#appointment">発令</a>
                                            <a
                                                href="${pageContext.request.contextPath}/settings/register2.do?empId=<c:out value='${employee.employeeId}' />#recommendation">おすすめ/アイデンティティ保証</a>
                                            <a
                                                href="${pageContext.request.contextPath}/settings/register2.do?empId=<c:out value='${employee.employeeId}' />#retirement">退職</a>
                                        </c:when>
                                        <%-- 社員番号がない場合（新規登録中の状態） --%>
                                        <c:otherwise>
                                            <a style="opacity: 0.5; cursor: not-allowed;"
                                                title="基本情報を先に保存してください。">資格/ライセンス</a>
                                            <a style="opacity: 0.5; cursor: not-allowed;"
                                                title="基本情報を先に保存してください。">教育/訓練</a>
                                            <a style="opacity: 0.5; cursor: not-allowed;"
                                                title="基本情報を先に保存してください。">罰</a>
                                            <a style="opacity: 0.5; cursor: not-allowed;"
                                                title="基本情報を先に保存してください。">発令</a>
                                            <a style="opacity: 0.5; cursor: not-allowed;"
                                                title="基本情報を先に保存してください。">おすすめ/身元保証</a>
                                            <a style="opacity: 0.5; cursor: not-allowed;"
                                                title="基本情報を先に保存してください。">退職</a>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </section>
                        </nav>
                    </aside>

                    <div class="employee-form">
                        <section class="form-card" id="basic-info">
                            <h2>基本情報</h2>
                            <div class="form-grid">
                                <label class="field"><span>社員番号</span><input
                                    name="empNo" value="<c:out value='${not empty employee.empNo ? employee.empNo : anticipatedEmpNo}' />"
                                    readonly></label> <label class="field"><span><b>*</b>
                                        雇用形態</span><select name="empType" required><option value="">選択してください。</option>
                                        <c:forEach var="type" items="${employmentTypes}">
                                            <option value="${type}"
                                            ${type eq employee.empType ? 'selected' : ''}><c:choose><c:when test="${type eq '정규직'}">正社員</c:when><c:when test="${type eq '계약직'}">契約社員</c:when><c:when test="${type eq '임시직'}">臨時社員</c:when><c:when test="${type eq '파견직'}">派遣社員</c:when><c:when test="${type eq '위촉직'}">委嘱社員</c:when><c:when test="${type eq '일용직'}">日雇い</c:when><c:otherwise><c:out value="${type}" /></c:otherwise></c:choose></option>
                                        </c:forEach></select></label> <label class="field"><span><b>*</b> 氏名</span><input
                                    name="empNameKr"
                                    value="<c:out value='${employee.empNameKr}' />" required
                                    maxlength="50"></label> <label class="field"><span>氏名（英語）</span><input
                                    name="empNameEn"
                                    value="<c:out value='${employee.empNameEn}' />" maxlength="100"></label>
                                <label class="field"><span><b>*</b> 入社日</span><input
                                    type="date" lang="ja-JP" name="joinDate"
                                    value="<fmt:formatDate value='${employee.joinDate}' pattern='yyyy-MM-dd' />"
                                    required></label> <label class="field"><span>退社日</span><input
                                    type="date" lang="ja-JP"
                                    value="<fmt:formatDate value='${employee.retireDate}' pattern='yyyy-MM-dd' />"
                                    readonly></label> <label class="field"><span>部署</span><select
                                    name="deptId"><option value="">選択してください。</option>
                                        <c:choose>
                                            <c:when test="${not empty departmentList}">
                                                <c:forEach var="dept" items="${departmentList}">
                                                    <option value="${dept.departmentId}"
                                                        ${dept.departmentId eq employee.departmentId ? 'selected' : ''}><c:out
                                                            value="${dept.departmentName}" /></option>
                                                </c:forEach>
                                            </c:when>
                                        <c:otherwise>
                                            <option value="" disabled>登録された部署はありません。</option>
                                            </c:otherwise>
                                        </c:choose></select></label> <label class="field"><span>役職</span><select
                                    name="posId"><option value="">選択してください。</option>
                                        <c:choose>
                                            <c:when test="${not empty positionList}">
                                                <c:forEach var="pos" items="${positionList}">
                                                    <option value="${pos.jobPositionId}"
                                                        ${pos.jobPositionId eq employee.jobPositionId ? 'selected' : ''}><c:out
                                                            value="${pos.jobPositionName}" /></option>
                                                </c:forEach>
                                            </c:when>
                                        <c:otherwise>
                                            <option value="" disabled>登録された役職はありません。</option>
                                            </c:otherwise>
                                    </c:choose></select></label> <label class="field"><span><b>*</b> 内・外国人</span><select
                                    name="foreignYn" required><option value="">選択してください。</option>
                                        <option value="N"
                                            ${employee.foreignYn eq 'N' ? 'selected' : ''}>内国人</option>
                                        <option value="Y"
                                            ${employee.foreignYn eq 'Y' ? 'selected' : ''}>外国人</option></select></label> <label
                                    class="field"><span>住民番号</span><input name="juminNo"
                                    value="<c:out value='${employee.juminNo}' />" maxlength="100"></label>
                                <div class="field field--wide">
                                    <span>住所</span>
                                    <div class="address-row">
                                        <input name="zipCode"
                                            value="<c:out value='${employee.zipCode}' />" maxlength="10"
                                            placeholder="郵便番号"><input name="address"
                                            value="<c:out value='${employee.address}' />" maxlength="500"
                                            placeholder="住所">
                                    </div>
                                </div>
                                <label class="field"><span>電話番号</span><input type="tel"
                                    name="telNo" value="<c:out value='${employee.telNo}' />"
                                    maxlength="30"></label> <label class="field"><span>携帯電話</span><input
                                    type="tel" name="mobileNo"
                                    value="<c:out value='${employee.mobileNo}' />" maxlength="30"></label>
                                <label class="field"><span>メール</span><input type="email"
                                    name="email" value="<c:out value='${employee.email}' />"
                                    maxlength="150"></label> <label class="field"><span>SNS</span><input
                                    name="snsAddress"
                                    value="<c:out value='${employee.snsAddress}' />"
                                    maxlength="200"></label> <label class="field field--wide"><span>その他</span>
                                    <textarea name="memo" maxlength="1000"><c:out
                                            value="${employee.memo}" /></textarea></label>
                            </div>
                        </section>

                        <div class="part-divider">社員情報1</div>
                        <section class="form-card" id="salary-insurance">
                            <h2>給与/ 4大保険</h2>
                            <h3>給与</h3>
                            <div class="detail-rows">
                                <div class="detail-row detail-row--wide">
                                    <span><b>*</b> 四大保険</span>
                                    <div class="option-line">
                                        <label><input type="checkbox" name="npYn" value="Y"
                                            ${empty employee.npYn or employee.npYn eq 'Y' ? 'checked' : ''}>
                                            国民年金</label><label><input type="checkbox" name="hiYn"
                                            value="Y"
                                            ${empty employee.hiYn or employee.hiYn eq 'Y' ? 'checked' : ''}>
                                            健康保険</label><label>減免 <select name="hiReduceRate"><option
                                                    value="0">選択</option>
                                                <option>10</option>
                                                <option>30</option>
                                                <option>50</option>
                                                <option>60</option></select>%
                                        </label><label><input type="checkbox" name="ltciYn" value="Y"
                                            ${empty employee.ltciYn or employee.ltciYn eq 'Y' ? 'checked' : ''}>
                                            長期療養保険を含む</label><label>減免 <select name="ltciReduceRate"><option
                                                    value="0">選択</option>
                                                <option>30</option>
                                                <option>50</option>
                                                <option>60</option></select>%
                                        </label><label><input type="checkbox" name="eiYn" value="Y"
                                            ${empty employee.eiYn or employee.eiYn eq 'Y' ? 'checked' : ''}>
                                            雇用保険</label>
                                    </div>
                                </div>
                                <div class="detail-row detail-row--wide">
                                    <span><b>*</b> 給与所得税</span>
                                    <div class="option-line option-line--tax">
                                        <div class="tax-line">
                                            <label><input type="radio" name="incomeType"
                                                value="근로소득자"
                                                ${empty employee.incomeType or employee.incomeType eq '근로소득자' ? 'checked' : ''}>
                                                給与所得者（簡易税額表）</label><label>税額 <select
                                            name="incomeTaxRate"><option value="80" ${employee.incomeTaxRate eq 80 ? 'selected' : ''}>80</option>
                                                <option value="100" ${empty employee or employee.incomeTaxRate eq 100 or employee.incomeTaxRate eq 0 ? 'selected' : ''}>100</option>
                                                <option value="120" ${employee.incomeTaxRate eq 120 ? 'selected' : ''}>120</option></select>%
                                            </label><label><input type="checkbox" name="youthTaxReduceYn"
                                                value="Y"
                                                ${employee.youthTaxReduceYn eq 'Y' ? 'checked' : ''}>
                                                中小企業青年所得税の減免</label><select name="youthTaxRate"><option
                                                value="0" ${empty employee.youthTaxRate or employee.youthTaxRate eq 0 ? 'selected' : ''}>選択</option>
                                            <option value="50" ${employee.youthTaxRate eq 50 ? 'selected' : ''}>50</option>
                                            <option value="70" ${employee.youthTaxRate eq 70 ? 'selected' : ''}>70</option>
                                            <option value="90" ${employee.youthTaxRate eq 90 ? 'selected' : ''}>90</option></select>
                                        </div>
                                        <div class="tax-line">
                                            <label><input type="radio" name="incomeType"
                                            value="사업소득자" ${employee.incomeType eq '사업소득자' ? 'checked' : ''}> 事業所得者（3.3％）</label><label><input
                                            type="radio" name="incomeType" value="일용직" ${employee.incomeType eq '일용직' ? 'checked' : ''}>
                                                日雇い(2.97%)</label><label><input type="radio"
                                            name="incomeType" value="기타소득자" ${employee.incomeType eq '기타소득자' ? 'checked' : ''}> その他所得者（8.8％）</label><label><input
                                            type="radio" name="incomeType" value="근로/사업소득자" ${employee.incomeType eq '근로/사업소득자' ? 'checked' : ''}>
                                                給与・事業所得者</label><label><input type="radio" name="incomeType"
                                                value="면제" ${employee.incomeType eq '면제' ? 'checked' : ''}> 免除</label>
                                        </div>
                                    </div>
                                </div>
                                <div class="detail-row detail-row--wide durunuri-setting">
                                    <span><span class="durunuri-label-text"><b>*</b> ドゥルヌリ<br>社会保険支援</span>
                                        <label class="durunuri-separate"><input type="checkbox" name="durunuriSeparateYn" value="Y" ${employee.durunuriSeparateYn eq 'Y' ? 'checked' : ''}> 分離設定</label>
                                    </span>
                                    <div class="durunuri-options">
                                        <div class="option-line">
                                            <label><input type="radio" name="durunuriRate" value="0" ${empty employee.durunuriSeparateYn or employee.durunuriSeparateYn eq 'N' and employee.durunuriNpRate eq 0 ? 'checked' : ''}> 該当なし</label>
                                            <label><input type="radio" name="durunuriRate" value="80" ${employee.durunuriSeparateYn eq 'N' and employee.durunuriNpRate eq 80 ? 'checked' : ''}> 新規加入者（80％サポート）</label>
                                            <label><input type="radio" name="durunuriRate" value="90" ${employee.durunuriSeparateYn eq 'N' and employee.durunuriNpRate eq 90 ? 'checked' : ''}> 新規加入者（90％サポート）</label>
                                        </div>
                                        <div class="durunuri-separate-fields">
                                            <div class="option-line"><strong>国民年金</strong>
                                                <label><input type="radio" name="durunuriNpRate" value="0" ${empty employee.durunuriNpRate or employee.durunuriNpRate eq 0 ? 'checked' : ''}> 該当なし</label>
                                                <label><input type="radio" name="durunuriNpRate" value="80" ${employee.durunuriNpRate eq 80 ? 'checked' : ''}> 80％サポート</label>
                                                <label><input type="radio" name="durunuriNpRate" value="90" ${employee.durunuriNpRate eq 90 ? 'checked' : ''}> 90％サポート</label>
                                            </div>
                                            <div class="option-line"><strong>雇用保険</strong>
                                                <label><input type="radio" name="durunuriEiRate" value="0" ${empty employee.durunuriEiRate or employee.durunuriEiRate eq 0 ? 'checked' : ''}> 該当なし</label>
                                                <label><input type="radio" name="durunuriEiRate" value="80" ${employee.durunuriEiRate eq 80 ? 'checked' : ''}> 80％サポート</label>
                                                <label><input type="radio" name="durunuriEiRate" value="90" ${employee.durunuriEiRate eq 90 ? 'checked' : ''}> 90％サポート</label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="detail-row">
                                    <span><b>*</b> 基本給/日給</span><label><input type="number"
                                        min="0" name="basicPay" required
                                        value="<c:out value='${employee.basicPay}' />">円</label>
                                </div>
                                <div class="detail-row">
                                    <span>国民年金基準所得月額</span><label><input type="number"
                                        min="0" name="npMonthlyBase"
                                        value="<c:out value='${employee.npMonthlyBase}' />">円</label>
                                </div>
                                <div class="detail-row">
                                    <span>健康保険報酬月額</span><label><input type="number"
                                        min="0" name="hiMonthlyBase"
                                        value="<c:out value='${employee.hiMonthlyBase}' />">円</label>
                                </div>
                                <div class="detail-row">
                                    <span>雇用保険報酬月額</span><label><input type="number"
                                        min="0" name="eiMonthlyBase"
                                        value="<c:out value='${employee.eiMonthlyBase}' />">円</label>
                                </div>
                                <div class="detail-row detail-row--wide">
                                    <span>給与口座</span>
                                    <div class="account-row">
                                        <select name="bankName"><option value="">選択してください</option>
                                            <c:forEach var="bank" items="${banks}">
                                                <option value="${bank}"
                                                    ${bank eq employee.bankName ? 'selected' : ''}>${bank}</option>
                                            </c:forEach></select><input name="accountNo"
                                            value="<c:out value='${employee.accountNo}' />"
                                            maxlength="100" placeholder="口座番号">
                                    </div>
                                </div>
                            </div>
                            <h3>4大保険</h3>
                            <div class="table-wrap">
                                <table>
                                    <thead>
                                        <tr>
                                            <th>区分</th>
                                            <th>記号番号</th>
                                            <th>取得日</th>
                                            <th>喪失日</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="insurance" items="${insuranceRows}">
                                            <tr>
                                                <th><c:choose>
                                                    <c:when test="${insurance.insuranceType eq '국민연금'}">国民年金</c:when>
                                                    <c:when test="${insurance.insuranceType eq '건강보험'}">健康保険</c:when>
                                                    <c:when test="${insurance.insuranceType eq '고용보험'}">雇用保険</c:when>
                                                    <c:when test="${insurance.insuranceType eq '산재보험'}">労災保険</c:when>
                                                    <c:otherwise><c:out value="${insurance.insuranceType}" /></c:otherwise>
                                                </c:choose></th>
                                                <td><input name="insuranceNo"
                                                    value="<c:out value='${insurance.symbolNo}' />"></td>
                                                <td><input type="date" lang="ja-JP" name="insuranceStartDate"
                                                    value="<fmt:formatDate value='${insurance.acquireDate}' pattern='yyyy-MM-dd' />"></td>
                                                <td><input type="date" lang="ja-JP" name="insuranceEndDate"
                                                    value="<fmt:formatDate value='${insurance.lossDate}' pattern='yyyy-MM-dd' />"></td>
                                            </tr>
                                        </c:forEach>
                                        <c:if test="${empty insuranceRows}">
                                            <c:forEach var="type"
                                                items="${fn:split('국민연금,건강보험,고용보험,산재보험', ',')}">
                                                <tr>
                                                    <th><c:choose>
                                                        <c:when test="${type eq '국민연금'}">国民年金</c:when>
                                                        <c:when test="${type eq '건강보험'}">健康保険</c:when>
                                                        <c:when test="${type eq '고용보험'}">雇用保険</c:when>
                                                        <c:when test="${type eq '산재보험'}">労災保険</c:when>
                                                        <c:otherwise><c:out value="${type}" /></c:otherwise>
                                                    </c:choose></th>
                                                    <td><input name="insuranceNo"></td>
                                                    <td><input type="date" lang="ja-JP" name="insuranceStartDate"></td>
                                                    <td><input type="date" lang="ja-JP" name="insuranceEndDate"></td>
                                                </tr>
                                            </c:forEach>
                                        </c:if>
                                    </tbody>
                                </table>
                            </div>
                        </section>

                        <section class="form-card" id="dependents">
                            <div class="card-title">
                                <h2>扶養家族</h2>
                                <div>
                                    <button name="action" value="addDependent" formnovalidate>追加する</button>
                                    <button name="action" value="deleteDependents" formnovalidate>選択削除</button>
                                </div>
                            </div>
                            <div class="table-wrap table-wrap--wide">
                                <table class="dependent-table">
                                    <thead>
                                        <tr>
                                            <th>選択</th>
                                            <th><b>*</b> 関係</th>
                                            <th><b>*</b> 氏名</th>
                                            <th>区分</th>
                                            <th>住民登録番号</th>
                                            <th>障害有無</th>
                                            <th>人的控除</th>
                                            <th>健康保険</th>
                                            <th>同居有無</th>
                                            <th>給与所得税</th>
                                            <th>20歳未満の子供</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach begin="0" end="${dependentRowCount - 1}" varStatus="row">
                                            <c:set var="dep" value="${dependents[row.index]}" />
                                            <tr>
                                                <td><input type="checkbox" name="dependentDeleteIds"
                                                    value="${dep.employeeDependentId}"></td>
                                                <td><select name="dependents[${row.index}].relation"><option
                                                            value="">選択</option>
                                                        <c:forEach var="rel" items="${relations}">
                                                            <option ${rel eq dep.relation ? 'selected' : ''}>${rel}</option>
                                                        </c:forEach></select></td>
                                                <td><input name="dependents[${row.index}].depName"
                                                    value="<c:out value='${dep.depName}' />"></td>
                                                <td><select name="dependents[${row.index}].nationality"><option
                                                            value="">選択</option>
                                                        <option value="내국인" ${dep.nationalType eq '내국인' ? 'selected' : ''}>内国人</option>
    													<option value="외국인" ${dep.nationalType eq '외국인' ? 'selected' : ''}>外国人</option></select></td>
                                                <td><input name="dependents[${row.index}].juminNo"
                                                    value="<c:out value='${dep.juminNo}' />"></td>
                                                <td><input type="checkbox"
                                                    name="dependents[${row.index}].disabledYn" value="Y"
                                                    ${dep.disabledYn eq 'Y' ? 'checked' : ''}></td>
                                                <td><input type="checkbox"
                                                    name="dependents[${row.index}].deductionYn" value="Y"
                                                    ${dep.basicDeductYn eq 'Y' ? 'checked' : ''}></td>
                                                <td><input type="checkbox"
                                                    name="dependents[${row.index}].healthYn" value="Y"
                                                    ${dep.healthInsYn eq 'Y' ? 'checked' : ''}></td>
                                                <td><input type="checkbox"
                                                    name="dependents[${row.index}].cohabitYn" value="Y"
                                                    ${dep.cohabitYn eq 'Y' ? 'checked' : ''}></td>
                                                <td><input type="checkbox"
                                                    name="dependents[${row.index}].incomeTaxYn" value="Y"
                                                    ${dep.incomeTaxYn eq 'Y' ? 'checked' : ''}></td>
                                                <td><input type="checkbox"
                                                    name="dependents[${row.index}].childYn" value="Y"
                                                    ${dep.childUnder20Yn eq 'Y' ? 'checked' : ''}></td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </section>

                        <section class="form-card" id="education">
                            <div class="card-title">
                                <h2>学歴</h2>
                                <div>
                                    <button name="action" value="addEducation" formnovalidate>追加する</button>
                                    <button name="action" value="deleteEducations" formnovalidate>選択削除</button>
                                </div>
                            </div>
                            <div class="table-wrap">
                                <table>
                                    <thead>
                                        <tr>
                                            <th>選択</th>
                                            <th>区分</th>
                                            <th>入学年月</th>
                                            <th>卒業年月</th>
                                            <th>学校名</th>
                                            <th>専攻</th>
                                            <th>履修</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach begin="0" end="${educationRowCount - 1}" varStatus="row">
                                            <c:set var="edu" value="${educations[row.index]}" />
                                            <tr>
                                                <td><input type="checkbox" name="educationDeleteIds"
                                                    value="${edu.employeeEducationId}"></td>
                                                <td><select name="educations[${row.index}].schoolType"><option
                                                            value="">選択</option>
                                                        <c:forEach var="type" items="${schoolTypes}">
                                                            <option ${type eq edu.eduType ? 'selected' : ''}>${type}</option>
                                                        </c:forEach></select></td>

                                                <td><input type="month"
                                                    name="educations[${row.index}].admissionYm"
                                                    value="${fn:length(edu.admissionYm) eq 6 ? fn:substring(edu.admissionYm, 0, 4) : ''}${fn:length(edu.admissionYm) eq 6 ? '-' : ''}${fn:length(edu.admissionYm) eq 6 ? fn:substring(edu.admissionYm, 4, 6) : edu.admissionYm}"></td>

                                                <td><input type="month"
                                                    name="educations[${row.index}].graduationYm"
                                                    value="${fn:length(edu.gradYm) eq 6 ? fn:substring(edu.gradYm, 0, 4) : ''}${fn:length(edu.gradYm) eq 6 ? '-' : ''}${fn:length(edu.gradYm) eq 6 ? fn:substring(edu.gradYm, 4, 6) : edu.gradYm}"></td>

                                                <td><input name="educations[${row.index}].schoolName"
                                                    value="<c:out value='${edu.schoolName}' />"></td>
                                                <td><input name="educations[${row.index}].major"
                                                    value="<c:out value='${edu.majorName}' />"></td>
                                                <td><select
                                                    name="educations[${row.index}].completionStatus"><option
                                                            value="">選択</option>
                                                        <c:forEach var="state" items="${schoolStates}">
                                                            <option ${state eq edu.completeType ? 'selected' : ''}>${state}</option>
                                                        </c:forEach></select></td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </section>

                        <section class="form-card" id="career">
                            <div class="card-title">
                                <h2>職歴</h2>
                                <div>
                                    <button name="action" value="addCareer" formnovalidate>追加する</button>
                                    <button name="action" value="deleteCareers" formnovalidate>選択削除</button>
                                </div>
                            </div>
                            <div class="table-wrap">
                                <table>
                                    <thead>
                                        <tr>
                                            <th>選択</th>
                                            <th>会社名</th>
                                            <th>入社日</th>
                                            <th>退社日</th>
                                            <th>勤務期間</th>
                                            <th>最終役職</th>
                                            <th>担当職務</th>
                                            <th>退職理由</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach begin="0" end="${careerRowCount - 1}" varStatus="row">
                                            <c:set var="career" value="${careers[row.index]}" />
                                            <tr>
                                                <td><input type="checkbox" name="careerDeleteIds"
                                                    value="${career.employeeCareerId}"></td>
                                                <td><input name="careers[${row.index}].companyName"
                                                    value="<c:out value='${career.companyName}' />"></td>
                                                <td><input type="date" lang="ja-JP"
                                                    name="careers[${row.index}].startDate"
                                                    value="<fmt:formatDate value='${career.joinDate}' pattern='yyyy-MM-dd' />"></td>
                                                <td><input type="date" lang="ja-JP"
                                                    name="careers[${row.index}].endDate"
                                                    value="<fmt:formatDate value='${career.quitDate}' pattern='yyyy-MM-dd' />"></td>
                                                <td><div class="duration">
                                                        <input value="${career.years}" readonly>年 <input
                                                            value="${career.months}" readonly>月
                                                    </div></td>
                                                <td><input name="careers[${row.index}].lastPosition"
                                                    value="<c:out value='${career.finalPosition}' />"></td>
                                                <td><input name="careers[${row.index}].duty"
                                                    value="<c:out value='${career.duty}' />"></td>
                                                <td><input name="careers[${row.index}].retireReason"
                                                    value="<c:out value='${career.quitReason}' />"></td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </section>

                        <section class="form-card" id="military">
                            <h2>兵役</h2>
                            <div class="table-wrap">
                                <table>
                                    <thead>
                                        <tr>
                                            <th>兵役区分</th>
                                            <th>軍別</th>
                                            <th>服務期間（から）</th>
                                            <th>服務期間（まで）</th>
                                            <th>最終階級</th>
                                            <th>兵科</th>
                                            <th>兵役未了理由</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td><select name="dischargeType"><option
                                                        value="">選択</option>
                                                    <option value="군필"
                                                        ${employee.dischargeType eq '군필' ? 'selected' : ''}>兵役済み</option>
                                                    <option value="미필"
                                                        ${employee.dischargeType eq '미필' ? 'selected' : ''}>兵役未了</option></select></td>
                                            <td><select name="milBranch"><option value="">選択</option>
                                                    <c:forEach var="branch"
                                                        items="${fn:split('육군,해군,공군,상비군,면제,기타', ',')}">
                                                <option value="${branch}" ${branch eq employee.milBranch ? 'selected' : ''}><ui:code-label value="${branch}" /></option>
                                                    </c:forEach></select></td>
                                            <td><input type="date" lang="ja-JP" name="milServiceStart"
                                                value="<fmt:formatDate value='${employee.milServiceStart}' pattern='yyyy-MM-dd' />"></td>
                                            <td><input type="date" lang="ja-JP" name="milServiceEnd"
                                                value="<fmt:formatDate value='${employee.milServiceEnd}' pattern='yyyy-MM-dd' />"></td>
                                            <td><input name="milRank"
                                                value="<c:out value='${employee.milRank}' />"></td>
                                            <td><input name="milSpecialty"
                                                value="<c:out value='${employee.milSpecialty}' />"></td>
                                            <td><input name="milUnfinishedReason"
                                                value="<c:out value='${employee.milUnfinishedReason}' />"></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </section>

                        <div class="form-actions">
                            <button class="button button--primary" name="action" value="save">保存する</button>
                            <a class="button"
                                href="${pageContext.request.contextPath}/settings/register1.do">キャンセル</a>
                            <c:choose>
                                <c:when test="${employee.employeeId gt 0}">
                                    <a class="button button--step" href="${pageContext.request.contextPath}/settings/register2.do?empId=${employee.employeeId}">次に</a>
                                </c:when>
                                <c:otherwise>
                                    <span class="button button--disabled" title="社員情報1を先に保存してください。">次に</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>

                <div id="photo-upload-modal" class="upload-modal" role="dialog"
                    aria-modal="true" aria-labelledby="photo-upload-title">
                    <a class="upload-modal__backdrop" href="#" aria-label="閉じる"></a>
                    <div class="upload-modal__panel">
                        <div class="upload-modal__title">
                            <h2 id="photo-upload-title">画像を登録する</h2>
                            <a href="#" aria-label="閉じる">×</a>
                        </div>
                        <div class="upload-modal__body">
                            <div class="photo-preset-grid">
                                <label><input type="radio" name="photoCandidate" value="01" ${draftPhotoPreset eq '01' ? 'checked' : ''}><img src="${pageContext.request.contextPath}/images/settings/employee-presets/employee-01.png" alt="イエス社員の写真1"><span>写真1</span></label>
                                <label><input type="radio" name="photoCandidate" value="02" ${draftPhotoPreset eq '02' ? 'checked' : ''}><img src="${pageContext.request.contextPath}/images/settings/employee-presets/employee-02.png" alt="イエス社員の写真2"><span>写真2</span></label>
                                <label><input type="radio" name="photoCandidate" value="03" ${draftPhotoPreset eq '03' ? 'checked' : ''}><img src="${pageContext.request.contextPath}/images/settings/employee-presets/employee-03.png" alt="イエス社員の写真3"><span>写真3</span></label>
                                <label><input type="radio" name="photoCandidate" value="04" ${draftPhotoPreset eq '04' ? 'checked' : ''}><img src="${pageContext.request.contextPath}/images/settings/employee-presets/employee-04.png" alt="イエス社員の写真4"><span>写真4</span></label>
                                <label><input type="radio" name="photoCandidate" value="05" ${draftPhotoPreset eq '05' ? 'checked' : ''}><img src="${pageContext.request.contextPath}/images/settings/employee-presets/employee-05.png" alt="イエス社員の写真5"><span>写真5</span></label>
                            </div>
                        </div>
                        <div class="photo-confirm-actions">
                            <span class="upload-modal__confirm photo-confirm-disabled">写真を選択してください。</span>
                            <button class="upload-modal__confirm photo-confirm" type="submit"
                                name="action" value="previewPhoto" formnovalidate>選択完了</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </main>
    <c:if test="${not empty message}">
        <c:url var="register1ReturnUrl" value="/settings/register1.do"><c:param name="dismissMessage" value="true" /><c:if test="${employee.employeeId gt 0}"><c:param name="empId" value="${employee.employeeId}" /></c:if></c:url>
        <div class="employee-setting-alert" role="alertdialog" aria-modal="true" aria-labelledby="employee-setting-alert-message">
            <a class="employee-setting-alert__backdrop" href="${register1ReturnUrl}" aria-label="確認"></a>
            <div class="employee-setting-alert__panel">
                <p id="employee-setting-alert-message"><ui:message-label value="${message}" /></p>
                <a href="${register1ReturnUrl}">確認</a>
            </div>
        </div>
        <c:remove var="message" scope="session" />
    </c:if>
    <%@ include file="/WEB-INF/view/common/footer.jspf"%>
</body>
</html>
