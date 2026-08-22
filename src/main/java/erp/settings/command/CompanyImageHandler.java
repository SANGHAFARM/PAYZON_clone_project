package erp.settings.command;

import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.settings.service.CompanyImageService;
import mvc.command.CommandHandler;

// 사업장이미지 화면의 HTTP 요청을 구분하고 적절한 서비스 호출과 화면 이동을 담당한다.
// 事業所画像画面のHTTPリクエストを判別し、適切なサービス呼び出しと画面遷移を担当する。
public class CompanyImageHandler implements CommandHandler {

	private CompanyImageService imageService = CompanyImageService.getInstance();

	@Override
	// 요청 방식과 작업 구분을 확인하여 사업장이미지 조회·저장 작업을 적절한 처리로 연결한다.
	// 요청 파라미터를 검증해 Service에 전달하고 처리 결과를 request 또는 session에 저장한 뒤 JSP 포워드나 리다이렉트 경로를 결정한다.
	// リクエスト方式と処理区分を確認し、事業所画像の照会・保存処理へ適切に振り分ける。
	// リクエストパラメーターを検証してServiceへ渡し、処理結果をrequestまたはsessionへ保存した後、JSPフォワードまたはリダイレクト先を決定する。
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// 비동기 통신이므로 한글 깨짐 방지를 위해 응답 헤더 설정
		// 非同期レスポンスの文字化けを防ぐため、UTF-8のContent-Typeと文字エンコーディングを設定する。
		res.setContentType("application/json; charset=UTF-8");
		PrintWriter out = res.getWriter();

		int companyId = 1;
		// 클라이언트에서 넘어온 동작 파라미터 (upload 또는 delete)
		// リクエストから入力値を取得し、空白除去と形式変換を行って業務処理へ渡す。
		String action = req.getParameter("action");
		// 클라이언트에서 넘어온 이미지 타입 파라미터 (logo 또는 stamp)
		// リクエストから入力値を取得し、空白除去と形式変換を行って業務処理へ渡す。
		String imageType = req.getParameter("imageType");

		try {
			if ("upload".equals(action)) {
				// ========================================================
				// [TODO] Servlet 3.0 Part 또는 COS 라이브러리를 활용한 물리적 파일 저장 로직
				// 1. request.getPart("imageFile") 로 파일을 꺼냄
				// 2. 서버의 특정 경로(예: /upload/images/1_logo.png)에 파일 쓰기 수행
				// 選択された画像識別値と保存パスを確認し、登録済み画像を各画面で再利用できるようにする。
				// ========================================================
				String savedFilePath = "/upload/images/" + companyId + "_" + imageType + ".png"; // 임시 가짜 경로

				// 서비스 호출하여 DB에 경로 저장
				// 選択された画像識別値と保存パスを確認し、登録済み画像を各画面で再利用できるようにする。
				imageService.uploadImage(companyId, imageType, savedFilePath);

				out.print("{\"result\":\"success\", \"message\":\"이미지가 정상적으로 등록되었습니다.\", \"path\":\"" + savedFilePath
						+ "\"}");

			} else if ("delete".equals(action)) {
				// 서비스 호출하여 DB에서 경로 삭제 (null 처리)
				// 未入力値とNULLを区別して正規化し、制約違反や不要な値の保存を防止する。
				imageService.deleteImage(companyId, imageType);

				// [TODO] 서버에 저장된 물리적인 실제 이미지 파일도 File 객체를 통해 삭제하는 로직 필요
				// 選択された画像識別値と保存パスを確認し、登録済み画像を各画面で再利用できるようにする。

				out.print("{\"result\":\"success\", \"message\":\"이미지가 삭제되었습니다.\"}");
			} else {
				out.print("{\"result\":\"fail\", \"message\":\"잘못된 요청입니다.\"}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			out.print("{\"result\":\"error\", \"message\":\"서버 오류가 발생했습니다.\"}");
		} finally {
			out.flush();
			out.close();
		}

		// 비동기 요청이므로 View 이동 없이 처리 종료
		// 処理結果に応じて表示対象のJSPまたは次のリクエスト経路へ遷移する。
		return null;
	}
}
