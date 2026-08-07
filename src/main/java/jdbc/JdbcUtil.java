package jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// JDBC 연동 과정에서 사용한 자원(Connection, Statement, ResultSet)을 안전하게 닫거나 롤백하기 위한 공통 유틸리티 클래스
public class JdbcUtil {

    // ResultSet(조회 결과 데이터) 객체가 null이 아닐 경우 예외 발생 없이 안전하게 자원을 해제하는 메서드
    public static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ex) {
            }
        }
    }

    // Statement(또는 PreparedStatement, 실행할 SQL 쿼리문) 객체가 null이 아닐 경우 예외 발생 없이 안전하게 자원을 해제하는 메서드
    public static void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ex) {
            }
        }
    }

    // Connection(DB 연결) 객체가 null이 아닐 경우 예외 발생 없이 커넥션 풀로 반환(또는 연결 종료)하는 메서드
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
            }
        }
    }

    // 트랜잭션 처리 중 오류가 발생했을 때, 지금까지 실행된 DB 작업들을 취소하고 안전하게 이전 상태로 되돌리는(롤백) 메서드
    public static void rollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
            }
        }
    }
}