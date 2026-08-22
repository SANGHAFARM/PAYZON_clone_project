package mvc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 사용자가 요청한 주소(URI)에 매핑된 핸들러를 찾을 수 없을 때, 기본적으로 실행될 빈(Null) 핸들러 클래스
// 빈 값 화면의 HTTP 요청을 구분하고 적절한 서비스 호출과 화면 이동을 담당한다.
// 空値画面のHTTPリクエストを判別し、適切なサービス呼び出しと画面遷移を担当する。
public class NullHandler implements CommandHandler {

    // 존재하지 않는 요청이 들어왔음을 알리기 위해 브라우저에 404(Not Found) 에러 상태 코드를 전송하고, 보여줄 화면(View)이 없으므로 null을 반환하는 메서드
    // 未入力値とNULLを区別して正規化し、制約違反や不要な値の保存を防止する。
    @Override
    // 요청 방식과 작업 구분을 확인하여 빈 값 조회·저장 작업을 적절한 처리로 연결한다.
    // 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
    // リクエスト方式と処理区分を確認し、空値の照会・保存処理へ適切に振り分ける。
    // リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
    public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
        
        res.sendError(HttpServletResponse.SC_NOT_FOUND);
        
        return null;
    }
}
