package erp.settings.command;

import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import erp.settings.service.CompanyImageService;
import mvc.command.CommandHandler;

public class CompanyImageHandler implements CommandHandler {

	private CompanyImageService imageService = CompanyImageService.getInstance();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// 비동기 통신이므로 한글 깨짐 방지를 위해 응답 헤더 설정
		res.setContentType("application/json; charset=UTF-8");
		PrintWriter out = res.getWriter();

		int companyId = 1;
		// 클라이언트에서 넘어온 동작 파라미터 (upload 또는 delete)
		String action = req.getParameter("action");
		// 클라이언트에서 넘어온 이미지 타입 파라미터 (logo 또는 stamp)
		String imageType = req.getParameter("imageType");

		try {
			if ("upload".equals(action)) {
				// ========================================================
				// [TODO] Servlet 3.0 Part 또는 COS 라이브러리를 활용한 물리적 파일 저장 로직
				// 1. request.getPart("imageFile") 로 파일을 꺼냄
				// 2. 서버의 특정 경로(예: /upload/images/1_logo.png)에 파일 쓰기 수행
				// ========================================================
				String savedFilePath = "/upload/images/" + companyId + "_" + imageType + ".png"; // 임시 가짜 경로

				// 서비스 호출하여 DB에 경로 저장
				imageService.uploadImage(companyId, imageType, savedFilePath);

				out.print("{\"result\":\"success\", \"message\":\"이미지가 정상적으로 등록되었습니다.\", \"path\":\"" + savedFilePath
						+ "\"}");

			} else if ("delete".equals(action)) {
				// 서비스 호출하여 DB에서 경로 삭제 (null 처리)
				imageService.deleteImage(companyId, imageType);

				// [TODO] 서버에 저장된 물리적인 실제 이미지 파일도 File 객체를 통해 삭제하는 로직 필요

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
		return null;
	}
}