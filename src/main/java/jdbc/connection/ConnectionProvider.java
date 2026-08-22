package jdbc.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// DBCPInitListener에서 구동 시 미리 만들어둔 커넥션 풀에서 DB 연결 객체를 꺼내 쓰기 위한 제공자(Provider) 클래스
// DB 연결제공자 정보를 보관하고 관련 기능에서 사용할 수 있도록 제공한다.
// DB接続プロバイダー情報を保持し、関連機能から利用できるように提供する。
public class ConnectionProvider {
    
    // 'payzon'이라는 이름으로 등록된 아파치 커넥션 풀(DBCP)을 찾아, 현재 사용 가능한 커넥션(Connection)을 하나 반환하는 메서드
    // DB 연결제공자 객체에 저장된 DB 연결 값을 반환한다.
    // 저장된 필드를 외부에 직접 노출하지 않고 접근 메서드를 통해 필요한 계층에 제공한다.
    // DB接続プロバイダーオブジェクトに保存されたDB接続の値を返す。
    // 保持しているフィールドを直接公開せず、アクセサーメソッドを通して必要な階層へ提供する。
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:apache:commons:dbcp:payzon");
    }
}
