package mvc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// MVC 패턴에서 각 요청(URI)을 처리할 핸들러(비즈니스 로직 처리 클래스)들이 공통으로 구현해야 하는 규격을 미리 정의해 둔 인터페이스
// 요청경로 화면의 HTTP 요청을 구분하고 적절한 서비스 호출과 화면 이동을 담당한다.
// リクエスト経路画面のHTTPリクエストを判別し、適切なサービス呼び出しと画面遷移を担当する。
public interface CommandHandler {
    
    // 클라이언트의 요청(Request)을 받아 알맞은 로직을 수행한 뒤, 최종적으로 보여줄 화면(View, JSP)의 경로를 문자열(String)로 반환하는 추상 메서드
    // 요청 방식과 작업 구분을 확인하여 요청경로 조회·저장 작업을 적절한 처리로 연결한다.
    // 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
    // リクエスト方式と処理区分を確認し、リクエスト経路の照会・保存処理へ適切に振り分ける。
    // リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
    public String process(HttpServletRequest req, HttpServletResponse res) throws Exception;
}
