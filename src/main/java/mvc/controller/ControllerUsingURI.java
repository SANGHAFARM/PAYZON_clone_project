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
// 対象一覧を順番に処理し、各行の入力値または照会結果を同じ基準で構成する。
@MultipartConfig(
	    maxFileSize = 1024 * 1024 * 5,       // 파일 1개당 최대 크기 제한 (5MB)
	    maxRequestSize = 1024 * 1024 * 50    // 전체 폼 요청의 최대 크기 제한 (50MB)
	)
// 사용자의 모든 요청(URI)을 가장 먼저 받아 알맞은 핸들러로 분배해 주는 프론트 컨트롤러 서블릿 클래스
// 프론트 컨트롤러UsingURI 정보를 보관하고 관련 기능에서 사용할 수 있도록 제공한다.
// フロントコントローラーUsingURI情報を保持し、関連機能から利用できるように提供する。
public class ControllerUsingURI extends HttpServlet {

	// 요청 URI(명령어)와 그에 매핑된 실제 핸들러 객체(인스턴스)를 쌍으로 저장해 둘 맵(Map) 자료구조
	// リクエストURIに対応するHandlerを特定して実行し、処理結果を適切な画面へ渡す。
	private Map<String, CommandHandler> commandHandlerMap = new HashMap<>();

