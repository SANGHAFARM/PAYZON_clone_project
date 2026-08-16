<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>기본환경설정 &gt; 사용자 정보</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/settings/user-info.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/payzon-ui.css">
</head>
<body>
	<%@ include file="/WEB-INF/view/common/header.jspf" %>

	<main class="page-content">
		<div class="user-info-page">
			<%-- 컨트롤러 목록이 아직 연결되지 않은 미리보기에서도 원본 선택 항목이 모두 표시되도록 하는 기본값 --%>
			<c:set var="defaultDepartments" value="${fn:split('사장실,개발팀,콘텐츠팀,업무지원팀,디자인팀,관리팀,기획전략팀', ',')}" />
			<c:set var="defaultPositions" value="${fn:split('이사,차장,사장,부장,과장,대리,주임,사원,실장', ',')}" />
			<c:set var="defaultBanks" value="${fn:split('국민은행,기업은행,농협중앙회,농협은행,산업은행,신한은행,스탠다드차타드은행,우리은행,외환은행,하나은행,한국씨티은행,경남은행,광주은행,지역농협,대구은행,부산은행,전북은행,제주은행,카카오뱅크,케이뱅크,토스뱅크,산림조합,상호저축은행,새마을금고,신용협동조합,수협중앙회,우체국,도이치뱅크,BOA,에이비엔암로,HSBC,JP모간,BNP파리바,OK저축은행,골든브릿지투자증권,교보증권,대신증권,동부증권,리딩투자증권,메리츠종합금융증권,미래에셋대우,미래에셋증권,바로투자증권,부국증권,삼성증권,신영증권,신한금융투자,유안타증권,유진투자증권,유화증권,이베스트투자증권,카카오페이증권,코리아에셋투자증권,키움증권,토스증권,하나금융투자,하이투자증권,한국투자증권,한양증권,한화투자증권,현대증권,흥국증권,BNK투자증권,HMC투자증권,IBK투자증권,KB투자증권,KTB투자증권,LIG투자증권,NH투자증권,SK증권', ',')}" />
			<c:set var="payDays" value="${fn:split('01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,99', ',')}" />
			<header class="page-heading">
				<div>
					<p class="page-heading__path">기본환경설정</p>
					<h1>사용자 정보</h1>
				</div>
				<p class="page-heading__notice"><strong>*</strong> 표시는 필수입력사항입니다.</p>
			</header>

			<c:if test="${not empty message}">
				<p class="form-message" role="status"><c:out value="${message}" /></p>
			</c:if>

			<form class="company-form" action="${pageContext.request.contextPath}/settings/user-info.do" method="post" enctype="multipart/form-data">
				<input type="hidden" name="companyId" value="<c:out value='${company.companyId}' />">

				<section class="form-section">
					<h2>회사정보</h2>
					<div class="form-grid">
						<label class="field"><span><b>*</b> 상호</span><input name="cmpnName" value="<c:out value='${company.cmpnName}' />" required maxlength="100"></label>
						<div class="field"><span><b>*</b> 대표자직급 / 대표자</span><div class="field-pair"><input name="ceoTitle" value="<c:out value='${company.ceoTitle}' />" required maxlength="50" aria-label="대표자 직급"><i>/</i><input name="ceoName" value="<c:out value='${company.ceoName}' />" required maxlength="50" aria-label="대표자 성명"></div></div>
						<label class="field"><span><b>*</b> 사업자번호</span><input name="bizRegNo" value="<c:out value='${company.bizRegNo}' />" required maxlength="20"></label>
						<label class="field"><span>법인등록번호</span><input name="corpRegNo" value="<c:out value='${company.corpRegNo}' />" maxlength="20"></label>
						<label class="field"><span>설립일</span><input type="date" name="foundationDate" value="<c:out value='${company.foundationDate}' />"></label>
						<label class="field"><span>홈페이지</span><input type="url" name="homepageUrl" value="<c:out value='${company.homepageUrl}' />" maxlength="300" placeholder="https://example.com"></label>
						<div class="field field--wide"><span><b>*</b> 사업장 주소</span><div class="address-fields"><input name="zipCode" value="<c:out value='${company.zipCode}' />" required maxlength="10" placeholder="우편번호" aria-label="우편번호"><input name="address" value="<c:out value='${company.address}' />" required maxlength="500" placeholder="주소" aria-label="사업장 주소"></div></div>
						<label class="field"><span><b>*</b> 전화번호</span><input type="tel" name="telNo" value="<c:out value='${company.telNo}' />" required maxlength="30" placeholder="02-0000-0000"></label>
						<label class="field"><span>팩스번호</span><input type="tel" name="faxNo" value="<c:out value='${company.faxNo}' />" maxlength="30"></label>
						<label class="field"><span>업태</span><input name="bizType" value="<c:out value='${company.bizType}' />" maxlength="100"></label>
						<label class="field"><span>종목</span><input name="bizItem" value="<c:out value='${company.bizItem}' />" maxlength="100"></label>
					</div>
				</section>

				<section class="form-section">
					<div class="section-title-row"><h2>담당자 정보</h2></div>
					<div class="form-grid form-grid--three">
						<label class="field"><span><b>*</b> 성명</span><input name="managerName" value="<c:out value='${company.managerName}' />" required maxlength="50"></label>
						<label class="field"><span>부서</span><select name="managerDeptName"><option value="">선택</option><c:choose><c:when test="${not empty departmentList}"><c:forEach var="dept" items="${departmentList}"><option value="<c:out value='${dept.deptName}' />" ${dept.deptName eq company.managerDeptName ? 'selected' : ''}><c:out value="${dept.deptName}" /></option></c:forEach></c:when><c:otherwise><c:forEach var="deptName" items="${defaultDepartments}"><option value="<c:out value='${deptName}' />" ${deptName eq company.managerDeptName ? 'selected' : ''}><c:out value="${deptName}" /></option></c:forEach></c:otherwise></c:choose></select></label>
						<label class="field"><span>직위</span><select name="managerPosName"><option value="">선택</option><c:choose><c:when test="${not empty positionList}"><c:forEach var="pos" items="${positionList}"><option value="<c:out value='${pos.posName}' />" ${pos.posName eq company.managerPosName ? 'selected' : ''}><c:out value="${pos.posName}" /></option></c:forEach></c:when><c:otherwise><c:forEach var="posName" items="${defaultPositions}"><option value="<c:out value='${posName}' />" ${posName eq company.managerPosName ? 'selected' : ''}><c:out value="${posName}" /></option></c:forEach></c:otherwise></c:choose></select></label>
						<label class="field"><span>전화번호</span><input type="tel" name="managerTelNo" value="<c:out value='${company.managerTelNo}' />" maxlength="30"></label>
						<label class="field"><span>휴대폰번호</span><input type="tel" name="managerMobileNo" value="<c:out value='${company.managerMobileNo}' />" maxlength="30"></label>
						<label class="field"><span>이메일</span><input type="email" name="managerEmail" value="<c:out value='${company.managerEmail}' />" maxlength="150"></label>
					</div>
				</section>

				<section class="form-section">
					<h2>급여지급 정보</h2>
					<div class="pay-info-table">
						<div class="pay-info-label">급여 산정기간</div><div class="pay-period"><select name="payCalcStartScope" required><option value="P" ${company.payCalcStartScope eq 'P' ? 'selected' : ''}>전월</option><option value="N" ${empty company.payCalcStartScope or company.payCalcStartScope eq 'N' ? 'selected' : ''}>당월</option></select><select name="payCalcStartDay" required><c:forEach var="day" items="${payDays}"><option value="${day}" ${day eq company.payCalcStartDay or (empty company.payCalcStartDay and day eq '01') ? 'selected' : ''}>${day eq '99' ? '말일' : day}</option></c:forEach></select><span>~</span><select name="payCalcEndScope" required><option value="P" ${company.payCalcEndScope eq 'P' ? 'selected' : ''}>전월</option><option value="N" ${empty company.payCalcEndScope or company.payCalcEndScope eq 'N' ? 'selected' : ''}>당월</option></select><select name="payCalcEndDay" required><c:forEach var="day" items="${payDays}"><option value="${day}" ${day eq company.payCalcEndDay or (empty company.payCalcEndDay and day eq '99') ? 'selected' : ''}>${day eq '99' ? '말일' : day}</option></c:forEach></select></div>
						<div class="pay-info-label">급여지급일</div><div class="pay-day"><select name="payDateScope" required><option value="0" ${company.payDateScope eq '0' ? 'selected' : ''}>당월</option><option value="1" ${empty company.payDateScope or company.payDateScope eq '1' ? 'selected' : ''}>익월</option></select><select name="payDateDay" required><c:forEach var="day" items="${payDays}"><option value="${day}" ${day eq company.payDateDay or (empty company.payDateDay and day eq '05') ? 'selected' : ''}>${day eq '99' ? '말일' : day}</option></c:forEach></select><span>일</span></div>
						<div class="pay-info-label">금융기관</div><div class="pay-bank"><select name="payBankName"><option value="">선택해주세요</option><c:forEach var="bank" items="${empty bankList ? defaultBanks : bankList}"><option value="<c:out value='${bank}' />" ${bank eq company.payBankName ? 'selected' : ''}><c:out value="${bank}" /></option></c:forEach></select></div>
						<div class="pay-info-label">계좌번호</div><div class="pay-account"><input name="payAccountNo" value="<c:out value='${company.payAccountNo}' />" maxlength="100"></div>
						<div class="pay-info-label">예금주</div><div class="pay-holder"><input name="payAccountHolder" value="<c:out value='${company.payAccountHolder}' />" maxlength="100"></div>
					</div>
				</section>

				<section class="asset-grid">
					<article class="asset-card">
						<div class="asset-card__title"><h2>로고등록</h2><c:if test="${not empty company.logoImgPath}"><span>완료</span></c:if></div>
						<div class="asset-card__body"><div class="asset-preview"><c:choose><c:when test="${not empty company.logoImgPath}"><img src="${pageContext.request.contextPath}<c:out value='${company.logoImgPath}' />" alt="회사 로고"></c:when><c:otherwise><span>등록된 로고가 없습니다.</span></c:otherwise></c:choose></div><div><p>로고는 가로 150px 썸네일로 생성됩니다.</p><p>투명 PNG 이미지 사용을 권장합니다.</p><div class="mini-actions"><a href="#logo-upload-modal">등록</a><button type="submit" name="action" value="deleteLogo">삭제</button></div></div></div>
					</article>
					<article class="asset-card">
						<div class="asset-card__title"><h2>도장등록</h2><c:if test="${not empty company.stampImgPath}"><span>사용</span></c:if></div>
						<div class="asset-card__body"><div class="asset-preview"><c:choose><c:when test="${not empty company.stampImgPath}"><img src="${pageContext.request.contextPath}<c:out value='${company.stampImgPath}' />" alt="회사 도장"></c:when><c:otherwise><span>등록된 도장이 없습니다.</span></c:otherwise></c:choose></div><div><p>로고는 가로 150px 썸네일로 생성됩니다.</p><p>투명 PNG 이미지 사용을 권장합니다.</p><div class="mini-actions"><a href="#stamp-upload-modal">등록</a><button type="submit" name="action" value="deleteStamp">삭제</button></div></div></div>
					</article>
				</section>

				<div id="logo-upload-modal" class="upload-modal" role="dialog" aria-modal="true" aria-labelledby="logo-upload-title"><a class="upload-modal__backdrop" href="#" aria-label="닫기"></a><div class="upload-modal__panel"><div class="upload-modal__title"><h2 id="logo-upload-title">이미지 등록하기</h2><a href="#" aria-label="닫기">×</a></div><div class="upload-modal__body"><input type="file" name="logoFile" accept="image/png,image/jpeg"><p>* 파일 용량 : <strong>1MB 미만</strong>이어야 합니다.<br>* 파일명 : <strong>영문 또는 숫자</strong>로 되어 있어야 합니다.</p></div><button class="upload-modal__confirm" type="submit" name="action" value="saveLogo">확인</button></div></div>
				<div id="stamp-upload-modal" class="upload-modal" role="dialog" aria-modal="true" aria-labelledby="stamp-upload-title"><a class="upload-modal__backdrop" href="#" aria-label="닫기"></a><div class="upload-modal__panel"><div class="upload-modal__title"><h2 id="stamp-upload-title">이미지 등록하기</h2><a href="#" aria-label="닫기">×</a></div><div class="upload-modal__body"><input type="file" name="stampFile" accept="image/png,image/jpeg"><p>* 파일 용량 : <strong>1MB 미만</strong>이어야 합니다.<br>* 파일명 : <strong>영문 또는 숫자</strong>로 되어 있어야 합니다.</p></div><button class="upload-modal__confirm" type="submit" name="action" value="saveStamp">확인</button></div></div>

				<div class="form-actions"><button class="button button--primary" type="submit" name="action" value="save">저장하기</button><a class="button" href="${pageContext.request.contextPath}/settings/user-info.do">취소하기</a></div>
			</form>
		</div>
	</main>

	<%@ include file="/WEB-INF/view/common/footer.jspf" %>
</body>
</html>
