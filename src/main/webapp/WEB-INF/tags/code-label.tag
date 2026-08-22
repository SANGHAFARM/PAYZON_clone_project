<%@ tag pageEncoding="UTF-8" body-content="empty" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ attribute name="value" required="false" type="java.lang.String" %>

<%-- DB/Java에서 사용하는 한국어 코드값은 유지하고 화면에 표시할 때만 일본어로 변환한다. --%>
<c:choose>
	<c:when test="${value eq '정규직'}">正社員</c:when>
	<c:when test="${value eq '계약직'}">契約社員</c:when>
	<c:when test="${value eq '임시직'}">臨時社員</c:when>
	<c:when test="${value eq '파견직'}">派遣社員</c:when>
	<c:when test="${value eq '위촉직'}">委嘱社員</c:when>
	<c:when test="${value eq '일용직'}">日雇い社員</c:when>
	<c:when test="${value eq '재직' or value eq 'WORK' or value eq 'ACTIVE'}">在職</c:when>
	<c:when test="${value eq '퇴직' or value eq 'RETIRED'}">退職</c:when>
	<c:when test="${value eq '내국인'}">内国人</c:when>
	<c:when test="${value eq '외국인'}">外国人</c:when>
	<c:when test="${value eq '국민연금'}">国民年金</c:when>
	<c:when test="${value eq '건강보험'}">健康保険</c:when>
	<c:when test="${value eq '노인장기요양보험' or value eq '장기요양보험'}">介護保険</c:when>
	<c:when test="${value eq '고용보험'}">雇用保険</c:when>
	<c:when test="${value eq '산재보험'}">労災保険</c:when>
	<c:when test="${value eq '기본급'}">基本給</c:when>
	<c:when test="${value eq '직책수당'}">役職手当</c:when>
	<c:when test="${value eq '연장근무수당'}">時間外勤務手当</c:when>
	<c:when test="${value eq '성과급'}">業績賞与</c:when>
	<c:when test="${value eq '식대'}">食事手当</c:when>
	<c:when test="${value eq '자가운전보조금'}">自家用車手当</c:when>
	<c:when test="${value eq '보육수당'}">保育手当</c:when>
	<c:when test="${value eq '연구보조비'}">研究補助費</c:when>
	<c:when test="${value eq '소득세'}">所得税</c:when>
	<c:when test="${value eq '지방소득세'}">地方所得税</c:when>
	<c:when test="${value eq '사우회비'}">親睦会費</c:when>
	<c:when test="${value eq '기타공제'}">その他控除</c:when>
	<c:when test="${value eq '사원부담분'}">従業員負担分</c:when>
	<c:when test="${value eq '급여소득세'}">給与所得税</c:when>
	<c:when test="${value eq '대여금 등'}">貸付金など</c:when>
	<c:when test="${value eq '지급합계'}">支給合計</c:when>
	<c:when test="${value eq '공제합계'}">控除合計</c:when>
	<c:when test="${value eq '지급항목'}">支給項目</c:when>
	<c:when test="${value eq '공제항목'}">控除項目</c:when>
	<c:when test="${value eq '전체과세'}">全額課税</c:when>
	<c:when test="${value eq '비과세'}">非課税</c:when>
	<c:when test="${value eq '일괄지급'}">一括支給</c:when>
	<c:when test="${value eq '근태연결'}">勤怠連携</c:when>
	<c:when test="${value eq '근로소득자'}">給与所得者</c:when>
	<c:when test="${value eq '사업소득자'}">事業所得者</c:when>
	<c:when test="${value eq '기타소득자'}">その他所得者</c:when>
	<c:when test="${value eq '근로/사업소득자'}">給与・事業所得者</c:when>
	<c:when test="${value eq '면제'}">免除</c:when>
	<c:when test="${value eq '퇴직정산' or value eq 'RETIREMENT'}">退職精算</c:when>
	<c:when test="${value eq '중간정산' or value eq 'INTERIM'}">中間精算</c:when>
	<c:when test="${value eq '계좌이체'}">口座振込</c:when>
	<c:when test="${value eq '급여계좌이체'}">給与口座振込</c:when>
	<c:when test="${value eq 'IRP계좌'}">IRP口座</c:when>
	<c:when test="${value eq '재직증명서'}">在職証明書</c:when>
	<c:when test="${value eq '경력증명서'}">職歴証明書</c:when>
	<c:when test="${value eq '퇴직증명서'}">退職証明書</c:when>
	<c:when test="${value eq '재직경력서'}">在職・職歴証明書</c:when>
	<c:when test="${value eq '일'}">日</c:when>
	<c:when test="${value eq '시간'}">時間</c:when>
	<c:when test="${value eq '소정근로'}">所定労働</c:when>
	<c:when test="${value eq '연장근로'}">時間外労働</c:when>
	<c:when test="${value eq '야간근로'}">深夜労働</c:when>
	<c:when test="${value eq '휴일근로'}">休日労働</c:when>
	<c:when test="${value eq '근무'}">勤務</c:when>
	<c:when test="${value eq '휴가'}">休暇</c:when>
	<c:when test="${value eq '연차'}">年次有給休暇</c:when>
	<c:when test="${value eq '반차'}">半日休暇</c:when>
	<c:when test="${value eq '포상휴가'}">褒賞休暇</c:when>
	<c:when test="${value eq '청원휴가'}">特別休暇</c:when>
	<c:when test="${value eq '연장근무'}">時間外勤務</c:when>
	<c:when test="${value eq '상'}">上</c:when>
	<c:when test="${value eq '중'}">中</c:when>
	<c:when test="${value eq '하'}">下</c:when>
	<c:when test="${fn:contains(value, '급여-') or fn:contains(value, '사업/기타') or fn:contains(value, '사업·기타') or fn:contains(value, '일반') or fn:contains(value, '일용직')}">
		<c:set var="localizedRound" value="${fn:replace(value, '급여-', '給与-')}" />
		<c:set var="localizedRound" value="${fn:replace(localizedRound, '차', '回目')}" />
		<c:set var="localizedRound" value="${fn:replace(localizedRound, '일반', '一般')}" />
		<c:set var="localizedRound" value="${fn:replace(localizedRound, '사업/기타', '事業・その他')}" />
		<c:set var="localizedRound" value="${fn:replace(localizedRound, '사업·기타', '事業・その他')}" />
		<c:set var="localizedRound" value="${fn:replace(localizedRound, '일용직', '日雇い社員')}" />
		<c:out value="${localizedRound}" />
	</c:when>
	<c:otherwise><c:out value="${value}" /></c:otherwise>
</c:choose>
