package util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

// 클라이언트의 요청 데이터를 지정된 문자셋으로 인코딩하여 한글 깨짐을 방지하는 공통 필터 클래스
public class CharacterEncodingFilter implements Filter {

    // 적용할 인코딩 방식(문자셋 이름)을 보관할 변수
    private String encoding;

    // 실제 필터링 작업이 수행되는 메서드로, 요청 데이터의 인코딩을 설정한 후 다음 목적지로 요청을 전달하는 역할
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        
        // 클라이언트가 보낸 텍스트 데이터(파라미터 등)의 문자 인코딩을 지정된 값으로 설정
        req.setCharacterEncoding(encoding);
        
        // 인코딩 설정 작업이 끝난 후, 다음 순서의 필터나 최종 목적지(프론트 컨트롤러 서블릿 등)로 요청과 응답 흐름을 전달
        chain.doFilter(req, res);
    }

    // 톰캣 서버가 구동될 때 필터 객체가 생성되면서 최초 한 번 실행되는 초기화 메서드
    @Override
    public void init(FilterConfig config) throws ServletException {
        
        // web.xml의 <filter> 태그 내부에 설정된 'encoding' 파라미터 값을 읽어와 변수에 저장
        encoding = config.getInitParameter("encoding");
        
        // web.xml에 따로 설정된 인코딩 값이 없을 경우, 기본값으로 'UTF-8'을 지정하는 안전장치
        if (encoding == null) {
            encoding = "UTF-8";
        }
    }

    // 웹 애플리케이션 종료 시 필터 객체가 메모리에서 소멸될 때 호출되는 메서드 (현재는 해제할 자원이 없어 비어있음)
    @Override
    public void destroy() {
    }

}