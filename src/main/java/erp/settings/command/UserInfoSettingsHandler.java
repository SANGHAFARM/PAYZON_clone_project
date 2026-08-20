package erp.settings.command;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.imageio.ImageIO;

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
	private static final long IMAGE_MAX_SIZE = 1024L * 1024L;
	private static final int IMAGE_MAX_WIDTH = 150;

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
		Object managerMessage = req.getSession().getAttribute("managerMessage");
		if (managerMessage != null) {
			req.setAttribute("managerMessage", managerMessage);
			req.setAttribute("managerType", req.getSession().getAttribute("managerType"));
			req.getSession().removeAttribute("managerMessage");
			req.getSession().removeAttribute("managerType");
		}

		return "/WEB-INF/view/settings/user-info.jsp";
	}

	// [POST] 폼 전송(Submit) 시 action 파라미터 값에 따른 분기 처리 로직
	private String processAction(HttpServletRequest req, HttpServletResponse res) throws Exception {
		req.setCharacterEncoding("UTF-8");

		// JSP 폼 버튼에서 넘어온 기능 구분 값 (save, saveLogo, deleteLogo 등)
		String action = req.getParameter("action");
		int companyId = 1;
		String managerTarget = "";

		try {
			if ("addDepartment".equals(action)) {
				Department department = new Department();
				department.setDepartmentName(trim(req.getParameter("departmentName")));
				deptPosService.addDepartment(department);
				managerTarget = "#department-manager-modal";

			} else if ("updateDepartment".equals(action)) {
				Department department = new Department();
				department.setDepartmentId(parseId(req.getParameter("departmentId")));
				department.setDepartmentName(trim(req.getParameter("departmentName")));
				deptPosService.updateDepartment(department);
				managerTarget = "#department-manager-modal";

			} else if ("requestDeleteDepartment".equals(action)) {
				req.setAttribute("deleteSettingType", "department");
				req.setAttribute("deleteSettingId", parseId(req.getParameter("departmentId")));
				req.setAttribute("deleteSettingName", trim(req.getParameter("departmentName")));
				return processForm(req, res);

			} else if ("deleteDepartment".equals(action)) {
				deptPosService.deleteDepartment(parseId(req.getParameter("departmentId")));
				managerTarget = "#department-manager-modal";

			} else if ("addPosition".equals(action)) {
				JobPosition position = new JobPosition();
				position.setJobPositionName(trim(req.getParameter("positionName")));
				deptPosService.addJobPosition(position);
				managerTarget = "#position-manager-modal";

			} else if ("updatePosition".equals(action)) {
				JobPosition position = new JobPosition();
				position.setJobPositionId(parseId(req.getParameter("positionId")));
				position.setJobPositionName(trim(req.getParameter("positionName")));
				deptPosService.updateJobPosition(position);
				managerTarget = "#position-manager-modal";

			} else if ("requestDeletePosition".equals(action)) {
				req.setAttribute("deleteSettingType", "position");
				req.setAttribute("deleteSettingId", parseId(req.getParameter("positionId")));
				req.setAttribute("deleteSettingName", trim(req.getParameter("positionName")));
				return processForm(req, res);

			} else if ("deletePosition".equals(action)) {
				deptPosService.deleteJobPosition(parseId(req.getParameter("positionId")));
				managerTarget = "#position-manager-modal";

			} else if ("save".equals(action)) {
				// 회사 텍스트 정보 전체 저장
				saveCompanyTextInfo(req);
				req.getSession().setAttribute("message", "기본환경설정이 성공적으로 저장되었습니다.");

			} else if ("saveLogo".equals(action)) {
				// 기본 이미지 선택 시 프로젝트 이미지를 사용하고, 그 외에는 업로드 파일을 저장합니다.
				String savedPath = "payzon".equals(req.getParameter("logoPreset"))
						? "/images/settings/presets/payzon-logo.png"
						: saveImageFile(req, req.getPart("logoFile"), "logo", companyId);
				imageService.uploadImage(companyId, "logo", savedPath);
				req.getSession().setAttribute("message", "로고 이미지가 등록되었습니다.");

			} else if ("deleteLogo".equals(action)) {
				// DB 경로와 서버에 저장된 실제 파일을 함께 삭제합니다.
				imageService.deleteImage(companyId, "logo");
				deleteImageFile(req, "logo", companyId);
				req.getSession().setAttribute("message", "로고 이미지가 삭제되었습니다.");

			} else if ("saveStamp".equals(action)) {
				String savedPath = "payzon".equals(req.getParameter("stampPreset"))
						? "/images/settings/presets/payzon-stamp.png"
						: saveImageFile(req, req.getPart("stampFile"), "stamp", companyId);
				imageService.uploadImage(companyId, "stamp", savedPath);
				req.getSession().setAttribute("message", "도장 이미지가 등록되었습니다.");

			} else if ("deleteStamp".equals(action)) {
				imageService.deleteImage(companyId, "stamp");
				deleteImageFile(req, "stamp", companyId);
				req.getSession().setAttribute("message", "도장 이미지가 삭제되었습니다.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			boolean managerAction = false;
			if (action != null && action.toLowerCase().contains("department")) {
				managerTarget = "#department-manager-modal";
				managerAction = true;
				req.getSession().setAttribute("managerType", "department");
			} else if (action != null && action.toLowerCase().contains("position")) {
				managerTarget = "#position-manager-modal";
				managerAction = true;
				req.getSession().setAttribute("managerType", "position");
			}
			String errorMessage = e.getMessage();
			String displayMessage = errorMessage == null || errorMessage.trim().isEmpty()
					? "처리 중 오류가 발생했습니다." : errorMessage;
			req.getSession().setAttribute(managerAction ? "managerMessage" : "message", displayMessage);
		}

		// 로직 처리가 완료되면 POST 중복 전송 방지를 위해 GET 방식의 조회 페이지로 리다이렉트합니다.
		res.sendRedirect(req.getContextPath() + "/settings/user-info.do" + managerTarget);
		return null;
	}

	private int parseId(String value) {
		try {
			int id = Integer.parseInt(value);
			if (id > 0) {
				return id;
			}
		} catch (NumberFormatException e) {
			// 아래의 공통 안내로 처리한다.
		}
		throw new IllegalArgumentException("잘못된 설정 항목입니다.");
	}

	private String trim(String value) {
		return value == null ? "" : value.trim();
	}

	// 업로드 파일을 검증한 뒤 가로 150px 이하의 PNG 이미지로 저장합니다.
	private String saveImageFile(HttpServletRequest req, Part filePart, String imageType, int companyId)
			throws IOException {
		if (filePart == null || filePart.getSize() == 0) {
			throw new IllegalArgumentException("등록할 이미지를 선택해주세요.");
		}
		if (filePart.getSize() >= IMAGE_MAX_SIZE) {
			throw new IllegalArgumentException("이미지 파일은 1MB 미만으로 선택해주세요.");
		}
		String contentType = filePart.getContentType();
		if (!"image/png".equalsIgnoreCase(contentType) && !"image/jpeg".equalsIgnoreCase(contentType)) {
			throw new IllegalArgumentException("PNG 또는 JPG 이미지만 등록할 수 있습니다.");
		}

		BufferedImage source;
		try (InputStream input = filePart.getInputStream()) {
			source = ImageIO.read(input);
		}
		if (source == null) {
			throw new IllegalArgumentException("올바른 이미지 파일을 선택해주세요.");
		}

		int targetWidth = Math.min(source.getWidth(), IMAGE_MAX_WIDTH);
		int targetHeight = Math.max(1,
				(int) Math.round(source.getHeight() * (targetWidth / (double) source.getWidth())));
		BufferedImage resized = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = resized.createGraphics();
		try {
			graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			graphics.drawImage(source, 0, 0, targetWidth, targetHeight, null);
		} finally {
			graphics.dispose();
		}

		String webPath = "/upload/company/" + imageType + "_" + companyId + ".png";
		String uploadDirectory = req.getServletContext().getRealPath("/upload/company");
		if (uploadDirectory == null) {
			throw new IOException("이미지 저장 경로를 확인할 수 없습니다.");
		}
		Path directory = Paths.get(uploadDirectory);
		Files.createDirectories(directory);
		Path target = directory.resolve(imageType + "_" + companyId + ".png");
		Path temporary = Files.createTempFile(directory, imageType + "_", ".tmp");
		try {
			if (!ImageIO.write(resized, "png", temporary.toFile())) {
				throw new IOException("이미지를 PNG 형식으로 변환하지 못했습니다.");
			}
			Files.move(temporary, target, StandardCopyOption.REPLACE_EXISTING);
		} finally {
			Files.deleteIfExists(temporary);
		}
		return webPath;
	}

	// 삭제 요청 시 현재 저장 규칙의 파일과 기존 임시 구현의 파일을 함께 정리합니다.
	private void deleteImageFile(HttpServletRequest req, String imageType, int companyId) throws IOException {
		deleteWebFile(req, "/upload/company/" + imageType + "_" + companyId + ".png");
		deleteWebFile(req, "/upload/" + imageType + "_" + companyId + ".png");
	}

	private void deleteWebFile(HttpServletRequest req, String webPath) throws IOException {
		String realPath = req.getServletContext().getRealPath(webPath);
		if (realPath != null) {
			Files.deleteIfExists(Paths.get(realPath));
		}
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

		// 회사정보만 수정할 때 이미 등록된 로고와 도장 경로가 지워지지 않도록 보존합니다.
		Company existingCompany = companyService.getCompanyDetails(company.getCompanyId());
		if (existingCompany != null) {
			company.setLogoImgPath(existingCompany.getLogoImgPath());
			company.setStampImgPath(existingCompany.getStampImgPath());
		}

		companyService.saveAllCompanyInfo(company);
	}
}
