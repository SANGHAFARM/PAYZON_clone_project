package erp.settings.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import erp.settings.service.PayItemSettingService;

/**
 * 웹 애플리케이션 시작 시 필수 지급·공제항목을 보장한다.
 */
// 웹 애플리케이션 시작과 종료 시 필수값지급항목Init 초기화 작업을 수행한다.
// Webアプリケーションの起動・終了時に必須値支給項目Initの初期化処理を実行する。
public class RequiredPayItemInitListener implements ServletContextListener {

	@Override
	// 웹 애플리케이션 시작 시 데이터베이스 연결과 필수 기준정보를 초기화한다.
	// 서버 생명주기에 맞춰 공통 자원을 한 번만 준비하거나 종료 시 해제하여 중복 초기화와 자원 누수를 방지한다.
	// Webアプリケーション起動時にデータベース接続と必須マスター情報を初期化する。
	// サーバーのライフサイクルに合わせて共通リソースを一度だけ準備または終了時に解放し、重複初期化とリソース漏れを防止する。
	public void contextInitialized(ServletContextEvent sce) {
		PayItemSettingService.getInstance().initializeRequiredItems();
	}

	@Override
	// 웹 애플리케이션 종료 시 초기화된 공통 자원을 안전하게 정리한다.
	// 서버 생명주기에 맞춰 공통 자원을 한 번만 준비하거나 종료 시 해제하여 중복 초기화와 자원 누수를 방지한다.
	// Webアプリケーション終了時に初期化済みの共通リソースを安全に整理する。
	// サーバーのライフサイクルに合わせて共通リソースを一度だけ準備または終了時に解放し、重複初期化とリソース漏れを防止する。
	public void contextDestroyed(ServletContextEvent sce) {
	}
}
