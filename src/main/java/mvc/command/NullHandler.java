package mvc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 사용자가 요청한 주소(URI)에 매핑된 핸들러를 찾을 수 없을 때, 기본적으로 실행될 빈(Null) 핸들러 클래스
public class NullHandler implements CommandHandler {

    // 존재하지 않는 요청이 들어왔음을 알리기 위해 브라우저에 404(Not Found) 에러 상태 코드를 전송하고, 보여줄 화면(View)이 없으므로 null을 반환하는 메서드
    @Override
    public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
        
        res.sendError(HttpServletResponse.SC_NOT_FOUND);
        
        return null;
    }
}