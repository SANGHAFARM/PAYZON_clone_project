package jdbc;

import java.io.IOException;
import java.io.StringReader;
import java.sql.DriverManager;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDriver;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

// 웹 애플리케이션 구동 시 데이터베이스 커넥션 풀(DBCP)을 초기화하기 위한 리스너 클래스
public class DBCPInitListener implements ServletContextListener {

	// 웹 서버(톰캣) 시작 시 자동으로 호출되는 메서드
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// web.xml에 설정된 'poolConfig' 파라미터 값(문자열)을 읽어오는 과정
		String poolConfig = sce.getServletContext().getInitParameter("poolConfig");
		Properties prop = new Properties();
		try {
			// 읽어온 문자열을 줄 단위로 분석하여 Key-Value 형태의 Properties 객체로 변환
			prop.load(new StringReader(poolConfig));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		// 추출한 속성값을 바탕으로 JDBC 드라이버 로드 및 커넥션 풀 생성 메서드 호출
		loadJDBCDriver(prop);
		initConnectionPool(prop);
	}

	// Properties에 설정된 드라이버 클래스 이름으로 JDBC 드라이버를 메모리에 로드하는 메서드
	private void loadJDBCDriver(Properties prop) {
		String driverClass = prop.getProperty("jdbcdriver");
		try {
			Class.forName(driverClass);
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException("fail to load JDBC Driver", ex);
		}
	}

	// 커넥션 풀을 생성하고 세부 환경을 설정하는 핵심 메서드
	private void initConnectionPool(Properties prop) {
		try {
			// DB 연결에 필요한 기본 정보(URL, 아이디, 비밀번호) 추출
			String jdbcUrl = prop.getProperty("jdbcUrl");
			String username = prop.getProperty("dbUser");
			String pw = prop.getProperty("dbPass");

			// 실제 DB와의 물리적인 연결(Connection)을 생성하는 팩토리 객체
			ConnectionFactory connFactory = new DriverManagerConnectionFactory(jdbcUrl, username, pw);

			// 생성된 커넥션을 풀(Pool)에서 관리할 수 있는 형태(PoolableConnection)로 감싸주는 팩토리 객체
			PoolableConnectionFactory poolableConnFactory = new PoolableConnectionFactory(connFactory, null);
			
			// DB 연결이 정상인지 확인하기 위한 검증용 쿼리 설정
			String validationQuery = prop.getProperty("validationQuery");
			if (validationQuery != null && !validationQuery.isEmpty()) {
				poolableConnFactory.setValidationQuery(validationQuery); // 변수명 오타 수정 반영 영역
			}

			// 커넥션 풀의 동작 방식과 옵션을 제어하는 설정 객체
			GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
			// 유휴(대기) 커넥션을 검사하는 주기 설정 (5분)
			poolConfig.setTimeBetweenEvictionRunsMillis(1000L * 60L * 5L);
			// 대기 중인 커넥션이 유효한지 검사하도록 설정
			poolConfig.setTestWhileIdle(true);
			
			// 풀에 유지할 최소 커넥션 개수 설정 (기본값 5)
			int minIdle = getIntProperty(prop, "minIdle", 5);
			poolConfig.setMinIdle(minIdle);
			
			// 풀이 생성할 수 있는 최대 커넥션 개수 설정 (기본값 50)
			int maxTotal = getIntProperty(prop, "maxTotal", 50);
			poolConfig.setMaxTotal(maxTotal);

			// 앞서 설정한 팩토리와 옵션을 바탕으로 실제 커넥션들을 담아둘 풀(Pool) 객체 생성
			GenericObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<>(poolableConnFactory, poolConfig);
			// 팩토리 객체에 방금 만든 커넥션 풀을 연결
			poolableConnFactory.setPool(connectionPool);

			// 커넥션 풀을 JDBC 드라이버처럼 사용할 수 있게 해주는 PoolingDriver 로드
			Class.forName("org.apache.commons.dbcp2.PoolingDriver");
			PoolingDriver driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
			
			// web.xml에서 지정한 이름(poolName)으로 톰캣 시스템에 완성된 커넥션 풀을 등록
			String poolName = prop.getProperty("poolName");
			driver.registerPool(poolName, connectionPool);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// Properties에서 정수형(int) 값을 안전하게 변환하여 가져오기 위한 유틸리티 메서드 (값이 없으면 기본값 반환)
	private int getIntProperty(Properties prop, String propName, int defaultValue) {
		String value = prop.getProperty(propName);
		if (value == null)
			return defaultValue;

		return Integer.parseInt(value);
	}

	// 웹 애플리케이션 종료 시 호출되는 메서드 (자원 해제 등을 처리할 수 있으나 현재는 비어있음)
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

}