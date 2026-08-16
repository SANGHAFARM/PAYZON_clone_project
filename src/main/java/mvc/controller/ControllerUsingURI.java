package mvc.controller;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mvc.command.CommandHandler;
import mvc.command.NullHandler;

// 서블릿이 파일 업로드(multipart/form-data) 요청을 처리할 수 있도록 지정
@MultipartConfig(
	    maxFileSize = 1024 * 1024 * 5,       // 파일 1개당 최대 크기 제한 (5MB)
	    maxRequestSize = 1024 * 1024 * 50    // 전체 폼 요청의 최대 크기 제한 (50MB)
	)
// 사용자의 모든 요청(URI)을 가장 먼저 받아 알맞은 핸들러로 분배해 주는 프론트 컨트롤러 서블릿 클래스
public class ControllerUsingURI extends HttpServlet {

	// 요청 URI(명령어)와 그에 매핑된 실제 핸들러 객체(인스턴스)를 쌍으로 저장해 둘 맵(Map) 자료구조
	private Map<String, CommandHandler> commandHandlerMap = new HashMap<>();

	// 톰캣 서버가 구동되고 이 서블릿이 최초로 생성될 때 한 번 실행되는 초기화 메서드
	public void init() throws ServletException {
		
		// web.xml에 설정된 초기화 파라미터(매핑 설정 파일의 경로) 값을 가져오는 과정
		String configFile = getInitParameter("configFile");
		Properties prop = new Properties();
		
		// 설정 파일의 실제 서버 내 물리적 경로(절대 경로) 구하기
		String configFilePath = getServletContext().getRealPath(configFile);
		try (FileReader fis = new FileReader(configFilePath)) {
			// 물리적 경로에 있는 설정 파일을 읽어들여 키-값 형태로 Properties 객체에 저장
			prop.load(fis);
		} catch (IOException e) {
			throw new ServletException(e);
		}
		
		Iterator keyIter = prop.keySet().iterator();
		
		// 설정 파일에 등록된 명령어(URI)들을 하나씩 순회하며 핸들러 객체를 미리 생성하는 반복문
		while (keyIter.hasNext()) {
			String command = (String) keyIter.next();
			String handlerClassName = prop.getProperty(command);
			try {
				// 문자열로 된 클래스 이름을 이용해 실제 클래스 정보를 메모리에 로드
				Class<?> handlerClass = Class.forName(handlerClassName);
				// 로드된 클래스의 기본 생성자를 호출하여 실제 핸들러 객체(인스턴스) 생성
				CommandHandler handlerInstance = (CommandHandler) handlerClass.getDeclaredConstructor().newInstance();
				
				// 생성된 핸들러 객체를 URI 명령어와 짝지어 맵에 보관 (이후 요청 시 빠르게 꺼내 쓰기 위함)
				commandHandlerMap.put(command, handlerInstance);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				throw new ServletException(e);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// GET 방식의 요청이 들어왔을 때 공통 처리 메서드인 process()를 호출
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}

	// POST 방식의 요청이 들어왔을 때 공통 처리 메서드인 process()를 호출
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		process(request, response);
	}

	// 사용자의 요청 주소를 분석하고 맵에서 알맞은 핸들러를 찾아 실행한 뒤, 결과 화면(View)으로 이동시키는 핵심 처리 메서드
	private void process(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// 사용자가 요청한 전체 URI 문자열 추출 (예: /payzon/board/list.do)
		String command = request.getRequestURI();
		
		// 전체 URI에서 컨텍스트 패스(프로젝트명) 부분을 잘라내어 순수한 식별용 명령어(예: /board/list.do)만 남기는 과정
		if (command.indexOf(request.getContextPath()) == 0) {
			command = command.substring(request.getContextPath().length());
		}
		
		// 정제된 명령어를 키(Key)로 사용하여 맵에서 미리 생성해 둔 핸들러 객체 검색
		CommandHandler handler = commandHandlerMap.get(command);
		
		// 요청한 명령어에 해당하는 핸들러가 맵에 없을 경우, 404 에러를 처리하는 NullHandler 객체 할당
		if (handler == null) {
			handler = new NullHandler();
		}
		
		String viewPage = null;
		try {
			// 찾아낸 핸들러의 process() 메서드를 실행하여 실제 비즈니스 로직을 수행하고, 이동할 JSP 페이지의 경로 반환
			viewPage = handler.process(request, response);
		} catch (Throwable e) {
			throw new ServletException(e);
		}
		
		// 반환된 화면(JSP) 경로가 존재할 경우, 해당 페이지로 요청의 흐름을 넘겨(포워딩) 실제 화면 출력
		if (viewPage != null) {
			RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
			dispatcher.forward(request, response);
		}
	}
}