package util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

// 클라이언트의 요청 데이터를 지정된 문자셋으로 인코딩하여 한글 깨짐을 방지하는 공통 필터 클래스
// 컨트롤러 실행 전에 문자인코딩 요청을 공통 처리한다.
// コントローラー実行前に文字エンコーディングリクエストを共通処理する。
public class CharacterEncodingFilter implements Filter {

    // 적용할 인코딩 방식(문자셋 이름)을 보관할 변수
    // 業務処理で共有する値をフィールドへ保持し、必要な階層から参照・変更できるようにする。
    private String encoding;

    // 실제 필터링 작업이 수행되는 메서드로, 요청 데이터의 인코딩을 설정한 후 다음 목적지로 요청을 전달하는 역할
    // 検索語と選択条件を組み合わせ、利用者が指定した対象だけを照会する。
    @Override
    // 요청 문자 인코딩을 설정한 뒤 다음 필터 또는 컨트롤러로 요청을 전달한다.
    // 전달받은 값과 문자인코딩의 현재 상태를 기준으로 처리하며 호출자가 바로 사용할 수 있는 결과를 반환하거나 상태를 반영한다.
    // リクエストの文字エンコーディングを設定し、次のフィルターまたはコントローラーへ渡す。
    // 受け取った値と文字エンコーディングの現在状態を基準に処理し、呼び出し側が直ちに使用できる結果を返すか状態へ反映する。
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        
        // 클라이언트가 보낸 텍스트 데이터(파라미터 등)의 문자 인코딩을 지정된 값으로 설정
        // リクエストから入力値を取得し、空白除去と形式変換を行って業務処理へ渡す。
        req.setCharacterEncoding(encoding);
        
        // 인코딩 설정 작업이 끝난 후, 다음 순서의 필터나 최종 목적지(프론트 컨트롤러 서블릿 등)로 요청과 응답 흐름을 전달
        // リクエストURIに対応するHandlerを特定して実行し、処理結果を適切な画面へ渡す。
        chain.doFilter(req, res);
    }

    // 톰캣 서버가 구동될 때 필터 객체가 생성되면서 최초 한 번 실행되는 초기화 메서드
    // 検索語と選択条件を組み合わせ、利用者が指定した対象だけを照会する。
    @Override
    // 문자인코딩 처리에 사용할 문자인코딩 데이터나 객체를 생성한다.
    // 서버 생명주기에 맞춰 공통 자원을 한 번만 준비하거나 종료 시 해제하여 중복 초기화와 자원 누수를 방지한다.
    // 文字エンコーディング処理で使用する文字エンコーディングデータまたはオブジェクトを生成する。
    // サーバーのライフサイクルに合わせて共通リソースを一度だけ準備または終了時に解放し、重複初期化とリソース漏れを防止する。
    public void init(FilterConfig config) throws ServletException {
        
        // web.xml의 <filter> 태그 내부에 설정된 'encoding' 파라미터 값을 읽어와 변수에 저장
        // リクエストから入力値を取得し、空白除去と形式変換を行って業務処理へ渡す。
        encoding = config.getInitParameter("encoding");
        
        // web.xml에 따로 설정된 인코딩 값이 없을 경우, 기본값으로 'UTF-8'을 지정하는 안전장치
        // 処理区分と現在状態を確認し、条件に合う業務処理だけを実行する。
        if (encoding == null) {
            encoding = "UTF-8";
        }
    }

    // 웹 애플리케이션 종료 시 필터 객체가 메모리에서 소멸될 때 호출되는 메서드 (현재는 해제할 자원이 없어 비어있음)
    // 使用済みのJDBCリソースを安全に閉じ、接続漏れやリソース漏れを防止する。
    @Override
    // 선택되거나 식별된 문자인코딩 데이터를 삭제하고 관련 상태를 정리한다.
    // 서버 생명주기에 맞춰 공통 자원을 한 번만 준비하거나 종료 시 해제하여 중복 초기화와 자원 누수를 방지한다.
    // 選択または識別された文字エンコーディングデータを削除し、関連状態を整理する。
    // サーバーのライフサイクルに合わせて共通リソースを一度だけ準備または終了時に解放し、重複初期化とリソース漏れを防止する。
    public void destroy() {
    }

}