	// 톰캣 서버가 구동되고 이 서블릿이 최초로 생성될 때 한 번 실행되는 초기화 메서드
	// 프론트 컨트롤러UsingURI 처리에 사용할 프론트 컨트롤러UsingURI 데이터나 객체를 생성한다.
	// 서버 생명주기에 맞춰 공통 자원을 한 번만 준비하거나 종료 시 해제하여 중복 초기화와 자원 누수를 방지한다.
	// フロントコントローラーUsingURI処理で使用するフロントコントローラーUsingURIデータまたはオブジェクトを生成する。
	// サーバーのライフサイクルに合わせて共通リソースを一度だけ準備または終了時に解放し、重複初期化とリソース漏れを防止する。
	public void init() throws ServletException {
		
		// web.xml에 설정된 초기화 파라미터(매핑 설정 파일의 경로) 값을 가져오는 과정
		// リクエストから入力値を取得し、空白除去と形式変換を行って業務処理へ渡す。
		String configFile = getInitParameter("configFile");
		Properties prop = new Properties();
		
		// 설정 파일의 실제 서버 내 물리적 경로(절대 경로) 구하기
		// 選択された画像識別値と保存パスを確認し、登録済み画像を各画面で再利用できるようにする。
		String configFilePath = getServletContext().getRealPath(configFile);
		try (FileReader fis = new FileReader(configFilePath)) {
			// 물리적 경로에 있는 설정 파일을 읽어들여 키-값 형태로 Properties 객체에 저장
			// 選択された画像識別値と保存パスを確認し、登録済み画像を各画面で再利用できるようにする。
			prop.load(fis);
		} catch (IOException e) {
			throw new ServletException(e);
		}
		
		Iterator keyIter = prop.keySet().iterator();
		
		// 설정 파일에 등록된 명령어(URI)들을 하나씩 순회하며 핸들러 객체를 미리 생성하는 반복문
		// リクエストURIに対応するHandlerを特定して実行し、処理結果を適切な画面へ渡す。
		while (keyIter.hasNext()) {
			String command = (String) keyIter.next();
			String handlerClassName = prop.getProperty(command);
			try {
				// 문자열로 된 클래스 이름을 이용해 실제 클래스 정보를 메모리에 로드
				// 設定ファイルに記載されたクラス名から実際のHandlerクラスを動的に読み込む。
				Class<?> handlerClass = Class.forName(handlerClassName);
				// 로드된 클래스의 기본 생성자를 호출하여 실제 핸들러 객체(인스턴스) 생성
				// リクエストURIに対応するHandlerを特定して実行し、処理結果を適切な画面へ渡す。
				CommandHandler handlerInstance = (CommandHandler) handlerClass.getDeclaredConstructor().newInstance();
				
				// 생성된 핸들러 객체를 URI 명령어와 짝지어 맵에 보관 (이후 요청 시 빠르게 꺼내 쓰기 위함)
				// リクエストURIに対応するHandlerを特定して実行し、処理結果を適切な画面へ渡す。
				commandHandlerMap.put(command, handlerInstance);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				throw new ServletException(e);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// GET 방식의 요청이 들어왔을 때 공통 처리 메서드인 process()를 호출
	// HTTP 요청을 공통 처리 메서드로 전달하여 등록된 업무 흐름을 실행한다.
	// 전달받은 값과 프론트 컨트롤러UsingURI의 현재 상태를 기준으로 처리하며 호출자가 바로 사용할 수 있는 결과를 반환하거나 상태를 반영한다.
	// HTTPリクエストを共通処理メソッドへ渡し、登録された業務フローを実行する。
	// 受け取った値とフロントコントローラーUsingURIの現在状態を基準に処理し、呼び出し側が直ちに使用できる結果を返すか状態へ反映する。
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}

	// POST 방식의 요청이 들어왔을 때 공통 처리 메서드인 process()를 호출
	// HTTP 요청을 공통 처리 메서드로 전달하여 등록된 업무 흐름을 실행한다.
	// 전달받은 값과 프론트 컨트롤러UsingURI의 현재 상태를 기준으로 처리하며 호출자가 바로 사용할 수 있는 결과를 반환하거나 상태를 반영한다.
	// HTTPリクエストを共通処理メソッドへ渡し、登録された業務フローを実行する。
	// 受け取った値とフロントコントローラーUsingURIの現在状態を基準に処理し、呼び出し側が直ちに使用できる結果を返すか状態へ反映する。
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		process(request, response);
	}

	// 사용자의 요청 주소를 분석하고 맵에서 알맞은 핸들러를 찾아 실행한 뒤, 결과 화면(View)으로 이동시키는 핵심 처리 메서드
	// 요청 방식과 작업 구분을 확인하여 프론트 컨트롤러UsingURI 조회·저장 작업을 적절한 처리로 연결한다.
	// 전달받은 값과 프론트 컨트롤러UsingURI의 현재 상태를 기준으로 처리하며 호출자가 바로 사용할 수 있는 결과를 반환하거나 상태를 반영한다.
	// リクエスト方式と処理区分を確認し、フロントコントローラーUsingURIの照会・保存処理へ適切に振り分ける。
	// 受け取った値とフロントコントローラーUsingURIの現在状態を基準に処理し、呼び出し側が直ちに使用できる結果を返すか状態へ反映する。
	private void process(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// 사용자가 요청한 전체 URI 문자열 추출 (예: /payzon/board/list.do)
		// 受信した完全なURIからコンテキストパスを分離するため、元のリクエストURIを取得する。
		String command = request.getRequestURI();
		
		// 전체 URI에서 컨텍스트 패스(프로젝트명) 부분을 잘라내어 순수한 식별용 명령어(예: /board/list.do)만 남기는 과정
		// データを一意に識別するキーを発行または適用し、対象レコードを正確に処理する。
		if (command.indexOf(request.getContextPath()) == 0) {
			command = command.substring(request.getContextPath().length());
		}
		
		// 정제된 명령어를 키(Key)로 사용하여 맵에서 미리 생성해 둔 핸들러 객체 검색
		// リクエストURIに対応するHandlerを特定して実行し、処理結果を適切な画面へ渡す。
		CommandHandler handler = commandHandlerMap.get(command);
		
		// 요청한 명령어에 해당하는 핸들러가 맵에 없을 경우, 404 에러를 처리하는 NullHandler 객체 할당
		// 未入力値とNULLを区別して正規化し、制約違反や不要な値の保存を防止する。
		if (handler == null) {
			handler = new NullHandler();
		}
		
		String viewPage = null;
		try {
			// 찾아낸 핸들러의 process() 메서드를 실행하여 실제 비즈니스 로직을 수행하고, 이동할 JSP 페이지의 경로 반환
			// リクエストURIに対応するHandlerを特定して実行し、処理結果を適切な画面へ渡す。
			viewPage = handler.process(request, response);
		} catch (Throwable e) {
			throw new ServletException(e);
		}
		
		// 반환된 화면(JSP) 경로가 존재할 경우, 해당 페이지로 요청의 흐름을 넘겨(포워딩) 실제 화면 출력
		// 画面表示に必要なデータとメッセージをrequestまたはsessionへ保存し、JSPへ引き渡す。
		if (viewPage != null) {
			RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
			dispatcher.forward(request, response);
		}
	}
}
