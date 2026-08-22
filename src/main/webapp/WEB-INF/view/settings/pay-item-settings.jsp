<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html lang="ja-JP">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>基本設定>給与項目設定</title>
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
					<p>基本設定</p>
					<h1>給与項目の設定</h1>
				</div>
			</header>

			<section class="setting-card" id="payment-settings">
				<div class="card-title">
					<h2>支払い項目の設定</h2>
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
										<th>支給項目</th>
										<th>課税可</th>
										<th>非課税限度額</th>
										<th>端数処理単位</th>
										<th>勤怠連携/一括支給</th>
										<th>使用可</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="item" items="${paymentItems}">
										<tr
											class="${item.payItemId eq selectedPaymentItem.payItemId ? 'is-selected' : ''}">
											<td><a
												href="${pageContext.request.contextPath}/settings/pay-item.do?payItemId=${item.payItemId}#payment-settings"><ui:code-label
														value="${item.payName}" /></a></td>
											<td><ui:code-label value="${item.taxType}" />
												<c:if
												test="${item.taxType eq '비과세' and not empty item.taxFreeName}">_<ui:code-label
																value="${item.taxFreeName}" />
												</c:if></td>
											<td class="number"><c:if
													test="${not empty item.taxFreeLimit}">
													<c:out value="${item.taxFreeLimit}" />円</c:if></td>
											<td><c:out value="${item.roundUnit}" /></td>
											<td><c:choose>
												<c:when test="${item.payMethod eq '일괄지급'}">一括支給<c:if test="${not empty item.bulkPayAmount}">_<fmt:formatNumber value="${item.bulkPayAmount}" pattern="#,##0" />円</c:if></c:when>
												<c:when test="${not empty item.attendName}"><ui:code-label value="${item.attendName}" /></c:when>
											</c:choose></td>
											<td><span
												class="use-status use-status--${item.useYn eq 'Y' ? 'on' : 'off'}">${item.useYn eq 'Y' ? '使用' : '未使用'}</span></td>
										</tr>
									</c:forEach>
									<c:if test="${empty paymentItems}">
										<tr>
											<td colspan="6" class="empty-row">登録された支払い項目はありません。</td>
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
						<h3>支給項目情報</h3>
						<label class="editor-field"><span>支給項目</span>
							<c:choose>
								<c:when test="${selectedPaymentItem.payName eq '기본급'}">
									<input type="hidden" name="payName" value="기본급">
									<input value="基本給" readonly>
								</c:when>
								<c:otherwise>
									<input name="payName" maxlength="50"
										value="<c:out value='${selectedPaymentItem.payName}' />"
										placeholder="お支払い項目を入力してください" required>
								</c:otherwise>
							</c:choose>
						</label>
						<div class="editor-field">
							<span>課税可</span>
							<div class="radio-line">
								<label><input type="radio" name="taxType" value="전체과세"
									${empty selectedPaymentItem.taxType or selectedPaymentItem.taxType eq '전체과세' ? 'checked' : ''}>
									全体課税</label><label><input type="radio" name="taxType"
									value="비과세"
									${selectedPaymentItem.taxType eq '비과세' ? 'checked' : ''}>
									非課税</label>
							</div>
						</div>
						<div class="editor-field tax-free-field tax-free-selector-field">
							<span>非課税</span>
							<div class="input-with-button">
								<input name="taxFreeName"
									value="<c:out value='${selectedPaymentItem.taxFreeName}' />"
									readonly><a href="#tax-free-modal">選択</a>
							</div>
						</div>
						<label class="editor-field tax-free-field standard-tax-free-field"><span>非課税
								限度額</span>
						<div class="amount-input">
								<input name="taxFreeLimit" inputmode="numeric" readonly
									value="<c:out value='${selectedPaymentItem.taxFreeLimit}' />"><i>円</i>
							</div></label>
						<label class="editor-field tax-free-field direct-tax-free-field"><span>非課税名直接入力</span><input
							name="directTaxFreeName" maxlength="300"
							value="<c:out value='${selectedPaymentItem.directTaxFreeName}' />"
							placeholder="非課税名を入力してください"></label>
						<label class="editor-field tax-free-field direct-tax-free-field"><span>非課税限度額</span>
							<div class="amount-input"><input name="directTaxFreeLimit" type="number" min="0" inputmode="numeric"
								value="<c:out value='${selectedPaymentItem.directTaxFreeLimit}' />"><i>円</i></div></label>
						<label class="editor-field"><span>計算方法</span><input
							name="calcMethod"
							value="<c:out value='${selectedPaymentItem.calcMethod}' />"
							placeholder="計算方法を入力してください"></label> <label class="editor-field"><span>端数処理単位</span><select
							name="roundUnit"><option value="0"
									${empty selectedPaymentItem.roundUnit or selectedPaymentItem.roundUnit eq '0' ? 'selected' : ''}>なし</option>
								<option value="1"
									${selectedPaymentItem.roundUnit eq '1' ? 'selected' : ''}>1円
									単位</option>
								<option value="10"
									${selectedPaymentItem.roundUnit eq '10' ? 'selected' : ''}>10円
									単位</option>
								<option value="100"
									${selectedPaymentItem.roundUnit eq '100' ? 'selected' : ''}>100円
									単位</option></select></label> <label class="editor-field"><span>勤怠連携/一括支給</span><select
							name="linkAttendId"><option value="">選択してください。</option>
								<c:forEach var="attend" items="${attendItems}">
									<option value="${attend.attendanceItemId}"
										${attend.attendanceItemId eq selectedPaymentItem.linkAttendId ? 'selected' : ''}><c:out
											value="${attend.attendName}" /></option>
								</c:forEach>
								<option value="BATCH"
									${selectedPaymentItem.payMethod eq '일괄지급' ? 'selected' : ''}>一括支給</option></select></label>
						<label class="editor-field batch-amount-field"><span>一括支払額</span>
						<div class="amount-input">
								<input name="bulkPayAmount" inputmode="numeric"
									value="<c:out value='${selectedPaymentItem.bulkPayAmount}' />"><i>円</i>
							</div></label>
						<div class="editor-field">
							<span>使用可</span>
							<div class="radio-line use-radio-line">
								<label><input type="radio" name="useYn" value="Y"
									${empty selectedPaymentItem.useYn or selectedPaymentItem.useYn eq 'Y' ? 'checked' : ''}>
									使用</label><label><input type="radio" name="useYn" value="N"
									${selectedPaymentItem.useYn eq 'N' ? 'checked' : ''}>
									無効</label>
							</div>
						</div>
						<div class="editor-actions">
							<button name="action" value="insert">追加</button>
							<button name="action" value="update">修正</button>
							<button class="gray" name="action" value="requestDelete">削除</button>
							<button class="gray clear-button" name="action" value="clear">内容を消去する</button>
						</div>
					</form>
				</div>
			</section>

			<section class="setting-card" id="deduction-settings">
				<div class="card-title">
					<h2>控除項目の設定</h2>
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
										<th>控除項目</th>
										<th>端数処理単位</th>
										<th>使用可</th>
										<th>備考</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="item" items="${deductionItems}">
										<tr
											class="${item.deductItemId eq selectedDeductionItem.deductItemId ? 'is-selected' : ''}">
											<td><a
												href="${pageContext.request.contextPath}/settings/pay-item.do?deductItemId=${item.deductItemId}#deduction-settings"><ui:code-label
														value="${item.deductName}" /></a></td>
											<td><c:out value="${item.roundUnit}" /></td>
											<td><span
												class="use-status use-status--${item.useYn eq 'Y' ? 'on' : 'off'}">${item.useYn eq 'Y' ? '使用' : '未使用'}</span></td>
											<td><ui:code-label value="${item.note}" /></td>
										</tr>
									</c:forEach>
									<c:if test="${empty deductionItems}">
										<tr>
											<td colspan="4" class="empty-row">登録された控除項目はありません。</td>
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
					<h3>控除項目情報</h3>
						<label class="editor-field"><span>控除項目</span>
							<c:choose>
								<c:when test="${selectedDeductionItem.deductName eq '국민연금'}"><input type="hidden" name="deductName" value="국민연금"><input value="国民年金" readonly></c:when>
								<c:when test="${selectedDeductionItem.deductName eq '건강보험'}"><input type="hidden" name="deductName" value="건강보험"><input value="健康保険" readonly></c:when>
								<c:when test="${selectedDeductionItem.deductName eq '장기요양보험' or selectedDeductionItem.deductName eq '노인장기요양보험'}"><input type="hidden" name="deductName" value="<c:out value='${selectedDeductionItem.deductName}' />"><input value="介護保険" readonly></c:when>
								<c:when test="${selectedDeductionItem.deductName eq '고용보험'}"><input type="hidden" name="deductName" value="고용보험"><input value="雇用保険" readonly></c:when>
								<c:when test="${selectedDeductionItem.deductName eq '소득세'}"><input type="hidden" name="deductName" value="소득세"><input value="所得税" readonly></c:when>
								<c:when test="${selectedDeductionItem.deductName eq '지방소득세'}"><input type="hidden" name="deductName" value="지방소득세"><input value="地方所得税" readonly></c:when>
								<c:otherwise><input name="deductName" maxlength="50" value="<c:out value='${selectedDeductionItem.deductName}' />" placeholder="控除項目を入力してください" required></c:otherwise>
							</c:choose>
						</label> <label
							class="editor-field"><span>計算方法</span><input
							name="calcMethod"
							value="<c:out value='${selectedDeductionItem.calcMethod}' />"
							placeholder="計算方法を入力してください"></label> <label class="editor-field"><span>端数処理単位</span><select
							name="roundUnit"><option value="0">なし</option>
								<option value="1"
									${selectedDeductionItem.roundUnit eq '1' ? 'selected' : ''}>1円
									単位</option>
								<option value="10"
									${selectedDeductionItem.roundUnit eq '10' ? 'selected' : ''}>10円
									単位</option>
								<option value="100"
									${selectedDeductionItem.roundUnit eq '100' ? 'selected' : ''}>100円
									単位</option></select></label> <label class="editor-field"><span>備考</span><input
							name="note"
							value="<c:out value='${selectedDeductionItem.note}' />"></label>
						<div class="editor-field">
							<span>使用可</span>
							<div class="radio-line use-radio-line">
								<label><input type="radio" name="useYn" value="Y"
									${empty selectedDeductionItem.useYn or selectedDeductionItem.useYn eq 'Y' ? 'checked' : ''}>
									使用</label><label><input type="radio" name="useYn" value="N"
									${selectedDeductionItem.useYn eq 'N' ? 'checked' : ''}>
									無効</label>
							</div>
						</div>
						<div class="editor-actions">
							<button name="action" value="insert">追加</button>
							<button name="action" value="update">修正</button>
							<button class="gray" name="action" value="requestDelete">削除</button>
							<button class="gray clear-button" name="action" value="clear">内容を消去する</button>
						</div>
					</form>
				</div>
			</section>

			<div id="tax-free-modal" class="tax-modal" role="dialog"
				aria-modal="true" aria-labelledby="tax-modal-title">
				<a class="tax-modal__backdrop" href="#payment-settings"
					aria-label="閉じる"></a>
				<div class="tax-modal__panel">
					<div class="tax-modal__title">
						<h2 id="tax-modal-title">非課税項目を選択</h2>
						<a href="#payment-settings" aria-label="閉じる">×</a>
					</div>
					<div class="tax-modal__table">
						<table>
							<thead>
								<tr>
									<th>法令</th>
									<th>コード</th>
									<th>記載欄</th>
									<th>非課税項目</th>
									<th>限めっき</th>
									<th>支払調書作成</th>
									<th>選択</th>
								</tr>
							</thead>
							<tbody>
								<tr class="tax-category-row">
									<td colspan="7"><div><strong>非課税</strong>
										<button type="submit" form="payment-editor-form" formmethod="get" formnovalidate
											formaction="${pageContext.request.contextPath}/settings/pay-item.do#payment-settings"
											name="selectedTaxFreeCode" value="DIRECT">直接入力</button></div></td>
								</tr>
								<c:set var="previousTaxCategory" value="비과세" />
								<c:forEach var="tax" items="${taxFreeItems}">
									<c:if test="${tax.incomeCategory ne previousTaxCategory}">
										<tr class="tax-category-row"><td colspan="7"><strong><ui:tax-free-label code="${tax.taxFreeCode}" field="category" value="${tax.incomeCategory}" /></strong></td></tr>
										<c:set var="previousTaxCategory" value="${tax.incomeCategory}" />
									</c:if>
									<tr>
										<td><ui:tax-free-label code="${tax.taxFreeCode}" field="legal" value="${tax.legalClause}" /></td>
										<td><c:out value="${tax.taxFreeCode}" /></td>
										<td><ui:code-label value="${tax.reportField}" /></td>
										<td><ui:tax-free-label code="${tax.taxFreeCode}" field="name" value="${tax.taxFreeName}" /></td>
										<td><c:choose><c:when test="${not empty tax.defaultLimit}"><fmt:formatNumber value="${tax.defaultLimit}" pattern="#,##0" />円</c:when><c:otherwise>-</c:otherwise></c:choose></td>
										<td><span class="statement-mark statement-mark--${tax.payStatementYn eq 'Y' ? 'yes' : 'no'}">${tax.payStatementYn eq 'Y' ? '○' : '×'}</span></td>
										<td><button type="submit" form="payment-editor-form" formmethod="get" formnovalidate
											formaction="${pageContext.request.contextPath}/settings/pay-item.do#payment-settings"
											name="selectedTaxFreeCode" value="${tax.taxFreeCode}">選択</button></td>
									</tr>
								</c:forEach>
								<c:if test="${empty taxFreeItems}">
									<tr>
										<td colspan="7" class="empty-row">登録されている非課税項目はありません。</td>
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
			<a class="setting-alert__backdrop" href="${pageContext.request.contextPath}/settings/pay-item.do?dismissDelete=true#${deleteItemType eq 'PAY' ? 'payment-settings' : 'deduction-settings'}" aria-label="削除のキャンセル"></a>
			<form class="setting-alert__panel" method="post" action="${pageContext.request.contextPath}/settings/${deleteItemType eq 'PAY' ? 'pay-item.do' : 'deduction-item.do'}">
				<p id="delete-alert-message">選択した${deleteItemType eq 'PAY' ? '支給項目' : '控除項目'}を削除してもよろしいですか？</p>
				<p class="setting-alert__warning">削除した項目は復元できません。</p>
				<input type="hidden" name="${deleteItemType eq 'PAY' ? 'payItemId' : 'deductItemId'}" value="<c:out value='${deleteItemId}' />">
				<div class="setting-alert__actions">
					<button type="submit" name="action" value="delete">削除</button>
					<a href="${pageContext.request.contextPath}/settings/pay-item.do?dismissDelete=true#${deleteItemType eq 'PAY' ? 'payment-settings' : 'deduction-settings'}">キャンセル</a>
				</div>
			</form>
		</div>
		<c:remove var="deleteItemType" scope="session" />
		<c:remove var="deleteItemId" scope="session" />
	</c:if>
	<c:if test="${not empty message}">
		<div class="setting-alert" role="alertdialog" aria-modal="true" aria-labelledby="setting-alert-message">
			<a class="setting-alert__backdrop" href="${pageContext.request.contextPath}/settings/pay-item.do?dismissMessage=true${empty messageAnchor ? '' : messageAnchor}" aria-label="閉じる"></a>
			<div class="setting-alert__panel">
				<p id="setting-alert-message"><ui:message-label value="${message}" /></p>
				<a class="setting-alert__confirm" href="${pageContext.request.contextPath}/settings/pay-item.do?dismissMessage=true${empty messageAnchor ? '' : messageAnchor}">確認</a>
			</div>
		</div>
		<c:remove var="message" scope="session" />
		<c:remove var="messageAnchor" scope="session" />
	</c:if>
	<%@ include file="/WEB-INF/view/common/footer.jspf"%>
</body>
</html>
