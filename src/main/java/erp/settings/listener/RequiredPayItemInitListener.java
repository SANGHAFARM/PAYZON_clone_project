package erp.settings.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import erp.settings.service.PayItemSettingService;

/**
 * 웹 애플리케이션 시작 시 필수 지급·공제항목을 보장한다.
 */
public class RequiredPayItemInitListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		PayItemSettingService.getInstance().initializeRequiredItems();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}
}
