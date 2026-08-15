package erp.home.command;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.home.dto.HomeDashboard;
import erp.home.service.HomeService;
import mvc.command.CommandHandler;

// 홈 대시보드 조회 요청을 처리한다.
public class HomeHandler implements CommandHandler {

	private static final String VIEW = "/WEB-INF/view/home/home.jsp";
	private final HomeService homeService = new HomeService();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) {
		if (!"GET".equalsIgnoreCase(req.getMethod())) {
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}

		LocalDate today = LocalDate.now();
		HomeDashboard dashboard = homeService.getDashboard();
		req.setAttribute("dashboard", dashboard);
		req.setAttribute("today", today.format(
				DateTimeFormatter.ofPattern("yyyy년 M월 d일 EEEE", Locale.KOREAN)));
		req.setAttribute("currentYear", today.getYear());
		req.setAttribute("currentMonth", today.getMonthValue());
		return VIEW;
	}
}
