<%@ tag pageEncoding="UTF-8" body-content="empty" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ attribute name="code" required="false" type="java.lang.String" %>
<%@ attribute name="field" required="true" type="java.lang.String" %>
<%@ attribute name="value" required="false" type="java.lang.String" %>

<c:choose>
	<c:when test="${field eq 'category'}">
		<c:choose>
			<c:when test="${value eq '비과세'}">非課税</c:when>
			<c:when test="${value eq '감면 소득'}">減免所得</c:when>
			<c:otherwise><c:out value="${value}" /></c:otherwise>
		</c:choose>
	</c:when>
	<c:when test="${field eq 'legal'}">
		<c:set var="legal" value="${fn:replace(value, '구 조특법', '旧租税特例制限法')}" />
		<c:set var="legal" value="${fn:replace(legal, '조특법', '租税特例制限法')}" />
		<c:set var="legal" value="${fn:replace(legal, '소법', '所得税法')}" />
		<c:set var="legal" value="${fn:replace(legal, '소령', '所得税法施行令')}" />
		<c:set var="legal" value="${fn:replace(legal, '조세조약', '租税条約')}" />
		<c:set var="legal" value="${fn:replace(legal, ' 가', ' イ')}" />
		<c:set var="legal" value="${fn:replace(legal, ' 나', ' ロ')}" />
		<c:set var="legal" value="${fn:replace(legal, ' 다', ' ハ')}" />
		<c:set var="legal" value="${fn:replace(legal, ' 라', ' ニ')}" />
		<c:set var="legal" value="${fn:replace(legal, ' 마', ' ホ')}" />
		<c:set var="legal" value="${fn:replace(legal, ' 바', ' ヘ')}" />
		<c:set var="legal" value="${fn:replace(legal, ' 사', ' ト')}" />
		<c:set var="legal" value="${fn:replace(legal, ' 아', ' チ')}" />
		<c:set var="legal" value="${fn:replace(legal, ' 자', ' リ')}" />
		<c:set var="legal" value="${fn:replace(legal, ' 차', ' ヌ')}" />
		<c:set var="legal" value="${fn:replace(legal, ' 카', ' ル')}" />
		<c:set var="legal" value="${fn:replace(legal, ' 타', ' ヲ')}" />
		<c:set var="legal" value="${fn:replace(legal, ' 파', ' ワ')}" />
		<c:set var="legal" value="${fn:replace(legal, ' 하', ' カ')}" />
		<c:set var="legal" value="${fn:replace(legal, ' 거', ' ヨ')}" />
		<c:set var="legal" value="${fn:replace(legal, ' 너', ' タ')}" />
		<c:set var="legal" value="${fn:replace(legal, ' 더', ' レ')}" />
		<c:set var="legal" value="${fn:replace(legal, ' 러', ' ソ')}" />
		<c:set var="legal" value="${fn:replace(legal, ' 머', ' ツ')}" />
		<c:set var="legal" value="${fn:replace(legal, ' 버', ' ネ')}" />
		<c:set var="legal" value="${fn:replace(legal, ' 서', ' ナ')}" />
		<c:out value="${legal}" />
	</c:when>
	<c:when test="${field eq 'name'}">
		<c:choose>
			<c:when test="${code eq 'CAR001'}">自家用車手当</c:when>
			<c:when test="${code eq 'CHILD1'}">保育手当</c:when>
			<c:when test="${code eq 'MEAL01'}">食事手当</c:when>
			<c:when test="${code eq 'RSRCH1'}">研究補助費</c:when>
			<c:when test="${code eq 'A01'}">兵役服務中に受ける給与</c:when>
			<c:when test="${code eq 'B01'}">法令による動員職場で受ける給与</c:when>
			<c:when test="${code eq 'C01'}">産業災害補償保険法に基づく療養給付等</c:when>
			<c:when test="${code eq 'D01'}">労働基準法等に基づく療養補償金等</c:when>
			<c:when test="${code eq 'E01'}">雇用保険法に基づく育児休業給付等</c:when>
			<c:when test="${code eq 'E02'}">国家公務員法等に基づく育児休業手当等</c:when>
			<c:when test="${code eq 'E10'}">国民年金法に基づく返還一時金および死亡一時金</c:when>
			<c:when test="${code eq 'F01'}">公務員年金法等に基づく療養費等</c:when>
			<c:when test="${code eq 'G01'}">非課税奨学金</c:when>
			<c:when test="${code eq 'H01'}">無報酬の委員等が受ける手当</c:when>
			<c:when test="${code eq 'H02'}">日直・宿直手当等</c:when>
			<c:when test="${code eq 'H03'}">自家用車運転補助金</c:when>
			<c:when test="${code eq 'H04'}">法令により着用する制服等</c:when>
			<c:when test="${code eq 'H05'}">警護手当・乗船手当等</c:when>
			<c:when test="${code eq 'H06'}">研究補助費（幼児教育・初中等教育機関）</c:when>
			<c:when test="${code eq 'H07'}">研究補助費（高等教育機関）</c:when>
			<c:when test="${code eq 'H08'}">研究補助費（特別法による教育機関）</c:when>
			<c:when test="${code eq 'H09' or code eq 'H10'}">研究補助費</c:when>
			<c:when test="${code eq 'H14'}">保育教師勤務環境改善費</c:when>
			<c:when test="${code eq 'H15'}">私立幼稚園教師等の人件費</c:when>
			<c:when test="${code eq 'H11'}">取材手当</c:when>
			<c:when test="${code eq 'H12'}">僻地手当</c:when>
			<c:when test="${code eq 'H13'}">災害により受ける給与</c:when>
			<c:when test="${code eq 'H16'}">地方移転機関従事者への移転支援金</c:when>
			<c:when test="${code eq 'I01'}">外国政府・国際機関勤務者の非課税所得</c:when>
			<c:when test="${code eq 'J01'}">国家功労者等が受ける報勲給付金・学習補助費</c:when>
			<c:when test="${code eq 'J10'}">元大統領礼遇法に基づく年金</c:when>
			<c:when test="${code eq 'K01'}">海外駐屯軍人等が受ける給与</c:when>
			<c:when test="${code eq 'L01'}">従軍中に戦死した軍人等の当該課税期間の所得</c:when>
			<c:when test="${code eq 'M01'}">国外勤務所得（月100万ウォン限度）</c:when>
			<c:when test="${code eq 'M02'}">国外勤務所得（月300万ウォン限度）</c:when>
			<c:when test="${code eq 'M03'}">国外勤務所得</c:when>
			<c:when test="${code eq 'N01'}">国民健康保険法等に基づく事業主負担金</c:when>
			<c:when test="${code eq 'O01'}">生産職従事者等の深夜勤務手当等</c:when>
			<c:when test="${code eq 'P01'}">非課税食事手当（月20万ウォン以下）</c:when>
			<c:when test="${code eq 'P02'}">現物給食</c:when>
			<c:when test="${code eq 'Q01'}">出産・6歳以下の子どもの保育関連所得</c:when>
			<c:when test="${code eq 'R01'}">国軍捕虜が受ける報酬等</c:when>
			<c:when test="${code eq 'R10'}">教育基本法に基づく奨学金</c:when>
			<c:when test="${code eq 'S01'}">ストックオプション所得</c:when>
			<c:when test="${code eq 'Y02'}">従業員持株組合引出金（50％）</c:when>
			<c:when test="${code eq 'Y03'}">従業員持株組合引出金（75％）</c:when>
			<c:when test="${code eq 'Y21'}">長期未就業者の中小企業就職所得（廃止）</c:when>
			<c:when test="${code eq 'Y22'}">専攻医研修補助手当</c:when>
			<c:when test="${code eq 'T01'}">外国人技術者の所得税免除</c:when>
			<c:when test="${code eq 'T10'}">中小企業に就職した若者の所得税減免</c:when>
			<c:when test="${code eq 'T20'}">租税条約の教職員条項による所得税減免</c:when>
			<c:otherwise><c:out value="${value}" /></c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise><c:out value="${value}" /></c:otherwise>
</c:choose>
