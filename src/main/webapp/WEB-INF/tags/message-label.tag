<%@ tag pageEncoding="UTF-8" body-content="empty" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ attribute name="value" required="false" type="java.lang.String" %>

<%-- 백엔드 안내문은 유지하고 팝업에 출력할 때만 일본어로 변환한다. --%>
<c:choose>
	<c:when test="${value eq '검색어를 2자 이상 입력해주세요' or value eq '검색어를 2자 이상 입력해주세요.'}">検索語を2文字以上入力してください。</c:when>
	<c:when test="${value eq '검색 조건을 하나 이상 설정해주세요.'}">検索条件を1つ以上設定してください。</c:when>
	<c:when test="${value eq '급여내역은 최대 12개월까지 조회할 수 있습니다.'}">給与履歴は最大12か月まで照会できます。</c:when>
	<c:when test="${value eq '급여내역을 조회할 사원을 선택해주세요'}">給与履歴を照会する社員を選択してください。</c:when>
	<c:when test="${value eq '급여내역을 저장할 사원을 선택해주세요'}">給与履歴を保存する社員を選択してください。</c:when>
	<c:when test="${value eq '조회할 항목을 선택해주세요'}">照会する項目を選択してください。</c:when>
	<c:when test="${value eq '삭제할 지급항목을 선택해주세요.'}">削除する支給項目を選択してください。</c:when>
	<c:when test="${value eq '삭제할 공제항목을 선택해주세요.'}">削除する控除項目を選択してください。</c:when>
	<c:when test="${value eq '비과세 항목을 선택해주세요.'}">非課税項目を選択してください。</c:when>
	<c:when test="${value eq '사원을 선택해주세요' or value eq '사원을 선택해주세요.' or value eq '처리할 사원을 선택하세요.'}">社員を選択してください。</c:when>
	<c:when test="${value eq '삭제할 사원을 선택해주세요.'}">削除する社員を選択してください。</c:when>
	<c:when test="${value eq '추가할 사원을 선택해주세요'}">追加する社員を選択してください。</c:when>
	<c:when test="${value eq '급여내역이 없습니다.'}">給与履歴がありません。</c:when>
	<c:when test="${value eq '사원, 발급용도와 발급부서를 모두 선택해주세요.'}">社員、発行用途、発行部署をすべて選択してください。</c:when>
	<c:when test="${value eq '증명서 발급내역을 저장했습니다.'}">証明書の発行履歴を保存しました。</c:when>
	<c:when test="${value eq '선택한 발급내역을 삭제했습니다.'}">選択した発行履歴を削除しました。</c:when>
	<c:when test="${value eq '전체 발급내역을 삭제했습니다.'}">すべての発行履歴を削除しました。</c:when>
	<c:when test="${value eq '삭제할 발급내역을 선택해주세요.'}">削除する発行履歴を選択してください。</c:when>
	<c:when test="${value eq '기본환경설정이 성공적으로 저장되었습니다.'}">基本設定を保存しました。</c:when>
	<c:when test="${value eq '사원 정보가 성공적으로 저장되었습니다.'}">社員情報を保存しました。</c:when>
	<c:when test="${value eq '사원 정보(2)가 저장되었습니다.'}">社員情報2を保存しました。</c:when>
	<c:when test="${value eq '사원정보 1을 먼저 저장해주세요.' or value eq '사원정보 1을 먼저 저장해 주세요.'}">先に社員情報1を保存してください。</c:when>
	<c:when test="${value eq '사원 기본 정보가 없습니다. 1단계를 먼저 완료해 주세요.' or value eq '사원 기본 정보가 존재하지 않습니다. 1단계를 먼저 완료해 주세요.'}">社員の基本情報がありません。先に手順1を完了してください。</c:when>
	<c:when test="${value eq '사진이 등록되었습니다.'}">写真を登録しました。</c:when>
	<c:when test="${value eq '사진이 삭제되었습니다.'}">写真を削除しました。</c:when>
	<c:when test="${value eq '로고 이미지가 등록되었습니다.'}">ロゴ画像を登録しました。</c:when>
	<c:when test="${value eq '로고 이미지가 삭제되었습니다.'}">ロゴ画像を削除しました。</c:when>
	<c:when test="${value eq '도장 이미지가 등록되었습니다.'}">印鑑画像を登録しました。</c:when>
	<c:when test="${value eq '도장 이미지가 삭제되었습니다.'}">印鑑画像を削除しました。</c:when>
	<c:when test="${value eq '지급항목 설정이 완료되었습니다.'}">支給項目の設定が完了しました。</c:when>
	<c:when test="${value eq '공제항목 설정이 완료되었습니다.'}">控除項目の設定が完了しました。</c:when>
	<c:when test="${value eq '퇴직처리를 완료했습니다.'}">退職処理が完了しました。</c:when>
	<c:when test="${value eq '퇴직처리를 취소했습니다.'}">退職処理を取り消しました。</c:when>
	<c:when test="${value eq '퇴직급여 내역을 저장했습니다.'}">退職給付履歴を保存しました。</c:when>
	<c:when test="${value eq '퇴직급여 내역을 삭제했습니다.'}">退職給付履歴を削除しました。</c:when>
	<c:when test="${value eq '정산 종료일은 시작일보다 빠를 수 없습니다.'}">精算終了日は開始日より前に設定できません。</c:when>
	<c:when test="${value eq '제외일수를 확인하세요.'}">除外日数を確認してください。</c:when>
	<c:when test="${fn:contains(value, '오류') or fn:contains(value, '에러') or fn:contains(value, '실패') or fn:contains(value, '못했습니다')}">処理中にエラーが発生しました。入力内容を確認してください。</c:when>
	<c:when test="${fn:contains(value, '이미 등록') or fn:contains(value, '이미 추가')}">すでに登録されている項目です。</c:when>
	<c:when test="${fn:contains(value, '잘못') or fn:contains(value, '올바른') or fn:contains(value, '올바르지')}">入力内容が正しくありません。もう一度確認してください。</c:when>
	<c:when test="${fn:contains(value, '삭제할')}">削除する項目を選択してください。</c:when>
	<c:when test="${fn:contains(value, '등록할')}">登録する項目を選択してください。</c:when>
	<c:when test="${fn:contains(value, '저장해')}">先に必要な情報を保存してください。</c:when>
	<c:when test="${fn:contains(value, '등록해')}">必要な情報を登録してください。</c:when>
	<c:when test="${fn:contains(value, '수정할')}">修正する項目を選択してください。</c:when>
	<c:when test="${fn:contains(value, '저장')}">保存しました。</c:when>
	<c:when test="${fn:contains(value, '등록')}">登録しました。</c:when>
	<c:when test="${fn:contains(value, '삭제')}">削除しました。</c:when>
	<c:when test="${fn:contains(value, '수정')}">修正しました。</c:when>
	<c:when test="${fn:contains(value, '선택')}">必要な項目を選択してください。</c:when>
	<c:when test="${fn:contains(value, '검색') or fn:contains(value, '조회')}">検索条件を確認してください。</c:when>
	<c:when test="${fn:contains(value, '입력')}">入力内容を確認してください。</c:when>
	<c:when test="${fn:contains(value, '사원')}">社員情報を確認してください。</c:when>
	<c:when test="${fn:contains(value, '퇴직')}">退職情報を確認してください。</c:when>
	<c:when test="${fn:contains(value, '급여')}">給与情報を確認してください。</c:when>
	<c:when test="${fn:contains(value, '휴가') or fn:contains(value, '근태')}">勤怠・休暇情報を確認してください。</c:when>
	<c:when test="${fn:contains(value, '이미지') or fn:contains(value, '사진')}">画像情報を確認してください。</c:when>
	<c:when test="${fn:contains(value, '날짜') or fn:contains(value, '일자')}">日付を確認してください。</c:when>
	<c:when test="${fn:contains(value, '교육')}">教育・研修情報を確認してください。</c:when>
	<c:otherwise><c:out value="${value}" /></c:otherwise>
</c:choose>
