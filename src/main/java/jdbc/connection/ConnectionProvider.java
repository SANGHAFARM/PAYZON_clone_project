package jdbc.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// DBCPInitListener에서 구동 시 미리 만들어둔 커넥션 풀에서 DB 연결 객체를 꺼내 쓰기 위한 제공자(Provider) 클래스
public class ConnectionProvider {
    
    // 'payzon'이라는 이름으로 등록된 아파치 커넥션 풀(DBCP)을 찾아, 현재 사용 가능한 커넥션(Connection)을 하나 반환하는 메서드
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:apache:commons:dbcp:payzon");
    }
}