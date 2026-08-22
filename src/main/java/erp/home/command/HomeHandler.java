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
// 홈 화면의 HTTP 요청을 구분하고 적절한 서비스 호출과 화면 이동을 담당한다.
// ホーム画面のHTTPリクエストを判別し、適切なサービス呼び出しと画面遷移を担当する。
public class HomeHandler implements CommandHandler {

	private static final String VIEW = "/WEB-INF/view/home/home.jsp";
	private final HomeService homeService = new HomeService();

	@Override
	// 요청 방식과 작업 구분을 확인하여 홈 조회·저장 작업을 적절한 처리로 연결한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエスト方式と処理区分を確認し、ホームの照会・保存処理へ適切に振り分ける。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	public String process(HttpServletRequest req, HttpServletResponse res) {
		if (!"GET".equalsIgnoreCase(req.getMethod())) {
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}

		LocalDate today = LocalDate.now();
		HomeDashboard dashboard = homeService.getDashboard();
		req.setAttribute("dashboard", dashboard);
		req.setAttribute("today", today.format(
				DateTimeFormatter.ofPattern("yyyy年M月d日 EEEE", Locale.JAPANESE)));
		req.setAttribute("currentYear", today.getYear());
		req.setAttribute("currentMonth", today.getMonthValue());
		return VIEW;
	}
}
