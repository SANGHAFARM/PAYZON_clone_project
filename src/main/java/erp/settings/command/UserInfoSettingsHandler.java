package erp.settings.command;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import erp.settings.model.Company;
import erp.settings.model.Department;
import erp.settings.model.JobPosition;
import erp.settings.service.CompanyInfoService;
import erp.settings.service.CompanyImageService;
import erp.settings.service.DepartmentPositionService;
import mvc.command.CommandHandler;

public class UserInfoSettingsHandler implements CommandHandler {

	// 비즈니스 로직 처리를 위한 서비스 객체들을 싱글톤으로 불러옵니다.
	private CompanyInfoService companyService = CompanyInfoService.getInstance();
	private CompanyImageService imageService = CompanyImageService.getInstance();
	private DepartmentPositionService deptPosService = DepartmentPositionService.getInstance();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// HTTP 요청 방식에 따라 화면 렌더링(GET)과 데이터 처리(POST)를 분기합니다.
		if (req.getMethod().equalsIgnoreCase("GET")) {
			return processForm(req, res);
		} else if (req.getMethod().equalsIgnoreCase("POST")) {
			return processAction(req, res);
		}
		return null;
	}

	// [GET] 화면 렌더링을 위한 데이터베이스 조회 및 바인딩 로직
	private String processForm(HttpServletRequest req, HttpServletResponse res) throws Exception {
		int companyId = 1;

		// 데이터베이스에서 실제 등록된 회사, 부서, 직위 정보를 조회합니다.
		Company companyInfo = companyService.getCompanyDetails(companyId);
		List<Department> departmentList = deptPosService.getDepartmentOptions();
		List<JobPosition> positionList = deptPosService.getJobPositionOptions();

		// JSP 화면에서 JSTL을 통해 사용할 수 있도록 Request 객체에 데이터를 담아줍니다.
		req.setAttribute("company", companyInfo);
		req.setAttribute("departmentList", departmentList);
		req.setAttribute("positionList", positionList);

		return "/WEB-INF/view/settings/user-info.jsp";
	}

	// [POST] 폼 전송(Submit) 시 action 파라미터 값에 따른 분기 처리 로직
	private String processAction(HttpServletRequest req, HttpServletResponse res) throws Exception {
		req.setCharacterEncoding("UTF-8");

		// JSP 폼 버튼에서 넘어온 기능 구분 값 (save, saveLogo, deleteLogo 등)
		String action = req.getParameter("action");
		int companyId = 1;

		try {
			if ("save".equals(action)) {
				// 회사 텍스트 정보 전체 저장
				saveCompanyTextInfo(req);
				req.getSession().setAttribute("message", "기본환경설정이 성공적으로 저장되었습니다.");

			} else if ("saveLogo".equals(action)) {
				// 로고 파일 업로드 및 DB 경로 갱신
				Part filePart = req.getPart("logoFile");
				// 물리적 파일 저장 처리 위치
				String savedPath = "/upload/logo_" + companyId + ".png";
				imageService.uploadImage(companyId, "logo", savedPath);
				req.getSession().setAttribute("message", "로고 이미지가 등록되었습니다.");

			} else if ("deleteLogo".equals(action)) {
				// 로고 삭제 및 DB 경로 초기화
				imageService.deleteImage(companyId, "logo");
				req.getSession().setAttribute("message", "로고 이미지가 삭제되었습니다.");

			} else if ("saveStamp".equals(action)) {
				// 도장 파일 업로드 및 DB 경로 갱신
				Part filePart = req.getPart("stampFile");
				String savedPath = "/upload/stamp_" + companyId + ".png";
				imageService.uploadImage(companyId, "stamp", savedPath);
				req.getSession().setAttribute("message", "도장 이미지가 등록되었습니다.");

			} else if ("deleteStamp".equals(action)) {
				// 도장 삭제 및 DB 경로 초기화
				imageService.deleteImage(companyId, "stamp");
				req.getSession().setAttribute("message", "도장 이미지가 삭제되었습니다.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			req.getSession().setAttribute("message", "처리 중 오류가 발생했습니다.");
		}

		// 로직 처리가 완료되면 POST 중복 전송 방지를 위해 GET 방식의 조회 페이지로 리다이렉트합니다.
		res.sendRedirect(req.getContextPath() + "/settings/user-info.do");
		return null;
	}

	// 화면에서 입력받은 회사 기본정보를 추출하여 DB에 저장하는 헬퍼 메서드
	private void saveCompanyTextInfo(HttpServletRequest req) throws Exception {
		Company company = new Company();

		company.setCompanyId(1);
		company.setCmpnName(req.getParameter("cmpnName"));
		company.setCeoTitle(req.getParameter("ceoTitle"));
		company.setCeoName(req.getParameter("ceoName"));
		company.setBizRegNo(req.getParameter("bizRegNo"));
		company.setCorpRegNo(req.getParameter("corpRegNo"));

		String foundationDateStr = req.getParameter("foundationDate");
		if (foundationDateStr != null && !foundationDateStr.trim().isEmpty()) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			company.setFoundationDate(sdf.parse(foundationDateStr));
		}

		company.setHomepageUrl(req.getParameter("homepageUrl"));
		company.setZipCode(req.getParameter("zipCode"));
		company.setAddress(req.getParameter("address"));
		company.setTelNo(req.getParameter("telNo"));
		company.setFaxNo(req.getParameter("faxNo"));
		company.setBizType(req.getParameter("bizType"));
		company.setBizItem(req.getParameter("bizItem"));

		company.setManagerName(req.getParameter("managerName"));
		company.setManagerDeptName(req.getParameter("managerDeptName"));
		company.setManagerPosName(req.getParameter("managerPosName"));
		company.setManagerTelNo(req.getParameter("managerTelNo"));
		company.setManagerMobileNo(req.getParameter("managerMobileNo"));
		company.setManagerEmail(req.getParameter("managerEmail"));

		company.setPayCalcStartScope(req.getParameter("payCalcStartScope"));
		company.setPayCalcStartDay(req.getParameter("payCalcStartDay"));
		company.setPayCalcEndScope(req.getParameter("payCalcEndScope"));
		company.setPayCalcEndDay(req.getParameter("payCalcEndDay"));
		company.setPayDateScope(req.getParameter("payDateScope"));
		company.setPayDateDay(req.getParameter("payDateDay"));
		company.setPayBankName(req.getParameter("payBankName"));
		company.setPayAccountNo(req.getParameter("payAccountNo"));
		company.setPayAccountHolder(req.getParameter("payAccountHolder"));

		companyService.saveAllCompanyInfo(company);
	}
}