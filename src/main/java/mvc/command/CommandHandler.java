package mvc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// MVC 패턴에서 각 요청(URI)을 처리할 핸들러(비즈니스 로직 처리 클래스)들이 공통으로 구현해야 하는 규격을 미리 정의해 둔 인터페이스
public interface CommandHandler {
    
    // 클라이언트의 요청(Request)을 받아 알맞은 로직을 수행한 뒤, 최종적으로 보여줄 화면(View, JSP)의 경로를 문자열(String)로 반환하는 추상 메서드
    public String process(HttpServletRequest req, HttpServletResponse res) throws Exception;
}